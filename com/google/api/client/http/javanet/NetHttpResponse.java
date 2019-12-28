package com.google.api.client.http.javanet;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import com.google.api.client.http.LowLevelHttpResponse;

final class NetHttpResponse extends LowLevelHttpResponse
{
    private final HttpURLConnection connection;
    private final int responseCode;
    private final String responseMessage;
    private final ArrayList<String> headerNames;
    private final ArrayList<String> headerValues;
    
    NetHttpResponse(final HttpURLConnection v-5) throws IOException {
        this.headerNames = new ArrayList<String>();
        this.headerValues = new ArrayList<String>();
        this.connection = v-5;
        final int responseCode = v-5.getResponseCode();
        this.responseCode = ((responseCode == -1) ? 0 : responseCode);
        this.responseMessage = v-5.getResponseMessage();
        final List<String> headerNames = this.headerNames;
        final List<String> headerValues = this.headerValues;
        for (final Map.Entry<String, List<String>> v0 : v-5.getHeaderFields().entrySet()) {
            final String v = v0.getKey();
            if (v != null) {
                for (final String a1 : v0.getValue()) {
                    if (a1 != null) {
                        headerNames.add(v);
                        headerValues.add(a1);
                    }
                }
            }
        }
    }
    
    @Override
    public int getStatusCode() {
        /*SL:57*/return this.responseCode;
    }
    
    @Override
    public InputStream getContent() throws IOException {
        InputStream v0 = /*EL:88*/null;
        try {
            /*SL:90*/v0 = this.connection.getInputStream();
        }
        catch (IOException v) {
            /*SL:92*/v0 = this.connection.getErrorStream();
        }
        /*SL:94*/return (v0 == null) ? null : new SizeValidatingInputStream(v0);
    }
    
    @Override
    public String getContentEncoding() {
        /*SL:99*/return this.connection.getContentEncoding();
    }
    
    @Override
    public long getContentLength() {
        final String v1 = /*EL:104*/this.connection.getHeaderField("Content-Length");
        /*SL:105*/return (v1 == null) ? -1L : Long.parseLong(v1);
    }
    
    @Override
    public String getContentType() {
        /*SL:110*/return this.connection.getHeaderField("Content-Type");
    }
    
    @Override
    public String getReasonPhrase() {
        /*SL:115*/return this.responseMessage;
    }
    
    @Override
    public String getStatusLine() {
        final String v1 = /*EL:120*/this.connection.getHeaderField(0);
        /*SL:121*/return (v1 != null && v1.startsWith("HTTP/1.")) ? v1 : null;
    }
    
    @Override
    public int getHeaderCount() {
        /*SL:126*/return this.headerNames.size();
    }
    
    @Override
    public String getHeaderName(final int a1) {
        /*SL:131*/return this.headerNames.get(a1);
    }
    
    @Override
    public String getHeaderValue(final int a1) {
        /*SL:136*/return this.headerValues.get(a1);
    }
    
    @Override
    public void disconnect() {
        /*SL:146*/this.connection.disconnect();
    }
    
    private final class SizeValidatingInputStream extends FilterInputStream
    {
        private long bytesRead;
        
        public SizeValidatingInputStream(final InputStream a1) {
            /*SL:147*/super(a1);
            this.bytesRead = 0L;
        }
        
        @Override
        public int read(final byte[] a1, final int a2, final int a3) throws IOException {
            final int v1 = /*EL:169*/this.in.read(a1, a2, a3);
            /*SL:170*/if (v1 == -1) {
                /*SL:171*/this.throwIfFalseEOF();
            }
            else {
                /*SL:173*/this.bytesRead += v1;
            }
            /*SL:175*/return v1;
        }
        
        @Override
        public int read() throws IOException {
            final int v1 = /*EL:180*/this.in.read();
            /*SL:181*/if (v1 == -1) {
                /*SL:182*/this.throwIfFalseEOF();
            }
            else {
                /*SL:184*/++this.bytesRead;
            }
            /*SL:186*/return v1;
        }
        
        private void throwIfFalseEOF() throws IOException {
            final long v1 = /*EL:191*/NetHttpResponse.this.getContentLength();
            /*SL:192*/if (v1 == -1L) {
                /*SL:194*/return;
            }
            /*SL:201*/if (this.bytesRead != 0L && this.bytesRead < v1) {
                /*SL:202*/throw new IOException("Connection closed prematurely: bytesRead = " + this.bytesRead + ", Content-Length = " + v1);
            }
        }
    }
}
