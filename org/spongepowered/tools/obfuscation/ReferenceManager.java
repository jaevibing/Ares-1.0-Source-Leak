package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import java.util.List;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;

public class ReferenceManager implements IReferenceManager
{
    private final IMixinAnnotationProcessor ap;
    private final String outRefMapFileName;
    private final List<ObfuscationEnvironment> environments;
    private final ReferenceMapper refMapper;
    private boolean allowConflicts;
    
    public ReferenceManager(final IMixinAnnotationProcessor a1, final List<ObfuscationEnvironment> a2) {
        this.refMapper = new ReferenceMapper();
        this.ap = a1;
        this.environments = a2;
        this.outRefMapFileName = this.ap.getOption("outRefMapFile");
    }
    
    @Override
    public boolean getAllowConflicts() {
        /*SL:106*/return this.allowConflicts;
    }
    
    @Override
    public void setAllowConflicts(final boolean a1) {
        /*SL:115*/this.allowConflicts = a1;
    }
    
    @Override
    public void write() {
        /*SL:123*/if (this.outRefMapFileName == null) {
            /*SL:124*/return;
        }
        PrintWriter v0 = /*EL:127*/null;
        try {
            /*SL:130*/v0 = this.newWriter(this.outRefMapFileName, "refmap");
            /*SL:131*/this.refMapper.write(v0);
        }
        catch (IOException v) {
            /*SL:133*/v.printStackTrace();
        }
        finally {
            /*SL:135*/if (v0 != null) {
                try {
                    /*SL:137*/v0.close();
                }
                catch (Exception ex) {}
            }
        }
    }
    
    private PrintWriter newWriter(final String v1, final String v2) throws IOException {
        /*SL:149*/if (v1.matches("^.*[\\\\/:].*$")) {
            final File a1 = /*EL:150*/new File(v1);
            /*SL:151*/a1.getParentFile().mkdirs();
            /*SL:152*/this.ap.printMessage(Diagnostic.Kind.NOTE, "Writing " + v2 + " to " + a1.getAbsolutePath());
            /*SL:153*/return new PrintWriter(a1);
        }
        final FileObject v3 = /*EL:156*/this.ap.getProcessingEnvironment().getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", v1, new Element[0]);
        /*SL:157*/this.ap.printMessage(Diagnostic.Kind.NOTE, "Writing " + v2 + " to " + new File(v3.toUri()).getAbsolutePath());
        /*SL:158*/return new PrintWriter(v3.openWriter());
    }
    
    @Override
    public ReferenceMapper getMapper() {
        /*SL:167*/return this.refMapper;
    }
    
    @Override
    public void addMethodMapping(final String v2, final String v3, final ObfuscationData<MappingMethod> v4) {
        /*SL:177*/for (MemberInfo a3 : this.environments) {
            final MappingMethod a2 = /*EL:178*/v4.get(a3.getType());
            /*SL:179*/if (a2 != null) {
                /*SL:180*/a3 = new MemberInfo(a2);
                /*SL:181*/this.addMapping(a3.getType(), v2, v3, a3.toString());
            }
        }
    }
    
    @Override
    public void addMethodMapping(final String v1, final String v2, final MemberInfo v3, final ObfuscationData<MappingMethod> v4) {
        /*SL:194*/for (MemberInfo a3 : this.environments) {
            final MappingMethod a2 = /*EL:195*/v4.get(a3.getType());
            /*SL:196*/if (a2 != null) {
                /*SL:197*/a3 = v3.remapUsing(a2, true);
                /*SL:198*/this.addMapping(a3.getType(), v1, v2, a3.toString());
            }
        }
    }
    
    @Override
    public void addFieldMapping(final String v1, final String v2, final MemberInfo v3, final ObfuscationData<MappingField> v4) {
        /*SL:211*/for (MemberInfo a3 : this.environments) {
            final MappingField a2 = /*EL:212*/v4.get(a3.getType());
            /*SL:213*/if (a2 != null) {
                /*SL:214*/a3 = MemberInfo.fromMapping(a2.transform(a3.remapDescriptor(v3.desc)));
                /*SL:215*/this.addMapping(a3.getType(), v1, v2, a3.toString());
            }
        }
    }
    
    @Override
    public void addClassMapping(final String v1, final String v2, final ObfuscationData<String> v3) {
        /*SL:227*/for (String a2 : this.environments) {
            /*SL:228*/a2 = v3.get(a2.getType());
            /*SL:229*/if (a2 != null) {
                /*SL:230*/this.addMapping(a2.getType(), v1, v2, a2);
            }
        }
    }
    
    protected void addMapping(final ObfuscationType a1, final String a2, final String a3, final String a4) {
        final String v1 = /*EL:236*/this.refMapper.addMapping(a1.getKey(), a2, a3, a4);
        /*SL:237*/if (a1.isDefault()) {
            /*SL:238*/this.refMapper.addMapping(null, a2, a3, a4);
        }
        /*SL:241*/if (!this.allowConflicts && v1 != null && !v1.equals(a4)) {
            /*SL:242*/throw new ReferenceConflictException(v1, a4);
        }
    }
    
    public static class ReferenceConflictException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        private final String oldReference;
        private final String newReference;
        
        public ReferenceConflictException(final String a1, final String a2) {
            this.oldReference = a1;
            this.newReference = a2;
        }
        
        public String getOld() {
            /*SL:63*/return this.oldReference;
        }
        
        public String getNew() {
            /*SL:67*/return this.newReference;
        }
    }
}
