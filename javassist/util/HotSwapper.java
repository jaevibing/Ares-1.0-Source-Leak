package javassist.util;

import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import java.util.Set;
import com.sun.jdi.ReferenceType;
import java.util.HashMap;
import com.sun.jdi.request.EventRequest;
import java.util.Iterator;
import java.util.List;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import java.io.IOException;
import java.util.Map;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.VirtualMachine;

public class HotSwapper
{
    private VirtualMachine jvm;
    private MethodEntryRequest request;
    private Map newClassFiles;
    private Trigger trigger;
    private static final String HOST_NAME = "localhost";
    private static final String TRIGGER_NAME;
    
    public HotSwapper(final int a1) throws IOException, IllegalConnectorArgumentsException {
        this(Integer.toString(a1));
    }
    
    public HotSwapper(final String a1) throws IOException, IllegalConnectorArgumentsException {
        this.jvm = null;
        this.request = null;
        this.newClassFiles = null;
        this.trigger = new Trigger();
        final AttachingConnector v1 = (AttachingConnector)this.findConnector("com.sun.jdi.SocketAttach");
        final Map v2 = v1.defaultArguments();
        v2.get("hostname").setValue("localhost");
        v2.get("port").setValue(a1);
        this.jvm = v1.attach(v2);
        final EventRequestManager v3 = this.jvm.eventRequestManager();
        this.request = methodEntryRequests(v3, HotSwapper.TRIGGER_NAME);
    }
    
    private Connector findConnector(final String v2) throws IOException {
        final List v3 = /*EL:120*/Bootstrap.virtualMachineManager().allConnectors();
        /*SL:121*/for (final Connector a1 : v3) {
            /*SL:124*/if (a1.name().equals(v2)) {
                /*SL:125*/return a1;
            }
        }
        /*SL:129*/throw new IOException("Not found: " + v2);
    }
    
    private static MethodEntryRequest methodEntryRequests(final EventRequestManager a1, final String a2) {
        final MethodEntryRequest v1 = /*EL:135*/a1.createMethodEntryRequest();
        /*SL:136*/v1.addClassFilter(a2);
        /*SL:137*/v1.setSuspendPolicy(1);
        /*SL:138*/return v1;
    }
    
    private void deleteEventRequest(final EventRequestManager a1, final MethodEntryRequest a2) {
        /*SL:145*/a1.deleteEventRequest((EventRequest)a2);
    }
    
    public void reload(final String a1, final byte[] a2) {
        final ReferenceType v1 = /*EL:155*/this.toRefType(a1);
        final Map v2 = /*EL:156*/new HashMap();
        /*SL:157*/v2.put(v1, a2);
        /*SL:158*/this.reload2(v2, a1);
    }
    
    public void reload(final Map v2) {
        final Set v3 = /*EL:170*/v2.entrySet();
        final Iterator v4 = /*EL:171*/v3.iterator();
        final Map v5 = /*EL:172*/new HashMap();
        String v6 = /*EL:173*/null;
        /*SL:174*/while (v4.hasNext()) {
            final Map.Entry a1 = /*EL:175*/v4.next();
            /*SL:176*/v6 = a1.getKey();
            /*SL:177*/v5.put(this.toRefType(v6), a1.getValue());
        }
        /*SL:180*/if (v6 != null) {
            /*SL:181*/this.reload2(v5, v6 + " etc.");
        }
    }
    
    private ReferenceType toRefType(final String a1) {
        final List v1 = /*EL:185*/this.jvm.classesByName(a1);
        /*SL:186*/if (v1 == null || v1.isEmpty()) {
            /*SL:187*/throw new RuntimeException("no such class: " + a1);
        }
        /*SL:189*/return v1.get(0);
    }
    
    private void reload2(final Map v1, final String v2) {
        /*SL:193*/synchronized (this.trigger) {
            /*SL:194*/this.startDaemon();
            /*SL:195*/this.newClassFiles = v1;
            /*SL:196*/this.request.enable();
            /*SL:197*/this.trigger.doSwap();
            /*SL:198*/this.request.disable();
            final Map a1 = /*EL:199*/this.newClassFiles;
            /*SL:200*/if (a1 != null) {
                /*SL:201*/this.newClassFiles = null;
                /*SL:202*/throw new RuntimeException("failed to reload: " + v2);
            }
        }
    }
    
    private void startDaemon() {
        /*SL:208*/new Thread() {
            private void errorMsg(final Throwable a1) {
                System.err.print(/*EL:210*/"Exception in thread \"HotSwap\" ");
                /*SL:211*/a1.printStackTrace(System.err);
            }
            
            @Override
            public void run() {
                EventSet waitEvent = /*EL:215*/null;
                try {
                    /*SL:217*/waitEvent = HotSwapper.this.waitEvent();
                    final EventIterator v0 = /*EL:218*/waitEvent.eventIterator();
                    /*SL:219*/while (v0.hasNext()) {
                        final Event v = /*EL:220*/v0.nextEvent();
                        /*SL:221*/if (v instanceof MethodEntryEvent) {
                            /*SL:222*/HotSwapper.this.hotswap();
                            /*SL:223*/break;
                        }
                    }
                }
                catch (Throwable v2) {
                    /*SL:228*/this.errorMsg(v2);
                }
                try {
                    /*SL:231*/if (waitEvent != null) {
                        /*SL:232*/waitEvent.resume();
                    }
                }
                catch (Throwable v2) {
                    /*SL:235*/this.errorMsg(v2);
                }
            }
        }.start();
    }
    
    EventSet waitEvent() throws InterruptedException {
        final EventQueue v1 = /*EL:242*/this.jvm.eventQueue();
        /*SL:243*/return v1.remove();
    }
    
    void hotswap() {
        final Map v1 = /*EL:247*/this.newClassFiles;
        /*SL:248*/this.jvm.redefineClasses(v1);
        /*SL:249*/this.newClassFiles = null;
    }
    
    static {
        TRIGGER_NAME = Trigger.class.getName();
    }
}
