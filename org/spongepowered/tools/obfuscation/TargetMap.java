package org.spongepowered.tools.obfuscation;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.common.io.Files;
import java.nio.charset.Charset;
import java.io.File;
import java.util.HashSet;
import java.util.Collections;
import java.util.Collection;
import javax.lang.model.element.TypeElement;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.List;
import java.util.Set;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;
import java.util.HashMap;

public final class TargetMap extends HashMap<TypeReference, Set<TypeReference>>
{
    private static final long serialVersionUID = 1L;
    private final String sessionId;
    
    private TargetMap() {
        this(String.valueOf(System.currentTimeMillis()));
    }
    
    private TargetMap(final String a1) {
        this.sessionId = a1;
    }
    
    public String getSessionId() {
        /*SL:81*/return this.sessionId;
    }
    
    public void registerTargets(final AnnotatedMixin a1) {
        /*SL:90*/this.registerTargets(a1.getTargets(), a1.getHandle());
    }
    
    public void registerTargets(final List<TypeHandle> v1, final TypeHandle v2) {
        /*SL:100*/for (final TypeHandle a1 : v1) {
            /*SL:101*/this.addMixin(a1, v2);
        }
    }
    
    public void addMixin(final TypeHandle a1, final TypeHandle a2) {
        /*SL:112*/this.addMixin(a1.getReference(), a2.getReference());
    }
    
    public void addMixin(final String a1, final String a2) {
        /*SL:122*/this.addMixin(new TypeReference(a1), new TypeReference(a2));
    }
    
    public void addMixin(final TypeReference a1, final TypeReference a2) {
        final Set<TypeReference> v1 = /*EL:132*/this.getMixinsFor(a1);
        /*SL:133*/v1.add(a2);
    }
    
    public Collection<TypeReference> getMixinsTargeting(final TypeElement a1) {
        /*SL:143*/return this.getMixinsTargeting(new TypeHandle(a1));
    }
    
    public Collection<TypeReference> getMixinsTargeting(final TypeHandle a1) {
        /*SL:153*/return this.getMixinsTargeting(a1.getReference());
    }
    
    public Collection<TypeReference> getMixinsTargeting(final TypeReference a1) {
        /*SL:163*/return Collections.<TypeReference>unmodifiableCollection((Collection<? extends TypeReference>)this.getMixinsFor(a1));
    }
    
    private Set<TypeReference> getMixinsFor(final TypeReference a1) {
        Set<TypeReference> v1 = /*EL:173*/((HashMap<K, Set<TypeReference>>)this).get(a1);
        /*SL:174*/if (v1 == null) {
            /*SL:175*/v1 = new HashSet<TypeReference>();
            /*SL:176*/this.put(a1, v1);
        }
        /*SL:178*/return v1;
    }
    
    public void readImports(final File v-1) throws IOException {
        /*SL:188*/if (!v-1.isFile()) {
            /*SL:189*/return;
        }
        /*SL:192*/for (final String v1 : Files.readLines(v-1, Charset.defaultCharset())) {
            final String[] a1 = /*EL:193*/v1.split("\t");
            /*SL:194*/if (a1.length == 2) {
                /*SL:195*/this.addMixin(a1[1], a1[0]);
            }
        }
    }
    
    public void write(final boolean v-2) {
        ObjectOutputStream objectOutputStream = /*EL:206*/null;
        FileOutputStream v0 = /*EL:207*/null;
        try {
            final File a1 = getSessionFile(/*EL:209*/this.sessionId);
            /*SL:210*/if (v-2) {
                /*SL:211*/a1.deleteOnExit();
            }
            /*SL:213*/v0 = new FileOutputStream(a1, true);
            /*SL:214*/objectOutputStream = new ObjectOutputStream(v0);
            /*SL:215*/objectOutputStream.writeObject(this);
        }
        catch (Exception v) {
            /*SL:217*/v.printStackTrace();
            /*SL:219*/if (objectOutputStream != null) {
                try {
                    /*SL:221*/objectOutputStream.close();
                }
                catch (IOException v2) {
                    /*SL:223*/v2.printStackTrace();
                }
            }
        }
        finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                }
                catch (IOException v3) {
                    v3.printStackTrace();
                }
            }
        }
    }
    
    private static TargetMap read(final File v-2) {
        ObjectInputStream objectInputStream = /*EL:236*/null;
        FileInputStream v0 = /*EL:237*/null;
        try {
            /*SL:239*/v0 = new FileInputStream(v-2);
            /*SL:240*/objectInputStream = new ObjectInputStream(v0);
            /*SL:241*/return (TargetMap)objectInputStream.readObject();
        }
        catch (Exception v) {
            /*SL:243*/v.printStackTrace();
            /*SL:245*/if (objectInputStream != null) {
                try {
                    /*SL:247*/objectInputStream.close();
                }
                catch (IOException v2) {
                    /*SL:249*/v2.printStackTrace();
                }
            }
        }
        finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                }
                catch (IOException v3) {
                    v3.printStackTrace();
                }
            }
        }
        /*SL:253*/return null;
    }
    
    public static TargetMap create(final String v0) {
        /*SL:266*/if (v0 != null) {
            final File v = getSessionFile(/*EL:267*/v0);
            /*SL:268*/if (v.exists()) {
                final TargetMap a1 = read(/*EL:269*/v);
                /*SL:270*/if (a1 != null) {
                    /*SL:271*/return a1;
                }
            }
        }
        /*SL:276*/return new TargetMap();
    }
    
    private static File getSessionFile(final String a1) {
        final File v1 = /*EL:280*/new File(System.getProperty("java.io.tmpdir"));
        /*SL:281*/return new File(v1, String.format("mixin-targetdb-%s.tmp", a1));
    }
}
