package org.spongepowered.asm.util;

import java.util.HashMap;
import org.spongepowered.asm.lib.tree.analysis.Frame;
import org.spongepowered.asm.lib.tree.analysis.AnalyzerException;
import org.spongepowered.asm.lib.tree.analysis.Interpreter;
import org.spongepowered.asm.lib.tree.analysis.BasicValue;
import org.spongepowered.asm.lib.tree.analysis.Analyzer;
import org.spongepowered.asm.util.asm.MixinVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.FrameNode;
import org.spongepowered.asm.util.throwables.LVTGeneratorException;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import java.util.List;
import java.util.Map;

public final class Locals
{
    private static final Map<String, List<LocalVariableNode>> calculatedLocalVariables;
    
    public static void loadLocals(final Type[] a1, final InsnList a2, int a3, int a4) {
        /*SL:80*/while (a3 < a1.length && a4 > 0) {
            /*SL:81*/if (a1[a3] != null) {
                /*SL:82*/a2.add(new VarInsnNode(a1[a3].getOpcode(21), a3));
                /*SL:83*/--a4;
            }
            ++a3;
        }
    }
    
    public static LocalVariableNode[] getLocalsAt(final ClassNode v-19, final MethodNode v-18, AbstractInsnNode v-17) {
        /*SL:131*/for (int a1 = 0; a1 < 3 && (v-17 instanceof LabelNode || v-17 instanceof LineNumberNode); /*SL:132*/v-17 = nextNode(v-18.instructions, v-17), ++a1) {}
        final ClassInfo forName = /*EL:135*/ClassInfo.forName(v-19.name);
        /*SL:136*/if (forName == null) {
            /*SL:137*/throw new LVTGeneratorException("Could not load class metadata for " + v-19.name + " generating LVT for " + v-18.name);
        }
        final ClassInfo.Method method = /*EL:139*/forName.findMethod(v-18);
        /*SL:140*/if (method == null) {
            /*SL:141*/throw new LVTGeneratorException("Could not locate method metadata for " + v-18.name + " generating LVT in " + v-19.name);
        }
        final List<ClassInfo.FrameData> frames = /*EL:143*/method.getFrames();
        final LocalVariableNode[] array = /*EL:145*/new LocalVariableNode[v-18.maxLocals];
        int a5 = /*EL:146*/0;
        int n = 0;
        /*SL:149*/if ((v-18.access & 0x8) == 0x0) {
            /*SL:150*/array[a5++] = new LocalVariableNode("this", v-19.name, null, null, null, 0);
        }
        /*SL:154*/for (final Type a2 : Type.getArgumentTypes(v-18.desc)) {
            /*SL:155*/array[a5] = new LocalVariableNode("arg" + n++, a2.toString(), null, null, null, a5);
            /*SL:156*/a5 += a2.getSize();
        }
        final int n2 = /*EL:159*/a5;
        int n3 = /*EL:160*/-1;
        int n4 = 0;
        /*SL:162*/for (final AbstractInsnNode abstractInsnNode : v-18.instructions) {
            /*SL:164*/if (abstractInsnNode instanceof FrameNode) {
                /*SL:165*/++n3;
                final FrameNode frameNode = /*EL:166*/(FrameNode)abstractInsnNode;
                final ClassInfo.FrameData frameData = /*EL:167*/(n3 < frames.size()) ? frames.get(n3) : null;
                /*SL:169*/n4 = ((frameData != null && frameData.type == 0) ? Math.min(n4, frameData.locals) : frameNode.local.size());
                /*SL:172*/for (int n5 = 0, j = 0; j < array.length; ++j, ++n5) {
                    final Object o = /*EL:174*/(n5 < frameNode.local.size()) ? frameNode.local.get(n5) : null;
                    /*SL:176*/if (o instanceof String) {
                        /*SL:177*/array[j] = getLocalVariableAt(v-19, v-18, v-17, j);
                    }
                    else/*SL:178*/ if (o instanceof Integer) {
                        final boolean a4 = /*EL:179*/o == Opcodes.UNINITIALIZED_THIS || o == Opcodes.NULL;
                        final boolean v1 = /*EL:180*/o == Opcodes.INTEGER || o == Opcodes.FLOAT;
                        final boolean v2 = /*EL:181*/o == Opcodes.DOUBLE || o == Opcodes.LONG;
                        /*SL:182*/if (o != Opcodes.TOP) {
                            /*SL:184*/if (a4) {
                                /*SL:185*/array[j] = null;
                            }
                            else {
                                /*SL:186*/if (!v1 && !v2) {
                                    /*SL:194*/throw new LVTGeneratorException("Unrecognised locals opcode " + o + " in locals array at position " + n5 + " in " + v-19.name + "." + v-18.name + v-18.desc);
                                }
                                array[j] = getLocalVariableAt(v-19, v-18, v-17, j);
                                if (v2) {
                                    ++j;
                                    array[j] = null;
                                }
                            }
                        }
                    }
                    else {
                        /*SL:197*/if (o != null) {
                            /*SL:202*/throw new LVTGeneratorException("Invalid value " + o + " in locals array at position " + n5 + " in " + v-19.name + "." + v-18.name + v-18.desc);
                        }
                        if (j >= n2 && j >= n4 && n4 > 0) {
                            array[j] = null;
                        }
                    }
                }
            }
            else/*SL:206*/ if (abstractInsnNode instanceof VarInsnNode) {
                final VarInsnNode varInsnNode = /*EL:207*/(VarInsnNode)abstractInsnNode;
                /*SL:208*/array[varInsnNode.var] = getLocalVariableAt(v-19, v-18, v-17, varInsnNode.var);
            }
            /*SL:211*/if (abstractInsnNode == v-17) {
                /*SL:212*/break;
            }
        }
        /*SL:217*/for (int k = 0; k < array.length; ++k) {
            /*SL:218*/if (array[k] != null && array[k].desc == null) {
                /*SL:219*/array[k] = null;
            }
        }
        /*SL:223*/return array;
    }
    
