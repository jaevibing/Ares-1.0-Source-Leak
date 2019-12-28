package com.google.api.client.googleapis.json;

import com.google.api.client.util.Data;
import com.google.api.client.util.GenericData;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import java.util.List;
import com.google.api.client.json.GenericJson;

public class GoogleJsonError extends GenericJson
{
    @Key
    private List<ErrorInfo> errors;
    @Key
    private int code;
    @Key
    private String message;
    
    public static GoogleJsonError parse(final JsonFactory a1, final HttpResponse a2) throws IOException {
        final JsonObjectParser v1 = /*EL:50*/new JsonObjectParser.Builder(a1).setWrapperKeys(Collections.<String>singleton("error")).build();
        /*SL:52*/return v1.<GoogleJsonError>parseAndClose(a2.getContent(), a2.getContentCharset(), GoogleJsonError.class);
    }
    
    public final List<ErrorInfo> getErrors() {
        /*SL:209*/return this.errors;
    }
    
    public final void setErrors(final List<ErrorInfo> a1) {
        /*SL:218*/this.errors = a1;
    }
    
    public final int getCode() {
        /*SL:227*/return this.code;
    }
    
    public final void setCode(final int a1) {
        /*SL:236*/this.code = a1;
    }
    
    public final String getMessage() {
        /*SL:245*/return this.message;
    }
    
    public final void setMessage(final String a1) {
        /*SL:254*/this.message = a1;
    }
    
    public GoogleJsonError set(final String a1, final Object a2) {
        /*SL:259*/return (GoogleJsonError)super.set(a1, a2);
    }
    
    public GoogleJsonError clone() {
        /*SL:264*/return (GoogleJsonError)super.clone();
    }
    
    static {
        Data.<Object>nullOf(ErrorInfo.class);
    }
    
    public static class ErrorInfo extends GenericJson
    {
        @Key
        private String domain;
        @Key
        private String reason;
        @Key
        private String message;
        @Key
        private String location;
        @Key
        private String locationType;
        
        public final String getDomain() {
            /*SL:94*/return this.domain;
        }
        
        public final void setDomain(final String a1) {
            /*SL:103*/this.domain = a1;
        }
        
        public final String getReason() {
            /*SL:112*/return this.reason;
        }
        
        public final void setReason(final String a1) {
            /*SL:121*/this.reason = a1;
        }
        
        public final String getMessage() {
            /*SL:130*/return this.message;
        }
        
        public final void setMessage(final String a1) {
            /*SL:139*/this.message = a1;
        }
        
        public final String getLocation() {
            /*SL:149*/return this.location;
        }
        
        public final void setLocation(final String a1) {
            /*SL:159*/this.location = a1;
        }
        
        public final String getLocationType() {
            /*SL:168*/return this.locationType;
        }
        
        public final void setLocationType(final String a1) {
            /*SL:177*/this.locationType = a1;
        }
        
        public ErrorInfo set(final String a1, final Object a2) {
            /*SL:182*/return (ErrorInfo)super.set(a1, a2);
        }
        
        public ErrorInfo clone() {
            /*SL:187*/return (ErrorInfo)super.clone();
        }
    }
}
