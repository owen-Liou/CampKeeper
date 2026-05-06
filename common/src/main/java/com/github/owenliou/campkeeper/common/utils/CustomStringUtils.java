package com.github.owenliou.campkeeper.common.utils;

import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CustomStringUtils {

    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
