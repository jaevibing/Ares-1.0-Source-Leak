package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import java.util.Collection;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.util.ObfuscationUtil;
import javax.lang.model.type.TypeMirror;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import java.util.Iterator;
import java.io.File;
import javax.tools.Diagnostic;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import java.util.List;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationEnvironment;

public abstract class ObfuscationEnvironment implements IObfuscationEnvironment
{
    protected final ObfuscationType type;
    protected final IMappingProvider mappingProvider;
    protected final IMappingWriter mappingWriter;
    protected final RemapperProxy remapper;
    protected final IMixinAnnotationProcessor ap;
    protected final String outFileName;
    protected final List<String> inFileNames;
    private boolean initDone;
    
    protected ObfuscationEnvironment(final ObfuscationType a1) {
        this.remapper = new RemapperProxy();
        this.type = a1;
        this.ap = a1.getAnnotationProcessor();
        this.inFileNames = a1.getInputFileNames();
        this.outFileName = a1.getOutputFileName();
        this.mappingProvider = this.getMappingProvider(this.ap, this.ap.getProcessingEnvironment().getFiler());
        this.mappingWriter = this.getMappingWriter(this.ap, this.ap.getProcessingEnvironment().getFiler());
    }
    
    @Override
    public String toString() {
        /*SL:133*/return this.type.toString();
    }
    
    protected abstract IMappingProvider getMappingProvider(final Messager p0, final Filer p1);
    
    protected abstract IMappingWriter getMappingWriter(final Messager p0, final Filer p1);
    
    private boolean initMappings() {
        /*SL:141*/if (!this.initDone) {
            /*SL:142*/this.initDone = true;
            /*SL:144*/if (this.inFileNames == null) {
                /*SL:145*/this.ap.printMessage(Diagnostic.Kind.ERROR, "The " + this.type.getConfig().getInputFileOption() + " argument was not supplied, obfuscation processing will not occur");
                /*SL:147*/return false;
            }
            int n = /*EL:150*/0;
            /*SL:152*/for (final String s : this.inFileNames) {
                final File v0 = /*EL:153*/new File(s);
                try {
                    /*SL:155*/if (!v0.isFile()) {
                        continue;
                    }
                    /*SL:156*/this.ap.printMessage(Diagnostic.Kind.NOTE, "Loading " + this.type + " mappings from " + v0.getAbsolutePath());
                    /*SL:157*/this.mappingProvider.read(v0);
                    /*SL:158*/++n;
                }
                catch (Exception v) {
                    /*SL:161*/v.printStackTrace();
                }
            }
            /*SL:165*/if (n < 1) {
                /*SL:166*/this.ap.printMessage(Diagnostic.Kind.ERROR, "No valid input files for " + this.type + " could be read, processing may not be sucessful.");
                /*SL:167*/this.mappingProvider.clear();
            }
        }
        /*SL:171*/return !this.mappingProvider.isEmpty();
    }
    
    public ObfuscationType getType() {
        /*SL:178*/return this.type;
    }
    
    @Override
    public MappingMethod getObfMethod(final MemberInfo a1) {
        final MappingMethod v1 = /*EL:186*/this.getObfMethod(a1.asMethodMapping());
        /*SL:187*/if (v1 != null || !a1.isFullyQualified()) {
            /*SL:188*/return v1;
        }
        final TypeHandle v2 = /*EL:192*/this.ap.getTypeProvider().getTypeHandle(a1.owner);
        /*SL:193*/if (v2 == null || v2.isImaginary()) {
            /*SL:194*/return null;
        }
        final TypeMirror v3 = /*EL:198*/v2.getElement().getSuperclass();
        /*SL:199*/if (v3.getKind() != TypeKind.DECLARED) {
            /*SL:200*/return null;
        }
        final String v4 = /*EL:204*/((TypeElement)((DeclaredType)v3).asElement()).getQualifiedName().toString();
        /*SL:205*/return this.getObfMethod(new MemberInfo(a1.name, v4.replace('.', '/'), a1.desc, a1.matchAll));
    }
    
    @Override
    public MappingMethod getObfMethod(final MappingMethod a1) {
        /*SL:213*/return this.getObfMethod(a1, true);
    }
    
