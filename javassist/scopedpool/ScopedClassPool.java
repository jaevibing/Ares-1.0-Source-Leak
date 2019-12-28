package javassist.scopedpool;

import javassist.CannotCompileException;
import java.security.ProtectionDomain;
import javassist.NotFoundException;
import java.util.Iterator;
import java.util.Map;
import javassist.CtClass;
import javassist.ClassPath;
import javassist.LoaderClassPath;
import java.lang.ref.WeakReference;
import javassist.ClassPool;

public class ScopedClassPool extends ClassPool
{
    protected ScopedClassPoolRepository repository;
    protected WeakReference classLoader;
    protected LoaderClassPath classPath;
    protected SoftValueHashMap softcache;
    boolean isBootstrapCl;
    
    protected ScopedClassPool(final ClassLoader a1, final ClassPool a2, final ScopedClassPoolRepository a3) {
        this(a1, a2, a3, false);
    }
    
    protected ScopedClassPool(final ClassLoader a1, final ClassPool a2, final ScopedClassPoolRepository a3, final boolean a4) {
        super(a2);
        this.softcache = new SoftValueHashMap();
        this.isBootstrapCl = true;
        this.repository = a3;
        this.classLoader = new WeakReference((T)a1);
        if (a1 != null) {
            this.insertClassPath(this.classPath = new LoaderClassPath(a1));
        }
        this.childFirstLookup = true;
        if (!a4 && a1 == null) {
            this.isBootstrapCl = true;
        }
    }
    
    @Override
    public ClassLoader getClassLoader() {
        final ClassLoader v1 = /*EL:103*/this.getClassLoader0();
        /*SL:104*/if (v1 == null && !this.isBootstrapCl) {
            /*SL:106*/throw new IllegalStateException("ClassLoader has been garbage collected");
        }
        /*SL:109*/return v1;
    }
    
    protected ClassLoader getClassLoader0() {
        /*SL:113*/return (ClassLoader)this.classLoader.get();
    }
    
    public void close() {
        /*SL:120*/this.removeClassPath(this.classPath);
        /*SL:121*/this.classPath.close();
        /*SL:122*/this.classes.clear();
        /*SL:123*/this.softcache.clear();
    }
    
    public synchronized void flushClass(final String a1) {
        /*SL:133*/this.classes.remove(a1);
        /*SL:134*/this.softcache.remove(a1);
    }
    
    public synchronized void soften(final CtClass a1) {
        /*SL:144*/if (this.repository.isPrune()) {
            /*SL:145*/a1.prune();
        }
        /*SL:146*/this.classes.remove(a1.getName());
        /*SL:147*/this.softcache.put(a1.getName(), a1);
    }
    
    public boolean isUnloadedClassLoader() {
        /*SL:156*/return false;
    }
    
    @Override
    protected CtClass getCached(final String v-4) {
        CtClass ctClass = /*EL:167*/this.getCachedLocally(v-4);
        /*SL:168*/if (ctClass == null) {
            boolean b = /*EL:169*/false;
            final ClassLoader classLoader0 = /*EL:171*/this.getClassLoader0();
            /*SL:172*/if (classLoader0 != null) {
                final int a1 = /*EL:173*/v-4.lastIndexOf(36);
                String v1 = /*EL:174*/null;
                /*SL:175*/if (a1 < 0) {
                    /*SL:176*/v1 = v-4.replaceAll("[\\.]", "/") + ".class";
                }
                else {
                    /*SL:182*/v1 = v-4.substring(0, a1).replaceAll("[\\.]", "/") + v-4.substring(a1) + ".class";
                }
                /*SL:185*/b = (classLoader0.getResource(v1) != null);
            }
            /*SL:188*/if (!b) {
                final Map v2 = /*EL:189*/this.repository.getRegisteredCLs();
                /*SL:190*/synchronized (v2) {
                    /*SL:191*/for (final ScopedClassPool v4 : v2.values()) {
                        /*SL:194*/if (v4.isUnloadedClassLoader()) {
                            /*SL:195*/this.repository.unregisterClassLoader(v4.getClassLoader());
                        }
                        else {
                            /*SL:200*/ctClass = v4.getCachedLocally(v-4);
                            /*SL:201*/if (ctClass != null) {
                                /*SL:202*/return ctClass;
                            }
                            /*SL:204*/continue;
                        }
                    }
                }
            }
        }
        /*SL:209*/return ctClass;
    }
    
    @Override
    protected void cacheCtClass(final String a1, final CtClass a2, final boolean a3) {
        /*SL:223*/if (a3) {
            /*SL:224*/super.cacheCtClass(a1, a2, a3);
        }
        else {
            /*SL:227*/if (this.repository.isPrune()) {
                /*SL:228*/a2.prune();
            }
            /*SL:229*/this.softcache.put(a1, a2);
        }
    }
    
    public void lockInCache(final CtClass a1) {
        /*SL:240*/super.cacheCtClass(a1.getName(), a1, false);
    }
    
    protected CtClass getCachedLocally(final String a1) {
        final CtClass v1 = /*EL:251*/this.classes.get(a1);
        /*SL:252*/if (v1 != null) {
            /*SL:253*/return v1;
        }
        /*SL:254*/synchronized (this.softcache) {
            /*SL:255*/return (CtClass)this.softcache.get(a1);
        }
    }
    
    public synchronized CtClass getLocally(final String a1) throws NotFoundException {
        /*SL:270*/this.softcache.remove(a1);
        CtClass v1 = /*EL:271*/this.classes.get(a1);
        /*SL:272*/if (v1 == null) {
            /*SL:273*/v1 = this.createCtClass(a1, true);
            /*SL:274*/if (v1 == null) {
                /*SL:275*/throw new NotFoundException(a1);
            }
            /*SL:276*/super.cacheCtClass(a1, v1, false);
        }
        /*SL:279*/return v1;
    }
    
    @Override
    public Class toClass(final CtClass a1, final ClassLoader a2, final ProtectionDomain a3) throws CannotCompileException {
        /*SL:306*/this.lockInCache(a1);
        /*SL:307*/return super.toClass(a1, this.getClassLoader0(), a3);
    }
    
    static {
        ClassPool.doPruning = false;
        ClassPool.releaseUnmodifiedClassFile = false;
    }
}
