package org.spongepowered.asm.mixin.injection.callback;

import org.spongepowered.asm.mixin.injection.Coerce;
import java.util.ArrayList;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.util.SignaturePrinter;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.util.Locals;
import org.spongepowered.asm.util.Bytecode;
import com.google.common.base.Strings;
import java.util.Set;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.points.BeforeReturn;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import java.util.HashMap;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Map;
import org.spongepowered.asm.mixin.injection.code.Injector;

public class CallbackInjector extends Injector
{
    private final boolean cancellable;
    private final LocalCapture localCapture;
    private final String identifier;
    private final Map<Integer, String> ids;
    private int totalInjections;
    private int callbackInfoVar;
    private String lastId;
    private String lastDesc;
    private Target lastTarget;
    private String callbackInfoClass;
    
    public CallbackInjector(final InjectionInfo a1, final boolean a2, final LocalCapture a3, final String a4) {
        super(a1);
        this.ids = new HashMap<Integer, String>();
        this.totalInjections = 0;
        this.callbackInfoVar = -1;
        this.cancellable = a2;
        this.localCapture = a3;
        this.identifier = a4;
    }
    
    @Override
    protected void sanityCheck(final Target v1, final List<InjectionPoint> v2) {
        /*SL:367*/super.sanityCheck(v1, v2);
        /*SL:369*/if (v1.isStatic != this.isStatic) {
            /*SL:370*/throw new InvalidInjectionException(this.info, "'static' modifier of callback method does not match target in " + this);
        }
        /*SL:373*/if ("<init>".equals(v1.method.name)) {
            /*SL:374*/for (final InjectionPoint a1 : v2) {
                /*SL:375*/if (!a1.getClass().equals(BeforeReturn.class)) {
                    /*SL:376*/throw new InvalidInjectionException(this.info, "Found injection point type " + a1.getClass().getSimpleName() + " targetting a ctor in " + this + ". Only RETURN allowed for a ctor target");
                }
            }
        }
    }
    
    @Override
    protected void addTargetNode(final Target v1, final List<InjectionNodes.InjectionNode> v2, final AbstractInsnNode v3, final Set<InjectionPoint> v4) {
        final InjectionNodes.InjectionNode v5 = /*EL:390*/v1.addInjectionNode(v3);
        /*SL:392*/for (String a3 : v4) {
            final String a2 = /*EL:393*/a3.getId();
            /*SL:394*/if (Strings.isNullOrEmpty(a2)) {
                /*SL:395*/continue;
            }
            /*SL:398*/a3 = this.ids.get(v5.getId());
            /*SL:399*/if (a3 != null && !a3.equals(a2)) {
                Injector.logger.warn(/*EL:400*/"Conflicting id for {} insn in {}, found id {} on {}, previously defined as {}", new Object[] { Bytecode.getOpcodeName(v3), v1.toString(), /*EL:401*/a2, this.info, a3 });
                /*SL:402*/break;
            }
            /*SL:405*/this.ids.put(v5.getId(), a2);
        }
        /*SL:408*/v2.add(v5);
        /*SL:409*/++this.totalInjections;
    }
    
    @Override
    protected void inject(final Target a1, final InjectionNodes.InjectionNode a2) {
        LocalVariableNode[] v1 = /*EL:419*/null;
        /*SL:421*/if (this.localCapture.isCaptureLocals() || this.localCapture.isPrintLocals()) {
            /*SL:422*/v1 = Locals.getLocalsAt(this.classNode, a1.method, a2.getCurrentTarget());
        }
        /*SL:425*/this.inject(new Callback(this.methodNode, a1, a2, v1, this.localCapture.isCaptureLocals()));
    }
    
