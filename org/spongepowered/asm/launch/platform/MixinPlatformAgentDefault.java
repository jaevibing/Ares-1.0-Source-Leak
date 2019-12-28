package org.spongepowered.asm.launch.platform;

import java.net.URI;

public class MixinPlatformAgentDefault extends MixinPlatformAgentAbstract
{
    public MixinPlatformAgentDefault(final MixinPlatformManager a1, final URI a2) {
        super(a1, a2);
    }
    
    @Override
    public void prepare() {
        final String value = /*EL:48*/this.attributes.get("MixinCompatibilityLevel");
        /*SL:49*/if (value != null) {
            /*SL:50*/this.manager.setCompatibilityLevel(value);
        }
        final String value2 = /*EL:53*/this.attributes.get("MixinConfigs");
        /*SL:54*/if (value2 != null) {
            /*SL:55*/for (final String v1 : value2.split(",")) {
                /*SL:56*/this.manager.addConfig(v1.trim());
            }
        }
        final String value3 = /*EL:60*/this.attributes.get("MixinTokenProviders");
        /*SL:61*/if (value3 != null) {
            /*SL:62*/for (final String v2 : value3.split(",")) {
                /*SL:63*/this.manager.addTokenProvider(v2.trim());
            }
        }
    }
    
    @Override
    public void initPrimaryContainer() {
    }
    
    @Override
    public void inject() {
    }
    
    @Override
    public String getLaunchTarget() {
        /*SL:78*/return this.attributes.get("Main-Class");
    }
}
