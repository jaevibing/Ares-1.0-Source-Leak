package org.spongepowered.tools.obfuscation;

import java.util.LinkedHashMap;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.lang.reflect.Constructor;
import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.service.ObfuscationTypeDescriptor;
import java.util.Map;

public final class ObfuscationType
{
    private static final Map<String, ObfuscationType> types;
    private final String key;
    private final ObfuscationTypeDescriptor descriptor;
    private final IMixinAnnotationProcessor ap;
    private final IOptionProvider options;
    
    private ObfuscationType(final ObfuscationTypeDescriptor a1, final IMixinAnnotationProcessor a2) {
        this.key = a1.getKey();
        this.descriptor = a1;
        this.ap = a2;
        this.options = a2;
    }
    
    public final ObfuscationEnvironment createEnvironment() {
        try {
            final Class<? extends ObfuscationEnvironment> v1 = /*EL:82*/this.descriptor.getEnvironmentType();
            final Constructor<? extends ObfuscationEnvironment> v2 = /*EL:83*/v1.getDeclaredConstructor(ObfuscationType.class);
            /*SL:84*/v2.setAccessible(true);
            /*SL:85*/return (ObfuscationEnvironment)v2.newInstance(this);
        }
        catch (Exception v3) {
            /*SL:87*/throw new RuntimeException(v3);
        }
    }
    
    @Override
    public String toString() {
        /*SL:93*/return this.key;
    }
    
    public String getKey() {
        /*SL:98*/return this.key;
    }
    
    public ObfuscationTypeDescriptor getConfig() {
        /*SL:102*/return this.descriptor;
    }
    
    public IMixinAnnotationProcessor getAnnotationProcessor() {
        /*SL:106*/return this.ap;
    }
    
    public boolean isDefault() {
        final String v1 = /*EL:113*/this.options.getOption("defaultObfuscationEnv");
        /*SL:114*/return (v1 == null && this.key.equals("searge")) || (v1 != null && this.key.equals(v1.toLowerCase()));
    }
    
    public boolean isSupported() {
        /*SL:122*/return this.getInputFileNames().size() > 0;
    }
    
    public List<String> getInputFileNames() {
        final ImmutableList.Builder<String> builder = /*EL:129*/ImmutableList.<String>builder();
        final String option = /*EL:131*/this.options.getOption(this.descriptor.getInputFileOption());
        /*SL:132*/if (option != null) {
            /*SL:133*/builder.add(option);
        }
        final String option2 = /*EL:136*/this.options.getOption(this.descriptor.getExtraInputFilesOption());
        /*SL:137*/if (option2 != null) {
            /*SL:138*/for (final String v1 : option2.split(";")) {
                /*SL:139*/builder.add(v1.trim());
            }
        }
        /*SL:143*/return builder.build();
    }
    
    public String getOutputFileName() {
        /*SL:150*/return this.options.getOption(this.descriptor.getOutputFileOption());
    }
    
    public static Iterable<ObfuscationType> types() {
        /*SL:157*/return ObfuscationType.types.values();
    }
    
    public static ObfuscationType create(final ObfuscationTypeDescriptor a1, final IMixinAnnotationProcessor a2) {
        final String v1 = /*EL:168*/a1.getKey();
        /*SL:169*/if (ObfuscationType.types.containsKey(v1)) {
            /*SL:170*/throw new IllegalArgumentException("Obfuscation type with key " + v1 + " was already registered");
        }
        final ObfuscationType v2 = /*EL:172*/new ObfuscationType(a1, a2);
        ObfuscationType.types.put(/*EL:173*/v1, v2);
        /*SL:174*/return v2;
    }
    
    public static ObfuscationType get(final String a1) {
        final ObfuscationType v1 = ObfuscationType.types.get(/*EL:185*/a1);
        /*SL:186*/if (v1 == null) {
            /*SL:187*/throw new IllegalArgumentException("Obfuscation type with key " + a1 + " was not registered");
        }
        /*SL:189*/return v1;
    }
    
    static {
        types = new LinkedHashMap<String, ObfuscationType>();
    }
}
