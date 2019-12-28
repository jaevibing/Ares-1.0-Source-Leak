package com.ares.api;

import java.util.List;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.security.GeneralSecurityException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.auth.oauth2.Credential;

class GoogleCloudApi
{
    private static String SPREADSHEET_ID;
    
    private static Credential getCredential() throws IOException {
        final InputStream v1 = /*EL:39*/GoogleCloudApi.class.getResourceAsStream("/resources/ares-client-340b78ae95c4.json");
        final Credential v2 = /*EL:41*/GoogleCredential.fromStream(v1).createScoped(/*EL:42*/Collections.<String>singleton("https://www.googleapis.com/auth/spreadsheets"));
        /*SL:43*/return v2;
    }
    
    private static Sheets getSheetsService() throws GeneralSecurityException, IOException {
        final Sheets v1 = /*EL:50*/new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), getCredential()).setApplicationName("Ares License Handler").build();
        /*SL:52*/return v1;
    }
    
    private static String getFirstdataFromRange(final String v1) throws IOException, GeneralSecurityException {
        final ValueRange v2 = getSheetsService().spreadsheets().values().get(GoogleCloudApi.SPREADSHEET_ID, /*EL:55*/v1).execute();
        /*SL:56*/for (int a1 = 0; a1 < v2.getValues().size(); ++a1) {
            System.out.println(/*EL:57*/v2.getValues().get(a1));
        }
        final String v3 = /*EL:59*/(v2.getValues() != null) ? v2.getValues().get(0).toString() : "";
        /*SL:60*/return v3;
    }
    
    private static List<List<Object>> getFullData(final String a1) throws IOException, GeneralSecurityException {
        final ValueRange v1 = getSheetsService().spreadsheets().values().get(GoogleCloudApi.SPREADSHEET_ID, /*EL:63*/a1).execute();
        /*SL:64*/return v1.getValues();
    }
    
    public static boolean foundToken(final String v1) throws IOException, GeneralSecurityException {
        final ValueRange v2 = getSheetsService().spreadsheets().values().get(GoogleCloudApi.SPREADSHEET_ID, /*EL:68*/"A2:A").execute();
        /*SL:69*/for (int a1 = 0; a1 < v2.getValues().size(); ++a1) {
            /*SL:70*/if (v2.getValues().get(a1).toString().replace("[", "").replace("]", "").equals(v1)) {
                return true;
            }
        }
        /*SL:72*/return false;
    }
    
    static {
        GoogleCloudApi.SPREADSHEET_ID = "1_kxn8nNafDEUPpKNZ6ozlUaASlODC_Sf9hIniJvH33E";
    }
}
