package org.yaml.snakeyaml;

import org.yaml.snakeyaml.representer.Represent;
import java.util.TimeZone;
import java.util.Arrays;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.introspector.Property;
import java.util.Set;
import java.util.Collection;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import org.yaml.snakeyaml.representer.SafeRepresenter;
import org.yaml.snakeyaml.introspector.BeanAccess;
import java.util.regex.Pattern;
import java.io.StringReader;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.parser.ParserImpl;
import java.io.Reader;
import org.yaml.snakeyaml.reader.UnicodeReader;
import java.io.InputStream;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.events.Event;
import java.io.IOException;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.serializer.Serializer;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.nodes.Tag;
import java.io.Writer;
import java.io.StringWriter;
import org.yaml.snakeyaml.nodes.Node;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.resolver.Resolver;

public class Yaml
{
    protected final Resolver resolver;
    private String name;
    protected BaseConstructor constructor;
    protected Representer representer;
    protected DumperOptions dumperOptions;
    protected LoaderOptions loadingConfig;
    
    public Yaml() {
        this(new Constructor(), new Representer(), new DumperOptions(), new LoaderOptions(), new Resolver());
    }
    
    public Yaml(final DumperOptions a1) {
        this(new Constructor(), new Representer(), a1);
    }
    
    public Yaml(final LoaderOptions a1) {
        this(new Constructor(), new Representer(), new DumperOptions(), a1);
    }
    
    public Yaml(final Representer a1) {
        this(new Constructor(), a1);
    }
    
    public Yaml(final BaseConstructor a1) {
        this(a1, new Representer());
    }
    
    public Yaml(final BaseConstructor a1, final Representer a2) {
        this(a1, a2, initDumperOptions(a2));
    }
    
    private static DumperOptions initDumperOptions(final Representer a1) {
        final DumperOptions v1 = /*EL:124*/new DumperOptions();
        /*SL:125*/v1.setDefaultFlowStyle(a1.getDefaultFlowStyle());
        /*SL:126*/v1.setDefaultScalarStyle(a1.getDefaultScalarStyle());
        /*SL:127*/v1.setAllowReadOnlyProperties(a1.getPropertyUtils().isAllowReadOnlyProperties());
        /*SL:128*/v1.setTimeZone(a1.getTimeZone());
        /*SL:129*/return v1;
    }
    
    public Yaml(final Representer a1, final DumperOptions a2) {
        this(new Constructor(), a1, a2, new LoaderOptions(), new Resolver());
    }
    
    public Yaml(final BaseConstructor a1, final Representer a2, final DumperOptions a3) {
        this(a1, a2, a3, new LoaderOptions(), new Resolver());
    }
    
    public Yaml(final BaseConstructor a1, final Representer a2, final DumperOptions a3, final LoaderOptions a4) {
        this(a1, a2, a3, a4, new Resolver());
    }
    
    public Yaml(final BaseConstructor a1, final Representer a2, final DumperOptions a3, final Resolver a4) {
        this(a1, a2, a3, new LoaderOptions(), a4);
    }
    
    public Yaml(final BaseConstructor a1, final Representer a2, final DumperOptions a3, final LoaderOptions a4, final Resolver a5) {
        if (!a1.isExplicitPropertyUtils()) {
            a1.setPropertyUtils(a2.getPropertyUtils());
        }
        else if (!a2.isExplicitPropertyUtils()) {
            a2.setPropertyUtils(a1.getPropertyUtils());
        }
        (this.constructor = a1).setAllowDuplicateKeys(a4.isAllowDuplicateKeys());
        if (a3.getIndent() <= a3.getIndicatorIndent()) {
            throw new YAMLException("Indicator indent must be smaller then indent.");
        }
        a2.setDefaultFlowStyle(a3.getDefaultFlowStyle());
        a2.setDefaultScalarStyle(a3.getDefaultScalarStyle());
        a2.getPropertyUtils().setAllowReadOnlyProperties(a3.isAllowReadOnlyProperties());
        a2.setTimeZone(a3.getTimeZone());
        this.representer = a2;
        this.dumperOptions = a3;
        this.loadingConfig = a4;
        this.resolver = a5;
        this.name = "Yaml:" + System.identityHashCode(this);
    }
    
    public String dump(final Object a1) {
        final List<Object> v1 = /*EL:243*/new ArrayList<Object>(1);
        /*SL:244*/v1.add(a1);
        /*SL:245*/return this.dumpAll(v1.iterator());
    }
    
