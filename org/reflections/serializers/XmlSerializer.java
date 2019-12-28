package org.reflections.serializers;

import org.reflections.Store;
import org.dom4j.DocumentFactory;
import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;
import java.io.OutputStream;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;
import java.io.FileOutputStream;
import org.reflections.util.Utils;
import java.io.File;
import java.util.Iterator;
import org.dom4j.Document;
import java.lang.reflect.Constructor;
import org.dom4j.DocumentException;
import org.reflections.ReflectionsException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.reflections.Configuration;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.Reflections;
import java.io.InputStream;

public class XmlSerializer implements Serializer
{
    @Override
    public Reflections read(final InputStream v0) {
        Reflections v;
        try {
            final Constructor<Reflections> a1 = /*EL:40*/Reflections.class.getDeclaredConstructor((Class<?>[])new Class[0]);
            /*SL:41*/a1.setAccessible(true);
            /*SL:42*/v = a1.newInstance(new Object[0]);
        }
        catch (Exception v13) {
            /*SL:44*/v = new Reflections(new ConfigurationBuilder());
        }
        try {
            final Document v2 = /*EL:48*/new SAXReader().read(v0);
            /*SL:49*/for (final Object v3 : v2.getRootElement().elements()) {
                final Element v4 = /*EL:50*/(Element)v3;
                /*SL:51*/for (final Object v5 : v4.elements()) {
                    final Element v6 = /*EL:52*/(Element)v5;
                    final Element v7 = /*EL:53*/v6.element("key");
                    final Element v8 = /*EL:54*/v6.element("values");
                    /*SL:55*/for (final Object v9 : v8.elements()) {
                        final Element v10 = /*EL:56*/(Element)v9;
                        /*SL:57*/v.getStore().getOrCreate(v4.getName()).put(v7.getText(), v10.getText());
                    }
                }
            }
        }
        catch (DocumentException v11) {
            /*SL:62*/throw new ReflectionsException("could not read.", (Throwable)v11);
        }
        catch (Throwable v12) {
            /*SL:64*/throw new RuntimeException("Could not read. Make sure relevant dependencies exist on classpath.", v12);
        }
        /*SL:67*/return v;
    }
    
    @Override
    public File save(final Reflections v-2, final String v-1) {
        final File v0 = /*EL:71*/Utils.prepareFile(v-1);
        try {
            final Document a1 = /*EL:75*/this.createDocument(v-2);
            final XMLWriter a2 = /*EL:76*/new XMLWriter((OutputStream)new FileOutputStream(v0), OutputFormat.createPrettyPrint());
            /*SL:77*/a2.write(a1);
            /*SL:78*/a2.close();
        }
        catch (IOException v) {
            /*SL:80*/throw new ReflectionsException("could not save to file " + v-1, v);
        }
        catch (Throwable v2) {
            /*SL:82*/throw new RuntimeException("Could not save to file " + v-1 + ". Make sure relevant dependencies exist on classpath.", v2);
        }
        /*SL:85*/return v0;
    }
    
    @Override
    public String toString(final Reflections v-2) {
        final Document document = /*EL:89*/this.createDocument(v-2);
        try {
            final StringWriter a1 = /*EL:92*/new StringWriter();
            final XMLWriter v1 = /*EL:93*/new XMLWriter((Writer)a1, OutputFormat.createPrettyPrint());
            /*SL:94*/v1.write(document);
            /*SL:95*/v1.close();
            /*SL:96*/return a1.toString();
        }
        catch (IOException v2) {
            /*SL:98*/throw new RuntimeException();
        }
    }
    
    private Document createDocument(final Reflections v-8) {
        final Store store = /*EL:103*/v-8.getStore();
        final Document document = /*EL:105*/DocumentFactory.getInstance().createDocument();
        final Element addElement = /*EL:106*/document.addElement("Reflections");
        /*SL:107*/for (final String s : store.keySet()) {
            final Element addElement2 = /*EL:108*/addElement.addElement(s);
            /*SL:109*/for (final String v0 : store.get(s).keySet()) {
                final Element v = /*EL:110*/addElement2.addElement("entry");
                /*SL:111*/v.addElement("key").setText(v0);
                final Element v2 = /*EL:112*/v.addElement("values");
                /*SL:113*/for (final String a1 : store.get(s).get(v0)) {
                    /*SL:114*/v2.addElement("value").setText(a1);
                }
            }
        }
        /*SL:118*/return document;
    }
}
