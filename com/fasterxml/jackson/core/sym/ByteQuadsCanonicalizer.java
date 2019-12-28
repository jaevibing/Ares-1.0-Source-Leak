package com.fasterxml.jackson.core.sym;

import java.util.Arrays;
import com.fasterxml.jackson.core.util.InternCache;
import com.fasterxml.jackson.core.JsonFactory;
import java.util.concurrent.atomic.AtomicReference;

public final class ByteQuadsCanonicalizer
{
    private static final int DEFAULT_T_SIZE = 64;
    private static final int MAX_T_SIZE = 65536;
    private static final int MIN_HASH_SIZE = 16;
    static final int MAX_ENTRIES_FOR_REUSE = 6000;
    private final ByteQuadsCanonicalizer _parent;
    private final AtomicReference<TableInfo> _tableInfo;
    private final int _seed;
    private boolean _intern;
    private final boolean _failOnDoS;
    private int[] _hashArea;
    private int _hashSize;
    private int _secondaryStart;
    private int _tertiaryStart;
    private int _tertiaryShift;
    private int _count;
    private String[] _names;
    private int _spilloverEnd;
    private int _longNameOffset;
    private transient boolean _needRehash;
    private boolean _hashShared;
    private static final int MULT = 33;
    private static final int MULT2 = 65599;
    private static final int MULT3 = 31;
    
    private ByteQuadsCanonicalizer(int a3, final boolean a4, final int v1, final boolean v2) {
        this._parent = null;
        this._seed = v1;
        this._intern = a4;
        this._failOnDoS = v2;
        if (a3 < 16) {
            a3 = 16;
        }
        else if ((a3 & a3 - 1) != 0x0) {
            int a5;
            for (a5 = 16; a5 < a3; a5 += a5) {}
            a3 = a5;
        }
        this._tableInfo = new AtomicReference<TableInfo>(TableInfo.createInitial(a3));
    }
    
    private ByteQuadsCanonicalizer(final ByteQuadsCanonicalizer a1, final boolean a2, final int a3, final boolean a4, final TableInfo a5) {
        this._parent = a1;
        this._seed = a3;
        this._intern = a2;
        this._failOnDoS = a4;
        this._tableInfo = null;
        this._count = a5.count;
        this._hashSize = a5.size;
        this._secondaryStart = this._hashSize << 2;
        this._tertiaryStart = this._secondaryStart + (this._secondaryStart >> 1);
        this._tertiaryShift = a5.tertiaryShift;
        this._hashArea = a5.mainHash;
        this._names = a5.names;
        this._spilloverEnd = a5.spilloverEnd;
        this._longNameOffset = a5.longNameOffset;
        this._needRehash = false;
        this._hashShared = true;
    }
    
    public static ByteQuadsCanonicalizer createRoot() {
        final long v1 = /*EL:287*/System.currentTimeMillis();
        final int v2 = /*EL:289*/(int)v1 + (int)(v1 >>> 32) | 0x1;
        /*SL:290*/return createRoot(v2);
    }
    
    protected static ByteQuadsCanonicalizer createRoot(final int a1) {
        /*SL:296*/return new ByteQuadsCanonicalizer(64, true, a1, true);
    }
    
    public ByteQuadsCanonicalizer makeChild(final int a1) {
        /*SL:304*/return new ByteQuadsCanonicalizer(this, JsonFactory.Feature.INTERN_FIELD_NAMES.enabledIn(a1), this._seed, JsonFactory.Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW.enabledIn(a1), this._tableInfo.get());
    }
    
    public void release() {
        /*SL:320*/if (this._parent != null && this.maybeDirty()) {
            /*SL:321*/this._parent.mergeChild(new TableInfo(this));
            /*SL:324*/this._hashShared = true;
        }
    }
    
    private void mergeChild(TableInfo a1) {
        final int v1 = /*EL:330*/a1.count;
        final TableInfo v2 = /*EL:331*/this._tableInfo.get();
        /*SL:335*/if (v1 == v2.count) {
            /*SL:336*/return;
        }
        /*SL:343*/if (v1 > 6000) {
            /*SL:345*/a1 = TableInfo.createInitial(64);
        }
        /*SL:347*/this._tableInfo.compareAndSet(v2, a1);
    }
    
