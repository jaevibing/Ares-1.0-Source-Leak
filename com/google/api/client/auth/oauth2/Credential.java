package com.google.api.client.auth.oauth2;

import com.google.api.client.util.Lists;
import com.google.api.client.http.GenericUrl;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import com.google.api.client.util.Objects;
import com.google.api.client.http.HttpResponse;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import java.util.Collections;
import com.google.api.client.util.Preconditions;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Collection;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Clock;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpExecuteInterceptor;

public class Credential implements HttpExecuteInterceptor, HttpRequestInitializer, HttpUnsuccessfulResponseHandler
{
    static final Logger LOGGER;
    private final Lock lock;
    private final AccessMethod method;
    private final Clock clock;
    private String accessToken;
    private Long expirationTimeMilliseconds;
    private String refreshToken;
    private final HttpTransport transport;
    private final HttpExecuteInterceptor clientAuthentication;
    private final JsonFactory jsonFactory;
    private final String tokenServerEncodedUrl;
    private final Collection<CredentialRefreshListener> refreshListeners;
    private final HttpRequestInitializer requestInitializer;
    
    public Credential(final AccessMethod a1) {
        this(new Builder(a1));
    }
    
    protected Credential(final Builder a1) {
        this.lock = new ReentrantLock();
        this.method = Preconditions.<AccessMethod>checkNotNull(a1.method);
        this.transport = a1.transport;
        this.jsonFactory = a1.jsonFactory;
        this.tokenServerEncodedUrl = ((a1.tokenServerUrl == null) ? null : a1.tokenServerUrl.build());
        this.clientAuthentication = a1.clientAuthentication;
        this.requestInitializer = a1.requestInitializer;
        this.refreshListeners = Collections.<CredentialRefreshListener>unmodifiableCollection((Collection<? extends CredentialRefreshListener>)a1.refreshListeners);
        this.clock = Preconditions.<Clock>checkNotNull(a1.clock);
    }
    
    @Override
    public void intercept(final HttpRequest v2) throws IOException {
        /*SL:212*/this.lock.lock();
        try {
            final Long a1 = /*EL:214*/this.getExpiresInSeconds();
            /*SL:216*/if (this.accessToken == null || (a1 != null && a1 <= 60L)) {
                /*SL:217*/this.refreshToken();
                /*SL:218*/if (this.accessToken == null) {
                    /*SL:220*/return;
                }
            }
            /*SL:223*/this.method.intercept(v2, this.accessToken);
        }
        finally {
            /*SL:225*/this.lock.unlock();
        }
    }
    
    @Override
    public boolean handleResponse(final HttpRequest v1, final HttpResponse v2, final boolean v3) {
        boolean v4 = /*EL:242*/false;
        boolean v5 = /*EL:243*/false;
        final List<String> v6 = /*EL:245*/v2.getHeaders().getAuthenticateAsList();
        /*SL:251*/if (v6 != null) {
            /*SL:252*/for (final String a1 : v6) {
                /*SL:253*/if (a1.startsWith("Bearer ")) {
                    /*SL:255*/v5 = true;
                    /*SL:256*/v4 = BearerToken.INVALID_TOKEN_ERROR.matcher(a1).find();
                    /*SL:257*/break;
                }
            }
        }
        /*SL:263*/if (!v5) {
            /*SL:264*/v4 = (v2.getStatusCode() == 401);
        }
        /*SL:267*/if (v4) {
            try {
                /*SL:269*/this.lock.lock();
                try {
                    /*SL:272*/return !Objects.equal(this.accessToken, this.method.getAccessTokenFromRequest(v1)) || this.refreshToken();
                }
                finally {
                    /*SL:275*/this.lock.unlock();
                }
            }
            catch (IOException a2) {
                Credential.LOGGER.log(Level.SEVERE, /*EL:278*/"unable to refresh token", a2);
            }
        }
        /*SL:281*/return false;
    }
    
    @Override
    public void initialize(final HttpRequest a1) throws IOException {
        /*SL:285*/a1.setInterceptor(this);
        /*SL:286*/a1.setUnsuccessfulResponseHandler(this);
    }
    
    public final String getAccessToken() {
        /*SL:291*/this.lock.lock();
        try {
            /*SL:293*/return this.accessToken;
        }
        finally {
            /*SL:295*/this.lock.unlock();
        }
    }
    
