package org.spongepowered.asm.util;

import java.io.PrintStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.Map;
import com.google.common.base.Strings;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class PrettyPrinter
{
    private final HorizontalRule horizontalRule;
    private final List<Object> lines;
    private Table table;
    private boolean recalcWidth;
    protected int width;
    protected int wrapWidth;
    protected int kvKeyWidth;
    protected String kvFormat;
    
    public PrettyPrinter() {
        this(100);
    }
    
    public PrettyPrinter(final int a1) {
        this.horizontalRule = new HorizontalRule(new char[] { '*' });
        this.lines = new ArrayList<Object>();
        this.recalcWidth = false;
        this.width = 100;
        this.wrapWidth = 80;
        this.kvKeyWidth = 10;
        this.kvFormat = makeKvFormat(this.kvKeyWidth);
        this.width = a1;
    }
    
    public PrettyPrinter wrapTo(final int a1) {
        /*SL:434*/this.wrapWidth = a1;
        /*SL:435*/return this;
    }
    
    public int wrapTo() {
        /*SL:444*/return this.wrapWidth;
    }
    
    public PrettyPrinter table() {
        /*SL:453*/this.table = new Table();
        /*SL:454*/return this;
    }
    
    public PrettyPrinter table(final String... v2) {
        /*SL:464*/this.table = new Table();
        /*SL:465*/for (final String a1 : v2) {
            /*SL:466*/this.table.addColumn(a1);
        }
        /*SL:468*/return this;
    }
    
    public PrettyPrinter table(final Object... v-4) {
        /*SL:493*/this.table = new Table();
        Column column = /*EL:494*/null;
        /*SL:495*/for (final Object v1 : v-4) {
            /*SL:496*/if (v1 instanceof String) {
                /*SL:497*/column = this.table.addColumn((String)v1);
            }
            else/*SL:498*/ if (v1 instanceof Integer && column != null) {
                final int a1 = /*EL:499*/(int)v1;
                /*SL:500*/if (a1 > 0) {
                    /*SL:501*/column.setWidth(a1);
                }
                else/*SL:502*/ if (a1 < 0) {
                    /*SL:503*/column.setMaxWidth(-a1);
                }
            }
            else/*SL:505*/ if (v1 instanceof Alignment && column != null) {
                /*SL:506*/column.setAlignment((Alignment)v1);
            }
            else/*SL:507*/ if (v1 != null) {
                /*SL:508*/column = this.table.addColumn(v1.toString());
            }
        }
        /*SL:511*/return this;
    }
    
    public PrettyPrinter spacing(final int a1) {
        /*SL:521*/if (this.table == null) {
            /*SL:522*/this.table = new Table();
        }
        /*SL:524*/this.table.setColSpacing(a1);
        /*SL:525*/return this;
    }
    
    public PrettyPrinter th() {
        /*SL:535*/return this.th(false);
    }
    
    private PrettyPrinter th(final boolean a1) {
        /*SL:539*/if (this.table == null) {
            /*SL:540*/this.table = new Table();
        }
        /*SL:542*/if (!a1 || this.table.addHeader) {
            /*SL:543*/this.table.headerAdded();
            /*SL:544*/this.addLine(this.table);
        }
        /*SL:546*/return this;
    }
    
    public PrettyPrinter tr(final Object... a1) {
        /*SL:558*/this.th(true);
        /*SL:559*/this.addLine(this.table.addRow(a1));
        /*SL:560*/this.recalcWidth = true;
        /*SL:561*/return this;
    }
    
    public PrettyPrinter add() {
        /*SL:570*/this.addLine("");
        /*SL:571*/return this;
    }
    
    public PrettyPrinter add(final String a1) {
        /*SL:581*/this.addLine(a1);
        /*SL:582*/this.width = Math.max(this.width, a1.length());
        /*SL:583*/return this;
    }
    
    public PrettyPrinter add(final String a1, final Object... a2) {
        final String v1 = /*EL:595*/String.format(a1, a2);
        /*SL:596*/this.addLine(v1);
        /*SL:597*/this.width = Math.max(this.width, v1.length());
        /*SL:598*/return this;
    }
    
    public PrettyPrinter add(final Object[] a1) {
        /*SL:608*/return this.add(a1, "%s");
    }
    
    public PrettyPrinter add(final Object[] v1, final String v2) {
        /*SL:619*/for (final Object a1 : v1) {
            /*SL:620*/this.add(v2, a1);
        }
        /*SL:623*/return this;
    }
    
    public PrettyPrinter addIndexed(final Object[] v2) {
        final int v3 = /*EL:633*/String.valueOf(v2.length - 1).length();
        final String v4 = /*EL:634*/"[%" + v3 + "d] %s";
        /*SL:635*/for (int a1 = 0; a1 < v2.length; ++a1) {
            /*SL:636*/this.add(v4, a1, v2[a1]);
        }
        /*SL:639*/return this;
    }
    
    public PrettyPrinter addWithIndices(final Collection<?> a1) {
        /*SL:649*/return this.addIndexed(a1.toArray());
    }
    
    public PrettyPrinter add(final IPrettyPrintable a1) {
        /*SL:660*/if (a1 != null) {
            /*SL:661*/a1.print(this);
        }
        /*SL:663*/return this;
    }
    
    public PrettyPrinter add(final Throwable a1) {
        /*SL:674*/return this.add(a1, 4);
    }
    
    public PrettyPrinter add(Throwable a1, final int a2) {
        /*SL:686*/while (a1 != null) {
            /*SL:687*/this.add("%s: %s", a1.getClass().getName(), a1.getMessage());
            /*SL:688*/this.add(a1.getStackTrace(), a2);
            /*SL:689*/a1 = a1.getCause();
        }
        /*SL:691*/return this;
    }
    
    public PrettyPrinter add(final StackTraceElement[] v1, final int v2) {
        final String v3 = /*EL:703*/Strings.repeat(" ", v2);
        /*SL:704*/for (final StackTraceElement a1 : v1) {
            /*SL:705*/this.add("%s%s", v3, a1);
        }
        /*SL:707*/return this;
    }
    
    public PrettyPrinter add(final Object a1) {
        /*SL:717*/return this.add(a1, 0);
    }
    
    public PrettyPrinter add(final Object a1, final int a2) {
        final String v1 = /*EL:728*/Strings.repeat(" ", a2);
        /*SL:729*/return this.append(a1, a2, v1);
    }
    
    private PrettyPrinter append(final Object a3, final int v1, final String v2) {
        /*SL:733*/if (a3 instanceof String) {
            /*SL:734*/return this.add("%s%s", v2, a3);
        }
        /*SL:735*/if (a3 instanceof Iterable) {
            /*SL:736*/for (final Object a4 : (Iterable)a3) {
                /*SL:737*/this.append(a4, v1, v2);
            }
            /*SL:739*/return this;
        }
        /*SL:740*/if (a3 instanceof Map) {
            /*SL:741*/this.kvWidth(v1);
            /*SL:742*/return this.add((Map<?, ?>)a3);
        }
        /*SL:743*/if (a3 instanceof IPrettyPrintable) {
            /*SL:744*/return this.add((IPrettyPrintable)a3);
        }
        /*SL:745*/if (a3 instanceof Throwable) {
            /*SL:746*/return this.add((Throwable)a3, v1);
        }
        /*SL:747*/if (a3.getClass().isArray()) {
            /*SL:748*/return this.add((Object[])a3, v1 + "%s");
        }
        /*SL:750*/return this.add("%s%s", v2, a3);
    }
    
    public PrettyPrinter addWrapped(final String a1, final Object... a2) {
        /*SL:763*/return this.addWrapped(this.wrapWidth, a1, a2);
    }
    
    public PrettyPrinter addWrapped(final int v1, final String v2, final Object... v3) {
        String v4 = /*EL:777*/"";
        final String v5 = /*EL:778*/String.format(v2, v3).replace("\t", "    ");
        final Matcher v6 = /*EL:779*/Pattern.compile("^(\\s+)(.*)$").matcher(v5);
        /*SL:780*/if (v6.matches()) {
            /*SL:781*/v4 = v6.group(1);
        }
        try {
            /*SL:785*/for (final String a1 : this.getWrapped(v1, v5, v4)) {
                /*SL:786*/this.addLine(a1);
            }
        }
        catch (Exception a2) {
            /*SL:789*/this.add(v5);
        }
        /*SL:791*/return this;
    }
    
    private List<String> getWrapped(final int v1, String v2, final String v3) {
        final List<String> v4 = /*EL:795*/new ArrayList<String>();
        /*SL:797*/while (v2.length() > v1) {
            int a1 = /*EL:798*/v2.lastIndexOf(32, v1);
            /*SL:799*/if (a1 < 10) {
                /*SL:800*/a1 = v1;
            }
            final String a2 = /*EL:802*/v2.substring(0, a1);
            /*SL:803*/v4.add(a2);
            /*SL:804*/v2 = v3 + v2.substring(a1 + 1);
        }
        /*SL:807*/if (v2.length() > 0) {
            /*SL:808*/v4.add(v2);
        }
        /*SL:811*/return v4;
    }
    
    public PrettyPrinter kv(final String a1, final String a2, final Object... a3) {
        /*SL:823*/return this.kv(a1, (Object)String.format(a2, a3));
    }
    
    public PrettyPrinter kv(final String a1, final Object a2) {
        /*SL:834*/this.addLine(new KeyValue(a1, a2));
        /*SL:835*/return this.kvWidth(a1.length());
    }
    
    public PrettyPrinter kvWidth(final int a1) {
        /*SL:845*/if (a1 > this.kvKeyWidth) {
            /*SL:846*/this.kvKeyWidth = a1;
            /*SL:847*/this.kvFormat = makeKvFormat(a1);
        }
        /*SL:849*/this.recalcWidth = true;
        /*SL:850*/return this;
    }
    
    public PrettyPrinter add(final Map<?, ?> v-1) {
        /*SL:860*/for (final Map.Entry<?, ?> v1 : v-1.entrySet()) {
            final String a1 = /*EL:861*/(v1.getKey() == null) ? "null" : v1.getKey().toString();
            /*SL:862*/this.kv(a1, v1.getValue());
        }
        /*SL:864*/return this;
    }
    
    public PrettyPrinter hr() {
        /*SL:873*/return this.hr('*');
    }
    
    public PrettyPrinter hr(final char a1) {
        /*SL:884*/this.addLine(new HorizontalRule(new char[] { a1 }));
        /*SL:885*/return this;
    }
    
    public PrettyPrinter centre() {
        /*SL:894*/if (!this.lines.isEmpty()) {
            final Object v1 = /*EL:895*/this.lines.get(this.lines.size() - 1);
            /*SL:896*/if (v1 instanceof String) {
                /*SL:897*/this.addLine(new CentredText(this.lines.remove(this.lines.size() - 1)));
            }
        }
        /*SL:900*/return this;
    }
    
    private void addLine(final Object a1) {
        /*SL:904*/if (a1 == null) {
            /*SL:905*/return;
        }
        /*SL:907*/this.lines.add(a1);
        /*SL:908*/this.recalcWidth |= (a1 instanceof IVariableWidthEntry);
    }
    
    public PrettyPrinter trace() {
        /*SL:918*/return this.trace(getDefaultLoggerName());
    }
    
    public PrettyPrinter trace(final Level a1) {
        /*SL:929*/return this.trace(getDefaultLoggerName(), a1);
    }
    
    public PrettyPrinter trace(final String a1) {
        /*SL:940*/return this.trace(System.err, LogManager.getLogger(a1));
    }
    
    public PrettyPrinter trace(final String a1, final Level a2) {
        /*SL:952*/return this.trace(System.err, LogManager.getLogger(a1), a2);
    }
    
    public PrettyPrinter trace(final Logger a1) {
        /*SL:963*/return this.trace(System.err, a1);
    }
    
    public PrettyPrinter trace(final Logger a1, final Level a2) {
        /*SL:975*/return this.trace(System.err, a1, a2);
    }
    
    public PrettyPrinter trace(final PrintStream a1) {
        /*SL:986*/return this.trace(a1, getDefaultLoggerName());
    }
    
    public PrettyPrinter trace(final PrintStream a1, final Level a2) {
        /*SL:998*/return this.trace(a1, getDefaultLoggerName(), a2);
    }
    
    public PrettyPrinter trace(final PrintStream a1, final String a2) {
        /*SL:1010*/return this.trace(a1, LogManager.getLogger(a2));
    }
    
    public PrettyPrinter trace(final PrintStream a1, final String a2, final Level a3) {
        /*SL:1023*/return this.trace(a1, LogManager.getLogger(a2), a3);
    }
    
    public PrettyPrinter trace(final PrintStream a1, final Logger a2) {
        /*SL:1035*/return this.trace(a1, a2, Level.DEBUG);
    }
    
    public PrettyPrinter trace(final PrintStream a1, final Logger a2, final Level a3) {
        /*SL:1048*/this.log(a2, a3);
        /*SL:1049*/this.print(a1);
        /*SL:1050*/return this;
    }
    
    public PrettyPrinter print() {
        /*SL:1059*/return this.print(System.err);
    }
    
    public PrettyPrinter print(final PrintStream v2) {
        /*SL:1069*/this.updateWidth();
        /*SL:1070*/this.printSpecial(v2, this.horizontalRule);
        /*SL:1071*/for (final Object a1 : this.lines) {
            /*SL:1072*/if (a1 instanceof ISpecialEntry) {
                /*SL:1073*/this.printSpecial(v2, (ISpecialEntry)a1);
            }
            else {
                /*SL:1075*/this.printString(v2, a1.toString());
            }
        }
        /*SL:1078*/this.printSpecial(v2, this.horizontalRule);
        /*SL:1079*/return this;
    }
    
    private void printSpecial(final PrintStream a1, final ISpecialEntry a2) {
        /*SL:1083*/a1.printf("/*%s*/\n", a2.toString());
    }
    
    private void printString(final PrintStream a1, final String a2) {
        /*SL:1087*/if (a2 != null) {
            /*SL:1088*/a1.printf("/* %-" + this.width + "s */\n", a2);
        }
    }
    
    public PrettyPrinter log(final Logger a1) {
        /*SL:1099*/return this.log(a1, Level.INFO);
    }
    
    public PrettyPrinter log(final Logger v1, final Level v2) {
        /*SL:1110*/this.updateWidth();
        /*SL:1111*/this.logSpecial(v1, v2, this.horizontalRule);
        /*SL:1112*/for (final Object a1 : this.lines) {
            /*SL:1113*/if (a1 instanceof ISpecialEntry) {
                /*SL:1114*/this.logSpecial(v1, v2, (ISpecialEntry)a1);
            }
            else {
                /*SL:1116*/this.logString(v1, v2, a1.toString());
            }
        }
        /*SL:1119*/this.logSpecial(v1, v2, this.horizontalRule);
        /*SL:1120*/return this;
    }
    
    private void logSpecial(final Logger a1, final Level a2, final ISpecialEntry a3) {
        /*SL:1124*/a1.log(a2, "/*{}*/", new Object[] { a3.toString() });
    }
    
    private void logString(final Logger a1, final Level a2, final String a3) {
        /*SL:1128*/if (a3 != null) {
            /*SL:1129*/a1.log(a2, String.format("/* %-" + this.width + "s */", a3));
        }
    }
    
    private void updateWidth() {
        /*SL:1134*/if (this.recalcWidth) {
            /*SL:1135*/this.recalcWidth = false;
            /*SL:1136*/for (final Object v1 : this.lines) {
                /*SL:1137*/if (v1 instanceof IVariableWidthEntry) {
                    /*SL:1138*/this.width = Math.min(4096, Math.max(this.width, ((IVariableWidthEntry)v1).getWidth()));
                }
            }
        }
    }
    
    private static String makeKvFormat(final int a1) {
        /*SL:1145*/return String.format("%%%ds : %%s", a1);
    }
    
    private static String getDefaultLoggerName() {
        final String v1 = /*EL:1149*/new Throwable().getStackTrace()[2].getClassName();
        final int v2 = /*EL:1150*/v1.lastIndexOf(46);
        /*SL:1151*/return (v2 == -1) ? v1 : v1.substring(v2 + 1);
    }
    
    public static void dumpStack() {
        /*SL:1159*/new PrettyPrinter().add(new Exception("Stack trace")).print(System.err);
    }
    
    public static void print(final Throwable a1) {
        /*SL:1168*/new PrettyPrinter().add(a1).print(System.err);
    }
    
    class KeyValue implements IVariableWidthEntry
    {
        private final String key;
        private final Object value;
        
        public KeyValue(final String a2, final Object a3) {
            this.key = a2;
            this.value = a3;
        }
        
        @Override
        public String toString() {
            /*SL:92*/return String.format(PrettyPrinter.this.kvFormat, this.key, this.value);
        }
        
        @Override
        public int getWidth() {
            /*SL:97*/return this.toString().length();
        }
    }
    
    class HorizontalRule implements ISpecialEntry
    {
        private final char[] hrChars;
        
        public HorizontalRule(final char... a2) {
            this.hrChars = a2;
        }
        
        @Override
        public String toString() {
            /*SL:115*/return Strings.repeat(new String(this.hrChars), PrettyPrinter.this.width + 2);
        }
    }
    
    class CentredText
    {
        private final Object centred;
        
        public CentredText(final Object a2) {
            this.centred = a2;
        }
        
        @Override
        public String toString() {
            final String v1 = /*EL:133*/this.centred.toString();
            /*SL:134*/return String.format("%" + ((PrettyPrinter.this.width - v1.length()) / 2 + v1.length()) + "s", v1);
        }
    }
    
    public enum Alignment
    {
        LEFT, 
        RIGHT;
    }
    
    static class Table implements IVariableWidthEntry
    {
        final List<Column> columns;
        final List<Row> rows;
        String format;
        int colSpacing;
        boolean addHeader;
        
        Table() {
            this.columns = new ArrayList<Column>();
            this.rows = new ArrayList<Row>();
            this.format = "%s";
            this.colSpacing = 2;
            this.addHeader = true;
        }
        
        void headerAdded() {
            /*SL:163*/this.addHeader = false;
        }
        
        void setColSpacing(final int a1) {
            /*SL:167*/this.colSpacing = Math.max(0, a1);
            /*SL:168*/this.updateFormat();
        }
        
        Table grow(final int a1) {
            /*SL:172*/while (this.columns.size() < a1) {
                /*SL:173*/this.columns.add(new Column(this));
            }
            /*SL:175*/this.updateFormat();
            /*SL:176*/return this;
        }
        
        Column add(final Column a1) {
            /*SL:180*/this.columns.add(a1);
            /*SL:181*/return a1;
        }
        
        Row add(final Row a1) {
            /*SL:185*/this.rows.add(a1);
            /*SL:186*/return a1;
        }
        
        Column addColumn(final String a1) {
            /*SL:190*/return this.add(new Column(this, a1));
        }
        
        Column addColumn(final Alignment a1, final int a2, final String a3) {
            /*SL:194*/return this.add(new Column(this, a1, a2, a3));
        }
        
        Row addRow(final Object... a1) {
            /*SL:198*/return this.add(new Row(this, a1));
        }
        
        void updateFormat() {
            final String repeat = /*EL:202*/Strings.repeat(" ", this.colSpacing);
            final StringBuilder sb = /*EL:203*/new StringBuilder();
            boolean b = /*EL:204*/false;
            /*SL:205*/for (final Column v1 : this.columns) {
                /*SL:206*/if (b) {
                    /*SL:207*/sb.append(repeat);
                }
                /*SL:209*/b = true;
                /*SL:210*/sb.append(v1.getFormat());
            }
            /*SL:212*/this.format = sb.toString();
        }
        
        String getFormat() {
            /*SL:216*/return this.format;
        }
        
        Object[] getTitles() {
            final List<Object> list = /*EL:220*/new ArrayList<Object>();
            /*SL:221*/for (final Column v1 : this.columns) {
                /*SL:222*/list.add(v1.getTitle());
            }
            /*SL:224*/return list.toArray();
        }
        
        @Override
        public String toString() {
            boolean b = /*EL:229*/false;
            final String[] v0 = /*EL:230*/new String[this.columns.size()];
            /*SL:231*/for (int v = 0; v < this.columns.size(); ++v) {
                /*SL:232*/v0[v] = this.columns.get(v).toString();
                /*SL:233*/b |= !v0[v].isEmpty();
            }
            /*SL:235*/return b ? String.format(this.format, (Object[])v0) : null;
        }
        
        @Override
        public int getWidth() {
            final String v1 = /*EL:240*/this.toString();
            /*SL:241*/return (v1 != null) ? v1.length() : 0;
        }
    }
    
    static class Column
    {
        private final Table table;
        private Alignment align;
        private int minWidth;
        private int maxWidth;
        private int size;
        private String title;
        private String format;
        
        Column(final Table a1) {
            this.align = Alignment.LEFT;
            this.minWidth = 1;
            this.maxWidth = Integer.MAX_VALUE;
            this.size = 0;
            this.title = "";
            this.format = "%s";
            this.table = a1;
        }
        
        Column(final Table a1, final String a2) {
            this(a1);
            this.title = a2;
            this.minWidth = a2.length();
            this.updateFormat();
        }
        
        Column(final Table a1, final Alignment a2, final int a3, final String a4) {
            this(a1, a4);
            this.align = a2;
            this.size = a3;
        }
        
        void setAlignment(final Alignment a1) {
            /*SL:283*/this.align = a1;
            /*SL:284*/this.updateFormat();
        }
        
        void setWidth(final int a1) {
            /*SL:288*/if (a1 > this.size) {
                /*SL:289*/this.size = a1;
                /*SL:290*/this.updateFormat();
            }
        }
        
        void setMinWidth(final int a1) {
            /*SL:295*/if (a1 > this.minWidth) {
                /*SL:296*/this.minWidth = a1;
                /*SL:297*/this.updateFormat();
            }
        }
        
        void setMaxWidth(final int a1) {
            /*SL:302*/this.size = Math.min(this.size, this.maxWidth);
            /*SL:303*/this.maxWidth = Math.max(1, a1);
            /*SL:304*/this.updateFormat();
        }
        
        void setTitle(final String a1) {
            /*SL:308*/this.title = a1;
            /*SL:309*/this.setWidth(a1.length());
        }
        
        private void updateFormat() {
            final int v1 = /*EL:313*/Math.min(this.maxWidth, (this.size == 0) ? this.minWidth : this.size);
            /*SL:314*/this.format = "%" + ((this.align == Alignment.RIGHT) ? "" : "-") + v1 + "s";
            /*SL:315*/this.table.updateFormat();
        }
        
        int getMaxWidth() {
            /*SL:319*/return this.maxWidth;
        }
        
        String getTitle() {
            /*SL:323*/return this.title;
        }
        
        String getFormat() {
            /*SL:327*/return this.format;
        }
        
        @Override
        public String toString() {
            /*SL:332*/if (this.title.length() > this.maxWidth) {
                /*SL:333*/return this.title.substring(0, this.maxWidth);
            }
            /*SL:336*/return this.title;
        }
    }
    
    static class Row implements IVariableWidthEntry
    {
        final Table table;
        final String[] args;
        
        public Row(final Table v1, final Object... v2) {
            this.table = v1.grow(v2.length);
            this.args = new String[v2.length];
            for (int a1 = 0; a1 < v2.length; ++a1) {
                this.args[a1] = v2[a1].toString();
                this.table.columns.get(a1).setMinWidth(this.args[a1].length());
            }
        }
        
        @Override
        public String toString() {
            final Object[] array = /*EL:361*/new Object[this.table.columns.size()];
            /*SL:362*/for (int v0 = 0; v0 < array.length; ++v0) {
                final Column v = /*EL:363*/this.table.columns.get(v0);
                /*SL:364*/if (v0 >= this.args.length) {
                    /*SL:365*/array[v0] = "";
                }
                else {
                    /*SL:367*/array[v0] = ((this.args[v0].length() > v.getMaxWidth()) ? this.args[v0].substring(0, v.getMaxWidth()) : this.args[v0]);
                }
            }
            /*SL:371*/return String.format(this.table.format, array);
        }
        
        @Override
        public int getWidth() {
            /*SL:376*/return this.toString().length();
        }
    }
    
    interface IVariableWidthEntry
    {
        int getWidth();
    }
    
    interface ISpecialEntry
    {
    }
    
    public interface IPrettyPrintable
    {
        void print(PrettyPrinter p0);
    }
}
