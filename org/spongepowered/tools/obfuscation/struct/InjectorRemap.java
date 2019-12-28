package org.spongepowered.tools.obfuscation.struct;

import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class InjectorRemap
{
    private final boolean remap;
    private Message message;
    private int remappedCount;
    
    public InjectorRemap(final boolean a1) {
        this.remap = a1;
    }
    
    public boolean shouldRemap() {
        /*SL:69*/return this.remap;
    }
    
    public void notifyRemapped() {
        /*SL:77*/++this.remappedCount;
        /*SL:78*/this.clearMessage();
    }
    
    public void addMessage(final Diagnostic.Kind a1, final CharSequence a2, final Element a3, final AnnotationHandle a4) {
        /*SL:91*/this.message = new Message(a1, a2, a3, a4);
    }
    
    public void clearMessage() {
        /*SL:98*/this.message = null;
    }
    
    public void dispatchPendingMessages(final Messager a1) {
        /*SL:108*/if (this.remappedCount == 0 && this.message != null) {
            /*SL:109*/this.message.sendTo(a1);
        }
    }
}
