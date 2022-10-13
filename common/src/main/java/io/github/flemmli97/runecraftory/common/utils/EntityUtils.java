package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EntityUtils {

    public static boolean isExhaust(LivingEntity entity) {
        return entity.hasEffect(ModEffects.fatigue.get());
    }

    public static boolean paralysed(LivingEntity entity) {
        return entity.hasEffect(ModEffects.paralysis.get());
    }

    public static boolean sleeping(LivingEntity entity) {
        return entity.hasEffect(ModEffects.sleep.get());
    }

    public static boolean sealed(LivingEntity entity) {
        return entity.hasEffect(ModEffects.seal.get());
    }

    public static boolean shouldShowFarmlandView(LivingEntity entity) {
        ItemStack main = entity.getMainHandItem();
        ItemStack off = entity.getOffhandItem();
        return ItemNBT.canBeUsedAsMagnifyingGlass(main) || ItemNBT.canBeUsedAsMagnifyingGlass(off);
    }

    public static void foodHealing(LivingEntity entity, float amount) {
        if (amount > 0)
            entity.heal(amount);
        else
            entity.setHealth(entity.getHealth() + amount);
    }

    public static float playerLuck(Player player) {
        float luckAtt = player.getLuck();

        return luckAtt;
    }

    public static boolean isDisabled(LivingEntity entity) {
        return entity.hasEffect(ModEffects.sleep.get());
    }

    @Nullable
    public static UUID tryGetOwner(LivingEntity entity) {
        if (entity instanceof OwnableEntity ownableEntity)
            return ownableEntity.getOwnerUUID();
        return null;
    }

    public static float tamingChance(BaseMonster monster, Player player, float multiplier) {
        if (multiplier == 0)
            return 0;
        int lvl = Platform.INSTANCE.getPlayerData(player).map(d -> d.getPlayerLevel().getLevel()).orElse(1) + 1;
        float lvlPenalty = (float) Math.pow(0.89, Math.max(0, monster.level().getLevel() - lvl));
        return monster.tamingChance() * multiplier * GeneralConfig.tamingMultiplier * lvlPenalty;
    }

    public static NullPointerException playerDataException() {
        return new NullPointerException("Player capability is null. This shouldn't be");
    }

    public static EntityType<?> trySpawnTreasureChest(GateEntity spawner) {
        if (spawner.getRandom().nextFloat() < MobConfig.treasureChance) {
            if (spawner.getRandom().nextFloat() < MobConfig.mimicChance) {
                if (spawner.getRandom().nextFloat() < MobConfig.mimicStrongChance)
                    return ModEntities.gobbleBox.get();
                else
                    return ModEntities.monsterBox.get();
            } else
                return ModEntities.treasureChest.get();
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

    record WeightedChestTier(int tier, int weight, float modifier, int max) {
        int getModifiedWeight(int mod) {
            return Math.min(this.max, (int) (this.weight + this.modifier * mod));
        }
    }
}
