package com.fasterxml.jackson.core.filter;

import com.fasterxml.jackson.core.JsonPointer;

public class JsonPointerBasedFilter extends TokenFilter
{
    protected final JsonPointer _pathToMatch;
    
    public JsonPointerBasedFilter(final String a1) {
        this(JsonPointer.compile(a1));
    }
    
    public JsonPointerBasedFilter(final JsonPointer a1) {
        this._pathToMatch = a1;
    }
    
    @Override
    public TokenFilter includeElement(final int a1) {
        final JsonPointer v1 = /*EL:27*/this._pathToMatch.matchElement(a1);
        /*SL:28*/if (v1 == null) {
            /*SL:29*/return null;
        }
        /*SL:31*/if (v1.matches()) {
            /*SL:32*/return TokenFilter.INCLUDE_ALL;
        }
        /*SL:34*/return new JsonPointerBasedFilter(v1);
    }
    
    @Override
    public TokenFilter includeProperty(final String a1) {
        final JsonPointer v1 = /*EL:39*/this._pathToMatch.matchProperty(a1);
        /*SL:40*/if (v1 == null) {
            /*SL:41*/return null;
        }
        /*SL:43*/if (v1.matches()) {
            /*SL:44*/return TokenFilter.INCLUDE_ALL;
        }
        /*SL:46*/return new JsonPointerBasedFilter(v1);
    }
    
    @Override
    public TokenFilter filterStartArray() {
        /*SL:51*/return this;
    }
    
    @Override
    public TokenFilter filterStartObject() {
        /*SL:56*/return this;
    }
    
    @Override
    protected boolean _includeScalar() {
        /*SL:62*/return this._pathToMatch.matches();
    }
    
    @Override
    public String toString() {
        /*SL:67*/return "[JsonPointerFilter at: " + this._pathToMatch + "]";
    }
}
