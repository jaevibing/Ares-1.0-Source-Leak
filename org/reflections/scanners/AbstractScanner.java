package org.reflections.scanners;

import org.reflections.adapters.MetadataAdapter;
import org.reflections.ReflectionsException;
import org.reflections.vfs.Vfs;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import org.reflections.Configuration;

public abstract class AbstractScanner implements Scanner
{
    private Configuration configuration;
    private Multimap<String, String> store;
    private Predicate<String> resultFilter;
    
    public AbstractScanner() {
        this.resultFilter = Predicates.<String>alwaysTrue();
    }
    
    @Override
    public boolean acceptsInput(final String a1) {
        /*SL:24*/return this.getMetadataAdapter().acceptsInput(a1);
    }
    
    @Override
    public Object scan(final Vfs.File v1, Object v2) {
        /*SL:28*/if (v2 == null) {
            try {
                /*SL:30*/v2 = this.configuration.getMetadataAdapter().getOfCreateClassObject(v1);
            }
            catch (Exception a1) {
                /*SL:32*/throw new ReflectionsException("could not create class object from file " + v1.getRelativePath(), a1);
            }
        }
        /*SL:35*/this.scan(v2);
        /*SL:36*/return v2;
    }
    
    public abstract void scan(final Object p0);
    
    public Configuration getConfiguration() {
        /*SL:43*/return this.configuration;
    }
    
    @Override
    public void setConfiguration(final Configuration a1) {
        /*SL:47*/this.configuration = a1;
    }
    
    @Override
    public Multimap<String, String> getStore() {
        /*SL:51*/return this.store;
    }
    
    @Override
    public void setStore(final Multimap<String, String> a1) {
        /*SL:55*/this.store = a1;
    }
    
    public Predicate<String> getResultFilter() {
        /*SL:59*/return this.resultFilter;
    }
    
    public void setResultFilter(final Predicate<String> a1) {
        /*SL:63*/this.resultFilter = a1;
    }
    
    @Override
    public Scanner filterResultsBy(final Predicate<String> a1) {
        /*SL:67*/this.setResultFilter(a1);
        return this;
    }
    
    @Override
    public boolean acceptResult(final String a1) {
        /*SL:72*/return a1 != null && this.resultFilter.apply(a1);
    }
    
    protected MetadataAdapter getMetadataAdapter() {
        /*SL:76*/return this.configuration.getMetadataAdapter();
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:81*/return this == a1 || (a1 != null && this.getClass() == a1.getClass());
    }
    
    @Override
    public int hashCode() {
        /*SL:85*/return this.getClass().hashCode();
    }
}