    public int size() {
        /*SL:358*/if (this._tableInfo != null) {
            /*SL:359*/return this._tableInfo.get().count;
        }
        /*SL:362*/return this._count;
    }
    
    public int bucketCount() {
        /*SL:368*/return this._hashSize;
    }
    
    public boolean maybeDirty() {
        /*SL:375*/return !this._hashShared;
    }
    
    public int hashSeed() {
        /*SL:377*/return this._seed;
    }
    
    public int primaryCount() {
        int v0 = /*EL:386*/0;
        /*SL:387*/for (int v = 3, v2 = this._secondaryStart; v < v2; v += 4) {
            /*SL:388*/if (this._hashArea[v] != 0) {
                /*SL:389*/++v0;
            }
        }
        /*SL:392*/return v0;
    }
    
    public int secondaryCount() {
        int n = /*EL:400*/0;
        /*SL:402*/for (int v0 = this._secondaryStart + 3, v = this._tertiaryStart; v0 < v; v0 += 4) {
            /*SL:403*/if (this._hashArea[v0] != 0) {
                /*SL:404*/++n;
            }
        }
        /*SL:407*/return n;
    }
    
    public int tertiaryCount() {
        int n = /*EL:415*/0;
        /*SL:417*/for (int v0 = this._tertiaryStart + 3, v = v0 + this._hashSize; v0 < v; v0 += 4) {
            /*SL:418*/if (this._hashArea[v0] != 0) {
                /*SL:419*/++n;
            }
        }
        /*SL:422*/return n;
    }
    
    public int spilloverCount() {
        /*SL:431*/return this._spilloverEnd - this._spilloverStart() >> 2;
    }
    
    public int totalCount() {
        int v0 = /*EL:436*/0;
        /*SL:437*/for (int v = 3, v2 = this._hashSize << 3; v < v2; v += 4) {
            /*SL:438*/if (this._hashArea[v] != 0) {
                /*SL:439*/++v0;
            }
        }
        /*SL:442*/return v0;
    }
    
    @Override
    public String toString() {
        final int v1 = /*EL:447*/this.primaryCount();
        final int v2 = /*EL:448*/this.secondaryCount();
        final int v3 = /*EL:449*/this.tertiaryCount();
        final int v4 = /*EL:450*/this.spilloverCount();
        final int v5 = /*EL:451*/this.totalCount();
        /*SL:452*/return String.format("[%s: size=%d, hashSize=%d, %d/%d/%d/%d pri/sec/ter/spill (=%s), total:%d]", this.getClass().getName(), this._count, this._hashSize, v1, v2, v3, v4, v1 + v2 + v3 + v4, v5);
    }
    
    public String findName(final int a1) {
        final int v1 = /*EL:465*/this._calcOffset(this.calcHash(a1));
        final int[] v2 = /*EL:467*/this._hashArea;
        int v3 = /*EL:469*/v2[v1 + 3];
        /*SL:471*/if (v3 == 1) {
            /*SL:472*/if (v2[v1] == a1) {
                /*SL:473*/return this._names[v1 >> 2];
            }
        }
        else/*SL:475*/ if (v3 == 0) {
            /*SL:476*/return null;
        }
        final int v4 = /*EL:479*/this._secondaryStart + (v1 >> 3 << 2);
        /*SL:481*/v3 = v2[v4 + 3];
        /*SL:483*/if (v3 == 1) {
            /*SL:484*/if (v2[v4] == a1) {
                /*SL:485*/return this._names[v4 >> 2];
            }
        }
        else/*SL:487*/ if (v3 == 0) {
            /*SL:488*/return null;
        }
        /*SL:492*/return this._findSecondary(v1, a1);
    }
    
    public String findName(final int a1, final int a2) {
        final int v1 = /*EL:497*/this._calcOffset(this.calcHash(a1, a2));
        final int[] v2 = /*EL:499*/this._hashArea;
        int v3 = /*EL:501*/v2[v1 + 3];
        /*SL:503*/if (v3 == 2) {
            /*SL:504*/if (a1 == v2[v1] && a2 == v2[v1 + 1]) {
                /*SL:505*/return this._names[v1 >> 2];
            }
        }
        else/*SL:507*/ if (v3 == 0) {
            /*SL:508*/return null;
        }
        final int v4 = /*EL:511*/this._secondaryStart + (v1 >> 3 << 2);
        /*SL:513*/v3 = v2[v4 + 3];
        /*SL:515*/if (v3 == 2) {
            /*SL:516*/if (a1 == v2[v4] && a2 == v2[v4 + 1]) {
                /*SL:517*/return this._names[v4 >> 2];
            }
        }
        else/*SL:519*/ if (v3 == 0) {
            /*SL:520*/return null;
        }
        /*SL:522*/return this._findSecondary(v1, a1, a2);
    }
    
