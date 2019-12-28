package com.google.api.client.http;

import java.io.IOException;
import com.google.api.client.util.StreamingContent;

public abstract class LowLevelHttpRequest
{
    private long contentLength;
    private String contentEncoding;
    private String contentType;
    private StreamingContent streamingContent;
    
    public LowLevelHttpRequest() {
        this.contentLength = -1L;
    }
    
    public abstract void addHeader(final String p0, final String p1) throws IOException;
    
    public final void setContentLength(final long a1) throws IOException {
        /*SL:75*/this.contentLength = a1;
    }
    
    public final long getContentLength() {
        /*SL:84*/return this.contentLength;
    }
    
    public final void setContentEncoding(final String a1) throws IOException {
        /*SL:94*/this.contentEncoding = a1;
    }
    
    public final String getContentEncoding() {
        /*SL:103*/return this.contentEncoding;
    }
    
    public final void setContentType(final String a1) throws IOException {
        /*SL:113*/this.contentType = a1;
    }
    
    public final String getContentType() {
        /*SL:122*/return this.contentType;
    }
    
    public final void setStreamingContent(final StreamingContent a1) throws IOException {
        /*SL:133*/this.streamingContent = a1;
    }
    
    public final StreamingContent getStreamingContent() {
        /*SL:142*/return this.streamingContent;
    }
    
    public void setTimeout(final int a1, final int a2) throws IOException {
    }
    
    public abstract LowLevelHttpResponse execute() throws IOException;
}
