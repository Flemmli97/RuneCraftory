package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.IExtendedMob;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;

public class CombatUtils {

    /**
     * For damage calculation target should be null. The damage reduction gets done at the target.
     */
    public static int getAttributeValueRaw(Entity attacker, Attribute att) {
        return getAttributeValue(attacker, att, null);
    }

    /**
     * For damage calculation target should be null. The damage reduction gets done at the target.
     */
    public static int getAttributeValue(Entity entity, Attribute att, Entity target) {
        if (!(entity instanceof LivingEntity attacker))
            return 1;
        float increase = 0;
        if (attacker instanceof Player player) {
            increase += Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getAttributeValue((Player) attacker, att)).orElse(0d);
        } else if (attacker.getAttribute(att) != null) {
            increase += attacker.getAttributeValue(att);
        }
        if (!(target instanceof LivingEntity))
            return (int) increase;
        Attribute opp = opposing(att);
        if (opp == null)
            return (int) increase;
        if (target instanceof Player player) {
            increase -= Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getAttributeValue((Player) target, opp)).orElse(0d);
        } else {
            LivingEntity living = (LivingEntity) target;
            if (living.getAttribute(opp) != null)
                increase -= living.getAttributeValue(opp);
        }
        return (int) increase;
    }

    public static Attribute opposing(Attribute att) {
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
        return null;
    }

    public static float reduceDamageFromStats(LivingEntity entity, DamageSource source, float amount) {
        int reduce = 0;
        if (!GeneralConfig.disableDefence && !source.isBypassMagic()) {
            if (!source.isBypassArmor()) {
                if (source.isMagic())
                    reduce = getAttributeValue(entity, ModAttributes.RF_MAGIC_DEFENCE.get(), null);
                else
                    reduce = getAttributeValue(entity, ModAttributes.RF_DEFENCE.get(), null);
            }
        }
        float dmg = Math.max(0.02f * amount, amount - reduce);
        if (source instanceof CustomDamage && GeneralConfig.randomDamage) {
            dmg = (float) Math.floor(dmg + (entity.level.random.nextGaussian() * dmg / 10.0));
        }
        return elementalReduction(entity, source, dmg);
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
        Entity attacker = source.getEntity();
        float strength = source.knockAmount();
        strength = (float) (strength * (1.0D - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        double xRatio = 0.0;
        double zRatio = 0.0;
        double yRatio = strength;
        if (attacker != null) {
            switch (source.getKnockBackType()) {
                case BACK:
                    Vec3 distVec = entity.position().subtract(attacker.position()).normalize();
                    xRatio = -distVec.x;
                    zRatio = -distVec.z;
                    break;
                case VANILLA:
                    xRatio = Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F));
                    zRatio = -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F));
                    break;
                case UP:
                    break;
            }
        }
        if (source.getKnockBackType() == CustomDamage.KnockBackType.VANILLA && strength > 0) {
            entity.knockback(strength, xRatio, zRatio);
        } else {
            Vec3 mot = entity.getDeltaMovement();
            double y = mot.y;
            entity.hasImpulse = true;
            if (xRatio != 0.0 || zRatio != 0.0) {
                float f = (float) Math.sqrt(xRatio * xRatio + zRatio * zRatio);
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
            entity.setDeltaMovement(new Vec3(mot.x, y, mot.z));
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
    public static boolean playerAttackWithItem(Player player, Entity target, boolean resetCooldown, boolean playSound, boolean levelSkill) {
        return CombatUtils.playerAttackWithItem(player, target, player.getMainHandItem(), 1, resetCooldown, playSound, levelSkill);
    }

    public static boolean playerAttackWithItem(Player player, Entity target, ItemStack stack, float damageModifier, boolean resetCooldown, boolean playSound, boolean levelSkill) {
        if (!(stack.getItem() instanceof IItemUsable item))
            return false;
        if (target.isAttackable() && !target.skipAttackInteraction(player) && player.getCooldowns().getCooldownPercent(stack.getItem(), 0.0f) <= 0) {
            float damagePhys = getAttributeValueRaw(player, Attributes.ATTACK_DAMAGE) * damageModifier;
            if (damagePhys > 0) {
                if (resetCooldown) {
                    player.getCooldowns().addCooldown(stack.getItem(), item.itemCoolDownTicks());
                }
                boolean faintChance = player.level.random.nextInt(100) < getAttributeValue(player, ModAttributes.RFFAINT.get(), target);
                boolean critChance = player.level.random.nextInt(100) < getAttributeValue(player, ModAttributes.RFCRIT.get(), target);
                boolean knockBackChance = player.level.random.nextInt(100) < getAttributeValue(player, ModAttributes.RFKNOCK.get(), target);
                int i = knockBackChance ? 1 : 0;
                if (player.isSprinting()) {
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0f, 1.0f);
                    ++i;
                }
                Vec3 targetMot = target.getDeltaMovement();
                damagePhys = faintChance ? Float.MAX_VALUE : damagePhys;
                boolean ignoreArmor = critChance || faintChance;
                float knockback = i * 0.5f + 0.1f;
                if (target instanceof BaseMonster) {
                    knockback *= 0.85f;
                }
                if (player.level instanceof ServerLevel serverLevel)
                    ModSpells.STAFFCAST.get().use(serverLevel, player, stack, 1, 1, 1);
                CustomDamage source = new CustomDamage.Builder(player).element(ItemNBT.getElement(stack)).damageType(ignoreArmor ? CustomDamage.DamageType.IGNOREDEF : CustomDamage.DamageType.NORMAL).knock(CustomDamage.KnockBackType.VANILLA)
                        .knockAmount(knockback).hurtResistant(0).get();
                if (damage(player, target, source, damagePhys, stack)) {
                    //Level skill on successful attack
                    if (levelSkill && player instanceof ServerPlayer)
                        item.onEntityHit((ServerPlayer) player, stack);
                    if (i > 0) {
                        player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                        player.setSprinting(false);
                    }
                    if (target instanceof ServerPlayer serverPlayer && target.hurtMarked) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                        target.hurtMarked = false;
                        target.setDeltaMovement(targetMot);
                    }
                    if (critChance) {
                        if (playSound) {
                            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0f, 1.0f);
                        }
                        player.crit(target);
                        player.magicCrit(target);
                    } else if (stack.getItem() instanceof IAOEWeapon && ((IAOEWeapon) stack.getItem()).getFOV() == 0.0f && playSound) {
                        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0f, 1.0f);
                    } else if (playSound) {
                        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0f, 1.0f);
                    }
                } else if (playSound) {
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0f, 1.0f);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target) {
        CustomDamage source = build(attacker, target, new CustomDamage.Builder(attacker)).hurtResistant(5).get();
        return mobAttack(attacker, target, source, CombatUtils.getAttributeValue(attacker, Attributes.ATTACK_DAMAGE, target));
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, CustomDamage source, float dmg) {
        if (target.level.getDifficulty() == Difficulty.PEACEFUL && target instanceof Player)
            return false;
        if (dmg > 0) {
            boolean faintChance = attacker.level.random.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFFAINT.get(), target);
            dmg = faintChance ? Float.MAX_VALUE : dmg;
            return damage(attacker, target, source, dmg, null);
        }
        return false;
    }

    public static CustomDamage.Builder build(LivingEntity attacker, Entity target, CustomDamage.Builder builder) {
        boolean faintChance = attacker.level.random.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFFAINT.get(), target);
        boolean critChance = attacker.level.random.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFCRIT.get(), target);
        boolean knockBackChance = attacker.level.random.nextInt(100) < getAttributeValue(attacker, ModAttributes.RFKNOCK.get(), target);
        int i = knockBackChance ? 2 : 1;
        if (attacker.isSprinting()) {
            attacker.level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, attacker.getSoundSource(), 1.0f, 1.0f);
            ++i;
        }
        boolean ignoreArmor = critChance || faintChance;
        float knockback = i * 0.5f - 0.1f;
        if (target instanceof BaseMonster) {
            knockback *= 0.85f;
        }
        return builder.damageType(ignoreArmor ? CustomDamage.DamageType.IGNOREDEF : CustomDamage.DamageType.NORMAL).knock(CustomDamage.KnockBackType.VANILLA).knockAmount(knockback);
    }

    public static boolean damage(@Nullable Entity attacker, Entity target, CustomDamage source, float damage, @Nullable ItemStack stack) {
        boolean success = target.hurt(source, damage);
        if (success) {
            spawnElementalParticle(target, source.getElement());
            if (attacker instanceof LivingEntity livingAttacker) {
                int drainPercent = getAttributeValue(attacker, ModAttributes.RFDRAIN.get(), target);
                if (drainPercent > 0f) {
                    if (livingAttacker instanceof Player player)
                        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.regenHealth((Player) attacker, drainPercent * damage));
                    else
                        livingAttacker.heal(drainPercent * damage);
                }
                livingAttacker.setLastHurtMob(target);
            }
            if (target instanceof LivingEntity livingTarget) {
                knockBack(livingTarget, source);
                if (attacker instanceof LivingEntity livingAttacker) {
                    applyStatusEffects(livingAttacker, livingTarget);
                    EnchantmentHelper.doPostHurtEffects(livingTarget, livingAttacker);
                    if (stack != null && livingAttacker instanceof Player player) {
                        ItemStack beforeHitCopy = stack.copy();
                        stack.hurtEnemy(livingTarget, player);
                        if (stack.isEmpty()) {
                            Platform.INSTANCE.destroyItem(player, beforeHitCopy, InteractionHand.MAIN_HAND);
                            livingAttacker.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
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
            if (element == EnumElement.FIRE)
                target.setSecondsOnFire(3);
            if (element == EnumElement.DARK && target instanceof LivingEntity)
                ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
        }
    }

    //TODO: Need to look more into stun and dizzy
    public static void applyStatusEffects(LivingEntity attackingEntity, LivingEntity target) {
        if (target instanceof Player || target instanceof IExtendedMob) {
            boolean poisonChance = attackingEntity.level.random.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFPOISON.get(), target);
            boolean sleepChance = attackingEntity.level.random.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFSLEEP.get(), target);
            boolean fatigueChance = attackingEntity.level.random.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFFAT.get(), target);
            boolean coldChance = attackingEntity.level.random.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFCOLD.get(), target);
            boolean paraChance = attackingEntity.level.random.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFPARA.get(), target);
            boolean sealChance = attackingEntity.level.random.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFSEAL.get(), target);
            boolean dizzyChance = attackingEntity.level.random.nextInt(1000) < getAttributeValue(attackingEntity, ModAttributes.RFDIZ.get(), target);
            boolean stunChance = attackingEntity.level.random.nextInt(100) < getAttributeValue(attackingEntity, ModAttributes.RFSTUN.get(), target);
            if (poisonChance) {
                target.addEffect(new MobEffectInstance(ModEffects.poison.get()));
            }
            if (fatigueChance) {
                target.addEffect(new MobEffectInstance(ModEffects.fatigue.get()));
            }
            if (coldChance) {
                target.addEffect(new MobEffectInstance(ModEffects.cold.get()));
            }
            if (paraChance) {
                target.addEffect(new MobEffectInstance(ModEffects.paralysis.get()));
            }
            if (sealChance) {
                target.addEffect(new MobEffectInstance(ModEffects.seal.get()));
            }
            if (dizzyChance) {
                target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 1, true, false));
            }
            if (stunChance) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 4, true, false));
                target.addEffect(new MobEffectInstance(MobEffects.JUMP, 80, 1, true, false));
            }
            if (sleepChance) {
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 80, 1, true, false));
                target.addEffect(new MobEffectInstance(ModEffects.sleep.get(), 80, 1, true, false));
            }
        }
    }

    public static void spawnElementalParticle(Entity target, EnumElement element) {
        if (target.level instanceof ServerLevel serverLevel) {
            int color = 0xFFFFFF;
            switch (element) {
                case DARK -> color = 0x1B133F;
                case EARTH -> color = 0x8C680F;
                case FIRE -> color = 0xC40707;
                case LIGHT -> color = 0xFFFF47;
                case LOVE -> color = 0xF783DA;
                case WATER -> color = 0x2A6FDD;
                case WIND -> color = 0x21A51A;
                default -> {
                }
            }
            double r = (color >> 16 & 0xFF) / 255.0;
            double g = (color >> 8 & 0xFF) / 255.0;
            double b = (color >> 0 & 0xFF) / 255.0;
            Random rand = new Random();
            for (int i = 0; i < 7; ++i) {
                serverLevel.sendParticles(ParticleTypes.ENTITY_EFFECT, target.getX() + (rand.nextDouble() - 0.5) * target.getBbWidth(), target.getY() + 0.3 + rand.nextDouble() * target.getBbHeight(), target.getZ() + (rand.nextDouble() - 0.5) * target.getBbWidth(), 0, r, g, b, 1);
            }
        }
    }
}