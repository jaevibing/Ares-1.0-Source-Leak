package com.google.api.client.repackaged.com.google.common.base;

import com.google.api.client.repackaged.com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Strings
{
    public static String nullToEmpty(@Nullable final String a1) {
        /*SL:41*/return (a1 == null) ? "" : a1;
    }
    
    @Nullable
    public static String emptyToNull(@Nullable final String a1) {
        /*SL:52*/return isNullOrEmpty(a1) ? null : a1;
    }
    
    public static boolean isNullOrEmpty(@Nullable final String a1) {
        /*SL:67*/return Platform.stringIsNullOrEmpty(a1);
    }
    
    public static String padStart(final String a2, final int a3, final char v1) {
        /*SL:89*/Preconditions.<String>checkNotNull(a2);
        /*SL:90*/if (a2.length() >= a3) {
            /*SL:91*/return a2;
        }
        final StringBuilder v2 = /*EL:93*/new StringBuilder(a3);
        /*SL:94*/for (int a4 = a2.length(); a4 < a3; ++a4) {
            /*SL:95*/v2.append(v1);
        }
        /*SL:97*/v2.append(a2);
        /*SL:98*/return v2.toString();
    }
    
    public static String padEnd(final String a2, final int a3, final char v1) {
        /*SL:120*/Preconditions.<String>checkNotNull(a2);
        /*SL:121*/if (a2.length() >= a3) {
            /*SL:122*/return a2;
        }
        final StringBuilder v2 = /*EL:124*/new StringBuilder(a3);
        /*SL:125*/v2.append(a2);
        /*SL:126*/for (int a4 = a2.length(); a4 < a3; ++a4) {
            /*SL:127*/v2.append(v1);
        }
        /*SL:129*/return v2.toString();
    }
    
    public static String repeat(final String a1, final int a2) {
        /*SL:143*/Preconditions.<String>checkNotNull(a1);
        /*SL:145*/if (a2 <= 1) {
            /*SL:146*/Preconditions.checkArgument(a2 >= 0, "invalid count: %s", a2);
            /*SL:147*/return (a2 == 0) ? "" : a1;
        }
        final int v1 = /*EL:151*/a1.length();
        final long v2 = /*EL:152*/v1 * a2;
        final int v3 = /*EL:153*/(int)v2;
        /*SL:154*/if (v3 != v2) {
            /*SL:155*/throw new ArrayIndexOutOfBoundsException("Required array size too large: " + v2);
        }
        final char[] v4 = /*EL:158*/new char[v3];
        /*SL:159*/a1.getChars(0, v1, v4, 0);
        int v5;
        /*SL:161*/for (v5 = v1; v5 < v3 - v5; v5 <<= 1) {
            /*SL:162*/System.arraycopy(v4, 0, v4, v5, v5);
        }
        /*SL:164*/System.arraycopy(v4, 0, v4, v5, v3 - v5);
        /*SL:165*/return new String(v4);
    }
    
    public static String commonPrefix(final CharSequence a1, final CharSequence a2) {
        /*SL:177*/Preconditions.<CharSequence>checkNotNull(a1);
        /*SL:178*/Preconditions.<CharSequence>checkNotNull(a2);
        int v1;
        int v2;
        /*SL:182*/for (v1 = Math.min(a1.length(), a2.length()), v2 = 0; v2 < v1 && a1.charAt(v2) == a2.charAt(v2); /*SL:183*/++v2) {}
        /*SL:185*/if (validSurrogatePairAt(a1, v2 - 1) || validSurrogatePairAt(a2, v2 - 1)) {
            /*SL:186*/--v2;
        }
        /*SL:188*/return a1.subSequence(0, v2).toString();
    }
    
    public static String commonSuffix(final CharSequence a1, final CharSequence a2) {
        /*SL:200*/Preconditions.<CharSequence>checkNotNull(a1);
        /*SL:201*/Preconditions.<CharSequence>checkNotNull(a2);
        int v1;
        int v2;
        /*SL:205*/for (v1 = Math.min(a1.length(), a2.length()), v2 = 0; v2 < v1 && a1.charAt(a1.length() - v2 - 1) == a2.charAt(a2.length() - v2 - 1); /*SL:206*/++v2) {}
        /*SL:208*/if (validSurrogatePairAt(a1, a1.length() - v2 - 1) || validSurrogatePairAt(a2, a2.length() - v2 - 1)) {
            /*SL:210*/--v2;
        }
        /*SL:212*/return a1.subSequence(a1.length() - v2, a1.length()).toString();
    }
    
    @VisibleForTesting
    static boolean validSurrogatePairAt(final CharSequence a1, final int a2) {
        /*SL:221*/return a2 >= 0 && a2 <= a1.length() - 2 && Character.isHighSurrogate(a1.charAt(a2)) && Character.isLowSurrogate(a1.charAt(a2 + 1));
    }
}
