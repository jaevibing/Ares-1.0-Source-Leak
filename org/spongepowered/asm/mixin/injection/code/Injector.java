package org.spongepowered.asm.mixin.injection.code;

import java.util.Collections;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import java.util.Map;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import java.util.TreeMap;
import java.util.Collection;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Set;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Iterator;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.apache.logging.log4j.Logger;

public abstract class Injector
{
    protected static final Logger logger;
    protected InjectionInfo info;
    protected final ClassNode classNode;
    protected final MethodNode methodNode;
    protected final Type[] methodArgs;
    protected final Type returnType;
    protected final boolean isStatic;
    
    public Injector(final InjectionInfo a1) {
        this(a1.getClassNode(), a1.getMethod());
        this.info = a1;
    }
    
    private Injector(final ClassNode a1, final MethodNode a2) {
        this.classNode = a1;
        this.methodNode = a2;
        this.methodArgs = Type.getArgumentTypes(this.methodNode.desc);
        this.returnType = Type.getReturnType(this.methodNode.desc);
        this.isStatic = Bytecode.methodIsStatic(this.methodNode);
    }
    
    @Override
    public String toString() {
        /*SL:161*/return String.format("%s::%s", this.classNode.name, this.methodNode.name);
    }
    
    public final List<InjectionNodes.InjectionNode> find(final InjectorTarget v1, final List<InjectionPoint> v2) {
        /*SL:173*/this.sanityCheck(v1.getTarget(), v2);
        final List<InjectionNodes.InjectionNode> v3 = /*EL:175*/new ArrayList<InjectionNodes.InjectionNode>();
        /*SL:176*/for (final TargetNode a1 : this.findTargetNodes(v1, v2)) {
            /*SL:177*/this.addTargetNode(v1.getTarget(), v3, a1.insn, a1.nominators);
        }
        /*SL:179*/return v3;
    }
    
    protected void addTargetNode(final Target a1, final List<InjectionNodes.InjectionNode> a2, final AbstractInsnNode a3, final Set<InjectionPoint> a4) {
        /*SL:183*/a2.add(a1.addInjectionNode(a3));
    }
    
    public final void inject(final Target v2, final List<InjectionNodes.InjectionNode> v3) {
        /*SL:193*/for (final InjectionNodes.InjectionNode a1 : v3) {
            /*SL:194*/if (a1.isRemoved()) {
                /*SL:195*/if (!this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                    continue;
                }
                Injector.logger.warn(/*EL:196*/"Target node for {} was removed by a previous injector in {}", new Object[] { this.info, v2 });
            }
            else {
                /*SL:200*/this.inject(v2, a1);
            }
        }
        /*SL:203*/for (final InjectionNodes.InjectionNode a2 : v3) {
            /*SL:204*/this.postInject(v2, a2);
        }
    }
    
    private Collection<TargetNode> findTargetNodes(final InjectorTarget v-8, final List<InjectionPoint> v-7) {
        final IMixinContext context = /*EL:218*/this.info.getContext();
        final MethodNode method = /*EL:219*/v-8.getMethod();
        final Map<Integer, TargetNode> map = /*EL:220*/new TreeMap<Integer, TargetNode>();
        final Collection<AbstractInsnNode> a3 = /*EL:221*/new ArrayList<AbstractInsnNode>(32);
        /*SL:223*/for (final InjectionPoint injectionPoint : v-7) {
            /*SL:224*/a3.clear();
            /*SL:227*/if (v-8.isMerged() && !context.getClassName().equals(v-8.getMergedBy()) && !injectionPoint.checkPriority(v-8.getMergedPriority(), /*EL:228*/context.getPriority())) {
                /*SL:229*/throw new InvalidInjectionException(this.info, String.format("%s on %s with priority %d cannot inject into %s merged by %s with priority %d", injectionPoint, this, context.getPriority(), /*EL:230*/v-8, v-8.getMergedBy(), /*EL:231*/v-8.getMergedPriority()));
            }
            /*SL:234*/if (!this.findTargetNodes(method, injectionPoint, v-8.getSlice(injectionPoint), a3)) {
                continue;
            }
            /*SL:235*/for (final AbstractInsnNode v1 : a3) {
                final Integer a1 = /*EL:236*/method.instructions.indexOf(v1);
                TargetNode a2 = /*EL:237*/map.get(a1);
                /*SL:238*/if (a2 == null) {
                    /*SL:239*/a2 = new TargetNode(v1);
                    /*SL:240*/map.put(a1, a2);
                }
                /*SL:242*/a2.nominators.add(injectionPoint);
            }
        }
        /*SL:247*/return map.values();
    }
    
