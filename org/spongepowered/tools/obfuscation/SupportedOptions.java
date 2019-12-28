package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.service.ObfuscationServices;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public final class SupportedOptions
{
    public static final String TOKENS = "tokens";
    public static final String OUT_REFMAP_FILE = "outRefMapFile";
    public static final String DISABLE_TARGET_VALIDATOR = "disableTargetValidator";
    public static final String DISABLE_TARGET_EXPORT = "disableTargetExport";
    public static final String DISABLE_OVERWRITE_CHECKER = "disableOverwriteChecker";
    public static final String OVERWRITE_ERROR_LEVEL = "overwriteErrorLevel";
    public static final String DEFAULT_OBFUSCATION_ENV = "defaultObfuscationEnv";
    public static final String DEPENDENCY_TARGETS_FILE = "dependencyTargetsFile";
    
    public static Set<String> getAllOptions() {
        final ImmutableSet.Builder<String> v1 = /*EL:55*/ImmutableSet.<String>builder();
        /*SL:56*/v1.add("tokens", "outRefMapFile", "disableTargetValidator", "disableTargetExport", "disableOverwriteChecker", "overwriteErrorLevel", "defaultObfuscationEnv", "dependencyTargetsFile");
        /*SL:66*/v1.addAll(/*EL:67*/ObfuscationServices.getInstance().getSupportedOptions());
        /*SL:69*/return v1.build();
    }
}
