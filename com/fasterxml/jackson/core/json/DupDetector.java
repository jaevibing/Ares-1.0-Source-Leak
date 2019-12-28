package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import java.util.HashSet;

public class DupDetector
{
    protected final Object _source;
    protected String _firstName;
    protected String _secondName;
    protected HashSet<String> _seen;
    
    private DupDetector(final Object a1) {
        this._source = a1;
    }
    
    public static DupDetector rootDetector(final JsonParser a1) {
        /*SL:41*/return new DupDetector(a1);
    }
    
    public static DupDetector rootDetector(final JsonGenerator a1) {
        /*SL:45*/return new DupDetector(a1);
    }
    
    public DupDetector child() {
        /*SL:49*/return new DupDetector(this._source);
    }
    
    public void reset() {
        /*SL:53*/this._firstName = null;
        /*SL:54*/this._secondName = null;
        /*SL:55*/this._seen = null;
    }
    
    public JsonLocation findLocation() {
        /*SL:60*/if (this._source instanceof JsonParser) {
            /*SL:61*/return ((JsonParser)this._source).getCurrentLocation();
        }
        /*SL:64*/return null;
    }
    
    public Object getSource() {
        /*SL:71*/return this._source;
    }
    
    public boolean isDup(final String a1) throws JsonParseException {
        /*SL:76*/if (this._firstName == null) {
            /*SL:77*/this._firstName = a1;
            /*SL:78*/return false;
        }
        /*SL:80*/if (a1.equals(this._firstName)) {
            /*SL:81*/return true;
        }
        /*SL:83*/if (this._secondName == null) {
            /*SL:84*/this._secondName = a1;
            /*SL:85*/return false;
        }
        /*SL:87*/if (a1.equals(this._secondName)) {
            /*SL:88*/return true;
        }
        /*SL:90*/if (this._seen == null) {
            /*SL:91*/(this._seen = new HashSet<String>(16)).add(/*EL:92*/this._firstName);
            /*SL:93*/this._seen.add(this._secondName);
        }
        /*SL:95*/return !this._seen.add(a1);
    }
}
