package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.http.HttpRequestInitializer;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.File;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.util.Clock;
import com.google.api.client.auth.oauth2.BearerToken;
import java.security.KeyFactory;
import java.io.Reader;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import com.google.api.client.util.SecurityUtils;
import java.security.spec.PKCS8EncodedKeySpec;
import com.google.api.client.util.PemReader;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Joiner;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.auth.oauth2.TokenResponse;
import java.util.Collections;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import java.io.InputStream;
import com.google.api.client.util.Preconditions;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import java.io.IOException;
import com.google.api.client.googleapis.util.Utils;
import java.security.PrivateKey;
import java.util.Collection;
import com.google.api.client.util.Beta;
import com.google.api.client.auth.oauth2.Credential;

public class GoogleCredential extends Credential
{
    static final String USER_FILE_TYPE = "authorized_user";
    static final String SERVICE_ACCOUNT_FILE_TYPE = "service_account";
    @Beta
    private static DefaultCredentialProvider defaultCredentialProvider;
    private String serviceAccountId;
    private String serviceAccountProjectId;
    private Collection<String> serviceAccountScopes;
    private PrivateKey serviceAccountPrivateKey;
    private String serviceAccountPrivateKeyId;
    private String serviceAccountUser;
    
    @Beta
    public static GoogleCredential getApplicationDefault() throws IOException {
        /*SL:191*/return getApplicationDefault(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory());
    }
    
    @Beta
    public static GoogleCredential getApplicationDefault(final HttpTransport a1, final JsonFactory a2) throws IOException {
        /*SL:211*/Preconditions.<HttpTransport>checkNotNull(a1);
        /*SL:212*/Preconditions.<JsonFactory>checkNotNull(a2);
        /*SL:213*/return GoogleCredential.defaultCredentialProvider.getDefaultCredential(a1, a2);
    }
    
    @Beta
    public static GoogleCredential fromStream(final InputStream a1) throws IOException {
        /*SL:226*/return fromStream(a1, Utils.getDefaultTransport(), Utils.getDefaultJsonFactory());
    }
    
    @Beta
    public static GoogleCredential fromStream(final InputStream a1, final HttpTransport a2, final JsonFactory a3) throws IOException {
        /*SL:245*/Preconditions.<InputStream>checkNotNull(a1);
        /*SL:246*/Preconditions.<HttpTransport>checkNotNull(a2);
        /*SL:247*/Preconditions.<JsonFactory>checkNotNull(a3);
        final JsonObjectParser v1 = /*EL:249*/new JsonObjectParser(a3);
        final GenericJson v2 = /*EL:250*/v1.<GenericJson>parseAndClose(a1, OAuth2Utils.UTF_8, GenericJson.class);
        final String v3 = /*EL:252*/(String)v2.get("type");
        /*SL:253*/if (v3 == null) {
            /*SL:254*/throw new IOException("Error reading credentials from stream, 'type' field not specified.");
        }
        /*SL:256*/if ("authorized_user".equals(v3)) {
            /*SL:257*/return fromStreamUser(v2, a2, a3);
        }
        /*SL:259*/if ("service_account".equals(v3)) {
            /*SL:260*/return fromStreamServiceAccount(v2, a2, a3);
        }
        /*SL:262*/throw new IOException(String.format("Error reading credentials from stream, 'type' value '%s' not recognized. Expecting '%s' or '%s'.", v3, "authorized_user", "service_account"));
    }
    
    public GoogleCredential() {
        this(new Builder());
    }
    
    protected GoogleCredential(final Builder a1) {
        super(a1);
        if (a1.serviceAccountPrivateKey == null) {
            Preconditions.checkArgument(a1.serviceAccountId == null && a1.serviceAccountScopes == null && a1.serviceAccountUser == null);
        }
        else {
            this.serviceAccountId = Preconditions.<String>checkNotNull(a1.serviceAccountId);
            this.serviceAccountProjectId = a1.serviceAccountProjectId;
            this.serviceAccountScopes = (Collection<String>)((a1.serviceAccountScopes == null) ? Collections.<Object>emptyList() : Collections.<Object>unmodifiableCollection((Collection<?>)a1.serviceAccountScopes));
            this.serviceAccountPrivateKey = a1.serviceAccountPrivateKey;
            this.serviceAccountPrivateKeyId = a1.serviceAccountPrivateKeyId;
            this.serviceAccountUser = a1.serviceAccountUser;
        }
    }
    
    public GoogleCredential setAccessToken(final String a1) {
        /*SL:340*/return (GoogleCredential)super.setAccessToken(a1);
    }
    
