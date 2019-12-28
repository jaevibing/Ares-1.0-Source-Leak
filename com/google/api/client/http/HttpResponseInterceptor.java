package com.google.api.client.http;

import java.io.IOException;

public interface HttpResponseInterceptor
{
    void interceptResponse(HttpResponse p0) throws IOException;
}
