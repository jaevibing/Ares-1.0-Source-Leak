package javassist.tools.rmi;

import java.io.Serializable;

public class RemoteRef implements Serializable
{
    public int oid;
    public String classname;
    
    public RemoteRef(final int a1) {
        this.oid = a1;
        this.classname = null;
    }
    
    public RemoteRef(final int a1, final String a2) {
        this.oid = a1;
        this.classname = a2;
    }
}
