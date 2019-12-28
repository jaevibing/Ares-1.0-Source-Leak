package org.yaml.snakeyaml.introspector;

import java.util.logging.Level;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.lang.annotation.Annotation;
import java.util.List;
import org.yaml.snakeyaml.error.YAMLException;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Collection;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class PropertySubstitute extends Property
{
    private static final Logger log;
    protected Class<?> targetType;
    private final String readMethod;
    private final String writeMethod;
    private transient Method read;
    private transient Method write;
    private Field field;
    protected Class<?>[] parameters;
    private Property delegate;
    private boolean filler;
    
    public PropertySubstitute(final String a1, final Class<?> a2, final String a3, final String a4, final Class<?>... a5) {
        super(a1, a2);
        this.readMethod = a3;
        this.writeMethod = a4;
        this.setActualTypeArguments(a5);
        this.filler = false;
    }
    
    public PropertySubstitute(final String a1, final Class<?> a2, final Class<?>... a3) {
        this(a1, a2, null, (String)null, a3);
    }
    
    @Override
    public Class<?>[] getActualTypeArguments() {
        /*SL:64*/if (this.parameters == null && this.delegate != null) {
            /*SL:65*/return this.delegate.getActualTypeArguments();
        }
        /*SL:67*/return this.parameters;
    }
    
    public void setActualTypeArguments(final Class<?>... a1) {
        /*SL:71*/if (a1 != null && a1.length > 0) {
            /*SL:72*/this.parameters = a1;
        }
        else {
            /*SL:74*/this.parameters = null;
        }
    }
    
    @Override
    public void set(final Object v-3, final Object v-2) throws Exception {
        /*SL:80*/if (this.write != null) {
            /*SL:81*/if (!this.filler) {
                /*SL:82*/this.write.invoke(v-3, v-2);
            }
            else/*SL:83*/ if (v-2 != null) {
                /*SL:84*/if (v-2 instanceof Collection) {
                    Collection<?> a2 = /*EL:85*/(Collection<?>)v-2;
                    final Iterator<?> iterator = /*EL:86*/a2.iterator();
                    while (iterator.hasNext()) {
                        a2 = iterator.next();
                        /*SL:87*/this.write.invoke(v-3, a2);
                    }
                }
                else/*SL:89*/ if (v-2 instanceof Map) {
                    final Map<?, ?> a3 = /*EL:90*/(Map<?, ?>)v-2;
                    /*SL:91*/for (final Map.Entry<?, ?> a4 : a3.entrySet()) {
                        /*SL:92*/this.write.invoke(v-3, a4.getKey(), a4.getValue());
                    }
                }
                else/*SL:94*/ if (v-2.getClass().isArray()) {
                    /*SL:99*/for (int length = Array.getLength(v-2), v0 = 0; v0 < length; ++v0) {
                        /*SL:100*/this.write.invoke(v-3, Array.get(v-2, v0));
                    }
                }
            }
        }
        else/*SL:104*/ if (this.field != null) {
            /*SL:105*/this.field.set(v-3, v-2);
        }
        else/*SL:106*/ if (this.delegate != null) {
            /*SL:107*/this.delegate.set(v-3, v-2);
        }
        else {
            PropertySubstitute.log.warning(/*EL:109*/"No setter/delegate for '" + this.getName() + "' on object " + v-3);
        }
    }
    
    @Override
    public Object get(final Object v2) {
        try {
            /*SL:117*/if (this.read != null) {
                /*SL:118*/return this.read.invoke(v2, new Object[0]);
            }
            /*SL:119*/if (this.field != null) {
                /*SL:120*/return this.field.get(v2);
            }
        }
        catch (Exception a1) {
            /*SL:123*/throw new YAMLException("Unable to find getter for property '" + this.getName() + "' on object " + v2 + ":" + a1);
        }
        /*SL:127*/if (this.delegate != null) {
            /*SL:128*/return this.delegate.get(v2);
        }
        /*SL:130*/throw new YAMLException("No getter or delegate for property '" + this.getName() + "' on object " + v2);
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        Annotation[] v1 = /*EL:136*/null;
        /*SL:137*/if (this.read != null) {
            /*SL:138*/v1 = this.read.getAnnotations();
        }
        else/*SL:139*/ if (this.field != null) {
            /*SL:140*/v1 = this.field.getAnnotations();
        }
        /*SL:142*/return (v1 != null) ? Arrays.<Annotation>asList(v1) : this.delegate.getAnnotations();
    }
    
    @Override
    public <A extends java.lang.Object> A getAnnotation(final Class<A> v0) {
        A a1;
        /*SL:148*/if (this.read != null) {
            /*SL:149*/a1 = this.read.<A>getAnnotation(v0);
        }
        else/*SL:150*/ if (this.field != null) {
            /*SL:151*/a1 = this.field.<A>getAnnotation(v0);
        }
        else {
            /*SL:153*/a1 = (A)this.delegate.getAnnotation((Class)v0);
        }
        /*SL:155*/return a1;
    }
    
    public void setTargetType(final Class<?> v-5) {
        /*SL:159*/if (this.targetType != v-5) {
            /*SL:160*/this.targetType = v-5;
            final String name = /*EL:162*/this.getName();
            /*SL:163*/for (Class<?> superclass = v-5; superclass != null; superclass = superclass.getSuperclass()) {
                final Field[] declaredFields = /*EL:164*/superclass.getDeclaredFields();
                final int length = declaredFields.length;
                int i = 0;
                while (i < length) {
                    final Field v1 = declaredFields[i];
                    /*SL:165*/if (v1.getName().equals(name)) {
                        final int a1 = /*EL:166*/v1.getModifiers();
                        /*SL:167*/if (!Modifier.isStatic(a1) && !Modifier.isTransient(a1)) {
                            /*SL:168*/v1.setAccessible(true);
                            /*SL:169*/this.field = v1;
                            break;
                        }
                        break;
                    }
                    else {
                        ++i;
                    }
                }
            }
            /*SL:175*/if (this.field == null && PropertySubstitute.log.isLoggable(Level.FINE)) {
                PropertySubstitute.log.fine(/*EL:176*/String.format("Failed to find field for %s.%s", v-5.getName(), this.getName()));
            }
            /*SL:181*/if (this.readMethod != null) {
                /*SL:182*/this.read = this.discoverMethod(v-5, this.readMethod, (Class<?>[])new Class[0]);
            }
            /*SL:184*/if (this.writeMethod != null) {
                /*SL:185*/this.filler = false;
                /*SL:186*/this.write = this.discoverMethod(v-5, this.writeMethod, this.getType());
                /*SL:187*/if (this.write == null && this.parameters != null) {
                    /*SL:188*/this.filler = true;
                    /*SL:189*/this.write = this.discoverMethod(v-5, this.writeMethod, this.parameters);
                }
            }
        }
    }
    
    private Method discoverMethod(final Class<?> v-6, final String v-5, final Class<?>... v-4) {
        /*SL:196*/for (Class<?> superclass = v-6; superclass != null; superclass = superclass.getSuperclass()) {
            /*SL:197*/for (final Method v1 : superclass.getDeclaredMethods()) {
                /*SL:198*/if (v-5.equals(v1.getName())) {
                    Class<?>[] a2 = /*EL:199*/v1.getParameterTypes();
                    /*SL:200*/if (a2.length == v-4.length) {
                        /*SL:203*/a2 = true;
                        /*SL:204*/for (int a3 = 0; a3 < a2.length; ++a3) {
                            /*SL:205*/if (!a2[a3].isAssignableFrom(v-4[a3])) {
                                /*SL:206*/a2 = false;
                            }
                        }
                        /*SL:209*/if (a2) {
                            /*SL:210*/v1.setAccessible(true);
                            /*SL:211*/return v1;
                        }
                    }
                }
            }
        }
        /*SL:216*/if (PropertySubstitute.log.isLoggable(Level.FINE)) {
            PropertySubstitute.log.fine(/*EL:217*/String.format("Failed to find [%s(%d args)] for %s.%s", v-5, v-4.length, this.targetType.getName(), /*EL:218*/this.getName()));
        }
        /*SL:220*/return null;
    }
    
    @Override
    public String getName() {
        final String v1 = /*EL:225*/super.getName();
        /*SL:226*/if (v1 != null) {
            /*SL:227*/return v1;
        }
        /*SL:229*/return (this.delegate != null) ? this.delegate.getName() : null;
    }
    
    @Override
    public Class<?> getType() {
        final Class<?> v1 = /*EL:234*/super.getType();
        /*SL:235*/if (v1 != null) {
            /*SL:236*/return v1;
        }
        /*SL:238*/return (this.delegate != null) ? this.delegate.getType() : null;
    }
    
    @Override
    public boolean isReadable() {
        /*SL:243*/return this.read != null || this.field != null || (this.delegate != null && this.delegate.isReadable());
    }
    
    @Override
    public boolean isWritable() {
        /*SL:248*/return this.write != null || this.field != null || (this.delegate != null && this.delegate.isWritable());
    }
    
    public void setDelegate(final Property a1) {
        /*SL:252*/this.delegate = a1;
        /*SL:253*/if (this.writeMethod != null && this.write == null && !this.filler) {
            /*SL:254*/this.filler = true;
            /*SL:255*/this.write = this.discoverMethod(this.targetType, this.writeMethod, this.getActualTypeArguments());
        }
    }
    
    static {
        log = Logger.getLogger(PropertySubstitute.class.getPackage().getName());
    }
}
