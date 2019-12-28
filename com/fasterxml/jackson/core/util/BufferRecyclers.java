package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import java.lang.ref.SoftReference;

public class BufferRecyclers
{
    public static final String SYSTEM_PROPERTY_TRACK_REUSABLE_BUFFERS = "com.fasterxml.jackson.core.util.BufferRecyclers.trackReusableBuffers";
    private static final ThreadLocalBufferManager _bufferRecyclerTracker;
    protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef;
    protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _encoderRef;
    
    public static BufferRecycler getBufferRecycler() {
        SoftReference<BufferRecycler> v1 = BufferRecyclers._recyclerRef.get();
        BufferRecycler v2 = /*EL:64*/(v1 == null) ? null : v1.get();
        /*SL:66*/if (v2 == null) {
            /*SL:67*/v2 = new BufferRecycler();
            /*SL:68*/if (BufferRecyclers._bufferRecyclerTracker != null) {
                /*SL:69*/v1 = BufferRecyclers._bufferRecyclerTracker.wrapAndTrack(v2);
            }
            else {
                /*SL:71*/v1 = new SoftReference<BufferRecycler>(v2);
            }
            BufferRecyclers._recyclerRef.set(/*EL:73*/v1);
        }
        /*SL:75*/return v2;
    }
    
    public static int releaseBuffers() {
        /*SL:92*/if (BufferRecyclers._bufferRecyclerTracker != null) {
            /*SL:93*/return BufferRecyclers._bufferRecyclerTracker.releaseBuffers();
        }
        /*SL:95*/return -1;
    }
    
    public static JsonStringEncoder getJsonStringEncoder() {
        final SoftReference<JsonStringEncoder> v1 = BufferRecyclers._encoderRef.get();
        JsonStringEncoder v2 = /*EL:114*/(v1 == null) ? null : v1.get();
        /*SL:116*/if (v2 == null) {
            /*SL:117*/v2 = new JsonStringEncoder();
            BufferRecyclers._encoderRef.set(/*EL:118*/new SoftReference<JsonStringEncoder>(v2));
        }
        /*SL:120*/return v2;
    }
    
    public static byte[] encodeAsUTF8(final String a1) {
        /*SL:129*/return getJsonStringEncoder().encodeAsUTF8(a1);
    }
    
    public static char[] quoteAsJsonText(final String a1) {
        /*SL:136*/return getJsonStringEncoder().quoteAsString(a1);
    }
    
    public static void quoteAsJsonText(final CharSequence a1, final StringBuilder a2) {
        getJsonStringEncoder().quoteAsString(/*EL:143*/a1, a2);
    }
    
    public static byte[] quoteAsJsonUTF8(final String a1) {
        /*SL:150*/return getJsonStringEncoder().quoteAsUTF8(a1);
    }
    
    static {
        _bufferRecyclerTracker = ("true".equals(System.getProperty("com.fasterxml.jackson.core.util.BufferRecyclers.trackReusableBuffers")) ? ThreadLocalBufferManager.instance() : null);
        _recyclerRef = new ThreadLocal<SoftReference<BufferRecycler>>();
        _encoderRef = new ThreadLocal<SoftReference<JsonStringEncoder>>();
    }
}
