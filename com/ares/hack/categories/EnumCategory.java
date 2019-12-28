package com.ares.hack.categories;

public enum EnumCategory
{
    MOVEMENT("Movement"), 
    RENDER("Render"), 
    PLAYER("Player"), 
    COMBAT("Combat"), 
    MISC("Misc"), 
    EXPLOIT("Exploits"), 
    CLIENT("Client"), 
    UI("UIs"), 
    CHATBOT("ChatBot"), 
    HUD("Hud");
    
    public Category category;
    
    private EnumCategory(final String a1) {
        this.category = new Category(a1);
    }
}
