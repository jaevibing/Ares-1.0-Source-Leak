package com.google.api.client.util;

import com.google.api.client.repackaged.com.google.common.base.Objects;

public final class Objects
{
    public static boolean equal(final Object a1, final Object a2) {
        /*SL:41*/return com.google.api.client.repackaged.com.google.common.base.Objects.equal(a1, a2);
    }
    
    public static ToStringHelper toStringHelper(final Object a1) {
        /*SL:84*/return new ToStringHelper(a1.getClass().getSimpleName());
    }
    
    public static final class ToStringHelper
    {
        private final String className;
        private ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;
        
        ToStringHelper(final String a1) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.className = a1;
        }
        
        public ToStringHelper omitNullValues() {
            /*SL:108*/this.omitNullValues = true;
            /*SL:109*/return this;
        }
        
        public ToStringHelper add(final String a1, final Object a2) {
            /*SL:118*/return this.addHolder(a1, a2);
        }
        
        @Override
        public String toString() {
            final boolean omitNullValues = /*EL:124*/this.omitNullValues;
            String s = /*EL:125*/"";
            final StringBuilder v0 = /*EL:126*/new StringBuilder(32).append(this.className).append('{');
            /*SL:128*/for (ValueHolder v = this.holderHead.next; v != null; /*SL:129*/v = v.next) {
                /*SL:130*/if (!omitNullValues || v.value != null) {
                    /*SL:131*/v0.append(s);
                    /*SL:132*/s = ", ";
                    /*SL:134*/if (v.name != null) {
                        /*SL:135*/v0.append(v.name).append('=');
                    }
                    /*SL:137*/v0.append(v.value);
                }
            }
            /*SL:140*/return v0.append('}').toString();
        }
        
        private ValueHolder addHolder() {
            final ValueHolder v1 = /*EL:144*/new ValueHolder();
            final ValueHolder holderTail = /*EL:145*/this.holderTail;
            final ValueHolder valueHolder = v1;
            holderTail.next = valueHolder;
            this.holderTail = valueHolder;
            /*SL:146*/return v1;
        }
        
        private ToStringHelper addHolder(final String a1, final Object a2) {
            final ValueHolder v1 = /*EL:150*/this.addHolder();
            /*SL:151*/v1.value = a2;
            /*SL:152*/v1.name = Preconditions.<String>checkNotNull(a1);
            /*SL:153*/return this;
        }
        
        private static final class ValueHolder
        {
            String name;
            Object value;
            ValueHolder next;
        }
    }
}
