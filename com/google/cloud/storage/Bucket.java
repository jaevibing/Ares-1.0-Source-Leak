package com.google.cloud.storage;

import java.util.Map;
import java.io.Serializable;
import java.util.Set;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;
import java.security.Key;
import com.google.common.base.Function;
import com.google.cloud.storage.spi.v1.StorageRpc;
import com.google.api.services.storage.model.Bucket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;
import java.io.InputStream;
import com.google.cloud.Tuple;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import com.google.api.gax.paging.Page;
import java.util.Arrays;
import com.google.common.base.Preconditions;

public class Bucket extends BucketInfo
{
    private static final long serialVersionUID = 8574601739542252586L;
    private final StorageOptions options;
    private transient Storage storage;
    
    Bucket(final Storage a1, final BuilderImpl a2) {
        super(a2);
        this.storage = Preconditions.<Storage>checkNotNull(a1);
        this.options = (StorageOptions)a1.getOptions();
    }
    
    public boolean exists(final BucketSourceOption... a1) {
        final int v1 = /*EL:700*/a1.length;
        final Storage.BucketGetOption[] v2 = /*EL:701*/Arrays.<Storage.BucketGetOption>copyOf(BucketSourceOption.toGetOptions(this, a1), v1 + 1);
        /*SL:702*/v2[v1] = Storage.BucketGetOption.fields(new Storage.BucketField[0]);
        /*SL:703*/return this.storage.get(this.getName(), v2) != null;
    }
    
    public Bucket reload(final BucketSourceOption... a1) {
        /*SL:724*/return this.storage.get(this.getName(), BucketSourceOption.toGetOptions(this, a1));
    }
    
    public Bucket update(final Storage.BucketTargetOption... a1) {
        /*SL:745*/return this.storage.update(this, a1);
    }
    
    public boolean delete(final BucketSourceOption... a1) {
        /*SL:768*/return this.storage.delete(this.getName(), BucketSourceOption.toSourceOptions(this, a1));
    }
    
    public Page<Blob> list(final Storage.BlobListOption... a1) {
        /*SL:789*/return this.storage.list(this.getName(), a1);
    }
    
    public Blob get(final String a1, final Storage.BlobGetOption... a2) {
        /*SL:809*/return this.storage.get(BlobId.of(this.getName(), a1), a2);
    }
    
    public List<Blob> get(final String a3, final String v1, final String... v2) {
        final List<BlobId> v3 = /*EL:835*/(List<BlobId>)Lists.<Object>newArrayListWithCapacity(v2.length + 2);
        /*SL:836*/v3.add(BlobId.of(this.getName(), a3));
        /*SL:837*/v3.add(BlobId.of(this.getName(), v1));
        /*SL:838*/for (final String a4 : v2) {
            /*SL:839*/v3.add(BlobId.of(this.getName(), a4));
        }
        /*SL:841*/return this.storage.get(v3);
    }
    
    public List<Blob> get(final Iterable<String> v2) {
        final ImmutableList.Builder<BlobId> v3 = /*EL:868*/ImmutableList.<BlobId>builder();
        /*SL:869*/for (final String a1 : v2) {
            /*SL:870*/v3.add(BlobId.of(this.getName(), a1));
        }
        /*SL:872*/return this.storage.get(v3.build());
    }
    
    public Blob create(final String a1, final byte[] a2, final String a3, final BlobTargetOption... a4) {
        final BlobInfo v1 = /*EL:897*/BlobInfo.newBuilder(BlobId.of(this.getName(), a1)).setContentType(a3).build();
        final Tuple<BlobInfo, Storage.BlobTargetOption[]> v2 = /*EL:899*/BlobTargetOption.toTargetOptions(v1, a4);
        /*SL:900*/return this.storage.create((BlobInfo)v2.x(), a2, (Storage.BlobTargetOption[])v2.y());
    }
    
