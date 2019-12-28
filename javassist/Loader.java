package javassist;

import java.io.InputStream;
import java.net.URL;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.util.Vector;
import java.util.Hashtable;

public class Loader extends ClassLoader
{
    private Hashtable notDefinedHere;
    private Vector notDefinedPackages;
    private ClassPool source;
    private Translator translator;
    private ProtectionDomain domain;
    public boolean doDelegation;
    
    public Loader() {
        this((ClassPool)null);
    }
    
    public Loader(final ClassPool a1) {
        this.doDelegation = true;
        this.init(a1);
    }
    
    public Loader(final ClassLoader a1, final ClassPool a2) {
        super(a1);
        this.doDelegation = true;
        this.init(a2);
    }
    
    private void init(final ClassPool a1) {
        /*SL:185*/this.notDefinedHere = new Hashtable();
        /*SL:186*/this.notDefinedPackages = new Vector();
        /*SL:187*/this.source = a1;
        /*SL:188*/this.translator = null;
        /*SL:189*/this.domain = null;
        /*SL:190*/this.delegateLoadingOf("javassist.Loader");
    }
    
    public void delegateLoadingOf(final String a1) {
        /*SL:202*/if (a1.endsWith(".")) {
            /*SL:203*/this.notDefinedPackages.addElement(a1);
        }
        else {
            /*SL:205*/this.notDefinedHere.put(a1, this);
        }
    }
    
    public void setDomain(final ProtectionDomain a1) {
        /*SL:215*/this.domain = a1;
    }
    
    public void setClassPool(final ClassPool a1) {
        /*SL:222*/this.source = a1;
    }
    
    public void addTranslator(final ClassPool a1, final Translator a2) throws NotFoundException, CannotCompileException {
        /*SL:236*/this.source = a1;
        /*SL:237*/(this.translator = a2).start(/*EL:238*/a1);
    }
    
    public static void main(final String[] a1) throws Throwable {
        final Loader v1 = /*EL:255*/new Loader();
        /*SL:256*/v1.run(a1);
    }
    
    public void run(final String[] v-1) throws Throwable {
        final int v0 = /*EL:269*/v-1.length - 1;
        /*SL:270*/if (v0 >= 0) {
            final String[] v = /*EL:271*/new String[v0];
            /*SL:272*/for (int a1 = 0; a1 < v0; ++a1) {
                /*SL:273*/v[a1] = v-1[a1 + 1];
            }
            /*SL:275*/this.run(v-1[0], v);
        }
    }
    
    public void run(final String v1, final String[] v2) throws Throwable {
        final Class v3 = /*EL:286*/this.loadClass(v1);
        try {
            /*SL:288*/v3.getDeclaredMethod("main", String[].class).invoke(null, v2);
        }
        catch (InvocationTargetException a1) {
            /*SL:293*/throw a1.getTargetException();
        }
    }
    
    @Override
    protected Class loadClass(String v1, final boolean v2) throws ClassFormatError, ClassNotFoundException {
        /*SL:302*/v1 = v1.intern();
        /*SL:303*/synchronized (v1) {
            Class a1 = /*EL:304*/this.findLoadedClass(v1);
            /*SL:305*/if (a1 == null) {
                /*SL:306*/a1 = this.loadClassByDelegation(v1);
            }
            /*SL:308*/if (a1 == null) {
                /*SL:309*/a1 = this.findClass(v1);
            }
            /*SL:311*/if (a1 == null) {
                /*SL:312*/a1 = this.delegateToParent(v1);
            }
            /*SL:314*/if (v2) {
                /*SL:315*/this.resolveClass(a1);
            }
            /*SL:317*/return a1;
        }
    }
    
    @Override
    protected Class findClass(final String v-1) throws ClassNotFoundException {
        byte[] v3 = null;
        try {
            Label_0101: {
                /*SL:336*/if (this.source != null) {
                    /*SL:337*/if (this.translator != null) {
                        /*SL:338*/this.translator.onLoad(this.source, v-1);
                    }
                    try {
                        final byte[] a1 = /*EL:341*/this.source.get(v-1).toBytecode();
                        break Label_0101;
                    }
                    catch (NotFoundException v7) {
                        /*SL:344*/return null;
                    }
                }
                final String v1 = /*EL:348*/"/" + v-1.replace('.', '/') + ".class";
                final InputStream v2 = /*EL:349*/this.getClass().getResourceAsStream(v1);
                /*SL:350*/if (v2 == null) {
                    /*SL:351*/return null;
                }
                /*SL:353*/v3 = ClassPoolTail.readStream(v2);
            }
        }
        catch (Exception v4) {
            /*SL:357*/throw new ClassNotFoundException("caught an exception while obtaining a class file for " + v-1, v4);
        }
        final int v5 = /*EL:362*/v-1.lastIndexOf(46);
        /*SL:363*/if (v5 != -1) {
            final String v6 = /*EL:364*/v-1.substring(0, v5);
            /*SL:365*/if (this.getPackage(v6) == null) {
                try {
                    /*SL:367*/this.definePackage(v6, null, null, null, null, null, null, null);
                }
                catch (IllegalArgumentException ex) {}
            }
        }
        /*SL:376*/if (this.domain == null) {
            /*SL:377*/return this.defineClass(v-1, v3, 0, v3.length);
        }
        /*SL:379*/return this.defineClass(v-1, v3, 0, v3.length, this.domain);
    }
    
    protected Class loadClassByDelegation(final String a1) throws ClassNotFoundException {
        Class v1 = /*EL:395*/null;
        /*SL:396*/if (this.doDelegation && /*EL:397*/(a1.startsWith("java.") || a1.startsWith("javax.") || /*EL:398*/a1.startsWith("sun.") || /*EL:399*/a1.startsWith("com.sun.") || /*EL:400*/a1.startsWith("org.w3c.") || /*EL:401*/a1.startsWith("org.xml.") || /*EL:402*/this.notDelegated(a1))) {
            /*SL:404*/v1 = this.delegateToParent(a1);
        }
        /*SL:406*/return v1;
    }
    
    private boolean notDelegated(final String v2) {
        /*SL:410*/if (this.notDefinedHere.get(v2) != null) {
            /*SL:411*/return true;
        }
        /*SL:414*/for (int v3 = this.notDefinedPackages.size(), a1 = 0; a1 < v3; ++a1) {
            /*SL:415*/if (v2.startsWith(this.notDefinedPackages.elementAt(a1))) {
                /*SL:416*/return true;
            }
        }
        /*SL:418*/return false;
    }
    
    protected Class delegateToParent(final String a1) throws ClassNotFoundException {
        final ClassLoader v1 = /*EL:424*/this.getParent();
        /*SL:425*/if (v1 != null) {
            /*SL:426*/return v1.loadClass(a1);
        }
        /*SL:428*/return this.findSystemClass(a1);
    }
}
