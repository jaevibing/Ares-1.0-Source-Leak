package org.spongepowered.asm.mixin.injection.invoke;

import com.google.common.collect.ObjectArrays;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import com.google.common.base.Joiner;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.Coerce;
import com.google.common.primitives.Ints;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.Set;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Final;
import java.util.HashMap;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.points.BeforeNew;
import java.util.Map;

public class RedirectInjector extends InvokeInjector
{
    private static final String KEY_NOMINATORS = "nominators";
    private static final String KEY_FUZZ = "fuzz";
    private static final String KEY_OPCODE = "opcode";
    protected Meta meta;
    private Map<BeforeNew, ConstructorRedirectData> ctorRedirectors;
    
    public RedirectInjector(final InjectionInfo a1) {
        this(a1, "@Redirect");
    }
    
    protected RedirectInjector(final InjectionInfo a1, final String a2) {
        super(a1, a2);
        this.ctorRedirectors = new HashMap<BeforeNew, ConstructorRedirectData>();
        final int v1 = a1.getContext().getPriority();
        final boolean v2 = Annotations.getVisible(this.methodNode, Final.class) != null;
        this.meta = new Meta(v1, v2, this.info.toString(), this.methodNode.desc);
    }
    
    @Override
    protected void checkTarget(final Target a1) {
    }
    
