package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.tenshilib.common.effect.ExtendedMobEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

public class BathEffect extends MobEffect implements ExtendedMobEffect {

    public BathEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xff40b5e1);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        if (living.level().isClientSide())
            return true;
        EntityData entityData = RunecraftoryAttachments.ENTITY_DATA.get().get(living);
        if (this.isInWater(living)) {
            entityData.setEnteredBath(true);
            living.heal(living.getMaxHealth() * 0.04f);
            if (living instanceof ServerPlayer player) {
                PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
                data.regenRunePoints(Math.max(1, (int) (data.getMaxRunePoints() * 0.03f)));
                LevelCalc.levelSkill(data, Skills.BATH, 2f);
            }
        } else if (entityData.enteredBath()) {
            entityData.setEnteredBath(false);
            living.removeEffect(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(this));
        }
        return super.applyEffectTick(living, amplifier);
    }

    private boolean isInWater(LivingEntity entity) {
        AABB aABB = entity.getBoundingBox().deflate(0.001);
        int minX = Mth.floor(aABB.minX);
        int maxX = Mth.ceil(aABB.maxX);
        int minY = Mth.floor(aABB.minY);
        int maxY = Mth.ceil(aABB.maxY);
        int minZ = Mth.floor(aABB.minZ);
        int maxZ = Mth.ceil(aABB.maxZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int y = minY; y < maxY; ++y) {
            for (int x = minX; x < maxX; ++x) {
                for (int z = minZ; z < maxZ; ++z) {
                    pos.set(x, y, z);
                    FluidState fluidState = entity.level().getFluidState(pos);
                    if (fluidState.is(RunecraftoryTags.Fluids.HOT_SPRING_FLUID)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldSync() {
        return false;
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, MobEffectInstance instance) {
        RunecraftoryAttachments.ENTITY_DATA.get().get(entity).setEnteredBath(false);
    }
}
