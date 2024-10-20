package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.network.S2CUpdateAttributesWithAdditional;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
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

    public static Rotation fromDirection(Direction direction) {
        return switch (direction) {
            case EAST -> Rotation.CLOCKWISE_90;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
    }

    public static boolean isExhaust(LivingEntity entity) {
        return entity.hasEffect(ModEffects.FATIGUE.get());
    }

    public static void applyPermanentEffect(LivingEntity entity, MobEffect effect, int amplifier) {
        if (!entity.hasEffect(effect)) {
            entity.addEffect(new MobEffectInstance(effect, Integer.MAX_VALUE, amplifier));
        }
    }

    public static boolean paralysed(LivingEntity entity) {
        return entity.hasEffect(ModEffects.PARALYSIS.get());
    }

    public static boolean sealed(LivingEntity entity) {
        return entity.hasEffect(ModEffects.SEAL.get());
    }

    public static boolean canMonsterTargetNPC(Entity e) {
        if (e instanceof EntityNPCBase npc && npc.getEntityToFollowUUID() != null)
            return true;
        return MobConfig.mobAttackNpc && e instanceof Npc;
    }

    public static void sendAttributesTo(LivingEntity entity, ServerPlayer player) {
        AttributeInstance att = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (att != null)
            entity.getAttributes().getDirtyAttributes().add(att);
        if (entity == player) {
            Platform.INSTANCE.sendToClient(new S2CUpdateAttributesWithAdditional(entity.getAttributes().getDirtyAttributes()), player);
            entity.getAttributes().getDirtyAttributes().clear();
        } else {
            player.connection.send(new ClientboundUpdateAttributesPacket(entity.getId(), entity.getAttributes().getDirtyAttributes()));
            entity.getAttributes().getDirtyAttributes().clear();
        }
    }

    public static boolean shouldShowFarmlandView(LivingEntity entity) {
        ItemStack main = entity.getMainHandItem();
        ItemStack off = entity.getOffhandItem();
        return ItemNBT.canBeUsedAsMagnifyingGlass(main) || ItemNBT.canBeUsedAsMagnifyingGlass(off);
    }

    public static void foodHealing(LivingEntity entity, float amount) {
        if (amount > 0)
            entity.heal(amount);
        else if (!entity.getType().is(RunecraftoryTags.BOSS_MONSTERS))
            entity.setHealth(entity.getHealth() + amount);
    }

    public static float playerLuck(Player player) {
        float luckAtt = player.getLuck();

        return luckAtt;
    }

    public static boolean isDisabled(LivingEntity entity) {
        return entity.hasEffect(ModEffects.SLEEP.get()) || entity.hasEffect(ModEffects.STUNNED.get());
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
        int lvl = Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel().getLevel()).orElse(1) + 1;
        float lvlPenalty = Math.max(0, (monster.level().getLevel() - lvl) * 0.02f);
        float brushBonus = brushCount * 0.05f;
        float loveAttackBonus = loveAttackCount * 0.002f;
        float tamingLvlBonus = (Platform.INSTANCE.getPlayerData(player).map(d -> d.getSkillLevel(EnumSkills.TAMING).getLevel()).orElse(1) - 1) * 0.005f;
        float tamingBonus = 1 + brushBonus + loveAttackBonus + tamingLvlBonus;
        return monster.tamingChance() * GeneralConfig.tamingMultiplier * tamingBonus - lvlPenalty;
    }

    public static NullPointerException playerDataException() {
        return new NullPointerException("Player capability is null. This shouldn't be");
    }

    public static EntityType<?> trySpawnTreasureChest(GateEntity spawner) {
        if (spawner.getRandom().nextFloat() < MobConfig.treasureChance) {
            if (spawner.getRandom().nextFloat() < MobConfig.mimicChance) {
                if (spawner.getRandom().nextFloat() < MobConfig.mimicStrongChance)
                    return ModEntities.GOBBLE_BOX.get();
                else
                    return ModEntities.MONSTER_BOX.get();
            } else
                return ModEntities.TREASURE_CHEST.get();
        }
        return null;
    }

    private static final List<WeightedChestTier> CHEST_TIERS = ImmutableList.of(
            new WeightedChestTier(0, 150, 0.4f, 550),
            new WeightedChestTier(1, 40, 0.3f, 200),
            new WeightedChestTier(2, 1, 0.3f, 60),
            new WeightedChestTier(3, 0, 0, 20) //0.05f
    );

    public static void tieredTreasureChest(GateEntity spawner, EntityTreasureChest chest) {
        int max = 0;
        for (WeightedChestTier tier : CHEST_TIERS) {
            max += tier.getModifiedWeight(spawner.level().getLevel());
        }

        int rand = spawner.getRandom().nextInt(max);
        for (WeightedChestTier tier : CHEST_TIERS) {
            if ((rand -= tier.getModifiedWeight(spawner.level().getLevel())) >= 0) continue;
            chest.setTier(tier.tier);
        }
    }

    public static int getRPFromVanillaFood(ItemStack stack) {
        FoodProperties prop = stack.getItem().getFoodProperties();
        if (prop != null)
            return (int) (stack.getItem().getFoodProperties().getNutrition() * 1.5
                    * (1 + 1.8 * stack.getItem().getFoodProperties().getSaturationModifier()));
        return 0;
    }

    public static Vec3 getStraightProjectileTarget(Vec3 from, Entity target) {
        AABB aabb = target.getBoundingBox().inflate(target.getBbHeight() * 0.1);
        return new Vec3(target.getX(), Mth.clamp(from.y(), aabb.minY, aabb.maxY), target.getZ());
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
            return owner.level.getNearestEntity(LivingEntity.class, TargetingConditions.forCombat().ignoreLineOfSight()
                    .range(range).selector(pred), livingOwner, livingOwner.getX(), livingOwner.getY(), livingOwner.getZ(), new AABB(-10, -10, -10, 10, 10, 10)
                    .move(livingOwner.position()));
        }
        return null;
    }

    record WeightedChestTier(int tier, int weight, float modifier, int max) {
        int getModifiedWeight(int mod) {
            return Math.min(this.max, (int) (this.weight + this.modifier * mod));
        }
    }
}
