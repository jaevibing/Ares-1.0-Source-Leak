package com.google.api.client.repackaged.com.google.common.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Predicate<T>
{
    @CanIgnoreReturnValue
    boolean apply(@Nullable T p0);
    
    boolean equals(@Nullable Object p0);
}
