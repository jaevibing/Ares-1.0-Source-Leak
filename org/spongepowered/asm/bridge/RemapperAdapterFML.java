package org.spongepowered.asm.bridge;

import java.lang.reflect.Field;
import org.spongepowered.asm.mixin.extensibility.IRemapper;
import org.objectweb.asm.commons.Remapper;
import java.lang.reflect.Method;

public final class RemapperAdapterFML extends RemapperAdapter
{
    private static final String DEOBFUSCATING_REMAPPER_CLASS = "fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
    private static final String DEOBFUSCATING_REMAPPER_CLASS_FORGE = "net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
    private static final String DEOBFUSCATING_REMAPPER_CLASS_LEGACY = "cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
    private static final String INSTANCE_FIELD = "INSTANCE";
    private static final String UNMAP_METHOD = "unmap";
    private final Method mdUnmap;
    
    private RemapperAdapterFML(final Remapper a1, final Method a2) {
        super(a1);
        this.logger.info("Initialised Mixin FML Remapper Adapter with {}", new Object[] { a1 });
        this.mdUnmap = a2;
    }
    
    @Override
    public String unmap(final String v2) {
        try {
            /*SL:54*/return this.mdUnmap.invoke(this.remapper, v2).toString();
        }
        catch (Exception a1) {
            /*SL:56*/return v2;
        }
    }
    
    public static IRemapper create() {
        try {
            final Class<?> v1 = getFMLDeobfuscatingRemapper();
            final Field v2 = /*EL:66*/v1.getDeclaredField("INSTANCE");
            final Method v3 = /*EL:67*/v1.getDeclaredMethod("unmap", String.class);
            final Remapper v4 = /*EL:68*/(Remapper)v2.get(null);
            /*SL:69*/return new RemapperAdapterFML(v4, v3);
        }
        catch (Exception v5) {
            /*SL:71*/v5.printStackTrace();
            /*SL:72*/return null;
        }
    }
    
    private static Class<?> getFMLDeobfuscatingRemapper() throws ClassNotFoundException {
        try {
            /*SL:82*/return Class.forName("net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper");
        }
        catch (ClassNotFoundException v1) {
            /*SL:84*/return Class.forName("cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper");
        }
    }
}
