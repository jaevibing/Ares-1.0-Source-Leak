package com.ares.hack.hacks;

import com.ares.extensions.Wrapper;
import java.util.Comparator;

class StrLengthComparator implements Comparator<BaseHack>
{
    @Override
    public int compare(final BaseHack a1, final BaseHack a2) {
        final int v1 = Wrapper.fontRenderer.func_78256_a(/*EL:90*/a1.name);
        final int v2 = Wrapper.fontRenderer.func_78256_a(/*EL:91*/a2.name);
        /*SL:92*/return Integer.compare(v2, v1);
    }
}
