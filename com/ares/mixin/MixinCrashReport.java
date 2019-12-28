package com.ares.mixin;

import org.spongepowered.asm.mixin.injection.Inject;
import javax.swing.JOptionPane;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.google.common.base.Throwables;
import com.ares.api.HasteBinApi;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import java.awt.Frame;
import com.ares.Globals;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.io.File;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.ares.DrmManager;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ CrashReport.class })
public class MixinCrashReport
{
    @Final
    @Shadow
    private Throwable field_71511_b;
    
    @Redirect(method = { "getCompleteReport" }, at = @At(value = "INVOKE", target = "Ljava/lang/StringBuilder;toString()Ljava/lang/String;"))
    public String interceptReport(final StringBuilder a1) {
        try {
            /*SL:33*/return DrmManager.handleCrash(a1);
        }
        catch (Throwable t) {
            /*SL:35*/return a1.toString();
        }
    }
    
    @Inject(method = { "saveToFile" }, at = { @At("RETURN") })
    private void showDialog(final File v-7, final CallbackInfoReturnable<Boolean> v-6) {
        /*SL:41*/if (Globals.mc.func_71372_G()) {
            Globals.mc.func_71352_k();
        }
        final Frame frame = /*EL:44*/new Frame();
        /*SL:46*/frame.setAlwaysOnTop(true);
        /*SL:47*/frame.setState(1);
        final JPanel panel = /*EL:49*/new JPanel();
        /*SL:51*/panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        /*SL:52*/panel.setLayout(new BorderLayout(0, 0));
        final String func_71502_e = /*EL:54*/((CrashReport)this).func_71502_e();
        String message = null;
        try {
            final String a1 = /*EL:59*/HasteBinApi.uploadImpl("http://paste.dimdev.org", "mccrash", func_71502_e);
        }
        catch (Exception a2) {
            /*SL:67*/message = a2.getMessage();
        }
        final JTextArea textArea = /*EL:70*/new JTextArea("Uploaded crash report: " + message + "\n" + /*EL:72*/Throwables.getStackTraceAsString(this.field_71511_b));
        /*SL:74*/textArea.setEditable(false);
        final JScrollPane v0 = /*EL:75*/new JScrollPane(textArea, 22, 32);
        /*SL:76*/panel.add(v0);
        StackTraceElement v;
        /*SL:79*/if (this.field_71511_b.getStackTrace().length > 0) {
            /*SL:80*/v = this.field_71511_b.getStackTrace()[0];
        }
        else {
            /*SL:82*/v = new StackTraceElement("", "", "", -1);
        }
        /*SL:84*/JOptionPane.showMessageDialog(frame, panel, "ERROR encountered at " + v.toString(), /*EL:87*/0);
        /*SL:91*/frame.requestFocus();
    }
}
