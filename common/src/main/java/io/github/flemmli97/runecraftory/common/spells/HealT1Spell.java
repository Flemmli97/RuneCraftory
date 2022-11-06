package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.entities.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class HealT1Spell extends Spell {

    public static void spawnHealParticles(LivingEntity entity) {
        if (entity.level.isClientSide)
            return;
        ServerLevel serverLevel = (ServerLevel) entity.level;
        serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + entity.getBbHeight() + 0.5, entity.getZ(), 0, 0, 0.1, 0, 0);
        for (int i = 0; i < 10; i++) {
            serverLevel.sendParticles(new ColoredParticleData(ModParticles.light.get(), 63 / 255F, 201 / 255F, 63 / 255F, 0.4f, 2f), entity.getX() + entity.getRandom().nextGaussian() * 0.2, entity.getY() + entity.getBbHeight() * 0.5 + entity.getRandom().nextGaussian() * 0.07, entity.getZ() + entity.getRandom().nextGaussian() * 0.2, 1, entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03, 0);
        }
    }

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.LOVE, 1));
    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        boolean rp = !(entity instanceof Player player) || Platform.INSTANCE.getPlayerData(player).map(data ->
                LevelCalc.useRP(player, data, (int) (data.getMaxRunePoints() * 0.04), false, true, true, EnumSkills.LOVE)).orElse(false);
        if (!rp)
            return false;
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(2), e -> {
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
        float healAmount = CombatUtils.getAttributeValueRaw(entity, ModAttributes.RF_MAGIC.get()) * (0.6f + lvl * 0.2f);
        entity.heal(healAmount);
        spawnHealParticles(entity);
        entities.forEach(e -> {
            e.heal(healAmount);
            spawnHealParticles(e);
        });
        return true;
    }

    @Override
    public int rpCost() {
        return 20;
    }
}
