package org.reflections.scanners;

import javassist.ClassPath;
import javassist.LoaderClassPath;
import org.reflections.util.ClasspathHelper;
import com.google.common.base.Joiner;
import javassist.bytecode.MethodInfo;
import javassist.expr.FieldAccess;
import javassist.expr.ConstructorCall;
import javassist.expr.MethodCall;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.NewExpr;
import javassist.expr.ExprEditor;
import javassist.CtMethod;
import javassist.CtBehavior;
import javassist.CtConstructor;
import javassist.CtClass;
import org.reflections.ReflectionsException;
import javassist.ClassPool;

public class MemberUsageScanner extends AbstractScanner
{
    private ClassPool classPool;
    
    @Override
    public void scan(final Object v-4) {
        try {
            final CtClass value = /*EL:19*/this.getClassPool().get(this.getMetadataAdapter().getClassName(v-4));
            /*SL:20*/for (final CtBehavior a1 : value.getDeclaredConstructors()) {
                /*SL:21*/this.scanMember(a1);
            }
            /*SL:23*/for (final CtBehavior v1 : value.getDeclaredMethods()) {
                /*SL:24*/this.scanMember(v1);
            }
            /*SL:26*/value.detach();
        }
        catch (Exception a2) {
            /*SL:28*/throw new ReflectionsException("Could not scan method usage for " + this.getMetadataAdapter().getClassName(v-4), a2);
        }
    }
    
    void scanMember(final CtBehavior a1) throws CannotCompileException {
        final String v1 = /*EL:34*/a1.getDeclaringClass().getName() + "." + a1.getMethodInfo().getName() + "(" + this.parameterNames(a1.getMethodInfo()) + /*EL:35*/")";
        /*SL:36*/a1.instrument(new ExprEditor() {
            @Override
            public void edit(final NewExpr v2) throws CannotCompileException {
                try {
                    /*SL:40*/MemberUsageScanner.this.put(v2.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.this.parameterNames(v2.getConstructor().getMethodInfo()) + /*EL:41*/")", v2.getLineNumber(), v1);
                }
                catch (NotFoundException a1) {
                    /*SL:43*/throw new ReflectionsException("Could not find new instance usage in " + v1, a1);
                }
            }
            
            @Override
            public void edit(final MethodCall v2) throws CannotCompileException {
                try {
                    /*SL:50*/MemberUsageScanner.this.put(v2.getMethod().getDeclaringClass().getName() + "." + v2.getMethodName() + "(" + MemberUsageScanner.this.parameterNames(v2.getMethod().getMethodInfo()) + /*EL:51*/")", v2.getLineNumber(), v1);
                }
                catch (NotFoundException a1) {
                    /*SL:53*/throw new ReflectionsException("Could not find member " + v2.getClassName() + " in " + v1, a1);
                }
            }
            
            @Override
            public void edit(final ConstructorCall v2) throws CannotCompileException {
                try {
                    /*SL:60*/MemberUsageScanner.this.put(v2.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.this.parameterNames(v2.getConstructor().getMethodInfo()) + /*EL:61*/")", v2.getLineNumber(), v1);
                }
                catch (NotFoundException a1) {
                    /*SL:63*/throw new ReflectionsException("Could not find member " + v2.getClassName() + " in " + v1, a1);
                }
            }
            
            @Override
            public void edit(final FieldAccess v2) throws CannotCompileException {
                try {
                    /*SL:70*/MemberUsageScanner.this.put(v2.getField().getDeclaringClass().getName() + "." + v2.getFieldName(), v2.getLineNumber(), /*EL:72*/v1);
                }
                catch (NotFoundException a1) {
                    throw new ReflectionsException("Could not find member " + v2.getFieldName() + " in " + v1, a1);
                }
            }
        });
    }
    
    private void put(final String a1, final int a2, final String a3) {
        /*SL:79*/if (this.acceptResult(a1)) {
            /*SL:80*/this.getStore().put(a1, a3 + " #" + a2);
        }
    }
    
    String parameterNames(final MethodInfo a1) {
        /*SL:85*/return Joiner.on(", ").join(this.getMetadataAdapter().getParameterNames(a1));
    }
    
    private ClassPool getClassPool() {
        /*SL:89*/if (this.classPool == null) {
            /*SL:90*/synchronized (this) {
                /*SL:91*/this.classPool = new ClassPool();
                ClassLoader[] array = /*EL:92*/this.getConfiguration().getClassLoaders();
                /*SL:93*/if (array == null) {
                    /*SL:94*/array = ClasspathHelper.classLoaders(new ClassLoader[0]);
                }
                /*SL:96*/for (final ClassLoader v1 : array) {
                    /*SL:97*/this.classPool.appendClassPath(new LoaderClassPath(v1));
                }
            }
        }
        /*SL:101*/return this.classPool;
    }
}
