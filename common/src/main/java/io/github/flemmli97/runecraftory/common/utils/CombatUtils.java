package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
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

    private static final UUID TEMP_ATTRIBUTE_MOD = UUID.fromString("5c8e5c2d-1eb0-434a-858f-8ab81f51832c");

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
        if (att == ModAttributes.PARA.get())
            return ModAttributes.RES_PARA.get();
        if (att == ModAttributes.POISON.get())
            return ModAttributes.RES_POISON.get();
        if (att == ModAttributes.SEAL.get())
            return ModAttributes.RES_SEAL.get();
        if (att == ModAttributes.SLEEP.get())
            return ModAttributes.RES_SLEEP.get();
        if (att == ModAttributes.FATIGUE.get())
            return ModAttributes.RES_FAT.get();
        if (att == ModAttributes.COLD.get())
            return ModAttributes.RES_COLD.get();
        if (att == ModAttributes.DIZZY.get())
            return ModAttributes.RES_DIZZY.get();
        if (att == ModAttributes.CRIT.get())
            return ModAttributes.RES_CRIT.get();
        if (att == ModAttributes.STUN.get())
            return ModAttributes.RES_STUN.get();
        if (att == ModAttributes.FAINT.get())
            return ModAttributes.RES_FAINT.get();
        if (att == ModAttributes.DRAIN.get())
            return ModAttributes.RES_DRAIN.get();
        return null;
    }

    public static EnumSkills matchingSkill(Attribute att) {
        if (att == ModAttributes.PARA.get())
            return EnumSkills.RES_PARA;
        if (att == ModAttributes.POISON.get())
            return EnumSkills.RES_POISON;
        if (att == ModAttributes.SEAL.get())
            return EnumSkills.RES_SEAL;
        if (att == ModAttributes.SLEEP.get())
            return EnumSkills.RES_SLEEP;
        if (att == ModAttributes.FATIGUE.get())
            return EnumSkills.RES_FATIGUE;
        if (att == ModAttributes.COLD.get())
            return EnumSkills.RES_COLD;
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
                        reduce = (float) getAttributeValue(entity, ModAttributes.MAGIC_DEFENCE.get());
                } else if (!source.isBypassArmor()) {
                    reduce = (float) getAttributeValue(entity, ModAttributes.DEFENCE.get());
                }
            }
        }
        float dmg = amount - reduce;
        if (reduce > amount * 0.8)
            dmg = (float) Math.max(0.05 * amount, amount * 0.2 * Math.pow(0.997, reduce - amount * 0.8));
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
                    percent = getAttributeValue(entity, ModAttributes.RES_DARK.get());
                    break;
                case EARTH:
                    percent = getAttributeValue(entity, ModAttributes.RES_EARTH.get());
                    break;
                case FIRE:
                    percent = getAttributeValue(entity, ModAttributes.RES_FIRE.get());
                    break;
                case LIGHT:
                    percent = getAttributeValue(entity, ModAttributes.RES_LIGHT.get());
                    break;
                case LOVE:
                    percent = getAttributeValue(entity, ModAttributes.RES_LOVE.get());
                    break;
                case WATER:
                    percent = getAttributeValue(entity, ModAttributes.RES_WATER.get());
                    break;
                case WIND:
                    percent = getAttributeValue(entity, ModAttributes.RES_WIND.get());
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
            double damagePhys = getAttributeValue(player, Attributes.ATTACK_DAMAGE) * damageModifier;
            if (damagePhys > 0) {
                /*if (resetCooldown) {
                    player.getCooldowns().addCooldown(stack.getItem(), ItemNBT.cooldown(player, stack));
                }*/
                boolean faintChance = player.level.random.nextDouble() < statusEffectChance(player, ModAttributes.FAINT.get(), target);
                boolean critChance = player.level.random.nextDouble() < statusEffectChance(player, ModAttributes.CRIT.get(), target);
                CustomDamage.DamageType damageType = CustomDamage.DamageType.NORMAL;
                if (faintChance)
                    damageType = CustomDamage.DamageType.FAINT;
                else if (critChance)
                    damageType = CustomDamage.DamageType.IGNOREDEF;
                boolean knockBackChance = player.level.random.nextDouble() < statusEffectChance(player, ModAttributes.KNOCK.get(), target);
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
                    ModSpells.STAFF_CAST.get().use(serverLevel, player, stack);
                if (ItemNBT.doesFixedOneDamage(stack)) {
                    damageType = CustomDamage.DamageType.FIXED;
                    damagePhys = 1;
                }
                CustomDamage.Builder source = new CustomDamage.Builder(player).element(ItemNBT.getElement(stack)).damageType(damageType).knock(CustomDamage.KnockBackType.VANILLA)
                        .knockAmount(knockback).hurtResistant(0);
                Vec3 targetMot = target.getDeltaMovement();
                if (damageWithFaintAndCrit(player, target, source, damagePhys, stack)) {
                    //Level skill on successful attack
                    if (levelSkill && player instanceof ServerPlayer serverPlayer)
                        hitEntityWithItemPlayer(serverPlayer, stack);
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
                    } else if (stack.getItem() instanceof IAOEWeapon aoe && aoe.getFOV(player, stack) == 0.0f && playSound) {
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
        CustomDamage.Builder source = new CustomDamage.Builder(attacker).hurtResistant(5).element(ItemNBT.getElement(stack));
        double damagePhys = getAttributeValue(attacker, Attributes.ATTACK_DAMAGE);
        if (attacker.level instanceof ServerLevel serverLevel)
            ModSpells.STAFF_CAST.get().use(serverLevel, attacker, stack);
        if (ItemNBT.doesFixedOneDamage(stack)) {
            source.damageType(CustomDamage.DamageType.FIXED);
            damagePhys = 1;
        }
        return mobAttack(attacker, target, source, damagePhys);
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, CustomDamage.Builder source, double dmg) {
        if (target.level.getDifficulty() == Difficulty.PEACEFUL && target instanceof Player)
            return false;
        if (dmg > 0) {
            return damageWithFaintAndCrit(attacker, target, source, dmg, null);
        }
        return false;
    }

    public static boolean damageWithFaintAndCrit(@Nullable Entity attacker, Entity target, CustomDamage.Builder builder, double damage, @Nullable ItemStack stack) {
        return damage(attacker, target, builder, damage, stack, true, true);
    }

    public static boolean damage(@Nullable Entity attacker, Entity target, CustomDamage.Builder builder, double damage, @Nullable ItemStack stack, boolean allowCrit, boolean allowFaint) {
        // Setup some more things
        if (attacker instanceof LivingEntity livingAttacker) {
            builder.getAttributesChanges().forEach((att, val) -> CombatUtils.applyTempAttribute(livingAttacker, att, val));
            if (allowFaint && livingAttacker.level.random.nextDouble() < statusEffectChance(livingAttacker, ModAttributes.FAINT.get(), target)) {
                builder.damageType(CustomDamage.DamageType.FAINT);
            } else if (allowCrit && livingAttacker.level.random.nextDouble() < statusEffectChance(livingAttacker, ModAttributes.CRIT.get(), target)) {
                switch (builder.getDamageType()) {
                    case MAGIC -> builder.damageType(CustomDamage.DamageType.IGNOREMAGICDEF);
                    case NORMAL -> builder.damageType(CustomDamage.DamageType.IGNOREDEF);
                }
            }
            if (builder.calculateKnockback()) {
                boolean knockBackChance = livingAttacker.level.random.nextDouble() < statusEffectChance(livingAttacker, ModAttributes.KNOCK.get(), target);
                int i = knockBackChance ? 2 : 1;
                if (livingAttacker.isSprinting()) {
                    ++i;
                }
                float knockback = i * 0.5f - 0.1f;
                if (target instanceof BaseMonster) {
                    knockback *= 0.85f;
                }
                builder.knockAmount(knockback);
            }
        }

        CustomDamage source = builder.get();
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
        if (attacker instanceof LivingEntity livingAttacker) {
            source.getAttributesChange().forEach((att, val) -> CombatUtils.removeAttribute(livingAttacker, att));
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
        boolean poisonChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.POISON.get(), target);
        boolean sleepChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.SLEEP.get(), target);
        boolean fatigueChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.FATIGUE.get(), target);
        boolean coldChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.COLD.get(), target);
        boolean paraChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.PARA.get(), target);
        boolean sealChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.SEAL.get(), target);
        boolean dizzyChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.DIZZY.get(), target);
        boolean stunChance = attackingEntity.level.random.nextDouble() < statusEffectChance(attackingEntity, ModAttributes.STUN.get(), target);
        if (poisonChance) {
            EntityUtils.applyPermanentEffect(target, ModEffects.POISON.get(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_POISON, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_POISON, 15));
        }
        if (fatigueChance) {
            EntityUtils.applyPermanentEffect(target, ModEffects.FATIGUE.get(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_FATIGUE, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_FATIGUE, 15));
        }
        if (coldChance) {
            EntityUtils.applyPermanentEffect(target, ModEffects.COLD.get(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_COLD, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_COLD, 15));
        }
        if (paraChance) {
            EntityUtils.applyPermanentEffect(target, ModEffects.PARALYSIS.get(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_PARA, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_PARA, 15));
        }
        if (sealChance) {
            EntityUtils.applyPermanentEffect(target, ModEffects.SEAL.get(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_SEAL, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_SEAL, 15));
        }
        if (dizzyChance) {
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 1, true, false));
        }
        if (stunChance) {
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 4, true, false));
            target.addEffect(new MobEffectInstance(MobEffects.JUMP, 80, 1, true, false));
        }
        if (sleepChance) {
            target.addEffect(new MobEffectInstance(ModEffects.SLEEP.get(), 80, 0, true, false));
            if (attackingEntity instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_SLEEP, 5));
            if (target instanceof ServerPlayer player)
                Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.RES_SLEEP, 15));
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
        if (inst != null && inst.getModifier(TEMP_ATTRIBUTE_MOD) == null)
            inst.addTransientModifier(new AttributeModifier(TEMP_ATTRIBUTE_MOD, "temp_mod", val, AttributeModifier.Operation.ADDITION));
    }

    public static void removeAttribute(LivingEntity entity, Attribute att) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null)
            inst.removeModifier(TEMP_ATTRIBUTE_MOD);
    }

    public static void hitEntityWithItemPlayer(ServerPlayer player, ItemStack stack) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player).orElse(null);
        if (data == null)
            return;
        //Weapons
        if (stack.getItem() instanceof ItemStaffBase) {
            switch (ItemNBT.getElement(stack)) {
                case WATER -> LevelCalc.levelSkill(player, data, EnumSkills.WATER, 3);
                case EARTH -> LevelCalc.levelSkill(player, data, EnumSkills.EARTH, 3);
                case WIND -> LevelCalc.levelSkill(player, data, EnumSkills.WIND, 3);
                case FIRE -> LevelCalc.levelSkill(player, data, EnumSkills.FIRE, 3);
                case LIGHT -> LevelCalc.levelSkill(player, data, EnumSkills.LIGHT, 3);
                case DARK -> LevelCalc.levelSkill(player, data, EnumSkills.DARK, 3);
                case LOVE -> LevelCalc.levelSkill(player, data, EnumSkills.LOVE, 3);
            }
            return;
        }
        if (stack.is(ModTags.SHORTSWORDS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.SHORTSWORD, 2);
        }
        if (stack.is(ModTags.LONGSWORDS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.LONGSWORD, 4);
        }
        if (stack.is(ModTags.SPEARS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.SPEAR, 3);
        }
        if (stack.is(ModTags.AXES) || stack.is(ModTags.HAMMERS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 5);
        }
        if (stack.is(ModTags.DUALBLADES)) {
            LevelCalc.levelSkill(player, data, EnumSkills.DUAL, 2);
        }
        if (stack.is(ModTags.FISTS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.FIST, 2);
        }
        //Tools
        if (stack.is(ModTags.AXE_TOOLS) || stack.is(ModTags.HAMMER_TOOLS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 1);
        }
        if (stack.is(ModTags.HOES) || stack.is(ModTags.WATERINGCANS) || stack.is(ModTags.SICKLES)) {
            LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 1);
        }
    }
}