    public static LocalVariableNode getLocalVariableAt(final ClassNode a1, final MethodNode a2, final AbstractInsnNode a3, final int a4) {
        /*SL:239*/return getLocalVariableAt(a1, a2, a2.instructions.indexOf(a3), a4);
    }
    
    private static LocalVariableNode getLocalVariableAt(final ClassNode a3, final MethodNode a4, final int v1, final int v2) {
        LocalVariableNode v3 = /*EL:254*/null;
        LocalVariableNode v4 = /*EL:255*/null;
        /*SL:257*/for (final LocalVariableNode a5 : getLocalVariableTable(a3, a4)) {
            /*SL:258*/if (a5.index != v2) {
                /*SL:259*/continue;
            }
            /*SL:261*/if (isOpcodeInRange(a4.instructions, a5, v1)) {
                /*SL:262*/v3 = a5;
            }
            else {
                /*SL:263*/if (v3 != null) {
                    continue;
                }
                /*SL:264*/v4 = a5;
            }
        }
        /*SL:268*/if (v3 == null && !a4.localVariables.isEmpty()) {
            /*SL:269*/for (final LocalVariableNode a6 : getGeneratedLocalVariableTable(a3, a4)) {
                /*SL:270*/if (a6.index == v2 && isOpcodeInRange(a4.instructions, a6, v1)) {
                    /*SL:271*/v3 = a6;
                }
            }
        }
        /*SL:276*/return (v3 != null) ? v3 : v4;
    }
    
    private static boolean isOpcodeInRange(final InsnList a1, final LocalVariableNode a2, final int a3) {
        /*SL:280*/return a1.indexOf(a2.start) < a3 && a1.indexOf(a2.end) > a3;
    }
    
    public static List<LocalVariableNode> getLocalVariableTable(final ClassNode a1, final MethodNode a2) {
        /*SL:295*/if (a2.localVariables.isEmpty()) {
            /*SL:296*/return getGeneratedLocalVariableTable(a1, a2);
        }
        /*SL:298*/return a2.localVariables;
    }
    
    public static List<LocalVariableNode> getGeneratedLocalVariableTable(final ClassNode a1, final MethodNode a2) {
        final String v1 = /*EL:309*/String.format("%s.%s%s", a1.name, a2.name, a2.desc);
        List<LocalVariableNode> v2 = Locals.calculatedLocalVariables.get(/*EL:310*/v1);
        /*SL:311*/if (v2 != null) {
            /*SL:312*/return v2;
        }
        /*SL:315*/v2 = generateLocalVariableTable(a1, a2);
        Locals.calculatedLocalVariables.put(/*EL:316*/v1, v2);
        /*SL:317*/return v2;
    }
    
