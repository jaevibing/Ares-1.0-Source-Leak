package com.google.api.client.testing.http.javanet;

import java.util.ArrayList;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.OutputStream;
import com.google.api.client.util.Beta;
import java.net.HttpURLConnection;

@Beta
public class MockHttpURLConnection extends HttpURLConnection
{
    private boolean doOutputCalled;
    private OutputStream outputStream;
    @Deprecated
    public static final byte[] INPUT_BUF;
    @Deprecated
    public static final byte[] ERROR_BUF;
    private InputStream inputStream;
    private InputStream errorStream;
    private Map<String, List<String>> headers;
    
    public MockHttpURLConnection(final URL a1) {
        super(a1);
        this.outputStream = new ByteArrayOutputStream(0);
        this.inputStream = null;
        this.errorStream = null;
        this.headers = new LinkedHashMap<String, List<String>>();
    }
    
    @Override
    public void disconnect() {
    }
    
    @Override
    public boolean usingProxy() {
        /*SL:94*/return false;
    }
    
    @Override
    public void connect() throws IOException {
    }
    
    @Override
    public int getResponseCode() throws IOException {
        /*SL:103*/return this.responseCode;
    }
    
    @Override
    public void setDoOutput(final boolean a1) {
        /*SL:108*/this.doOutputCalled = true;
    }
    
    @Override
    public OutputStream getOutputStream() throws IOException {
        /*SL:113*/if (this.outputStream != null) {
            /*SL:114*/return this.outputStream;
        }
        /*SL:116*/return super.getOutputStream();
    }
    
    public final boolean doOutputCalled() {
        /*SL:121*/return this.doOutputCalled;
    }
    
    public MockHttpURLConnection setOutputStream(final OutputStream a1) {
        /*SL:133*/this.outputStream = a1;
        /*SL:134*/return this;
    }
    
    public MockHttpURLConnection setResponseCode(final int a1) {
        /*SL:139*/Preconditions.checkArgument(a1 >= -1);
        /*SL:140*/this.responseCode = a1;
        /*SL:141*/return this;
    }
    
    public MockHttpURLConnection addHeader(final String v1, final String v2) {
        /*SL:150*/Preconditions.<String>checkNotNull(v1);
        /*SL:151*/Preconditions.<String>checkNotNull(v2);
        /*SL:152*/if (this.headers.containsKey(v1)) {
            /*SL:153*/this.headers.get(v1).add(v2);
        }
        else {
            final List<String> a1 = /*EL:155*/new ArrayList<String>();
            /*SL:156*/a1.add(v2);
            /*SL:157*/this.headers.put(v1, a1);
        }
        /*SL:159*/return this;
    }
    
    public MockHttpURLConnection setInputStream(final InputStream a1) {
        /*SL:170*/Preconditions.<InputStream>checkNotNull(a1);
        /*SL:171*/if (this.inputStream == null) {
            /*SL:172*/this.inputStream = a1;
        }
        /*SL:174*/return this;
    }
    
    public MockHttpURLConnection setErrorStream(final InputStream a1) {
        /*SL:185*/Preconditions.<InputStream>checkNotNull(a1);
        /*SL:186*/if (this.errorStream == null) {
            /*SL:187*/this.errorStream = a1;
        }
        /*SL:189*/return this;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        /*SL:194*/if (this.responseCode < 400) {
            /*SL:195*/return this.inputStream;
        }
        /*SL:197*/throw new IOException();
    }
    
    @Override
    public InputStream getErrorStream() {
        /*SL:202*/return this.errorStream;
    }
    
    @Override
    public Map<String, List<String>> getHeaderFields() {
        /*SL:207*/return this.headers;
    }
    
    @Override
    public String getHeaderField(final String a1) {
        final List<String> v1 = /*EL:212*/this.headers.get(a1);
        /*SL:213*/return (v1 == null) ? null : v1.get(0);
    }
    
    static {
        INPUT_BUF = new byte[1];
        ERROR_BUF = new byte[5];
    }
}
