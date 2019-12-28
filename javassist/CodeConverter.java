package javassist;

import javassist.bytecode.CodeIterator;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.convert.TransformAfter;
import javassist.convert.TransformBefore;
import javassist.convert.TransformCall;
import javassist.convert.TransformAccessArrayField;
import javassist.convert.TransformWriteField;
import javassist.convert.TransformReadField;
import javassist.convert.TransformFieldAccess;
import javassist.convert.TransformNewClass;
import javassist.convert.TransformNew;
import javassist.convert.Transformer;

public class CodeConverter
{
    protected Transformer transformers;
    
    public CodeConverter() {
        this.transformers = null;
    }
    
    public void replaceNew(final CtClass a1, final CtClass a2, final String a3) {
        /*SL:98*/this.transformers = new TransformNew(this.transformers, a1.getName(), a2.getName(), a3);
    }
    
    public void replaceNew(final CtClass a1, final CtClass a2) {
        /*SL:124*/this.transformers = new TransformNewClass(this.transformers, a1.getName(), a2.getName());
    }
    
    public void redirectFieldAccess(final CtField a1, final CtClass a2, final String a3) {
        /*SL:147*/this.transformers = new TransformFieldAccess(this.transformers, a1, a2.getName(), a3);
    }
    
    public void replaceFieldRead(final CtField a1, final CtClass a2, final String a3) {
        /*SL:187*/this.transformers = new TransformReadField(this.transformers, a1, a2.getName(), a3);
    }
    
    public void replaceFieldWrite(final CtField a1, final CtClass a2, final String a3) {
        /*SL:228*/this.transformers = new TransformWriteField(this.transformers, a1, a2.getName(), a3);
    }
    
    public void replaceArrayAccess(final CtClass a1, final ArrayAccessReplacementMethodNames a2) throws NotFoundException {
        /*SL:330*/this.transformers = new TransformAccessArrayField(this.transformers, a1.getName(), a2);
    }
    
    public void redirectMethodCall(final CtMethod a1, final CtMethod a2) throws CannotCompileException {
        final String v1 = /*EL:352*/a1.getMethodInfo2().getDescriptor();
        final String v2 = /*EL:353*/a2.getMethodInfo2().getDescriptor();
        /*SL:354*/if (!v1.equals(v2)) {
            /*SL:355*/throw new CannotCompileException("signature mismatch: " + a2.getLongName());
        }
        final int v3 = /*EL:358*/a1.getModifiers();
        final int v4 = /*EL:359*/a2.getModifiers();
        /*SL:361*/if (Modifier.isStatic(v3) != Modifier.isStatic(v4) || (Modifier.isPrivate(v3) && !Modifier.isPrivate(v4)) || a1.getDeclaringClass().isInterface() != /*EL:362*/a2.getDeclaringClass().isInterface()) {
            /*SL:364*/throw new CannotCompileException("invoke-type mismatch " + a2.getLongName());
        }
        /*SL:367*/this.transformers = new TransformCall(this.transformers, a1, a2);
    }
    
    public void redirectMethodCall(final String a1, final CtMethod a2) throws CannotCompileException {
        /*SL:392*/this.transformers = new TransformCall(this.transformers, a1, a2);
    }
    
    public void insertBeforeMethod(final CtMethod v1, final CtMethod v2) throws CannotCompileException {
        try {
            /*SL:435*/this.transformers = new TransformBefore(this.transformers, v1, v2);
        }
        catch (NotFoundException a1) {
            /*SL:439*/throw new CannotCompileException(a1);
        }
    }
    
    public void insertAfterMethod(final CtMethod v1, final CtMethod v2) throws CannotCompileException {
        try {
            /*SL:483*/this.transformers = new TransformAfter(this.transformers, v1, v2);
        }
        catch (NotFoundException a1) {
            /*SL:487*/throw new CannotCompileException(a1);
        }
    }
    
