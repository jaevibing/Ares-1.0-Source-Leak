package javassist.expr;

import javassist.bytecode.ExceptionTable;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.BadBytecode;
import javassist.CannotCompileException;
import javassist.bytecode.MethodInfo;
import javassist.CtClass;

public class ExprEditor
{
    public boolean doit(final CtClass v-7, final MethodInfo v-6) throws CannotCompileException {
        final CodeAttribute codeAttribute = /*EL:82*/v-6.getCodeAttribute();
        /*SL:83*/if (codeAttribute == null) {
            /*SL:84*/return false;
        }
        final CodeIterator iterator = /*EL:86*/codeAttribute.iterator();
        boolean b = /*EL:87*/false;
        final LoopContext v2 = /*EL:88*/new LoopContext(codeAttribute.getMaxLocals());
        /*SL:90*/while (iterator.hasNext()) {
            /*SL:91*/if (this.loopBody(iterator, v-7, v-6, v2)) {
                /*SL:92*/b = true;
            }
        }
        final ExceptionTable exceptionTable = /*EL:94*/codeAttribute.getExceptionTable();
        final int v0 = /*EL:95*/exceptionTable.size();
        /*SL:96*/for (Handler a2 = (Handler)0; a2 < v0; ++a2) {
            /*SL:97*/a2 = new Handler(exceptionTable, a2, iterator, v-7, v-6);
            /*SL:98*/this.edit(a2);
            /*SL:99*/if (a2.edited()) {
                /*SL:100*/b = true;
                /*SL:101*/v2.updateMax(a2.locals(), a2.stack());
            }
        }
        /*SL:107*/if (codeAttribute.getMaxLocals() < v2.maxLocals) {
            /*SL:108*/codeAttribute.setMaxLocals(v2.maxLocals);
        }
        /*SL:110*/codeAttribute.setMaxStack(codeAttribute.getMaxStack() + v2.maxStack);
        try {
            /*SL:112*/if (b) {
                /*SL:113*/v-6.rebuildStackMapIf6(v-7.getClassPool(), v-7.getClassFile2());
            }
        }
        catch (BadBytecode v) {
            /*SL:117*/throw new CannotCompileException(v.getMessage(), v);
        }
        /*SL:120*/return b;
    }
    
    boolean doit(final CtClass a4, final MethodInfo a5, final LoopContext v1, final CodeIterator v2, int v3) throws CannotCompileException {
        boolean v4 = /*EL:130*/false;
        /*SL:131*/while (v2.hasNext() && v2.lookAhead() < v3) {
            final int a6 = /*EL:132*/v2.getCodeLength();
            /*SL:133*/if (this.loopBody(v2, a4, a5, v1)) {
                /*SL:134*/v4 = true;
                final int a7 = /*EL:135*/v2.getCodeLength();
                /*SL:136*/if (a6 == a7) {
                    continue;
                }
                /*SL:137*/v3 += a7 - a6;
            }
        }
        /*SL:141*/return v4;
    }
    
