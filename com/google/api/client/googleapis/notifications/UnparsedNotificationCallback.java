package com.google.api.client.googleapis.notifications;

import java.io.IOException;
import com.google.api.client.util.Beta;
import java.io.Serializable;

@Beta
public interface UnparsedNotificationCallback extends Serializable
{
    void onNotification(StoredChannel p0, UnparsedNotification p1) throws IOException;
}
