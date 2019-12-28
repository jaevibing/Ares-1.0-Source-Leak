package com.google.cloud.storage;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import com.google.common.base.Preconditions;
import com.google.cloud.storage.spi.v1.StorageRpc;
import java.io.Serializable;

public abstract class Option implements Serializable
{
    private static final long serialVersionUID = -73199088766477208L;
    private final StorageRpc.Option rpcOption;
    private final Object value;
    
    Option(final StorageRpc.Option a1, final Object a2) {
        this.rpcOption = Preconditions.<StorageRpc.Option>checkNotNull(a1);
        this.value = a2;
    }
    
    StorageRpc.Option getRpcOption() {
        /*SL:40*/return this.rpcOption;
    }
    
    Object getValue() {
        /*SL:44*/return this.value;
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:49*/if (!(a1 instanceof Option)) {
            /*SL:50*/return false;
        }
        final Option v1 = /*EL:52*/(Option)a1;
        /*SL:53*/return Objects.equals(this.rpcOption, v1.rpcOption) && Objects.equals(this.value, v1.value);
    }
    
    @Override
    public int hashCode() {
        /*SL:58*/return Objects.hash(this.rpcOption, this.value);
    }
    
    @Override
    public String toString() {
        /*SL:63*/return MoreObjects.toStringHelper((Object)this).add("name", (Object)this.rpcOption.value()).add(/*EL:64*/"value", this.value).toString();
    }
}
