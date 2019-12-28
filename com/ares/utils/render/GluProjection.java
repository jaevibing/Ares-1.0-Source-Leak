package com.ares.utils.render;

import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;

public final class GluProjection
{
    private static GluProjection instance;
    private final FloatBuffer coords;
    private IntBuffer viewport;
    private FloatBuffer modelview;
    private FloatBuffer projection;
    private Vector3D frustumPos;
    private Vector3D[] frustum;
    private Vector3D[] invFrustum;
    private Vector3D viewVec;
    private double displayWidth;
    private double displayHeight;
    private double widthScale;
    private double heightScale;
    private double bra;
    private double bla;
    private double tra;
    private double tla;
    private Line tb;
    private Line bb;
    private Line lb;
    private Line rb;
    private float fovY;
    private float fovX;
    private Vector3D lookVec;
    
    private GluProjection() {
        this.coords = BufferUtils.createFloatBuffer(3);
    }
    
    public static GluProjection getInstance() {
        /*SL:56*/if (GluProjection.instance == null) {
            GluProjection.instance = /*EL:57*/new GluProjection();
        }
        /*SL:59*/return GluProjection.instance;
    }
    
    public void updateMatrices(final IntBuffer a1, final FloatBuffer a2, final FloatBuffer a3, final double a4, final double a5) {
        /*SL:63*/this.viewport = a1;
        /*SL:64*/this.modelview = a2;
        /*SL:65*/this.projection = a3;
        /*SL:66*/this.widthScale = a4;
        /*SL:67*/this.heightScale = a5;
        final float v1 = /*EL:70*/(float)Math.toDegrees(Math.atan(1.0 / this.projection.get(5)) * 2.0);
        /*SL:71*/this.fovY = v1;
        /*SL:72*/this.displayWidth = this.viewport.get(2);
        /*SL:73*/this.displayHeight = this.viewport.get(3);
        /*SL:74*/this.fovX = (float)Math.toDegrees(2.0 * Math.atan(this.displayWidth / this.displayHeight * Math.tan(Math.toRadians(this.fovY) / 2.0)));
        final Vector3D v2 = /*EL:76*/new Vector3D(this.modelview.get(12), this.modelview.get(13), this.modelview.get(14));
        final Vector3D v3 = /*EL:77*/new Vector3D(this.modelview.get(0), this.modelview.get(1), this.modelview.get(2));
        final Vector3D v4 = /*EL:78*/new Vector3D(this.modelview.get(4), this.modelview.get(5), this.modelview.get(6));
        final Vector3D v5 = /*EL:79*/new Vector3D(this.modelview.get(8), this.modelview.get(9), this.modelview.get(10));
        final Vector3D v6 = /*EL:81*/new Vector3D(0.0, 1.0, 0.0);
        final Vector3D v7 = /*EL:82*/new Vector3D(1.0, 0.0, 0.0);
        final Vector3D v8 = /*EL:83*/new Vector3D(0.0, 0.0, 1.0);
        double v9 = /*EL:85*/Math.toDegrees(Math.atan2(v7.cross(v3).length(), v7.dot(v3))) + 180.0;
        /*SL:86*/if (v5.x < 0.0) {
            /*SL:87*/v9 = 360.0 - v9;
        }
        double v10 = /*EL:89*/0.0;
        /*SL:90*/if ((-v5.y > 0.0 && v9 >= 90.0 && v9 < 270.0) || (v5.y > 0.0 && (v9 < 90.0 || v9 >= 270.0))) {
            /*SL:91*/v10 = Math.toDegrees(Math.atan2(v6.cross(v4).length(), v6.dot(v4)));
        }
        else {
            /*SL:93*/v10 = -Math.toDegrees(Math.atan2(v6.cross(v4).length(), v6.dot(v4)));
        }
        /*SL:95*/this.lookVec = this.getRotationVector(v9, v10);
        final Matrix4f v11 = /*EL:97*/new Matrix4f();
        /*SL:98*/v11.load(this.modelview.asReadOnlyBuffer());
        /*SL:99*/v11.invert();
        /*SL:101*/this.frustumPos = new Vector3D(v11.m30, v11.m31, v11.m32);
        /*SL:102*/this.frustum = this.getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, v9, v10, v1, 1.0, this.displayWidth / this.displayHeight);
        /*SL:103*/this.invFrustum = this.getFrustum(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z, v9 - 180.0, -v10, v1, 1.0, this.displayWidth / this.displayHeight);
        /*SL:105*/this.viewVec = this.getRotationVector(v9, v10).normalized();
        /*SL:107*/this.bra = Math.toDegrees(Math.acos(this.displayHeight * a5 / Math.sqrt(this.displayWidth * a4 * this.displayWidth * a4 + this.displayHeight * a5 * this.displayHeight * a5)));
        /*SL:108*/this.bla = 360.0 - this.bra;
        /*SL:109*/this.tra = this.bla - 180.0;
        /*SL:110*/this.tla = this.bra + 180.0;
        /*SL:112*/this.rb = new Line(this.displayWidth * this.widthScale, 0.0, 0.0, 0.0, 1.0, 0.0);
        /*SL:113*/this.tb = new Line(0.0, 0.0, 0.0, 1.0, 0.0, 0.0);
        /*SL:114*/this.lb = new Line(0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        /*SL:115*/this.bb = new Line(0.0, this.displayHeight * this.heightScale, 0.0, 1.0, 0.0, 0.0);
    }
    
