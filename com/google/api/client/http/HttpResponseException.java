package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.IOException;

public class HttpResponseException extends IOException
{
    private static final long serialVersionUID = -1875819453475890043L;
    private final int statusCode;
    private final String statusMessage;
    private final transient HttpHeaders headers;
    private final String content;
    
    public HttpResponseException(final HttpResponse a1) {
        this(new Builder(a1));
    }
    
    protected HttpResponseException(final Builder a1) {
        super(a1.message);
        this.statusCode = a1.statusCode;
        this.statusMessage = a1.statusMessage;
        this.headers = a1.headers;
        this.content = a1.content;
    }
    
    public final boolean isSuccessStatusCode() {
        /*SL:91*/return HttpStatusCodes.isSuccess(this.statusCode);
    }
    
    public final int getStatusCode() {
        /*SL:100*/return this.statusCode;
    }
    
    public final String getStatusMessage() {
        /*SL:109*/return this.statusMessage;
    }
    
    public HttpHeaders getHeaders() {
        /*SL:118*/return this.headers;
    }
    
    public final String getContent() {
        /*SL:127*/return this.content;
    }
    
    public static StringBuilder computeMessageBuffer(final HttpResponse a1) {
        final StringBuilder v1 = /*EL:294*/new StringBuilder();
        final int v2 = /*EL:295*/a1.getStatusCode();
        /*SL:296*/if (v2 != 0) {
            /*SL:297*/v1.append(v2);
        }
        final String v3 = /*EL:299*/a1.getStatusMessage();
        /*SL:300*/if (v3 != null) {
            /*SL:301*/if (v2 != 0) {
                /*SL:302*/v1.append(' ');
            }
            /*SL:304*/v1.append(v3);
        }
        /*SL:306*/return v1;
    }
    
    public static class Builder
    {
        int statusCode;
        String statusMessage;
        HttpHeaders headers;
        String content;
        String message;
        
        public Builder(final int a1, final String a2, final HttpHeaders a3) {
            this.setStatusCode(a1);
            this.setStatusMessage(a2);
            this.setHeaders(a3);
        }
        
        public Builder(final HttpResponse v2) {
            this(v2.getStatusCode(), v2.getStatusMessage(), v2.getHeaders());
            try {
                this.content = v2.parseAsString();
                if (this.content.length() == 0) {
                    this.content = null;
                }
            }
            catch (IOException a1) {
                a1.printStackTrace();
            }
            final StringBuilder v3 = HttpResponseException.computeMessageBuffer(v2);
            if (this.content != null) {
                v3.append(StringUtils.LINE_SEPARATOR).append(this.content);
            }
            this.message = v3.toString();
        }
        
        public final String getMessage() {
            /*SL:193*/return this.message;
        }
        
        public Builder setMessage(final String a1) {
            /*SL:205*/this.message = a1;
            /*SL:206*/return this;
        }
        
        public final int getStatusCode() {
            /*SL:211*/return this.statusCode;
        }
        
        public Builder setStatusCode(final int a1) {
            /*SL:223*/Preconditions.checkArgument(a1 >= 0);
            /*SL:224*/this.statusCode = a1;
            /*SL:225*/return this;
        }
        
        public final String getStatusMessage() {
            /*SL:230*/return this.statusMessage;
        }
        
        public Builder setStatusMessage(final String a1) {
            /*SL:242*/this.statusMessage = a1;
            /*SL:243*/return this;
        }
        
        public HttpHeaders getHeaders() {
            /*SL:248*/return this.headers;
        }
        
        public Builder setHeaders(final HttpHeaders a1) {
            /*SL:260*/this.headers = Preconditions.<HttpHeaders>checkNotNull(a1);
            /*SL:261*/return this;
        }
        
        public final String getContent() {
            /*SL:266*/return this.content;
        }
        
        public Builder setContent(final String a1) {
            /*SL:278*/this.content = a1;
            /*SL:279*/return this;
        }
        
        public HttpResponseException build() {
            /*SL:284*/return new HttpResponseException(this);
        }
    }
}
