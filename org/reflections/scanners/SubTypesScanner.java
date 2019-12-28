package org.reflections.scanners;

import java.util.Iterator;
import com.google.common.base.Predicate;
import org.reflections.util.FilterBuilder;

public class SubTypesScanner extends AbstractScanner
{
    public SubTypesScanner() {
        this(true);
    }
    
    public SubTypesScanner(final boolean a1) {
        if (a1) {
            this.filterResultsBy(new FilterBuilder().exclude(Object.class.getName()));
        }
    }
    
    @Override
    public void scan(final Object v2) {
        final String v3 = /*EL:25*/this.getMetadataAdapter().getClassName(v2);
        final String v4 = /*EL:26*/this.getMetadataAdapter().getSuperclassName(v2);
        /*SL:28*/if (this.acceptResult(v4)) {
            /*SL:29*/this.getStore().put(v4, v3);
        }
        /*SL:32*/for (final String a1 : this.getMetadataAdapter().getInterfacesNames(v2)) {
            /*SL:33*/if (this.acceptResult(a1)) {
                /*SL:34*/this.getStore().put(a1, v3);
            }
        }
    }
}
