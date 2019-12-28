package org.spongepowered.asm.util;

import org.spongepowered.asm.util.throwables.ConstraintViolationException;
import java.util.regex.Matcher;
import org.spongepowered.asm.util.throwables.InvalidConstraintException;
import java.util.regex.Pattern;
import org.spongepowered.asm.lib.tree.AnnotationNode;

public final class ConstraintParser
{
    public static Constraint parse(final String v-5) {
        /*SL:285*/if (v-5 == null || v-5.length() == 0) {
            /*SL:286*/return Constraint.NONE;
        }
        final String[] split = /*EL:289*/v-5.replaceAll("\\s", "").toUpperCase().split(";");
        Constraint constraint = /*EL:290*/null;
        /*SL:291*/for (final String v1 : split) {
            final Constraint a1 = /*EL:292*/new Constraint(v1);
            /*SL:293*/if (constraint == null) {
                /*SL:294*/constraint = a1;
            }
            else {
                /*SL:296*/constraint.append(a1);
            }
        }
        /*SL:300*/return (constraint != null) ? constraint : Constraint.NONE;
    }
    
    public static Constraint parse(final AnnotationNode a1) {
        final String v1 = /*EL:313*/Annotations.<String>getValue(a1, "constraints", "");
        /*SL:314*/return parse(v1);
    }
    
    public static class Constraint
    {
        public static final Constraint NONE;
        private static final Pattern pattern;
        private final String expr;
        private String token;
        private String[] constraint;
        private int min;
        private int max;
        private Constraint next;
        
        Constraint(final String a1) {
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
            this.expr = a1;
            final Matcher v1 = Constraint.pattern.matcher(a1);
            if (!v1.matches()) {
                throw new InvalidConstraintException("Constraint syntax was invalid parsing: " + this.expr);
            }
            this.token = v1.group(1);
            this.constraint = new String[] { v1.group(2), v1.group(3), v1.group(4), v1.group(5), v1.group(6), v1.group(7), v1.group(8) };
            this.parse();
        }
        
        private Constraint() {
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
            this.expr = null;
            this.token = "*";
            this.constraint = new String[0];
        }
        
        private void parse() {
            /*SL:139*/if (!this.has(1)) {
                /*SL:140*/return;
            }
            final int val = /*EL:143*/this.val(1);
            this.min = val;
            this.max = val;
            final boolean v0 = /*EL:144*/this.has(0);
            /*SL:146*/if (this.has(4)) {
                /*SL:147*/if (v0) {
                    /*SL:148*/throw new InvalidConstraintException("Unexpected modifier '" + this.elem(0) + "' in " + this.expr + " parsing range");
                }
                /*SL:150*/this.max = this.val(4);
                /*SL:151*/if (this.max < this.min) {
                    /*SL:152*/throw new InvalidConstraintException("Invalid range specified '" + this.max + "' is less than " + this.min + " in " + this.expr);
                }
            }
            else {
                /*SL:155*/if (!this.has(6)) {
                    /*SL:163*/if (v0) {
                        /*SL:164*/if (this.has(3)) {
                            /*SL:165*/throw new InvalidConstraintException("Unexpected trailing modifier '" + this.elem(3) + "' in " + this.expr);
                        }
                        final String v = /*EL:167*/this.elem(0);
                        /*SL:168*/if (">".equals(v)) {
                            /*SL:169*/++this.min;
                            /*SL:170*/this.max = Integer.MAX_VALUE;
                        }
                        else/*SL:171*/ if (">=".equals(v)) {
                            /*SL:172*/this.max = Integer.MAX_VALUE;
                        }
                        else/*SL:173*/ if ("<".equals(v)) {
                            final int n = /*EL:174*/this.min - 1;
                            this.min = n;
                            this.max = n;
                            /*SL:175*/this.min = Integer.MIN_VALUE;
                        }
                        else/*SL:176*/ if ("<=".equals(v)) {
                            /*SL:177*/this.max = this.min;
                            /*SL:178*/this.min = Integer.MIN_VALUE;
                        }
                    }
                    else/*SL:180*/ if (this.has(2)) {
                        final String v = /*EL:181*/this.elem(2);
                        /*SL:182*/if ("<".equals(v)) {
                            /*SL:183*/this.max = this.min;
                            /*SL:184*/this.min = Integer.MIN_VALUE;
                        }
                        else {
                            /*SL:186*/this.max = Integer.MAX_VALUE;
                        }
                    }
                    /*SL:189*/return;
                }
                if (v0) {
                    throw new InvalidConstraintException("Unexpected modifier '" + this.elem(0) + "' in " + this.expr + " parsing range");
                }
                this.max = this.min + this.val(6);
            }
        }
        
        private boolean has(final int a1) {
            /*SL:192*/return this.constraint[a1] != null;
        }
        
        private String elem(final int a1) {
            /*SL:196*/return this.constraint[a1];
        }
        
        private int val(final int a1) {
            /*SL:200*/return (this.constraint[a1] != null) ? Integer.parseInt(this.constraint[a1]) : 0;
        }
        
        void append(final Constraint a1) {
            /*SL:204*/if (this.next != null) {
                /*SL:205*/this.next.append(a1);
                /*SL:206*/return;
            }
            /*SL:208*/this.next = a1;
        }
        
        public String getToken() {
            /*SL:212*/return this.token;
        }
        
        public int getMin() {
            /*SL:216*/return this.min;
        }
        
        public int getMax() {
            /*SL:220*/return this.max;
        }
        
        public void check(final ITokenProvider v2) throws ConstraintViolationException {
            /*SL:231*/if (this != Constraint.NONE) {
                final Integer a1 = /*EL:232*/v2.getToken(this.token);
                /*SL:233*/if (a1 == null) {
                    /*SL:234*/throw new ConstraintViolationException("The token '" + this.token + "' could not be resolved in " + v2, this);
                }
                /*SL:236*/if (a1 < this.min) {
                    /*SL:237*/throw new ConstraintViolationException("Token '" + this.token + "' has a value (" + a1 + ") which is less than the minimum value " + this.min + " in " + v2, this, a1);
                }
                /*SL:240*/if (a1 > this.max) {
                    /*SL:241*/throw new ConstraintViolationException("Token '" + this.token + "' has a value (" + a1 + ") which is greater than the maximum value " + this.max + " in " + v2, this, a1);
                }
            }
            /*SL:245*/if (this.next != null) {
                /*SL:246*/this.next.check(v2);
            }
        }
        
        public String getRangeHumanReadable() {
            /*SL:255*/if (this.min == Integer.MIN_VALUE && this.max == Integer.MAX_VALUE) {
                /*SL:256*/return "ANY VALUE";
            }
            /*SL:257*/if (this.min == Integer.MIN_VALUE) {
                /*SL:258*/return String.format("less than or equal to %d", this.max);
            }
            /*SL:259*/if (this.max == Integer.MAX_VALUE) {
                /*SL:260*/return String.format("greater than or equal to %d", this.min);
            }
            /*SL:261*/if (this.min == this.max) {
                /*SL:262*/return String.format("%d", this.min);
            }
            /*SL:264*/return String.format("between %d and %d", this.min, this.max);
        }
        
        @Override
        public String toString() {
            /*SL:269*/return String.format("Constraint(%s [%d-%d])", this.token, this.min, this.max);
        }
        
        static {
            NONE = new Constraint();
            pattern = Pattern.compile("^([A-Z0-9\\-_\\.]+)\\((?:(<|<=|>|>=|=)?([0-9]+)(<|(-)([0-9]+)?|>|(\\+)([0-9]+)?)?)?\\)$");
        }
    }
}
