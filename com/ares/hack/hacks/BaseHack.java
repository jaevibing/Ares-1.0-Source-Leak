package com.ares.hack.hacks;

import com.ares.hack.categories.EnumCategory;
import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import com.ares.Globals;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.client.ares.HackDisabled;
import com.ares.Ares;
import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.fml.common.eventhandler.Event;
import com.ares.event.client.ares.HackEnabled;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import com.ares.hack.settings.settings.BindSetting;
import com.ares.hack.settings.settings.BooleanSetting;
import java.util.Objects;
import com.ares.hack.settings.Setting;
import com.ares.hack.categories.Category;
import net.minecraft.client.Minecraft;

public class BaseHack
{
    protected static final Minecraft mc;
    public final String name;
    public final Category category;
    public final String description;
    public final Setting<Boolean> isVisible;
    public final Setting<String> bind;
    private boolean setEnabledQueue;
    private boolean enabled;
    
    public BaseHack() {
        this.name = Objects.<String>requireNonNull(this.getAnnotation().name());
        this.category = Objects.<Category>requireNonNull(this.getAnnotation().category().category);
        this.description = this.getAnnotation().description();
        this.setEnabled(this.getAnnotation().defaultOn());
        this.isVisible = new BooleanSetting("Is Visible", this, this.getAnnotation().defaultIsVisible());
        this.bind = new BindSetting("Bind", this, this.getAnnotation().defaultBind());
        HackManager.getAll().add(this);
        HackManager.hacksMap.put(this.name, this);
        this.category.getHacks().add(this);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    private Hack getAnnotation() {
        /*SL:74*/return this.getClass().<Hack>getAnnotation(Hack.class);
    }
    
    public void setEnabled(final boolean a1) {
        /*SL:92*/this.setEnabledQueue = a1;
    }
    
    public static void setEnabled(final String a1, final boolean a2) {
        HackManager.hacksMap.get(/*EL:95*/a1).setEnabledQueue = a2;
    }
    
    public boolean getEnabled() {
        /*SL:97*/return this.enabled;
    }
    
    public static boolean getEnabled(final String a1) {
        /*SL:99*/return HackManager.hacksMap.get(a1).enabled;
    }
    
    protected void onLogic() {
    }
    
    protected void onRender2d() {
    }
    
    protected void onRender3d() {
    }
    
    protected void onEnabled() {
    }
    
    protected void onDisabled() {
    }
    
    public void onDestroy() {
        MinecraftForge.EVENT_BUS.unregister(/*EL:111*/(Object)this);
    }
    
    public boolean getSetEnabledQueue() {
        /*SL:113*/return this.setEnabledQueue;
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent v0) {
        /*SL:118*/if (BaseHack.mc.field_71441_e == null) {
            return;
        }
        /*SL:121*/if (this.setEnabledQueue != this.getEnabled()) {
            /*SL:123*/this.enabled = this.setEnabledQueue;
            /*SL:125*/if (this.setEnabledQueue) {
                try {
                    /*SL:129*/this.onEnabled();
                    final HackEnabled a1 = /*EL:130*/new HackEnabled(HackManager.hacksMap.get(this.name));
                    MinecraftForge.EVENT_BUS.post(/*EL:131*/(Event)a1);
                }
                catch (Throwable v) {
                    /*SL:135*/this.setEnabled(false);
                    /*SL:136*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while enabling: " + v.getMessage(), "red");
                    /*SL:137*/v.printStackTrace();
                }
            }
            else {
                /*SL:142*/Ares.setTitle();
                try {
                    /*SL:145*/this.onDisabled();
                    final HackDisabled v2 = /*EL:146*/new HackDisabled(HackManager.hacksMap.get(this.name));
                    MinecraftForge.EVENT_BUS.post(/*EL:147*/(Event)v2);
                }
                catch (Throwable v) {
                    /*SL:151*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while disabling: " + v.getMessage(), "red");
                    /*SL:152*/v.printStackTrace();
                }
            }
        }
        try {
            /*SL:160*/this.onLogic();
        }
        catch (Throwable v) {
            /*SL:164*/this.setEnabled(false);
            /*SL:165*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while ticking: " + v.getMessage(), "red");
            /*SL:166*/v.printStackTrace();
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void RenderHud(final RenderGameOverlayEvent.Post v2) {
        /*SL:173*/if (v2.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            try {
                /*SL:177*/this.onRender2d();
            }
            catch (Throwable a1) {
                /*SL:181*/this.setEnabled(false);
                /*SL:182*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while rendering: " + a1.getMessage(), "red");
                /*SL:183*/a1.printStackTrace();
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderWorldLast(final RenderWorldLastEvent v2) {
        try {
            /*SL:193*/this.onRender3d();
        }
        catch (Throwable a1) {
            /*SL:197*/this.setEnabled(false);
            /*SL:198*/ChatUtils.printMessage("Disabled '" + this.name + "' due to error while rendering 3d: " + a1.getMessage(), "red");
            /*SL:199*/a1.printStackTrace();
        }
    }
    
    @Override
    public String toString() {
        /*SL:206*/return this.name + ":" + this.enabled;
    }
    
    static {
        mc = Globals.mc;
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface Hack {
        @Nonnull
        String name();
        
        @Nonnull
        String description();
        
        @Nonnull
        EnumCategory category();
        
        boolean defaultOn() default false;
        
        boolean defaultIsVisible() default true;
        
        String defaultBind() default "NONE";
    }
}
