package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface Constant {
    boolean nullValue() default false;
    
    int intValue() default 0;
    
    float floatValue() default 0.0f;
    
    long longValue() default 0L;
    
    double doubleValue() default 0.0;
    
    String stringValue() default "";
    
    Class<?> classValue() default Object.class;
    
    int ordinal() default -1;
    
    String slice() default "";
    
    Condition[] expandZeroConditions() default {};
    
    boolean log() default false;
    
    public enum Condition
    {
        LESS_THAN_ZERO(new int[] { 155, 156 }), 
        LESS_THAN_OR_EQUAL_TO_ZERO(new int[] { 158, 157 }), 
        GREATER_THAN_OR_EQUAL_TO_ZERO(Condition.LESS_THAN_ZERO), 
        GREATER_THAN_ZERO(Condition.LESS_THAN_OR_EQUAL_TO_ZERO);
        
        private final int[] opcodes;
        private final Condition equivalence;
        
        private Condition(int[] a1) {
            this(null, a1);
        }
        
        private Condition(Condition a1) {
            this(a1, a1.opcodes);
        }
        
        private Condition(Condition a1, int[] a2) {
            this.equivalence = ((a1 != null) ? a1 : this);
            this.opcodes = a2;
        }
        
        public Condition getEquivalentCondition() {
            /*SL:108*/return this.equivalence;
        }
        
        public int[] getOpcodes() {
            /*SL:115*/return this.opcodes;
        }
    }
}