    protected void doit(final CtClass v-7, final MethodInfo v-6, final ConstPool v-5) throws CannotCompileException {
        final CodeAttribute codeAttribute = /*EL:498*/v-6.getCodeAttribute();
        /*SL:499*/if (codeAttribute == null || this.transformers == null) {
            /*SL:500*/return;
        }
        /*SL:501*/for (Transformer transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
            /*SL:502*/transformer.initialize(v-5, v-7, v-6);
        }
        final CodeIterator iterator = /*EL:504*/codeAttribute.iterator();
        /*SL:505*/while (iterator.hasNext()) {
            try {
                int a1 = /*EL:507*/iterator.next();
                /*SL:508*/for (Transformer transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
                    /*SL:509*/a1 = transformer.transform(v-7, a1, iterator, v-5);
                }
                /*SL:513*/continue;
            }
            catch (BadBytecode a2) {
                throw new CannotCompileException(a2);
            }
            break;
        }
        int n = /*EL:516*/0;
        int v0 = /*EL:517*/0;
        /*SL:518*/for (Transformer transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
            int a3 = /*EL:519*/transformer.extraLocals();
            /*SL:520*/if (a3 > n) {
                /*SL:521*/n = a3;
            }
            /*SL:523*/a3 = transformer.extraStack();
            /*SL:524*/if (a3 > v0) {
                /*SL:525*/v0 = a3;
            }
        }
        /*SL:528*/for (Transformer transformer = this.transformers; transformer != null; transformer = transformer.getNext()) {
            /*SL:529*/transformer.clean();
        }
        /*SL:531*/if (n > 0) {
            /*SL:532*/codeAttribute.setMaxLocals(codeAttribute.getMaxLocals() + n);
        }
        /*SL:534*/if (v0 > 0) {
            /*SL:535*/codeAttribute.setMaxStack(codeAttribute.getMaxStack() + v0);
        }
        try {
            /*SL:538*/v-6.rebuildStackMapIf6(v-7.getClassPool(), v-7.getClassFile2());
        }
        catch (BadBytecode v) {
            /*SL:542*/throw new CannotCompileException(v.getMessage(), v);
        }
    }
    
    public static class DefaultArrayAccessReplacementMethodNames implements ArrayAccessReplacementMethodNames
    {
        @Override
        public String byteOrBooleanRead() {
            /*SL:671*/return "arrayReadByteOrBoolean";
        }
        
        @Override
        public String byteOrBooleanWrite() {
            /*SL:680*/return "arrayWriteByteOrBoolean";
        }
        
        @Override
        public String charRead() {
            /*SL:689*/return "arrayReadChar";
        }
        
        @Override
        public String charWrite() {
            /*SL:698*/return "arrayWriteChar";
        }
        
        @Override
        public String doubleRead() {
            /*SL:707*/return "arrayReadDouble";
        }
        
        @Override
        public String doubleWrite() {
            /*SL:716*/return "arrayWriteDouble";
        }
        
        @Override
        public String floatRead() {
            /*SL:725*/return "arrayReadFloat";
        }
        
        @Override
        public String floatWrite() {
            /*SL:734*/return "arrayWriteFloat";
        }
        
        @Override
        public String intRead() {
            /*SL:743*/return "arrayReadInt";
        }
        
        @Override
        public String intWrite() {
            /*SL:752*/return "arrayWriteInt";
        }
        
        @Override
        public String longRead() {
            /*SL:761*/return "arrayReadLong";
        }
        
        @Override
        public String longWrite() {
            /*SL:770*/return "arrayWriteLong";
        }
        
        @Override
        public String objectRead() {
            /*SL:779*/return "arrayReadObject";
        }
        
        @Override
        public String objectWrite() {
            /*SL:788*/return "arrayWriteObject";
        }
        
        @Override
        public String shortRead() {
            /*SL:797*/return "arrayReadShort";
        }
        
        @Override
        public String shortWrite() {
            /*SL:806*/return "arrayWriteShort";
        }
    }
    
    public interface ArrayAccessReplacementMethodNames
    {
        String byteOrBooleanRead();
        
        String byteOrBooleanWrite();
        
        String charRead();
        
        String charWrite();
        
        String doubleRead();
        
        String doubleWrite();
        
        String floatRead();
        
        String floatWrite();
        
        String intRead();
        
        String intWrite();
        
        String longRead();
        
        String longWrite();
        
        String objectRead();
        
        String objectWrite();
        
        String shortRead();
        
        String shortWrite();
    }
}
