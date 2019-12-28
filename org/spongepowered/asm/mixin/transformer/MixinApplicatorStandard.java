package org.spongepowered.asm.mixin.transformer;

import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.util.throwables.InvalidConstraintException;
import org.spongepowered.asm.util.throwables.ConstraintViolationException;
import org.spongepowered.asm.util.ITokenProvider;
import org.spongepowered.asm.util.ConstraintParser;
import java.util.Set;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import java.util.HashSet;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import java.util.ArrayDeque;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.Label;
import java.util.Deque;
import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.signature.SignatureVisitor;
import org.spongepowered.asm.lib.signature.SignatureReader;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.util.Map;
import org.spongepowered.asm.util.Bytecode;
import java.util.Iterator;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import java.util.ArrayList;
import java.util.SortedSet;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionClassExporter;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.apache.logging.log4j.Logger;
import java.lang.annotation.Annotation;
import java.util.List;

class MixinApplicatorStandard
{
    protected static final List<Class<? extends Annotation>> CONSTRAINED_ANNOTATIONS;
    protected static final int[] INITIALISER_OPCODE_BLACKLIST;
    protected final Logger logger;
    protected final TargetClassContext context;
    protected final String targetName;
    protected final ClassNode targetClass;
    protected final Profiler profiler;
    protected final boolean mergeSignatures;
    