    public Credential setAccessToken(final String a1) {
        /*SL:310*/this.lock.lock();
        try {
            /*SL:312*/this.accessToken = a1;
        }
        finally {
            /*SL:314*/this.lock.unlock();
        }
        /*SL:316*/return this;
    }
    
    public final AccessMethod getMethod() {
        /*SL:324*/return this.method;
    }
    
    public final Clock getClock() {
        /*SL:332*/return this.clock;
    }
    
    public final HttpTransport getTransport() {
        /*SL:337*/return this.transport;
    }
    
    public final JsonFactory getJsonFactory() {
        /*SL:345*/return this.jsonFactory;
    }
    
    public final String getTokenServerEncodedUrl() {
        /*SL:350*/return this.tokenServerEncodedUrl;
    }
    
    public final String getRefreshToken() {
        /*SL:358*/this.lock.lock();
        try {
            /*SL:360*/return this.refreshToken;
        }
        finally {
            /*SL:362*/this.lock.unlock();
        }
    }
    
    public Credential setRefreshToken(final String a1) {
        /*SL:377*/this.lock.lock();
        try {
            /*SL:379*/if (a1 != null) {
                /*SL:380*/Preconditions.checkArgument(this.jsonFactory != null && this.transport != null && this.clientAuthentication != null && this.tokenServerEncodedUrl != null, (Object)"Please use the Builder and call setJsonFactory, setTransport, setClientAuthentication and setTokenServerUrl/setTokenServerEncodedUrl");
            }
            /*SL:385*/this.refreshToken = a1;
        }
        finally {
            /*SL:387*/this.lock.unlock();
        }
        /*SL:389*/return this;
    }
    
    public final Long getExpirationTimeMilliseconds() {
        /*SL:397*/this.lock.lock();
        try {
            /*SL:399*/return this.expirationTimeMilliseconds;
        }
        finally {
            /*SL:401*/this.lock.unlock();
        }
    }
    
    public Credential setExpirationTimeMilliseconds(final Long a1) {
        /*SL:415*/this.lock.lock();
        try {
            /*SL:417*/this.expirationTimeMilliseconds = a1;
        }
        finally {
            /*SL:419*/this.lock.unlock();
        }
        /*SL:421*/return this;
    }
    
    public final Long getExpiresInSeconds() {
        /*SL:429*/this.lock.lock();
        try {
            /*SL:431*/if (this.expirationTimeMilliseconds == null) {
                /*SL:432*/return null;
            }
            /*SL:434*/return (this.expirationTimeMilliseconds - this.clock.currentTimeMillis()) / 1000L;
        }
        finally {
            /*SL:436*/this.lock.unlock();
        }
    }
    
    public Credential setExpiresInSeconds(final Long a1) {
        /*SL:453*/return this.setExpirationTimeMilliseconds((a1 == null) ? null : (this.clock.currentTimeMillis() + /*EL:454*/a1 * 1000L));
    }
    
    public final HttpExecuteInterceptor getClientAuthentication() {
        /*SL:459*/return this.clientAuthentication;
    }
    
    public final HttpRequestInitializer getRequestInitializer() {
        /*SL:467*/return this.requestInitializer;
    }
    
    public final boolean refreshToken() throws IOException {
        /*SL:490*/this.lock.lock();
        try {
            try {
                final TokenResponse executeRefreshToken = /*EL:493*/this.executeRefreshToken();
                /*SL:494*/if (executeRefreshToken != null) {
                    /*SL:495*/this.setFromTokenResponse(executeRefreshToken);
                    /*SL:496*/for (final CredentialRefreshListener v1 : this.refreshListeners) {
                        /*SL:497*/v1.onTokenResponse(this, executeRefreshToken);
                    }
                    /*SL:499*/return true;
                }
            }
            catch (TokenResponseException ex) {
                final boolean v2 = /*EL:502*/400 <= ex.getStatusCode() && ex.getStatusCode() < 500;
                /*SL:504*/if (ex.getDetails() != null && v2) {
                    /*SL:507*/this.setAccessToken(null);
                    /*SL:508*/this.setExpiresInSeconds(null);
                }
                /*SL:510*/for (final CredentialRefreshListener v3 : this.refreshListeners) {
                    /*SL:511*/v3.onTokenErrorResponse(this, ex.getDetails());
                }
                /*SL:513*/if (v2) {
                    /*SL:514*/throw ex;
                }
            }
            /*SL:517*/return false;
        }
        finally {
            /*SL:519*/this.lock.unlock();
        }
    }
    
