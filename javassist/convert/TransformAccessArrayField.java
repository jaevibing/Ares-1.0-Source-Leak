package javassist.convert;

import javassist.bytecode.Descriptor;
import javassist.bytecode.analysis.Analyzer;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.CannotCompileException;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;
import javassist.bytecode.ConstPool;
import javassist.NotFoundException;
import javassist.bytecode.analysis.Frame;
import javassist.CodeConverter;

public final class TransformAccessArrayField extends Transformer
{
    private final String methodClassname;
    private final CodeConverter.ArrayAccessReplacementMethodNames names;
    private Frame[] frames;
    private int offset;
    
    public TransformAccessArrayField(final Transformer a1, final String a2, final CodeConverter.ArrayAccessReplacementMethodNames a3) throws NotFoundException {
        super(a1);
        this.methodClassname = a2;
        this.names = a3;
    }
    
    @Override
    public void initialize(final ConstPool v2, final CtClass v3, final MethodInfo v4) throws CannotCompileException {
        final CodeIterator v5 = /*EL:63*/v4.getCodeAttribute().iterator();
        /*SL:64*/while (v5.hasNext()) {
            try {
                int a1 = /*EL:66*/v5.next();
                final int a2 = /*EL:67*/v5.byteAt(a1);
                /*SL:69*/if (a2 == 50) {
                    /*SL:70*/this.initFrames(v3, v4);
                }
                /*SL:72*/if (a2 == 50 || a2 == 51 || a2 == 52 || a2 == 49 || a2 == 48 || a2 == 46 || a2 == 47 || a2 == 53) {
                    /*SL:75*/a1 = this.replace(v2, v5, a1, a2, this.getLoadReplacementSignature(a2));
                }
                else {
                    /*SL:76*/if (a2 != 83 && a2 != 84 && a2 != 85 && a2 != 82 && a2 != 81 && a2 != 79 && a2 != 80 && a2 != 86) {
                        continue;
                    }
                    /*SL:79*/a1 = this.replace(v2, v5, a1, a2, this.getStoreReplacementSignature(a2));
                }
                /*SL:84*/continue;
            }
            catch (Exception a3) {
                throw new CannotCompileException(a3);
            }
            break;
        }
    }
    
    @Override
    public void clean() {
        /*SL:89*/this.frames = null;
        /*SL:90*/this.offset = -1;
    }
    
    @Override
    public int transform(final CtClass a1, final int a2, final CodeIterator a3, final ConstPool a4) throws BadBytecode {
        /*SL:96*/return a2;
    }
    
    private Frame getFrame(final int a1) throws BadBytecode {
        /*SL:100*/return this.frames[a1 - this.offset];
    }
    
    private void initFrames(final CtClass a1, final MethodInfo a2) throws BadBytecode {
        /*SL:104*/if (this.frames == null) {
            /*SL:105*/this.frames = new Analyzer().analyze(a1, a2);
            /*SL:106*/this.offset = 0;
        }
    }
    
    private int updatePos(final int a1, final int a2) {
        /*SL:111*/if (this.offset > -1) {
            /*SL:112*/this.offset += a2;
        }
        /*SL:114*/return a1 + a2;
    }
    
    private String getTopType(final int a1) throws BadBytecode {
        final Frame v1 = /*EL:118*/this.getFrame(a1);
        /*SL:119*/if (v1 == null) {
            /*SL:120*/return null;
        }
        final CtClass v2 = /*EL:122*/v1.peek().getCtClass();
        /*SL:123*/return (v2 != null) ? Descriptor.toJvmName(v2) : null;
    }
    
    private int replace(final ConstPool v1, final CodeIterator v2, int v3, final int v4, final String v5) throws BadBytecode {
        String v6 = /*EL:128*/null;
        final String v7 = /*EL:129*/this.getMethodName(v4);
        /*SL:130*/if (v7 != null) {
            /*SL:132*/if (v4 == 50) {
                /*SL:133*/v6 = this.getTopType(v2.lookAhead());
                /*SL:137*/if (v6 == null) {
                    /*SL:138*/return v3;
                }
                /*SL:139*/if ("java/lang/Object".equals(v6)) {
                    /*SL:140*/v6 = null;
                }
            }
            /*SL:145*/v2.writeByte(0, v3);
            CodeIterator.Gap a2 = /*EL:147*/v2.insertGapAt(v3, (v6 != null) ? 5 : 2, false);
            /*SL:148*/v3 = a2.position;
            /*SL:149*/a2 = v1.addClassInfo(this.methodClassname);
            final int a3 = /*EL:150*/v1.addMethodrefInfo(a2, v7, v5);
            /*SL:151*/v2.writeByte(184, v3);
            /*SL:152*/v2.write16bit(a3, v3 + 1);
            /*SL:154*/if (v6 != null) {
                final int a4 = /*EL:155*/v1.addClassInfo(v6);
                /*SL:156*/v2.writeByte(192, v3 + 3);
                /*SL:157*/v2.write16bit(a4, v3 + 4);
            }
            /*SL:160*/v3 = this.updatePos(v3, a2.length);
        }
        /*SL:163*/return v3;
    }
    
