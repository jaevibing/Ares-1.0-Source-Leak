package com.ares.utils;

import net.minecraft.util.Session;

public class YggdrasilAuthenticator
{
    public YggdrasilPayload Payload;
    public String Username;
    public String Password;
    private Session session;
    
    public YggdrasilAuthenticator(final String a1, final String a2) {
        this.Payload = new YggdrasilPayload();
        this.Username = a1;
        this.Password = a2;
    }
    
    public boolean login() {
        /*SL:18*/if (this.Password == null || this.Password.equals("")) {
            final YggdrasilPayload payload = /*EL:26*/this.Payload;
            final Session v1 = YggdrasilPayload.loginCrack(this.Username);
            /*SL:27*/this.session = v1;
            /*SL:28*/return true;
        }
        final YggdrasilPayload payload2 = this.Payload;
        final Session v1 = YggdrasilPayload.loginPassword(this.Username, this.Password);
        if (v1 != null) {
            this.session = v1;
            return true;
        }
        /*SL:31*/return false;
    }
    
    public Session getSession() {
        /*SL:35*/return this.session;
    }
}
