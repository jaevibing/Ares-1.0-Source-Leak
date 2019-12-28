package com.google.cloud.storage;

import com.google.cloud.StringEnumType;
import com.google.api.core.ApiFunction;
import com.google.cloud.StringEnumValue;

public final class HttpMethod extends StringEnumValue
{
    private static final long serialVersionUID = -1394461645628254471L;
    private static final ApiFunction<String, HttpMethod> CONSTRUCTOR;
    private static final StringEnumType<HttpMethod> type;
    public static final HttpMethod GET;
    public static final HttpMethod HEAD;
    public static final HttpMethod PUT;
    public static final HttpMethod POST;
    public static final HttpMethod DELETE;
    public static final HttpMethod OPTIONS;
    
    private HttpMethod(final String a1) {
        super(a1);
    }
    
    public static HttpMethod valueOfStrict(final String a1) {
        /*SL:54*/return (HttpMethod)HttpMethod.type.valueOfStrict(a1);
    }
    
    public static HttpMethod valueOf(final String a1) {
        /*SL:59*/return (HttpMethod)HttpMethod.type.valueOf(a1);
    }
    
    public static HttpMethod[] values() {
        /*SL:64*/return (HttpMethod[])HttpMethod.type.values();
    }
    
    static {
        CONSTRUCTOR = (ApiFunction)new ApiFunction<String, HttpMethod>() {
            public HttpMethod apply(final String a1) {
                /*SL:35*/return new HttpMethod(a1, null);
            }
        };
        type = new StringEnumType((Class)HttpMethod.class, (ApiFunction)HttpMethod.CONSTRUCTOR);
        GET = (HttpMethod)HttpMethod.type.createAndRegister("GET");
        HEAD = (HttpMethod)HttpMethod.type.createAndRegister("HEAD");
        PUT = (HttpMethod)HttpMethod.type.createAndRegister("PUT");
        POST = (HttpMethod)HttpMethod.type.createAndRegister("POST");
        DELETE = (HttpMethod)HttpMethod.type.createAndRegister("DELETE");
        OPTIONS = (HttpMethod)HttpMethod.type.createAndRegister("OPTIONS");
    }
}
