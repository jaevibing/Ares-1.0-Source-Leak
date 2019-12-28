package com.ares.commands;

import com.ares.utils.chat.ChatUtils;
import net.minecraftforge.common.MinecraftForge;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.ArrayList;

public abstract class CommandBase implements ICommandInterface
{
    public static ArrayList<CommandBase> objects;
    protected List<String> aliases;
    public Minecraft mc;
    
    private CommandBase() {
        this.mc = Minecraft.func_71410_x();
    }
    
    CommandBase(final String a1) {
        this(new String[] { a1 });
    }
    
    CommandBase(final String... a1) {
        this(Arrays.<String>asList(a1));
    }
    
    CommandBase(final List<String> a1) {
        this.mc = Minecraft.func_71410_x();
        this.aliases = a1.stream().<Object>map((Function<? super Object, ?>)String::toLowerCase).<List<String>, ?>collect((Collector<? super Object, ?, List<String>>)Collectors.<? super Object>toList());
        CommandBase.objects.add(this);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public abstract boolean execute(final String[] p0);
    
    @Override
    public void getHelp(final String[] a1, final String a2) {
        /*SL:73*/ChatUtils.printMessage("Usage: " + this.getSyntax(), "yellow");
    }
    
    @Override
    public boolean checkCommandWithOption(final String[] a3, final String[] v1, final String v2) {
        /*SL:79*/if (a3[0].equals("")) {
            /*SL:81*/this.getHelp(v1, v2);
            /*SL:82*/return false;
        }
        /*SL:85*/for (int a4 = 0; a4 <= v1.length; ++a4) {
            /*SL:87*/if (v1[a4].equals(a3[0])) {
                return true;
            }
            /*SL:89*/if (!v1[a4].equals(a3[0]) && a4 == v1.length - 1) {
                /*SL:91*/this.getHelp(v1, v2);
                /*SL:92*/return false;
            }
        }
        /*SL:96*/return true;
    }
    
    @Override
    public abstract String getSyntax();
    
    static {
        CommandBase.objects = new ArrayList<CommandBase>();
    }
}
