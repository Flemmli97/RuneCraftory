package io.github.flemmli97.runecraftory.common.world.family;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FamilyEntry {

    public static final int DEPTH = 3;

    private UUID father, mother;

    private final Set<UUID> siblings = new HashSet<>();

    private final Set<UUID> children = new HashSet<>();
    private Component name;
    private NPCData.Gender gender;

    private UUID self;
    private UUID partner;
    private Relationship relationship = Relationship.NONE;

    private EntityState entityState;

    private final FamilyHandler familyHandler;


    public FamilyEntry(FamilyHandler familyHandler, UUID self, Component name, NPCData.Gender gender, boolean player) {
        this.familyHandler = familyHandler;
        this.self = self;
        this.name = name;
        this.gender = gender;
        this.entityState = player ? EntityState.PLAYER : EntityState.ALIVE;
    }

    public FamilyEntry(FamilyHandler familyHandler, CompoundTag tag) {
        this.familyHandler = familyHandler;
        this.load(tag);
    }

    public void setFather(UUID father) {
        if (this.father != null) {
            this.familyHandler.getFamily(this.father)
                    .ifPresent(e -> {
                        e.children.forEach(child -> this.familyHandler.getFamily(child).ifPresent(ce -> ce.siblings.remove(this.self)));
                        e.removeChild(this.self);
                    });
        }
        this.father = father;
        this.familyHandler.getFamily(this.father)
                .ifPresent(e -> e.addChild(this.self));
        this.familyHandler.setDirty();
    }

    public void setMother(UUID mother) {
        if (this.mother != null) {
            this.familyHandler.getFamily(this.mother)
                    .ifPresent(e -> {
                        e.children.forEach(child -> this.familyHandler.getFamily(child).ifPresent(ce -> ce.siblings.remove(this.self)));
                        e.removeChild(this.self);
                    });
        }
        this.mother = mother;
        this.familyHandler.getFamily(this.mother)
                .ifPresent(e -> e.addChild(this.self));
        this.familyHandler.setDirty();
    }

    public void addChild(UUID uuid) {
        if (this.partner != null) {
            this.familyHandler.getFamily(this.partner)
                    .ifPresent(e -> e.children.add(this.self));
        }
        this.children.add(uuid);
        this.children.forEach(child -> this.familyHandler.getFamily(child)
                .ifPresent(e -> e.siblings.add(this.self)));
        this.familyHandler.setDirty();
    }

    public void removeChild(UUID uuid) {
        if (this.partner != null) {
            this.familyHandler.getFamily(this.partner)
                    .ifPresent(e -> e.children.remove(this.self));
        }
        this.children.remove(uuid);
        this.children.forEach(child -> this.familyHandler.getFamily(child)
                .ifPresent(e -> e.siblings.remove(this.self)));
        this.familyHandler.setDirty();
    }

    public void addSibling(UUID uuid) {
        if (this.father != null) {
            this.familyHandler.getFamily(this.father)
                    .ifPresent(e -> e.children.add(this.self));
        }
        if (this.mother != null) {
            this.familyHandler.getFamily(this.mother)
                    .ifPresent(e -> e.children.add(this.self));
        }
        this.siblings.forEach((sibling) -> this.familyHandler.getFamily(sibling).ifPresent(e -> e.siblings.add(uuid)));
        this.siblings.add(uuid);
        this.familyHandler.setDirty();
    }

    public void removeSibling(UUID uuid) {
        if (this.father != null) {
            this.familyHandler.getFamily(this.father)
                    .ifPresent(e -> e.children.remove(this.self));
        }
        if (this.mother != null) {
            this.familyHandler.getFamily(this.mother)
                    .ifPresent(e -> e.children.remove(this.self));
        }
        this.siblings.remove(uuid);
        this.siblings.forEach((sibling) -> this.familyHandler.getFamily(sibling).ifPresent(e -> e.siblings.remove(uuid)));
        this.familyHandler.setDirty();
    }

    public void updateName(Entity entity) {
        this.name = entity.getName();
        this.familyHandler.setDirty();
    }

    public void updateRelationship(Relationship relationship, UUID partner) {
        if (this.partner != null) {
            this.familyHandler.getFamily(this.partner)
                    .ifPresent(e -> {
                        e.partner = null;
                        e.relationship = Relationship.NONE;
                    });
        }
        this.relationship = relationship;
        this.partner = partner;
        if (this.partner != null) {
            this.familyHandler.getFamily(this.partner)
                    .ifPresent(e -> {
                        e.partner = this.self;
                        e.relationship = relationship;
                    });
        }
        this.familyHandler.setDirty();
    }

    public Relationship getRelationship() {
        return this.relationship;
    }

    public UUID getPartner() {
        return this.partner;
    }

    /**
     * @return True if the given uuid is related to this entry
     */
    public boolean isRelated(UUID uuid) {
        return this.findRelativesRecursive(DEPTH, uuid);
    }

    /**
     * @return The FamilyRelation for the given uuid. If the uuid is neither not related or 2 depth apart it returns null
     */
    @Nullable
    public FamilyRelation getRelativeState(UUID toFind) {
        return this.findRelativeStateRecursive(2, FamilyRelation.NONE, toFind);
    }

    private FamilyRelation findRelativeStateRecursive(int depth, FamilyRelation current, UUID toFind) {
        if (depth > 0) {
            if (this.father != null) {
                FamilyRelation next = FamilyRelation.MAP.get(current).father;
                if (this.father.equals(toFind))
                    return next;
                FamilyRelation relation = this.familyHandler.getFamily(this.father)
                        .map(e -> this.findRelativeStateRecursive(depth - 1, next, toFind)).orElse(null);
                if (relation != null) {
                    return relation;
                }
            }
            if (this.mother != null) {
                FamilyRelation next = FamilyRelation.MAP.get(current).mother;
                if (this.mother.equals(toFind))
                    return next;
                FamilyRelation relation = this.familyHandler.getFamily(this.mother)
                        .map(e -> this.findRelativeStateRecursive(depth - 1, next, toFind)).orElse(null);
                if (relation != null) {
                    return relation;
                }
            }
            for (UUID sibling : this.siblings) {
                FamilyRelation relation = this.familyHandler.getFamily(sibling)
                        .map(e -> {
                            FamilyRelation next = e.gender == NPCData.Gender.MALE ? FamilyRelation.MAP.get(current).maleSibling : FamilyRelation.MAP.get(current).femaleSibling;
                            if (sibling.equals(toFind)) {
                                return next;
                            }
                            return this.findRelativeStateRecursive(depth - 1, next, toFind);
                        }).orElse(null);
                if (relation != null) {
                    return relation;
                }
            }
            for (UUID child : this.children) {
                FamilyRelation relation = this.familyHandler.getFamily(child)
                        .map(e -> {
                            FamilyRelation next = e.gender == NPCData.Gender.MALE ? FamilyRelation.MAP.get(current).maleChild : FamilyRelation.MAP.get(current).femaleChild;
                            if (child.equals(toFind)) {
                                return next;
                            }
                            return this.findRelativeStateRecursive(depth - 1, next, toFind);
                        }).orElse(null);
                if (relation != null) {
                    return relation;
                }
            }
        }
        return null;
    }

    private boolean findRelativesRecursive(int depth, UUID toFind) {
        if (depth > 0) {
            if (this.father != null) {
                if (this.father.equals(toFind))
                    return true;
                if (this.familyHandler.getFamily(this.father)
                        .map(e -> this.findRelativesRecursive(depth - 1, toFind)).orElse(false))
                    return true;
            }
            if (this.mother != null) {
                if (this.mother.equals(toFind))
                    return true;
                if (this.familyHandler.getFamily(this.mother)
                        .map(e -> this.findRelativesRecursive(depth - 1, toFind)).orElse(false))
                    return true;
            }
            for (UUID sibling : this.siblings) {
                if (sibling.equals(toFind))
                    return true;
                if (this.familyHandler.getFamily(sibling)
                        .map(e -> this.findRelativesRecursive(depth - 1, toFind)).orElse(false))
                    return true;
            }
            for (UUID child : this.children) {
                if (child.equals(toFind))
                    return true;
                if (this.familyHandler.getFamily(child)
                        .map(e -> this.findRelativesRecursive(depth - 1, toFind)).orElse(false))
                    return true;
            }
        }
        return false;
    }

    public void markAsDead() {
        if (this.entityState != EntityState.PLAYER)
            this.entityState = EntityState.DEAD;
        this.familyHandler.setDirty();
    }

    /**
     * Check if all entities DEPTH apart from this entry are alive. If not this can be deleted
     */
    public boolean shouldPersist() {
        return this.shouldPersist(DEPTH);
    }

    private boolean shouldPersist(int depth) {
        if (depth > 0) {
            if (this.father != null) {
                if (this.familyHandler.getFamily(this.father)
                        .map(e -> {
                            if (e.entityState == EntityState.ALIVE || e.entityState == EntityState.PLAYER)
                                return true;
                            return e.shouldPersist(depth - 2);
                        }).orElse(false))
                    return true;
            }
            if (this.mother != null) {
                if (this.familyHandler.getFamily(this.mother)
                        .map(e -> {
                            if (e.entityState == EntityState.ALIVE || e.entityState == EntityState.PLAYER)
                                return true;
                            return e.shouldPersist(depth - 2);
                        }).orElse(false))
                    return true;
            }
            for (UUID sibling : this.siblings) {
                if (this.familyHandler.getFamily(sibling)
                        .map(e -> {
                            if (e.entityState == EntityState.ALIVE || e.entityState == EntityState.PLAYER)
                                return true;
                            return e.shouldPersist(depth - 2);
                        }).orElse(false))
                    return true;
            }
            for (UUID child : this.children) {
                if (this.familyHandler.getFamily(child)
                        .map(e -> {
                            if (e.entityState == EntityState.ALIVE || e.entityState == EntityState.PLAYER)
                                return true;
                            return e.shouldPersist(depth - 2);
                        }).orElse(false))
                    return true;
            }
        }
        return false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        if (this.father != null)
            tag.putUUID("Father", this.father);
        if (this.mother != null)
            tag.putUUID("Mother", this.mother);
        ListTag siblings = new ListTag();
        this.siblings.forEach(uuid -> siblings.add(NbtUtils.createUUID(uuid)));
        tag.put("Siblings", siblings);
        ListTag children = new ListTag();
        this.children.forEach(uuid -> children.add(NbtUtils.createUUID(uuid)));
        tag.put("Children", children);
        tag.putString("Name", Component.Serializer.toJson(this.name));
        tag.putInt("Gender", this.gender.ordinal());
        tag.putUUID("OwnID", this.self);
        if (this.partner != null)
            tag.putUUID("Partner", this.partner);
        tag.putInt("RelationShip", this.relationship.ordinal());
        tag.putInt("EntityState", this.entityState.ordinal());
        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.hasUUID("Father"))
            this.father = tag.getUUID("Father");
        if (tag.hasUUID("Mother"))
            this.mother = tag.getUUID("Mother");
        ListTag siblings = tag.getList("Siblings", Tag.TAG_INT_ARRAY);
        siblings.forEach(sibling -> this.siblings.add(NbtUtils.loadUUID(sibling)));
        ListTag children = tag.getList("Children", Tag.TAG_INT_ARRAY);
        children.forEach(sibling -> this.children.add(NbtUtils.loadUUID(sibling)));
        this.name = Component.Serializer.fromJson(tag.getString("Name"));
        this.gender = NPCData.Gender.values()[tag.getInt("Gender")];
        this.self = tag.getUUID("OwnID");
        this.gender = NPCData.Gender.values()[tag.getInt("Gender")];
        if (tag.hasUUID("Partner"))
            this.partner = tag.getUUID("Partner");
        this.relationship = Relationship.values()[tag.getInt("RelationShip")];
        this.entityState = EntityState.values()[tag.getInt("EntityState")];
    }

    public SyncedFamilyData forSyncing() {
        Component father = this.father != null ? this.familyHandler.getFamily(this.father)
                .map(e -> e.name).orElse(null) : null;
        Component mother = this.mother != null ? this.familyHandler.getFamily(this.mother)
                .map(e -> e.name).orElse(null) : null;
        Component partner = this.partner != null ? this.familyHandler.getFamily(this.partner)
                .map(e -> e.name).orElse(null) : null;
        return new SyncedFamilyData(father, mother, partner, this.relationship);
    }

    public enum Relationship {
        NONE,
        DATING,
        MARRIED
    }

    public enum EntityState {
        ALIVE,
        DEAD,
        PLAYER
    }

    // Supports to 2 depth. no more...
    public enum FamilyRelation {
        NONE,
        FATHER,
        MOTHER,
        BROTHER,
        SISTER,
        SON,
        DAUGHTER,
        GRAND_SON,
        GRAND_DAUGHTER,
        NEPHEW,
        NIECE,
        UNCLE,
        AUNT,
        GRAND_FATHER,
        GRAND_MOTHER;

        private static final EnumMap<FamilyRelation, FamilyRelationRelation> MAP;

        static {
            ImmutableMap.Builder<FamilyRelation, FamilyRelationRelation> builder = new ImmutableMap.Builder<>();
            builder.put(NONE, new FamilyRelationRelation(FATHER, MOTHER, BROTHER, SISTER, SON, DAUGHTER));
            builder.put(FATHER, new FamilyRelationRelation(GRAND_FATHER, GRAND_MOTHER, UNCLE, AUNT, BROTHER, SISTER));
            builder.put(MOTHER, new FamilyRelationRelation(GRAND_FATHER, GRAND_MOTHER, UNCLE, AUNT, BROTHER, SISTER));
            builder.put(BROTHER, new FamilyRelationRelation(FATHER, MOTHER, BROTHER, SISTER, NEPHEW, NIECE));
            builder.put(SISTER, new FamilyRelationRelation(FATHER, MOTHER, BROTHER, SISTER, NEPHEW, NIECE));
            builder.put(SON, new FamilyRelationRelation(NONE, NONE, SON, DAUGHTER, GRAND_SON, GRAND_DAUGHTER));
            builder.put(DAUGHTER, new FamilyRelationRelation(NONE, NONE, SON, DAUGHTER, GRAND_SON, GRAND_DAUGHTER));
            builder.put(GRAND_SON, new FamilyRelationRelation(null, null, null, null, null, null));
            builder.put(GRAND_DAUGHTER, new FamilyRelationRelation(null, null, null, null, null, null));
            builder.put(NEPHEW, new FamilyRelationRelation(BROTHER, SISTER, NEPHEW, NIECE, null, null));
            builder.put(NIECE, new FamilyRelationRelation(BROTHER, SISTER, NEPHEW, NIECE, null, null));
            builder.put(UNCLE, new FamilyRelationRelation(GRAND_FATHER, GRAND_MOTHER, UNCLE, AUNT, null, null));
            builder.put(AUNT, new FamilyRelationRelation(GRAND_FATHER, GRAND_MOTHER, UNCLE, AUNT, null, null));
            builder.put(GRAND_FATHER, new FamilyRelationRelation(null, null, null, null, null, null));
            builder.put(GRAND_MOTHER, new FamilyRelationRelation(null, null, null, null, null, null));
            MAP = new EnumMap<>(builder.build());
        }

        record FamilyRelationRelation(FamilyRelation father, FamilyRelation mother, FamilyRelation maleSibling,
                                      FamilyRelation femaleSibling, FamilyRelation maleChild,
                                      FamilyRelation femaleChild) {
        }
    }
}
