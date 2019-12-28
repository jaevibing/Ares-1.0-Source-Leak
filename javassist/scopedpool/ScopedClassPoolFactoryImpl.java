package javassist.scopedpool;

import javassist.ClassPool;

public class ScopedClassPoolFactoryImpl implements ScopedClassPoolFactory
{
    @Override
    public ScopedClassPool create(final ClassLoader a1, final ClassPool a2, final ScopedClassPoolRepository a3) {
        /*SL:33*/return new ScopedClassPool(a1, a2, a3, false);
    }
    
    @Override
    public ScopedClassPool create(final ClassPool a1, final ScopedClassPoolRepository a2) {
        /*SL:41*/return new ScopedClassPool(null, a1, a2, true);
    }
}
