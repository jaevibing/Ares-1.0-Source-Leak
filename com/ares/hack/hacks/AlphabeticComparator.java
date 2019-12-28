package com.ares.hack.hacks;

import java.util.Comparator;

class AlphabeticComparator implements Comparator<BaseHack>
{
    @Override
    public int compare(final BaseHack a1, final BaseHack a2) {
        /*SL:100*/return a1.name.compareTo(a2.name);
    }
}
