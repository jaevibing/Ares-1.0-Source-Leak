package org.spongepowered.asm.lib.tree.analysis;

import java.util.Map;
import org.spongepowered.asm.lib.tree.IincInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.TableSwitchInsnNode;
import org.spongepowered.asm.lib.tree.LookupSwitchInsnNode;
import org.spongepowered.asm.lib.Type;
import java.util.HashMap;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import java.util.ArrayList;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.TryCatchBlockNode;
import java.util.List;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.Opcodes;

public class Analyzer<V extends java.lang.Object> implements Opcodes
{
    private final Interpreter<V> interpreter;
    private int n;
    private InsnList insns;
    private List<TryCatchBlockNode>[] handlers;
    private Frame<V>[] frames;
    private Subroutine[] subroutines;
    private boolean[] queued;
    private int[] queue;
    private int top;
    
    public Analyzer(final Interpreter<V> a1) {
        this.interpreter = a1;
    }
    
    public Frame<V>[] analyze(final String v-2, final MethodNode v-1) throws AnalyzerException {
        /*SL:108*/if ((v-1.access & 0x500) != 0x0) {
            /*SL:110*/return this.frames = (Frame<V>[])new Frame[0];
        }
        /*SL:112*/this.n = v-1.instructions.size();
        /*SL:113*/this.insns = v-1.instructions;
        /*SL:114*/this.handlers = (List<TryCatchBlockNode>[])new List[this.n];
        /*SL:115*/this.frames = (Frame<V>[])new Frame[this.n];
        /*SL:116*/this.subroutines = new Subroutine[this.n];
        /*SL:117*/this.queued = new boolean[this.n];
        /*SL:118*/this.queue = new int[this.n];
        /*SL:119*/this.top = 0;
        /*SL:122*/for (int v0 = 0; v0 < v-1.tryCatchBlocks.size(); ++v0) {
            final TryCatchBlockNode v = /*EL:123*/v-1.tryCatchBlocks.get(v0);
            final int v2 = /*EL:124*/this.insns.indexOf(v.start);
            final int v3 = /*EL:125*/this.insns.indexOf(v.end);
            /*SL:126*/for (List<TryCatchBlockNode> a2 = (List<TryCatchBlockNode>)v2; a2 < v3; ++a2) {
                /*SL:127*/a2 = this.handlers[a2];
                /*SL:128*/if (a2 == null) {
                    /*SL:129*/a2 = new ArrayList<TryCatchBlockNode>();
                    /*SL:130*/this.handlers[a2] = a2;
                }
                /*SL:132*/a2.add(v);
            }
        }
        final Subroutine v4 = /*EL:137*/new Subroutine(null, v-1.maxLocals, null);
        final List<AbstractInsnNode> v5 = /*EL:138*/new ArrayList<AbstractInsnNode>();
        final Map<LabelNode, Subroutine> v6 = /*EL:139*/new HashMap<LabelNode, Subroutine>();
        /*SL:140*/this.findSubroutine(0, v4, v5);
        /*SL:141*/while (!v5.isEmpty()) {
            final JumpInsnNode v7 = /*EL:142*/v5.remove(0);
            Subroutine v8 = /*EL:143*/v6.get(v7.label);
            /*SL:144*/if (v8 == null) {
                /*SL:145*/v8 = new Subroutine(v7.label, v-1.maxLocals, v7);
                /*SL:146*/v6.put(v7.label, v8);
                /*SL:147*/this.findSubroutine(this.insns.indexOf(v7.label), v8, v5);
            }
            else {
                /*SL:149*/v8.callers.add(v7);
            }
        }
        /*SL:152*/for (int v3 = 0; v3 < this.n; ++v3) {
            /*SL:153*/if (this.subroutines[v3] != null && this.subroutines[v3].start == null) {
                /*SL:154*/this.subroutines[v3] = null;
            }
        }
        final Frame<V> v9 = /*EL:159*/this.newFrame(v-1.maxLocals, v-1.maxStack);
        final Frame<V> a3 = /*EL:160*/this.newFrame(v-1.maxLocals, v-1.maxStack);
        /*SL:161*/v9.setReturn(this.interpreter.newValue(Type.getReturnType(v-1.desc)));
        final Type[] v10 = /*EL:162*/Type.getArgumentTypes(v-1.desc);
        int v11 = /*EL:163*/0;
        /*SL:164*/if ((v-1.access & 0x8) == 0x0) {
            final Type v12 = /*EL:165*/Type.getObjectType(v-2);
            /*SL:166*/v9.setLocal(v11++, this.interpreter.newValue(v12));
        }
        /*SL:168*/for (int v13 = 0; v13 < v10.length; ++v13) {
            /*SL:169*/v9.setLocal(v11++, this.interpreter.newValue(v10[v13]));
            /*SL:170*/if (v10[v13].getSize() == 2) {
                /*SL:171*/v9.setLocal(v11++, this.interpreter.newValue((Type)null));
            }
        }
        /*SL:174*/while (v11 < v-1.maxLocals) {
            /*SL:175*/v9.setLocal(v11++, this.interpreter.newValue((Type)null));
        }
        /*SL:177*/this.merge(0, v9, null);
        /*SL:179*/this.init(v-2, v-1);
        /*SL:182*/while (this.top > 0) {
            final int[] queue = /*EL:183*/this.queue;
            final int top = this.top - 1;
            this.top = top;
            final int v13 = queue[top];
            final Frame<V> v14 = /*EL:184*/this.frames[v13];
            Subroutine v15 = /*EL:185*/this.subroutines[v13];
            /*SL:186*/this.queued[v13] = false;
            AbstractInsnNode v16 = /*EL:188*/null;
            try {
                /*SL:190*/v16 = v-1.instructions.get(v13);
                final int v17 = /*EL:191*/v16.getOpcode();
                final int v18 = /*EL:192*/v16.getType();
                /*SL:194*/if (v18 == 8 || v18 == 15 || v18 == 14) {
                    /*SL:197*/this.merge(v13 + 1, v14, v15);
                    /*SL:198*/this.newControlFlowEdge(v13, v13 + 1);
                }
                else {
                    /*SL:200*/v9.init((Frame<? extends V>)v14).execute(v16, this.interpreter);
                    /*SL:201*/v15 = ((v15 == null) ? null : v15.copy());
                    /*SL:203*/if (v16 instanceof JumpInsnNode) {
                        final JumpInsnNode v19 = /*EL:204*/(JumpInsnNode)v16;
                        /*SL:205*/if (v17 != 167 && v17 != 168) {
                            /*SL:206*/this.merge(v13 + 1, v9, v15);
                            /*SL:207*/this.newControlFlowEdge(v13, v13 + 1);
                        }
                        final int v20 = /*EL:209*/this.insns.indexOf(v19.label);
                        /*SL:210*/if (v17 == 168) {
                            /*SL:211*/this.merge(v20, v9, new Subroutine(v19.label, v-1.maxLocals, v19));
                        }
                        else {
                            /*SL:214*/this.merge(v20, v9, v15);
                        }
                        /*SL:216*/this.newControlFlowEdge(v13, v20);
                    }
                    else/*SL:217*/ if (v16 instanceof LookupSwitchInsnNode) {
                        final LookupSwitchInsnNode v21 = /*EL:218*/(LookupSwitchInsnNode)v16;
                        int v20 = /*EL:219*/this.insns.indexOf(v21.dflt);
                        /*SL:220*/this.merge(v20, v9, v15);
                        /*SL:221*/this.newControlFlowEdge(v13, v20);
                        /*SL:222*/for (int v22 = 0; v22 < v21.labels.size(); ++v22) {
                            final LabelNode v23 = /*EL:223*/v21.labels.get(v22);
                            /*SL:224*/v20 = this.insns.indexOf(v23);
                            /*SL:225*/this.merge(v20, v9, v15);
                            /*SL:226*/this.newControlFlowEdge(v13, v20);
                        }
                    }
                    else/*SL:228*/ if (v16 instanceof TableSwitchInsnNode) {
                        final TableSwitchInsnNode v24 = /*EL:229*/(TableSwitchInsnNode)v16;
                        int v20 = /*EL:230*/this.insns.indexOf(v24.dflt);
                        /*SL:231*/this.merge(v20, v9, v15);
                        /*SL:232*/this.newControlFlowEdge(v13, v20);
                        /*SL:233*/for (int v22 = 0; v22 < v24.labels.size(); ++v22) {
                            final LabelNode v23 = /*EL:234*/v24.labels.get(v22);
                            /*SL:235*/v20 = this.insns.indexOf(v23);
                            /*SL:236*/this.merge(v20, v9, v15);
                            /*SL:237*/this.newControlFlowEdge(v13, v20);
                        }
                    }
                    else/*SL:239*/ if (v17 == 169) {
                        /*SL:240*/if (v15 == null) {
                            /*SL:241*/throw new AnalyzerException(v16, "RET instruction outside of a sub routine");
                        }
                        /*SL:244*/for (int v25 = 0; v25 < v15.callers.size(); ++v25) {
                            final JumpInsnNode v26 = /*EL:245*/v15.callers.get(v25);
                            final int v22 = /*EL:246*/this.insns.indexOf(v26);
                            /*SL:247*/if (this.frames[v22] != null) {
                                /*SL:248*/this.merge(v22 + 1, this.frames[v22], v9, this.subroutines[v22], v15.access);
                                /*SL:250*/this.newControlFlowEdge(v13, v22 + 1);
                            }
                        }
                    }
                    else/*SL:253*/ if (v17 != 191 && (v17 < 172 || v17 > 177)) {
                        /*SL:255*/if (v15 != null) {
                            /*SL:256*/if (v16 instanceof VarInsnNode) {
                                final int v25 = /*EL:257*/((VarInsnNode)v16).var;
                                /*SL:258*/v15.access[v25] = true;
                                /*SL:259*/if (v17 == 22 || v17 == 24 || v17 == 55 || v17 == 57) {
                                    /*SL:262*/v15.access[v25 + 1] = true;
                                }
                            }
                            else/*SL:264*/ if (v16 instanceof IincInsnNode) {
                                final int v25 = /*EL:265*/((IincInsnNode)v16).var;
                                /*SL:266*/v15.access[v25] = true;
                            }
                        }
                        /*SL:269*/this.merge(v13 + 1, v9, v15);
                        /*SL:270*/this.newControlFlowEdge(v13, v13 + 1);
                    }
                }
                final List<TryCatchBlockNode> v27 = /*EL:274*/this.handlers[v13];
                /*SL:275*/if (v27 == null) {
                    continue;
                }
                /*SL:276*/for (int v20 = 0; v20 < v27.size(); ++v20) {
                    final TryCatchBlockNode v28 = /*EL:277*/v27.get(v20);
                    Type v29;
                    /*SL:279*/if (v28.type == null) {
                        /*SL:280*/v29 = Type.getObjectType("java/lang/Throwable");
                    }
                    else {
                        /*SL:282*/v29 = Type.getObjectType(v28.type);
                    }
                    final int v30 = /*EL:284*/this.insns.indexOf(v28.handler);
                    /*SL:285*/if (this.newControlFlowExceptionEdge(v13, v28)) {
                        /*SL:286*/a3.init((Frame<? extends V>)v14);
                        /*SL:287*/a3.clearStack();
                        /*SL:288*/a3.push(this.interpreter.newValue(v29));
                        /*SL:289*/this.merge(v30, a3, v15);
                    }
                }
            }
            catch (AnalyzerException v31) {
                /*SL:294*/throw new AnalyzerException(v31.node, "Error at instruction " + v13 + ": " + v31.getMessage(), /*EL:295*/v31);
            }
            catch (Exception v32) {
                /*SL:297*/throw new AnalyzerException(v16, "Error at instruction " + v13 + ": " + v32.getMessage(), /*EL:298*/v32);
            }
        }
        /*SL:302*/return this.frames;
    }
    
