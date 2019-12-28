package com.google.api.client.repackaged.com.google.common.base;

import java.io.Serializable;
import java.util.Iterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class Converter<A, B> implements Function<A, B>
{
    private final boolean handleNullAutomatically;
    @LazyInit
    private transient Converter<B, A> reverse;
    
    protected Converter() {
        this(true);
    }
    
    Converter(final boolean a1) {
        this.handleNullAutomatically = a1;
    }
    
    protected abstract B doForward(final A p0);
    
    protected abstract A doBackward(final B p0);
    
    @Nullable
    @CanIgnoreReturnValue
    public final B convert(@Nullable final A a1) {
        /*SL:168*/return this.correctedDoForward(a1);
    }
    
    @Nullable
    B correctedDoForward(@Nullable final A a1) {
        /*SL:173*/if (this.handleNullAutomatically) {
            /*SL:175*/return (a1 == null) ? null : Preconditions.<B>checkNotNull(this.doForward(a1));
        }
        /*SL:177*/return this.doForward(a1);
    }
    
    @Nullable
    A correctedDoBackward(@Nullable final B a1) {
        /*SL:183*/if (this.handleNullAutomatically) {
            /*SL:185*/return (a1 == null) ? null : Preconditions.<A>checkNotNull(this.doBackward(a1));
        }
        /*SL:187*/return this.doBackward(a1);
    }
    
    @CanIgnoreReturnValue
    public Iterable<B> convertAll(final Iterable<? extends A> a1) {
        /*SL:201*/Preconditions.<Iterable<? extends A>>checkNotNull(a1, (Object)"fromIterable");
        /*SL:202*/return new Iterable<B>() {
            @Override
            public Iterator<B> iterator() {
                /*SL:205*/return new Iterator<B>() {
                    private final Iterator<? extends A> fromIterator = a1.iterator();
                    
                    @Override
                    public boolean hasNext() {
                        /*SL:210*/return this.fromIterator.hasNext();
                    }
                    
                    @Override
                    public B next() {
                        /*SL:215*/return Converter.this.convert(this.fromIterator.next());
                    }
                    
                    @Override
                    public void remove() {
                        /*SL:220*/this.fromIterator.remove();
                    }
                };
            }
        };
    }
    
    @CanIgnoreReturnValue
    public Converter<B, A> reverse() {
        final Converter<B, A> v1 = /*EL:236*/this.reverse;
        /*SL:237*/return (v1 == null) ? (this.reverse = (Converter<B, A>)new ReverseConverter((Converter<Object, Object>)this)) : v1;
    }
    
    public final <C> Converter<A, C> andThen(final Converter<B, C> a1) {
        /*SL:312*/return (Converter<A, C>)this.<Object>doAndThen((Converter<B, Object>)a1);
    }
    
     <C> Converter<A, C> doAndThen(final Converter<B, C> a1) {
        /*SL:319*/return (Converter<A, C>)new ConverterComposition((Converter<Object, Object>)this, (Converter<Object, Object>)Preconditions.<Converter<B, C>>checkNotNull(a1));
    }
    
    @Deprecated
    @Nullable
    @CanIgnoreReturnValue
    @Override
    public final B apply(@Nullable final A a1) {
        /*SL:391*/return this.convert(a1);
    }
    
    @Override
    public boolean equals(@Nullable final Object a1) {
        /*SL:407*/return super.equals(a1);
    }
    
    public static <A, B> Converter<A, B> from(final Function<? super A, ? extends B> a1, final Function<? super B, ? extends A> a2) {
        /*SL:429*/return new FunctionBasedConverter<A, B>((Function)a1, (Function)a2);
    }
    
    public static <T> Converter<T, T> identity() {
        /*SL:480*/return (Converter<T, T>)IdentityConverter.INSTANCE;
    }
    
    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable
    {
        final Converter<A, B> original;
        private static final long serialVersionUID = 0L;
        
        ReverseConverter(final Converter<A, B> a1) {
            this.original = a1;
        }
        
        @Override
        protected A doForward(final B a1) {
            /*SL:257*/throw new AssertionError();
        }
        
        @Override
        protected B doBackward(final A a1) {
            /*SL:262*/throw new AssertionError();
        }
        
        @Nullable
        @Override
        A correctedDoForward(@Nullable final B a1) {
            /*SL:268*/return this.original.correctedDoBackward(a1);
        }
        
        @Nullable
        @Override
        B correctedDoBackward(@Nullable final A a1) {
            /*SL:274*/return this.original.correctedDoForward(a1);
        }
        
        @Override
        public Converter<A, B> reverse() {
            /*SL:279*/return this.original;
        }
        
        @Override
        public boolean equals(@Nullable final Object v2) {
            /*SL:284*/if (v2 instanceof ReverseConverter) {
                final ReverseConverter<?, ?> a1 = /*EL:285*/(ReverseConverter<?, ?>)v2;
                /*SL:286*/return this.original.equals(a1.original);
            }
            /*SL:288*/return false;
        }
        
        @Override
        public int hashCode() {
            /*SL:293*/return ~this.original.hashCode();
        }
        
        @Override
        public String toString() {
            /*SL:298*/return this.original + ".reverse()";
        }
    }
    
    private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable
    {
        final Converter<A, B> first;
        final Converter<B, C> second;
        private static final long serialVersionUID = 0L;
        
        ConverterComposition(final Converter<A, B> a1, final Converter<B, C> a2) {
            this.first = a1;
            this.second = a2;
        }
        
        @Override
        protected C doForward(final A a1) {
            /*SL:341*/throw new AssertionError();
        }
        
        @Override
        protected A doBackward(final C a1) {
            /*SL:346*/throw new AssertionError();
        }
        
        @Nullable
        @Override
        C correctedDoForward(@Nullable final A a1) {
            /*SL:352*/return this.second.correctedDoForward(this.first.correctedDoForward(a1));
        }
        
        @Nullable
        @Override
        A correctedDoBackward(@Nullable final C a1) {
            /*SL:358*/return this.first.correctedDoBackward(this.second.correctedDoBackward(a1));
        }
        
        @Override
        public boolean equals(@Nullable final Object v2) {
            /*SL:363*/if (v2 instanceof ConverterComposition) {
                final ConverterComposition<?, ?, ?> a1 = /*EL:364*/(ConverterComposition<?, ?, ?>)v2;
                /*SL:365*/return this.first.equals(a1.first) && this.second.equals(a1.second);
            }
            /*SL:367*/return false;
        }
        
        @Override
        public int hashCode() {
            /*SL:372*/return 31 * this.first.hashCode() + this.second.hashCode();
        }
        
        @Override
        public String toString() {
            /*SL:377*/return this.first + ".andThen(" + this.second + ")";
        }
    }
    
    private static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable
    {
        private final Function<? super A, ? extends B> forwardFunction;
        private final Function<? super B, ? extends A> backwardFunction;
        
        private FunctionBasedConverter(final Function<? super A, ? extends B> a1, final Function<? super B, ? extends A> a2) {
            this.forwardFunction = Preconditions.<Function<? super A, ? extends B>>checkNotNull(a1);
            this.backwardFunction = Preconditions.<Function<? super B, ? extends A>>checkNotNull(a2);
        }
        
        @Override
        protected B doForward(final A a1) {
            /*SL:446*/return (B)this.forwardFunction.apply((Object)a1);
        }
        
        @Override
        protected A doBackward(final B a1) {
            /*SL:451*/return (A)this.backwardFunction.apply((Object)a1);
        }
        
        @Override
        public boolean equals(@Nullable final Object v2) {
            /*SL:456*/if (v2 instanceof FunctionBasedConverter) {
                final FunctionBasedConverter<?, ?> a1 = /*EL:457*/(FunctionBasedConverter<?, ?>)v2;
                /*SL:458*/return this.forwardFunction.equals(a1.forwardFunction) && this.backwardFunction.equals(a1.backwardFunction);
            }
            /*SL:461*/return false;
        }
        
        @Override
        public int hashCode() {
            /*SL:466*/return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
        }
        
        @Override
        public String toString() {
            /*SL:471*/return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
        }
    }
    
    private static final class IdentityConverter<T> extends Converter<T, T> implements Serializable
    {
        static final IdentityConverter INSTANCE;
        private static final long serialVersionUID = 0L;
        
        @Override
        protected T doForward(final T a1) {
            /*SL:492*/return a1;
        }
        
        @Override
        protected T doBackward(final T a1) {
            /*SL:497*/return a1;
        }
        
        @Override
        public IdentityConverter<T> reverse() {
            /*SL:502*/return this;
        }
        
        @Override
         <S> Converter<T, S> doAndThen(final Converter<T, S> a1) {
            /*SL:507*/return Preconditions.<Converter<T, S>>checkNotNull(a1, (Object)"otherConverter");
        }
        
        @Override
        public String toString() {
            /*SL:517*/return "Converter.identity()";
        }
        
        private Object readResolve() {
            /*SL:521*/return IdentityConverter.INSTANCE;
        }
        
        static {
            INSTANCE = new IdentityConverter();
        }
    }
}
