package javassist.tools.rmi;

import java.lang.reflect.Method;
import javassist.CtConstructor;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.CtField;
import javassist.Modifier;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.CtClass;
import javassist.CtMethod;
import java.util.Hashtable;
import javassist.ClassPool;
import javassist.Translator;

public class StubGenerator implements Translator
{
    private static final String fieldImporter = "importer";
    private static final String fieldObjectId = "objectId";
    private static final String accessorObjectId = "_getObjectId";
    private static final String sampleClass = "javassist.tools.rmi.Sample";
    private ClassPool classPool;
    private Hashtable proxyClasses;
    private CtMethod forwardMethod;
    private CtMethod forwardStaticMethod;
    private CtClass[] proxyConstructorParamTypes;
    private CtClass[] interfacesForProxy;
    private CtClass[] exceptionForProxy;
    
    public StubGenerator() {
        this.proxyClasses = new Hashtable();
    }
    
    @Override
    public void start(final ClassPool a1) throws NotFoundException {
        /*SL:72*/this.classPool = a1;
        final CtClass v1 = /*EL:73*/a1.get("javassist.tools.rmi.Sample");
        /*SL:74*/this.forwardMethod = v1.getDeclaredMethod("forward");
        /*SL:75*/this.forwardStaticMethod = v1.getDeclaredMethod("forwardStatic");
        /*SL:78*/this.proxyConstructorParamTypes = a1.get(new String[] { "javassist.tools.rmi.ObjectImporter", "int" });
        /*SL:81*/this.interfacesForProxy = a1.get(new String[] { "java.io.Serializable", "javassist.tools.rmi.Proxy" });
        /*SL:84*/this.exceptionForProxy = new CtClass[] { a1.get("javassist.tools.rmi.RemoteException") };
    }
    
    @Override
    public void onLoad(final ClassPool a1, final String a2) {
    }
    
    public boolean isProxyClass(final String a1) {
        /*SL:101*/return this.proxyClasses.get(a1) != null;
    }
    
    public synchronized boolean makeProxyClass(final Class v2) throws CannotCompileException, NotFoundException {
        final String v3 = /*EL:116*/v2.getName();
        /*SL:117*/if (this.proxyClasses.get(v3) != null) {
            /*SL:118*/return false;
        }
        final CtClass a1 = /*EL:120*/this.produceProxyClass(this.classPool.get(v3), v2);
        /*SL:122*/this.proxyClasses.put(v3, a1);
        /*SL:123*/this.modifySuperclass(a1);
        /*SL:124*/return true;
    }
    
    private CtClass produceProxyClass(final CtClass v1, final Class v2) throws CannotCompileException, NotFoundException {
        final int v3 = /*EL:131*/v1.getModifiers();
        /*SL:132*/if (Modifier.isAbstract(v3) || Modifier.isNative(v3) || /*EL:133*/!Modifier.isPublic(v3)) {
            /*SL:134*/throw new CannotCompileException(v1.getName() + " must be public, non-native, and non-abstract.");
        }
        final CtClass v4 = /*EL:137*/this.classPool.makeClass(v1.getName(), v1.getSuperclass());
        /*SL:140*/v4.setInterfaces(this.interfacesForProxy);
        CtField v5 = /*EL:143*/new CtField(this.classPool.get("javassist.tools.rmi.ObjectImporter"), "importer", v4);
        /*SL:145*/v5.setModifiers(2);
        /*SL:146*/v4.addField(v5, CtField.Initializer.byParameter(0));
        /*SL:148*/v5 = new CtField(CtClass.intType, "objectId", v4);
        /*SL:149*/v5.setModifiers(2);
        /*SL:150*/v4.addField(v5, CtField.Initializer.byParameter(1));
        /*SL:152*/v4.addMethod(CtNewMethod.getter("_getObjectId", v5));
        /*SL:154*/v4.addConstructor(CtNewConstructor.defaultConstructor(v4));
        final CtConstructor v6 = /*EL:156*/CtNewConstructor.skeleton(this.proxyConstructorParamTypes, null, v4);
        /*SL:158*/v4.addConstructor(v6);
        try {
            /*SL:161*/this.addMethods(v4, v2.getMethods());
            /*SL:162*/return v4;
        }
        catch (SecurityException a1) {
            /*SL:165*/throw new CannotCompileException(a1);
        }
    }
    
    private CtClass toCtClass(Class v-1) throws NotFoundException {
        final String v2;
        /*SL:171*/if (!v-1.isArray()) {
            final String a1 = /*EL:172*/v-1.getName();
        }
        else {
            final StringBuffer v1 = /*EL:174*/new StringBuffer();
            /*SL:178*/do {
                v1.append("[]");
                v-1 = v-1.getComponentType();
            } while (v-1.isArray());
            /*SL:179*/v1.insert(0, v-1.getName());
            /*SL:180*/v2 = v1.toString();
        }
        /*SL:183*/return this.classPool.get(v2);
    }
    
    private CtClass[] toCtClass(final Class[] v2) throws NotFoundException {
        final int v3 = /*EL:187*/v2.length;
        final CtClass[] v4 = /*EL:188*/new CtClass[v3];
        /*SL:189*/for (int a1 = 0; a1 < v3; ++a1) {
            /*SL:190*/v4[a1] = this.toCtClass(v2[a1]);
        }
        /*SL:192*/return v4;
    }
    
    private void addMethods(final CtClass v-1, final Method[] v0) throws CannotCompileException, NotFoundException {
        /*SL:202*/for (int v = 0; v < v0.length; ++v) {
            final Method v2 = /*EL:203*/v0[v];
            final int v3 = /*EL:204*/v2.getModifiers();
            /*SL:205*/if (v2.getDeclaringClass() != Object.class && /*EL:206*/!Modifier.isFinal(v3)) {
                /*SL:207*/if (Modifier.isPublic(v3)) {
                    final CtMethod a2;
                    /*SL:209*/if (Modifier.isStatic(v3)) {
                        final CtMethod a1 = /*EL:210*/this.forwardStaticMethod;
                    }
                    else {
                        /*SL:212*/a2 = this.forwardMethod;
                    }
                    final CtMethod v4 = /*EL:215*/CtNewMethod.wrapped(this.toCtClass(v2.getReturnType()), v2.getName(), /*EL:216*/this.toCtClass((Class[])v2.getParameterTypes()), /*EL:217*/this.exceptionForProxy, a2, /*EL:220*/CtMethod.ConstParameter.integer(v), v-1);
                    /*SL:222*/v4.setModifiers(v3);
                    /*SL:223*/v-1.addMethod(v4);
                }
                else/*SL:225*/ if (!Modifier.isProtected(v3) && /*EL:226*/!Modifier.isPrivate(v3)) {
                    /*SL:228*/throw new CannotCompileException("the methods must be public, protected, or private.");
                }
            }
        }
    }
    
    private void modifySuperclass(CtClass a1) throws CannotCompileException, NotFoundException {
        while (true) {
            final CtClass v1 = /*EL:241*/a1.getSuperclass();
            /*SL:242*/if (v1 == null) {
                /*SL:243*/break;
            }
            try {
                /*SL:246*/v1.getDeclaredConstructor(null);
            }
            catch (NotFoundException ex) {
                /*SL:252*/v1.addConstructor(/*EL:253*/CtNewConstructor.defaultConstructor(v1));
                a1 = v1;
                continue;
            }
            break;
        }
    }
}