    public Node represent(final Object a1) {
        /*SL:258*/return this.representer.represent(a1);
    }
    
    public String dumpAll(final Iterator<?> a1) {
        final StringWriter v1 = /*EL:269*/new StringWriter();
        /*SL:270*/this.dumpAll(a1, v1, null);
        /*SL:271*/return v1.toString();
    }
    
    public void dump(final Object a1, final Writer a2) {
        final List<Object> v1 = /*EL:283*/new ArrayList<Object>(1);
        /*SL:284*/v1.add(a1);
        /*SL:285*/this.dumpAll(v1.iterator(), a2, null);
    }
    
    public void dumpAll(final Iterator<?> a1, final Writer a2) {
        /*SL:297*/this.dumpAll(a1, a2, null);
    }
    
    private void dumpAll(final Iterator<?> v1, final Writer v2, final Tag v3) {
        final Serializer v4 = /*EL:301*/new Serializer(new Emitter(v2, this.dumperOptions), this.resolver, this.dumperOptions, v3);
        try {
            /*SL:304*/v4.open();
            /*SL:305*/while (v1.hasNext()) {
                final Node a1 = /*EL:306*/this.representer.represent(v1.next());
                /*SL:307*/v4.serialize(a1);
            }
            /*SL:309*/v4.close();
        }
        catch (IOException a2) {
            /*SL:311*/throw new YAMLException(a2);
        }
    }
    
    public String dumpAs(final Object a1, final Tag a2, final DumperOptions.FlowStyle a3) {
        final DumperOptions.FlowStyle v1 = /*EL:356*/this.representer.getDefaultFlowStyle();
        /*SL:357*/if (a3 != null) {
            /*SL:358*/this.representer.setDefaultFlowStyle(a3);
        }
        final List<Object> v2 = /*EL:360*/new ArrayList<Object>(1);
        /*SL:361*/v2.add(a1);
        final StringWriter v3 = /*EL:362*/new StringWriter();
        /*SL:363*/this.dumpAll(v2.iterator(), v3, a2);
        /*SL:364*/this.representer.setDefaultFlowStyle(v1);
        /*SL:365*/return v3.toString();
    }
    
