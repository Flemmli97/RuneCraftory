package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.tenshilib.common.particle.AdvancedParticleContainer;
import io.github.flemmli97.tenshilib.common.particle.data.ColorData;
import io.github.flemmli97.tenshilib.common.particle.data.MotionData;
import io.github.flemmli97.tenshilib.common.particle.data.ParticleMetaData;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
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
        if (entity.level().isClientSide)
            return;
        ServerLevel serverLevel = (ServerLevel) entity.level();
        serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + entity.getBbHeight() + 0.5, entity.getZ(), 0, 0, 0.1, 0, 0);
        for (int i = 0; i < 16; i++) {
            AdvancedParticleContainer.make(RuneCraftoryParticles.LIGHT.get())
                    .addData(new ColorData(57 / 255F, 112 / 255F, 179 / 255F, 1))
                    .addData(new MotionData(entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03))
                    .addData(new ParticleMetaData(20, false, 0))
                    .add(entity.level(), entity.getRandomX(1.2),
                            entity.getY() + entity.getBbHeight() * 0.5 + entity.getRandom().nextGaussian() * 0.5 * entity.getBbHeight() * 0.3,
                            entity.getRandomZ(1.2));
        }
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        Consumer<LivingEntity> apply = living -> {
            if (lvl >= 10) {
                living.removeEffect(RuneCraftoryEffects.SEAL.asHolder());
            }
            if (lvl >= 5) {
                living.removeEffect(RuneCraftoryEffects.PARALYSIS.asHolder());
                living.removeEffect(MobEffects.WITHER);
            }
            living.removeEffect(MobEffects.POISON);
            living.removeEffect(RuneCraftoryEffects.POISON.asHolder());
            spawnStatusHealParticles(living);
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
