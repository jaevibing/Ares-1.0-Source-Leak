package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.net.SocketTimeoutException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import java.util.Iterator;
import java.util.Collection;
import com.google.api.client.http.HttpHeaders;
import java.util.logging.Logger;
import java.nio.charset.Charset;
import com.google.api.client.util.Beta;

@Beta
public class OAuth2Utils
{
    static final Charset UTF_8;
    private static final Logger LOGGER;
    private static final String DEFAULT_METADATA_SERVER_URL = "http://169.254.169.254";
    private static final int MAX_COMPUTE_PING_TRIES = 3;
    private static final int COMPUTE_PING_CONNECTION_TIMEOUT_MS = 500;
    
    static <T extends Throwable> T exceptionWithCause(final T a1, final Throwable a2) {
        /*SL:56*/a1.initCause(a2);
        /*SL:57*/return a1;
    }
    
    static boolean headersContainValue(final HttpHeaders v1, final String v2, final String v3) {
        final Object v4 = /*EL:61*/v1.get(v2);
        /*SL:62*/if (v4 instanceof Collection) {
            Collection<Object> a3 = /*EL:64*/(Collection<Object>)v4;
            final Iterator a2 = /*EL:65*/a3.iterator();
            while (a2.hasNext()) {
                a3 = a2.next();
                /*SL:66*/if (a3 instanceof String && ((String)a3).equals(v3)) {
                    /*SL:67*/return true;
                }
            }
        }
        /*SL:71*/return false;
    }
    
    static boolean runningOnComputeEngine(final HttpTransport v-4, final SystemEnvironmentProvider v-3) {
        /*SL:77*/if (Boolean.parseBoolean(v-3.getEnv("NO_GCE_CHECK"))) {
            /*SL:78*/return false;
        }
        final GenericUrl a3 = /*EL:81*/new GenericUrl(getMetadataServerUrl(v-3));
        /*SL:82*/for (int i = 1; i <= 3; ++i) {
            try {
                HttpRequest a2 = /*EL:84*/v-4.createRequestFactory().buildGetRequest(a3);
                /*SL:85*/a2.setConnectTimeout(500);
                final HttpResponse v1 = /*EL:86*/a2.execute();
                try {
                    /*SL:88*/a2 = v1.getHeaders();
                    /*SL:91*/return headersContainValue(a2, "Metadata-Flavor", "Google");
                }
                finally {
                    v1.disconnect();
                }
            }
            catch (SocketTimeoutException ex) {}
            catch (IOException v2) {
                OAuth2Utils.LOGGER.log(Level.WARNING, /*EL:96*/"Failed to detect whether we are running on Google Compute Engine.", v2);
            }
        }
        /*SL:102*/return false;
    }
    
    public static String getMetadataServerUrl() {
        /*SL:106*/return getMetadataServerUrl(SystemEnvironmentProvider.INSTANCE);
    }
    
    static String getMetadataServerUrl(final SystemEnvironmentProvider a1) {
        final String v1 = /*EL:110*/a1.getEnv("GCE_METADATA_HOST");
        /*SL:111*/if (v1 != null) {
            final String s = /*EL:112*/"http://";
            final String value = String.valueOf(v1);
            return (value.length() != 0) ? s.concat(value) : new String(s);
        }
        /*SL:114*/return "http://169.254.169.254";
    }
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        LOGGER = Logger.getLogger(OAuth2Utils.class.getName());
    }
}
