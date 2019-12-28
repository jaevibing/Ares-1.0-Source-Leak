package com.google.api.client.googleapis.batch.json;

import com.google.api.client.googleapis.json.GoogleJsonError;
import java.io.IOException;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.googleapis.json.GoogleJsonErrorContainer;
import com.google.api.client.googleapis.batch.BatchCallback;

public abstract class JsonBatchCallback<T> implements BatchCallback<T, GoogleJsonErrorContainer>
{
    public final void onFailure(final GoogleJsonErrorContainer a1, final HttpHeaders a2) throws IOException {
        /*SL:54*/this.onFailure(a1.getError(), a2);
    }
    
    public abstract void onFailure(final GoogleJsonError p0, final HttpHeaders p1) throws IOException;
}
