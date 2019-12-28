package javassist.scopedpool;

import javassist.ClassPool;

public interface ScopedClassPoolFactory
{
    ScopedClassPool create(ClassLoader p0, ClassPool p1, ScopedClassPoolRepository p2);
    
    ScopedClassPool create(ClassPool p0, ScopedClassPoolRepository p1);
}
