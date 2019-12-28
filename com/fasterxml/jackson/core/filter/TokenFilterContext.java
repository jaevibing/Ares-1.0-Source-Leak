package com.fasterxml.jackson.core.filter;

import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;

public class TokenFilterContext extends JsonStreamContext
{
    protected final TokenFilterContext _parent;
    protected TokenFilterContext _child;
    protected String _currentName;
    protected TokenFilter _filter;
    protected boolean _startHandled;
    protected boolean _needToHandleName;
    
    protected TokenFilterContext(final int a1, final TokenFilterContext a2, final TokenFilter a3, final boolean a4) {
        this._type = a1;
        this._parent = a2;
        this._filter = a3;
        this._index = -1;
        this._startHandled = a4;
        this._needToHandleName = false;
    }
    
    protected TokenFilterContext reset(final int a1, final TokenFilter a2, final boolean a3) {
        /*SL:83*/this._type = a1;
        /*SL:84*/this._filter = a2;
        /*SL:85*/this._index = -1;
        /*SL:86*/this._currentName = null;
        /*SL:87*/this._startHandled = a3;
        /*SL:88*/this._needToHandleName = false;
        /*SL:89*/return this;
    }
    
    public static TokenFilterContext createRootContext(final TokenFilter a1) {
        /*SL:100*/return new TokenFilterContext(0, null, a1, true);
    }
    
    public TokenFilterContext createChildArrayContext(final TokenFilter a1, final boolean a2) {
        TokenFilterContext v1 = /*EL:104*/this._child;
        /*SL:105*/if (v1 == null) {
            /*SL:106*/v1 = (this._child = new TokenFilterContext(1, this, a1, a2));
            /*SL:107*/return v1;
        }
        /*SL:109*/return v1.reset(1, a1, a2);
    }
    
    public TokenFilterContext createChildObjectContext(final TokenFilter a1, final boolean a2) {
        TokenFilterContext v1 = /*EL:113*/this._child;
        /*SL:114*/if (v1 == null) {
            /*SL:115*/v1 = (this._child = new TokenFilterContext(2, this, a1, a2));
            /*SL:116*/return v1;
        }
        /*SL:118*/return v1.reset(2, a1, a2);
    }
    
    public TokenFilter setFieldName(final String a1) throws JsonProcessingException {
        /*SL:128*/this._currentName = a1;
        /*SL:129*/this._needToHandleName = true;
        /*SL:130*/return this._filter;
    }
    
    public TokenFilter checkValue(final TokenFilter a1) {
        /*SL:139*/if (this._type == 2) {
            /*SL:140*/return a1;
        }
        final int v1 = /*EL:143*/++this._index;
        /*SL:144*/if (this._type == 1) {
            /*SL:145*/return a1.includeElement(v1);
        }
        /*SL:147*/return a1.includeRootValue(v1);
    }
    
    public void writePath(final JsonGenerator a1) throws IOException {
        /*SL:156*/if (this._filter == null || this._filter == TokenFilter.INCLUDE_ALL) {
            /*SL:157*/return;
        }
        /*SL:159*/if (this._parent != null) {
            /*SL:160*/this._parent._writePath(a1);
        }
        /*SL:162*/if (this._startHandled) {
            /*SL:164*/if (this._needToHandleName) {
                /*SL:165*/a1.writeFieldName(this._currentName);
            }
        }
        else {
            /*SL:168*/this._startHandled = true;
            /*SL:169*/if (this._type == 2) {
                /*SL:170*/a1.writeStartObject();
                /*SL:171*/a1.writeFieldName(this._currentName);
            }
            else/*SL:172*/ if (this._type == 1) {
                /*SL:173*/a1.writeStartArray();
            }
        }
    }
    
    public void writeImmediatePath(final JsonGenerator a1) throws IOException {
        /*SL:186*/if (this._filter == null || this._filter == TokenFilter.INCLUDE_ALL) {
            /*SL:187*/return;
        }
        /*SL:189*/if (this._startHandled) {
            /*SL:191*/if (this._needToHandleName) {
                /*SL:192*/a1.writeFieldName(this._currentName);
            }
        }
        else {
            /*SL:195*/this._startHandled = true;
            /*SL:196*/if (this._type == 2) {
                /*SL:197*/a1.writeStartObject();
                /*SL:198*/if (this._needToHandleName) {
                    /*SL:199*/a1.writeFieldName(this._currentName);
                }
            }
            else/*SL:201*/ if (this._type == 1) {
                /*SL:202*/a1.writeStartArray();
            }
        }
    }
    
