package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.type.TypeKind;
import javax.lang.model.element.Modifier;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.ArrayType;
import org.spongepowered.asm.util.SignaturePrinter;
import java.util.Iterator;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;

public abstract class TypeUtils
{
    private static final int MAX_GENERIC_RECURSION_DEPTH = 5;
    private static final String OBJECT_SIG = "java.lang.Object";
    private static final String OBJECT_REF = "java/lang/Object";
    
    public static PackageElement getPackage(final TypeMirror a1) {
        /*SL:67*/if (!(a1 instanceof DeclaredType)) {
            /*SL:68*/return null;
        }
        /*SL:70*/return getPackage((TypeElement)((DeclaredType)a1).asElement());
    }
    
    public static PackageElement getPackage(final TypeElement a1) {
        Element v1;
        /*SL:80*/for (v1 = a1.getEnclosingElement(); v1 != null && !(v1 instanceof PackageElement); /*SL:81*/v1 = v1.getEnclosingElement()) {}
        /*SL:83*/return (PackageElement)v1;
    }
    
    public static String getElementType(final Element a1) {
        /*SL:94*/if (a1 instanceof TypeElement) {
            /*SL:95*/return "TypeElement";
        }
        /*SL:96*/if (a1 instanceof ExecutableElement) {
            /*SL:97*/return "ExecutableElement";
        }
        /*SL:98*/if (a1 instanceof VariableElement) {
            /*SL:99*/return "VariableElement";
        }
        /*SL:100*/if (a1 instanceof PackageElement) {
            /*SL:101*/return "PackageElement";
        }
        /*SL:102*/if (a1 instanceof TypeParameterElement) {
            /*SL:103*/return "TypeParameterElement";
        }
        /*SL:106*/return a1.getClass().getSimpleName();
    }
    
    public static String stripGenerics(final String v-1) {
        final StringBuilder v0 = /*EL:116*/new StringBuilder();
        int v = /*EL:117*/0;
        int v2 = 0;
        while (v < v-1.length()) {
            final char a1 = /*EL:118*/v-1.charAt(v);
            /*SL:119*/if (a1 == '<') {
                /*SL:120*/++v2;
            }
            /*SL:122*/if (v2 == 0) {
                /*SL:123*/v0.append(a1);
            }
            else/*SL:124*/ if (a1 == '>') {
                /*SL:125*/--v2;
            }
            ++v;
        }
        /*SL:128*/return v0.toString();
    }
    
    public static String getName(final VariableElement a1) {
        /*SL:138*/return (a1 != null) ? a1.getSimpleName().toString() : null;
    }
    
    public static String getName(final ExecutableElement a1) {
        /*SL:148*/return (a1 != null) ? a1.getSimpleName().toString() : null;
    }
    
    public static String getJavaSignature(final Element v0) {
        /*SL:161*/if (v0 instanceof ExecutableElement) {
            final ExecutableElement v = /*EL:162*/(ExecutableElement)v0;
            final StringBuilder v2 = /*EL:163*/new StringBuilder().append("(");
            boolean v3 = /*EL:164*/false;
            /*SL:165*/for (final VariableElement a1 : v.getParameters()) {
                /*SL:166*/if (v3) {
                    /*SL:167*/v2.append(',');
                }
                /*SL:169*/v2.append(getTypeName(a1.asType()));
                /*SL:170*/v3 = true;
            }
            /*SL:172*/v2.append(')').append(getTypeName(v.getReturnType()));
            /*SL:173*/return v2.toString();
        }
        /*SL:175*/return getTypeName(v0.asType());
    }
    
    public static String getJavaSignature(final String a1) {
        /*SL:185*/return new SignaturePrinter("", a1).setFullyQualified(true).toDescriptor();
    }
    
    public static String getTypeName(final TypeMirror a1) {
        /*SL:195*/switch (a1.getKind()) {
            case ARRAY: {
                /*SL:196*/return getTypeName(((ArrayType)a1).getComponentType()) + "[]";
            }
            case DECLARED: {
                /*SL:197*/return getTypeName((DeclaredType)a1);
            }
            case TYPEVAR: {
                /*SL:198*/return getTypeName(getUpperBound(a1));
            }
            case ERROR: {
                /*SL:199*/return "java.lang.Object";
            }
            default: {
                /*SL:200*/return a1.toString();
            }
        }
    }
    
    public static String getTypeName(final DeclaredType a1) {
        /*SL:211*/if (a1 == null) {
            /*SL:212*/return "java.lang.Object";
        }
        /*SL:214*/return getInternalName((TypeElement)a1.asElement()).replace('/', '.');
    }
    
    public static String getDescriptor(final Element a1) {
        /*SL:224*/if (a1 instanceof ExecutableElement) {
            /*SL:225*/return getDescriptor((ExecutableElement)a1);
        }
        /*SL:226*/if (a1 instanceof VariableElement) {
            /*SL:227*/return getInternalName((VariableElement)a1);
        }
        /*SL:230*/return getInternalName(a1.asType());
    }
    
    public static String getDescriptor(final ExecutableElement v1) {
        /*SL:240*/if (v1 == null) {
            /*SL:241*/return null;
        }
        final StringBuilder v2 = /*EL:244*/new StringBuilder();
        /*SL:246*/for (final VariableElement a1 : v1.getParameters()) {
            /*SL:247*/v2.append(getInternalName(a1));
        }
        final String v3 = getInternalName(/*EL:250*/v1.getReturnType());
        /*SL:251*/return String.format("(%s)%s", v2, v3);
    }
    
