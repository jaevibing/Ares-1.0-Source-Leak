package com.google.api.client.http;

import com.google.api.client.util.Charsets;
import java.nio.charset.Charset;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import com.google.api.client.util.IOUtils;
import java.io.OutputStream;
import java.io.EOFException;
import com.google.api.client.util.LoggingInputStream;
import java.util.zip.GZIPInputStream;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.logging.Logger;
import com.google.api.client.util.StringUtils;
import java.util.logging.Level;
import java.io.InputStream;

public final class HttpResponse
{
    private InputStream content;
    private final String contentEncoding;
    private final String contentType;
    private final HttpMediaType mediaType;
    LowLevelHttpResponse response;
    private final int statusCode;
    private final String statusMessage;
    private final HttpRequest request;
    private int contentLoggingLimit;
    private boolean loggingEnabled;
    private boolean contentRead;
    
    HttpResponse(final HttpRequest v1, final LowLevelHttpResponse v2) throws IOException {
        this.request = v1;
        this.contentLoggingLimit = v1.getContentLoggingLimit();
        this.loggingEnabled = v1.isLoggingEnabled();
        this.response = v2;
        this.contentEncoding = v2.getContentEncoding();
        final int v3 = v2.getStatusCode();
        this.statusCode = ((v3 < 0) ? 0 : v3);
        final String v4 = v2.getReasonPhrase();
        this.statusMessage = v4;
        final Logger v5 = HttpTransport.LOGGER;
        final boolean v6 = this.loggingEnabled && v5.isLoggable(Level.CONFIG);
        StringBuilder v7 = null;
        if (v6) {
            v7 = new StringBuilder();
            v7.append("-------------- RESPONSE --------------").append(StringUtils.LINE_SEPARATOR);
            final String a1 = v2.getStatusLine();
            if (a1 != null) {
                v7.append(a1);
            }
            else {
                v7.append(this.statusCode);
                if (v4 != null) {
                    v7.append(' ').append(v4);
                }
            }
            v7.append(StringUtils.LINE_SEPARATOR);
        }
        v1.getResponseHeaders().fromHttpResponse(v2, v6 ? v7 : null);
        String v8 = v2.getContentType();
        if (v8 == null) {
            v8 = v1.getResponseHeaders().getContentType();
        }
        this.mediaType = (((this.contentType = v8) == null) ? null : new HttpMediaType(v8));
        if (v6) {
            v5.config(v7.toString());
        }
    }
    
    public int getContentLoggingLimit() {
        /*SL:188*/return this.contentLoggingLimit;
    }
    
    public HttpResponse setContentLoggingLimit(final int a1) {
        /*SL:214*/Preconditions.checkArgument(a1 >= 0, (Object)"The content logging limit must be non-negative.");
        /*SL:216*/this.contentLoggingLimit = a1;
        /*SL:217*/return this;
    }
    
    public boolean isLoggingEnabled() {
        /*SL:230*/return this.loggingEnabled;
    }
    
    public HttpResponse setLoggingEnabled(final boolean a1) {
        /*SL:243*/this.loggingEnabled = a1;
        /*SL:244*/return this;
    }
    
    public String getContentEncoding() {
        /*SL:253*/return this.contentEncoding;
    }
    
    public String getContentType() {
        /*SL:262*/return this.contentType;
    }
    
    public HttpMediaType getMediaType() {
        /*SL:272*/return this.mediaType;
    }
    
    public HttpHeaders getHeaders() {
        /*SL:281*/return this.request.getResponseHeaders();
    }
    
    public boolean isSuccessStatusCode() {
        /*SL:291*/return HttpStatusCodes.isSuccess(this.statusCode);
    }
    
    public int getStatusCode() {
        /*SL:300*/return this.statusCode;
    }
    
    public String getStatusMessage() {
        /*SL:309*/return this.statusMessage;
    }
    
    public HttpTransport getTransport() {
        /*SL:318*/return this.request.getTransport();
    }
    
    public HttpRequest getRequest() {
        /*SL:327*/return this.request;
    }
    
    public InputStream getContent() throws IOException {
        /*SL:353*/if (!this.contentRead) {
            InputStream content = /*EL:354*/this.response.getContent();
            /*SL:355*/if (content != null) {
                boolean v0 = /*EL:358*/false;
                try {
                    final String v = /*EL:361*/this.contentEncoding;
                    /*SL:362*/if (v != null && v.contains("gzip")) {
                        /*SL:363*/content = new GZIPInputStream(content);
                    }
                    final Logger v2 = HttpTransport.LOGGER;
                    /*SL:367*/if (this.loggingEnabled && v2.isLoggable(Level.CONFIG)) {
                        /*SL:368*/content = new LoggingInputStream(content, v2, Level.CONFIG, this.contentLoggingLimit);
                    }
                    /*SL:371*/this.content = content;
                    /*SL:372*/v0 = true;
                }
                catch (EOFException ex) {}
                finally {
                    /*SL:377*/if (!v0) {
                        /*SL:378*/content.close();
                    }
                }
            }
            /*SL:382*/this.contentRead = true;
        }
        /*SL:384*/return this.content;
    }
    
    public void download(final OutputStream a1) throws IOException {
        final InputStream v1 = /*EL:420*/this.getContent();
        /*SL:421*/IOUtils.copy(v1, a1);
    }
    
    public void ignore() throws IOException {
        final InputStream v1 = /*EL:428*/this.getContent();
        /*SL:429*/if (v1 != null) {
            /*SL:430*/v1.close();
        }
    }
    
    public void disconnect() throws IOException {
        /*SL:441*/this.ignore();
        /*SL:442*/this.response.disconnect();
    }
    
    public <T> T parseAs(final Class<T> a1) throws IOException {
        /*SL:456*/if (!this.hasMessageBody()) {
            /*SL:457*/return null;
        }
        /*SL:459*/return this.request.getParser().<T>parseAndClose(this.getContent(), this.getContentCharset(), a1);
    }
    
    private boolean hasMessageBody() throws IOException {
        final int v1 = /*EL:467*/this.getStatusCode();
        /*SL:468*/if (this.getRequest().getRequestMethod().equals("HEAD") || v1 / 100 == 1 || v1 == 204 || v1 == 304) {
            /*SL:471*/this.ignore();
            /*SL:472*/return false;
        }
        /*SL:474*/return true;
    }
    
    public Object parseAs(final Type a1) throws IOException {
        /*SL:485*/if (!this.hasMessageBody()) {
            /*SL:486*/return null;
        }
        /*SL:488*/return this.request.getParser().parseAndClose(this.getContent(), this.getContentCharset(), a1);
    }
    
    public String parseAsString() throws IOException {
        final InputStream v1 = /*EL:510*/this.getContent();
        /*SL:511*/if (v1 == null) {
            /*SL:512*/return "";
        }
        final ByteArrayOutputStream v2 = /*EL:514*/new ByteArrayOutputStream();
        /*SL:515*/IOUtils.copy(v1, v2);
        /*SL:516*/return v2.toString(this.getContentCharset().name());
    }
    
    public Charset getContentCharset() {
        /*SL:526*/return (this.mediaType == null || this.mediaType.getCharsetParameter() == null) ? Charsets.ISO_8859_1 : this.mediaType.getCharsetParameter();
    }
}