    public String findName(final int a1, final int a2, final int a3) {
        final int v1 = /*EL:527*/this._calcOffset(this.calcHash(a1, a2, a3));
        final int[] v2 = /*EL:528*/this._hashArea;
        int v3 = /*EL:529*/v2[v1 + 3];
        /*SL:531*/if (v3 == 3) {
            /*SL:532*/if (a1 == v2[v1] && v2[v1 + 1] == a2 && v2[v1 + 2] == a3) {
                /*SL:533*/return this._names[v1 >> 2];
            }
        }
        else/*SL:535*/ if (v3 == 0) {
            /*SL:536*/return null;
        }
        final int v4 = /*EL:539*/this._secondaryStart + (v1 >> 3 << 2);
        /*SL:541*/v3 = v2[v4 + 3];
        /*SL:543*/if (v3 == 3) {
            /*SL:544*/if (a1 == v2[v4] && v2[v4 + 1] == a2 && v2[v4 + 2] == a3) {
                /*SL:545*/return this._names[v4 >> 2];
            }
        }
        else/*SL:547*/ if (v3 == 0) {
            /*SL:548*/return null;
        }
        /*SL:550*/return this._findSecondary(v1, a1, a2, a3);
    }
    
    public String findName(final int[] a1, final int a2) {
        /*SL:559*/if (a2 < 4) {
            /*SL:560*/switch (a2) {
                case 3: {
                    /*SL:562*/return this.findName(a1[0], a1[1], a1[2]);
                }
                case 2: {
                    /*SL:564*/return this.findName(a1[0], a1[1]);
                }
                case 1: {
                    /*SL:566*/return this.findName(a1[0]);
                }
                default: {
                    /*SL:568*/return "";
                }
            }
        }
        else {
            final int v1 = /*EL:571*/this.calcHash(a1, a2);
            final int v2 = /*EL:572*/this._calcOffset(v1);
            final int[] v3 = /*EL:574*/this._hashArea;
            final int v4 = /*EL:576*/v3[v2 + 3];
            /*SL:578*/if (v1 == v3[v2] && v4 == a2 && /*EL:580*/this._verifyLongName(a1, a2, v3[v2 + 1])) {
                /*SL:581*/return this._names[v2 >> 2];
            }
            /*SL:584*/if (v4 == 0) {
                /*SL:585*/return null;
            }
            final int v5 = /*EL:588*/this._secondaryStart + (v2 >> 3 << 2);
            final int v6 = /*EL:590*/v3[v5 + 3];
            /*SL:591*/if (v1 == v3[v5] && v6 == a2 && /*EL:592*/this._verifyLongName(a1, a2, v3[v5 + 1])) {
                /*SL:593*/return this._names[v5 >> 2];
            }
            /*SL:596*/return this._findSecondary(v2, v1, a1, a2);
        }
    }
    
    private final int _calcOffset(final int a1) {
        final int v1 = /*EL:604*/a1 & this._hashSize - 1;
        /*SL:606*/return v1 << 2;
    }
    
    private String _findSecondary(final int v2, final int v3) {
        int v4 = /*EL:621*/this._tertiaryStart + (v2 >> this._tertiaryShift + 2 << this._tertiaryShift);
        final int[] v5 = /*EL:622*/this._hashArea;
        final int v6 = /*EL:623*/1 << this._tertiaryShift;
        /*SL:624*/for (int a2 = v4 + v6; v4 < a2; v4 += 4) {
            /*SL:625*/a2 = v5[v4 + 3];
            /*SL:626*/if (v3 == v5[v4] && 1 == a2) {
                /*SL:627*/return this._names[v4 >> 2];
            }
            /*SL:629*/if (a2 == 0) {
                /*SL:630*/return null;
            }
        }
        /*SL:636*/for (v4 = this._spilloverStart(); v4 < this._spilloverEnd; v4 += 4) {
            /*SL:637*/if (v3 == v5[v4] && 1 == v5[v4 + 3]) {
                /*SL:638*/return this._names[v4 >> 2];
            }
        }
        /*SL:641*/return null;
    }
    
