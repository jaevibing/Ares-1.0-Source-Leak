package org.spongepowered.asm.util.perf;

import java.util.Arrays;
import java.text.DecimalFormat;
import org.spongepowered.asm.util.PrettyPrinter;
import java.util.Collections;
import java.util.Collection;
import java.util.NoSuchElementException;
import com.google.common.base.Joiner;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public final class Profiler
{
    public static final int ROOT = 1;
    public static final int FINE = 2;
    private final Map<String, Section> sections;
    private final List<String> phases;
    private final Deque<Section> stack;
    private boolean active;
    
    public Profiler() {
        this.sections = new TreeMap<String, Section>();
        this.phases = new ArrayList<String>();
        this.stack = new LinkedList<Section>();
        this.phases.add("Initial");
    }
    
    public void setActive(final boolean a1) {
        /*SL:517*/if ((!this.active && a1) || !a1) {
            /*SL:518*/this.reset();
        }
        /*SL:520*/this.active = a1;
    }
    
    public void reset() {
        /*SL:527*/for (final Section v1 : this.sections.values()) {
            /*SL:528*/v1.invalidate();
        }
        /*SL:531*/this.sections.clear();
        /*SL:532*/this.phases.clear();
        /*SL:533*/this.phases.add("Initial");
        /*SL:534*/this.stack.clear();
    }
    
    public Section get(final String a1) {
        Section v1 = /*EL:544*/this.sections.get(a1);
        /*SL:545*/if (v1 == null) {
            /*SL:546*/v1 = (this.active ? new LiveSection(a1, this.phases.size() - 1) : new Section(a1));
            /*SL:547*/this.sections.put(a1, v1);
        }
        /*SL:550*/return v1;
    }
    
    private Section getSubSection(final String a1, final String a2, final Section a3) {
        Section v1 = /*EL:554*/this.sections.get(a1);
        /*SL:555*/if (v1 == null) {
            /*SL:556*/v1 = new SubSection(a1, this.phases.size() - 1, a2, a3);
            /*SL:557*/this.sections.put(a1, v1);
        }
        /*SL:560*/return v1;
    }
    
    boolean isHead(final Section a1) {
        /*SL:564*/return this.stack.peek() == a1;
    }
    
    public Section begin(final String... a1) {
        /*SL:574*/return this.begin(0, a1);
    }
    
    public Section begin(final int a1, final String... a2) {
        /*SL:585*/return this.begin(a1, Joiner.on('.').join(a2));
    }
    
    public Section begin(final String a1) {
        /*SL:595*/return this.begin(0, a1);
    }
    
    public Section begin(final int v1, String v2) {
        boolean v3 = /*EL:606*/(v1 & 0x1) != 0x0;
        final boolean v4 = /*EL:607*/(v1 & 0x2) != 0x0;
        String v5 = /*EL:609*/v2;
        final Section v6 = /*EL:610*/this.stack.peek();
        /*SL:611*/if (v6 != null) {
            /*SL:612*/v5 = v6.getName() + (v3 ? " -> " : ".") + v5;
            /*SL:613*/if (v6.isRoot() && !v3) {
                final int a1 = /*EL:614*/v6.getName().lastIndexOf(" -> ");
                /*SL:615*/v2 = ((a1 > -1) ? v6.getName().substring(a1 + 4) : v6.getName()) + "." + v2;
                /*SL:616*/v3 = true;
            }
        }
        Section v7 = /*EL:620*/this.get(v3 ? v2 : v5);
        /*SL:621*/if (v3 && v6 != null && this.active) {
            /*SL:622*/v7 = this.getSubSection(v5, v6.getName(), v7);
        }
        /*SL:625*/v7.setFine(v4).setRoot(v3);
        /*SL:626*/this.stack.push(v7);
        /*SL:628*/return v7.start();
    }
    
    void end(final Section v-1) {
        try {
            Section v1;
            final Section a1 = /*EL:639*/v1 = this.stack.pop();
            while (v1 != v-1) {
                /*SL:640*/if (v1 == null && this.active) {
                    /*SL:641*/if (a1 == null) {
                        /*SL:642*/throw new IllegalStateException("Attempted to pop " + v-1 + " but the stack is empty");
                    }
                    /*SL:644*/throw new IllegalStateException("Attempted to pop " + v-1 + " which was not in the stack, head was " + a1);
                }
                else {
                    v1 = this.stack.pop();
                }
            }
        }
        catch (NoSuchElementException v2) {
            /*SL:648*/if (this.active) {
                /*SL:649*/throw new IllegalStateException("Attempted to pop " + v-1 + " but the stack is empty");
            }
        }
    }
    
    public void mark(final String v-2) {
        long n = /*EL:662*/0L;
        /*SL:663*/for (final Section a1 : this.sections.values()) {
            /*SL:664*/n += a1.getTime();
        }
        /*SL:668*/if (n == 0L) {
            final int v1 = /*EL:669*/this.phases.size();
            /*SL:670*/this.phases.set(v1 - 1, v-2);
            /*SL:671*/return;
        }
        /*SL:674*/this.phases.add(v-2);
        /*SL:675*/for (final Section v2 : this.sections.values()) {
            /*SL:676*/v2.mark();
        }
    }
    
    public Collection<Section> getSections() {
        /*SL:684*/return Collections.<Section>unmodifiableCollection((Collection<? extends Section>)this.sections.values());
    }
    
    public PrettyPrinter printer(final boolean v-9, final boolean v-8) {
        final PrettyPrinter prettyPrinter = /*EL:696*/new PrettyPrinter();
        final int n = /*EL:699*/this.phases.size() + 4;
        final int[] array = /*EL:704*/{ 0, 1, 2, n - 2, n - 1 };
        final Object[] v-10 = /*EL:706*/new Object[n * 2];
        int a1 = /*EL:707*/0;
        int a2 = 0;
        while (a1 < n) {
            /*SL:708*/v-10[a2 + 1] = PrettyPrinter.Alignment.RIGHT;
            /*SL:709*/if (a1 == array[0]) {
                /*SL:710*/v-10[a2] = (v-8 ? "" : "  ") + "Section";
                /*SL:711*/v-10[a2 + 1] = PrettyPrinter.Alignment.LEFT;
            }
            else/*SL:712*/ if (a1 == array[1]) {
                /*SL:713*/v-10[a2] = "    TOTAL";
            }
            else/*SL:714*/ if (a1 == array[3]) {
                /*SL:715*/v-10[a2] = "    Count";
            }
            else/*SL:716*/ if (a1 == array[4]) {
                /*SL:717*/v-10[a2] = "Avg. ";
            }
            else/*SL:718*/ if (a1 - array[2] < this.phases.size()) {
                /*SL:719*/v-10[a2] = this.phases.get(a1 - array[2]);
            }
            else {
                /*SL:721*/v-10[a2] = "";
            }
            a2 = ++a1 * 2;
        }
        /*SL:725*/prettyPrinter.table(v-10).th().hr().add();
        /*SL:727*/for (final Section v2 : this.sections.values()) {
            /*SL:728*/if (!v2.isFine() || v-9) {
                if (v-8 && v2.getDelegate() != v2) {
                    /*SL:729*/continue;
                }
                /*SL:733*/this.printSectionRow(prettyPrinter, n, array, v2, v-8);
                /*SL:736*/if (!v-8) {
                    continue;
                }
                /*SL:737*/for (final Section v0 : this.sections.values()) {
                    final Section v = /*EL:738*/v0.getDelegate();
                    /*SL:739*/if ((!v0.isFine() || v-9) && v == v2) {
                        if (v == v0) {
                            /*SL:740*/continue;
                        }
                        /*SL:743*/this.printSectionRow(prettyPrinter, n, array, v0, v-8);
                    }
                }
            }
        }
        /*SL:748*/return prettyPrinter.add();
    }
    
    private void printSectionRow(final PrettyPrinter a4, final int a5, final int[] v1, final Section v2, final boolean v3) {
        final boolean v4 = /*EL:752*/v2.getDelegate() != v2;
        final Object[] v5 = /*EL:753*/new Object[a5];
        int v6 = /*EL:754*/1;
        /*SL:755*/if (v3) {
            /*SL:756*/v5[0] = (v4 ? ("  > " + v2.getBaseName()) : v2.getName());
        }
        else {
            /*SL:758*/v5[0] = (v4 ? "+ " : "  ") + v2.getName();
        }
        final long[] times;
        final long[] v7 = /*EL:762*/times = v2.getTimes();
        for (final long a6 : times) {
            /*SL:763*/if (v6 == v1[1]) {
                /*SL:764*/v5[v6++] = v2.getTotalTime() + " ms";
            }
            /*SL:766*/if (v6 >= v1[2] && v6 < v5.length) {
                /*SL:767*/v5[v6++] = a6 + " ms";
            }
        }
        /*SL:771*/v5[v1[3]] = v2.getTotalCount();
        /*SL:772*/v5[v1[4]] = new DecimalFormat("   ###0.000 ms").format(v2.getTotalAverageTime());
        /*SL:774*/for (int a7 = 0; a7 < v5.length; ++a7) {
            /*SL:775*/if (v5[a7] == null) {
                /*SL:776*/v5[a7] = "-";
            }
        }
        /*SL:780*/a4.tr(v5);
    }
    
    public class Section
    {
        static final String SEPARATOR_ROOT = " -> ";
        static final String SEPARATOR_CHILD = ".";
        private final String name;
        private boolean root;
        private boolean fine;
        protected boolean invalidated;
        private String info;
        
        Section(final String a2) {
            this.name = a2;
            this.info = a2;
        }
        
        Section getDelegate() {
            /*SL:97*/return this;
        }
        
        Section invalidate() {
            /*SL:101*/this.invalidated = true;
            /*SL:102*/return this;
        }
        
        Section setRoot(final boolean a1) {
            /*SL:111*/this.root = a1;
            /*SL:112*/return this;
        }
        
        public boolean isRoot() {
            /*SL:119*/return this.root;
        }
        
        Section setFine(final boolean a1) {
            /*SL:128*/this.fine = a1;
            /*SL:129*/return this;
        }
        
        public boolean isFine() {
            /*SL:136*/return this.fine;
        }
        
        public String getName() {
            /*SL:143*/return this.name;
        }
        
        public String getBaseName() {
            /*SL:151*/return this.name;
        }
        
        public void setInfo(final String a1) {
            /*SL:160*/this.info = a1;
        }
        
        public String getInfo() {
            /*SL:167*/return this.info;
        }
        
        Section start() {
            /*SL:176*/return this;
        }
        
        protected Section stop() {
            /*SL:185*/return this;
        }
        
        public Section end() {
            /*SL:194*/if (!this.invalidated) {
                /*SL:195*/Profiler.this.end(this);
            }
            /*SL:197*/return this;
        }
        
        public Section next(final String a1) {
            /*SL:207*/this.end();
            /*SL:208*/return Profiler.this.begin(a1);
        }
        
        void mark() {
        }
        
        public long getTime() {
            /*SL:224*/return 0L;
        }
        
        public long getTotalTime() {
            /*SL:231*/return 0L;
        }
        
        public double getSeconds() {
            /*SL:238*/return 0.0;
        }
        
        public double getTotalSeconds() {
            /*SL:245*/return 0.0;
        }
        
        public long[] getTimes() {
            /*SL:253*/return new long[1];
        }
        
        public int getCount() {
            /*SL:260*/return 0;
        }
        
        public int getTotalCount() {
            /*SL:267*/return 0;
        }
        
        public double getAverageTime() {
            /*SL:275*/return 0.0;
        }
        
        public double getTotalAverageTime() {
            /*SL:283*/return 0.0;
        }
        
        @Override
        public final String toString() {
            /*SL:291*/return this.name;
        }
    }
    
    class LiveSection extends Section
    {
        private int cursor;
        private long[] times;
        private long start;
        private long time;
        private long markedTime;
        private int count;
        private int markedCount;
        
        LiveSection(final String a2, final int a3) {
            super(a2);
            this.cursor = 0;
            this.times = new long[0];
            this.start = 0L;
            this.cursor = a3;
        }
        
        @Override
        Section start() {
            /*SL:335*/this.start = System.currentTimeMillis();
            /*SL:336*/return this;
        }
        
        @Override
        protected Section stop() {
            /*SL:341*/if (this.start > 0L) {
                /*SL:342*/this.time += System.currentTimeMillis() - this.start;
            }
            /*SL:344*/this.start = 0L;
            /*SL:345*/++this.count;
            /*SL:346*/return this;
        }
        
        @Override
        public Section end() {
            /*SL:351*/this.stop();
            /*SL:352*/if (!this.invalidated) {
                /*SL:353*/Profiler.this.end(this);
            }
            /*SL:355*/return this;
        }
        
        @Override
        void mark() {
            /*SL:360*/if (this.cursor >= this.times.length) {
                /*SL:361*/this.times = Arrays.copyOf(this.times, this.cursor + 4);
            }
            /*SL:363*/this.times[this.cursor] = this.time;
            /*SL:364*/this.markedTime += this.time;
            /*SL:365*/this.markedCount += this.count;
            /*SL:366*/this.time = 0L;
            /*SL:367*/this.count = 0;
            /*SL:368*/++this.cursor;
        }
        
        @Override
        public long getTime() {
            /*SL:373*/return this.time;
        }
        
        @Override
        public long getTotalTime() {
            /*SL:378*/return this.time + this.markedTime;
        }
        
        @Override
        public double getSeconds() {
            /*SL:383*/return this.time * 0.001;
        }
        
        @Override
        public double getTotalSeconds() {
            /*SL:388*/return (this.time + this.markedTime) * 0.001;
        }
        
        @Override
        public long[] getTimes() {
            final long[] v1 = /*EL:393*/new long[this.cursor + 1];
            /*SL:394*/System.arraycopy(this.times, 0, v1, 0, Math.min(this.times.length, this.cursor));
            /*SL:395*/v1[this.cursor] = this.time;
            /*SL:396*/return v1;
        }
        
        @Override
        public int getCount() {
            /*SL:401*/return this.count;
        }
        
        @Override
        public int getTotalCount() {
            /*SL:406*/return this.count + this.markedCount;
        }
        
        @Override
        public double getAverageTime() {
            /*SL:411*/return (this.count > 0) ? (this.time / this.count) : 0.0;
        }
        
        @Override
        public double getTotalAverageTime() {
            /*SL:416*/return (this.count > 0) ? ((this.time + this.markedTime) / (this.count + this.markedCount)) : 0.0;
        }
    }
    
    class SubSection extends LiveSection
    {
        private final String baseName;
        private final Section root;
        
        SubSection(final String a2, final int a3, final String a4, final Section a5) {
            super(a2, a3);
            this.baseName = a4;
            this.root = a5;
        }
        
        @Override
        Section invalidate() {
            /*SL:446*/this.root.invalidate();
            /*SL:447*/return super.invalidate();
        }
        
        @Override
        public String getBaseName() {
            /*SL:452*/return this.baseName;
        }
        
        @Override
        public void setInfo(final String a1) {
            /*SL:457*/this.root.setInfo(a1);
            /*SL:458*/super.setInfo(a1);
        }
        
        @Override
        Section getDelegate() {
            /*SL:463*/return this.root;
        }
        
        @Override
        Section start() {
            /*SL:468*/this.root.start();
            /*SL:469*/return super.start();
        }
        
        @Override
        public Section end() {
            /*SL:474*/this.root.stop();
            /*SL:475*/return super.end();
        }
        
        @Override
        public Section next(final String a1) {
            /*SL:480*/super.stop();
            /*SL:481*/return this.root.next(a1);
        }
    }
}
