package com.google.api.client.http;

import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.Executor;
import java.io.InputStream;
import com.google.api.client.util.StreamingContent;
import java.util.logging.Logger;
import java.io.IOException;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.LoggingStreamingContent;
import com.google.api.client.util.StringUtils;
import java.util.logging.Level;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sleeper;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.Beta;

public final class HttpRequest
{
    public static final String VERSION = "1.25.0";
    public static final String USER_AGENT_SUFFIX = "Google-HTTP-Java-Client/1.25.0 (gzip)";
    public static final int DEFAULT_NUMBER_OF_RETRIES = 10;
    private HttpExecuteInterceptor executeInterceptor;
    private HttpHeaders headers;
    private HttpHeaders responseHeaders;
    private int numRetries;
    private int contentLoggingLimit;
    private boolean loggingEnabled;
    private boolean curlLoggingEnabled;
    private HttpContent content;
    private final HttpTransport transport;
    private String requestMethod;
    private GenericUrl url;
    private int connectTimeout;
    private int readTimeout;
    private HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler;
    @Beta
    private HttpIOExceptionHandler ioExceptionHandler;
    private HttpResponseInterceptor responseInterceptor;
    private ObjectParser objectParser;
    private HttpEncoding encoding;
    @Deprecated
    @Beta
    private BackOffPolicy backOffPolicy;
    private boolean followRedirects;
    private boolean throwExceptionOnExecuteError;
    @Deprecated
    @Beta
    private boolean retryOnExecuteIOException;
    private boolean suppressUserAgentSuffix;
    private Sleeper sleeper;
    
    HttpRequest(final HttpTransport a1, final String a2) {
        this.headers = new HttpHeaders();
        this.responseHeaders = new HttpHeaders();
        this.numRetries = 10;
        this.contentLoggingLimit = 16384;
        this.loggingEnabled = true;
        this.curlLoggingEnabled = true;
        this.connectTimeout = 20000;
        this.readTimeout = 20000;
        this.followRedirects = true;
        this.throwExceptionOnExecuteError = true;
        this.retryOnExecuteIOException = false;
        this.sleeper = Sleeper.DEFAULT;
        this.transport = a1;
        this.setRequestMethod(a2);
    }
    
    public HttpTransport getTransport() {
        /*SL:228*/return this.transport;
    }
    
    public String getRequestMethod() {
        /*SL:237*/return this.requestMethod;
    }
    
    public HttpRequest setRequestMethod(final String a1) {
        /*SL:246*/Preconditions.checkArgument(a1 == null || HttpMediaType.matchesToken(a1));
        /*SL:247*/this.requestMethod = a1;
        /*SL:248*/return this;
    }
    
    public GenericUrl getUrl() {
        /*SL:257*/return this.url;
    }
    
    public HttpRequest setUrl(final GenericUrl a1) {
        /*SL:266*/this.url = Preconditions.<GenericUrl>checkNotNull(a1);
        /*SL:267*/return this;
    }
    
    public HttpContent getContent() {
        /*SL:276*/return this.content;
    }
    
    public HttpRequest setContent(final HttpContent a1) {
        /*SL:285*/this.content = a1;
        /*SL:286*/return this;
    }
    
    public HttpEncoding getEncoding() {
        /*SL:295*/return this.encoding;
    }
    
    public HttpRequest setEncoding(final HttpEncoding a1) {
        /*SL:304*/this.encoding = a1;
        /*SL:305*/return this;
    }
    
    @Deprecated
    @Beta
    public BackOffPolicy getBackOffPolicy() {
        /*SL:320*/return this.backOffPolicy;
    }
    
    @Deprecated
    @Beta
    public HttpRequest setBackOffPolicy(final BackOffPolicy a1) {
        /*SL:335*/this.backOffPolicy = a1;
        /*SL:336*/return this;
    }
    
    public int getContentLoggingLimit() {
        /*SL:362*/return this.contentLoggingLimit;
    }
    
    public HttpRequest setContentLoggingLimit(final int a1) {
        /*SL:388*/Preconditions.checkArgument(a1 >= 0, (Object)"The content logging limit must be non-negative.");
        /*SL:390*/this.contentLoggingLimit = a1;
        /*SL:391*/return this;
    }
    
    public boolean isLoggingEnabled() {
        /*SL:404*/return this.loggingEnabled;
    }
    
    public HttpRequest setLoggingEnabled(final boolean a1) {
        /*SL:417*/this.loggingEnabled = a1;
        /*SL:418*/return this;
    }
    
