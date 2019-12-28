package com.google.api.client.auth.oauth2;

import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Strings;
import java.io.IOException;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpResponseException;

public class TokenResponseException extends HttpResponseException
{
    private static final long serialVersionUID = 4020689092957439244L;
    private final transient TokenErrorResponse details;
    
    TokenResponseException(final Builder a1, final TokenErrorResponse a2) {
        super(a1);
        this.details = a2;
    }
    
    public final TokenErrorResponse getDetails() {
        /*SL:61*/return this.details;
    }
    
    public static TokenResponseException from(final JsonFactory a2, final HttpResponse v1) {
        final Builder v2 = /*EL:79*/new Builder(v1.getStatusCode(), v1.getStatusMessage(), v1.getHeaders());
        /*SL:81*/Preconditions.<JsonFactory>checkNotNull(a2);
        TokenErrorResponse v3 = /*EL:82*/null;
        String v4 = /*EL:83*/null;
        final String v5 = /*EL:84*/v1.getContentType();
        try {
            /*SL:86*/if (!v1.isSuccessStatusCode() && v5 != null && v1.getContent() != null && /*EL:87*/HttpMediaType.equalsIgnoreParameters("application/json; charset=UTF-8", v5)) {
                /*SL:88*/v3 = new JsonObjectParser(a2).<TokenErrorResponse>parseAndClose(v1.getContent(), /*EL:89*/v1.getContentCharset(), TokenErrorResponse.class);
                /*SL:90*/v4 = v3.toPrettyString();
            }
            else {
                /*SL:92*/v4 = v1.parseAsString();
            }
        }
        catch (IOException a3) {
            /*SL:96*/a3.printStackTrace();
        }
        final StringBuilder v6 = /*EL:99*/HttpResponseException.computeMessageBuffer(v1);
        /*SL:100*/if (!Strings.isNullOrEmpty(v4)) {
            /*SL:101*/v6.append(StringUtils.LINE_SEPARATOR).append(v4);
            /*SL:102*/v2.setContent(v4);
        }
        /*SL:104*/v2.setMessage(v6.toString());
        /*SL:105*/return new TokenResponseException(v2, v3);
    }
}