    public GoogleCredential setRefreshToken(final String a1) {
        /*SL:345*/if (a1 != null) {
            /*SL:346*/Preconditions.checkArgument(this.getJsonFactory() != null && this.getTransport() != null && this.getClientAuthentication() != null, (Object)"Please use the Builder and call setJsonFactory, setTransport and setClientSecrets");
        }
        /*SL:350*/return (GoogleCredential)super.setRefreshToken(a1);
    }
    
    public GoogleCredential setExpirationTimeMilliseconds(final Long a1) {
        /*SL:355*/return (GoogleCredential)super.setExpirationTimeMilliseconds(a1);
    }
    
    public GoogleCredential setExpiresInSeconds(final Long a1) {
        /*SL:360*/return (GoogleCredential)super.setExpiresInSeconds(a1);
    }
    
    public GoogleCredential setFromTokenResponse(final TokenResponse a1) {
        /*SL:365*/return (GoogleCredential)super.setFromTokenResponse(a1);
    }
    
    @Beta
    protected TokenResponse executeRefreshToken() throws IOException {
        /*SL:371*/if (this.serviceAccountPrivateKey == null) {
            /*SL:372*/return super.executeRefreshToken();
        }
        final JsonWebSignature.Header a3 = /*EL:375*/new JsonWebSignature.Header();
        /*SL:376*/a3.setAlgorithm("RS256");
        /*SL:377*/a3.setType("JWT");
        /*SL:378*/a3.setKeyId(this.serviceAccountPrivateKeyId);
        final JsonWebToken.Payload a2 = /*EL:379*/new JsonWebToken.Payload();
        final long currentTimeMillis = /*EL:380*/this.getClock().currentTimeMillis();
        /*SL:381*/a2.setIssuer(this.serviceAccountId);
        /*SL:382*/a2.setAudience(this.getTokenServerEncodedUrl());
        /*SL:383*/a2.setIssuedAtTimeSeconds(currentTimeMillis / 1000L);
        /*SL:384*/a2.setExpirationTimeSeconds(currentTimeMillis / 1000L + 3600L);
        /*SL:385*/a2.setSubject(this.serviceAccountUser);
        /*SL:386*/a2.put("scope", Joiner.on(' ').join(this.serviceAccountScopes));
        try {
            final String v1 = /*EL:388*/JsonWebSignature.signUsingRsaSha256(this.serviceAccountPrivateKey, this.getJsonFactory(), a3, a2);
            final TokenRequest v2 = /*EL:390*/new TokenRequest(this.getTransport(), this.getJsonFactory(), new GenericUrl(this.getTokenServerEncodedUrl()), "urn:ietf:params:oauth:grant-type:jwt-bearer");
            /*SL:393*/v2.put("assertion", v1);
            /*SL:394*/return v2.execute();
        }
        catch (GeneralSecurityException v4) {
            final IOException v3 = /*EL:396*/new IOException();
            /*SL:397*/v3.initCause(v4);
            /*SL:398*/throw v3;
        }
    }
    
    public final String getServiceAccountId() {
        /*SL:407*/return this.serviceAccountId;
    }
    
    public final String getServiceAccountProjectId() {
        /*SL:416*/return this.serviceAccountProjectId;
    }
    
    public final Collection<String> getServiceAccountScopes() {
        /*SL:424*/return this.serviceAccountScopes;
    }
    
    public final String getServiceAccountScopesAsString() {
        /*SL:434*/return (this.serviceAccountScopes == null) ? null : Joiner.on(' ').join(this.serviceAccountScopes);
    }
    
    public final PrivateKey getServiceAccountPrivateKey() {
        /*SL:442*/return this.serviceAccountPrivateKey;
    }
    
    @Beta
    public final String getServiceAccountPrivateKeyId() {
        /*SL:452*/return this.serviceAccountPrivateKeyId;
    }
    
    public final String getServiceAccountUser() {
        /*SL:460*/return this.serviceAccountUser;
    }
    
    @Beta
    public boolean createScopedRequired() {
        /*SL:470*/return this.serviceAccountPrivateKey != null && /*EL:473*/(this.serviceAccountScopes == null || this.serviceAccountScopes.isEmpty());
    }
    
    @Beta
    public GoogleCredential createScoped(final Collection<String> a1) {
        /*SL:483*/if (this.serviceAccountPrivateKey == null) {
            /*SL:484*/return this;
        }
        /*SL:486*/return new Builder().setServiceAccountPrivateKey(this.serviceAccountPrivateKey).setServiceAccountPrivateKeyId(this.serviceAccountPrivateKeyId).setServiceAccountId(this.serviceAccountId).setServiceAccountProjectId(this.serviceAccountProjectId).setServiceAccountUser(this.serviceAccountUser).setServiceAccountScopes(a1).setTokenServerEncodedUrl(this.getTokenServerEncodedUrl()).setTransport(this.getTransport()).setJsonFactory(this.getJsonFactory()).setClock(this.getClock()).build();
    }
    
