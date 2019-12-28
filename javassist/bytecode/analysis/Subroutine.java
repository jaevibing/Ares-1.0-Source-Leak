package javassist.bytecode.analysis;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public class Subroutine
{
    private List callers;
    private Set access;
    private int start;
    
    public Subroutine(final int a1, final int a2) {
        this.callers = new ArrayList();
        this.access = new HashSet();
        this.start = a1;
        this.callers.add(new Integer(a2));
    }
    
    public void addCaller(final int a1) {
        /*SL:41*/this.callers.add(new Integer(a1));
    }
    
    public int start() {
        /*SL:45*/return this.start;
    }
    
    public void access(final int a1) {
        /*SL:49*/this.access.add(new Integer(a1));
    }
    
    public boolean isAccessed(final int a1) {
        /*SL:53*/return this.access.contains(new Integer(a1));
    }
    
    public Collection accessed() {
        /*SL:57*/return this.access;
    }
    
    public Collection callers() {
        /*SL:61*/return this.callers;
    }
    
    @Override
    public String toString() {
        /*SL:65*/return "start = " + this.start + " callers = " + this.callers.toString();
    }
}
