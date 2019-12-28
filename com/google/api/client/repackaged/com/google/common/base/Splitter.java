package com.google.api.client.repackaged.com.google.common.base;

import java.util.LinkedHashMap;
import java.util.Map;
import com.google.api.client.repackaged.com.google.common.annotations.Beta;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import com.google.api.client.repackaged.com.google.common.annotations.GwtIncompatible;
import java.util.regex.Pattern;
import java.util.Iterator;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Splitter
{
    private final CharMatcher trimmer;
    private final boolean omitEmptyStrings;
    private final Strategy strategy;
    private final int limit;
    
    private Splitter(final Strategy a1) {
        this(a1, false, CharMatcher.none(), Integer.MAX_VALUE);
    }
    
    private Splitter(final Strategy a1, final boolean a2, final CharMatcher a3, final int a4) {
        this.strategy = a1;
        this.omitEmptyStrings = a2;
        this.trimmer = a3;
        this.limit = a4;
    }
    
    public static Splitter on(final char a1) {
        /*SL:119*/return on(CharMatcher.is(a1));
    }
    
    public static Splitter on(final CharMatcher a1) {
        /*SL:133*/Preconditions.<CharMatcher>checkNotNull(a1);
        /*SL:135*/return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter a1, final CharSequence a2) {
                /*SL:139*/return new SplittingIterator(a1, a2) {
                    @Override
                    int separatorStart(final int a1) {
                        /*SL:142*/return a1.indexIn(this.toSplit, a1);
                    }
                    
                    @Override
                    int separatorEnd(final int a1) {
                        /*SL:147*/return a1 + 1;
                    }
                };
            }
        });
    }
    
    public static Splitter on(final String a1) {
        /*SL:163*/Preconditions.checkArgument(a1.length() != 0, (Object)"The separator may not be the empty string.");
        /*SL:164*/if (a1.length() == 1) {
            /*SL:165*/return on(a1.charAt(0));
        }
        /*SL:167*/return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter a1, final CharSequence a2) {
                /*SL:171*/return new SplittingIterator(a1, a2) {
                    public int separatorStart(final int v-1) {
                        final int v0 = /*EL:174*//*EL:179*/a1.length();
                        int v = v-1;
                        final int v2 = this.toSplit.length() - v0;
                    Label_0026:
                        while (v <= v2) {
                            for (int a1 = 0; a1 < v0; ++a1) {
                                if (this.toSplit.charAt(a1 + v) != a1.charAt(a1)) {
                                    ++v;
                                    continue Label_0026;
                                }
                            }
                            /*SL:183*/return v;
                        }
                        /*SL:185*/return -1;
                    }
                    
                    public int separatorEnd(final int a1) {
                        /*SL:190*/return a1 + a1.length();
                    }
                };
            }
        });
    }
    
    @GwtIncompatible
    public static Splitter on(final Pattern a1) {
        /*SL:209*/return on(new JdkPattern(a1));
    }
    
    private static Splitter on(final CommonPattern a1) {
        /*SL:213*/Preconditions.checkArgument(!a1.matcher("").matches(), "The pattern may not match the empty string: %s", a1);
        /*SL:218*/return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter a1, final CharSequence a2) {
                final CommonMatcher v1 = /*EL:222*//*EL:223*/a1.matcher(a2);
                return new SplittingIterator(a1, a2) {
                    public int separatorStart(final int a1) {
                        /*SL:226*/return v1.find(a1) ? v1.start() : -1;
                    }
                    
                    public int separatorEnd(final int a1) {
                        /*SL:231*/return v1.end();
                    }
                };
            }
        });
    }
    
    @GwtIncompatible
    public static Splitter onPattern(final String a1) {
        /*SL:252*/return on(Platform.compilePattern(a1));
    }
    
    public static Splitter fixedLength(final int a1) {
        /*SL:273*/Preconditions.checkArgument(a1 > 0, (Object)"The length may not be less than 1");
        /*SL:275*/return new Splitter(new Strategy() {
            @Override
            public SplittingIterator iterator(final Splitter a1, final CharSequence a2) {
                /*SL:279*/return new SplittingIterator(a1, a2) {
                    public int separatorStart(final int a1) {
                        final int v1 = /*EL:282*/a1 + /*EL:283*/a1;
                        return (v1 < this.toSplit.length()) ? v1 : -1;
                    }
                    
                    public int separatorEnd(final int a1) {
                        /*SL:288*/return a1;
                    }
                };
            }
        });
    }
    
    public Splitter omitEmptyStrings() {
        /*SL:312*/return new Splitter(this.strategy, true, this.trimmer, this.limit);
    }
    
    public Splitter limit(final int a1) {
        /*SL:333*/Preconditions.checkArgument(a1 > 0, "must be greater than zero: %s", a1);
        /*SL:334*/return new Splitter(this.strategy, this.omitEmptyStrings, this.trimmer, a1);
    }
    
    public Splitter trimResults() {
        /*SL:347*/return this.trimResults(CharMatcher.whitespace());
    }
    
    public Splitter trimResults(final CharMatcher a1) {
        /*SL:363*/Preconditions.<CharMatcher>checkNotNull(a1);
        /*SL:364*/return new Splitter(this.strategy, this.omitEmptyStrings, a1, this.limit);
    }
    
    public Iterable<String> split(final CharSequence a1) {
        /*SL:376*/Preconditions.<CharSequence>checkNotNull(a1);
        /*SL:378*/return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                /*SL:381*/return Splitter.this.splittingIterator(a1);
            }
            
            @Override
            public String toString() {
                /*SL:386*/return Joiner.on(", ").appendTo(new StringBuilder().append('['), (Iterable<?>)this).append(']').toString();
            }
        };
    }
    
    private Iterator<String> splittingIterator(final CharSequence a1) {
        /*SL:395*/return this.strategy.iterator(this, a1);
    }
    
    @Beta
    public List<String> splitToList(final CharSequence a1) {
        /*SL:408*/Preconditions.<CharSequence>checkNotNull(a1);
        final Iterator<String> v1 = /*EL:410*/this.splittingIterator(a1);
        final List<String> v2 = /*EL:411*/new ArrayList<String>();
        /*SL:413*/while (v1.hasNext()) {
            /*SL:414*/v2.add(v1.next());
        }
        /*SL:417*/return Collections.<String>unmodifiableList((List<? extends String>)v2);
    }
    
    @Beta
    public MapSplitter withKeyValueSeparator(final String a1) {
        /*SL:428*/return this.withKeyValueSeparator(on(a1));
    }
    
    @Beta
    public MapSplitter withKeyValueSeparator(final char a1) {
        /*SL:439*/return this.withKeyValueSeparator(on(a1));
    }
    
    @Beta
    public MapSplitter withKeyValueSeparator(final Splitter a1) {
        /*SL:450*/return new MapSplitter(this, a1);
    }
    
    @Beta
    public static final class MapSplitter
    {
        private static final String INVALID_ENTRY_MESSAGE = "Chunk [%s] is not a valid entry";
        private final Splitter outerSplitter;
        private final Splitter entrySplitter;
        
        private MapSplitter(final Splitter a1, final Splitter a2) {
            this.outerSplitter = a1;
            this.entrySplitter = Preconditions.<Splitter>checkNotNull(a2);
        }
        
        public Map<String, String> split(final CharSequence v-4) {
            final Map<String, String> map = /*EL:482*/new LinkedHashMap<String, String>();
            /*SL:483*/for (final String s : this.outerSplitter.split(v-4)) {
                final Iterator<String> a1 = /*EL:484*/this.entrySplitter.splittingIterator(s);
                /*SL:486*/Preconditions.checkArgument(a1.hasNext(), "Chunk [%s] is not a valid entry", s);
                final String v1 = /*EL:487*/a1.next();
                /*SL:488*/Preconditions.checkArgument(!map.containsKey(v1), "Duplicate key [%s] found.", v1);
                /*SL:490*/Preconditions.checkArgument(a1.hasNext(), "Chunk [%s] is not a valid entry", s);
                final String v2 = /*EL:491*/a1.next();
                /*SL:492*/map.put(v1, v2);
                /*SL:494*/Preconditions.checkArgument(!a1.hasNext(), "Chunk [%s] is not a valid entry", s);
            }
            /*SL:496*/return Collections.<String, String>unmodifiableMap((Map<? extends String, ? extends String>)map);
        }
    }
    
    private abstract static class SplittingIterator extends AbstractIterator<String>
    {
        final CharSequence toSplit;
        final CharMatcher trimmer;
        final boolean omitEmptyStrings;
        int offset;
        int limit;
        
        abstract int separatorStart(final int p0);
        
        abstract int separatorEnd(final int p0);
        
        protected SplittingIterator(final Splitter a1, final CharSequence a2) {
            this.offset = 0;
            this.trimmer = a1.trimmer;
            this.omitEmptyStrings = a1.omitEmptyStrings;
            this.limit = a1.limit;
            this.toSplit = a2;
        }
        
        @Override
        protected String computeNext() {
            int n = /*EL:539*/this.offset;
            /*SL:540*/while (this.offset != -1) {
                int n2 = /*EL:541*/n;
                final int v0 = /*EL:544*/this.separatorStart(this.offset);
                int v;
                /*SL:545*/if (v0 == -1) {
                    /*SL:546*/v = this.toSplit.length();
                    /*SL:547*/this.offset = -1;
                }
                else {
                    /*SL:549*/v = v0;
                    /*SL:550*/this.offset = this.separatorEnd(v0);
                }
                /*SL:552*/if (this.offset == n) {
                    /*SL:559*/++this.offset;
                    /*SL:560*/if (this.offset < this.toSplit.length()) {
                        continue;
                    }
                    /*SL:561*/this.offset = -1;
                }
                else {
                    /*SL:566*/while (n2 < v && this.trimmer.matches(this.toSplit.charAt(n2))) {
                        /*SL:567*/++n2;
                    }
                    /*SL:569*/while (v > n2 && this.trimmer.matches(this.toSplit.charAt(v - 1))) {
                        /*SL:570*/--v;
                    }
                    /*SL:573*/if (!this.omitEmptyStrings || n2 != v) {
                        /*SL:579*/if (this.limit == 1) {
                            /*SL:583*/v = this.toSplit.length();
                            /*SL:584*/this.offset = -1;
                            /*SL:586*/while (v > n2 && this.trimmer.matches(this.toSplit.charAt(v - 1))) {
                                /*SL:587*/--v;
                            }
                        }
                        else {
                            /*SL:590*/--this.limit;
                        }
                        /*SL:593*/return this.toSplit.subSequence(n2, v).toString();
                    }
                    n = this.offset;
                }
            }
            /*SL:595*/return this.endOfData();
        }
    }
    
    private interface Strategy
    {
        Iterator<String> iterator(Splitter p0, CharSequence p1);
    }
}