    @Beta
    private static GoogleCredential fromStreamUser(final GenericJson a1, final HttpTransport a2, final JsonFactory a3) throws IOException {
        final String v1 = /*EL:799*/(String)a1.get("client_id");
        final String v2 = /*EL:800*/(String)a1.get("client_secret");
        final String v3 = /*EL:801*/(String)a1.get("refresh_token");
        /*SL:802*/if (v1 == null || v2 == null || v3 == null) {
            /*SL:803*/throw new IOException("Error reading user credential from stream,  expecting 'client_id', 'client_secret' and 'refresh_token'.");
        }
        final GoogleCredential v4 = /*EL:807*/new Builder().setClientSecrets(v1, v2).setTransport(a2).setJsonFactory(a3).build();
        /*SL:812*/v4.setRefreshToken(v3);
        /*SL:815*/v4.refreshToken();
        /*SL:816*/return v4;
    }
    
    @Beta
    private static GoogleCredential fromStreamServiceAccount(final GenericJson a1, final HttpTransport a2, final JsonFactory a3) throws IOException {
        final String v1 = /*EL:822*/(String)a1.get("client_id");
        final String v2 = /*EL:823*/(String)a1.get("client_email");
        final String v3 = /*EL:824*/(String)a1.get("private_key");
        final String v4 = /*EL:825*/(String)a1.get("private_key_id");
        /*SL:826*/if (v1 == null || v2 == null || v3 == null || v4 == null) {
            /*SL:828*/throw new IOException("Error reading service account credential from stream, expecting  'client_id', 'client_email', 'private_key' and 'private_key_id'.");
        }
        final PrivateKey v5 = privateKeyFromPkcs8(/*EL:832*/v3);
        final Collection<String> v6 = /*EL:834*/(Collection<String>)Collections.<Object>emptyList();
        final Builder v7 = /*EL:836*/new Builder().setTransport(a2).setJsonFactory(a3).setServiceAccountId(v2).setServiceAccountScopes(v6).setServiceAccountPrivateKey(v5).setServiceAccountPrivateKeyId(v4);
        final String v8 = /*EL:843*/(String)a1.get("token_uri");
        /*SL:844*/if (v8 != null) {
            /*SL:845*/v7.setTokenServerEncodedUrl(v8);
        }
        final String v9 = /*EL:847*/(String)a1.get("project_id");
        /*SL:848*/if (v9 != null) {
            /*SL:849*/v7.setServiceAccountProjectId(v9);
        }
        /*SL:853*/return v7.build();
    }
    
    @Beta
    private static PrivateKey privateKeyFromPkcs8(final String v-6) throws IOException {
        final Reader a2 = /*EL:858*/new StringReader(v-6);
        final PemReader.Section firstSectionAndClose = /*EL:859*/PemReader.readFirstSectionAndClose(a2, "PRIVATE KEY");
        /*SL:860*/if (firstSectionAndClose == null) {
            /*SL:861*/throw new IOException("Invalid PKCS8 data.");
        }
        final byte[] base64DecodedBytes = /*EL:863*/firstSectionAndClose.getBase64DecodedBytes();
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = /*EL:864*/new PKCS8EncodedKeySpec(base64DecodedBytes);
        Exception a3 = /*EL:865*/null;
        try {
            final KeyFactory a1 = /*EL:867*/SecurityUtils.getRsaKeyFactory();
            final PrivateKey v1 = /*EL:868*/a1.generatePrivate(pkcs8EncodedKeySpec);
            /*SL:869*/return v1;
        }
        catch (NoSuchAlgorithmException v2) {
            /*SL:871*/a3 = v2;
        }
        catch (InvalidKeySpecException v3) {
            /*SL:873*/a3 = v3;
        }
        /*SL:875*/throw OAuth2Utils.<IOException>exceptionWithCause(new IOException("Unexpected exception reading PKCS data"), a3);
    }
    
    static {
        GoogleCredential.defaultCredentialProvider = new DefaultCredentialProvider();
    }
    
    public static class Builder extends Credential.Builder
    {
        String serviceAccountId;
        Collection<String> serviceAccountScopes;
        PrivateKey serviceAccountPrivateKey;
        String serviceAccountPrivateKeyId;
        String serviceAccountProjectId;
        String serviceAccountUser;
        
        public Builder() {
            super(BearerToken.authorizationHeaderAccessMethod());
            this.setTokenServerEncodedUrl("https://accounts.google.com/o/oauth2/token");
        }
        