    MixinApplicatorStandard(final TargetClassContext a1) {
        this.logger = LogManager.getLogger("mixin");
        this.profiler = MixinEnvironment.getProfiler();
        this.context = a1;
        this.targetName = a1.getClassName();
        this.targetClass = a1.getClassNode();
        final ExtensionClassExporter v1 = (ExtensionClassExporter)a1.getExtensions().getExtension((Class)ExtensionClassExporter.class);
        this.mergeSignatures = (v1.isDecompilerActive() && MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE_MERGESIGNATURES));
    }
    
    void apply(final SortedSet<MixinInfo> v-3) {
        final List<MixinTargetContext> list = /*EL:263*/new ArrayList<MixinTargetContext>();
        /*SL:265*/for (final MixinInfo a1 : v-3) {
            /*SL:266*/this.logger.log(a1.getLoggingLevel(), "Mixing {} from {} into {}", new Object[] { a1.getName(), a1.getParent(), this.targetName });
            /*SL:267*/list.add(a1.createContextFor(this.context));
        }
        MixinTargetContext a2 = /*EL:270*/null;
        try {
            /*SL:273*/for (final MixinTargetContext v1 : list) {
                /*SL:274*/(a2 = v1).preApply(this.targetName, this.targetClass);
            }
            /*SL:277*/for (final ApplicatorPass v2 : ApplicatorPass.values()) {
                final Profiler.Section v3 = /*EL:278*/this.profiler.begin("pass", v2.name().toLowerCase());
                /*SL:279*/for (final MixinTargetContext v4 : list) {
                    /*SL:280*/this.applyMixin(a2 = v4, v2);
                }
                /*SL:282*/v3.end();
            }
            /*SL:285*/for (final MixinTargetContext v1 : list) {
                /*SL:286*/(a2 = v1).postApply(this.targetName, this.targetClass);
            }
        }
        catch (InvalidMixinException v5) {
            /*SL:289*/throw v5;
        }
        catch (Exception v6) {
            /*SL:291*/throw new InvalidMixinException(a2, "Unexpecteded " + v6.getClass().getSimpleName() + " whilst applying the mixin class: " + v6.getMessage(), /*EL:292*/v6);
        }
        /*SL:295*/this.applySourceMap(this.context);
        /*SL:296*/this.context.processDebugTasks();
    }
    
    protected final void applyMixin(final MixinTargetContext a1, final ApplicatorPass a2) {
        /*SL:305*/switch (a2) {
            case MAIN: {
                /*SL:307*/this.applySignature(a1);
                /*SL:308*/this.applyInterfaces(a1);
                /*SL:309*/this.applyAttributes(a1);
                /*SL:310*/this.applyAnnotations(a1);
                /*SL:311*/this.applyFields(a1);
                /*SL:312*/this.applyMethods(a1);
                /*SL:313*/this.applyInitialisers(a1);
                /*SL:314*/break;
            }
            case PREINJECT: {
                /*SL:317*/this.prepareInjections(a1);
                /*SL:318*/break;
            }
            case INJECT: {
                /*SL:321*/this.applyAccessors(a1);
                /*SL:322*/this.applyInjections(a1);
                /*SL:323*/break;
            }
            default: {
                /*SL:327*/throw new IllegalStateException("Invalid pass specified " + a2);
            }
        }
    }
    
    protected void applySignature(final MixinTargetContext a1) {
        /*SL:332*/if (this.mergeSignatures) {
            /*SL:333*/this.context.mergeSignature(a1.getSignature());
        }
    }
    
    protected void applyInterfaces(final MixinTargetContext v2) {
        /*SL:343*/for (final String a1 : v2.getInterfaces()) {
            /*SL:344*/if (!this.targetClass.interfaces.contains(a1)) {
                /*SL:345*/this.targetClass.interfaces.add(a1);
                /*SL:346*/v2.getTargetClassInfo().addInterface(a1);
            }
        }
    }
    
    protected void applyAttributes(final MixinTargetContext a1) {
        /*SL:357*/if (a1.shouldSetSourceFile()) {
            /*SL:358*/this.targetClass.sourceFile = a1.getSourceFile();
        }
        /*SL:360*/this.targetClass.version = Math.max(this.targetClass.version, a1.getMinRequiredClassVersion());
    }
    
    protected void applyAnnotations(final MixinTargetContext a1) {
        final ClassNode v1 = /*EL:369*/a1.getClassNode();
        /*SL:370*/Bytecode.mergeAnnotations(v1, this.targetClass);
    }
    
    protected void applyFields(final MixinTargetContext a1) {
        /*SL:382*/this.mergeShadowFields(a1);
        /*SL:383*/this.mergeNewFields(a1);
    }
    
    protected void mergeShadowFields(final MixinTargetContext v-3) {
        /*SL:387*/for (final Map.Entry<FieldNode, ClassInfo.Field> entry : v-3.getShadowFields()) {
            final FieldNode a1 = /*EL:388*/entry.getKey();
            final FieldNode v1 = /*EL:389*/this.findTargetField(a1);
            /*SL:390*/if (v1 != null) {
                /*SL:391*/Bytecode.mergeAnnotations(a1, v1);
                /*SL:394*/if (!entry.getValue().isDecoratedMutable() || Bytecode.hasFlag(v1, 2)) {
                    continue;
                }
                final FieldNode fieldNode = /*EL:395*/v1;
                fieldNode.access &= 0xFFFFFFEF;
            }
        }
    }
    
    protected void mergeNewFields(final MixinTargetContext v-2) {
        /*SL:402*/for (final FieldNode v0 : v-2.getFields()) {
            final FieldNode v = /*EL:403*/this.findTargetField(v0);
            /*SL:404*/if (v == null) {
                /*SL:406*/this.targetClass.fields.add(v0);
                /*SL:408*/if (v0.signature == null) {
                    continue;
                }
                /*SL:409*/if (this.mergeSignatures) {
                    final SignatureVisitor a1 = /*EL:410*/v-2.getSignature().getRemapper();
                    /*SL:411*/new SignatureReader(v0.signature).accept(a1);
                    /*SL:412*/v0.signature = a1.toString();
                }
                else {
                    /*SL:414*/v0.signature = null;
                }
            }
        }
    }
    
    protected void applyMethods(final MixinTargetContext v-1) {
        /*SL:427*/for (final MethodNode a1 : v-1.getShadowMethods()) {
            /*SL:428*/this.applyShadowMethod(v-1, a1);
        }
        /*SL:431*/for (final MethodNode v1 : v-1.getMethods()) {
            /*SL:432*/this.applyNormalMethod(v-1, v1);
        }
    }
    
    protected void applyShadowMethod(final MixinTargetContext a1, final MethodNode a2) {
        final MethodNode v1 = /*EL:437*/this.findTargetMethod(a2);
        /*SL:438*/if (v1 != null) {
            /*SL:439*/Bytecode.mergeAnnotations(a2, v1);
        }
    }
    
    protected void applyNormalMethod(final MixinTargetContext a1, final MethodNode a2) {
        /*SL:445*/a1.transformMethod(a2);
        /*SL:447*/if (!a2.name.startsWith("<")) {
            /*SL:448*/this.checkMethodVisibility(a1, a2);
            /*SL:449*/this.checkMethodConstraints(a1, a2);
            /*SL:450*/this.mergeMethod(a1, a2);
        }
        else/*SL:451*/ if ("<clinit>".equals(a2.name)) {
            /*SL:453*/this.appendInsns(a1, a2);
        }
    }
    
    protected void mergeMethod(final MixinTargetContext v2, final MethodNode v3) {
        final boolean v4 = /*EL:464*/Annotations.getVisible(v3, Overwrite.class) != null;
        final MethodNode v5 = /*EL:465*/this.findTargetMethod(v3);
        /*SL:467*/if (v5 != null) {
            /*SL:468*/if (this.isAlreadyMerged(v2, v3, v4, v5)) {
                /*SL:469*/return;
            }
            final AnnotationNode a1 = /*EL:472*/Annotations.getInvisible(v3, Intrinsic.class);
            /*SL:473*/if (a1 != null) {
                /*SL:474*/if (this.mergeIntrinsic(v2, v3, v4, v5, a1)) {
                    /*SL:475*/v2.getTarget().methodMerged(v3);
                    /*SL:476*/return;
                }
            }
            else {
                /*SL:479*/if (v2.requireOverwriteAnnotations() && !v4) {
                    /*SL:480*/throw new InvalidMixinException(/*EL:481*/v2, String.format("%s%s in %s cannot overwrite method in %s because @Overwrite is required by the parent configuration", v3.name, v3.desc, v2, v2.getTarget().getClassName()));
                }
                /*SL:485*/this.targetClass.methods.remove(v5);
            }
        }
        else/*SL:487*/ if (v4) {
            /*SL:488*/throw new InvalidMixinException(v2, String.format("Overwrite target \"%s\" was not located in target class %s", v3.name, v2.getTargetClassRef()));
        }
        /*SL:492*/this.targetClass.methods.add(v3);
        /*SL:493*/v2.methodMerged(v3);
        /*SL:495*/if (v3.signature != null) {
            /*SL:496*/if (this.mergeSignatures) {
                final SignatureVisitor a2 = /*EL:497*/v2.getSignature().getRemapper();
                /*SL:498*/new SignatureReader(v3.signature).accept(a2);
                /*SL:499*/v3.signature = a2.toString();
            }
            else {
                /*SL:501*/v3.signature = null;
            }
        }
    }
    
    protected boolean isAlreadyMerged(final MixinTargetContext a1, final MethodNode a2, final boolean a3, final MethodNode a4) {
        final AnnotationNode v1 = /*EL:518*/Annotations.getVisible(a4, MixinMerged.class);
        /*SL:519*/if (v1 == null) {
            /*SL:520*/if (Annotations.getVisible(a4, Final.class) != null) {
                /*SL:521*/this.logger.warn("Overwrite prohibited for @Final method {} in {}. Skipping method.", new Object[] { a2.name, a1 });
                /*SL:522*/return true;
            }
            /*SL:524*/return false;
        }
        else {
            final String v2 = /*EL:527*/Annotations.<String>getValue(v1, "sessionId");
            /*SL:529*/if (!this.context.getSessionId().equals(v2)) {
                /*SL:530*/throw new ClassFormatError("Invalid @MixinMerged annotation found in" + a1 + " at " + a2.name + " in " + this.targetClass.name);
            }
            /*SL:533*/if (Bytecode.hasFlag(a4, 4160) && /*EL:534*/Bytecode.hasFlag(a2, 4160)) {
                /*SL:535*/if (a1.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                    /*SL:536*/this.logger.warn("Synthetic bridge method clash for {} in {}", new Object[] { a2.name, a1 });
                }
                /*SL:538*/return true;
            }
            final String v3 = /*EL:541*/Annotations.<String>getValue(v1, "mixin");
            final int v4 = /*EL:542*/Annotations.<Integer>getValue(v1, "priority");
            /*SL:544*/if (v4 >= a1.getPriority() && !v3.equals(a1.getClassName())) {
                /*SL:545*/this.logger.warn("Method overwrite conflict for {} in {}, previously written by {}. Skipping method.", new Object[] { a2.name, a1, v3 });
                /*SL:546*/return true;
            }
            /*SL:549*/if (Annotations.getVisible(a4, Final.class) != null) {
                /*SL:550*/this.logger.warn("Method overwrite conflict for @Final method {} in {} declared by {}. Skipping method.", new Object[] { a2.name, a1, v3 });
                /*SL:551*/return true;
            }
            /*SL:554*/return false;
        }
    }
    
    protected boolean mergeIntrinsic(final MixinTargetContext a3, final MethodNode a4, final boolean a5, final MethodNode v1, final AnnotationNode v2) {
        /*SL:573*/if (a5) {
            /*SL:574*/throw new InvalidMixinException(a3, "@Intrinsic is not compatible with @Overwrite, remove one of these annotations on " + a4.name + " in " + a3);
        }
        final String v3 = /*EL:578*/a4.name + a4.desc;
        /*SL:579*/if (Bytecode.hasFlag(a4, 8)) {
            /*SL:580*/throw new InvalidMixinException(a3, "@Intrinsic method cannot be static, found " + v3 + " in " + a3);
        }
        /*SL:583*/if (!Bytecode.hasFlag(a4, 4096)) {
            final AnnotationNode a6 = /*EL:584*/Annotations.getVisible(a4, MixinRenamed.class);
            /*SL:585*/if (a6 == null || !Annotations.<Boolean>getValue(a6, "isInterfaceMember", Boolean.FALSE)) {
                /*SL:586*/throw new InvalidMixinException(a3, "@Intrinsic method must be prefixed interface method, no rename encountered on " + v3 + " in " + a3);
            }
        }
        /*SL:591*/if (!Annotations.<Boolean>getValue(v2, "displace", Boolean.FALSE)) {
            /*SL:592*/this.logger.log(a3.getLoggingLevel(), "Skipping Intrinsic mixin method {} for {}", new Object[] { v3, a3.getTargetClassRef() });
            /*SL:593*/return true;
        }
        /*SL:596*/this.displaceIntrinsic(a3, a4, v1);
        /*SL:597*/return false;
    }
    
    protected void displaceIntrinsic(final MixinTargetContext v2, final MethodNode v3, final MethodNode v4) {
        final String v5 = /*EL:610*/"proxy+" + v4.name;
        /*SL:612*/for (final AbstractInsnNode a2 : v3.instructions) {
            /*SL:614*/if (a2 instanceof MethodInsnNode && a2.getOpcode() != 184) {
                final Iterator<AbstractInsnNode> a3 = /*EL:615*/(Iterator<AbstractInsnNode>)a2;
                /*SL:616*/if (!a3.owner.equals(this.targetClass.name) || !a3.name.equals(v4.name) || !a3.desc.equals(v4.desc)) {
                    continue;
                }
                /*SL:617*/a3.name = v5;
            }
        }
        /*SL:622*/v4.name = v5;
    }
    
    protected final void appendInsns(final MixinTargetContext v-2, final MethodNode v-1) {
        /*SL:633*/if (Type.getReturnType(v-1.desc) != Type.VOID_TYPE) {
            /*SL:634*/throw new IllegalArgumentException("Attempted to merge insns from a method which does not return void");
        }
        final MethodNode v0 = /*EL:637*/this.findTargetMethod(v-1);
        /*SL:639*/if (v0 != null) {
            final AbstractInsnNode v = /*EL:640*/Bytecode.findInsn(v0, 177);
            /*SL:642*/if (v != null) {
                Iterator<AbstractInsnNode> a2 = /*EL:643*/v-1.instructions.iterator();
                /*SL:644*/while (a2.hasNext()) {
                    /*SL:645*/a2 = a2.next();
                    /*SL:646*/if (!(a2 instanceof LineNumberNode) && a2.getOpcode() != 177) {
                        /*SL:647*/v0.instructions.insertBefore(v, a2);
                    }
                }
                /*SL:651*/v0.maxLocals = Math.max(v0.maxLocals, v-1.maxLocals);
                /*SL:652*/v0.maxStack = Math.max(v0.maxStack, v-1.maxStack);
            }
            /*SL:655*/return;
        }
        /*SL:658*/this.targetClass.methods.add(v-1);
    }
    
    protected void applyInitialisers(final MixinTargetContext v2) {
        final MethodNode v3 = /*EL:669*/this.getConstructor(v2);
        /*SL:670*/if (v3 == null) {
            /*SL:671*/return;
        }
        final Deque<AbstractInsnNode> v4 = /*EL:675*/this.getInitialiser(v2, v3);
        /*SL:676*/if (v4 == null || v4.size() == 0) {
            /*SL:677*/return;
        }
        /*SL:681*/for (final MethodNode a1 : this.targetClass.methods) {
            /*SL:682*/if ("<init>".equals(a1.name)) {
                /*SL:683*/a1.maxStack = Math.max(a1.maxStack, v3.maxStack);
                /*SL:684*/this.injectInitialiser(v2, a1, v4);
            }
        }
    }
    
    protected MethodNode getConstructor(final MixinTargetContext v2) {
        MethodNode v3 = /*EL:696*/null;
        /*SL:698*/for (final MethodNode a1 : v2.getMethods()) {
            /*SL:699*/if ("<init>".equals(a1.name) && Bytecode.methodHasLineNumbers(a1)) {
                /*SL:700*/if (v3 == null) {
                    /*SL:701*/v3 = a1;
                }
                else {
                    /*SL:704*/this.logger.warn(String.format("Mixin %s has multiple constructors, %s was selected\n", v2, v3.desc));
                }
            }
        }
        /*SL:709*/return v3;
    }
    
    private Range getConstructorRange(final MethodNode v-6) {
        boolean b = /*EL:721*/false;
        AbstractInsnNode abstractInsnNode = /*EL:722*/null;
        int line = /*EL:724*/0;
        int n = 0;
        int a2 = 0;
        int v0 = -1;
        /*SL:725*/for (final AbstractInsnNode a1 : v-6.instructions) {
            /*SL:727*/if (a1 instanceof LineNumberNode) {
                /*SL:728*/line = ((LineNumberNode)a1).line;
                /*SL:729*/b = true;
            }
            else/*SL:730*/ if (a1 instanceof MethodInsnNode) {
                /*SL:731*/if (a1.getOpcode() != 183 || !"<init>".equals(((MethodInsnNode)a1).name) || v0 != -1) {
                    continue;
                }
                /*SL:732*/v0 = v-6.instructions.indexOf(a1);
                /*SL:733*/n = line;
            }
            else/*SL:735*/ if (a1.getOpcode() == 181) {
                /*SL:736*/b = false;
            }
            else {
                /*SL:737*/if (a1.getOpcode() != 177) {
                    continue;
                }
                /*SL:738*/if (b) {
                    /*SL:739*/a2 = line;
                }
                else {
                    /*SL:741*/a2 = n;
                    /*SL:742*/abstractInsnNode = a1;
                }
            }
        }
        /*SL:747*/if (abstractInsnNode != null) {
            final LabelNode v2 = /*EL:748*/new LabelNode(new Label());
            /*SL:749*/v-6.instructions.insertBefore(abstractInsnNode, v2);
            /*SL:750*/v-6.instructions.insertBefore(abstractInsnNode, new LineNumberNode(n, v2));
        }
        /*SL:753*/return new Range(n, a2, v0);
    }
    
    protected final Deque<AbstractInsnNode> getInitialiser(final MixinTargetContext v-9, final MethodNode v-8) {
        final Range constructorRange = /*EL:771*/this.getConstructorRange(v-8);
        /*SL:772*/if (!constructorRange.isValid()) {
            /*SL:773*/return null;
        }
        int line = /*EL:777*/0;
        final Deque<AbstractInsnNode> deque = /*EL:778*/new ArrayDeque<AbstractInsnNode>();
        boolean excludes = /*EL:779*/false;
        int n = /*EL:780*/-1;
        LabelNode labelNode = /*EL:781*/null;
        final Iterator<AbstractInsnNode> iterator = /*EL:782*/v-8.instructions.iterator(constructorRange.marker);
        while (iterator.hasNext()) {
            final AbstractInsnNode v0 = /*EL:783*/iterator.next();
            /*SL:784*/if (v0 instanceof LineNumberNode) {
                /*SL:785*/line = ((LineNumberNode)v0).line;
                final AbstractInsnNode a1 = /*EL:786*/v-8.instructions.get(v-8.instructions.indexOf(v0) + 1);
                /*SL:787*/if (line == constructorRange.end && a1.getOpcode() != 177) {
                    /*SL:788*/excludes = true;
                    /*SL:789*/n = 177;
                }
                else {
                    /*SL:791*/excludes = constructorRange.excludes(line);
                    /*SL:792*/n = -1;
                }
            }
            else {
                /*SL:794*/if (!excludes) {
                    continue;
                }
                /*SL:795*/if (labelNode != null) {
                    /*SL:796*/deque.add(labelNode);
                    /*SL:797*/labelNode = null;
                }
                /*SL:800*/if (v0 instanceof LabelNode) {
                    /*SL:801*/labelNode = (LabelNode)v0;
                }
                else {
                    final int v = /*EL:803*/v0.getOpcode();
                    /*SL:804*/if (v == n) {
                        /*SL:805*/n = -1;
                    }
                    else {
                        /*SL:808*/for (final int a2 : MixinApplicatorStandard.INITIALISER_OPCODE_BLACKLIST) {
                            /*SL:809*/if (v == a2) {
                                /*SL:812*/throw new InvalidMixinException(/*EL:813*/v-9, "Cannot handle " + Bytecode.getOpcodeName(v) + " opcode (0x" + Integer.toHexString(v).toUpperCase() + ") in class initialiser");
                            }
                        }
                        /*SL:817*/deque.add(v0);
                    }
                }
            }
        }
        final AbstractInsnNode abstractInsnNode = /*EL:823*/deque.peekLast();
        /*SL:824*/if (abstractInsnNode != null && /*EL:825*/abstractInsnNode.getOpcode() != 181) {
            /*SL:826*/throw new InvalidMixinException(/*EL:827*/v-9, "Could not parse initialiser, expected 0xB5, found 0x" + Integer.toHexString(abstractInsnNode.getOpcode()) + " in " + v-9);
        }
        /*SL:831*/return deque;
    }
    
    protected final void injectInitialiser(final MixinTargetContext v1, final MethodNode v2, final Deque<AbstractInsnNode> v3) {
        final Map<LabelNode, LabelNode> v4 = /*EL:842*/Bytecode.cloneLabels(v2.instructions);
        AbstractInsnNode v5 = /*EL:844*/this.findInitialiserInjectionPoint(v1, v2, v3);
        /*SL:845*/if (v5 == null) {
            /*SL:846*/this.logger.warn("Failed to locate initialiser injection point in <init>{}, initialiser was not mixed in.", new Object[] { v2.desc });
            /*SL:847*/return;
        }
        /*SL:850*/for (AbstractInsnNode a2 : v3) {
            /*SL:851*/if (a2 instanceof LabelNode) {
                /*SL:852*/continue;
            }
            /*SL:854*/if (a2 instanceof JumpInsnNode) {
                /*SL:855*/throw new InvalidMixinException(v1, "Unsupported JUMP opcode in initialiser in " + v1);
            }
            /*SL:857*/a2 = a2.clone(v4);
            /*SL:858*/v2.instructions.insert(v5, a2);
            /*SL:859*/v5 = a2;
        }
    }
    
    protected AbstractInsnNode findInitialiserInjectionPoint(final MixinTargetContext v-8, final MethodNode v-7, final Deque<AbstractInsnNode> v-6) {
        final Set<String> set = /*EL:873*/new HashSet<String>();
        /*SL:874*/for (final AbstractInsnNode a1 : v-6) {
            /*SL:875*/if (a1.getOpcode() == 181) {
                /*SL:876*/set.add(fieldKey((FieldInsnNode)a1));
            }
        }
        final InitialiserInjectionMode initialiserInjectionMode = /*EL:880*/this.getInitialiserInjectionMode(v-8.getEnvironment());
        final String name = /*EL:881*/v-8.getTargetClassInfo().getName();
        final String superName = /*EL:882*/v-8.getTargetClassInfo().getSuperName();
        AbstractInsnNode abstractInsnNode = /*EL:883*/null;
        /*SL:885*/for (final AbstractInsnNode v : v-7.instructions) {
            /*SL:887*/if (v.getOpcode() == 183 && "<init>".equals(((MethodInsnNode)v).name)) {
                final String a2 = /*EL:888*/((MethodInsnNode)v).owner;
                /*SL:889*/if (!a2.equals(name) && !a2.equals(superName)) {
                    continue;
                }
                /*SL:890*/abstractInsnNode = v;
                /*SL:891*/if (initialiserInjectionMode == InitialiserInjectionMode.SAFE) {
                    /*SL:892*/break;
                }
                continue;
            }
            else {
                /*SL:895*/if (v.getOpcode() != 181 || initialiserInjectionMode != InitialiserInjectionMode.DEFAULT) {
                    continue;
                }
                final String a3 = fieldKey(/*EL:896*/(FieldInsnNode)v);
                /*SL:897*/if (!set.contains(a3)) {
                    continue;
                }
                /*SL:898*/abstractInsnNode = v;
            }
        }
        /*SL:903*/return abstractInsnNode;
    }
    
    private InitialiserInjectionMode getInitialiserInjectionMode(final MixinEnvironment v2) {
        final String v3 = /*EL:907*/v2.getOptionValue(MixinEnvironment.Option.INITIALISER_INJECTION_MODE);
        /*SL:908*/if (v3 == null) {
            /*SL:909*/return InitialiserInjectionMode.DEFAULT;
        }
        try {
            /*SL:912*/return InitialiserInjectionMode.valueOf(v3.toUpperCase());
        }
        catch (Exception a1) {
            /*SL:914*/this.logger.warn("Could not parse unexpected value \"{}\" for mixin.initialiserInjectionMode, reverting to DEFAULT", new Object[] { v3 });
            /*SL:915*/return InitialiserInjectionMode.DEFAULT;
        }
    }
    
    private static String fieldKey(final FieldInsnNode a1) {
        /*SL:920*/return String.format("%s:%s", a1.desc, a1.name);
    }
    
    protected void prepareInjections(final MixinTargetContext a1) {
        /*SL:929*/a1.prepareInjections();
    }
    
    protected void applyInjections(final MixinTargetContext a1) {
        /*SL:938*/a1.applyInjections();
    }
    
    protected void applyAccessors(final MixinTargetContext v2) {
        final List<MethodNode> v3 = /*EL:947*/v2.generateAccessors();
        /*SL:948*/for (final MethodNode a1 : v3) {
            /*SL:949*/if (!a1.name.startsWith("<")) {
                /*SL:950*/this.mergeMethod(v2, a1);
            }
        }
    }
    
    protected void checkMethodVisibility(final MixinTargetContext a1, final MethodNode a2) {
        /*SL:964*/if (Bytecode.hasFlag(a2, 8) && !Bytecode.hasFlag(a2, 2) && !Bytecode.hasFlag(a2, 4096) && /*EL:965*/Annotations.getVisible(a2, Overwrite.class) == null) {
            /*SL:966*/throw new InvalidMixinException(/*EL:967*/a1, String.format("Mixin %s contains non-private static method %s", a1, a2));
        }
    }
    
    protected void applySourceMap(final TargetClassContext a1) {
        /*SL:972*/this.targetClass.sourceDebug = a1.getSourceMap().toString();
    }
    
    protected void checkMethodConstraints(final MixinTargetContext v2, final MethodNode v3) {
        /*SL:982*/for (AnnotationNode a2 : MixinApplicatorStandard.CONSTRAINED_ANNOTATIONS) {
            /*SL:983*/a2 = Annotations.getVisible(v3, a2);
            /*SL:984*/if (a2 != null) {
                /*SL:985*/this.checkConstraints(v2, v3, a2);
            }
        }
    }
    
    protected final void checkConstraints(final MixinTargetContext v-2, final MethodNode v-1, final AnnotationNode v0) {
        try {
            final ConstraintParser.Constraint a3 = /*EL:1000*/ConstraintParser.parse(v0);
            try {
                /*SL:1002*/a3.check(v-2.getEnvironment());
            }
            catch (ConstraintViolationException a3) {
                final String a2 = /*EL:1004*/String.format("Constraint violation: %s on %s in %s", a3.getMessage(), v-1, v-2);
                /*SL:1005*/this.logger.warn(a2);
                /*SL:1006*/if (!v-2.getEnvironment().getOption(MixinEnvironment.Option.IGNORE_CONSTRAINTS)) {
                    /*SL:1007*/throw new InvalidMixinException(v-2, a2, a3);
                }
            }
        }
        catch (InvalidConstraintException v) {
            /*SL:1011*/throw new InvalidMixinException(v-2, v.getMessage());
        }
    }
    
    protected final MethodNode findTargetMethod(final MethodNode v2) {
        /*SL:1022*/for (final MethodNode a1 : this.targetClass.methods) {
            /*SL:1023*/if (a1.name.equals(v2.name) && a1.desc.equals(v2.desc)) {
                /*SL:1024*/return a1;
            }
        }
        /*SL:1028*/return null;
    }
    
    protected final FieldNode findTargetField(final FieldNode v2) {
        /*SL:1038*/for (final FieldNode a1 : this.targetClass.fields) {
            /*SL:1039*/if (a1.name.equals(v2.name)) {
                /*SL:1040*/return a1;
            }
        }
        /*SL:1044*/return null;
    }
    
    static {
        CONSTRAINED_ANNOTATIONS = ImmutableList.<Class<Overwrite>>of(Overwrite.class, (Class<Overwrite>)Inject.class, (Class<Overwrite>)ModifyArg.class, (Class<Overwrite>)ModifyArgs.class, (Class<Overwrite>)Redirect.class, (Class<Overwrite>)ModifyVariable.class, (Class<Overwrite>)ModifyConstant.class);
        INITIALISER_OPCODE_BLACKLIST = new int[] { 177, 21, 22, 23, 24, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 79, 80, 81, 82, 83, 84, 85, 86 };
    }
    
    enum ApplicatorPass
    {
        MAIN, 
        PREINJECT, 
        INJECT;
    }
    
    enum InitialiserInjectionMode
    {
        DEFAULT, 
        SAFE;
    }
    
    class Range
    {
        final int start;
        final int end;
        final int marker;
        
        Range(final int a2, final int a3, final int a4) {
            this.start = a2;
            this.end = a3;
            this.marker = a4;
        }
        
        boolean isValid() {
            /*SL:169*/return this.start != 0 && this.end != 0 && this.end >= this.start;
        }
        
        boolean contains(final int a1) {
            /*SL:179*/return a1 >= this.start && a1 <= this.end;
        }
        
        boolean excludes(final int a1) {
            /*SL:188*/return a1 < this.start || a1 > this.end;
        }
        
        @Override
        public String toString() {
            /*SL:196*/return String.format("Range[%d-%d,%d,valid=%s)", this.start, this.end, this.marker, this.isValid());
        }
    }
}
