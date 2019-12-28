package com.google.api.client.testing.http;

import com.google.api.client.util.Preconditions;
import java.io.OutputStream;
import java.io.IOException;
import com.google.api.client.util.Beta;
import com.google.api.client.http.HttpContent;

@Beta
public class MockHttpContent implements HttpContent
{
    private long length;
    private String type;
    private byte[] content;
    
    public MockHttpContent() {
        this.length = -1L;
        this.content = new byte[0];
    }
    
    @Override
    public long getLength() throws IOException {
        /*SL:48*/return this.length;
    }
    
    @Override
    public String getType() {
        /*SL:52*/return this.type;
    }
    
    @Override
    public void writeTo(final OutputStream a1) throws IOException {
        /*SL:56*/a1.write(this.content);
        /*SL:57*/a1.flush();
    }
    
    @Override
    public boolean retrySupported() {
        /*SL:61*/return true;
    }
    
    public final byte[] getContent() {
        /*SL:70*/return this.content;
    }
    
    public MockHttpContent setContent(final byte[] a1) {
        /*SL:83*/this.content = Preconditions.<byte[]>checkNotNull(a1);
        /*SL:84*/return this;
    }
    
    public MockHttpContent setLength(final long a1) {
        /*SL:97*/Preconditions.checkArgument(a1 >= -1L);
        /*SL:98*/this.length = a1;
        /*SL:99*/return this;
    }
    
    public MockHttpContent setType(final String a1) {
        /*SL:108*/this.type = a1;
        /*SL:109*/return this;
    }
}