    public static List<LocalVariableNode> generateLocalVariableTable(final ClassNode v-16, final MethodNode v-15) {
        List<Type> a5 = /*EL:329*/null;
        /*SL:330*/if (v-16.interfaces != null) {
            /*SL:331*/a5 = new ArrayList<Type>();
            /*SL:332*/for (final String a1 : v-16.interfaces) {
                /*SL:333*/a5.add(Type.getObjectType(a1));
            }
        }
        Type objectType = /*EL:337*/null;
        /*SL:338*/if (v-16.superName != null) {
            /*SL:339*/objectType = Type.getObjectType(v-16.superName);
        }
        final Analyzer<BasicValue> a2 = /*EL:343*/new Analyzer<BasicValue>(/*EL:344*/new MixinVerifier(Type.getObjectType(v-16.name), objectType, a5, false));
        try {
            /*SL:346*/a2.analyze(v-16.name, v-15);
        }
        catch (AnalyzerException a3) {
            /*SL:348*/a3.printStackTrace();
        }
        final Frame<BasicValue>[] a4 = /*EL:352*/a2.getFrames();
        final int size = /*EL:355*/v-15.instructions.size();
        final List<LocalVariableNode> list = /*EL:358*/new ArrayList<LocalVariableNode>();
        final LocalVariableNode[] array = /*EL:360*/new LocalVariableNode[v-15.maxLocals];
        final BasicValue[] array2 = /*EL:361*/new BasicValue[v-15.maxLocals];
        final LabelNode[] array3 = /*EL:362*/new LabelNode[size];
        final String[] array4 = /*EL:363*/new String[v-15.maxLocals];
        /*SL:366*/for (int i = 0; i < size; ++i) {
            final Frame<BasicValue> frame = /*EL:367*/a4[i];
            /*SL:368*/if (frame != null) {
                LabelNode a6 = /*EL:371*/null;
                /*SL:373*/for (int j = 0; j < frame.getLocals(); ++j) {
                    final BasicValue v0 = /*EL:374*/(BasicValue)frame.getLocal(j);
                    /*SL:375*/if (v0 != null || array2[j] != null) {
                        /*SL:378*/if (v0 == null || !v0.equals(array2[j])) {
                            /*SL:382*/if (a6 == null) {
                                final AbstractInsnNode v = /*EL:383*/v-15.instructions.get(i);
                                /*SL:384*/if (v instanceof LabelNode) {
                                    /*SL:385*/a6 = (LabelNode)v;
                                }
                                else {
                                    /*SL:387*/a6 = (array3[i] = new LabelNode());
                                }
                            }
                            /*SL:391*/if (v0 == null && array2[j] != null) {
                                /*SL:392*/list.add(array[j]);
                                /*SL:393*/array[j].end = a6;
                                /*SL:394*/array[j] = null;
                            }
                            else/*SL:395*/ if (v0 != null) {
                                /*SL:396*/if (array2[j] != null) {
                                    /*SL:397*/list.add(array[j]);
                                    /*SL:398*/array[j].end = a6;
                                    /*SL:399*/array[j] = null;
                                }
                                final String v2 = /*EL:402*/(v0.getType() != null) ? v0.getType().getDescriptor() : array4[j];
                                /*SL:403*/array[j] = new LocalVariableNode("var" + j, v2, null, a6, null, j);
                                /*SL:404*/if (v2 != null) {
                                    /*SL:405*/array4[j] = v2;
                                }
                            }
                            /*SL:409*/array2[j] = v0;
                        }
                    }
                }
            }
        }
        LabelNode labelNode = /*EL:414*/null;
        /*SL:415*/for (int k = 0; k < array.length; ++k) {
            /*SL:416*/if (array[k] != null) {
                /*SL:417*/if (labelNode == null) {
                    /*SL:418*/labelNode = new LabelNode();
                    /*SL:419*/v-15.instructions.add(labelNode);
                }
                /*SL:422*/array[k].end = labelNode;
                /*SL:423*/list.add(array[k]);
            }
        }
        /*SL:428*/for (int k = size - 1; k >= 0; --k) {
            /*SL:429*/if (array3[k] != null) {
                /*SL:430*/v-15.instructions.insert(v-15.instructions.get(k), array3[k]);
            }
        }
        /*SL:434*/return list;
    }
    
    private static AbstractInsnNode nextNode(final InsnList a1, final AbstractInsnNode a2) {
        final int v1 = /*EL:446*/a1.indexOf(a2) + 1;
        /*SL:447*/if (v1 > 0 && v1 < a1.size()) {
            /*SL:448*/return a1.get(v1);
        }
        /*SL:450*/return a2;
    }
    
    static {
        calculatedLocalVariables = new HashMap<String, List<LocalVariableNode>>();
    }
}
