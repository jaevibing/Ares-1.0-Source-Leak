package com.google.api.client.repackaged.com.google.common.base;

import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.ServiceConfigurationError;
import javax.annotation.Nullable;
import java.util.Locale;
import java.lang.ref.WeakReference;
import java.util.logging.Logger;
import com.google.api.client.repackaged.com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform
{
    private static final Logger logger;
    private static final PatternCompiler patternCompiler;
    
    static long systemNanoTime() {
        /*SL:40*/return System.nanoTime();
    }
    
    static CharMatcher precomputeCharMatcher(final CharMatcher a1) {
        /*SL:44*/return a1.precomputedInternal();
    }
    
    static <T extends Enum<T>> Optional<T> getEnumIfPresent(final Class<T> a1, final String a2) {
        final WeakReference<? extends Enum<?>> v1 = /*EL:48*/Enums.<T>getEnumConstants(a1).get(a2);
        /*SL:49*/return (v1 == null) ? Optional.<T>absent() : Optional.<T>of(a1.cast(v1.get()));
    }
    
    static String formatCompact4Digits(final double a1) {
        /*SL:55*/return String.format(Locale.ROOT, "%.4g", a1);
    }
    
    static boolean stringIsNullOrEmpty(@Nullable final String a1) {
        /*SL:59*/return a1 == null || a1.isEmpty();
    }
    
    static CommonPattern compilePattern(final String a1) {
        /*SL:63*/Preconditions.<String>checkNotNull(a1);
        /*SL:64*/return Platform.patternCompiler.compile(a1);
    }
    
    static boolean usingJdkPatternCompiler() {
        /*SL:68*/return Platform.patternCompiler instanceof JdkPatternCompiler;
    }
    
    private static PatternCompiler loadPatternCompiler() {
        /*SL:77*/return new JdkPatternCompiler();
    }
    
    private static void logPatternCompilerError(final ServiceConfigurationError a1) {
        Platform.logger.log(Level.WARNING, /*EL:81*/"Error loading regex compiler, falling back to next option", a1);
    }
    
    static {
        logger = Logger.getLogger(Platform.class.getName());
        patternCompiler = loadPatternCompiler();
    }
    
    private static final class JdkPatternCompiler implements PatternCompiler
    {
        @Override
        public CommonPattern compile(final String a1) {
            /*SL:87*/return new JdkPattern(Pattern.compile(a1));
        }
    }
}
