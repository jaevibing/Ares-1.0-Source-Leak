package org.spongepowered.tools.obfuscation.struct;

import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class Message
{
    private Diagnostic.Kind kind;
    private CharSequence msg;
    private final Element element;
    private final AnnotationMirror annotation;
    private final AnnotationValue value;
    
    public Message(final Diagnostic.Kind a1, final CharSequence a2) {
        this(a1, a2, null, (AnnotationMirror)null, null);
    }
    
    public Message(final Diagnostic.Kind a1, final CharSequence a2, final Element a3) {
        this(a1, a2, a3, (AnnotationMirror)null, null);
    }
    
    public Message(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationHandle a4) {
        this(a1, a2, a3, a4.asMirror(), null);
    }
    
    public Message(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationMirror a4) {
        this(a1, a2, a3, a4, null);
    }
    
    public Message(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationHandle a4, final AnnotationValue a5) {
        this(a1, a2, a3, a4.asMirror(), a5);
    }
    
    public Message(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationMirror a4, final AnnotationValue a5) {
        this.kind = a1;
        this.msg = a2;
        this.element = a3;
        this.annotation = a4;
        this.value = a5;
    }
    
    public Message sendTo(final Messager a1) {
        /*SL:82*/if (this.value != null) {
            /*SL:83*/a1.printMessage(this.kind, this.msg, this.element, this.annotation, this.value);
        }
        else/*SL:84*/ if (this.annotation != null) {
            /*SL:85*/a1.printMessage(this.kind, this.msg, this.element, this.annotation);
        }
        else/*SL:86*/ if (this.element != null) {
            /*SL:87*/a1.printMessage(this.kind, this.msg, this.element);
        }
        else {
            /*SL:89*/a1.printMessage(this.kind, this.msg);
        }
        /*SL:91*/return this;
    }
    
    public Diagnostic.Kind getKind() {
        /*SL:98*/return this.kind;
    }
    
    public Message setKind(final Diagnostic.Kind a1) {
        /*SL:108*/this.kind = a1;
        /*SL:109*/return this;
    }
    
    public CharSequence getMsg() {
        /*SL:118*/return this.msg;
    }
    
    public Message setMsg(final CharSequence a1) {
        /*SL:128*/this.msg = a1;
        /*SL:129*/return this;
    }
    
    public Element getElement() {
        /*SL:136*/return this.element;
    }
    
    public AnnotationMirror getAnnotation() {
        /*SL:143*/return this.annotation;
    }
    
    public AnnotationValue getValue() {
        /*SL:150*/return this.value;
    }
}
