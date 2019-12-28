package com.google.api.client.http;

import java.util.zip.GZIPOutputStream;
import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import com.google.api.client.util.StreamingContent;

public class GZipEncoding implements HttpEncoding
{
    @Override
    public String getName() {
        /*SL:33*/return "gzip";
    }
    
    @Override
    public void encode(final StreamingContent a1, final OutputStream a2) throws IOException {
        final OutputStream v1 = /*EL:38*/new BufferedOutputStream(a2) {
            @Override
            public void close() throws IOException {
                try {
                    /*SL:43*/this.flush();
                }
                catch (IOException ex) {}
            }
        };
        final GZIPOutputStream v2 = /*EL:48*/new GZIPOutputStream(v1);
        /*SL:49*/a1.writeTo(v2);
        /*SL:51*/v2.close();
    }
}