    public boolean isCurlLoggingEnabled() {
        /*SL:427*/return this.curlLoggingEnabled;
    }
    
    public HttpRequest setCurlLoggingEnabled(final boolean a1) {
        /*SL:440*/this.curlLoggingEnabled = a1;
        /*SL:441*/return this;
    }
    
    public int getConnectTimeout() {
        /*SL:451*/return this.connectTimeout;
    }
    
    public HttpRequest setConnectTimeout(final int a1) {
        /*SL:465*/Preconditions.checkArgument(a1 >= 0);
        /*SL:466*/this.connectTimeout = a1;
        /*SL:467*/return this;
    }
    
    public int getReadTimeout() {
        /*SL:481*/return this.readTimeout;
    }
    
    public HttpRequest setReadTimeout(final int a1) {
        /*SL:491*/Preconditions.checkArgument(a1 >= 0);
        /*SL:492*/this.readTimeout = a1;
        /*SL:493*/return this;
    }
    
    public HttpHeaders getHeaders() {
        /*SL:502*/return this.headers;
    }
    
    public HttpRequest setHeaders(final HttpHeaders a1) {
        /*SL:515*/this.headers = Preconditions.<HttpHeaders>checkNotNull(a1);
        /*SL:516*/return this;
    }
    
    public HttpHeaders getResponseHeaders() {
        /*SL:525*/return this.responseHeaders;
    }
    
    public HttpRequest setResponseHeaders(final HttpHeaders a1) {
        /*SL:552*/this.responseHeaders = Preconditions.<HttpHeaders>checkNotNull(a1);
        /*SL:553*/return this;
    }
    
    public HttpExecuteInterceptor getInterceptor() {
        /*SL:563*/return this.executeInterceptor;
    }
    
    public HttpRequest setInterceptor(final HttpExecuteInterceptor a1) {
        /*SL:573*/this.executeInterceptor = a1;
        /*SL:574*/return this;
    }
    
    public HttpUnsuccessfulResponseHandler getUnsuccessfulResponseHandler() {
        /*SL:583*/return this.unsuccessfulResponseHandler;
    }
    
    public HttpRequest setUnsuccessfulResponseHandler(final HttpUnsuccessfulResponseHandler a1) {
        /*SL:593*/this.unsuccessfulResponseHandler = a1;
        /*SL:594*/return this;
    }
    
    @Beta
    public HttpIOExceptionHandler getIOExceptionHandler() {
        /*SL:605*/return this.ioExceptionHandler;
    }
    
    @Beta
    public HttpRequest setIOExceptionHandler(final HttpIOExceptionHandler a1) {
        /*SL:616*/this.ioExceptionHandler = a1;
        /*SL:617*/return this;
    }
    
    public HttpResponseInterceptor getResponseInterceptor() {
        /*SL:626*/return this.responseInterceptor;
    }
    
    public HttpRequest setResponseInterceptor(final HttpResponseInterceptor a1) {
        /*SL:635*/this.responseInterceptor = a1;
        /*SL:636*/return this;
    }
    
    public int getNumberOfRetries() {
        /*SL:649*/return this.numRetries;
    }
    
    public HttpRequest setNumberOfRetries(final int a1) {
        /*SL:665*/Preconditions.checkArgument(a1 >= 0);
        /*SL:666*/this.numRetries = a1;
        /*SL:667*/return this;
    }
    
    public HttpRequest setParser(final ObjectParser a1) {
        /*SL:681*/this.objectParser = a1;
        /*SL:682*/return this;
    }
    
    public final ObjectParser getParser() {
        /*SL:691*/return this.objectParser;
    }
    
    public boolean getFollowRedirects() {
        /*SL:700*/return this.followRedirects;
    }
    
    public HttpRequest setFollowRedirects(final boolean a1) {
        /*SL:713*/this.followRedirects = a1;
        /*SL:714*/return this;
    }
    
    public boolean getThrowExceptionOnExecuteError() {
        /*SL:724*/return this.throwExceptionOnExecuteError;
    }
    
    public HttpRequest setThrowExceptionOnExecuteError(final boolean a1) {
        /*SL:738*/this.throwExceptionOnExecuteError = a1;
        /*SL:739*/return this;
    }
    
    @Deprecated
    @Beta
    public boolean getRetryOnExecuteIOException() {
        /*SL:754*/return this.retryOnExecuteIOException;
    }
    
    @Deprecated
    @Beta
    public HttpRequest setRetryOnExecuteIOException(final boolean a1) {
        /*SL:773*/this.retryOnExecuteIOException = a1;
        /*SL:774*/return this;
    }
    
