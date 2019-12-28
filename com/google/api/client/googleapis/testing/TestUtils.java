package com.google.api.client.googleapis.testing;

import java.util.List;
import java.util.Iterator;
import java.net.URLDecoder;
import java.io.IOException;
import com.google.common.collect.Lists;
import com.google.common.base.Splitter;
import java.util.HashMap;
import java.util.Map;

public final class TestUtils
{
    private static final String UTF_8 = "UTF-8";
    
    public static Map<String, String> parseQuery(final String v-5) throws IOException {
        final Map<String, String> map = /*EL:34*/new HashMap<String, String>();
        final Iterable<String> split = /*EL:35*/Splitter.on('&').split(v-5);
        /*SL:36*/for (final String sequence : split) {
            final List<String> a1 = /*EL:37*/(List<String>)Lists.<Object>newArrayList((Iterable<?>)Splitter.on('=').split(sequence));
            /*SL:38*/if (a1.size() != 2) {
                /*SL:39*/throw new IOException("Invalid Query String");
            }
            final String v1 = /*EL:41*/URLDecoder.decode(a1.get(0), "UTF-8");
            final String v2 = /*EL:42*/URLDecoder.decode(a1.get(1), "UTF-8");
            /*SL:43*/map.put(v1, v2);
        }
        /*SL:45*/return map;
    }
}
