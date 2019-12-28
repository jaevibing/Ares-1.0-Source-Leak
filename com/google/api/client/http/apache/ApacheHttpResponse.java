package com.google.api.client.http.apache;

import java.io.IOException;
import org.apache.http.HttpEntity;
import java.io.InputStream;
import org.apache.http.StatusLine;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import com.google.api.client.http.LowLevelHttpResponse;

final class ApacheHttpResponse extends LowLevelHttpResponse
{
    private final HttpRequestBase request;
    private final HttpResponse response;
    private final Header[] allHeaders;
    
    ApacheHttpResponse(final HttpRequestBase a1, final HttpResponse a2) {
        this.request = a1;
        this.response = a2;
        this.allHeaders = a2.getAllHeaders();
    }
    
    @Override
    public int getStatusCode() {
        final StatusLine v1 = /*EL:40*/this.response.getStatusLine();
        /*SL:41*/return (v1 == null) ? 0 : v1.getStatusCode();
    }
    
    @Override
    public InputStream getContent() throws IOException {
        final HttpEntity v1 = /*EL:46*/this.response.getEntity();
        /*SL:47*/return (v1 == null) ? null : v1.getContent();
    }
    
    @Override
    public String getContentEncoding() {
        final HttpEntity v0 = /*EL:52*/this.response.getEntity();
        /*SL:53*/if (v0 != null) {
            final Header v = /*EL:54*/v0.getContentEncoding();
            /*SL:55*/if (v != null) {
                /*SL:56*/return v.getValue();
            }
        }
        /*SL:59*/return null;
    }
    
    @Override
    public long getContentLength() {
        final HttpEntity v1 = /*EL:64*/this.response.getEntity();
        /*SL:65*/return (v1 == null) ? -1L : v1.getContentLength();
    }
    
    @Override
    public String getContentType() {
        final HttpEntity v0 = /*EL:70*/this.response.getEntity();
        /*SL:71*/if (v0 != null) {
            final Header v = /*EL:72*/v0.getContentType();
            /*SL:73*/if (v != null) {
                /*SL:74*/return v.getValue();
            }
        }
        /*SL:77*/return null;
    }
    
    @Override
    public String getReasonPhrase() {
        final StatusLine v1 = /*EL:82*/this.response.getStatusLine();
        /*SL:83*/return (v1 == null) ? null : v1.getReasonPhrase();
    }
    
    @Override
    public String getStatusLine() {
        final StatusLine v1 = /*EL:88*/this.response.getStatusLine();
        /*SL:89*/return (v1 == null) ? null : v1.toString();
    }
    
    public String getHeaderValue(final String a1) {
        /*SL:93*/return this.response.getLastHeader(a1).getValue();
    }
    
    @Override
    public int getHeaderCount() {
        /*SL:98*/return this.allHeaders.length;
    }
    
    @Override
    public String getHeaderName(final int a1) {
        /*SL:103*/return this.allHeaders[a1].getName();
    }
    
    @Override
    public String getHeaderValue(final int a1) {
        /*SL:108*/return this.allHeaders[a1].getValue();
    }
    
    @Override
    public void disconnect() {
        /*SL:118*/this.request.abort();
    }
}