    public boolean getSuppressUserAgentSuffix() {
        /*SL:783*/return this.suppressUserAgentSuffix;
    }
    
    public HttpRequest setSuppressUserAgentSuffix(final boolean a1) {
        /*SL:796*/this.suppressUserAgentSuffix = a1;
        /*SL:797*/return this;
    }
    
    public HttpResponse execute() throws IOException {
        boolean b = /*EL:844*/false;
        /*SL:845*/Preconditions.checkArgument(this.numRetries >= 0);
        int numRetries = /*EL:846*/this.numRetries;
        /*SL:847*/if (this.backOffPolicy != null) {
            /*SL:849*/this.backOffPolicy.reset();
        }
        HttpResponse a1 = /*EL:851*/null;
        /*SL:854*/Preconditions.<String>checkNotNull(this.requestMethod);
        /*SL:855*/Preconditions.<GenericUrl>checkNotNull(this.url);
        IOException ex;
        /*SL:1059*/do {
            if (a1 != null) {
                a1.ignore();
            }
            a1 = null;
            ex = null;
            if (this.executeInterceptor != null) {
                this.executeInterceptor.intercept(this);
            }
            final String build = this.url.build();
            final LowLevelHttpRequest buildRequest = this.transport.buildRequest(this.requestMethod, build);
            final Logger logger = HttpTransport.LOGGER;
            final boolean b2 = this.loggingEnabled && logger.isLoggable(Level.CONFIG);
            StringBuilder a2 = null;
            StringBuilder a3 = null;
            if (b2) {
                a2 = new StringBuilder();
                a2.append("-------------- REQUEST  --------------").append(StringUtils.LINE_SEPARATOR);
                a2.append(this.requestMethod).append(' ').append(build).append(StringUtils.LINE_SEPARATOR);
                if (this.curlLoggingEnabled) {
                    a3 = new StringBuilder("curl -v --compressed");
                    if (!this.requestMethod.equals("GET")) {
                        a3.append(" -X ").append(this.requestMethod);
                    }
                }
            }
            final String userAgent = this.headers.getUserAgent();
            if (!this.suppressUserAgentSuffix) {
                if (userAgent == null) {
                    this.headers.setUserAgent("Google-HTTP-Java-Client/1.25.0 (gzip)");
                }
                else {
                    this.headers.setUserAgent(userAgent + " " + "Google-HTTP-Java-Client/1.25.0 (gzip)");
                }
            }
            HttpHeaders.serializeHeaders(this.headers, a2, a3, logger, buildRequest);
            if (!this.suppressUserAgentSuffix) {
                this.headers.setUserAgent(userAgent);
            }
            StreamingContent content = this.content;
            final boolean b3 = content == null || this.content.retrySupported();
            if (content != null) {
                final String v0 = this.content.getType();
                if (b2) {
                    content = new LoggingStreamingContent(content, HttpTransport.LOGGER, Level.CONFIG, this.contentLoggingLimit);
                }
                String v;
                long v2;
                if (this.encoding == null) {
                    v = null;
                    v2 = this.content.getLength();
                }
                else {
                    v = this.encoding.getName();
                    content = new HttpEncodingStreamingContent(content, this.encoding);
                    v2 = (b3 ? IOUtils.computeLength(content) : -1L);
                }
                if (b2) {
                    if (v0 != null) {
                        final String v3 = "Content-Type: " + v0;
                        a2.append(v3).append(StringUtils.LINE_SEPARATOR);
                        if (a3 != null) {
                            a3.append(" -H '" + v3 + "'");
                        }
                    }
                    if (v != null) {
                        final String v3 = "Content-Encoding: " + v;
                        a2.append(v3).append(StringUtils.LINE_SEPARATOR);
                        if (a3 != null) {
                            a3.append(" -H '" + v3 + "'");
                        }
                    }
                    if (v2 >= 0L) {
                        final String v3 = "Content-Length: " + v2;
                        a2.append(v3).append(StringUtils.LINE_SEPARATOR);
                    }
                }
                if (a3 != null) {
                    a3.append(" -d '@-'");
                }
                buildRequest.setContentType(v0);
                buildRequest.setContentEncoding(v);
                buildRequest.setContentLength(v2);
                buildRequest.setStreamingContent(content);
            }
            if (b2) {
                logger.config(a2.toString());
                if (a3 != null) {
                    a3.append(" -- '");
                    a3.append(build.replaceAll("'", "'\"'\"'"));
                    a3.append("'");
                    if (content != null) {
                        a3.append(" << $$$");
                    }
                    logger.config(a3.toString());
                }
            }
            b = (b3 && numRetries > 0);
            buildRequest.setTimeout(this.connectTimeout, this.readTimeout);
            try {
                final LowLevelHttpResponse v4 = buildRequest.execute();
                boolean v5 = false;
                try {
                    a1 = new HttpResponse(this, v4);
                    v5 = true;
                }
                finally {
                    if (!v5) {
                        final InputStream v6 = v4.getContent();
                        if (v6 != null) {
                            v6.close();
                        }
                    }
                }
            }
            catch (IOException v7) {
                if (!this.retryOnExecuteIOException && (this.ioExceptionHandler == null || !this.ioExceptionHandler.handleIOException(this, b))) {
                    throw v7;
                }
                ex = v7;
                if (b2) {
                    logger.log(Level.WARNING, "exception thrown while executing request", v7);
                }
            }
            boolean v8 = false;
            try {
                if (a1 != null && !a1.isSuccessStatusCode()) {
                    boolean v5 = false;
                    if (this.unsuccessfulResponseHandler != null) {
                        v5 = this.unsuccessfulResponseHandler.handleResponse(this, a1, b);
                    }
                    if (!v5) {
                        if (this.handleRedirect(a1.getStatusCode(), a1.getHeaders())) {
                            v5 = true;
                        }
                        else if (b && this.backOffPolicy != null && this.backOffPolicy.isBackOffRequired(a1.getStatusCode())) {
                            final long v9 = this.backOffPolicy.getNextBackOffMillis();
                            if (v9 != -1L) {
                                try {
                                    this.sleeper.sleep(v9);
                                }
                                catch (InterruptedException ex2) {}
                                v5 = true;
                            }
                        }
                    }
                    b &= v5;
                    if (b) {
                        a1.ignore();
                    }
                }
                else {
                    b &= (a1 == null);
                }
                --numRetries;
                v8 = true;
            }
            finally {
                if (a1 != null && !v8) {
                    a1.disconnect();
                }
            }
        } while (b);
        /*SL:1061*/if (a1 == null) {
            /*SL:1063*/throw ex;
        }
        /*SL:1066*/if (this.responseInterceptor != null) {
            /*SL:1067*/this.responseInterceptor.interceptResponse(a1);
        }
        /*SL:1070*/if (this.throwExceptionOnExecuteError && !a1.isSuccessStatusCode()) {
            try {
                /*SL:1072*/throw new HttpResponseException(a1);
            }
            finally {
                /*SL:1074*/a1.disconnect();
            }
        }
        /*SL:1077*/return a1;
    }
    
