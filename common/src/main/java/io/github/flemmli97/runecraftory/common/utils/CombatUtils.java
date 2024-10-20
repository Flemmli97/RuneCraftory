package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.ElementalAttackMob;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import io.github.flemmli97.tenshilib.common.utils.AOEWeaponHandler;
import io.github.flemmli97.tenshilib.common.utils.CircleSector;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CombatUtils {

    private static final UUID TEMP_ATTRIBUTE_MOD = UUID.fromString("5c8e5c2d-1eb0-434a-858f-8ab81f51832c");
    private static final UUID TEMP_ATTRIBUTE_MOD_MULT = UUID.fromString("e2465d35-6c65-4ec8-a13c-a305a9d34c66");

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
        int inc = (int) increase;
        double restRound = Math.round((increase - inc) * 2) / 2d;
        return inc + restRound;
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

    public static double statusEffectValue(LivingEntity entity, Attribute att, Entity target) {
        double value = getAttributeValue(entity, att) * 0.01;
        Attribute opposing = opposing(att);
        double res = target instanceof LivingEntity livingTarget && opposing != null ? getAttributeValue(livingTarget, opposing) : 0;
        if (target instanceof Player player) {
            EnumSkills matchingSkill = matchingSkill(att);
            if (matchingSkill != null)
                res += Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(matchingSkill).getLevel() * 0.005).orElse(0d);
        }
        res *= 0.01;
        return value * (1 - res);
    }

    public static float reduceDamageFromStats(LivingEntity entity, DamageSource source, float amount) {
        float reduce = 0;
        boolean ignoreDefence = switch (GeneralConfig.defenceSystem) {
            case NO_DEFENCE -> true;
            case VANILLA_IGNORE -> !(source instanceof CustomDamage);
            case IGNORE_VANILLA_MOBS -> !(source instanceof CustomDamage) && source.getEntity() instanceof Mob;
            case IGNORE_VANILLA_PLAYER_ATT -> !(source instanceof CustomDamage) && source.getEntity() instanceof Player;
            case IGNORE_VANILLA_PLAYER_HURT -> !(source instanceof CustomDamage) && entity instanceof Player;
            case IGNORE_VANILLA_PLAYER ->
                    !(source instanceof CustomDamage) && (entity instanceof Player || source.getEntity() instanceof Player);
        };
        if (!ignoreDefence) {
            if (source.isMagic()) {
                if (!source.isBypassMagic())
                    reduce = (float) getAttributeValue(entity, ModAttributes.MAGIC_DEFENCE.get());
            } else if (!source.isBypassArmor()) {
                reduce = (float) getAttributeValue(entity, ModAttributes.DEFENCE.get());
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

    public static void knockBackEntity(LivingEntity attacker, LivingEntity entity, float strength) {
        Vec3 distVec = entity.position().subtract(attacker.position()).normalize();
        knockbackEntityIgnoreResistance(entity, strength, -distVec.x, -distVec.z);
    }

    public static void knockbackEntityIgnoreResistance(LivingEntity entity, double strength, double x, double z) {
        if (!entity.getType().is(RunecraftoryTags.BOSSES))
            applyTempAttribute(entity, Attributes.KNOCKBACK_RESISTANCE, -entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        entity.knockback(strength, x, z);
        if (!entity.getType().is(RunecraftoryTags.BOSSES))
            removeTempAttribute(entity, Attributes.KNOCKBACK_RESISTANCE);
    }

    public static void knockBack(LivingEntity entity, CustomDamage source) {
        if (source.getKnockBackType() == CustomDamage.KnockBackType.NONE)
            return;
        Entity attacker = source.getEntity();
        float strength = source.knockAmount();
        strength = (float) (strength * (1.0D - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        if (strength == 0)
            return;
        double xRatio = 0.0;
        double zRatio = 0.0;
        double yRatio = strength;
        if (attacker != null) {
            switch (source.getKnockBackType()) {
                case BACK:
                    Vec3 distVec = entity.position().subtract(attacker.position()).normalize();
                    xRatio = distVec.x;
                    zRatio = distVec.z;
                    break;
                case VANILLA:
                    xRatio = Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F));
                    zRatio = -Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F));
                    break;
                case UP:
                    break;
            }
        }
        if (source.getKnockBackType() == CustomDamage.KnockBackType.VANILLA) {
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
     * @return if the attack was successful or not
     */
    public static boolean playerAttackWithItem(Player player, Entity target, boolean resetCooldown, boolean levelSkill) {
        return CombatUtils.playerAttackWithItem(player, target, player.getMainHandItem(), 1, resetCooldown, levelSkill);
    }

    public static boolean playerAttackWithItem(Player player, Entity target, ItemStack stack, float damageModifier, boolean resetCooldown, boolean levelSkill) {
        if (target.isAttackable() && !target.skipAttackInteraction(player) && player.getCooldowns().getCooldownPercent(stack.getItem(), 0.0f) <= 0) {
            double damagePhys = getAttributeValue(player, Attributes.ATTACK_DAMAGE) * damageModifier;
            if (damagePhys > 0) {
                boolean playSound = false;
                if (resetCooldown && stack.getItem() instanceof IItemUsable usable && usable.hasCooldown()) {
                    player.getCooldowns().addCooldown(stack.getItem(), Mth.ceil(20 * ItemNBT.attackSpeedModifier(player)));
                    playSound = true;
                }
                boolean faintChance = player.level.random.nextDouble() < statusEffectValue(player, ModAttributes.FAINT.get(), target);
                boolean critChance = player.level.random.nextDouble() < statusEffectValue(player, ModAttributes.CRIT.get(), target);
                CustomDamage.DamageType damageType = CustomDamage.DamageType.NORMAL;
                if (faintChance)
                    damageType = CustomDamage.DamageType.FAINT;
                else if (critChance)
                    damageType = CustomDamage.DamageType.IGNOREDEF;

                double knockbackAtt = statusEffectValue(player, ModAttributes.KNOCK.get(), target);
                int i = player.isSprinting() ? 1 : 0;
                i += EnchantmentHelper.getKnockbackBonus(player);
                float knockback = (float) (i * 0.5f + knockbackAtt * 3);
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
        return mobAttack(attacker, target, source);
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, CustomDamage.Builder source) {
        ItemStack stack = attacker.getMainHandItem();
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
            if (attacker instanceof ElementalAttackMob mob) {
                EnumElement element = mob.getAttackElement();
                if (element != null)
                    source.element(element);
            }
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
            if (allowFaint && livingAttacker.level.random.nextDouble() < statusEffectValue(livingAttacker, ModAttributes.FAINT.get(), target)) {
                builder.damageType(CustomDamage.DamageType.FAINT);
            } else if (allowCrit && livingAttacker.level.random.nextDouble() < statusEffectValue(livingAttacker, ModAttributes.CRIT.get(), target)) {
                switch (builder.getDamageType()) {
                    case MAGIC -> builder.damageType(CustomDamage.DamageType.IGNOREMAGICDEF);
                    case NORMAL -> builder.damageType(CustomDamage.DamageType.IGNOREDEF);
                }
            }
            if (builder.calculateKnockback()) {
                double knockbackAtt = statusEffectValue(livingAttacker, ModAttributes.KNOCK.get(), target);
                int i = livingAttacker.isSprinting() ? 1 : 0;
                i += EnchantmentHelper.getKnockbackBonus(livingAttacker);
                float knockback = (float) (i * 0.5f + knockbackAtt * 3);
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
            source.getAttributesChange().forEach((att, val) -> CombatUtils.removeTempAttribute(livingAttacker, att));
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

    public static void applyStatusEffects(LivingEntity attackingEntity, LivingEntity target) {
        boolean poisonChance = attackingEntity.level.random.nextDouble() < statusEffectValue(attackingEntity, ModAttributes.POISON.get(), target);
        boolean sleepChance = attackingEntity.level.random.nextDouble() < statusEffectValue(attackingEntity, ModAttributes.SLEEP.get(), target);
        boolean fatigueChance = attackingEntity.level.random.nextDouble() < statusEffectValue(attackingEntity, ModAttributes.FATIGUE.get(), target);
        boolean coldChance = attackingEntity.level.random.nextDouble() < statusEffectValue(attackingEntity, ModAttributes.COLD.get(), target);
        boolean paraChance = attackingEntity.level.random.nextDouble() < statusEffectValue(attackingEntity, ModAttributes.PARA.get(), target);
        boolean sealChance = attackingEntity.level.random.nextDouble() < statusEffectValue(attackingEntity, ModAttributes.SEAL.get(), target);
        boolean dizzyChance = attackingEntity.level.random.nextDouble() < statusEffectValue(attackingEntity, ModAttributes.DIZZY.get(), target);
        double stunAmount = statusEffectValue(attackingEntity, ModAttributes.STUN.get(), target);
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
        if (stunAmount > 0.1 && attackingEntity.level.random.nextDouble() < stunAmount) {
            target.addEffect(new MobEffectInstance(ModEffects.STUNNED.get(), Mth.floor(Math.min(1, stunAmount) * 50), 0, true, false));
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
            double b = (color & 0xFF) / 255.0;
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

    public static void applyTempAttributeMult(LivingEntity entity, Attribute att, double val) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null && inst.getModifier(TEMP_ATTRIBUTE_MOD_MULT) == null)
            inst.addTransientModifier(new AttributeModifier(TEMP_ATTRIBUTE_MOD_MULT, "temp_mod_mult", (val - 1), AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public static void removeTempAttribute(LivingEntity entity, Attribute att) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null) {
            inst.removeModifier(TEMP_ATTRIBUTE_MOD);
            inst.removeModifier(TEMP_ATTRIBUTE_MOD_MULT);
        }
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
        if (stack.is(RunecraftoryTags.SHORTSWORDS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.SHORTSWORD, 2);
        }
        if (stack.is(RunecraftoryTags.LONGSWORDS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.LONGSWORD, 4);
        }
        if (stack.is(RunecraftoryTags.SPEARS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.SPEAR, 3);
        }
        if (stack.is(RunecraftoryTags.AXES) || stack.is(RunecraftoryTags.HAMMERS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 5);
        }
        if (stack.is(RunecraftoryTags.DUALBLADES)) {
            LevelCalc.levelSkill(player, data, EnumSkills.DUAL, 2);
        }
        if (stack.is(RunecraftoryTags.FISTS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.FIST, 2);
        }
        //Tools
        if (stack.is(RunecraftoryTags.AXE_TOOLS) || stack.is(RunecraftoryTags.HAMMER_TOOLS)) {
            LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 1);
        }
        if (stack.is(RunecraftoryTags.HOES) || stack.is(RunecraftoryTags.WATERINGCANS) || stack.is(RunecraftoryTags.SICKLES)) {
            LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 1);
        }
    }

    public static float getAOE(LivingEntity entity, ItemStack held, float bonus) {
        if (held.getItem() instanceof IAOEWeapon weapon)
            return weapon.getFOV(entity, held) + bonus;
        return bonus;
    }

    public static int getSpellLevelFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemSpell)
            return ItemNBT.itemLevel(stack);
        return 1;
    }

    public static double getAbilityDamageBonus(ItemStack stack) {
        return getAbilityDamageBonus(getSpellLevelFromStack(stack), 1);
    }

    public static float getAbilityDamageBonus(int level, float origin) {
        return origin * (1 + (level - 1) * 0.025f);
    }

    public static Vec3 fromRelativeVector(Entity entity, Vec3 relative) {
        return fromRelativeVector(entity.getYRot(), relative);
    }

    public static Vec3 fromRelativeVector(float yRot, Vec3 relative) {
        Vec3 vec3 = relative.normalize();
        float f = Mth.sin(yRot * Mth.DEG_TO_RAD);
        float g = Mth.cos(yRot * Mth.DEG_TO_RAD);
        return new Vec3(vec3.x * g - vec3.z * f, vec3.y, vec3.z * g + vec3.x * f);
    }

    public static boolean canPerform(LivingEntity entity, EnumSkills skill, int requiredLvl) {
        if (!(entity instanceof Player player))
            return false;
        return player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(skill).getLevel() >= requiredLvl).orElse(false);
    }

    public static void attack(LivingEntity entity, ItemStack stack) {
        if (entity instanceof Player player) {
            if (stack.getItem() instanceof IAOEWeapon weapon)
                AOEWeaponHandler.onAOEWeaponSwing(player, stack, weapon);
        } else if (entity instanceof EntityNPCBase npc) {
            npc.npcAttack(npc::doHurtTarget);
        }
    }

    public static class EntityAttack {

        private final LivingEntity attacker;
        private Predicate<LivingEntity> targetPred;
        private Map<Attribute, Double> bonusAttributes;
        private Map<Attribute, Double> bonusAttributesMultiplier;

        private Consumer<LivingEntity> onSuccess;

        private SoundEvent soundToPlay;

        private final BiFunction<LivingEntity, Predicate<LivingEntity>, List<LivingEntity>> targets;

        protected EntityAttack(LivingEntity attacker, BiFunction<LivingEntity, Predicate<LivingEntity>, List<LivingEntity>> targets) {
            this.attacker = attacker;
            this.targets = targets;
        }

        public static EntityAttack create(LivingEntity attacker, BiFunction<LivingEntity, Predicate<LivingEntity>, List<LivingEntity>> targets) {
            return new EntityAttack(attacker, targets);
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, List<LivingEntity>> circleTargets(Vec3 dir, float aoe, float rangeBonus) {
            return (attacker, predicate) -> {
                float reach = (float) attacker.getAttributeValue(ModAttributes.ATTACK_RANGE.get()) + rangeBonus;
                CircleSector circ = new CircleSector(attacker.position().add(0, attacker.getBbHeight() * 0.6, 0), dir, reach, Mth.abs(aoe * 0.5f), attacker);
                return attacker.level.getEntities(EntityTypeTest.forClass(LivingEntity.class), attacker.getBoundingBox().inflate(reach + 1),
                        t -> t != attacker && (predicate == null || predicate.test(t)) && !t.isAlliedTo(attacker) && t.isPickable()
                                && (t.getBoundingBox().minY <= attacker.getBoundingBox().maxY || t.getBoundingBox().maxY >= attacker.getBoundingBox().minY)
                                && circ.intersects(t.level, t.getBoundingBox().inflate(0.15, attacker.getBbHeight() * 1.5, 0.15)));
            };
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, List<LivingEntity>> circleTargets(float minYRot, float maxYRot, float rangeBonus) {
            return (attacker, predicate) -> {
                float rot = Mth.wrapDegrees(maxYRot - minYRot);
                Vec3 dir = Vec3.directionFromRotation(0, Mth.wrapDegrees(minYRot + rot * 0.5f));
                float reach = (float) attacker.getAttributeValue(ModAttributes.ATTACK_RANGE.get()) + rangeBonus;
                CircleSector circ = new CircleSector(attacker.position().add(0, attacker.getBbHeight() * 0.6, 0), dir, reach, Mth.abs(rot * 0.5f), attacker);
                return attacker.level.getEntities(EntityTypeTest.forClass(LivingEntity.class), attacker.getBoundingBox().inflate(reach + 1),
                        t -> t != attacker && (predicate == null || predicate.test(t)) && !t.isAlliedTo(attacker) && t.isPickable()
                                && (t.getBoundingBox().minY <= attacker.getBoundingBox().maxY || t.getBoundingBox().maxY >= attacker.getBoundingBox().minY)
                                && circ.intersects(t.level, t.getBoundingBox().inflate(0.15, attacker.getBbHeight() * 1.5, 0.15)));
            };
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, List<LivingEntity>> circleTargetsFixedRange(float minYRot, float maxYRot, float reach) {
            return (attacker, predicate) -> {
                float rot = Mth.wrapDegrees(maxYRot - minYRot);
                Vec3 dir = Vec3.directionFromRotation(0, Mth.wrapDegrees(minYRot + rot * 0.5f));
                CircleSector circ = new CircleSector(attacker.position().add(0, attacker.getBbHeight() * 0.6, 0), dir, reach, Mth.abs(rot * 0.5f), attacker);
                return attacker.level.getEntities(EntityTypeTest.forClass(LivingEntity.class), attacker.getBoundingBox().inflate(reach + 1),
                        t -> t != attacker && (predicate == null || predicate.test(t)) && !t.isAlliedTo(attacker) && t.isPickable()
                                && (t.getBoundingBox().minY <= attacker.getBoundingBox().maxY || t.getBoundingBox().maxY >= attacker.getBoundingBox().minY)
                                && circ.intersects(t.level, t.getBoundingBox().inflate(0.15, attacker.getBbHeight() * 1.5, 0.15)));
            };
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, List<LivingEntity>> aabbTargets(AABB aabb) {
            return (attacker, predicate) -> attacker.level.getEntities(EntityTypeTest.forClass(LivingEntity.class), aabb,
                    t -> t != attacker && (predicate == null || predicate.test(t)) && !t.isAlliedTo(attacker) && t.isPickable());
        }

        public EntityAttack withTargetPredicate(Predicate<LivingEntity> targetPred) {
            this.targetPred = targetPred;
            return this;
        }

        public EntityAttack withBonusAttributes(Map<Attribute, Double> bonusAttributes) {
            this.bonusAttributes = bonusAttributes;
            return this;
        }

        public EntityAttack withBonusAttributesMultiplier(Map<Attribute, Double> multiplier) {
            this.bonusAttributesMultiplier = multiplier;
            return this;
        }

        public EntityAttack doOnSuccess(Consumer<LivingEntity> onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        public EntityAttack withAttackSound(SoundEvent sound) {
            this.soundToPlay = sound;
            return this;
        }

        public List<LivingEntity> executeAttack() {
            if (this.attacker.level.isClientSide)
                return List.of();
            List<LivingEntity> list = this.targets.apply(this.attacker, this.targetPred);
            if (this.bonusAttributes != null) {
                this.bonusAttributes.forEach((att, val) -> applyTempAttribute(this.attacker, att, val));
            }
            if (this.bonusAttributesMultiplier != null) {
                this.bonusAttributesMultiplier.forEach((att, val) -> applyTempAttributeMult(this.attacker, att, val));
            }
            for (LivingEntity livingEntity : list) {
                boolean flag = false;
                if (this.attacker instanceof Player player)
                    flag = CombatUtils.playerAttackWithItem(player, livingEntity, false, false);
                else if (this.attacker instanceof Mob mob)
                    flag = mob.doHurtTarget(livingEntity);
                if (flag) {
                    if (this.onSuccess != null)
                        this.onSuccess.accept(livingEntity);
                    if (this.soundToPlay != null)
                        this.attacker.level.playSound(null, this.attacker.getX(), this.attacker.getY(), this.attacker.getZ(),
                                this.soundToPlay, this.attacker.getSoundSource(), 1.0f, 1.0f);
                }
            }
            if (this.bonusAttributes != null) {
                this.bonusAttributes.forEach((att, val) -> removeTempAttribute(this.attacker, att));
            }
            if (this.bonusAttributesMultiplier != null) {
                this.bonusAttributesMultiplier.forEach((att, val) -> removeTempAttribute(this.attacker, att));
            }
            return list;
        }
    }
}