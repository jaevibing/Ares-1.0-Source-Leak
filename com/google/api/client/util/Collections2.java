package com.google.api.client.util;

import java.util.Collection;

public final class Collections2
{
    static <T> Collection<T> cast(final Iterable<T> a1) {
        /*SL:34*/return (Collection<T>)(Collection)a1;
    }
}
