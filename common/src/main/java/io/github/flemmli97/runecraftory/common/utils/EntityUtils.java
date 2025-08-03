package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.TreasureChestEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.items.creative.TreasureChestSpawnegg;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.network.S2CUpdateAttributesWithAdditional;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class EntityUtils {

    public static double tryGetAttribute(LivingEntity entity, Holder<Attribute> attribute) {
        AttributeInstance inst = entity.getAttribute(attribute);
        if (inst == null)
            return attribute.value().getDefaultValue();
        return inst.getValue();
    }

    public static double attackSpeedModifier(LivingEntity entity) {
        return tryGetAttribute(entity, RuneCraftoryAttributes.ATTACK_SPEED.asHolder());
    }

    public static Rotation fromDirection(Direction direction) {
        return switch (direction) {
            case EAST -> Rotation.CLOCKWISE_90;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    public static void applyPermanentEffect(LivingEntity entity, Holder<MobEffect> effect, int amplifier) {
        if (!entity.hasEffect(effect)) {
            entity.addEffect(new MobEffectInstance(effect, -1, amplifier, true, false));
        }
    }

    public static boolean sealed(LivingEntity entity) {
        return entity.hasEffect(RuneCraftoryEffects.SEAL.asHolder());
    }

    public static boolean canMonsterTargetNPC(Entity e) {
        if (e instanceof NPCEntity npc && npc.getEntityToFollowUUID() != null)
            return true;
        return MobConfig.mobAttackNpc && e instanceof Npc;
    }

    public static void sendAttributesTo(LivingEntity entity, ServerPlayer player) {
        AttributeInstance att = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (att != null)
            entity.getAttributes().getAttributesToUpdate().add(att);
        if (entity == player) {
            LoaderNetwork.INSTANCE.sendToPlayer(new S2CUpdateAttributesWithAdditional(entity.getAttributes().getAttributesToUpdate()), player);
        } else {
            player.connection.send(new ClientboundUpdateAttributesPacket(entity.getId(), entity.getAttributes().getAttributesToUpdate()));
        }
    }

    public static void foodHealing(LivingEntity entity, float amount) {
        if (amount > 0)
            entity.heal(amount);
        else if (!entity.getType().is(RunecraftoryTags.EntityTypes.BOSS_MONSTERS))
            entity.setHealth(entity.getHealth() + amount);
    }

    public static float playerLuck(Player player) {
        return player.getLuck();
    }

    public static boolean isDisabled(LivingEntity entity) {
        return entity.hasEffect(RuneCraftoryEffects.SLEEP.asHolder()) || entity.hasEffect(RuneCraftoryEffects.STUNNED.asHolder());
    }

    @Nullable
    public static UUID tryGetOwner(LivingEntity entity) {
        if (entity instanceof OwnableEntity ownableEntity)
            return ownableEntity.getOwnerUUID();
        return null;
    }

    public static boolean canAttackOwned(LivingEntity entity, boolean allowUuidOnly, boolean otherwise, Predicate<LivingEntity> pred) {
        if (entity instanceof OwnableEntity ownableEntity) {
            if (entity.getUUID().equals(ownableEntity.getOwnerUUID()))
                return false;
            Entity owner = ownableEntity.getOwner();
            if (owner instanceof LivingEntity living)
                return pred.test(living);
            return allowUuidOnly && ownableEntity.getOwnerUUID() != null || otherwise;
        }
        return otherwise;
    }

    public static float tamingChance(BaseMonster monster, Player player, float itemMultiplier, int brushCount, int loveAttackCount) {
        if (itemMultiplier == 0 || GeneralConfig.tamingMultiplier == 0)
            return 0;
        int lvl = Platform.INSTANCE.getPlayerData(player).getPlayerLevel().getLevel() + 1;
        float lvlPenalty = Math.max(0, (monster.xpLevel().getLevel() - lvl) * 0.02f);
        float brushBonus = brushCount * 0.05f;
        float loveAttackBonus = loveAttackCount * 0.002f;
        float tamingLvlBonus = (Platform.INSTANCE.getPlayerData(player).getSkillLevel(Skills.TAMING).getLevel() - 1) * 0.005f;
        float tamingBonus = 1 + brushBonus + loveAttackBonus + tamingLvlBonus;
        return monster.tamingChance() * GeneralConfig.tamingMultiplier * tamingBonus - lvlPenalty;
    }

    public static EntityType<?> trySpawnTreasureChest(GateEntity spawner) {
        if (spawner.getRandom().nextFloat() < MobConfig.treasureChance) {
            if (spawner.getRandom().nextFloat() < MobConfig.mimicChance) {
                if (spawner.getRandom().nextFloat() < MobConfig.mimicStrongChance)
                    return RuneCraftoryEntities.GOBBLE_BOX.get();
                else
                    return RuneCraftoryEntities.MONSTER_BOX.get();
            } else
                return RuneCraftoryEntities.TREASURE_CHEST.get();
        }
        return null;
    }

    private static final List<WeightedChestTier> CHEST_TIERS = ImmutableList.of(
            new WeightedChestTier(TreasureChestSpawnegg.ChestTier.COMMON, 150, 0.4f, 550),
            new WeightedChestTier(TreasureChestSpawnegg.ChestTier.UNCOMMON, 40, 0.3f, 200),
            new WeightedChestTier(TreasureChestSpawnegg.ChestTier.RARE, 1, 0.3f, 60),
            new WeightedChestTier(TreasureChestSpawnegg.ChestTier.EPIC, 0, 0, 20) //0.05f
    );

    public static void tieredTreasureChest(GateEntity spawner, TreasureChestEntity chest) {
        int max = 0;
        for (WeightedChestTier tier : CHEST_TIERS) {
            max += tier.getModifiedWeight(spawner.xpLevel().getLevel());
        }

        int rand = spawner.getRandom().nextInt(max);
        for (WeightedChestTier tier : CHEST_TIERS) {
            if ((rand -= tier.getModifiedWeight(spawner.xpLevel().getLevel())) >= 0) continue;
            chest.setTier(tier.tier);
        }
    }

    public static int getRPFromVanillaFood(ItemStack stack) {
        FoodProperties prop = stack.get(DataComponents.FOOD);
        if (prop != null)
            return (int) (prop.nutrition() * 1.5
                    * (1 + 1.8 * prop.saturation()));
        return 0;
    }

    public static Vec3 horizontalLookAngle(Entity entity) {
        Vec3 look = entity.getLookAngle();
        return new Vec3(look.x(), 0, look.z()).normalize();
    }

    public static Vec3 getStraightProjectileTarget(Vec3 from, Entity target) {
        AABB aabb = target.getBoundingBox().inflate(target.getBbHeight() * 0.1);
        return getStraightProjectileTarget(from, target.position(), aabb.minY, aabb.maxY);
    }

    public static Vec3 getStraightProjectileTarget(Vec3 from, Vec3 target, double minY, double maxY) {
        return new Vec3(target.x(), Mth.clamp(from.y(), minY, maxY), target.z());
    }

    public static Vec3 getTargetDirection(Mob mob, EntityAnchorArgument.Anchor anchor) {
        return getTargetDirection(mob, anchor, false);
    }

    public static Vec3 getTargetDirection(Mob mob, EntityAnchorArgument.Anchor anchor, boolean horizontalOnly) {
        Vec3 pos = anchor.apply(mob);
        Vec3 dir;
        if (mob.getControllingPassenger() != null) {
            dir = mob.getControllingPassenger().getLookAngle();
        } else if (mob.getTarget() != null) {
            LivingEntity target = mob.getTarget();
            dir = anchor.apply(target).subtract(pos);
        } else {
            dir = mob.getLookAngle();
        }
        if (horizontalOnly) {
            dir = new Vec3(dir.x(), 0, dir.z());
        }
        return dir.normalize();
    }

    public static LivingEntity ownedProjectileTarget(Entity owner, int range) {
        if (owner instanceof Mob mob && mob.getTarget() != null)
            return mob.getTarget();
        else if (owner instanceof LivingEntity livingOwner) {
            Function<LivingEntity, Predicate<LivingEntity>> generator = ownerEntity -> ownerEntity instanceof BaseMonster monster ? monster.targetPred : e -> {
                if (ownerEntity instanceof Player)
                    return !(e instanceof Animal || e instanceof Npc || (e instanceof OwnableEntity ownable && ownerEntity.getUUID().equals(ownable.getOwnerUUID())));
                if (ownerEntity instanceof Mob mob) {
                    return e == mob.getTarget();
                }
                return false;
            };
            Predicate<LivingEntity> pred = owner.getControllingPassenger() instanceof LivingEntity controller ? generator.apply(controller) : generator.apply(livingOwner);
            return owner.level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat().ignoreLineOfSight()
                    .range(range).selector(pred), livingOwner, livingOwner.getX(), livingOwner.getY(), livingOwner.getZ(), new AABB(-10, -10, -10, 10, 10, 10)
                    .move(livingOwner.position()));
        }
        return null;
    }

    public static void playSoundForPlayer(ServerPlayer player, SoundEvent event, float volume, float pitch) {
        playSoundForPlayer(player, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(event), player.getSoundSource(), volume, pitch);
    }

    public static void playSoundForPlayer(ServerPlayer player, SoundEvent event, SoundSource source, float volume, float pitch) {
        playSoundForPlayer(player, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(event), source, volume, pitch);
    }

    public static void playSoundForPlayer(ServerPlayer player, Holder<SoundEvent> event, float volume, float pitch) {
        player.connection.send(new ClientboundSoundPacket(event, player.getSoundSource(), player.getX(), player.getY(), player.getZ(),
                volume, pitch, player.getRandom().nextLong()));
    }

    public static void playSoundForPlayer(ServerPlayer player, Holder<SoundEvent> event, SoundSource source, float volume, float pitch) {
        player.connection.send(new ClientboundSoundPacket(event, source, player.getX(), player.getY(), player.getZ(),
                volume, pitch, player.getRandom().nextLong()));
    }

    record WeightedChestTier(TreasureChestSpawnegg.ChestTier tier, int weight, float modifier, int max) {

        int getModifiedWeight(int mod) {
            return Math.min(this.max, (int) (this.weight + this.modifier * mod));
        }
    }
}
