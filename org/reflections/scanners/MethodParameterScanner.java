package org.reflections.scanners;

import java.util.List;
import java.util.Iterator;
import org.reflections.adapters.MetadataAdapter;

public class MethodParameterScanner extends AbstractScanner
{
    @Override
    public void scan(final Object v-6) {
        final MetadataAdapter metadataAdapter = /*EL:13*/this.getMetadataAdapter();
        /*SL:15*/for (final Object next : metadataAdapter.getMethods(v-6)) {
            final String string = /*EL:17*/metadataAdapter.getParameterNames(next).toString();
            /*SL:18*/if (this.acceptResult(string)) {
                /*SL:19*/this.getStore().put(string, metadataAdapter.getMethodFullKey(v-6, next));
            }
            final String returnTypeName = /*EL:22*/metadataAdapter.getReturnTypeName(next);
            /*SL:23*/if (this.acceptResult(returnTypeName)) {
                /*SL:24*/this.getStore().put(returnTypeName, metadataAdapter.getMethodFullKey(v-6, next));
            }
            final List<String> v0 = /*EL:27*/metadataAdapter.getParameterNames(next);
            /*SL:28*/for (int v = 0; v < v0.size(); ++v) {
                /*SL:29*/for (final Object a1 : metadataAdapter.getParameterAnnotationNames(next, v)) {
                    /*SL:30*/if (this.acceptResult((String)a1)) {
                        /*SL:31*/this.getStore().put((String)a1, metadataAdapter.getMethodFullKey(v-6, next));
                    }
                }
            }
        }
    }
}
