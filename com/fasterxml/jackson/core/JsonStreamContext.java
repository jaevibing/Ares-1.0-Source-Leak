package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.io.CharTypes;

public abstract class JsonStreamContext
{
    protected static final int TYPE_ROOT = 0;
    protected static final int TYPE_ARRAY = 1;
    protected static final int TYPE_OBJECT = 2;
    protected int _type;
    protected int _index;
    
    protected JsonStreamContext() {
    }
    
    protected JsonStreamContext(final JsonStreamContext a1) {
        this._type = a1._type;
        this._index = a1._index;
    }
    
    protected JsonStreamContext(final int a1, final int a2) {
        this._type = a1;
        this._index = a2;
    }
    
    public abstract JsonStreamContext getParent();
    
    public final boolean inArray() {
        /*SL:82*/return this._type == 1;
    }
    
    public final boolean inRoot() {
        /*SL:89*/return this._type == 0;
    }
    
    public final boolean inObject() {
        /*SL:95*/return this._type == 2;
    }
    
    @Deprecated
    public final String getTypeDesc() {
        /*SL:106*/switch (this._type) {
            case 0: {
                /*SL:107*/return "ROOT";
            }
            case 1: {
                /*SL:108*/return "ARRAY";
            }
            case 2: {
                /*SL:109*/return "OBJECT";
            }
            default: {
                /*SL:111*/return "?";
            }
        }
    }
    
    public String typeDesc() {
        /*SL:118*/switch (this._type) {
            case 0: {
                /*SL:119*/return "root";
            }
            case 1: {
                /*SL:120*/return "Array";
            }
            case 2: {
                /*SL:121*/return "Object";
            }
            default: {
                /*SL:123*/return "?";
            }
        }
    }
    
    public final int getEntryCount() {
        /*SL:129*/return this._index + 1;
    }
    
    public final int getCurrentIndex() {
        /*SL:134*/return (this._index < 0) ? 0 : this._index;
    }
    
    public boolean hasCurrentIndex() {
        /*SL:143*/return this._index >= 0;
    }
    
    public boolean hasPathSegment() {
        /*SL:163*/if (this._type == 2) {
            /*SL:164*/return this.hasCurrentName();
        }
        /*SL:165*/return this._type == 1 && /*EL:166*/this.hasCurrentIndex();
    }
    
    public abstract String getCurrentName();
    
    public boolean hasCurrentName() {
        /*SL:181*/return this.getCurrentName() != null;
    }
    
    public Object getCurrentValue() {
        /*SL:198*/return null;
    }
    
    public void setCurrentValue(final Object a1) {
    }
    
    public JsonPointer pathAsPointer() {
        /*SL:218*/return JsonPointer.forPath(this, false);
    }
    
    public JsonPointer pathAsPointer(final boolean a1) {
        /*SL:231*/return JsonPointer.forPath(this, a1);
    }
    
    public JsonLocation getStartLocation(final Object a1) {
        /*SL:250*/return JsonLocation.NA;
    }
    
    @Override
    public String toString() {
        final StringBuilder v0 = /*EL:261*/new StringBuilder(64);
        /*SL:262*/switch (this._type) {
            case 0: {
                /*SL:264*/v0.append("/");
                /*SL:265*/break;
            }
            case 1: {
                /*SL:267*/v0.append('[');
                /*SL:268*/v0.append(this.getCurrentIndex());
                /*SL:269*/v0.append(']');
                /*SL:270*/break;
            }
            default: {
                /*SL:273*/v0.append('{');
                final String v = /*EL:274*/this.getCurrentName();
                /*SL:275*/if (v != null) {
                    /*SL:276*/v0.append('\"');
                    /*SL:277*/CharTypes.appendQuoted(v0, v);
                    /*SL:278*/v0.append('\"');
                }
                else {
                    /*SL:280*/v0.append('?');
                }
                /*SL:282*/v0.append('}');
                break;
            }
        }
        /*SL:285*/return v0.toString();
    }
}
