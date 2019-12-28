package javassist.bytecode.annotation;

import java.io.IOException;
import javassist.bytecode.ConstPool;
import java.io.OutputStream;

public class TypeAnnotationsWriter extends AnnotationsWriter
{
    public TypeAnnotationsWriter(final OutputStream a1, final ConstPool a2) {
        super(a1, a2);
    }
    
    @Override
    public void numAnnotations(final int a1) throws IOException {
        /*SL:32*/super.numAnnotations(a1);
    }
    
    public void typeParameterTarget(final int a1, final int a2) throws IOException {
        /*SL:42*/this.output.write(a1);
        /*SL:43*/this.output.write(a2);
    }
    
    public void supertypeTarget(final int a1) throws IOException {
        /*SL:53*/this.output.write(16);
        /*SL:54*/this.write16bit(a1);
    }
    
    public void typeParameterBoundTarget(final int a1, final int a2, final int a3) throws IOException {
        /*SL:64*/this.output.write(a1);
        /*SL:65*/this.output.write(a2);
        /*SL:66*/this.output.write(a3);
    }
    
    public void emptyTarget(final int a1) throws IOException {
        /*SL:74*/this.output.write(a1);
    }
    
    public void formalParameterTarget(final int a1) throws IOException {
        /*SL:84*/this.output.write(22);
        /*SL:85*/this.output.write(a1);
    }
    
    public void throwsTarget(final int a1) throws IOException {
        /*SL:95*/this.output.write(23);
        /*SL:96*/this.write16bit(a1);
    }
    
    public void localVarTarget(final int a1, final int a2) throws IOException {
        /*SL:108*/this.output.write(a1);
        /*SL:109*/this.write16bit(a2);
    }
    
    public void localVarTargetTable(final int a1, final int a2, final int a3) throws IOException {
        /*SL:119*/this.write16bit(a1);
        /*SL:120*/this.write16bit(a2);
        /*SL:121*/this.write16bit(a3);
    }
    
    public void catchTarget(final int a1) throws IOException {
        /*SL:131*/this.output.write(66);
        /*SL:132*/this.write16bit(a1);
    }
    
    public void offsetTarget(final int a1, final int a2) throws IOException {
        /*SL:142*/this.output.write(a1);
        /*SL:143*/this.write16bit(a2);
    }
    
    public void typeArgumentTarget(final int a1, final int a2, final int a3) throws IOException {
        /*SL:153*/this.output.write(a1);
        /*SL:154*/this.write16bit(a2);
        /*SL:155*/this.output.write(a3);
    }
    
    public void typePath(final int a1) throws IOException {
        /*SL:162*/this.output.write(a1);
    }
    
    public void typePathPath(final int a1, final int a2) throws IOException {
        /*SL:171*/this.output.write(a1);
        /*SL:172*/this.output.write(a2);
    }
}
