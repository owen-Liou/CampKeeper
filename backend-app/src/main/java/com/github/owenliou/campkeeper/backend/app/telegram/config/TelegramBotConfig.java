package com.github.owenliou.campkeeper.backend.app.telegram.config;

import com.github.owenliou.campkeeper.backend.app.telegram.CampKeeperBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsApplication(CampKeeperBot bot, @Value("${TELEGRAM_BOT_TOKEN}") String token) throws Exception {
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        app.registerBot(token, bot);
        return app;
    }
}
