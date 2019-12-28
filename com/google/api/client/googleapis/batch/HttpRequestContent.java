package com.google.api.client.googleapis.batch;

import java.io.IOException;
import com.google.api.client.http.HttpContent;
import java.io.Writer;
import java.util.logging.Logger;
import com.google.api.client.http.HttpHeaders;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.AbstractHttpContent;

class HttpRequestContent extends AbstractHttpContent
{
    static final String NEWLINE = "\r\n";
    private final HttpRequest request;
    private static final String HTTP_VERSION = "HTTP/1.1";
    
    HttpRequestContent(final HttpRequest a1) {
        super("application/http");
        this.request = a1;
    }
    
    public void writeTo(final OutputStream v2) throws IOException {
        final Writer v3 = /*EL:46*/new OutputStreamWriter(v2, this.getCharset());
        /*SL:48*/v3.write(this.request.getRequestMethod());
        /*SL:49*/v3.write(" ");
        /*SL:50*/v3.write(this.request.getUrl().build());
        /*SL:51*/v3.write(" ");
        /*SL:52*/v3.write("HTTP/1.1");
        /*SL:53*/v3.write("\r\n");
        final HttpHeaders v4 = /*EL:56*/new HttpHeaders();
        /*SL:57*/v4.fromHttpHeaders(this.request.getHeaders());
        /*SL:58*/v4.setAcceptEncoding(null).setUserAgent(null).setContentEncoding(null).setContentType(null).setContentLength(null);
        final HttpContent v5 = /*EL:61*/this.request.getContent();
        /*SL:62*/if (v5 != null) {
            /*SL:63*/v4.setContentType(v5.getType());
            final long a1 = /*EL:65*/v5.getLength();
            /*SL:66*/if (a1 != -1L) {
                /*SL:67*/v4.setContentLength(a1);
            }
        }
        /*SL:70*/HttpHeaders.serializeHeadersForMultipartRequests(v4, null, null, v3);
        /*SL:72*/v3.write("\r\n");
        /*SL:73*/v3.flush();
        /*SL:75*/if (v5 != null) {
            /*SL:76*/v5.writeTo(v2);
        }
    }
}
