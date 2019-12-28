package javassist.bytecode.stackmap;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.MethodInfo;

public class TypedBlock extends BasicBlock
{
    public int stackTop;
    public int numLocals;
    public TypeData[] localsTypes;
    public TypeData[] stackTypes;
    
    public static TypedBlock[] makeBlocks(final MethodInfo a1, final CodeAttribute a2, final boolean a3) throws BadBytecode {
        final TypedBlock[] v1 = /*EL:39*/(TypedBlock[])new Maker().make(a1);
        /*SL:40*/if (a3 && v1.length < 2 && /*EL:41*/(v1.length == 0 || v1[0].incoming == 0)) {
            /*SL:42*/return null;
        }
        final ConstPool v2 = /*EL:44*/a1.getConstPool();
        final boolean v3 = /*EL:45*/(a1.getAccessFlags() & 0x8) != 0x0;
        /*SL:46*/v1[0].initFirstBlock(a2.getMaxStack(), a2.getMaxLocals(), v2.getClassName(), /*EL:47*/a1.getDescriptor(), v3, a1.isConstructor());
        /*SL:49*/return v1;
    }
    
    protected TypedBlock(final int a1) {
        super(a1);
        this.localsTypes = null;
    }
    
    @Override
    protected void toString2(final StringBuffer a1) {
        /*SL:58*/super.toString2(a1);
        /*SL:59*/a1.append(",\n stack={");
        /*SL:60*/this.printTypes(a1, this.stackTop, this.stackTypes);
        /*SL:61*/a1.append("}, locals={");
        /*SL:62*/this.printTypes(a1, this.numLocals, this.localsTypes);
        /*SL:63*/a1.append('}');
    }
    
    private void printTypes(final StringBuffer v1, final int v2, final TypeData[] v3) {
        /*SL:68*/if (v3 == null) {
            /*SL:69*/return;
        }
        /*SL:71*/for (TypeData a2 = (TypeData)0; a2 < v2; ++a2) {
            /*SL:72*/if (a2 > 0) {
                /*SL:73*/v1.append(", ");
            }
            /*SL:75*/a2 = v3[a2];
            /*SL:76*/v1.append((a2 == null) ? "<>" : a2.toString());
        }
    }
    
    public boolean alreadySet() {
        /*SL:81*/return this.localsTypes != null;
    }
    
    public void setStackMap(final int a1, final TypeData[] a2, final int a3, final TypeData[] a4) throws BadBytecode {
        /*SL:87*/this.stackTop = a1;
        /*SL:88*/this.stackTypes = a2;
        /*SL:89*/this.numLocals = a3;
        /*SL:90*/this.localsTypes = a4;
    }
    
    public void resetNumLocals() {
        /*SL:97*/if (this.localsTypes != null) {
            int v1;
            for (/*SL:98*/v1 = this.localsTypes.length; /*EL:99*/v1 > 0 && this.localsTypes[v1 - 1].isBasicType() == TypeTag.TOP && /*EL:100*/(v1 <= 1 || /*EL:101*/!this.localsTypes[v1 - 2].is2WordType()); /*SL:105*/--v1) {}
            /*SL:108*/this.numLocals = v1;
        }
    }
    
    void initFirstBlock(final int a3, final int a4, final String a5, final String a6, final boolean v1, final boolean v2) throws BadBytecode {
        /*SL:136*/if (a6.charAt(0) != '(') {
            /*SL:137*/throw new BadBytecode("no method descriptor: " + a6);
        }
        /*SL:139*/this.stackTop = 0;
        /*SL:140*/this.stackTypes = TypeData.make(a3);
        final TypeData[] v3 = /*EL:141*/TypeData.make(a4);
        /*SL:142*/if (v2) {
            /*SL:143*/v3[0] = new TypeData.UninitThis(a5);
        }
        else/*SL:144*/ if (!v1) {
            /*SL:145*/v3[0] = new TypeData.ClassName(a5);
        }
        int v4 = /*EL:147*/v1 ? -1 : 0;
        int v5 = /*EL:148*/1;
        try {
            /*SL:150*/while ((v5 = descToTag(a6, v5, ++v4, v3)) > 0) {
                /*SL:151*/if (v3[v4].is2WordType()) {
                    /*SL:152*/v3[++v4] = TypeTag.TOP;
                }
            }
        }
        catch (StringIndexOutOfBoundsException a7) {
            /*SL:155*/throw new BadBytecode("bad method descriptor: " + a6);
        }
        /*SL:159*/this.numLocals = v4;
        /*SL:160*/this.localsTypes = v3;
    }
    
    private static int descToTag(final String a3, int a4, final int v1, final TypeData[] v2) throws BadBytecode {
        int v3 = /*EL:168*/0;
        char v4 = /*EL:169*/a3.charAt(a4);
        /*SL:170*/if (v4 == ')') {
            /*SL:171*/return 0;
        }
        /*SL:173*/while (v4 == '[') {
            /*SL:174*/++v3;
            /*SL:175*/v4 = a3.charAt(++a4);
        }
        /*SL:178*/if (v4 == 'L') {
            int a5 = /*EL:179*/a3.indexOf(59, ++a4);
            /*SL:180*/if (v3 > 0) {
                /*SL:181*/v2[v1] = new TypeData.ClassName(a3.substring(a4, ++a5));
            }
            else {
                /*SL:184*/v2[v1] = new TypeData.ClassName(a3.substring(a4 + 1, ++a5 - 1).replace('/', '.'));
            }
            /*SL:185*/return a5;
        }
        /*SL:187*/if (v3 > 0) {
            /*SL:188*/v2[v1] = new TypeData.ClassName(a3.substring(a4, ++a4));
            /*SL:189*/return a4;
        }
        final TypeData a6 = toPrimitiveTag(/*EL:192*/v4);
        /*SL:193*/if (a6 == null) {
            /*SL:194*/throw new BadBytecode("bad method descriptor: " + a3);
        }
        /*SL:196*/v2[v1] = a6;
        /*SL:197*/return a4 + 1;
    }
    
    private static TypeData toPrimitiveTag(final char a1) {
        /*SL:202*/switch (a1) {
            case 'B':
            case 'C':
            case 'I':
            case 'S':
            case 'Z': {
                /*SL:208*/return TypeTag.INTEGER;
            }
            case 'J': {
                /*SL:210*/return TypeTag.LONG;
            }
            case 'F': {
                /*SL:212*/return TypeTag.FLOAT;
            }
            case 'D': {
                /*SL:214*/return TypeTag.DOUBLE;
            }
            default: {
                /*SL:217*/return null;
            }
        }
    }
    
    public static String getRetType(final String a1) {
        final int v1 = /*EL:222*/a1.indexOf(41);
        /*SL:223*/if (v1 < 0) {
            /*SL:224*/return "java.lang.Object";
        }
        final char v2 = /*EL:226*/a1.charAt(v1 + 1);
        /*SL:227*/if (v2 == '[') {
            /*SL:228*/return a1.substring(v1 + 1);
        }
        /*SL:229*/if (v2 == 'L') {
            /*SL:230*/return a1.substring(v1 + 2, a1.length() - 1).replace('/', '.');
        }
        /*SL:232*/return "java.lang.Object";
    }
    
    public static class Maker extends BasicBlock.Maker
    {
        @Override
        protected BasicBlock makeBlock(final int a1) {
            /*SL:114*/return new TypedBlock(a1);
        }
        
        @Override
        protected BasicBlock[] makeArray(final int a1) {
            /*SL:118*/return new TypedBlock[a1];
        }
    }
}
