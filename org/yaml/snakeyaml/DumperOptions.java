package org.yaml.snakeyaml;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.serializer.NumberAnchorGenerator;
import org.yaml.snakeyaml.serializer.AnchorGenerator;
import java.util.Map;
import java.util.TimeZone;

public class DumperOptions
{
    private ScalarStyle defaultStyle;
    private FlowStyle defaultFlowStyle;
    private boolean canonical;
    private boolean allowUnicode;
    private boolean allowReadOnlyProperties;
    private int indent;
    private int indicatorIndent;
    private int bestWidth;
    private boolean splitLines;
    private LineBreak lineBreak;
    private boolean explicitStart;
    private boolean explicitEnd;
    private TimeZone timeZone;
    private Version version;
    private Map<String, String> tags;
    private Boolean prettyFlow;
    private AnchorGenerator anchorGenerator;
    
    public DumperOptions() {
        this.defaultStyle = ScalarStyle.PLAIN;
        this.defaultFlowStyle = FlowStyle.AUTO;
        this.canonical = false;
        this.allowUnicode = true;
        this.allowReadOnlyProperties = false;
        this.indent = 2;
        this.indicatorIndent = 0;
        this.bestWidth = 80;
        this.splitLines = true;
        this.lineBreak = LineBreak.UNIX;
        this.explicitStart = false;
        this.explicitEnd = false;
        this.timeZone = null;
        this.version = null;
        this.tags = null;
        this.prettyFlow = false;
        this.anchorGenerator = new NumberAnchorGenerator(0);
    }
    
    public boolean isAllowUnicode() {
        /*SL:180*/return this.allowUnicode;
    }
    
    public void setAllowUnicode(final boolean a1) {
        /*SL:194*/this.allowUnicode = a1;
    }
    
    public ScalarStyle getDefaultScalarStyle() {
        /*SL:198*/return this.defaultStyle;
    }
    
    public void setDefaultScalarStyle(final ScalarStyle a1) {
        /*SL:209*/if (a1 == null) {
            /*SL:210*/throw new NullPointerException("Use ScalarStyle enum.");
        }
        /*SL:212*/this.defaultStyle = a1;
    }
    
    public void setIndent(final int a1) {
        /*SL:216*/if (a1 < 1) {
            /*SL:217*/throw new YAMLException("Indent must be at least 1");
        }
        /*SL:219*/if (a1 > 10) {
            /*SL:220*/throw new YAMLException("Indent must be at most 10");
        }
        /*SL:222*/this.indent = a1;
    }
    
    public int getIndent() {
        /*SL:226*/return this.indent;
    }
    
    public void setIndicatorIndent(final int a1) {
        /*SL:230*/if (a1 < 0) {
            /*SL:231*/throw new YAMLException("Indicator indent must be non-negative.");
        }
        /*SL:233*/if (a1 > 9) {
            /*SL:234*/throw new YAMLException("Indicator indent must be at most Emitter.MAX_INDENT-1: 9");
        }
        /*SL:236*/this.indicatorIndent = a1;
    }
    
    public int getIndicatorIndent() {
        /*SL:240*/return this.indicatorIndent;
    }
    
    public void setVersion(final Version a1) {
        /*SL:244*/this.version = a1;
    }
    
    public Version getVersion() {
        /*SL:248*/return this.version;
    }
    
    public void setCanonical(final boolean a1) {
        /*SL:258*/this.canonical = a1;
    }
    
    public boolean isCanonical() {
        /*SL:262*/return this.canonical;
    }
    
    public void setPrettyFlow(final boolean a1) {
        /*SL:273*/this.prettyFlow = a1;
    }
    
    public boolean isPrettyFlow() {
        /*SL:277*/return this.prettyFlow;
    }
    
    public void setWidth(final int a1) {
        /*SL:289*/this.bestWidth = a1;
    }
    
    public int getWidth() {
        /*SL:293*/return this.bestWidth;
    }
    
    public void setSplitLines(final boolean a1) {
        /*SL:304*/this.splitLines = a1;
    }
    
    public boolean getSplitLines() {
        /*SL:308*/return this.splitLines;
    }
    
    public LineBreak getLineBreak() {
        /*SL:312*/return this.lineBreak;
    }
    
    public void setDefaultFlowStyle(final FlowStyle a1) {
        /*SL:316*/if (a1 == null) {
            /*SL:317*/throw new NullPointerException("Use FlowStyle enum.");
        }
        /*SL:319*/this.defaultFlowStyle = a1;
    }
    
    public FlowStyle getDefaultFlowStyle() {
        /*SL:323*/return this.defaultFlowStyle;
    }
    
    public void setLineBreak(final LineBreak a1) {
        /*SL:333*/if (a1 == null) {
            /*SL:334*/throw new NullPointerException("Specify line break.");
        }
        /*SL:336*/this.lineBreak = a1;
    }
    
