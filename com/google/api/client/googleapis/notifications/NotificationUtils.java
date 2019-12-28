package com.google.api.client.googleapis.notifications;

import java.util.UUID;

public final class NotificationUtils
{
    public static String randomUuidString() {
        /*SL:29*/return UUID.randomUUID().toString();
    }
}