    public Blob create(final String a1, final InputStream a2, final String a3, final BlobWriteOption... a4) {
        final BlobInfo v1 = /*EL:926*/BlobInfo.newBuilder(BlobId.of(this.getName(), a1)).setContentType(a3).build();
        final Tuple<BlobInfo, Storage.BlobWriteOption[]> v2 = /*EL:928*/BlobWriteOption.toWriteOptions(v1, a4);
        /*SL:929*/return this.storage.create((BlobInfo)v2.x(), a2, (Storage.BlobWriteOption[])v2.y());
    }
    
    public Blob create(final String a1, final byte[] a2, final BlobTargetOption... a3) {
        final BlobInfo v1 = /*EL:952*/BlobInfo.newBuilder(BlobId.of(this.getName(), a1)).build();
        final Tuple<BlobInfo, Storage.BlobTargetOption[]> v2 = /*EL:954*/BlobTargetOption.toTargetOptions(v1, a3);
        /*SL:955*/return this.storage.create((BlobInfo)v2.x(), a2, (Storage.BlobTargetOption[])v2.y());
    }
    
    public Blob create(final String a1, final InputStream a2, final BlobWriteOption... a3) {
        final BlobInfo v1 = /*EL:978*/BlobInfo.newBuilder(BlobId.of(this.getName(), a1)).build();
        final Tuple<BlobInfo, Storage.BlobWriteOption[]> v2 = /*EL:980*/BlobWriteOption.toWriteOptions(v1, a3);
        /*SL:981*/return this.storage.create((BlobInfo)v2.x(), a2, (Storage.BlobWriteOption[])v2.y());
    }
    
    public Acl getAcl(final Acl.Entity a1) {
        /*SL:996*/return this.storage.getAcl(this.getName(), a1);
    }
    
    public boolean deleteAcl(final Acl.Entity a1) {
        /*SL:1017*/return this.storage.deleteAcl(this.getName(), a1);
    }
    
    public Acl createAcl(final Acl a1) {
        /*SL:1032*/return this.storage.createAcl(this.getName(), a1);
    }
    
    public Acl updateAcl(final Acl a1) {
        /*SL:1047*/return this.storage.updateAcl(this.getName(), a1);
    }
    
    public List<Acl> listAcls() {
        /*SL:1065*/return this.storage.listAcls(this.getName());
    }
    
    public Acl getDefaultAcl(final Acl.Entity a1) {
        /*SL:1084*/return this.storage.getDefaultAcl(this.getName(), a1);
    }
    
    public boolean deleteDefaultAcl(final Acl.Entity a1) {
        /*SL:1108*/return this.storage.deleteDefaultAcl(this.getName(), a1);
    }
    
    public Acl createDefaultAcl(final Acl a1) {
        /*SL:1126*/return this.storage.createDefaultAcl(this.getName(), a1);
    }
    
    public Acl updateDefaultAcl(final Acl a1) {
        /*SL:1144*/return this.storage.updateDefaultAcl(this.getName(), a1);
    }
    
    public List<Acl> listDefaultAcls() {
        /*SL:1165*/return this.storage.listDefaultAcls(this.getName());
    }
    
    public Bucket lockRetentionPolicy(final Storage.BucketTargetOption... a1) {
        /*SL:1190*/return this.storage.lockRetentionPolicy(this, a1);
    }
    
    public Storage getStorage() {
        /*SL:1195*/return this.storage;
    }
    
    @Override
    public Builder toBuilder() {
        /*SL:1200*/return new Builder(this);
    }
    
    @Override
    public final boolean equals(final Object a1) {
        /*SL:1205*/if (a1 == this) {
            /*SL:1206*/return true;
        }
        /*SL:1208*/if (a1 == null || !a1.getClass().equals(Bucket.class)) {
            /*SL:1209*/return false;
        }
        final Bucket v1 = /*EL:1211*/(Bucket)a1;
        /*SL:1212*/return Objects.equals(this.toPb(), v1.toPb()) && Objects.equals(this.options, v1.options);
    }
    
    @Override
    public final int hashCode() {
        /*SL:1217*/return Objects.hash(super.hashCode(), this.options);
    }
    
