package com.ares.commands;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ChatType;
import com.ares.utils.chat.ChatUtils;

public class FakeMsg extends CommandBase
{
    public FakeMsg() {
        super(new String[] { "fakemsg", "msg", "impersonate" });
    }
    
    @Override
    public boolean execute(final String[] v-2) {
        /*SL:14*/if (v-2.length < 3) {
            /*SL:93*/return false;
        }
        final String s = v-2[0];
        switch (s) {
            default: {
                final StringBuilder v1 = new StringBuilder();
                v1.append("<").append(v-2[1]).append("> ");
                for (int a1 = 2; a1 < v-2.length; ++a1) {
                    v1.append(v-2[a1]).append(" ");
                }
                ChatUtils.printMessage(v1.toString(), false);
                return true;
            }
            case "whisper": {
                final String v2 = v-2[1];
                final StringBuilder v3 = new StringBuilder();
                for (int v4 = 2; v4 < v-2.length; ++v4) {
                    v3.append(v-2[v4]).append(" ");
                }
                this.mc.field_71456_v.func_191742_a(ChatType.CHAT, (ITextComponent)new TextComponentString("§d" + v2 + " whispers: " + v3.toString()));
                return true;
            }
            case "server": {
                final StringBuilder v1 = new StringBuilder("§e[SERVER] ");
                for (int v4 = 1; v4 < v-2.length; ++v4) {
                    v1.append(v-2[v4]).append(" ");
                }
                ChatUtils.printMessage(v1.toString(), false);
                return true;
            }
            case "suicide": {
                final String v5 = v-2[1];
                final StringBuilder v1 = new StringBuilder("§4");
                for (int v6 = 2; v6 < v-2.length; ++v6) {
                    v1.append(v-2[v6]).append(" ");
                }
                String v7 = v1.toString();
                v7 = v7.replace(" player ", " §3" + v5 + " §4");
                ChatUtils.printMessage(v7, false);
                return true;
            }
            case "kill": {
                final String v5 = v-2[1];
                final String v8 = v-2[2];
                final StringBuilder v1 = new StringBuilder("§4");
                for (int v9 = 3; v9 < v-2.length; ++v9) {
                    v1.append(v-2[v9]).append(" ");
                }
                String v7 = v1.toString();
                v7 = v7.replace(" player1 ", " §3" + v5 + " §4");
                v7 = v7.replace(" player2 ", " §3" + v8 + " §4");
                ChatUtils.printMessage(v7, false);
                return true;
            }
            case "killWeapon": {
                final String v5 = v-2[1];
                final String v8 = v-2[2];
                final String v10 = v-2[3];
                final StringBuilder v1 = new StringBuilder("§4");
                for (int v11 = 4; v11 < v-2.length; ++v11) {
                    v1.append(v-2[v11]).append(" ");
                }
                String v7 = v1.toString();
                v7 = v7.replace(" player1 ", " §3" + v5 + " §4");
                v7 = v7.replace(" player2 ", " §3" + v8 + " §4");
                v7 = v7.replace(" weapon ", " §6" + v10 + " §4");
                ChatUtils.printMessage(v7, false);
                return true;
            }
        }
    }
    
    @Override
    public String getSyntax() {
        /*SL:99*/return "-fakemsg chat 4yl im kinda ez ngl\n-fakemsg whisper John200410 Ares client on top\n-fakemsg server buy prio pls";
    }
}