    public Credential setFromTokenResponse(final TokenResponse a1) {
        /*SL:540*/this.setAccessToken(a1.getAccessToken());
        /*SL:543*/if (a1.getRefreshToken() != null) {
            /*SL:544*/this.setRefreshToken(a1.getRefreshToken());
        }
        /*SL:546*/this.setExpiresInSeconds(a1.getExpiresInSeconds());
        /*SL:547*/return this;
    }
    
    protected TokenResponse executeRefreshToken() throws IOException {
        /*SL:571*/if (this.refreshToken == null) {
            /*SL:572*/return null;
        }
        /*SL:574*/return new RefreshTokenRequest(this.transport, this.jsonFactory, new GenericUrl(this.tokenServerEncodedUrl), this.refreshToken).setClientAuthentication(this.clientAuthentication).setRequestInitializer(/*EL:575*/this.requestInitializer).execute();
    }
    
    public final Collection<CredentialRefreshListener> getRefreshListeners() {
        /*SL:581*/return this.refreshListeners;
    }
    
    static {
        LOGGER = Logger.getLogger(Credential.class.getName());
    }
    
    public static class Builder
    {
        final AccessMethod method;
        HttpTransport transport;
        JsonFactory jsonFactory;
        GenericUrl tokenServerUrl;
        Clock clock;
        HttpExecuteInterceptor clientAuthentication;
        HttpRequestInitializer requestInitializer;
        Collection<CredentialRefreshListener> refreshListeners;
        
        public Builder(final AccessMethod a1) {
            this.clock = Clock.SYSTEM;
            this.refreshListeners = (Collection<CredentialRefreshListener>)Lists.<Object>newArrayList();
            this.method = Preconditions.<AccessMethod>checkNotNull(a1);
        }
        
        public Credential build() {
            /*SL:641*/return new Credential(this);
        }
        
        public final AccessMethod getMethod() {
            /*SL:649*/return this.method;
        }
        
        public final HttpTransport getTransport() {
            /*SL:657*/return this.transport;
        }
        
        public Builder setTransport(final HttpTransport a1) {
            /*SL:670*/this.transport = a1;
            /*SL:671*/return this;
        }
        
        public final Clock getClock() {
            /*SL:679*/return this.clock;
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:692*/this.clock = Preconditions.<Clock>checkNotNull(a1);
            /*SL:693*/return this;
        }
        
        public final JsonFactory getJsonFactory() {
            /*SL:701*/return this.jsonFactory;
        }
        
        public Builder setJsonFactory(final JsonFactory a1) {
            /*SL:714*/this.jsonFactory = a1;
            /*SL:715*/return this;
        }
        
        public final GenericUrl getTokenServerUrl() {
            /*SL:720*/return this.tokenServerUrl;
        }
        
        public Builder setTokenServerUrl(final GenericUrl a1) {
            /*SL:732*/this.tokenServerUrl = a1;
            /*SL:733*/return this;
        }
        
        public Builder setTokenServerEncodedUrl(final String a1) {
            /*SL:745*/this.tokenServerUrl = ((a1 == null) ? null : new GenericUrl(a1));
            /*SL:747*/return this;
        }
        
        public final HttpExecuteInterceptor getClientAuthentication() {
            /*SL:755*/return this.clientAuthentication;
        }
        
        public Builder setClientAuthentication(final HttpExecuteInterceptor a1) {
            /*SL:768*/this.clientAuthentication = a1;
            /*SL:769*/return this;
        }
        
        public final HttpRequestInitializer getRequestInitializer() {
            /*SL:777*/return this.requestInitializer;
        }
        
        public Builder setRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:790*/this.requestInitializer = a1;
            /*SL:791*/return this;
        }
        
        public Builder addRefreshListener(final CredentialRefreshListener a1) {
            /*SL:805*/this.refreshListeners.add(Preconditions.<CredentialRefreshListener>checkNotNull(a1));
            /*SL:806*/return this;
        }
        
        public final Collection<CredentialRefreshListener> getRefreshListeners() {
            /*SL:811*/return this.refreshListeners;
        }
        
        public Builder setRefreshListeners(final Collection<CredentialRefreshListener> a1) {
            /*SL:823*/this.refreshListeners = Preconditions.<Collection<CredentialRefreshListener>>checkNotNull(a1);
            /*SL:824*/return this;
        }
    }
    
    public interface AccessMethod
    {
        void intercept(HttpRequest p0, String p1) throws IOException;
        
        String getAccessTokenFromRequest(HttpRequest p0);
    }
}
