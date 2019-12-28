package com.google.api.client.auth.oauth2;

import com.google.api.client.util.Data;
import com.google.api.client.http.UrlEncodedContent;
import java.util.Map;
import com.google.api.client.util.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import com.google.api.client.http.HttpRequest;
import java.util.regex.Pattern;

public class BearerToken
{
    static final String PARAM_NAME = "access_token";
    static final Pattern INVALID_TOKEN_ERROR;
    
    public static Credential.AccessMethod authorizationHeaderAccessMethod() {
        /*SL:134*/return new AuthorizationHeaderAccessMethod();
    }
    
    public static Credential.AccessMethod formEncodedBodyAccessMethod() {
        /*SL:143*/return new FormEncodedBodyAccessMethod();
    }
    
    public static Credential.AccessMethod queryParameterAccessMethod() {
        /*SL:152*/return new QueryParameterAccessMethod();
    }
    
    static {
        INVALID_TOKEN_ERROR = Pattern.compile("\\s*error\\s*=\\s*\"?invalid_token\"?");
    }
    
    static final class AuthorizationHeaderAccessMethod implements Credential.AccessMethod
    {
        static final String HEADER_PREFIX = "Bearer ";
        
        @Override
        public void intercept(final HttpRequest a1, final String a2) throws IOException {
            /*SL:64*/a1.getHeaders().setAuthorization("Bearer " + a2);
        }
        
        @Override
        public String getAccessTokenFromRequest(final HttpRequest v2) {
            final List<String> v3 = /*EL:68*/v2.getHeaders().getAuthorizationAsList();
            /*SL:69*/if (v3 != null) {
                /*SL:70*/for (final String a1 : v3) {
                    /*SL:71*/if (a1.startsWith("Bearer ")) {
                        /*SL:72*/return a1.substring("Bearer ".length());
                    }
                }
            }
            /*SL:76*/return null;
        }
    }
    
    static final class FormEncodedBodyAccessMethod implements Credential.AccessMethod
    {
        @Override
        public void intercept(final HttpRequest a1, final String a2) throws IOException {
            /*SL:90*/Preconditions.checkArgument(!"GET".equals(a1.getRequestMethod()), /*EL:91*/(Object)"HTTP GET method is not supported");
            getData(/*EL:92*/a1).put("access_token", a2);
        }
        
        @Override
        public String getAccessTokenFromRequest(final HttpRequest a1) {
            final Object v1 = getData(/*EL:96*/a1).get("access_token");
            /*SL:97*/return (v1 == null) ? null : v1.toString();
        }
        
        private static Map<String, Object> getData(final HttpRequest a1) {
            /*SL:101*/return Data.mapOf(UrlEncodedContent.getContent(a1).getData());
        }
    }
    
    static final class QueryParameterAccessMethod implements Credential.AccessMethod
    {
        @Override
        public void intercept(final HttpRequest a1, final String a2) throws IOException {
            /*SL:115*/a1.getUrl().set("access_token", a2);
        }
        
        @Override
        public String getAccessTokenFromRequest(final HttpRequest a1) {
            final Object v1 = /*EL:119*/a1.getUrl().get("access_token");
            /*SL:120*/return (v1 == null) ? null : v1.toString();
        }
    }
}
