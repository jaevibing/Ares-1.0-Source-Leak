package com.google.api.client.http.apache;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;
import org.apache.http.entity.AbstractHttpEntity;

final class ContentEntity extends AbstractHttpEntity
{
    private final long contentLength;
    private final StreamingContent streamingContent;
    
    ContentEntity(final long a1, final StreamingContent a2) {
        this.contentLength = a1;
        this.streamingContent = Preconditions.<StreamingContent>checkNotNull(a2);
    }
    
    public InputStream getContent() {
        /*SL:45*/throw new UnsupportedOperationException();
    }
    
    public long getContentLength() {
        /*SL:49*/return this.contentLength;
    }
    
    public boolean isRepeatable() {
        /*SL:53*/return false;
    }
    
    public boolean isStreaming() {
        /*SL:57*/return true;
    }
    
    public void writeTo(final OutputStream a1) throws IOException {
        /*SL:61*/if (this.contentLength != 0L) {
            /*SL:62*/this.streamingContent.writeTo(a1);
        }
    }
}
