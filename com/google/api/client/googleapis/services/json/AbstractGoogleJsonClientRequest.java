package com.google.api.client.googleapis.services.json;

import com.google.api.client.util.GenericData;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpResponse;
import java.io.IOException;
import com.google.api.client.googleapis.batch.BatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonErrorContainer;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpContent;
import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;

public abstract class AbstractGoogleJsonClientRequest<T> extends AbstractGoogleClientRequest<T>
{
    private final Object jsonContent;
    
    protected AbstractGoogleJsonClientRequest(final AbstractGoogleJsonClient a1, final String a2, final String a3, final Object a4, final Class<T> a5) {
        super(a1, a2, a3, (a4 == null) ? null : new JsonHttpContent(a1.getJsonFactory(), a4).setWrapperKey(a1.getObjectParser().getWrapperKeys().isEmpty() ? null : "data"), a5);
        this.jsonContent = a4;
    }
    
    public AbstractGoogleJsonClient getAbstractGoogleClient() {
        /*SL:66*/return (AbstractGoogleJsonClient)super.getAbstractGoogleClient();
    }
    
    public AbstractGoogleJsonClientRequest<T> setDisableGZipContent(final boolean a1) {
        /*SL:71*/return (AbstractGoogleJsonClientRequest)super.setDisableGZipContent(a1);
    }
    
    public AbstractGoogleJsonClientRequest<T> setRequestHeaders(final HttpHeaders a1) {
        /*SL:76*/return (AbstractGoogleJsonClientRequest)super.setRequestHeaders(a1);
    }
    
    public final void queue(final BatchRequest a1, final JsonBatchCallback<T> a2) throws IOException {
        /*SL:108*/super.<GoogleJsonErrorContainer>queue(a1, GoogleJsonErrorContainer.class, a2);
    }
    
    protected GoogleJsonResponseException newExceptionOnError(final HttpResponse a1) {
        /*SL:113*/return GoogleJsonResponseException.from(this.getAbstractGoogleClient().getJsonFactory(), a1);
    }
    
    public Object getJsonContent() {
        /*SL:118*/return this.jsonContent;
    }
    
    public AbstractGoogleJsonClientRequest<T> set(final String a1, final Object a2) {
        /*SL:123*/return (AbstractGoogleJsonClientRequest)super.set(a1, a2);
    }
}
