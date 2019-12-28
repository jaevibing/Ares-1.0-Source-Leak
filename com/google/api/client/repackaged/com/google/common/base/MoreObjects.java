package com.google.api.client.repackaged.com.google.common.base;

import java.util.Arrays;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class MoreObjects
{
    public static <T> T firstNonNull(@Nullable final T a1, @Nullable final T a2) {
        /*SL:56*/return (a1 != null) ? a1 : Preconditions.<T>checkNotNull(a2);
    }
    
    public static ToStringHelper toStringHelper(final Object a1) {
        /*SL:100*/return new ToStringHelper(a1.getClass().getSimpleName());
    }
    
    public static ToStringHelper toStringHelper(final Class<?> a1) {
        /*SL:114*/return new ToStringHelper(a1.getSimpleName());
    }
    
    public static ToStringHelper toStringHelper(final String a1) {
        /*SL:126*/return new ToStringHelper(a1);
    }
    
    public static final class ToStringHelper
    {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;
        
        private ToStringHelper(final String a1) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.className = Preconditions.<String>checkNotNull(a1);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper omitNullValues() {
            /*SL:157*/this.omitNullValues = true;
            /*SL:158*/return this;
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, @Nullable final Object a2) {
            /*SL:168*/return this.addHolder(a1, a2);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final boolean a2) {
            /*SL:178*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final char a2) {
            /*SL:188*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final double a2) {
            /*SL:198*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final float a2) {
            /*SL:208*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final int a2) {
            /*SL:218*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final long a2) {
            /*SL:228*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(@Nullable final Object a1) {
            /*SL:239*/return this.addHolder(a1);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final boolean a1) {
            /*SL:252*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final char a1) {
            /*SL:265*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final double a1) {
            /*SL:278*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final float a1) {
            /*SL:291*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final int a1) {
            /*SL:304*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final long a1) {
            /*SL:317*/return this.addHolder(String.valueOf(a1));
        }
        
        @Override
        public String toString() {
            final boolean omitNullValues = /*EL:331*/this.omitNullValues;
            String s = /*EL:332*/"";
            final StringBuilder append = /*EL:333*/new StringBuilder(32).append(this.className).append('{');
            /*SL:335*/for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; /*SL:336*/valueHolder = valueHolder.next) {
                final Object v0 = /*EL:337*/valueHolder.value;
                /*SL:338*/if (!omitNullValues || v0 != null) {
                    /*SL:339*/append.append(s);
                    /*SL:340*/s = ", ";
                    /*SL:342*/if (valueHolder.name != null) {
                        /*SL:343*/append.append(valueHolder.name).append('=');
                    }
                    /*SL:345*/if (v0 != null && v0.getClass().isArray()) {
                        final Object[] v = /*EL:346*/{ v0 };
                        final String v2 = /*EL:347*/Arrays.deepToString(v);
                        /*SL:348*/append.append(v2, 1, v2.length() - 1);
                    }
                    else {
                        /*SL:350*/append.append(v0);
                    }
                }
            }
            /*SL:354*/return append.append('}').toString();
        }
        
        private ValueHolder addHolder() {
            final ValueHolder v1 = /*EL:358*/new ValueHolder();
            final ValueHolder holderTail = /*EL:359*/this.holderTail;
            final ValueHolder valueHolder = v1;
            holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            /*SL:360*/return v1;
        }
        
        private ToStringHelper addHolder(@Nullable final Object a1) {
            final ValueHolder v1 = /*EL:364*/this.addHolder();
            /*SL:365*/v1.value = a1;
            /*SL:366*/return this;
        }
        
        private ToStringHelper addHolder(final String a1, @Nullable final Object a2) {
            final ValueHolder v1 = /*EL:370*/this.addHolder();
            /*SL:371*/v1.value = a2;
            /*SL:372*/v1.name = Preconditions.<String>checkNotNull(a1);
            /*SL:373*/return this;
        }
        
        private static final class ValueHolder
        {
            String name;
            Object value;
            ValueHolder next;
        }
    }
}
