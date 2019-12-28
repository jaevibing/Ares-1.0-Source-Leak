package com.google.api.client.repackaged.com.google.common.base;

import com.google.api.client.repackaged.com.google.common.annotations.Beta;
import java.util.Map;
import java.util.AbstractList;
import javax.annotation.Nullable;
import java.util.Arrays;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.util.Iterator;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
public class Joiner
{
    private final String separator;
    
    public static Joiner on(final String a1) {
        /*SL:67*/return new Joiner(a1);
    }
    
    public static Joiner on(final char a1) {
        /*SL:74*/return new Joiner(String.valueOf(a1));
    }
    
    private Joiner(final String a1) {
        this.separator = Preconditions.<String>checkNotNull(a1);
    }
    
    private Joiner(final Joiner a1) {
        this.separator = a1.separator;
    }
    
    @CanIgnoreReturnValue
    public <A extends java.lang.Object> A appendTo(final A a1, final Iterable<?> a2) throws IOException {
        /*SL:93*/return (A)this.appendTo((Appendable)a1, (Iterator)a2.iterator());
    }
    
    @CanIgnoreReturnValue
    public <A extends java.lang.Object> A appendTo(final A a1, final Iterator<?> a2) throws IOException {
        /*SL:104*/Preconditions.<A>checkNotNull(a1);
        /*SL:105*/if (a2.hasNext()) {
            /*SL:106*/((Appendable)a1).append(this.toString(a2.next()));
            /*SL:107*/while (a2.hasNext()) {
                /*SL:108*/((Appendable)a1).append(this.separator);
                /*SL:109*/((Appendable)a1).append(this.toString(a2.next()));
            }
        }
        /*SL:112*/return a1;
    }
    
    @CanIgnoreReturnValue
    public final <A extends java.lang.Object> A appendTo(final A a1, final Object[] a2) throws IOException {
        /*SL:121*/return (A)this.appendTo((Appendable)a1, (Iterable)Arrays.<Object>asList(a2));
    }
    
    @CanIgnoreReturnValue
    public final <A extends java.lang.Object> A appendTo(final A a1, @Nullable final Object a2, @Nullable final Object a3, final Object... a4) throws IOException {
        /*SL:131*/return (A)this.appendTo((Appendable)a1, (Iterable)iterable(a2, a3, a4));
    }
    
    @CanIgnoreReturnValue
    public final StringBuilder appendTo(final StringBuilder a1, final Iterable<?> a2) {
        /*SL:141*/return this.appendTo(a1, a2.iterator());
    }
    
    @CanIgnoreReturnValue
    public final StringBuilder appendTo(final StringBuilder v1, final Iterator<?> v2) {
        try {
            /*SL:154*/this.appendTo((Appendable)v1, (Iterator)v2);
        }
        catch (IOException a1) {
            /*SL:156*/throw new AssertionError((Object)a1);
        }
        /*SL:158*/return v1;
    }
    
    @CanIgnoreReturnValue
    public final StringBuilder appendTo(final StringBuilder a1, final Object[] a2) {
        /*SL:168*/return this.appendTo(a1, (Iterable<?>)Arrays.<Object>asList(a2));
    }
    
    @CanIgnoreReturnValue
    public final StringBuilder appendTo(final StringBuilder a1, @Nullable final Object a2, @Nullable final Object a3, final Object... a4) {
        /*SL:179*/return this.appendTo(a1, (Iterable<?>)iterable(a2, a3, a4));
    }
    
    public final String join(final Iterable<?> a1) {
        /*SL:187*/return this.join(a1.iterator());
    }
    
    public final String join(final Iterator<?> a1) {
        /*SL:197*/return this.appendTo(new StringBuilder(), a1).toString();
    }
    
    public final String join(final Object[] a1) {
        /*SL:205*/return this.join(Arrays.<Object>asList(a1));
    }
    
    public final String join(@Nullable final Object a1, @Nullable final Object a2, final Object... a3) {
        /*SL:213*/return this.join(iterable(a1, a2, a3));
    }
    
    public Joiner useForNull(final String a1) {
        /*SL:221*/Preconditions.<String>checkNotNull(a1);
        /*SL:222*/return new Joiner(this) {
            @Override
            CharSequence toString(@Nullable final Object a1) {
                /*SL:225*/return (a1 == null) ? a1 : Joiner.this.toString(a1);
            }
            
            @Override
            public Joiner useForNull(final String a1) {
                /*SL:230*/throw new UnsupportedOperationException("already specified useForNull");
            }
            
            @Override
            public Joiner skipNulls() {
                /*SL:235*/throw new UnsupportedOperationException("already specified useForNull");
            }
        };
    }
    
    public Joiner skipNulls() {
        /*SL:245*/return new Joiner(this) {
            @Override
            public <A extends java.lang.Object> A appendTo(final A v2, final Iterator<?> v3) throws IOException {
                /*SL:248*/Preconditions.<A>checkNotNull(v2, (Object)"appendable");
                /*SL:249*/Preconditions.<Iterator<?>>checkNotNull(v3, (Object)"parts");
                /*SL:250*/while (v3.hasNext()) {
                    final Object a1 = /*EL:251*/v3.next();
                    /*SL:252*/if (a1 != null) {
                        /*SL:253*/((Appendable)v2).append(Joiner.this.toString(a1));
                        /*SL:254*/break;
                    }
                }
                /*SL:257*/while (v3.hasNext()) {
                    final Object a2 = /*EL:258*/v3.next();
                    /*SL:259*/if (a2 != null) {
                        /*SL:260*/((Appendable)v2).append(Joiner.this.separator);
                        /*SL:261*/((Appendable)v2).append(Joiner.this.toString(a2));
                    }
                }
                /*SL:264*/return v2;
            }
            
            @Override
            public Joiner useForNull(final String a1) {
                /*SL:269*/throw new UnsupportedOperationException("already specified skipNulls");
            }
            
            @Override
            public MapJoiner withKeyValueSeparator(final String a1) {
                /*SL:274*/throw new UnsupportedOperationException("can't use .skipNulls() with maps");
            }
        };
    }
    
