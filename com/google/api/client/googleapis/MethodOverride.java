package com.google.api.client.googleapis;

import java.io.IOException;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpExecuteInterceptor;

public final class MethodOverride implements HttpExecuteInterceptor, HttpRequestInitializer
{
    public static final String HEADER = "X-HTTP-Method-Override";
    static final int MAX_URL_LENGTH = 2048;
    private final boolean overrideAllMethods;
    
    public MethodOverride() {
        this(false);
    }
    
    MethodOverride(final boolean a1) {
        this.overrideAllMethods = a1;
    }
    
    public void initialize(final HttpRequest a1) {
        /*SL:89*/a1.setInterceptor(this);
    }
    
    public void intercept(final HttpRequest v2) throws IOException {
        /*SL:93*/if (this.overrideThisMethod(v2)) {
            final String a1 = /*EL:94*/v2.getRequestMethod();
            /*SL:95*/v2.setRequestMethod("POST");
            /*SL:96*/v2.getHeaders().set("X-HTTP-Method-Override", a1);
            /*SL:97*/if (a1.equals("GET")) {
                /*SL:99*/v2.setContent(new UrlEncodedContent(v2.getUrl().clone()));
                /*SL:101*/v2.getUrl().clear();
            }
            else/*SL:102*/ if (v2.getContent() == null) {
                /*SL:104*/v2.setContent(new EmptyContent());
            }
        }
    }
    
    private boolean overrideThisMethod(final HttpRequest a1) throws IOException {
        final String v1 = /*EL:110*/a1.getRequestMethod();
        /*SL:111*/if (v1.equals("POST")) {
            /*SL:112*/return false;
        }
        /*SL:114*/if (v1.equals("GET")) {
            if (a1.getUrl().build().length() <= 2048) {
                return /*EL:118*/!a1.getTransport().supportsMethod(v1);
            }
        }
        else if (!this.overrideAllMethods) {
            return !a1.getTransport().supportsMethod(v1);
        }
        return true;
    }
    
    public static final class Builder
    {
        private boolean overrideAllMethods;
        
        public MethodOverride build() {
            /*SL:137*/return new MethodOverride(this.overrideAllMethods);
        }
        
        public boolean getOverrideAllMethods() {
            /*SL:145*/return this.overrideAllMethods;
        }
        
        public Builder setOverrideAllMethods(final boolean a1) {
            /*SL:157*/this.overrideAllMethods = a1;
            /*SL:158*/return this;
        }
    }
}
