package org.yaml.snakeyaml.extensions.compactnotation;

public class PackageCompactConstructor extends CompactConstructor
{
    private String packageName;
    
    public PackageCompactConstructor(final String a1) {
        this.packageName = a1;
    }
    
    @Override
    protected Class<?> getClassForName(final String v2) throws ClassNotFoundException {
        /*SL:27*/if (v2.indexOf(46) < 0) {
            try {
                final Class<?> a1 = /*EL:29*/Class.forName(this.packageName + "." + v2);
                /*SL:30*/return a1;
            }
            catch (ClassNotFoundException ex) {}
        }
        /*SL:35*/return super.getClassForName(v2);
    }
}
