package org.reflections.scanners;

import java.util.Iterator;
import java.util.List;

public class FieldAnnotationsScanner extends AbstractScanner
{
    @Override
    public void scan(final Object v-6) {
        final String className = /*EL:9*/this.getMetadataAdapter().getClassName(v-6);
        final List<Object> fields = /*EL:10*/this.getMetadataAdapter().getFields(v-6);
        /*SL:11*/for (final Object next : fields) {
            final List<String> fieldAnnotationNames = /*EL:12*/this.getMetadataAdapter().getFieldAnnotationNames(next);
            /*SL:13*/for (final String v1 : fieldAnnotationNames) {
                /*SL:15*/if (this.acceptResult(v1)) {
                    final String a1 = /*EL:16*/this.getMetadataAdapter().getFieldName(next);
                    /*SL:17*/this.getStore().put(v1, String.format("%s.%s", className, a1));
                }
            }
        }
    }
}
