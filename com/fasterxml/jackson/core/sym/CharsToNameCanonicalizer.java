package com.fasterxml.jackson.core.sym;

import java.util.Arrays;
import com.fasterxml.jackson.core.util.InternCache;
import com.fasterxml.jackson.core.JsonFactory;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicReference;

public final class CharsToNameCanonicalizer
{
    public static final int HASH_MULT = 33;
    private static final int DEFAULT_T_SIZE = 64;
    private static final int MAX_T_SIZE = 65536;
    static final int MAX_ENTRIES_FOR_REUSE = 12000;
    static final int MAX_COLL_CHAIN_LENGTH = 100;
    private final CharsToNameCanonicalizer _parent;
    private final AtomicReference<TableInfo> _tableInfo;
    private final int _seed;
    private final int _flags;
    private boolean _canonicalize;
    private String[] _symbols;
    private Bucket[] _buckets;
    private int _size;
    private int _sizeThreshold;
    private int _indexMask;
    private int _longestCollisionList;
    private boolean _hashShared;
    private BitSet _overflows;
    
    private CharsToNameCanonicalizer(final int a1) {
        this._parent = null;
        this._seed = a1;
        this._canonicalize = true;
        this._flags = -1;
        this._hashShared = false;
        this._longestCollisionList = 0;
        this._tableInfo = new AtomicReference<TableInfo>(TableInfo.createInitial(64));
    }
    
    private CharsToNameCanonicalizer(final CharsToNameCanonicalizer a1, final int a2, final int a3, final TableInfo a4) {
        this._parent = a1;
        this._seed = a3;
        this._tableInfo = null;
        this._flags = a2;
        this._canonicalize = JsonFactory.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(a2);
        this._symbols = a4.symbols;
        this._buckets = a4.buckets;
        this._size = a4.size;
        this._longestCollisionList = a4.longestCollisionList;
        final int v1 = this._symbols.length;
        this._sizeThreshold = _thresholdSize(v1);
        this._indexMask = v1 - 1;
        this._hashShared = true;
    }
    
    private static int _thresholdSize(final int a1) {
        /*SL:276*/return a1 - (a1 >> 2);
    }
    
    public static CharsToNameCanonicalizer createRoot() {
        final long v1 = /*EL:293*/System.currentTimeMillis();
        final int v2 = /*EL:295*/(int)v1 + (int)(v1 >>> 32) | 0x1;
        /*SL:296*/return createRoot(v2);
    }
    
    protected static CharsToNameCanonicalizer createRoot(final int a1) {
        /*SL:300*/return new CharsToNameCanonicalizer(a1);
    }
    
    public CharsToNameCanonicalizer makeChild(final int a1) {
        /*SL:315*/return new CharsToNameCanonicalizer(this, a1, this._seed, this._tableInfo.get());
    }
    
    public void release() {
        /*SL:326*/if (!this.maybeDirty()) {
            return;
        }
        /*SL:329*/if (this._parent != null && this._canonicalize) {
            /*SL:330*/this._parent.mergeChild(new TableInfo(this));
            /*SL:333*/this._hashShared = true;
        }
    }
    
    private void mergeChild(TableInfo a1) {
        final int v1 = /*EL:346*/a1.size;
        final TableInfo v2 = /*EL:347*/this._tableInfo.get();
        /*SL:351*/if (v1 == v2.size) {
            /*SL:352*/return;
        }
        /*SL:358*/if (v1 > 12000) {
            /*SL:360*/a1 = TableInfo.createInitial(64);
        }
        /*SL:362*/this._tableInfo.compareAndSet(v2, a1);
    }
    
    public int size() {
        /*SL:372*/if (this._tableInfo != null) {
            /*SL:373*/return this._tableInfo.get().size;
        }
        /*SL:376*/return this._size;
    }
    
    public int bucketCount() {
        /*SL:385*/return this._symbols.length;
    }
    
    public boolean maybeDirty() {
        /*SL:386*/return !this._hashShared;
    }
    
    public int hashSeed() {
        /*SL:387*/return this._seed;
    }
    
    public int collisionCount() {
        int n = /*EL:397*/0;
        /*SL:399*/for (final Bucket v : this._buckets) {
            /*SL:400*/if (v != null) {
                /*SL:401*/n += v.length;
            }
        }
        /*SL:404*/return n;
    }
    
    public int maxCollisionLength() {
        /*SL:414*/return this._longestCollisionList;
    }
    
