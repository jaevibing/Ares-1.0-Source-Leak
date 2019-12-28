package com.ares.hack.hacks.chatbot;

import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.script.ScriptException;
import org.apache.logging.log4j.Logger;

public class ChatBotScriptHandler
{
    static final String FILENAME = "Ares/chatbot.js";
    ScriptHandler scriptHandler;
    static final Logger logger;
    
    ChatBotScriptHandler() throws Exception {
        this.scriptHandler = new ScriptHandler().eval(getScriptContents("Ares/chatbot.js")).addLogger(ChatBotScriptHandler.logger);
    }
    
    String onChatRecieved(final String a1, final String a2) throws ScriptException, NoSuchMethodException, IllegalStateException {
        /*SL:25*/return (String)this.scriptHandler.invokeFunction("onChatRecieved", a1, a2);
    }
    
    public static String getScriptContents(final String v-1) throws IOException {
        try {
            final StringBuilder a1 = /*EL:32*/new StringBuilder();
            final BufferedReader v1 = /*EL:34*/new BufferedReader(new FileReader(v-1));
            String v2;
            /*SL:37*/while ((v2 = v1.readLine()) != null) {
                /*SL:38*/a1.append(v2);
            }
            /*SL:40*/v1.close();
            ChatBotScriptHandler.logger.info(/*EL:42*/"Successfully read chatbot script");
            /*SL:44*/return a1.toString();
        }
        catch (FileNotFoundException v4) {
            final File v3 = /*EL:48*/new File(v-1);
            try {
                /*SL:52*/v3.createNewFile();
            }
            catch (Exception ex) {}
            /*SL:56*/throw new IllegalStateException("Could not find chatbot script file");
        }
    }
    
    static {
        logger = LogManager.getLogger("BackdooredChatBot");
    }
}