    public MapJoiner withKeyValueSeparator(final char a1) {
        /*SL:286*/return this.withKeyValueSeparator(String.valueOf(a1));
    }
    
    public MapJoiner withKeyValueSeparator(final String a1) {
        /*SL:294*/return new MapJoiner(this, a1);
    }
    
    CharSequence toString(final Object a1) {
        /*SL:454*/Preconditions.<Object>checkNotNull(a1);
        /*SL:455*/return (a1 instanceof CharSequence) ? ((CharSequence)a1) : a1.toString();
    }
    
    private static Iterable<Object> iterable(final Object a1, final Object a2, final Object[] a3) {
        /*SL:460*/Preconditions.<Object[]>checkNotNull(a3);
        /*SL:461*/return new AbstractList<Object>() {
            @Override
            public int size() {
                /*SL:464*/return a3.length + 2;
            }
            
            @Override
            public Object get(final int a1) {
                /*SL:469*/switch (a1) {
                    case 0: {
                        /*SL:471*/return a1;
                    }
                    case 1: {
                        /*SL:473*/return a2;
                    }
                    default: {
                        /*SL:475*/return a3[a1 - 2];
                    }
                }
            }
        };
    }
    
    public static final class MapJoiner
    {
        private final Joiner joiner;
        private final String keyValueSeparator;
        
        private MapJoiner(final Joiner a1, final String a2) {
            this.joiner = a1;
            this.keyValueSeparator = Preconditions.<String>checkNotNull(a2);
        }
        
        @CanIgnoreReturnValue
        public <A extends java.lang.Object> A appendTo(final A a1, final Map<?, ?> a2) throws IOException {
            /*SL:330*/return (A)this.appendTo((Appendable)a1, (Iterable)a2.entrySet());
        }
        
        @CanIgnoreReturnValue
        public StringBuilder appendTo(final StringBuilder a1, final Map<?, ?> a2) {
            /*SL:340*/return this.appendTo(a1, (Iterable<? extends Map.Entry<?, ?>>)a2.entrySet());
        }
        
        public String join(final Map<?, ?> a1) {
            /*SL:348*/return this.join(a1.entrySet());
        }
        
        @Beta
        @CanIgnoreReturnValue
        public <A extends java.lang.Object> A appendTo(final A a1, final Iterable<? extends Map.Entry<?, ?>> a2) throws IOException {
            /*SL:361*/return (A)this.appendTo((Appendable)a1, (Iterator)a2.iterator());
        }
        
        @Beta
        @CanIgnoreReturnValue
        public <A extends java.lang.Object> A appendTo(final A v2, final Iterator<? extends Map.Entry<?, ?>> v3) throws IOException {
            /*SL:374*/Preconditions.<A>checkNotNull(v2);
            /*SL:375*/if (v3.hasNext()) {
                Map.Entry<?, ?> a2 = /*EL:376*/(Map.Entry<?, ?>)v3.next();
                /*SL:377*/((Appendable)v2).append(this.joiner.toString(a2.getKey()));
                /*SL:378*/((Appendable)v2).append(this.keyValueSeparator);
                /*SL:379*/((Appendable)v2).append(this.joiner.toString(a2.getValue()));
                /*SL:380*/while (v3.hasNext()) {
                    /*SL:381*/((Appendable)v2).append(this.joiner.separator);
                    /*SL:382*/a2 = (Map.Entry<?, ?>)v3.next();
                    /*SL:383*/((Appendable)v2).append(this.joiner.toString(a2.getKey()));
                    /*SL:384*/((Appendable)v2).append(this.keyValueSeparator);
                    /*SL:385*/((Appendable)v2).append(this.joiner.toString(a2.getValue()));
                }
            }
            /*SL:388*/return v2;
        }
        
        @Beta
        @CanIgnoreReturnValue
        public StringBuilder appendTo(final StringBuilder a1, final Iterable<? extends Map.Entry<?, ?>> a2) {
            /*SL:401*/return this.appendTo(a1, a2.iterator());
        }
        
        @Beta
        @CanIgnoreReturnValue
        public StringBuilder appendTo(final StringBuilder v1, final Iterator<? extends Map.Entry<?, ?>> v2) {
            try {
                /*SL:415*/this.appendTo((Appendable)v1, (Iterator)v2);
            }
            catch (IOException a1) {
                /*SL:417*/throw new AssertionError((Object)a1);
            }
            /*SL:419*/return v1;
        }
        
        @Beta
        public String join(final Iterable<? extends Map.Entry<?, ?>> a1) {
            /*SL:430*/return this.join(a1.iterator());
        }
        
        @Beta
        public String join(final Iterator<? extends Map.Entry<?, ?>> a1) {
            /*SL:441*/return this.appendTo(new StringBuilder(), a1).toString();
        }
        
        public MapJoiner useForNull(final String a1) {
            /*SL:449*/return new MapJoiner(this.joiner.useForNull(a1), this.keyValueSeparator);
        }
    }
}
