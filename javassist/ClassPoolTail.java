package javassist;

import java.net.URL;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;

final class ClassPoolTail
{
    protected ClassPathList pathList;
    
    public ClassPoolTail() {
        this.pathList = null;
    }
    
    @Override
    public String toString() {
        final StringBuffer v1 = /*EL:187*/new StringBuffer();
        /*SL:188*/v1.append("[class path: ");
        /*SL:190*/for (ClassPathList v2 = this.pathList; v2 != null; /*SL:193*/v2 = v2.next) {
            v1.append(v2.path.toString());
            v1.append(File.pathSeparatorChar);
        }
        /*SL:196*/v1.append(']');
        /*SL:197*/return v1.toString();
    }
    
    public synchronized ClassPath insertClassPath(final ClassPath a1) {
        /*SL:201*/this.pathList = new ClassPathList(a1, this.pathList);
        /*SL:202*/return a1;
    }
    
    public synchronized ClassPath appendClassPath(final ClassPath a1) {
        final ClassPathList v1 = /*EL:206*/new ClassPathList(a1, null);
        ClassPathList v2 = /*EL:207*/this.pathList;
        /*SL:208*/if (v2 == null) {
            /*SL:209*/this.pathList = v1;
        }
        else {
            /*SL:211*/while (v2.next != null) {
                /*SL:212*/v2 = v2.next;
            }
            /*SL:214*/v2.next = v1;
        }
        /*SL:217*/return a1;
    }
    
    public synchronized void removeClassPath(final ClassPath a1) {
        ClassPathList v1 = /*EL:221*/this.pathList;
        /*SL:222*/if (v1 != null) {
            /*SL:223*/if (v1.path == a1) {
                /*SL:224*/this.pathList = v1.next;
            }
            else {
                /*SL:226*/while (v1.next != null) {
                    /*SL:227*/if (v1.next.path == a1) {
                        /*SL:228*/v1.next = v1.next.next;
                    }
                    else {
                        /*SL:230*/v1 = v1.next;
                    }
                }
            }
        }
        /*SL:233*/a1.close();
    }
    
    public ClassPath appendSystemPath() {
        /*SL:237*/return this.appendClassPath(new ClassClassPath());
    }
    
    public ClassPath insertClassPath(final String a1) throws NotFoundException {
        /*SL:243*/return this.insertClassPath(makePathObject(a1));
    }
    
    public ClassPath appendClassPath(final String a1) throws NotFoundException {
        /*SL:249*/return this.appendClassPath(makePathObject(a1));
    }
    
    private static ClassPath makePathObject(final String v1) throws NotFoundException {
        final String v2 = /*EL:255*/v1.toLowerCase();
        /*SL:256*/if (v2.endsWith(".jar") || v2.endsWith(".zip")) {
            /*SL:257*/return new JarClassPath(v1);
        }
        final int v3 = /*EL:259*/v1.length();
        /*SL:260*/if (v3 > 2 && v1.charAt(v3 - 1) == '*' && (v1.charAt(v3 - 2) == /*EL:261*/'/' || v1.charAt(v3 - 2) == File.separatorChar)) {
            final String a1 = /*EL:263*/v1.substring(0, v3 - 2);
            /*SL:264*/return new JarDirClassPath(a1);
        }
        /*SL:267*/return new DirClassPath(v1);
    }
    
    void writeClassfile(final String a1, final OutputStream a2) throws NotFoundException, IOException, CannotCompileException {
        final InputStream v1 = /*EL:276*/this.openClassfile(a1);
        /*SL:277*/if (v1 == null) {
            /*SL:278*/throw new NotFoundException(a1);
        }
        try {
            copyStream(/*EL:281*/v1, a2);
        }
        finally {
            /*SL:284*/v1.close();
        }
    }
    
    InputStream openClassfile(final String v2) throws NotFoundException {
        ClassPathList v3 = /*EL:318*/this.pathList;
        InputStream v4 = /*EL:319*/null;
        NotFoundException v5 = /*EL:320*/null;
        /*SL:321*/while (v3 != null) {
            try {
                /*SL:323*/v4 = v3.path.openClassfile(v2);
            }
            catch (NotFoundException a1) {
                /*SL:326*/if (v5 == null) {
                    /*SL:327*/v5 = a1;
                }
            }
            /*SL:330*/if (v4 != null) {
                /*SL:333*/return v4;
            }
            v3 = v3.next;
        }
        /*SL:336*/if (v5 != null) {
            /*SL:337*/throw v5;
        }
        /*SL:339*/return null;
    }
    
    public URL find(final String a1) {
        ClassPathList v1 = /*EL:351*/this.pathList;
        URL v2 = /*EL:352*/null;
        /*SL:353*/while (v1 != null) {
            /*SL:354*/v2 = v1.path.find(a1);
            /*SL:355*/if (v2 != null) {
                /*SL:358*/return v2;
            }
            v1 = v1.next;
        }
        /*SL:361*/return null;
    }
    
    public static byte[] readStream(final InputStream v-5) throws IOException {
        final byte[][] array = /*EL:370*/new byte[8][];
        int n = /*EL:371*/4096;
        /*SL:373*/for (int i = 0; i < 8; ++i) {
            /*SL:374*/array[i] = new byte[n];
            int j = /*EL:375*/0;
            int v0 = /*EL:376*/0;
            /*SL:392*/do {
                v0 = v-5.read(array[i], j, n - j);
                if (v0 < 0) {
                    final byte[] v = new byte[n - 4096 + j];
                    int v2 = 0;
                    for (int a1 = 0; a1 < i; ++a1) {
                        System.arraycopy(array[a1], 0, v, v2, v2 + 4096);
                        v2 = v2 + v2 + 4096;
                    }
                    System.arraycopy(array[i], 0, v, v2, j);
                    return v;
                }
                j += v0;
            } while (j < n);
            /*SL:393*/n *= 2;
        }
        /*SL:396*/throw new IOException("too much data");
    }
    
    public static void copyStream(final InputStream v-3, final OutputStream v-2) throws IOException {
        int n = /*EL:407*/4096;
        byte[] v0 = /*EL:408*/null;
        /*SL:409*/for (int v = 0; v < 64; ++v) {
            /*SL:410*/if (v < 8) {
                /*SL:411*/n *= 2;
                /*SL:412*/v0 = new byte[n];
            }
            int a1 = /*EL:414*/0;
            int a2 = /*EL:415*/0;
            /*SL:424*/do {
                a2 = v-3.read(v0, a1, n - a1);
                if (a2 < 0) {
                    v-2.write(v0, 0, a1);
                    return;
                }
                a1 += a2;
            } while (a1 < n);
            /*SL:425*/v-2.write(v0);
        }
        /*SL:428*/throw new IOException("too much data");
    }
}
