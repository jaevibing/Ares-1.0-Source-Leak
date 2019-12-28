package com.ares.event.client.ares;

import com.ares.hack.hacks.BaseHack;
import java.util.List;
import com.ares.event.AresEvent;

public abstract class HacksInitialisation extends AresEvent
{
    public static class Pre extends HacksInitialisation
    {
    }
    
    public static class Post extends HacksInitialisation
    {
        public List<BaseHack> hacks;
        
        public Post(final List<BaseHack> a1) {
            this.hacks = a1;
        }
    }
}
