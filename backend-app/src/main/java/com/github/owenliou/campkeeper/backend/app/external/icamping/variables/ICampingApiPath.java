package com.github.owenliou.campkeeper.backend.app.external.icamping.variables;

public enum ICampingApiPath {

    CLIENT_HOST("https://m.icamping.app"),

    API_BASE_URL_TIER1("https://api-guest-prod-tier-1-wwclgij22a-an.a.run.app"),
    API_BASE_URL_TIER2("https://api-guest-prod-tier-2-wwclgij22a-an.a.run.app"),

    STORE_LIST("/api/guest/v1/store/list"),
    EXTERNAL_LINK_LIST("/api/guest/v1/externalLink/store_name/list"),

    STORE_LIST_TOP("/api/guest/v1/store/list_top"),
    STUFF_LIST("/api/guest/v1/stuff/store_name/list");

    private final String path;

    ICampingApiPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}