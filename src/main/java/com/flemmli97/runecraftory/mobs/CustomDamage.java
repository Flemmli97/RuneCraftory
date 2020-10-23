package com.flemmli97.runecraftory.mobs;

import com.flemmli97.runecraftory.lib.EnumElement;
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
    private Entity sourceEntity;
    private float knockAmount;
    private int protection;

    public CustomDamage(@Nullable LivingEntity attacker, EnumElement element, KnockBackType knock, float knockBackAmount, int hurtTimeProtection) {
        super("rfAttack", attacker);
        this.element = element;
        this.knock = knock;
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
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        if (this.damageSourceEntity != null) {
            ItemStack itemstack = this.damageSourceEntity instanceof LivingEntity ? ((LivingEntity) this.damageSourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
            String s = "death.attack." + this.damageType;
            String s1 = s + ".item";
            return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.hasKey(s1) ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getTextComponent()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), this.damageSourceEntity.getDisplayName());
        } else {
            ItemStack itemstack = this.sourceEntity instanceof LivingEntity ? ((LivingEntity) this.sourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
            String s = "death.attack." + this.damageType;
            String s1 = s + ".item";
            return !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.hasKey(s1) ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), this.sourceEntity.getDisplayName(), itemstack.getTextComponent()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), this.sourceEntity.getDisplayName());
        }
    }

    public static final CustomDamage attack(@Nullable LivingEntity attacker, EnumElement element, DamageType type, KnockBackType knock, float knockBackAmount, int hurtTimeProtection) {
        CustomDamage source = new CustomDamage(attacker, element, knock, knockBackAmount, hurtTimeProtection);
        switch (type) {
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
        }
        return source;
    }


    public enum DamageType {
        NORMAL,
        MAGIC,
        IGNOREDEF,
        IGNOREMAGICDEF
    }

    public enum KnockBackType {
        BACK,
        UP,
        VANILLA
    }
}
