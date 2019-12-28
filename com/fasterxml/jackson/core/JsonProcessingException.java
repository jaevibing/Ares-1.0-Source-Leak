package com.fasterxml.jackson.core;

import java.io.IOException;

public class JsonProcessingException extends IOException
{
    static final long serialVersionUID = 123L;
    protected JsonLocation _location;
    
    protected JsonProcessingException(final String a1, final JsonLocation a2, final Throwable a3) {
        super(a1);
        if (a3 != null) {
            this.initCause(a3);
        }
        this._location = a2;
    }
    
    protected JsonProcessingException(final String a1) {
        super(a1);
    }
    
    protected JsonProcessingException(final String a1, final JsonLocation a2) {
        this(a1, a2, null);
    }
    
    protected JsonProcessingException(final String a1, final Throwable a2) {
        this(a1, null, a2);
    }
    
    protected JsonProcessingException(final Throwable a1) {
        this(null, null, a1);
    }
    
    public JsonLocation getLocation() {
        /*SL:54*/return this._location;
    }
    
    public void clearLocation() {
        /*SL:63*/this._location = null;
    }
    
    public String getOriginalMessage() {
        /*SL:72*/return super.getMessage();
    }
    
    public Object getProcessor() {
        /*SL:88*/return null;
    }
    
    protected String getMessageSuffix() {
        /*SL:101*/return null;
    }
    
    @Override
    public String getMessage() {
        String s = /*EL:113*/super.getMessage();
        /*SL:114*/if (s == null) {
            /*SL:115*/s = "N/A";
        }
        final JsonLocation location = /*EL:117*/this.getLocation();
        final String v0 = /*EL:118*/this.getMessageSuffix();
        /*SL:120*/if (location != null || v0 != null) {
            final StringBuilder v = /*EL:121*/new StringBuilder(100);
            /*SL:122*/v.append(s);
            /*SL:123*/if (v0 != null) {
                /*SL:124*/v.append(v0);
            }
            /*SL:126*/if (location != null) {
                /*SL:127*/v.append('\n');
                /*SL:128*/v.append(" at ");
                /*SL:129*/v.append(location.toString());
            }
            /*SL:131*/s = v.toString();
        }
        /*SL:133*/return s;
    }
    
    @Override
    public String toString() {
        /*SL:136*/return this.getClass().getName() + ": " + this.getMessage();
    }
}