    private void inject(final Callback v-1) {
        /*SL:434*/if (this.localCapture.isPrintLocals()) {
            /*SL:435*/this.printLocals(v-1);
            /*SL:436*/this.info.addCallbackInvocation(this.methodNode);
            /*SL:437*/return;
        }
        MethodNode v0 = /*EL:443*/this.methodNode;
        /*SL:445*/if (!v-1.checkDescriptor(this.methodNode.desc)) {
            /*SL:446*/if (this.info.getTargets().size() > 1) {
                /*SL:447*/return;
            }
            /*SL:450*/if (v-1.canCaptureLocals) {
                final MethodNode v = /*EL:456*/Bytecode.findMethod(this.classNode, this.methodNode.name, v-1.getDescriptor());
                /*SL:457*/if (v != null && Annotations.getVisible(v, Surrogate.class) != null) {
                    /*SL:459*/v0 = v;
                }
                else {
                    final String a1 = /*EL:462*/this.generateBadLVTMessage(v-1);
                    /*SL:464*/switch (this.localCapture) {
                        case CAPTURE_FAILEXCEPTION: {
                            Injector.logger.error(/*EL:466*/"Injection error: {}", new Object[] { a1 });
                            /*SL:467*/v0 = this.generateErrorMethod(v-1, "org/spongepowered/asm/mixin/injection/throwables/InjectionError", a1);
                            /*SL:469*/break;
                        }
                        case CAPTURE_FAILSOFT: {
                            Injector.logger.warn(/*EL:471*/"Injection warning: {}", new Object[] { a1 });
                            /*SL:472*/return;
                        }
                        default: {
                            Injector.logger.error(/*EL:474*/"Critical injection failure: {}", new Object[] { a1 });
                            /*SL:475*/throw new InjectionError(a1);
                        }
                    }
                }
            }
            else {
                final String v2 = /*EL:480*/this.methodNode.desc.replace("Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;", "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;");
                /*SL:484*/if (v-1.checkDescriptor(v2)) {
                    /*SL:487*/throw new InvalidInjectionException(this.info, "Invalid descriptor on " + this.info + "! CallbackInfoReturnable is required!");
                }
                final MethodNode v3 = /*EL:490*/Bytecode.findMethod(this.classNode, this.methodNode.name, v-1.getDescriptor());
                /*SL:491*/if (v3 == null || Annotations.getVisible(v3, Surrogate.class) == null) {
                    /*SL:495*/throw new InvalidInjectionException(this.info, "Invalid descriptor on " + this.info + "! Expected " + v-1.getDescriptor() + " but found " + this.methodNode.desc);
                }
                v0 = v3;
            }
        }
        /*SL:501*/this.dupReturnValue(v-1);
        /*SL:502*/if (this.cancellable || this.totalInjections > 1) {
            /*SL:503*/this.createCallbackInfo(v-1, true);
        }
        /*SL:505*/this.invokeCallback(v-1, v0);
        /*SL:506*/this.injectCancellationCode(v-1);
        /*SL:508*/v-1.inject();
        /*SL:509*/this.info.notifyInjected(v-1.target);
    }
    
    private String generateBadLVTMessage(final Callback a1) {
        final int v1 = /*EL:519*/a1.target.indexOf(a1.node);
        final List<String> v2 = summariseLocals(/*EL:520*/this.methodNode.desc, a1.target.arguments.length + 1);
        final List<String> v3 = summariseLocals(/*EL:521*/a1.getDescriptorWithAllLocals(), a1.frameSize);
        /*SL:522*/return String.format("LVT in %s has incompatible changes at opcode %d in callback %s.\nExpected: %s\n   Found: %s", a1.target, v1, /*EL:523*/this, v2, v3);
    }
    
    private MethodNode generateErrorMethod(final Callback a1, final String a2, final String a3) {
        final MethodNode v1 = /*EL:535*/this.info.addMethod(this.methodNode.access, this.methodNode.name + "$missing", a1.getDescriptor());
        /*SL:536*/v1.maxLocals = Bytecode.getFirstNonArgLocalIndex(Type.getArgumentTypes(a1.getDescriptor()), !this.isStatic);
        /*SL:537*/v1.maxStack = 3;
        final InsnList v2 = /*EL:538*/v1.instructions;
        /*SL:539*/v2.add(new TypeInsnNode(187, a2));
        /*SL:540*/v2.add(new InsnNode(89));
        /*SL:541*/v2.add(new LdcInsnNode(a3));
        /*SL:542*/v2.add(new MethodInsnNode(183, a2, "<init>", "(Ljava/lang/String;)V", false));
        /*SL:543*/v2.add(new InsnNode(191));
        /*SL:544*/return v1;
    }
    
