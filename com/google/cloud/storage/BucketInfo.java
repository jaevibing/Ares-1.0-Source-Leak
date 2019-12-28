package com.google.cloud.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import com.google.common.base.Functions;
import java.util.Set;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.HashSet;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.BucketAccessControl;
import com.google.common.collect.Lists;
import com.google.api.client.util.DateTime;
import com.google.common.base.MoreObjects;
import java.util.Objects;
import com.google.api.core.BetaApi;
import com.google.api.client.util.Data;
import java.util.Map;
import java.util.List;
import com.google.api.services.storage.model.Bucket;
import com.google.common.base.Function;
import java.io.Serializable;

public class BucketInfo implements Serializable
{
    static final Function<Bucket, BucketInfo> FROM_PB_FUNCTION;
    static final Function<BucketInfo, Bucket> TO_PB_FUNCTION;
    private static final long serialVersionUID = -4712013629621638459L;
    private final String generatedId;
    private final String name;
    private final Acl.Entity owner;
    private final String selfLink;
    private final Boolean requesterPays;
    private final Boolean versioningEnabled;
    private final String indexPage;
    private final String notFoundPage;
    private final List<DeleteRule> deleteRules;
    private final List<LifecycleRule> lifecycleRules;
    private final String etag;
    private final Long createTime;
    private final Long metageneration;
    private final List<Cors> cors;
    private final List<Acl> acl;
    private final List<Acl> defaultAcl;
    private final String location;
    private final StorageClass storageClass;
    private final Map<String, String> labels;
    private final String defaultKmsKeyName;
    private final Boolean defaultEventBasedHold;
    private final Long retentionEffectiveTime;
    private final Boolean retentionPolicyIsLocked;
    private final Long retentionPeriod;
    private final IamConfiguration iamConfiguration;
    private final String locationType;
    
    BucketInfo(final BuilderImpl a1) {
        this.generatedId = a1.generatedId;
        this.name = a1.name;
        this.etag = a1.etag;
        this.createTime = a1.createTime;
        this.metageneration = a1.metageneration;
        this.location = a1.location;
        this.storageClass = a1.storageClass;
        this.cors = a1.cors;
        this.acl = a1.acl;
        this.defaultAcl = a1.defaultAcl;
        this.owner = a1.owner;
        this.selfLink = a1.selfLink;
        this.versioningEnabled = a1.versioningEnabled;
        this.indexPage = a1.indexPage;
        this.notFoundPage = a1.notFoundPage;
        this.deleteRules = a1.deleteRules;
        this.lifecycleRules = a1.lifecycleRules;
        this.labels = a1.labels;
        this.requesterPays = a1.requesterPays;
        this.defaultKmsKeyName = a1.defaultKmsKeyName;
        this.defaultEventBasedHold = a1.defaultEventBasedHold;
        this.retentionEffectiveTime = a1.retentionEffectiveTime;
        this.retentionPolicyIsLocked = a1.retentionPolicyIsLocked;
        this.retentionPeriod = a1.retentionPeriod;
        this.iamConfiguration = a1.iamConfiguration;
        this.locationType = a1.locationType;
    }
    
    public String getGeneratedId() {
        /*SL:1178*/return this.generatedId;
    }
    
    public String getName() {
        /*SL:1183*/return this.name;
    }
    
    public Acl.Entity getOwner() {
        /*SL:1188*/return this.owner;
    }
    
    public String getSelfLink() {
        /*SL:1193*/return this.selfLink;
    }
    
    public Boolean versioningEnabled() {
        /*SL:1218*/return Data.isNull(this.versioningEnabled) ? null : this.versioningEnabled;
    }
    
    public Boolean requesterPays() {
        /*SL:1238*/return Data.isNull(this.requesterPays) ? null : this.requesterPays;
    }
    
    public String getIndexPage() {
        /*SL:1246*/return this.indexPage;
    }
    
    public String getNotFoundPage() {
        /*SL:1251*/return this.notFoundPage;
    }
    
    @Deprecated
    public List<? extends DeleteRule> getDeleteRules() {
        /*SL:1261*/return this.deleteRules;
    }
    
    public List<? extends LifecycleRule> getLifecycleRules() {
        /*SL:1265*/return this.lifecycleRules;
    }
    
    public String getEtag() {
        /*SL:1274*/return this.etag;
    }
    
    public Long getCreateTime() {
        /*SL:1279*/return this.createTime;
    }
    
    public Long getMetageneration() {
        /*SL:1284*/return this.metageneration;
    }
    
    public String getLocation() {
        /*SL:1294*/return this.location;
    }
    
    public String getLocationType() {
        /*SL:1303*/return this.locationType;
    }
    
    public StorageClass getStorageClass() {
        /*SL:1313*/return this.storageClass;
    }
    
    public List<Cors> getCors() {
        /*SL:1323*/return this.cors;
    }
    
    public List<Acl> getAcl() {
        /*SL:1333*/return this.acl;
    }
    
    public List<Acl> getDefaultAcl() {
        /*SL:1343*/return this.defaultAcl;
    }
    
    public Map<String, String> getLabels() {
        /*SL:1348*/return this.labels;
    }
    
    public String getDefaultKmsKeyName() {
        /*SL:1353*/return this.defaultKmsKeyName;
    }
    
    @BetaApi
    public Boolean getDefaultEventBasedHold() {
        /*SL:1382*/return Data.isNull(this.defaultEventBasedHold) ? null : this.defaultEventBasedHold;
    }
    
    @BetaApi
    public Long getRetentionEffectiveTime() {
        /*SL:1391*/return this.retentionEffectiveTime;
    }
    
    @BetaApi
    public Boolean retentionPolicyIsLocked() {
        /*SL:1412*/return Data.isNull(this.retentionPolicyIsLocked) ? null : this.retentionPolicyIsLocked;
    }
    
    @BetaApi
    public Long getRetentionPeriod() {
        /*SL:1418*/return this.retentionPeriod;
    }
    
    @BetaApi
    public IamConfiguration getIamConfiguration() {
        /*SL:1424*/return this.iamConfiguration;
    }
    
    public Builder toBuilder() {
        /*SL:1429*/return new BuilderImpl(this);
    }
    