    public static String getInternalName(final VariableElement a1) {
        /*SL:261*/if (a1 == null) {
            /*SL:262*/return null;
        }
        /*SL:264*/return getInternalName(a1.asType());
    }
    
    public static String getInternalName(final TypeMirror a1) {
        /*SL:274*/switch (a1.getKind()) {
            case ARRAY: {
                /*SL:275*/return "[" + getInternalName(((ArrayType)a1).getComponentType());
            }
            case DECLARED: {
                /*SL:276*/return "L" + getInternalName((DeclaredType)a1) + ";";
            }
            case TYPEVAR: {
                /*SL:277*/return "L" + getInternalName(getUpperBound(a1)) + ";";
            }
            case BOOLEAN: {
                /*SL:278*/return "Z";
            }
            case BYTE: {
                /*SL:279*/return "B";
            }
            case CHAR: {
                /*SL:280*/return "C";
            }
            case DOUBLE: {
                /*SL:281*/return "D";
            }
            case FLOAT: {
                /*SL:282*/return "F";
            }
            case INT: {
                /*SL:283*/return "I";
            }
            case LONG: {
                /*SL:284*/return "J";
            }
            case SHORT: {
                /*SL:285*/return "S";
            }
            case VOID: {
                /*SL:286*/return "V";
            }
            case ERROR: {
                /*SL:288*/return "Ljava/lang/Object;";
            }
            default: {
                /*SL:292*/throw new IllegalArgumentException("Unable to parse type symbol " + a1 + " with " + a1.getKind() + " to equivalent bytecode type");
            }
        }
    }
    
    public static String getInternalName(final DeclaredType a1) {
        /*SL:302*/if (a1 == null) {
            /*SL:303*/return "java/lang/Object";
        }
        /*SL:305*/return getInternalName((TypeElement)a1.asElement());
    }
    
    public static String getInternalName(final TypeElement a1) {
        /*SL:315*/if (a1 == null) {
            /*SL:316*/return null;
        }
        final StringBuilder v1 = /*EL:318*/new StringBuilder();
        /*SL:319*/v1.append(a1.getSimpleName());
        /*SL:321*/for (Element v2 = a1.getEnclosingElement(); v2 != null; /*SL:327*/v2 = v2.getEnclosingElement()) {
            if (v2 instanceof TypeElement) {
                v1.insert(0, "$").insert(0, v2.getSimpleName());
            }
            else if (v2 instanceof PackageElement) {
                v1.insert(0, "/").insert(0, ((PackageElement)v2).getQualifiedName().toString().replace('.', '/'));
            }
        }
        /*SL:329*/return v1.toString();
    }
    
    private static DeclaredType getUpperBound(final TypeMirror v0) {
        try {
            /*SL:334*/return getUpperBound0(v0, 5);
        }
        catch (IllegalStateException a1) {
            /*SL:336*/throw new IllegalArgumentException("Type symbol \"" + v0 + "\" is too complex", a1);
        }
        catch (IllegalArgumentException v) {
            /*SL:338*/throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + v0, v);
        }
    }
    
    private static DeclaredType getUpperBound0(final TypeMirror v-1, int v0) {
        /*SL:343*/if (v0 == 0) {
            /*SL:344*/throw new IllegalStateException("Generic symbol \"" + v-1 + "\" is too complex, exceeded " + 5 + " iterations attempting to determine upper bound");
        }
        /*SL:347*/if (v-1 instanceof DeclaredType) {
            /*SL:348*/return (DeclaredType)v-1;
        }
        /*SL:350*/if (v-1 instanceof TypeVariable) {
            try {
                final TypeMirror a1 = /*EL:352*/((TypeVariable)v-1).getUpperBound();
                /*SL:353*/return getUpperBound0(a1, --v0);
            }
            catch (IllegalStateException a2) {
                /*SL:355*/throw a2;
            }
            catch (IllegalArgumentException v) {
                /*SL:357*/throw v;
            }
            catch (Exception v2) {
                /*SL:359*/throw new IllegalArgumentException("Unable to compute upper bound of type symbol " + v-1);
            }
        }
        /*SL:362*/return null;
    }
    
    public static boolean isAssignable(final ProcessingEnvironment a3, final TypeMirror v1, final TypeMirror v2) {
        final boolean v3 = /*EL:374*/a3.getTypeUtils().isAssignable(v1, v2);
        /*SL:375*/if (!v3 && v1 instanceof DeclaredType && v2 instanceof DeclaredType) {
            final TypeMirror a4 = toRawType(/*EL:376*/a3, (DeclaredType)v1);
            final TypeMirror a5 = toRawType(/*EL:377*/a3, (DeclaredType)v2);
            /*SL:378*/return a3.getTypeUtils().isAssignable(a4, a5);
        }
        /*SL:381*/return v3;
    }
    
    private static TypeMirror toRawType(final ProcessingEnvironment a1, final DeclaredType a2) {
        /*SL:385*/return a1.getElementUtils().getTypeElement(((TypeElement)a2.asElement()).getQualifiedName()).asType();
    }
    
    public static Visibility getVisibility(final Element v1) {
        /*SL:395*/if (v1 == null) {
            /*SL:396*/return null;
        }
        /*SL:399*/for (final Modifier a1 : v1.getModifiers()) {
            /*SL:400*/switch (a1) {
                case PUBLIC: {
                    /*SL:401*/return Visibility.PUBLIC;
                }
                case PROTECTED: {
                    /*SL:402*/return Visibility.PROTECTED;
                }
                case PRIVATE: {
                    /*SL:403*/return Visibility.PRIVATE;
                }
                default: {
                    /*SL:406*/continue;
                }
            }
        }
        /*SL:408*/return Visibility.PACKAGE;
    }
}
