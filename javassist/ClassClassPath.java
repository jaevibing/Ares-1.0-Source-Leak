package javassist;

import java.net.URL;
import java.io.InputStream;

public class ClassClassPath implements ClassPath
{
    private Class thisClass;
    
    public ClassClassPath(final Class a1) {
        this.thisClass = a1;
    }
    
    ClassClassPath() {
        this(Object.class);
    }
    
    @Override
    public InputStream openClassfile(final String a1) {
        final String v1 = /*EL:74*/"/" + a1.replace('.', '/') + ".class";
        /*SL:75*/return this.thisClass.getResourceAsStream(v1);
    }
    
    @Override
    public URL find(final String a1) {
        final String v1 = /*EL:84*/"/" + a1.replace('.', '/') + ".class";
        /*SL:85*/return this.thisClass.getResource(v1);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String toString() {
        /*SL:95*/return this.thisClass.getName() + ".class";
    }
}
