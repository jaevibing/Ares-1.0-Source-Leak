package javassist;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.util.jar.JarFile;

final class JarClassPath implements ClassPath
{
    JarFile jarfile;
    String jarfileURL;
    
    JarClassPath(final String a1) throws NotFoundException {
        try {
            this.jarfile = new JarFile(a1);
            this.jarfileURL = new File(a1).getCanonicalFile().toURI().toURL().toString();
        }
        catch (IOException ex) {
            throw new NotFoundException(a1);
        }
    }
    
    @Override
    public InputStream openClassfile(final String v-1) throws NotFoundException {
        try {
            final String a1 = /*EL:142*/v-1.replace('.', '/') + ".class";
            final JarEntry v1 = /*EL:143*/this.jarfile.getJarEntry(a1);
            /*SL:144*/if (v1 != null) {
                /*SL:145*/return this.jarfile.getInputStream(v1);
            }
            /*SL:147*/return null;
        }
        catch (IOException ex) {
            /*SL:150*/throw new NotFoundException("broken jar file?: " + this.jarfile.getName());
        }
    }
    
    @Override
    public URL find(final String a1) {
        final String v1 = /*EL:155*/a1.replace('.', '/') + ".class";
        final JarEntry v2 = /*EL:156*/this.jarfile.getJarEntry(v1);
        /*SL:157*/if (v2 != null) {
            try {
                /*SL:159*/return new URL("jar:" + this.jarfileURL + "!/" + v1);
            }
            catch (MalformedURLException ex) {}
        }
        /*SL:163*/return null;
    }
    
    @Override
    public void close() {
        try {
            /*SL:168*/this.jarfile.close();
            /*SL:169*/this.jarfile = null;
        }
        catch (IOException ex) {}
    }
    
    @Override
    public String toString() {
        /*SL:175*/return (this.jarfile == null) ? "<null>" : this.jarfile.toString();
    }
}
