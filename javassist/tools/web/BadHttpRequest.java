package javassist.tools.web;

public class BadHttpRequest extends Exception
{
    private Exception e;
    
    public BadHttpRequest() {
        this.e = null;
    }
    
    public BadHttpRequest(final Exception a1) {
        this.e = a1;
    }
    
    @Override
    public String toString() {
        /*SL:30*/if (this.e == null) {
            /*SL:31*/return super.toString();
        }
        /*SL:33*/return this.e.toString();
    }
}
