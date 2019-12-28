package org.spongepowered.asm.util.throwables;

import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.Type;
import java.util.Iterator;
import org.spongepowered.asm.util.Bytecode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.mixin.throwables.MixinException;

public class SyntheticBridgeException extends MixinException
{
    private static final long serialVersionUID = 1L;
    private final Problem problem;
    private final String name;
    private final String desc;
    private final int index;
    private final AbstractInsnNode a;
    private final AbstractInsnNode b;
    
    public SyntheticBridgeException(final Problem a1, final String a2, final String a3, final int a4, final AbstractInsnNode a5, final AbstractInsnNode a6) {
        super(a1.getMessage(a2, a3, a4, a5, a6));
        this.problem = a1;
        this.name = a2;
        this.desc = a3;
        this.index = a4;
        this.a = a5;
        this.b = a6;
    }
    
    public void printAnalysis(final IMixinContext a1, final MethodNode a2, final MethodNode a3) {
        final PrettyPrinter v1 = /*EL:123*/new PrettyPrinter();
        /*SL:124*/v1.addWrapped(100, this.getMessage(), new Object[0]).hr();
        /*SL:125*/v1.add().kv("Method", (Object)(this.name + this.desc)).kv("Problem Type", this.problem).add().hr();
        final String v2 = /*EL:126*/Annotations.<String>getValue(Annotations.getVisible(a2, MixinMerged.class), "mixin");
        final String v3 = /*EL:127*/(v2 != null) ? v2 : a1.getTargetClassRef().replace('/', '.');
        /*SL:128*/this.printMethod(v1.add("Existing method").add().kv("Owner", (Object)v3).add(), a2).hr();
        /*SL:129*/this.printMethod(v1.add("Incoming method").add().kv("Owner", (Object)a1.getClassRef().replace('/', '.')).add(), a3).hr();
        /*SL:130*/this.printProblem(v1, a1, a2, a3).print(System.err);
    }
    
    private PrettyPrinter printMethod(final PrettyPrinter v1, final MethodNode v2) {
        int v3 = /*EL:134*/0;
        final Iterator<AbstractInsnNode> a1 = /*EL:135*/v2.instructions.iterator();
        while (a1.hasNext()) {
            /*SL:136*/v1.kv((v3 == this.index) ? ">>>>" : "", (Object)Bytecode.describeNode(a1.next()));
            ++v3;
        }
        /*SL:138*/return v1.add();
    }
    
