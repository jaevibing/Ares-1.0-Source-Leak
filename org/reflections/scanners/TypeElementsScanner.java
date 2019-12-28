package org.reflections.scanners;

import java.util.Iterator;
import com.google.common.base.Joiner;

public class TypeElementsScanner extends AbstractScanner
{
    private boolean includeFields;
    private boolean includeMethods;
    private boolean includeAnnotations;
    private boolean publicOnly;
    
    public TypeElementsScanner() {
        this.includeFields = true;
        this.includeMethods = true;
        this.includeAnnotations = true;
        this.publicOnly = true;
    }
    
    @Override
    public void scan(final Object v-2) {
        final String className = /*EL:14*/this.getMetadataAdapter().getClassName(v-2);
        /*SL:15*/if (!this.acceptResult(className)) {
            return;
        }
        /*SL:17*/this.getStore().put(className, "");
        /*SL:19*/if (this.includeFields) {
            /*SL:20*/for (final Object v1 : this.getMetadataAdapter().getFields(v-2)) {
                final String a1 = /*EL:21*/this.getMetadataAdapter().getFieldName(v1);
                /*SL:22*/this.getStore().put(className, a1);
            }
        }
        /*SL:26*/if (this.includeMethods) {
            /*SL:27*/for (final Object v1 : this.getMetadataAdapter().getMethods(v-2)) {
                /*SL:28*/if (!this.publicOnly || this.getMetadataAdapter().isPublic(v1)) {
                    final String v2 = /*EL:29*/this.getMetadataAdapter().getMethodName(v1) + "(" + /*EL:30*/Joiner.on(", ").join(this.getMetadataAdapter().getParameterNames(v1)) + ")";
                    /*SL:31*/this.getStore().put(className, v2);
                }
            }
        }
        /*SL:36*/if (this.includeAnnotations) {
            /*SL:37*/for (final Object v1 : this.getMetadataAdapter().getClassAnnotationNames(v-2)) {
                /*SL:38*/this.getStore().put(className, "@" + v1);
            }
        }
    }
    
    public TypeElementsScanner includeFields() {
        /*SL:44*/return this.includeFields(true);
    }
    
    public TypeElementsScanner includeFields(final boolean a1) {
        /*SL:45*/this.includeFields = a1;
        return this;
    }
    
    public TypeElementsScanner includeMethods() {
        /*SL:46*/return this.includeMethods(true);
    }
    
    public TypeElementsScanner includeMethods(final boolean a1) {
        /*SL:47*/this.includeMethods = a1;
        return this;
    }
    
    public TypeElementsScanner includeAnnotations() {
        /*SL:48*/return this.includeAnnotations(true);
    }
    
    public TypeElementsScanner includeAnnotations(final boolean a1) {
        /*SL:49*/this.includeAnnotations = a1;
        return this;
    }
    
    public TypeElementsScanner publicOnly(final boolean a1) {
        /*SL:50*/this.publicOnly = a1;
        return this;
    }
    
    public TypeElementsScanner publicOnly() {
        /*SL:51*/return this.publicOnly(true);
    }
}
