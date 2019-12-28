package com.google.api.client.auth.oauth;

import java.security.GeneralSecurityException;
import java.security.Signature;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.SecurityUtils;
import java.security.PrivateKey;
import com.google.api.client.util.Beta;

@Beta
public final class OAuthRsaSigner implements OAuthSigner
{
    public PrivateKey privateKey;
    
    @Override
    public String getSignatureMethod() {
        /*SL:44*/return "RSA-SHA1";
    }
    
    @Override
    public String computeSignature(final String a1) throws GeneralSecurityException {
        final Signature v1 = /*EL:48*/SecurityUtils.getSha1WithRsaSignatureAlgorithm();
        final byte[] v2 = /*EL:49*/StringUtils.getBytesUtf8(a1);
        /*SL:50*/return Base64.encodeBase64String(SecurityUtils.sign(v1, this.privateKey, v2));
    }
}
