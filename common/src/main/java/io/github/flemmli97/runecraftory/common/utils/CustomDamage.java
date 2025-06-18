package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModDamageType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomDamage extends DamageSource {

    private final EnumElement element;
    private final KnockBackType knock;
    private final float knockAmount;
    private final int hurtProtection;
    private final boolean faintEntity;
    private final boolean fixedDamage;

    private final ImmutableMap<Holder<Attribute>, Double> attributesChange;

    private final Set<TagKey<DamageType>> dynamicTags;

    public CustomDamage(Holder<DamageType> type, Entity attacker, @Nullable Entity cause, EnumElement element, KnockBackType knock,
                        float knockBackAmount, int hurtTimeProtection, boolean faintEntity, boolean fixedDamage,
                        Map<Holder<Attribute>, Double> attributesChange, Set<TagKey<DamageType>> dynamicTags) {
        super(type, attacker, cause);
        this.element = element;
        this.knock = knock;
        this.knockAmount = knockBackAmount;
        this.hurtProtection = hurtTimeProtection;
        this.faintEntity = faintEntity;
        this.fixedDamage = fixedDamage;
        this.attributesChange = ImmutableMap.copyOf(attributesChange);
        this.dynamicTags = dynamicTags;
    }

    @Override
    public boolean is(TagKey<DamageType> damageTypeKey) {
        return super.is(damageTypeKey) || this.dynamicTags.contains(damageTypeKey);
    }

    public EnumElement getElement() {
        return this.element;
    }

    public KnockBackType getKnockBackType() {
        return this.knock;
    }

    public float knockAmount() {
        return this.knockAmount;
    }

    public int hurtProtection() {
        return this.hurtProtection;
    }

    public boolean criticalDamage() {
        return this.faintEntity;
    }

    public boolean fixedDamage() {
        return this.fixedDamage;
    }

    public ImmutableMap<Holder<Attribute>, Double> getAttributesChange() {
        return this.attributesChange;
    }

    /**
     * Attacks entity with a reduced invulnerability timer
     */
    public boolean hurtEntity(Entity target, float dmg) {
        int invul = target.invulnerableTime;
        boolean modified = false;
        if (target.invulnerableTime + this.hurtProtection() <= 20) {
            target.invulnerableTime = Math.min(target.invulnerableTime, 10);
            modified = true;
        }
        boolean success = target.hurt(this, dmg);
        if (!success && modified) {
            target.invulnerableTime = invul;
        }
        return success;
    }

    public enum DamageCategory {
        NORMAL,
        MAGIC,
        IGNOREDEF,
        IGNOREMAGICDEF,
        FAINT,
        FIXED
    }

    public enum KnockBackType {
        BACK,
        UP,
        VANILLA,
        NONE
    }

    public static class Builder {

        private final Entity cause;

        private EnumElement element = EnumElement.NONE;
        private KnockBackType knock = KnockBackType.VANILLA;
        private Entity trueSource;
        private float knockAmount;
        private int protection = 10;
        private DamageCategory dmg = DamageCategory.NORMAL;
        private final Map<Holder<Attribute>, Double> attributesChange = new HashMap<>();

        private boolean isProjectile;

        public Builder(Entity attacker) {
            this.cause = attacker;
        }

        public Builder(Entity attacker, Entity source) {
            this.cause = attacker;
            this.trueSource = source;
        }

        public Builder element(EnumElement el) {
            this.element = el;
            return this;
        }

        public Builder knock(KnockBackType k) {
            this.knock = k;
            return this;
        }

        public Builder knockAmount(float amount) {
            this.knockAmount = amount;
            return this;
        }

        public Builder noKnockback() {
            this.knock = KnockBackType.NONE;
            return this;
        }

        public boolean calculateKnockback() {
            return this.knock == KnockBackType.VANILLA && this.knockAmount == 0;
        }

        public Builder hurtResistant(int time) {
            this.protection = time;
            return this;
        }

        public Builder magic() {
            this.dmg = DamageCategory.MAGIC;
            return this;
        }

        public Builder damageType(DamageCategory type) {
            if (this.dmg != DamageCategory.FAINT)
                this.dmg = type;
            return this;
        }

        public DamageCategory getDamageType() {
            return this.dmg;
        }

        public Builder withChangedAttribute(Holder<Attribute> att, double change) {
            this.attributesChange.put(att, change);
            return this;
        }

        public Builder projectile() {
            this.isProjectile = true;
            return this;
        }

        public Map<Holder<Attribute>, Double> getAttributesChanges() {
            return this.attributesChange;
        }

        public CustomDamage get(HolderLookup.Provider provider) {
            Set<TagKey<DamageType>> tags = new HashSet<>();
            switch (this.dmg) {
                case MAGIC -> {
                    tags.add(RunecraftoryTags.DamageTypes.IS_MAGIC);
                    tags.add(DamageTypeTags.BYPASSES_ARMOR);
                }
                case FAINT, FIXED, IGNOREDEF -> tags.add(DamageTypeTags.BYPASSES_ARMOR);
                case IGNOREMAGICDEF -> {
                    tags.add(RunecraftoryTags.DamageTypes.IS_MAGIC);
                    tags.add(RunecraftoryTags.DamageTypes.BYPASS_MAGIC);
                    tags.add(DamageTypeTags.BYPASSES_ARMOR);
                    tags.add(DamageTypeTags.BYPASSES_EFFECTS);
                    tags.add(DamageTypeTags.BYPASSES_RESISTANCE);
                    tags.add(DamageTypeTags.BYPASSES_ENCHANTMENTS);
                    tags.add(DamageTypeTags.BYPASSES_SHIELD);
                }
            }
            if (this.isProjectile)
                tags.add(DamageTypeTags.IS_PROJECTILE);
            if (this.cause instanceof Player)
                tags.add(DamageTypeTags.IS_PLAYER_ATTACK);
            return new CustomDamage(provider.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(ModDamageType.DYNAMIC_DAMAGE_TYPE), this.cause, this.trueSource, this.element, this.knock, this.knockAmount, this.protection,
                    this.dmg == DamageCategory.FAINT, this.dmg == DamageCategory.FIXED,
                    this.attributesChange, tags);
        }
    }
}
