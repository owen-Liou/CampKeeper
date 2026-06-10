package com.github.owenliou.campkeeper.backend.app.telegram;

import com.github.owenliou.campkeeper.backend.app.ai.service.EmbeddingService;
import com.github.owenliou.campkeeper.model.entity.Campsite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Slf4j
@Component
public class CampKeeperBot implements LongPollingUpdateConsumer {

    private static final int SEARCH_TOP_K = 5;

    private final TelegramClient telegramClient;
    private final EmbeddingService embeddingService;

    public CampKeeperBot(@Value("${TELEGRAM_BOT_TOKEN}") String token, EmbeddingService embeddingService) {
        this.telegramClient = new OkHttpTelegramClient(token);
        this.embeddingService = embeddingService;
        log.info("CampKeeperBot 初始化成功");
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(this::handleUpdate);
    }

    private void handleUpdate(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        String text = update.getMessage().getText().trim();
        long chatId = update.getMessage().getChatId();

        if (text.startsWith("/start")) {
            handleStart(chatId);
        } else if (text.startsWith("/help")) {
            handleHelp(chatId);
        } else if (text.startsWith("/search")) {
            String query = text.replaceFirst("/search", "").trim();
            handleSearch(chatId, query);
        } else if (!text.startsWith("/")) {
            handleSearch(chatId, text);
        }
    }

    private void handleStart(long chatId) {
        sendText(chatId, """
                歡迎使用 CampKeeper 露營助手！

                輸入關鍵字或使用 /search 即可搜尋合適的營地。

                範例：
                /search 適合親子的山上營地
                /search 寵物友善 南投
                /search 有衛浴 海拔高

                輸入 /help 查看完整說明。
                """);
    }

    private void handleHelp(long chatId) {
        sendText(chatId, """
                指令說明：

                /search <關鍵字> — 語意搜尋營地
                /start — 顯示歡迎訊息
                /help — 顯示此說明

                也可以直接輸入任意文字進行搜尋，不需要加 /search 指令。
                """);
    }

    private void handleSearch(long chatId, String query) {
        if (query.isBlank()) {
            sendText(chatId, "請輸入搜尋關鍵字，例如：/search 適合親子的山上營地");
            return;
        }

        log.info("Telegram search: chatId={}, query={}", chatId, query);

        try {
            List<Campsite> results = embeddingService.semanticSearchCamp(query, SEARCH_TOP_K);

            if (results.isEmpty()) {
                sendText(chatId, "找不到相關營地，請試試其他關鍵字。");
                return;
            }

            StringBuilder sb = new StringBuilder("找到 " + results.size() + " 筆相關營地：\n\n");
            for (int i = 0; i < results.size(); i++) {
                Campsite c = results.get(i);
                sb.append(i + 1).append(". ").append(c.getName()).append("\n");
                sb.append("   ").append(c.getCity()).append(c.getDistrict());
                if (c.getAltitude() != null) {
                    sb.append(" | 海拔 ").append(c.getAltitude()).append("m");
                }
                sb.append("\n");
                sb.append("   ").append(buildFeatureTag(c)).append("\n\n");
            }

            sendText(chatId, sb.toString().trim());

        } catch (Exception e) {
            log.error("Telegram search error, query={}", query, e);
            sendText(chatId, "搜尋時發生錯誤，請稍後再試。");
        }
    }

    private String buildFeatureTag(Campsite c) {
        StringBuilder tags = new StringBuilder();
        if (Boolean.TRUE.equals(c.getHasPower()))    tags.append("⚡有電 ");
        if (Boolean.TRUE.equals(c.getHasShower()))   tags.append("🚿有衛浴 ");
        if (Boolean.TRUE.equals(c.getPetFriendly())) tags.append("🐾寵物友善 ");
        return tags.isEmpty() ? "—" : tags.toString().trim();
    }

    private void sendText(long chatId, String text) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram message to chatId={}", chatId, e);
        }
    }
}
