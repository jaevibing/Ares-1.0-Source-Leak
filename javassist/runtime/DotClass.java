package javassist.runtime;

public class DotClass
{
    public static NoClassDefFoundError fail(final ClassNotFoundException a1) {
        /*SL:27*/return new NoClassDefFoundError(a1.getMessage());
    }
}
