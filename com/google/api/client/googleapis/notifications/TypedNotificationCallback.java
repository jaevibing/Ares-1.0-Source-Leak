package com.google.api.client.googleapis.notifications;

import java.nio.charset.Charset;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.ObjectParser;
import java.io.IOException;
import com.google.api.client.util.Beta;

@Beta
public abstract class TypedNotificationCallback<T> implements UnparsedNotificationCallback
{
    private static final long serialVersionUID = 1L;
    
    protected abstract void onNotification(final StoredChannel p0, final TypedNotification<T> p1) throws IOException;
    
    protected abstract ObjectParser getObjectParser() throws IOException;
    
    protected abstract Class<T> getDataClass() throws IOException;
    
    public final void onNotification(final StoredChannel v2, final UnparsedNotification v3) throws IOException {
        final TypedNotification<T> v4 = /*EL:101*/new TypedNotification<T>(v3);
        final String v5 = /*EL:103*/v3.getContentType();
        /*SL:104*/if (v5 != null) {
            final Charset a1 = /*EL:105*/new HttpMediaType(v5).getCharsetParameter();
            final Class<T> a2 = /*EL:106*/Preconditions.<Class<T>>checkNotNull(this.getDataClass());
            /*SL:107*/v4.setContent(this.getObjectParser().<T>parseAndClose(v3.getContentStream(), a1, a2));
        }
        /*SL:110*/this.onNotification(v2, v4);
    }
}
