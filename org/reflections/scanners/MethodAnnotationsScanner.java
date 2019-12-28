package org.reflections.scanners;

import java.util.Iterator;

public class MethodAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object v-1) {
        /*SL:9*/for (final Object v1 : this.getMetadataAdapter().getMethods(v-1)) {
            /*SL:10*/for (final String a1 : this.getMetadataAdapter().getMethodAnnotationNames(v1)) {
                /*SL:11*/if (this.acceptResult(a1)) {
                    /*SL:12*/this.getStore().put(a1, this.getMetadataAdapter().getMethodFullKey(v-1, v1));
                }
            }
        }
    }
}