    private String _findSecondary(final int v1, final int v2, final int v3) {
        int v4 = /*EL:646*/this._tertiaryStart + (v1 >> this._tertiaryShift + 2 << this._tertiaryShift);
        final int[] v5 = /*EL:647*/this._hashArea;
        final int v6 = /*EL:649*/1 << this._tertiaryShift;
        /*SL:650*/for (int a2 = v4 + v6; v4 < a2; v4 += 4) {
            /*SL:651*/a2 = v5[v4 + 3];
            /*SL:652*/if (v2 == v5[v4] && v3 == v5[v4 + 1] && 2 == a2) {
                /*SL:653*/return this._names[v4 >> 2];
            }
            /*SL:655*/if (a2 == 0) {
                /*SL:656*/return null;
            }
        }
        /*SL:659*/for (v4 = this._spilloverStart(); v4 < this._spilloverEnd; v4 += 4) {
            /*SL:660*/if (v2 == v5[v4] && v3 == v5[v4 + 1] && 2 == v5[v4 + 3]) {
                /*SL:661*/return this._names[v4 >> 2];
            }
        }
        /*SL:664*/return null;
    }
    
    private String _findSecondary(final int a4, final int v1, final int v2, final int v3) {
        int v4 = /*EL:669*/this._tertiaryStart + (a4 >> this._tertiaryShift + 2 << this._tertiaryShift);
        final int[] v5 = /*EL:670*/this._hashArea;
        final int v6 = /*EL:672*/1 << this._tertiaryShift;
        /*SL:673*/for (int a5 = v4 + v6; v4 < a5; v4 += 4) {
            final int a6 = /*EL:674*/v5[v4 + 3];
            /*SL:675*/if (v1 == v5[v4] && v2 == v5[v4 + 1] && v3 == v5[v4 + 2] && 3 == a6) {
                /*SL:676*/return this._names[v4 >> 2];
            }
            /*SL:678*/if (a6 == 0) {
                /*SL:679*/return null;
            }
        }
        /*SL:682*/for (v4 = this._spilloverStart(); v4 < this._spilloverEnd; v4 += 4) {
            /*SL:683*/if (v1 == v5[v4] && v2 == v5[v4 + 1] && v3 == v5[v4 + 2] && 3 == v5[v4 + 3]) {
                /*SL:685*/return this._names[v4 >> 2];
            }
        }
        /*SL:688*/return null;
    }
    
    private String _findSecondary(final int a4, final int v1, final int[] v2, final int v3) {
        int v4 = /*EL:693*/this._tertiaryStart + (a4 >> this._tertiaryShift + 2 << this._tertiaryShift);
        final int[] v5 = /*EL:694*/this._hashArea;
        final int v6 = /*EL:696*/1 << this._tertiaryShift;
        /*SL:697*/for (int a5 = v4 + v6; v4 < a5; v4 += 4) {
            final int a6 = /*EL:698*/v5[v4 + 3];
            /*SL:699*/if (v1 == v5[v4] && v3 == a6 && /*EL:700*/this._verifyLongName(v2, v3, v5[v4 + 1])) {
                /*SL:701*/return this._names[v4 >> 2];
            }
            /*SL:704*/if (a6 == 0) {
                /*SL:705*/return null;
            }
        }
        /*SL:708*/for (v4 = this._spilloverStart(); v4 < this._spilloverEnd; v4 += 4) {
            /*SL:709*/if (v1 == v5[v4] && v3 == v5[v4 + 3] && /*EL:710*/this._verifyLongName(v2, v3, v5[v4 + 1])) {
                /*SL:711*/return this._names[v4 >> 2];
            }
        }
        /*SL:715*/return null;
    }
    
