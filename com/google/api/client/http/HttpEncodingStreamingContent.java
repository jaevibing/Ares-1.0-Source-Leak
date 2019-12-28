package com.google.api.client.http;

import java.io.IOException;
import java.io.OutputStream;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;

public final class HttpEncodingStreamingContent implements StreamingContent
{
    private final StreamingContent content;
    private final HttpEncoding encoding;
    
    public HttpEncodingStreamingContent(final StreamingContent a1, final HttpEncoding a2) {
        this.content = Preconditions.<StreamingContent>checkNotNull(a1);
        this.encoding = Preconditions.<HttpEncoding>checkNotNull(a2);
    }
    
    @Override
    public void writeTo(final OutputStream a1) throws IOException {
        /*SL:51*/this.encoding.encode(this.content, a1);
    }
    
    public StreamingContent getContent() {
        /*SL:56*/return this.content;
    }
    
    public HttpEncoding getEncoding() {
        /*SL:61*/return this.encoding;
    }
}
