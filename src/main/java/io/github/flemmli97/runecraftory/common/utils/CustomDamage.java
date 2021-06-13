package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class CustomDamage extends EntityDamageSource {

    public static final DamageSource EXHAUST = (new DamageSource("rfExhaust")).setDamageBypassesArmor().setDamageIsAbsolute();

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
    public Entity getTrueSource() {
        return this.trueSourceEntity != null ? this.trueSourceEntity : this.getImmediateSource();
    }

    @Override
    public Entity getImmediateSource() {
        return this.damageSourceEntity;
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        Entity source = this.getTrueSource() != null ? this.getTrueSource() : this.getImmediateSource();
        ItemStack itemstack = source instanceof LivingEntity ? ((LivingEntity) source).getHeldItemMainhand() : ItemStack.EMPTY;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.hasKey(s1) ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), source.getDisplayName(), itemstack.getTextComponent()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), source.getDisplayName());
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
                    source.setMagicDamage();
                    break;
                case IGNOREDEF:
                    source.setDamageBypassesArmor();
                    break;
                case IGNOREMAGICDEF:
                    source.setMagicDamage();
                    source.ignoreMagic = true;
                    break;
                case IGNOREALL:
                    source.setDamageBypassesArmor();
                    source.setMagicDamage();
                    source.ignoreMagic = true;
                    break;
            }
            return source;
        }
    }
}
