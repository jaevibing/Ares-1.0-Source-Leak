package com.google.api.client.extensions.java6.auth.oauth2;

import java.io.PrintStream;
import java.util.logging.Level;
import java.net.URI;
import java.awt.Desktop;
import java.io.IOException;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Preconditions;
import java.util.logging.Logger;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;

public class AuthorizationCodeInstalledApp
{
    private final AuthorizationCodeFlow flow;
    private final VerificationCodeReceiver receiver;
    private static final Logger LOGGER;
    
    public AuthorizationCodeInstalledApp(final AuthorizationCodeFlow a1, final VerificationCodeReceiver a2) {
        this.flow = Preconditions.<AuthorizationCodeFlow>checkNotNull(a1);
        this.receiver = Preconditions.<VerificationCodeReceiver>checkNotNull(a2);
    }
    
    public Credential authorize(final String v-1) throws IOException {
        try {
            final Credential a1 = /*EL:70*/this.flow.loadCredential(v-1);
            /*SL:71*/if (a1 != null && (a1.getRefreshToken() != null || a1.getExpiresInSeconds() == null || a1.getExpiresInSeconds() > 60L)) {
                /*SL:88*/return a1;
            }
            final String v1 = this.receiver.getRedirectUri();
            final AuthorizationCodeRequestUrl v2 = this.flow.newAuthorizationUrl().setRedirectUri(v1);
            this.onAuthorization(v2);
            final String v3 = this.receiver.waitForCode();
            final TokenResponse v4 = this.flow.newTokenRequest(v3).setRedirectUri(v1).execute();
            return this.flow.createAndStoreCredential(v4, v-1);
        }
        finally {
            this.receiver.stop();
        }
    }
    
    protected void onAuthorization(final AuthorizationCodeRequestUrl a1) throws IOException {
        browse(/*EL:113*/a1.build());
    }
    
    public static void browse(final String v0) {
        /*SL:123*/Preconditions.<String>checkNotNull(v0);
        System.out.println(/*EL:125*/"Please open the following address in your browser:");
        final PrintStream out = System.out;
        final String s = /*EL:126*/"  ";
        final String value = String.valueOf(v0);
        out.println((value.length() != 0) ? s.concat(value) : new String(s));
        try {
            /*SL:129*/if (Desktop.isDesktopSupported()) {
                final Desktop a1 = /*EL:130*/Desktop.getDesktop();
                /*SL:131*/if (a1.isSupported(Desktop.Action.BROWSE)) {
                    System.out.println(/*EL:132*/"Attempting to open that address in the default browser now...");
                    /*SL:133*/a1.browse(URI.create(v0));
                }
            }
        }
        catch (IOException v) {
            AuthorizationCodeInstalledApp.LOGGER.log(Level.WARNING, /*EL:137*/"Unable to open browser", v);
        }
        catch (InternalError v2) {
            AuthorizationCodeInstalledApp.LOGGER.log(Level.WARNING, /*EL:143*/"Unable to open browser", v2);
        }
    }
    
    public final AuthorizationCodeFlow getFlow() {
        /*SL:149*/return this.flow;
    }
    
    public final VerificationCodeReceiver getReceiver() {
        /*SL:154*/return this.receiver;
    }
    
    static {
        LOGGER = Logger.getLogger(AuthorizationCodeInstalledApp.class.getName());
    }
}
