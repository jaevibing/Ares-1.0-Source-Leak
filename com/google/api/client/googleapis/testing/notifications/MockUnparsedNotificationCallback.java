package com.google.api.client.googleapis.testing.notifications;

import java.io.IOException;
import com.google.api.client.googleapis.notifications.UnparsedNotification;
import com.google.api.client.googleapis.notifications.StoredChannel;
import com.google.api.client.util.Beta;
import com.google.api.client.googleapis.notifications.UnparsedNotificationCallback;

@Beta
public class MockUnparsedNotificationCallback implements UnparsedNotificationCallback
{
    private static final long serialVersionUID = 0L;
    private boolean wasCalled;
    
    public boolean wasCalled() {
        /*SL:43*/return this.wasCalled;
    }
    
    public void onNotification(final StoredChannel a1, final UnparsedNotification a2) throws IOException {
        /*SL:52*/this.wasCalled = true;
    }
}
