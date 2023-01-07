package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BathEffect extends MobEffect {

    public BathEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.isInWater()) {
            BlockState state = living.getFeetBlockState();
            if (state.getFluidState().is(FluidTags.WATER)) {
                BlockState under = living.level.getBlockState(living.blockPosition().below());
                BlockState under2 = living.level.getBlockState(living.blockPosition().below(2));
                if (under.is(ModTags.ONSEN_PROVIDER) || under2.is(ModTags.ONSEN_PROVIDER)) {
                    living.heal(living.getMaxHealth() * 0.04f);
                    if (living instanceof ServerPlayer player)
                        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                            data.refreshRunePoints(player, Math.max(1, (int) (data.getMaxRunePoints() * 0.03f)));
                            LevelCalc.levelSkill(player, data, EnumSkills.BATH, 2f);
                        });
                }
            }
        }
        super.applyEffectTick(living, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
