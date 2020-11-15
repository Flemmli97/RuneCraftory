package com.flemmli97.runecraftory.combat.utils;

import com.flemmli97.runecraftory.lib.EnumElement;
import com.flemmli97.runecraftory.mobs.CustomDamage;
import com.flemmli97.runecraftory.mobs.IBaseMob;
import com.flemmli97.runecraftory.mobs.IExtendedMob;
import com.flemmli97.runecraftory.mobs.MobUtils;
import com.flemmli97.runecraftory.registry.ModAttributes;
import com.flemmli97.runecraftory.registry.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CombatUtils {

    public static void applyAddEffect(EnumElement element, LivingEntity target) {
        if (!(target instanceof IBaseMob) && element == EnumElement.FIRE && !target.isBurning()) {
            target.setFire(3);
        }
    }

    //TODO: Need to look more into stun and dizzy
    public static void applyStatusEffects(LivingEntity attackingEntity, LivingEntity target) {
        if (target instanceof PlayerEntity || target instanceof IExtendedMob) {
            boolean poisonChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFPOISON, target);
            boolean sleepChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFSLEEP, target);
            boolean fatigueChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFFAT, target);
            boolean coldChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFCOLD, target);
            boolean paraChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFPARA, target);
            boolean sealChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFSEAL, target);
            boolean dizzyChance = attackingEntity.world.rand.nextInt(1000) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFDIZ, target);
            boolean stunChance = attackingEntity.world.rand.nextInt(100) < MobUtils.getAttributeValue(attackingEntity, ModAttributes.RFSTUN, target);
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
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESDARK, null);
                    break;
                case EARTH:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESEARTH, null);
                    break;
                case FIRE:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESFIRE, null);
                    break;
                case LIGHT:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESLIGHT, null);
                    break;
                case LOVE:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESLOVE, null);
                    break;
                case WATER:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESWATER, null);
                    break;
                case WIND:
                    percent = MobUtils.getAttributeValue(entity, ModAttributes.RFRESWIND, null);
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
        if(source.getKnockBackType() == CustomDamage.KnockBackType.NONE)
            return;
        Entity attacker = source.getTrueSource();
        float strength = source.knockAmount();
        strength = (float)(strength * (1.0D - entity.getAttributeValue(Attributes.GENERIC_KNOCKBACK_RESISTANCE)));
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