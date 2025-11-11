package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class ParaHealSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Consumer<LivingEntity> apply = living -> {
            if (lvl >= 10) {
                entity.removeEffect(RuneCraftoryEffects.SEAL.asHolder());
            }
            if (lvl >= 5) {
                entity.removeEffect(RuneCraftoryEffects.POISON.asHolder());
                entity.removeEffect(MobEffects.DIG_SLOWDOWN);
            }
            entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            entity.removeEffect(RuneCraftoryEffects.PARALYSIS.asHolder());
            PoisonHealSpell.spawnStatusHealParticles(living);
        };
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(12), e -> {
            if (e == entity)
                return true;
            if (entity instanceof Player player) {
                return e instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID())
                        || e instanceof AbstractVillager || e instanceof Animal || RunecraftoryAttachments.PLAYER_DATA.get().get(player).party.isPartyMember(e);
            } else {
                if (entity instanceof HealingPredicateEntity healer)
                    return healer.healeableEntities().test(e);
                return false;
            }
        });
        apply.accept(entity);
        entities.forEach(apply);
        playSound(entity, RuneCraftorySounds.SPELL_GENERIC_HEAL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
