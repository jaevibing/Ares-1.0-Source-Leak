package com.google.api.client.http;

import java.io.IOException;
import com.google.api.client.util.BackOffUtils;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sleeper;
import com.google.api.client.util.BackOff;
import com.google.api.client.util.Beta;

@Beta
public class HttpBackOffIOExceptionHandler implements HttpIOExceptionHandler
{
    private final BackOff backOff;
    private Sleeper sleeper;
    
    public HttpBackOffIOExceptionHandler(final BackOff a1) {
        this.sleeper = Sleeper.DEFAULT;
        this.backOff = Preconditions.<BackOff>checkNotNull(a1);
    }
    
    public final BackOff getBackOff() {
        /*SL:75*/return this.backOff;
    }
    
    public final Sleeper getSleeper() {
        /*SL:80*/return this.sleeper;
    }
    
    public HttpBackOffIOExceptionHandler setSleeper(final Sleeper a1) {
        /*SL:96*/this.sleeper = Preconditions.<Sleeper>checkNotNull(a1);
        /*SL:97*/return this;
    }
    
    @Override
    public boolean handleIOException(final HttpRequest v1, final boolean v2) throws IOException {
        /*SL:109*/if (!v2) {
            /*SL:110*/return false;
        }
        try {
            /*SL:113*/return BackOffUtils.next(this.sleeper, this.backOff);
        }
        catch (InterruptedException a1) {
            /*SL:115*/return false;
        }
    }
}
