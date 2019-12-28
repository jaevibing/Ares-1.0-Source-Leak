package com.ares.utils.math;

import java.util.Objects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public abstract class Angle
{
    public static final Angle ZERO;
    private final float pitch;
    private final float yaw;
    private final float roll;
    
    public static Angle radians(final float a1, final float a2, final float a3) {
        /*SL:15*/return new Radians(a1, a2, a3);
    }
    
    public static Angle radians(final float a1, final float a2) {
        /*SL:19*/return radians(a1, a2, 0.0f);
    }
    
    public static Angle radians(final double a1, final double a2, final double a3) {
        /*SL:23*/return radians(/*EL:24*/(float)AngleHelper.roundAngle(a1), /*EL:25*/(float)AngleHelper.roundAngle(a2), /*EL:26*/(float)AngleHelper.roundAngle(a3));
    }
    
    public static Angle radians(final double a1, final double a2) {
        /*SL:30*/return radians(a1, a2, 0.0);
    }
    
    public static Angle degrees(final float a1, final float a2, final float a3) {
        /*SL:34*/return new Degrees(a1, a2, a3);
    }
    
    public static Angle degrees(final float a1, final float a2) {
        /*SL:38*/return degrees(a1, a2, 0.0f);
    }
    
    public static Angle degrees(final double a1, final double a2, final double a3) {
        /*SL:42*/return degrees(/*EL:43*/(float)AngleHelper.roundAngle(a1), /*EL:44*/(float)AngleHelper.roundAngle(a2), /*EL:45*/(float)AngleHelper.roundAngle(a3));
    }
    
    public static Angle degrees(final double a1, final double a2) {
        /*SL:49*/return degrees(a1, a2, 0.0);
    }
    
    public static Angle copy(final Angle a1) {
        /*SL:53*/return a1.newInstance(a1.getPitch(), a1.getYaw(), a1.getRoll());
    }
    
    private Angle(final float a1, final float a2, final float a3) {
        this.pitch = a1;
        this.yaw = a2;
        this.roll = a3;
    }
    
    public float getPitch() {
        /*SL:67*/return this.pitch;
    }
    
    public float getYaw() {
        /*SL:71*/return this.yaw;
    }
    
    public float getRoll() {
        /*SL:75*/return this.roll;
    }
    
    public Angle setPitch(final float a1) {
        /*SL:79*/return this.newInstance(a1, this.getYaw(), this.getRoll());
    }
    
    public Angle setYaw(final float a1) {
        /*SL:83*/return this.newInstance(this.getPitch(), a1, this.getRoll());
    }
    
    public Angle setRoll(final float a1) {
        /*SL:87*/return this.newInstance(this.getPitch(), this.getYaw(), a1);
    }
    
    public abstract boolean isInDegrees();
    
    public boolean isInRadians() {
        /*SL:93*/return !this.isInDegrees();
    }
    
    public Angle add(final Angle a1) {
        /*SL:97*/return this.newInstance(this.getPitch() + /*EL:98*/a1.same(this).getPitch(), this.getYaw() + /*EL:99*/a1.same(this).getYaw(), this.getRoll() + /*EL:100*/a1.same(this).getRoll());
    }
    
    public Angle add(final float a1, final float a2, final float a3) {
        /*SL:104*/return this.add(this.newInstance(a1, a2, a3));
    }
    
    public Angle add(final float a1, final float a2) {
        /*SL:108*/return this.add(a1, a2, 0.0f);
    }
    
    public Angle sub(final Angle a1) {
        /*SL:112*/return this.add(a1.scale(-1.0f));
    }
    
    public Angle sub(final float a1, final float a2, final float a3) {
        /*SL:116*/return this.add(-a1, -a2, -a3);
    }
    
    public Angle sub(final float a1, final float a2) {
        /*SL:120*/return this.sub(a1, a2, 0.0f);
    }
    
    public Angle scale(final float a1) {
        /*SL:124*/return this.newInstance(this.getPitch() * a1, this.getYaw() * a1, this.getRoll() * a1);
    }
    
    public abstract Angle normalize();
    
    public double[] getForwardVector() {
        final double v1 = /*EL:133*/Math.sin(this.inRadians().getPitch());
        final double v2 = /*EL:134*/Math.cos(this.inRadians().getPitch());
        final double v3 = /*EL:135*/Math.sin(this.inRadians().getYaw());
        final double v4 = /*EL:136*/Math.cos(this.inRadians().getYaw());
        /*SL:137*/return new double[] { v2 * v4, v1, v2 * v3 };
    }
    
    public Vec3d getDirectionVector() {
        final float v1 = /*EL:145*/MathHelper.func_76134_b(-this.inDegrees().getYaw() * 0.017453292f - 3.1415927f);
        final float v2 = /*EL:146*/MathHelper.func_76126_a(-this.inDegrees().getYaw() * 0.017453292f - 3.1415927f);
        final float v3 = /*EL:147*/-MathHelper.func_76134_b(-this.inDegrees().getPitch() * 0.017453292f);
        final float v4 = /*EL:148*/MathHelper.func_76126_a(-this.inDegrees().getPitch() * 0.017453292f);
        /*SL:149*/return new Vec3d((double)(v2 * v3), (double)v4, (double)(v1 * v3));
    }
    
    public float[] toArray() {
        /*SL:153*/return new float[] { this.getPitch(), this.getYaw(), this.getRoll() };
    }
    
    public abstract Angle inRadians();
    
    public abstract Angle inDegrees();
    
    protected Angle same(final Angle a1) {
        /*SL:161*/return a1.isInDegrees() ? this.inDegrees() : this.inRadians();
    }
    
    protected abstract Angle newInstance(final float p0, final float p1, final float p2);
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:168*/return this == a1 || (a1 instanceof Angle && AngleHelper.isEqual(this, (Angle)a1));
    }
    
    @Override
    public int hashCode() {
        final Angle v1 = /*EL:173*/this.normalize().inDegrees();
        /*SL:174*/return Objects.hash(v1.getPitch(), v1.getYaw(), v1.getRoll());
    }
    
    @Override
    public String toString() {
        /*SL:179*/return String.format("(%.15f, %.15f, %.15f)[%s]", this.getPitch(), /*EL:181*/this.getYaw(), this.getRoll(), this.isInRadians() ? "rad" : "deg");
    }
    
    static {
        ZERO = degrees(0.0f, 0.0f, 0.0f);
    }
    
    static class Degrees extends Angle
    {
        private Radians radians;
        
        private Degrees(final float a1, final float a2, final float a3) {
            super(a1, a2, a3, null);
            this.radians = null;
        }
        
        @Override
        public boolean isInDegrees() {
            /*SL:194*/return true;
        }
        
        @Override
        public Angle normalize() {
            /*SL:199*/return this.newInstance(/*EL:200*/AngleHelper.normalizeInDegrees(this.getPitch()), /*EL:201*/AngleHelper.normalizeInDegrees(this.getYaw()), /*EL:202*/AngleHelper.normalizeInDegrees(this.getRoll()));
        }
        
        @Override
        public Angle inRadians() {
            /*SL:207*/return (this.radians == null) ? /*EL:210*/(this.radians = (Radians)Angle.radians(/*EL:211*/Math.toRadians(this.getPitch()), /*EL:212*/Math.toRadians(this.getYaw()), /*EL:213*/Math.toRadians(this.getRoll()))) : this.radians;
        }
        
        @Override
        public Angle inDegrees() {
            /*SL:219*/return this;
        }
        
        @Override
        protected Angle newInstance(final float a1, final float a2, final float a3) {
            /*SL:224*/return new Degrees(a1, a2, a3);
        }
    }
    
    static class Radians extends Angle
    {
        private Degrees degrees;
        
        private Radians(final float a1, final float a2, final float a3) {
            super(a1, a2, a3, null);
            this.degrees = null;
        }
        
        @Override
        public boolean isInDegrees() {
            /*SL:238*/return false;
        }
        
        @Override
        public Angle normalize() {
            /*SL:243*/return this.newInstance(/*EL:244*/AngleHelper.normalizeInRadians(this.getPitch()), /*EL:245*/AngleHelper.normalizeInRadians(this.getYaw()), /*EL:246*/AngleHelper.normalizeInRadians(this.getRoll()));
        }
        
        @Override
        public Angle inRadians() {
            /*SL:251*/return this;
        }
        
        @Override
        public Angle inDegrees() {
            /*SL:256*/return (this.degrees == null) ? /*EL:259*/(this.degrees = (Degrees)Angle.degrees(/*EL:260*/Math.toDegrees(this.getPitch()), /*EL:261*/Math.toDegrees(this.getYaw()), /*EL:262*/Math.toDegrees(this.getRoll()))) : this.degrees;
        }
        
        @Override
        protected Angle newInstance(final float a1, final float a2, final float a3) {
            /*SL:268*/return new Radians(a1, a2, a3);
        }
    }
}