    public boolean isExplicitStart() {
        /*SL:340*/return this.explicitStart;
    }
    
    public void setExplicitStart(final boolean a1) {
        /*SL:344*/this.explicitStart = a1;
    }
    
    public boolean isExplicitEnd() {
        /*SL:348*/return this.explicitEnd;
    }
    
    public void setExplicitEnd(final boolean a1) {
        /*SL:352*/this.explicitEnd = a1;
    }
    
    public Map<String, String> getTags() {
        /*SL:356*/return this.tags;
    }
    
    public void setTags(final Map<String, String> a1) {
        /*SL:360*/this.tags = a1;
    }
    
    public boolean isAllowReadOnlyProperties() {
        /*SL:370*/return this.allowReadOnlyProperties;
    }
    
    public void setAllowReadOnlyProperties(final boolean a1) {
        /*SL:382*/this.allowReadOnlyProperties = a1;
    }
    
    public TimeZone getTimeZone() {
        /*SL:386*/return this.timeZone;
    }
    
    public void setTimeZone(final TimeZone a1) {
        /*SL:395*/this.timeZone = a1;
    }
    
    public AnchorGenerator getAnchorGenerator() {
        /*SL:400*/return this.anchorGenerator;
    }
    
    public void setAnchorGenerator(final AnchorGenerator a1) {
        /*SL:404*/this.anchorGenerator = a1;
    }
    
    public enum ScalarStyle
    {
        DOUBLE_QUOTED(/*EL:405*/'\"'), 
        SINGLE_QUOTED('\''), 
        LITERAL('|'), 
        FOLDED('>'), 
        PLAIN((Character)null);
        
        private Character styleChar;
        
        private ScalarStyle(final Character a1) {
            this.styleChar = a1;
        }
        
        public Character getChar() {
            /*SL:48*/return this.styleChar;
        }
        
        @Override
        public String toString() {
            /*SL:53*/return "Scalar style: '" + this.styleChar + "'";
        }
        
        public static ScalarStyle createStyle(final Character a1) {
            /*SL:57*/if (a1 == null) {
                /*SL:58*/return ScalarStyle.PLAIN;
            }
            /*SL:60*/switch ((char)a1) {
                case '\"': {
                    /*SL:62*/return ScalarStyle.DOUBLE_QUOTED;
                }
                case '\'': {
                    /*SL:64*/return ScalarStyle.SINGLE_QUOTED;
                }
                case '|': {
                    /*SL:66*/return ScalarStyle.LITERAL;
                }
                case '>': {
                    /*SL:68*/return ScalarStyle.FOLDED;
                }
                default: {
                    /*SL:70*/throw new YAMLException("Unknown scalar style character: " + a1);
                }
            }
        }
    }
    
    public enum FlowStyle
    {
        FLOW(Boolean.TRUE), 
        BLOCK(Boolean.FALSE), 
        AUTO((Boolean)null);
        
        private Boolean styleBoolean;
        
        private FlowStyle(final Boolean a1) {
            this.styleBoolean = a1;
        }
        
        public Boolean getStyleBoolean() {
            /*SL:94*/return this.styleBoolean;
        }
        
        @Override
        public String toString() {
            /*SL:99*/return "Flow style: '" + this.styleBoolean + "'";
        }
    }
    
    public enum LineBreak
    {
        WIN("\r\n"), 
        MAC("\r"), 
        UNIX("\n");
        
        private String lineBreak;
        
        private LineBreak(final String a1) {
            this.lineBreak = a1;
        }
        
        public String getString() {
            /*SL:116*/return this.lineBreak;
        }
        
        @Override
        public String toString() {
            /*SL:121*/return "Line break: " + this.name();
        }
        
        public static LineBreak getPlatformLineBreak() {
            final String property = /*EL:125*/System.getProperty("line.separator");
            /*SL:126*/for (final LineBreak v1 : values()) {
                /*SL:127*/if (v1.lineBreak.equals(property)) {
                    /*SL:128*/return v1;
                }
            }
            /*SL:131*/return LineBreak.UNIX;
        }
    }
    
    public enum Version
    {
        V1_0(new Integer[] { 1, 0 }), 
        V1_1(new Integer[] { 1, 1 });
        
        private Integer[] version;
        
        private Version(final Integer[] a1) {
            this.version = a1;
        }
        
        public int major() {
            /*SL:147*/return this.version[0];
        }
        
        public int minor() {
            /*SL:148*/return this.version[1];
        }
        
        public String getRepresentation() {
            /*SL:151*/return this.version[0] + "." + this.version[1];
        }
        
        @Override
        public String toString() {
            /*SL:156*/return "Version: " + this.getRepresentation();
        }
    }
}
