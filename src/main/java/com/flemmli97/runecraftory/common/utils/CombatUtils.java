package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.IBaseMob;
import com.flemmli97.runecraftory.common.entities.IExtendedMob;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModPotions;
import com.flemmli97.tenshilib.api.item.IAOEWeapon;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

import java.util.Random;

public class CombatUtils {

    /**
     * The player attack
     *
     * @param player        the attacking player
     * @param target        the target
     * @param resetCooldown should the attack reset cooldown
     * @param playSound     should the attack play sounds
     * @return if the attack was successful or not
     */
    public static boolean doPlayerAttack(PlayerEntity player, LivingEntity target, boolean resetCooldown, boolean playSound, boolean levelSkill) {
        IPlayerCap cap = player.getCapability(PlayerCapProvider.PlayerCap).orElseThrow(() -> new NullPointerException("Error getting capability"));
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof IItemUsable))
            return false;
        IItemUsable item = (IItemUsable) stack.getItem();
        if (target.canBeAttackedWithItem() && !target.hitByEntity(player)) {
            //Gather damage from player
            float damagePhys = cap.getAttributeValue(player, Attributes.GENERIC_ATTACK_DAMAGE);
            float coolDown = player.getCooldownTracker().getCooldown(stack.getItem(), 0.0f);
            //If cooldown isnt finished disable further processing.
            if (coolDown > 0) {
                return false;
            }
            if (damagePhys > 0) {
                if (resetCooldown) {
                    System.out.println("cooldown ");
                    player.getCooldownTracker().setCooldown(stack.getItem(), item.itemCoolDownTicks());
                }
                boolean faintChance = player.world.rand.nextInt(100) < MobUtils.getAttributeValue(player, ModAttributes.RFFAINT.get(), target);
                boolean critChance = player.world.rand.nextInt(100) < MobUtils.getAttributeValue(player, ModAttributes.RFCRIT.get(), target);
                boolean knockBackChance = player.world.rand.nextInt(100) < MobUtils.getAttributeValue(player, ModAttributes.RFKNOCK.get(), target);
                int i = knockBackChance ? 2 : 1;
                if (player.isSprinting()) {
                    player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, player.getSoundCategory(), 1.0f, 1.0f);
                    ++i;
                }
                Vector3d targetMot = target.getMotion();
                damagePhys = faintChance ? Float.MAX_VALUE : damagePhys + (float) (player.world.rand.nextGaussian() * damagePhys / 10.0);
                boolean ignoreArmor = critChance || faintChance;
                //Scale damage for vanilla mobs

                damagePhys = (faintChance ? Float.MAX_VALUE : damagePhys);
                float knockback = i * 0.5f - 0.1f;
                if (target instanceof BaseMonster) {
                    knockback *= 0.85f;
                }
                CustomDamage source = new CustomDamage.Builder(player).element(ItemNBT.getElement(stack)).damageType(ignoreArmor ? CustomDamage.DamageType.IGNOREDEF : CustomDamage.DamageType.NORMAL).knock(CustomDamage.KnockBackType.VANILLA).knockAmount(knockback).hurtResistant(0).get();
                if (playerDamage(player, target, source, damagePhys, cap, stack)) {
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

    public static boolean playerDamage(PlayerEntity player, LivingEntity target, CustomDamage source, float damagePhys, IPlayerCap cap, ItemStack stack) {
        boolean success = target.attackEntityFrom(source, damagePhys);
        spawnElementalParticle(target, source.getElement());
        if (success) {
            knockBack(target, source);
            int drainPercent = (int) MobUtils.getAttributeValue(player, ModAttributes.RFDRAIN.get(), target);
            if (drainPercent > 0f) {
                cap.regenHealth(player, drainPercent * damagePhys);
            }
            applyStatusEffects(player, target);
            player.setLastAttackedEntity(target);
            EnchantmentHelper.applyThornEnchantments(target, player);
            ItemStack beforeHitCopy = stack.copy();
            stack.hitEntity(target, player);
            if (stack.isEmpty()) {
                ForgeEventFactory.onPlayerDestroyItem(player, beforeHitCopy, Hand.MAIN_HAND);
                player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            }
            applyAddEffect(ItemNBT.getElement(stack), target);
        }
        return success;
    }

    public static void applyAddEffect(EnumElement element, LivingEntity target) {
        if (!(target instanceof IBaseMob) && element == EnumElement.FIRE && !target.isBurning()) {
            target.setFire(3);
        }
    }

    //TODO: Need to look more into stun and dizzy
    public static void applyStatusEffects(LivingEntity attackingEntity, LivingEntity target) {
        if (target instanceof PlayerEntity || target instanceof IExtendedMob) {
            boolean poisonChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFPOISON.get(), target);
            boolean sleepChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFSLEEP.get(), target);
            boolean fatigueChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFFAT.get(), target);
            boolean coldChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFCOLD.get(), target);
            boolean paraChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFPARA.get(), target);
            boolean sealChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFSEAL.get(), target);
            boolean dizzyChance = attackingEntity.world.rand.nextInt(1000) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFDIZ.get(), target);
            boolean stunChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFSTUN.get(), target);
            if (poisonChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.poison));
            }
            if (fatigueChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.fatigue));
            }
            if (coldChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.cold));
            }
            if (paraChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.paralysis));
            }
            if (sealChance) {
                target.addPotionEffect(new EffectInstance(ModPotions.seal));
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
                target.addPotionEffect(new EffectInstance(ModPotions.sleep, 80, 1, true, false));
            }
        }
    }

    public static boolean attackEntity(Entity target, CustomDamage source, float amount) {
        if (target instanceof LivingEntity) {
            knockBack((LivingEntity) target, source);
        }
        if (!(source.getTrueSource() instanceof PlayerEntity) && target.world.getDifficulty() == Difficulty.PEACEFUL && target instanceof PlayerEntity) {
            return false;
        }
        spawnElementalParticle(target, source.getElement());
        return target.attackEntityFrom(source, amount);
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

    public static float elementalReduction(LivingEntity entity, DamageSource source, float amount) {
        if (source instanceof CustomDamage && ((CustomDamage) source).getElement() != EnumElement.NONE) {
            EnumElement element = ((CustomDamage) source).getElement();
            float percent = 0;
            switch (element) {
                case DARK:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESDARK.get(), null);
                    break;
                case EARTH:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESEARTH.get(), null);
                    break;
                case FIRE:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESFIRE.get(), null);
                    break;
                case LIGHT:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESLIGHT.get(), null);
                    break;
                case LOVE:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESLOVE.get(), null);
                    break;
                case WATER:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESWATER.get(), null);
                    break;
                case WIND:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESWIND.get(), null);
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
        if (attacker instanceof LivingEntity) {
            switch (source.getKnockBackType()) {
                case BACK:
                    Vector3d distVec = entity.getPositionVec().subtract(attacker.getPositionVec()).normalize();
                    xRatio = -distVec.x;
                    zRatio = -distVec.z;
                    break;
                case VANILLA:
                    xRatio = MathHelper.sin(attacker.rotationYaw * 0.017453292f);
                    zRatio = -MathHelper.cos(attacker.rotationYaw * 0.017453292f);
                    break;
                case UP:
                    break;
            }
        }

        //If knockback type is not vanilla, entity will always take knockback
        if (source.getKnockBackType() != CustomDamage.KnockBackType.VANILLA || strength > 0) {
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
}