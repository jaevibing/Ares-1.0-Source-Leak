package com.google.api.client.repackaged.com.google.common.base;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Present<T> extends Optional<T>
{
    private final T reference;
    private static final long serialVersionUID = 0L;
    
    Present(final T a1) {
        this.reference = a1;
    }
    
    @Override
    public boolean isPresent() {
        /*SL:37*/return true;
    }
    
    @Override
    public T get() {
        /*SL:42*/return this.reference;
    }
    
    @Override
    public T or(final T a1) {
        /*SL:47*/Preconditions.<T>checkNotNull(a1, (Object)"use Optional.orNull() instead of Optional.or(null)");
        /*SL:48*/return this.reference;
    }
    
    @Override
    public Optional<T> or(final Optional<? extends T> a1) {
        /*SL:53*/Preconditions.<Optional<? extends T>>checkNotNull(a1);
        /*SL:54*/return this;
    }
    
    @Override
    public T or(final Supplier<? extends T> a1) {
        /*SL:59*/Preconditions.<Supplier<? extends T>>checkNotNull(a1);
        /*SL:60*/return this.reference;
    }
    
    @Override
    public T orNull() {
        /*SL:65*/return this.reference;
    }
    
    @Override
    public Set<T> asSet() {
        /*SL:70*/return Collections.<T>singleton(this.reference);
    }
    
    @Override
    public <V> Optional<V> transform(final Function<? super T, V> a1) {
        /*SL:75*/return new Present<V>(Preconditions.<V>checkNotNull(a1.apply((Object)this.reference), (Object)"the Function passed to Optional.transform() must not return null."));
    }
    
    @Override
    public boolean equals(@Nullable final Object v2) {
        /*SL:83*/if (v2 instanceof Present) {
            final Present<?> a1 = /*EL:84*/(Present<?>)v2;
            /*SL:85*/return this.reference.equals(a1.reference);
        }
        /*SL:87*/return false;
    }
    
    @Override
    public int hashCode() {
        /*SL:92*/return 1502476572 + this.reference.hashCode();
    }
    
    @Override
    public String toString() {
        /*SL:97*/return "Optional.of(" + this.reference + ")";
    }
}
