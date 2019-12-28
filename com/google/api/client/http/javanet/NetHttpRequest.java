package com.google.api.client.http.javanet;

import java.io.OutputStream;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import com.google.api.client.http.LowLevelHttpResponse;
import java.net.HttpURLConnection;
import com.google.api.client.http.LowLevelHttpRequest;

final class NetHttpRequest extends LowLevelHttpRequest
{
    private final HttpURLConnection connection;
    
    NetHttpRequest(final HttpURLConnection a1) {
        (this.connection = a1).setInstanceFollowRedirects(false);
    }
    
    @Override
    public void addHeader(final String a1, final String a2) {
        /*SL:42*/this.connection.addRequestProperty(a1, a2);
    }
    
    @Override
    public void setTimeout(final int a1, final int a2) {
        /*SL:47*/this.connection.setReadTimeout(a2);
        /*SL:48*/this.connection.setConnectTimeout(a1);
    }
    
    @Override
    public LowLevelHttpResponse execute() throws IOException {
        final HttpURLConnection connection = /*EL:53*/this.connection;
        /*SL:55*/if (this.getStreamingContent() != null) {
            final String contentType = /*EL:56*/this.getContentType();
            /*SL:57*/if (contentType != null) {
                /*SL:58*/this.addHeader("Content-Type", contentType);
            }
            final String contentEncoding = /*EL:60*/this.getContentEncoding();
            /*SL:61*/if (contentEncoding != null) {
                /*SL:62*/this.addHeader("Content-Encoding", contentEncoding);
            }
            final long contentLength = /*EL:64*/this.getContentLength();
            /*SL:65*/if (contentLength >= 0L) {
                /*SL:66*/connection.setRequestProperty("Content-Length", Long.toString(contentLength));
            }
            final String requestMethod = /*EL:68*/connection.getRequestMethod();
            /*SL:69*/if ("POST".equals(requestMethod) || "PUT".equals(requestMethod)) {
                /*SL:70*/connection.setDoOutput(true);
                /*SL:72*/if (contentLength >= 0L && contentLength <= 2147483647L) {
                    /*SL:73*/connection.setFixedLengthStreamingMode((int)contentLength);
                }
                else {
                    /*SL:75*/connection.setChunkedStreamingMode(0);
                }
                final OutputStream outputStream = /*EL:77*/connection.getOutputStream();
                boolean v0 = /*EL:78*/true;
                try {
                    /*SL:80*/this.getStreamingContent().writeTo(outputStream);
                    /*SL:81*/v0 = false;
                }
                finally {
                    try {
                        /*SL:84*/outputStream.close();
                    }
                    catch (IOException v) {
                        /*SL:89*/if (!v0) {
                            /*SL:90*/throw v;
                        }
                    }
                }
            }
            else {
                /*SL:97*/Preconditions.checkArgument(contentLength == 0L, "%s with non-zero content length is not supported", requestMethod);
            }
        }
        boolean b = /*EL:102*/false;
        try {
            /*SL:104*/connection.connect();
            final NetHttpResponse netHttpResponse = /*EL:105*/new NetHttpResponse(connection);
            /*SL:106*/b = true;
            /*SL:107*/return netHttpResponse;
        }
        finally {
            /*SL:109*/if (!b) {
                /*SL:110*/connection.disconnect();
            }
        }
    }
}
