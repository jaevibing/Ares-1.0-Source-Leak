package com.google.api.client.googleapis.services;

import com.google.api.client.googleapis.batch.BatchCallback;
import com.google.api.client.googleapis.batch.BatchRequest;
import java.io.OutputStream;
import java.io.InputStream;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseInterceptor;
import com.google.api.client.http.HttpEncoding;
import com.google.api.client.http.GZipEncoding;
import java.util.Map;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.googleapis.MethodOverride;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.util.Preconditions;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpContent;
import com.google.api.client.util.GenericData;

public abstract class AbstractGoogleClientRequest<T> extends GenericData
{
    public static final String USER_AGENT_SUFFIX = "Google-API-Java-Client";
    private final AbstractGoogleClient abstractGoogleClient;
    private final String requestMethod;
    private final String uriTemplate;
    private final HttpContent httpContent;
    private HttpHeaders requestHeaders;
    private HttpHeaders lastResponseHeaders;
    private int lastStatusCode;
    private String lastStatusMessage;
    private boolean disableGZipContent;
    private Class<T> responseClass;
    private MediaHttpUploader uploader;
    private MediaHttpDownloader downloader;
    
    protected AbstractGoogleClientRequest(final AbstractGoogleClient a1, final String a2, final String a3, final HttpContent a4, final Class<T> a5) {
        this.requestHeaders = new HttpHeaders();
        this.lastStatusCode = -1;
        this.responseClass = Preconditions.<Class<T>>checkNotNull(a5);
        this.abstractGoogleClient = Preconditions.<AbstractGoogleClient>checkNotNull(a1);
        this.requestMethod = Preconditions.<String>checkNotNull(a2);
        this.uriTemplate = Preconditions.<String>checkNotNull(a3);
        this.httpContent = a4;
        final String v1 = a1.getApplicationName();
        if (v1 != null) {
            final HttpHeaders requestHeaders = this.requestHeaders;
            final String value = String.valueOf(String.valueOf(v1));
            final String value2 = String.valueOf(String.valueOf("Google-API-Java-Client"));
            requestHeaders.setUserAgent(new StringBuilder(1 + value.length() + value2.length()).append(value).append(" ").append(value2).toString());
        }
        else {
            this.requestHeaders.setUserAgent("Google-API-Java-Client");
        }
    }
    
    public final boolean getDisableGZipContent() {
        /*SL:126*/return this.disableGZipContent;
    }
    
    public AbstractGoogleClientRequest<T> setDisableGZipContent(final boolean a1) {
        /*SL:142*/this.disableGZipContent = a1;
        /*SL:143*/return this;
    }
    
    public final String getRequestMethod() {
        /*SL:148*/return this.requestMethod;
    }
    
    public final String getUriTemplate() {
        /*SL:153*/return this.uriTemplate;
    }
    
    public final HttpContent getHttpContent() {
        /*SL:158*/return this.httpContent;
    }
    
    public AbstractGoogleClient getAbstractGoogleClient() {
        /*SL:170*/return this.abstractGoogleClient;
    }
    
    public final HttpHeaders getRequestHeaders() {
        /*SL:175*/return this.requestHeaders;
    }
    
    public AbstractGoogleClientRequest<T> setRequestHeaders(final HttpHeaders a1) {
        /*SL:192*/this.requestHeaders = a1;
        /*SL:193*/return this;
    }
    
    public final HttpHeaders getLastResponseHeaders() {
        /*SL:200*/return this.lastResponseHeaders;
    }
    
    public final int getLastStatusCode() {
        /*SL:207*/return this.lastStatusCode;
    }
    
    public final String getLastStatusMessage() {
        /*SL:215*/return this.lastStatusMessage;
    }
    
    public final Class<T> getResponseClass() {
        /*SL:220*/return this.responseClass;
    }
    
    public final MediaHttpUploader getMediaHttpUploader() {
        /*SL:225*/return this.uploader;
    }
    
    protected final void initializeMediaUpload(final AbstractInputStreamContent a1) {
        final HttpRequestFactory v1 = /*EL:234*/this.abstractGoogleClient.getRequestFactory();
        /*SL:235*/(this.uploader = new MediaHttpUploader(a1, v1.getTransport(), v1.getInitializer())).setInitiationRequestMethod(/*EL:237*/this.requestMethod);
        /*SL:238*/if (this.httpContent != null) {
            /*SL:239*/this.uploader.setMetadata(this.httpContent);
        }
    }
    
    public final MediaHttpDownloader getMediaHttpDownloader() {
        /*SL:245*/return this.downloader;
    }
    
    protected final void initializeMediaDownload() {
        final HttpRequestFactory v1 = /*EL:250*/this.abstractGoogleClient.getRequestFactory();
        /*SL:251*/this.downloader = new MediaHttpDownloader(v1.getTransport(), v1.getInitializer());
    }
    
    public GenericUrl buildHttpRequestUrl() {
        /*SL:265*/return new GenericUrl(UriTemplate.expand(this.abstractGoogleClient.getBaseUrl(), this.uriTemplate, this, true));
    }
    
    public HttpRequest buildHttpRequest() throws IOException {
        /*SL:277*/return this.buildHttpRequest(false);
    }
    
    protected HttpRequest buildHttpRequestUsingHead() throws IOException {
        /*SL:292*/return this.buildHttpRequest(true);
    }
    