    private void _writePath(final JsonGenerator a1) throws IOException {
        /*SL:209*/if (this._filter == null || this._filter == TokenFilter.INCLUDE_ALL) {
            /*SL:210*/return;
        }
        /*SL:212*/if (this._parent != null) {
            /*SL:213*/this._parent._writePath(a1);
        }
        /*SL:215*/if (this._startHandled) {
            /*SL:217*/if (this._needToHandleName) {
                /*SL:218*/this._needToHandleName = false;
                /*SL:219*/a1.writeFieldName(this._currentName);
            }
        }
        else {
            /*SL:222*/this._startHandled = true;
            /*SL:223*/if (this._type == 2) {
                /*SL:224*/a1.writeStartObject();
                /*SL:225*/if (this._needToHandleName) {
                    /*SL:226*/this._needToHandleName = false;
                    /*SL:227*/a1.writeFieldName(this._currentName);
                }
            }
            else/*SL:229*/ if (this._type == 1) {
                /*SL:230*/a1.writeStartArray();
            }
        }
    }
    
    public TokenFilterContext closeArray(final JsonGenerator a1) throws IOException {
        /*SL:237*/if (this._startHandled) {
            /*SL:238*/a1.writeEndArray();
        }
        /*SL:240*/if (this._filter != null && this._filter != TokenFilter.INCLUDE_ALL) {
            /*SL:241*/this._filter.filterFinishArray();
        }
        /*SL:243*/return this._parent;
    }
    
    public TokenFilterContext closeObject(final JsonGenerator a1) throws IOException {
        /*SL:248*/if (this._startHandled) {
            /*SL:249*/a1.writeEndObject();
        }
        /*SL:251*/if (this._filter != null && this._filter != TokenFilter.INCLUDE_ALL) {
            /*SL:252*/this._filter.filterFinishObject();
        }
        /*SL:254*/return this._parent;
    }
    
    public void skipParentChecks() {
        /*SL:258*/this._filter = null;
        /*SL:259*/for (TokenFilterContext v1 = this._parent; v1 != null; v1 = v1._parent) {
            /*SL:260*/this._parent._filter = null;
        }
    }
    
    @Override
    public Object getCurrentValue() {
        /*SL:271*/return null;
    }
    
    @Override
    public void setCurrentValue(final Object a1) {
    }
    
    @Override
    public final TokenFilterContext getParent() {
        /*SL:276*/return this._parent;
    }
    
    @Override
    public final String getCurrentName() {
        /*SL:277*/return this._currentName;
    }
    
    @Override
    public boolean hasCurrentName() {
        /*SL:279*/return this._currentName != null;
    }
    
    public TokenFilter getFilter() {
        /*SL:281*/return this._filter;
    }
    
    public boolean isStartHandled() {
        /*SL:282*/return this._startHandled;
    }
    
    public JsonToken nextTokenToRead() {
        /*SL:285*/if (!this._startHandled) {
            /*SL:286*/this._startHandled = true;
            /*SL:287*/if (this._type == 2) {
                /*SL:288*/return JsonToken.START_OBJECT;
            }
            /*SL:291*/return JsonToken.START_ARRAY;
        }
        else {
            /*SL:294*/if (this._needToHandleName && this._type == 2) {
                /*SL:295*/this._needToHandleName = false;
                /*SL:296*/return JsonToken.FIELD_NAME;
            }
            /*SL:298*/return null;
        }
    }
    
    public TokenFilterContext findChildOf(final TokenFilterContext v2) {
        /*SL:302*/if (this._parent == v2) {
            /*SL:303*/return this;
        }
        TokenFilterContext a1;
        /*SL:306*/for (TokenFilterContext v3 = this._parent; v3 != null; /*SL:311*/v3 = a1) {
            a1 = v3._parent;
            if (a1 == v2) {
                return v3;
            }
        }
        /*SL:314*/return null;
    }
    
    protected void appendDesc(final StringBuilder a1) {
        /*SL:320*/if (this._parent != null) {
            /*SL:321*/this._parent.appendDesc(a1);
        }
        /*SL:323*/if (this._type == 2) {
            /*SL:324*/a1.append('{');
            /*SL:325*/if (this._currentName != null) {
                /*SL:326*/a1.append('\"');
                /*SL:328*/a1.append(this._currentName);
                /*SL:329*/a1.append('\"');
            }
            else {
                /*SL:331*/a1.append('?');
            }
            /*SL:333*/a1.append('}');
        }
        else/*SL:334*/ if (this._type == 1) {
            /*SL:335*/a1.append('[');
            /*SL:336*/a1.append(this.getCurrentIndex());
            /*SL:337*/a1.append(']');
        }
        else {
            /*SL:340*/a1.append("/");
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder v1 = /*EL:351*/new StringBuilder(64);
        /*SL:352*/this.appendDesc(v1);
        /*SL:353*/return v1.toString();
    }
}
