package com.sun.jna;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.io.RandomAccessFile;
import java.io.IOException;

class ELFAnalyser
{
    private static final byte[] ELF_MAGIC;
    private static final int EF_ARM_ABI_FLOAT_HARD = 1024;
    private static final int EF_ARM_ABI_FLOAT_SOFT = 512;
    private static final int EI_DATA_BIG_ENDIAN = 2;
    private static final int E_MACHINE_ARM = 40;
    private static final int EI_CLASS_64BIT = 2;
    private final String filename;
    private boolean ELF;
    private boolean _64Bit;
    private boolean bigEndian;
    private boolean armHardFloat;
    private boolean armSoftFloat;
    private boolean arm;
    
    public static ELFAnalyser analyse(final String a1) throws IOException {
        final ELFAnalyser v1 = /*EL:35*/new ELFAnalyser(a1);
        /*SL:36*/v1.runDetection();
        /*SL:37*/return v1;
    }
    
    public boolean isELF() {
        /*SL:52*/return this.ELF;
    }
    
    public boolean is64Bit() {
        /*SL:60*/return this._64Bit;
    }
    
    public boolean isBigEndian() {
        /*SL:68*/return this.bigEndian;
    }
    
    public String getFilename() {
        /*SL:75*/return this.filename;
    }
    
    public boolean isArmHardFloat() {
        /*SL:83*/return this.armHardFloat;
    }
    
    public boolean isArmSoftFloat() {
        /*SL:91*/return this.armSoftFloat;
    }
    
    public boolean isArm() {
        /*SL:99*/return this.arm;
    }
    
    private ELFAnalyser(final String a1) {
        this.ELF = false;
        this._64Bit = false;
        this.bigEndian = false;
        this.armHardFloat = false;
        this.armSoftFloat = false;
        this.arm = false;
        this.filename = a1;
    }
    
    private void runDetection() throws IOException {
        final RandomAccessFile v0 = /*EL:109*/new RandomAccessFile(this.filename, "r");
        /*SL:110*/if (v0.length() > 4L) {
            final byte[] v = /*EL:111*/new byte[4];
            /*SL:112*/v0.seek(0L);
            /*SL:113*/v0.read(v);
            /*SL:114*/if (Arrays.equals(v, ELFAnalyser.ELF_MAGIC)) {
                /*SL:115*/this.ELF = true;
            }
        }
        /*SL:118*/if (!this.ELF) {
            /*SL:119*/return;
        }
        /*SL:121*/v0.seek(4L);
        final byte v2 = /*EL:124*/v0.readByte();
        /*SL:125*/this._64Bit = (v2 == 2);
        /*SL:126*/v0.seek(0L);
        final ByteBuffer v3 = /*EL:127*/ByteBuffer.allocate(this._64Bit ? 64 : 52);
        /*SL:128*/v0.getChannel().read(v3, 0L);
        /*SL:129*/this.bigEndian = (v3.get(5) == 2);
        /*SL:130*/v3.order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        /*SL:132*/this.arm = (v3.get(18) == 40);
        /*SL:134*/if (this.arm) {
            final int v4 = /*EL:135*/v3.getInt(this._64Bit ? 48 : 36);
            /*SL:136*/this.armSoftFloat = ((v4 & 0x200) == 0x200);
            /*SL:137*/this.armHardFloat = ((v4 & 0x400) == 0x400);
        }
    }
    
    static {
        ELF_MAGIC = new byte[] { 127, 69, 76, 70 };
    }
}
