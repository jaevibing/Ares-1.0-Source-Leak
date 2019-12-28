package com.google.api.client.http.apache;

import java.net.URI;
import com.google.api.client.util.Preconditions;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

final class HttpExtensionMethod extends HttpEntityEnclosingRequestBase
{
    private final String methodName;
    
    public HttpExtensionMethod(final String a1, final String a2) {
        this.methodName = Preconditions.<String>checkNotNull(a1);
        this.setURI(URI.create(a2));
    }
    
    public String getMethod() {
        /*SL:40*/return this.methodName;
    }
}
