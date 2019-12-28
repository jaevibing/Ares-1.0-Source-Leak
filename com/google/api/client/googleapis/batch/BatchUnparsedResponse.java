package com.google.api.client.googleapis.batch;

import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.HttpTransport;
import java.io.ByteArrayInputStream;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.BackOffPolicy;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpResponse;
import java.io.FilterInputStream;
import com.google.api.client.util.ByteStreams;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.InputStream;
import java.util.List;

final class BatchUnparsedResponse
{
    private final String boundary;
    private final List<BatchRequest.RequestInfo<?, ?>> requestInfos;
    private final InputStream inputStream;
    boolean hasNext;
    List<BatchRequest.RequestInfo<?, ?>> unsuccessfulRequestInfos;
    boolean backOffRequired;
    private int contentId;
    private final boolean retryAllowed;
    
    BatchUnparsedResponse(final InputStream a1, final String a2, final List<BatchRequest.RequestInfo<?, ?>> a3, final boolean a4) throws IOException {
        this.hasNext = true;
        this.unsuccessfulRequestInfos = new ArrayList<BatchRequest.RequestInfo<?, ?>>();
        this.contentId = 0;
        this.boundary = a2;
        this.requestInfos = a3;
        this.retryAllowed = a4;
        this.inputStream = a1;
        this.checkForFinalBoundary(this.readLine());
    }
    
    void parseNextResponse() throws IOException {
        /*SL:99*/++this.contentId;
        String s;
        /*SL:103*/while ((s = this.readLine()) != null && !s.equals("")) {}
        final String line = /*EL:108*/this.readLine();
        final String[] split = /*EL:109*/line.split(" ");
        final int int1 = /*EL:110*/Integer.parseInt(split[1]);
        final List<String> a3 = /*EL:115*/new ArrayList<String>();
        final List<String> a2 = /*EL:116*/new ArrayList<String>();
        long long1 = /*EL:117*/-1L;
        /*SL:118*/while ((s = this.readLine()) != null && !s.equals("")) {
            final String[] v1 = /*EL:119*/s.split(": ", 2);
            final String v2 = /*EL:120*/v1[0];
            final String v3 = /*EL:121*/v1[1];
            /*SL:122*/a3.add(v2);
            /*SL:123*/a2.add(v3);
            /*SL:124*/if ("Content-Length".equalsIgnoreCase(v2.trim())) {
                /*SL:125*/long1 = Long.parseLong(v3);
            }
        }
        InputStream v5;
        /*SL:130*/if (long1 == -1L) {
            final ByteArrayOutputStream v4 = /*EL:132*/new ByteArrayOutputStream();
            /*SL:133*/while ((s = this.readRawLine()) != null && !s.startsWith(this.boundary)) {
                /*SL:135*/v4.write(s.getBytes("ISO-8859-1"));
            }
            /*SL:139*/v5 = trimCrlf(v4.toByteArray());
            /*SL:142*/s = trimCrlf(s);
        }
        else {
            /*SL:144*/v5 = new FilterInputStream(ByteStreams.limit(this.inputStream, long1)) {
                public void close() {
                }
            };
        }
        final HttpResponse v6 = /*EL:152*/this.getFakeResponse(int1, v5, a3, a2);
        /*SL:155*/this.<?, ?>parseAndCallback(this.requestInfos.get(this.contentId - 1), int1, v6);
        while (true) {
            /*SL:158*/if (v5.skip(long1) <= 0L) {
                if (v5.read() != -1) {
                    continue;
                }
                break;
            }
        }
        /*SL:161*/if (long1 != -1L) {
            /*SL:162*/s = this.readLine();
        }
        /*SL:167*/while (s != null && s.length() == 0) {
            /*SL:168*/s = this.readLine();
        }
        /*SL:171*/this.checkForFinalBoundary(s);
    }
    
