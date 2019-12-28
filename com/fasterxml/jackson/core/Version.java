package com.fasterxml.jackson.core;

import java.io.Serializable;

public class Version implements Comparable<Version>, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Version UNKNOWN_VERSION;
    protected final int _majorVersion;
    protected final int _minorVersion;
    protected final int _patchLevel;
    protected final String _groupId;
    protected final String _artifactId;
    protected final String _snapshotInfo;
    
    public Version(final int a1, final int a2, final int a3, final String a4) {
        this(a1, a2, a3, a4, null, null);
    }
    
    public Version(final int a1, final int a2, final int a3, final String a4, final String a5, final String a6) {
        this._majorVersion = a1;
        this._minorVersion = a2;
        this._patchLevel = a3;
        this._snapshotInfo = a4;
        this._groupId = ((a5 == null) ? "" : a5);
        this._artifactId = ((a6 == null) ? "" : a6);
    }
    
    public static Version unknownVersion() {
        /*SL:65*/return Version.UNKNOWN_VERSION;
    }
    
    public boolean isUnknownVersion() {
        /*SL:70*/return this == Version.UNKNOWN_VERSION;
    }
    
    public boolean isSnapshot() {
        /*SL:72*/return this._snapshotInfo != null && this._snapshotInfo.length() > 0;
    }
    
    @Deprecated
    public boolean isUknownVersion() {
        /*SL:78*/return this.isUnknownVersion();
    }
    
    public int getMajorVersion() {
        /*SL:80*/return this._majorVersion;
    }
    
    public int getMinorVersion() {
        /*SL:81*/return this._minorVersion;
    }
    
    public int getPatchLevel() {
        /*SL:82*/return this._patchLevel;
    }
    
    public String getGroupId() {
        /*SL:84*/return this._groupId;
    }
    
    public String getArtifactId() {
        /*SL:85*/return this._artifactId;
    }
    
    public String toFullString() {
        /*SL:88*/return this._groupId + '/' + this._artifactId + '/' + this.toString();
    }
    
    @Override
    public String toString() {
        final StringBuilder v1 = /*EL:92*/new StringBuilder();
        /*SL:93*/v1.append(this._majorVersion).append('.');
        /*SL:94*/v1.append(this._minorVersion).append('.');
        /*SL:95*/v1.append(this._patchLevel);
        /*SL:96*/if (this.isSnapshot()) {
            /*SL:97*/v1.append('-').append(this._snapshotInfo);
        }
        /*SL:99*/return v1.toString();
    }
    
    @Override
    public int hashCode() {
        /*SL:103*/return this._artifactId.hashCode() ^ this._groupId.hashCode() + this._majorVersion - this._minorVersion + this._patchLevel;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:109*/if (a1 == this) {
            return true;
        }
        /*SL:110*/if (a1 == null) {
            return false;
        }
        /*SL:111*/if (a1.getClass() != this.getClass()) {
            return false;
        }
        final Version v1 = /*EL:112*/(Version)a1;
        /*SL:113*/return v1._majorVersion == this._majorVersion && v1._minorVersion == this._minorVersion && v1._patchLevel == this._patchLevel && v1._artifactId.equals(this._artifactId) && v1._groupId.equals(this._groupId);
    }
    
    @Override
    public int compareTo(final Version a1) {
        /*SL:124*/if (a1 == this) {
            return 0;
        }
        int v1 = /*EL:126*/this._groupId.compareTo(a1._groupId);
        /*SL:127*/if (v1 == 0) {
            /*SL:128*/v1 = this._artifactId.compareTo(a1._artifactId);
            /*SL:129*/if (v1 == 0) {
                /*SL:130*/v1 = this._majorVersion - a1._majorVersion;
                /*SL:131*/if (v1 == 0) {
                    /*SL:132*/v1 = this._minorVersion - a1._minorVersion;
                    /*SL:133*/if (v1 == 0) {
                        /*SL:134*/v1 = this._patchLevel - a1._patchLevel;
                    }
                }
            }
        }
        /*SL:139*/return v1;
    }
    
    static {
        UNKNOWN_VERSION = new Version(0, 0, 0, null, null, null);
    }
}