    private String getMethodName(final int a1) {
        String v1 = /*EL:167*/null;
        /*SL:168*/switch (a1) {
            case 50: {
                /*SL:170*/v1 = this.names.objectRead();
                /*SL:171*/break;
            }
            case 51: {
                /*SL:173*/v1 = this.names.byteOrBooleanRead();
                /*SL:174*/break;
            }
            case 52: {
                /*SL:176*/v1 = this.names.charRead();
                /*SL:177*/break;
            }
            case 49: {
                /*SL:179*/v1 = this.names.doubleRead();
                /*SL:180*/break;
            }
            case 48: {
                /*SL:182*/v1 = this.names.floatRead();
                /*SL:183*/break;
            }
            case 46: {
                /*SL:185*/v1 = this.names.intRead();
                /*SL:186*/break;
            }
            case 53: {
                /*SL:188*/v1 = this.names.shortRead();
                /*SL:189*/break;
            }
            case 47: {
                /*SL:191*/v1 = this.names.longRead();
                /*SL:192*/break;
            }
            case 83: {
                /*SL:194*/v1 = this.names.objectWrite();
                /*SL:195*/break;
            }
            case 84: {
                /*SL:197*/v1 = this.names.byteOrBooleanWrite();
                /*SL:198*/break;
            }
            case 85: {
                /*SL:200*/v1 = this.names.charWrite();
                /*SL:201*/break;
            }
            case 82: {
                /*SL:203*/v1 = this.names.doubleWrite();
                /*SL:204*/break;
            }
            case 81: {
                /*SL:206*/v1 = this.names.floatWrite();
                /*SL:207*/break;
            }
            case 79: {
                /*SL:209*/v1 = this.names.intWrite();
                /*SL:210*/break;
            }
            case 86: {
                /*SL:212*/v1 = this.names.shortWrite();
                /*SL:213*/break;
            }
            case 80: {
                /*SL:215*/v1 = this.names.longWrite();
                break;
            }
        }
        /*SL:219*/if (v1.equals("")) {
            /*SL:220*/v1 = null;
        }
        /*SL:222*/return v1;
    }
    
    private String getLoadReplacementSignature(final int a1) throws BadBytecode {
        /*SL:226*/switch (a1) {
            case 50: {
                /*SL:228*/return "(Ljava/lang/Object;I)Ljava/lang/Object;";
            }
            case 51: {
                /*SL:230*/return "(Ljava/lang/Object;I)B";
            }
            case 52: {
                /*SL:232*/return "(Ljava/lang/Object;I)C";
            }
            case 49: {
                /*SL:234*/return "(Ljava/lang/Object;I)D";
            }
            case 48: {
                /*SL:236*/return "(Ljava/lang/Object;I)F";
            }
            case 46: {
                /*SL:238*/return "(Ljava/lang/Object;I)I";
            }
            case 53: {
                /*SL:240*/return "(Ljava/lang/Object;I)S";
            }
            case 47: {
                /*SL:242*/return "(Ljava/lang/Object;I)J";
            }
            default: {
                /*SL:245*/throw new BadBytecode(a1);
            }
        }
    }
    
    private String getStoreReplacementSignature(final int a1) throws BadBytecode {
        /*SL:249*/switch (a1) {
            case 83: {
                /*SL:251*/return "(Ljava/lang/Object;ILjava/lang/Object;)V";
            }
            case 84: {
                /*SL:253*/return "(Ljava/lang/Object;IB)V";
            }
            case 85: {
                /*SL:255*/return "(Ljava/lang/Object;IC)V";
            }
            case 82: {
                /*SL:257*/return "(Ljava/lang/Object;ID)V";
            }
            case 81: {
                /*SL:259*/return "(Ljava/lang/Object;IF)V";
            }
            case 79: {
                /*SL:261*/return "(Ljava/lang/Object;II)V";
            }
            case 86: {
                /*SL:263*/return "(Ljava/lang/Object;IS)V";
            }
            case 80: {
                /*SL:265*/return "(Ljava/lang/Object;IJ)V";
            }
            default: {
                /*SL:268*/throw new BadBytecode(a1);
            }
        }
    }
}