    private void findSubroutine(int v-3, final Subroutine v-2, final List<AbstractInsnNode> v-1) throws AnalyzerException {
        /*SL:308*/while (v-3 >= 0 && v-3 < this.n) {
            /*SL:312*/if (this.subroutines[v-3] != null) {
                /*SL:313*/return;
            }
            /*SL:315*/this.subroutines[v-3] = v-2.copy();
            final AbstractInsnNode v0 = /*EL:316*/this.insns.get(v-3);
            /*SL:319*/if (v0 instanceof JumpInsnNode) {
                /*SL:320*/if (v0.getOpcode() == 168) {
                    /*SL:322*/v-1.add(v0);
                }
                else {
                    final JumpInsnNode a1 = /*EL:324*/(JumpInsnNode)v0;
                    /*SL:325*/this.findSubroutine(this.insns.indexOf(a1.label), v-2, v-1);
                }
            }
            else/*SL:327*/ if (v0 instanceof TableSwitchInsnNode) {
                final TableSwitchInsnNode v = /*EL:328*/(TableSwitchInsnNode)v0;
                /*SL:329*/this.findSubroutine(this.insns.indexOf(v.dflt), v-2, v-1);
                /*SL:330*/for (int a2 = v.labels.size() - 1; a2 >= 0; --a2) {
                    final LabelNode a3 = /*EL:331*/v.labels.get(a2);
                    /*SL:332*/this.findSubroutine(this.insns.indexOf(a3), v-2, v-1);
                }
            }
            else/*SL:334*/ if (v0 instanceof LookupSwitchInsnNode) {
                final LookupSwitchInsnNode v2 = /*EL:335*/(LookupSwitchInsnNode)v0;
                /*SL:336*/this.findSubroutine(this.insns.indexOf(v2.dflt), v-2, v-1);
                /*SL:337*/for (int v3 = v2.labels.size() - 1; v3 >= 0; --v3) {
                    final LabelNode v4 = /*EL:338*/v2.labels.get(v3);
                    /*SL:339*/this.findSubroutine(this.insns.indexOf(v4), v-2, v-1);
                }
            }
            final List<TryCatchBlockNode> a4 = /*EL:344*/this.handlers[v-3];
            /*SL:345*/if (a4 != null) {
                /*SL:346*/for (int v3 = 0; v3 < a4.size(); ++v3) {
                    final TryCatchBlockNode v5 = /*EL:347*/a4.get(v3);
                    /*SL:348*/this.findSubroutine(this.insns.indexOf(v5.handler), v-2, v-1);
                }
            }
            /*SL:353*/switch (v0.getOpcode()) {
                case 167:
                case 169:
                case 170:
                case 171:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 191: {
                    /*SL:365*/return;
                }
                default: {
                    /*SL:367*/++v-3;
                    /*SL:368*/continue;
                }
            }
        }
        throw new AnalyzerException(null, "Execution can fall off end of the code");
    }
    
