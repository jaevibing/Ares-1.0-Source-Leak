package com.google.api.client.testing.http;

import java.io.IOException;
import com.google.api.client.testing.util.TestableByteArrayInputStream;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import com.google.api.client.util.Beta;
import com.google.api.client.http.LowLevelHttpResponse;

@Beta
public class MockLowLevelHttpResponse extends LowLevelHttpResponse
{
    private InputStream content;
    private String contentType;
    private int statusCode;
    private String reasonPhrase;
    private List<String> headerNames;
    private List<String> headerValues;
    private String contentEncoding;
    private long contentLength;
    private boolean isDisconnected;
    
    public MockLowLevelHttpResponse() {
        this.statusCode = 200;
        this.headerNames = new ArrayList<String>();
        this.headerValues = new ArrayList<String>();
        this.contentLength = -1L;
    }
    
    public MockLowLevelHttpResponse addHeader(final String a1, final String a2) {
        /*SL:76*/this.headerNames.add(Preconditions.<String>checkNotNull(a1));
        /*SL:77*/this.headerValues.add(Preconditions.<String>checkNotNull(a2));
        /*SL:78*/return this;
    }
    
    public MockLowLevelHttpResponse setContent(final String a1) {
        /*SL:92*/return (a1 == null) ? this.setZeroContent() : /*EL:93*/this.setContent(/*EL:94*/StringUtils.getBytesUtf8(a1));
    }
    
    public MockLowLevelHttpResponse setContent(final byte[] a1) {
        /*SL:111*/if (a1 == null) {
            /*SL:112*/return this.setZeroContent();
        }
        /*SL:114*/this.content = new TestableByteArrayInputStream(a1);
        /*SL:115*/this.setContentLength(a1.length);
        /*SL:116*/return this;
    }
    
    public MockLowLevelHttpResponse setZeroContent() {
        /*SL:126*/this.content = null;
        /*SL:127*/this.setContentLength(0L);
        /*SL:128*/return this;
    }
    
    @Override
    public InputStream getContent() throws IOException {
        /*SL:133*/return this.content;
    }
    
    @Override
    public String getContentEncoding() {
        /*SL:138*/return this.contentEncoding;
    }
    
    @Override
    public long getContentLength() {
        /*SL:143*/return this.contentLength;
    }
    
    @Override
    public final String getContentType() {
        /*SL:148*/return this.contentType;
    }
    
    @Override
    public int getHeaderCount() {
        /*SL:153*/return this.headerNames.size();
    }
    
    @Override
    public String getHeaderName(final int a1) {
        /*SL:158*/return this.headerNames.get(a1);
    }
    
    @Override
    public String getHeaderValue(final int a1) {
        /*SL:163*/return this.headerValues.get(a1);
    }
    
    @Override
    public String getReasonPhrase() {
        /*SL:168*/return this.reasonPhrase;
    }
    
    @Override
    public int getStatusCode() {
        /*SL:173*/return this.statusCode;
    }
    
    @Override
    public String getStatusLine() {
        final StringBuilder v1 = /*EL:178*/new StringBuilder();
        /*SL:179*/v1.append(this.statusCode);
        /*SL:180*/if (this.reasonPhrase != null) {
            /*SL:181*/v1.append(this.reasonPhrase);
        }
        /*SL:183*/return v1.toString();
    }
    
    public final List<String> getHeaderNames() {
        /*SL:192*/return this.headerNames;
    }
    
    public MockLowLevelHttpResponse setHeaderNames(final List<String> a1) {
        /*SL:205*/this.headerNames = Preconditions.<List<String>>checkNotNull(a1);
        /*SL:206*/return this;
    }
    
    public final List<String> getHeaderValues() {
        /*SL:219*/return this.headerValues;
    }
    
    public MockLowLevelHttpResponse setHeaderValues(final List<String> a1) {
        /*SL:228*/this.headerValues = Preconditions.<List<String>>checkNotNull(a1);
        /*SL:229*/return this;
    }
    
    public MockLowLevelHttpResponse setContent(final InputStream a1) {
        /*SL:238*/this.content = a1;
        /*SL:239*/return this;
    }
    
    public MockLowLevelHttpResponse setContentType(final String a1) {
        /*SL:248*/this.contentType = a1;
        /*SL:249*/return this;
    }
    
    public MockLowLevelHttpResponse setContentEncoding(final String a1) {
        /*SL:258*/this.contentEncoding = a1;
        /*SL:259*/return this;
    }
    
    public MockLowLevelHttpResponse setContentLength(final long a1) {
        /*SL:272*/this.contentLength = a1;
        /*SL:273*/Preconditions.checkArgument(a1 >= -1L);
        /*SL:274*/return this;
    }
    
    public MockLowLevelHttpResponse setStatusCode(final int a1) {
        /*SL:287*/this.statusCode = a1;
        /*SL:288*/return this;
    }
    
    public MockLowLevelHttpResponse setReasonPhrase(final String a1) {
        /*SL:297*/this.reasonPhrase = a1;
        /*SL:298*/return this;
    }
    
    @Override
    public void disconnect() throws IOException {
        /*SL:303*/this.isDisconnected = true;
        /*SL:304*/super.disconnect();
    }
    
    public boolean isDisconnected() {
        /*SL:313*/return this.isDisconnected;
    }
}
