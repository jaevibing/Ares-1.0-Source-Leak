package com.google.api.client.repackaged.com.google.common.base;

import com.google.api.client.repackaged.com.google.common.annotations.VisibleForTesting;
import java.util.Arrays;
import com.google.api.client.repackaged.com.google.common.annotations.GwtIncompatible;
import java.util.BitSet;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class CharMatcher implements Predicate<Character>
{
    @Deprecated
    public static final CharMatcher WHITESPACE;
    @Deprecated
    public static final CharMatcher BREAKING_WHITESPACE;
    @Deprecated
    public static final CharMatcher ASCII;
    @Deprecated
    public static final CharMatcher DIGIT;
    @Deprecated
    public static final CharMatcher JAVA_DIGIT;
    @Deprecated
    public static final CharMatcher JAVA_LETTER;
    @Deprecated
    public static final CharMatcher JAVA_LETTER_OR_DIGIT;
    @Deprecated
    public static final CharMatcher JAVA_UPPER_CASE;
    @Deprecated
    public static final CharMatcher JAVA_LOWER_CASE;
    @Deprecated
    public static final CharMatcher JAVA_ISO_CONTROL;
    @Deprecated
    public static final CharMatcher INVISIBLE;
    @Deprecated
    public static final CharMatcher SINGLE_WIDTH;
    @Deprecated
    public static final CharMatcher ANY;
    @Deprecated
    public static final CharMatcher NONE;
    private static final int DISTINCT_CHARS = 65536;
    
    public static CharMatcher any() {
        /*SL:108*/return Any.INSTANCE;
    }
    
    public static CharMatcher none() {
        /*SL:117*/return None.INSTANCE;
    }
    
    public static CharMatcher whitespace() {
        /*SL:134*/return Whitespace.INSTANCE;
    }
    
    public static CharMatcher breakingWhitespace() {
        /*SL:145*/return BreakingWhitespace.INSTANCE;
    }
    
    public static CharMatcher ascii() {
        /*SL:154*/return Ascii.INSTANCE;
    }
    
    public static CharMatcher digit() {
        /*SL:165*/return Digit.INSTANCE;
    }
    
    public static CharMatcher javaDigit() {
        /*SL:176*/return JavaDigit.INSTANCE;
    }
    
    public static CharMatcher javaLetter() {
        /*SL:187*/return JavaLetter.INSTANCE;
    }
    
    public static CharMatcher javaLetterOrDigit() {
        /*SL:197*/return JavaLetterOrDigit.INSTANCE;
    }
    
    public static CharMatcher javaUpperCase() {
        /*SL:207*/return JavaUpperCase.INSTANCE;
    }
    
    public static CharMatcher javaLowerCase() {
        /*SL:217*/return JavaLowerCase.INSTANCE;
    }
    
    public static CharMatcher javaIsoControl() {
        /*SL:227*/return JavaIsoControl.INSTANCE;
    }
    
    public static CharMatcher invisible() {
        /*SL:238*/return Invisible.INSTANCE;
    }
    
    public static CharMatcher singleWidth() {
        /*SL:252*/return SingleWidth.INSTANCE;
    }
    
    public static CharMatcher is(final char a1) {
        /*SL:420*/return new Is(a1);
    }
    
    public static CharMatcher isNot(final char a1) {
        /*SL:429*/return new IsNot(a1);
    }
    
    public static CharMatcher anyOf(final CharSequence a1) {
        /*SL:437*/switch (a1.length()) {
            case 0: {
                /*SL:439*/return none();
            }
            case 1: {
                /*SL:441*/return is(a1.charAt(0));
            }
            case 2: {
                /*SL:443*/return isEither(a1.charAt(0), a1.charAt(1));
            }
            default: {
                /*SL:447*/return new AnyOf(a1);
            }
        }
    }
    
    public static CharMatcher noneOf(final CharSequence a1) {
        /*SL:456*/return anyOf(a1).negate();
    }
    
    public static CharMatcher inRange(final char a1, final char a2) {
        /*SL:467*/return new InRange(a1, a2);
    }
    
    public static CharMatcher forPredicate(final Predicate<? super Character> a1) {
        /*SL:475*/return (a1 instanceof CharMatcher) ? ((CharMatcher)a1) : new ForPredicate(a1);
    }
    
    public abstract boolean matches(final char p0);
    
    public CharMatcher negate() {
        /*SL:497*/return new Negated(this);
    }
    
    public CharMatcher and(final CharMatcher a1) {
        /*SL:504*/return new And(this, a1);
    }
    
    public CharMatcher or(final CharMatcher a1) {
        /*SL:511*/return new Or(this, a1);
    }
    
    public CharMatcher precomputed() {
        /*SL:524*/return Platform.precomputeCharMatcher(this);
    }
    
    @GwtIncompatible
    CharMatcher precomputedInternal() {
        final BitSet v5 = /*EL:541*/new BitSet();
        /*SL:542*/this.setBits(v5);
        final int v0 = /*EL:543*/v5.cardinality();
        /*SL:544*/if (v0 * 2 <= 65536) {
            /*SL:545*/return precomputedPositive(v0, v5, this.toString());
        }
        /*SL:548*/v5.flip(0, 65536);
        final int v = /*EL:549*/65536 - v0;
        final String v2 = /*EL:550*/".negate()";
        final String v3 = /*EL:551*/this.toString();
        final String v4 = /*EL:552*/v3.endsWith(v2) ? v3.substring(0, v3.length() - v2.length()) : (v3 + v2);
        /*SL:556*/return new NegatedFastMatcher(precomputedPositive(v, v5, v4)) {
            @Override
            public String toString() {
                /*SL:560*/return v3;
            }
        };
    }
    
    @GwtIncompatible
    private static CharMatcher precomputedPositive(final int a3, final BitSet v1, final String v2) {
        /*SL:572*/switch (a3) {
            case 0: {
                /*SL:574*/return none();
            }
            case 1: {
                /*SL:576*/return is((char)v1.nextSetBit(0));
            }
            case 2: {
                final char a4 = /*EL:578*/(char)v1.nextSetBit(0);
                final char a5 = /*EL:579*/(char)v1.nextSetBit(a4 + '\u0001');
                /*SL:580*/return isEither(a4, a5);
            }
            default: {
                /*SL:582*/return isSmall(a3, v1.length()) ? SmallCharMatcher.from(v1, v2) : new BitSetMatcher(v1, v2);
            }
        }
    }
    
    @GwtIncompatible
    private static boolean isSmall(final int a1, final int a2) {
        /*SL:590*/return a1 <= 1023 && a2 > a1 * 4 * 16;
    }
    
    @GwtIncompatible
    void setBits(final BitSet v2) {
        /*SL:600*/for (int a1 = 65535; a1 >= 0; --a1) {
            /*SL:601*/if (this.matches((char)a1)) {
                /*SL:602*/v2.set(a1);
            }
        }
    }
    
    public boolean matchesAnyOf(final CharSequence a1) {
        /*SL:621*/return !this.matchesNoneOf(a1);
    }
    
    public boolean matchesAllOf(final CharSequence v2) {
        /*SL:635*/for (int a1 = v2.length() - 1; a1 >= 0; --a1) {
            /*SL:636*/if (!this.matches(v2.charAt(a1))) {
                /*SL:637*/return false;
            }
        }
        /*SL:640*/return true;
    }
    
    public boolean matchesNoneOf(final CharSequence a1) {
        /*SL:655*/return this.indexIn(a1) == -1;
    }
    
    public int indexIn(final CharSequence a1) {
        /*SL:669*/return this.indexIn(a1, 0);
    }
    
    public int indexIn(final CharSequence v1, final int v2) {
        final int v3 = /*EL:688*/v1.length();
        /*SL:689*/Preconditions.checkPositionIndex(v2, v3);
        /*SL:690*/for (int a1 = v2; a1 < v3; ++a1) {
            /*SL:691*/if (this.matches(v1.charAt(a1))) {
                /*SL:692*/return a1;
            }
        }
        /*SL:695*/return -1;
    }
    
    public int lastIndexIn(final CharSequence v2) {
        /*SL:709*/for (int a1 = v2.length() - 1; a1 >= 0; --a1) {
            /*SL:710*/if (this.matches(v2.charAt(a1))) {
                /*SL:711*/return a1;
            }
        }
        /*SL:714*/return -1;
    }
    
    public int countIn(final CharSequence v2) {
        int v3 = /*EL:721*/0;
        /*SL:722*/for (int a1 = 0; a1 < v2.length(); ++a1) {
            /*SL:723*/if (this.matches(v2.charAt(a1))) {
                /*SL:724*/++v3;
            }
        }
        /*SL:727*/return v3;
    }
    
    public String removeFrom(final CharSequence a1) {
        final String v1 = /*EL:739*/a1.toString();
        int v2 = /*EL:740*/this.indexIn(v1);
        /*SL:741*/if (v2 == -1) {
            /*SL:742*/return v1;
        }
        final char[] v3 = /*EL:745*/v1.toCharArray();
        int v4 = /*EL:746*/1;
    Label_0029:
        while (true) {
            /*SL:751*/++v2;
            /*SL:753*/while (v2 != v3.length) {
                /*SL:756*/if (this.matches(v3[v2])) {
                    /*SL:762*/++v4;
                    continue Label_0029;
                }
                v3[v2 - v4] = v3[v2];
                ++v2;
            }
            break;
        }
        /*SL:764*/return new String(v3, 0, v2 - v4);
    }
    
    public String retainFrom(final CharSequence a1) {
        /*SL:776*/return this.negate().removeFrom(a1);
    }
    
    public String replaceFrom(final CharSequence v1, final char v2) {
        final String v3 = /*EL:797*/v1.toString();
        final int v4 = /*EL:798*/this.indexIn(v3);
        /*SL:799*/if (v4 == -1) {
            /*SL:800*/return v3;
        }
        final char[] v5 = /*EL:802*/v3.toCharArray();
        /*SL:803*/v5[v4] = v2;
        /*SL:804*/for (int a1 = v4 + 1; a1 < v5.length; ++a1) {
            /*SL:805*/if (this.matches(v5[a1])) {
                /*SL:806*/v5[a1] = v2;
            }
        }
        /*SL:809*/return new String(v5);
    }
    
    public String replaceFrom(final CharSequence a1, final CharSequence a2) {
        final int v1 = /*EL:829*/a2.length();
        /*SL:830*/if (v1 == 0) {
            /*SL:831*/return this.removeFrom(a1);
        }
        /*SL:833*/if (v1 == 1) {
            /*SL:834*/return this.replaceFrom(a1, a2.charAt(0));
        }
        final String v2 = /*EL:837*/a1.toString();
        int v3 = /*EL:838*/this.indexIn(v2);
        /*SL:839*/if (v3 == -1) {
            /*SL:840*/return v2;
        }
        final int v4 = /*EL:843*/v2.length();
        final StringBuilder v5 = /*EL:844*/new StringBuilder(v4 * 3 / 2 + 16);
        int v6 = /*EL:846*/0;
        /*SL:852*/do {
            v5.append(v2, v6, v3);
            v5.append(a2);
            v6 = v3 + 1;
            v3 = this.indexIn(v2, v6);
        } while (v3 != -1);
        /*SL:854*/v5.append(v2, v6, v4);
        /*SL:855*/return v5.toString();
    }
    
    public String trimFrom(final CharSequence a1) {
        int v1;
        int v2;
        for (/*SL:873*/v1 = a1.length(), /*SL:877*/v2 = 0; v2 < v1 && /*EL:878*/this.matches(a1.charAt(v2)); ++v2) {}
        int v3;
        for (/*SL:882*/v3 = v1 - 1; v3 > v2 && /*EL:883*/this.matches(a1.charAt(v3)); --v3) {}
        /*SL:888*/return a1.subSequence(v2, v3 + 1).toString();
    }
    
    public String trimLeadingFrom(final CharSequence v2) {
        /*SL:901*/for (int v3 = v2.length(), a1 = 0; a1 < v3; ++a1) {
            /*SL:902*/if (!this.matches(v2.charAt(a1))) {
                /*SL:903*/return v2.subSequence(a1, v3).toString();
            }
        }
        /*SL:906*/return "";
    }
    
    public String trimTrailingFrom(final CharSequence v2) {
        final int v3 = /*EL:918*/v2.length();
        /*SL:919*/for (int a1 = v3 - 1; a1 >= 0; --a1) {
            /*SL:920*/if (!this.matches(v2.charAt(a1))) {
                /*SL:921*/return v2.subSequence(0, a1 + 1).toString();
            }
        }
        /*SL:924*/return "";
    }
    
    public String collapseFrom(final CharSequence v-2, final char v-1) {
        /*SL:948*/for (int v0 = v-2.length(), v = 0; v < v0; ++v) {
            char a2 = /*EL:949*/v-2.charAt(v);
            /*SL:950*/if (this.matches(a2)) {
                /*SL:951*/if (a2 != v-1 || (v != v0 - 1 && this.matches(v-2.charAt(v + 1)))) {
                    /*SL:955*/a2 = new StringBuilder(v0).append(v-2, 0, v).append(v-1);
                    /*SL:956*/return this.finishCollapseFrom(v-2, v + 1, v0, v-1, a2, true);
                }
                ++v;
            }
        }
        /*SL:961*/return v-2.toString();
    }
    
    public String trimAndCollapseFrom(final CharSequence a1, final char a2) {
        final int v1 = /*EL:971*/a1.length();
        int v2 = /*EL:972*/0;
        int v3 = /*EL:973*/v1 - 1;
        /*SL:975*/while (v2 < v1 && this.matches(a1.charAt(v2))) {
            /*SL:976*/++v2;
        }
        /*SL:979*/while (v3 > v2 && this.matches(a1.charAt(v3))) {
            /*SL:980*/--v3;
        }
        /*SL:983*/return (v2 == 0 && v3 == v1 - 1) ? this.collapseFrom(a1, a2) : this.finishCollapseFrom(a1, v2, v3 + 1, a2, new StringBuilder(v3 + 1 - v2), false);
    }
    
    private String finishCollapseFrom(final CharSequence a4, final int a5, final int a6, final char v1, final StringBuilder v2, boolean v3) {
        /*SL:996*/for (int a7 = a5; a7 < a6; ++a7) {
            final char a8 = /*EL:997*/a4.charAt(a7);
            /*SL:998*/if (this.matches(a8)) {
                /*SL:999*/if (!v3) {
                    /*SL:1000*/v2.append(v1);
                    /*SL:1001*/v3 = true;
                }
            }
            else {
                /*SL:1004*/v2.append(a8);
                /*SL:1005*/v3 = false;
            }
        }
        /*SL:1008*/return v2.toString();
    }
    
    @Deprecated
    @Override
    public boolean apply(final Character a1) {
        /*SL:1018*/return this.matches(a1);
    }
    
    @Override
    public String toString() {
        /*SL:1027*/return super.toString();
    }
    
    private static String showCharacter(char v1) {
        final String v2 = /*EL:1035*/"0123456789ABCDEF";
        final char[] v3 = /*EL:1036*/{ '\\', 'u', '\0', '\0', '\0', '\0' };
        /*SL:1037*/for (int a1 = 0; a1 < 4; ++a1) {
            /*SL:1038*/v3[5 - a1] = v2.charAt(v1 & '\u000f');
            /*SL:1039*/v1 >>= 4;
        }
        /*SL:1041*/return String.copyValueOf(v3);
    }
    
    private static IsEither isEither(final char a1, final char a2) {
        /*SL:1791*/return new IsEither(a1, a2);
    }
    
    static {
        WHITESPACE = whitespace();
        BREAKING_WHITESPACE = breakingWhitespace();
        ASCII = ascii();
        DIGIT = digit();
        JAVA_DIGIT = javaDigit();
        JAVA_LETTER = javaLetter();
        JAVA_LETTER_OR_DIGIT = javaLetterOrDigit();
        JAVA_UPPER_CASE = javaUpperCase();
        JAVA_LOWER_CASE = javaLowerCase();
        JAVA_ISO_CONTROL = javaIsoControl();
        INVISIBLE = invisible();
        SINGLE_WIDTH = singleWidth();
        ANY = any();
        NONE = none();
    }
    
    abstract static class FastMatcher extends CharMatcher
    {
        @Override
        public final CharMatcher precomputed() {
            /*SL:1051*/return this;
        }
        
        @Override
        public CharMatcher negate() {
            /*SL:1056*/return new NegatedFastMatcher(this);
        }
    }
    
    abstract static class NamedFastMatcher extends FastMatcher
    {
        private final String description;
        
        NamedFastMatcher(final String a1) {
            this.description = Preconditions.<String>checkNotNull(a1);
        }
        
        @Override
        public final String toString() {
            /*SL:1071*/return this.description;
        }
    }
    
    static class NegatedFastMatcher extends Negated
    {
        NegatedFastMatcher(final CharMatcher a1) {
            super(a1);
        }
        
        @Override
        public final CharMatcher precomputed() {
            /*SL:1084*/return this;
        }
    }
    
    @GwtIncompatible
    private static final class BitSetMatcher extends NamedFastMatcher
    {
        private final BitSet table;
        
        private BitSetMatcher(BitSet a1, final String a2) {
            super(a2);
            if (a1.length() + 64 < a1.size()) {
                a1 = (BitSet)a1.clone();
            }
            this.table = a1;
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1105*/return this.table.get(a1);
        }
        
        @Override
        void setBits(final BitSet a1) {
            /*SL:1110*/a1.or(this.table);
        }
    }
    
    private static final class Any extends NamedFastMatcher
    {
        static final Any INSTANCE;
        
        private Any() {
            super("CharMatcher.any()");
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1127*/return true;
        }
        
        @Override
        public int indexIn(final CharSequence a1) {
            /*SL:1132*/return (a1.length() == 0) ? -1 : 0;
        }
        
        @Override
        public int indexIn(final CharSequence a1, final int a2) {
            final int v1 = /*EL:1137*/a1.length();
            /*SL:1138*/Preconditions.checkPositionIndex(a2, v1);
            /*SL:1139*/return (a2 == v1) ? -1 : a2;
        }
        
        @Override
        public int lastIndexIn(final CharSequence a1) {
            /*SL:1144*/return a1.length() - 1;
        }
        
        @Override
        public boolean matchesAllOf(final CharSequence a1) {
            /*SL:1149*/Preconditions.<CharSequence>checkNotNull(a1);
            /*SL:1150*/return true;
        }
        
        @Override
        public boolean matchesNoneOf(final CharSequence a1) {
            /*SL:1155*/return a1.length() == 0;
        }
        
        @Override
        public String removeFrom(final CharSequence a1) {
            /*SL:1160*/Preconditions.<CharSequence>checkNotNull(a1);
            /*SL:1161*/return "";
        }
        
        @Override
        public String replaceFrom(final CharSequence a1, final char a2) {
            final char[] v1 = /*EL:1166*/new char[a1.length()];
            /*SL:1167*/Arrays.fill(v1, a2);
            /*SL:1168*/return new String(v1);
        }
        
        @Override
        public String replaceFrom(final CharSequence v1, final CharSequence v2) {
            final StringBuilder v3 = /*EL:1173*/new StringBuilder(v1.length() * v2.length());
            /*SL:1174*/for (int a1 = 0; a1 < v1.length(); ++a1) {
                /*SL:1175*/v3.append(v2);
            }
            /*SL:1177*/return v3.toString();
        }
        
        @Override
        public String collapseFrom(final CharSequence a1, final char a2) {
            /*SL:1182*/return (a1.length() == 0) ? "" : String.valueOf(a2);
        }
        
        @Override
        public String trimFrom(final CharSequence a1) {
            /*SL:1187*/Preconditions.<CharSequence>checkNotNull(a1);
            /*SL:1188*/return "";
        }
        
        @Override
        public int countIn(final CharSequence a1) {
            /*SL:1193*/return a1.length();
        }
        
        @Override
        public CharMatcher and(final CharMatcher a1) {
            /*SL:1198*/return Preconditions.<CharMatcher>checkNotNull(a1);
        }
        
        @Override
        public CharMatcher or(final CharMatcher a1) {
            /*SL:1203*/Preconditions.<CharMatcher>checkNotNull(a1);
            /*SL:1204*/return this;
        }
        
        @Override
        public CharMatcher negate() {
            /*SL:1209*/return CharMatcher.none();
        }
        
        static {
            INSTANCE = new Any();
        }
    }
    
    private static final class None extends NamedFastMatcher
    {
        static final None INSTANCE;
        
        private None() {
            super("CharMatcher.none()");
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1224*/return false;
        }
        
        @Override
        public int indexIn(final CharSequence a1) {
            /*SL:1229*/Preconditions.<CharSequence>checkNotNull(a1);
            /*SL:1230*/return -1;
        }
        
        @Override
        public int indexIn(final CharSequence a1, final int a2) {
            final int v1 = /*EL:1235*/a1.length();
            /*SL:1236*/Preconditions.checkPositionIndex(a2, v1);
            /*SL:1237*/return -1;
        }
        
        @Override
        public int lastIndexIn(final CharSequence a1) {
            /*SL:1242*/Preconditions.<CharSequence>checkNotNull(a1);
            /*SL:1243*/return -1;
        }
        
        @Override
        public boolean matchesAllOf(final CharSequence a1) {
            /*SL:1248*/return a1.length() == 0;
        }
        
        @Override
        public boolean matchesNoneOf(final CharSequence a1) {
            /*SL:1253*/Preconditions.<CharSequence>checkNotNull(a1);
            /*SL:1254*/return true;
        }
        
        @Override
        public String removeFrom(final CharSequence a1) {
            /*SL:1259*/return a1.toString();
        }
        
        @Override
        public String replaceFrom(final CharSequence a1, final char a2) {
            /*SL:1264*/return a1.toString();
        }
        
        @Override
        public String replaceFrom(final CharSequence a1, final CharSequence a2) {
            /*SL:1269*/Preconditions.<CharSequence>checkNotNull(a2);
            /*SL:1270*/return a1.toString();
        }
        
        @Override
        public String collapseFrom(final CharSequence a1, final char a2) {
            /*SL:1275*/return a1.toString();
        }
        
        @Override
        public String trimFrom(final CharSequence a1) {
            /*SL:1280*/return a1.toString();
        }
        
        @Override
        public String trimLeadingFrom(final CharSequence a1) {
            /*SL:1285*/return a1.toString();
        }
        
        @Override
        public String trimTrailingFrom(final CharSequence a1) {
            /*SL:1290*/return a1.toString();
        }
        
        @Override
        public int countIn(final CharSequence a1) {
            /*SL:1295*/Preconditions.<CharSequence>checkNotNull(a1);
            /*SL:1296*/return 0;
        }
        
        @Override
        public CharMatcher and(final CharMatcher a1) {
            /*SL:1301*/Preconditions.<CharMatcher>checkNotNull(a1);
            /*SL:1302*/return this;
        }
        
        @Override
        public CharMatcher or(final CharMatcher a1) {
            /*SL:1307*/return Preconditions.<CharMatcher>checkNotNull(a1);
        }
        
        @Override
        public CharMatcher negate() {
            /*SL:1312*/return CharMatcher.any();
        }
        
        static {
            INSTANCE = new None();
        }
    }
    
    @VisibleForTesting
    static final class Whitespace extends NamedFastMatcher
    {
        static final String TABLE = "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000";
        static final int MULTIPLIER = 1682554634;
        static final int SHIFT;
        static final Whitespace INSTANCE;
        
        Whitespace() {
            super("CharMatcher.whitespace()");
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1336*/return "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".charAt(1682554634 * a1 >>> Whitespace.SHIFT) == a1;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet v2) {
            /*SL:1342*/for (int a1 = 0; a1 < "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".length(); ++a1) {
                /*SL:1343*/v2.set("\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".charAt(a1));
            }
        }
        
        static {
            SHIFT = Integer.numberOfLeadingZeros("\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".length() - 1);
            INSTANCE = new Whitespace();
        }
    }
    
    private static final class BreakingWhitespace extends CharMatcher
    {
        static final CharMatcher INSTANCE;
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1355*/switch (a1) {
                case '\t':
                case '\n':
                case '\u000b':
                case '\f':
                case '\r':
                case ' ':
                case '\u0085':
                case '\u1680':
                case '\u2028':
                case '\u2029':
                case '\u205f':
                case '\u3000': {
                    /*SL:1368*/return true;
                }
                case '\u2007': {
                    /*SL:1370*/return false;
                }
                default: {
                    /*SL:1372*/return a1 >= '\u2000' && a1 <= '\u200a';
                }
            }
        }
        
        @Override
        public String toString() {
            /*SL:1378*/return "CharMatcher.breakingWhitespace()";
        }
        
        static {
            INSTANCE = new BreakingWhitespace();
        }
    }
    
    private static final class Ascii extends NamedFastMatcher
    {
        static final Ascii INSTANCE;
        
        Ascii() {
            super("CharMatcher.ascii()");
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1393*/return a1 <= '\u007f';
        }
        
        static {
            INSTANCE = new Ascii();
        }
    }
    
    private static class RangesMatcher extends CharMatcher
    {
        private final String description;
        private final char[] rangeStarts;
        private final char[] rangeEnds;
        
        RangesMatcher(final String a3, final char[] v1, final char[] v2) {
            this.description = a3;
            this.rangeStarts = v1;
            this.rangeEnds = v2;
            Preconditions.checkArgument(v1.length == v2.length);
            for (int a4 = 0; a4 < v1.length; ++a4) {
                Preconditions.checkArgument(v1[a4] <= v2[a4]);
                if (a4 + 1 < v1.length) {
                    Preconditions.checkArgument(v2[a4] < v1[a4 + 1]);
                }
            }
        }
        
        @Override
        public boolean matches(final char a1) {
            int v1 = /*EL:1419*/Arrays.binarySearch(this.rangeStarts, a1);
            /*SL:1420*/if (v1 >= 0) {
                /*SL:1421*/return true;
            }
            /*SL:1423*/v1 = ~v1 - 1;
            /*SL:1424*/return v1 >= 0 && a1 <= this.rangeEnds[v1];
        }
        
        @Override
        public String toString() {
            /*SL:1430*/return this.description;
        }
    }
    
    private static final class Digit extends RangesMatcher
    {
        private static final String ZEROES = "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10";
        static final Digit INSTANCE;
        
        private static char[] zeroes() {
            /*SL:1444*/return "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".toCharArray();
        }
        
        private static char[] nines() {
            final char[] v0 = /*EL:1448*/new char["0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".length()];
            /*SL:1449*/for (int v = 0; v < "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".length(); ++v) {
                /*SL:1450*/v0[v] = (char)("0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".charAt(v) + '\t');
            }
            /*SL:1452*/return v0;
        }
        
        private Digit() {
            super("CharMatcher.digit()", zeroes(), nines());
        }
        
        static {
            INSTANCE = new Digit();
        }
    }
    
    private static final class JavaDigit extends CharMatcher
    {
        static final JavaDigit INSTANCE;
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1469*/return Character.isDigit(a1);
        }
        
        @Override
        public String toString() {
            /*SL:1474*/return "CharMatcher.javaDigit()";
        }
        
        static {
            INSTANCE = new JavaDigit();
        }
    }
    
    private static final class JavaLetter extends CharMatcher
    {
        static final JavaLetter INSTANCE;
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1485*/return Character.isLetter(a1);
        }
        
        @Override
        public String toString() {
            /*SL:1490*/return "CharMatcher.javaLetter()";
        }
        
        static {
            INSTANCE = new JavaLetter();
        }
    }
    
    private static final class JavaLetterOrDigit extends CharMatcher
    {
        static final JavaLetterOrDigit INSTANCE;
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1501*/return Character.isLetterOrDigit(a1);
        }
        
        @Override
        public String toString() {
            /*SL:1506*/return "CharMatcher.javaLetterOrDigit()";
        }
        
        static {
            INSTANCE = new JavaLetterOrDigit();
        }
    }
    
    private static final class JavaUpperCase extends CharMatcher
    {
        static final JavaUpperCase INSTANCE;
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1517*/return Character.isUpperCase(a1);
        }
        
        @Override
        public String toString() {
            /*SL:1522*/return "CharMatcher.javaUpperCase()";
        }
        
        static {
            INSTANCE = new JavaUpperCase();
        }
    }
    
    private static final class JavaLowerCase extends CharMatcher
    {
        static final JavaLowerCase INSTANCE;
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1533*/return Character.isLowerCase(a1);
        }
        
        @Override
        public String toString() {
            /*SL:1538*/return "CharMatcher.javaLowerCase()";
        }
        
        static {
            INSTANCE = new JavaLowerCase();
        }
    }
    
    private static final class JavaIsoControl extends NamedFastMatcher
    {
        static final JavaIsoControl INSTANCE;
        
        private JavaIsoControl() {
            super("CharMatcher.javaIsoControl()");
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1553*/return a1 <= '\u001f' || (a1 >= '\u007f' && a1 <= '\u009f');
        }
        
        static {
            INSTANCE = new JavaIsoControl();
        }
    }
    
    private static final class Invisible extends RangesMatcher
    {
        private static final String RANGE_STARTS = "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u1680\u180e\u2000\u2028\u205f\u2066\u2067\u2068\u2069\u206a\u3000\ud800\ufeff\ufff9\ufffa";
        private static final String RANGE_ENDS = "  \u00ad\u0604\u061c\u06dd\u070f\u1680\u180e\u200f\u202f\u2064\u2066\u2067\u2068\u2069\u206f\u3000\uf8ff\ufeff\ufff9\ufffb";
        static final Invisible INSTANCE;
        
        private Invisible() {
            super("CharMatcher.invisible()", "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u1680\u180e\u2000\u2028\u205f\u2066\u2067\u2068\u2069\u206a\u3000\ud800\ufeff\ufff9\ufffa".toCharArray(), "  \u00ad\u0604\u061c\u06dd\u070f\u1680\u180e\u200f\u202f\u2064\u2066\u2067\u2068\u2069\u206f\u3000\uf8ff\ufeff\ufff9\ufffb".toCharArray());
        }
        
        static {
            INSTANCE = new Invisible();
        }
    }
    
    private static final class SingleWidth extends RangesMatcher
    {
        static final SingleWidth INSTANCE;
        
        private SingleWidth() {
            super("CharMatcher.singleWidth()", "\u0000\u05be\u05d0\u05f3\u0600\u0750\u0e00\u1e00\u2100\ufb50\ufe70\uff61".toCharArray(), "\u04f9\u05be\u05ea\u05f4\u06ff\u077f\u0e7f\u20af\u213a\ufdff\ufeff\uffdc".toCharArray());
        }
        
        static {
            INSTANCE = new SingleWidth();
        }
    }
    
    private static class Negated extends CharMatcher
    {
        final CharMatcher original;
        
        Negated(final CharMatcher a1) {
            this.original = Preconditions.<CharMatcher>checkNotNull(a1);
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1600*/return !this.original.matches(a1);
        }
        
        @Override
        public boolean matchesAllOf(final CharSequence a1) {
            /*SL:1605*/return this.original.matchesNoneOf(a1);
        }
        
        @Override
        public boolean matchesNoneOf(final CharSequence a1) {
            /*SL:1610*/return this.original.matchesAllOf(a1);
        }
        
        @Override
        public int countIn(final CharSequence a1) {
            /*SL:1615*/return a1.length() - this.original.countIn(a1);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet a1) {
            final BitSet v1 = /*EL:1621*/new BitSet();
            /*SL:1622*/this.original.setBits(v1);
            /*SL:1623*/v1.flip(0, 65536);
            /*SL:1624*/a1.or(v1);
        }
        
        @Override
        public CharMatcher negate() {
            /*SL:1629*/return this.original;
        }
        
        @Override
        public String toString() {
            /*SL:1634*/return this.original + ".negate()";
        }
    }
    
    private static final class And extends CharMatcher
    {
        final CharMatcher first;
        final CharMatcher second;
        
        And(final CharMatcher a1, final CharMatcher a2) {
            this.first = Preconditions.<CharMatcher>checkNotNull(a1);
            this.second = Preconditions.<CharMatcher>checkNotNull(a2);
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1651*/return this.first.matches(a1) && this.second.matches(a1);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet a1) {
            final BitSet v1 = /*EL:1657*/new BitSet();
            /*SL:1658*/this.first.setBits(v1);
            final BitSet v2 = /*EL:1659*/new BitSet();
            /*SL:1660*/this.second.setBits(v2);
            /*SL:1661*/v1.and(v2);
            /*SL:1662*/a1.or(v1);
        }
        
        @Override
        public String toString() {
            /*SL:1667*/return "CharMatcher.and(" + this.first + ", " + this.second + ")";
        }
    }
    
    private static final class Or extends CharMatcher
    {
        final CharMatcher first;
        final CharMatcher second;
        
        Or(final CharMatcher a1, final CharMatcher a2) {
            this.first = Preconditions.<CharMatcher>checkNotNull(a1);
            this.second = Preconditions.<CharMatcher>checkNotNull(a2);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet a1) {
            /*SL:1685*/this.first.setBits(a1);
            /*SL:1686*/this.second.setBits(a1);
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1691*/return this.first.matches(a1) || this.second.matches(a1);
        }
        
        @Override
        public String toString() {
            /*SL:1696*/return "CharMatcher.or(" + this.first + ", " + this.second + ")";
        }
    }
    
    private static final class Is extends FastMatcher
    {
        private final char match;
        
        Is(final char a1) {
            this.match = a1;
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1713*/return a1 == this.match;
        }
        
        @Override
        public String replaceFrom(final CharSequence a1, final char a2) {
            /*SL:1718*/return a1.toString().replace(this.match, a2);
        }
        
        @Override
        public CharMatcher and(final CharMatcher a1) {
            /*SL:1723*/return a1.matches(this.match) ? this : CharMatcher.none();
        }
        
        @Override
        public CharMatcher or(final CharMatcher a1) {
            /*SL:1728*/return a1.matches(this.match) ? a1 : super.or(a1);
        }
        
        @Override
        public CharMatcher negate() {
            /*SL:1733*/return CharMatcher.isNot(this.match);
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet a1) {
            /*SL:1739*/a1.set(this.match);
        }
        
        @Override
        public String toString() {
            /*SL:1744*/return "CharMatcher.is('" + showCharacter(this.match) + "')";
        }
    }
    
    private static final class IsNot extends FastMatcher
    {
        private final char match;
        
        IsNot(final char a1) {
            this.match = a1;
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1759*/return a1 != this.match;
        }
        
        @Override
        public CharMatcher and(final CharMatcher a1) {
            /*SL:1764*/return a1.matches(this.match) ? super.and(a1) : a1;
        }
        
        @Override
        public CharMatcher or(final CharMatcher a1) {
            /*SL:1769*/return a1.matches(this.match) ? CharMatcher.any() : this;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet a1) {
            /*SL:1775*/a1.set(0, this.match);
            /*SL:1776*/a1.set(this.match + '\u0001', 65536);
        }
        
        @Override
        public CharMatcher negate() {
            /*SL:1781*/return CharMatcher.is(this.match);
        }
        
        @Override
        public String toString() {
            /*SL:1786*/return "CharMatcher.isNot('" + showCharacter(this.match) + "')";
        }
    }
    
    private static final class IsEither extends FastMatcher
    {
        private final char match1;
        private final char match2;
        
        IsEither(final char a1, final char a2) {
            this.match1 = a1;
            this.match2 = a2;
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1807*/return a1 == this.match1 || a1 == this.match2;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet a1) {
            /*SL:1813*/a1.set(this.match1);
            /*SL:1814*/a1.set(this.match2);
        }
        
        @Override
        public String toString() {
            /*SL:1819*/return "CharMatcher.anyOf(\"" + showCharacter(this.match1) + showCharacter(this.match2) + "\")";
        }
    }
    
    private static final class AnyOf extends CharMatcher
    {
        private final char[] chars;
        
        public AnyOf(final CharSequence a1) {
            Arrays.sort(this.chars = a1.toString().toCharArray());
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1835*/return Arrays.binarySearch(this.chars, a1) >= 0;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet v0) {
            /*SL:1841*/for (final char a1 : this.chars) {
                /*SL:1842*/v0.set(a1);
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = /*EL:1848*/new StringBuilder("CharMatcher.anyOf(\"");
            /*SL:1849*/for (final char v : this.chars) {
                /*SL:1850*/sb.append(showCharacter(v));
            }
            /*SL:1852*/sb.append("\")");
            /*SL:1853*/return sb.toString();
        }
    }
    
    private static final class InRange extends FastMatcher
    {
        private final char startInclusive;
        private final char endInclusive;
        
        InRange(final char a1, final char a2) {
            Preconditions.checkArgument(a2 >= a1);
            this.startInclusive = a1;
            this.endInclusive = a2;
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1871*/return this.startInclusive <= a1 && a1 <= this.endInclusive;
        }
        
        @GwtIncompatible
        @Override
        void setBits(final BitSet a1) {
            /*SL:1877*/a1.set(this.startInclusive, this.endInclusive + '\u0001');
        }
        
        @Override
        public String toString() {
            /*SL:1882*/return "CharMatcher.inRange('" + showCharacter(this.startInclusive) + "', '" + showCharacter(this.endInclusive) + "')";
        }
    }
    
    private static final class ForPredicate extends CharMatcher
    {
        private final Predicate<? super Character> predicate;
        
        ForPredicate(final Predicate<? super Character> a1) {
            this.predicate = Preconditions.<Predicate<? super Character>>checkNotNull(a1);
        }
        
        @Override
        public boolean matches(final char a1) {
            /*SL:1901*/return this.predicate.apply(a1);
        }
        
        @Override
        public boolean apply(final Character a1) {
            /*SL:1907*/return this.predicate.apply(Preconditions.<Character>checkNotNull(a1));
        }
        
        @Override
        public String toString() {
            /*SL:1912*/return "CharMatcher.forPredicate(" + this.predicate + ")";
        }
    }
}
