package com.fasterxml.jackson.core.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;

public class JsonWriteContext extends JsonStreamContext
{
    public static final int STATUS_OK_AS_IS = 0;
    public static final int STATUS_OK_AFTER_COMMA = 1;
    public static final int STATUS_OK_AFTER_COLON = 2;
    public static final int STATUS_OK_AFTER_SPACE = 3;
    public static final int STATUS_EXPECT_VALUE = 4;
    public static final int STATUS_EXPECT_NAME = 5;
    protected final JsonWriteContext _parent;
    protected DupDetector _dups;
    protected JsonWriteContext _child;
    protected String _currentName;
    protected Object _currentValue;
    protected boolean _gotName;
    
    protected JsonWriteContext(final int a1, final JsonWriteContext a2, final DupDetector a3) {
        this._type = a1;
        this._parent = a2;
        this._dups = a3;
        this._index = -1;
    }
    
    protected JsonWriteContext reset(final int a1) {
        /*SL:78*/this._type = a1;
        /*SL:79*/this._index = -1;
        /*SL:80*/this._currentName = null;
        /*SL:81*/this._gotName = false;
        /*SL:82*/this._currentValue = null;
        /*SL:83*/if (this._dups != null) {
            this._dups.reset();
        }
        /*SL:84*/return this;
    }
    
    public JsonWriteContext withDupDetector(final DupDetector a1) {
        /*SL:88*/this._dups = a1;
        /*SL:89*/return this;
    }
    
    @Override
    public Object getCurrentValue() {
        /*SL:94*/return this._currentValue;
    }
    
    @Override
    public void setCurrentValue(final Object a1) {
        /*SL:99*/this._currentValue = a1;
    }
    
    @Deprecated
    public static JsonWriteContext createRootContext() {
        /*SL:112*/return createRootContext(null);
    }
    
    public static JsonWriteContext createRootContext(final DupDetector a1) {
        /*SL:115*/return new JsonWriteContext(0, null, a1);
    }
    
    public JsonWriteContext createChildArrayContext() {
        JsonWriteContext v1 = /*EL:119*/this._child;
        /*SL:120*/if (v1 == null) {
            /*SL:121*/v1 = (this._child = new JsonWriteContext(1, this, (this._dups == null) ? null : this._dups.child()));
            /*SL:122*/return v1;
        }
        /*SL:124*/return v1.reset(1);
    }
    
    public JsonWriteContext createChildObjectContext() {
        JsonWriteContext v1 = /*EL:128*/this._child;
        /*SL:129*/if (v1 == null) {
            /*SL:130*/v1 = (this._child = new JsonWriteContext(2, this, (this._dups == null) ? null : this._dups.child()));
            /*SL:131*/return v1;
        }
        /*SL:133*/return v1.reset(2);
    }
    
    @Override
    public final JsonWriteContext getParent() {
        /*SL:136*/return this._parent;
    }
    
    @Override
    public final String getCurrentName() {
        /*SL:137*/return this._currentName;
    }
    
    @Override
    public boolean hasCurrentName() {
        /*SL:139*/return this._currentName != null;
    }
    
    public JsonWriteContext clearAndGetParent() {
        /*SL:152*/this._currentValue = null;
        /*SL:154*/return this._parent;
    }
    
    public DupDetector getDupDetector() {
        /*SL:158*/return this._dups;
    }
    
    public int writeFieldName(final String a1) throws JsonProcessingException {
        /*SL:167*/if (this._type != 2 || this._gotName) {
            /*SL:168*/return 4;
        }
        /*SL:170*/this._gotName = true;
        /*SL:171*/this._currentName = a1;
        /*SL:172*/if (this._dups != null) {
            this._checkDup(this._dups, a1);
        }
        /*SL:173*/return (this._index >= 0) ? 1 : 0;
    }
    
    private final void _checkDup(final DupDetector v1, final String v2) throws JsonProcessingException {
        /*SL:177*/if (v1.isDup(v2)) {
            final Object a1 = /*EL:178*/v1.getSource();
            /*SL:179*/throw new JsonGenerationException("Duplicate field '" + v2 + "'", (a1 instanceof JsonGenerator) ? ((JsonGenerator)a1) : null);
        }
    }
    
    public int writeValue() {
        /*SL:186*/if (this._type == 2) {
            /*SL:187*/if (!this._gotName) {
                /*SL:188*/return 5;
            }
            /*SL:190*/this._gotName = false;
            /*SL:191*/++this._index;
            /*SL:192*/return 2;
        }
        else {
            /*SL:196*/if (this._type == 1) {
                final int v1 = /*EL:197*/this._index;
                /*SL:198*/++this._index;
                /*SL:199*/return (v1 >= 0) ? 1 : 0;
            }
            /*SL:204*/++this._index;
            /*SL:205*/return (this._index == 0) ? 0 : 3;
        }
    }
}
