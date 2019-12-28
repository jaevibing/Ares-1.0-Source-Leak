package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.tree.MultiANewArrayInsnNode;
import org.spongepowered.asm.lib.tree.InvokeDynamicInsnNode;
import java.util.List;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.ArrayList;
import org.spongepowered.asm.lib.tree.IincInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;

public class Frame<V extends java.lang.Object>
{
    private V returnValue;
    private V[] values;
    private int locals;
    private int top;
    
    public Frame(final int a1, final int a2) {
        this.values = new Value[a1 + a2];
        this.locals = a1;
    }
    
    public Frame(final Frame<? extends V> a1) {
        this(a1.locals, a1.values.length - a1.locals);
        this.init(a1);
    }
    
    public Frame<V> init(final Frame<? extends V> a1) {
        /*SL:111*/this.returnValue = a1.returnValue;
        /*SL:112*/System.arraycopy(a1.values, 0, this.values, 0, this.values.length);
        /*SL:113*/this.top = a1.top;
        /*SL:114*/return this;
    }
    
    public void setReturn(final V a1) {
        /*SL:125*/this.returnValue = (Value)a1;
    }
    
    public int getLocals() {
        /*SL:134*/return this.locals;
    }
    
    public int getMaxStackSize() {
        /*SL:143*/return this.values.length - this.locals;
    }
    
    public V getLocal(final int a1) throws IndexOutOfBoundsException {
        /*SL:156*/if (a1 >= this.locals) {
            /*SL:157*/throw new IndexOutOfBoundsException("Trying to access an inexistant local variable");
        }
        /*SL:160*/return (V)this.values[a1];
    }
    
    public void setLocal(final int a1, final V a2) throws IndexOutOfBoundsException {
        /*SL:175*/if (a1 >= this.locals) {
            /*SL:176*/throw new IndexOutOfBoundsException("Trying to access an inexistant local variable " + a1);
        }
        /*SL:179*/this.values[a1] = (Value)a2;
    }
    
    public int getStackSize() {
        /*SL:189*/return this.top;
    }
    
    public V getStack(final int a1) throws IndexOutOfBoundsException {
        /*SL:202*/return (V)this.values[a1 + this.locals];
    }
    
    public void clearStack() {
        /*SL:209*/this.top = 0;
    }
    
    public V pop() throws IndexOutOfBoundsException {
        /*SL:220*/if (this.top == 0) {
            /*SL:221*/throw new IndexOutOfBoundsException("Cannot pop operand off an empty stack.");
        }
        final Value[] values = /*EL:224*/this.values;
        final int top = this.top - 1;
        this.top = top;
        return (V)values[top + this.locals];
    }
    
    public void push(final V a1) throws IndexOutOfBoundsException {
        /*SL:236*/if (this.top + this.locals >= this.values.length) {
            /*SL:237*/throw new IndexOutOfBoundsException("Insufficient maximum stack size.");
        }
        /*SL:240*/this.values[this.top++ + this.locals] = (Value)a1;
    }
    
