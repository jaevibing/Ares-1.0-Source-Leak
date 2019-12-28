package com.ares.mixin;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer
{
    public byte[] transform(final String a1, final String a2, final byte[] a3) {
        /*SL:58*/return a3;
    }
}
