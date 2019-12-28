package org.spongepowered.tools.obfuscation;

import javax.lang.model.type.TypeKind;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import com.google.common.base.Strings;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.mirror.FieldHandle;
import java.util.Iterator;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class AnnotatedMixinElementHandlerAccessor extends AnnotatedMixinElementHandler implements IMixinContext
{
    public AnnotatedMixinElementHandlerAccessor(final IMixinAnnotationProcessor a1, final AnnotatedMixin a2) {
        super(a1, a2);
    }
    
    @Override
    public ReferenceMapper getReferenceMapper() {
        /*SL:157*/return null;
    }
    
    @Override
    public String getClassName() {
        /*SL:162*/return this.mixin.getClassRef().replace('/', '.');
    }
    
    @Override
    public String getClassRef() {
        /*SL:167*/return this.mixin.getClassRef();
    }
    
    @Override
    public String getTargetClassRef() {
        /*SL:172*/throw new UnsupportedOperationException("Target class not available at compile time");
    }
    
    @Override
    public IMixinInfo getMixin() {
        /*SL:177*/throw new UnsupportedOperationException("MixinInfo not available at compile time");
    }
    
    @Override
    public Extensions getExtensions() {
        /*SL:182*/throw new UnsupportedOperationException("Mixin Extensions not available at compile time");
    }
    
    @Override
    public boolean getOption(final MixinEnvironment.Option a1) {
        /*SL:187*/throw new UnsupportedOperationException("Options not available at compile time");
    }
    
    @Override
    public int getPriority() {
        /*SL:192*/throw new UnsupportedOperationException("Priority not available at compile time");
    }
    
    @Override
    public Target getTargetMethod(final MethodNode a1) {
        /*SL:197*/throw new UnsupportedOperationException("Target not available at compile time");
    }
    
    public void registerAccessor(final AnnotatedElementAccessor v2) {
        /*SL:206*/if (v2.getAccessorType() == null) {
            /*SL:207*/v2.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unsupported accessor type");
            /*SL:208*/return;
        }
        final String v3 = /*EL:211*/this.getAccessorTargetName(v2);
        /*SL:212*/if (v3 == null) {
            /*SL:213*/v2.printMessage(this.ap, Diagnostic.Kind.WARNING, "Cannot inflect accessor target name");
            /*SL:214*/return;
        }
        /*SL:216*/v2.setTargetName(v3);
        /*SL:218*/for (final TypeHandle a1 : this.mixin.getTargets()) {
            /*SL:219*/if (v2.getAccessorType() == AccessorInfo.AccessorType.METHOD_PROXY) {
                /*SL:220*/this.registerInvokerForTarget((AnnotatedElementInvoker)v2, a1);
            }
            else {
                /*SL:222*/this.registerAccessorForTarget(v2, a1);
            }
        }
    }
    
    private void registerAccessorForTarget(final AnnotatedElementAccessor v2, final TypeHandle v3) {
        FieldHandle v4 = /*EL:228*/v3.findField(v2.getTargetName(), v2.getTargetTypeName(), false);
        /*SL:229*/if (v4 == null) {
            /*SL:230*/if (!v3.isImaginary()) {
                /*SL:231*/v2.printMessage(this.ap, Diagnostic.Kind.ERROR, "Could not locate @Accessor target " + v2 + " in target " + v3);
                /*SL:232*/return;
            }
            /*SL:235*/v4 = new FieldHandle(v3.getName(), v2.getTargetName(), v2.getDesc());
        }
        /*SL:238*/if (!v2.shouldRemap()) {
            /*SL:239*/return;
        }
        ObfuscationData<MappingField> v5 = /*EL:242*/this.obf.getDataProvider().getObfField(v4.asMapping(false).move(v3.getName()));
        /*SL:243*/if (v5.isEmpty()) {
            final String a1 = /*EL:244*/this.mixin.isMultiTarget() ? (" in target " + v3) : "";
            /*SL:245*/v2.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + a1 + " for @Accessor target " + v2);
            /*SL:246*/return;
        }
        /*SL:249*/v5 = AnnotatedMixinElementHandler.<MappingField>stripOwnerData(v5);
        try {
            /*SL:252*/this.obf.getReferenceManager().addFieldMapping(this.mixin.getClassRef(), v2.getTargetName(), v2.getContext(), v5);
        }
        catch (ReferenceManager.ReferenceConflictException a2) {
            /*SL:254*/v2.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Accessor target " + v2 + ": " + a2.getNew() + " for target " + v3 + " conflicts with existing mapping " + a2.getOld());
        }
    }
    
    private void registerInvokerForTarget(final AnnotatedElementInvoker v2, final TypeHandle v3) {
        MethodHandle v4 = /*EL:260*/v3.findMethod(v2.getTargetName(), v2.getTargetTypeName(), false);
        /*SL:261*/if (v4 == null) {
            /*SL:262*/if (!v3.isImaginary()) {
                /*SL:263*/v2.printMessage(this.ap, Diagnostic.Kind.ERROR, "Could not locate @Invoker target " + v2 + " in target " + v3);
                /*SL:264*/return;
            }
            /*SL:267*/v4 = new MethodHandle(v3, v2.getTargetName(), v2.getDesc());
        }
        /*SL:270*/if (!v2.shouldRemap()) {
            /*SL:271*/return;
        }
        ObfuscationData<MappingMethod> v5 = /*EL:274*/this.obf.getDataProvider().getObfMethod(v4.asMapping(false).move(v3.getName()));
        /*SL:275*/if (v5.isEmpty()) {
            final String a1 = /*EL:276*/this.mixin.isMultiTarget() ? (" in target " + v3) : "";
            /*SL:277*/v2.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + a1 + " for @Accessor target " + v2);
            /*SL:278*/return;
        }
        /*SL:281*/v5 = AnnotatedMixinElementHandler.<MappingMethod>stripOwnerData(v5);
        try {
            /*SL:284*/this.obf.getReferenceManager().addMethodMapping(this.mixin.getClassRef(), v2.getTargetName(), v2.getContext(), v5);
        }
        catch (ReferenceManager.ReferenceConflictException a2) {
            /*SL:286*/v2.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Invoker target " + v2 + ": " + a2.getNew() + " for target " + v3 + " conflicts with existing mapping " + a2.getOld());
        }
    }
    
    private String getAccessorTargetName(final AnnotatedElementAccessor a1) {
        final String v1 = /*EL:292*/a1.getAnnotationValue();
        /*SL:293*/if (Strings.isNullOrEmpty(v1)) {
            /*SL:294*/return this.inflectAccessorTarget(a1);
        }
        /*SL:296*/return v1;
    }
    
    private String inflectAccessorTarget(final AnnotatedElementAccessor a1) {
        /*SL:300*/return AccessorInfo.inflectTarget(a1.getSimpleName(), a1.getAccessorType(), "", this, false);
    }
    
    static class AnnotatedElementAccessor extends AnnotatedElement<ExecutableElement>
    {
        private final boolean shouldRemap;
        private final TypeMirror returnType;
        private String targetName;
        
        public AnnotatedElementAccessor(final ExecutableElement a1, final AnnotationHandle a2, final boolean a3) {
            super((Element)a1, a2);
            this.shouldRemap = a3;
            this.returnType = ((ExecutableElement)this.getElement()).getReturnType();
        }
        
        public boolean shouldRemap() {
            /*SL:77*/return this.shouldRemap;
        }
        
        public String getAnnotationValue() {
            /*SL:81*/return this.getAnnotation().<String>getValue();
        }
        
        public TypeMirror getTargetType() {
            /*SL:85*/switch (this.getAccessorType()) {
                case FIELD_GETTER: {
                    /*SL:87*/return this.returnType;
                }
                case FIELD_SETTER: {
                    /*SL:89*/return ((VariableElement)((ExecutableElement)this.getElement()).getParameters().get(0)).asType();
                }
                default: {
                    /*SL:91*/return null;
                }
            }
        }
        
        public String getTargetTypeName() {
            /*SL:96*/return TypeUtils.getTypeName(this.getTargetType());
        }
        
        public String getAccessorDesc() {
            /*SL:100*/return TypeUtils.getInternalName(this.getTargetType());
        }
        
        public MemberInfo getContext() {
            /*SL:104*/return new MemberInfo(this.getTargetName(), null, this.getAccessorDesc());
        }
        
        public AccessorInfo.AccessorType getAccessorType() {
            /*SL:108*/return (this.returnType.getKind() == TypeKind.VOID) ? AccessorInfo.AccessorType.FIELD_SETTER : AccessorInfo.AccessorType.FIELD_GETTER;
        }
        
        public void setTargetName(final String a1) {
            /*SL:112*/this.targetName = a1;
        }
        
        public String getTargetName() {
            /*SL:116*/return this.targetName;
        }
        
        @Override
        public String toString() {
            /*SL:121*/return (this.targetName != null) ? this.targetName : "<invalid>";
        }
    }
    
    static class AnnotatedElementInvoker extends AnnotatedElementAccessor
    {
        public AnnotatedElementInvoker(final ExecutableElement a1, final AnnotationHandle a2, final boolean a3) {
            super(a1, a2, a3);
        }
        
        @Override
        public String getAccessorDesc() {
            /*SL:136*/return TypeUtils.getDescriptor((ExecutableElement)this.getElement());
        }
        
        @Override
        public AccessorInfo.AccessorType getAccessorType() {
            /*SL:141*/return AccessorInfo.AccessorType.METHOD_PROXY;
        }
        
        @Override
        public String getTargetTypeName() {
            /*SL:146*/return TypeUtils.getJavaSignature(this.getElement());
        }
    }
}
