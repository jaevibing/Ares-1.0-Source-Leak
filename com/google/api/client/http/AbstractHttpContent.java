package com.google.api.client.http;

import com.google.api.client.util.StreamingContent;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Charsets;
import java.nio.charset.Charset;
import java.io.IOException;

public abstract class AbstractHttpContent implements HttpContent
{
    private HttpMediaType mediaType;
    private long computedLength;
    
    protected AbstractHttpContent(final String a1) {
        this((a1 == null) ? null : new HttpMediaType(a1));
    }
    
    protected AbstractHttpContent(final HttpMediaType a1) {
        this.computedLength = -1L;
        this.mediaType = a1;
    }
    
    @Override
    public long getLength() throws IOException {
        /*SL:64*/if (this.computedLength == -1L) {
            /*SL:65*/this.computedLength = this.computeLength();
        }
        /*SL:67*/return this.computedLength;
    }
    
    public final HttpMediaType getMediaType() {
        /*SL:76*/return this.mediaType;
    }
    
    public AbstractHttpContent setMediaType(final HttpMediaType a1) {
        /*SL:90*/this.mediaType = a1;
        /*SL:91*/return this;
    }
    
    protected final Charset getCharset() {
        /*SL:100*/return (this.mediaType == null || this.mediaType.getCharsetParameter() == null) ? Charsets.UTF_8 : this.mediaType.getCharsetParameter();
    }
    
    @Override
    public String getType() {
        /*SL:105*/return (this.mediaType == null) ? null : this.mediaType.build();
    }
    
    protected long computeLength() throws IOException {
        /*SL:117*/return computeLength(this);
    }
    
    @Override
    public boolean retrySupported() {
        /*SL:122*/return true;
    }
    
    public static long computeLength(final HttpContent a1) throws IOException {
        /*SL:136*/if (!a1.retrySupported()) {
            /*SL:137*/return -1L;
        }
        /*SL:139*/return IOUtils.computeLength(a1);
    }
}
