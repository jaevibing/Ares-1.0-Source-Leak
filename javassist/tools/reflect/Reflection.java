package javassist.tools.reflect;

import java.util.Iterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ClassFile;
import javassist.Modifier;
import javassist.CtNewMethod;
import javassist.CtField;
import javassist.CannotCompileException;
import javassist.bytecode.BadBytecode;
import javassist.NotFoundException;
import javassist.CodeConverter;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Translator;

public class Reflection implements Translator
{
    static final String classobjectField = "_classobject";
    static final String classobjectAccessor = "_getClass";
    static final String metaobjectField = "_metaobject";
    static final String metaobjectGetter = "_getMetaobject";
    static final String metaobjectSetter = "_setMetaobject";
    static final String readPrefix = "_r_";
    static final String writePrefix = "_w_";
    static final String metaobjectClassName = "javassist.tools.reflect.Metaobject";
    static final String classMetaobjectClassName = "javassist.tools.reflect.ClassMetaobject";
    protected CtMethod trapMethod;
    protected CtMethod trapStaticMethod;
    protected CtMethod trapRead;
    protected CtMethod trapWrite;
    protected CtClass[] readParam;
    protected ClassPool classPool;
    protected CodeConverter converter;
    
    private boolean isExcluded(final String a1) {
        /*SL:94*/return a1.startsWith("_m_") || a1.equals("_getClass") || a1.equals("_setMetaobject") || a1.equals("_getMetaobject") || a1.startsWith("_r_") || a1.startsWith("_w_");
    }
    
    public Reflection() {
        this.classPool = null;
        this.converter = new CodeConverter();
    }
    
    @Override
    public void start(final ClassPool v-1) throws NotFoundException {
        /*SL:109*/this.classPool = v-1;
        final String v0 = /*EL:110*/"javassist.tools.reflect.Sample is not found or broken.";
        try {
            final CtClass a1 = /*EL:113*/this.classPool.get("javassist.tools.reflect.Sample");
            /*SL:114*/this.rebuildClassFile(a1.getClassFile());
            /*SL:115*/this.trapMethod = a1.getDeclaredMethod("trap");
            /*SL:116*/this.trapStaticMethod = a1.getDeclaredMethod("trapStatic");
            /*SL:117*/this.trapRead = a1.getDeclaredMethod("trapRead");
            /*SL:118*/this.trapWrite = a1.getDeclaredMethod("trapWrite");
            /*SL:120*/this.readParam = new CtClass[] { this.classPool.get("java.lang.Object") };
        }
        catch (NotFoundException v) {
            /*SL:123*/throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
        }
        catch (BadBytecode v2) {
            /*SL:125*/throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
        }
    }
    
    @Override
    public void onLoad(final ClassPool a1, final String a2) throws CannotCompileException, NotFoundException {
        final CtClass v1 = /*EL:136*/a1.get(a2);
        /*SL:137*/v1.instrument(this.converter);
    }
    
    public boolean makeReflective(final String a1, final String a2, final String a3) throws CannotCompileException, NotFoundException {
        /*SL:157*/return this.makeReflective(this.classPool.get(a1), this.classPool.get(a2), /*EL:158*/this.classPool.get(a3));
    }
    
    public boolean makeReflective(final Class a1, final Class a2, final Class a3) throws CannotCompileException, NotFoundException {
        /*SL:183*/return this.makeReflective(a1.getName(), a2.getName(), a3.getName());
    }
    
    public boolean makeReflective(final CtClass a1, final CtClass a2, final CtClass a3) throws CannotCompileException, CannotReflectException, NotFoundException {
        /*SL:210*/if (a1.isInterface()) {
            /*SL:211*/throw new CannotReflectException("Cannot reflect an interface: " + a1.getName());
        }
        /*SL:214*/if (a1.subclassOf(this.classPool.get("javassist.tools.reflect.ClassMetaobject"))) {
            /*SL:215*/throw new CannotReflectException("Cannot reflect a subclass of ClassMetaobject: " + a1.getName());
        }
        /*SL:219*/if (a1.subclassOf(this.classPool.get("javassist.tools.reflect.Metaobject"))) {
            /*SL:220*/throw new CannotReflectException("Cannot reflect a subclass of Metaobject: " + a1.getName());
        }
        /*SL:224*/this.registerReflectiveClass(a1);
        /*SL:225*/return this.modifyClassfile(a1, a2, a3);
    }
    
