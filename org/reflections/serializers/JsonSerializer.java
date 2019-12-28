package org.reflections.serializers;

import com.google.gson.JsonParseException;
import java.util.Iterator;
import com.google.common.collect.SetMultimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.base.Supplier;
import java.util.Collection;
import java.util.HashMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonSerializer;
import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import com.google.common.io.Files;
import java.nio.charset.Charset;
import org.reflections.util.Utils;
import java.io.File;
import java.io.Reader;
import java.io.InputStreamReader;
import org.reflections.Reflections;
import java.io.InputStream;
import com.google.gson.Gson;

public class JsonSerializer implements Serializer
{
    private Gson gson;
    
    @Override
    public Reflections read(final InputStream a1) {
        /*SL:33*/return (Reflections)this.getGson().fromJson((Reader)new InputStreamReader(a1), (Class)Reflections.class);
    }
    
    @Override
    public File save(final Reflections v2, final String v3) {
        try {
            final File a1 = /*EL:38*/Utils.prepareFile(v3);
            /*SL:39*/Files.write(this.toString(v2), a1, Charset.defaultCharset());
            /*SL:40*/return a1;
        }
        catch (IOException a2) {
            /*SL:42*/throw new RuntimeException(a2);
        }
    }
    
    @Override
    public String toString(final Reflections a1) {
        /*SL:47*/return this.getGson().toJson((Object)a1);
    }
    
    private Gson getGson() {
        /*SL:51*/if (this.gson == null) {
            /*SL:74*/this.gson = new GsonBuilder().registerTypeAdapter((Type)Multimap.class, (Object)new com.google.gson.JsonSerializer<Multimap>() {
                public JsonElement serialize(final Multimap a1, final Type a2, final JsonSerializationContext a3) {
                    /*SL:55*/return a3.serialize((Object)a1.asMap());
                }
            }).registerTypeAdapter((Type)Multimap.class, (Object)new JsonDeserializer<Multimap>() {
                public Multimap deserialize(final JsonElement v1, final Type v2, final JsonDeserializationContext v3) throws JsonParseException {
                    final SetMultimap<String, String> v4 = /*EL:60*/Multimaps.<String, String>newSetMultimap(new HashMap<String, Collection<String>>(), new Supplier<Set<String>>() {
                        @Override
                        public Set<String> get() {
                            /*SL:62*/return (Set<String>)Sets.<Object>newHashSet();
                        }
                    });
                    /*SL:65*/for (JsonElement a2 : ((JsonObject)v1).entrySet()) {
                        final Iterator iterator2 = /*EL:66*/((JsonArray)a2.getValue()).iterator();
                        while (iterator2.hasNext()) {
                            a2 = iterator2.next();
                            /*SL:67*/v4.get(a2.getKey()).add(a2.getAsString());
                        }
                    }
                    /*SL:70*/return v4;
                }
            }).setPrettyPrinting().create();
        }
        /*SL:77*/return this.gson;
    }
}
