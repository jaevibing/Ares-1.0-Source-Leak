package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.LabelNode;
import java.util.List;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;

public class Target implements Comparable<Target>, Iterable<AbstractInsnNode>
{
    public final ClassNode classNode;
    public final MethodNode method;
    public final InsnList insns;
    public final boolean isStatic;
    public final boolean isCtor;
    public final Type[] arguments;
    public final Type returnType;
    private final int maxStack;
    private final int maxLocals;
    private final InjectionNodes injectionNodes;
    private String callbackInfoClass;
    private String callbackDescriptor;
    private int[] argIndices;
    private List<Integer> argMapVars;
    private LabelNode start;
    private LabelNode end;
    
    public Target(final ClassNode a1, final MethodNode a2) {
        this.injectionNodes = new InjectionNodes();
        this.classNode = a1;
        this.method = a2;
        this.insns = a2.instructions;
        this.isStatic = Bytecode.methodIsStatic(a2);
        this.isCtor = a2.name.equals("<init>");
        this.arguments = Type.getArgumentTypes(a2.desc);
        this.returnType = Type.getReturnType(a2.desc);
        this.maxStack = a2.maxStack;
        this.maxLocals = a2.maxLocals;
    }
    
    public InjectionNodes.InjectionNode addInjectionNode(final AbstractInsnNode a1) {
        /*SL:155*/return this.injectionNodes.add(a1);
    }
    
    public InjectionNodes.InjectionNode getInjectionNode(final AbstractInsnNode a1) {
        /*SL:166*/return this.injectionNodes.get(a1);
    }
    
    public int getMaxLocals() {
        /*SL:175*/return this.maxLocals;
    }
    
    public int getMaxStack() {
        /*SL:184*/return this.maxStack;
    }
    
    public int getCurrentMaxLocals() {
        /*SL:193*/return this.method.maxLocals;
    }
    
    public int getCurrentMaxStack() {
        /*SL:202*/return this.method.maxStack;
    }
    
    public int allocateLocal() {
        /*SL:211*/return this.allocateLocals(1);
    }
    
    public int allocateLocals(final int a1) {
        final int v1 = /*EL:222*/this.method.maxLocals;
        final MethodNode method = /*EL:223*/this.method;
        method.maxLocals += a1;
        /*SL:224*/return v1;
    }
    
    public void addToLocals(final int a1) {
        /*SL:233*/this.setMaxLocals(this.maxLocals + a1);
    }
    
    public void setMaxLocals(final int a1) {
        /*SL:243*/if (a1 > this.method.maxLocals) {
            /*SL:244*/this.method.maxLocals = a1;
        }
    }
    
    public void addToStack(final int a1) {
        /*SL:254*/this.setMaxStack(this.maxStack + a1);
    }
    
    public void setMaxStack(final int a1) {
        /*SL:264*/if (a1 > this.method.maxStack) {
            /*SL:265*/this.method.maxStack = a1;
        }
    }
    
    public int[] generateArgMap(final Type[] v-3, final int v-2) {
        /*SL:280*/if (this.argMapVars == null) {
            /*SL:281*/this.argMapVars = new ArrayList<Integer>();
        }
        final int[] array = /*EL:284*/new int[v-3.length];
        int a2 = /*EL:285*/v-2;
        int v1 = 0;
        while (a2 < v-3.length) {
            /*SL:286*/a2 = v-3[a2].getSize();
            /*SL:287*/array[a2] = this.allocateArgMapLocal(v1, a2);
            /*SL:288*/v1 += a2;
            ++a2;
        }
        /*SL:290*/return array;
    }
    
    private int allocateArgMapLocal(final int v-2, final int v-1) {
        /*SL:306*/if (v-2 >= this.argMapVars.size()) {
            int a2 = /*EL:307*/this.allocateLocals(v-1);
            /*SL:308*/for (a2 = 0; a2 < v-1; ++a2) {
                /*SL:309*/this.argMapVars.add(a2 + a2);
            }
            /*SL:311*/return a2;
        }
        final int v0 = /*EL:314*/this.argMapVars.get(v-2);
        /*SL:317*/if (v-1 <= 1 || v-2 + v-1 <= this.argMapVars.size()) {
            /*SL:332*/return v0;
        }
        final int v = this.allocateLocals(1);
        if (v == v0 + 1) {
            this.argMapVars.add(v);
            return v0;
        }
        this.argMapVars.set(v-2, v);
        this.argMapVars.add(this.allocateLocals(1));
        return v;
    }
    
    public int[] getArgIndices() {
        /*SL:341*/if (this.argIndices == null) {
            /*SL:342*/this.argIndices = this.calcArgIndices(this.isStatic ? 0 : 1);
        }
        /*SL:344*/return this.argIndices;
    }
    
    private int[] calcArgIndices(int v2) {
        final int[] v3 = /*EL:348*/new int[this.arguments.length];
        /*SL:349*/for (int a1 = 0; a1 < this.arguments.length; ++a1) {
            /*SL:350*/v3[a1] = v2;
            /*SL:351*/v2 += this.arguments[a1].getSize();
        }
        /*SL:353*/return v3;
    }
    
    public String getCallbackInfoClass() {
        /*SL:363*/if (this.callbackInfoClass == null) {
            /*SL:364*/this.callbackInfoClass = CallbackInfo.getCallInfoClassName(this.returnType);
        }
        /*SL:366*/return this.callbackInfoClass;
    }
    
    public String getSimpleCallbackDescriptor() {
        /*SL:375*/return String.format("(L%s;)V", this.getCallbackInfoClass());
    }
    
    public String getCallbackDescriptor(final Type[] a1, final Type[] a2) {
        /*SL:386*/return this.getCallbackDescriptor(false, a1, a2, 0, 32767);
    }
    
