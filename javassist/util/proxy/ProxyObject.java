package javassist.util.proxy;

public interface ProxyObject extends Proxy
{
    void setHandler(MethodHandler p0);
    
    MethodHandler getHandler();
}