    @Beta
    public Future<HttpResponse> executeAsync(final Executor a1) {
        final FutureTask<HttpResponse> v1 = /*EL:1090*/new FutureTask<HttpResponse>(new Callable<HttpResponse>() {
            @Override
            public HttpResponse call() throws Exception {
                /*SL:1093*/return HttpRequest.this.execute();
            }
        });
        /*SL:1096*/a1.execute(v1);
        /*SL:1097*/return v1;
    }
    
    @Beta
    public Future<HttpResponse> executeAsync() {
        /*SL:1110*/return this.executeAsync(Executors.newSingleThreadExecutor());
    }
    
    public boolean handleRedirect(final int a1, final HttpHeaders a2) {
        final String v1 = /*EL:1133*/a2.getLocation();
        /*SL:1134*/if (this.getFollowRedirects() && HttpStatusCodes.isRedirect(a1) && v1 != null) {
            /*SL:1137*/this.setUrl(new GenericUrl(this.url.toURL(v1)));
            /*SL:1139*/if (a1 == 303) {
                /*SL:1140*/this.setRequestMethod("GET");
                /*SL:1142*/this.setContent(null);
            }
            /*SL:1145*/this.headers.setAuthorization((String)null);
            /*SL:1146*/this.headers.setIfMatch(null);
            /*SL:1147*/this.headers.setIfNoneMatch(null);
            /*SL:1148*/this.headers.setIfModifiedSince(null);
            /*SL:1149*/this.headers.setIfUnmodifiedSince(null);
            /*SL:1150*/this.headers.setIfRange(null);
            /*SL:1151*/return true;
        }
        /*SL:1153*/return false;
    }
    
    public Sleeper getSleeper() {
        /*SL:1162*/return this.sleeper;
    }
    
    public HttpRequest setSleeper(final Sleeper a1) {
        /*SL:1171*/this.sleeper = Preconditions.<Sleeper>checkNotNull(a1);
        /*SL:1172*/return this;
    }
}
