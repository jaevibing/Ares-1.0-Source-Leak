package org.spongepowered.asm.lib;

class Frame
{
    static final int DIM = -268435456;
    static final int ARRAY_OF = 268435456;
    static final int ELEMENT_OF = -268435456;
    static final int KIND = 251658240;
    static final int TOP_IF_LONG_OR_DOUBLE = 8388608;
    static final int VALUE = 8388607;
    static final int BASE_KIND = 267386880;
    static final int BASE_VALUE = 1048575;
    static final int BASE = 16777216;
    static final int OBJECT = 24117248;
    static final int UNINITIALIZED = 25165824;
    private static final int LOCAL = 33554432;
    private static final int STACK = 50331648;
    static final int TOP = 16777216;
    static final int BOOLEAN = 16777225;
    static final int BYTE = 16777226;
    static final int CHAR = 16777227;
    static final int SHORT = 16777228;
    static final int INTEGER = 16777217;
    static final int FLOAT = 16777218;
    static final int DOUBLE = 16777219;
    static final int LONG = 16777220;
    static final int NULL = 16777221;
    static final int UNINITIALIZED_THIS = 16777222;
    static final int[] SIZE;
    Label owner;
    int[] inputLocals;
    int[] inputStack;
    private int[] outputLocals;
    private int[] outputStack;
    int outputStackTop;
    private int initializationCount;
    private int[] initializations;
    
    final void set(final ClassWriter a3, final int a4, final Object[] a5, final int v1, final Object[] v2) {
        /*SL:549*/for (int v3 = convert(a3, a4, a5, this.inputLocals); v3 < a5.length; /*SL:550*/this.inputLocals[v3++] = 16777216) {}
        int v4 = /*EL:552*/0;
        /*SL:553*/for (int a6 = 0; a6 < v1; ++a6) {
            /*SL:554*/if (v2[a6] == Opcodes.LONG || v2[a6] == Opcodes.DOUBLE) {
                /*SL:555*/++v4;
            }
        }
        convert(/*EL:559*/a3, v1, v2, this.inputStack = new int[v1 + v4]);
        /*SL:560*/this.outputStackTop = 0;
        /*SL:561*/this.initializationCount = 0;
    }
    
    private static int convert(final ClassWriter a2, final int a3, final Object[] a4, final int[] v1) {
        int v2 = /*EL:589*/0;
        /*SL:590*/for (int a5 = 0; a5 < a3; ++a5) {
            /*SL:591*/if (a4[a5] instanceof Integer) {
                /*SL:592*/v1[v2++] = (0x1000000 | (int)a4[a5]);
                /*SL:593*/if (a4[a5] == Opcodes.LONG || a4[a5] == Opcodes.DOUBLE) {
                    /*SL:594*/v1[v2++] = 16777216;
                }
            }
            else/*SL:596*/ if (a4[a5] instanceof String) {
                /*SL:597*/v1[v2++] = type(a2, Type.getObjectType((String)a4[a5]).getDescriptor());
            }
            else {
                /*SL:601*/v1[v2++] = (0x1800000 | a2.addUninitializedType("", ((Label)a4[a5]).position));
            }
        }
        /*SL:605*/return v2;
    }
    
    final void set(final Frame a1) {
        /*SL:618*/this.inputLocals = a1.inputLocals;
        /*SL:619*/this.inputStack = a1.inputStack;
        /*SL:620*/this.outputLocals = a1.outputLocals;
        /*SL:621*/this.outputStack = a1.outputStack;
        /*SL:622*/this.outputStackTop = a1.outputStackTop;
        /*SL:623*/this.initializationCount = a1.initializationCount;
        /*SL:624*/this.initializations = a1.initializations;
    }
    
    private int get(final int v2) {
        /*SL:635*/if (this.outputLocals == null || v2 >= this.outputLocals.length) {
            /*SL:638*/return 0x2000000 | v2;
        }
        int a1 = /*EL:640*/this.outputLocals[v2];
        /*SL:641*/if (a1 == 0) {
            final int[] outputLocals = /*EL:644*/this.outputLocals;
            final int n = 0x2000000 | v2;
            outputLocals[v2] = n;
            a1 = n;
        }
        /*SL:646*/return a1;
    }
    
