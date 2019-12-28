package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.JsonParser;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Strings;
import java.io.IOException;
import com.google.api.client.json.JsonToken;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.util.Preconditions;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpResponseException;

public class GoogleJsonResponseException extends HttpResponseException
{
    private static final long serialVersionUID = 409811126989994864L;
    private final transient GoogleJsonError details;
    
    public GoogleJsonResponseException(final Builder a1, final GoogleJsonError a2) {
        super(a1);
        this.details = a2;
    }
    
    public final GoogleJsonError getDetails() {
        /*SL:77*/return this.details;
    }
    
    public static GoogleJsonResponseException from(final JsonFactory v-4, final HttpResponse v-3) {
        final Builder a3 = /*EL:94*/new Builder(v-3.getStatusCode(), v-3.getStatusMessage(), v-3.getHeaders());
        /*SL:97*/Preconditions.<JsonFactory>checkNotNull(v-4);
        GoogleJsonError a4 = /*EL:98*/null;
        String v0 = /*EL:99*/null;
        try {
            /*SL:101*/if (!v-3.isSuccessStatusCode() && HttpMediaType.equalsIgnoreParameters("application/json; charset=UTF-8", v-3.getContentType()) && v-3.getContent() != null) {
                JsonParser v = /*EL:104*/null;
                try {
                    /*SL:106*/v = v-4.createJsonParser(v-3.getContent());
                    JsonToken a1 = /*EL:107*/v.getCurrentToken();
                    /*SL:109*/if (a1 == null) {
                        /*SL:110*/a1 = v.nextToken();
                    }
                    /*SL:113*/if (a1 != null) {
                        /*SL:115*/v.skipToKey("error");
                        /*SL:116*/if (v.getCurrentToken() != JsonToken.END_OBJECT) {
                            /*SL:117*/a4 = v.<GoogleJsonError>parseAndClose(GoogleJsonError.class);
                            /*SL:118*/v0 = a4.toPrettyString();
                        }
                    }
                }
                catch (IOException a2) {
                    /*SL:123*/a2.printStackTrace();
                }
                finally {
                    /*SL:125*/if (v == null) {
                        /*SL:126*/v-3.ignore();
                    }
                    else/*SL:127*/ if (a4 == null) {
                        /*SL:128*/v.close();
                    }
                }
            }
            else {
                /*SL:132*/v0 = v-3.parseAsString();
            }
        }
        catch (IOException v2) {
            /*SL:136*/v2.printStackTrace();
        }
        final StringBuilder v3 = /*EL:139*/HttpResponseException.computeMessageBuffer(v-3);
        /*SL:140*/if (!Strings.isNullOrEmpty(v0)) {
            /*SL:141*/v3.append(StringUtils.LINE_SEPARATOR).append(v0);
            /*SL:142*/a3.setContent(v0);
        }
        /*SL:144*/a3.setMessage(v3.toString());
        /*SL:146*/return new GoogleJsonResponseException(a3, a4);
    }
    
    public static HttpResponse execute(final JsonFactory a1, final HttpRequest a2) throws GoogleJsonResponseException, IOException {
        /*SL:179*/Preconditions.<JsonFactory>checkNotNull(a1);
        final boolean v1 = /*EL:180*/a2.getThrowExceptionOnExecuteError();
        /*SL:181*/if (v1) {
            /*SL:182*/a2.setThrowExceptionOnExecuteError(false);
        }
        final HttpResponse v2 = /*EL:184*/a2.execute();
        /*SL:185*/a2.setThrowExceptionOnExecuteError(v1);
        /*SL:186*/if (!v1 || v2.isSuccessStatusCode()) {
            /*SL:187*/return v2;
        }
        throw from(/*EL:189*/a1, v2);
    }
}