    public String findSymbol(final char[] a4, final int v1, final int v2, final int v3) {
        /*SL:424*/if (v2 < 1) {
            /*SL:425*/return "";
        }
        /*SL:427*/if (!this._canonicalize) {
            /*SL:428*/return new String(a4, v1, v2);
        }
        final int v4 = /*EL:436*/this._hashToIndex(v3);
        String v5 = /*EL:437*/this._symbols[v4];
        /*SL:440*/if (v5 != null) {
            /*SL:442*/if (v5.length() == v2) {
                int a5 = /*EL:443*/0;
                /*SL:444*/while (v5.charAt(a5) == a4[v1 + a5]) {
                    /*SL:446*/if (++a5 == v2) {
                        /*SL:447*/return v5;
                    }
                }
            }
            final Bucket a6 = /*EL:451*/this._buckets[v4 >> 1];
            /*SL:452*/if (a6 != null) {
                /*SL:453*/v5 = a6.has(a4, v1, v2);
                /*SL:454*/if (v5 != null) {
                    /*SL:455*/return v5;
                }
                /*SL:457*/v5 = this._findSymbol2(a4, v1, v2, a6.next);
                /*SL:458*/if (v5 != null) {
                    /*SL:459*/return v5;
                }
            }
        }
        /*SL:463*/return this._addSymbol(a4, v1, v2, v3, v4);
    }
    
    private String _findSymbol2(final char[] a3, final int a4, final int v1, Bucket v2) {
        /*SL:467*/while (v2 != null) {
            final String a5 = /*EL:468*/v2.has(a3, a4, v1);
            /*SL:469*/if (a5 != null) {
                /*SL:470*/return a5;
            }
            /*SL:472*/v2 = v2.next;
        }
        /*SL:474*/return null;
    }
    
    private String _addSymbol(final char[] a5, final int v1, final int v2, final int v3, int v4) {
        /*SL:479*/if (this._hashShared) {
            /*SL:480*/this.copyArrays();
            /*SL:481*/this._hashShared = false;
        }
        else/*SL:482*/ if (this._size >= this._sizeThreshold) {
            /*SL:483*/this.rehash();
            /*SL:487*/v4 = this._hashToIndex(this.calcHash(a5, v1, v2));
        }
        String v5 = /*EL:490*/new String(a5, v1, v2);
        /*SL:491*/if (JsonFactory.Feature.INTERN_FIELD_NAMES.enabledIn(this._flags)) {
            /*SL:492*/v5 = InternCache.instance.intern(v5);
        }
        /*SL:494*/++this._size;
        /*SL:496*/if (this._symbols[v4] == null) {
            /*SL:497*/this._symbols[v4] = v5;
        }
        else {
            final int a6 = /*EL:499*/v4 >> 1;
            final Bucket a7 = /*EL:500*/new Bucket(v5, this._buckets[a6]);
            final int a8 = /*EL:501*/a7.length;
            /*SL:502*/if (a8 > 100) {
                /*SL:505*/this._handleSpillOverflow(a6, a7);
            }
            else {
                /*SL:507*/this._buckets[a6] = a7;
                /*SL:508*/this._longestCollisionList = Math.max(a8, this._longestCollisionList);
            }
        }
        /*SL:511*/return v5;
    }
    
    private void _handleSpillOverflow(final int a1, final Bucket a2) {
        /*SL:516*/if (this._overflows == null) {
            /*SL:517*/(this._overflows = new BitSet()).set(/*EL:518*/a1);
        }
        else/*SL:520*/ if (this._overflows.get(a1)) {
            /*SL:522*/if (JsonFactory.Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW.enabledIn(this._flags)) {
                /*SL:523*/this.reportTooManyCollisions(100);
            }
            /*SL:526*/this._canonicalize = false;
        }
        else {
            /*SL:528*/this._overflows.set(a1);
        }
        /*SL:532*/this._symbols[a1 + a1] = a2.symbol;
        /*SL:533*/this._buckets[a1] = null;
        /*SL:535*/this._size -= a2.length;
        /*SL:537*/this._longestCollisionList = -1;
    }
    
    public int _hashToIndex(int a1) {
        /*SL:546*/a1 += a1 >>> 15;
        /*SL:547*/a1 ^= a1 << 7;
        /*SL:548*/a1 += a1 >>> 3;
        /*SL:549*/return a1 & this._indexMask;
    }
    
    public int calcHash(final char[] v1, final int v2, final int v3) {
        int v4 = /*EL:562*/this._seed;
        /*SL:563*/for (int a1 = v2, a2 = v2 + v3; a1 < a2; ++a1) {
            /*SL:564*/v4 = v4 * 33 + v1[a1];
        }
        /*SL:567*/return (v4 == 0) ? 1 : v4;
    }
    
