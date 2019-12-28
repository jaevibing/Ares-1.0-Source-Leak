package org.spongepowered.asm.mixin.refmap;

import com.google.gson.Gson;
import java.io.InputStream;
import org.spongepowered.asm.service.IMixinService;
import java.io.Reader;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.IOUtils;
import com.google.gson.JsonParseException;
import java.io.InputStreamReader;
import org.spongepowered.asm.service.MixinService;
import org.apache.logging.log4j.LogManager;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Map;
import java.io.Serializable;

public final class ReferenceMapper implements IReferenceMapper, Serializable
{
    private static final long serialVersionUID = 2L;
    public static final String DEFAULT_RESOURCE = "mixin.refmap.json";
    public static final ReferenceMapper DEFAULT_MAPPER;
    private final Map<String, Map<String, String>> mappings;
    private final Map<String, Map<String, Map<String, String>>> data;
    private final transient boolean readOnly;
    private transient String context;
    private transient String resource;
    
    public ReferenceMapper() {
        this(false, "mixin.refmap.json");
    }
    
    private ReferenceMapper(final boolean a1, final String a2) {
        this.mappings = (Map<String, Map<String, String>>)Maps.<Object, Object>newHashMap();
        this.data = (Map<String, Map<String, Map<String, String>>>)Maps.<Object, Object>newHashMap();
        this.context = null;
        this.readOnly = a1;
        this.resource = a2;
    }
    
    @Override
    public boolean isDefault() {
        /*SL:120*/return this.readOnly;
    }
    
    private void setResourceName(final String a1) {
        /*SL:124*/if (!this.readOnly) {
            /*SL:125*/this.resource = ((a1 != null) ? a1 : "<unknown resource>");
        }
    }
    
    @Override
    public String getResourceName() {
        /*SL:135*/return this.resource;
    }
    
    @Override
    public String getStatus() {
        /*SL:143*/return this.isDefault() ? "No refMap loaded." : ("Using refmap " + this.getResourceName());
    }
    
    @Override
    public String getContext() {
        /*SL:151*/return this.context;
    }
    
    @Override
    public void setContext(final String a1) {
        /*SL:160*/this.context = a1;
    }
    
    @Override
    public String remap(final String a1, final String a2) {
        /*SL:169*/return this.remapWithContext(this.context, a1, a2);
    }
    
    @Override
    public String remapWithContext(final String a1, final String a2, final String a3) {
        Map<String, Map<String, String>> v1 = /*EL:179*/this.mappings;
        /*SL:180*/if (a1 != null) {
            /*SL:181*/v1 = this.data.get(a1);
            /*SL:182*/if (v1 == null) {
                /*SL:183*/v1 = this.mappings;
            }
        }
        /*SL:186*/return this.remap(v1, a2, a3);
    }
    
    private String remap(final Map<String, Map<String, String>> a3, final String v1, final String v2) {
        /*SL:193*/if (v1 == null) {
            /*SL:194*/for (final Map<String, String> a4 : a3.values()) {
                /*SL:195*/if (a4.containsKey(v2)) {
                    /*SL:196*/return a4.get(v2);
                }
            }
        }
        final Map<String, String> v3 = /*EL:201*/a3.get(v1);
        /*SL:202*/if (v3 == null) {
            /*SL:203*/return v2;
        }
        final String v4 = /*EL:205*/v3.get(v2);
        /*SL:206*/return (v4 != null) ? v4 : v2;
    }
    
    public String addMapping(final String a1, final String a2, final String a3, final String a4) {
        /*SL:219*/if (this.readOnly || a3 == null || a4 == null || a3.equals(a4)) {
            /*SL:220*/return null;
        }
        Map<String, Map<String, String>> v1 = /*EL:222*/this.mappings;
        /*SL:223*/if (a1 != null) {
            /*SL:224*/v1 = this.data.get(a1);
            /*SL:225*/if (v1 == null) {
                /*SL:226*/v1 = (Map<String, Map<String, String>>)Maps.<Object, Object>newHashMap();
                /*SL:227*/this.data.put(a1, v1);
            }
        }
        Map<String, String> v2 = /*EL:230*/v1.get(a2);
        /*SL:231*/if (v2 == null) {
            /*SL:232*/v2 = new HashMap<String, String>();
            /*SL:233*/v1.put(a2, v2);
        }
        /*SL:235*/return v2.put(a3, a4);
    }
    
    public void write(final Appendable a1) {
        /*SL:244*/new GsonBuilder().setPrettyPrinting().create().toJson((Object)this, a1);
    }
    
    public static ReferenceMapper read(final String v-2) {
        final Logger logger = /*EL:254*/LogManager.getLogger("mixin");
        Reader v0 = /*EL:255*/null;
        try {
            final IMixinService v = /*EL:257*/MixinService.getService();
            final InputStream v2 = /*EL:258*/v.getResourceAsStream(v-2);
            /*SL:259*/if (v2 != null) {
                /*SL:260*/v0 = new InputStreamReader(v2);
                final ReferenceMapper a1 = readJson(/*EL:261*/v0);
                /*SL:262*/a1.setResourceName(v-2);
                /*SL:263*/return a1;
            }
            /*SL:271*/return ReferenceMapper.DEFAULT_MAPPER;
        }
        catch (JsonParseException v3) {
            logger.error("Invalid REFMAP JSON in " + v-2 + ": " + v3.getClass().getName() + " " + v3.getMessage());
        }
        catch (Exception v4) {
            logger.error("Failed reading REFMAP JSON from " + v-2 + ": " + v4.getClass().getName() + " " + v4.getMessage());
        }
        finally {
            IOUtils.closeQuietly(v0);
        }
        /*SL:273*/return ReferenceMapper.DEFAULT_MAPPER;
    }
    
    public static ReferenceMapper read(final Reader v1, final String v2) {
        try {
            final ReferenceMapper a1 = readJson(/*EL:285*/v1);
            /*SL:286*/a1.setResourceName(v2);
            /*SL:287*/return a1;
        }
        catch (Exception a2) {
            /*SL:289*/return ReferenceMapper.DEFAULT_MAPPER;
        }
    }
    
    private static ReferenceMapper readJson(final Reader a1) {
        /*SL:294*/return (ReferenceMapper)new Gson().fromJson(a1, (Class)ReferenceMapper.class);
    }
    
    static {
        DEFAULT_MAPPER = new ReferenceMapper(true, "invalid");
    }
}