        public GoogleCredential build() {
            /*SL:539*/return new GoogleCredential(this);
        }
        
        public Builder setTransport(final HttpTransport a1) {
            /*SL:544*/return (Builder)super.setTransport(a1);
        }
        
        public Builder setJsonFactory(final JsonFactory a1) {
            /*SL:549*/return (Builder)super.setJsonFactory(a1);
        }
        
        public Builder setClock(final Clock a1) {
            /*SL:557*/return (Builder)super.setClock(a1);
        }
        
        public Builder setClientSecrets(final String a1, final String a2) {
            /*SL:569*/this.setClientAuthentication(new ClientParametersAuthentication(a1, a2));
            /*SL:570*/return this;
        }
        
        public Builder setClientSecrets(final GoogleClientSecrets a1) {
            final GoogleClientSecrets.Details v1 = /*EL:582*/a1.getDetails();
            /*SL:583*/this.setClientAuthentication(new ClientParametersAuthentication(v1.getClientId(), v1.getClientSecret()));
            /*SL:585*/return this;
        }
        
        public final String getServiceAccountId() {
            /*SL:592*/return this.serviceAccountId;
        }
        
        public Builder setServiceAccountId(final String a1) {
            /*SL:604*/this.serviceAccountId = a1;
            /*SL:605*/return this;
        }
        
        public final String getServiceAccountProjectId() {
            /*SL:612*/return this.serviceAccountProjectId;
        }
        
        public Builder setServiceAccountProjectId(final String a1) {
            /*SL:624*/this.serviceAccountProjectId = a1;
            /*SL:625*/return this;
        }
        
        public final Collection<String> getServiceAccountScopes() {
            /*SL:633*/return this.serviceAccountScopes;
        }
        
        public Builder setServiceAccountScopes(final Collection<String> a1) {
            /*SL:650*/this.serviceAccountScopes = a1;
            /*SL:651*/return this;
        }
        
        public final PrivateKey getServiceAccountPrivateKey() {
            /*SL:658*/return this.serviceAccountPrivateKey;
        }
        
        public Builder setServiceAccountPrivateKey(final PrivateKey a1) {
            /*SL:670*/this.serviceAccountPrivateKey = a1;
            /*SL:671*/return this;
        }
        
        @Beta
        public final String getServiceAccountPrivateKeyId() {
            /*SL:681*/return this.serviceAccountPrivateKeyId;
        }
        
        @Beta
        public Builder setServiceAccountPrivateKeyId(final String a1) {
            /*SL:696*/this.serviceAccountPrivateKeyId = a1;
            /*SL:697*/return this;
        }
        
        public Builder setServiceAccountPrivateKeyFromP12File(final File a1) throws GeneralSecurityException, IOException {
            /*SL:714*/this.serviceAccountPrivateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(), new FileInputStream(a1), "notasecret", "privatekey", "notasecret");
            /*SL:717*/return this;
        }
        
        @Beta
        public Builder setServiceAccountPrivateKeyFromPemFile(final File a1) throws GeneralSecurityException, IOException {
            final byte[] v1 = /*EL:736*/PemReader.readFirstSectionAndClose(new FileReader(a1), "PRIVATE KEY").getBase64DecodedBytes();
            /*SL:738*/this.serviceAccountPrivateKey = SecurityUtils.getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(v1));
            /*SL:740*/return this;
        }
        
        public final String getServiceAccountUser() {
            /*SL:748*/return this.serviceAccountUser;
        }
        
        public Builder setServiceAccountUser(final String a1) {
            /*SL:761*/this.serviceAccountUser = a1;
            /*SL:762*/return this;
        }
        
        public Builder setRequestInitializer(final HttpRequestInitializer a1) {
            /*SL:767*/return (Builder)super.setRequestInitializer(a1);
        }
        
        public Builder addRefreshListener(final CredentialRefreshListener a1) {
            /*SL:772*/return (Builder)super.addRefreshListener(a1);
        }
        
        public Builder setRefreshListeners(final Collection<CredentialRefreshListener> a1) {
            /*SL:777*/return (Builder)super.setRefreshListeners(a1);
        }
        
        public Builder setTokenServerUrl(final GenericUrl a1) {
            /*SL:782*/return (Builder)super.setTokenServerUrl(a1);
        }
        
        public Builder setTokenServerEncodedUrl(final String a1) {
            /*SL:787*/return (Builder)super.setTokenServerEncodedUrl(a1);
        }
        
        public Builder setClientAuthentication(final HttpExecuteInterceptor a1) {
            /*SL:792*/return (Builder)super.setClientAuthentication(a1);
        }
    }
}