    @Override
    public int hashCode() {
        /*SL:1434*/return Objects.hash(this.name);
    }
    
    @Override
    public boolean equals(final Object a1) {
        /*SL:1439*/return a1 == this || (a1 != null && a1.getClass().equals(/*EL:1441*/BucketInfo.class) && /*EL:1442*/Objects.equals(this.toPb(), ((BucketInfo)a1).toPb()));
    }
    
    @Override
    public String toString() {
        /*SL:1447*/return MoreObjects.toStringHelper((Object)this).add("name", (Object)this.name).toString();
    }
    
    Bucket toPb() {
        final Bucket v0 = /*EL:1451*/new Bucket();
        /*SL:1453*/v0.setId(this.generatedId);
        /*SL:1454*/v0.setName(this.name);
        /*SL:1455*/v0.setEtag(this.etag);
        /*SL:1456*/if (this.createTime != null) {
            /*SL:1457*/v0.setTimeCreated(new DateTime(this.createTime));
        }
        /*SL:1459*/if (this.metageneration != null) {
            /*SL:1460*/v0.setMetageneration(this.metageneration);
        }
        /*SL:1462*/if (this.location != null) {
            /*SL:1463*/v0.setLocation(this.location);
        }
        /*SL:1465*/if (this.locationType != null) {
            /*SL:1466*/v0.setLocationType(this.locationType);
        }
        /*SL:1468*/if (this.storageClass != null) {
            /*SL:1469*/v0.setStorageClass(this.storageClass.toString());
        }
        /*SL:1471*/if (this.cors != null) {
            /*SL:1472*/v0.setCors((List)Lists.<Cors, Object>transform(this.cors, (Function<? super Cors, ?>)Cors.TO_PB_FUNCTION));
        }
        /*SL:1474*/if (this.acl != null) {
            /*SL:1475*/v0.setAcl(/*EL:1476*/(List)Lists.<Acl, Object>transform(this.acl, (Function<? super Acl, ?>)new Function<Acl, BucketAccessControl>() {
                @Override
                public BucketAccessControl apply(final Acl a1) {
                    /*SL:1481*/return a1.toBucketPb();
                }
            }));
        }
        /*SL:1485*/if (this.defaultAcl != null) {
            /*SL:1486*/v0.setDefaultObjectAcl(/*EL:1487*/(List)Lists.<Acl, Object>transform(this.defaultAcl, (Function<? super Acl, ?>)new Function<Acl, ObjectAccessControl>() {
                @Override
                public ObjectAccessControl apply(final Acl a1) {
                    /*SL:1492*/return a1.toObjectPb();
                }
            }));
        }
        /*SL:1496*/if (this.owner != null) {
            /*SL:1497*/v0.setOwner(new Bucket.Owner().setEntity(this.owner.toPb()));
        }
        /*SL:1499*/v0.setSelfLink(this.selfLink);
        /*SL:1500*/if (this.versioningEnabled != null) {
            /*SL:1501*/v0.setVersioning(new Bucket.Versioning().setEnabled(this.versioningEnabled));
        }
        /*SL:1503*/if (this.requesterPays != null) {
            final Bucket.Billing v = /*EL:1504*/new Bucket.Billing();
            /*SL:1505*/v.setRequesterPays(this.requesterPays);
            /*SL:1506*/v0.setBilling(v);
        }
        /*SL:1508*/if (this.indexPage != null || this.notFoundPage != null) {
            final Bucket.Website v2 = /*EL:1509*/new Bucket.Website();
            /*SL:1510*/v2.setMainPageSuffix(this.indexPage);
            /*SL:1511*/v2.setNotFoundPage(this.notFoundPage);
            /*SL:1512*/v0.setWebsite(v2);
        }
        final Set<Bucket.Lifecycle.Rule> v3 = /*EL:1514*/new HashSet<Bucket.Lifecycle.Rule>();
        /*SL:1515*/if (this.deleteRules != null) {
            /*SL:1516*/v3.addAll(/*EL:1517*/(Collection<? extends Bucket.Lifecycle.Rule>)Lists.<DeleteRule, Object>transform(this.deleteRules, (Function<? super DeleteRule, ?>)new Function<DeleteRule, Bucket.Lifecycle.Rule>() {
                @Override
                public Bucket.Lifecycle.Rule apply(final DeleteRule a1) {
                    /*SL:1522*/return a1.toPb();
                }
            }));
        }
        /*SL:1526*/if (this.lifecycleRules != null) {
            /*SL:1527*/v3.addAll(/*EL:1528*/(Collection<? extends Bucket.Lifecycle.Rule>)Lists.<LifecycleRule, Object>transform(this.lifecycleRules, (Function<? super LifecycleRule, ?>)new Function<LifecycleRule, Bucket.Lifecycle.Rule>() {
                @Override
                public Bucket.Lifecycle.Rule apply(final LifecycleRule a1) {
                    /*SL:1533*/return a1.toPb();
                }
            }));
        }
        /*SL:1537*/if (!v3.isEmpty()) {
            final Bucket.Lifecycle v4 = /*EL:1538*/new Bucket.Lifecycle();
            /*SL:1539*/v4.setRule((List)ImmutableList.<Object>copyOf((Collection<?>)v3));
            /*SL:1540*/v0.setLifecycle(v4);
        }
        /*SL:1542*/if (this.labels != null) {
            /*SL:1543*/v0.setLabels((Map)this.labels);
        }
        /*SL:1545*/if (this.defaultKmsKeyName != null) {
            /*SL:1546*/v0.setEncryption(new Bucket.Encryption().setDefaultKmsKeyName(this.defaultKmsKeyName));
        }
        /*SL:1548*/if (this.defaultEventBasedHold != null) {
            /*SL:1549*/v0.setDefaultEventBasedHold(this.defaultEventBasedHold);
        }
        /*SL:1551*/if (this.retentionPeriod != null) {
            /*SL:1552*/if (Data.isNull(this.retentionPeriod)) {
                /*SL:1553*/v0.setRetentionPolicy(/*EL:1554*/(Bucket.RetentionPolicy)Data.<Bucket.RetentionPolicy>nullOf(Bucket.RetentionPolicy.class));
            }
            else {
                final Bucket.RetentionPolicy v5 = /*EL:1556*/new Bucket.RetentionPolicy();
                /*SL:1557*/v5.setRetentionPeriod(this.retentionPeriod);
                /*SL:1558*/if (this.retentionEffectiveTime != null) {
                    /*SL:1559*/v5.setEffectiveTime(new DateTime(this.retentionEffectiveTime));
                }
                /*SL:1561*/if (this.retentionPolicyIsLocked != null) {
                    /*SL:1562*/v5.setIsLocked(this.retentionPolicyIsLocked);
                }
                /*SL:1564*/v0.setRetentionPolicy(v5);
            }
        }
        /*SL:1567*/if (this.iamConfiguration != null) {
            /*SL:1568*/v0.setIamConfiguration(this.iamConfiguration.toPb());
        }
        /*SL:1571*/return v0;
    }
    