    public Projection project(final double v-21, final double v-19, final double v-17, final ClampMode v-15, final boolean v-14) {
        /*SL:119*/if (this.viewport != null && this.modelview != null && this.projection != null) {
            final Vector3D vector3D = /*EL:120*/new Vector3D(v-21, v-19, v-17);
            final boolean[] doFrustumCheck = /*EL:121*/this.doFrustumCheck(this.frustum, this.frustumPos, v-21, v-19, v-17);
            final boolean b = /*EL:122*/doFrustumCheck[0] || doFrustumCheck[1] || doFrustumCheck[2] || doFrustumCheck[3];
            /*SL:124*/if (b) {
                final boolean b2 = /*EL:126*/vector3D.sub(this.frustumPos).dot(this.viewVec) <= 0.0;
                final boolean[] doFrustumCheck2 = /*EL:128*/this.doFrustumCheck(this.invFrustum, this.frustumPos, v-21, v-19, v-17);
                final boolean b3 = /*EL:129*/doFrustumCheck2[0] || doFrustumCheck2[1] || doFrustumCheck2[2] || doFrustumCheck2[3];
                /*SL:130*/if ((v-14 && !b3) || (b3 && v-15 != ClampMode.NONE)) {
                    /*SL:131*/if ((v-14 && !b3) || (v-15 == ClampMode.DIRECT && b3)) {
                        double a1 = /*EL:134*/0.0;
                        double a2 = /*EL:135*/0.0;
                        /*SL:136*/if (GLU.gluProject((float)v-21, (float)v-19, (float)v-17, this.modelview, this.projection, this.viewport, this.coords)) {
                            /*SL:138*/if (b2) {
                                /*SL:140*/a1 = this.displayWidth * this.widthScale - this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0;
                                /*SL:141*/a2 = this.displayHeight * this.heightScale - (this.displayHeight - this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0;
                            }
                            else {
                                /*SL:143*/a1 = this.coords.get(0) * this.widthScale - this.displayWidth * this.widthScale / 2.0;
                                /*SL:144*/a2 = (this.displayHeight - this.coords.get(1)) * this.heightScale - this.displayHeight * this.heightScale / 2.0;
                            }
                            final Vector3D a3 = /*EL:150*/new Vector3D(a1, a2, 0.0).snormalize();
                            /*SL:151*/a1 = a3.x;
                            /*SL:152*/a2 = a3.y;
                            final Line a4 = /*EL:154*/new Line(this.displayWidth * this.widthScale / 2.0, this.displayHeight * this.heightScale / 2.0, 0.0, a1, a2, 0.0);
                            double a5 = /*EL:156*/Math.toDegrees(Math.acos(a3.y / Math.sqrt(a3.x * a3.x + a3.y * a3.y)));
                            /*SL:157*/if (a1 < 0.0) {
                                /*SL:158*/a5 = 360.0 - a5;
                            }
                            Vector3D v1 = /*EL:161*/new Vector3D(0.0, 0.0, 0.0);
                            /*SL:163*/if (a5 >= this.bra && a5 < this.tra) {
                                /*SL:165*/v1 = this.rb.intersect(a4);
                            }
                            else/*SL:166*/ if (a5 >= this.tra && a5 < this.tla) {
                                /*SL:168*/v1 = this.tb.intersect(a4);
                            }
                            else/*SL:169*/ if (a5 >= this.tla && a5 < this.bla) {
                                /*SL:171*/v1 = this.lb.intersect(a4);
                            }
                            else {
                                /*SL:174*/v1 = this.bb.intersect(a4);
                            }
                            /*SL:176*/return new Projection(v1.x, v1.y, b3 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                        }
                        return new Projection(0.0, 0.0, Projection.Type.FAIL);
                    }
                    else/*SL:177*/ if (v-15 == ClampMode.ORTHOGONAL && b3) {
                        /*SL:178*/if (GLU.gluProject((float)v-21, (float)v-19, (float)v-17, this.modelview, this.projection, this.viewport, this.coords)) {
                            double n = /*EL:180*/this.coords.get(0) * this.widthScale;
                            double n2 = /*EL:181*/(this.displayHeight - this.coords.get(1)) * this.heightScale;
                            /*SL:182*/if (b2) {
                                /*SL:184*/n = this.displayWidth * this.widthScale - n;
                                /*SL:185*/n2 = this.displayHeight * this.heightScale - n2;
                            }
                            /*SL:187*/if (n < 0.0) {
                                /*SL:188*/n = 0.0;
                            }
                            else/*SL:189*/ if (n > this.displayWidth * this.widthScale) {
                                /*SL:190*/n = this.displayWidth * this.widthScale;
                            }
                            /*SL:192*/if (n2 < 0.0) {
                                /*SL:193*/n2 = 0.0;
                            }
                            else/*SL:194*/ if (n2 > this.displayHeight * this.heightScale) {
                                /*SL:195*/n2 = this.displayHeight * this.heightScale;
                            }
                            /*SL:197*/return new Projection(n, n2, b3 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                        }
                        /*SL:199*/return new Projection(0.0, 0.0, Projection.Type.FAIL);
                    }
                }
                else {
                    /*SL:204*/if (GLU.gluProject((float)v-21, (float)v-19, (float)v-17, this.modelview, this.projection, this.viewport, this.coords)) {
                        double n = /*EL:206*/this.coords.get(0) * this.widthScale;
                        double n2 = /*EL:207*/(this.displayHeight - this.coords.get(1)) * this.heightScale;
                        /*SL:208*/if (b2) {
                            /*SL:210*/n = this.displayWidth * this.widthScale - n;
                            /*SL:211*/n2 = this.displayHeight * this.heightScale - n2;
                        }
                        /*SL:213*/return new Projection(n, n2, b3 ? Projection.Type.OUTSIDE : Projection.Type.INVERTED);
                    }
                    /*SL:215*/return new Projection(0.0, 0.0, Projection.Type.FAIL);
                }
            }
            else {
                /*SL:220*/if (GLU.gluProject((float)v-21, (float)v-19, (float)v-17, this.modelview, this.projection, this.viewport, this.coords)) {
                    final double a6 = /*EL:222*/this.coords.get(0) * this.widthScale;
                    final double a7 = /*EL:223*/(this.displayHeight - this.coords.get(1)) * this.heightScale;
                    /*SL:224*/return new Projection(a6, a7, Projection.Type.INSIDE);
                }
                /*SL:226*/return new Projection(0.0, 0.0, Projection.Type.FAIL);
            }
        }
        /*SL:230*/return new Projection(0.0, 0.0, Projection.Type.FAIL);
    }
    
    public boolean[] doFrustumCheck(final Vector3D[] a1, final Vector3D a2, final double a3, final double a4, final double a5) {
        final Vector3D v1 = /*EL:234*/new Vector3D(a3, a4, a5);
        final boolean v2 = /*EL:235*/this.crossPlane(new Vector3D[] { a2, a1[3], a1[0] }, v1);
        final boolean v3 = /*EL:236*/this.crossPlane(new Vector3D[] { a2, a1[0], a1[1] }, v1);
        final boolean v4 = /*EL:237*/this.crossPlane(new Vector3D[] { a2, a1[1], a1[2] }, v1);
        final boolean v5 = /*EL:238*/this.crossPlane(new Vector3D[] { a2, a1[2], a1[3] }, v1);
        /*SL:239*/return new boolean[] { v2, v3, v4, v5 };
    }
    
    public boolean crossPlane(final Vector3D[] a1, final Vector3D a2) {
        final Vector3D v1 = /*EL:243*/new Vector3D(0.0, 0.0, 0.0);
        final Vector3D v2 = /*EL:244*/a1[1].sub(a1[0]);
        final Vector3D v3 = /*EL:245*/a1[2].sub(a1[0]);
        final Vector3D v4 = /*EL:246*/v2.cross(v3).snormalize();
        final double v5 = /*EL:247*/v1.sub(v4).dot(a1[2]);
        final double v6 = /*EL:248*/v4.dot(a2) + v5;
        /*SL:249*/return v6 >= 0.0;
    }
    
    public Vector3D[] getFrustum(final double a1, final double a2, final double a3, final double a4, final double a5, final double a6, final double a7, final double a8) {
        final Vector3D v1 = /*EL:253*/this.getRotationVector(a4, a5).snormalize();
        final double v2 = /*EL:254*/2.0 * Math.tan(Math.toRadians(a6 / 2.0)) * a7;
        final double v3 = /*EL:255*/v2 * a8;
        final Vector3D v4 = /*EL:256*/this.getRotationVector(a4, a5).snormalize();
        final Vector3D v5 = /*EL:257*/this.getRotationVector(a4, a5 - 90.0).snormalize();
        final Vector3D v6 = /*EL:258*/this.getRotationVector(a4 + 90.0, 0.0).snormalize();
        final Vector3D v7 = /*EL:259*/new Vector3D(a1, a2, a3);
        final Vector3D v8 = /*EL:260*/v4.add(v7);
        final Vector3D v9 = /*EL:261*/new Vector3D(v8.x * a7, v8.y * a7, v8.z * a7);
        final Vector3D v10 = /*EL:262*/new Vector3D(v9.x + v5.x * v2 / 2.0 - v6.x * v3 / 2.0, v9.y + v5.y * v2 / 2.0 - v6.y * v3 / 2.0, v9.z + v5.z * v2 / 2.0 - v6.z * v3 / 2.0);
        final Vector3D v11 = /*EL:263*/new Vector3D(v9.x - v5.x * v2 / 2.0 - v6.x * v3 / 2.0, v9.y - v5.y * v2 / 2.0 - v6.y * v3 / 2.0, v9.z - v5.z * v2 / 2.0 - v6.z * v3 / 2.0);
        final Vector3D v12 = /*EL:264*/new Vector3D(v9.x + v5.x * v2 / 2.0 + v6.x * v3 / 2.0, v9.y + v5.y * v2 / 2.0 + v6.y * v3 / 2.0, v9.z + v5.z * v2 / 2.0 + v6.z * v3 / 2.0);
        final Vector3D v13 = /*EL:265*/new Vector3D(v9.x - v5.x * v2 / 2.0 + v6.x * v3 / 2.0, v9.y - v5.y * v2 / 2.0 + v6.y * v3 / 2.0, v9.z - v5.z * v2 / 2.0 + v6.z * v3 / 2.0);
        /*SL:266*/return new Vector3D[] { v10, v11, v13, v12 };
    }
    
    public Vector3D[] getFrustum() {
        /*SL:270*/return this.frustum;
    }
    
    public float getFovX() {
        /*SL:274*/return this.fovX;
    }
    
    public float getFovY() {
        /*SL:278*/return this.fovY;
    }
    
    public Vector3D getLookVector() {
        /*SL:282*/return this.lookVec;
    }
    
    public Vector3D getRotationVector(final double a1, final double a2) {
        final double v1 = /*EL:286*/Math.cos(-a1 * 0.01745329238474369 - 3.141592653589793);
        final double v2 = /*EL:287*/Math.sin(-a1 * 0.01745329238474369 - 3.141592653589793);
        final double v3 = /*EL:288*/-Math.cos(-a2 * 0.01745329238474369);
        final double v4 = /*EL:289*/Math.sin(-a2 * 0.01745329238474369);
        /*SL:290*/return new Vector3D(v2 * v3, v4, v1 * v3);
    }
    
    public enum ClampMode
    {
        ORTHOGONAL, 
        DIRECT, 
        NONE;
    }
    
    public static class Line
    {
        public Vector3D sourcePoint;
        public Vector3D direction;
        
        public Line(final double a1, final double a2, final double a3, final double a4, final double a5, final double a6) {
            this.sourcePoint = new Vector3D(0.0, 0.0, 0.0);
            this.direction = new Vector3D(0.0, 0.0, 0.0);
            this.sourcePoint.x = a1;
            this.sourcePoint.y = a2;
            this.sourcePoint.z = a3;
            this.direction.x = a4;
            this.direction.y = a5;
            this.direction.z = a6;
        }
        
        public Vector3D intersect(final Line a1) {
            final double v1 = /*EL:313*/this.sourcePoint.x;
            final double v2 = /*EL:314*/this.direction.x;
            final double v3 = /*EL:315*/a1.sourcePoint.x;
            final double v4 = /*EL:316*/a1.direction.x;
            final double v5 = /*EL:317*/this.sourcePoint.y;
            final double v6 = /*EL:318*/this.direction.y;
            final double v7 = /*EL:319*/a1.sourcePoint.y;
            final double v8 = /*EL:320*/a1.direction.y;
            final double v9 = /*EL:321*/-(v1 * v8 - v3 * v8 - v4 * (v5 - v7));
            final double v10 = /*EL:322*/v2 * v8 - v4 * v6;
            /*SL:323*/if (v10 == 0.0) {
                /*SL:324*/return this.intersectXZ(a1);
            }
            final double v11 = /*EL:326*/v9 / v10;
            final Vector3D v12 = /*EL:327*/new Vector3D(0.0, 0.0, 0.0);
            /*SL:328*/v12.x = this.sourcePoint.x + this.direction.x * v11;
            /*SL:329*/v12.y = this.sourcePoint.y + this.direction.y * v11;
            /*SL:330*/v12.z = this.sourcePoint.z + this.direction.z * v11;
            /*SL:331*/return v12;
        }
        
        private Vector3D intersectXZ(final Line a1) {
            final double v1 = /*EL:335*/this.sourcePoint.x;
            final double v2 = /*EL:336*/this.direction.x;
            final double v3 = /*EL:337*/a1.sourcePoint.x;
            final double v4 = /*EL:338*/a1.direction.x;
            final double v5 = /*EL:339*/this.sourcePoint.z;
            final double v6 = /*EL:340*/this.direction.z;
            final double v7 = /*EL:341*/a1.sourcePoint.z;
            final double v8 = /*EL:342*/a1.direction.z;
            final double v9 = /*EL:343*/-(v1 * v8 - v3 * v8 - v4 * (v5 - v7));
            final double v10 = /*EL:344*/v2 * v8 - v4 * v6;
            /*SL:345*/if (v10 == 0.0) {
                /*SL:346*/return this.intersectYZ(a1);
            }
            final double v11 = /*EL:348*/v9 / v10;
            final Vector3D v12 = /*EL:349*/new Vector3D(0.0, 0.0, 0.0);
            /*SL:350*/v12.x = this.sourcePoint.x + this.direction.x * v11;
            /*SL:351*/v12.y = this.sourcePoint.y + this.direction.y * v11;
            /*SL:352*/v12.z = this.sourcePoint.z + this.direction.z * v11;
            /*SL:353*/return v12;
        }
        
        private Vector3D intersectYZ(final Line a1) {
            final double v1 = /*EL:357*/this.sourcePoint.y;
            final double v2 = /*EL:358*/this.direction.y;
            final double v3 = /*EL:359*/a1.sourcePoint.y;
            final double v4 = /*EL:360*/a1.direction.y;
            final double v5 = /*EL:361*/this.sourcePoint.z;
            final double v6 = /*EL:362*/this.direction.z;
            final double v7 = /*EL:363*/a1.sourcePoint.z;
            final double v8 = /*EL:364*/a1.direction.z;
            final double v9 = /*EL:365*/-(v1 * v8 - v3 * v8 - v4 * (v5 - v7));
            final double v10 = /*EL:366*/v2 * v8 - v4 * v6;
            /*SL:367*/if (v10 == 0.0) {
                /*SL:368*/return null;
            }
            final double v11 = /*EL:370*/v9 / v10;
            final Vector3D v12 = /*EL:371*/new Vector3D(0.0, 0.0, 0.0);
            /*SL:372*/v12.x = this.sourcePoint.x + this.direction.x * v11;
            /*SL:373*/v12.y = this.sourcePoint.y + this.direction.y * v11;
            /*SL:374*/v12.z = this.sourcePoint.z + this.direction.z * v11;
            /*SL:375*/return v12;
        }
        
        public Vector3D intersectPlane(final Vector3D a1, final Vector3D a2) {
            final Vector3D v1 = /*EL:379*/new Vector3D(this.sourcePoint.x, this.sourcePoint.y, this.sourcePoint.z);
            final double v2 = /*EL:380*/a1.sub(this.sourcePoint).dot(a2) / this.direction.dot(a2);
            /*SL:381*/v1.sadd(this.direction.mul(v2));
            /*SL:382*/if (this.direction.dot(a2) == 0.0) {
                /*SL:383*/return null;
            }
            /*SL:385*/return v1;
        }
    }
    
    public static class Vector3D
    {
        public double x;
        public double y;
        public double z;
        
        public Vector3D(final double a1, final double a2, final double a3) {
            this.x = a1;
            this.y = a2;
            this.z = a3;
        }
        
        public Vector3D add(final Vector3D a1) {
            /*SL:401*/return new Vector3D(this.x + a1.x, this.y + a1.y, this.z + a1.z);
        }
        
        public Vector3D add(final double a1, final double a2, final double a3) {
            /*SL:405*/return new Vector3D(this.x + a1, this.y + a2, this.z + a3);
        }
        
        public Vector3D sub(final Vector3D a1) {
            /*SL:409*/return new Vector3D(this.x - a1.x, this.y - a1.y, this.z - a1.z);
        }
        
        public Vector3D sub(final double a1, final double a2, final double a3) {
            /*SL:413*/return new Vector3D(this.x - a1, this.y - a2, this.z - a3);
        }
        
        public Vector3D normalized() {
            final double v1 = /*EL:417*/Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            /*SL:418*/return new Vector3D(this.x / v1, this.y / v1, this.z / v1);
        }
        
        public double dot(final Vector3D a1) {
            /*SL:422*/return this.x * a1.x + this.y * a1.y + this.z * a1.z;
        }
        
        public Vector3D cross(final Vector3D a1) {
            /*SL:426*/return new Vector3D(this.y * a1.z - this.z * a1.y, this.z * a1.x - this.x * a1.z, this.x * a1.y - this.y * a1.x);
        }
        
        public Vector3D mul(final double a1) {
            /*SL:430*/return new Vector3D(this.x * a1, this.y * a1, this.z * a1);
        }
        
        public Vector3D div(final double a1) {
            /*SL:434*/return new Vector3D(this.x / a1, this.y / a1, this.z / a1);
        }
        
        public double length() {
            /*SL:438*/return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        }
        
        public Vector3D sadd(final Vector3D a1) {
            /*SL:442*/this.x += a1.x;
            /*SL:443*/this.y += a1.y;
            /*SL:444*/this.z += a1.z;
            /*SL:445*/return this;
        }
        
        public Vector3D sadd(final double a1, final double a2, final double a3) {
            /*SL:449*/this.x += a1;
            /*SL:450*/this.y += a2;
            /*SL:451*/this.z += a3;
            /*SL:452*/return this;
        }
        
        public Vector3D ssub(final Vector3D a1) {
            /*SL:456*/this.x -= a1.x;
            /*SL:457*/this.y -= a1.y;
            /*SL:458*/this.z -= a1.z;
            /*SL:459*/return this;
        }
        
        public Vector3D ssub(final double a1, final double a2, final double a3) {
            /*SL:463*/this.x -= a1;
            /*SL:464*/this.y -= a2;
            /*SL:465*/this.z -= a3;
            /*SL:466*/return this;
        }
        
        public Vector3D snormalize() {
            final double v1 = /*EL:470*/Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            /*SL:471*/this.x /= v1;
            /*SL:472*/this.y /= v1;
            /*SL:473*/this.z /= v1;
            /*SL:474*/return this;
        }
        
        public Vector3D scross(final Vector3D a1) {
            /*SL:478*/this.x = this.y * a1.z - this.z * a1.y;
            /*SL:479*/this.y = this.z * a1.x - this.x * a1.z;
            /*SL:480*/this.z = this.x * a1.y - this.y * a1.x;
            /*SL:481*/return this;
        }
        
        public Vector3D smul(final double a1) {
            /*SL:485*/this.x *= a1;
            /*SL:486*/this.y *= a1;
            /*SL:487*/this.z *= a1;
            /*SL:488*/return this;
        }
        
        public Vector3D sdiv(final double a1) {
            /*SL:492*/this.x /= a1;
            /*SL:493*/this.y /= a1;
            /*SL:494*/this.z /= a1;
            /*SL:495*/return this;
        }
        
        @Override
        public String toString() {
            /*SL:500*/return "(X: " + this.x + " Y: " + this.y + " Z: " + this.z + ")";
        }
    }
    
    public static class Projection
    {
        private final double x;
        private final double y;
        private final Type t;
        
        public Projection(final double a1, final double a2, final Type a3) {
            this.x = a1;
            this.y = a2;
            this.t = a3;
        }
        
        public double getX() {
            /*SL:516*/return this.x;
        }
        
        public double getY() {
            /*SL:520*/return this.y;
        }
        
        public Type getType() {
            /*SL:524*/return this.t;
        }
        
        public boolean isType(final Type a1) {
            /*SL:528*/return this.t == a1;
        }
        
        public enum Type
        {
            INSIDE, 
            OUTSIDE, 
            INVERTED, 
            FAIL;
        }
    }
}
