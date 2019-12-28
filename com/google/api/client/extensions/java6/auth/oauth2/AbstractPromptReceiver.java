package com.google.api.client.extensions.java6.auth.oauth2;

import java.util.Scanner;

public abstract class AbstractPromptReceiver implements VerificationCodeReceiver
{
    public String waitForCode() {
        String v1;
        /*SL:39*/do {
            System.out.print("Please enter code: ");
            v1 = new Scanner(System.in).nextLine();
        } while (v1.isEmpty());
        /*SL:40*/return v1;
    }
    
    public void stop() {
    }
}