    public static BucketInfo of(final String a1) {
        /*SL:1576*/return newBuilder(a1).build();
    }
    
    public static Builder newBuilder(final String a1) {
        /*SL:1581*/return new BuilderImpl(a1);
    }
    
    static BucketInfo fromPb(final Bucket a1) {
        final Builder v1 = /*EL:1585*/new BuilderImpl(a1.getName());
        /*SL:1586*/if (a1.getId() != null) {
            /*SL:1587*/v1.setGeneratedId(a1.getId());
        }
        /*SL:1589*/if (a1.getEtag() != null) {
            /*SL:1590*/v1.setEtag(a1.getEtag());
        }
        /*SL:1592*/if (a1.getMetageneration() != null) {
            /*SL:1593*/v1.setMetageneration(a1.getMetageneration());
        }
        /*SL:1595*/if (a1.getSelfLink() != null) {
            /*SL:1596*/v1.setSelfLink(a1.getSelfLink());
        }
        /*SL:1598*/if (a1.getTimeCreated() != null) {
            /*SL:1599*/v1.setCreateTime(a1.getTimeCreated().getValue());
        }
        /*SL:1601*/if (a1.getLocation() != null) {
            /*SL:1602*/v1.setLocation(a1.getLocation());
        }
        /*SL:1604*/if (a1.getStorageClass() != null) {
            /*SL:1605*/v1.setStorageClass(StorageClass.valueOf(a1.getStorageClass()));
        }
        /*SL:1607*/if (a1.getCors() != null) {
            /*SL:1608*/v1.setCors((Iterable<Cors>)Lists.<Object, Object>transform((List<Object>)a1.getCors(), (Function<? super Object, ?>)Cors.FROM_PB_FUNCTION));
        }
        /*SL:1610*/if (a1.getAcl() != null) {
            /*SL:1611*/v1.setAcl(/*EL:1612*/(Iterable<Acl>)Lists.<Object, Object>transform((List<Object>)a1.getAcl(), /*EL:1613*/(Function<? super Object, ?>)new Function<BucketAccessControl, Acl>() {
                @Override
                public Acl apply(final BucketAccessControl a1) {
                    /*SL:1617*/return Acl.fromPb(a1);
                }
            }));
        }
        /*SL:1621*/if (a1.getDefaultObjectAcl() != null) {
            /*SL:1622*/v1.setDefaultAcl(/*EL:1623*/(Iterable<Acl>)Lists.<Object, Object>transform((List<Object>)a1.getDefaultObjectAcl(), /*EL:1624*/(Function<? super Object, ?>)new Function<ObjectAccessControl, Acl>() {
                @Override
                public Acl apply(final ObjectAccessControl a1) {
                    /*SL:1628*/return Acl.fromPb(a1);
                }
            }));
        }
        /*SL:1632*/if (a1.getOwner() != null) {
            /*SL:1633*/v1.setOwner(Acl.Entity.fromPb(a1.getOwner().getEntity()));
        }
        /*SL:1635*/if (a1.getVersioning() != null) {
            /*SL:1636*/v1.setVersioningEnabled(a1.getVersioning().getEnabled());
        }
        final Bucket.Website v2 = /*EL:1638*/a1.getWebsite();
        /*SL:1639*/if (v2 != null) {
            /*SL:1640*/v1.setIndexPage(v2.getMainPageSuffix());
            /*SL:1641*/v1.setNotFoundPage(v2.getNotFoundPage());
        }
        /*SL:1643*/if (a1.getLifecycle() != null && a1.getLifecycle().getRule() != null) {
            /*SL:1644*/v1.setLifecycleRules(/*EL:1645*/(Iterable<? extends LifecycleRule>)Lists.<Object, Object>transform((List<Object>)a1.getLifecycle().getRule(), /*EL:1646*/(Function<? super Object, ?>)new Function<Bucket.Lifecycle.Rule, LifecycleRule>() {
                @Override
                public LifecycleRule apply(final Bucket.Lifecycle.Rule a1) {
                    /*SL:1650*/return LifecycleRule.fromPb(a1);
                }
            }));
            /*SL:1653*/v1.setDeleteRules(/*EL:1654*/(Iterable<? extends DeleteRule>)Lists.<Object, Object>transform((List<Object>)a1.getLifecycle().getRule(), /*EL:1655*/(Function<? super Object, ?>)new Function<Bucket.Lifecycle.Rule, DeleteRule>() {
                @Override
                public DeleteRule apply(final Bucket.Lifecycle.Rule a1) {
                    /*SL:1659*/return DeleteRule.fromPb(a1);
                }
            }));
        }
        /*SL:1663*/if (a1.getLabels() != null) {
            /*SL:1664*/v1.setLabels(a1.getLabels());
        }
        final Bucket.Billing v3 = /*EL:1666*/a1.getBilling();
        /*SL:1667*/if (v3 != null) {
            /*SL:1668*/v1.setRequesterPays(v3.getRequesterPays());
        }
        final Bucket.Encryption v4 = /*EL:1670*/a1.getEncryption();
        /*SL:1672*/if (v4 != null && v4.getDefaultKmsKeyName() != null && !v4.getDefaultKmsKeyName().isEmpty()) {
            /*SL:1674*/v1.setDefaultKmsKeyName(v4.getDefaultKmsKeyName());
        }
        /*SL:1676*/if (a1.getDefaultEventBasedHold() != null) {
            /*SL:1677*/v1.setDefaultEventBasedHold(a1.getDefaultEventBasedHold());
        }
        final Bucket.RetentionPolicy v5 = /*EL:1679*/a1.getRetentionPolicy();
        /*SL:1680*/if (v5 != null) {
            /*SL:1681*/if (v5.getEffectiveTime() != null) {
                /*SL:1682*/v1.setRetentionEffectiveTime(v5.getEffectiveTime().getValue());
            }
            /*SL:1684*/if (v5.getIsLocked() != null) {
                /*SL:1685*/v1.setRetentionPolicyIsLocked(v5.getIsLocked());
            }
            /*SL:1687*/if (v5.getRetentionPeriod() != null) {
                /*SL:1688*/v1.setRetentionPeriod(v5.getRetentionPeriod());
            }
        }
        final Bucket.IamConfiguration v6 = /*EL:1691*/a1.getIamConfiguration();
        /*SL:1693*/if (a1.getLocationType() != null) {
            /*SL:1694*/v1.setLocationType(a1.getLocationType());
        }
        /*SL:1697*/if (v6 != null) {
            /*SL:1698*/v1.setIamConfiguration(IamConfiguration.fromPb(v6));
        }
        /*SL:1700*/return v1.build();
    }
    
