package com.google.cloud.storage;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

public class CanonicalExtensionHeadersSerializer
{
    private static final char HEADER_SEPARATOR = ':';
    private static final char HEADER_NAME_SEPARATOR = ';';
    private final Storage.SignUrlOption.SignatureVersion signatureVersion;
    
    public CanonicalExtensionHeadersSerializer(final Storage.SignUrlOption.SignatureVersion a1) {
        this.signatureVersion = a1;
    }
    
    public CanonicalExtensionHeadersSerializer() {
        this.signatureVersion = Storage.SignUrlOption.SignatureVersion.V2;
    }
    
    public StringBuilder serialize(final Map<String, String> v2) {
        final StringBuilder v3 = /*EL:51*/new StringBuilder();
        /*SL:53*/if (v2 == null || v2.isEmpty()) {
            /*SL:54*/return v3;
        }
        final Map<String, String> v4 = /*EL:57*/this.getLowercaseHeaders(v2);
        final List<String> v5 = /*EL:60*/new ArrayList<String>(v4.keySet());
        /*SL:61*/Collections.<String>sort(v5);
        /*SL:63*/for (final String a1 : v5) {
            /*SL:64*/v3.append(a1).append(/*EL:65*/':').append(/*EL:66*/v4.get(a1).trim().replaceAll(/*EL:71*/"[\\s]{2,}", " ").replaceAll(/*EL:73*/"(\\t|\\r?\\n)+", " ")).append('\n');
        }
        /*SL:80*/return v3;
    }
    
    public StringBuilder serializeHeaderNames(final Map<String, String> v2) {
        final StringBuilder v3 = /*EL:84*/new StringBuilder();
        /*SL:86*/if (v2 == null || v2.isEmpty()) {
            /*SL:87*/return v3;
        }
        final Map<String, String> v4 = /*EL:89*/this.getLowercaseHeaders(v2);
        final List<String> v5 = /*EL:91*/new ArrayList<String>(v4.keySet());
        /*SL:92*/Collections.<String>sort(v5);
        /*SL:94*/for (final String a1 : v5) {
            /*SL:95*/v3.append(a1).append(';');
        }
        /*SL:98*/v3.setLength(v3.length() - 1);
        /*SL:100*/return v3;
    }
    
    private Map<String, String> getLowercaseHeaders(final Map<String, String> v-2) {
        final Map<String, String> map = /*EL:106*/new HashMap<String, String>();
        /*SL:107*/for (final String v1 : new ArrayList<String>(v-2.keySet())) {
            final String a1 = /*EL:109*/v1.toLowerCase();
            /*SL:113*/if (Storage.SignUrlOption.SignatureVersion.V2.equals(this.signatureVersion)) {
                /*SL:114*/if ("x-goog-encryption-key".equals(a1)) {
                    continue;
                }
                /*SL:115*/if ("x-goog-encryption-key-sha256".equals(a1)) {
                    /*SL:117*/continue;
                }
            }
            /*SL:120*/map.put(a1, v-2.get(v1));
        }
        /*SL:123*/return map;
    }
}