    private boolean _verifyLongName(final int[] a1, final int a2, int a3) {
        final int[] v1 = /*EL:720*/this._hashArea;
        int v2 = /*EL:722*/0;
        /*SL:724*/switch (a2) {
            default: {
                /*SL:726*/return this._verifyLongName2(a1, a2, a3);
            }
            case 8: {
                /*SL:728*/if (a1[v2++] != v1[a3++]) {
                    return false;
                }
            }
            case 7: {
                /*SL:730*/if (a1[v2++] != v1[a3++]) {
                    return false;
                }
            }
            case 6: {
                /*SL:732*/if (a1[v2++] != v1[a3++]) {
                    return false;
                }
            }
            case 5: {
                /*SL:734*/if (a1[v2++] != v1[a3++]) {
                    return false;
                }
                return /*EL:736*/a1[v2++] == v1[a3++] && /*EL:737*/a1[v2++] == v1[a3++] && /*EL:738*/a1[v2++] == v1[a3++] && /*EL:739*/a1[v2++] == v1[a3++];
            }
            case 4: {
                return a1[v2++] == v1[a3++] && a1[v2++] == v1[a3++] && a1[v2++] == v1[a3++] && a1[v2++] == v1[a3++];
            }
        }
    }
    
    private boolean _verifyLongName2(final int[] a1, final int a2, int a3) {
        int v1 = /*EL:746*/0;
        /*SL:748*/while (a1[v1++] == this._hashArea[a3++]) {
            /*SL:751*/if (v1 >= a2) {
                /*SL:752*/return true;
            }
        }
        return false;
    }
    
    public String addName(String a1, final int a2) {
        /*SL:762*/this._verifySharing();
        /*SL:763*/if (this._intern) {
            /*SL:764*/a1 = InternCache.instance.intern(a1);
        }
        final int v1 = /*EL:766*/this._findOffsetForAdd(this.calcHash(a2));
        /*SL:767*/this._hashArea[v1] = a2;
        /*SL:768*/this._hashArea[v1 + 3] = 1;
        /*SL:769*/this._names[v1 >> 2] = a1;
        /*SL:770*/++this._count;
        /*SL:771*/this._verifyNeedForRehash();
        /*SL:772*/return a1;
    }
    
    public String addName(String a1, final int a2, final int a3) {
        /*SL:776*/this._verifySharing();
        /*SL:777*/if (this._intern) {
            /*SL:778*/a1 = InternCache.instance.intern(a1);
        }
        final int v1 = /*EL:780*/(a3 == 0) ? this.calcHash(a2) : this.calcHash(a2, a3);
        final int v2 = /*EL:781*/this._findOffsetForAdd(v1);
        /*SL:782*/this._hashArea[v2] = a2;
        /*SL:783*/this._hashArea[v2 + 1] = a3;
        /*SL:784*/this._hashArea[v2 + 3] = 2;
        /*SL:785*/this._names[v2 >> 2] = a1;
        /*SL:786*/++this._count;
        /*SL:787*/this._verifyNeedForRehash();
        /*SL:788*/return a1;
    }
    
    public String addName(String a1, final int a2, final int a3, final int a4) {
        /*SL:792*/this._verifySharing();
        /*SL:793*/if (this._intern) {
            /*SL:794*/a1 = InternCache.instance.intern(a1);
        }
        final int v1 = /*EL:796*/this._findOffsetForAdd(this.calcHash(a2, a3, a4));
        /*SL:797*/this._hashArea[v1] = a2;
        /*SL:798*/this._hashArea[v1 + 1] = a3;
        /*SL:799*/this._hashArea[v1 + 2] = a4;
        /*SL:800*/this._hashArea[v1 + 3] = 3;
        /*SL:801*/this._names[v1 >> 2] = a1;
        /*SL:802*/++this._count;
        /*SL:803*/this._verifyNeedForRehash();
        /*SL:804*/return a1;
    }
    
