package com.google.api.client.googleapis.testing.json;

import java.io.IOException;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Beta;

@Beta
public final class GoogleJsonResponseExceptionFactoryTesting
{
    public static GoogleJsonResponseException newMock(final JsonFactory a1, final int a2, final String a3) throws IOException {
        final MockLowLevelHttpResponse v1 = /*EL:59*/new MockLowLevelHttpResponse().setStatusCode(a2).setReasonPhrase(a3);
        final MockHttpTransport v2 = /*EL:63*/new MockHttpTransport.Builder().setLowLevelHttpResponse(v1).build();
        final HttpRequest v3 = /*EL:66*/v2.createRequestFactory().buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
        /*SL:68*/v3.setThrowExceptionOnExecuteError(false);
        final HttpResponse v4 = /*EL:69*/v3.execute();
        /*SL:70*/return GoogleJsonResponseException.from(a1, v4);
    }
}
