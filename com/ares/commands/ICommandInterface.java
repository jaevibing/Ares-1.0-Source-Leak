package com.ares.commands;

public interface ICommandInterface
{
    boolean execute(String[] p0);
    
    void getHelp(String[] p0, String p1);
    
    boolean checkCommandWithOption(String[] p0, String[] p1, String p2);
    
    String getSyntax();
}
