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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class BathEffect extends MobEffect {

    public BathEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living instanceof ServerPlayer player && player.isInWater()) {
            BlockState state = player.getFeetBlockState();
            if (state.getFluidState().is(FluidTags.WATER)) {
                BlockState under = player.level.getBlockState(player.blockPosition().below());
                BlockState under2 = player.level.getBlockState(player.blockPosition().below(2));
                if (under.is(ModTags.ONSEN_PROVIDER) || under2.is(ModTags.ONSEN_PROVIDER)) {
                    player.heal(player.getMaxHealth() * 0.02f);
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                        data.refreshRunePoints(player, Math.max(1, (int) (data.getMaxRunePoints() * 0.02f)));
                        LevelCalc.levelSkill(player, data, EnumSkills.BATH, 1.5f);
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
