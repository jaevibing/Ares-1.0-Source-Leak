package org.json.simple;

import java.io.IOException;
import java.io.Writer;

public interface JSONStreamAware
{
    void writeJSONString(Writer p0) throws IOException;
}
