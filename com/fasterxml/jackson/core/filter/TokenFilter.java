package com.fasterxml.jackson.core.filter;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;

public class TokenFilter
{
    public static final TokenFilter INCLUDE_ALL;
    
    public TokenFilter filterStartObject() {
        /*SL:66*/return this;
    }
    
    public TokenFilter filterStartArray() {
        /*SL:93*/return this;
    }
    
    public void filterFinishObject() {
    }
    
    public void filterFinishArray() {
    }
    
    public TokenFilter includeProperty(final String a1) {
        /*SL:141*/return this;
    }
    
    public TokenFilter includeElement(final int a1) {
        /*SL:167*/return this;
    }
    
    public TokenFilter includeRootValue(final int a1) {
        /*SL:193*/return this;
    }
    
    public boolean includeValue(final JsonParser a1) throws IOException {
        /*SL:210*/return this._includeScalar();
    }
    
    public boolean includeBoolean(final boolean a1) {
        /*SL:225*/return this._includeScalar();
    }
    
    public boolean includeNull() {
        /*SL:234*/return this._includeScalar();
    }
    
    public boolean includeString(final String a1) {
        /*SL:243*/return this._includeScalar();
    }
    
    public boolean includeNumber(final int a1) {
        /*SL:254*/return this._includeScalar();
    }
    
    public boolean includeNumber(final long a1) {
        /*SL:263*/return this._includeScalar();
    }
    
    public boolean includeNumber(final float a1) {
        /*SL:272*/return this._includeScalar();
    }
    
    public boolean includeNumber(final double a1) {
        /*SL:281*/return this._includeScalar();
    }
    
    public boolean includeNumber(final BigDecimal a1) {
        /*SL:290*/return this._includeScalar();
    }
    
    public boolean includeNumber(final BigInteger a1) {
        /*SL:299*/return this._includeScalar();
    }
    
    public boolean includeBinary() {
        /*SL:310*/return this._includeScalar();
    }
    
    public boolean includeRawValue() {
        /*SL:323*/return this._includeScalar();
    }
    
    public boolean includeEmbeddedValue(final Object a1) {
        /*SL:332*/return this._includeScalar();
    }
    
    @Override
    public String toString() {
        /*SL:343*/if (this == TokenFilter.INCLUDE_ALL) {
            /*SL:344*/return "TokenFilter.INCLUDE_ALL";
        }
        /*SL:346*/return super.toString();
    }
    
    protected boolean _includeScalar() {
        /*SL:361*/return true;
    }
    
    static {
        INCLUDE_ALL = new TokenFilter();
    }
}
