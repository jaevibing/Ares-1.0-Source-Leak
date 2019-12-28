package org.yaml.snakeyaml.introspector;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GenericProperty extends Property
{
    private Type genType;
    private boolean actualClassesChecked;
    private Class<?>[] actualClasses;
    
    public GenericProperty(final String a1, final Class<?> a2, final Type a3) {
        super(a1, a2);
        this.genType = a3;
        this.actualClassesChecked = (a3 == null);
    }
    
    @Override
    public Class<?>[] getActualTypeArguments() {
        /*SL:37*/if (!this.actualClassesChecked) {
            /*SL:38*/if (this.genType instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = /*EL:39*/(ParameterizedType)this.genType;
                final Type[] actualTypeArguments = /*EL:40*/parameterizedType.getActualTypeArguments();
                /*SL:41*/if (actualTypeArguments.length > 0) {
                    /*SL:42*/this.actualClasses = (Class<?>[])new Class[actualTypeArguments.length];
                    /*SL:43*/for (int v0 = 0; v0 < actualTypeArguments.length; ++v0) {
                        /*SL:44*/if (actualTypeArguments[v0] instanceof Class) {
                            /*SL:45*/this.actualClasses[v0] = (Class<?>)actualTypeArguments[v0];
                        }
                        else/*SL:46*/ if (actualTypeArguments[v0] instanceof ParameterizedType) {
                            /*SL:48*/this.actualClasses[v0] = (Class<?>)((ParameterizedType)actualTypeArguments[v0]).getRawType();
                        }
                        else {
                            /*SL:49*/if (!(actualTypeArguments[v0] instanceof GenericArrayType)) {
                                /*SL:60*/this.actualClasses = null;
                                /*SL:61*/break;
                            }
                            final Type v = ((GenericArrayType)actualTypeArguments[v0]).getGenericComponentType();
                            if (!(v instanceof Class)) {
                                this.actualClasses = null;
                                break;
                            }
                            this.actualClasses[v0] = Array.newInstance((Class<?>)v, 0).getClass();
                        }
                    }
                }
            }
            else/*SL:65*/ if (this.genType instanceof GenericArrayType) {
                final Type genericComponentType = /*EL:66*/((GenericArrayType)this.genType).getGenericComponentType();
                /*SL:67*/if (genericComponentType instanceof Class) {
                    /*SL:68*/this.actualClasses = (Class<?>[])new Class[] { (Class)genericComponentType };
                }
            }
            else/*SL:70*/ if (this.genType instanceof Class) {
                final Class<?> clazz = /*EL:72*/(Class<?>)this.genType;
                /*SL:73*/if (clazz.isArray()) {
                    /*SL:75*/(this.actualClasses = (Class<?>[])new Class[1])[0] = this.getType().getComponentType();
                }
            }
            /*SL:78*/this.actualClassesChecked = true;
        }
        /*SL:80*/return this.actualClasses;
    }
}