    public String addName(String v1, final int[] v2, final int v3) {
        /*SL:809*/this._verifySharing();
        /*SL:810*/if (this._intern) {
            /*SL:811*/v1 = InternCache.instance.intern(v1);
        }
        int v4 = 0;
        /*SL:815*/switch (v3) {
            case 1: {
                /*SL:818*/v4 = this._findOffsetForAdd(this.calcHash(v2[0]));
                /*SL:819*/this._hashArea[v4] = v2[0];
                /*SL:820*/this._hashArea[v4 + 3] = 1;
                /*SL:822*/break;
            }
            case 2: {
                /*SL:825*/v4 = this._findOffsetForAdd(this.calcHash(v2[0], v2[1]));
                /*SL:826*/this._hashArea[v4] = v2[0];
                /*SL:827*/this._hashArea[v4 + 1] = v2[1];
                /*SL:828*/this._hashArea[v4 + 3] = 2;
                /*SL:830*/break;
            }
            case 3: {
                /*SL:833*/v4 = this._findOffsetForAdd(this.calcHash(v2[0], v2[1], v2[2]));
                /*SL:834*/this._hashArea[v4] = v2[0];
                /*SL:835*/this._hashArea[v4 + 1] = v2[1];
                /*SL:836*/this._hashArea[v4 + 2] = v2[2];
                /*SL:837*/this._hashArea[v4 + 3] = 3;
                /*SL:839*/break;
            }
            default: {
                final int a1 = /*EL:841*/this.calcHash(v2, v3);
                /*SL:842*/v4 = this._findOffsetForAdd(a1);
                /*SL:844*/this._hashArea[v4] = a1;
                final int a2 = /*EL:845*/this._appendLongName(v2, v3);
                /*SL:846*/this._hashArea[v4 + 1] = a2;
                /*SL:847*/this._hashArea[v4 + 3] = v3;
                break;
            }
        }
        /*SL:850*/this._names[v4 >> 2] = v1;
        /*SL:853*/++this._count;
        /*SL:854*/this._verifyNeedForRehash();
        /*SL:855*/return v1;
    }
    
    private void _verifyNeedForRehash() {
        /*SL:860*/if (this._count > this._hashSize >> 1) {
            final int v1 = /*EL:861*/this._spilloverEnd - this._spilloverStart() >> 2;
            /*SL:862*/if (v1 > 1 + this._count >> 7 || this._count > this._hashSize * 0.8) {
                /*SL:864*/this._needRehash = true;
            }
        }
    }
    
    private void _verifySharing() {
        /*SL:871*/if (this._hashShared) {
            /*SL:872*/this._hashArea = Arrays.copyOf(this._hashArea, this._hashArea.length);
            /*SL:873*/this._names = Arrays.<String>copyOf(this._names, this._names.length);
            /*SL:874*/this._hashShared = false;
            /*SL:877*/this._verifyNeedForRehash();
        }
        /*SL:879*/if (this._needRehash) {
            /*SL:880*/this.rehash();
        }
    }
    
    private int _findOffsetForAdd(final int v2) {
        int v3 = /*EL:890*/this._calcOffset(v2);
        final int[] v4 = /*EL:891*/this._hashArea;
        /*SL:892*/if (v4[v3 + 3] == 0) {
            /*SL:894*/return v3;
        }
        int v5 = /*EL:897*/this._secondaryStart + (v3 >> 3 << 2);
        /*SL:898*/if (v4[v5 + 3] == 0) {
            /*SL:900*/return v5;
        }
        /*SL:904*/v5 = this._tertiaryStart + (v3 >> this._tertiaryShift + 2 << this._tertiaryShift);
        final int v6 = /*EL:905*/1 << this._tertiaryShift;
        /*SL:906*/for (int a1 = v5 + v6; v5 < a1; v5 += 4) {
            /*SL:907*/if (v4[v5 + 3] == 0) {
                /*SL:909*/return v5;
            }
        }
        /*SL:914*/v3 = this._spilloverEnd;
        /*SL:915*/this._spilloverEnd += 4;
        final int v7 = /*EL:925*/this._hashSize << 3;
        /*SL:926*/if (this._spilloverEnd >= v7) {
            /*SL:927*/if (this._failOnDoS) {
                /*SL:928*/this._reportTooManyCollisions();
            }
            /*SL:932*/this._needRehash = true;
        }
        /*SL:934*/return v3;
    }
    
    private int _appendLongName(final int[] v-4, final int v-3) {
        final int longNameOffset = /*EL:939*/this._longNameOffset;
        /*SL:942*/if (longNameOffset + v-3 > this._hashArea.length) {
            final int a1 = /*EL:944*/longNameOffset + v-3 - this._hashArea.length;
            final int a2 = /*EL:946*/Math.min(4096, this._hashSize);
            final int v1 = /*EL:948*/this._hashArea.length + Math.max(a1, a2);
            /*SL:949*/this._hashArea = Arrays.copyOf(this._hashArea, v1);
        }
        /*SL:951*/System.arraycopy(v-4, 0, this._hashArea, longNameOffset, v-3);
        /*SL:952*/this._longNameOffset += v-3;
        /*SL:953*/return longNameOffset;
    }
    
