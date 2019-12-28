package org.reflections.scanners;

import org.reflections.vfs.Vfs;

public class ResourcesScanner extends AbstractScanner
{
    @Override
    public boolean acceptsInput(final String a1) {
        /*SL:9*/return !a1.endsWith(".class");
    }
    
    @Override
    public Object scan(final Vfs.File a1, final Object a2) {
        /*SL:13*/this.getStore().put(a1.getName(), a1.getRelativePath());
        /*SL:14*/return a2;
    }
    
    @Override
    public void scan(final Object a1) {
        /*SL:18*/throw new UnsupportedOperationException();
    }
}
