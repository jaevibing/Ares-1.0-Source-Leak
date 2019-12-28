package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.security.AccessControlException;
import java.util.Locale;
import java.io.File;
import java.io.IOException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;

@Beta
class DefaultCredentialProvider extends SystemEnvironmentProvider
{
    static final String CREDENTIAL_ENV_VAR = "GOOGLE_APPLICATION_CREDENTIALS";
    static final String WELL_KNOWN_CREDENTIALS_FILE = "application_default_credentials.json";
    static final String CLOUDSDK_CONFIG_DIRECTORY = "gcloud";
    static final String HELP_PERMALINK = "https://developers.google.com/accounts/docs/application-default-credentials";
    static final String APP_ENGINE_CREDENTIAL_CLASS = "com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential$AppEngineCredentialWrapper";
    static final String CLOUD_SHELL_ENV_VAR = "DEVSHELL_CLIENT_PORT";
    private GoogleCredential cachedCredential;
    private Environment detectedEnvironment;
    
    DefaultCredentialProvider() {
        this.cachedCredential = null;
        this.detectedEnvironment = null;
    }
    
    final GoogleCredential getDefaultCredential(final HttpTransport a1, final JsonFactory a2) throws IOException {
        /*SL:89*/synchronized (this) {
            /*SL:90*/if (this.cachedCredential == null) {
                /*SL:91*/this.cachedCredential = this.getDefaultCredentialUnsynchronized(a1, a2);
            }
            /*SL:93*/if (this.cachedCredential != null) {
                /*SL:94*/return this.cachedCredential;
            }
        }
        /*SL:98*/throw new IOException(String.format("The Application Default Credentials are not available. They are available if running on Google App Engine, Google Compute Engine, or Google Cloud Shell. Otherwise, the environment variable %s must be defined pointing to a file defining the credentials. See %s for more information.", "GOOGLE_APPLICATION_CREDENTIALS", "https://developers.google.com/accounts/docs/application-default-credentials"));
    }
    
    private final GoogleCredential getDefaultCredentialUnsynchronized(final HttpTransport a1, final JsonFactory a2) throws IOException {
        /*SL:109*/if (this.detectedEnvironment == null) {
            /*SL:110*/this.detectedEnvironment = this.detectEnvironment(a1);
        }
        /*SL:113*/switch (this.detectedEnvironment) {
            case ENVIRONMENT_VARIABLE: {
                /*SL:115*/return this.getCredentialUsingEnvironmentVariable(a1, a2);
            }
            case WELL_KNOWN_FILE: {
                /*SL:117*/return this.getCredentialUsingWellKnownFile(a1, a2);
            }
            case APP_ENGINE: {
                /*SL:119*/return this.getAppEngineCredential(a1, a2);
            }
            case CLOUD_SHELL: {
                /*SL:121*/return this.getCloudShellCredential(a2);
            }
            case COMPUTE_ENGINE: {
                /*SL:123*/return this.getComputeCredential(a1, a2);
            }
            default: {
                /*SL:125*/return null;
            }
        }
    }
    
    private final File getWellKnownCredentialsFile() {
        File file = /*EL:130*/null;
        final String v0 = /*EL:131*/this.getProperty("os.name", "").toLowerCase(Locale.US);
        /*SL:132*/if (v0.indexOf("windows") >= 0) {
            final File v = /*EL:133*/new File(this.getEnv("APPDATA"));
            /*SL:134*/file = new File(v, "gcloud");
        }
        else {
            final File v = /*EL:136*/new File(this.getProperty("user.home", ""), ".config");
            /*SL:137*/file = new File(v, "gcloud");
        }
        final File v = /*EL:139*/new File(file, "application_default_credentials.json");
        /*SL:140*/return v;
    }
    
    boolean fileExists(final File a1) {
        /*SL:147*/return a1.exists() && !a1.isDirectory();
    }
    
    String getProperty(final String a1, final String a2) {
        /*SL:154*/return System.getProperty(a1, a2);
    }
    
    Class<?> forName(final String a1) throws ClassNotFoundException {
        /*SL:161*/return Class.forName(a1);
    }
    
