package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.IBaseMob;
import com.flemmli97.runecraftory.common.entities.IExtendedMob;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModPotions;
import com.flemmli97.tenshilib.api.item.IAOEWeapon;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SEntityVelocityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.Random;

public class CombatUtils {

    public static float getAttributeValueRaw(LivingEntity attacker, Attribute att) {
        return getAttributeValue(attacker, att, null);
    }

    public static float getAttributeValue(LivingEntity attacker, Attribute att, Entity target) {
        float increase = 0;
        if (attacker instanceof PlayerEntity) {
            increase += attacker.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getAttributeValue((PlayerEntity) attacker, att)).orElse(0);
        } else if (attacker.getAttribute(att) != null) {
            increase += attacker.getAttributeValue(att);
        }
        if (!(target instanceof LivingEntity))
            return increase;
        Attribute opp = opposing(att);
        if (opp == null)
            return increase;
        if (target instanceof PlayerEntity) {
            increase -= target.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getAttributeValue((PlayerEntity) target, opp)).orElse(0);
        } else {
            LivingEntity living = (LivingEntity) target;
            if (living.getAttribute(opp) != null)
                increase -= living.getAttributeValue(opp);
        }
        return increase;
    }

    public static Attribute opposing(Attribute att) {
        //if (att == Attributes.GENERIC_ATTACK_DAMAGE)
        //    return ModAttributes.RF_DEFENCE;
        //if (att == ModAttributes.RF_MAGIC)
        //    return ModAttributes.RF_MAGIC_DEFENCE;
        if (att == ModAttributes.RFPARA.get())
            return ModAttributes.RFRESPARA.get();
        if (att == ModAttributes.RFPOISON.get())
            return ModAttributes.RFRESPOISON.get();
        if (att == ModAttributes.RFSEAL.get())
            return ModAttributes.RFRESSEAL.get();
        if (att == ModAttributes.RFSLEEP.get())
            return ModAttributes.RFRESSLEEP.get();
        if (att == ModAttributes.RFFAT.get())
            return ModAttributes.RFRESFAT.get();
        if (att == ModAttributes.RFCOLD.get())
            return ModAttributes.RFRESCOLD.get();
        if (att == ModAttributes.RFDIZ.get())
            return ModAttributes.RFRESDIZ.get();
        if (att == ModAttributes.RFCRIT.get())
            return ModAttributes.RFRESCRIT.get();
        if (att == ModAttributes.RFSTUN.get())
            return ModAttributes.RFRESSTUN.get();
        if (att == ModAttributes.RFFAINT.get())
            return ModAttributes.RFRESFAINT.get();
        if (att == ModAttributes.RFDRAIN.get())
            return ModAttributes.RFRESDRAIN.get();
        //if(att == ModAttributes.RFKNOCK)
        //    return ModAttributes.RESKNOCK;
        return null;
    }

    public static float reduceDamageFromStats(LivingEntity entity, DamageSource source, float amount) {
        float reduce = 0.0f;
        if (!source.isDamageAbsolute()) {
            if (!source.isUnblockable()) {
                if (source.isMagicDamage())
                    reduce = getAttributeValue(entity, ModAttributes.RF_MAGIC_DEFENCE.get(), null);
                else
                    reduce = getAttributeValue(entity, ModAttributes.RF_DEFENCE.get(), null);
            }
        }
        return elementalReduction(entity, source, Math.max(0.0f, amount - reduce));
    }

    public static float elementalReduction(LivingEntity entity, DamageSource source, float amount) {
        if (source instanceof CustomDamage && ((CustomDamage) source).getElement() != EnumElement.NONE) {
            EnumElement element = ((CustomDamage) source).getElement();
            float percent = 0;
            switch (element) {
                case DARK:
                    percent = getAttributeValue(entity, ModAttributes.RFRESDARK.get(), null);
                    break;
                case EARTH:
                    percent = getAttributeValue(entity, ModAttributes.RFRESEARTH.get(), null);
                    break;
                case FIRE:
                    percent = getAttributeValue(entity, ModAttributes.RFRESFIRE.get(), null);
                    break;
                case LIGHT:
                    percent = getAttributeValue(entity, ModAttributes.RFRESLIGHT.get(), null);
                    break;
                case LOVE:
                    percent = getAttributeValue(entity, ModAttributes.RFRESLOVE.get(), null);
                    break;
                case WATER:
                    percent = getAttributeValue(entity, ModAttributes.RFRESWATER.get(), null);
                    break;
                case WIND:
                    percent = getAttributeValue(entity, ModAttributes.RFRESWIND.get(), null);
                    break;
                case NONE:
                    break;
            }
            if (percent < 0) {
                amount *= 1.0f + Math.abs(percent) / 100.0f;
            } else if (percent > 100) {
                amount *= -((percent - 100) / 100.0f);
            } else {
                amount *= 1.0f - percent / 100.0f;
            }
        }
        return amount;
    }

    public static void knockBack(LivingEntity entity, CustomDamage source) {
        if (source.getKnockBackType() == CustomDamage.KnockBackType.NONE)
            return;
        Entity attacker = source.getTrueSource();
        float strength = source.knockAmount();
        strength = (float) (strength * (1.0D - entity.getAttributeValue(Attributes.GENERIC_KNOCKBACK_RESISTANCE)));
        double xRatio = 0.0;
        double zRatio = 0.0;
        double yRatio = strength;
        if (attacker != null) {
            switch (source.getKnockBackType()) {
                case BACK:
                    Vector3d distVec = entity.getPositionVec().subtract(attacker.getPositionVec()).normalize();
                    xRatio = -distVec.x;
                    zRatio = -distVec.z;
                    break;
                case VANILLA:
                    xRatio = MathHelper.sin(attacker.rotationYaw * ((float)Math.PI / 180F));
                    zRatio = -MathHelper.cos(attacker.rotationYaw * ((float)Math.PI / 180F));
                    break;
                case UP:
                    break;
            }
        }
        if(source.getKnockBackType() == CustomDamage.KnockBackType.VANILLA && strength > 0){
            entity.takeKnockback(strength, xRatio, zRatio);
        }
        else {
            Vector3d mot = entity.getMotion();
            double y = mot.y;
            entity.isAirBorne = true;
            if (xRatio != 0.0 || zRatio != 0.0) {
                float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
                mot = mot.scale(0.5).add(xRatio / f * strength, 0, zRatio / f * strength);
            }
            if (source.getKnockBackType() != CustomDamage.KnockBackType.UP) {
                if (entity.isOnGround()) {
                    y /= 2.0;
                    y += strength;
                    if (y > 0.4000000059604645) {
                        y = 0.4000000059604645;
                    }
                }
            } else if (yRatio != 0.0) {
                y = yRatio;
            }
            entity.setMotion(new Vector3d(mot.x, y, mot.z));
        }
    }

    /**
     * The player attack
     *
     * @param player        the attacking player
     * @param target        the target
     * @param resetCooldown should the attack reset cooldown
     * @param playSound     should the attack play sounds
     * @return if the attack was successful or not
     */
    public static boolean playerAttackWithItem(PlayerEntity player, Entity target, boolean resetCooldown, boolean playSound, boolean levelSkill) {
        IPlayerCap cap = player.getCapability(CapabilityInsts.PlayerCap).orElseThrow(() -> new NullPointerException("Error getting capability"));
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof IItemUsable))
            return false;
        IItemUsable item = (IItemUsable) stack.getItem();
        if (target.canBeAttackedWithItem() && !target.hitByEntity(player) && player.getCooldownTracker().getCooldown(stack.getItem(), 0.0f) <= 0) {
            float damagePhys = cap.getAttributeValue(player, Attributes.GENERIC_ATTACK_DAMAGE);
            if (damagePhys > 0) {
                if (resetCooldown) {
                    player.getCooldownTracker().setCooldown(stack.getItem(), item.itemCoolDownTicks());
                }
                boolean faintChance = player.world.rand.nextInt(100) < getAttributeValue(player, ModAttributes.RFFAINT.get(), target);
                boolean critChance = player.world.rand.nextInt(100) < getAttributeValue(player, ModAttributes.RFCRIT.get(), target);
                boolean knockBackChance = player.world.rand.nextInt(100) < getAttributeValue(player, ModAttributes.RFKNOCK.get(), target);
                int i = knockBackChance ? 1 : 0;
                if (player.isSprinting()) {
                    player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0f, 1.0f);
                    ++i;
                }
                Vector3d targetMot = target.getMotion();
                damagePhys = faintChance ? Float.MAX_VALUE : GeneralConfig.randomDamage ? damagePhys + (float) (player.world.rand.nextGaussian() * damagePhys / 10.0) : damagePhys;
                boolean ignoreArmor = critChance || faintChance;
                float knockback = i * 0.5f + 0.1f;
                if (target instanceof BaseMonster) {
                    knockback *= 0.85f;
                }
                CustomDamage source = new CustomDamage.Builder(player).element(ItemNBT.getElement(stack)).damageType(ignoreArmor ? CustomDamage.DamageType.IGNOREDEF : CustomDamage.DamageType.NORMAL).knock(CustomDamage.KnockBackType.VANILLA).knockAmount(knockback).hurtResistant(0).get();
                if (damage(player, target, source, damagePhys, stack)) {
                    //Level skill on successful attack
                    if (levelSkill && player instanceof ServerPlayerEntity)
                        item.onEntityHit((ServerPlayerEntity) player);
                    if (i > 0) {
                        player.setMotion(player.getMotion().mul(0.6D, 1.0D, 0.6D));
                        player.setSprinting(false);
                    }
                    if (target instanceof ServerPlayerEntity && target.velocityChanged) {
                        ((ServerPlayerEntity) target).connection.sendPacket(new SEntityVelocityPacket(target));
                        target.velocityChanged = false;
                        target.setMotion(targetMot);
                    }
                    if (critChance) {
                        if (playSound) {
                            player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0f, 1.0f);
                        }
                        player.onCriticalHit(target);
                        player.onEnchantmentCritical(target);
                    } else if (stack.getItem() instanceof IAOEWeapon && ((IAOEWeapon) stack.getItem()).getFOV() == 0.0f && playSound) {
                        player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0f, 1.0f);
                    } else if (playSound) {
                        player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0f, 1.0f);
                    }
                } else if (playSound) {
                    player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, player.getSoundCategory(), 1.0f, 1.0f);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, CustomDamage source, float dmg) {
        if(target.world.getDifficulty() == Difficulty.PEACEFUL && target instanceof PlayerEntity)
            return false;
        if (dmg > 0) {
            boolean faintChance = attacker.world.rand.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFFAINT.get(), target);
            dmg = faintChance ? Float.MAX_VALUE : GeneralConfig.randomDamage ? dmg + (float) (attacker.world.rand.nextGaussian() * dmg / 10.0) : dmg;
            return damage(attacker, target, source, dmg, null);
        }
        return false;
    }

    public static CustomDamage.Builder build(LivingEntity attacker, LivingEntity target, CustomDamage.Builder builder) {
        boolean faintChance = attacker.world.rand.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFFAINT.get(), target);
        boolean critChance = attacker.world.rand.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFCRIT.get(), target);
        boolean knockBackChance = attacker.world.rand.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFKNOCK.get(), target);
        int i = knockBackChance ? 2 : 1;
        if (attacker.isSprinting()) {
            attacker.world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, attacker.getSoundCategory(), 1.0f, 1.0f);
            ++i;
        }
        boolean ignoreArmor = critChance || faintChance;
        float knockback = i * 0.5f - 0.1f;
        if (target instanceof BaseMonster) {
            knockback *= 0.85f;
        }
        return builder.damageType(ignoreArmor ? CustomDamage.DamageType.IGNOREDEF : CustomDamage.DamageType.NORMAL).knock(CustomDamage.KnockBackType.VANILLA).knockAmount(knockback);
    }

    public static boolean damage(@Nullable LivingEntity attacker, Entity target, CustomDamage source, float damagePhys, @Nullable ItemStack stack) {
        boolean success = target.attackEntityFrom(source, damagePhys);
        spawnElementalParticle(target, source.getElement());
        if (success) {
            if(attacker != null) {
                int drainPercent = (int) getAttributeValue(attacker, ModAttributes.RFDRAIN.get(), target);
                if (drainPercent > 0f) {
                    if (attacker instanceof PlayerEntity)
                        attacker.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> cap.regenHealth((PlayerEntity) attacker, drainPercent * damagePhys));
                    else
                        attacker.heal(drainPercent * damagePhys);
                }
                attacker.setLastAttackedEntity(target);
            }
            if(target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) target;
                knockBack(livingTarget, source);
                if(attacker != null) {
                    applyStatusEffects(attacker, livingTarget);
                    EnchantmentHelper.applyThornEnchantments(livingTarget, attacker);
                    if (stack != null && attacker instanceof PlayerEntity) {
                        ItemStack beforeHitCopy = stack.copy();
                        stack.hitEntity(livingTarget, (PlayerEntity) attacker);
                        if (stack.isEmpty()) {
                            ForgeEventFactory.onPlayerDestroyItem((PlayerEntity) attacker, beforeHitCopy, Hand.MAIN_HAND);
                            attacker.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    }
                }
            }
            elementalEffects(source.getElement(), target);
        }
        return success;
    }

    public static void elementalEffects(EnumElement element, Entity target) {
        if (!(target instanceof IBaseMob)) {
            if(element == EnumElement.FIRE)
                target.setFire(3);
            if(element == EnumElement.DARK && target instanceof LivingEntity)
                ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.WITHER, 200));
        }
    }

    //TODO: Need to look more into stun and dizzy
    public static void applyStatusEffects(LivingEntity attackingEntity, LivingEntity target) {
        if (target instanceof PlayerEntity || target instanceof IExtendedMob) {
            boolean poisonChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFPOISON.get(), target);
            boolean sleepChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFSLEEP.get(), target);
            boolean fatigueChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFFAT.get(), target);
            boolean coldChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFCOLD.get(), target);
            boolean paraChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFPARA.get(), target);
            boolean sealChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFSEAL.get(), target);
            boolean dizzyChance = attackingEntity.world.rand.nextInt(1000) < getAttributeValue(attackingEntity, ModAttributes.RFDIZ.get(), target);
            boolean stunChance = attackingEntity.world.rand.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFSTUN.get(), target);
            if (poisonChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.poison.get()));
            }
            if (fatigueChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.fatigue.get()));
            }
            if (coldChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.cold.get()));
            }
            if (paraChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.paralysis.get()));
            }
            if (sealChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.seal.get()));
            }
            if (dizzyChance) {
                target.addPotionEffect(new EffectInstance(Effects.NAUSEA, 80, 1, true, false));
            }
            if (stunChance) {
                target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 80, 4, true, false));
                target.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 80, 1, true, false));
            }
            if (sleepChance) {
                target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 80, 1, true, false));
                target.addPotionEffect(new EffectInstance(ModPotions.sleep.get(), 80, 1, true, false));
            }
        }
    }

    public static void spawnElementalParticle(Entity target, EnumElement element) {
        if (target.world instanceof ServerWorld) {
            int color = 0xFFFFFF;
            switch (element) {
                case DARK:
                    color = 0x1B133F;
                    break;
                case EARTH:
                    color = 0x8C680F;
                    break;
                case FIRE:
                    color = 0xC40707;
                    break;
                case LIGHT:
                    color = 0xFFFF47;
                    break;
                case LOVE:
                    color = 0xF783DA;
                    break;
                case WATER:
                    color = 0x2A6FDD;
                    break;
                case WIND:
                    color = 0x21A51A;
                    break;
                default:
                    break;
            }
            double r = (color >> 16 & 0xFF) / 255.0;
            double g = (color >> 8 & 0xFF) / 255.0;
            double b = (color >> 0 & 0xFF) / 255.0;
            Random rand = new Random();
            for (int i = 0; i < 7; ++i) {
                ((ServerWorld) target.world).spawnParticle(ParticleTypes.ENTITY_EFFECT, target.getX() + (rand.nextDouble() - 0.5) * target.getWidth(), target.getY() + 0.3 + rand.nextDouble() * target.getHeight(), target.getZ() + (rand.nextDouble() - 0.5) * target.getWidth(), 0, r, g, b, 1);
            }
        }
    }
}