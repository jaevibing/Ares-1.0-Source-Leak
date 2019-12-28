package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.Unique;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.injection.throwables.InjectionValidationException;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.apache.logging.log4j.Level;
import java.util.Set;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.obfuscation.RemapperChain;
import java.util.Deque;
import java.util.Collection;
import java.util.LinkedList;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.mixin.SoftOverride;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.mixin.struct.MemberRef;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.util.ClassSignature;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.mixin.struct.SourceMap;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.util.Map;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.List;
import com.google.common.collect.BiMap;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class MixinTargetContext extends ClassContext implements IMixinContext
{
    private static final Logger logger;
    private final MixinInfo mixin;
    private final ClassNode classNode;
    private final TargetClassContext targetClass;
    private final String sessionId;
    private final ClassInfo targetClassInfo;
    private final BiMap<String, String> innerClasses;
    private final List<MethodNode> shadowMethods;
    private final Map<FieldNode, ClassInfo.Field> shadowFields;
    private final List<MethodNode> mergedMethods;
    private final InjectorGroupInfo.Map injectorGroups;
    private final List<InjectionInfo> injectors;
    private final List<AccessorInfo> accessors;
    private final boolean inheritsFromMixin;
    private final boolean detachedSuper;
    private final SourceMap.File stratum;
    private int minRequiredClassVersion;
    
    MixinTargetContext(final MixinInfo a3, final ClassNode v1, final TargetClassContext v2) {
        this.innerClasses = (BiMap<String, String>)HashBiMap.<Object, Object>create();
        this.shadowMethods = new ArrayList<MethodNode>();
        this.shadowFields = new LinkedHashMap<FieldNode, ClassInfo.Field>();
        this.mergedMethods = new ArrayList<MethodNode>();
        this.injectorGroups = new InjectorGroupInfo.Map();
        this.injectors = new ArrayList<InjectionInfo>();
        this.accessors = new ArrayList<AccessorInfo>();
        this.minRequiredClassVersion = MixinEnvironment.CompatibilityLevel.JAVA_6.classVersion();
        this.mixin = a3;
        this.classNode = v1;
        this.targetClass = v2;
        this.targetClassInfo = ClassInfo.forName(this.getTarget().getClassRef());
        this.stratum = v2.getSourceMap().addFile(this.classNode);
        this.inheritsFromMixin = (a3.getClassInfo().hasMixinInHierarchy() || this.targetClassInfo.hasMixinTargetInHierarchy());
        this.detachedSuper = !this.classNode.superName.equals(this.getTarget().getClassNode().superName);
        this.sessionId = v2.getSessionId();
        this.requireVersion(v1.version);
        final InnerClassGenerator v3 = (InnerClassGenerator)v2.getExtensions().getGenerator((Class)InnerClassGenerator.class);
        for (final String a4 : this.mixin.getInnerClasses()) {
            this.innerClasses.put(a4, v3.registerInnerClass(this.mixin, a4, this));
        }
    }
    
    void addShadowMethod(final MethodNode a1) {
        /*SL:206*/this.shadowMethods.add(a1);
    }
    
    void addShadowField(final FieldNode a1, final ClassInfo.Field a2) {
        /*SL:216*/this.shadowFields.put(a1, a2);
    }
    
    void addAccessorMethod(final MethodNode a1, final Class<? extends Annotation> a2) {
        /*SL:226*/this.accessors.add(AccessorInfo.of(this, a1, a2));
    }
    
    void addMixinMethod(final MethodNode a1) {
        /*SL:230*/Annotations.setVisible(a1, MixinMerged.class, "mixin", this.getClassName());
        /*SL:231*/this.getTarget().addMixinMethod(a1);
    }
    
    void methodMerged(final MethodNode a1) {
        /*SL:240*/this.mergedMethods.add(a1);
        /*SL:241*/this.targetClassInfo.addMethod(a1);
        /*SL:242*/this.getTarget().methodMerged(a1);
        /*SL:244*/Annotations.setVisible(a1, MixinMerged.class, "mixin", this.getClassName(), /*EL:245*/"priority", this.getPriority(), /*EL:246*/"sessionId", this.sessionId);
    }
    
    @Override
    public String toString() {
        /*SL:255*/return this.mixin.toString();
    }
    
    public MixinEnvironment getEnvironment() {
        /*SL:264*/return this.mixin.getParent().getEnvironment();
    }
    
    @Override
    public boolean getOption(final MixinEnvironment.Option a1) {
        /*SL:273*/return this.getEnvironment().getOption(a1);
    }
    
    public ClassNode getClassNode() {
        /*SL:283*/return this.classNode;
    }
    
    @Override
    public String getClassName() {
        /*SL:293*/return this.mixin.getClassName();
    }
    
    @Override
    public String getClassRef() {
        /*SL:302*/return this.mixin.getClassRef();
    }
    
    public TargetClassContext getTarget() {
        /*SL:311*/return this.targetClass;
    }
    
    @Override
    public String getTargetClassRef() {
        /*SL:322*/return this.getTarget().getClassRef();
    }
    
    public ClassNode getTargetClassNode() {
        /*SL:331*/return this.getTarget().getClassNode();
    }
    
    public ClassInfo getTargetClassInfo() {
        /*SL:340*/return this.targetClassInfo;
    }
    
    protected ClassInfo getClassInfo() {
        /*SL:350*/return this.mixin.getClassInfo();
    }
    
    public ClassSignature getSignature() {
        /*SL:359*/return this.getClassInfo().getSignature();
    }
    
    public SourceMap.File getStratum() {
        /*SL:368*/return this.stratum;
    }
    
    public int getMinRequiredClassVersion() {
        /*SL:375*/return this.minRequiredClassVersion;
    }
    
    public int getDefaultRequiredInjections() {
        /*SL:385*/return this.mixin.getParent().getDefaultRequiredInjections();
    }
    
    public String getDefaultInjectorGroup() {
        /*SL:394*/return this.mixin.getParent().getDefaultInjectorGroup();
    }
    
    public int getMaxShiftByValue() {
        /*SL:403*/return this.mixin.getParent().getMaxShiftByValue();
    }
    
    public InjectorGroupInfo.Map getInjectorGroups() {
        /*SL:412*/return this.injectorGroups;
    }
    
    public boolean requireOverwriteAnnotations() {
        /*SL:421*/return this.mixin.getParent().requireOverwriteAnnotations();
    }
    
    public ClassInfo findRealType(final ClassInfo a1) {
        /*SL:432*/if (a1 == this.getClassInfo()) {
            /*SL:433*/return this.targetClassInfo;
        }
        final ClassInfo v1 = /*EL:436*/this.targetClassInfo.findCorrespondingType(a1);
        /*SL:437*/if (v1 == null) {
            /*SL:438*/throw new InvalidMixinException(this, "Resolution error: unable to find corresponding type for " + a1 + " in hierarchy of " + this.targetClassInfo);
        }
        /*SL:442*/return v1;
    }
    
    public void transformMethod(final MethodNode v-1) {
        /*SL:454*/this.validateMethod(v-1);
        /*SL:455*/this.transformDescriptor(v-1);
        /*SL:456*/this.transformLVT(v-1);
        /*SL:459*/this.stratum.applyOffset(v-1);
        AbstractInsnNode v0 = /*EL:461*/null;
        final Iterator<AbstractInsnNode> v = /*EL:462*/v-1.instructions.iterator();
        while (v.hasNext()) {
            final AbstractInsnNode a1 = /*EL:463*/v.next();
            /*SL:465*/if (a1 instanceof MethodInsnNode) {
                /*SL:466*/this.transformMethodRef(v-1, v, new MemberRef.Method((MethodInsnNode)a1));
            }
            else/*SL:467*/ if (a1 instanceof FieldInsnNode) {
                /*SL:468*/this.transformFieldRef(v-1, v, new MemberRef.Field((FieldInsnNode)a1));
                /*SL:469*/this.checkFinal(v-1, v, (FieldInsnNode)a1);
            }
            else/*SL:470*/ if (a1 instanceof TypeInsnNode) {
                /*SL:471*/this.transformTypeNode(v-1, v, (TypeInsnNode)a1, v0);
            }
            else/*SL:472*/ if (a1 instanceof LdcInsnNode) {
                /*SL:473*/this.transformConstantNode(v-1, v, (LdcInsnNode)a1);
            }
            else/*SL:474*/ if (a1 instanceof InvokeDynamicInsnNode) {
                /*SL:475*/this.transformInvokeDynamicNode(v-1, v, (InvokeDynamicInsnNode)a1);
            }
            /*SL:478*/v0 = a1;
        }
    }
    
    private void validateMethod(final MethodNode v2) {
        /*SL:490*/if (Annotations.getInvisible(v2, SoftOverride.class) != null) {
            final ClassInfo.Method a1 = /*EL:491*/this.targetClassInfo.findMethodInHierarchy(v2.name, v2.desc, ClassInfo.SearchType.SUPER_CLASSES_ONLY, ClassInfo.Traversal.SUPER);
            /*SL:493*/if (a1 == null || !a1.isInjected()) {
                /*SL:494*/throw new InvalidMixinException(/*EL:495*/this, "Mixin method " + v2.name + v2.desc + " is tagged with @SoftOverride but no valid method was found in superclasses of " + this.getTarget().getClassName());
            }
        }
    }
    
    private void transformLVT(final MethodNode v2) {
        /*SL:506*/if (v2.localVariables == null) {
            /*SL:507*/return;
        }
        /*SL:510*/for (final LocalVariableNode a1 : v2.localVariables) {
            /*SL:511*/if (a1 != null) {
                if (a1.desc == null) {
                    /*SL:512*/continue;
                }
                /*SL:515*/a1.desc = this.transformSingleDescriptor(Type.getType(a1.desc));
            }
        }
    }
    
    private void transformMethodRef(final MethodNode a3, final Iterator<AbstractInsnNode> v1, final MemberRef v2) {
        /*SL:528*/this.transformDescriptor(v2);
        /*SL:530*/if (v2.getOwner().equals(this.getClassRef())) {
            /*SL:531*/v2.setOwner(this.getTarget().getClassRef());
            final ClassInfo.Method a4 = /*EL:532*/this.getClassInfo().findMethod(v2.getName(), v2.getDesc(), 10);
            /*SL:533*/if (a4 != null && a4.isRenamed() && a4.getOriginalName().equals(v2.getName()) && a4.isSynthetic()) {
                /*SL:534*/v2.setName(a4.getName());
            }
            /*SL:536*/this.upgradeMethodRef(a3, v2, a4);
        }
        else/*SL:537*/ if (this.innerClasses.containsKey(v2.getOwner())) {
            /*SL:538*/v2.setOwner(this.innerClasses.get(v2.getOwner()));
            /*SL:539*/v2.setDesc(this.transformMethodDescriptor(v2.getDesc()));
        }
        else/*SL:540*/ if (this.detachedSuper || this.inheritsFromMixin) {
            /*SL:541*/if (v2.getOpcode() == 183) {
                /*SL:542*/this.updateStaticBinding(a3, v2);
            }
            else/*SL:543*/ if (v2.getOpcode() == 182 && ClassInfo.forName(v2.getOwner()).isMixin()) {
                /*SL:544*/this.updateDynamicBinding(a3, v2);
            }
        }
    }
    
    private void transformFieldRef(final MethodNode v2, final Iterator<AbstractInsnNode> v3, final MemberRef v4) {
        /*SL:561*/if ("super$".equals(v4.getName())) {
            /*SL:562*/if (!(v4 instanceof MemberRef.Field)) {
                /*SL:566*/throw new InvalidMixinException(this.mixin, "Cannot call imaginary super from method handle.");
            }
            this.processImaginarySuper(v2, ((MemberRef.Field)v4).insn);
            v3.remove();
        }
        /*SL:570*/this.transformDescriptor(v4);
        /*SL:572*/if (v4.getOwner().equals(this.getClassRef())) {
            /*SL:573*/v4.setOwner(this.getTarget().getClassRef());
            final ClassInfo.Field a1 = /*EL:575*/this.getClassInfo().findField(v4.getName(), v4.getDesc(), 10);
            /*SL:577*/if (a1 != null && a1.isRenamed() && a1.getOriginalName().equals(v4.getName()) && a1.isStatic()) {
                /*SL:578*/v4.setName(a1.getName());
            }
        }
        else {
            final ClassInfo a2 = /*EL:581*/ClassInfo.forName(v4.getOwner());
            /*SL:582*/if (a2.isMixin()) {
                final ClassInfo a3 = /*EL:583*/this.targetClassInfo.findCorrespondingType(a2);
                /*SL:584*/v4.setOwner((a3 != null) ? a3.getName() : this.getTarget().getClassRef());
            }
        }
    }
    
    private void checkFinal(final MethodNode v2, final Iterator<AbstractInsnNode> v3, final FieldInsnNode v4) {
        /*SL:590*/if (!v4.owner.equals(this.getTarget().getClassRef())) {
            /*SL:591*/return;
        }
        final int v5 = /*EL:594*/v4.getOpcode();
        /*SL:595*/if (v5 == 180 || v5 == 178) {
            /*SL:596*/return;
        }
        /*SL:599*/for (ClassInfo.Field a3 : this.shadowFields.entrySet()) {
            final FieldNode a2 = /*EL:600*/a3.getKey();
            /*SL:601*/if (a2.desc.equals(v4.desc)) {
                if (!a2.name.equals(v4.name)) {
                    /*SL:602*/continue;
                }
                /*SL:604*/a3 = a3.getValue();
                /*SL:605*/if (a3.isDecoratedFinal()) {
                    /*SL:606*/if (a3.isDecoratedMutable()) {
                        /*SL:607*/if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                            MixinTargetContext.logger.warn(/*EL:608*/"Write access to @Mutable @Final field {} in {}::{}", new Object[] { a3, this.mixin, v2.name });
                        }
                    }
                    else/*SL:611*/ if ("<init>".equals(v2.name) || "<clinit>".equals(v2.name)) {
                        MixinTargetContext.logger.warn(/*EL:612*/"@Final field {} in {} should be final", new Object[] { a3, this.mixin });
                    }
                    else {
                        MixinTargetContext.logger.error(/*EL:614*/"Write access detected to @Final field {} in {}::{}", new Object[] { a3, this.mixin, v2.name });
                        /*SL:615*/if (this.mixin.getParent().getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERIFY)) {
                            /*SL:616*/throw new InvalidMixinException(this.mixin, "Write access detected to @Final field " + a3 + " in " + this.mixin + "::" + v2.name);
                        }
                    }
                }
            }
        }
    }
    
    private void transformTypeNode(final MethodNode a3, final Iterator<AbstractInsnNode> a4, final TypeInsnNode v1, final AbstractInsnNode v2) {
        /*SL:639*/if (v1.getOpcode() == 192 && v1.desc.equals(this.getTarget().getClassRef()) && v2.getOpcode() == 25 && ((VarInsnNode)v2).var == 0) {
            /*SL:641*/a4.remove();
            /*SL:642*/return;
        }
        /*SL:645*/if (v1.desc.equals(this.getClassRef())) {
            /*SL:646*/v1.desc = this.getTarget().getClassRef();
        }
        else {
            final String a5 = /*EL:648*/this.innerClasses.get(v1.desc);
            /*SL:649*/if (a5 != null) {
                /*SL:650*/v1.desc = a5;
            }
        }
        /*SL:654*/this.transformDescriptor(v1);
    }
    
    private void transformConstantNode(final MethodNode a1, final Iterator<AbstractInsnNode> a2, final LdcInsnNode a3) {
        /*SL:666*/a3.cst = this.transformConstant(a1, a2, a3.cst);
    }
    
    private void transformInvokeDynamicNode(final MethodNode a3, final Iterator<AbstractInsnNode> v1, final InvokeDynamicInsnNode v2) {
        /*SL:677*/this.requireVersion(51);
        /*SL:678*/v2.desc = this.transformMethodDescriptor(v2.desc);
        /*SL:679*/v2.bsm = this.transformHandle(a3, v1, v2.bsm);
        /*SL:680*/for (int a4 = 0; a4 < v2.bsmArgs.length; ++a4) {
            /*SL:681*/v2.bsmArgs[a4] = this.transformConstant(a3, v1, v2.bsmArgs[a4]);
        }
    }
    
    private Object transformConstant(final MethodNode v1, final Iterator<AbstractInsnNode> v2, final Object v3) {
        /*SL:694*/if (v3 instanceof Type) {
            final Type a1 = /*EL:695*/(Type)v3;
            final String a2 = /*EL:696*/this.transformDescriptor(a1);
            /*SL:697*/if (!a1.toString().equals(a2)) {
                /*SL:698*/return Type.getType(a2);
            }
            /*SL:700*/return v3;
        }
        else {
            /*SL:701*/if (v3 instanceof Handle) {
                /*SL:702*/return this.transformHandle(v1, v2, (Handle)v3);
            }
            /*SL:704*/return v3;
        }
    }
    
    private Handle transformHandle(final MethodNode a1, final Iterator<AbstractInsnNode> a2, final Handle a3) {
        final MemberRef.Handle v1 = /*EL:716*/new MemberRef.Handle(a3);
        /*SL:717*/if (v1.isField()) {
            /*SL:718*/this.transformFieldRef(a1, a2, v1);
        }
        else {
            /*SL:720*/this.transformMethodRef(a1, a2, v1);
        }
        /*SL:722*/return v1.getMethodHandle();
    }
    
    private void processImaginarySuper(final MethodNode v-1, final FieldInsnNode v0) {
        /*SL:738*/if (v0.getOpcode() != 180) {
            /*SL:739*/if ("<init>".equals(v-1.name)) {
                /*SL:740*/throw new InvalidMixinException(this, "Illegal imaginary super declaration: field " + v0.name + " must not specify an initialiser");
            }
            /*SL:744*/throw new InvalidMixinException(this, "Illegal imaginary super access: found " + Bytecode.getOpcodeName(v0.getOpcode()) + " opcode in " + v-1.name + v-1.desc);
        }
        else {
            /*SL:748*/if ((v-1.access & 0x2) != 0x0 || (v-1.access & 0x8) != 0x0) {
                /*SL:749*/throw new InvalidMixinException(this, "Illegal imaginary super access: method " + v-1.name + v-1.desc + " is private or static");
            }
            /*SL:753*/if (Annotations.getInvisible(v-1, SoftOverride.class) == null) {
                /*SL:754*/throw new InvalidMixinException(this, "Illegal imaginary super access: method " + v-1.name + v-1.desc + " is not decorated with @SoftOverride");
            }
            final Iterator<AbstractInsnNode> v = /*EL:758*/v-1.instructions.iterator(v-1.instructions.indexOf(v0));
            while (v.hasNext()) {
                AbstractInsnNode a2 = /*EL:759*/v.next();
                /*SL:760*/if (a2 instanceof MethodInsnNode) {
                    /*SL:761*/a2 = (MethodInsnNode)a2;
                    /*SL:762*/if (a2.owner.equals(this.getClassRef()) && a2.name.equals(v-1.name) && a2.desc.equals(v-1.desc)) {
                        /*SL:763*/a2.setOpcode(183);
                        /*SL:764*/this.updateStaticBinding(v-1, new MemberRef.Method(a2));
                        /*SL:765*/return;
                    }
                    continue;
                }
            }
            /*SL:770*/throw new InvalidMixinException(this, "Illegal imaginary super access: could not find INVOKE for " + v-1.name + v-1.desc);
        }
    }
    
    private void updateStaticBinding(final MethodNode a1, final MemberRef a2) {
        /*SL:781*/this.updateBinding(a1, a2, ClassInfo.Traversal.SUPER);
    }
    
    private void updateDynamicBinding(final MethodNode a1, final MemberRef a2) {
        /*SL:792*/this.updateBinding(a1, a2, ClassInfo.Traversal.ALL);
    }
    
    private void updateBinding(final MethodNode a1, final MemberRef a2, final ClassInfo.Traversal a3) {
        /*SL:797*/if ("<init>".equals(a1.name) || a2.getOwner().equals(this.getTarget().getClassRef()) || this.getTarget().getClassRef().startsWith(/*EL:798*/"<")) {
            /*SL:799*/return;
        }
        final ClassInfo.Method v1 = /*EL:802*/this.targetClassInfo.findMethodInHierarchy(a2.getName(), a2.getDesc(), a3.getSearchType(), /*EL:803*/a3);
        /*SL:804*/if (v1 != null) {
            /*SL:805*/if (v1.getOwner().isMixin()) {
                /*SL:806*/throw new InvalidMixinException(this, "Invalid " + a2 + " in " + this + " resolved " + v1.getOwner() + " but is mixin.");
            }
            /*SL:809*/a2.setOwner(v1.getImplementor().getName());
        }
        else/*SL:810*/ if (ClassInfo.forName(a2.getOwner()).isMixin()) {
            /*SL:811*/throw new MixinTransformerError("Error resolving " + a2 + " in " + this);
        }
    }
    
    public void transformDescriptor(final FieldNode a1) {
        /*SL:821*/if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            /*SL:822*/return;
        }
        /*SL:824*/a1.desc = this.transformSingleDescriptor(a1.desc, false);
    }
    
    public void transformDescriptor(final MethodNode a1) {
        /*SL:833*/if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            /*SL:834*/return;
        }
        /*SL:836*/a1.desc = this.transformMethodDescriptor(a1.desc);
    }
    
    public void transformDescriptor(final MemberRef a1) {
        /*SL:846*/if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            /*SL:847*/return;
        }
        /*SL:849*/if (a1.isField()) {
            /*SL:850*/a1.setDesc(this.transformSingleDescriptor(a1.getDesc(), false));
        }
        else {
            /*SL:852*/a1.setDesc(this.transformMethodDescriptor(a1.getDesc()));
        }
    }
    
    public void transformDescriptor(final TypeInsnNode a1) {
        /*SL:862*/if (!this.inheritsFromMixin && this.innerClasses.size() == 0) {
            /*SL:863*/return;
        }
        /*SL:865*/a1.desc = this.transformSingleDescriptor(a1.desc, true);
    }
    
    private String transformDescriptor(final Type a1) {
        /*SL:869*/if (a1.getSort() == 11) {
            /*SL:870*/return this.transformMethodDescriptor(a1.getDescriptor());
        }
        /*SL:872*/return this.transformSingleDescriptor(a1);
    }
    
    private String transformSingleDescriptor(final Type a1) {
        /*SL:876*/if (a1.getSort() < 9) {
            /*SL:877*/return a1.toString();
        }
        /*SL:880*/return this.transformSingleDescriptor(a1.toString(), false);
    }
    
    private String transformSingleDescriptor(final String a1, boolean a2) {
        String v1 = /*EL:884*/a1;
        /*SL:885*/while (v1.startsWith("[") || v1.startsWith("L")) {
            /*SL:886*/if (v1.startsWith("[")) {
                /*SL:887*/v1 = v1.substring(1);
            }
            else {
                /*SL:890*/v1 = v1.substring(1, v1.indexOf(";"));
                /*SL:891*/a2 = true;
            }
        }
        /*SL:894*/if (!a2) {
            /*SL:895*/return a1;
        }
        final String v2 = /*EL:898*/this.innerClasses.get(v1);
        /*SL:899*/if (v2 != null) {
            /*SL:900*/return a1.replace(v1, v2);
        }
        /*SL:903*/if (this.innerClasses.inverse().containsKey(v1)) {
            /*SL:904*/return a1;
        }
        final ClassInfo v3 = /*EL:907*/ClassInfo.forName(v1);
        /*SL:909*/if (!v3.isMixin()) {
            /*SL:910*/return a1;
        }
        /*SL:913*/return a1.replace(v1, this.findRealType(v3).toString());
    }
    
    private String transformMethodDescriptor(final String v2) {
        final StringBuilder v3 = /*EL:917*/new StringBuilder();
        /*SL:918*/v3.append('(');
        /*SL:919*/for (final Type a1 : Type.getArgumentTypes(v2)) {
            /*SL:920*/v3.append(this.transformSingleDescriptor(a1));
        }
        /*SL:922*/return v3.append(')').append(this.transformSingleDescriptor(Type.getReturnType(v2))).toString();
    }
    
    @Override
    public Target getTargetMethod(final MethodNode a1) {
        /*SL:933*/return this.getTarget().getTargetMethod(a1);
    }
    
    MethodNode findMethod(final MethodNode v1, final AnnotationNode v2) {
        final Deque<String> v3 = /*EL:937*/new LinkedList<String>();
        /*SL:938*/v3.add(v1.name);
        /*SL:939*/if (v2 != null) {
            final List<String> a1 = /*EL:940*/Annotations.<List<String>>getValue(v2, "aliases");
            /*SL:941*/if (a1 != null) {
                /*SL:942*/v3.addAll((Collection<?>)a1);
            }
        }
        /*SL:946*/return this.getTarget().findMethod(v3, v1.desc);
    }
    
    MethodNode findRemappedMethod(final MethodNode a1) {
        final RemapperChain v1 = /*EL:950*/this.getEnvironment().getRemappers();
        final String v2 = /*EL:951*/v1.mapMethodName(this.getTarget().getClassRef(), a1.name, a1.desc);
        /*SL:952*/if (v2.equals(a1.name)) {
            /*SL:953*/return null;
        }
        final Deque<String> v3 = /*EL:956*/new LinkedList<String>();
        /*SL:957*/v3.add(v2);
        /*SL:959*/return this.getTarget().findAliasedMethod(v3, a1.desc);
    }
    
    FieldNode findField(final FieldNode v1, final AnnotationNode v2) {
        final Deque<String> v3 = /*EL:963*/new LinkedList<String>();
        /*SL:964*/v3.add(v1.name);
        /*SL:965*/if (v2 != null) {
            final List<String> a1 = /*EL:966*/Annotations.<List<String>>getValue(v2, "aliases");
            /*SL:967*/if (a1 != null) {
                /*SL:968*/v3.addAll((Collection<?>)a1);
            }
        }
        /*SL:972*/return this.getTarget().findAliasedField(v3, v1.desc);
    }
    
    FieldNode findRemappedField(final FieldNode a1) {
        final RemapperChain v1 = /*EL:976*/this.getEnvironment().getRemappers();
        final String v2 = /*EL:977*/v1.mapFieldName(this.getTarget().getClassRef(), a1.name, a1.desc);
        /*SL:978*/if (v2.equals(a1.name)) {
            /*SL:979*/return null;
        }
        final Deque<String> v3 = /*EL:982*/new LinkedList<String>();
        /*SL:983*/v3.add(v2);
        /*SL:984*/return this.getTarget().findAliasedField(v3, a1.desc);
    }
    
    protected void requireVersion(final int a1) {
        /*SL:994*/this.minRequiredClassVersion = Math.max(this.minRequiredClassVersion, a1);
        /*SL:999*/if (a1 > MixinEnvironment.getCompatibilityLevel().classVersion()) {
            /*SL:1000*/throw new InvalidMixinException(this, "Unsupported mixin class version " + a1);
        }
    }
    
    @Override
    public Extensions getExtensions() {
        /*SL:1009*/return this.targetClass.getExtensions();
    }
    
    @Override
    public IMixinInfo getMixin() {
        /*SL:1017*/return this.mixin;
    }
    
    MixinInfo getInfo() {
        /*SL:1024*/return this.mixin;
    }
    
    @Override
    public int getPriority() {
        /*SL:1034*/return this.mixin.getPriority();
    }
    
    public Set<String> getInterfaces() {
        /*SL:1043*/return this.mixin.getInterfaces();
    }
    
    public Collection<MethodNode> getShadowMethods() {
        /*SL:1052*/return this.shadowMethods;
    }
    
    public List<MethodNode> getMethods() {
        /*SL:1061*/return this.classNode.methods;
    }
    
    public Set<Map.Entry<FieldNode, ClassInfo.Field>> getShadowFields() {
        /*SL:1070*/return this.shadowFields.entrySet();
    }
    
    public List<FieldNode> getFields() {
        /*SL:1079*/return this.classNode.fields;
    }
    
    public Level getLoggingLevel() {
        /*SL:1088*/return this.mixin.getLoggingLevel();
    }
    
    public boolean shouldSetSourceFile() {
        /*SL:1098*/return this.mixin.getParent().shouldSetSourceFile();
    }
    
    public String getSourceFile() {
        /*SL:1107*/return this.classNode.sourceFile;
    }
    
    @Override
    public IReferenceMapper getReferenceMapper() {
        /*SL:1116*/return this.mixin.getParent().getReferenceMapper();
    }
    
    public void preApply(final String a1, final ClassNode a2) {
        /*SL:1126*/this.mixin.preApply(a1, a2);
    }
    
    public void postApply(final String v2, final ClassNode v3) {
        try {
            /*SL:1137*/this.injectorGroups.validateAll();
        }
        catch (InjectionValidationException a2) {
            final InjectorGroupInfo a1 = /*EL:1139*/a2.getGroup();
            /*SL:1140*/throw new InjectionError(/*EL:1141*/String.format("Critical injection failure: Callback group %s in %s failed injection check: %s", a1, this.mixin, a2.getMessage()));
        }
        /*SL:1145*/this.mixin.postApply(v2, v3);
    }
    
    public String getUniqueName(final MethodNode a1, final boolean a2) {
        /*SL:1158*/return this.getTarget().getUniqueName(a1, a2);
    }
    
    public String getUniqueName(final FieldNode a1) {
        /*SL:1169*/return this.getTarget().getUniqueName(a1);
    }
    
    public void prepareInjections() {
        /*SL:1177*/this.injectors.clear();
        /*SL:1179*/for (final MethodNode v0 : this.mergedMethods) {
            final InjectionInfo v = /*EL:1180*/InjectionInfo.parse(this, v0);
            /*SL:1181*/if (v == null) {
                /*SL:1182*/continue;
            }
            /*SL:1185*/if (v.isValid()) {
                /*SL:1186*/v.prepare();
                /*SL:1187*/this.injectors.add(v);
            }
            /*SL:1190*/v0.visibleAnnotations.remove(v.getAnnotation());
        }
    }
    
    public void applyInjections() {
        /*SL:1198*/for (final InjectionInfo v1 : this.injectors) {
            /*SL:1199*/v1.inject();
        }
        /*SL:1202*/for (final InjectionInfo v1 : this.injectors) {
            /*SL:1203*/v1.postInject();
        }
        /*SL:1206*/this.injectors.clear();
    }
    
    public List<MethodNode> generateAccessors() {
        /*SL:1214*/for (final AccessorInfo v1 : this.accessors) {
            /*SL:1215*/v1.locate();
        }
        final List<MethodNode> v2 = /*EL:1218*/new ArrayList<MethodNode>();
        /*SL:1220*/for (final AccessorInfo v3 : this.accessors) {
            final MethodNode v4 = /*EL:1221*/v3.generate();
            /*SL:1222*/this.getTarget().addMixinMethod(v4);
            /*SL:1223*/v2.add(v4);
        }
        /*SL:1226*/return v2;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}
