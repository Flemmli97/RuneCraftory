package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.entities.utils.ElementalAttackMob;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import io.github.flemmli97.runecraftory.common.entities.utils.TargetableOpponent;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.mixin.LivingEntityAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.HitResultUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.FastColor;
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
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CombatUtils {

    private static final ResourceLocation TEMP_ATTRIBUTE_MOD = RuneCraftory.modRes("combat_temp_mod");
    private static final ResourceLocation TEMP_ATTRIBUTE_MOD_MULT = RuneCraftory.modRes("combat_temp_mod_multiply");

    /**
     * For damage calculation target should be null. The damage reduction gets done at the target.
     */
    public static double getAttributeValue(Entity entity, Holder<Attribute> att) {
        if (!(entity instanceof LivingEntity attacker))
            return 0;
        double increase = 0;
        if (attacker.getAttribute(att) != null) {
            increase += attacker.getAttributeValue(att);
        }
        int inc = (int) increase;
        double restRound = Math.round((increase - inc) * 2) / 2d;
        return inc + restRound;
    }

    public static Holder<Attribute> opposing(Holder<Attribute> att) {
        if (att.is(RuneCraftoryAttributes.PARALYSIS.getID()))
            return RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.POISON.getID()))
            return RuneCraftoryAttributes.POISON_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.SEAL.getID()))
            return RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.SLEEP.getID()))
            return RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.FATIGUE.getID()))
            return RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.COLD.getID()))
            return RuneCraftoryAttributes.COLD_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.DIZZY.getID()))
            return RuneCraftoryAttributes.DIZZY_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.CRITICAL.getID()))
            return RuneCraftoryAttributes.CRITICAL_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.STUN.getID()))
            return RuneCraftoryAttributes.STUN_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.FAINT.getID()))
            return RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder();
        if (att.is(RuneCraftoryAttributes.DRAIN.getID()))
            return RuneCraftoryAttributes.DRAIN_RESISTANCE.asHolder();
        return null;
    }

    public static Skills matchingSkill(Holder<Attribute> att) {
        if (att.is(RuneCraftoryAttributes.PARALYSIS.getID()))
            return Skills.RES_PARA;
        if (att.is(RuneCraftoryAttributes.POISON.getID()))
            return Skills.RES_POISON;
        if (att.is(RuneCraftoryAttributes.SEAL.getID()))
            return Skills.RES_SEAL;
        if (att.is(RuneCraftoryAttributes.SLEEP.getID()))
            return Skills.RES_SLEEP;
        if (att.is(RuneCraftoryAttributes.FATIGUE.getID()))
            return Skills.RES_FATIGUE;
        if (att.is(RuneCraftoryAttributes.COLD.getID()))
            return Skills.RES_COLD;
        return null;
    }

    public static double statusEffectValue(LivingEntity entity, Holder<Attribute> att, Entity target) {
        double value = getAttributeValue(entity, att) * 0.01;
        Holder<Attribute> opposing = opposing(att);
        double res = target instanceof LivingEntity livingTarget && opposing != null ? getAttributeValue(livingTarget, opposing) : 0;
        if (target instanceof Player player) {
            Skills matchingSkill = matchingSkill(att);
            if (matchingSkill != null)
                res += Platform.INSTANCE.getPlayerData(player).getSkillLevel(matchingSkill).getLevel() * 0.005;
        }
        res *= 0.01;
        return value * (1 - res);
    }

    public static float reduceDamageFromStats(LivingEntity entity, DamageSource source, float amount) {
        if (ArmorEffect.hasArmorEffect(entity, RuneCraftoryArmorEffects.SHIELD_RING.asHolder()) && entity.getRandom().nextFloat() < 0.1)
            return 1;
        float reduce = 0;
        boolean ignoreDefence = switch (GeneralConfig.defenceSystem) {
            case NO_DEFENCE -> true;
            case VANILLA_IGNORE -> !(source instanceof DynamicDamage);
            case IGNORE_VANILLA_MOBS -> !(source instanceof DynamicDamage) && source.getEntity() instanceof Mob;
            case IGNORE_VANILLA_PLAYER_ATT ->
                    !(source instanceof DynamicDamage) && source.getEntity() instanceof Player;
            case IGNORE_VANILLA_PLAYER_HURT -> !(source instanceof DynamicDamage) && entity instanceof Player;
            case IGNORE_VANILLA_PLAYER ->
                    !(source instanceof DynamicDamage) && (entity instanceof Player || source.getEntity() instanceof Player);
        };
        if (!ignoreDefence) {
            if (source.is(RunecraftoryTags.DamageTypes.IS_MAGIC)) {
                if (!source.is(RunecraftoryTags.DamageTypes.BYPASS_MAGIC))
                    reduce = (float) getAttributeValue(entity, RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder());
            } else if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                reduce = (float) getAttributeValue(entity, RuneCraftoryAttributes.DEFENCE.asHolder());
            }
        }
        float dmg = amount - reduce;
        if (reduce > amount * 0.8)
            dmg = (float) Math.max(0.05 * amount, amount * 0.2 * Math.pow(0.997, reduce - amount * 0.8));
        if (source instanceof DynamicDamage custom && GeneralConfig.randomDamage && !custom.fixedDamage()) {
            dmg += entity.level().random.nextGaussian() * dmg / 10.0;
        }
        return elementalReduction(entity, source, dmg);
    }

    public static float elementalReduction(LivingEntity entity, DamageSource source, float amount) {
        if (source instanceof DynamicDamage && ((DynamicDamage) source).getElement() != ItemElement.NONE) {
            ItemElement element = ((DynamicDamage) source).getElement();
            double percent = 0;
            switch (element) {
                case DARK:
                    percent = getAttributeValue(entity, RuneCraftoryAttributes.DARK_RESISTANCE.asHolder());
                    break;
                case EARTH:
                    percent = getAttributeValue(entity, RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder());
                    break;
                case FIRE:
                    percent = getAttributeValue(entity, RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder());
                    break;
                case LIGHT:
                    percent = getAttributeValue(entity, RuneCraftoryAttributes.LIGHT_RESISTANCE.asHolder());
                    break;
                case LOVE:
                    percent = getAttributeValue(entity, RuneCraftoryAttributes.LOVE_RESISTANCE.asHolder());
                    break;
                case WATER:
                    percent = getAttributeValue(entity, RuneCraftoryAttributes.WATER_RESISTANCE.asHolder());
                    break;
                case WIND:
                    percent = getAttributeValue(entity, RuneCraftoryAttributes.WIND_RESISTANCE.asHolder());
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
        if (!entity.getType().is(RunecraftoryTags.EntityTypes.BOSSES))
            applyTempAttribute(entity, Attributes.KNOCKBACK_RESISTANCE, -entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        entity.knockback(strength, x, z);
        if (!entity.getType().is(RunecraftoryTags.EntityTypes.BOSSES))
            removeTempAttribute(entity, Attributes.KNOCKBACK_RESISTANCE);
    }

    public static void knockBack(LivingEntity entity, DynamicDamage source) {
        if (source.getKnockBackType() == DynamicDamage.KnockBackType.NONE)
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
        if (source.getKnockBackType() == DynamicDamage.KnockBackType.VANILLA) {
            entity.knockback(strength, xRatio, zRatio);
        } else {
            Vec3 mot = entity.getDeltaMovement();
            double y = mot.y;
            entity.hasImpulse = true;
            if (xRatio != 0.0 || zRatio != 0.0) {
                float f = (float) Math.sqrt(xRatio * xRatio + zRatio * zRatio);
                mot = mot.scale(0.5).add(xRatio / f * strength, 0, zRatio / f * strength);
            }
            if (source.getKnockBackType() != DynamicDamage.KnockBackType.UP) {
                if (entity.onGround()) {
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
    public static boolean attackWithItem(Player player, Entity target, boolean resetCooldown, boolean levelSkill) {
        return CombatUtils.attackWithItem(player, target, player.getMainHandItem(), 1, resetCooldown, levelSkill);
    }

    public static boolean attackWithItem(Player player, Entity target, ItemStack stack, float damageModifier, boolean resetCooldown, boolean levelSkill) {
        if (!(player.level() instanceof ServerLevel serverLevel))
            return false;
        if (target.isAttackable() && !target.skipAttackInteraction(player) && player.getCooldowns().getCooldownPercent(stack.getItem(), 0.0f) <= 0) {
            float damage = (float) (getAttributeValue(player, Attributes.ATTACK_DAMAGE) * damageModifier);
            if (damage > 0) {
                if (target.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && target instanceof Projectile projectile) {
                    if (projectile.deflect(ProjectileDeflection.AIM_DEFLECT, player, player, true)) {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource());
                        return true;
                    }
                }
                if (resetCooldown) {
                    player.getCooldowns().addCooldown(stack.getItem(), Mth.ceil(20 * EntityUtils.attackSpeedModifier(player)));
                }
                boolean faint = player.level().random.nextDouble() < statusEffectValue(player, RuneCraftoryAttributes.FAINT.asHolder(), target);
                boolean critical = player.level().random.nextDouble() < statusEffectValue(player, RuneCraftoryAttributes.CRITICAL.asHolder(), target);
                DynamicDamage.DamageCategory damageCategory = DynamicDamage.DamageCategory.NORMAL;
                if (faint)
                    damageCategory = DynamicDamage.DamageCategory.FAINT;
                else if (critical)
                    damageCategory = DynamicDamage.DamageCategory.IGNOREDEF;
                if (stack.has(RuneCraftoryDataComponentTypes.SCRAP_METAL_PLUS.get())) {
                    damageCategory = DynamicDamage.DamageCategory.FIXED;
                    damage = 1;
                }
                DynamicDamage.Builder source = new DynamicDamage.Builder(player).element(ItemComponentUtils.getElement(stack))
                        .damageType(damageCategory).hurtResistant(0);
                DynamicDamage tempBuild = source.get(player.registryAccess());

                double enchantBonus = EnchantmentHelper.modifyDamage(serverLevel, stack, player, tempBuild, damage) - damage;
                damage += enchantBonus;

                float knockback = ((LivingEntityAccessor) player).getEntityKnockback(target, tempBuild) + (player.isSprinting() ? 1.0F : 0.0F);
                source.knockAmount(knockback);
                Vec3 targetMot = target.getDeltaMovement();
                if (damage(player, target, source, damage, stack, false, false)) {
                    //Level skill on successful attack
                    if (levelSkill && player instanceof ServerPlayer serverPlayer)
                        hitEntityWithItemPlayer(serverPlayer, stack);
                    if (knockback > 0) {
                        player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                        player.setSprinting(false);
                    }
                    if (target instanceof ServerPlayer serverPlayer && target.hurtMarked) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                        target.hurtMarked = false;
                        target.setDeltaMovement(targetMot);
                    }
                    if (critical) {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0f, 1.0f);
                        player.crit(target);
                        player.magicCrit(target);
                    } else {
                        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0f, 1.0f);
                    }
                } else {
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0f, 1.0f);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target) {
        ItemStack stack = attacker.getMainHandItem();
        DynamicDamage.Builder source = new DynamicDamage.Builder(attacker).hurtResistant(5).element(ItemComponentUtils.getElement(stack));
        return mobAttack(attacker, target, source);
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, DynamicDamage.Builder source) {
        ItemStack stack = attacker.getMainHandItem();
        double damage = getAttributeValue(attacker, Attributes.ATTACK_DAMAGE);
        if (attacker.level() instanceof ServerLevel serverLevel)
            RuneCraftorySpells.STAFF_CAST.get().use(serverLevel, attacker, stack);
        if (stack.has(RuneCraftoryDataComponentTypes.SCRAP_METAL_PLUS.get())) {
            source.damageType(DynamicDamage.DamageCategory.FIXED);
            damage = 1;
        }
        return mobAttack(attacker, target, source, damage);
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, DynamicDamage.Builder source, double damage) {
        return mobAttack(attacker, target, source, damage, null);
    }

    public static boolean mobAttack(LivingEntity attacker, Entity target, DynamicDamage.Builder source, double damage, @Nullable ItemStack stack) {
        if (target.level().getDifficulty() == Difficulty.PEACEFUL && target instanceof Player)
            return false;
        if (damage > 0) {
            if (attacker instanceof ElementalAttackMob mob) {
                ItemElement element = mob.getAttackElement();
                if (element != null)
                    source.element(element);
            }
            return damageWithFaintAndCrit(attacker, target, source, damage, stack);
        }
        return false;
    }

    public static boolean damageWithFaintAndCrit(@Nullable Entity attacker, Entity target, DynamicDamage.Builder builder, double damage, @Nullable ItemStack stack) {
        return damage(attacker, target, builder, damage, stack, true, true);
    }

    public static boolean damage(@Nullable Entity attacker, Entity target, DynamicDamage.Builder builder, double damage, @Nullable ItemStack stack, boolean allowCrit, boolean allowFaint) {
        // Setup some more things
        if (attacker instanceof LivingEntity living) {
            builder.getAttributesChanges().forEach((att, val) -> CombatUtils.applyTempAttribute(living, att, val));
            if (allowFaint && living.level().random.nextDouble() < statusEffectValue(living, RuneCraftoryAttributes.FAINT.asHolder(), target)) {
                builder.damageType(DynamicDamage.DamageCategory.FAINT);
            } else if (allowCrit && living.level().random.nextDouble() < statusEffectValue(living, RuneCraftoryAttributes.CRITICAL.asHolder(), target)) {
                switch (builder.getDamageType()) {
                    case MAGIC -> builder.damageType(DynamicDamage.DamageCategory.IGNOREMAGICDEF);
                    case NORMAL -> builder.damageType(DynamicDamage.DamageCategory.IGNOREDEF);
                }
            }
            if (builder.calculateKnockback()) {
                float knockback = ((LivingEntityAccessor) living).getEntityKnockback(target, builder.get(living.registryAccess()))
                        + (living.isSprinting() ? 1.0F : 0.0F);
                builder.knockAmount(knockback);
            }
        }

        DynamicDamage source = builder.get(target.registryAccess());
        float dmg = (float) damage;
        if (source.criticalDamage())
            dmg = Float.MAX_VALUE;
        else if (!source.fixedDamage())
            dmg = modifyDmgElement(source.getElement(), target, dmg);
        boolean success = source.hurtEntity(target, dmg);
        if (success) {
            spawnElementalParticle(target, source.getElement());
            if (attacker instanceof LivingEntity livingAttacker) {
                livingAttacker.setLastHurtMob(target);
            }
            if (target instanceof LivingEntity livingTarget) {
                knockBack(livingTarget, source);
                boolean handleStack = attacker instanceof Player && stack != null;
                if (handleStack) {
                    handleStack = stack.hurtEnemy(livingTarget, (Player) attacker);
                }
                if (attacker instanceof LivingEntity livingAttacker && livingAttacker.level() instanceof ServerLevel serverLevel) {
                    applyStatusEffects(livingAttacker, livingTarget);
                    EnchantmentHelper.doPostAttackEffects(serverLevel, target, source);
                }
                if (handleStack && !stack.isEmpty())
                    stack.postHurtEnemy(livingTarget, (Player) attacker);
                if (stack != null && attacker instanceof Player player) {
                    ItemStack beforeHitCopy = stack.copy();
                    if (stack.isEmpty()) {
                        Platform.INSTANCE.destroyItem(player, beforeHitCopy, InteractionHand.MAIN_HAND);
                        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
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

    public static float modifyDmgElement(ItemElement element, Entity target, float dmg) {
        if (!(target instanceof IBaseMob) && !(target instanceof Player)) {
            if (element == ItemElement.WATER && target instanceof LivingEntity living && (living.fireImmune() || living.isSensitiveToWater()))
                dmg *= 1.1;
        }
        return dmg;
    }

    public static void elementalEffects(Entity attacker, ItemElement element, Entity target) {
        if (!(target instanceof IBaseMob) && !(target instanceof Player)) {
            switch (element) {
                case FIRE -> target.igniteForSeconds(3);
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
        boolean poisonChance = attackingEntity.level().random.nextDouble() < statusEffectValue(attackingEntity, RuneCraftoryAttributes.POISON.asHolder(), target);
        boolean sleepChance = attackingEntity.level().random.nextDouble() < statusEffectValue(attackingEntity, RuneCraftoryAttributes.SLEEP.asHolder(), target);
        boolean fatigueChance = attackingEntity.level().random.nextDouble() < statusEffectValue(attackingEntity, RuneCraftoryAttributes.FATIGUE.asHolder(), target);
        boolean coldChance = attackingEntity.level().random.nextDouble() < statusEffectValue(attackingEntity, RuneCraftoryAttributes.COLD.asHolder(), target);
        boolean paraChance = attackingEntity.level().random.nextDouble() < statusEffectValue(attackingEntity, RuneCraftoryAttributes.PARALYSIS.asHolder(), target);
        boolean sealChance = attackingEntity.level().random.nextDouble() < statusEffectValue(attackingEntity, RuneCraftoryAttributes.SEAL.asHolder(), target);
        boolean dizzyChance = attackingEntity.level().random.nextDouble() < statusEffectValue(attackingEntity, RuneCraftoryAttributes.DIZZY.asHolder(), target);
        double stunAmount = statusEffectValue(attackingEntity, RuneCraftoryAttributes.STUN.asHolder(), target);
        if (poisonChance) {
            EntityUtils.applyPermanentEffect(target, RuneCraftoryEffects.POISON.asHolder(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_POISON, 5);
            if (target instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_POISON, 15);
        }
        if (fatigueChance) {
            EntityUtils.applyPermanentEffect(target, RuneCraftoryEffects.FATIGUE.asHolder(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_FATIGUE, 5);
            if (target instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_FATIGUE, 15);
        }
        if (coldChance) {
            EntityUtils.applyPermanentEffect(target, RuneCraftoryEffects.COLD.asHolder(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_COLD, 5);
            if (target instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_COLD, 15);
        }
        if (paraChance) {
            EntityUtils.applyPermanentEffect(target, RuneCraftoryEffects.PARALYSIS.asHolder(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_PARA, 5);
            if (target instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_PARA, 15);
        }
        if (sealChance) {
            EntityUtils.applyPermanentEffect(target, RuneCraftoryEffects.SEAL.asHolder(), 0);
            if (attackingEntity instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_SEAL, 5);
            if (target instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_SEAL, 15);
        }
        if (dizzyChance) {
            target.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 1, true, false));
        }
        if (stunAmount > 0.1 && attackingEntity.level().random.nextDouble() < stunAmount) {
            target.addEffect(new MobEffectInstance(RuneCraftoryEffects.STUNNED.asHolder(), Mth.floor(Math.min(1, stunAmount) * 60), 0, true, false));
        }
        if (sleepChance) {
            target.addEffect(new MobEffectInstance(RuneCraftoryEffects.SLEEP.asHolder(), 80, 0, true, false));
            if (attackingEntity instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_SLEEP, 5);
            if (target instanceof ServerPlayer player)
                LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.RES_SLEEP, 15);
        }
    }

    public static void spawnElementalParticle(Entity target, ItemElement element) {
        if (target.level() instanceof ServerLevel serverLevel) {
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
            int r = (color >> 16 & 0xFF);
            int g = (color >> 8 & 0xFF);
            int b = (color & 0xFF);
            Random rand = new Random();
            for (int i = 0; i < 7; ++i) {
                serverLevel.sendParticles(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, FastColor.ARGB32.color(r, g, b)),
                        target.getX() + (rand.nextDouble() - 0.5) * target.getBbWidth(),
                        target.getY() + 0.3 + rand.nextDouble() * target.getBbHeight(),
                        target.getZ() + (rand.nextDouble() - 0.5) * target.getBbWidth(), 0, 0, 0, 0, 1);
            }
        }
    }

    public static void applyTempAttribute(LivingEntity entity, Holder<Attribute> att, double val) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null && inst.getModifier(TEMP_ATTRIBUTE_MOD) == null)
            inst.addTransientModifier(new AttributeModifier(TEMP_ATTRIBUTE_MOD, val, AttributeModifier.Operation.ADD_VALUE));
    }

    public static void applyTempAttributeMult(LivingEntity entity, Holder<Attribute> att, double val) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null && inst.getModifier(TEMP_ATTRIBUTE_MOD_MULT) == null)
            inst.addTransientModifier(new AttributeModifier(TEMP_ATTRIBUTE_MOD_MULT, (val - 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    public static void removeTempAttribute(LivingEntity entity, Holder<Attribute> att) {
        AttributeInstance inst = entity.getAttribute(att);
        if (inst != null) {
            inst.removeModifier(TEMP_ATTRIBUTE_MOD);
            inst.removeModifier(TEMP_ATTRIBUTE_MOD_MULT);
        }
    }

    public static void hitEntityWithItemPlayer(ServerPlayer player, ItemStack stack) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        //Weapons
        if (stack.getItem() instanceof ItemStaffBase) {
            switch (ItemComponentUtils.getElement(stack)) {
                case WATER -> LevelCalc.levelSkill(data, Skills.WATER, 3);
                case EARTH -> LevelCalc.levelSkill(data, Skills.EARTH, 3);
                case WIND -> LevelCalc.levelSkill(data, Skills.WIND, 3);
                case FIRE -> LevelCalc.levelSkill(data, Skills.FIRE, 3);
                case LIGHT -> LevelCalc.levelSkill(data, Skills.LIGHT, 3);
                case DARK -> LevelCalc.levelSkill(data, Skills.DARK, 3);
                case LOVE -> LevelCalc.levelSkill(data, Skills.LOVE, 3);
            }
            return;
        }
        if (stack.is(RunecraftoryTags.Items.SHORTSWORDS)) {
            LevelCalc.levelSkill(data, Skills.SHORTSWORD, 2);
        }
        if (stack.is(RunecraftoryTags.Items.LONGSWORDS)) {
            LevelCalc.levelSkill(data, Skills.LONGSWORD, 4);
        }
        if (stack.is(RunecraftoryTags.Items.SPEARS)) {
            LevelCalc.levelSkill(data, Skills.SPEAR, 3);
        }
        if (stack.is(RunecraftoryTags.Items.AXES) || stack.is(RunecraftoryTags.Items.HAMMERS)) {
            LevelCalc.levelSkill(data, Skills.HAMMERAXE, 5);
        }
        if (stack.is(RunecraftoryTags.Items.DUALBLADES)) {
            LevelCalc.levelSkill(data, Skills.DUAL, 2);
        }
        if (stack.is(RunecraftoryTags.Items.FISTS)) {
            LevelCalc.levelSkill(data, Skills.FIST, 2);
        }
        //Tools
        if (stack.is(RunecraftoryTags.Items.AXE_TOOLS) || stack.is(RunecraftoryTags.Items.HAMMER_TOOLS)) {
            LevelCalc.levelSkill(data, Skills.HAMMERAXE, 1);
        }
        if (stack.is(RunecraftoryTags.Items.HOES) || stack.is(RunecraftoryTags.Items.WATERINGCANS) || stack.is(RunecraftoryTags.Items.SICKLES)) {
            LevelCalc.levelSkill(data, Skills.FARMING, 1);
        }
    }

    public static double getRange(LivingEntity entity, double bonus) {
        return (EntityUtils.tryGetAttribute(entity, RuneCraftoryAttributes.ATTACK_RANGE.asHolder()) + bonus)
                * entity.getScale();
    }

    public static double getWidth(LivingEntity entity, double bonus) {
        return (EntityUtils.tryGetAttribute(entity, RuneCraftoryAttributes.ATTACK_WIDTH.asHolder()) + bonus)
                * entity.getScale();
    }

    public static int getSpellLevelFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemSpell)
            return ItemComponentUtils.itemLevel(stack);
        return 1;
    }

    public static double getAbilityDamageBonus(ItemStack stack, Supplier<? extends Spell> source) {
        return getAbilityDamageBonus(getSpellLevelFromStack(stack), source.get().properties().baseDamageMultiplier());
    }

    public static float getAbilityDamageBonus(int level, Spell source) {
        return getAbilityDamageBonus(level, source.properties().baseDamageMultiplier());
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

    public static boolean canPerform(LivingEntity entity, Skills skill, int requiredLvl) {
        if (!(entity instanceof Player player))
            return false;
        return player.isCreative() || Platform.INSTANCE.getPlayerData(player).getSkillLevel(skill).getLevel() >= requiredLvl;
    }

    public static class EntityAttack {

        private final LivingEntity attacker;
        private Predicate<LivingEntity> targetPred;
        private final Map<Holder<Attribute>, Double> bonusAttributes = new HashMap<>();
        private final Map<Holder<Attribute>, Double> bonusAttributesMultiplier = new HashMap<>();

        private Consumer<LivingEntity> onSuccess;

        private SoundEvent soundToPlay;

        private final BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> targets;

        protected EntityAttack(LivingEntity attacker, BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> targets) {
            this.attacker = attacker;
            this.targets = targets;
        }

        public static EntityAttack create(LivingEntity attacker, BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> targets) {
            return new EntityAttack(attacker, targets);
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> circleTargets(float startRot, float endRot, float rangeBonus) {
            return circleTargets(startRot, endRot, null, rangeBonus);
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> circleTargets(float startRot, float endRot, FloatMap xRot, float rangeBonus) {
            return (attacker, predicate) -> {
                double reach = getRange(attacker, rangeBonus);
                double incHalf = Math.asin(0.5 / reach) * Mth.RAD_TO_DEG;
                float minYRot = Math.min(startRot, endRot);
                float maxYRot = Math.max(startRot, endRot);
                AABB aabb = new AABB(-0.5, -0.02, 0, 0.5, attacker.getBbHeight() + 0.02, reach);
                int rotationSteps = (int) ((maxYRot - minYRot) / (incHalf * 2)) + 2;
                float inc = (maxYRot - minYRot) / rotationSteps;
                Set<LivingEntity> entities = new HashSet<>();
                for (int steps = 0; steps <= rotationSteps; steps++) {
                    float yRot = minYRot + inc * steps;
                    OrientedBoundingBox obb = new OrientedBoundingBox(aabb, yRot, xRot == null ? 0 : xRot.get((float) steps / rotationSteps), attacker.position());
                    entities.addAll(HitResultUtils.getEntities(attacker, obb, false, EntityTypeTest.forClass(LivingEntity.class), predicate));
                    S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, attacker);
                }
                return entities;
            };
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> circleTargetsFixedRange(float startRot, float endRot, float reach) {
            return (attacker, predicate) -> {
                double incHalf = Math.asin(0.5 / reach) * Mth.RAD_TO_DEG;
                float minYRot = Math.min(startRot, endRot);
                float maxYRot = Math.max(startRot, endRot);
                AABB aabb = new AABB(-0.5, -0.02, 0, 0.5, attacker.getBbHeight() + 0.02, reach);
                int rotationSteps = (int) ((maxYRot - minYRot) / (incHalf * 2)) + 2;
                float inc = (maxYRot - minYRot) / rotationSteps;
                Set<LivingEntity> entities = new HashSet<>();
                for (int steps = 0; steps <= rotationSteps; steps++) {
                    float yRot = minYRot + inc * steps;
                    OrientedBoundingBox obb = new OrientedBoundingBox(aabb, yRot, 0, attacker.position());
                    entities.addAll(HitResultUtils.getEntities(attacker, obb, false, EntityTypeTest.forClass(LivingEntity.class), predicate));
                    S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, attacker);
                }
                return entities;
            };
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> aabbTargets(AABB aabb) {
            return aabbTargets(aabb, true);
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> aabbTargets(AABB aabb, boolean relative) {
            return (attacker, predicate) -> {
                OrientedBoundingBox obb = new OrientedBoundingBox(relative ? aabb.move(attacker.position().scale(-1)) : aabb, attacker.getYRot(), 0, attacker.position());
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, attacker);
                return HitResultUtils.getEntities(attacker, obb, true, EntityTypeTest.forClass(LivingEntity.class), predicate);
            };
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> obbTargets(float yRot, float xRot, double width, double range, boolean fixed) {
            return (attacker, predicate) -> {
                double reach = fixed ? range : getRange(attacker, range);
                AABB aabb = new AABB(-width * 0.5, -0.02, 0, width * 0.5, attacker.getBbHeight(), reach);
                OrientedBoundingBox obb = new OrientedBoundingBox(aabb, yRot, -xRot, attacker.position());
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, attacker);
                return HitResultUtils.getEntities(attacker, obb, false, EntityTypeTest.forClass(LivingEntity.class), predicate);
            };
        }

        public static BiFunction<LivingEntity, Predicate<LivingEntity>, Collection<LivingEntity>> obbTargets(OrientedBoundingBox obb) {
            return (attacker, predicate) -> {
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, attacker);
                return HitResultUtils.getEntities(attacker, obb, false, EntityTypeTest.forClass(LivingEntity.class), predicate);
            };
        }

        public EntityAttack withTargetPredicate(Predicate<LivingEntity> targetPred) {
            this.targetPred = targetPred;
            return this;
        }

        public EntityAttack withBonusAttributes(Holder<Attribute> att, double val) {
            this.bonusAttributes.put(att, val);
            return this;
        }

        public EntityAttack withBonusAttributesMultiplier(Holder<Attribute> att, double val) {
            this.bonusAttributesMultiplier.put(att, val);
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

        public Collection<LivingEntity> executeAttack() {
            if (this.attacker.level().isClientSide)
                return List.of();
            if (this.attacker instanceof TargetableOpponent pred)
                this.targetPred = this.targetPred == null ? pred.validTargetPredicate() :
                        pred.validTargetPredicate().and(this.targetPred);
            Collection<LivingEntity> list = this.targets.apply(this.attacker, this.targetPred);
            this.bonusAttributes.forEach((att, val) -> applyTempAttribute(this.attacker, att, val));
            this.bonusAttributesMultiplier.forEach((att, val) -> applyTempAttributeMult(this.attacker, att, val));
            LivingEntity target = this.attacker instanceof Mob mob ? mob.getTarget() : null;
            for (LivingEntity livingEntity : list) {
                boolean flag = false;
                if (target != livingEntity && this.attacker.getVehicle() == livingEntity)
                    continue;
                if (this.attacker instanceof Player player) {
                    flag = CombatUtils.attackWithItem(player, livingEntity, false, false);
                } else if (this.attacker instanceof Mob mob)
                    flag = mob.doHurtTarget(livingEntity);
                if (flag) {
                    if (this.onSuccess != null)
                        this.onSuccess.accept(livingEntity);
                    if (this.soundToPlay != null)
                        this.attacker.level().playSound(null, this.attacker.getX(), this.attacker.getY(), this.attacker.getZ(),
                                this.soundToPlay, this.attacker.getSoundSource(), 1.0f, 1.0f);
                }
            }
            this.bonusAttributes.forEach((att, val) -> removeTempAttribute(this.attacker, att));
            this.bonusAttributesMultiplier.forEach((att, val) -> removeTempAttribute(this.attacker, att));
            return list;
        }
    }

    public interface FloatMap {

        float get(float val);
    }
}