    private void readObject(final ObjectInputStream a1) throws IOException, ClassNotFoundException {
        /*SL:1221*/a1.defaultReadObject();
        /*SL:1222*/this.storage = (Storage)this.options.getService();
    }
    
    static Bucket fromPb(final Storage a1, final com.google.api.services.storage.model.Bucket a2) {
        /*SL:1226*/return new Bucket(a1, new BuilderImpl(BucketInfo.fromPb(a2)));
    }
    
    public static class BucketSourceOption extends Option
    {
        private static final long serialVersionUID = 6928872234155522371L;
        
        private BucketSourceOption(final StorageRpc.Option a1) {
            super(a1, null);
        }
        
        private BucketSourceOption(final StorageRpc.Option a1, final Object a2) {
            super(a1, a2);
        }
        
        private Storage.BucketSourceOption toSourceOption(final BucketInfo a1) {
            /*SL:75*/switch (this.getRpcOption()) {
                case IF_METAGENERATION_MATCH: {
                    /*SL:77*/return Storage.BucketSourceOption.metagenerationMatch(a1.getMetageneration());
                }
                case IF_METAGENERATION_NOT_MATCH: {
                    /*SL:79*/return Storage.BucketSourceOption.metagenerationNotMatch(a1.getMetageneration());
                }
                default: {
                    /*SL:81*/throw new AssertionError((Object)"Unexpected enum value");
                }
            }
        }
        
        private Storage.BucketGetOption toGetOption(final BucketInfo a1) {
            /*SL:86*/switch (this.getRpcOption()) {
                case IF_METAGENERATION_MATCH: {
                    /*SL:88*/return Storage.BucketGetOption.metagenerationMatch(a1.getMetageneration());
                }
                case IF_METAGENERATION_NOT_MATCH: {
                    /*SL:90*/return Storage.BucketGetOption.metagenerationNotMatch(a1.getMetageneration());
                }
                default: {
                    /*SL:92*/throw new AssertionError((Object)"Unexpected enum value");
                }
            }
        }
        
        public static BucketSourceOption metagenerationMatch() {
            /*SL:101*/return new BucketSourceOption(StorageRpc.Option.IF_METAGENERATION_MATCH);
        }
        
        public static BucketSourceOption metagenerationNotMatch() {
            /*SL:109*/return new BucketSourceOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH);
        }
        
        public static BucketSourceOption userProject(final String a1) {
            /*SL:117*/return new BucketSourceOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        static Storage.BucketSourceOption[] toSourceOptions(final BucketInfo a2, final BucketSourceOption... v1) {
            final Storage.BucketSourceOption[] v2 = /*EL:122*/new Storage.BucketSourceOption[v1.length];
            int v3 = /*EL:124*/0;
            /*SL:125*/for (final BucketSourceOption a3 : v1) {
                /*SL:126*/v2[v3++] = a3.toSourceOption(a2);
            }
            /*SL:128*/return v2;
        }
        
        static Storage.BucketGetOption[] toGetOptions(final BucketInfo a2, final BucketSourceOption... v1) {
            final Storage.BucketGetOption[] v2 = /*EL:133*/new Storage.BucketGetOption[v1.length];
            int v3 = /*EL:134*/0;
            /*SL:135*/for (final BucketSourceOption a3 : v1) {
                /*SL:136*/v2[v3++] = a3.toGetOption(a2);
            }
            /*SL:138*/return v2;
        }
    }
    
    public static class BlobTargetOption extends Option
    {
        private static final Function<BlobTargetOption, StorageRpc.Option> TO_ENUM;
        private static final long serialVersionUID = 8345296337342509425L;
        
        private BlobTargetOption(final StorageRpc.Option a1, final Object a2) {
            super(a1, a2);
        }
        
