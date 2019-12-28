package com.google.api.client.util;

import java.io.IOException;

@Beta
public final class BackOffUtils
{
    public static boolean next(final Sleeper a1, final BackOff a2) throws InterruptedException, IOException {
        final long v1 = /*EL:46*/a2.nextBackOffMillis();
        /*SL:47*/if (v1 == -1L) {
            /*SL:48*/return false;
        }
        /*SL:50*/a1.sleep(v1);
        /*SL:51*/return true;
    }
}
