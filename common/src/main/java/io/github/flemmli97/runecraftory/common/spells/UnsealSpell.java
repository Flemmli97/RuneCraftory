package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.platform.Platform;
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

public class UnsealSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Consumer<LivingEntity> apply = living -> {
            if (lvl >= 10) {
                entity.removeEffect(ModEffects.POISON.get());
            }
            if (lvl >= 5) {
                entity.removeEffect(ModEffects.PARALYSIS.get());
            }
            entity.removeEffect(MobEffects.DIG_SLOWDOWN);
            entity.removeEffect(ModEffects.SEAL.get());
            PoisonHealSpell.spawnStatusHealParticles(living);
        };
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
        apply.accept(entity);
        entities.forEach(apply);
        return true;
    }
}
