package org.spongepowered.asm.launch.platform;

import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.net.URI;
import org.apache.logging.log4j.Logger;

public abstract class MixinPlatformAgentAbstract implements IMixinPlatformAgent
{
    protected static final Logger logger;
    protected final MixinPlatformManager manager;
    protected final URI uri;
    protected final File container;
    protected final MainAttributes attributes;
    
    public MixinPlatformAgentAbstract(final MixinPlatformManager a1, final URI a2) {
        this.manager = a1;
        this.uri = a2;
        this.container = ((this.uri != null) ? new File(this.uri) : null);
        this.attributes = MainAttributes.of(a2);
    }
    
    @Override
    public String toString() {
        /*SL:75*/return String.format("PlatformAgent[%s:%s]", this.getClass().getSimpleName(), this.uri);
    }
    
    @Override
    public String getPhaseProvider() {
        /*SL:80*/return null;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
}