    private PrettyPrinter printProblem(final PrettyPrinter v-14, final IMixinContext v-13, final MethodNode v-12, final MethodNode v-11) {
        final Type objectType = /*EL:142*/Type.getObjectType(v-13.getTargetClassRef());
        /*SL:144*/v-14.add("Analysis").add();
        /*SL:145*/switch (this.problem) {
            case BAD_INSN: {
                /*SL:147*/v-14.add("The bridge methods are not compatible because they contain incompatible opcodes");
                /*SL:148*/v-14.add("at index " + this.index + ":").add();
                /*SL:149*/v-14.kv("Existing opcode: %s", (Object)Bytecode.getOpcodeName(this.a));
                /*SL:150*/v-14.kv("Incoming opcode: %s", (Object)Bytecode.getOpcodeName(this.b)).add();
                /*SL:151*/v-14.add("This implies that the bridge methods are from different interfaces. This problem");
                /*SL:152*/v-14.add("may not be resolvable without changing the base interfaces.").add();
                /*SL:153*/break;
            }
            case BAD_LOAD: {
                /*SL:156*/v-14.add("The bridge methods are not compatible because they contain different variables at");
                /*SL:157*/v-14.add("opcode index " + this.index + ".").add();
                final ListIterator<AbstractInsnNode> iterator = /*EL:159*/v-12.instructions.iterator();
                final ListIterator<AbstractInsnNode> iterator2 = /*EL:160*/v-11.instructions.iterator();
                final Type[] argumentTypes = /*EL:162*/Type.getArgumentTypes(v-12.desc);
                final Type[] argumentTypes2 = /*EL:163*/Type.getArgumentTypes(v-11.desc);
                int n = /*EL:164*/0;
                while (iterator.hasNext() && iterator2.hasNext()) {
                    final AbstractInsnNode abstractInsnNode = /*EL:165*/iterator.next();
                    final AbstractInsnNode abstractInsnNode2 = /*EL:166*/iterator2.next();
                    /*SL:167*/if (abstractInsnNode instanceof VarInsnNode && abstractInsnNode2 instanceof VarInsnNode) {
                        VarInsnNode a2 = /*EL:168*/(VarInsnNode)abstractInsnNode;
                        /*SL:169*/a2 = (VarInsnNode)abstractInsnNode2;
                        final Type a3 = /*EL:171*/(a2.var > 0) ? argumentTypes[a2.var - 1] : objectType;
                        final Type v1 = /*EL:172*/(a2.var > 0) ? argumentTypes2[a2.var - 1] : objectType;
                        /*SL:173*/v-14.kv("Target " + n, "%8s %-2d %s", Bytecode.getOpcodeName(a2), a2.var, a3);
                        /*SL:174*/v-14.kv("Incoming " + n, "%8s %-2d %s", Bytecode.getOpcodeName(a2), a2.var, v1);
                        /*SL:176*/if (a3.equals(v1)) {
                            /*SL:177*/v-14.kv("", "Types match: %s", a3);
                        }
                        else/*SL:178*/ if (a3.getSort() != v1.getSort()) {
                            /*SL:179*/v-14.kv("", (Object)"Types are incompatible");
                        }
                        else/*SL:180*/ if (a3.getSort() == 10) {
                            final ClassInfo a4 = /*EL:181*/ClassInfo.getCommonSuperClassOrInterface(a3, v1);
                            /*SL:182*/v-14.kv("", "Common supertype: %s", a4);
                        }
                        /*SL:185*/v-14.add();
                    }
                    ++n;
                }
                /*SL:189*/v-14.add("Since this probably means that the methods come from different interfaces, you");
                /*SL:190*/v-14.add("may have a \"multiple inheritance\" problem, it may not be possible to implement");
                /*SL:191*/v-14.add("both root interfaces");
                /*SL:192*/break;
            }
            case BAD_CAST: {
                /*SL:195*/v-14.add("Incompatible CHECKCAST encountered at opcode " + this.index + ", this could indicate that the bridge");
                /*SL:196*/v-14.add("is casting down for contravariant generic types. It may be possible to coalesce the");
                /*SL:197*/v-14.add("bridges by adjusting the types in the target method.").add();
                final Type objectType2 = /*EL:199*/Type.getObjectType(((TypeInsnNode)this.a).desc);
                final Type objectType3 = /*EL:200*/Type.getObjectType(((TypeInsnNode)this.b).desc);
                /*SL:201*/v-14.kv("Target type", objectType2);
                /*SL:202*/v-14.kv("Incoming type", objectType3);
                /*SL:203*/v-14.kv("Common supertype", ClassInfo.getCommonSuperClassOrInterface(objectType2, objectType3)).add();
                /*SL:204*/break;
            }
            case BAD_INVOKE_NAME: {
                /*SL:207*/v-14.add("Incompatible invocation targets in synthetic bridge. This is extremely unusual");
                /*SL:208*/v-14.add("and implies that a remapping transformer has incorrectly remapped a method. This");
                /*SL:209*/v-14.add("is an unrecoverable error.");
                /*SL:210*/break;
            }
            case BAD_INVOKE_DESC: {
                final MethodInsnNode methodInsnNode = /*EL:213*/(MethodInsnNode)this.a;
                final MethodInsnNode methodInsnNode2 = /*EL:214*/(MethodInsnNode)this.b;
                final Type[] argumentTypes3 = /*EL:216*/Type.getArgumentTypes(methodInsnNode.desc);
                final Type[] v2 = /*EL:217*/Type.getArgumentTypes(methodInsnNode2.desc);
                /*SL:219*/if (argumentTypes3.length != v2.length) {
                    final int v3 = /*EL:220*/Type.getArgumentTypes(v-12.desc).length;
                    final String v4 = /*EL:221*/(argumentTypes3.length == v3) ? "The TARGET" : ((v2.length == v3) ? " The INCOMING" : "NEITHER");
                    /*SL:223*/v-14.add("Mismatched invocation descriptors in synthetic bridge implies that a remapping");
                    /*SL:224*/v-14.add("transformer has incorrectly coalesced a bridge method with a conflicting name.");
                    /*SL:225*/v-14.add("Overlapping bridge methods should always have the same number of arguments, yet");
                    /*SL:226*/v-14.add("the target method has %d arguments, the incoming method has %d. This is an", argumentTypes3.length, v2.length);
                    /*SL:227*/v-14.add("unrecoverable error. %s method has the expected arg count of %d", v4, v3);
                    /*SL:228*/break;
                }
                final Type v1 = /*EL:231*/Type.getReturnType(methodInsnNode.desc);
                final Type v5 = /*EL:232*/Type.getReturnType(methodInsnNode2.desc);
                /*SL:234*/v-14.add("Incompatible invocation descriptors in synthetic bridge implies that generified");
                /*SL:235*/v-14.add("types are incompatible over one or more generic superclasses or interfaces. It may");
                /*SL:236*/v-14.add("be possible to adjust the generic types on implemented members to rectify this");
                /*SL:237*/v-14.add("problem by coalescing the appropriate generic types.").add();
                /*SL:239*/this.printTypeComparison(v-14, "return type", v1, v5);
                /*SL:240*/for (int v6 = 0; v6 < argumentTypes3.length; ++v6) {
                    /*SL:241*/this.printTypeComparison(v-14, "arg " + v6, argumentTypes3[v6], v2[v6]);
                }
                /*SL:244*/break;
            }
            case BAD_LENGTH: {
                /*SL:247*/v-14.add("Mismatched bridge method length implies the bridge methods are incompatible");
                /*SL:248*/v-14.add("and may originate from different superinterfaces. This is an unrecoverable");
                /*SL:249*/v-14.add("error.").add();
                break;
            }
        }
        /*SL:256*/return v-14;
    }
    
