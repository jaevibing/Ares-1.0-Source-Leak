package com.google.api.client.http;

import java.io.IOException;

public interface HttpUnsuccessfulResponseHandler
{
    boolean handleResponse(HttpRequest p0, HttpResponse p1, boolean p2) throws IOException;
}
