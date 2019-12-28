package com.google.api.client.googleapis.batch;

import java.io.InputStream;
import com.google.api.client.http.HttpResponse;
import java.util.Iterator;
import com.google.api.client.http.BackOffPolicy;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpContent;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.Preconditions;
import java.util.ArrayList;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Sleeper;
import java.util.List;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.GenericUrl;

public final class BatchRequest
{
    private GenericUrl batchUrl;
    private final HttpRequestFactory requestFactory;
    List<RequestInfo<?, ?>> requestInfos;
    private Sleeper sleeper;
    
    public BatchRequest(final HttpTransport a1, final HttpRequestInitializer a2) {
        this.batchUrl = new GenericUrl("https://www.googleapis.com/batch");
        this.requestInfos = new ArrayList<RequestInfo<?, ?>>();
        this.sleeper = Sleeper.DEFAULT;
        this.requestFactory = ((a2 == null) ? a1.createRequestFactory() : a1.createRequestFactory(a2));
    }
    
    public BatchRequest setBatchUrl(final GenericUrl a1) {
        /*SL:144*/this.batchUrl = a1;
        /*SL:145*/return this;
    }
    
    public GenericUrl getBatchUrl() {
        /*SL:150*/return this.batchUrl;
    }
    
    public Sleeper getSleeper() {
        /*SL:159*/return this.sleeper;
    }
    
    public BatchRequest setSleeper(final Sleeper a1) {
        /*SL:168*/this.sleeper = Preconditions.<Sleeper>checkNotNull(a1);
        /*SL:169*/return this;
    }
    
    public <T, E> BatchRequest queue(final HttpRequest a1, final Class<T> a2, final Class<E> a3, final BatchCallback<T, E> a4) throws IOException {
        /*SL:189*/Preconditions.<HttpRequest>checkNotNull(a1);
        /*SL:191*/Preconditions.<BatchCallback<T, E>>checkNotNull(a4);
        /*SL:192*/Preconditions.<Class<T>>checkNotNull(a2);
        /*SL:193*/Preconditions.<Class<E>>checkNotNull(a3);
        /*SL:195*/this.requestInfos.add(new RequestInfo<Object, Object>((BatchCallback<Object, Object>)a4, (Class<Object>)a2, (Class<Object>)a3, a1));
        /*SL:196*/return this;
    }
    
    public int size() {
        /*SL:203*/return this.requestInfos.size();
    }
    
    public void execute() throws IOException {
        /*SL:217*/Preconditions.checkState(!this.requestInfos.isEmpty());
        final HttpRequest buildPostRequest = /*EL:218*/this.requestFactory.buildPostRequest(this.batchUrl, null);
        final HttpExecuteInterceptor interceptor = /*EL:220*/buildPostRequest.getInterceptor();
        /*SL:221*/buildPostRequest.setInterceptor(new BatchInterceptor(interceptor));
        int numberOfRetries = /*EL:222*/buildPostRequest.getNumberOfRetries();
        final BackOffPolicy backOffPolicy = /*EL:223*/buildPostRequest.getBackOffPolicy();
        /*SL:225*/if (backOffPolicy != null) {
            /*SL:227*/backOffPolicy.reset();
        }
        boolean a4;
        /*SL:277*/do {
            a4 = (numberOfRetries > 0);
            final MultipartContent content = new MultipartContent();
            content.getMediaType().setSubType("mixed");
            int n = 1;
            for (final RequestInfo<?, ?> v : this.requestInfos) {
                content.addPart(new MultipartContent.Part(new HttpHeaders().setAcceptEncoding(null).set("Content-ID", n++), new HttpRequestContent(v.request)));
            }
            buildPostRequest.setContent(content);
            final HttpResponse v2 = buildPostRequest.execute();
            BatchUnparsedResponse v5;
            try {
                final String s = "--";
                final String value = String.valueOf(v2.getMediaType().getParameter("boundary"));
                final String v3 = (value.length() != 0) ? s.concat(value) : new String(s);
                final InputStream v4 = v2.getContent();
                v5 = new BatchUnparsedResponse(v4, v3, this.requestInfos, a4);
                while (v5.hasNext) {
                    v5.parseNextResponse();
                }
            }
            finally {
                v2.disconnect();
            }
            final List<RequestInfo<?, ?>> v6 = v5.unsuccessfulRequestInfos;
            if (v6.isEmpty()) {
                break;
            }
            this.requestInfos = v6;
            if (v5.backOffRequired && backOffPolicy != null) {
                final long v7 = backOffPolicy.getNextBackOffMillis();
                if (v7 != -1L) {
                    try {
                        this.sleeper.sleep(v7);
                    }
                    catch (InterruptedException ex) {}
                }
            }
            --numberOfRetries;
        } while (a4);
        /*SL:278*/this.requestInfos.clear();
    }
    
    static class RequestInfo<T, E>
    {
        final BatchCallback<T, E> callback;
        final Class<T> dataClass;
        final Class<E> errorClass;
        final HttpRequest request;
        
        RequestInfo(final BatchCallback<T, E> a1, final Class<T> a2, final Class<E> a3, final HttpRequest a4) {
            this.callback = a1;
            this.dataClass = a2;
            this.errorClass = a3;
            this.request = a4;
        }
    }
    
    class BatchInterceptor implements HttpExecuteInterceptor
    {
        private HttpExecuteInterceptor originalInterceptor;
        
        BatchInterceptor(final HttpExecuteInterceptor a1) {
            this.originalInterceptor = a1;
        }
        
        public void intercept(final HttpRequest v-1) throws IOException {
            /*SL:294*/if (this.originalInterceptor != null) {
                /*SL:295*/this.originalInterceptor.intercept(v-1);
            }
            /*SL:297*/for (final RequestInfo<?, ?> v : BatchRequest.this.requestInfos) {
                final HttpExecuteInterceptor a1 = /*EL:298*/v.request.getInterceptor();
                /*SL:299*/if (a1 != null) {
                    /*SL:300*/a1.intercept(v.request);
                }
            }
        }
    }
}
