package org.spongepowered.asm.util;

public class ReEntranceLock
{
    private final int maxDepth;
    private int depth;
    private boolean semaphore;
    
    public ReEntranceLock(final int a1) {
        this.depth = 0;
        this.semaphore = false;
        this.maxDepth = a1;
    }
    
    public int getMaxDepth() {
        /*SL:55*/return this.maxDepth;
    }
    
    public int getDepth() {
        /*SL:62*/return this.depth;
    }
    
    public ReEntranceLock push() {
        /*SL:72*/++this.depth;
        /*SL:73*/this.checkAndSet();
        /*SL:74*/return this;
    }
    
    public ReEntranceLock pop() {
        /*SL:83*/if (this.depth == 0) {
            /*SL:84*/throw new IllegalStateException("ReEntranceLock pop() with zero depth");
        }
        /*SL:87*/--this.depth;
        /*SL:88*/return this;
    }
    
    public boolean check() {
        /*SL:97*/return this.depth > this.maxDepth;
    }
    
    public boolean checkAndSet() {
        /*SL:106*/return this.semaphore |= this.check();
    }
    
    public ReEntranceLock set() {
        /*SL:115*/this.semaphore = true;
        /*SL:116*/return this;
    }
    
    public boolean isSet() {
        /*SL:123*/return this.semaphore;
    }
    
    public ReEntranceLock clear() {
        /*SL:132*/this.semaphore = false;
        /*SL:133*/return this;
    }
}