    final boolean loopBody(final CodeIterator v-3, final CtClass v-2, final MethodInfo v-1, final LoopContext v0) throws CannotCompileException {
        try {
            Expr v = /*EL:181*/null;
            final int v2 = /*EL:182*/v-3.next();
            final int v3 = /*EL:183*/v-3.byteAt(v2);
            /*SL:185*/if (v3 >= 178) {
                /*SL:187*/if (v3 < 188) {
                    /*SL:188*/if (v3 == 184 || v3 == 185 || v3 == 182) {
                        /*SL:191*/v = new MethodCall(v2, v-3, v-2, v-1);
                        /*SL:192*/this.edit((MethodCall)v);
                    }
                    else/*SL:194*/ if (v3 == 180 || v3 == 178 || v3 == 181 || v3 == 179) {
                        /*SL:197*/v = new FieldAccess(v2, v-3, v-2, v-1, v3);
                        /*SL:198*/this.edit((FieldAccess)v);
                    }
                    else/*SL:200*/ if (v3 == 187) {
                        final int a1 = /*EL:201*/v-3.u16bitAt(v2 + 1);
                        /*SL:203*/v0.newList = new NewOp(v0.newList, v2, v-1.getConstPool().getClassInfo(a1));
                    }
                    else/*SL:205*/ if (v3 == 183) {
                        final NewOp a2 = /*EL:206*/v0.newList;
                        /*SL:207*/if (a2 != null && v-1.getConstPool().isConstructor(/*EL:208*/a2.type, v-3.u16bitAt(v2 + 1)) > 0) {
                            /*SL:210*/v = new NewExpr(v2, v-3, v-2, v-1, a2.type, a2.pos);
                            /*SL:212*/this.edit((NewExpr)v);
                            /*SL:213*/v0.newList = a2.next;
                        }
                        else {
                            final MethodCall a3 = /*EL:216*/new MethodCall(v2, v-3, v-2, v-1);
                            /*SL:217*/if (a3.getMethodName().equals("<init>")) {
                                final ConstructorCall a4 = /*EL:219*/(ConstructorCall)(v = new ConstructorCall(v2, v-3, v-2, v-1));
                                /*SL:220*/this.edit(a4);
                            }
                            else {
                                /*SL:223*/v = a3;
                                /*SL:224*/this.edit(a3);
                            }
                        }
                    }
                }
                else/*SL:230*/ if (v3 == 188 || v3 == 189 || v3 == 197) {
                    /*SL:232*/v = new NewArray(v2, v-3, v-2, v-1, v3);
                    /*SL:233*/this.edit((NewArray)v);
                }
                else/*SL:235*/ if (v3 == 193) {
                    /*SL:236*/v = new Instanceof(v2, v-3, v-2, v-1);
                    /*SL:237*/this.edit((Instanceof)v);
                }
                else/*SL:239*/ if (v3 == 192) {
                    /*SL:240*/v = new Cast(v2, v-3, v-2, v-1);
                    /*SL:241*/this.edit((Cast)v);
                }
            }
            /*SL:245*/if (v != null && v.edited()) {
                /*SL:246*/v0.updateMax(v.locals(), v.stack());
                /*SL:247*/return true;
            }
            /*SL:250*/return false;
        }
        catch (BadBytecode v4) {
            /*SL:253*/throw new CannotCompileException(v4);
        }
    }
    
    public void edit(final NewExpr a1) throws CannotCompileException {
    }
    
    public void edit(final NewArray a1) throws CannotCompileException {
    }
    
    public void edit(final MethodCall a1) throws CannotCompileException {
    }
    
    public void edit(final ConstructorCall a1) throws CannotCompileException {
    }
    
    public void edit(final FieldAccess a1) throws CannotCompileException {
    }
    
    public void edit(final Instanceof a1) throws CannotCompileException {
    }
    
    public void edit(final Cast a1) throws CannotCompileException {
    }
    
    public void edit(final Handler a1) throws CannotCompileException {
    }
    
    static final class NewOp
    {
        NewOp next;
        int pos;
        String type;
        
        NewOp(final NewOp a1, final int a2, final String a3) {
            /*SL:316*/this.next = a1;
            this.pos = a2;
            this.type = a3;
        }
    }
    
    static final class LoopContext
    {
        NewOp newList;
        int maxLocals;
        int maxStack;
        
        LoopContext(final int a1) {
            this.maxLocals = a1;
            this.maxStack = 0;
            this.newList = null;
        }
        
        void updateMax(final int a1, final int a2) {
            /*SL:168*/if (this.maxLocals < a1) {
                /*SL:169*/this.maxLocals = a1;
            }
            /*SL:171*/if (this.maxStack < a2) {
                /*SL:172*/this.maxStack = a2;
            }
        }
    }
}