    public void execute(final AbstractInsnNode v-4, final Interpreter<V> v-3) throws AnalyzerException {
        /*SL:249*/switch (v-4.getOpcode()) {
            case 0: {
                /*SL:251*/break;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18: {
                /*SL:270*/this.push(v-3.newOperation(v-4));
                /*SL:271*/break;
            }
            case 21:
            case 22:
            case 23:
            case 24:
            case 25: {
                /*SL:277*/this.push(v-3.copyOperation(v-4, this.getLocal(((VarInsnNode)v-4).var)));
                /*SL:279*/break;
            }
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53: {
                V a2 = /*EL:288*/(V)this.pop();
                /*SL:289*/a2 = (V)this.pop();
                /*SL:290*/this.push(v-3.binaryOperation(v-4, (Value)a2, (Value)a2));
                /*SL:291*/break;
            }
            case 54:
            case 55:
            case 56:
            case 57:
            case 58: {
                final V a2 = /*EL:297*/(V)v-3.copyOperation(v-4, this.pop());
                final int v0 = /*EL:298*/((VarInsnNode)v-4).var;
                /*SL:299*/this.setLocal(v0, (Value)a2);
                /*SL:300*/if (((Value)a2).getSize() == 2) {
                    /*SL:301*/this.setLocal(v0 + 1, v-3.newValue((Type)null));
                }
                /*SL:303*/if (v0 > 0) {
                    final Value v = /*EL:304*/this.getLocal(v0 - 1);
                    /*SL:305*/if (v != null && v.getSize() == 2) {
                        /*SL:306*/this.setLocal(v0 - 1, v-3.newValue((Type)null));
                    }
                    /*SL:308*/break;
                }
                break;
            }
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86: {
                final V v2 = /*EL:318*/(V)this.pop();
                V a2 = /*EL:319*/(V)this.pop();
                /*SL:320*/a2 = (V)this.pop();
                /*SL:321*/v-3.ternaryOperation(v-4, (Value)a2, (Value)a2, (Value)v2);
                /*SL:322*/break;
            }
            case 87: {
                /*SL:324*/if (this.pop().getSize() == 2) {
                    /*SL:325*/throw new AnalyzerException(v-4, "Illegal use of POP");
                }
                break;
            }
            case 88: {
                /*SL:329*/if (this.pop().getSize() == 1 && /*EL:330*/this.pop().getSize() != 1) {
                    /*SL:331*/throw new AnalyzerException(v-4, "Illegal use of POP2");
                }
                break;
            }
            case 89: {
                final V a2 = /*EL:336*/(V)this.pop();
                /*SL:337*/if (((Value)a2).getSize() != 1) {
                    /*SL:338*/throw new AnalyzerException(v-4, "Illegal use of DUP");
                }
                /*SL:340*/this.push((Value)a2);
                /*SL:341*/this.push(v-3.copyOperation(v-4, (Value)a2));
                /*SL:342*/break;
            }
            case 90: {
                V a2 = /*EL:344*/(V)this.pop();
                /*SL:345*/a2 = (V)this.pop();
                /*SL:346*/if (((Value)a2).getSize() != 1 || ((Value)a2).getSize() != 1) {
                    /*SL:347*/throw new AnalyzerException(v-4, "Illegal use of DUP_X1");
                }
                /*SL:349*/this.push(v-3.copyOperation(v-4, (Value)a2));
                /*SL:350*/this.push((Value)a2);
                /*SL:351*/this.push((Value)a2);
                /*SL:352*/break;
            }
            case 91: {
                V a2 = /*EL:354*/(V)this.pop();
                /*SL:355*/if (((Value)a2).getSize() == 1) {
                    /*SL:356*/a2 = (V)this.pop();
                    /*SL:357*/if (((Value)a2).getSize() != 1) {
                        /*SL:367*/this.push(v-3.copyOperation(v-4, (Value)a2));
                        /*SL:368*/this.push((Value)a2);
                        /*SL:369*/this.push((Value)a2);
                        /*SL:370*/break;
                    }
                    final V v2 = (V)this.pop();
                    if (((Value)v2).getSize() == 1) {
                        this.push(v-3.copyOperation(v-4, (Value)a2));
                        this.push((Value)v2);
                        this.push((Value)a2);
                        this.push((Value)a2);
                        break;
                    }
                }
                /*SL:373*/throw new AnalyzerException(v-4, "Illegal use of DUP_X2");
            }
            case 92: {
                V a2 = /*EL:375*/(V)this.pop();
                /*SL:376*/if (((Value)a2).getSize() != 1) {
                    /*SL:386*/this.push((Value)a2);
                    /*SL:387*/this.push(v-3.copyOperation(v-4, (Value)a2));
                    /*SL:388*/break;
                }
                a2 = (V)this.pop();
                if (((Value)a2).getSize() == 1) {
                    this.push((Value)a2);
                    this.push((Value)a2);
                    this.push(v-3.copyOperation(v-4, (Value)a2));
                    this.push(v-3.copyOperation(v-4, (Value)a2));
                    break;
                }
                /*SL:390*/throw new AnalyzerException(v-4, "Illegal use of DUP2");
            }
            case 93: {
                V a2 = /*EL:392*/(V)this.pop();
                /*SL:393*/if (((Value)a2).getSize() == 1) {
                    /*SL:394*/a2 = (V)this.pop();
                    /*SL:395*/if (((Value)a2).getSize() == 1) {
                        final V v2 = /*EL:396*/(V)this.pop();
                        /*SL:397*/if (((Value)v2).getSize() == 1) {
                            /*SL:398*/this.push(v-3.copyOperation(v-4, (Value)a2));
                            /*SL:399*/this.push(v-3.copyOperation(v-4, (Value)a2));
                            /*SL:400*/this.push((Value)v2);
                            /*SL:401*/this.push((Value)a2);
                            /*SL:402*/this.push((Value)a2);
                            /*SL:403*/break;
                        }
                    }
                }
                else {
                    /*SL:407*/a2 = (V)this.pop();
                    /*SL:408*/if (((Value)a2).getSize() == 1) {
                        /*SL:409*/this.push(v-3.copyOperation(v-4, (Value)a2));
                        /*SL:410*/this.push((Value)a2);
                        /*SL:411*/this.push((Value)a2);
                        /*SL:412*/break;
                    }
                }
                /*SL:415*/throw new AnalyzerException(v-4, "Illegal use of DUP2_X1");
            }
            case 94: {
                V a2 = /*EL:417*/(V)this.pop();
                /*SL:418*/if (((Value)a2).getSize() == 1) {
                    /*SL:419*/a2 = (V)this.pop();
                    /*SL:420*/if (((Value)a2).getSize() == 1) {
                        final V v2 = /*EL:421*/(V)this.pop();
                        /*SL:422*/if (((Value)v2).getSize() != 1) {
                            /*SL:434*/this.push(v-3.copyOperation(v-4, (Value)a2));
                            /*SL:435*/this.push(v-3.copyOperation(v-4, (Value)a2));
                            /*SL:436*/this.push((Value)v2);
                            /*SL:437*/this.push((Value)a2);
                            /*SL:438*/this.push((Value)a2);
                            /*SL:439*/break;
                        }
                        final V v3 = (V)this.pop();
                        if (((Value)v3).getSize() == 1) {
                            this.push(v-3.copyOperation(v-4, (Value)a2));
                            this.push(v-3.copyOperation(v-4, (Value)a2));
                            this.push((Value)v3);
                            this.push((Value)v2);
                            this.push((Value)a2);
                            this.push((Value)a2);
                            break;
                        }
                    }
                }
                else {
                    /*SL:443*/a2 = (V)this.pop();
                    /*SL:444*/if (((Value)a2).getSize() != 1) {
                        /*SL:454*/this.push(v-3.copyOperation(v-4, (Value)a2));
                        /*SL:455*/this.push((Value)a2);
                        /*SL:456*/this.push((Value)a2);
                        /*SL:457*/break;
                    }
                    final V v2 = (V)this.pop();
                    if (((Value)v2).getSize() == 1) {
                        this.push(v-3.copyOperation(v-4, (Value)a2));
                        this.push((Value)v2);
                        this.push((Value)a2);
                        this.push((Value)a2);
                        break;
                    }
                }
                /*SL:460*/throw new AnalyzerException(v-4, "Illegal use of DUP2_X2");
            }
            case 95: {
                V a2 = /*EL:462*/(V)this.pop();
                /*SL:463*/a2 = (V)this.pop();
                /*SL:464*/if (((Value)a2).getSize() != 1 || ((Value)a2).getSize() != 1) {
                    /*SL:465*/throw new AnalyzerException(v-4, "Illegal use of SWAP");
                }
                /*SL:467*/this.push(v-3.copyOperation(v-4, (Value)a2));
                /*SL:468*/this.push(v-3.copyOperation(v-4, (Value)a2));
                /*SL:469*/break;
            }
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115: {
                V a2 = /*EL:490*/(V)this.pop();
                /*SL:491*/a2 = (V)this.pop();
                /*SL:492*/this.push(v-3.binaryOperation(v-4, (Value)a2, (Value)a2));
                /*SL:493*/break;
            }
            case 116:
            case 117:
            case 118:
            case 119: {
                /*SL:498*/this.push(v-3.unaryOperation(v-4, this.pop()));
                /*SL:499*/break;
            }
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131: {
                V a2 = /*EL:512*/(V)this.pop();
                /*SL:513*/a2 = (V)this.pop();
                /*SL:514*/this.push(v-3.binaryOperation(v-4, (Value)a2, (Value)a2));
                /*SL:515*/break;
            }
            case 132: {
                final int v0 = /*EL:517*/((IincInsnNode)v-4).var;
                /*SL:518*/this.setLocal(v0, v-3.unaryOperation(v-4, this.getLocal(v0)));
                /*SL:519*/break;
            }
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147: {
                /*SL:535*/this.push(v-3.unaryOperation(v-4, this.pop()));
                /*SL:536*/break;
            }
            case 148:
            case 149:
            case 150:
            case 151:
            case 152: {
                V a2 = /*EL:542*/(V)this.pop();
                /*SL:543*/a2 = (V)this.pop();
                /*SL:544*/this.push(v-3.binaryOperation(v-4, (Value)a2, (Value)a2));
                /*SL:545*/break;
            }
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158: {
                /*SL:552*/v-3.unaryOperation(v-4, this.pop());
                /*SL:553*/break;
            }
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166: {
                V a2 = /*EL:562*/(V)this.pop();
                /*SL:563*/a2 = (V)this.pop();
                /*SL:564*/v-3.binaryOperation(v-4, (Value)a2, (Value)a2);
                /*SL:565*/break;
            }
            case 167: {
                /*SL:567*/break;
            }
            case 168: {
                /*SL:569*/this.push(v-3.newOperation(v-4));
                /*SL:570*/break;
            }
            case 169: {
                /*SL:572*/break;
            }
            case 170:
            case 171: {
                /*SL:575*/v-3.unaryOperation(v-4, this.pop());
                /*SL:576*/break;
            }
            case 172:
            case 173:
            case 174:
            case 175:
            case 176: {
                final V a2 = /*EL:582*/(V)this.pop();
                /*SL:583*/v-3.unaryOperation(v-4, (Value)a2);
                /*SL:584*/v-3.returnOperation(v-4, (Value)a2, this.returnValue);
                /*SL:585*/break;
            }
            case 177: {
                /*SL:587*/if (this.returnValue != null) {
                    /*SL:588*/throw new AnalyzerException(v-4, "Incompatible return type");
                }
                break;
            }
            case 178: {
                /*SL:592*/this.push(v-3.newOperation(v-4));
                /*SL:593*/break;
            }
            case 179: {
                /*SL:595*/v-3.unaryOperation(v-4, this.pop());
                /*SL:596*/break;
            }
            case 180: {
                /*SL:598*/this.push(v-3.unaryOperation(v-4, this.pop()));
                /*SL:599*/break;
            }
            case 181: {
                V a2 = /*EL:601*/(V)this.pop();
                /*SL:602*/a2 = (V)this.pop();
                /*SL:603*/v-3.binaryOperation(v-4, (Value)a2, (Value)a2);
                /*SL:604*/break;
            }
            case 182:
            case 183:
            case 184:
            case 185: {
                final List<V> v4 = /*EL:609*/new ArrayList<V>();
                final String v5 = /*EL:610*/((MethodInsnNode)v-4).desc;
                /*SL:611*/for (int v6 = Type.getArgumentTypes(v5).length; v6 > 0; --v6) {
                    /*SL:612*/v4.add(0, (V)this.pop());
                }
                /*SL:614*/if (v-4.getOpcode() != 184) {
                    /*SL:615*/v4.add(0, (V)this.pop());
                }
                /*SL:617*/if (Type.getReturnType(v5) == Type.VOID_TYPE) {
                    /*SL:618*/v-3.naryOperation(v-4, (List)v4);
                    break;
                }
                /*SL:620*/this.push(v-3.naryOperation(v-4, (List)v4));
                /*SL:622*/break;
            }
            case 186: {
                final List<V> v4 = /*EL:625*/new ArrayList<V>();
                final String v5 = /*EL:626*/((InvokeDynamicInsnNode)v-4).desc;
                /*SL:627*/for (int v6 = Type.getArgumentTypes(v5).length; v6 > 0; --v6) {
                    /*SL:628*/v4.add(0, (V)this.pop());
                }
                /*SL:630*/if (Type.getReturnType(v5) == Type.VOID_TYPE) {
                    /*SL:631*/v-3.naryOperation(v-4, (List)v4);
                    break;
                }
                /*SL:633*/this.push(v-3.naryOperation(v-4, (List)v4));
                /*SL:635*/break;
            }
            case 187: {
                /*SL:638*/this.push(v-3.newOperation(v-4));
                /*SL:639*/break;
            }
            case 188:
            case 189:
            case 190: {
                /*SL:643*/this.push(v-3.unaryOperation(v-4, this.pop()));
                /*SL:644*/break;
            }
            case 191: {
                /*SL:646*/v-3.unaryOperation(v-4, this.pop());
                /*SL:647*/break;
            }
            case 192:
            case 193: {
                /*SL:650*/this.push(v-3.unaryOperation(v-4, this.pop()));
                /*SL:651*/break;
            }
            case 194:
            case 195: {
                /*SL:654*/v-3.unaryOperation(v-4, this.pop());
                /*SL:655*/break;
            }
            case 197: {
                final List<V> v4 = /*EL:657*/new ArrayList<V>();
                /*SL:658*/for (int v7 = ((MultiANewArrayInsnNode)v-4).dims; v7 > 0; --v7) {
                    /*SL:659*/v4.add(0, (V)this.pop());
                }
                /*SL:661*/this.push(v-3.naryOperation(v-4, (List)v4));
                /*SL:662*/break;
            }
            case 198:
            case 199: {
                /*SL:665*/v-3.unaryOperation(v-4, this.pop());
                /*SL:666*/break;
            }
            default: {
                /*SL:668*/throw new RuntimeException("Illegal opcode " + v-4.getOpcode());
            }
        }
    }
    
    public boolean merge(final Frame<? extends V> v2, final Interpreter<V> v3) throws AnalyzerException {
        /*SL:686*/if (this.top != v2.top) {
            /*SL:687*/throw new AnalyzerException(null, "Incompatible stack heights");
        }
        boolean v4 = /*EL:689*/false;
        /*SL:690*/for (V a2 = (V)0; a2 < this.locals + this.top; ++a2) {
            /*SL:691*/a2 = (V)v3.merge(this.values[a2], v2.values[a2]);
            /*SL:692*/if (!a2.equals((Object)this.values[a2])) {
                /*SL:693*/this.values[a2] = (Value)a2;
                /*SL:694*/v4 = true;
            }
        }
        /*SL:697*/return v4;
    }
    
    public boolean merge(final Frame<? extends V> v1, final boolean[] v2) {
        boolean v3 = /*EL:712*/false;
        /*SL:713*/for (int a1 = 0; a1 < this.locals; ++a1) {
            /*SL:714*/if (!v2[a1] && !this.values[a1].equals(v1.values[a1])) {
                /*SL:715*/this.values[a1] = v1.values[a1];
                /*SL:716*/v3 = true;
            }
        }
        /*SL:719*/return v3;
    }
    
    public String toString() {
        final StringBuilder v0 = /*EL:729*/new StringBuilder();
        /*SL:730*/for (int v = 0; v < this.getLocals(); ++v) {
            /*SL:731*/v0.append(this.getLocal(v));
        }
        /*SL:733*/v0.append(' ');
        /*SL:734*/for (int v = 0; v < this.getStackSize(); ++v) {
            /*SL:735*/v0.append(this.getStack(v).toString());
        }
        /*SL:737*/return v0.toString();
    }
}
