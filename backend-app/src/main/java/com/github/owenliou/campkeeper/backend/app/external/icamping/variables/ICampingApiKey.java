package com.github.owenliou.campkeeper.backend.app.external.icamping.variables;

public enum ICampingApiKey {

    GUEST("AIzaSyAKQXJQUSQUChNI3-RklARZWaIzI5hh3ds");

    private final String value;

    ICampingApiKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}