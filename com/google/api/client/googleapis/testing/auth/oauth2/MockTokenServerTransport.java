package com.google.api.client.googleapis.testing.auth.oauth2;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.webtoken.JsonWebSignature;
import java.io.IOException;
import com.google.api.client.googleapis.testing.TestUtils;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpRequest;
import java.util.HashMap;
import java.util.Map;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;
import com.google.api.client.testing.http.MockHttpTransport;

@Beta
public class MockTokenServerTransport extends MockHttpTransport
{
    static final String EXPECTED_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    static final JsonFactory JSON_FACTORY;
    final String tokenServerUrl;
    Map<String, String> serviceAccounts;
    Map<String, String> clients;
    Map<String, String> refreshTokens;
    
    public MockTokenServerTransport() {
        this("https://accounts.google.com/o/oauth2/token");
    }
    
    public MockTokenServerTransport(final String a1) {
        this.serviceAccounts = new HashMap<String, String>();
        this.clients = new HashMap<String, String>();
        this.refreshTokens = new HashMap<String, String>();
        this.tokenServerUrl = a1;
    }
    
    public void addServiceAccount(final String a1, final String a2) {
        /*SL:59*/this.serviceAccounts.put(a1, a2);
    }
    
    public void addClient(final String a1, final String a2) {
        /*SL:63*/this.clients.put(a1, a2);
    }
    
    public void addRefreshToken(final String a1, final String a2) {
        /*SL:67*/this.refreshTokens.put(a1, a2);
    }
    
    public LowLevelHttpRequest buildRequest(final String v1, final String v2) throws IOException {
        /*SL:72*/if (v2.equals(this.tokenServerUrl)) {
            final MockLowLevelHttpRequest a1 = /*EL:73*/new MockLowLevelHttpRequest(v2) {
                public LowLevelHttpResponse execute() throws IOException {
                    final String contentAsString = /*EL:76*/this.getContentAsString();
                    final Map<String, String> query = /*EL:77*/TestUtils.parseQuery(contentAsString);
                    String v9 = /*EL:78*/null;
                    final String v0 = /*EL:80*/query.get("client_id");
                    /*SL:81*/if (v0 != null) {
                        /*SL:82*/if (!MockTokenServerTransport.this.clients.containsKey(v0)) {
                            /*SL:83*/throw new IOException("Client ID not found.");
                        }
                        final String v = /*EL:85*/query.get("client_secret");
                        final String v2 = /*EL:86*/MockTokenServerTransport.this.clients.get(v0);
                        /*SL:87*/if (v == null || !v.equals(v2)) {
                            /*SL:88*/throw new IOException("Client secret not found.");
                        }
                        final String v3 = /*EL:90*/query.get("refresh_token");
                        /*SL:91*/if (!MockTokenServerTransport.this.refreshTokens.containsKey(v3)) {
                            /*SL:92*/throw new IOException("Refresh Token not found.");
                        }
                        /*SL:94*/v9 = MockTokenServerTransport.this.refreshTokens.get(v3);
                    }
                    else {
                        /*SL:95*/if (!query.containsKey("grant_type")) {
                            /*SL:112*/throw new IOException("Unknown token type.");
                        }
                        final String v = query.get("grant_type");
                        if (!"urn:ietf:params:oauth:grant-type:jwt-bearer".equals(v)) {
                            throw new IOException("Unexpected Grant Type.");
                        }
                        final String v2 = query.get("assertion");
                        final JsonWebSignature v4 = JsonWebSignature.parse(MockTokenServerTransport.JSON_FACTORY, v2);
                        final String v5 = v4.getPayload().getIssuer();
                        if (!MockTokenServerTransport.this.serviceAccounts.containsKey(v5)) {
                            throw new IOException("Service Account Email not found as issuer.");
                        }
                        v9 = MockTokenServerTransport.this.serviceAccounts.get(v5);
                        final String v6 = (String)v4.getPayload().get("scope");
                        if (v6 == null || v6.length() == 0) {
                            throw new IOException("Scopes not found.");
                        }
                    }
                    final GenericJson v7 = /*EL:116*/new GenericJson();
                    /*SL:117*/v7.setFactory(MockTokenServerTransport.JSON_FACTORY);
                    /*SL:118*/v7.put("access_token", v9);
                    /*SL:119*/v7.put("expires_in", 3600000);
                    /*SL:120*/v7.put("token_type", "Bearer");
                    final String v2 = /*EL:121*/v7.toPrettyString();
                    final MockLowLevelHttpResponse v8 = /*EL:123*/new MockLowLevelHttpResponse().setContentType("application/json; charset=UTF-8").setContent(v2);
                    /*SL:126*/return v8;
                }
            };
            /*SL:129*/return a1;
        }
        /*SL:131*/return super.buildRequest(v1, v2);
    }
    
    static {
        JSON_FACTORY = new JacksonFactory();
    }
}
