package org.reflections.scanners;

import java.util.Iterator;
import java.lang.annotation.Inherited;

public class TypeAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object v2) {
        final String v3 = /*EL:10*/this.getMetadataAdapter().getClassName(v2);
        /*SL:12*/for (final String a1 : this.getMetadataAdapter().getClassAnnotationNames(v2)) {
            /*SL:14*/if (this.acceptResult(a1) || a1.equals(Inherited.class.getName())) {
                /*SL:16*/this.getStore().put(a1, v3);
            }
        }
    }
}
