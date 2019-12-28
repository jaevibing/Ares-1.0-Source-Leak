package com.google.api.client.repackaged.com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Objects extends ExtraObjectsMethodsForWeb
{
    public static boolean equal(@Nullable final Object a1, @Nullable final Object a2) {
        /*SL:55*/return a1 == a2 || (a1 != null && a1.equals(a2));
    }
    
    public static int hashCode(@Nullable final Object... a1) {
        /*SL:79*/return Arrays.hashCode(a1);
    }
    
    @Deprecated
    public static ToStringHelper toStringHelper(final Object a1) {
        /*SL:126*/return new ToStringHelper(a1.getClass().getSimpleName());
    }
    
    @Deprecated
    public static ToStringHelper toStringHelper(final Class<?> a1) {
        /*SL:143*/return new ToStringHelper(a1.getSimpleName());
    }
    
    @Deprecated
    public static ToStringHelper toStringHelper(final String a1) {
        /*SL:158*/return new ToStringHelper(a1);
    }
    
    @Deprecated
    public static <T> T firstNonNull(@Nullable final T a1, @Nullable final T a2) {
        /*SL:179*/return MoreObjects.<T>firstNonNull(a1, a2);
    }
    
    @Deprecated
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
            /*SL:213*/this.omitNullValues = true;
            /*SL:214*/return this;
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, @Nullable final Object a2) {
            /*SL:224*/return this.addHolder(a1, a2);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final boolean a2) {
            /*SL:234*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final char a2) {
            /*SL:244*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final double a2) {
            /*SL:254*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final float a2) {
            /*SL:264*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final int a2) {
            /*SL:274*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper add(final String a1, final long a2) {
            /*SL:284*/return this.addHolder(a1, String.valueOf(a2));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(@Nullable final Object a1) {
            /*SL:295*/return this.addHolder(a1);
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final boolean a1) {
            /*SL:308*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final char a1) {
            /*SL:321*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final double a1) {
            /*SL:334*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final float a1) {
            /*SL:347*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final int a1) {
            /*SL:360*/return this.addHolder(String.valueOf(a1));
        }
        
        @CanIgnoreReturnValue
        public ToStringHelper addValue(final long a1) {
            /*SL:373*/return this.addHolder(String.valueOf(a1));
        }
        
        @Override
        public String toString() {
            final boolean omitNullValues = /*EL:387*/this.omitNullValues;
            String s = /*EL:388*/"";
            final StringBuilder v0 = /*EL:389*/new StringBuilder(32).append(this.className).append('{');
            /*SL:391*/for (ValueHolder v = this.holderHead.next; v != null; /*SL:392*/v = v.next) {
                /*SL:393*/if (!omitNullValues || v.value != null) {
                    /*SL:394*/v0.append(s);
                    /*SL:395*/s = ", ";
                    /*SL:397*/if (v.name != null) {
                        /*SL:398*/v0.append(v.name).append('=');
                    }
                    /*SL:400*/v0.append(v.value);
                }
            }
            /*SL:403*/return v0.append('}').toString();
        }
        
        private ValueHolder addHolder() {
            final ValueHolder v1 = /*EL:407*/new ValueHolder();
            final ValueHolder holderTail = /*EL:408*/this.holderTail;
            final ValueHolder valueHolder = v1;
            holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            /*SL:409*/return v1;
        }
        
        private ToStringHelper addHolder(@Nullable final Object a1) {
            final ValueHolder v1 = /*EL:413*/this.addHolder();
            /*SL:414*/v1.value = a1;
            /*SL:415*/return this;
        }
        
        private ToStringHelper addHolder(final String a1, @Nullable final Object a2) {
            final ValueHolder v1 = /*EL:419*/this.addHolder();
            /*SL:420*/v1.value = a2;
            /*SL:421*/v1.name = Preconditions.<String>checkNotNull(a1);
            /*SL:422*/return this;
        }
        
        private static final class ValueHolder
        {
            String name;
            Object value;
            ValueHolder next;
        }
    }
}
