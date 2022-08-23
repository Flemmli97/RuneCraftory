package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HealT2Spell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.LOVE, 2));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data ->
                LevelCalc.useRP(player, data, (int) (data.getMaxRunePoints() * 0.15), false, true, true, 1, EnumSkills.LOVE)).orElse(false);
        if (rp) {
            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(8), e -> {
                if (e == entity)
                    return true;
                if (entity instanceof Player player) {
                    return e instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID())
                            || e instanceof AbstractVillager || e instanceof Animal;
                } else {
                    if (entity instanceof HealingPredicateEntity healer)
                        return healer.healeableEntities().test(e);
                    return false;
                }
            });
            float healAmount = CombatUtils.getAttributeValueRaw(entity, ModAttributes.RF_MAGIC.get()) * (1.5f + level * 0.1f);
            entity.heal(healAmount);
            HealT1Spell.spawnHealParticles(entity);
            entities.forEach(e -> {
                e.heal(healAmount);
                HealT1Spell.spawnHealParticles(e);
            });
            return true;
        }
        return false;
    }

    @Override
    public int rpCost() {
        return 0;
    }
}