    private void registerReflectiveClass(final CtClass v-2) {
        final CtField[] declaredFields = /*EL:233*/v-2.getDeclaredFields();
        /*SL:234*/for (int v0 = 0; v0 < declaredFields.length; ++v0) {
            final CtField v = /*EL:235*/declaredFields[v0];
            final int v2 = /*EL:236*/v.getModifiers();
            /*SL:237*/if ((v2 & 0x1) != 0x0 && (v2 & 0x10) == 0x0) {
                final String a1 = /*EL:238*/v.getName();
                /*SL:239*/this.converter.replaceFieldRead(v, v-2, "_r_" + a1);
                /*SL:240*/this.converter.replaceFieldWrite(v, v-2, "_w_" + a1);
            }
        }
    }
    
    private boolean modifyClassfile(final CtClass a3, final CtClass v1, final CtClass v2) throws CannotCompileException, NotFoundException {
        /*SL:249*/if (a3.getAttribute("Reflective") != null) {
            /*SL:250*/return false;
        }
        /*SL:252*/a3.setAttribute("Reflective", new byte[0]);
        final CtClass v3 = /*EL:254*/this.classPool.get("javassist.tools.reflect.Metalevel");
        final boolean v4 = /*EL:255*/!a3.subtypeOf(v3);
        /*SL:256*/if (v4) {
            /*SL:257*/a3.addInterface(v3);
        }
        /*SL:259*/this.processMethods(a3, v4);
        /*SL:260*/this.processFields(a3);
        /*SL:263*/if (v4) {
            final CtField a4 = /*EL:264*/new CtField(this.classPool.get("javassist.tools.reflect.Metaobject"), "_metaobject", a3);
            /*SL:266*/a4.setModifiers(4);
            /*SL:267*/a3.addField(a4, CtField.Initializer.byNewWithParams(v1));
            /*SL:269*/a3.addMethod(CtNewMethod.getter("_getMetaobject", a4));
            /*SL:270*/a3.addMethod(CtNewMethod.setter("_setMetaobject", a4));
        }
        final CtField v5 = /*EL:273*/new CtField(this.classPool.get("javassist.tools.reflect.ClassMetaobject"), "_classobject", a3);
        /*SL:275*/v5.setModifiers(10);
        /*SL:276*/a3.addField(v5, CtField.Initializer.byNew(v2, new String[] { a3.getName() }));
        /*SL:279*/a3.addMethod(CtNewMethod.getter("_getClass", v5));
        /*SL:280*/return true;
    }
    
    private void processMethods(final CtClass v-2, final boolean v-1) throws CannotCompileException, NotFoundException {
        final CtMethod[] v0 = /*EL:286*/v-2.getMethods();
        /*SL:287*/for (int v = 0; v < v0.length; ++v) {
            final CtMethod a1 = /*EL:288*/v0[v];
            final int a2 = /*EL:289*/a1.getModifiers();
            /*SL:290*/if (Modifier.isPublic(a2) && !Modifier.isAbstract(a2)) {
                /*SL:291*/this.processMethods0(a2, v-2, a1, v, v-1);
            }
        }
    }
    
