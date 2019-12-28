package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.AnnotationMirror;

public final class AnnotationHandle
{
    public static final AnnotationHandle MISSING;
    private final AnnotationMirror annotation;
    
    private AnnotationHandle(final AnnotationMirror a1) {
        this.annotation = a1;
    }
    
    public AnnotationMirror asMirror() {
        /*SL:69*/return this.annotation;
    }
    
    public boolean exists() {
        /*SL:79*/return this.annotation != null;
    }
    
    @Override
    public String toString() {
        /*SL:84*/if (this.annotation == null) {
            /*SL:85*/return "@{UnknownAnnotation}";
        }
        /*SL:87*/return "@" + (Object)this.annotation.getAnnotationType().asElement().getSimpleName();
    }
    
    public <T> T getValue(final String v1, final T v2) {
        /*SL:101*/if (this.annotation == null) {
            /*SL:102*/return v2;
        }
        final AnnotationValue v3 = /*EL:105*/this.getAnnotationValue(v1);
        /*SL:106*/if (!(v2 instanceof Enum) || v3 == null) {
            /*SL:114*/return (T)((v3 != null) ? v3.getValue() : v2);
        }
        final VariableElement a1 = (VariableElement)v3.getValue();
        if (a1 == null) {
            return v2;
        }
        return Enum.<T>valueOf(v2.getClass(), a1.getSimpleName().toString());
    }
    
    public <T> T getValue() {
        /*SL:124*/return this.<T>getValue("value", (T)null);
    }
    
    public <T> T getValue(final String a1) {
        /*SL:136*/return this.<T>getValue(a1, (T)null);
    }
    
    public boolean getBoolean(final String a1, final boolean a2) {
        /*SL:148*/return this.<Boolean>getValue(a1, a2);
    }
    
    public AnnotationHandle getAnnotation(final String v2) {
        final Object v3 = /*EL:158*/this.<Object>getValue(v2);
        /*SL:159*/if (v3 instanceof AnnotationMirror) {
            /*SL:160*/return of((AnnotationMirror)v3);
        }
        /*SL:161*/if (v3 instanceof AnnotationValue) {
            final Object a1 = /*EL:162*/((AnnotationValue)v3).getValue();
            /*SL:163*/if (a1 instanceof AnnotationMirror) {
                /*SL:164*/return of((AnnotationMirror)a1);
            }
        }
        /*SL:167*/return null;
    }
    
    public <T> List<T> getList() {
        /*SL:178*/return this.<T>getList("value");
    }
    
    public <T> List<T> getList(final String a1) {
        final List<AnnotationValue> v1 = /*EL:191*/this.<List<AnnotationValue>>getValue(a1, Collections.<AnnotationValue>emptyList());
        /*SL:192*/return AnnotationHandle.<T>unwrapAnnotationValueList(v1);
    }
    
    public List<AnnotationHandle> getAnnotationList(final String v2) {
        final Object v3 = /*EL:202*/this.<Object>getValue(v2, (Object)null);
        /*SL:203*/if (v3 == null) {
            /*SL:204*/return Collections.<AnnotationHandle>emptyList();
        }
        /*SL:208*/if (v3 instanceof AnnotationMirror) {
            /*SL:209*/return ImmutableList.<AnnotationHandle>of(of((AnnotationMirror)v3));
        }
        final List<AnnotationValue> v4 = /*EL:212*/(List<AnnotationValue>)v3;
        final List<AnnotationHandle> v5 = /*EL:213*/new ArrayList<AnnotationHandle>(v4.size());
        /*SL:214*/for (final AnnotationValue a1 : v4) {
            /*SL:215*/v5.add(new AnnotationHandle((AnnotationMirror)a1.getValue()));
        }
        /*SL:217*/return Collections.<AnnotationHandle>unmodifiableList((List<? extends AnnotationHandle>)v5);
    }
    
    protected AnnotationValue getAnnotationValue(final String v2) {
        /*SL:221*/for (final ExecutableElement a1 : this.annotation.getElementValues().keySet()) {
            /*SL:222*/if (a1.getSimpleName().contentEquals(v2)) {
                /*SL:223*/return (AnnotationValue)this.annotation.getElementValues().get(a1);
            }
        }
        /*SL:227*/return null;
    }
    
    protected static <T> List<T> unwrapAnnotationValueList(final List<AnnotationValue> v1) {
        /*SL:232*/if (v1 == null) {
            /*SL:233*/return Collections.<T>emptyList();
        }
        final List<T> v2 = /*EL:236*/new ArrayList<T>(v1.size());
        /*SL:237*/for (final AnnotationValue a1 : v1) {
            /*SL:238*/v2.add((T)a1.getValue());
        }
        /*SL:241*/return v2;
    }
    
    protected static AnnotationMirror getAnnotation(final Element v-3, final Class<? extends Annotation> v-2) {
        /*SL:245*/if (v-3 == null) {
            /*SL:246*/return null;
        }
        final List<? extends AnnotationMirror> annotationMirrors = /*EL:249*/v-3.getAnnotationMirrors();
        /*SL:251*/if (annotationMirrors == null) {
            /*SL:252*/return null;
        }
        /*SL:255*/for (final AnnotationMirror v1 : annotationMirrors) {
            final Element a1 = /*EL:256*/v1.getAnnotationType().asElement();
            /*SL:257*/if (!(a1 instanceof TypeElement)) {
                /*SL:258*/continue;
            }
            final TypeElement a2 = /*EL:260*/(TypeElement)a1;
            /*SL:261*/if (a2.getQualifiedName().contentEquals(v-2.getName())) {
                /*SL:262*/return v1;
            }
        }
        /*SL:266*/return null;
    }
    
    public static AnnotationHandle of(final AnnotationMirror a1) {
        /*SL:276*/return new AnnotationHandle(a1);
    }
    
    public static AnnotationHandle of(final Element a1, final Class<? extends Annotation> a2) {
        /*SL:289*/return new AnnotationHandle(getAnnotation(a1, a2));
    }
    
    static {
        MISSING = new AnnotationHandle(null);
    }
}
