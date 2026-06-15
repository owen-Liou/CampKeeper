package com.github.owenliou.campkeeper.backend.app.telegram.varaible;

public enum promptText {

    START("/start", """
            "歡迎使用 CampKeeper 露營助手！
                輸入關鍵字或使用 /search 即可搜尋合適的營地。
                範例：
                /search 適合親子的山上營地
                /search 寵物友善 南投
                /search 有衛浴 海拔高
                輸入 /help 查看完整說明。)
            """),

    HELP("/help","""
            " 指令說明：
                /search <關鍵字> — 語意搜尋營地
                /start — 顯示歡迎訊息
                /help — 顯示此說明
                也可以直接輸入任意文字進行搜尋，不需要加 /search 指令。
            """),

    SEARCH("/search", """
            "請輸入搜尋關鍵字，例如：/search 適合親子的山上營地
            """),
    ;

    private final String prompt;
    private final String text;

    promptText(String prompt, String text) {
        this.prompt = prompt;
        this.text = text;

    }
    public String getPrompt() {
        return prompt;
    }
    public String getText() {
        return text;
    }

}