    private final Environment detectEnvironment(final HttpTransport a1) throws IOException {
        /*SL:166*/if (this.runningUsingEnvironmentVariable()) {
            /*SL:167*/return Environment.ENVIRONMENT_VARIABLE;
        }
        /*SL:170*/if (this.runningUsingWellKnownFile()) {
            /*SL:171*/return Environment.WELL_KNOWN_FILE;
        }
        /*SL:174*/if (this.runningOnAppEngine()) {
            /*SL:175*/return Environment.APP_ENGINE;
        }
        /*SL:179*/if (this.runningOnCloudShell()) {
            /*SL:180*/return Environment.CLOUD_SHELL;
        }
        /*SL:183*/if (OAuth2Utils.runningOnComputeEngine(a1, this)) {
            /*SL:184*/return Environment.COMPUTE_ENGINE;
        }
        /*SL:187*/return Environment.UNKNOWN;
    }
    
    private boolean runningUsingEnvironmentVariable() throws IOException {
        final String v0 = /*EL:191*/this.getEnv("GOOGLE_APPLICATION_CREDENTIALS");
        /*SL:192*/if (v0 == null || v0.length() == 0) {
            /*SL:193*/return false;
        }
        try {
            final File v = /*EL:197*/new File(v0);
            /*SL:198*/if (!v.exists() || v.isDirectory()) {
                /*SL:199*/throw new IOException(String.format("Error reading credential file from environment variable %s, value '%s': File does not exist.", "GOOGLE_APPLICATION_CREDENTIALS", v0));
            }
            /*SL:204*/return true;
        }
        catch (AccessControlException v2) {
            /*SL:207*/return false;
        }
    }
    
    private GoogleCredential getCredentialUsingEnvironmentVariable(final HttpTransport v1, final JsonFactory v2) throws IOException {
        final String v3 = /*EL:213*/this.getEnv("GOOGLE_APPLICATION_CREDENTIALS");
        InputStream v4 = /*EL:215*/null;
        try {
            /*SL:217*/v4 = new FileInputStream(v3);
            /*SL:228*/return GoogleCredential.fromStream(v4, v1, v2);
        }
        catch (IOException a1) {
            throw OAuth2Utils.<IOException>exceptionWithCause(new IOException(String.format("Error reading credential file from environment variable %s, value '%s': %s", "GOOGLE_APPLICATION_CREDENTIALS", v3, a1.getMessage())), a1);
        }
        finally {
            if (v4 != null) {
                v4.close();
            }
        }
    }
    
    private boolean runningUsingWellKnownFile() {
        final File v0 = /*EL:234*/this.getWellKnownCredentialsFile();
        try {
            /*SL:236*/return this.fileExists(v0);
        }
        catch (AccessControlException v) {
            /*SL:239*/return false;
        }
    }
    
    private GoogleCredential getCredentialUsingWellKnownFile(final HttpTransport v1, final JsonFactory v2) throws IOException {
        final File v3 = /*EL:245*/this.getWellKnownCredentialsFile();
        InputStream v4 = /*EL:246*/null;
        try {
            /*SL:248*/v4 = new FileInputStream(v3);
            /*SL:256*/return GoogleCredential.fromStream(v4, v1, v2);
        }
        catch (IOException a1) {
            throw new IOException(String.format("Error reading credential file from location %s: %s", v3, a1.getMessage()));
        }
        finally {
            if (v4 != null) {
                v4.close();
            }
        }
    }
    
    private boolean runningOnAppEngine() {
        Class<?> v0 = /*EL:262*/null;
        try {
            /*SL:264*/v0 = this.forName("com.google.appengine.api.utils.SystemProperty");
        }
        catch (ClassNotFoundException v13) {
            /*SL:267*/return false;
        }
        Exception v = /*EL:269*/null;
        try {
            final Field v2 = /*EL:272*/v0.getField("environment");
            final Object v3 = /*EL:273*/v2.get(null);
            final Class<?> v4 = /*EL:274*/v2.getType();
            final Method v5 = /*EL:275*/v4.getMethod("value", (Class<?>[])new Class[0]);
            final Object v6 = /*EL:276*/v5.invoke(v3, new Object[0]);
            /*SL:277*/return v6 != null;
        }
        catch (NoSuchFieldException v7) {
            /*SL:279*/v = v7;
        }
        catch (SecurityException v8) {
            /*SL:281*/v = v8;
        }
        catch (IllegalArgumentException v9) {
            /*SL:283*/v = v9;
        }
        catch (IllegalAccessException v10) {
            /*SL:285*/v = v10;
        }
        catch (NoSuchMethodException v11) {
            /*SL:287*/v = v11;
        }
        catch (InvocationTargetException v12) {
            /*SL:289*/v = v12;
        }
        /*SL:291*/throw OAuth2Utils.<RuntimeException>exceptionWithCause(new RuntimeException(String.format("Unexpcted error trying to determine if runnning on Google App Engine: %s", v.getMessage())), v);
    }
    