    private HttpRequest buildHttpRequest(final boolean a1) throws IOException {
        /*SL:297*/Preconditions.checkArgument(this.uploader == null);
        /*SL:298*/Preconditions.checkArgument(!a1 || this.requestMethod.equals("GET"));
        final String v1 = /*EL:299*/a1 ? "HEAD" : this.requestMethod;
        final HttpRequest v2 = /*EL:300*/this.getAbstractGoogleClient().getRequestFactory().buildRequest(v1, this.buildHttpRequestUrl(), this.httpContent);
        /*SL:302*/new MethodOverride().intercept(v2);
        /*SL:303*/v2.setParser(this.getAbstractGoogleClient().getObjectParser());
        /*SL:305*/if (this.httpContent == null && (this.requestMethod.equals("POST") || this.requestMethod.equals("PUT") || this.requestMethod.equals("PATCH"))) {
            /*SL:307*/v2.setContent(new EmptyContent());
        }
        /*SL:309*/v2.getHeaders().putAll(this.requestHeaders);
        /*SL:310*/if (!this.disableGZipContent) {
            /*SL:311*/v2.setEncoding(new GZipEncoding());
        }
        final HttpResponseInterceptor v3 = /*EL:313*/v2.getResponseInterceptor();
        /*SL:314*/v2.setResponseInterceptor(new HttpResponseInterceptor() {
            public void interceptResponse(final HttpResponse a1) throws IOException {
                /*SL:317*/if (/*EL:323*/v3 != null) {
                    v3.interceptResponse(a1);
                }
                if (!a1.isSuccessStatusCode() && v2.getThrowExceptionOnExecuteError()) {
                    throw AbstractGoogleClientRequest.this.newExceptionOnError(a1);
                }
            }
        });
        /*SL:325*/return v2;
    }
    
    public HttpResponse executeUnparsed() throws IOException {
        /*SL:352*/return this.executeUnparsed(false);
    }
    
    protected HttpResponse executeMedia() throws IOException {
        /*SL:379*/this.set("alt", "media");
        /*SL:380*/return this.executeUnparsed();
    }
    
    protected HttpResponse executeUsingHead() throws IOException {
        /*SL:405*/Preconditions.checkArgument(this.uploader == null);
        final HttpResponse v1 = /*EL:406*/this.executeUnparsed(true);
        /*SL:407*/v1.ignore();
        /*SL:408*/return v1;
    }
    
    private HttpResponse executeUnparsed(final boolean v-1) throws IOException {
        final HttpResponse v4;
        /*SL:417*/if (this.uploader == null) {
            final HttpResponse a1 = /*EL:419*/this.buildHttpRequest(v-1).execute();
        }
        else {
            final GenericUrl v1 = /*EL:422*/this.buildHttpRequestUrl();
            final HttpRequest v2 = /*EL:423*/this.getAbstractGoogleClient().getRequestFactory().buildRequest(this.requestMethod, v1, this.httpContent);
            final boolean v3 = /*EL:425*/v2.getThrowExceptionOnExecuteError();
            /*SL:427*/v4 = this.uploader.setInitiationHeaders(this.requestHeaders).setDisableGZipContent(this.disableGZipContent).upload(v1);
            /*SL:429*/v4.getRequest().setParser(this.getAbstractGoogleClient().getObjectParser());
            /*SL:431*/if (v3 && !v4.isSuccessStatusCode()) {
                /*SL:432*/throw this.newExceptionOnError(v4);
            }
        }
        /*SL:436*/this.lastResponseHeaders = v4.getHeaders();
        /*SL:437*/this.lastStatusCode = v4.getStatusCode();
        /*SL:438*/this.lastStatusMessage = v4.getStatusMessage();
        /*SL:439*/return v4;
    }
    
    protected IOException newExceptionOnError(final HttpResponse a1) {
        /*SL:456*/return new HttpResponseException(a1);
    }
    
    public T execute() throws IOException {
        /*SL:469*/return this.executeUnparsed().<T>parseAs(this.responseClass);
    }
    
    public InputStream executeAsInputStream() throws IOException {
        /*SL:496*/return this.executeUnparsed().getContent();
    }
    
    protected InputStream executeMediaAsInputStream() throws IOException {
        /*SL:523*/return this.executeMedia().getContent();
    }
    
    public void executeAndDownloadTo(final OutputStream a1) throws IOException {
        /*SL:541*/this.executeUnparsed().download(a1);
    }
    
    protected void executeMediaAndDownloadTo(final OutputStream a1) throws IOException {
        /*SL:559*/if (this.downloader == null) {
            /*SL:560*/this.executeMedia().download(a1);
        }
        else {
            /*SL:562*/this.downloader.download(this.buildHttpRequestUrl(), this.requestHeaders, a1);
        }
    }
    
    public final <E> void queue(final BatchRequest a1, final Class<E> a2, final BatchCallback<T, E> a3) throws IOException {
        /*SL:581*/Preconditions.checkArgument(this.uploader == null, (Object)"Batching media requests is not supported");
        /*SL:582*/a1.<T, E>queue(this.buildHttpRequest(), this.getResponseClass(), a2, a3);
    }
    
    public AbstractGoogleClientRequest<T> set(final String a1, final Object a2) {
        /*SL:592*/return (AbstractGoogleClientRequest)super.set(a1, a2);
    }
    
    protected final void checkRequiredParameter(final Object a1, final String a2) {
        /*SL:606*/Preconditions.checkArgument(this.abstractGoogleClient.getSuppressRequiredParameterChecks() || a1 != null, "Required parameter %s must be specified", a2);
    }
}