    private void printLocals(final Callback v-5) {
        final Type[] argumentTypes = /*EL:553*/Type.getArgumentTypes(v-5.getDescriptorWithAllLocals());
        final SignaturePrinter a2 = /*EL:554*/new SignaturePrinter(v-5.target.method, v-5.argNames);
        final SignaturePrinter signaturePrinter = /*EL:555*/new SignaturePrinter(this.methodNode.name, v-5.target.returnType, argumentTypes, v-5.argNames);
        /*SL:556*/signaturePrinter.setModifiers(this.methodNode);
        final PrettyPrinter prettyPrinter = /*EL:558*/new PrettyPrinter();
        /*SL:559*/prettyPrinter.kv("Target Class", (Object)this.classNode.name.replace('/', '.'));
        /*SL:560*/prettyPrinter.kv("Target Method", a2);
        /*SL:561*/prettyPrinter.kv("Target Max LOCALS", v-5.target.getMaxLocals());
        /*SL:562*/prettyPrinter.kv("Initial Frame Size", v-5.frameSize);
        /*SL:563*/prettyPrinter.kv("Callback Name", (Object)this.methodNode.name);
        /*SL:564*/prettyPrinter.kv("Instruction", "%s %s", v-5.node.getClass().getSimpleName(), /*EL:565*/Bytecode.getOpcodeName(v-5.node.getCurrentTarget().getOpcode()));
        /*SL:566*/prettyPrinter.hr();
        /*SL:567*/if (v-5.locals.length > v-5.frameSize) {
            /*SL:568*/prettyPrinter.add("  %s  %20s  %s", "LOCAL", "TYPE", "NAME");
            /*SL:569*/for (int v0 = 0; v0 < v-5.locals.length; ++v0) {
                final String v = /*EL:570*/(v0 == v-5.frameSize) ? ">" : " ";
                /*SL:571*/if (v-5.locals[v0] != null) {
                    /*SL:572*/prettyPrinter.add("%s [%3d]  %20s  %-50s %s", v, v0, SignaturePrinter.getTypeName(v-5.localTypes[v0], false), meltSnowman(v0, v-5.locals[v0].name), /*EL:573*/(v0 >= v-5.frameSize) ? "<capture>" : "");
                }
                else {
                    final boolean a1 = /*EL:575*/v0 > 0 && v-5.localTypes[v0 - 1] != null && v-5.localTypes[v0 - 1].getSize() > 1;
                    /*SL:576*/prettyPrinter.add("%s [%3d]  %20s", v, v0, a1 ? "<top>" : "-");
                }
            }
            /*SL:579*/prettyPrinter.hr();
        }
        /*SL:581*/prettyPrinter.add().add("/**").add(" * Expected callback signature").add(" * /");
        /*SL:582*/prettyPrinter.add("%s {", signaturePrinter);
        /*SL:583*/prettyPrinter.add("    // Method body").add("}").add().print(System.err);
    }
    
    private void createCallbackInfo(final Callback a1, final boolean a2) {
        /*SL:592*/if (a1.target != this.lastTarget) {
            /*SL:593*/this.lastId = null;
            /*SL:594*/this.lastDesc = null;
        }
        /*SL:596*/this.lastTarget = a1.target;
        final String v1 = /*EL:598*/this.getIdentifier(a1);
        final String v2 = /*EL:599*/a1.getCallbackInfoConstructorDescriptor();
        /*SL:602*/if (v1.equals(this.lastId) && v2.equals(this.lastDesc) && !a1.isAtReturn && !this.cancellable) {
            /*SL:603*/return;
        }
        /*SL:606*/this.instanceCallbackInfo(a1, v1, v2, a2);
    }
    
