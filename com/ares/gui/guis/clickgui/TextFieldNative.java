package com.ares.gui.guis.clickgui;

import java.awt.AWTEvent;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.JPanel;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class TextFieldNative extends JFrame implements ActionListener
{
    private JTextField textField;
    private JFrame frame;
    private JButton button;
    private JLabel label;
    private Consumer<String> callback;
    private String btnText;
    
    public TextFieldNative(final String a1, final String a2, final String a3, final Consumer<String> a4) {
        this.callback = a4;
        this.btnText = a3;
        this.frame = new JFrame(a1);
        this.label = new JLabel(a2);
        (this.button = new JButton(a3)).addActionListener(this);
        this.textField = new JTextField(16);
        final JPanel v1 = new JPanel();
        v1.add(this.textField);
        v1.add(this.button);
        v1.add(this.label);
        this.frame.add(v1);
        this.frame.setSize(300, 300);
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent a1) {
        /*SL:70*/if (a1.getActionCommand().equals(this.btnText)) {
            /*SL:72*/this.callback.accept(this.textField.getText());
            /*SL:73*/this.frame.dispatchEvent(new WindowEvent(this.frame, 201));
        }
    }
}