    public String getCallbackDescriptor(final boolean a3, final Type[] a4, final Type[] a5, final int v1, int v2) {
        /*SL:400*/if (this.callbackDescriptor == null) {
            /*SL:401*/this.callbackDescriptor = String.format("(%sL%s;)V", this.method.desc.substring(1, this.method.desc.indexOf(41)), this.getCallbackInfoClass());
        }
        /*SL:405*/if (!a3 || a4 == null) {
            /*SL:406*/return this.callbackDescriptor;
        }
        final StringBuilder v3 = /*EL:409*/new StringBuilder(this.callbackDescriptor.substring(0, this.callbackDescriptor.indexOf(41)));
        /*SL:410*/for (int a6 = v1; a6 < a4.length && v2 > 0; ++a6) {
            /*SL:411*/if (a4[a6] != null) {
                /*SL:412*/v3.append(a4[a6].getDescriptor());
                /*SL:413*/--v2;
            }
        }
        /*SL:417*/return v3.append(")V").toString();
    }
    
    @Override
    public String toString() {
        /*SL:422*/return String.format("%s::%s%s", this.classNode.name, this.method.name, this.method.desc);
    }
    
    @Override
    public int compareTo(final Target a1) {
        /*SL:427*/if (a1 == null) {
            /*SL:428*/return Integer.MAX_VALUE;
        }
        /*SL:430*/return this.toString().compareTo(a1.toString());
    }
    
    public int indexOf(final InjectionNodes.InjectionNode a1) {
        /*SL:440*/return this.insns.indexOf(a1.getCurrentTarget());
    }
    
    public int indexOf(final AbstractInsnNode a1) {
        /*SL:450*/return this.insns.indexOf(a1);
    }
    
    public AbstractInsnNode get(final int a1) {
        /*SL:460*/return this.insns.get(a1);
    }
    
    @Override
    public Iterator<AbstractInsnNode> iterator() {
        /*SL:468*/return this.insns.iterator();
    }
    
    public MethodInsnNode findInitNodeFor(final TypeInsnNode v-2) {
        final int index = /*EL:479*/this.indexOf(v-2);
        final Iterator<AbstractInsnNode> v0 = /*EL:480*/this.insns.iterator(index);
        while (v0.hasNext()) {
            final AbstractInsnNode v = /*EL:481*/v0.next();
            /*SL:482*/if (v instanceof MethodInsnNode && v.getOpcode() == 183) {
                final MethodInsnNode a1 = /*EL:483*/(MethodInsnNode)v;
                /*SL:484*/if ("<init>".equals(a1.name) && a1.owner.equals(v-2.desc)) {
                    /*SL:485*/return a1;
                }
                continue;
            }
        }
        /*SL:489*/return null;
    }
    
    public MethodInsnNode findSuperInitNode() {
        /*SL:500*/if (!this.isCtor) {
            /*SL:501*/return null;
        }
        /*SL:504*/return Bytecode.findSuperInit(this.method, ClassInfo.forName(this.classNode.name).getSuperName());
    }
    
    public void insertBefore(final InjectionNodes.InjectionNode a1, final InsnList a2) {
        /*SL:514*/this.insns.insertBefore(a1.getCurrentTarget(), a2);
    }
    
    public void insertBefore(final AbstractInsnNode a1, final InsnList a2) {
        /*SL:524*/this.insns.insertBefore(a1, a2);
    }
    
    public void replaceNode(final AbstractInsnNode a1, final AbstractInsnNode a2) {
        /*SL:535*/this.insns.insertBefore(a1, a2);
        /*SL:536*/this.insns.remove(a1);
        /*SL:537*/this.injectionNodes.replace(a1, a2);
    }
    
    public void replaceNode(final AbstractInsnNode a1, final AbstractInsnNode a2, final InsnList a3) {
        /*SL:549*/this.insns.insertBefore(a1, a3);
        /*SL:550*/this.insns.remove(a1);
        /*SL:551*/this.injectionNodes.replace(a1, a2);
    }
    
    public void wrapNode(final AbstractInsnNode a1, final AbstractInsnNode a2, final InsnList a3, final InsnList a4) {
        /*SL:564*/this.insns.insertBefore(a1, a3);
        /*SL:565*/this.insns.insert(a1, a4);
        /*SL:566*/this.injectionNodes.replace(a1, a2);
    }
    
    public void replaceNode(final AbstractInsnNode a1, final InsnList a2) {
        /*SL:577*/this.insns.insertBefore(a1, a2);
        /*SL:578*/this.removeNode(a1);
    }
    
    public void removeNode(final AbstractInsnNode a1) {
        /*SL:588*/this.insns.remove(a1);
        /*SL:589*/this.injectionNodes.remove(a1);
    }
    
    public void addLocalVariable(final int a1, final String a2, final String a3) {
        /*SL:600*/if (this.start == null) {
            /*SL:601*/this.start = new LabelNode(new Label());
            /*SL:602*/this.end = new LabelNode(new Label());
            /*SL:603*/this.insns.insert(this.start);
            /*SL:604*/this.insns.add(this.end);
        }
        /*SL:606*/this.addLocalVariable(a1, a2, a3, this.start, this.end);
    }
    
    private void addLocalVariable(final int a1, final String a2, final String a3, final LabelNode a4, final LabelNode a5) {
        /*SL:619*/if (this.method.localVariables == null) {
            /*SL:620*/this.method.localVariables = new ArrayList<LocalVariableNode>();
        }
        /*SL:622*/this.method.localVariables.add(new LocalVariableNode(a2, a3, null, a4, a5, a1));
    }
}
