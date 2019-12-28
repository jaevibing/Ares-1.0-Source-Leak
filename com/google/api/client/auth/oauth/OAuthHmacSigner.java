package com.google.api.client.auth.oauth;

import java.security.GeneralSecurityException;
import javax.crypto.SecretKey;
import com.google.api.client.util.Base64;
import java.security.Key;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Beta;

@Beta
public final class OAuthHmacSigner implements OAuthSigner
{
    public String clientSharedSecret;
    public String tokenSharedSecret;
    
    @Override
    public String getSignatureMethod() {
        /*SL:44*/return "HMAC-SHA1";
    }
    
    @Override
    public String computeSignature(final String a1) throws GeneralSecurityException {
        final StringBuilder v1 = /*EL:49*/new StringBuilder();
        final String v2 = /*EL:50*/this.clientSharedSecret;
        /*SL:51*/if (v2 != null) {
            /*SL:52*/v1.append(OAuthParameters.escape(v2));
        }
        /*SL:54*/v1.append('&');
        final String v3 = /*EL:55*/this.tokenSharedSecret;
        /*SL:56*/if (v3 != null) {
            /*SL:57*/v1.append(OAuthParameters.escape(v3));
        }
        final String v4 = /*EL:59*/v1.toString();
        final SecretKey v5 = /*EL:61*/new SecretKeySpec(StringUtils.getBytesUtf8(v4), "HmacSHA1");
        final Mac v6 = /*EL:62*/Mac.getInstance("HmacSHA1");
        /*SL:63*/v6.init(v5);
        /*SL:64*/return Base64.encodeBase64String(v6.doFinal(StringUtils.getBytesUtf8(a1)));
    }
}
