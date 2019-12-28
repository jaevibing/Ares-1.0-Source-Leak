package com.google.api.client.http;

import java.io.IOException;
import com.google.api.client.util.Beta;

@Beta
public interface HttpIOExceptionHandler
{
    boolean handleIOException(HttpRequest p0, boolean p1) throws IOException;
}
