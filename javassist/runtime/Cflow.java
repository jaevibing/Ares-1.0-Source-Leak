package javassist.runtime;

public class Cflow extends ThreadLocal
{
    @Override
    protected synchronized Object initialValue() {
        /*SL:36*/return new Depth();
    }
    
    public void enter() {
        /*SL:42*/this.get().inc();
    }
    
    public void exit() {
        /*SL:47*/this.get().dec();
    }
    
    public int value() {
        /*SL:52*/return this.get().get();
    }
    
    private static class Depth
    {
        private int depth;
        
        Depth() {
            this.depth = 0;
        }
        
        int get() {
            /*SL:30*/return this.depth;
        }
        
        void inc() {
            /*SL:31*/++this.depth;
        }
        
        void dec() {
            /*SL:32*/--this.depth;
        }
    }
}
