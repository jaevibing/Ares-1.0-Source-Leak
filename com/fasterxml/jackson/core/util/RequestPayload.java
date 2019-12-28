package com.fasterxml.jackson.core.util;

import java.io.IOException;
import java.io.Serializable;

public class RequestPayload implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected byte[] _payloadAsBytes;
    protected CharSequence _payloadAsText;
    protected String _charset;
    
    public RequestPayload(final byte[] a1, final String a2) {
        if (a1 == null) {
            throw new IllegalArgumentException();
        }
        this._payloadAsBytes = a1;
        this._charset = ((a2 == null || a2.isEmpty()) ? "UTF-8" : a2);
    }
    
    public RequestPayload(final CharSequence a1) {
        if (a1 == null) {
            throw new IllegalArgumentException();
        }
        this._payloadAsText = a1;
    }
    
    public Object getRawPayload() {
        /*SL:49*/if (this._payloadAsBytes != null) {
            /*SL:50*/return this._payloadAsBytes;
        }
        /*SL:53*/return this._payloadAsText;
    }
    
    @Override
    public String toString() {
        /*SL:58*/if (this._payloadAsBytes != null) {
            try {
                /*SL:60*/return new String(this._payloadAsBytes, this._charset);
            }
            catch (IOException v1) {
                /*SL:62*/throw new RuntimeException(v1);
            }
        }
        /*SL:65*/return this._payloadAsText.toString();
    }
}
