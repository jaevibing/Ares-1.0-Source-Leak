package javassist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;

final class DirClassPath implements ClassPath
{
    String directory;
    
    DirClassPath(final String a1) {
        this.directory = a1;
    }
    
    @Override
    public InputStream openClassfile(final String v-1) {
        try {
            final char a1 = File.separatorChar;
            final String v1 = /*EL:45*/this.directory + a1 + v-1.replace('.', a1) + /*EL:46*/".class";
            /*SL:47*/return new FileInputStream(v1.toString());
        }
        catch (FileNotFoundException ex) {}
        catch (SecurityException ex2) {}
        /*SL:51*/return null;
    }
    
    @Override
    public URL find(final String a1) {
        final char v1 = File.separatorChar;
        final String v2 = /*EL:56*/this.directory + v1 + a1.replace('.', v1) + /*EL:57*/".class";
        final File v3 = /*EL:58*/new File(v2);
        /*SL:59*/if (v3.exists()) {
            try {
                /*SL:61*/return v3.getCanonicalFile().toURI().toURL();
            }
            catch (MalformedURLException ex) {}
            catch (IOException ex2) {}
        }
        /*SL:66*/return null;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String toString() {
        /*SL:72*/return this.directory;
    }
}
