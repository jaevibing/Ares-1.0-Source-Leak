package com.ares.mixin.minecraftforge;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraftforge.fml.relauncher.libraries.LibraryManager;
import java.util.List;
import java.io.File;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Loader.class })
public class MixinLoader
{
    @Redirect(method = { "identifyMods" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/relauncher/libraries/LibraryManager;gatherLegacyCanidates(Ljava/io/File;)Ljava/util/List;", remap = false), remap = false)
    protected static List<File> gatherLegacyCanidates(final File a1) {
        System.out.println(/*EL:18*/"Called gatherLegacyCandidates mixin");
        /*SL:19*/return (List<File>)LibraryManager.gatherLegacyCanidates(a1);
    }
}
