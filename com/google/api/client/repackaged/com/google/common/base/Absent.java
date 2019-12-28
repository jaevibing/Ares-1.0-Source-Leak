package com.google.api.client.repackaged.com.google.common.base;

import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Absent<T> extends Optional<T>
{
    static final Absent<Object> INSTANCE;
    private static final long serialVersionUID = 0L;
    
    static <T> Optional<T> withType() {
        /*SL:33*/return (Optional<T>)Absent.INSTANCE;
    }
    
    @Override
    public boolean isPresent() {
        /*SL:40*/return false;
    }
    
    @Override
    public T get() {
        /*SL:45*/throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }
    
    @Override
    public T or(final T a1) {
        /*SL:50*/return Preconditions.<T>checkNotNull(a1, (Object)"use Optional.orNull() instead of Optional.or(null)");
    }
    
    @Override
    public Optional<T> or(final Optional<? extends T> a1) {
        /*SL:56*/return Preconditions.<Optional<T>>checkNotNull((Optional<T>)a1);
    }
    
    @Override
    public T or(final Supplier<? extends T> a1) {
        /*SL:61*/return Preconditions.<T>checkNotNull((T)a1.get(), (Object)"use Optional.orNull() instead of a Supplier that returns null");
    }
    
    @Nullable
    @Override
    public T orNull() {
        /*SL:68*/return null;
    }
    
    @Override
    public Set<T> asSet() {
        /*SL:73*/return Collections.<T>emptySet();
    }
    
    @Override
    public <V> Optional<V> transform(final Function<? super T, V> a1) {
        /*SL:78*/Preconditions.<Function<? super T, V>>checkNotNull(a1);
        /*SL:79*/return Optional.<V>absent();
    }
    
    @Override
    public boolean equals(@Nullable final Object a1) {
        /*SL:84*/return a1 == this;
    }
    
    @Override
    public int hashCode() {
        /*SL:89*/return 2040732332;
    }
    
    @Override
    public String toString() {
        /*SL:94*/return "Optional.absent()";
    }
    
    private Object readResolve() {
        /*SL:98*/return Absent.INSTANCE;
    }
    
    static {
        INSTANCE = new Absent<Object>();
    }
}
