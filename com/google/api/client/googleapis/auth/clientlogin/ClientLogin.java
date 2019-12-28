package com.google.api.client.googleapis.auth.clientlogin;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpExecuteInterceptor;
import java.io.IOException;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Strings;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Key;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;

@Beta
public final class ClientLogin
{
    public HttpTransport transport;
    public GenericUrl serverUrl;
    @Key("source")
    public String applicationName;
    @Key("service")
    public String authTokenType;
    @Key("Email")
    public String username;
    @Key("Passwd")
    public String password;
    @Key
    public String accountType;
    @Key("logintoken")
    public String captchaToken;
    @Key("logincaptcha")
    public String captchaAnswer;
    
    public ClientLogin() {
        this.serverUrl = new GenericUrl("https://www.google.com");
    }
    
    public Response authenticate() throws IOException {
        final GenericUrl v1 = /*EL:174*/this.serverUrl.clone();
        /*SL:175*/v1.appendRawPath("/accounts/ClientLogin");
        final HttpRequest v2 = /*EL:176*/this.transport.createRequestFactory().buildPostRequest(v1, new UrlEncodedContent(this));
        /*SL:178*/v2.setParser(AuthKeyValueParser.INSTANCE);
        /*SL:179*/v2.setContentLoggingLimit(0);
        /*SL:180*/v2.setThrowExceptionOnExecuteError(false);
        final HttpResponse v3 = /*EL:181*/v2.execute();
        /*SL:183*/if (v3.isSuccessStatusCode()) {
            /*SL:184*/return v3.<Response>parseAs(Response.class);
        }
        final HttpResponseException.Builder v4 = /*EL:186*/new HttpResponseException.Builder(v3.getStatusCode(), v3.getStatusMessage(), v3.getHeaders());
        final ErrorInfo v5 = /*EL:189*/v3.<ErrorInfo>parseAs(ErrorInfo.class);
        final String v6 = /*EL:190*/v5.toString();
        final StringBuilder v7 = /*EL:191*/HttpResponseException.computeMessageBuffer(v3);
        /*SL:192*/if (!Strings.isNullOrEmpty(v6)) {
            /*SL:193*/v7.append(StringUtils.LINE_SEPARATOR).append(v6);
            /*SL:194*/v4.setContent(v6);
        }
        /*SL:196*/v4.setMessage(v7.toString());
        /*SL:197*/throw new ClientLoginResponseException(v4, v5);
    }
    
    public static String getAuthorizationHeaderValue(final String a1) {
        final String s = /*EL:207*/"GoogleLogin auth=";
        final String value = String.valueOf(a1);
        return (value.length() != 0) ? s.concat(value) : new String(s);
    }
    
    public static final class Response implements HttpExecuteInterceptor, HttpRequestInitializer
    {
        @Key("Auth")
        public String auth;
        
        public String getAuthorizationHeaderValue() {
            /*SL:139*/return ClientLogin.getAuthorizationHeaderValue(this.auth);
        }
        
        public void initialize(final HttpRequest a1) {
            /*SL:143*/a1.setInterceptor(this);
        }
        
        public void intercept(final HttpRequest a1) {
            /*SL:147*/a1.getHeaders().setAuthorization(this.getAuthorizationHeaderValue());
        }
    }
    
    public static final class ErrorInfo
    {
        @Key("Error")
        public String error;
        @Key("Url")
        public String url;
        @Key("CaptchaToken")
        public String captchaToken;
        @Key("CaptchaUrl")
        public String captchaUrl;
    }
}