    public Frame<V>[] getFrames() {
        /*SL:383*/return this.frames;
    }
    
    public List<TryCatchBlockNode> getHandlers(final int a1) {
        /*SL:395*/return this.handlers[a1];
    }
    
    protected void init(final String a1, final MethodNode a2) throws AnalyzerException {
    }
    
    protected Frame<V> newFrame(final int a1, final int a2) {
        /*SL:423*/return new Frame<V>(a1, a2);
    }
    
    protected Frame<V> newFrame(final Frame<? extends V> a1) {
        /*SL:434*/return new Frame<V>(a1);
    }
    
    protected void newControlFlowEdge(final int a1, final int a2) {
    }
    
    protected boolean newControlFlowExceptionEdge(final int a1, final int a2) {
        /*SL:468*/return true;
    }
    
    protected boolean newControlFlowExceptionEdge(final int a1, final TryCatchBlockNode a2) {
        /*SL:492*/return this.newControlFlowExceptionEdge(a1, this.insns.indexOf(a2.handler));
    }
    
    private void merge(final int a3, final Frame<V> v1, final Subroutine v2) throws AnalyzerException {
        final Frame<V> v3 = /*EL:499*/this.frames[a3];
        final Subroutine v4 = /*EL:500*/this.subroutines[a3];
        boolean v5 = false;
        /*SL:503*/if (v3 == null) {
            /*SL:504*/this.frames[a3] = this.newFrame((Frame<? extends V>)v1);
            final boolean a4 = /*EL:505*/true;
        }
        else {
            /*SL:507*/v5 = v3.merge((Frame<? extends V>)v1, this.interpreter);
        }
        /*SL:510*/if (v4 == null) {
            /*SL:511*/if (v2 != null) {
                /*SL:512*/this.subroutines[a3] = v2.copy();
                /*SL:513*/v5 = true;
            }
        }
        else/*SL:516*/ if (v2 != null) {
            /*SL:517*/v5 |= v4.merge(v2);
        }
        /*SL:520*/if (v5 && !this.queued[a3]) {
            /*SL:521*/this.queued[a3] = true;
            /*SL:522*/this.queue[this.top++] = a3;
        }
    }
    
    private void merge(final int a3, final Frame<V> a4, final Frame<V> a5, final Subroutine v1, final boolean[] v2) throws AnalyzerException {
        final Frame<V> v3 = /*EL:529*/this.frames[a3];
        final Subroutine v4 = /*EL:530*/this.subroutines[a3];
        /*SL:533*/a5.merge((Frame<? extends V>)a4, v2);
        boolean v5 = false;
        /*SL:535*/if (v3 == null) {
            /*SL:536*/this.frames[a3] = this.newFrame((Frame<? extends V>)a5);
            final boolean a6 = /*EL:537*/true;
        }
        else {
            /*SL:539*/v5 = v3.merge((Frame<? extends V>)a5, this.interpreter);
        }
        /*SL:542*/if (v4 != null && v1 != null) {
            /*SL:543*/v5 |= v4.merge(v1);
        }
        /*SL:545*/if (v5 && !this.queued[a3]) {
            /*SL:546*/this.queued[a3] = true;
            /*SL:547*/this.queue[this.top++] = a3;
        }
    }
}
