package javassist;

import java.net.URL;
import java.io.InputStream;

public interface ClassPath
{
    InputStream openClassfile(String p0) throws NotFoundException;
    
    URL find(String p0);
    
    void close();
}