    @Override
    protected void addTargetNode(final Target v1, final List<InjectionNodes.InjectionNode> v2, final AbstractInsnNode v3, final Set<InjectionPoint> v4) {
        final InjectionNodes.InjectionNode v5 = /*EL:189*/v1.getInjectionNode(v3);
        ConstructorRedirectData v6 = /*EL:190*/null;
        int v7 = /*EL:191*/8;
        int v8 = /*EL:192*/0;
        /*SL:194*/if (v5 != null) {
            final Meta a1 = /*EL:195*/v5.<Meta>getDecoration("redirector");
            /*SL:197*/if (a1 != null && a1.getOwner() != this) {
                /*SL:198*/if (a1.priority >= this.meta.priority) {
                    Injector.logger.warn(/*EL:199*/"{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, this.meta.priority, /*EL:200*/a1.name, a1.priority });
                    /*SL:201*/return;
                }
                /*SL:202*/if (a1.isFinal) {
                    /*SL:203*/throw new InvalidInjectionException(this.info, String.format("%s conflict: %s failed because target was already remapped by %s", this.annotationType, this, a1.name));
                }
            }
        }
        /*SL:209*/for (final InjectionPoint a2 : v4) {
            /*SL:210*/if (a2 instanceof BeforeNew) {
                /*SL:211*/v6 = this.getCtorRedirect((BeforeNew)a2);
                /*SL:212*/v6.wildcard = !((BeforeNew)a2).hasDescriptor();
            }
            else {
                /*SL:213*/if (!(a2 instanceof BeforeFieldAccess)) {
                    continue;
                }
                final BeforeFieldAccess a3 = /*EL:214*/(BeforeFieldAccess)a2;
                /*SL:215*/v7 = a3.getFuzzFactor();
                /*SL:216*/v8 = a3.getArrayOpcode();
            }
        }
        final InjectionNodes.InjectionNode v9 = /*EL:220*/v1.addInjectionNode(v3);
        /*SL:221*/v9.<Meta>decorate("redirector", this.meta);
        /*SL:222*/v9.<Set<InjectionPoint>>decorate("nominators", v4);
        /*SL:223*/if (v3 instanceof TypeInsnNode && v3.getOpcode() == 187) {
            /*SL:224*/v9.<ConstructorRedirectData>decorate("ctor", v6);
        }
        else {
            /*SL:226*/v9.<Integer>decorate("fuzz", v7);
            /*SL:227*/v9.<Integer>decorate("opcode", v8);
        }
        /*SL:229*/v2.add(v9);
    }
    
    private ConstructorRedirectData getCtorRedirect(final BeforeNew a1) {
        ConstructorRedirectData v1 = /*EL:233*/this.ctorRedirectors.get(a1);
        /*SL:234*/if (v1 == null) {
            /*SL:235*/v1 = new ConstructorRedirectData();
            /*SL:236*/this.ctorRedirectors.put(a1, v1);
        }
        /*SL:238*/return v1;
    }
    
    @Override
    protected void inject(final Target a1, final InjectionNodes.InjectionNode a2) {
        /*SL:243*/if (!this.preInject(a2)) {
            /*SL:244*/return;
        }
        /*SL:247*/if (a2.isReplaced()) {
            /*SL:248*/throw new UnsupportedOperationException("Redirector target failure for " + this.info);
        }
        /*SL:251*/if (a2.getCurrentTarget() instanceof MethodInsnNode) {
            /*SL:252*/this.checkTargetForNode(a1, a2);
            /*SL:253*/this.injectAtInvoke(a1, a2);
            /*SL:254*/return;
        }
        /*SL:257*/if (a2.getCurrentTarget() instanceof FieldInsnNode) {
            /*SL:258*/this.checkTargetForNode(a1, a2);
            /*SL:259*/this.injectAtFieldAccess(a1, a2);
            /*SL:260*/return;
        }
        /*SL:263*/if (!(a2.getCurrentTarget() instanceof TypeInsnNode) || a2.getCurrentTarget().getOpcode() != 187) {
            /*SL:272*/throw new InvalidInjectionException(this.info, String.format("%s annotation on is targetting an invalid insn in %s in %s", this.annotationType, a1, this));
        }
        if (!this.isStatic && a1.isStatic) {
            throw new InvalidInjectionException(this.info, String.format("non-static callback method %s has a static target which is not supported", this));
        }
        this.injectAtConstructor(a1, a2);
    }
    
    protected boolean preInject(final InjectionNodes.InjectionNode a1) {
        final Meta v1 = /*EL:277*/a1.<Meta>getDecoration("redirector");
        /*SL:278*/if (v1.getOwner() != this) {
            Injector.logger.warn(/*EL:279*/"{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, this.meta.priority, /*EL:280*/v1.name, v1.priority });
            /*SL:281*/return false;
        }
        /*SL:283*/return true;
    }
    
    @Override
    protected void postInject(final Target v1, final InjectionNodes.InjectionNode v2) {
        /*SL:288*/super.postInject(v1, v2);
        /*SL:289*/if (v2.getOriginalTarget() instanceof TypeInsnNode && v2.getOriginalTarget().getOpcode() == 187) {
            final ConstructorRedirectData a1 = /*EL:290*/v2.<ConstructorRedirectData>getDecoration("ctor");
            /*SL:291*/if (a1.wildcard && a1.injected == 0) {
                /*SL:292*/throw new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", this.annotationType, v1));
            }
        }
    }
    
    @Override
    protected void injectAtInvoke(final Target v1, final InjectionNodes.InjectionNode v2) {
        final RedirectedInvoke v3 = /*EL:302*/new RedirectedInvoke(v1, (MethodInsnNode)v2.getCurrentTarget());
        /*SL:304*/this.validateParams(v3);
        final InsnList v4 = /*EL:306*/new InsnList();
        int v5 = /*EL:307*/Bytecode.getArgsSize(v3.locals) + 1;
        int v6 = /*EL:308*/1;
        int[] v7 = /*EL:309*/this.storeArgs(v1, v3.locals, v4, 0);
        /*SL:310*/if (v3.captureTargetArgs) {
            final int a1 = /*EL:311*/Bytecode.getArgsSize(v1.arguments);
            /*SL:312*/v5 += a1;
            /*SL:313*/v6 += a1;
            /*SL:314*/v7 = Ints.concat(new int[][] { v7, v1.getArgIndices() });
        }
        final AbstractInsnNode v8 = /*EL:316*/this.invokeHandlerWithArgs(this.methodArgs, v4, v7);
        /*SL:317*/v1.replaceNode(v3.node, v8, v4);
        /*SL:318*/v1.addToLocals(v5);
        /*SL:319*/v1.addToStack(v6);
    }
    
    protected void validateParams(final RedirectedInvoke v-3) {
        int n = /*EL:330*/this.methodArgs.length;
        final String format = /*EL:332*/String.format("%s handler method %s", this.annotationType, this);
        /*SL:333*/if (!v-3.returnType.equals(this.returnType)) {
            /*SL:334*/throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Expected return type %s found %s", format, this.returnType, v-3.returnType));
        }
        /*SL:338*/for (int v0 = 0; v0 < n; ++v0) {
            Type v = /*EL:339*/null;
            /*SL:340*/if (v0 >= this.methodArgs.length) {
                /*SL:341*/throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Not enough arguments found for capture of target method args, expected %d but found %d", format, n, /*EL:343*/this.methodArgs.length));
            }
            final Type v2 = /*EL:346*/this.methodArgs[v0];
            /*SL:348*/if (v0 < v-3.locals.length) {
                /*SL:349*/v = v-3.locals[v0];
            }
            else {
                /*SL:351*/v-3.captureTargetArgs = true;
                /*SL:352*/n = Math.max(n, v-3.locals.length + v-3.target.arguments.length);
                final int a1 = /*EL:353*/v0 - v-3.locals.length;
                /*SL:354*/if (a1 >= v-3.target.arguments.length) {
                    /*SL:355*/throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found unexpected additional target argument with type %s at index %d", format, v2, v0));
                }
                /*SL:359*/v = v-3.target.arguments[a1];
            }
            final AnnotationNode v3 = /*EL:362*/Annotations.getInvisibleParameter(this.methodNode, Coerce.class, v0);
            /*SL:364*/if (v2.equals(v)) {
                /*SL:365*/if (v3 != null && this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                    Injector.logger.warn(/*EL:366*/"Redundant @Coerce on {} argument {}, {} is identical to {}", new Object[] { format, v0, v, v2 });
                }
            }
            else {
                final boolean v4 = /*EL:372*/Injector.canCoerce(v2, v);
                /*SL:373*/if (v3 == null) {
                    /*SL:374*/throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Found unexpected argument type %s at index %d, expected %s", format, v2, v0, /*EL:376*/v));
                }
                /*SL:379*/if (!v4) {
                    /*SL:380*/throw new InvalidInjectionException(this.info, String.format("%s has an invalid signature. Cannot @Coerce argument type %s at index %d to %s", format, v, v0, /*EL:382*/v2));
                }
            }
        }
    }
    
    private void injectAtFieldAccess(final Target v2, final InjectionNodes.InjectionNode v3) {
        final FieldInsnNode v4 = /*EL:391*/(FieldInsnNode)v3.getCurrentTarget();
        final int v5 = /*EL:392*/v4.getOpcode();
        final Type v6 = /*EL:393*/Type.getType("L" + v4.owner + ";");
        final Type v7 = /*EL:394*/Type.getType(v4.desc);
        final int v8 = /*EL:396*/(v7.getSort() == 9) ? v7.getDimensions() : 0;
        final int v9 = /*EL:397*/(this.returnType.getSort() == 9) ? this.returnType.getDimensions() : 0;
        /*SL:399*/if (v9 > v8) {
            /*SL:400*/throw new InvalidInjectionException(this.info, "Dimensionality of handler method is greater than target array on " + this);
        }
        /*SL:401*/if (v9 == 0 && v8 > 0) {
            final int a1 = /*EL:402*/v3.<Integer>getDecoration("fuzz");
            final int a2 = /*EL:403*/v3.<Integer>getDecoration("opcode");
            /*SL:404*/this.injectAtArrayField(v2, v4, v5, v6, v7, a1, a2);
        }
        else {
            /*SL:406*/this.injectAtScalarField(v2, v4, v5, v6, v7);
        }
    }
    
    private void injectAtArrayField(final Target a4, final FieldInsnNode a5, final int a6, final Type a7, final Type v1, final int v2, int v3) {
        final Type v4 = /*EL:414*/v1.getElementType();
        /*SL:415*/if (a6 != 178 && a6 != 180) {
            /*SL:416*/throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for array access %s", /*EL:417*/Bytecode.getOpcodeName(a6), this.info));
        }
        /*SL:418*/if (this.returnType.getSort() != 0) {
            /*SL:419*/if (v3 != 190) {
                /*SL:420*/v3 = v4.getOpcode(46);
            }
            final AbstractInsnNode a8 = /*EL:422*/BeforeFieldAccess.findArrayNode(a4.insns, a5, v3, v2);
            /*SL:423*/this.injectAtGetArray(a4, a5, a8, a7, v1);
        }
        else {
            final AbstractInsnNode a9 = /*EL:425*/BeforeFieldAccess.findArrayNode(a4.insns, a5, v4.getOpcode(79), v2);
            /*SL:426*/this.injectAtSetArray(a4, a5, a9, a7, v1);
        }
    }
    
    private void injectAtGetArray(final Target a1, final FieldInsnNode a2, final AbstractInsnNode a3, final Type a4, final Type a5) {
        final String v1 = getGetArrayHandlerDescriptor(/*EL:434*/a3, this.returnType, a5);
        final boolean v2 = /*EL:435*/this.checkDescriptor(v1, a1, "array getter");
        /*SL:436*/this.injectArrayRedirect(a1, a2, a3, v2, "array getter");
    }
    
    private void injectAtSetArray(final Target a1, final FieldInsnNode a2, final AbstractInsnNode a3, final Type a4, final Type a5) {
        final String v1 = /*EL:443*/Bytecode.generateDescriptor(null, (Object[])getArrayArgs(a5, 1, a5.getElementType()));
        final boolean v2 = /*EL:444*/this.checkDescriptor(v1, a1, "array setter");
        /*SL:445*/this.injectArrayRedirect(a1, a2, a3, v2, "array setter");
    }
    
    public void injectArrayRedirect(final Target a3, final FieldInsnNode a4, final AbstractInsnNode a5, final boolean v1, final String v2) {
        /*SL:462*/if (a5 == null) {
            final String a6 = /*EL:463*/"";
            /*SL:464*/throw new InvalidInjectionException(this.info, String.format("Array element %s on %s could not locate a matching %s instruction in %s. %s", this.annotationType, this, v2, a3, a6));
        }
        /*SL:469*/if (!this.isStatic) {
            /*SL:470*/a3.insns.insertBefore(a4, new VarInsnNode(25, 0));
            /*SL:471*/a3.addToStack(1);
        }
        final InsnList v3 = /*EL:474*/new InsnList();
        /*SL:475*/if (v1) {
            /*SL:476*/this.pushArgs(a3.arguments, v3, a3.getArgIndices(), 0, a3.arguments.length);
            /*SL:477*/a3.addToStack(Bytecode.getArgsSize(a3.arguments));
        }
        /*SL:479*/a3.replaceNode(a5, this.invokeHandler(v3), v3);
    }
    
    public void injectAtScalarField(final Target a1, final FieldInsnNode a2, final int a3, final Type a4, final Type a5) {
        AbstractInsnNode v1 = /*EL:492*/null;
        final InsnList v2 = /*EL:493*/new InsnList();
        /*SL:494*/if (a3 == 178 || a3 == 180) {
            /*SL:495*/v1 = this.injectAtGetField(v2, a1, a2, a3 == 178, a4, a5);
        }
        else {
            /*SL:496*/if (a3 != 179 && a3 != 181) {
                /*SL:499*/throw new InvalidInjectionException(this.info, String.format("Unspported opcode %s for %s", Bytecode.getOpcodeName(a3), this.info));
            }
            v1 = this.injectAtPutField(v2, a1, a2, a3 == 179, a4, a5);
        }
        /*SL:502*/a1.replaceNode(a2, v1, v2);
    }
    
    private AbstractInsnNode injectAtGetField(final InsnList a1, final Target a2, final FieldInsnNode a3, final boolean a4, final Type a5, final Type a6) {
        final String v1 = /*EL:512*/a4 ? Bytecode.generateDescriptor(a6, new Object[0]) : Bytecode.generateDescriptor(a6, a5);
        final boolean v2 = /*EL:513*/this.checkDescriptor(v1, a2, "getter");
        /*SL:515*/if (!this.isStatic) {
            /*SL:516*/a1.add(new VarInsnNode(25, 0));
            /*SL:517*/if (!a4) {
                /*SL:518*/a1.add(new InsnNode(95));
            }
        }
        /*SL:522*/if (v2) {
            /*SL:523*/this.pushArgs(a2.arguments, a1, a2.getArgIndices(), 0, a2.arguments.length);
            /*SL:524*/a2.addToStack(Bytecode.getArgsSize(a2.arguments));
        }
        /*SL:527*/a2.addToStack(this.isStatic ? 0 : 1);
        /*SL:528*/return this.invokeHandler(a1);
    }
    
    private AbstractInsnNode injectAtPutField(final InsnList a3, final Target a4, final FieldInsnNode a5, final boolean a6, final Type v1, final Type v2) {
        final String v3 = /*EL:538*/a6 ? Bytecode.generateDescriptor(null, v2) : Bytecode.generateDescriptor(null, v1, v2);
        final boolean v4 = /*EL:539*/this.checkDescriptor(v3, a4, "setter");
        /*SL:541*/if (!this.isStatic) {
            /*SL:542*/if (a6) {
                /*SL:543*/a3.add(new VarInsnNode(25, 0));
                /*SL:544*/a3.add(new InsnNode(95));
            }
            else {
                final int a7 = /*EL:546*/a4.allocateLocals(v2.getSize());
                /*SL:547*/a3.add(new VarInsnNode(v2.getOpcode(54), a7));
                /*SL:548*/a3.add(new VarInsnNode(25, 0));
                /*SL:549*/a3.add(new InsnNode(95));
                /*SL:550*/a3.add(new VarInsnNode(v2.getOpcode(21), a7));
            }
        }
        /*SL:554*/if (v4) {
            /*SL:555*/this.pushArgs(a4.arguments, a3, a4.getArgIndices(), 0, a4.arguments.length);
            /*SL:556*/a4.addToStack(Bytecode.getArgsSize(a4.arguments));
        }
        /*SL:559*/a4.addToStack((!this.isStatic && !a6) ? 1 : 0);
        /*SL:560*/return this.invokeHandler(a3);
    }
    
    protected boolean checkDescriptor(final String a1, final Target a2, final String a3) {
        /*SL:574*/if (this.methodNode.desc.equals(a1)) {
            /*SL:575*/return false;
        }
        final int v1 = /*EL:578*/a1.indexOf(41);
        final String v2 = /*EL:579*/String.format("%s%s%s", a1.substring(0, v1), Joiner.on("").join(a2.arguments), a1.substring(v1));
        /*SL:580*/if (this.methodNode.desc.equals(v2)) {
            /*SL:581*/return true;
        }
        /*SL:584*/throw new InvalidInjectionException(this.info, String.format("%s method %s %s has an invalid signature. Expected %s but found %s", this.annotationType, a3, this, a1, this.methodNode.desc));
    }
    
    protected void injectAtConstructor(final Target v2, final InjectionNodes.InjectionNode v3) {
        final ConstructorRedirectData v4 = /*EL:589*/v3.<ConstructorRedirectData>getDecoration("ctor");
        /*SL:591*/if (v4 == null) {
            /*SL:593*/throw new InvalidInjectionException(this.info, String.format("%s ctor redirector has no metadata, the injector failed a preprocessing phase", this.annotationType));
        }
        final TypeInsnNode v5 = /*EL:597*/(TypeInsnNode)v3.getCurrentTarget();
        final AbstractInsnNode v6 = /*EL:598*/v2.get(v2.indexOf(v5) + 1);
        final MethodInsnNode v7 = /*EL:599*/v2.findInitNodeFor(v5);
        /*SL:601*/if (v7 != null) {
            final boolean v8 = /*EL:609*/v6.getOpcode() == 89;
            final String v9 = /*EL:610*/v7.desc.replace(")V", ")L" + v5.desc + ";");
            boolean v10 = /*EL:611*/false;
            try {
                /*SL:613*/v10 = this.checkDescriptor(v9, v2, "constructor");
            }
            catch (InvalidInjectionException a1) {
                /*SL:615*/if (!v4.wildcard) {
                    /*SL:616*/throw a1;
                }
                /*SL:618*/return;
            }
            /*SL:621*/if (v8) {
                /*SL:622*/v2.removeNode(v6);
            }
            /*SL:625*/if (this.isStatic) {
                /*SL:626*/v2.removeNode(v5);
            }
            else {
                /*SL:628*/v2.replaceNode(v5, new VarInsnNode(25, 0));
            }
            final InsnList v11 = /*EL:631*/new InsnList();
            /*SL:632*/if (v10) {
                /*SL:633*/this.pushArgs(v2.arguments, v11, v2.getArgIndices(), 0, v2.arguments.length);
                /*SL:634*/v2.addToStack(Bytecode.getArgsSize(v2.arguments));
            }
            /*SL:637*/this.invokeHandler(v11);
            /*SL:639*/if (v8) {
                final LabelNode a2 = /*EL:643*/new LabelNode();
                /*SL:644*/v11.add(new InsnNode(89));
                /*SL:645*/v11.add(new JumpInsnNode(199, a2));
                /*SL:646*/this.throwException(v11, "java/lang/NullPointerException", String.format("%s constructor handler %s returned null for %s", this.annotationType, this, v5.desc.replace('/', '.')));
                /*SL:648*/v11.add(a2);
                /*SL:649*/v2.addToStack(1);
            }
            else {
                /*SL:652*/v11.add(new InsnNode(87));
            }
            /*SL:655*/v2.replaceNode(v7, v11);
            final ConstructorRedirectData constructorRedirectData = /*EL:656*/v4;
            ++constructorRedirectData.injected;
            /*SL:657*/return;
        }
        if (!v4.wildcard) {
            throw new InvalidInjectionException(this.info, String.format("%s ctor invocation was not found in %s", this.annotationType, v2));
        }
    }
    
    private static String getGetArrayHandlerDescriptor(final AbstractInsnNode a1, final Type a2, final Type a3) {
        /*SL:660*/if (a1 != null && a1.getOpcode() == 190) {
            /*SL:661*/return Bytecode.generateDescriptor(Type.INT_TYPE, (Object[])getArrayArgs(a3, 0, new Type[0]));
        }
        /*SL:663*/return Bytecode.generateDescriptor(a2, (Object[])getArrayArgs(a3, 1, new Type[0]));
    }
    
    private static Type[] getArrayArgs(final Type a2, final int a3, final Type... v1) {
        final int v2 = /*EL:667*/a2.getDimensions() + a3;
        final Type[] v3 = /*EL:668*/new Type[v2 + v1.length];
        /*SL:669*/for (int a4 = 0; a4 < v3.length; ++a4) {
            /*SL:670*/v3[a4] = ((a4 == 0) ? a2 : ((a4 < v2) ? Type.INT_TYPE : v1[v2 - a4]));
        }
        /*SL:672*/return v3;
    }
    
    class Meta
    {
        public static final String KEY = "redirector";
        final int priority;
        final boolean isFinal;
        final String name;
        final String desc;
        
        public Meta(final int a2, final boolean a3, final String a4, final String a5) {
            this.priority = a2;
            this.isFinal = a3;
            this.name = a4;
            this.desc = a5;
        }
        
        RedirectInjector getOwner() {
            /*SL:117*/return RedirectInjector.this;
        }
    }
    
    static class ConstructorRedirectData
    {
        public static final String KEY = "ctor";
        public boolean wildcard;
        public int injected;
        
        ConstructorRedirectData() {
            this.wildcard = false;
            this.injected = 0;
        }
    }
    
    static class RedirectedInvoke
    {
        final Target target;
        final MethodInsnNode node;
        final Type returnType;
        final Type[] args;
        final Type[] locals;
        boolean captureTargetArgs;
        
        RedirectedInvoke(final Target a1, final MethodInsnNode a2) {
            this.captureTargetArgs = false;
            this.target = a1;
            this.node = a2;
            this.returnType = Type.getReturnType(a2.desc);
            this.args = Type.getArgumentTypes(a2.desc);
            this.locals = ((a2.getOpcode() == 184) ? this.args : ObjectArrays.<Type>concat(Type.getType("L" + a2.owner + ";"), this.args));
        }
    }
}
