package org.reflections.scanners;

import org.reflections.vfs.Vfs;

@Deprecated
public class TypesScanner extends AbstractScanner
{
    @Override
    public Object scan(final Vfs.File a1, Object a2) {
        /*SL:13*/a2 = super.scan(a1, a2);
        final String v1 = /*EL:14*/this.getMetadataAdapter().getClassName(a2);
        /*SL:15*/this.getStore().put(v1, v1);
        /*SL:16*/return a2;
    }
    
    @Override
    public void scan(final Object a1) {
        /*SL:21*/throw new UnsupportedOperationException("should not get here");
    }
}
