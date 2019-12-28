package com.fasterxml.jackson.core;

import java.util.Iterator;

public interface TreeNode
{
    JsonToken asToken();
    
    JsonParser.NumberType numberType();
    
    int size();
    
    boolean isValueNode();
    
    boolean isContainerNode();
    
    boolean isMissingNode();
    
    boolean isArray();
    
    boolean isObject();
    
    TreeNode get(String p0);
    
    TreeNode get(int p0);
    
    TreeNode path(String p0);
    
    TreeNode path(int p0);
    
    Iterator<String> fieldNames();
    
    TreeNode at(JsonPointer p0);
    
    TreeNode at(String p0) throws IllegalArgumentException;
    
    JsonParser traverse();
    
    JsonParser traverse(ObjectCodec p0);
}