    private void loadOrCreateCallbackInfo(final Callback a1) {
        /*SL:613*/if (this.cancellable || this.totalInjections > 1) {
            /*SL:614*/a1.add(new VarInsnNode(25, this.callbackInfoVar), false, true);
        }
        else {
            /*SL:616*/this.createCallbackInfo(a1, false);
        }
    }
    
    private void dupReturnValue(final Callback a1) {
        /*SL:629*/if (!a1.isAtReturn) {
            /*SL:630*/return;
        }
        /*SL:633*/a1.add(new InsnNode(89));
        /*SL:634*/a1.add(new VarInsnNode(a1.target.returnType.getOpcode(54), a1.marshalVar()));
    }
    
    protected void instanceCallbackInfo(final Callback a1, final String a2, final String a3, final boolean a4) {
        /*SL:645*/this.lastId = a2;
        /*SL:646*/this.lastDesc = a3;
        /*SL:647*/this.callbackInfoVar = a1.marshalVar();
        /*SL:648*/this.callbackInfoClass = a1.target.getCallbackInfoClass();
        final boolean v1 = /*EL:652*/a4 && this.totalInjections > 1 && !a1.isAtReturn && !this.cancellable;
        /*SL:654*/a1.add(new TypeInsnNode(187, this.callbackInfoClass), true, !a4, v1);
        /*SL:655*/a1.add(new InsnNode(89), true, true, v1);
        /*SL:656*/a1.add(new LdcInsnNode(a2), true, !a4, v1);
        /*SL:657*/a1.add(new InsnNode(this.cancellable ? 4 : 3), true, !a4, v1);
        /*SL:659*/if (a1.isAtReturn) {
            /*SL:660*/a1.add(new VarInsnNode(a1.target.returnType.getOpcode(21), a1.marshalVar()), true, !a4);
            /*SL:661*/a1.add(new MethodInsnNode(183, this.callbackInfoClass, "<init>", a3, false));
        }
        else {
            /*SL:664*/a1.add(new MethodInsnNode(183, this.callbackInfoClass, "<init>", a3, false), false, false, v1);
        }
        /*SL:668*/if (a4) {
            /*SL:669*/a1.target.addLocalVariable(this.callbackInfoVar, "callbackInfo" + this.callbackInfoVar, "L" + this.callbackInfoClass + ";");
            /*SL:670*/a1.add(new VarInsnNode(58, this.callbackInfoVar), false, false, v1);
        }
    }
    
    private void invokeCallback(final Callback a1, final MethodNode a2) {
        /*SL:679*/if (!this.isStatic) {
            /*SL:680*/a1.add(new VarInsnNode(25, 0), false, true);
        }
        /*SL:684*/if (a1.captureArgs()) {
            /*SL:685*/Bytecode.loadArgs(a1.target.arguments, a1, this.isStatic ? 0 : 1, -1);
        }
        /*SL:689*/this.loadOrCreateCallbackInfo(a1);
        /*SL:692*/if (a1.canCaptureLocals) {
            /*SL:693*/Locals.loadLocals(a1.localTypes, a1, a1.frameSize, a1.extraArgs);
        }
        /*SL:697*/this.invokeHandler(a1, a2);
    }
    
    private String getIdentifier(final Callback a1) {
        final String v1 = /*EL:709*/Strings.isNullOrEmpty(this.identifier) ? a1.target.method.name : this.identifier;
        final String v2 = /*EL:710*/this.ids.get(a1.node.getId());
        /*SL:711*/return v1 + (Strings.isNullOrEmpty(v2) ? "" : (":" + v2));
    }
    
    protected void injectCancellationCode(final Callback a1) {
        /*SL:720*/if (!this.cancellable) {
            /*SL:721*/return;
        }
        /*SL:724*/a1.add(new VarInsnNode(25, this.callbackInfoVar));
        /*SL:725*/a1.add(/*EL:726*/new MethodInsnNode(182, this.callbackInfoClass, CallbackInfo.getIsCancelledMethodName(), CallbackInfo.getIsCancelledMethodSig(), false));
        final LabelNode v1 = /*EL:728*/new LabelNode();
        /*SL:729*/a1.add(new JumpInsnNode(153, v1));
        /*SL:733*/this.injectReturnCode(a1);
        /*SL:735*/a1.add(v1);
    }
    
