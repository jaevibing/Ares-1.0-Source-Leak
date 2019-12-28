package org.spongepowered.asm.launch.platform;

import java.util.HashMap;
import java.util.jar.Manifest;
import java.io.IOException;
import java.util.jar.JarFile;
import java.io.File;
import java.util.jar.Attributes;
import java.net.URI;
import java.util.Map;

final class MainAttributes
{
    private static final Map<URI, MainAttributes> instances;
    protected final Attributes attributes;
    
    private MainAttributes() {
        this.attributes = new Attributes();
    }
    
    private MainAttributes(final File a1) {
        this.attributes = getAttributes(a1);
    }
    
    public final String get(final String a1) {
        /*SL:58*/if (this.attributes != null) {
            /*SL:59*/return this.attributes.getValue(a1);
        }
        /*SL:61*/return null;
    }
    
    private static Attributes getAttributes(final File v1) {
        /*SL:65*/if (v1 == null) {
            /*SL:66*/return null;
        }
        JarFile v2 = /*EL:69*/null;
        try {
            /*SL:71*/v2 = new JarFile(v1);
            final Manifest a1 = /*EL:72*/v2.getManifest();
            /*SL:73*/if (a1 != null) {
                /*SL:74*/return a1.getMainAttributes();
            }
        }
        catch (IOException ex) {}
        finally {
            try {
                /*SL:80*/if (v2 != null) {
                    /*SL:81*/v2.close();
                }
            }
            catch (IOException ex2) {}
        }
        /*SL:87*/return new Attributes();
    }
    
    public static MainAttributes of(final File a1) {
        /*SL:91*/return of(a1.toURI());
    }
    
    public static MainAttributes of(final URI a1) {
        MainAttributes v1 = MainAttributes.instances.get(/*EL:95*/a1);
        /*SL:96*/if (v1 == null) {
            /*SL:97*/v1 = new MainAttributes(new File(a1));
            MainAttributes.instances.put(/*EL:98*/a1, v1);
        }
        /*SL:100*/return v1;
    }
    
    static {
        instances = new HashMap<URI, MainAttributes>();
    }
}