    private final GoogleCredential getAppEngineCredential(final HttpTransport v-2, final JsonFactory v-1) throws IOException {
        Exception v0 = /*EL:298*/null;
        try {
            final Class<?> a1 = /*EL:300*/this.forName("com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential$AppEngineCredentialWrapper");
            final Constructor<?> a2 = /*EL:301*/a1.getConstructor(HttpTransport.class, JsonFactory.class);
            /*SL:303*/return (GoogleCredential)a2.newInstance(v-2, v-1);
        }
        catch (ClassNotFoundException v) {
            /*SL:305*/v0 = v;
        }
        catch (NoSuchMethodException v2) {
            /*SL:307*/v0 = v2;
        }
        catch (InstantiationException v3) {
            /*SL:309*/v0 = v3;
        }
        catch (IllegalAccessException v4) {
            /*SL:311*/v0 = v4;
        }
        catch (InvocationTargetException v5) {
            /*SL:313*/v0 = v5;
        }
        /*SL:315*/throw OAuth2Utils.<IOException>exceptionWithCause(new IOException(String.format("Application Default Credentials failed to create the Google App Engine service account credentials class %s. Check that the component 'google-api-client-appengine' is deployed.", "com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential$AppEngineCredentialWrapper")), v0);
    }
    
    private boolean runningOnCloudShell() {
        /*SL:324*/return this.getEnv("DEVSHELL_CLIENT_PORT") != null;
    }
    
    private GoogleCredential getCloudShellCredential(final JsonFactory a1) {
        final String v1 = /*EL:328*/this.getEnv("DEVSHELL_CLIENT_PORT");
        /*SL:329*/return new CloudShellCredential(Integer.parseInt(v1), a1);
    }
    
    private final GoogleCredential getComputeCredential(final HttpTransport a1, final JsonFactory a2) {
        /*SL:334*/return new ComputeGoogleCredential(a1, a2);
    }
    
    private enum Environment
    {
        UNKNOWN, 
        ENVIRONMENT_VARIABLE, 
        WELL_KNOWN_FILE, 
        CLOUD_SHELL, 
        APP_ENGINE, 
        COMPUTE_ENGINE;
    }
    
    private static class ComputeGoogleCredential extends GoogleCredential
    {
        private static final String TOKEN_SERVER_ENCODED_URL;
        
        ComputeGoogleCredential(final HttpTransport a1, final JsonFactory a2) {
            super(new Builder().setTransport(a1).setJsonFactory(a2).setTokenServerEncodedUrl(ComputeGoogleCredential.TOKEN_SERVER_ENCODED_URL));
        }
        
        protected TokenResponse executeRefreshToken() throws IOException {
            final GenericUrl a1 = /*EL:352*/new GenericUrl(this.getTokenServerEncodedUrl());
            final HttpRequest buildGetRequest = /*EL:353*/this.getTransport().createRequestFactory().buildGetRequest(a1);
            final JsonObjectParser parser = /*EL:354*/new JsonObjectParser(this.getJsonFactory());
            /*SL:355*/buildGetRequest.setParser(parser);
            /*SL:356*/buildGetRequest.getHeaders().set("Metadata-Flavor", "Google");
            /*SL:357*/buildGetRequest.setThrowExceptionOnExecuteError(false);
            final HttpResponse execute = /*EL:358*/buildGetRequest.execute();
            final int v0 = /*EL:359*/execute.getStatusCode();
            /*SL:360*/if (v0 == 200) {
                final InputStream v = /*EL:361*/execute.getContent();
                /*SL:362*/if (v == null) {
                    /*SL:365*/throw new IOException("Empty content from metadata token server request.");
                }
                /*SL:367*/return parser.<TokenResponse>parseAndClose(v, execute.getContentCharset(), TokenResponse.class);
            }
            else {
                /*SL:369*/if (v0 == 404) {
                    /*SL:370*/throw new IOException(String.format("Error code %s trying to get security access token from Compute Engine metadata for the default service account. This may be because the virtual machine instance does not have permission scopes specified.", v0));
                }
                /*SL:375*/throw new IOException(String.format("Unexpected Error code %s trying to get security access token from Compute Engine metadata for the default service account: %s", v0, execute.parseAsString()));
            }
        }
        
        static {
            TOKEN_SERVER_ENCODED_URL = String.valueOf(OAuth2Utils.getMetadataServerUrl()).concat("/computeMetadata/v1/instance/service-accounts/default/token");
        }
    }
}
