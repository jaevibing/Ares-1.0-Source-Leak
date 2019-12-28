package com.ares.api.classloading;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.net.URL;
import java.util.List;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class AresUrlClassLoader
{
    public final LaunchClassLoader classLoader;
    
    public AresUrlClassLoader() {
        this.classLoader = Launch.classLoader;
    }
    
    public List<Class> getClasses(final String a3, final String v1, final String... v2) throws MalformedURLException, ClassNotFoundException {
        final URL v3 = /*EL:39*/new URL(a3 + "/" + v1);
        /*SL:40*/this.classLoader.addURL(v3);
        final List<Class> v4 = /*EL:42*/new ArrayList<Class>();
        /*SL:44*/for (final String a4 : v2) {
            /*SL:46*/v4.add(this.classLoader.loadClass(a4));
        }
        /*SL:49*/return v4;
    }
}