    private <T, E> void parseAndCallback(final BatchRequest.RequestInfo<T, E> v-7, final int v-6, final HttpResponse v-5) throws IOException {
        final BatchCallback<T, E> callback = /*EL:181*/v-7.callback;
        final HttpHeaders headers = /*EL:183*/v-5.getHeaders();
        final HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler = /*EL:184*/v-7.request.getUnsuccessfulResponseHandler();
        final BackOffPolicy backOffPolicy = /*EL:186*/v-7.request.getBackOffPolicy();
        /*SL:189*/this.backOffRequired = false;
        /*SL:191*/if (HttpStatusCodes.isSuccess(v-6)) {
            /*SL:192*/if (callback == null) {
                /*SL:194*/return;
            }
            final T a1 = /*EL:196*/this.<T, T, E>getParsedDataClass(v-7.dataClass, v-5, v-7);
            /*SL:197*/callback.onSuccess(a1, headers);
        }
        else {
            final HttpContent a2 = /*EL:199*/v-7.request.getContent();
            final boolean v1 = /*EL:200*/this.retryAllowed && (a2 == null || a2.retrySupported());
            boolean v2 = /*EL:201*/false;
            boolean v3 = /*EL:202*/false;
            /*SL:203*/if (unsuccessfulResponseHandler != null) {
                /*SL:204*/v2 = unsuccessfulResponseHandler.handleResponse(v-7.request, v-5, v1);
            }
            /*SL:207*/if (!v2) {
                /*SL:208*/if (v-7.request.handleRedirect(v-5.getStatusCode(), v-5.getHeaders())) {
                    /*SL:209*/v3 = true;
                }
                else/*SL:210*/ if (v1 && backOffPolicy != null && backOffPolicy.isBackOffRequired(v-5.getStatusCode())) {
                    /*SL:212*/this.backOffRequired = true;
                }
            }
            /*SL:215*/if (v1 && (v2 || this.backOffRequired || v3)) {
                /*SL:216*/this.unsuccessfulRequestInfos.add(v-7);
            }
            else {
                /*SL:218*/if (callback == null) {
                    /*SL:220*/return;
                }
                final E a3 = /*EL:222*/this.<E, T, E>getParsedDataClass(v-7.errorClass, v-5, v-7);
                /*SL:223*/callback.onFailure(a3, headers);
            }
        }
    }
    
    private <A, T, E> A getParsedDataClass(final Class<A> a1, final HttpResponse a2, final BatchRequest.RequestInfo<T, E> a3) throws IOException {
        /*SL:231*/if (a1 == Void.class) {
            /*SL:232*/return null;
        }
        /*SL:234*/return a3.request.getParser().<A>parseAndClose(a2.getContent(), a2.getContentCharset(), a1);
    }
    
    private HttpResponse getFakeResponse(final int a1, final InputStream a2, final List<String> a3, final List<String> a4) throws IOException {
        final HttpRequest v1 = /*EL:242*/new FakeResponseHttpTransport(a1, a2, a3, a4).createRequestFactory().buildPostRequest(new GenericUrl("http://google.com/"), null);
        /*SL:245*/v1.setLoggingEnabled(false);
        /*SL:246*/v1.setThrowExceptionOnExecuteError(false);
        /*SL:247*/return v1.execute();
    }
    
    private String readRawLine() throws IOException {
        int v0 = /*EL:256*/this.inputStream.read();
        /*SL:257*/if (v0 == -1) {
            /*SL:258*/return null;
        }
        final StringBuilder v = /*EL:260*/new StringBuilder();
        /*SL:261*/while (v0 != -1) {
            /*SL:262*/v.append((char)v0);
            /*SL:263*/if (v0 == 10) {
                /*SL:264*/break;
            }
            v0 = this.inputStream.read();
        }
        /*SL:267*/return v.toString();
    }
    
    private String readLine() throws IOException {
        /*SL:280*/return trimCrlf(this.readRawLine());
    }
    
