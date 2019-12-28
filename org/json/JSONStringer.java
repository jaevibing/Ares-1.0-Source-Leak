package org.json;

import java.io.StringWriter;

public class JSONStringer extends JSONWriter
{
    public JSONStringer() {
        super(new StringWriter());
    }
    
    @Override
    public String toString() {
        /*SL:77*/return (this.mode == 'd') ? this.writer.toString() : null;
    }
}