    static {
        FROM_PB_FUNCTION = new Function<Bucket, BucketInfo>() {
            @Override
            public BucketInfo apply(final Bucket a1) {
                /*SL:63*/return BucketInfo.fromPb(a1);
            }
        };
        TO_PB_FUNCTION = new Function<BucketInfo, Bucket>() {
            @Override
            public Bucket apply(final BucketInfo a1) {
                /*SL:70*/return a1.toPb();
            }
        };
    }
    
    public static class IamConfiguration implements Serializable
    {
        private static final long serialVersionUID = -8671736104909424616L;
        private Boolean isBucketPolicyOnlyEnabled;
        private Long bucketPolicyOnlyLockedTime;
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:114*/if (this == a1) {
                return true;
            }
            /*SL:115*/if (a1 == null || this.getClass() != a1.getClass()) {
                /*SL:116*/return false;
            }
            final IamConfiguration v1 = /*EL:118*/(IamConfiguration)a1;
            /*SL:119*/return Objects.equals(this.toPb(), v1.toPb());
        }
        
        @Override
        public int hashCode() {
            /*SL:124*/return Objects.hash(this.isBucketPolicyOnlyEnabled, this.bucketPolicyOnlyLockedTime);
        }
        
        private IamConfiguration(final Builder a1) {
            this.isBucketPolicyOnlyEnabled = a1.isBucketPolicyOnlyEnabled;
            this.bucketPolicyOnlyLockedTime = a1.bucketPolicyOnlyLockedTime;
        }
        
        public static Builder newBuilder() {
            /*SL:133*/return new Builder();
        }
        
        public Builder toBuilder() {
            final Builder v1 = /*EL:137*/new Builder();
            /*SL:138*/v1.isBucketPolicyOnlyEnabled = this.isBucketPolicyOnlyEnabled;
            /*SL:139*/v1.bucketPolicyOnlyLockedTime = this.bucketPolicyOnlyLockedTime;
            /*SL:140*/return v1;
        }
        
        public Boolean isBucketPolicyOnlyEnabled() {
            /*SL:144*/return this.isBucketPolicyOnlyEnabled;
        }
        
        public Long getBucketPolicyOnlyLockedTime() {
            /*SL:148*/return this.bucketPolicyOnlyLockedTime;
        }
        
        Bucket.IamConfiguration toPb() {
            final Bucket.IamConfiguration v1 = /*EL:152*/new Bucket.IamConfiguration();
            final Bucket.IamConfiguration.BucketPolicyOnly v2 = /*EL:154*/new Bucket.IamConfiguration.BucketPolicyOnly();
            /*SL:156*/v2.setEnabled(this.isBucketPolicyOnlyEnabled);
            /*SL:157*/v2.setLockedTime((this.bucketPolicyOnlyLockedTime == null) ? null : new DateTime(this.bucketPolicyOnlyLockedTime));
            /*SL:160*/v1.setBucketPolicyOnly(v2);
            /*SL:161*/return v1;
        }
        
        static IamConfiguration fromPb(final Bucket.IamConfiguration a1) {
            final Bucket.IamConfiguration.BucketPolicyOnly v1 = /*EL:165*/a1.getBucketPolicyOnly();
            final DateTime v2 = /*EL:167*/v1.getLockedTime();
            /*SL:169*/return newBuilder().setIsBucketPolicyOnlyEnabled(v1.getEnabled()).setBucketPolicyOnlyLockedTime(/*EL:170*/(v2 == null) ? null : v2.getValue()).build();
        }
        
        public static class Builder
        {
            private Boolean isBucketPolicyOnlyEnabled;
            private Long bucketPolicyOnlyLockedTime;
            
            public Builder setIsBucketPolicyOnlyEnabled(final Boolean a1) {
                /*SL:189*/this.isBucketPolicyOnlyEnabled = a1;
                /*SL:190*/return this;
            }
            
            Builder setBucketPolicyOnlyLockedTime(final Long a1) {
                /*SL:200*/this.bucketPolicyOnlyLockedTime = a1;
                /*SL:201*/return this;
            }
            