    private void set(final int v1, final int v2) {
        /*SL:660*/if (this.outputLocals == null) {
            /*SL:661*/this.outputLocals = new int[10];
        }
        final int v3 = /*EL:663*/this.outputLocals.length;
        /*SL:664*/if (v1 >= v3) {
            final int[] a1 = /*EL:665*/new int[Math.max(v1 + 1, 2 * v3)];
            /*SL:666*/System.arraycopy(this.outputLocals, 0, a1, 0, v3);
            /*SL:667*/this.outputLocals = a1;
        }
        /*SL:670*/this.outputLocals[v1] = v2;
    }
    
    private void push(final int v2) {
        /*SL:681*/if (this.outputStack == null) {
            /*SL:682*/this.outputStack = new int[10];
        }
        final int v3 = /*EL:684*/this.outputStack.length;
        /*SL:685*/if (this.outputStackTop >= v3) {
            final int[] a1 = /*EL:686*/new int[Math.max(this.outputStackTop + 1, 2 * v3)];
            /*SL:687*/System.arraycopy(this.outputStack, 0, a1, 0, v3);
            /*SL:688*/this.outputStack = a1;
        }
        /*SL:691*/this.outputStack[this.outputStackTop++] = v2;
        final int v4 = /*EL:693*/this.owner.inputStackTop + this.outputStackTop;
        /*SL:694*/if (v4 > this.owner.outputStackMax) {
            /*SL:695*/this.owner.outputStackMax = v4;
        }
    }
    
    private void push(final ClassWriter a1, final String a2) {
        final int v1 = type(/*EL:710*/a1, a2);
        /*SL:711*/if (v1 != 0) {
            /*SL:712*/this.push(v1);
            /*SL:713*/if (v1 == 16777220 || v1 == 16777219) {
                /*SL:714*/this.push(16777216);
            }
        }
    }
    
