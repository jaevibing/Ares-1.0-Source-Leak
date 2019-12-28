package com.google.api.services.sheets.v4;

import java.io.IOException;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.googleapis.services.json.CommonGoogleJsonClientRequestInitializer;

public class SheetsRequestInitializer extends CommonGoogleJsonClientRequestInitializer
{
    public SheetsRequestInitializer() {
    }
    
    public SheetsRequestInitializer(final String a1) {
        super(a1);
    }
    
    public SheetsRequestInitializer(final String a1, final String a2) {
        super(a1, a2);
    }
    
    public final void initializeJsonRequest(final AbstractGoogleJsonClientRequest<?> a1) throws IOException {
        /*SL:107*/super.initializeJsonRequest(a1);
        /*SL:108*/this.initializeSheetsRequest((SheetsRequest<?>)a1);
    }
    
    protected void initializeSheetsRequest(final SheetsRequest<?> sheetsRequest) throws IOException {
    }
}
