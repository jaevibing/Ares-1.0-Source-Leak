package com.ares.hack.hacks.player;

import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.util.ChatAllowedCharacters;
import com.ares.utils.chat.ChatUtils;
import org.apache.commons.lang3.StringUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.ares.hack.settings.settings.number.IntegerSetting;
import com.ares.hack.settings.settings.EnumSetting;
import javax.swing.Timer;
import com.ares.hack.settings.Setting;
import java.io.File;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "Spammer", description = "Spam the chat", category = EnumCategory.PLAYER)
public class Spammer extends BaseHack
{
    private static final char[] chars;
    private static final File FILE;
    private SpamType lastKnownMode;
    private Setting<SpamType> mode;
    private Setting<Integer> delay;
    private Timer fileTimer;
    private Timer spamTimer;
    private Timer emptyTimer;
    
    public Spammer() {
        this.mode = new EnumSetting<SpamType>("Mode", this, SpamType.FILE);
        this.delay = new IntegerSetting("Delay", this, 2, 1, 60);
        try {
            Spammer.FILE.createNewFile();
        }
        catch (Exception v1) {
            v1.printStackTrace();
        }
    }
    
    public void onEnabled() {
        /*SL:61*/if (this.delay == null) {
            this.delay = new IntegerSetting("Delay", this, 2, 1, 60);
        }
        try {
            /*SL:65*/switch (this.mode.getValue()) {
                case FILE: {
                    final String[] v1 = /*EL:68*/new String(Files.readAllBytes(Paths.get(Spammer.FILE.getCanonicalPath(), new String[0]))).split("\n");
                    final int v2 = /*EL:69*/v1.length;
                    while (true) {
                        /*SL:70*/if (v2 == 0 || v1[0].isEmpty()) {
                            /*SL:77*/(this.fileTimer = new Timer(this.delay.getValue() * 1000, new ActionListener() {
                                int index = 0;
                                
                                @Override
                                public void actionPerformed(final ActionEvent a1) {
                                    /*SL:85*/if (this.index < /*EL:92*/v2) {
                                        Spammer.mc.field_71439_g.func_71165_d(v1[this.index]);
                                        ++this.index;
                                        if (this.index == v2) {
                                            this.index = 0;
                                        }
                                    }
                                }
                            })).start();
                            /*SL:97*/break;
                        }
                        continue;
                    }
                }
                case SPAM: {
                    /*SL:101*/(this.spamTimer = new Timer(this.delay.getValue() * 1000, a1 -> Spammer.mc.field_71439_g.func_71165_d(this.getRandomCharacters()))).start();
                    /*SL:108*/break;
                }
                default: {
                    /*SL:113*/this.emptyTimer = new Timer(this.delay.getValue() * 1000, a1 -> Spammer.mc.field_71439_g.func_71165_d(StringUtils.repeat(Spammer.chars[195], 256)));
                    break;
                }
            }
        }
        catch (Exception v3) {
            /*SL:120*/ChatUtils.printMessage("Disabled spammer due to error: " + v3.toString(), "red");
            /*SL:121*/v3.printStackTrace();
            /*SL:122*/this.setEnabled(false);
        }
    }
    
    public void onLogic() {
        /*SL:129*/if (this.getEnabled()) {
            /*SL:131*/if (this.lastKnownMode == null) {
                /*SL:133*/this.lastKnownMode = this.mode.getValue();
                /*SL:134*/return;
            }
            /*SL:136*/if (!this.lastKnownMode.equals(this.mode.getValue())) {
                /*SL:138*/this.onDisabled();
                /*SL:139*/this.onEnabled();
            }
            /*SL:141*/this.lastKnownMode = this.mode.getValue();
        }
    }
    
    public void onDisabled() {
        /*SL:148*/for (final Timer v1 : new Timer[] { this.fileTimer, this.spamTimer, this.emptyTimer }) {
            try {
                /*SL:152*/v1.stop();
            }
            catch (Exception ex) {}
        }
    }
    
    private String getRandomCharacters() {
        final StringBuilder sb = /*EL:160*/new StringBuilder();
        /*SL:161*/for (int v0 = 0; v0 < 256; ++v0) {
            char v;
            /*SL:164*/for (v = ' '; v == ' ' || !ChatAllowedCharacters.func_71566_a(v); /*SL:165*/v = Spammer.chars[new Random().nextInt(Spammer.chars.length)]) {}
            /*SL:167*/sb.append(v);
        }
        /*SL:169*/return sb.toString();
    }
    
    static {
        chars = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~\u00e2\u0152\u201a\u00c3\u2021\u00c3¼\u00c3©\u00c3¢\u00c3¤\u00c3 \u00c3¥\u00c3§\u00c3ª\u00c3«\u00c3¨\u00c3¯\u00c3®\u00c3¬\u00c3\u201e\u00c3\u2026\u00c3\u2030\u00c3¦\u00c3\u2020\u00c3´\u00c3¶\u00c3²\u00c3»\u00c3¹\u00c3¿\u00c3\u2013\u00c3\u0153\u00c3¸\u00c2£\u00c3\u02dc\u00c3\u2014\u00c6\u2019\u00c3¡\u00c3\u00ad\u00c3³\u00c3º\u00c3±\u00c3\u2018\u00c2ª\u00c2º".toCharArray();
        FILE = new File("Ares/spammer.txt");
    }
    
    enum SpamType
    {
        FILE, 
        SPAM, 
        EMPTY;
    }
}
