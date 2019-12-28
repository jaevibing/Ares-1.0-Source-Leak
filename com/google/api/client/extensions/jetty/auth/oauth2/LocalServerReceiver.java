package com.google.api.client.extensions.jetty.auth.oauth2;

import java.io.PrintWriter;
import org.mortbay.jetty.Request;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.Connector;
import java.io.IOException;
import com.google.api.client.util.Throwables;
import org.mortbay.jetty.Handler;
import java.util.concurrent.Semaphore;
import org.mortbay.jetty.Server;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

public final class LocalServerReceiver implements VerificationCodeReceiver
{
    private static final String LOCALHOST = "localhost";
    private static final String CALLBACK_PATH = "/Callback";
    private Server server;
    String code;
    String error;
    final Semaphore waitUnlessSignaled;
    private int port;
    private final String host;
    private final String callbackPath;
    private String successLandingPageUrl;
    private String failureLandingPageUrl;
    
    public LocalServerReceiver() {
        this("localhost", -1, "/Callback", null, null);
    }
    
    LocalServerReceiver(final String a1, final int a2, final String a3, final String a4) {
        this(a1, a2, "/Callback", a3, a4);
    }
    
    LocalServerReceiver(final String a1, final int a2, final String a3, final String a4, final String a5) {
        this.waitUnlessSignaled = new Semaphore(0);
        this.host = a1;
        this.port = a2;
        this.callbackPath = a3;
        this.successLandingPageUrl = a4;
        this.failureLandingPageUrl = a5;
    }
    
    public String getRedirectUri() throws IOException {
        /*SL:118*/this.server = new Server((this.port != -1) ? this.port : 0);
        final Connector v0 = /*EL:119*/this.server.getConnectors()[0];
        /*SL:120*/v0.setHost(this.host);
        /*SL:121*/this.server.addHandler((Handler)new CallbackHandler());
        try {
            /*SL:123*/this.server.start();
            /*SL:124*/this.port = v0.getLocalPort();
        }
        catch (Exception v) {
            /*SL:126*/Throwables.propagateIfPossible(v);
            /*SL:127*/throw new IOException(v);
        }
        final String value = /*EL:129*/String.valueOf(String.valueOf(this.host));
        final int port = this.port;
        final String value2 = String.valueOf(String.valueOf(this.callbackPath));
        return new StringBuilder(19 + value.length() + value2.length()).append("http://").append(value).append(":").append(port).append(value2).toString();
    }
    
    public String waitForCode() throws IOException {
        /*SL:143*/this.waitUnlessSignaled.acquireUninterruptibly();
        /*SL:144*/if (this.error != null) {
            final String value = /*EL:145*/String.valueOf(String.valueOf(this.error));
            throw new IOException(new StringBuilder(28 + value.length()).append("User authorization failed (").append(value).append(")").toString());
        }
        /*SL:147*/return this.code;
    }
    
    public void stop() throws IOException {
        /*SL:152*/this.waitUnlessSignaled.release();
        /*SL:153*/if (this.server != null) {
            try {
                /*SL:155*/this.server.stop();
            }
            catch (Exception v1) {
                /*SL:157*/Throwables.propagateIfPossible(v1);
                /*SL:158*/throw new IOException(v1);
            }
            /*SL:160*/this.server = null;
        }
    }
    
    public String getHost() {
        /*SL:166*/return this.host;
    }
    
    public int getPort() {
        /*SL:173*/return this.port;
    }
    
    public String getCallbackPath() {
        /*SL:180*/return this.callbackPath;
    }
    
    public static final class Builder
    {
        private String host;
        private int port;
        private String successLandingPageUrl;
        private String failureLandingPageUrl;
        private String callbackPath;
        
        public Builder() {
            this.host = "localhost";
            this.port = -1;
            this.callbackPath = "/Callback";
        }
        
        public LocalServerReceiver build() {
            /*SL:205*/return new LocalServerReceiver(this.host, this.port, this.callbackPath, this.successLandingPageUrl, this.failureLandingPageUrl);
        }
        
        public String getHost() {
            /*SL:211*/return this.host;
        }
        
        public Builder setHost(final String a1) {
            /*SL:216*/this.host = a1;
            /*SL:217*/return this;
        }
        
        public int getPort() {
            /*SL:222*/return this.port;
        }
        
        public Builder setPort(final int a1) {
            /*SL:227*/this.port = a1;
            /*SL:228*/return this;
        }
        
        public String getCallbackPath() {
            /*SL:233*/return this.callbackPath;
        }
        
        public Builder setCallbackPath(final String a1) {
            /*SL:238*/this.callbackPath = a1;
            /*SL:239*/return this;
        }
        
        public Builder setLandingPages(final String a1, final String a2) {
            /*SL:243*/this.successLandingPageUrl = a1;
            /*SL:244*/this.failureLandingPageUrl = a2;
            /*SL:245*/return this;
        }
    }
    
    class CallbackHandler extends AbstractHandler
    {
        public void handle(final String a1, final HttpServletRequest a2, final HttpServletResponse a3, final int a4) throws IOException {
            /*SL:259*/if (!"/Callback".equals(a1)) {
                /*SL:260*/return;
            }
            try {
                /*SL:264*/((Request)a2).setHandled(true);
                /*SL:265*/LocalServerReceiver.this.error = a2.getParameter("error");
                /*SL:266*/LocalServerReceiver.this.code = a2.getParameter("code");
                /*SL:268*/if (LocalServerReceiver.this.error == null && LocalServerReceiver.this.successLandingPageUrl != null) {
                    /*SL:269*/a3.sendRedirect(LocalServerReceiver.this.successLandingPageUrl);
                }
                else/*SL:270*/ if (LocalServerReceiver.this.error != null && LocalServerReceiver.this.failureLandingPageUrl != null) {
                    /*SL:271*/a3.sendRedirect(LocalServerReceiver.this.failureLandingPageUrl);
                }
                else {
                    /*SL:273*/this.writeLandingHtml(a3);
                }
                /*SL:275*/a3.flushBuffer();
            }
            finally {
                /*SL:278*/LocalServerReceiver.this.waitUnlessSignaled.release();
            }
        }
        
        private void writeLandingHtml(final HttpServletResponse a1) throws IOException {
            /*SL:283*/a1.setStatus(200);
            /*SL:284*/a1.setContentType("text/html");
            final PrintWriter v1 = /*EL:286*/a1.getWriter();
            /*SL:287*/v1.println("<html>");
            /*SL:288*/v1.println("<head><title>OAuth 2.0 Authentication Token Received</title></head>");
            /*SL:289*/v1.println("<body>");
            /*SL:290*/v1.println("Received verification code. You may now close this window.");
            /*SL:291*/v1.println("</body>");
            /*SL:292*/v1.println("</html>");
            /*SL:293*/v1.flush();
        }
    }
}
