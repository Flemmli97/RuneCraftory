package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleTypes;
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

public class PoisonHealSpell extends Spell {

    public static void spawnStatusHealParticles(LivingEntity entity) {
        if (entity.level.isClientSide)
            return;
        ServerLevel serverLevel = (ServerLevel) entity.level;
        serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + entity.getBbHeight() + 0.5, entity.getZ(), 0, 0, 0.1, 0, 0);
        for (int i = 0; i < 16; i++) {
            serverLevel.sendParticles(new ColoredParticleData(ModParticles.LIGHT.get(), 57 / 255F, 112 / 255F, 179 / 255F, 0.5f, 2f), entity.getRandomX(1.2), entity.getY() + entity.getBbHeight() * 0.5 + entity.getRandom().nextGaussian() * 0.5 * entity.getBbHeight() * 0.3, entity.getRandomZ(1.2), 1, entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03, 0);
        }
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Consumer<LivingEntity> apply = living -> {
            if (lvl >= 10) {
                living.removeEffect(ModEffects.SEAL.get());
            }
            if (lvl >= 5) {
                living.removeEffect(ModEffects.PARALYSIS.get());
                living.removeEffect(MobEffects.WITHER);
            }
            living.removeEffect(MobEffects.POISON);
            living.removeEffect(ModEffects.POISON.get());
            spawnStatusHealParticles(living);
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
        playSound(entity, ModSounds.SPELL_GENERIC_HEAL.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        return true;
    }
}