    @Override
    public MappingMethod getObfMethod(final MappingMethod v-2, final boolean v-1) {
        /*SL:221*/if (!this.initMappings()) {
            /*SL:246*/return null;
        }
        boolean a2 = true;
        MappingMethod v1;
        for (v1 = null, a2 = v-2; a2 != null && v1 == null; v1 = this.mappingProvider.getMethodMapping(a2), a2 = a2.getSuper()) {}
        if (v1 == null) {
            if (v-1) {
                return null;
            }
            v1 = v-2.copy();
            a2 = false;
        }
        final String v2 = this.getObfClass(v1.getOwner());
        if (v2 == null || v2.equals(v-2.getOwner()) || v2.equals(v1.getOwner())) {
            return a2 ? v1 : null;
        }
        if (a2) {
            return v1.move(v2);
        }
        final String v3 = ObfuscationUtil.mapDescriptor(v1.getDesc(), this.remapper);
        return new MappingMethod(v2, v1.getSimpleName(), v3);
    }
    
    @Override
    public MemberInfo remapDescriptor(final MemberInfo v-3) {
        boolean b = /*EL:257*/false;
        String owner = /*EL:259*/v-3.owner;
        /*SL:260*/if (owner != null) {
            final String a1 = /*EL:261*/this.remapper.map(owner);
            /*SL:262*/if (a1 != null) {
                /*SL:263*/owner = a1;
                /*SL:264*/b = true;
            }
        }
        String v0 = /*EL:268*/v-3.desc;
        /*SL:269*/if (v0 != null) {
            final String v = /*EL:270*/ObfuscationUtil.mapDescriptor(v-3.desc, this.remapper);
            /*SL:271*/if (!v.equals(v-3.desc)) {
                /*SL:272*/v0 = v;
                /*SL:273*/b = true;
            }
        }
        /*SL:277*/return b ? new MemberInfo(v-3.name, owner, v0, v-3.matchAll) : null;
    }
    
    @Override
    public String remapDescriptor(final String a1) {
        /*SL:289*/return ObfuscationUtil.mapDescriptor(a1, this.remapper);
    }
    
    @Override
    public MappingField getObfField(final MemberInfo a1) {
        /*SL:297*/return this.getObfField(a1.asFieldMapping(), true);
    }
    
    @Override
    public MappingField getObfField(final MappingField a1) {
        /*SL:305*/return this.getObfField(a1, true);
    }
    
    @Override
    public MappingField getObfField(final MappingField a1, final boolean a2) {
        /*SL:313*/if (!this.initMappings()) {
            /*SL:314*/return null;
        }
        MappingField v1 = /*EL:317*/this.mappingProvider.getFieldMapping(a1);
        /*SL:319*/if (v1 == null) {
            /*SL:320*/if (a2) {
                /*SL:321*/return null;
            }
            /*SL:323*/v1 = a1;
        }
        final String v2 = /*EL:325*/this.getObfClass(v1.getOwner());
        /*SL:326*/if (v2 == null || v2.equals(a1.getOwner()) || v2.equals(v1.getOwner())) {
            /*SL:327*/return (v1 != a1) ? v1 : null;
        }
        /*SL:329*/return v1.move(v2);
    }
    
    @Override
    public String getObfClass(final String a1) {
        /*SL:337*/if (!this.initMappings()) {
            /*SL:338*/return null;
        }
        /*SL:340*/return this.mappingProvider.getClassMapping(a1);
    }
    
    @Override
    public void writeMappings(final Collection<IMappingConsumer> v2) {
        final IMappingConsumer.MappingSet<MappingField> v3 = /*EL:348*/new IMappingConsumer.MappingSet<MappingField>();
        final IMappingConsumer.MappingSet<MappingMethod> v4 = /*EL:349*/new IMappingConsumer.MappingSet<MappingMethod>();
        /*SL:351*/for (final IMappingConsumer a1 : v2) {
            /*SL:352*/v3.addAll((Collection<?>)a1.getFieldMappings(this.type));
            /*SL:353*/v4.addAll((Collection<?>)a1.getMethodMappings(this.type));
        }
        /*SL:356*/this.mappingWriter.write(this.outFileName, this.type, v3, v4);
    }
    
    final class RemapperProxy implements ObfuscationUtil.IClassRemapper
    {
        @Override
        public String map(final String a1) {
            /*SL:69*/if (ObfuscationEnvironment.this.mappingProvider == null) {
                /*SL:70*/return null;
            }
            /*SL:72*/return ObfuscationEnvironment.this.mappingProvider.getClassMapping(a1);
        }
        
        @Override
        public String unmap(final String a1) {
            /*SL:77*/if (ObfuscationEnvironment.this.mappingProvider == null) {
                /*SL:78*/return null;
            }
            /*SL:80*/return ObfuscationEnvironment.this.mappingProvider.getClassMapping(a1);
        }
    }
}
