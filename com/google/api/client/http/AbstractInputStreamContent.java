package com.google.api.client.http;

import com.google.api.client.util.IOUtils;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractInputStreamContent implements HttpContent
{
    private String type;
    private boolean closeInputStream;
    
    public AbstractInputStreamContent(final String a1) {
        this.closeInputStream = true;
        this.setType(a1);
    }
    
    public abstract InputStream getInputStream() throws IOException;
    
    @Override
    public void writeTo(final OutputStream a1) throws IOException {
        /*SL:72*/IOUtils.copy(this.getInputStream(), a1, this.closeInputStream);
        /*SL:73*/a1.flush();
    }
    
    @Override
    public String getType() {
        /*SL:77*/return this.type;
    }
    
    public final boolean getCloseInputStream() {
        /*SL:87*/return this.closeInputStream;
    }
    
    public AbstractInputStreamContent setType(final String a1) {
        /*SL:96*/this.type = a1;
        /*SL:97*/return this;
    }
    
    public AbstractInputStreamContent setCloseInputStream(final boolean a1) {
        /*SL:107*/this.closeInputStream = a1;
        /*SL:108*/return this;
    }
}
