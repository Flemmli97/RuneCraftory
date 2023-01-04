package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class CombatUtils {

    private static final UUID attributeMod = UUID.fromString("5c8e5c2d-1eb0-434a-858f-8ab81f51832c");

    /**
     * For damage calculation target should be null. The damage reduction gets done at the target.
     */
    public static double getAttributeValue(Entity entity, Attribute att) {
        if (!(entity instanceof LivingEntity attacker))
            return 0;
        double increase = 0;
        if (attacker instanceof Player player) {
            increase += Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getAttributeValue((Player) attacker, att)).orElse(0d);
        } else if (attacker.getAttribute(att) != null) {
            increase += attacker.getAttributeValue(att);
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

    public static EnumSkills matchingSkill(Attribute att) {
        if (att == ModAttributes.RFPARA.get())
            return EnumSkills.RESPARA;
        if (att == ModAttributes.RFPOISON.get())
            return EnumSkills.RESPOISON;
        if (att == ModAttributes.RFSEAL.get())
            return EnumSkills.RESSEAL;
        if (att == ModAttributes.RFSLEEP.get())
            return EnumSkills.RESSLEEP;
        if (att == ModAttributes.RFFAT.get())
            return EnumSkills.RESFAT;
        if (att == ModAttributes.RFCOLD.get())
            return EnumSkills.RESCOLD;
        return null;
    }

    public static double statusEffectChance(LivingEntity entity, Attribute att, Entity target) {
        double chance = getAttributeValue(entity, att) * 0.01;
        Attribute opposing = opposing(att);
        double res = target instanceof LivingEntity livingTarget && opposing != null ? getAttributeValue(livingTarget, opposing) : 0;
        if (target instanceof Player player) {
            EnumSkills matchingSkill = matchingSkill(att);
            if (matchingSkill != null)
                res += Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(matchingSkill).getLevel() * 0.005).orElse(0d);
        }
        res *= 0.01;
        return chance * (1 - res);
    }

    public static float reduceDamageFromStats(LivingEntity entity, DamageSource source, float amount) {
        float reduce = 0;
        if (!GeneralConfig.disableDefence) {
            if (!GeneralConfig.vanillaIgnoreDefence || source instanceof CustomDamage) {
                if (source.isMagic()) {
                    if (!source.isBypassMagic())
                        reduce = (float) getAttributeValue(entity, ModAttributes.RF_MAGIC_DEFENCE.get());
                } else if (!source.isBypassArmor()) {
                    reduce = (float) getAttributeValue(entity, ModAttributes.RF_DEFENCE.get());
                }
            }
        }
        float dmg = Math.max(0.02f * amount, amount - reduce);
        if (source instanceof CustomDamage custom && GeneralConfig.randomDamage && !custom.fixedDamage()) {
            dmg += entity.level.random.nextGaussian() * dmg / 10.0;
        }
        return elementalReduction(entity, source, dmg);
    }

    public static float elementalReduction(LivingEntity entity, DamageSource source, float amount) {
        if (source instanceof CustomDamage && ((CustomDamage) source).getElement() != EnumElement.NONE) {
            EnumElement element = ((CustomDamage) source).getElement();
            double percent = 0;
            switch (element) {
                case DARK:
                    percent = getAttributeValue(entity, ModAttributes.RFRESDARK.get());
                    break;
                case EARTH:
                    percent = getAttributeValue(entity, ModAttributes.RFRESEARTH.get());
                    break;
                case FIRE:
                    percent = getAttributeValue(entity, ModAttributes.RFRESFIRE.get());
                    break;
                case LIGHT:
                    percent = getAttributeValue(entity, ModAttributes.RFRESLIGHT.get());
                    break;
                case LOVE:
                    percent = getAttributeValue(entity, ModAttributes.RFRESLOVE.get());
                    break;
                case WATER:
                    percent = getAttributeValue(entity, ModAttributes.RFRESWATER.get());
                    break;
                case WIND:
                    percent = getAttributeValue(entity, ModAttributes.RFRESWIND.get());
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
        if (target.isAttackable() && !target.skipAttackInteraction(player) && player.getCooldowns().getCooldownPercent(stack.getItem(), 0.0f) <= 0) {
            IItemUsable item = null;
            if (stack.getItem() instanceof IItemUsable)
                item = (IItemUsable) stack.getItem();
            double damagePhys = getAttributeValue(player, Attributes.ATTACK_DAMAGE) * damageModifier;
            if (damagePhys > 0) {
                if (resetCooldown) {
                    int cooldown = item != null ? item.itemCoolDownTicks() : (int) (1 / (player.getAttributeValue(Attributes.ATTACK_SPEED) * 20));
                    player.getCooldowns().addCooldown(stack.getItem(), cooldown);
                }
                boolean faintChance = player.level.random.nextDouble() < statusEffectChance(player, ModAttributes.RFFAINT.get(), target);
                boolean critChance = player.level.random.nextDouble() < statusEffectChance(player, ModAttributes.RFCRIT.get(), target);
                CustomDamage.DamageType damageType = CustomDamage.DamageType.NORMAL;
                if (faintChance)
                    damageType = CustomDamage.DamageType.FAINT;
                else if (critChance)
                    damageType = CustomDamage.DamageType.IGNOREDEF;
                boolean knockBackChance = player.level.random.nextDouble() < statusEffectChance(player, ModAttributes.RFKNOCK.get(), target);
                int i = knockBackChance ? 1 : 0;
                if (player.isSprinting()) {
                    player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0f, 1.0f);
                    ++i;
                }
                float knockback = i * 0.5f + 0.1f;
                if (target instanceof BaseMonster) {
                    knockback *= 0.85f;
                }
                if (player.level instanceof ServerLevel serverLevel)
                    ModSpells.STAFFCAST.get().use(serverLevel, player, stack);
                if (ItemNBT.doesFixedOneDamage(stack)) {
                    damageType = CustomDamage.DamageType.FIXED;
                    damagePhys = 1;
                }
                CustomDamage source = new CustomDamage.Builder(player).element(ItemNBT.getElement(stack)).damageType(damageType).knock(CustomDamage.KnockBackType.VANILLA)
                        .knockAmount(knockback).hurtResistant(0).get();
                Vec3 targetMot = target.getDeltaMovement();
                if (damage(player, target, source, damagePhys, stack)) {
                    //Level skill on successful attack
                    if (levelSkill && player instanceof ServerPlayer && item != null)
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
        ItemStack stack = attacker.getMainHandItem();
        CustomDamage.Builder source = build(attacker, target, new CustomDamage.Builder(attacker).hurtResistant(5), false, true)
                .element(ItemNBT.getElement(stack));
        double damagePhys = getAttributeValue(attacker, Attributes.ATTACK_DAMAGE);
        if (attacker.level instanceof ServerLevel serverLevel)
            ModSpells.STAFFCAST.get().use(serverLevel, attacker, stack);
        if (ItemNBT.doesFixedOneDamage(stack)) {
            source.damageType(CustomDamage.DamageType.FIXED);
            damagePhys = 1;
        }
        return mobAttack(attacker, target, source.get(), damagePhys);
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, CustomDamage source, double dmg) {
        if (target.level.getDifficulty() == Difficulty.PEACEFUL && target instanceof Player)
            return false;
        if (dmg > 0) {
            return damage(attacker, target, source, dmg, null);
        }
        return false;
    }

    public static CustomDamage.Builder build(Entity entity, Entity target, CustomDamage.Builder builder, boolean magic, boolean withDefaultKnockback) {
        if (!(entity instanceof LivingEntity attacker)) {
            return builder.damageType(magic ? CustomDamage.DamageType.MAGIC : CustomDamage.DamageType.NORMAL);
        }
        boolean faintChance = attacker.level.random.nextDouble() < statusEffectChance(attacker, ModAttributes.RFFAINT.get(), target);
        boolean critChance = attacker.level.random.nextDouble() < statusEffectChance(attacker, ModAttributes.RFCRIT.get(), target);
        CustomDamage.DamageType damageType = magic ? CustomDamage.DamageType.MAGIC : CustomDamage.DamageType.NORMAL;
        if (faintChance)
            damageType = CustomDamage.DamageType.FAINT;
        else if (critChance)
            damageType = magic ? CustomDamage.DamageType.IGNOREMAGICDEF : CustomDamage.DamageType.IGNOREDEF;
        if (withDefaultKnockback) {
            boolean knockBackChance = attacker.level.random.nextDouble() < statusEffectChance(attacker, ModAttributes.RFKNOCK.get(), target);
            int i = knockBackChance ? 2 : 1;
            if (attacker.isSprinting()) {
                ++i;
            }
            float knockback = i * 0.5f - 0.1f;
            if (target instanceof BaseMonster) {
                knockback *= 0.85f;
            }
            builder.knockAmount(knockback);
        }
        return builder.damageType(damageType);
    }

    public static boolean damage(@Nullable Entity attacker, Entity target, CustomDamage.Builder source, boolean magic, boolean withDefaultKnockback, double damage, @Nullable ItemStack stack) {
        return damage(attacker, target, build(attacker, target, source, magic, withDefaultKnockback).get(), damage, stack);
    }

    public static boolean damage(@Nullable Entity attacker, Entity target, CustomDamage source, double damage, @Nullable ItemStack stack) {
        float dmg = (float) damage;
        if (source.criticalDamage())
            dmg = Float.MAX_VALUE;
        else if (!source.fixedDamage())
            dmg = modifyDmgElement(source.getElement(), target, dmg);
        boolean success = target.hurt(source, dmg);
        if (success) {
            spawnElementalParticle(target, source.getElement());
            if (attacker instanceof LivingEntity livingAttacker) {
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
            elementalEffects(attacker, source.getElement(), target);
        }
        return success;
    }

    public static float modifyDmgElement(EnumElement element, Entity target, float dmg) {
        if (!(target instanceof IBaseMob) && !(target instanceof Player)) {
            if (element == EnumElement.WATER && target instanceof LivingEntity living && (living.fireImmune() || living.isSensitiveToWater()))
                dmg *= 1.1;
        }
        return dmg;
    }

    public static void elementalEffects(Entity attacker, EnumElement element, Entity target) {
        if (!(target instanceof IBaseMob) && !(target instanceof Player)) {
            switch (element) {
                case FIRE -> target.setSecondsOnFire(3);
                case DARK -> {
                    if (target instanceof LivingEntity living)
                        living.addEffect(new MobEffectInstance(MobEffects.WITHER, 200));
                }
                case WIND -> {
                    if (target instanceof LivingEntity living && attacker != null)
                        living.knockback(1.5, Mth.sin(attacker.getYRot() * ((float) Math.PI / 180)), -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180)));
                }
                case WATER -> {
                    if (target instanceof LivingEntity living)
                        living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200));
                }
            }
        }
    }

    //TODO: Need to look more into stun and dizzy
    public static void applyStatusEffects(LivingEntity attackingEntity, LivingEntity target) {
        boolean poisonChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFPOISON.get(), target);
        boolean sleepChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFSLEEP.get(), target);
        boolean fatigueChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFFAT.get(), target);
        boolean coldChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFCOLD.get(), target);
        boolean paraChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFPARA.get(), target);
        boolean sealChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFSEAL.get(), target);
        boolean dizzyChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFDIZ.get(), target);
        boolean stunChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.RFSTUN.get(), target);
        if (poisonChance) {
            target.addEffect(new MobEffectInstance(ModEffects.poison.get()));
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESPOISON, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESPOISON, 15));
        }
        if (fatigueChance) {
            target.addEffect(new MobEffectInstance(ModEffects.fatigue.get()));
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESFAT, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESFAT, 15));
        }
        if (coldChance) {
            target.addEffect(new MobEffectInstance(ModEffects.cold.get()));
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESCOLD, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESCOLD, 15));
        }
        if (paraChance) {
            target.addEffect(new MobEffectInstance(ModEffects.paralysis.get()));
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESPARA, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESPARA, 15));
        }
        if (sealChance) {
            target.addEffect(new MobEffectInstance(ModEffects.seal.get()));
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESSEAL, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESSEAL, 15));
        }
        if (dizzyChance) {
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 1, true, false));
        }
        if (stunChance) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 4, true, false));
            target.addEffect(new MobEffectInstance(MobEffects.JUMP, 80, 1, true, false));
        }
        if (sleepChance) {
            target.addEffect(new MobEffectInstance(ModEffects.sleep.get(), 80, 0, true, false));
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESSLEEP, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RESSLEEP, 15));
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

    public static void applyTempAttribute(LivingEntity entity, Attribute att, double val) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null && inst.getModifier(attributeMod) == null)
            inst.addTransientModifier(new AttributeModifier(attributeMod, "temp_mod", val, AttributeModifier.Operation.ADDITION));
    }

    public static void removeAttribute(LivingEntity entity, Attribute att) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null)
            inst.removeModifier(attributeMod);
    }
}