package javassist.compiler;

import javassist.compiler.ast.Declarator;
import java.util.HashMap;

public final class SymbolTable extends HashMap
{
    private SymbolTable parent;
    
    public SymbolTable() {
        this(null);
    }
    
    public SymbolTable(final SymbolTable a1) {
        this.parent = a1;
    }
    
    public SymbolTable getParent() {
        /*SL:32*/return this.parent;
    }
    
    public Declarator lookup(final String a1) {
        final Declarator v1 = /*EL:35*/this.get(a1);
        /*SL:36*/if (v1 == null && this.parent != null) {
            /*SL:37*/return this.parent.lookup(a1);
        }
        /*SL:39*/return v1;
    }
    
    public void append(final String a1, final Declarator a2) {
        /*SL:43*/this.put(a1, a2);
    }
}
