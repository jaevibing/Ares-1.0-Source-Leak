package com.google.api.client.http.apache;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.HttpEntity;
import com.google.api.client.util.Preconditions;
import org.apache.http.HttpEntityEnclosingRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import java.io.IOException;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.HttpClient;
import com.google.api.client.http.LowLevelHttpRequest;

final class ApacheHttpRequest extends LowLevelHttpRequest
{
    private final HttpClient httpClient;
    private final HttpRequestBase request;
    
    ApacheHttpRequest(final HttpClient a1, final HttpRequestBase a2) {
        this.httpClient = a1;
        this.request = a2;
    }
    
    @Override
    public void addHeader(final String a1, final String a2) {
        /*SL:43*/this.request.addHeader(a1, a2);
    }
    
    @Override
    public void setTimeout(final int a1, final int a2) throws IOException {
        final HttpParams v1 = /*EL:48*/this.request.getParams();
        /*SL:49*/ConnManagerParams.setTimeout(v1, (long)a1);
        /*SL:50*/HttpConnectionParams.setConnectionTimeout(v1, a1);
        /*SL:51*/HttpConnectionParams.setSoTimeout(v1, a2);
    }
    
    @Override
    public LowLevelHttpResponse execute() throws IOException {
        /*SL:56*/if (this.getStreamingContent() != null) {
            /*SL:57*/Preconditions.checkArgument(this.request instanceof HttpEntityEnclosingRequest, "Apache HTTP client does not support %s requests with content.", this.request.getRequestLine().getMethod());
            final ContentEntity v1 = /*EL:60*/new ContentEntity(this.getContentLength(), this.getStreamingContent());
            /*SL:61*/v1.setContentEncoding(this.getContentEncoding());
            /*SL:62*/v1.setContentType(this.getContentType());
            /*SL:63*/((HttpEntityEnclosingRequest)this.request).setEntity((HttpEntity)v1);
        }
        /*SL:65*/return new ApacheHttpResponse(this.request, this.httpClient.execute((HttpUriRequest)this.request));
    }
}
