package com.smartcampus.common;

import java.util.UUID;

public class RequestId {
    public static String generateRequestId() {
        return String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-", "");
    }
}
