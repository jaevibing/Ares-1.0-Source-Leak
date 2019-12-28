package com.google.api.client.repackaged.com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Preconditions
{
    public static void checkArgument(final boolean a1) {
        /*SL:107*/if (!a1) {
            /*SL:108*/throw new IllegalArgumentException();
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final Object a2) {
        /*SL:121*/if (!a1) {
            /*SL:122*/throw new IllegalArgumentException(String.valueOf(a2));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object... a3) {
        /*SL:145*/if (!a1) {
            /*SL:146*/throw new IllegalArgumentException(format(a2, a3));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final char a3) {
        /*SL:156*/if (!a1) {
            /*SL:157*/throw new IllegalArgumentException(format(a2, a3));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final int a3) {
        /*SL:167*/if (!a1) {
            /*SL:168*/throw new IllegalArgumentException(format(a2, a3));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final long a3) {
        /*SL:178*/if (!a1) {
            /*SL:179*/throw new IllegalArgumentException(format(a2, a3));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object a3) {
        /*SL:190*/if (!a1) {
            /*SL:191*/throw new IllegalArgumentException(format(a2, a3));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final char a3, final char a4) {
        /*SL:202*/if (!a1) {
            /*SL:203*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final char a3, final int a4) {
        /*SL:214*/if (!a1) {
            /*SL:215*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final char a3, final long a4) {
        /*SL:226*/if (!a1) {
            /*SL:227*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final char a3, @Nullable final Object a4) {
        /*SL:238*/if (!a1) {
            /*SL:239*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final int a3, final char a4) {
        /*SL:250*/if (!a1) {
            /*SL:251*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final int a3, final int a4) {
        /*SL:262*/if (!a1) {
            /*SL:263*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final int a3, final long a4) {
        /*SL:274*/if (!a1) {
            /*SL:275*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final int a3, @Nullable final Object a4) {
        /*SL:286*/if (!a1) {
            /*SL:287*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final long a3, final char a4) {
        /*SL:298*/if (!a1) {
            /*SL:299*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final long a3, final int a4) {
        /*SL:310*/if (!a1) {
            /*SL:311*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final long a3, final long a4) {
        /*SL:322*/if (!a1) {
            /*SL:323*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, final long a3, @Nullable final Object a4) {
        /*SL:334*/if (!a1) {
            /*SL:335*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object a3, final char a4) {
        /*SL:346*/if (!a1) {
            /*SL:347*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object a3, final int a4) {
        /*SL:358*/if (!a1) {
            /*SL:359*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object a3, final long a4) {
        /*SL:370*/if (!a1) {
            /*SL:371*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4) {
        /*SL:382*/if (!a1) {
            /*SL:383*/throw new IllegalArgumentException(format(a2, a3, a4));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4, @Nullable final Object a5) {
        /*SL:398*/if (!a1) {
            /*SL:399*/throw new IllegalArgumentException(format(a2, a3, a4, a5));
        }
    }
    
    public static void checkArgument(final boolean a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4, @Nullable final Object a5, @Nullable final Object a6) {
        /*SL:415*/if (!a1) {
            /*SL:416*/throw new IllegalArgumentException(format(a2, a3, a4, a5, a6));
        }
    }
    
    public static void checkState(final boolean a1) {
        /*SL:428*/if (!a1) {
            /*SL:429*/throw new IllegalStateException();
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final Object a2) {
        /*SL:443*/if (!a1) {
            /*SL:444*/throw new IllegalStateException(String.valueOf(a2));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object... a3) {
        /*SL:468*/if (!a1) {
            /*SL:469*/throw new IllegalStateException(format(a2, a3));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final char a3) {
        /*SL:480*/if (!a1) {
            /*SL:481*/throw new IllegalStateException(format(a2, a3));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final int a3) {
        /*SL:492*/if (!a1) {
            /*SL:493*/throw new IllegalStateException(format(a2, a3));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final long a3) {
        /*SL:504*/if (!a1) {
            /*SL:505*/throw new IllegalStateException(format(a2, a3));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object a3) {
        /*SL:517*/if (!a1) {
            /*SL:518*/throw new IllegalStateException(format(a2, a3));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final char a3, final char a4) {
        /*SL:530*/if (!a1) {
            /*SL:531*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final char a3, final int a4) {
        /*SL:542*/if (!a1) {
            /*SL:543*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final char a3, final long a4) {
        /*SL:555*/if (!a1) {
            /*SL:556*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final char a3, @Nullable final Object a4) {
        /*SL:568*/if (!a1) {
            /*SL:569*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final int a3, final char a4) {
        /*SL:580*/if (!a1) {
            /*SL:581*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final int a3, final int a4) {
        /*SL:592*/if (!a1) {
            /*SL:593*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final int a3, final long a4) {
        /*SL:604*/if (!a1) {
            /*SL:605*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final int a3, @Nullable final Object a4) {
        /*SL:617*/if (!a1) {
            /*SL:618*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final long a3, final char a4) {
        /*SL:630*/if (!a1) {
            /*SL:631*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final long a3, final int a4) {
        /*SL:642*/if (!a1) {
            /*SL:643*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final long a3, final long a4) {
        /*SL:655*/if (!a1) {
            /*SL:656*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, final long a3, @Nullable final Object a4) {
        /*SL:668*/if (!a1) {
            /*SL:669*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object a3, final char a4) {
        /*SL:681*/if (!a1) {
            /*SL:682*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object a3, final int a4) {
        /*SL:694*/if (!a1) {
            /*SL:695*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object a3, final long a4) {
        /*SL:707*/if (!a1) {
            /*SL:708*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4) {
        /*SL:720*/if (!a1) {
            /*SL:721*/throw new IllegalStateException(format(a2, a3, a4));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4, @Nullable final Object a5) {
        /*SL:737*/if (!a1) {
            /*SL:738*/throw new IllegalStateException(format(a2, a3, a4, a5));
        }
    }
    
    public static void checkState(final boolean a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4, @Nullable final Object a5, @Nullable final Object a6) {
        /*SL:755*/if (!a1) {
            /*SL:756*/throw new IllegalStateException(format(a2, a3, a4, a5, a6));
        }
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1) {
        /*SL:769*/if (a1 == null) {
            /*SL:770*/throw new NullPointerException();
        }
        /*SL:772*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final Object a2) {
        /*SL:786*/if (a1 == null) {
            /*SL:787*/throw new NullPointerException(String.valueOf(a2));
        }
        /*SL:789*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object... a3) {
        /*SL:809*/if (a1 == null) {
            /*SL:811*/throw new NullPointerException(format(a2, a3));
        }
        /*SL:813*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final char a3) {
        /*SL:823*/if (a1 == null) {
            /*SL:824*/throw new NullPointerException(format(a2, a3));
        }
        /*SL:826*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final int a3) {
        /*SL:836*/if (a1 == null) {
            /*SL:837*/throw new NullPointerException(format(a2, a3));
        }
        /*SL:839*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final long a3) {
        /*SL:849*/if (a1 == null) {
            /*SL:850*/throw new NullPointerException(format(a2, a3));
        }
        /*SL:852*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object a3) {
        /*SL:863*/if (a1 == null) {
            /*SL:864*/throw new NullPointerException(format(a2, a3));
        }
        /*SL:866*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final char a3, final char a4) {
        /*SL:876*/if (a1 == null) {
            /*SL:877*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:879*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final char a3, final int a4) {
        /*SL:889*/if (a1 == null) {
            /*SL:890*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:892*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final char a3, final long a4) {
        /*SL:902*/if (a1 == null) {
            /*SL:903*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:905*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final char a3, @Nullable final Object a4) {
        /*SL:916*/if (a1 == null) {
            /*SL:917*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:919*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final int a3, final char a4) {
        /*SL:929*/if (a1 == null) {
            /*SL:930*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:932*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final int a3, final int a4) {
        /*SL:942*/if (a1 == null) {
            /*SL:943*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:945*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final int a3, final long a4) {
        /*SL:955*/if (a1 == null) {
            /*SL:956*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:958*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final int a3, @Nullable final Object a4) {
        /*SL:969*/if (a1 == null) {
            /*SL:970*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:972*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final long a3, final char a4) {
        /*SL:982*/if (a1 == null) {
            /*SL:983*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:985*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final long a3, final int a4) {
        /*SL:995*/if (a1 == null) {
            /*SL:996*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:998*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final long a3, final long a4) {
        /*SL:1008*/if (a1 == null) {
            /*SL:1009*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:1011*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, final long a3, @Nullable final Object a4) {
        /*SL:1022*/if (a1 == null) {
            /*SL:1023*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:1025*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object a3, final char a4) {
        /*SL:1036*/if (a1 == null) {
            /*SL:1037*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:1039*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object a3, final int a4) {
        /*SL:1050*/if (a1 == null) {
            /*SL:1051*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:1053*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object a3, final long a4) {
        /*SL:1064*/if (a1 == null) {
            /*SL:1065*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:1067*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4) {
        /*SL:1078*/if (a1 == null) {
            /*SL:1079*/throw new NullPointerException(format(a2, a3, a4));
        }
        /*SL:1081*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4, @Nullable final Object a5) {
        /*SL:1096*/if (a1 == null) {
            /*SL:1097*/throw new NullPointerException(format(a2, a3, a4, a5));
        }
        /*SL:1099*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static <T> T checkNotNull(final T a1, @Nullable final String a2, @Nullable final Object a3, @Nullable final Object a4, @Nullable final Object a5, @Nullable final Object a6) {
        /*SL:1115*/if (a1 == null) {
            /*SL:1116*/throw new NullPointerException(format(a2, a3, a4, a5, a6));
        }
        /*SL:1118*/return a1;
    }
    
    @CanIgnoreReturnValue
    public static int checkElementIndex(final int a1, final int a2) {
        /*SL:1159*/return checkElementIndex(a1, a2, "index");
    }
    
    @CanIgnoreReturnValue
    public static int checkElementIndex(final int a1, final int a2, @Nullable final String a3) {
        /*SL:1176*/if (a1 < 0 || a1 >= a2) {
            /*SL:1177*/throw new IndexOutOfBoundsException(badElementIndex(a1, a2, a3));
        }
        /*SL:1179*/return a1;
    }
    
    private static String badElementIndex(final int a1, final int a2, final String a3) {
        /*SL:1183*/if (a1 < 0) {
            /*SL:1184*/return format("%s (%s) must not be negative", a3, a1);
        }
        /*SL:1185*/if (a2 < 0) {
            /*SL:1186*/throw new IllegalArgumentException("negative size: " + a2);
        }
        /*SL:1188*/return format("%s (%s) must be less than size (%s)", a3, a1, a2);
    }
    
    @CanIgnoreReturnValue
    public static int checkPositionIndex(final int a1, final int a2) {
        /*SL:1204*/return checkPositionIndex(a1, a2, "index");
    }
    
    @CanIgnoreReturnValue
    public static int checkPositionIndex(final int a1, final int a2, @Nullable final String a3) {
        /*SL:1221*/if (a1 < 0 || a1 > a2) {
            /*SL:1222*/throw new IndexOutOfBoundsException(badPositionIndex(a1, a2, a3));
        }
        /*SL:1224*/return a1;
    }
    
    private static String badPositionIndex(final int a1, final int a2, final String a3) {
        /*SL:1228*/if (a1 < 0) {
            /*SL:1229*/return format("%s (%s) must not be negative", a3, a1);
        }
        /*SL:1230*/if (a2 < 0) {
            /*SL:1231*/throw new IllegalArgumentException("negative size: " + a2);
        }
        /*SL:1233*/return format("%s (%s) must not be greater than size (%s)", a3, a1, a2);
    }
    
    public static void checkPositionIndexes(final int a1, final int a2, final int a3) {
        /*SL:1251*/if (a1 < 0 || a2 < a1 || a2 > a3) {
            /*SL:1252*/throw new IndexOutOfBoundsException(badPositionIndexes(a1, a2, a3));
        }
    }
    
    private static String badPositionIndexes(final int a1, final int a2, final int a3) {
        /*SL:1257*/if (a1 < 0 || a1 > a3) {
            /*SL:1258*/return badPositionIndex(a1, a3, "start index");
        }
        /*SL:1260*/if (a2 < 0 || a2 > a3) {
            /*SL:1261*/return badPositionIndex(a2, a3, "end index");
        }
        /*SL:1264*/return format("end index (%s) must not be less than start index (%s)", a2, a1);
    }
    
    static String format(String a2, @Nullable final Object... v1) {
        /*SL:1279*/a2 = String.valueOf(a2);
        final StringBuilder v2 = /*EL:1282*/new StringBuilder(a2.length() + 16 * v1.length);
        int v3 = /*EL:1283*/0;
        int v4 = /*EL:1284*/0;
        /*SL:1285*/while (v4 < v1.length) {
            final int a3 = /*EL:1286*/a2.indexOf("%s", v3);
            /*SL:1287*/if (a3 == -1) {
                /*SL:1288*/break;
            }
            /*SL:1290*/v2.append(a2, v3, a3);
            /*SL:1291*/v2.append(v1[v4++]);
            /*SL:1292*/v3 = a3 + 2;
        }
        /*SL:1294*/v2.append(a2, v3, a2.length());
        /*SL:1297*/if (v4 < v1.length) {
            /*SL:1298*/v2.append(" [");
            /*SL:1299*/v2.append(v1[v4++]);
            /*SL:1300*/while (v4 < v1.length) {
                /*SL:1301*/v2.append(", ");
                /*SL:1302*/v2.append(v1[v4++]);
            }
            /*SL:1304*/v2.append(']');
        }
        /*SL:1307*/return v2.toString();
    }
}
