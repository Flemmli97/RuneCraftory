package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BathEffect extends MobEffect {

    private static final VoxelShape VIRTUAL_FENCE_POST = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public BathEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.isInWater()) {
            BlockState state = living.getFeetBlockState();
            if (state.getFluidState().is(FluidTags.WATER)) {
                if (isSmokeyPos(living.level, living.blockPosition())) {
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

    public static boolean isSmokeyPos(Level level, BlockPos pos) {
        for (int i = 1; i <= 5; ++i) {
            BlockPos blockPos = pos.below(i);
            BlockState blockState = level.getBlockState(blockPos);
            if (isValidState(blockState)) {
                return true;
            }
            boolean bl = Shapes.joinIsNotEmpty(VIRTUAL_FENCE_POST, blockState.getCollisionShape(level, pos, CollisionContext.empty()), BooleanOp.AND);
            if (!bl) continue;
            BlockState blockState2 = level.getBlockState(blockPos.below());
            return isValidState(blockState2);
        }
        return false;
    }

    private static boolean isValidState(BlockState state) {
        if (state.is(BlockTags.CAMPFIRES)) {
            return state.hasProperty(CampfireBlock.LIT) && state.getValue(CampfireBlock.LIT);
        }
        return state.is(ModTags.ONSEN_PROVIDER);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