    public int calcHash(final String v2) {
        final int v3 = /*EL:572*/v2.length();
        int v4 = /*EL:574*/this._seed;
        /*SL:575*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:576*/v4 = v4 * 33 + v2.charAt(a1);
        }
        /*SL:579*/return (v4 == 0) ? 1 : v4;
    }
    
    private void copyArrays() {
        final String[] v1 = /*EL:593*/this._symbols;
        /*SL:594*/this._symbols = Arrays.<String>copyOf(v1, v1.length);
        final Bucket[] v2 = /*EL:595*/this._buckets;
        /*SL:596*/this._buckets = Arrays.<Bucket>copyOf(v2, v2.length);
    }
    
    private void rehash() {
        int length = /*EL:607*/this._symbols.length;
        final int a1 = /*EL:608*/length + length;
        /*SL:614*/if (a1 > 65536) {
            /*SL:617*/this._size = 0;
            /*SL:618*/this._canonicalize = false;
            /*SL:620*/this._symbols = new String[64];
            /*SL:621*/this._buckets = new Bucket[32];
            /*SL:622*/this._indexMask = 63;
            /*SL:623*/this._hashShared = false;
            /*SL:624*/return;
        }
        final String[] symbols = /*EL:627*/this._symbols;
        final Bucket[] buckets = /*EL:628*/this._buckets;
        /*SL:629*/this._symbols = new String[a1];
        /*SL:630*/this._buckets = new Bucket[a1 >> 1];
        /*SL:632*/this._indexMask = a1 - 1;
        /*SL:633*/this._sizeThreshold = _thresholdSize(a1);
        int n = /*EL:635*/0;
        int longestCollisionList = /*EL:639*/0;
        /*SL:640*/for (final String s : /*EL:641*/symbols) {
            /*SL:642*/if (s != null) {
                /*SL:643*/++n;
                final int v0 = /*EL:644*/this._hashToIndex(this.calcHash(s));
                /*SL:645*/if (this._symbols[v0] == null) {
                    /*SL:646*/this._symbols[v0] = s;
                }
                else {
                    final int v = /*EL:648*/v0 >> 1;
                    final Bucket v2 = /*EL:649*/new Bucket(s, this._buckets[v]);
                    /*SL:650*/this._buckets[v] = v2;
                    /*SL:651*/longestCollisionList = Math.max(longestCollisionList, v2.length);
                }
            }
        }
        /*SL:656*/length >>= 1;
        /*SL:657*/for (Bucket next : /*EL:658*/buckets) {
            /*SL:659*/while (next != null) {
                /*SL:660*/++n;
                final String v3 = /*EL:661*/next.symbol;
                final int v = /*EL:662*/this._hashToIndex(this.calcHash(v3));
                /*SL:663*/if (this._symbols[v] == null) {
                    /*SL:664*/this._symbols[v] = v3;
                }
                else {
                    final int v4 = /*EL:666*/v >> 1;
                    final Bucket v5 = /*EL:667*/new Bucket(v3, this._buckets[v4]);
                    /*SL:668*/this._buckets[v4] = v5;
                    /*SL:669*/longestCollisionList = Math.max(longestCollisionList, v5.length);
                }
                /*SL:671*/next = next.next;
            }
        }
        /*SL:674*/this._longestCollisionList = longestCollisionList;
        /*SL:675*/this._overflows = null;
        /*SL:677*/if (n != this._size) {
            /*SL:678*/throw new IllegalStateException(String.format("Internal error on SymbolTable.rehash(): had %d entries; now have %d", this._size, n));
        }
    }
    
    protected void reportTooManyCollisions(final int a1) {
        /*SL:688*/throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._size + ") now exceeds maximum, " + a1 + " -- suspect a DoS attack based on hash collisions");
    }
    
    static final class Bucket
    {
        public final String symbol;
        public final Bucket next;
        public final int length;
        
        public Bucket(final String a1, final Bucket a2) {
            this.symbol = a1;
            this.next = a2;
            this.length = ((a2 == null) ? 1 : (a2.length + 1));
        }
        
        public String has(final char[] a1, final int a2, final int a3) {
            /*SL:764*/if (this.symbol.length() != a3) {
                /*SL:765*/return null;
            }
            int v1 = /*EL:767*/0;
            /*SL:769*/while (this.symbol.charAt(v1) == a1[a2 + v1]) {
                /*SL:772*/if (++v1 >= a3) {
                    /*SL:773*/return this.symbol;
                }
            }
            return null;
        }
    }
    
    private static final class TableInfo
    {
        final int size;
        final int longestCollisionList;
        final String[] symbols;
        final Bucket[] buckets;
        
        public TableInfo(final int a1, final int a2, final String[] a3, final Bucket[] a4) {
            this.size = a1;
            this.longestCollisionList = a2;
            this.symbols = a3;
            this.buckets = a4;
        }
        
        public TableInfo(final CharsToNameCanonicalizer a1) {
            this.size = a1._size;
            this.longestCollisionList = a1._longestCollisionList;
            this.symbols = a1._symbols;
            this.buckets = a1._buckets;
        }
        
        public static TableInfo createInitial(final int a1) {
            /*SL:809*/return new TableInfo(0, 0, new String[a1], new Bucket[a1 >> 1]);
        }
    }
}
