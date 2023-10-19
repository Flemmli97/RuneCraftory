package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HealT2Spell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(12), e -> {
            if (e == entity)
                return true;
            if (entity instanceof Player player) {
                return e instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID())
                        || e instanceof AbstractVillager || e instanceof Animal || Platform.INSTANCE.getPlayerData(player).map(d -> d.party.isPartyMember(e)).orElse(false);
            } else {
                if (entity instanceof HealingPredicateEntity healer)
                    return healer.healeableEntities().test(e);
                return false;
            }
        });
        float healAmount = (float) (CombatUtils.getAttributeValue(entity, ModAttributes.MAGIC.get()) * (1.5f + lvl * 0.1f));
        entity.heal(healAmount);
        HealT1Spell.spawnHealParticles(entity);
        entities.forEach(e -> {
            e.heal(healAmount);
            HealT1Spell.spawnHealParticles(e);
        });
        return true;
    }
}
