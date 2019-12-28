package javassist;

import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;

public class URLClassPath implements ClassPath
{
    protected String hostname;
    protected int port;
    protected String directory;
    protected String packageName;
    
    public URLClassPath(final String a1, final int a2, final String a3, final String a4) {
        this.hostname = a1;
        this.port = a2;
        this.directory = a3;
        this.packageName = a4;
    }
    
    @Override
    public String toString() {
        /*SL:69*/return this.hostname + ":" + this.port + this.directory;
    }
    
    @Override
    public InputStream openClassfile(final String v2) {
        try {
            final URLConnection a1 = /*EL:79*/this.openClassfile0(v2);
            /*SL:80*/if (a1 != null) {
                /*SL:81*/return a1.getInputStream();
            }
        }
        catch (IOException ex) {}
        /*SL:84*/return null;
    }
    
    private URLConnection openClassfile0(final String v2) throws IOException {
        /*SL:88*/if (this.packageName == null || v2.startsWith(this.packageName)) {
            final String a1 = /*EL:89*/this.directory + v2.replace('.', '/') + /*EL:90*/".class";
            /*SL:91*/return fetchClass0(this.hostname, this.port, a1);
        }
        /*SL:94*/return null;
    }
    
    @Override
    public URL find(final String v-1) {
        try {
            final URLConnection a1 = /*EL:104*/this.openClassfile0(v-1);
            final InputStream v1 = /*EL:105*/a1.getInputStream();
            /*SL:106*/if (v1 != null) {
                /*SL:107*/v1.close();
                /*SL:108*/return a1.getURL();
            }
        }
        catch (IOException ex) {}
        /*SL:112*/return null;
    }
    
    @Override
    public void close() {
    }
    
    public static byte[] fetchClass(final String v1, final int v2, final String v3, final String v4) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* v1 */
        //     1: iload_1         /* v2 */
        //     2: new             Ljava/lang/StringBuilder;
        //     5: dup            
        //     6: invokespecial   java/lang/StringBuilder.<init>:()V
        //     9: aload_2         /* v3 */
        //    10: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    13: aload_3         /* v4 */
        //    14: bipush          46
        //    16: bipush          47
        //    18: invokevirtual   java/lang/String.replace:(CC)Ljava/lang/String;
        //    21: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    24: ldc             ".class"
        //    26: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    29: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    32: invokestatic    javassist/URLClassPath.fetchClass0:(Ljava/lang/String;ILjava/lang/String;)Ljava/net/URLConnection;
        //    35: astore          v6
        //    37: aload           v6
        //    39: invokevirtual   java/net/URLConnection.getContentLength:()I
        //    42: istore          v7
        //    44: aload           v6
        //    46: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //    49: astore          v8
        //    51: iload           v7
        //    53: ifgt            66
        //    56: aload           v8
        //    58: invokestatic    javassist/ClassPoolTail.readStream:(Ljava/io/InputStream;)[B
        //    61: astore          a1
        //    63: goto            137
        //    66: iload           v7
        //    68: newarray        B
        //    70: astore          a4
        //    72: iconst_0       
        //    73: istore          a3
        //    75: aload           v8
        //    77: aload           a4
        //    79: iload           a3
        //    81: iload           v7
        //    83: iload           a3
        //    85: isub           
        //    86: invokevirtual   java/io/InputStream.read:([BII)I
        //    89: istore          a2
        //    91: iload           a2
        //    93: ifge            123
        //    96: new             Ljava/io/IOException;
        //    99: dup            
        //   100: new             Ljava/lang/StringBuilder;
        //   103: dup            
        //   104: invokespecial   java/lang/StringBuilder.<init>:()V
        //   107: ldc             "the stream was closed: "
        //   109: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   112: aload_3         /* v4 */
        //   113: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   116: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   119: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   122: athrow         
        //   123: iload           a3
        //   125: iload           a2
        //   127: iadd           
        //   128: istore          a3
        //   130: iload           a3
        //   132: iload           v7
        //   134: if_icmplt       75
        //   137: aload           v8
        //   139: invokevirtual   java/io/InputStream.close:()V
        //   142: goto            155
        //   145: astore          10
        //   147: aload           v8
        //   149: invokevirtual   java/io/InputStream.close:()V
        //   152: aload           10
        //   154: athrow         
        //   155: aload           v5
        //   157: areturn        
        //    Exceptions:
        //  throws java.io.IOException
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  ------------------------
        //  63     3       4     a1    [B
        //  91     39      9     a2    I
        //  75     62      8     a3    I
        //  72     73      4     a4    [B
        //  0      158     0     v1    Ljava/lang/String;
        //  0      158     1     v2    I
        //  0      158     2     v3    Ljava/lang/String;
        //  0      158     3     v4    Ljava/lang/String;
        //  155    3       4     v5    [B
        //  37     121     5     v6    Ljava/net/URLConnection;
        //  44     114     6     v7    I
        //  51     107     7     v8    Ljava/io/InputStream;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  51     137    145    155    Any
        //  145    147    145    155    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3037)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2446)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:109)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at cuchaz.enigma.Deobfuscator.getSourceTree(Deobfuscator.java:224)
        //     at cuchaz.enigma.Deobfuscator.writeSources(Deobfuscator.java:306)
        //     at cuchaz.enigma.gui.GuiController$1.run(GuiController.java:110)
        //     at cuchaz.enigma.gui.ProgressDialog$1.run(ProgressDialog.java:98)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static URLConnection fetchClass0(final String a3, final int v1, final String v2) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/net/URL;
        //     3: dup            
        //     4: ldc             "http"
        //     6: aload_0         /* a3 */
        //     7: iload_1         /* v1 */
        //     8: aload_2         /* v2 */
        //     9: invokespecial   java/net/URL.<init>:(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V
        //    12: astore_3        /* a1 */
        //    13: goto            28
        //    16: astore          a2
        //    18: new             Ljava/io/IOException;
        //    21: dup            
        //    22: ldc             "invalid URL?"
        //    24: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //    27: athrow         
        //    28: aload_3         /* v3 */
        //    29: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    32: astore          v4
        //    34: aload           v4
        //    36: invokevirtual   java/net/URLConnection.connect:()V
        //    39: aload           v4
        //    41: areturn        
        //    Exceptions:
        //  throws java.io.IOException
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  --------------------------------
        //  13     3       3     a1    Ljava/net/URL;
        //  18     10      4     a2    Ljava/net/MalformedURLException;
        //  0      42      0     a3    Ljava/lang/String;
        //  0      42      1     v1    I
        //  0      42      2     v2    Ljava/lang/String;
        //  28     14      3     v3    Ljava/net/URL;
        //  34     8       4     v4    Ljava/net/URLConnection;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  0      13     16     28     Ljava/net/MalformedURLException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2987)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2446)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:109)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at cuchaz.enigma.Deobfuscator.getSourceTree(Deobfuscator.java:224)
        //     at cuchaz.enigma.Deobfuscator.writeSources(Deobfuscator.java:306)
        //     at cuchaz.enigma.gui.GuiController$1.run(GuiController.java:110)
        //     at cuchaz.enigma.gui.ProgressDialog$1.run(ProgressDialog.java:98)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
