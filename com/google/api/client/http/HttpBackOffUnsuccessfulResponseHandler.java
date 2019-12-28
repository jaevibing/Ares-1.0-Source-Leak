package com.google.api.client.http;

import java.io.IOException;
import com.google.api.client.util.BackOffUtils;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sleeper;
import com.google.api.client.util.BackOff;
import com.google.api.client.util.Beta;

@Beta
public class HttpBackOffUnsuccessfulResponseHandler implements HttpUnsuccessfulResponseHandler
{
    private final BackOff backOff;
    private BackOffRequired backOffRequired;
    private Sleeper sleeper;
    
    public HttpBackOffUnsuccessfulResponseHandler(final BackOff a1) {
        this.backOffRequired = BackOffRequired.ON_SERVER_ERROR;
        this.sleeper = Sleeper.DEFAULT;
        this.backOff = Preconditions.<BackOff>checkNotNull(a1);
    }
    
    public final BackOff getBackOff() {
        /*SL:79*/return this.backOff;
    }
    
    public final BackOffRequired getBackOffRequired() {
        /*SL:87*/return this.backOffRequired;
    }
    
    public HttpBackOffUnsuccessfulResponseHandler setBackOffRequired(final BackOffRequired a1) {
        /*SL:105*/this.backOffRequired = Preconditions.<BackOffRequired>checkNotNull(a1);
        /*SL:106*/return this;
    }
    
    public final Sleeper getSleeper() {
        /*SL:111*/return this.sleeper;
    }
    
    public HttpBackOffUnsuccessfulResponseHandler setSleeper(final Sleeper a1) {
        /*SL:127*/this.sleeper = Preconditions.<Sleeper>checkNotNull(a1);
        /*SL:128*/return this;
    }
    
    @Override
    public final boolean handleResponse(final HttpRequest a1, final HttpResponse a2, final boolean a3) throws IOException {
        /*SL:141*/if (!a3) {
            /*SL:142*/return false;
        }
        /*SL:145*/if (this.backOffRequired.isRequired(a2)) {
            try {
                /*SL:147*/return BackOffUtils.next(this.sleeper, this.backOff);
            }
            catch (InterruptedException ex) {}
        }
        /*SL:152*/return false;
    }
    
    @Beta
    public interface BackOffRequired
    {
        public static final BackOffRequired ALWAYS = new BackOffRequired() {
            @Override
            public boolean isRequired(HttpResponse a1) {
                /*SL:173*/return true;
            }
        };
        public static final BackOffRequired ON_SERVER_ERROR = new BackOffRequired() {
            @Override
            public boolean isRequired(HttpResponse a1) {
                /*SL:183*/return a1.getStatusCode() / 100 == 5;
            }
        };
        
        boolean isRequired(HttpResponse p0);
    }
}
