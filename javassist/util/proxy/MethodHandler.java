package javassist.util.proxy;

import java.lang.reflect.Method;

public interface MethodHandler
{
    Object invoke(Object p0, Method p1, Method p2, Object[] p3) throws Throwable;
}
