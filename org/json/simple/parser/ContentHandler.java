package org.json.simple.parser;

import java.io.IOException;

public interface ContentHandler
{
    void startJSON() throws ParseException, IOException;
    
    void endJSON() throws ParseException, IOException;
    
    boolean startObject() throws ParseException, IOException;
    
    boolean endObject() throws ParseException, IOException;
    
    boolean startObjectEntry(String p0) throws ParseException, IOException;
    
    boolean endObjectEntry() throws ParseException, IOException;
    
    boolean startArray() throws ParseException, IOException;
    
    boolean endArray() throws ParseException, IOException;
    
    boolean primitive(Object p0) throws ParseException, IOException;
}
