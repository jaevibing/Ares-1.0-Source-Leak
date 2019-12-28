package javassist.tools.web;

import java.io.IOException;
import java.net.Socket;

class ServiceThread extends Thread
{
    Webserver web;
    Socket sock;
    
    public ServiceThread(final Webserver a1, final Socket a2) {
        this.web = a1;
        this.sock = a2;
    }
    
    @Override
    public void run() {
        try {
            /*SL:403*/this.web.process(this.sock);
        }
        catch (IOException ex) {}
    }
}
