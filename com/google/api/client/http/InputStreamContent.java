package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.io.InputStream;

public final class InputStreamContent extends AbstractInputStreamContent
{
    private long length;
    private boolean retrySupported;
    private final InputStream inputStream;
    
    public InputStreamContent(final String a1, final InputStream a2) {
        super(a1);
        this.length = -1L;
        this.inputStream = Preconditions.<InputStream>checkNotNull(a2);
    }
    
    @Override
    public long getLength() {
        /*SL:72*/return this.length;
    }
    
    @Override
    public boolean retrySupported() {
        /*SL:76*/return this.retrySupported;
    }
    
    public InputStreamContent setRetrySupported(final boolean a1) {
        /*SL:90*/this.retrySupported = a1;
        /*SL:91*/return this;
    }
    
    @Override
    public InputStream getInputStream() {
        /*SL:96*/return this.inputStream;
    }
    
    @Override
    public InputStreamContent setType(final String a1) {
        /*SL:101*/return (InputStreamContent)super.setType(a1);
    }
    
    @Override
    public InputStreamContent setCloseInputStream(final boolean a1) {
        /*SL:106*/return (InputStreamContent)super.setCloseInputStream(a1);
    }
    
    public InputStreamContent setLength(final long a1) {
        /*SL:119*/this.length = a1;
        /*SL:120*/return this;
    }
}
