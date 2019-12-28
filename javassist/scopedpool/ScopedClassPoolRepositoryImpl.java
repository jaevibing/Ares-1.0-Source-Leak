package javassist.scopedpool;

import java.util.Iterator;
import java.util.ArrayList;
import javassist.ClassPath;
import javassist.LoaderClassPath;
import java.util.Collections;
import java.util.WeakHashMap;
import javassist.ClassPool;
import java.util.Map;

public class ScopedClassPoolRepositoryImpl implements ScopedClassPoolRepository
{
    private static final ScopedClassPoolRepositoryImpl instance;
    private boolean prune;
    boolean pruneWhenCached;
    protected Map registeredCLs;
    protected ClassPool classpool;
    protected ScopedClassPoolFactory factory;
    
    public static ScopedClassPoolRepository getInstance() {
        /*SL:61*/return ScopedClassPoolRepositoryImpl.instance;
    }
    
    private ScopedClassPoolRepositoryImpl() {
        this.prune = true;
        this.registeredCLs = Collections.<Object, Object>synchronizedMap(new WeakHashMap<Object, Object>());
        this.factory = new ScopedClassPoolFactoryImpl();
        this.classpool = ClassPool.getDefault();
        final ClassLoader v1 = Thread.currentThread().getContextClassLoader();
        this.classpool.insertClassPath(new LoaderClassPath(v1));
    }
    
    @Override
    public boolean isPrune() {
        /*SL:80*/return this.prune;
    }
    
    @Override
    public void setPrune(final boolean a1) {
        /*SL:89*/this.prune = a1;
    }
    
    @Override
    public ScopedClassPool createScopedClassPool(final ClassLoader a1, final ClassPool a2) {
        /*SL:100*/return this.factory.create(a1, a2, this);
    }
    
    @Override
    public ClassPool findClassPool(final ClassLoader a1) {
        /*SL:104*/if (a1 == null) {
            /*SL:105*/return this.registerClassLoader(ClassLoader.getSystemClassLoader());
        }
        /*SL:107*/return this.registerClassLoader(a1);
    }
    
    @Override
    public ClassPool registerClassLoader(final ClassLoader v2) {
        /*SL:117*/synchronized (this.registeredCLs) {
            /*SL:123*/if (this.registeredCLs.containsKey(v2)) {
                /*SL:124*/return this.registeredCLs.get(v2);
            }
            final ScopedClassPool a1 = /*EL:126*/this.createScopedClassPool(v2, this.classpool);
            /*SL:127*/this.registeredCLs.put(v2, a1);
            /*SL:128*/return a1;
        }
    }
    
    @Override
    public Map getRegisteredCLs() {
        /*SL:136*/this.clearUnregisteredClassLoaders();
        /*SL:137*/return this.registeredCLs;
    }
    
    @Override
    public void clearUnregisteredClassLoaders() {
        ArrayList list = /*EL:145*/null;
        /*SL:146*/synchronized (this.registeredCLs) {
            final Iterator iterator = /*EL:147*/this.registeredCLs.values().iterator();
            /*SL:148*/while (iterator.hasNext()) {
                final ScopedClassPool v0 = /*EL:149*/iterator.next();
                /*SL:150*/if (v0.isUnloadedClassLoader()) {
                    /*SL:151*/iterator.remove();
                    final ClassLoader v = /*EL:152*/v0.getClassLoader();
                    /*SL:153*/if (v == null) {
                        continue;
                    }
                    /*SL:154*/if (list == null) {
                        /*SL:155*/list = new ArrayList();
                    }
                    /*SL:157*/list.add(v);
                }
            }
            /*SL:161*/if (list != null) {
                /*SL:162*/for (int v2 = 0; v2 < list.size(); ++v2) {
                    /*SL:163*/this.unregisterClassLoader(list.get(v2));
                }
            }
        }
    }
    
    @Override
    public void unregisterClassLoader(final ClassLoader v2) {
        /*SL:170*/synchronized (this.registeredCLs) {
            final ScopedClassPool a1 = /*EL:171*/this.registeredCLs.remove(v2);
            /*SL:172*/if (a1 != null) {
                /*SL:173*/a1.close();
            }
        }
    }
    
    public void insertDelegate(final ScopedClassPoolRepository a1) {
    }
    
    @Override
    public void setClassPoolFactory(final ScopedClassPoolFactory a1) {
        /*SL:182*/this.factory = a1;
    }
    
    @Override
    public ScopedClassPoolFactory getClassPoolFactory() {
        /*SL:186*/return this.factory;
    }
    
    static {
        instance = new ScopedClassPoolRepositoryImpl();
    }
}
