package org.spongepowered.tools.obfuscation.mapping.common;

import java.io.IOException;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.PrintWriter;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;

public abstract class MappingWriter implements IMappingWriter
{
    private final Messager messager;
    private final Filer filer;
    
    public MappingWriter(final Messager a1, final Filer a2) {
        this.messager = a1;
        this.filer = a2;
    }
    
    protected PrintWriter openFileWriter(final String v1, final String v2) throws IOException {
        /*SL:62*/if (v1.matches("^.*[\\\\/:].*$")) {
            final File a1 = /*EL:63*/new File(v1);
            /*SL:64*/a1.getParentFile().mkdirs();
            /*SL:65*/this.messager.printMessage(Diagnostic.Kind.NOTE, "Writing " + v2 + " to " + a1.getAbsolutePath());
            /*SL:66*/return new PrintWriter(a1);
        }
        final FileObject v3 = /*EL:69*/this.filer.createResource(StandardLocation.CLASS_OUTPUT, "", v1, new Element[0]);
        /*SL:70*/this.messager.printMessage(Diagnostic.Kind.NOTE, "Writing " + v2 + " to " + new File(v3.toUri()).getAbsolutePath());
        /*SL:71*/return new PrintWriter(v3.openWriter());
    }
}
