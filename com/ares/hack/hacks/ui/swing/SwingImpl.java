package com.ares.hack.hacks.ui.swing;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import java.awt.Component;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.util.Iterator;
import com.ares.hack.hacks.BaseHack;
import com.ares.hack.hacks.HackManager;
import com.ares.event.client.gui.chat.AresChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import javax.swing.JScrollBar;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import java.awt.Dimension;
import net.minecraftforge.common.MinecraftForge;
import com.ares.Globals;
import com.ares.commands.Command;
import java.awt.Container;
import java.awt.Toolkit;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JFrame;

public class SwingImpl
{
    private JFrame frame;
    private JTextArea chat;
    private JTextField input;
    private JTextArea modulelist;
    private JPanel mainPanel;
    private JSplitPane splitPane;
    private JPanel consolePanel;
    private JPanel moduleListPanel;
    private JScrollPane consoleScrollPane;
    
    public SwingImpl() {
        this.$$$setupUI$$$();
        this.frame = new JFrame("SwingImpl");
        final Dimension v1 = Toolkit.getDefaultToolkit().getScreenSize();
        this.frame.setSize(v1.width / 2, v1.height / 2);
        this.frame.setContentPane(this.mainPanel);
        this.frame.pack();
        this.frame.setVisible(true);
        this.input.addActionListener(a1 -> {
            if (this.input.getText().startsWith(Command.commandPrefix)) {
                Command.processCommand(this.input.getText());
            }
            else {
                Globals.mc.field_71439_g.func_71165_d(this.input.getText());
            }
            this.input.setText("");
            return;
        });
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatReceivedEvent a1) {
        /*SL:52*/if (Swing.chat.getValue()) {
            /*SL:53*/this.chat.append(a1.getMessage().func_150260_c() + "\n");
        }
        final JScrollBar v1 = /*EL:55*/this.consoleScrollPane.getVerticalScrollBar();
        /*SL:56*/v1.setValue(v1.getMaximum());
    }
    
    @SubscribeEvent
    public void onBackdooredChat(final AresChatEvent a1) {
        /*SL:62*/this.chat.append(a1.txt.func_150260_c() + "\n");
        final JScrollBar v1 = /*EL:64*/this.consoleScrollPane.getVerticalScrollBar();
        /*SL:65*/v1.setValue(v1.getMaximum());
    }
    
    public void onUpdate() {
        final StringBuilder sb = /*EL:70*/new StringBuilder();
        /*SL:71*/for (final BaseHack v1 : HackManager.getAll()) {
            /*SL:73*/if (v1.getEnabled()) {
                /*SL:75*/sb.append(v1.name).append("\n");
            }
        }
        /*SL:78*/this.modulelist.setText(sb.toString());
    }
    
    public void close() {
        /*SL:83*/this.frame.setVisible(false);
        /*SL:84*/this.frame.dispose();
    }
    
    private void $$$setupUI$$$() {
        /*SL:103*/(this.mainPanel = new JPanel()).setLayout(/*EL:104*/new BorderLayout(0, 0));
        /*SL:105*/this.mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        /*SL:106*/this.splitPane = new JSplitPane();
        /*SL:107*/this.mainPanel.add(this.splitPane, "Center");
        /*SL:108*/(this.consolePanel = new JPanel()).setLayout(/*EL:109*/new BorderLayout(0, 0));
        /*SL:110*/this.splitPane.setLeftComponent(this.consolePanel);
        /*SL:111*/this.input = new JTextField();
        /*SL:112*/this.consolePanel.add(this.input, "South");
        /*SL:113*/(this.consoleScrollPane = new JScrollPane()).setHorizontalScrollBarPolicy(/*EL:114*/31);
        /*SL:115*/this.consoleScrollPane.setVerticalScrollBarPolicy(20);
        /*SL:116*/this.consolePanel.add(this.consoleScrollPane, "Center");
        /*SL:117*/(this.chat = new JTextArea()).setLineWrap(/*EL:118*/true);
        /*SL:119*/this.chat.setText("");
        /*SL:120*/this.consoleScrollPane.setViewportView(this.chat);
        /*SL:121*/(this.moduleListPanel = new JPanel()).setLayout(/*EL:122*/new BorderLayout(0, 0));
        /*SL:123*/this.splitPane.setRightComponent(this.moduleListPanel);
        /*SL:124*/this.modulelist = new JTextArea();
        /*SL:125*/this.moduleListPanel.add(this.modulelist, "Center");
    }
    
    public JComponent $$$getRootComponent$$$() {
        /*SL:133*/return this.mainPanel;
    }
}
