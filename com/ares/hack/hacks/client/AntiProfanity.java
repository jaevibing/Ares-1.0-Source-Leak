package com.ares.hack.hacks.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.ares.event.client.fontrenderer.DrawString;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import com.ares.hack.categories.EnumCategory;
import com.ares.hack.hacks.BaseHack;

@Hack(name = "AntiProfanity", description = "Filter out naughty words", category = EnumCategory.CLIENT)
public class AntiProfanity extends BaseHack
{
    private Set<String> badWords;
    
    public AntiProfanity() {
        this.badWords = new HashSet<String>();
        try {
            URL v1 = new URL("https://raw.githubusercontent.com/RobertJGabriel/Google-profanity-words/master/list.txt");
            HttpURLConnection v2 = (HttpURLConnection)v1.openConnection();
            final int v3 = v2.getResponseCode();
            if (v3 == 301 || v3 == 302 || v3 == 303) {
                v1 = new URL(v2.getHeaderField("Location"));
                System.out.println("Redirected to " + v1 + " when fetching offsets");
                v2 = (HttpURLConnection)v1.openConnection();
                v2.setRequestProperty("Cookie", v2.getHeaderField("Set-Cookie"));
            }
            final BufferedReader v4 = new BufferedReader(new InputStreamReader(v2.getInputStream()));
            this.badWords = v4.lines().<Object>map((Function<? super String, ?>)String::toLowerCase).<Set<String>, ?>collect((Collector<? super Object, ?, Set<String>>)Collectors.<? super Object>toSet());
        }
        catch (Exception v5) {
            System.out.println("Failure downloading profanity word list");
            v5.printStackTrace();
        }
    }
    
    private String filterBadWords(String v2) {
        /*SL:63*/for (final String a1 : this.badWords) {
            /*SL:65*/v2 = v2.replace(a1, "[censored]");
        }
        /*SL:67*/return v2;
    }
    
    @SubscribeEvent
    public void renderString(final DrawString a1) {
        /*SL:73*/if (this.getEnabled()) {
            /*SL:75*/a1.text = this.filterBadWords(a1.text);
        }
    }
}