    public String dumpAsMap(final Object a1) {
        /*SL:388*/return this.dumpAs(a1, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
    }
    
    public List<Event> serialize(final Node v2) {
        final SilentEmitter v3 = /*EL:400*/new SilentEmitter();
        final Serializer v4 = /*EL:401*/new Serializer(v3, this.resolver, this.dumperOptions, null);
        try {
            /*SL:403*/v4.open();
            /*SL:404*/v4.serialize(v2);
            /*SL:405*/v4.close();
        }
        catch (IOException a1) {
            /*SL:407*/throw new YAMLException(a1);
        }
        /*SL:409*/return v3.getEvents();
    }
    
    public <T> T load(final String a1) {
        /*SL:437*/return (T)this.loadFromReader(new StreamReader(a1), Object.class);
    }
    
    public <T> T load(final InputStream a1) {
        /*SL:452*/return (T)this.loadFromReader(new StreamReader(new UnicodeReader(a1)), Object.class);
    }
    
    public <T> T load(final Reader a1) {
        /*SL:467*/return (T)this.loadFromReader(new StreamReader(a1), Object.class);
    }
    
    public <T> T loadAs(final Reader a1, final Class<T> a2) {
        /*SL:484*/return (T)this.loadFromReader(new StreamReader(a1), a2);
    }
    
    public <T> T loadAs(final String a1, final Class<T> a2) {
        /*SL:501*/return (T)this.loadFromReader(new StreamReader(a1), a2);
    }
    
    public <T> T loadAs(final InputStream a1, final Class<T> a2) {
        /*SL:518*/return (T)this.loadFromReader(new StreamReader(new UnicodeReader(a1)), a2);
    }
    
    private Object loadFromReader(final StreamReader a1, final Class<?> a2) {
        final Composer v1 = /*EL:522*/new Composer(new ParserImpl(a1), this.resolver);
        /*SL:523*/this.constructor.setComposer(v1);
        /*SL:524*/return this.constructor.getSingleData(a2);
    }
    
    public Iterable<Object> loadAll(final Reader a1) {
        final Composer v1 = /*EL:537*/new Composer(new ParserImpl(new StreamReader(a1)), this.resolver);
        /*SL:538*/this.constructor.setComposer(v1);
        final Iterator<Object> v2 = /*EL:539*/new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                /*SL:542*/return Yaml.this.constructor.checkData();
            }
            
            @Override
            public Object next() {
                /*SL:547*/return Yaml.this.constructor.getData();
            }
            
            @Override
            public void remove() {
                /*SL:552*/throw new UnsupportedOperationException();
            }
        };
        /*SL:555*/return new YamlIterable(v2);
    }
    
    public Iterable<Object> loadAll(final String a1) {
        /*SL:582*/return this.loadAll(new StringReader(a1));
    }
    
    public Iterable<Object> loadAll(final InputStream a1) {
        /*SL:595*/return this.loadAll(new UnicodeReader(a1));
    }
    
    public Node compose(final Reader a1) {
        final Composer v1 = /*EL:609*/new Composer(new ParserImpl(new StreamReader(a1)), this.resolver);
        /*SL:610*/this.constructor.setComposer(v1);
        /*SL:611*/return v1.getSingleNode();
    }
    
    public Iterable<Node> composeAll(final Reader a1) {
        final Composer v1 = /*EL:624*/new Composer(new ParserImpl(new StreamReader(a1)), this.resolver);
        /*SL:625*/this.constructor.setComposer(v1);
        final Iterator<Node> v2 = /*EL:626*/new Iterator<Node>() {
            @Override
            public boolean hasNext() {
                /*SL:629*/return v1.checkNode();
            }
            
            @Override
            public Node next() {
                /*SL:634*/return v1.getNode();
            }
            
            @Override
            public void remove() {
                /*SL:639*/throw new UnsupportedOperationException();
            }
        };
        /*SL:642*/return new NodeIterable(v2);
    }
    
    public void addImplicitResolver(final Tag a1, final Pattern a2, final String a3) {
        /*SL:671*/this.resolver.addImplicitResolver(a1, a2, a3);
    }
    
    @Override
    public String toString() {
        /*SL:676*/return this.name;
    }
    
    public String getName() {
        /*SL:687*/return this.name;
    }
    
    public void setName(final String a1) {
        /*SL:697*/this.name = a1;
    }
    
    public Iterable<Event> parse(final Reader a1) {
        final Parser v1 = /*EL:709*/new ParserImpl(new StreamReader(a1));
        final Iterator<Event> v2 = /*EL:710*/new Iterator<Event>() {
            @Override
            public boolean hasNext() {
                /*SL:713*/return v1.peekEvent() != null;
            }
            
            @Override
            public Event next() {
                /*SL:718*/return v1.getEvent();
            }
            
            @Override
            public void remove() {
                /*SL:723*/throw new UnsupportedOperationException();
            }
        };
        /*SL:726*/return new EventIterable(v2);
    }
    
    public void setBeanAccess(final BeanAccess a1) {
        /*SL:743*/this.constructor.getPropertyUtils().setBeanAccess(a1);
        /*SL:744*/this.representer.getPropertyUtils().setBeanAccess(a1);
    }
    
    public void addTypeDescription(final TypeDescription a1) {
        /*SL:748*/this.constructor.addTypeDescription(a1);
        /*SL:749*/this.representer.addTypeDescription(a1);
    }
    
    private static class SilentEmitter implements Emitable
    {
        private List<Event> events;
        
        private SilentEmitter() {
            this.events = new ArrayList<Event>(100);
        }
        
        public List<Event> getEvents() {
            /*SL:416*/return this.events;
        }
        
        @Override
        public void emit(final Event a1) throws IOException {
            /*SL:421*/this.events.add(a1);
        }
    }
    
    private static class YamlIterable implements Iterable<Object>
    {
        private Iterator<Object> iterator;
        
        public YamlIterable(final Iterator<Object> a1) {
            this.iterator = a1;
        }
        
        @Override
        public Iterator<Object> iterator() {
            /*SL:567*/return this.iterator;
        }
    }
    
    private static class NodeIterable implements Iterable<Node>
    {
        private Iterator<Node> iterator;
        
        public NodeIterable(final Iterator<Node> a1) {
            this.iterator = a1;
        }
        
        @Override
        public Iterator<Node> iterator() {
            /*SL:654*/return this.iterator;
        }
    }
    
    private static class EventIterable implements Iterable<Event>
    {
        private Iterator<Event> iterator;
        
        public EventIterable(final Iterator<Event> a1) {
            this.iterator = a1;
        }
        
        @Override
        public Iterator<Event> iterator() {
            /*SL:738*/return this.iterator;
        }
    }
}