        private Tuple<BlobInfo, Storage.BlobTargetOption> toTargetOption(final BlobInfo a1) {
            BlobId v1 = /*EL:159*/a1.getBlobId();
            /*SL:160*/switch (this.getRpcOption()) {
                case PREDEFINED_ACL: {
                    /*SL:162*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1, /*EL:163*/(Object)Storage.BlobTargetOption.predefinedAcl((Storage.PredefinedAcl)this.getValue()));
                }
                case IF_GENERATION_MATCH: {
                    /*SL:165*/v1 = BlobId.of(v1.getBucket(), v1.getName(), (Long)this.getValue());
                    /*SL:166*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1.toBuilder().setBlobId(/*EL:167*/v1).build(), /*EL:168*/(Object)Storage.BlobTargetOption.generationMatch());
                }
                case IF_GENERATION_NOT_MATCH: {
                    /*SL:170*/v1 = BlobId.of(v1.getBucket(), v1.getName(), (Long)this.getValue());
                    /*SL:171*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1.toBuilder().setBlobId(/*EL:172*/v1).build(), /*EL:173*/(Object)Storage.BlobTargetOption.generationNotMatch());
                }
                case IF_METAGENERATION_MATCH: {
                    /*SL:175*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1.toBuilder().setMetageneration(/*EL:176*/(Long)this.getValue()).build(), /*EL:177*/(Object)Storage.BlobTargetOption.metagenerationMatch());
                }
                case IF_METAGENERATION_NOT_MATCH: {
                    /*SL:179*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1.toBuilder().setMetageneration(/*EL:180*/(Long)this.getValue()).build(), /*EL:181*/(Object)Storage.BlobTargetOption.metagenerationNotMatch());
                }
                case CUSTOMER_SUPPLIED_KEY: {
                    /*SL:183*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1, (Object)Storage.BlobTargetOption.encryptionKey((String)this.getValue()));
                }
                case KMS_KEY_NAME: {
                    /*SL:185*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1, (Object)Storage.BlobTargetOption.kmsKeyName((String)this.getValue()));
                }
                case USER_PROJECT: {
                    /*SL:187*/return (Tuple<BlobInfo, Storage.BlobTargetOption>)Tuple.of((Object)a1, (Object)Storage.BlobTargetOption.userProject((String)this.getValue()));
                }
                default: {
                    /*SL:189*/throw new AssertionError((Object)"Unexpected enum value");
                }
            }
        }
        
        public static BlobTargetOption predefinedAcl(final Storage.PredefinedAcl a1) {
            /*SL:195*/return new BlobTargetOption(StorageRpc.Option.PREDEFINED_ACL, a1);
        }
        
        public static BlobTargetOption doesNotExist() {
            /*SL:204*/return new BlobTargetOption(StorageRpc.Option.IF_GENERATION_MATCH, 0L);
        }
        
        public static BlobTargetOption generationMatch(final long a1) {
            /*SL:213*/return new BlobTargetOption(StorageRpc.Option.IF_GENERATION_MATCH, a1);
        }
        
        public static BlobTargetOption generationNotMatch(final long a1) {
            /*SL:222*/return new BlobTargetOption(StorageRpc.Option.IF_GENERATION_NOT_MATCH, a1);
        }
        
        public static BlobTargetOption metagenerationMatch(final long a1) {
            /*SL:231*/return new BlobTargetOption(StorageRpc.Option.IF_METAGENERATION_MATCH, a1);
        }
        
        public static BlobTargetOption metagenerationNotMatch(final long a1) {
            /*SL:240*/return new BlobTargetOption(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH, a1);
        }
        
        public static BlobTargetOption encryptionKey(final Key a1) {
            final String v1 = /*EL:248*/BaseEncoding.base64().encode(a1.getEncoded());
            /*SL:249*/return new BlobTargetOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, v1);
        }
        
        public static BlobTargetOption encryptionKey(final String a1) {
            /*SL:259*/return new BlobTargetOption(StorageRpc.Option.CUSTOMER_SUPPLIED_KEY, a1);
        }
        
        public static BlobTargetOption kmsKeyName(final String a1) {
            /*SL:268*/return new BlobTargetOption(StorageRpc.Option.KMS_KEY_NAME, a1);
        }
        
        public static BlobTargetOption userProject(final String a1) {
            /*SL:276*/return new BlobTargetOption(StorageRpc.Option.USER_PROJECT, a1);
        }
        
        static Tuple<BlobInfo, Storage.BlobTargetOption[]> toTargetOptions(final BlobInfo v1, final BlobTargetOption... v2) {
            final Set<StorageRpc.Option> v3 = /*EL:282*/(Set<StorageRpc.Option>)Sets.<Object>immutableEnumSet((Iterable<Object>)Lists.<Object, Object>transform((List<Object>)Arrays.<F>asList((F[])v2), (Function<? super Object, ?>)BlobTargetOption.TO_ENUM));
            /*SL:283*/Preconditions.checkArgument(!v3.contains(StorageRpc.Option.IF_METAGENERATION_NOT_MATCH) || /*EL:284*/!v3.contains(StorageRpc.Option.IF_METAGENERATION_MATCH), /*EL:285*/(Object)"metagenerationMatch and metagenerationNotMatch options can not be both provided");
            /*SL:287*/Preconditions.checkArgument(!v3.contains(StorageRpc.Option.IF_GENERATION_NOT_MATCH) || /*EL:288*/!v3.contains(StorageRpc.Option.IF_GENERATION_MATCH), /*EL:289*/(Object)"Only one option of generationMatch, doesNotExist or generationNotMatch can be provided");
            final Storage.BlobTargetOption[] v4 = /*EL:291*/new Storage.BlobTargetOption[v2.length];
            BlobInfo v5 = /*EL:292*/v1;
            int v6 = /*EL:293*/0;
            /*SL:294*/for (Tuple<BlobInfo, Storage.BlobTargetOption> a2 : v2) {
                /*SL:295*/a2 = a2.toTargetOption(v5);
                /*SL:296*/v5 = (BlobInfo)a2.x();
                /*SL:297*/v4[v6++] = (Storage.BlobTargetOption)a2.y();
            }
            /*SL:299*/return (Tuple<BlobInfo, Storage.BlobTargetOption[]>)Tuple.of((Object)v5, (Object)v4);
        }
        
        static {
            TO_ENUM = new Function<BlobTargetOption, StorageRpc.Option>() {
                @Override
                public StorageRpc.Option apply(final BlobTargetOption a1) {
                    /*SL:149*/return a1.getRpcOption();
                }
            };
        }
    }
    
    public static class BlobWriteOption implements Serializable
    {
        private static final Function<BlobWriteOption, Storage.BlobWriteOption.Option> TO_ENUM;
        private static final long serialVersionUID = 4722190734541993114L;
        private final Storage.BlobWriteOption.Option option;
        private final Object value;
        
        private Tuple<BlobInfo, Storage.BlobWriteOption> toWriteOption(final BlobInfo a1) {
            BlobId v1 = /*EL:319*/a1.getBlobId();
            /*SL:320*/switch (this.option) {
                case PREDEFINED_ACL: {
                    /*SL:322*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1, /*EL:323*/(Object)Storage.BlobWriteOption.predefinedAcl((Storage.PredefinedAcl)this.value));
                }
                case IF_GENERATION_MATCH: {
                    /*SL:325*/v1 = BlobId.of(v1.getBucket(), v1.getName(), (Long)this.value);
                    /*SL:326*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1.toBuilder().setBlobId(/*EL:327*/v1).build(), /*EL:328*/(Object)Storage.BlobWriteOption.generationMatch());
                }
                case IF_GENERATION_NOT_MATCH: {
                    /*SL:330*/v1 = BlobId.of(v1.getBucket(), v1.getName(), (Long)this.value);
                    /*SL:331*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1.toBuilder().setBlobId(/*EL:332*/v1).build(), /*EL:333*/(Object)Storage.BlobWriteOption.generationNotMatch());
                }
                case IF_METAGENERATION_MATCH: {
                    /*SL:335*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1.toBuilder().setMetageneration(/*EL:336*/(Long)this.value).build(), /*EL:337*/(Object)Storage.BlobWriteOption.metagenerationMatch());
                }
                case IF_METAGENERATION_NOT_MATCH: {
                    /*SL:339*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1.toBuilder().setMetageneration(/*EL:340*/(Long)this.value).build(), /*EL:341*/(Object)Storage.BlobWriteOption.metagenerationNotMatch());
                }
                case IF_MD5_MATCH: {
                    /*SL:343*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1.toBuilder().setMd5(/*EL:344*/(String)this.value).build(), /*EL:345*/(Object)Storage.BlobWriteOption.md5Match());
                }
                case IF_CRC32C_MATCH: {
                    /*SL:347*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1.toBuilder().setCrc32c(/*EL:348*/(String)this.value).build(), /*EL:349*/(Object)Storage.BlobWriteOption.crc32cMatch());
                }
                case CUSTOMER_SUPPLIED_KEY: {
                    /*SL:351*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1, (Object)Storage.BlobWriteOption.encryptionKey((String)this.value));
                }
                case KMS_KEY_NAME: {
                    /*SL:353*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1, (Object)Storage.BlobWriteOption.kmsKeyName((String)this.value));
                }
                case USER_PROJECT: {
                    /*SL:355*/return (Tuple<BlobInfo, Storage.BlobWriteOption>)Tuple.of((Object)a1, (Object)Storage.BlobWriteOption.userProject((String)this.value));
                }
                default: {
                    /*SL:357*/throw new AssertionError((Object)"Unexpected enum value");
                }
            }
        }
        
        private BlobWriteOption(final Storage.BlobWriteOption.Option a1, final Object a2) {
            this.option = a1;
            this.value = a2;
        }
        
        @Override
        public int hashCode() {
            /*SL:368*/return Objects.hash(this.option, this.value);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:373*/if (a1 == null) {
                /*SL:374*/return false;
            }
            /*SL:376*/if (!(a1 instanceof BlobWriteOption)) {
                /*SL:377*/return false;
            }
            final BlobWriteOption v1 = /*EL:379*/(BlobWriteOption)a1;
            /*SL:380*/return this.option == v1.option && Objects.equals(this.value, v1.value);
        }
        
        public static BlobWriteOption predefinedAcl(final Storage.PredefinedAcl a1) {
            /*SL:385*/return new BlobWriteOption(Storage.BlobWriteOption.Option.PREDEFINED_ACL, a1);
        }
        
        public static BlobWriteOption doesNotExist() {
            /*SL:394*/return new BlobWriteOption(Storage.BlobWriteOption.Option.IF_GENERATION_MATCH, 0L);
        }
        
        public static BlobWriteOption generationMatch(final long a1) {
            /*SL:403*/return new BlobWriteOption(Storage.BlobWriteOption.Option.IF_GENERATION_MATCH, a1);
        }
        
        public static BlobWriteOption generationNotMatch(final long a1) {
            /*SL:412*/return /*EL:413*/new BlobWriteOption(Storage.BlobWriteOption.Option.IF_GENERATION_NOT_MATCH, a1);
        }
        
        public static BlobWriteOption metagenerationMatch(final long a1) {
            /*SL:422*/return /*EL:423*/new BlobWriteOption(Storage.BlobWriteOption.Option.IF_METAGENERATION_MATCH, a1);
        }
        
        public static BlobWriteOption metagenerationNotMatch(final long a1) {
            /*SL:432*/return /*EL:433*/new BlobWriteOption(Storage.BlobWriteOption.Option.IF_METAGENERATION_NOT_MATCH, a1);
        }
        
        public static BlobWriteOption md5Match(final String a1) {
            /*SL:441*/return new BlobWriteOption(Storage.BlobWriteOption.Option.IF_MD5_MATCH, a1);
        }
        
        public static BlobWriteOption crc32cMatch(final String a1) {
            /*SL:449*/return new BlobWriteOption(Storage.BlobWriteOption.Option.IF_CRC32C_MATCH, a1);
        }
        
        public static BlobWriteOption encryptionKey(final Key a1) {
            final String v1 = /*EL:457*/BaseEncoding.base64().encode(a1.getEncoded());
            /*SL:458*/return new BlobWriteOption(Storage.BlobWriteOption.Option.CUSTOMER_SUPPLIED_KEY, v1);
        }
        
        public static BlobWriteOption encryptionKey(final String a1) {
            /*SL:468*/return new BlobWriteOption(Storage.BlobWriteOption.Option.CUSTOMER_SUPPLIED_KEY, a1);
        }
        
        public static BlobWriteOption userProject(final String a1) {
            /*SL:476*/return new BlobWriteOption(Storage.BlobWriteOption.Option.USER_PROJECT, a1);
        }
        
        static Tuple<BlobInfo, Storage.BlobWriteOption[]> toWriteOptions(final BlobInfo v1, final BlobWriteOption... v2) {
            final Set<Storage.BlobWriteOption.Option> v3 = /*EL:482*/(Set<Storage.BlobWriteOption.Option>)Sets.<Object>immutableEnumSet((Iterable<Object>)Lists.<Object, Object>transform((List<Object>)Arrays.<F>asList((F[])v2), (Function<? super Object, ?>)BlobWriteOption.TO_ENUM));
            /*SL:483*/Preconditions.checkArgument(!v3.contains(Storage.BlobWriteOption.Option.IF_METAGENERATION_NOT_MATCH) || /*EL:484*/!v3.contains(Storage.BlobWriteOption.Option.IF_METAGENERATION_MATCH), /*EL:485*/(Object)"metagenerationMatch and metagenerationNotMatch options can not be both provided");
            /*SL:487*/Preconditions.checkArgument(!v3.contains(Storage.BlobWriteOption.Option.IF_GENERATION_NOT_MATCH) || /*EL:488*/!v3.contains(Storage.BlobWriteOption.Option.IF_GENERATION_MATCH), /*EL:489*/(Object)"Only one option of generationMatch, doesNotExist or generationNotMatch can be provided");
            final Storage.BlobWriteOption[] v4 = /*EL:491*/new Storage.BlobWriteOption[v2.length];
            BlobInfo v5 = /*EL:492*/v1;
            int v6 = /*EL:493*/0;
            /*SL:494*/for (Tuple<BlobInfo, Storage.BlobWriteOption> a2 : v2) {
                /*SL:495*/a2 = a2.toWriteOption(v5);
                /*SL:496*/v5 = (BlobInfo)a2.x();
                /*SL:497*/v4[v6++] = (Storage.BlobWriteOption)a2.y();
            }
            /*SL:499*/return (Tuple<BlobInfo, Storage.BlobWriteOption[]>)Tuple.of((Object)v5, (Object)v4);
        }
        
        static {
            TO_ENUM = new Function<BlobWriteOption, Storage.BlobWriteOption.Option>() {
                @Override
                public Storage.BlobWriteOption.Option apply(final BlobWriteOption a1) {
                    /*SL:310*/return a1.option;
                }
            };
        }
    }
    
    public static class Builder extends BucketInfo.Builder
    {
        private final Storage storage;
        private final BuilderImpl infoBuilder;
        
        Builder(final Bucket a1) {
            this.storage = a1.storage;
            this.infoBuilder = new BuilderImpl(a1);
        }
        
        @Override
        public Builder setName(final String a1) {
            /*SL:515*/this.infoBuilder.setName(a1);
            /*SL:516*/return this;
        }
        
        @Override
        Builder setGeneratedId(final String a1) {
            /*SL:521*/this.infoBuilder.setGeneratedId(a1);
            /*SL:522*/return this;
        }
        
        @Override
        Builder setOwner(final Acl.Entity a1) {
            /*SL:527*/this.infoBuilder.setOwner(a1);
            /*SL:528*/return this;
        }
        
        @Override
        Builder setSelfLink(final String a1) {
            /*SL:533*/this.infoBuilder.setSelfLink(a1);
            /*SL:534*/return this;
        }
        
        @Override
        public Builder setVersioningEnabled(final Boolean a1) {
            /*SL:539*/this.infoBuilder.setVersioningEnabled(a1);
            /*SL:540*/return this;
        }
        
        @Override
        public Builder setRequesterPays(final Boolean a1) {
            /*SL:545*/this.infoBuilder.setRequesterPays(a1);
            /*SL:546*/return this;
        }
        
        @Override
        public Builder setIndexPage(final String a1) {
            /*SL:551*/this.infoBuilder.setIndexPage(a1);
            /*SL:552*/return this;
        }
        
        @Override
        public Builder setNotFoundPage(final String a1) {
            /*SL:557*/this.infoBuilder.setNotFoundPage(a1);
            /*SL:558*/return this;
        }
        
        @Deprecated
        @Override
        public Builder setDeleteRules(final Iterable<? extends DeleteRule> a1) {
            /*SL:564*/this.infoBuilder.setDeleteRules(a1);
            /*SL:565*/return this;
        }
        
        @Override
        public Builder setLifecycleRules(final Iterable<? extends LifecycleRule> a1) {
            /*SL:570*/this.infoBuilder.setLifecycleRules(a1);
            /*SL:571*/return this;
        }
        
        @Override
        public Builder setStorageClass(final StorageClass a1) {
            /*SL:576*/this.infoBuilder.setStorageClass(a1);
            /*SL:577*/return this;
        }
        
        @Override
        public Builder setLocation(final String a1) {
            /*SL:582*/this.infoBuilder.setLocation(a1);
            /*SL:583*/return this;
        }
        
        @Override
        Builder setEtag(final String a1) {
            /*SL:588*/this.infoBuilder.setEtag(a1);
            /*SL:589*/return this;
        }
        
        @Override
        Builder setCreateTime(final Long a1) {
            /*SL:594*/this.infoBuilder.setCreateTime(a1);
            /*SL:595*/return this;
        }
        
        @Override
        Builder setMetageneration(final Long a1) {
            /*SL:600*/this.infoBuilder.setMetageneration(a1);
            /*SL:601*/return this;
        }
        
        @Override
        public Builder setCors(final Iterable<Cors> a1) {
            /*SL:606*/this.infoBuilder.setCors(a1);
            /*SL:607*/return this;
        }
        
        @Override
        public Builder setAcl(final Iterable<Acl> a1) {
            /*SL:612*/this.infoBuilder.setAcl(a1);
            /*SL:613*/return this;
        }
        
        @Override
        public Builder setDefaultAcl(final Iterable<Acl> a1) {
            /*SL:618*/this.infoBuilder.setDefaultAcl(a1);
            /*SL:619*/return this;
        }
        
        @Override
        public Builder setLabels(final Map<String, String> a1) {
            /*SL:624*/this.infoBuilder.setLabels(a1);
            /*SL:625*/return this;
        }
        
        @Override
        public Builder setDefaultKmsKeyName(final String a1) {
            /*SL:630*/this.infoBuilder.setDefaultKmsKeyName(a1);
            /*SL:631*/return this;
        }
        
        @Override
        public Builder setDefaultEventBasedHold(final Boolean a1) {
            /*SL:636*/this.infoBuilder.setDefaultEventBasedHold(a1);
            /*SL:637*/return this;
        }
        
        @Override
        Builder setRetentionEffectiveTime(final Long a1) {
            /*SL:642*/this.infoBuilder.setRetentionEffectiveTime(a1);
            /*SL:643*/return this;
        }
        
        @Override
        Builder setRetentionPolicyIsLocked(final Boolean a1) {
            /*SL:648*/this.infoBuilder.setRetentionPolicyIsLocked(a1);
            /*SL:649*/return this;
        }
        
        @Override
        public Builder setRetentionPeriod(final Long a1) {
            /*SL:654*/this.infoBuilder.setRetentionPeriod(a1);
            /*SL:655*/return this;
        }
        
        @Override
        public Builder setIamConfiguration(final IamConfiguration a1) {
            /*SL:660*/this.infoBuilder.setIamConfiguration(a1);
            /*SL:661*/return this;
        }
        
        @Override
        Builder setLocationType(final String a1) {
            /*SL:666*/this.infoBuilder.setLocationType(a1);
            /*SL:667*/return this;
        }
        
        @Override
        public Bucket build() {
            /*SL:672*/return new Bucket(this.storage, this.infoBuilder);
        }
    }
}
