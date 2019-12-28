package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import java.util.List;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;

public class ObfuscationDataProvider implements IObfuscationDataProvider
{
    private final IMixinAnnotationProcessor ap;
    private final List<ObfuscationEnvironment> environments;
    
    public ObfuscationDataProvider(final IMixinAnnotationProcessor a1, final List<ObfuscationEnvironment> a2) {
        this.ap = a1;
        this.environments = a2;
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntryRecursive(final MemberInfo v-3) {
        MemberInfo move = /*EL:66*/v-3;
        final ObfuscationData<String> obfClass = /*EL:67*/this.getObfClass(move.owner);
        ObfuscationData<T> v0 = /*EL:68*/(ObfuscationData<T>)this.<Object>getObfEntry(move);
        try {
            /*SL:70*/while (v0.isEmpty()) {
                final TypeHandle v = /*EL:71*/this.ap.getTypeProvider().getTypeHandle(move.owner);
                /*SL:72*/if (v == null) {
                    /*SL:73*/return v0;
                }
                final TypeHandle v2 = /*EL:76*/v.getSuperclass();
                /*SL:77*/v0 = this.<T>getObfEntryUsing(move, v2);
                /*SL:78*/if (!v0.isEmpty()) {
                    /*SL:79*/return ObfuscationDataProvider.<T>applyParents(obfClass, v0);
                }
                /*SL:82*/for (final TypeHandle a1 : v.getInterfaces()) {
                    /*SL:83*/v0 = this.<T>getObfEntryUsing(move, a1);
                    /*SL:84*/if (!v0.isEmpty()) {
                        /*SL:85*/return ObfuscationDataProvider.<T>applyParents(obfClass, v0);
                    }
                }
                /*SL:89*/if (v2 == null) {
                    /*SL:90*/break;
                }
                /*SL:93*/move = move.move(v2.getName());
            }
        }
        catch (Exception v3) {
            /*SL:96*/v3.printStackTrace();
            /*SL:97*/return (ObfuscationData<T>)this.<Object>getObfEntry(v-3);
        }
        /*SL:99*/return v0;
    }
    
    private <T> ObfuscationData<T> getObfEntryUsing(final MemberInfo a1, final TypeHandle a2) {
        /*SL:113*/return (a2 == null) ? new ObfuscationData<T>() : this.<T>getObfEntry(a1.move(a2.getName()));
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntry(final MemberInfo a1) {
        /*SL:124*/if (a1.isField()) {
            /*SL:125*/return (ObfuscationData<T>)this.getObfField(a1);
        }
        /*SL:127*/return (ObfuscationData<T>)this.getObfMethod(a1.asMethodMapping());
    }
    
    @Override
    public <T> ObfuscationData<T> getObfEntry(final IMapping<T> a1) {
        /*SL:133*/if (a1 != null) {
            /*SL:134*/if (a1.getType() == IMapping.Type.FIELD) {
                /*SL:135*/return (ObfuscationData<T>)this.getObfField((MappingField)a1);
            }
            /*SL:136*/if (a1.getType() == IMapping.Type.METHOD) {
                /*SL:137*/return (ObfuscationData<T>)this.getObfMethod((MappingMethod)a1);
            }
        }
        /*SL:141*/return new ObfuscationData<T>();
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethodRecursive(final MemberInfo a1) {
        /*SL:151*/return this.<MappingMethod>getObfEntryRecursive(a1);
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethod(final MemberInfo a1) {
        /*SL:161*/return this.getRemappedMethod(a1, a1.isConstructor());
    }
    
    @Override
    public ObfuscationData<MappingMethod> getRemappedMethod(final MemberInfo a1) {
        /*SL:166*/return this.getRemappedMethod(a1, true);
    }
    
    private ObfuscationData<MappingMethod> getRemappedMethod(final MemberInfo v2, final boolean v3) {
        final ObfuscationData<MappingMethod> v4 = /*EL:170*/new ObfuscationData<MappingMethod>();
        /*SL:172*/for (MappingMethod a2 : this.environments) {
            /*SL:173*/a2 = a2.getObfMethod(v2);
            /*SL:174*/if (a2 != null) {
                /*SL:175*/v4.put(a2.getType(), a2);
            }
        }
        /*SL:179*/if (!v4.isEmpty() || !v3) {
            /*SL:180*/return v4;
        }
        /*SL:183*/return this.remapDescriptor(v4, v2);
    }
    
    @Override
    public ObfuscationData<MappingMethod> getObfMethod(final MappingMethod a1) {
        /*SL:193*/return this.getRemappedMethod(a1, a1.isConstructor());
    }
    
    @Override
    public ObfuscationData<MappingMethod> getRemappedMethod(final MappingMethod a1) {
        /*SL:198*/return this.getRemappedMethod(a1, true);
    }
    
    private ObfuscationData<MappingMethod> getRemappedMethod(final MappingMethod v2, final boolean v3) {
        final ObfuscationData<MappingMethod> v4 = /*EL:202*/new ObfuscationData<MappingMethod>();
        /*SL:204*/for (MappingMethod a2 : this.environments) {
            /*SL:205*/a2 = a2.getObfMethod(v2);
            /*SL:206*/if (a2 != null) {
                /*SL:207*/v4.put(a2.getType(), a2);
            }
        }
        /*SL:211*/if (!v4.isEmpty() || !v3) {
            /*SL:212*/return v4;
        }
        /*SL:215*/return this.remapDescriptor(v4, new MemberInfo(v2));
    }
    
    public ObfuscationData<MappingMethod> remapDescriptor(final ObfuscationData<MappingMethod> v2, final MemberInfo v3) {
        /*SL:226*/for (MemberInfo a2 : this.environments) {
            /*SL:227*/a2 = a2.remapDescriptor(v3);
            /*SL:228*/if (a2 != null) {
                /*SL:229*/v2.put(a2.getType(), a2.asMethodMapping());
            }
        }
        /*SL:233*/return v2;
    }
    
    @Override
    public ObfuscationData<MappingField> getObfFieldRecursive(final MemberInfo a1) {
        /*SL:243*/return this.<MappingField>getObfEntryRecursive(a1);
    }
    
    @Override
    public ObfuscationData<MappingField> getObfField(final MemberInfo a1) {
        /*SL:252*/return this.getObfField(a1.asFieldMapping());
    }
    
    @Override
    public ObfuscationData<MappingField> getObfField(final MappingField v-2) {
        final ObfuscationData<MappingField> obfuscationData = /*EL:261*/new ObfuscationData<MappingField>();
        /*SL:263*/for (final ObfuscationEnvironment v1 : this.environments) {
            MappingField a1 = /*EL:264*/v1.getObfField(v-2);
            /*SL:265*/if (a1 != null) {
                /*SL:266*/if (a1.getDesc() == null && v-2.getDesc() != null) {
                    /*SL:267*/a1 = a1.transform(v1.remapDescriptor(v-2.getDesc()));
                }
                /*SL:269*/obfuscationData.put(v1.getType(), a1);
            }
        }
        /*SL:273*/return obfuscationData;
    }
    
    @Override
    public ObfuscationData<String> getObfClass(final TypeHandle a1) {
        /*SL:282*/return this.getObfClass(a1.getName());
    }
    
    @Override
    public ObfuscationData<String> getObfClass(final String v-2) {
        final ObfuscationData<String> obfuscationData = /*EL:291*/new ObfuscationData<String>(v-2);
        /*SL:293*/for (final ObfuscationEnvironment v1 : this.environments) {
            final String a1 = /*EL:294*/v1.getObfClass(v-2);
            /*SL:295*/if (a1 != null) {
                /*SL:296*/obfuscationData.put(v1.getType(), a1);
            }
        }
        /*SL:300*/return obfuscationData;
    }
    
    private static <T> ObfuscationData<T> applyParents(final ObfuscationData<String> v-2, final ObfuscationData<T> v-1) {
        /*SL:312*/for (final ObfuscationType v1 : v-1) {
            final String a1 = /*EL:313*/v-2.get(v1);
            final T a2 = /*EL:314*/v-1.get(v1);
            /*SL:315*/v-1.put(v1, (T)MemberInfo.fromMapping((IMapping<?>)a2).move(a1).asMapping());
        }
        /*SL:317*/return v-1;
    }
}
