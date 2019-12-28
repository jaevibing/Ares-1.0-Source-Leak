package com.google.api.client.util;

import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.TimeZone;
import java.io.Serializable;

public final class DateTime implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final TimeZone GMT;
    private static final Pattern RFC3339_PATTERN;
    private final long value;
    private final boolean dateOnly;
    private final int tzShift;
    
    public DateTime(final Date a1, final TimeZone a2) {
        this(false, a1.getTime(), (a2 == null) ? null : (a2.getOffset(a1.getTime()) / 60000));
    }
    
    public DateTime(final long a1) {
        this(false, a1, null);
    }
    
    public DateTime(final Date a1) {
        this(a1.getTime());
    }
    
    public DateTime(final long a1, final int a2) {
        this(false, a1, a2);
    }
    
    public DateTime(final boolean a1, final long a2, final Integer a3) {
        this.dateOnly = a1;
        this.value = a2;
        this.tzShift = (a1 ? 0 : ((a3 == null) ? (TimeZone.getDefault().getOffset(a2) / 60000) : a3));
    }
    
    public DateTime(final String a1) {
        final DateTime v1 = parseRfc3339(a1);
        this.dateOnly = v1.dateOnly;
        this.value = v1.value;
        this.tzShift = v1.tzShift;
    }
    
    public long getValue() {
        /*SL:167*/return this.value;
    }
    
    public boolean isDateOnly() {
        /*SL:176*/return this.dateOnly;
    }
    
    public int getTimeZoneShift() {
        /*SL:185*/return this.tzShift;
    }
    
    public String toStringRfc3339() {
        final StringBuilder a2 = /*EL:190*/new StringBuilder();
        final Calendar calendar = /*EL:191*/new GregorianCalendar(DateTime.GMT);
        final long timeInMillis = /*EL:192*/this.value + this.tzShift * 60000L;
        /*SL:193*/calendar.setTimeInMillis(timeInMillis);
        appendInt(/*EL:195*/a2, calendar.get(1), 4);
        /*SL:196*/a2.append('-');
        appendInt(/*EL:197*/a2, calendar.get(2) + 1, 2);
        /*SL:198*/a2.append('-');
        appendInt(/*EL:199*/a2, calendar.get(5), 2);
        /*SL:200*/if (!this.dateOnly) {
            /*SL:202*/a2.append('T');
            appendInt(/*EL:203*/a2, calendar.get(11), 2);
            /*SL:204*/a2.append(':');
            appendInt(/*EL:205*/a2, calendar.get(12), 2);
            /*SL:206*/a2.append(':');
            appendInt(/*EL:207*/a2, calendar.get(13), 2);
            /*SL:209*/if (calendar.isSet(14)) {
                /*SL:210*/a2.append('.');
                appendInt(/*EL:211*/a2, calendar.get(14), 3);
            }
            /*SL:214*/if (this.tzShift == 0) {
                /*SL:215*/a2.append('Z');
            }
            else {
                int v1 = /*EL:217*/this.tzShift;
                /*SL:218*/if (this.tzShift > 0) {
                    /*SL:219*/a2.append('+');
                }
                else {
                    /*SL:221*/a2.append('-');
                    /*SL:222*/v1 = -v1;
                }
                final int v2 = /*EL:225*/v1 / 60;
                final int v3 = /*EL:226*/v1 % 60;
                appendInt(/*EL:227*/a2, v2, 2);
                /*SL:228*/a2.append(':');
                appendInt(/*EL:229*/a2, v3, 2);
            }
        }
        /*SL:232*/return a2.toString();
    }
    
    @Override
    public String toString() {
        /*SL:237*/return this.toStringRfc3339();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:250*/if (a1 == this) {
            /*SL:251*/return true;
        }
        /*SL:253*/if (!(a1 instanceof DateTime)) {
            /*SL:254*/return false;
        }
        final DateTime v1 = /*EL:256*/(DateTime)a1;
        /*SL:257*/return this.dateOnly == v1.dateOnly && this.value == v1.value && this.tzShift == v1.tzShift;
    }
    
    @Override
    public int hashCode() {
        /*SL:262*/return Arrays.hashCode(new long[] { this.value, this.dateOnly, this.tzShift });
    }
    
    public static DateTime parseRfc3339(final String v-15) throws NumberFormatException {
        final Matcher matcher = DateTime.RFC3339_PATTERN.matcher(/*EL:288*/v-15);
        /*SL:289*/if (!matcher.matches()) {
            /*SL:290*/throw new NumberFormatException("Invalid date/time format: " + v-15);
        }
        final int int1 = /*EL:293*/Integer.parseInt(matcher.group(1));
        final int n = /*EL:294*/Integer.parseInt(matcher.group(2)) - 1;
        final int int2 = /*EL:295*/Integer.parseInt(matcher.group(3));
        final boolean b = /*EL:296*/matcher.group(4) != null;
        final String group = /*EL:297*/matcher.group(9);
        final boolean b2 = /*EL:298*/group != null;
        int int3 = /*EL:299*/0;
        int int4 = /*EL:300*/0;
        int int5 = /*EL:301*/0;
        int int6 = /*EL:302*/0;
        Integer value = /*EL:303*/null;
        /*SL:305*/if (b2 && !b) {
            /*SL:306*/throw new NumberFormatException("Invalid date/time format, cannot specify time zone shift without specifying time: " + v-15);
        }
        /*SL:310*/if (b) {
            /*SL:311*/int3 = Integer.parseInt(matcher.group(5));
            /*SL:312*/int4 = Integer.parseInt(matcher.group(6));
            /*SL:313*/int5 = Integer.parseInt(matcher.group(7));
            /*SL:314*/if (matcher.group(8) != null) {
                /*SL:315*/int6 = Integer.parseInt(matcher.group(8).substring(1));
                final int a1 = /*EL:317*/matcher.group(8).substring(1).length() - 3;
                /*SL:318*/int6 /= (int)Math.pow(10.0, a1);
            }
        }
        final Calendar calendar = /*EL:321*/new GregorianCalendar(DateTime.GMT);
        /*SL:322*/calendar.set(int1, n, int2, int3, int4, int5);
        /*SL:323*/calendar.set(14, int6);
        long timeInMillis = /*EL:324*/calendar.getTimeInMillis();
        /*SL:326*/if (b && b2) {
            int v1;
            /*SL:328*/if (Character.toUpperCase(group.charAt(0)) == 'Z') {
                /*SL:329*/v1 = 0;
            }
            else {
                /*SL:332*/v1 = Integer.parseInt(matcher.group(11)) * 60 + Integer.parseInt(matcher.group(12));
                /*SL:333*/if (matcher.group(10).charAt(0) == '-') {
                    /*SL:334*/v1 = -v1;
                }
                /*SL:336*/timeInMillis -= v1 * 60000L;
            }
            /*SL:338*/value = v1;
        }
        /*SL:340*/return new DateTime(!b, timeInMillis, value);
    }
    
    private static void appendInt(final StringBuilder a2, int a3, int v1) {
        /*SL:345*/if (a3 < 0) {
            /*SL:346*/a2.append('-');
            /*SL:347*/a3 = -a3;
        }
        /*SL:350*/for (int v2 = a3; v2 > 0; /*SL:351*/v2 /= 10, /*SL:352*/--v1) {}
        /*SL:354*/for (int a4 = 0; a4 < v1; ++a4) {
            /*SL:355*/a2.append('0');
        }
        /*SL:357*/if (a3 != 0) {
            /*SL:358*/a2.append(a3);
        }
    }
    
    static {
        GMT = TimeZone.getTimeZone("GMT");
        RFC3339_PATTERN = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})([Tt](\\d{2}):(\\d{2}):(\\d{2})(\\.\\d+)?)?([Zz]|([+-])(\\d{2}):(\\d{2}))?");
    }
}