    public int calcHash(final int a1) {
        int v1 = /*EL:978*/a1 ^ this._seed;
        /*SL:984*/v1 += v1 >>> 16;
        /*SL:985*/v1 ^= v1 << 3;
        /*SL:986*/v1 += v1 >>> 12;
        /*SL:987*/return v1;
    }
    
    public int calcHash(final int a1, final int a2) {
        int v1 = /*EL:996*/a1 + (a1 >>> 15);
        /*SL:997*/v1 ^= v1 >>> 9;
        /*SL:998*/v1 += a2 * 33;
        /*SL:999*/v1 ^= this._seed;
        /*SL:1000*/v1 += v1 >>> 16;
        /*SL:1001*/v1 ^= v1 >>> 4;
        /*SL:1002*/v1 += v1 << 3;
        /*SL:1004*/return v1;
    }
    
    public int calcHash(final int a1, final int a2, final int a3) {
        int v1 = /*EL:1009*/a1 ^ this._seed;
        /*SL:1010*/v1 += v1 >>> 9;
        /*SL:1011*/v1 *= 31;
        /*SL:1012*/v1 += a2;
        /*SL:1013*/v1 *= 33;
        /*SL:1014*/v1 += v1 >>> 15;
        /*SL:1015*/v1 ^= a3;
        /*SL:1017*/v1 += v1 >>> 4;
        /*SL:1019*/v1 += v1 >>> 15;
        /*SL:1020*/v1 ^= v1 << 9;
        /*SL:1022*/return v1;
    }
    
    public int calcHash(final int[] v2, final int v3) {
        /*SL:1027*/if (v3 < 4) {
            /*SL:1028*/throw new IllegalArgumentException();
        }
        int v4 = /*EL:1035*/v2[0] ^ this._seed;
        /*SL:1036*/v4 += v4 >>> 9;
        /*SL:1037*/v4 += v2[1];
        /*SL:1038*/v4 += v4 >>> 15;
        /*SL:1039*/v4 *= 33;
        /*SL:1040*/v4 ^= v2[2];
        /*SL:1041*/v4 += v4 >>> 4;
        /*SL:1043*/for (int a2 = 3; a2 < v3; ++a2) {
            /*SL:1044*/a2 = v2[a2];
            /*SL:1045*/a2 ^= a2 >> 21;
            /*SL:1046*/v4 += a2;
        }
        /*SL:1048*/v4 *= 65599;
        /*SL:1051*/v4 += v4 >>> 19;
        /*SL:1052*/v4 ^= v4 << 5;
        /*SL:1053*/return v4;
    }
    
    private void rehash() {
        /*SL:1064*/this._needRehash = false;
        /*SL:1066*/this._hashShared = false;
        final int[] hashArea = /*EL:1070*/this._hashArea;
        final String[] names = /*EL:1071*/this._names;
        final int hashSize = /*EL:1072*/this._hashSize;
        final int count = /*EL:1073*/this._count;
        final int n = /*EL:1074*/hashSize + hashSize;
        final int spilloverEnd = /*EL:1075*/this._spilloverEnd;
        /*SL:1080*/if (n > 65536) {
            /*SL:1081*/this.nukeSymbols(true);
            /*SL:1082*/return;
        }
        /*SL:1085*/this._hashArea = new int[hashArea.length + (hashSize << 3)];
        /*SL:1086*/this._hashSize = n;
        /*SL:1087*/this._secondaryStart = n << 2;
        /*SL:1088*/this._tertiaryStart = this._secondaryStart + (this._secondaryStart >> 1);
        /*SL:1089*/this._tertiaryShift = _calcTertiaryShift(n);
        /*SL:1092*/this._names = new String[names.length << 1];
        /*SL:1093*/this.nukeSymbols(false);
        int n2 = /*EL:1100*/0;
        int[] array = /*EL:1101*/new int[16];
        /*SL:1102*/for (int i = 0, n3 = spilloverEnd; i < n3; i += 4) {
            final int v2 = /*EL:1103*/hashArea[i + 3];
            /*SL:1104*/if (v2 != 0) {
                /*SL:1107*/++n2;
                final String v0 = /*EL:1108*/names[i >> 2];
                /*SL:1109*/switch (v2) {
                    case 1: {
                        /*SL:1111*/array[0] = hashArea[i];
                        /*SL:1112*/this.addName(v0, array, 1);
                        /*SL:1113*/break;
                    }
                    case 2: {
                        /*SL:1115*/array[0] = hashArea[i];
                        /*SL:1116*/array[1] = hashArea[i + 1];
                        /*SL:1117*/this.addName(v0, array, 2);
                        /*SL:1118*/break;
                    }
                    case 3: {
                        /*SL:1120*/array[0] = hashArea[i];
                        /*SL:1121*/array[1] = hashArea[i + 1];
                        /*SL:1122*/array[2] = hashArea[i + 2];
                        /*SL:1123*/this.addName(v0, array, 3);
                        /*SL:1124*/break;
                    }
                    default: {
                        /*SL:1126*/if (v2 > array.length) {
                            /*SL:1127*/array = new int[v2];
                        }
                        final int v = /*EL:1130*/hashArea[i + 1];
                        /*SL:1131*/System.arraycopy(hashArea, v, array, 0, v2);
                        /*SL:1132*/this.addName(v0, array, v2);
                        break;
                    }
                }
            }
        }
        /*SL:1139*/if (n2 != count) {
            /*SL:1140*/throw new IllegalStateException("Failed rehash(): old count=" + count + ", copyCount=" + n2);
        }
    }
    
