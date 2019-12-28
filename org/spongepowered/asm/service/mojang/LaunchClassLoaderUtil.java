package org.spongepowered.asm.service.mojang;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import net.minecraft.launchwrapper.LaunchClassLoader;

final class LaunchClassLoaderUtil
{
    private static final String CACHED_CLASSES_FIELD = "cachedClasses";
    private static final String INVALID_CLASSES_FIELD = "invalidClasses";
    private static final String CLASS_LOADER_EXCEPTIONS_FIELD = "classLoaderExceptions";
    private static final String TRANSFORMER_EXCEPTIONS_FIELD = "transformerExceptions";
    private final LaunchClassLoader classLoader;
    private final Map<String, Class<?>> cachedClasses;
    private final Set<String> invalidClasses;
    private final Set<String> classLoaderExceptions;
    private final Set<String> transformerExceptions;
    
    LaunchClassLoaderUtil(final LaunchClassLoader a1) {
        this.classLoader = a1;
        this.cachedClasses = LaunchClassLoaderUtil.<Map<String, Class<?>>>getField(a1, "cachedClasses");
        this.invalidClasses = LaunchClassLoaderUtil.<Set<String>>getField(a1, "invalidClasses");
        this.classLoaderExceptions = LaunchClassLoaderUtil.<Set<String>>getField(a1, "classLoaderExceptions");
        this.transformerExceptions = LaunchClassLoaderUtil.<Set<String>>getField(a1, "transformerExceptions");
    }
    
    LaunchClassLoader getClassLoader() {
        /*SL:75*/return this.classLoader;
    }
    
    boolean isClassLoaded(final String a1) {
        /*SL:86*/return this.cachedClasses.containsKey(a1);
    }
    
    boolean isClassExcluded(final String a1, final String a2) {
        /*SL:98*/return this.isClassClassLoaderExcluded(a1, a2) || this.isClassTransformerExcluded(a1, a2);
    }
    
    boolean isClassClassLoaderExcluded(final String v1, final String v2) {
        /*SL:111*/for (final String a1 : this.getClassLoaderExceptions()) {
            /*SL:112*/if ((v2 != null && v2.startsWith(a1)) || v1.startsWith(a1)) {
                /*SL:113*/return true;
            }
        }
        /*SL:117*/return false;
    }
    
    boolean isClassTransformerExcluded(final String v1, final String v2) {
        /*SL:130*/for (final String a1 : this.getTransformerExceptions()) {
            /*SL:131*/if ((v2 != null && v2.startsWith(a1)) || v1.startsWith(a1)) {
                /*SL:132*/return true;
            }
        }
        /*SL:136*/return false;
    }
    
    void registerInvalidClass(final String a1) {
        /*SL:147*/if (this.invalidClasses != null) {
            /*SL:148*/this.invalidClasses.add(a1);
        }
    }
    
    Set<String> getClassLoaderExceptions() {
        /*SL:156*/if (this.classLoaderExceptions != null) {
            /*SL:157*/return this.classLoaderExceptions;
        }
        /*SL:159*/return Collections.<String>emptySet();
    }
    
    Set<String> getTransformerExceptions() {
        /*SL:166*/if (this.transformerExceptions != null) {
            /*SL:167*/return this.transformerExceptions;
        }
        /*SL:169*/return Collections.<String>emptySet();
    }
    
    private static <T> T getField(final LaunchClassLoader v1, final String v2) {
        try {
            final Field a1 = /*EL:175*/LaunchClassLoader.class.getDeclaredField(v2);
            /*SL:176*/a1.setAccessible(true);
            /*SL:177*/return (T)a1.get(v1);
        }
        catch (Exception a2) {
            /*SL:179*/a2.printStackTrace();
            /*SL:181*/return null;
        }
    }
}
