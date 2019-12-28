package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonStreamContext;

public final class JsonReadContext extends JsonStreamContext
{
    protected final JsonReadContext _parent;
    protected DupDetector _dups;
    protected JsonReadContext _child;
    protected String _currentName;
    protected Object _currentValue;
    protected int _lineNr;
    protected int _columnNr;
    
    public JsonReadContext(final JsonReadContext a1, final DupDetector a2, final int a3, final int a4, final int a5) {
        this._parent = a1;
        this._dups = a2;
        this._type = a3;
        this._lineNr = a4;
        this._columnNr = a5;
        this._index = -1;
    }
    
    protected void reset(final int a1, final int a2, final int a3) {
        /*SL:67*/this._type = a1;
        /*SL:68*/this._index = -1;
        /*SL:69*/this._lineNr = a2;
        /*SL:70*/this._columnNr = a3;
        /*SL:71*/this._currentName = null;
        /*SL:72*/this._currentValue = null;
        /*SL:73*/if (this._dups != null) {
            /*SL:74*/this._dups.reset();
        }
    }
    
    public JsonReadContext withDupDetector(final DupDetector a1) {
        /*SL:85*/this._dups = a1;
        /*SL:86*/return this;
    }
    
    @Override
    public Object getCurrentValue() {
        /*SL:91*/return this._currentValue;
    }
    
    @Override
    public void setCurrentValue(final Object a1) {
        /*SL:96*/this._currentValue = a1;
    }
    
    public static JsonReadContext createRootContext(final int a1, final int a2, final DupDetector a3) {
        /*SL:106*/return new JsonReadContext(null, a3, 0, a1, a2);
    }
    
    public static JsonReadContext createRootContext(final DupDetector a1) {
        /*SL:110*/return new JsonReadContext(null, a1, 0, 1, 0);
    }
    
    public JsonReadContext createChildArrayContext(final int a1, final int a2) {
        JsonReadContext v1 = /*EL:114*/this._child;
        /*SL:115*/if (v1 == null) {
            /*SL:116*/v1 = (this._child = new JsonReadContext(this, (this._dups == null) ? null : this._dups.child(), 1, a1, a2));
        }
        else {
            /*SL:119*/v1.reset(1, a1, a2);
        }
        /*SL:121*/return v1;
    }
    
    public JsonReadContext createChildObjectContext(final int a1, final int a2) {
        JsonReadContext v1 = /*EL:125*/this._child;
        /*SL:126*/if (v1 == null) {
            /*SL:127*/v1 = (this._child = new JsonReadContext(this, (this._dups == null) ? null : this._dups.child(), 2, a1, a2));
            /*SL:129*/return v1;
        }
        /*SL:131*/v1.reset(2, a1, a2);
        /*SL:132*/return v1;
    }
    
    @Override
    public String getCurrentName() {
        /*SL:141*/return this._currentName;
    }
    
    @Override
    public boolean hasCurrentName() {
        /*SL:144*/return this._currentName != null;
    }
    
    @Override
    public JsonReadContext getParent() {
        /*SL:146*/return this._parent;
    }
    
    @Override
    public JsonLocation getStartLocation(final Object a1) {
        final long v1 = /*EL:151*/-1L;
        /*SL:152*/return new JsonLocation(a1, v1, this._lineNr, this._columnNr);
    }
    
    public JsonReadContext clearAndGetParent() {
        /*SL:172*/this._currentValue = null;
        /*SL:174*/return this._parent;
    }
    
    public DupDetector getDupDetector() {
        /*SL:178*/return this._dups;
    }
    
    public boolean expectComma() {
        final int v1 = /*EL:192*/++this._index;
        /*SL:193*/return this._type != 0 && v1 > 0;
    }
    
    public void setCurrentName(final String a1) throws JsonProcessingException {
        /*SL:197*/this._currentName = a1;
        /*SL:198*/if (this._dups != null) {
            this._checkDup(this._dups, a1);
        }
    }
    
    private void _checkDup(final DupDetector v1, final String v2) throws JsonProcessingException {
        /*SL:202*/if (v1.isDup(v2)) {
            final Object a1 = /*EL:203*/v1.getSource();
            /*SL:204*/throw new JsonParseException((a1 instanceof JsonParser) ? ((JsonParser)a1) : null, "Duplicate field '" + v2 + "'");
        }
    }
}
