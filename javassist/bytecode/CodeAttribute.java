package javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class CodeAttribute extends AttributeInfo implements Opcode
{
    public static final String tag = "Code";
    private int maxStack;
    private int maxLocals;
    private ExceptionTable exceptions;
    private ArrayList attributes;
    
    public CodeAttribute(final ConstPool a1, final int a2, final int a3, final byte[] a4, final ExceptionTable a5) {
        super(a1, "Code");
        this.maxStack = a2;
        this.maxLocals = a3;
        this.info = a4;
        this.exceptions = a5;
        this.attributes = new ArrayList();
    }
    
    private CodeAttribute(final ConstPool v1, final CodeAttribute v2, final Map v3) throws BadBytecode {
        super(v1, "Code");
        this.maxStack = v2.getMaxStack();
        this.maxLocals = v2.getMaxLocals();
        this.exceptions = v2.getExceptionTable().copy(v1, v3);
        this.attributes = new ArrayList();
        final List v4 = v2.getAttributes();
        final int v5 = v4.size();
        for (AttributeInfo a2 = (AttributeInfo)0; a2 < v5; ++a2) {
            a2 = v4.get(a2);
            this.attributes.add(a2.copy(v1, v3));
        }
        this.info = v2.copyCode(v1, v3, this.exceptions, this);
    }
    
    CodeAttribute(final ConstPool a3, final int v1, final DataInputStream v2) throws IOException {
        super(a3, v1, (byte[])null);
        final int v3 = v2.readInt();
        this.maxStack = v2.readUnsignedShort();
        this.maxLocals = v2.readUnsignedShort();
        final int v4 = v2.readInt();
        v2.readFully(this.info = new byte[v4]);
        this.exceptions = new ExceptionTable(a3, v2);
        this.attributes = new ArrayList();
        for (int v5 = v2.readUnsignedShort(), a4 = 0; a4 < v5; ++a4) {
            this.attributes.add(AttributeInfo.read(a3, v2));
        }
    }
    
    @Override
    public AttributeInfo copy(final ConstPool v1, final Map v2) throws RuntimeCopyException {
        try {
            /*SL:137*/return new CodeAttribute(v1, this, v2);
        }
        catch (BadBytecode a1) {
            /*SL:140*/throw new RuntimeCopyException("bad bytecode. fatal?");
        }
    }
    
    @Override
    public int length() {
        /*SL:164*/return 18 + this.info.length + this.exceptions.size() * 8 + AttributeInfo.getLength(this.attributes);
    }
    
    @Override
    void write(final DataOutputStream a1) throws IOException {
        /*SL:168*/a1.writeShort(this.name);
        /*SL:169*/a1.writeInt(this.length() - 6);
        /*SL:170*/a1.writeShort(this.maxStack);
        /*SL:171*/a1.writeShort(this.maxLocals);
        /*SL:172*/a1.writeInt(this.info.length);
        /*SL:173*/a1.write(this.info);
        /*SL:174*/this.exceptions.write(a1);
        /*SL:175*/a1.writeShort(this.attributes.size());
        /*SL:176*/AttributeInfo.writeAll(this.attributes, a1);
    }
    
    @Override
    public byte[] get() {
        /*SL:185*/throw new UnsupportedOperationException("CodeAttribute.get()");
    }
    
    @Override
    public void set(final byte[] a1) {
        /*SL:194*/throw new UnsupportedOperationException("CodeAttribute.set()");
    }
    
    @Override
    void renameClass(final String a1, final String a2) {
        /*SL:198*/AttributeInfo.renameClass(this.attributes, a1, a2);
    }
    
    @Override
    void renameClass(final Map a1) {
        /*SL:202*/AttributeInfo.renameClass(this.attributes, a1);
    }
    
    @Override
    void getRefClasses(final Map a1) {
        /*SL:206*/AttributeInfo.getRefClasses(this.attributes, a1);
    }
    
    public String getDeclaringClass() {
        final ConstPool v1 = /*EL:214*/this.getConstPool();
        /*SL:215*/return v1.getClassName();
    }
    
    public int getMaxStack() {
        /*SL:222*/return this.maxStack;
    }
    
    public void setMaxStack(final int a1) {
        /*SL:229*/this.maxStack = a1;
    }
    
    public int computeMaxStack() throws BadBytecode {
        /*SL:241*/return this.maxStack = new CodeAnalyzer(this).computeMaxStack();
    }
    
    public int getMaxLocals() {
        /*SL:248*/return this.maxLocals;
    }
    
    public void setMaxLocals(final int a1) {
        /*SL:255*/this.maxLocals = a1;
    }
    
    public int getCodeLength() {
        /*SL:262*/return this.info.length;
    }
    
    public byte[] getCode() {
        /*SL:269*/return this.info;
    }
    
    void setCode(final byte[] a1) {
        /*SL:275*/super.set(a1);
    }
    
    public CodeIterator iterator() {
        /*SL:281*/return new CodeIterator(this);
    }
    
    public ExceptionTable getExceptionTable() {
        /*SL:287*/return this.exceptions;
    }
    
    public List getAttributes() {
        /*SL:297*/return this.attributes;
    }
    
    public AttributeInfo getAttribute(final String a1) {
        /*SL:307*/return AttributeInfo.lookup(this.attributes, a1);
    }
    
    public void setAttribute(final StackMapTable a1) {
        /*SL:319*/AttributeInfo.remove(this.attributes, "StackMapTable");
        /*SL:320*/if (a1 != null) {
            /*SL:321*/this.attributes.add(a1);
        }
    }
    
    public void setAttribute(final StackMap a1) {
        /*SL:334*/AttributeInfo.remove(this.attributes, "StackMap");
        /*SL:335*/if (a1 != null) {
            /*SL:336*/this.attributes.add(a1);
        }
    }
    
    private byte[] copyCode(final ConstPool a1, final Map a2, final ExceptionTable a3, final CodeAttribute a4) throws BadBytecode {
        final int v1 = /*EL:346*/this.getCodeLength();
        final byte[] v2 = /*EL:347*/new byte[v1];
        /*SL:348*/a4.info = v2;
        final LdcEntry v3 = copyCode(/*EL:349*/this.info, 0, v1, this.getConstPool(), v2, a1, a2);
        /*SL:351*/return LdcEntry.doit(v2, v3, a3, a4);
    }
    
    private static LdcEntry copyCode(final byte[] a6, final int a7, final int v1, final ConstPool v2, final byte[] v3, final ConstPool v4, final Map v5) throws BadBytecode {
        LdcEntry v6 = /*EL:360*/null;
        int a9;
        /*SL:362*/for (int a8 = a7; a8 < v1; a8 = a9) {
            /*SL:363*/a9 = CodeIterator.nextOpcode(a6, a8);
            final byte a10 = /*EL:364*/a6[a8];
            /*SL:365*/switch ((v3[a8] = a10) & /*EL:366*/0xFF) {
                case 19:
                case 20:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 187:
                case 189:
                case 192:
                case 193: {
                    copyConstPoolInfo(/*EL:380*/a8 + 1, a6, v2, v3, v4, v5);
                    /*SL:382*/break;
                }
                case 18: {
                    int a11 = /*EL:384*/a6[a8 + 1] & 0xFF;
                    /*SL:385*/a11 = v2.copy(a11, v4, v5);
                    /*SL:386*/if (a11 < 256) {
                        /*SL:387*/v3[a8 + 1] = (byte)a11;
                        break;
                    }
                    /*SL:390*/v3[a8 + 1] = (v3[a8] = 0);
                    final LdcEntry a12 = /*EL:391*/new LdcEntry();
                    /*SL:392*/a12.where = a8;
                    /*SL:393*/a12.index = a11;
                    /*SL:394*/a12.next = v6;
                    /*SL:395*/v6 = a12;
                    /*SL:397*/break;
                }
                case 185: {
                    copyConstPoolInfo(/*EL:399*/a8 + 1, a6, v2, v3, v4, v5);
                    /*SL:401*/v3[a8 + 3] = a6[a8 + 3];
                    /*SL:402*/v3[a8 + 4] = a6[a8 + 4];
                    /*SL:403*/break;
                }
                case 186: {
                    copyConstPoolInfo(/*EL:405*/a8 + 1, a6, v2, v3, v4, v5);
                    /*SL:408*/v3[a8 + 4] = (v3[a8 + 3] = 0);
                    /*SL:409*/break;
                }
                case 197: {
                    copyConstPoolInfo(/*EL:411*/a8 + 1, a6, v2, v3, v4, v5);
                    /*SL:413*/v3[a8 + 3] = a6[a8 + 3];
                    /*SL:414*/break;
                }
                default: {
                    /*SL:416*/while (++a8 < a9) {
                        /*SL:417*/v3[a8] = a6[a8];
                    }
                    break;
                }
            }
        }
        /*SL:423*/return v6;
    }
    
    private static void copyConstPoolInfo(final int a1, final byte[] a2, final ConstPool a3, final byte[] a4, final ConstPool a5, final Map a6) {
        int v1 = /*EL:429*/(a2[a1] & 0xFF) << 8 | (a2[a1 + 1] & 0xFF);
        /*SL:430*/v1 = a3.copy(v1, a5, a6);
        /*SL:431*/a4[a1] = (byte)(v1 >> 8);
        /*SL:432*/a4[a1 + 1] = (byte)v1;
    }
    
    public void insertLocalVar(final int a1, final int a2) throws BadBytecode {
        final CodeIterator v1 = /*EL:481*/this.iterator();
        /*SL:482*/while (v1.hasNext()) {
            shiftIndex(/*EL:483*/v1, a1, a2);
        }
        /*SL:485*/this.setMaxLocals(this.getMaxLocals() + a2);
    }
    
    private static void shiftIndex(final CodeIterator v-4, final int v-3, final int v-2) throws BadBytecode {
        final int next = /*EL:496*/v-4.next();
        final int v0 = /*EL:497*/v-4.byteAt(next);
        /*SL:498*/if (v0 < 21) {
            /*SL:499*/return;
        }
        /*SL:500*/if (v0 < 79) {
            /*SL:501*/if (v0 < 26) {
                shiftIndex8(/*EL:503*/v-4, next, v0, v-3, v-2);
            }
            else/*SL:505*/ if (v0 < 46) {
                shiftIndex0(/*EL:507*/v-4, next, v0, v-3, v-2, 26, 21);
            }
            else {
                /*SL:509*/if (v0 < 54) {
                    /*SL:510*/return;
                }
                /*SL:511*/if (v0 < 59) {
                    shiftIndex8(/*EL:513*/v-4, next, v0, v-3, v-2);
                }
                else {
                    shiftIndex0(/*EL:517*/v-4, next, v0, v-3, v-2, 59, 54);
                }
            }
        }
        else/*SL:520*/ if (v0 == 132) {
            int a3 = /*EL:521*/v-4.byteAt(next + 1);
            /*SL:522*/if (a3 < v-3) {
                /*SL:523*/return;
            }
            /*SL:525*/a3 += v-2;
            /*SL:526*/if (a3 < 256) {
                /*SL:527*/v-4.writeByte(a3, next + 1);
            }
            else {
                final int a2 = /*EL:529*/(byte)v-4.byteAt(next + 2);
                /*SL:530*/a3 = v-4.insertExGap(3);
                /*SL:531*/v-4.writeByte(196, a3 - 3);
                /*SL:532*/v-4.writeByte(132, a3 - 2);
                /*SL:533*/v-4.write16bit(a3, a3 - 1);
                /*SL:534*/v-4.write16bit(a2, a3 + 1);
            }
        }
        else/*SL:537*/ if (v0 == 169) {
            shiftIndex8(/*EL:538*/v-4, next, v0, v-3, v-2);
        }
        else/*SL:539*/ if (v0 == 196) {
            int v = /*EL:540*/v-4.u16bitAt(next + 2);
            /*SL:541*/if (v < v-3) {
                /*SL:542*/return;
            }
            /*SL:544*/v += v-2;
            /*SL:545*/v-4.write16bit(v, next + 2);
        }
    }
    
    private static void shiftIndex8(final CodeIterator a2, final int a3, final int a4, final int a5, final int v1) throws BadBytecode {
        int v2 = /*EL:553*/a2.byteAt(a3 + 1);
        /*SL:554*/if (v2 < a5) {
            /*SL:555*/return;
        }
        /*SL:557*/v2 += v1;
        /*SL:558*/if (v2 < 256) {
            /*SL:559*/a2.writeByte(v2, a3 + 1);
        }
        else {
            final int a6 = /*EL:561*/a2.insertExGap(2);
            /*SL:562*/a2.writeByte(196, a6 - 2);
            /*SL:563*/a2.writeByte(a4, a6 - 1);
            /*SL:564*/a2.write16bit(v2, a6);
        }
    }
    
    private static void shiftIndex0(final CodeIterator a3, final int a4, int a5, final int a6, final int a7, final int v1, final int v2) throws BadBytecode {
        int v3 = /*EL:573*/(a5 - v1) % 4;
        /*SL:574*/if (v3 < a6) {
            /*SL:575*/return;
        }
        /*SL:577*/v3 += a7;
        /*SL:578*/if (v3 < 4) {
            /*SL:579*/a3.writeByte(a5 + a7, a4);
        }
        else {
            /*SL:581*/a5 = (a5 - v1) / 4 + v2;
            /*SL:582*/if (v3 < 256) {
                final int a8 = /*EL:583*/a3.insertExGap(1);
                /*SL:584*/a3.writeByte(a5, a8 - 1);
                /*SL:585*/a3.writeByte(v3, a8);
            }
            else {
                final int a9 = /*EL:588*/a3.insertExGap(3);
                /*SL:589*/a3.writeByte(196, a9 - 1);
                /*SL:590*/a3.writeByte(a5, a9);
                /*SL:591*/a3.write16bit(v3, a9 + 1);
            }
        }
    }
    
    public static class RuntimeCopyException extends RuntimeException
    {
        public RuntimeCopyException(final String a1) {
            super(a1);
        }
    }
    
    static class LdcEntry
    {
        LdcEntry next;
        int where;
        int index;
        
        static byte[] doit(byte[] a1, final LdcEntry a2, final ExceptionTable a3, final CodeAttribute a4) throws BadBytecode {
            /*SL:444*/if (a2 != null) {
                /*SL:445*/a1 = CodeIterator.changeLdcToLdcW(a1, a3, a4, a2);
            }
            /*SL:460*/return a1;
        }
    }
}
