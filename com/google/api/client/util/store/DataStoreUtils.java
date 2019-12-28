package com.google.api.client.util.store;

import java.util.Iterator;
import java.io.IOException;

public final class DataStoreUtils
{
    public static String toString(final DataStore<?> v0) {
        try {
            final StringBuilder v = /*EL:42*/new StringBuilder();
            /*SL:43*/v.append('{');
            boolean v2 = /*EL:44*/true;
            /*SL:45*/for (final String a1 : v0.keySet()) {
                /*SL:46*/if (v2) {
                    /*SL:47*/v2 = false;
                }
                else {
                    /*SL:49*/v.append(", ");
                }
                /*SL:51*/v.append(a1).append('=').append(v0.get(a1));
            }
            /*SL:53*/return v.append('}').toString();
        }
        catch (IOException v3) {
            /*SL:55*/throw new RuntimeException(v3);
        }
    }
}