            public IamConfiguration build() {
                /*SL:206*/return new IamConfiguration(this);
            }
        }
    }
    
    public static class LifecycleRule implements Serializable
    {
        private static final long serialVersionUID = -5739807320148748613L;
        private final LifecycleAction lifecycleAction;
        private final LifecycleCondition lifecycleCondition;
        
        public LifecycleRule(final LifecycleAction a1, final LifecycleCondition a2) {
            if (a2.getIsLive() == null && a2.getAge() == null && a2.getCreatedBefore() == null && a2.getMatchesStorageClass() == null && a2.getNumberOfNewerVersions() == null) {
                throw new IllegalArgumentException("You must specify at least one condition to use object lifecycle management. Please see https://cloud.google.com/storage/docs/lifecycle for details.");
            }
            this.lifecycleAction = a1;
            this.lifecycleCondition = a2;
        }
        
        public LifecycleAction getAction() {
            /*SL:240*/return this.lifecycleAction;
        }
        
        public LifecycleCondition getCondition() {
            /*SL:244*/return this.lifecycleCondition;
        }
        
        @Override
        public int hashCode() {
            /*SL:249*/return Objects.hash(this.lifecycleAction, this.lifecycleCondition);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:254*/if (this == a1) {
                /*SL:255*/return true;
            }
            /*SL:257*/if (a1 == null || this.getClass() != a1.getClass()) {
                /*SL:258*/return false;
            }
            final LifecycleRule v1 = /*EL:260*/(LifecycleRule)a1;
            /*SL:261*/return Objects.equals(this.toPb(), v1.toPb());
        }
        
        Bucket.Lifecycle.Rule toPb() {
            final Bucket.Lifecycle.Rule v1 = /*EL:265*/new Bucket.Lifecycle.Rule();
            final Bucket.Lifecycle.Rule.Action v2 = /*EL:267*/new Bucket.Lifecycle.Rule.Action().setType(this.lifecycleAction.getActionType());
            /*SL:268*/if (this.lifecycleAction.getActionType().equals("SetStorageClass")) {
                /*SL:269*/v2.setStorageClass(((SetStorageClassLifecycleAction)this.lifecycleAction).getStorageClass().toString());
            }
            /*SL:273*/v1.setAction(v2);
            final Bucket.Lifecycle.Rule.Condition v3 = /*EL:275*/new Bucket.Lifecycle.Rule.Condition().setAge(this.lifecycleCondition.getAge()).setCreatedBefore(/*EL:277*/(this.lifecycleCondition.getCreatedBefore() == /*EL:279*/null) ? null : new DateTime(true, this.lifecycleCondition.getCreatedBefore().getValue(), /*EL:281*/0)).setIsLive(this.lifecycleCondition.getIsLive()).setNumNewerVersions(/*EL:282*/this.lifecycleCondition.getNumberOfNewerVersions()).setMatchesStorageClass(/*EL:283*/(List)((this.lifecycleCondition.getMatchesStorageClass() == /*EL:285*/null) ? null : /*EL:287*/Lists.<StorageClass, Object>transform(this.lifecycleCondition.getMatchesStorageClass(), /*EL:289*/(Function<? super StorageClass, ?>)Functions.toStringFunction())));
            /*SL:291*/v1.setCondition(v3);
            /*SL:293*/return v1;
        }
        
        static LifecycleRule fromPb(final Bucket.Lifecycle.Rule v0) {
            final Bucket.Lifecycle.Rule.Action v = /*EL:299*/v0.getAction();
            final String type = /*EL:301*/v.getType();
            final LifecycleAction v2;
            switch (type) {
                case "Delete": {
                    final LifecycleAction a1 = /*EL:303*/LifecycleAction.newDeleteAction();
                    /*SL:304*/break;
                }
                case "SetStorageClass": {
                    /*SL:307*/v2 = LifecycleAction.newSetStorageClassAction(/*EL:308*/StorageClass.valueOf(v.getStorageClass()));
                    /*SL:309*/break;
                }
                default: {
                    /*SL:311*/throw new UnsupportedOperationException("The specified lifecycle action " + v.getType() + /*EL:312*/" is not currently supported");
                }
            }
            final Bucket.Lifecycle.Rule.Condition v3 = /*EL:315*/v0.getCondition();
            final LifecycleCondition.Builder v4 = /*EL:318*/LifecycleCondition.newBuilder().setAge(v3.getAge()).setCreatedBefore(/*EL:319*/v3.getCreatedBefore()).setIsLive(/*EL:320*/v3.getIsLive()).setNumberOfNewerVersions(/*EL:321*/v3.getNumNewerVersions()).setMatchesStorageClass(/*EL:322*/(v3.getMatchesStorageClass() == /*EL:324*/null) ? null : /*EL:326*/Lists.<Object, StorageClass>transform((List<Object>)v3.getMatchesStorageClass(), /*EL:327*/(Function<? super Object, ? extends StorageClass>)new Function<String, StorageClass>() {
                @Override
                public StorageClass apply(final String a1) {
                    /*SL:330*/return StorageClass.valueOf(a1);
                }
            }));
            /*SL:334*/return new LifecycleRule(v2, v4.build());
        }
        
        public static class LifecycleCondition implements Serializable
        {
            private static final long serialVersionUID = -6482314338394768785L;
            private final Integer age;
            private final DateTime createdBefore;
            private final Integer numberOfNewerVersions;
            private final Boolean isLive;
            private final List<StorageClass> matchesStorageClass;
            
            private LifecycleCondition(final Builder a1) {
                this.age = a1.age;
                this.createdBefore = a1.createdBefore;
                this.numberOfNewerVersions = a1.numberOfNewerVersions;
                this.isLive = a1.isLive;
                this.matchesStorageClass = a1.matchesStorageClass;
            }
            
            public Builder toBuilder() {
                /*SL:360*/return newBuilder().setAge(this.age).setCreatedBefore(/*EL:361*/this.createdBefore).setNumberOfNewerVersions(/*EL:362*/this.numberOfNewerVersions).setIsLive(/*EL:363*/this.isLive).setMatchesStorageClass(/*EL:364*/this.matchesStorageClass);
            }
            
            public static Builder newBuilder() {
                /*SL:369*/return new Builder();
            }
            
            public Integer getAge() {
                /*SL:373*/return this.age;
            }
            
            public DateTime getCreatedBefore() {
                /*SL:377*/return this.createdBefore;
            }
            
            public Integer getNumberOfNewerVersions() {
                /*SL:381*/return this.numberOfNewerVersions;
            }
            
            public Boolean getIsLive() {
                /*SL:385*/return this.isLive;
            }
            
            public List<StorageClass> getMatchesStorageClass() {
                /*SL:389*/return this.matchesStorageClass;
            }
            
            public static class Builder
            {
                private Integer age;
                private DateTime createdBefore;
                private Integer numberOfNewerVersions;
                private Boolean isLive;
                private List<StorageClass> matchesStorageClass;
                
                public Builder setAge(final Integer a1) {
                    /*SL:410*/this.age = a1;
                    /*SL:411*/return this;
                }
                
                public Builder setCreatedBefore(final DateTime a1) {
                    /*SL:421*/this.createdBefore = a1;
                    /*SL:422*/return this;
                }
                
                public Builder setNumberOfNewerVersions(final Integer a1) {
                    /*SL:430*/this.numberOfNewerVersions = a1;
                    /*SL:431*/return this;
                }
                
                public Builder setIsLive(final Boolean a1) {
                    /*SL:440*/this.isLive = a1;
                    /*SL:441*/return this;
                }
                
                public Builder setMatchesStorageClass(final List<StorageClass> a1) {
                    /*SL:449*/this.matchesStorageClass = a1;
                    /*SL:450*/return this;
                }
                
                public LifecycleCondition build() {
                    /*SL:455*/return new LifecycleCondition(this);
                }
            }
        }
        
        public abstract static class LifecycleAction implements Serializable
        {
            private static final long serialVersionUID = 5801228724709173284L;
            
            public abstract String getActionType();
            
            public static DeleteLifecycleAction newDeleteAction() {
                /*SL:474*/return new DeleteLifecycleAction();
            }
            
            public static SetStorageClassLifecycleAction newSetStorageClassAction(final StorageClass a1) {
                /*SL:485*/return new SetStorageClassLifecycleAction(a1);
            }
        }
        
        public static class DeleteLifecycleAction extends LifecycleAction
        {
            public static final String TYPE = "Delete";
            private static final long serialVersionUID = -2050986302222644873L;
            
            @Override
            public String getActionType() {
                /*SL:497*/return "Delete";
            }
        }
        
        public static class SetStorageClassLifecycleAction extends LifecycleAction
        {
            public static final String TYPE = "SetStorageClass";
            private static final long serialVersionUID = -62615467186000899L;
            private final StorageClass storageClass;
            
            private SetStorageClassLifecycleAction(final StorageClass a1) {
                this.storageClass = a1;
            }
            
            @Override
            public String getActionType() {
                /*SL:513*/return "SetStorageClass";
            }
            
            StorageClass getStorageClass() {
                /*SL:517*/return this.storageClass;
            }
        }
    }
    
    @Deprecated
    public abstract static class DeleteRule implements Serializable
    {
        private static final long serialVersionUID = 3137971668395933033L;
        private static final String SUPPORTED_ACTION = "Delete";
        private final Type type;
        
        DeleteRule(final Type a1) {
            this.type = a1;
        }
        
        public Type getType() {
            /*SL:550*/return this.type;
        }
        
        @Override
        public int hashCode() {
            /*SL:555*/return Objects.hash(this.type);
        }
        
        @Override
        public boolean equals(final Object a1) {
            /*SL:560*/if (this == a1) {
                /*SL:561*/return true;
            }
            /*SL:563*/if (a1 == null || this.getClass() != a1.getClass()) {
                /*SL:564*/return false;
            }
            final DeleteRule v1 = /*EL:566*/(DeleteRule)a1;
            /*SL:567*/return Objects.equals(this.toPb(), v1.toPb());
        }
        
        Bucket.Lifecycle.Rule toPb() {
            final Bucket.Lifecycle.Rule v1 = /*EL:571*/new Bucket.Lifecycle.Rule();
            /*SL:572*/v1.setAction(new Bucket.Lifecycle.Rule.Action().setType("Delete"));
            final Bucket.Lifecycle.Rule.Condition v2 = /*EL:573*/new Bucket.Lifecycle.Rule.Condition();
            /*SL:574*/this.populateCondition(v2);
            /*SL:575*/v1.setCondition(v2);
            /*SL:576*/return v1;
        }
        
        abstract void populateCondition(final Bucket.Lifecycle.Rule.Condition p0);
        
        static DeleteRule fromPb(final Bucket.Lifecycle.Rule v-1) {
            /*SL:582*/if (v-1.getAction() != null && "Delete".endsWith(v-1.getAction().getType())) {
                final Bucket.Lifecycle.Rule.Condition a1 = /*EL:583*/v-1.getCondition();
                final Integer v1 = /*EL:584*/a1.getAge();
                /*SL:585*/if (v1 != null) {
                    /*SL:586*/return new AgeDeleteRule(v1);
                }
                final DateTime v2 = /*EL:588*/a1.getCreatedBefore();
                /*SL:589*/if (v2 != null) {
                    /*SL:590*/return new CreatedBeforeDeleteRule(v2.getValue());
                }
                final Integer v3 = /*EL:592*/a1.getNumNewerVersions();
                /*SL:593*/if (v3 != null) {
                    /*SL:594*/return new NumNewerVersionsDeleteRule(v3);
                }
                final Boolean v4 = /*EL:596*/a1.getIsLive();
                /*SL:597*/if (v4 != null) {
                    /*SL:598*/return new IsLiveDeleteRule(v4);
                }
            }
            /*SL:601*/return new RawDeleteRule(v-1);
        }
        
        public enum Type
        {
            AGE, 
            CREATE_BEFORE, 
            NUM_NEWER_VERSIONS, 
            IS_LIVE, 
            UNKNOWN;
        }
    }
    
    @Deprecated
    public static class AgeDeleteRule extends DeleteRule
    {
        private static final long serialVersionUID = 5697166940712116380L;
        private final int daysToLive;
        
        public AgeDeleteRule(final int a1) {
            super(Type.AGE);
            this.daysToLive = a1;
        }
        
        public int getDaysToLive() {
            /*SL:634*/return this.daysToLive;
        }
        
        @Override
        void populateCondition(final Bucket.Lifecycle.Rule.Condition a1) {
            /*SL:639*/a1.setAge(this.daysToLive);
        }
    }
    
    static class RawDeleteRule extends DeleteRule
    {
        private static final long serialVersionUID = -7166938278642301933L;
        private transient Bucket.Lifecycle.Rule rule;
        
        RawDeleteRule(final Bucket.Lifecycle.Rule a1) {
            super(Type.UNKNOWN);
            this.rule = a1;
        }
        
        @Override
        void populateCondition(final Bucket.Lifecycle.Rule.Condition a1) {
            /*SL:656*/throw new UnsupportedOperationException();
        }
        
        private void writeObject(final ObjectOutputStream a1) throws IOException {
            /*SL:660*/a1.defaultWriteObject();
            /*SL:661*/a1.writeUTF(this.rule.toString());
        }
        
        private void readObject(final ObjectInputStream a1) throws IOException, ClassNotFoundException {
            /*SL:665*/a1.defaultReadObject();
            /*SL:666*/this.rule = new JacksonFactory().<Bucket.Lifecycle.Rule>fromString(a1.readUTF(), Bucket.Lifecycle.Rule.class);
        }
        
        @Override
        Bucket.Lifecycle.Rule toPb() {
            /*SL:671*/return this.rule;
        }
    }
    
    @Deprecated
    public static class CreatedBeforeDeleteRule extends DeleteRule
    {
        private static final long serialVersionUID = 881692650279195867L;
        private final long timeMillis;
        
        public CreatedBeforeDeleteRule(final long a1) {
            super(Type.CREATE_BEFORE);
            this.timeMillis = a1;
        }
        
        public long getTimeMillis() {
            /*SL:700*/return this.timeMillis;
        }
        
        @Override
        void populateCondition(final Bucket.Lifecycle.Rule.Condition a1) {
            /*SL:705*/a1.setCreatedBefore(new DateTime(true, this.timeMillis, 0));
        }
    }
    
    @Deprecated
    public static class NumNewerVersionsDeleteRule extends DeleteRule
    {
        private static final long serialVersionUID = -1955554976528303894L;
        private final int numNewerVersions;
        
        public NumNewerVersionsDeleteRule(final int a1) {
            super(Type.NUM_NEWER_VERSIONS);
            this.numNewerVersions = a1;
        }
        
        public int getNumNewerVersions() {
            /*SL:735*/return this.numNewerVersions;
        }
        
        @Override
        void populateCondition(final Bucket.Lifecycle.Rule.Condition a1) {
            /*SL:740*/a1.setNumNewerVersions(this.numNewerVersions);
        }
    }
    
    @Deprecated
    public static class IsLiveDeleteRule extends DeleteRule
    {
        private static final long serialVersionUID = -3502994563121313364L;
        private final boolean isLive;
        
        public IsLiveDeleteRule(final boolean a1) {
            super(Type.IS_LIVE);
            this.isLive = a1;
        }
        
        public boolean isLive() {
            /*SL:769*/return this.isLive;
        }
        
        @Override
        void populateCondition(final Bucket.Lifecycle.Rule.Condition a1) {
            /*SL:774*/a1.setIsLive(this.isLive);
        }
    }
    
    public abstract static class Builder
    {
        public abstract Builder setName(final String p0);
        
        abstract Builder setGeneratedId(final String p0);
        
        abstract Builder setOwner(final Acl.Entity p0);
        
        abstract Builder setSelfLink(final String p0);
        
        public abstract Builder setRequesterPays(final Boolean p0);
        
        public abstract Builder setVersioningEnabled(final Boolean p0);
        
        public abstract Builder setIndexPage(final String p0);
        
        public abstract Builder setNotFoundPage(final String p0);
        
        @Deprecated
        public abstract Builder setDeleteRules(final Iterable<? extends DeleteRule> p0);
        
        public abstract Builder setLifecycleRules(final Iterable<? extends LifecycleRule> p0);
        
        public abstract Builder setStorageClass(final StorageClass p0);
        
        public abstract Builder setLocation(final String p0);
        
        abstract Builder setEtag(final String p0);
        
        abstract Builder setCreateTime(final Long p0);
        
        abstract Builder setMetageneration(final Long p0);
        
        abstract Builder setLocationType(final String p0);
        
        public abstract Builder setCors(final Iterable<Cors> p0);
        
        public abstract Builder setAcl(final Iterable<Acl> p0);
        
        public abstract Builder setDefaultAcl(final Iterable<Acl> p0);
        
        public abstract Builder setLabels(final Map<String, String> p0);
        
        public abstract Builder setDefaultKmsKeyName(final String p0);
        
        @BetaApi
        public abstract Builder setDefaultEventBasedHold(final Boolean p0);
        
        @BetaApi
        abstract Builder setRetentionEffectiveTime(final Long p0);
        
        @BetaApi
        abstract Builder setRetentionPolicyIsLocked(final Boolean p0);
        
        @BetaApi
        public abstract Builder setRetentionPeriod(final Long p0);
        
        @BetaApi
        public abstract Builder setIamConfiguration(final IamConfiguration p0);
        
        public abstract BucketInfo build();
    }
    
    static final class BuilderImpl extends Builder
    {
        private String generatedId;
        private String name;
        private Acl.Entity owner;
        private String selfLink;
        private Boolean requesterPays;
        private Boolean versioningEnabled;
        private String indexPage;
        private String notFoundPage;
        private List<DeleteRule> deleteRules;
        private List<LifecycleRule> lifecycleRules;
        private StorageClass storageClass;
        private String location;
        private String etag;
        private Long createTime;
        private Long metageneration;
        private List<Cors> cors;
        private List<Acl> acl;
        private List<Acl> defaultAcl;
        private Map<String, String> labels;
        private String defaultKmsKeyName;
        private Boolean defaultEventBasedHold;
        private Long retentionEffectiveTime;
        private Boolean retentionPolicyIsLocked;
        private Long retentionPeriod;
        private IamConfiguration iamConfiguration;
        private String locationType;
        
        BuilderImpl(final String a1) {
            this.name = a1;
        }
        
        BuilderImpl(final BucketInfo a1) {
            this.generatedId = a1.generatedId;
            this.name = a1.name;
            this.etag = a1.etag;
            this.createTime = a1.createTime;
            this.metageneration = a1.metageneration;
            this.location = a1.location;
            this.storageClass = a1.storageClass;
            this.cors = a1.cors;
            this.acl = a1.acl;
            this.defaultAcl = a1.defaultAcl;
            this.owner = a1.owner;
            this.selfLink = a1.selfLink;
            this.versioningEnabled = a1.versioningEnabled;
            this.indexPage = a1.indexPage;
            this.notFoundPage = a1.notFoundPage;
            this.deleteRules = a1.deleteRules;
            this.lifecycleRules = a1.lifecycleRules;
            this.labels = a1.labels;
            this.requesterPays = a1.requesterPays;
            this.defaultKmsKeyName = a1.defaultKmsKeyName;
            this.defaultEventBasedHold = a1.defaultEventBasedHold;
            this.retentionEffectiveTime = a1.retentionEffectiveTime;
            this.retentionPolicyIsLocked = a1.retentionPolicyIsLocked;
            this.retentionPeriod = a1.retentionPeriod;
            this.iamConfiguration = a1.iamConfiguration;
            this.locationType = a1.locationType;
        }
        
        @Override
        public Builder setName(final String a1) {
            /*SL:980*/this.name = Preconditions.<String>checkNotNull(a1);
            /*SL:981*/return this;
        }
        
        @Override
        Builder setGeneratedId(final String a1) {
            /*SL:986*/this.generatedId = a1;
            /*SL:987*/return this;
        }
        
        @Override
        Builder setOwner(final Acl.Entity a1) {
            /*SL:992*/this.owner = a1;
            /*SL:993*/return this;
        }
        
        @Override
        Builder setSelfLink(final String a1) {
            /*SL:998*/this.selfLink = a1;
            /*SL:999*/return this;
        }
        
        @Override
        public Builder setVersioningEnabled(final Boolean a1) {
            /*SL:1004*/this.versioningEnabled = (Boolean)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(Boolean.class));
            /*SL:1005*/return this;
        }
        
        @Override
        public Builder setRequesterPays(final Boolean a1) {
            /*SL:1010*/this.requesterPays = (Boolean)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(Boolean.class));
            /*SL:1011*/return this;
        }
        
        @Override
        public Builder setIndexPage(final String a1) {
            /*SL:1016*/this.indexPage = a1;
            /*SL:1017*/return this;
        }
        
        @Override
        public Builder setNotFoundPage(final String a1) {
            /*SL:1022*/this.notFoundPage = a1;
            /*SL:1023*/return this;
        }
        
        @Deprecated
        @Override
        public Builder setDeleteRules(final Iterable<? extends DeleteRule> a1) {
            /*SL:1030*/this.deleteRules = (List<DeleteRule>)((a1 != null) ? ImmutableList.<Object>copyOf((Iterable<?>)a1) : null);
            /*SL:1031*/return this;
        }
        
        @Override
        public Builder setLifecycleRules(final Iterable<? extends LifecycleRule> a1) {
            /*SL:1036*/this.lifecycleRules = (List<LifecycleRule>)((a1 != null) ? ImmutableList.<Object>copyOf((Iterable<?>)a1) : null);
            /*SL:1037*/return this;
        }
        
        @Override
        public Builder setStorageClass(final StorageClass a1) {
            /*SL:1042*/this.storageClass = a1;
            /*SL:1043*/return this;
        }
        
        @Override
        public Builder setLocation(final String a1) {
            /*SL:1048*/this.location = a1;
            /*SL:1049*/return this;
        }
        
        @Override
        Builder setEtag(final String a1) {
            /*SL:1054*/this.etag = a1;
            /*SL:1055*/return this;
        }
        
        @Override
        Builder setCreateTime(final Long a1) {
            /*SL:1060*/this.createTime = a1;
            /*SL:1061*/return this;
        }
        
        @Override
        Builder setMetageneration(final Long a1) {
            /*SL:1066*/this.metageneration = a1;
            /*SL:1067*/return this;
        }
        
        @Override
        public Builder setCors(final Iterable<Cors> a1) {
            /*SL:1072*/this.cors = (List<Cors>)((a1 != null) ? ImmutableList.<Object>copyOf((Iterable<?>)a1) : null);
            /*SL:1073*/return this;
        }
        
        @Override
        public Builder setAcl(final Iterable<Acl> a1) {
            /*SL:1078*/this.acl = (List<Acl>)((a1 != null) ? ImmutableList.<Object>copyOf((Iterable<?>)a1) : null);
            /*SL:1079*/return this;
        }
        
        @Override
        public Builder setDefaultAcl(final Iterable<Acl> a1) {
            /*SL:1084*/this.defaultAcl = (List<Acl>)((a1 != null) ? ImmutableList.<Object>copyOf((Iterable<?>)a1) : null);
            /*SL:1085*/return this;
        }
        
        @Override
        public Builder setLabels(final Map<String, String> a1) {
            /*SL:1090*/this.labels = (Map<String, String>)((a1 != null) ? ImmutableMap.<Object, Object>copyOf((Map<?, ?>)a1) : null);
            /*SL:1091*/return this;
        }
        
        @Override
        public Builder setDefaultKmsKeyName(final String a1) {
            /*SL:1097*/this.defaultKmsKeyName = ((a1 != null) ? a1 : Data.<String>nullOf(String.class));
            /*SL:1098*/return this;
        }
        
        @Override
        public Builder setDefaultEventBasedHold(final Boolean a1) {
            /*SL:1103*/this.defaultEventBasedHold = /*EL:1104*/(Boolean)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(Boolean.class));
            /*SL:1105*/return this;
        }
        
        @Override
        Builder setRetentionEffectiveTime(final Long a1) {
            /*SL:1110*/this.retentionEffectiveTime = /*EL:1111*/(Long)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(Long.class));
            /*SL:1112*/return this;
        }
        
        @Override
        Builder setRetentionPolicyIsLocked(final Boolean a1) {
            /*SL:1117*/this.retentionPolicyIsLocked = /*EL:1118*/(Boolean)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(Boolean.class));
            /*SL:1119*/return this;
        }
        
        @Override
        public Builder setRetentionPeriod(final Long a1) {
            /*SL:1124*/this.retentionPeriod = (Long)MoreObjects.firstNonNull((Object)a1, Data.<Object>nullOf(Long.class));
            /*SL:1125*/return this;
        }
        
        @Override
        public Builder setIamConfiguration(final IamConfiguration a1) {
            /*SL:1130*/this.iamConfiguration = a1;
            /*SL:1131*/return this;
        }
        
        @Override
        Builder setLocationType(final String a1) {
            /*SL:1136*/this.locationType = a1;
            /*SL:1137*/return this;
        }
        
        @Override
        public BucketInfo build() {
            /*SL:1142*/Preconditions.<String>checkNotNull(this.name);
            /*SL:1143*/return new BucketInfo(this);
        }
    }
}
