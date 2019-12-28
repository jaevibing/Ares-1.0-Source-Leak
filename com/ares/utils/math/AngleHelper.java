package com.ares.utils.math;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;

public class AngleHelper
{
    public static final long DEFAULT_N = 1000000000L;
    public static final double DEFAULT_EPSILON = 1.0E-9;
    public static final double TWO_PI = 6.283185307179586;
    public static final double HALF_PI = 1.5707963267948966;
    public static final double QUARTER_PI = 0.7853981633974483;
    
    public static double roundAngle(final double a1, final long a2) {
        /*SL:19*/return Math.round(a1 * a2) / a2;
    }
    
    public static double roundAngle(final double a1) {
        /*SL:23*/return roundAngle(a1, 1000000000L);
    }
    
    public static boolean isAngleEqual(final double a1, final double a2, final double a3) {
        /*SL:27*/return Double.compare(a1, a2) == 0 || Math.abs(a1 - a2) < a3;
    }
    
    public static boolean isAngleEqual(final double a1, final double a2) {
        /*SL:31*/return isAngleEqual(a1, a2, 1.0E-4);
    }
    
    public static boolean isEqual(final Angle a1, final Angle a2) {
        final Angle v1 = /*EL:35*/a1.normalize();
        final Angle v2 = /*EL:36*/a2.same(v1).normalize();
        /*SL:37*/return isAngleEqual(v1.getPitch(), v2.getPitch()) && isAngleEqual(v1.getYaw(), /*EL:38*/v2.getYaw()) && isAngleEqual(v1.getRoll(), /*EL:39*/v2.getRoll());
    }
    
    public static double normalizeInRadians(double a1) {
        /*SL:43*/while (a1 > 3.141592653589793) {
            /*SL:44*/a1 -= 6.283185307179586;
        }
        /*SL:46*/while (a1 < -3.141592653589793) {
            /*SL:47*/a1 += 6.283185307179586;
        }
        /*SL:49*/return a1;
    }
    
    public static float normalizeInRadians(float a1) {
        /*SL:53*/while (a1 > 3.141592653589793) {
            /*SL:54*/a1 -= 6.283185307179586;
        }
        /*SL:56*/while (a1 < -3.141592653589793) {
            /*SL:57*/a1 += 6.283185307179586;
        }
        /*SL:59*/return a1;
    }
    
    public static double normalizeInDegrees(final double a1) {
        /*SL:63*/return MathHelper.func_76138_g(a1);
    }
    
    public static float normalizeInDegrees(final float a1) {
        /*SL:67*/return MathHelper.func_76142_g(a1);
    }
    
    public static Angle getAngleFacingInRadians(final Vec3d v-2) {
        double v1;
        final double n;
        /*SL:72*/if (v-2.field_72450_a == 0.0 && v-2.field_72449_c == 0.0) {
            /*SL:73*/v1 = 0.0;
            final double a1 = /*EL:74*/1.5707963267948966;
        }
        else {
            /*SL:76*/v1 = Math.atan2(v-2.field_72449_c, v-2.field_72450_a) - 1.5707963267948966;
            final double v2 = /*EL:77*/Math.sqrt(v-2.field_72450_a * v-2.field_72450_a + v-2.field_72449_c * v-2.field_72449_c);
            /*SL:78*/n = -Math.atan2(v-2.field_72448_b, v2);
        }
        /*SL:80*/return Angle.radians((float)n, (float)v1);
    }
    
    public static Angle getAngleFacingInDegrees(final Vec3d a1) {
        /*SL:84*/return getAngleFacingInRadians(a1).inDegrees();
    }
}
