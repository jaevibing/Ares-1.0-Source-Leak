package com.google.api.client.googleapis.batch;

import java.io.IOException;
import com.google.api.client.http.HttpHeaders;

public interface BatchCallback<T, E>
{
    void onSuccess(T p0, HttpHeaders p1) throws IOException;
    
    void onFailure(E p0, HttpHeaders p1) throws IOException;
}
