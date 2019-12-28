package javassist.tools.web;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.util.Date;
import java.io.BufferedInputStream;
import java.net.Socket;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import java.io.IOException;
import javassist.Translator;
import javassist.ClassPool;
import java.net.ServerSocket;

public class Webserver
{
    private ServerSocket socket;
    private ClassPool classPool;
    protected Translator translator;
    private static final byte[] endofline;
    private static final int typeHtml = 1;
    private static final int typeClass = 2;
    private static final int typeGif = 3;
    private static final int typeJpeg = 4;
    private static final int typeText = 5;
    public String debugDir;
    public String htmlfileBase;
    
    public static void main(final String[] v1) throws IOException {
        /*SL:79*/if (v1.length == 1) {
            final Webserver a1 = /*EL:80*/new Webserver(v1[0]);
            /*SL:81*/a1.run();
        }
        else {
            System.err.println(/*EL:84*/"Usage: java javassist.tools.web.Webserver <port number>");
        }
    }
    
    public Webserver(final String a1) throws IOException {
        this(Integer.parseInt(a1));
    }
    
    public Webserver(final int a1) throws IOException {
        this.debugDir = null;
        this.htmlfileBase = null;
        this.socket = new ServerSocket(a1);
        this.classPool = null;
        this.translator = /*EL:86*/null;
    }
    
    public void setClassPool(final ClassPool a1) {
        /*SL:113*/this.classPool = a1;
    }
    
    public void addTranslator(final ClassPool a1, final Translator a2) throws NotFoundException, CannotCompileException {
        /*SL:127*/this.classPool = a1;
        /*SL:128*/(this.translator = a2).start(/*EL:129*/this.classPool);
    }
    
    public void end() throws IOException {
        /*SL:136*/this.socket.close();
    }
    
    public void logging(final String a1) {
        System.out.println(/*EL:143*/a1);
    }
    
    public void logging(final String a1, final String a2) {
        System.out.print(/*EL:150*/a1);
        System.out.print(/*EL:151*/" ");
        System.out.println(/*EL:152*/a2);
    }
    
    public void logging(final String a1, final String a2, final String a3) {
        System.out.print(/*EL:159*/a1);
        System.out.print(/*EL:160*/" ");
        System.out.print(/*EL:161*/a2);
        System.out.print(/*EL:162*/" ");
        System.out.println(/*EL:163*/a3);
    }
    
    public void logging2(final String a1) {
        System.out.print(/*EL:170*/"    ");
        System.out.println(/*EL:171*/a1);
    }
    
    public void run() {
        System.err.println(/*EL:178*/"ready to service...");
    Label_0008_Outer:
        while (true) {
            while (true) {
                try {
                    while (true) {
                        final ServiceThread v1 = /*EL:181*/new ServiceThread(this, this.socket.accept());
                        /*SL:182*/v1.start();
                    }
                }
                catch (IOException v2) {
                    /*SL:185*/this.logging(v2.toString());
                    /*SL:186*/continue Label_0008_Outer;
                }
                continue;
            }
        }
    }
    
    final void process(final Socket v2) throws IOException {
        final InputStream v3 = /*EL:190*/new BufferedInputStream(v2.getInputStream());
        final String v4 = /*EL:191*/this.readLine(v3);
        /*SL:192*/this.logging(v2.getInetAddress().getHostName(), new Date().toString(), /*EL:193*/v4);
        /*SL:194*/while (this.skipLine(v3) > 0) {}
        final OutputStream v5 = /*EL:197*/new BufferedOutputStream(v2.getOutputStream());
        try {
            /*SL:199*/this.doReply(v3, v5, v4);
        }
        catch (BadHttpRequest a1) {
            /*SL:202*/this.replyError(v5, a1);
        }
        /*SL:205*/v5.flush();
        /*SL:206*/v3.close();
        /*SL:207*/v5.close();
        /*SL:208*/v2.close();
    }
    
    private String readLine(final InputStream a1) throws IOException {
        final StringBuffer v1 = /*EL:212*/new StringBuffer();
        int v2;
        /*SL:214*/while ((v2 = a1.read()) >= 0 && v2 != 13) {
            /*SL:215*/v1.append((char)v2);
        }
        /*SL:217*/a1.read();
        /*SL:218*/return v1.toString();
    }
    
    private int skipLine(final InputStream a1) throws IOException {
        int v2 = /*EL:223*/0;
        /*SL:224*/while ((v2 = a1.read()) >= 0 && v2 != 13) {
            /*SL:225*/++v2;
        }
        /*SL:227*/a1.read();
        /*SL:228*/return v2;
    }
    
