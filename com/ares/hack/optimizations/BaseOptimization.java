package com.ares.hack.optimizations;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import com.ares.Globals;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.Ares;
import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.Objects;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class BaseOptimization
{
    protected static final Minecraft mc;
    private static ArrayList<BaseOptimization> optimizations;
    public final String name;
    public final String description;
    private boolean setEnabledQueue;
    private boolean enabled;
    
    public BaseOptimization() {
        this.name = Objects.<String>requireNonNull(this.getAnnotation().name());
        this.description = this.getAnnotation().description();
        this.setEnabled(this.getAnnotation().defaultOn());
        MinecraftForge.EVENT_BUS.register((Object)this);
        BaseOptimization.optimizations.add(this);
    }
    
    private Info getAnnotation() {
        /*SL:40*/return this.getClass().<Info>getAnnotation(Info.class);
    }
    
    public void setEnabled(final boolean a1) {
        /*SL:54*/this.setEnabledQueue = a1;
    }
    
    public boolean getEnabled() {
        /*SL:57*/return this.enabled;
    }
    
    protected void onLogic() {
    }
    
    protected void onEnabled() {
    }
    
    protected void onDisabled() {
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent v0) {
        /*SL:68*/if (BaseOptimization.mc.field_71441_e == null) {
            return;
        }
        /*SL:70*/if (this.setEnabledQueue != this.getEnabled()) {
            /*SL:72*/this.enabled = this.setEnabledQueue;
            /*SL:74*/if (this.setEnabledQueue) {
                try {
                    /*SL:78*/this.onEnabled();
                }
                catch (Throwable a1) {
                    /*SL:82*/this.setEnabled(false);
                    /*SL:83*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while enabling: " + a1.getMessage(), "red");
                    /*SL:84*/a1.printStackTrace();
                }
            }
            else {
                /*SL:89*/Ares.setTitle();
                try {
                    /*SL:92*/this.onDisabled();
                }
                catch (Throwable v) {
                    /*SL:96*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while disabling: " + v.getMessage(), "red");
                    /*SL:97*/v.printStackTrace();
                }
            }
        }
        try {
            /*SL:105*/this.onLogic();
        }
        catch (Throwable v) {
            /*SL:109*/this.setEnabled(false);
            /*SL:110*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while ticking: " + v.getMessage(), "red");
            /*SL:111*/v.printStackTrace();
        }
    }
    
    public static ArrayList<BaseOptimization> getAll() {
        /*SL:117*/return BaseOptimization.optimizations;
    }
    
    static {
        mc = Globals.mc;
        BaseOptimization.optimizations = new ArrayList<BaseOptimization>();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface Info {
        @Nonnull
        String name();
        
        @Nonnull
        String description();
        
        @Nonnull
        boolean defaultOn();
    }
}
