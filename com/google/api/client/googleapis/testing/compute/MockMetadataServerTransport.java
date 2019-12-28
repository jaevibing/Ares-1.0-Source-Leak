package com.google.api.client.googleapis.testing.compute;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.OAuth2Utils;
import com.google.api.client.json.GenericJson;
import java.io.IOException;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;
import com.google.api.client.testing.http.MockHttpTransport;

@Beta
public class MockMetadataServerTransport extends MockHttpTransport
{
    private static final String METADATA_SERVER_URL;
    private static final String METADATA_TOKEN_SERVER_URL;
    static final JsonFactory JSON_FACTORY;
    String accessToken;
    Integer tokenRequestStatusCode;
    
    public MockMetadataServerTransport(final String a1) {
        this.accessToken = a1;
    }
    
    public void setTokenRequestStatusCode(final Integer a1) {
        /*SL:56*/this.tokenRequestStatusCode = a1;
    }
    
    public LowLevelHttpRequest buildRequest(final String v2, final String v3) throws IOException {
        /*SL:61*/if (v3.equals(MockMetadataServerTransport.METADATA_TOKEN_SERVER_URL)) {
            final MockLowLevelHttpRequest a1 = /*EL:63*/new MockLowLevelHttpRequest(v3) {
                public LowLevelHttpResponse execute() throws IOException {
                    /*SL:67*/if (MockMetadataServerTransport.this.tokenRequestStatusCode != null) {
                        final MockLowLevelHttpResponse v1 = /*EL:68*/new MockLowLevelHttpResponse().setStatusCode(MockMetadataServerTransport.this.tokenRequestStatusCode).setContent("Token Fetch Error");
                        /*SL:71*/return v1;
                    }
                    final String v2 = /*EL:74*/this.getFirstHeaderValue("Metadata-Flavor");
                    /*SL:75*/if (!"Google".equals(v2)) {
                        /*SL:76*/throw new IOException("Metadata request header not found.");
                    }
                    final GenericJson v3 = /*EL:80*/new GenericJson();
                    /*SL:81*/v3.setFactory(MockMetadataServerTransport.JSON_FACTORY);
                    /*SL:82*/v3.put("access_token", MockMetadataServerTransport.this.accessToken);
                    /*SL:83*/v3.put("expires_in", 3600000);
                    /*SL:84*/v3.put("token_type", "Bearer");
                    final String v4 = /*EL:85*/v3.toPrettyString();
                    final MockLowLevelHttpResponse v5 = /*EL:87*/new MockLowLevelHttpResponse().setContentType("application/json; charset=UTF-8").setContent(v4);
                    /*SL:90*/return v5;
                }
            };
            /*SL:94*/return a1;
        }
        /*SL:95*/if (v3.equals(MockMetadataServerTransport.METADATA_SERVER_URL)) {
            final MockLowLevelHttpRequest a2 = /*EL:96*/new MockLowLevelHttpRequest(v3) {
                public LowLevelHttpResponse execute() {
                    final MockLowLevelHttpResponse v1 = /*EL:99*/new MockLowLevelHttpResponse();
                    /*SL:100*/v1.addHeader("Metadata-Flavor", "Google");
                    /*SL:101*/return v1;
                }
            };
            /*SL:104*/return a2;
        }
        /*SL:106*/return super.buildRequest(v2, v3);
    }
    
    static {
        METADATA_SERVER_URL = OAuth2Utils.getMetadataServerUrl();
        METADATA_TOKEN_SERVER_URL = String.valueOf(MockMetadataServerTransport.METADATA_SERVER_URL).concat("/computeMetadata/v1/instance/service-accounts/default/token");
        JSON_FACTORY = new JacksonFactory();
    }
}
