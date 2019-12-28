package org.spongepowered.asm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Serializable;

public final class VersionNumber implements Comparable<VersionNumber>, Serializable
{
    private static final long serialVersionUID = 1L;
    public static final VersionNumber NONE;
    private static final Pattern PATTERN;
    private final long value;
    private final String suffix;
    
    private VersionNumber() {
        this.value = 0L;
        this.suffix = "";
    }
    
    private VersionNumber(final short[] a1) {
        this(a1, null);
    }
    
    private VersionNumber(final short[] a1, final String a2) {
        this.value = pack(a1);
        this.suffix = ((a2 != null) ? a2 : "");
    }
    
    private VersionNumber(final short a1, final short a2, final short a3, final short a4) {
        this(a1, a2, a3, a4, null);
    }
    
    private VersionNumber(final short a1, final short a2, final short a3, final short a4, final String a5) {
        this.value = pack(a1, a2, a3, a4);
        this.suffix = ((a5 != null) ? a5 : "");
    }
    
    @Override
    public String toString() {
        final short[] v1 = unpack(/*EL:91*/this.value);
        /*SL:93*/return String.format("%d.%d%3$s%4$s%5$s", v1[0], /*EL:94*/v1[1], /*EL:95*/((this.value & 0x7FFFFFFFL) > 0L) ? /*EL:96*/String.format(".%d", v1[2]) : "", ((this.value & 0x7FFFL) > 0L) ? /*EL:97*/String.format(".%d", v1[3]) : "", this.suffix);
    }
    
    @Override
    public int compareTo(final VersionNumber a1) {
        /*SL:106*/if (a1 == null) {
            /*SL:107*/return 1;
        }
        final long v1 = /*EL:109*/this.value - a1.value;
        /*SL:110*/return (v1 > 0L) ? 1 : ((v1 < 0L) ? -1 : 0);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:118*/return a1 instanceof VersionNumber && /*EL:122*/((VersionNumber)a1).value == this.value;
    }
    
    @Override
    public int hashCode() {
        /*SL:130*/return (int)(this.value >> 32) ^ (int)(this.value & 0xFFFFFFFFL);
    }
    
    private static long pack(final short... a1) {
        /*SL:140*/return a1[0] << 48 | a1[1] << 32 | a1[2] << 16 | a1[3];
    }
    
    private static short[] unpack(final long a1) {
        /*SL:150*/return new short[] { (short)(a1 >> 48), (short)(a1 >> 32 & 0x7FFFL), (short)(a1 >> 16 & 0x7FFFL), (short)(a1 & 0x7FFFL) };
    }
    
    public static VersionNumber parse(final String a1) {
        /*SL:165*/return parse(a1, VersionNumber.NONE);
    }
    
    public static VersionNumber parse(final String a1, final String a2) {
        /*SL:177*/return parse(a1, parse(a2));
    }
    
    private static VersionNumber parse(final String v-3, final VersionNumber v-2) {
        /*SL:189*/if (v-3 == null) {
            /*SL:190*/return v-2;
        }
        final Matcher matcher = VersionNumber.PATTERN.matcher(/*EL:193*/v-3);
        /*SL:194*/if (!matcher.matches()) {
            /*SL:195*/return v-2;
        }
        final short[] v0 = /*EL:198*/new short[4];
        /*SL:199*/for (int v = 0; v < 4; ++v) {
            String a2 = /*EL:200*/matcher.group(v + 1);
            /*SL:201*/if (a2 != null) {
                /*SL:202*/a2 = Integer.parseInt(a2);
                /*SL:203*/if (a2 > 32767) {
                    /*SL:204*/throw new IllegalArgumentException("Version parts cannot exceed 32767, found " + a2);
                }
                /*SL:206*/v0[v] = (short)a2;
            }
        }
        /*SL:210*/return new VersionNumber(v0, matcher.group(5));
    }
    
    static {
        NONE = new VersionNumber();
        PATTERN = Pattern.compile("^(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5}))?)?)?(-[a-zA-Z0-9_\\-]+)?$");
    }
}
