package com.google.api.client.http;

import java.io.OutputStream;
import java.io.IOException;

public class EmptyContent implements HttpContent
{
    @Override
    public long getLength() throws IOException {
        /*SL:37*/return 0L;
    }
    
    @Override
    public String getType() {
        /*SL:41*/return null;
    }
    
    @Override
    public void writeTo(final OutputStream a1) throws IOException {
        /*SL:45*/a1.flush();
    }
    
    @Override
    public boolean retrySupported() {
        /*SL:49*/return true;
    }
}