    protected void injectReturnCode(final Callback v-1) {
        /*SL:744*/if (v-1.target.returnType.equals(Type.VOID_TYPE)) {
            /*SL:746*/v-1.add(new InsnNode(177));
        }
        else {
            /*SL:750*/v-1.add(new VarInsnNode(25, v-1.marshalVar()));
            final String a1 = /*EL:751*/CallbackInfoReturnable.getReturnAccessor(v-1.target.returnType);
            final String v1 = /*EL:752*/CallbackInfoReturnable.getReturnDescriptor(v-1.target.returnType);
            /*SL:753*/v-1.add(new MethodInsnNode(182, this.callbackInfoClass, a1, v1, false));
            /*SL:754*/if (v-1.target.returnType.getSort() == 10) {
                /*SL:755*/v-1.add(new TypeInsnNode(192, v-1.target.returnType.getInternalName()));
            }
            /*SL:757*/v-1.add(new InsnNode(v-1.target.returnType.getOpcode(172)));
        }
    }
    
    protected boolean isStatic() {
        /*SL:767*/return this.isStatic;
    }
    
    private static List<String> summariseLocals(final String a1, final int a2) {
        /*SL:771*/return summariseLocals(Type.getArgumentTypes(a1), a2);
    }
    
    private static List<String> summariseLocals(final Type[] a1, int a2) {
        final List<String> v1 = /*EL:775*/new ArrayList<String>();
        /*SL:776*/if (a1 != null) {
            /*SL:777*/while (a2 < a1.length) {
                /*SL:778*/if (a1[a2] != null) {
                    /*SL:779*/v1.add(a1[a2].toString());
                }
                ++a2;
            }
        }
        /*SL:783*/return v1;
    }
    
    static String meltSnowman(final int a1, final String a2) {
        /*SL:787*/return (a2 != null && '\u2603' == a2.charAt(0)) ? ("var" + a1) : a2;
    }
    
    private class Callback extends InsnList
    {
        private final MethodNode handler;
        private final AbstractInsnNode head;
        final Target target;
        final InjectionNodes.InjectionNode node;
        final LocalVariableNode[] locals;
        final Type[] localTypes;
        final int frameSize;
        final int extraArgs;
        final boolean canCaptureLocals;
        final boolean isAtReturn;
        final String desc;
        final String descl;
        final String[] argNames;
        int ctor;
        int invoke;
        private int marshalVar;
        private boolean captureArgs;
        
        Callback(final MethodNode a4, final Target a5, final InjectionNodes.InjectionNode a6, final LocalVariableNode[] v1, final boolean v2) {
            this.marshalVar = -1;
            this.captureArgs = true;
            this.handler = a4;
            this.target = a5;
            this.head = a5.insns.getFirst();
            this.node = a6;
            this.locals = v1;
            this.localTypes = (Type[])((v1 != null) ? new Type[v1.length] : null);
            this.frameSize = Bytecode.getFirstNonArgLocalIndex(a5.arguments, !CallbackInjector.this.isStatic());
            List<String> v3 = null;
            if (v1 != null) {
                final int a7 = CallbackInjector.this.isStatic() ? 0 : 1;
                v3 = new ArrayList<String>();
                for (int a8 = 0; a8 <= v1.length; ++a8) {
                    if (a8 == this.frameSize) {
                        v3.add((a5.returnType == Type.VOID_TYPE) ? "ci" : "cir");
                    }
                    if (a8 < v1.length && v1[a8] != null) {
                        this.localTypes[a8] = Type.getType(v1[a8].desc);
                        if (a8 >= a7) {
                            v3.add(CallbackInjector.meltSnowman(a8, v1[a8].name));
                        }
                    }
                }
            }
            this.extraArgs = Math.max(0, Bytecode.getFirstNonArgLocalIndex(this.handler) - (this.frameSize + 1));
            this.argNames = (String[])((v3 != null) ? ((String[])v3.<String>toArray(new String[v3.size()])) : null);
            this.canCaptureLocals = (v2 && v1 != null && v1.length > this.frameSize);
            this.isAtReturn = (this.node.getCurrentTarget() instanceof InsnNode && this.isValueReturnOpcode(this.node.getCurrentTarget().getOpcode()));
            this.desc = a5.getCallbackDescriptor(this.localTypes, a5.arguments);
            this.descl = a5.getCallbackDescriptor(true, this.localTypes, a5.arguments, this.frameSize, this.extraArgs);
            this.invoke = a5.arguments.length + (this.canCaptureLocals ? (this.localTypes.length - this.frameSize) : 0);
        }
        
