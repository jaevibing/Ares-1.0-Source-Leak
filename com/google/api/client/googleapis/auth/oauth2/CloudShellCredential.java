package com.google.api.client.googleapis.auth.oauth2;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.json.JsonFactory;

public class CloudShellCredential extends GoogleCredential
{
    private static final int ACCESS_TOKEN_INDEX = 2;
    private static final int READ_TIMEOUT_MS = 5000;
    protected static final String GET_AUTH_TOKEN_REQUEST = "2\n[]";
    private final int authPort;
    private final JsonFactory jsonFactory;
    
    public CloudShellCredential(final int a1, final JsonFactory a2) {
        this.authPort = a1;
        this.jsonFactory = a2;
    }
    
    protected int getAuthPort() {
        /*SL:73*/return this.authPort;
    }
    
    protected TokenResponse executeRefreshToken() throws IOException {
        final Socket socket = /*EL:79*/new Socket("localhost", this.getAuthPort());
        /*SL:80*/socket.setSoTimeout(5000);
        final TokenResponse v0 = /*EL:81*/new TokenResponse();
        try {
            final PrintWriter v = /*EL:83*/new PrintWriter(socket.getOutputStream(), true);
            /*SL:85*/v.println("2\n[]");
            final BufferedReader v2 = /*EL:87*/new BufferedReader(new InputStreamReader(socket.getInputStream()));
            /*SL:90*/v2.readLine();
            final Collection<Object> v3 = /*EL:92*/this.jsonFactory.createJsonParser(v2).<Object>parseArray(LinkedList.class, Object.class);
            final String v4 = /*EL:94*/((List)v3).get(2).toString();
            /*SL:95*/v0.setAccessToken(v4);
        }
        finally {
            /*SL:97*/socket.close();
        }
        /*SL:99*/return v0;
    }
}