    protected boolean findTargetNodes(final MethodNode a1, final InjectionPoint a2, final InsnList a3, final Collection<AbstractInsnNode> a4) {
        /*SL:251*/return a2.find(a1.desc, a3, a4);
    }
    
    protected void sanityCheck(final Target a1, final List<InjectionPoint> a2) {
        /*SL:255*/if (a1.classNode != this.classNode) {
            /*SL:256*/throw new InvalidInjectionException(this.info, "Target class does not match injector class in " + this);
        }
    }
    
    protected abstract void inject(final Target p0, final InjectionNodes.InjectionNode p1);
    
    protected void postInject(final Target a1, final InjectionNodes.InjectionNode a2) {
    }
    
    protected AbstractInsnNode invokeHandler(final InsnList a1) {
        /*SL:273*/return this.invokeHandler(a1, this.methodNode);
    }
    
    protected AbstractInsnNode invokeHandler(final InsnList a1, final MethodNode a2) {
        final boolean v1 = /*EL:285*/(a2.access & 0x2) != 0x0;
        final int v2 = /*EL:286*/this.isStatic ? 184 : (v1 ? 183 : 182);
        final MethodInsnNode v3 = /*EL:287*/new MethodInsnNode(v2, this.classNode.name, a2.name, a2.desc, false);
        /*SL:288*/a1.add(v3);
        /*SL:289*/this.info.addCallbackInvocation(a2);
        /*SL:290*/return v3;
    }
    
    protected void throwException(final InsnList a1, final String a2, final String a3) {
        /*SL:302*/a1.add(new TypeInsnNode(187, a2));
        /*SL:303*/a1.add(new InsnNode(89));
        /*SL:304*/a1.add(new LdcInsnNode(a3));
        /*SL:305*/a1.add(new MethodInsnNode(183, a2, "<init>", "(Ljava/lang/String;)V", false));
        /*SL:306*/a1.add(new InsnNode(191));
    }
    
    public static boolean canCoerce(final Type a1, final Type a2) {
        /*SL:318*/if (a1.getSort() == 10 && a2.getSort() == 10) {
            /*SL:319*/return canCoerce(ClassInfo.forType(a1), ClassInfo.forType(a2));
        }
        /*SL:322*/return canCoerce(a1.getDescriptor(), a2.getDescriptor());
    }
    
    public static boolean canCoerce(final String a1, final String a2) {
        /*SL:334*/return a1.length() <= 1 && a2.length() <= 1 && canCoerce(/*EL:338*/a1.charAt(0), a2.charAt(0));
    }
    
    public static boolean canCoerce(final char a1, final char a2) {
        /*SL:350*/return a2 == 'I' && "IBSCZ".indexOf(a1) > -1;
    }
    
    private static boolean canCoerce(final ClassInfo a1, final ClassInfo a2) {
        /*SL:363*/return a1 != null && a2 != null && (a2 == a1 || a2.hasSuperClass(a1));
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    public static final class TargetNode
    {
        final AbstractInsnNode insn;
        final Set<InjectionPoint> nominators;
        
        TargetNode(final AbstractInsnNode a1) {
            this.nominators = new HashSet<InjectionPoint>();
            this.insn = a1;
        }
        
        public AbstractInsnNode getNode() {
            /*SL:77*/return this.insn;
        }
        
        public Set<InjectionPoint> getNominators() {
            /*SL:81*/return Collections.<InjectionPoint>unmodifiableSet((Set<? extends InjectionPoint>)this.nominators);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:86*/return a1 != null && a1.getClass() == TargetNode.class && /*EL:90*/((TargetNode)a1).insn == this.insn;
        }
        
        @Override
        public int hashCode() {
            /*SL:95*/return this.insn.hashCode();
        }
    }
}