    private static int type(final ClassWriter v-4, final String v-3) {
        final int n = /*EL:730*/(v-3.charAt(0) == '(') ? (v-3.indexOf(41) + 1) : 0;
        /*SL:731*/switch (v-3.charAt(n)) {
            case 'V': {
                /*SL:733*/return 0;
            }
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z': {
                /*SL:739*/return 16777217;
            }
            case 'F': {
                /*SL:741*/return 16777218;
            }
            case 'J': {
                /*SL:743*/return 16777220;
            }
            case 'D': {
                /*SL:745*/return 16777219;
            }
            case 'L': {
                final String a1 = /*EL:748*/v-3.substring(n + 1, v-3.length() - 1);
                /*SL:749*/return 0x1700000 | v-4.addType(a1);
            }
            default: {
                int v0;
                /*SL:755*/for (v0 = n + 1; v-3.charAt(v0) == '['; /*SL:756*/++v0) {}
                int v = 0;
                /*SL:758*/switch (v-3.charAt(v0)) {
                    case 'Z': {
                        final int a2 = /*EL:760*/16777225;
                        /*SL:761*/break;
                    }
                    case 'C': {
                        /*SL:763*/v = 16777227;
                        /*SL:764*/break;
                    }
                    case 'B': {
                        /*SL:766*/v = 16777226;
                        /*SL:767*/break;
                    }
                    case 'S': {
                        /*SL:769*/v = 16777228;
                        /*SL:770*/break;
                    }
                    case 'I': {
                        /*SL:772*/v = 16777217;
                        /*SL:773*/break;
                    }
                    case 'F': {
                        /*SL:775*/v = 16777218;
                        /*SL:776*/break;
                    }
                    case 'J': {
                        /*SL:778*/v = 16777220;
                        /*SL:779*/break;
                    }
                    case 'D': {
                        /*SL:781*/v = 16777219;
                        /*SL:782*/break;
                    }
                    default: {
                        final String substring = /*EL:786*/v-3.substring(v0 + 1, v-3.length() - 1);
                        /*SL:787*/v = (0x1700000 | v-4.addType(substring));
                        break;
                    }
                }
                /*SL:789*/return v0 - n << 28 | v;
            }
        }
    }
    
    private int pop() {
        /*SL:799*/if (this.outputStackTop > 0) {
            final int[] outputStack = /*EL:800*/this.outputStack;
            final int outputStackTop = this.outputStackTop - 1;
            this.outputStackTop = outputStackTop;
            return outputStack[outputStackTop];
        }
        final int n = /*EL:803*/50331648;
        final Label owner = this.owner;
        return n | -(--owner.inputStackTop);
    }
    
    private void pop(final int a1) {
        /*SL:814*/if (this.outputStackTop >= a1) {
            /*SL:815*/this.outputStackTop -= a1;
        }
        else {
            final Label owner = /*EL:820*/this.owner;
            owner.inputStackTop -= a1 - this.outputStackTop;
            /*SL:821*/this.outputStackTop = 0;
        }
    }
    
    private void pop(final String a1) {
        final char v1 = /*EL:834*/a1.charAt(0);
        /*SL:835*/if (v1 == '(') {
            /*SL:836*/this.pop((Type.getArgumentsAndReturnSizes(a1) >> 2) - 1);
        }
        else/*SL:837*/ if (v1 == 'J' || v1 == 'D') {
            /*SL:838*/this.pop(2);
        }
        else {
            /*SL:840*/this.pop(1);
        }
    }
    
    private void init(final int v2) {
        /*SL:853*/if (this.initializations == null) {
            /*SL:854*/this.initializations = new int[2];
        }
        final int v3 = /*EL:856*/this.initializations.length;
        /*SL:857*/if (this.initializationCount >= v3) {
            final int[] a1 = /*EL:858*/new int[Math.max(this.initializationCount + 1, 2 * v3)];
            /*SL:859*/System.arraycopy(this.initializations, 0, a1, 0, v3);
            /*SL:860*/this.initializations = a1;
        }
        /*SL:863*/this.initializations[this.initializationCount++] = v2;
    }
    
    private int init(final ClassWriter v-1, final int v0) {
        final int v;
        /*SL:879*/if (v0 == 16777222) {
            final int a1 = /*EL:880*/0x1700000 | v-1.addType(v-1.thisName);
        }
        else {
            /*SL:881*/if ((v0 & 0xFFF00000) != 0x1800000) {
                /*SL:885*/return v0;
            }
            final String a2 = v-1.typeTable[v0 & 0xFFFFF].strVal1;
            v = (0x1700000 | v-1.addType(a2));
        }
        /*SL:887*/for (int v2 = 0; v2 < this.initializationCount; ++v2) {
            int v3 = /*EL:888*/this.initializations[v2];
            final int v4 = /*EL:889*/v3 & 0xF0000000;
            final int v5 = /*EL:890*/v3 & 0xF000000;
            /*SL:891*/if (v5 == 33554432) {
                /*SL:892*/v3 = v4 + this.inputLocals[v3 & 0x7FFFFF];
            }
            else/*SL:893*/ if (v5 == 50331648) {
                /*SL:894*/v3 = v4 + this.inputStack[this.inputStack.length - (v3 & 0x7FFFFF)];
            }
            /*SL:896*/if (v0 == v3) {
                /*SL:897*/return v;
            }
        }
        /*SL:900*/return v0;
    }
    
    final void initInputFrame(final ClassWriter a4, final int v1, final Type[] v2, final int v3) {
        /*SL:918*/this.inputLocals = new int[v3];
        /*SL:919*/this.inputStack = new int[0];
        int v4 = /*EL:920*/0;
        /*SL:921*/if ((v1 & 0x8) == 0x0) {
            /*SL:922*/if ((v1 & 0x80000) == 0x0) {
                /*SL:923*/this.inputLocals[v4++] = (0x1700000 | a4.addType(a4.thisName));
            }
            else {
                /*SL:925*/this.inputLocals[v4++] = 16777222;
            }
        }
        /*SL:928*/for (int a5 = 0; a5 < v2.length; ++a5) {
            final int a6 = type(/*EL:929*/a4, v2[a5].getDescriptor());
            /*SL:930*/this.inputLocals[v4++] = a6;
            /*SL:931*/if (a6 == 16777220 || a6 == 16777219) {
                /*SL:932*/this.inputLocals[v4++] = 16777216;
            }
        }
        /*SL:935*/while (v4 < v3) {
            /*SL:936*/this.inputLocals[v4++] = 16777216;
        }
    }
    
    void execute(final int v-4, final int v-3, final ClassWriter v-2, final Item v-1) {
        Label_2260: {
            /*SL:955*/switch (v-4) {
                case 0:
                case 116:
                case 117:
                case 118:
                case 119:
                case 145:
                case 146:
                case 147:
                case 167:
                case 177: {
                    /*SL:966*/break;
                }
                case 1: {
                    /*SL:968*/this.push(16777221);
                    /*SL:969*/break;
                }
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 16:
                case 17:
                case 21: {
                    /*SL:980*/this.push(16777217);
                    /*SL:981*/break;
                }
                case 9:
                case 10:
                case 22: {
                    /*SL:985*/this.push(16777220);
                    /*SL:986*/this.push(16777216);
                    /*SL:987*/break;
                }
                case 11:
                case 12:
                case 13:
                case 23: {
                    /*SL:992*/this.push(16777218);
                    /*SL:993*/break;
                }
                case 14:
                case 15:
                case 24: {
                    /*SL:997*/this.push(16777219);
                    /*SL:998*/this.push(16777216);
                    /*SL:999*/break;
                }
                case 18: {
                    /*SL:1001*/switch (v-1.type) {
                        case 3: {
                            /*SL:1003*/this.push(16777217);
                            break Label_2260;
                        }
                        case 5: {
                            /*SL:1006*/this.push(16777220);
                            /*SL:1007*/this.push(16777216);
                            break Label_2260;
                        }
                        case 4: {
                            /*SL:1010*/this.push(16777218);
                            break Label_2260;
                        }
                        case 6: {
                            /*SL:1013*/this.push(16777219);
                            /*SL:1014*/this.push(16777216);
                            break Label_2260;
                        }
                        case 7: {
                            /*SL:1017*/this.push(0x1700000 | v-2.addType("java/lang/Class"));
                            break Label_2260;
                        }
                        case 8: {
                            /*SL:1020*/this.push(0x1700000 | v-2.addType("java/lang/String"));
                            break Label_2260;
                        }
                        case 16: {
                            /*SL:1023*/this.push(0x1700000 | v-2.addType("java/lang/invoke/MethodType"));
                            break Label_2260;
                        }
                        default: {
                            /*SL:1027*/this.push(0x1700000 | v-2.addType("java/lang/invoke/MethodHandle"));
                            break Label_2260;
                        }
                    }
                    break;
                }
                case 25: {
                    /*SL:1031*/this.push(this.get(v-3));
                    /*SL:1032*/break;
                }
                case 46:
                case 51:
                case 52:
                case 53: {
                    /*SL:1037*/this.pop(2);
                    /*SL:1038*/this.push(16777217);
                    /*SL:1039*/break;
                }
                case 47:
                case 143: {
                    /*SL:1042*/this.pop(2);
                    /*SL:1043*/this.push(16777220);
                    /*SL:1044*/this.push(16777216);
                    /*SL:1045*/break;
                }
                case 48: {
                    /*SL:1047*/this.pop(2);
                    /*SL:1048*/this.push(16777218);
                    /*SL:1049*/break;
                }
                case 49:
                case 138: {
                    /*SL:1052*/this.pop(2);
                    /*SL:1053*/this.push(16777219);
                    /*SL:1054*/this.push(16777216);
                    /*SL:1055*/break;
                }
                case 50: {
                    /*SL:1057*/this.pop(1);
                    final int a1 = /*EL:1058*/this.pop();
                    /*SL:1059*/this.push(-268435456 + a1);
                    /*SL:1060*/break;
                }
                case 54:
                case 56:
                case 58: {
                    final int a2 = /*EL:1064*/this.pop();
                    /*SL:1065*/this.set(v-3, a2);
                    /*SL:1066*/if (v-3 <= 0) {
                        break;
                    }
                    final int a3 = /*EL:1067*/this.get(v-3 - 1);
                    /*SL:1069*/if (a3 == 16777220 || a3 == 16777219) {
                        /*SL:1070*/this.set(v-3 - 1, 16777216);
                        break;
                    }
                    /*SL:1071*/if ((a3 & 0xF000000) != 0x1000000) {
                        /*SL:1072*/this.set(v-3 - 1, a3 | 0x800000);
                        break;
                    }
                    break;
                }
                case 55:
                case 57: {
                    /*SL:1078*/this.pop(1);
                    final int a4 = /*EL:1079*/this.pop();
                    /*SL:1080*/this.set(v-3, a4);
                    /*SL:1081*/this.set(v-3 + 1, 16777216);
                    /*SL:1082*/if (v-3 <= 0) {
                        break;
                    }
                    final int v1 = /*EL:1083*/this.get(v-3 - 1);
                    /*SL:1085*/if (v1 == 16777220 || v1 == 16777219) {
                        /*SL:1086*/this.set(v-3 - 1, 16777216);
                        break;
                    }
                    /*SL:1087*/if ((v1 & 0xF000000) != 0x1000000) {
                        /*SL:1088*/this.set(v-3 - 1, v1 | 0x800000);
                        break;
                    }
                    break;
                }
                case 79:
                case 81:
                case 83:
                case 84:
                case 85:
                case 86: {
                    /*SL:1098*/this.pop(3);
                    /*SL:1099*/break;
                }
                case 80:
                case 82: {
                    /*SL:1102*/this.pop(4);
                    /*SL:1103*/break;
                }
                case 87:
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 170:
                case 171:
                case 172:
                case 174:
                case 176:
                case 191:
                case 194:
                case 195:
                case 198:
                case 199: {
                    /*SL:1121*/this.pop(1);
                    /*SL:1122*/break;
                }
                case 88:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 173:
                case 175: {
                    /*SL:1134*/this.pop(2);
                    /*SL:1135*/break;
                }
                case 89: {
                    final int v2 = /*EL:1137*/this.pop();
                    /*SL:1138*/this.push(v2);
                    /*SL:1139*/this.push(v2);
                    /*SL:1140*/break;
                }
                case 90: {
                    final int v2 = /*EL:1142*/this.pop();
                    final int v1 = /*EL:1143*/this.pop();
                    /*SL:1144*/this.push(v2);
                    /*SL:1145*/this.push(v1);
                    /*SL:1146*/this.push(v2);
                    /*SL:1147*/break;
                }
                case 91: {
                    final int v2 = /*EL:1149*/this.pop();
                    final int v1 = /*EL:1150*/this.pop();
                    final int v3 = /*EL:1151*/this.pop();
                    /*SL:1152*/this.push(v2);
                    /*SL:1153*/this.push(v3);
                    /*SL:1154*/this.push(v1);
                    /*SL:1155*/this.push(v2);
                    /*SL:1156*/break;
                }
                case 92: {
                    final int v2 = /*EL:1158*/this.pop();
                    final int v1 = /*EL:1159*/this.pop();
                    /*SL:1160*/this.push(v1);
                    /*SL:1161*/this.push(v2);
                    /*SL:1162*/this.push(v1);
                    /*SL:1163*/this.push(v2);
                    /*SL:1164*/break;
                }
                case 93: {
                    final int v2 = /*EL:1166*/this.pop();
                    final int v1 = /*EL:1167*/this.pop();
                    final int v3 = /*EL:1168*/this.pop();
                    /*SL:1169*/this.push(v1);
                    /*SL:1170*/this.push(v2);
                    /*SL:1171*/this.push(v3);
                    /*SL:1172*/this.push(v1);
                    /*SL:1173*/this.push(v2);
                    /*SL:1174*/break;
                }
                case 94: {
                    final int v2 = /*EL:1176*/this.pop();
                    final int v1 = /*EL:1177*/this.pop();
                    final int v3 = /*EL:1178*/this.pop();
                    final int v4 = /*EL:1179*/this.pop();
                    /*SL:1180*/this.push(v1);
                    /*SL:1181*/this.push(v2);
                    /*SL:1182*/this.push(v4);
                    /*SL:1183*/this.push(v3);
                    /*SL:1184*/this.push(v1);
                    /*SL:1185*/this.push(v2);
                    /*SL:1186*/break;
                }
                case 95: {
                    final int v2 = /*EL:1188*/this.pop();
                    final int v1 = /*EL:1189*/this.pop();
                    /*SL:1190*/this.push(v2);
                    /*SL:1191*/this.push(v1);
                    /*SL:1192*/break;
                }
                case 96:
                case 100:
                case 104:
                case 108:
                case 112:
                case 120:
                case 122:
                case 124:
                case 126:
                case 128:
                case 130:
                case 136:
                case 142:
                case 149:
                case 150: {
                    /*SL:1208*/this.pop(2);
                    /*SL:1209*/this.push(16777217);
                    /*SL:1210*/break;
                }
                case 97:
                case 101:
                case 105:
                case 109:
                case 113:
                case 127:
                case 129:
                case 131: {
                    /*SL:1219*/this.pop(4);
                    /*SL:1220*/this.push(16777220);
                    /*SL:1221*/this.push(16777216);
                    /*SL:1222*/break;
                }
                case 98:
                case 102:
                case 106:
                case 110:
                case 114:
                case 137:
                case 144: {
                    /*SL:1230*/this.pop(2);
                    /*SL:1231*/this.push(16777218);
                    /*SL:1232*/break;
                }
                case 99:
                case 103:
                case 107:
                case 111:
                case 115: {
                    /*SL:1238*/this.pop(4);
                    /*SL:1239*/this.push(16777219);
                    /*SL:1240*/this.push(16777216);
                    /*SL:1241*/break;
                }
                case 121:
                case 123:
                case 125: {
                    /*SL:1245*/this.pop(3);
                    /*SL:1246*/this.push(16777220);
                    /*SL:1247*/this.push(16777216);
                    /*SL:1248*/break;
                }
                case 132: {
                    /*SL:1250*/this.set(v-3, 16777217);
                    /*SL:1251*/break;
                }
                case 133:
                case 140: {
                    /*SL:1254*/this.pop(1);
                    /*SL:1255*/this.push(16777220);
                    /*SL:1256*/this.push(16777216);
                    /*SL:1257*/break;
                }
                case 134: {
                    /*SL:1259*/this.pop(1);
                    /*SL:1260*/this.push(16777218);
                    /*SL:1261*/break;
                }
                case 135:
                case 141: {
                    /*SL:1264*/this.pop(1);
                    /*SL:1265*/this.push(16777219);
                    /*SL:1266*/this.push(16777216);
                    /*SL:1267*/break;
                }
                case 139:
                case 190:
                case 193: {
                    /*SL:1271*/this.pop(1);
                    /*SL:1272*/this.push(16777217);
                    /*SL:1273*/break;
                }
                case 148:
                case 151:
                case 152: {
                    /*SL:1277*/this.pop(4);
                    /*SL:1278*/this.push(16777217);
                    /*SL:1279*/break;
                }
                case 168:
                case 169: {
                    /*SL:1282*/throw new RuntimeException("JSR/RET are not supported with computeFrames option");
                }
                case 178: {
                    /*SL:1285*/this.push(v-2, v-1.strVal3);
                    /*SL:1286*/break;
                }
                case 179: {
                    /*SL:1288*/this.pop(v-1.strVal3);
                    /*SL:1289*/break;
                }
                case 180: {
                    /*SL:1291*/this.pop(1);
                    /*SL:1292*/this.push(v-2, v-1.strVal3);
                    /*SL:1293*/break;
                }
                case 181: {
                    /*SL:1295*/this.pop(v-1.strVal3);
                    /*SL:1296*/this.pop();
                    /*SL:1297*/break;
                }
                case 182:
                case 183:
                case 184:
                case 185: {
                    /*SL:1302*/this.pop(v-1.strVal3);
                    /*SL:1303*/if (v-4 != 184) {
                        final int v2 = /*EL:1304*/this.pop();
                        /*SL:1305*/if (v-4 == 183 && v-1.strVal2.charAt(0) == /*EL:1306*/'<') {
                            /*SL:1307*/this.init(v2);
                        }
                    }
                    /*SL:1310*/this.push(v-2, v-1.strVal3);
                    /*SL:1311*/break;
                }
                case 186: {
                    /*SL:1313*/this.pop(v-1.strVal2);
                    /*SL:1314*/this.push(v-2, v-1.strVal2);
                    /*SL:1315*/break;
                }
                case 187: {
                    /*SL:1317*/this.push(0x1800000 | v-2.addUninitializedType(v-1.strVal1, v-3));
                    /*SL:1318*/break;
                }
                case 188: {
                    /*SL:1320*/this.pop();
                    /*SL:1321*/switch (v-3) {
                        case 4: {
                            /*SL:1323*/this.push(285212681);
                            break Label_2260;
                        }
                        case 5: {
                            /*SL:1326*/this.push(285212683);
                            break Label_2260;
                        }
                        case 8: {
                            /*SL:1329*/this.push(285212682);
                            break Label_2260;
                        }
                        case 9: {
                            /*SL:1332*/this.push(285212684);
                            break Label_2260;
                        }
                        case 10: {
                            /*SL:1335*/this.push(285212673);
                            break Label_2260;
                        }
                        case 6: {
                            /*SL:1338*/this.push(285212674);
                            break Label_2260;
                        }
                        case 7: {
                            /*SL:1341*/this.push(285212675);
                            break Label_2260;
                        }
                        default: {
                            /*SL:1345*/this.push(285212676);
                            break Label_2260;
                        }
                    }
                    break;
                }
                case 189: {
                    final String v5 = /*EL:1350*/v-1.strVal1;
                    /*SL:1351*/this.pop();
                    /*SL:1352*/if (v5.charAt(0) == '[') {
                        /*SL:1353*/this.push(v-2, '[' + v5);
                        break;
                    }
                    /*SL:1355*/this.push(0x11700000 | v-2.addType(v5));
                    /*SL:1357*/break;
                }
                case 192: {
                    final String v5 = /*EL:1359*/v-1.strVal1;
                    /*SL:1360*/this.pop();
                    /*SL:1361*/if (v5.charAt(0) == '[') {
                        /*SL:1362*/this.push(v-2, v5);
                        break;
                    }
                    /*SL:1364*/this.push(0x1700000 | v-2.addType(v5));
                    /*SL:1366*/break;
                }
                default: {
                    /*SL:1369*/this.pop(v-3);
                    /*SL:1370*/this.push(v-2, v-1.strVal1);
                    break;
                }
            }
        }
    }
    
    final boolean merge(final ClassWriter v-6, final Frame v-5, final int v-4) {
        boolean b = /*EL:1391*/false;
        final int length = /*EL:1394*/this.inputLocals.length;
        final int length2 = /*EL:1395*/this.inputStack.length;
        /*SL:1396*/if (v-5.inputLocals == null) {
            /*SL:1397*/v-5.inputLocals = new int[length];
            /*SL:1398*/b = true;
        }
        /*SL:1401*/for (int v0 = 0; v0 < length; ++v0) {
            int v4 = 0;
            /*SL:1402*/if (this.outputLocals != null && v0 < this.outputLocals.length) {
                final int v = /*EL:1403*/this.outputLocals[v0];
                /*SL:1404*/if (v == 0) {
                    final int a1 = /*EL:1405*/this.inputLocals[v0];
                }
                else {
                    final int v2 = /*EL:1407*/v & 0xF0000000;
                    final int v3 = /*EL:1408*/v & 0xF000000;
                    /*SL:1409*/if (v3 == 16777216) {
                        final int a2 = /*EL:1410*/v;
                    }
                    else {
                        /*SL:1412*/if (v3 == 33554432) {
                            final int a3 = /*EL:1413*/v2 + this.inputLocals[v & 0x7FFFFF];
                        }
                        else {
                            /*SL:1415*/v4 = v2 + this.inputStack[length2 - (v & 0x7FFFFF)];
                        }
                        /*SL:1417*/if ((v & 0x800000) != 0x0 && (v4 == 16777220 || v4 == 16777219)) {
                            /*SL:1419*/v4 = 16777216;
                        }
                    }
                }
            }
            else {
                /*SL:1424*/v4 = this.inputLocals[v0];
            }
            /*SL:1426*/if (this.initializations != null) {
                /*SL:1427*/v4 = this.init(v-6, v4);
            }
            /*SL:1429*/b |= merge(v-6, v4, v-5.inputLocals, v0);
        }
        /*SL:1432*/if (v-4 > 0) {
            /*SL:1433*/for (int v0 = 0; v0 < length; ++v0) {
                final int v4 = /*EL:1434*/this.inputLocals[v0];
                /*SL:1435*/b |= merge(v-6, v4, v-5.inputLocals, v0);
            }
            /*SL:1437*/if (v-5.inputStack == null) {
                /*SL:1438*/v-5.inputStack = new int[1];
                /*SL:1439*/b = true;
            }
            /*SL:1441*/b |= merge(v-6, v-4, v-5.inputStack, 0);
            /*SL:1442*/return b;
        }
        final int v5 = /*EL:1445*/this.inputStack.length + this.owner.inputStackTop;
        /*SL:1446*/if (v-5.inputStack == null) {
            /*SL:1447*/v-5.inputStack = new int[v5 + this.outputStackTop];
            /*SL:1448*/b = true;
        }
        /*SL:1451*/for (int v0 = 0; v0 < v5; ++v0) {
            int v4 = /*EL:1452*/this.inputStack[v0];
            /*SL:1453*/if (this.initializations != null) {
                /*SL:1454*/v4 = this.init(v-6, v4);
            }
            /*SL:1456*/b |= merge(v-6, v4, v-5.inputStack, v0);
        }
        /*SL:1458*/for (int v0 = 0; v0 < this.outputStackTop; ++v0) {
            final int v = /*EL:1459*/this.outputStack[v0];
            final int v2 = /*EL:1460*/v & 0xF0000000;
            final int v3 = /*EL:1461*/v & 0xF000000;
            int v4;
            /*SL:1462*/if (v3 == 16777216) {
                /*SL:1463*/v4 = v;
            }
            else {
                /*SL:1465*/if (v3 == 33554432) {
                    /*SL:1466*/v4 = v2 + this.inputLocals[v & 0x7FFFFF];
                }
                else {
                    /*SL:1468*/v4 = v2 + this.inputStack[length2 - (v & 0x7FFFFF)];
                }
                /*SL:1470*/if ((v & 0x800000) != 0x0 && (v4 == 16777220 || v4 == 16777219)) {
                    /*SL:1472*/v4 = 16777216;
                }
            }
            /*SL:1475*/if (this.initializations != null) {
                /*SL:1476*/v4 = this.init(v-6, v4);
            }
            /*SL:1478*/b |= merge(v-6, v4, v-5.inputStack, v5 + v0);
        }
        /*SL:1480*/return b;
    }
    
    private static boolean merge(final ClassWriter v-6, int v-5, final int[] v-4, final int v-3) {
        final int n = /*EL:1501*/v-4[v-3];
        /*SL:1502*/if (n == v-5) {
            /*SL:1504*/return false;
        }
        /*SL:1506*/if ((v-5 & 0xFFFFFFF) == 0x1000005) {
            /*SL:1507*/if (n == 16777221) {
                /*SL:1508*/return false;
            }
            /*SL:1510*/v-5 = 16777221;
        }
        /*SL:1512*/if (n == 0) {
            /*SL:1514*/v-4[v-3] = v-5;
            /*SL:1515*/return true;
        }
        int n2 = 0;
        /*SL:1518*/if ((n & 0xFF00000) == 0x1700000 || (n & 0xF0000000) != 0x0) {
            /*SL:1520*/if (v-5 == 16777221) {
                /*SL:1522*/return false;
            }
            /*SL:1523*/if ((v-5 & 0xFFF00000) == (n & 0xFFF00000)) {
                /*SL:1525*/if ((n & 0xFF00000) == 0x1700000) {
                    final int a1 = /*EL:1529*/(v-5 & 0xF0000000) | 0x1700000 | v-6.getMergedType(v-5 & 0xFFFFF, n & 0xFFFFF);
                }
                else {
                    final int a2 = /*EL:1534*/-268435456 + (n & 0xF0000000);
                    final int a3 = /*EL:1535*/a2 | 0x1700000 | v-6.addType("java/lang/Object");
                }
            }
            else/*SL:1537*/ if ((v-5 & 0xFF00000) == 0x1700000 || (v-5 & 0xF0000000) != 0x0) {
                final int a4 = /*EL:1542*/(((v-5 & 0xF0000000) == 0x0 || (v-5 & 0xFF00000) == 0x1700000) ? 0 : -268435456) + (v-5 & 0xF0000000);
                final int v1 = /*EL:1544*/(((n & 0xF0000000) == 0x0 || (n & 0xFF00000) == 0x1700000) ? 0 : -268435456) + (n & 0xF0000000);
                /*SL:1547*/n2 = (Math.min(a4, v1) | 0x1700000 | v-6.addType("java/lang/Object"));
            }
            else {
                /*SL:1550*/n2 = 16777216;
            }
        }
        else/*SL:1552*/ if (n == 16777221) {
            /*SL:1555*/n2 = (((v-5 & 0xFF00000) == 0x1700000 || (v-5 & 0xF0000000) != 0x0) ? v-5 : 16777216);
        }
        else {
            /*SL:1558*/n2 = 16777216;
        }
        /*SL:1560*/if (n != n2) {
            /*SL:1561*/v-4[v-3] = n2;
            /*SL:1562*/return true;
        }
        /*SL:1564*/return false;
    }
    
    static {
        final int[] size = new int[202];
        final String v0 = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        for (int v = 0; v < size.length; ++v) {
            size[v] = v0.charAt(v) - 'E';
        }
        SIZE = size;
    }
}