    public void doReply(final InputStream v-3, final OutputStream v-2, final String v-1) throws IOException, BadHttpRequest {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_3         /* v-1 */
        //     1: ldc             "GET /"
        //     3: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //     6: ifeq            29
        //     9: aload_3         /* v-1 */
        //    10: iconst_5       
        //    11: aload_3         /* v-1 */
        //    12: bipush          32
        //    14: iconst_5       
        //    15: invokevirtual   java/lang/String.indexOf:(II)I
        //    18: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    21: dup            
        //    22: astore          a2
        //    24: astore          a1
        //    26: goto            37
        //    29: new             Ljavassist/tools/web/BadHttpRequest;
        //    32: dup            
        //    33: invokespecial   javassist/tools/web/BadHttpRequest.<init>:()V
        //    36: athrow         
        //    37: aload           v2
        //    39: ldc             ".class"
        //    41: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    44: ifeq            53
        //    47: iconst_2       
        //    48: istore          a3
        //    50: goto            117
        //    53: aload           v2
        //    55: ldc             ".html"
        //    57: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    60: ifne            74
        //    63: aload           v2
        //    65: ldc_w           ".htm"
        //    68: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    71: ifeq            80
        //    74: iconst_1       
        //    75: istore          v1
        //    77: goto            117
        //    80: aload           v2
        //    82: ldc_w           ".gif"
        //    85: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //    88: ifeq            97
        //    91: iconst_3       
        //    92: istore          v1
        //    94: goto            117
        //    97: aload           v2
        //    99: ldc_w           ".jpg"
        //   102: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //   105: ifeq            114
        //   108: iconst_4       
        //   109: istore          v1
        //   111: goto            117
        //   114: iconst_5       
        //   115: istore          v1
        //   117: aload           v2
        //   119: invokevirtual   java/lang/String.length:()I
        //   122: istore          v0
        //   124: iload           v1
        //   126: iconst_2       
        //   127: if_icmpne       143
        //   130: aload_0         /* v-4 */
        //   131: aload_2         /* v-2 */
        //   132: aload           v2
        //   134: iload           v0
        //   136: invokespecial   javassist/tools/web/Webserver.letUsersSendClassfile:(Ljava/io/OutputStream;Ljava/lang/String;I)Z
        //   139: ifeq            143
        //   142: return         
        //   143: aload_0         /* v-4 */
        //   144: aload           v2
        //   146: iload           v0
        //   148: invokespecial   javassist/tools/web/Webserver.checkFilename:(Ljava/lang/String;I)V
        //   151: aload_0         /* v-4 */
        //   152: getfield        javassist/tools/web/Webserver.htmlfileBase:Ljava/lang/String;
        //   155: ifnull          182
        //   158: new             Ljava/lang/StringBuilder;
        //   161: dup            
        //   162: invokespecial   java/lang/StringBuilder.<init>:()V
        //   165: aload_0         /* v-4 */
        //   166: getfield        javassist/tools/web/Webserver.htmlfileBase:Ljava/lang/String;
        //   169: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   172: aload           v2
        //   174: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   177: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   180: astore          v2
        //   182: getstatic       java/io/File.separatorChar:C
        //   185: bipush          47
        //   187: if_icmpeq       202
        //   190: aload           v2
        //   192: bipush          47
        //   194: getstatic       java/io/File.separatorChar:C
        //   197: invokevirtual   java/lang/String.replace:(CC)Ljava/lang/String;
        //   200: astore          v2
        //   202: new             Ljava/io/File;
        //   205: dup            
        //   206: aload           v2
        //   208: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   211: astore          v4
        //   213: aload           v4
        //   215: invokevirtual   java/io/File.canRead:()Z
        //   218: ifeq            286
        //   221: aload_0         /* v-4 */
        //   222: aload_2         /* v-2 */
        //   223: aload           v4
        //   225: invokevirtual   java/io/File.length:()J
        //   228: iload           v1
        //   230: invokespecial   javassist/tools/web/Webserver.sendHeader:(Ljava/io/OutputStream;JI)V
        //   233: new             Ljava/io/FileInputStream;
        //   236: dup            
        //   237: aload           v4
        //   239: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   242: astore          v5
        //   244: sipush          4096
        //   247: newarray        B
        //   249: astore          v6
        //   251: aload           v5
        //   253: aload           v6
        //   255: invokevirtual   java/io/FileInputStream.read:([B)I
        //   258: istore          v0
        //   260: iload           v0
        //   262: ifgt            268
        //   265: goto            280
        //   268: aload_2         /* v-2 */
        //   269: aload           v6
        //   271: iconst_0       
        //   272: iload           v0
        //   274: invokevirtual   java/io/OutputStream.write:([BII)V
        //   277: goto            251
        //   280: aload           v5
        //   282: invokevirtual   java/io/FileInputStream.close:()V
        //   285: return         
        //   286: iload           v1
        //   288: iconst_2       
        //   289: if_icmpne       402
        //   292: aload_0         /* v-4 */
        //   293: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   296: new             Ljava/lang/StringBuilder;
        //   299: dup            
        //   300: invokespecial   java/lang/StringBuilder.<init>:()V
        //   303: ldc_w           "/"
        //   306: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   309: aload           v3
        //   311: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   314: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   317: invokevirtual   java/lang/Class.getResourceAsStream:(Ljava/lang/String;)Ljava/io/InputStream;
        //   320: astore          v5
        //   322: aload           v5
        //   324: ifnull          402
        //   327: new             Ljava/io/ByteArrayOutputStream;
        //   330: dup            
        //   331: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //   334: astore          v6
        //   336: sipush          4096
        //   339: newarray        B
        //   341: astore          v7
        //   343: aload           v5
        //   345: aload           v7
        //   347: invokevirtual   java/io/InputStream.read:([B)I
        //   350: istore          v0
        //   352: iload           v0
        //   354: ifgt            360
        //   357: goto            373
        //   360: aload           v6
        //   362: aload           v7
        //   364: iconst_0       
        //   365: iload           v0
        //   367: invokevirtual   java/io/ByteArrayOutputStream.write:([BII)V
        //   370: goto            343
        //   373: aload           v6
        //   375: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //   378: astore          v8
        //   380: aload_0         /* v-4 */
        //   381: aload_2         /* v-2 */
        //   382: aload           v8
        //   384: arraylength    
        //   385: i2l            
        //   386: iconst_2       
        //   387: invokespecial   javassist/tools/web/Webserver.sendHeader:(Ljava/io/OutputStream;JI)V
        //   390: aload_2         /* v-2 */
        //   391: aload           v8
        //   393: invokevirtual   java/io/OutputStream.write:([B)V
        //   396: aload           v5
        //   398: invokevirtual   java/io/InputStream.close:()V
        //   401: return         
        //   402: new             Ljavassist/tools/web/BadHttpRequest;
        //   405: dup            
        //   406: invokespecial   javassist/tools/web/BadHttpRequest.<init>:()V
        //   409: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //  throws javassist.tools.web.BadHttpRequest
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -------------------------------
        //  26     3       6     a1    Ljava/lang/String;
        //  24     5       7     a2    Ljava/lang/String;
        //  50     3       5     a3    I
        //  77     3       5     v1    I
        //  94     3       5     v1    I
        //  111    3       5     v1    I
        //  244    42      9     v5    Ljava/io/FileInputStream;
        //  251    35      10    v6    [B
        //  336    66      10    v6    Ljava/io/ByteArrayOutputStream;
        //  343    59      11    v7    [B
        //  380    22      12    v8    [B
        //  322    80      9     v5    Ljava/io/InputStream;
        //  0      410     0     v-4   Ljavassist/tools/web/Webserver;
        //  0      410     1     v-3   Ljava/io/InputStream;
        //  0      410     2     v-2   Ljava/io/OutputStream;
        //  0      410     3     v-1   Ljava/lang/String;
        //  124    286     4     v0    I
        //  117    293     5     v1    I
        //  37     373     6     v2    Ljava/lang/String;
        //  37     373     7     v3    Ljava/lang/String;
        //  213    197     8     v4    Ljava/io/File;
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
    
    private void checkFilename(final String v2, final int v3) throws BadHttpRequest {
        /*SL:320*/for (char a2 = 0; a2 < v3; ++a2) {
            /*SL:321*/a2 = v2.charAt(a2);
            /*SL:322*/if (!Character.isJavaIdentifierPart(a2) && a2 != '.' && a2 != '/') {
                /*SL:323*/throw new BadHttpRequest();
            }
        }
        /*SL:326*/if (v2.indexOf("..") >= 0) {
            /*SL:327*/throw new BadHttpRequest();
        }
    }
    
    private boolean letUsersSendClassfile(final OutputStream v2, final String v3, final int v4) throws IOException, BadHttpRequest {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0         /* v1 */
        //     1: getfield        javassist/tools/web/Webserver.classPool:Ljavassist/ClassPool;
        //     4: ifnonnull       9
        //     7: iconst_0       
        //     8: ireturn        
        //     9: aload_2         /* v3 */
        //    10: iconst_0       
        //    11: iload_3         /* v4 */
        //    12: bipush          6
        //    14: isub           
        //    15: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //    18: bipush          47
        //    20: bipush          46
        //    22: invokevirtual   java/lang/String.replace:(CC)Ljava/lang/String;
        //    25: astore          v6
        //    27: aload_0         /* v1 */
        //    28: getfield        javassist/tools/web/Webserver.translator:Ljavassist/Translator;
        //    31: ifnull          49
        //    34: aload_0         /* v1 */
        //    35: getfield        javassist/tools/web/Webserver.translator:Ljavassist/Translator;
        //    38: aload_0         /* v1 */
        //    39: getfield        javassist/tools/web/Webserver.classPool:Ljavassist/ClassPool;
        //    42: aload           v6
        //    44: invokeinterface javassist/Translator.onLoad:(Ljavassist/ClassPool;Ljava/lang/String;)V
        //    49: aload_0         /* v1 */
        //    50: getfield        javassist/tools/web/Webserver.classPool:Ljavassist/ClassPool;
        //    53: aload           v6
        //    55: invokevirtual   javassist/ClassPool.get:(Ljava/lang/String;)Ljavassist/CtClass;
        //    58: astore          a1
        //    60: aload           a1
        //    62: invokevirtual   javassist/CtClass.toBytecode:()[B
        //    65: astore          a2
        //    67: aload_0         /* v1 */
        //    68: getfield        javassist/tools/web/Webserver.debugDir:Ljava/lang/String;
        //    71: ifnull          83
        //    74: aload           a1
        //    76: aload_0         /* v1 */
        //    77: getfield        javassist/tools/web/Webserver.debugDir:Ljava/lang/String;
        //    80: invokevirtual   javassist/CtClass.writeFile:(Ljava/lang/String;)V
        //    83: goto            98
        //    86: astore          a3
        //    88: new             Ljavassist/tools/web/BadHttpRequest;
        //    91: dup            
        //    92: aload           a3
        //    94: invokespecial   javassist/tools/web/BadHttpRequest.<init>:(Ljava/lang/Exception;)V
        //    97: athrow         
        //    98: aload_0         /* v1 */
        //    99: aload_1         /* v2 */
        //   100: aload           v5
        //   102: arraylength    
        //   103: i2l            
        //   104: iconst_2       
        //   105: invokespecial   javassist/tools/web/Webserver.sendHeader:(Ljava/io/OutputStream;JI)V
        //   108: aload_1         /* v2 */
        //   109: aload           v5
        //   111: invokevirtual   java/io/OutputStream.write:([B)V
        //   114: iconst_1       
        //   115: ireturn        
        //    Exceptions:
        //  throws java.io.IOException
        //  throws javassist.tools.web.BadHttpRequest
        //    LocalVariableTable:
        //  Start  Length  Slot  Name  Signature
        //  -----  ------  ----  ----  -------------------------------
        //  60     23      6     a1    Ljavassist/CtClass;
        //  67     19      4     a2    [B
        //  88     10      6     a3    Ljava/lang/Exception;
        //  0      116     0     v1    Ljavassist/tools/web/Webserver;
        //  0      116     1     v2    Ljava/io/OutputStream;
        //  0      116     2     v3    Ljava/lang/String;
        //  0      116     3     v4    I
        //  98     18      4     v5    [B
        //  27     89      5     v6    Ljava/lang/String;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  27     83     86     98     Ljava/lang/Exception;
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
    
    private void sendHeader(final OutputStream a1, final long a2, final int a3) throws IOException {
        /*SL:361*/a1.write("HTTP/1.0 200 OK".getBytes());
        /*SL:362*/a1.write(Webserver.endofline);
        /*SL:363*/a1.write("Content-Length: ".getBytes());
        /*SL:364*/a1.write(Long.toString(a2).getBytes());
        /*SL:365*/a1.write(Webserver.endofline);
        /*SL:366*/if (a3 == 2) {
            /*SL:367*/a1.write("Content-Type: application/octet-stream".getBytes());
        }
        else/*SL:368*/ if (a3 == 1) {
            /*SL:369*/a1.write("Content-Type: text/html".getBytes());
        }
        else/*SL:370*/ if (a3 == 3) {
            /*SL:371*/a1.write("Content-Type: image/gif".getBytes());
        }
        else/*SL:372*/ if (a3 == 4) {
            /*SL:373*/a1.write("Content-Type: image/jpg".getBytes());
        }
        else/*SL:374*/ if (a3 == 5) {
            /*SL:375*/a1.write("Content-Type: text/plain".getBytes());
        }
        /*SL:377*/a1.write(Webserver.endofline);
        /*SL:378*/a1.write(Webserver.endofline);
    }
    
    private void replyError(final OutputStream a1, final BadHttpRequest a2) throws IOException {
        /*SL:384*/this.logging2("bad request: " + a2.toString());
        /*SL:385*/a1.write("HTTP/1.0 400 Bad Request".getBytes());
        /*SL:386*/a1.write(Webserver.endofline);
        /*SL:387*/a1.write(Webserver.endofline);
        /*SL:388*/a1.write("<H1>Bad Request</H1>".getBytes());
    }
    
    static {
        endofline = new byte[] { 13, 10 };
    }
}