    private void nukeSymbols(final boolean a1) {
        /*SL:1149*/this._count = 0;
        /*SL:1151*/this._spilloverEnd = this._spilloverStart();
        /*SL:1153*/this._longNameOffset = this._hashSize << 3;
        /*SL:1154*/if (a1) {
            /*SL:1155*/Arrays.fill(this._hashArea, 0);
            /*SL:1156*/Arrays.fill(this._names, null);
        }
    }
    
    private final int _spilloverStart() {
        final int v1 = /*EL:1172*/this._hashSize;
        /*SL:1173*/return (v1 << 3) - v1;
    }
    
    protected void _reportTooManyCollisions() {
        /*SL:1179*/if (this._hashSize <= 1024) {
            /*SL:1180*/return;
        }
        /*SL:1182*/throw new IllegalStateException("Spill-over slots in symbol table with " + this._count + " entries, hash area of " + this._hashSize + " slots is now full (all " + (this._hashSize >> 3) + " slots -- suspect a DoS attack based on hash collisions." + " You can disable the check via `JsonFactory.Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW`");
    }
    
    static int _calcTertiaryShift(final int a1) {
        final int v1 = /*EL:1191*/a1 >> 2;
        /*SL:1194*/if (v1 < 64) {
            /*SL:1195*/return 4;
        }
        /*SL:1197*/if (v1 <= 256) {
            /*SL:1198*/return 5;
        }
        /*SL:1200*/if (v1 <= 1024) {
            /*SL:1201*/return 6;
        }
        /*SL:1204*/return 7;
    }
    
    private static final class TableInfo
    {
        public final int size;
        public final int count;
        public final int tertiaryShift;
        public final int[] mainHash;
        public final String[] names;
        public final int spilloverEnd;
        public final int longNameOffset;
        
        public TableInfo(final int a1, final int a2, final int a3, final int[] a4, final String[] a5, final int a6, final int a7) {
            this.size = a1;
            this.count = a2;
            this.tertiaryShift = a3;
            this.mainHash = a4;
            this.names = a5;
            this.spilloverEnd = a6;
            this.longNameOffset = a7;
        }
        
        public TableInfo(final ByteQuadsCanonicalizer a1) {
            this.size = a1._hashSize;
            this.count = a1._count;
            this.tertiaryShift = a1._tertiaryShift;
            this.mainHash = a1._hashArea;
            this.names = a1._names;
            this.spilloverEnd = a1._spilloverEnd;
            this.longNameOffset = a1._longNameOffset;
        }
        
        public static TableInfo createInitial(final int a1) {
            final int v1 = /*EL:1254*/a1 << 3;
            final int v2 = /*EL:1255*/ByteQuadsCanonicalizer._calcTertiaryShift(a1);
            /*SL:1257*/return new TableInfo(a1, 0, v2, new int[v1], new String[a1 << 1], v1 - a1, v1);
        }
    }
}
