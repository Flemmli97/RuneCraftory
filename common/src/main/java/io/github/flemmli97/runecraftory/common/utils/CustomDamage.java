package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class CustomDamage extends EntityDamageSource {

    public static final DamageSource EXHAUST = Platform.INSTANCE.createDamageSource("rfExhaust", true, true);

    private EnumElement element;
    private KnockBackType knock;
    private boolean ignoreMagic;
    private Entity trueSourceEntity;
    private float knockAmount;
    private int protection;

    public CustomDamage(Entity attacker, @Nullable Entity cause, EnumElement element, KnockBackType knock, float knockBackAmount, int hurtTimeProtection) {
        super("rfAttack", attacker);
        this.element = element;
        this.knock = knock;
        this.trueSourceEntity = cause;
        this.knockAmount = knockBackAmount;
        this.protection = hurtTimeProtection;
    }

    public EnumElement getElement() {
        return this.element;
    }

    public boolean ignoreMagicDef() {
        return this.ignoreMagic;
    }

    public KnockBackType getKnockBackType() {
        return this.knock;
    }

    public float knockAmount() {
        return this.knockAmount;
    }

    public int hurtProtection() {
        return this.protection;
    }

    @Override
    @Nullable
    public Entity getEntity() {
        return this.trueSourceEntity != null ? this.trueSourceEntity : this.getDirectEntity();
    }

    @Override
    public Entity getDirectEntity() {
        return this.entity;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        Entity source = this.getEntity() != null ? this.getEntity() : this.getDirectEntity();
        ItemStack itemstack = source instanceof LivingEntity ? ((LivingEntity) source).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() && I18n.exists(s1) ? new TranslatableComponent(s1, entityLivingBaseIn.getDisplayName(), source.getDisplayName(), itemstack.getDisplayName()) : new TranslatableComponent(s, entityLivingBaseIn.getDisplayName(), source.getDisplayName());
    }

    public enum DamageType {
        NORMAL,
        MAGIC,
        IGNOREDEF,
        IGNOREMAGICDEF,
        IGNOREALL
    }

    public enum KnockBackType {
        BACK,
        UP,
        VANILLA,
        NONE
    }

    public static class Builder {

        private EnumElement element = EnumElement.NONE;
        private KnockBackType knock = KnockBackType.VANILLA;
        private boolean ignoreMagic;
        private Entity trueSource;
        private Entity cause;
        private float knockAmount;
        private int protection = 10;
        private DamageType dmg = DamageType.NORMAL;

        public Builder(Entity attacker) {
            this.cause = attacker;
        }

        public Builder(Entity attacker, Entity source) {
            this(attacker);
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

        public Builder hurtResistant(int time) {
            this.protection = time;
            return this;
        }

        public Builder damageType(DamageType type) {
            this.dmg = type;
            return this;
        }

        public CustomDamage get() {
            CustomDamage source = new CustomDamage(this.cause, this.trueSource, this.element, this.knock, this.knockAmount, this.protection);
            switch (this.dmg) {
                case NORMAL:
                    break;
                case MAGIC:
                    source.setMagic();
                    break;
                case IGNOREDEF:
                    source.bypassArmor();
                    break;
                case IGNOREMAGICDEF:
                    source.setMagic();
                    source.ignoreMagic = true;
                    break;
                case IGNOREALL:
                    source.bypassArmor();
                    source.setMagic();
                    source.ignoreMagic = true;
                    break;
            }
            return source;
        }
    }
}