    private void processMethods0(int a4, final CtClass a5, final CtMethod v1, final int v2, final boolean v3) throws CannotCompileException, NotFoundException {
        final String v4 = /*EL:300*/v1.getName();
        /*SL:302*/if (this.isExcluded(v4)) {
            /*SL:303*/return;
        }
        final CtMethod v5;
        /*SL:306*/if (v1.getDeclaringClass() == a5) {
            /*SL:307*/if (Modifier.isNative(a4)) {
                /*SL:308*/return;
            }
            /*SL:311*/if (Modifier.isFinal(a4)) {
                /*SL:312*/a4 &= 0xFFFFFFEF;
                /*SL:313*/v1.setModifiers(a4);
            }
        }
        else {
            /*SL:317*/if (Modifier.isFinal(a4)) {
                /*SL:318*/return;
            }
            /*SL:320*/a4 &= 0xFFFFFEFF;
            /*SL:321*/v5 = CtNewMethod.delegator(this.findOriginal(v1, v3), a5);
            /*SL:322*/v5.setModifiers(a4);
            /*SL:323*/a5.addMethod(v5);
        }
        /*SL:326*/v5.setName("_m_" + v2 + "_" + v4);
        final CtMethod v6;
        /*SL:329*/if (Modifier.isStatic(a4)) {
            final CtMethod a6 = /*EL:330*/this.trapStaticMethod;
        }
        else {
            /*SL:332*/v6 = this.trapMethod;
        }
        final CtMethod v7 = /*EL:335*/CtNewMethod.wrapped(v1.getReturnType(), v4, v1.getParameterTypes(), /*EL:336*/v1.getExceptionTypes(), v6, /*EL:337*/CtMethod.ConstParameter.integer(v2), a5);
        /*SL:339*/v7.setModifiers(a4);
        /*SL:340*/a5.addMethod(v7);
    }
    
    private CtMethod findOriginal(final CtMethod v2, final boolean v3) throws NotFoundException {
        /*SL:346*/if (v3) {
            /*SL:347*/return v2;
        }
        final String v4 = /*EL:349*/v2.getName();
        final CtMethod[] v5 = /*EL:350*/v2.getDeclaringClass().getDeclaredMethods();
        /*SL:351*/for (String a2 = (String)0; a2 < v5.length; ++a2) {
            /*SL:352*/a2 = v5[a2].getName();
            /*SL:354*/if (a2.endsWith(v4) && a2.startsWith("_m_") && v5[a2].getSignature().equals(/*EL:355*/v2.getSignature())) {
                /*SL:356*/return v5[a2];
            }
        }
        /*SL:359*/return v2;
    }
    
    private void processFields(final CtClass v-5) throws CannotCompileException, NotFoundException {
        final CtField[] declaredFields = /*EL:365*/v-5.getDeclaredFields();
        /*SL:366*/for (int i = 0; i < declaredFields.length; ++i) {
            final CtField ctField = /*EL:367*/declaredFields[i];
            int modifiers = /*EL:368*/ctField.getModifiers();
            /*SL:369*/if ((modifiers & 0x1) != 0x0 && (modifiers & 0x10) == 0x0) {
                /*SL:370*/modifiers |= 0x8;
                final String a1 = /*EL:371*/ctField.getName();
                final CtClass v1 = /*EL:372*/ctField.getType();
                CtMethod v2 = /*EL:374*/CtNewMethod.wrapped(v1, "_r_" + a1, this.readParam, null, this.trapRead, /*EL:376*/CtMethod.ConstParameter.string(a1), v-5);
                /*SL:378*/v2.setModifiers(modifiers);
                /*SL:379*/v-5.addMethod(v2);
                final CtClass[] v3 = /*EL:380*/{ /*EL:381*/this.classPool.get("java.lang.Object"), /*EL:382*/v1 };
                /*SL:383*/v2 = CtNewMethod.wrapped(CtClass.voidType, "_w_" + a1, v3, null, this.trapWrite, /*EL:386*/CtMethod.ConstParameter.string(a1), v-5);
                /*SL:387*/v2.setModifiers(modifiers);
                /*SL:388*/v-5.addMethod(v2);
            }
        }
    }
    
    public void rebuildClassFile(final ClassFile v2) throws BadBytecode {
        /*SL:394*/if (ClassFile.MAJOR_VERSION < 50) {
            /*SL:395*/return;
        }
        /*SL:397*/for (final MethodInfo a1 : v2.getMethods()) {
            /*SL:400*/a1.rebuildStackMap(this.classPool);
        }
    }
}
