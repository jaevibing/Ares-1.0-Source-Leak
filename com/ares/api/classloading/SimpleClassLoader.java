package com.ares.api.classloading;

import net.minecraftforge.fml.common.FMLLog;
import java.util.HashSet;
import java.util.Set;

public class SimpleClassLoader
{
    private Class[] clazzes;
    private Set<Class> erroredClasses;
    
    public SimpleClassLoader build(final Class[] a1) {
        /*SL:19*/this.clazzes = a1;
        /*SL:20*/this.erroredClasses = new HashSet<Class>(a1.length);
        /*SL:21*/return this;
    }
    
    public SimpleClassLoader initialise() {
        /*SL:26*/for (final Class v0 : this.clazzes) {
            try {
                /*SL:30*/v0.newInstance();
            }
            catch (Exception v) {
                /*SL:34*/this.erroredClasses.add(v0);
                FMLLog.log.info(/*EL:35*/"Error initialising class " + v0.getName());
                /*SL:36*/v.printStackTrace();
            }
        }
        /*SL:40*/return this;
    }
    
    public Set<Class> getErroredClasses() {
        /*SL:45*/return this.erroredClasses;
    }
}
