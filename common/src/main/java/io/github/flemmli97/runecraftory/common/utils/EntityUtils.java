package io.github.flemmli97.runecraftory.common.utils;

import com.google.common.collect.ImmutableList;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.S2CUpdateAttributesWithAdditional;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Npc;
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

    public static boolean canMonsterTargetNPC(Entity e) {
        if (e instanceof EntityNPCBase npc && npc.getEntityToFollowUUID() != null)
            return true;
        return MobConfig.mobAttackNPC && e instanceof Npc;
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
        else if (!entity.getType().is(ModTags.BOSS_MONSTERS))
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
