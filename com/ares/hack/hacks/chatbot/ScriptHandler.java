package com.ares.hack.hacks.chatbot;

import javax.script.Invocable;
import org.apache.logging.log4j.Logger;
import javax.script.ScriptException;
import java.util.Objects;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;

public class ScriptHandler
{
    private final ScriptEngine jsEngine;
    
    public ScriptHandler() {
        Objects.<ScriptEngine>requireNonNull(this.jsEngine = new ScriptEngineManager(null).getEngineByName("nashorn"));
    }
    
    public ScriptHandler eval(final String a1) throws ScriptException {
        /*SL:21*/this.jsEngine.eval(a1);
        /*SL:22*/return this;
    }
    
    public ScriptHandler addLogger(final Logger a1) {
        /*SL:27*/this.jsEngine.put("logger", a1);
        /*SL:28*/return this;
    }
    
    public Object invokeFunction(final String a1, final Object... a2) throws ScriptException, NoSuchMethodException {
        /*SL:33*/return ((Invocable)this.jsEngine).invokeFunction(a1, a2);
    }
}
