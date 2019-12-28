package org.reflections.adapters;

import org.reflections.vfs.Vfs;
import java.util.List;

public interface MetadataAdapter<C, F, M>
{
    String getClassName(C p0);
    
    String getSuperclassName(C p0);
    
    List<String> getInterfacesNames(C p0);
    
    List<F> getFields(C p0);
    
    List<M> getMethods(C p0);
    
    String getMethodName(M p0);
    
    List<String> getParameterNames(M p0);
    
    List<String> getClassAnnotationNames(C p0);
    
    List<String> getFieldAnnotationNames(F p0);
    
    List<String> getMethodAnnotationNames(M p0);
    
    List<String> getParameterAnnotationNames(M p0, int p1);
    
    String getReturnTypeName(M p0);
    
    String getFieldName(F p0);
    
    C getOfCreateClassObject(Vfs.File p0) throws Exception;
    
    String getMethodModifier(M p0);
    
    String getMethodKey(C p0, M p1);
    
    String getMethodFullKey(C p0, M p1);
    
    boolean isPublic(Object p0);
    
    boolean acceptsInput(String p0);
}
