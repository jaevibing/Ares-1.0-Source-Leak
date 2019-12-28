package com.google.api.client.auth.oauth2;

import java.io.IOException;
import com.google.api.client.util.Beta;

@Deprecated
@Beta
public interface CredentialStore
{
    boolean load(String p0, Credential p1) throws IOException;
    
    void store(String p0, Credential p1) throws IOException;
    
    void delete(String p0, Credential p1) throws IOException;
}
