package javassist;

import java.net.URL;
import java.io.InputStream;
import java.io.FilenameFilter;
import java.io.File;

final class JarDirClassPath implements ClassPath
{
    JarClassPath[] jars;
    
    JarDirClassPath(final String v2) throws NotFoundException {
        final File[] v3 = new File(v2).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File a1, String a2) {
                /*SL:82*/a2 = a2.toLowerCase();
                /*SL:83*/return a2.endsWith(".jar") || a2.endsWith(".zip");
            }
        });
        if (v3 != null) {
            this.jars = new JarClassPath[v3.length];
            for (int a1 = 0; a1 < v3.length; ++a1) {
                this.jars[a1] = new JarClassPath(v3[a1].getPath());
            }
        }
    }
    
    @Override
    public InputStream openClassfile(final String v0) throws NotFoundException {
        /*SL:95*/if (this.jars != null) {
            /*SL:96*/for (int v = 0; v < this.jars.length; ++v) {
                final InputStream a1 = /*EL:97*/this.jars[v].openClassfile(v0);
                /*SL:98*/if (a1 != null) {
                    /*SL:99*/return a1;
                }
            }
        }
        /*SL:102*/return null;
    }
    
    @Override
    public URL find(final String v0) {
        /*SL:106*/if (this.jars != null) {
            /*SL:107*/for (int v = 0; v < this.jars.length; ++v) {
                final URL a1 = /*EL:108*/this.jars[v].find(v0);
                /*SL:109*/if (a1 != null) {
                    /*SL:110*/return a1;
                }
            }
        }
        /*SL:113*/return null;
    }
    
    @Override
    public void close() {
        /*SL:117*/if (this.jars != null) {
            /*SL:118*/for (int v1 = 0; v1 < this.jars.length; ++v1) {
                /*SL:119*/this.jars[v1].close();
            }
        }
    }
}