        private boolean isValueReturnOpcode(final int a1) {
            /*SL:212*/return a1 >= 172 && a1 < 177;
        }
        
        String getDescriptor() {
            /*SL:216*/return this.canCaptureLocals ? this.descl : this.desc;
        }
        
        String getDescriptorWithAllLocals() {
            /*SL:220*/return this.target.getCallbackDescriptor(true, this.localTypes, this.target.arguments, this.frameSize, 32767);
        }
        
        String getCallbackInfoConstructorDescriptor() {
            /*SL:224*/return this.isAtReturn ? CallbackInfo.getConstructorDescriptor(this.target.returnType) : CallbackInfo.getConstructorDescriptor();
        }
        
        void add(final AbstractInsnNode a1, final boolean a2, final boolean a3) {
            /*SL:236*/this.add(a1, a2, a3, false);
        }
        
        void add(final AbstractInsnNode a1, final boolean a2, final boolean a3, final boolean a4) {
            /*SL:240*/if (a4) {
                /*SL:241*/this.target.insns.insertBefore(this.head, a1);
            }
            else {
                /*SL:243*/this.add(a1);
            }
            /*SL:245*/this.ctor += (a2 ? 1 : 0);
            /*SL:246*/this.invoke += (a3 ? 1 : 0);
        }
        
        void inject() {
            /*SL:254*/this.target.insertBefore(this.node, this);
            /*SL:255*/this.target.addToStack(Math.max(this.invoke, this.ctor));
        }
        
        boolean checkDescriptor(final String v-2) {
            /*SL:259*/if (this.getDescriptor().equals(v-2)) {
                /*SL:260*/return true;
            }
            /*SL:263*/if (this.target.getSimpleCallbackDescriptor().equals(v-2) && !this.canCaptureLocals) {
                /*SL:264*/this.captureArgs = false;
                /*SL:265*/return true;
            }
            final Type[] argumentTypes = /*EL:268*/Type.getArgumentTypes(v-2);
            final Type[] v0 = /*EL:269*/Type.getArgumentTypes(this.descl);
            /*SL:271*/if (argumentTypes.length != v0.length) {
                /*SL:272*/return false;
            }
            /*SL:275*/for (int v = 0; v < v0.length; ++v) {
                final Type a1 = /*EL:276*/argumentTypes[v];
                /*SL:277*/if (!a1.equals(v0[v])) {
                    /*SL:281*/if (a1.getSort() == 9) {
                        /*SL:282*/return false;
                    }
                    /*SL:285*/if (Annotations.getInvisibleParameter(this.handler, Coerce.class, v) == null) {
                        /*SL:286*/return false;
                    }
                    /*SL:289*/if (!Injector.canCoerce(argumentTypes[v], v0[v])) {
                        /*SL:293*/return false;
                    }
                }
            }
            /*SL:298*/return true;
        }
        
        boolean captureArgs() {
            /*SL:302*/return this.captureArgs;
        }
        
        int marshalVar() {
            /*SL:306*/if (this.marshalVar < 0) {
                /*SL:307*/this.marshalVar = this.target.allocateLocal();
            }
            /*SL:310*/return this.marshalVar;
        }
    }
}
