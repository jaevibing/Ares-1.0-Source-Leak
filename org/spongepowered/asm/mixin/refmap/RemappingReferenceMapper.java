package org.spongepowered.asm.mixin.refmap;

import org.apache.logging.log4j.LogManager;
import com.google.common.io.Files;
import java.io.IOException;
import com.google.common.base.Strings;
import com.google.common.io.LineProcessor;
import com.google.common.base.Charsets;
import java.io.File;
import java.util.Iterator;
import java.util.HashMap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public final class RemappingReferenceMapper implements IReferenceMapper
{
    private static final String DEFAULT_RESOURCE_PATH_PROPERTY = "net.minecraftforge.gradle.GradleStart.srg.srg-mcp";
    private static final String DEFAULT_MAPPING_ENV = "searge";
    private static final Logger logger;
    private static final Map<String, Map<String, String>> srgs;
    private final IReferenceMapper refMap;
    private final Map<String, String> mappings;
    private final Map<String, Map<String, String>> cache;
    
    private RemappingReferenceMapper(final MixinEnvironment a1, final IReferenceMapper a2) {
        this.cache = new HashMap<String, Map<String, String>>();
        (this.refMap = a2).setContext(getMappingEnv(a1));
        final String v1 = getResource(a1);
        this.mappings = loadSrgs(v1);
        RemappingReferenceMapper.logger.info("Remapping refMap {} using {}", new Object[] { a2.getResourceName(), v1 });
    }
    
    @Override
    public boolean isDefault() {
        /*SL:126*/return this.refMap.isDefault();
    }
    
    @Override
    public String getResourceName() {
        /*SL:135*/return this.refMap.getResourceName();
    }
    
    @Override
    public String getStatus() {
        /*SL:143*/return this.refMap.getStatus();
    }
    
    @Override
    public String getContext() {
        /*SL:151*/return this.refMap.getContext();
    }
    
    @Override
    public void setContext(final String a1) {
    }
    
    @Override
    public String remap(final String v1, final String v2) {
        final Map<String, String> v3 = /*EL:169*/this.getCache(v1);
        String v4 = /*EL:170*/v3.get(v2);
        /*SL:171*/if (v4 == null) {
            /*SL:172*/v4 = this.refMap.remap(v1, v2);
            /*SL:173*/for (final Map.Entry<String, String> a1 : this.mappings.entrySet()) {
                /*SL:174*/v4 = v4.replace(a1.getKey(), a1.getValue());
            }
            /*SL:176*/v3.put(v2, v4);
        }
        /*SL:178*/return v4;
    }
    
    private Map<String, String> getCache(final String a1) {
        Map<String, String> v1 = /*EL:182*/this.cache.get(a1);
        /*SL:183*/if (v1 == null) {
            /*SL:184*/v1 = new HashMap<String, String>();
            /*SL:185*/this.cache.put(a1, v1);
        }
        /*SL:187*/return v1;
    }
    
    @Override
    public String remapWithContext(final String a1, final String a2, final String a3) {
        /*SL:197*/return this.refMap.remapWithContext(a1, a2, a3);
    }
    
    private static Map<String, String> loadSrgs(final String v1) {
        /*SL:209*/if (RemappingReferenceMapper.srgs.containsKey(v1)) {
            /*SL:210*/return RemappingReferenceMapper.srgs.get(v1);
        }
        final Map<String, String> v2 = /*EL:213*/new HashMap<String, String>();
        RemappingReferenceMapper.srgs.put(/*EL:214*/v1, v2);
        final File v3 = /*EL:216*/new File(v1);
        /*SL:217*/if (!v3.isFile()) {
            /*SL:218*/return v2;
        }
        try {
            /*SL:222*/Files.<Object>readLines(v3, Charsets.UTF_8, (LineProcessor<Object>)new LineProcessor<Object>() {
                @Override
                public Object getResult() {
                    /*SL:226*/return null;
                }
                
                @Override
                public boolean processLine(final String v2) throws IOException {
                    /*SL:231*/if (Strings.isNullOrEmpty(v2) || v2.startsWith("#")) {
                        /*SL:232*/return true;
                    }
                    final int v3 = /*EL:234*/0;
                    int v4 = 0;
                    int n2;
                    final int n = /*EL:235*/v2.startsWith("MD: ") ? (n2 = 2) : (v2.startsWith("FD: ") ? (n2 = 1) : (n2 = 0));
                    v4 = n2;
                    if (n > 0) {
                        final String[] a1 = /*EL:236*/v2.substring(4).split(" ", 4);
                        /*SL:237*/v2.put(a1[v3].substring(a1[v3].lastIndexOf(47) + /*EL:238*/1), a1[v4].substring(a1[v4].lastIndexOf(47) + /*EL:239*/1));
                    }
                    /*SL:242*/return true;
                }
            });
        }
        catch (IOException a1) {
            RemappingReferenceMapper.logger.warn(/*EL:246*/"Could not read input SRG file: {}", new Object[] { v1 });
            RemappingReferenceMapper.logger.catching(/*EL:247*/(Throwable)a1);
        }
        /*SL:250*/return v2;
    }
    
    public static IReferenceMapper of(final MixinEnvironment a1, final IReferenceMapper a2) {
        /*SL:262*/if (!a2.isDefault() && hasData(a1)) {
            /*SL:263*/return new RemappingReferenceMapper(a1, a2);
        }
        /*SL:265*/return a2;
    }
    
    private static boolean hasData(final MixinEnvironment a1) {
        final String v1 = getResource(/*EL:269*/a1);
        /*SL:270*/return v1 != null && new File(v1).exists();
    }
    
    private static String getResource(final MixinEnvironment a1) {
        final String v1 = /*EL:274*/a1.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_RESOURCE);
        /*SL:275*/return Strings.isNullOrEmpty(v1) ? System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp") : v1;
    }
    
    private static String getMappingEnv(final MixinEnvironment a1) {
        final String v1 = /*EL:279*/a1.getOptionValue(MixinEnvironment.Option.REFMAP_REMAP_SOURCE_ENV);
        /*SL:280*/return Strings.isNullOrEmpty(v1) ? "searge" : v1;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
        srgs = new HashMap<String, Map<String, String>>();
    }
}