    private PrettyPrinter printTypeComparison(final PrettyPrinter a3, final String a4, final Type v1, final Type v2) {
        /*SL:260*/a3.kv("Target " + a4, "%s", v1);
        /*SL:261*/a3.kv("Incoming " + a4, "%s", v2);
        /*SL:263*/if (v1.equals(v2)) {
            /*SL:264*/a3.kv("Analysis", "Types match: %s", v1);
        }
        else/*SL:265*/ if (v1.getSort() != v2.getSort()) {
            /*SL:266*/a3.kv("Analysis", (Object)"Types are incompatible");
        }
        else/*SL:267*/ if (v1.getSort() == 10) {
            final ClassInfo a5 = /*EL:268*/ClassInfo.getCommonSuperClassOrInterface(v1, v2);
            /*SL:269*/a3.kv("Analysis", "Common supertype: L%s;", a5);
        }
        /*SL:272*/return a3.add();
    }
    
    public enum Problem
    {
        BAD_INSN("Conflicting opcodes %4$s and %5$s at offset %3$d in synthetic bridge method %1$s%2$s"), 
        BAD_LOAD("Conflicting variable access at offset %3$d in synthetic bridge method %1$s%2$s"), 
        BAD_CAST("Conflicting type cast at offset %3$d in synthetic bridge method %1$s%2$s"), 
        BAD_INVOKE_NAME("Conflicting synthetic bridge target method name in synthetic bridge method %1$s%2$s Existing:%6$s Incoming:%7$s"), 
        BAD_INVOKE_DESC("Conflicting synthetic bridge target method descriptor in synthetic bridge method %1$s%2$s Existing:%8$s Incoming:%9$s"), 
        BAD_LENGTH("Mismatched bridge method length for synthetic bridge method %1$s%2$s unexpected extra opcode at offset %3$d");
        
        private final String message;
        
        private Problem(final String a1) {
            this.message = a1;
        }
        
        String getMessage(final String a1, final String a2, final int a3, final AbstractInsnNode a4, final AbstractInsnNode a5) {
            /*SL:72*/return String.format(this.message, a1, a2, a3, Bytecode.getOpcodeName(a4), Bytecode.getOpcodeName(a4), getInsnName(a4), getInsnName(/*EL:73*/a5), getInsnDesc(a4), getInsnDesc(a5));
        }
        
        private static String getInsnName(final AbstractInsnNode a1) {
            /*SL:77*/return (a1 instanceof MethodInsnNode) ? ((MethodInsnNode)a1).name : "";
        }
        
        private static String getInsnDesc(final AbstractInsnNode a1) {
            /*SL:81*/return (a1 instanceof MethodInsnNode) ? ((MethodInsnNode)a1).desc : "";
        }
    }
}
