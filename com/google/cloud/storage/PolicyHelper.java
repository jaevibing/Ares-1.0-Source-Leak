package com.google.cloud.storage;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.cloud.Identity;
import com.google.cloud.Role;
import com.google.api.services.storage.model.Policy;

class PolicyHelper
{
    static com.google.cloud.Policy convertFromApiPolicy(final Policy v-2) {
        final com.google.cloud.Policy.Builder builder = /*EL:35*/com.google.cloud.Policy.newBuilder();
        /*SL:36*/for (final Policy.Bindings v1 : v-2.getBindings()) {
            /*SL:37*/for (final String a1 : v1.getMembers()) {
                /*SL:38*/builder.addIdentity(Role.of(v1.getRole()), Identity.valueOf(a1), new Identity[0]);
            }
        }
        /*SL:41*/return builder.setEtag(v-2.getEtag()).build();
    }
    
    static Policy convertToApiPolicy(final com.google.cloud.Policy v-3) {
        final List<Policy.Bindings> bindings = /*EL:45*/new ArrayList<Policy.Bindings>(v-3.getBindings().size());
        /*SL:46*/for (final Map.Entry<Role, Set<Identity>> v0 : v-3.getBindings().entrySet()) {
            final List<String> v = /*EL:47*/new ArrayList<String>(v0.getValue().size());
            /*SL:48*/for (final Identity a1 : v0.getValue()) {
                /*SL:49*/v.add(a1.strValue());
            }
            /*SL:51*/bindings.add(new Policy.Bindings().setMembers((List)v).setRole(v0.getKey().getValue()));
        }
        /*SL:53*/return new Policy().setBindings((List)bindings).setEtag(/*EL:54*/v-3.getEtag());
    }
}
