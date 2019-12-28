package org.reflections.scanners;

import java.util.List;
import java.util.Iterator;
import org.reflections.adapters.MetadataAdapter;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.lang.reflect.Modifier;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.LocalVariableAttribute;

public class MethodParameterNamesScanner extends AbstractScanner
{
    @Override
    public void scan(final Object v-4) {
        final MetadataAdapter metadataAdapter = /*EL:19*/this.getMetadataAdapter();
        /*SL:21*/for (final Object next : metadataAdapter.getMethods(v-4)) {
            final String v0 = /*EL:22*/metadataAdapter.getMethodFullKey(v-4, next);
            /*SL:23*/if (this.acceptResult(v0)) {
                final LocalVariableAttribute v = /*EL:24*/(LocalVariableAttribute)((MethodInfo)next).getCodeAttribute().getAttribute("LocalVariableTable");
                final int v2 = /*EL:25*/v.tableLength();
                int v3 = /*EL:26*/Modifier.isStatic(((MethodInfo)next).getAccessFlags()) ? 0 : 1;
                /*SL:27*/if (v3 >= v2) {
                    continue;
                }
                final List<String> a1 = /*EL:28*/new ArrayList<String>(v2 - v3);
                /*SL:29*/while (v3 < v2) {
                    a1.add(((MethodInfo)next).getConstPool().getUtf8Info(v.nameIndex(v3++)));
                }
                /*SL:30*/this.getStore().put(v0, Joiner.on(", ").join(a1));
            }
        }
    }
}