    private static String trimCrlf(final String a1) {
        /*SL:284*/if (a1.endsWith("\r\n")) {
            /*SL:285*/return a1.substring(0, a1.length() - 2);
        }
        /*SL:286*/if (a1.endsWith("\n")) {
            /*SL:287*/return a1.substring(0, a1.length() - 1);
        }
        /*SL:289*/return a1;
    }
    
    private static InputStream trimCrlf(final byte[] a1) {
        int v1 = /*EL:294*/a1.length;
        /*SL:295*/if (v1 > 0 && a1[v1 - 1] == 10) {
            /*SL:296*/--v1;
        }
        /*SL:298*/if (v1 > 0 && a1[v1 - 1] == 13) {
            /*SL:299*/--v1;
        }
        /*SL:301*/return new ByteArrayInputStream(a1, 0, v1);
    }
    
    private void checkForFinalBoundary(final String a1) throws IOException {
        /*SL:309*/if (a1.equals(String.valueOf(this.boundary).concat("--"))) {
            /*SL:310*/this.hasNext = false;
            /*SL:311*/this.inputStream.close();
        }
    }
    
    private static class FakeResponseHttpTransport extends HttpTransport
    {
        private int statusCode;
        private InputStream partContent;
        private List<String> headerNames;
        private List<String> headerValues;
        
        FakeResponseHttpTransport(final int a1, final InputStream a2, final List<String> a3, final List<String> a4) {
            this.statusCode = a1;
            this.partContent = a2;
            this.headerNames = a3;
            this.headerValues = a4;
        }
        
        protected LowLevelHttpRequest buildRequest(final String a1, final String a2) {
            /*SL:333*/return new FakeLowLevelHttpRequest(this.partContent, this.statusCode, this.headerNames, this.headerValues);
        }
    }
    
    private static class FakeLowLevelHttpRequest extends LowLevelHttpRequest
    {
        private InputStream partContent;
        private int statusCode;
        private List<String> headerNames;
        private List<String> headerValues;
        
        FakeLowLevelHttpRequest(final InputStream a1, final int a2, final List<String> a3, final List<String> a4) {
            this.partContent = a1;
            this.statusCode = a2;
            this.headerNames = a3;
            this.headerValues = a4;
        }
        
        public void addHeader(final String a1, final String a2) {
        }
        
        public LowLevelHttpResponse execute() {
            final FakeLowLevelHttpResponse v1 = /*EL:358*/new FakeLowLevelHttpResponse(this.partContent, this.statusCode, this.headerNames, this.headerValues);
            /*SL:360*/return v1;
        }
    }
    
    private static class FakeLowLevelHttpResponse extends LowLevelHttpResponse
    {
        private InputStream partContent;
        private int statusCode;
        private List<String> headerNames;
        private List<String> headerValues;
        
        FakeLowLevelHttpResponse(final InputStream a1, final int a2, final List<String> a3, final List<String> a4) {
            this.headerNames = new ArrayList<String>();
            this.headerValues = new ArrayList<String>();
            this.partContent = a1;
            this.statusCode = a2;
            this.headerNames = a3;
            this.headerValues = a4;
        }
        
        public InputStream getContent() {
            /*SL:381*/return this.partContent;
        }
        
        public int getStatusCode() {
            /*SL:386*/return this.statusCode;
        }
        
        public String getContentEncoding() {
            /*SL:391*/return null;
        }
        
        public long getContentLength() {
            /*SL:396*/return 0L;
        }
        
        public String getContentType() {
            /*SL:401*/return null;
        }
        
        public String getStatusLine() {
            /*SL:406*/return null;
        }
        
        public String getReasonPhrase() {
            /*SL:411*/return null;
        }
        
        public int getHeaderCount() {
            /*SL:416*/return this.headerNames.size();
        }
        
        public String getHeaderName(final int a1) {
            /*SL:421*/return this.headerNames.get(a1);
        }
        
        public String getHeaderValue(final int a1) {
            /*SL:426*/return this.headerValues.get(a1);
        }
    }
}
