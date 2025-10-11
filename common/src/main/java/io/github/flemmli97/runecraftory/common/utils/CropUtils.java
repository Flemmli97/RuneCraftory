package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.blocks.ExtendedCropBlock;
import io.github.flemmli97.runecraftory.common.blocks.util.Growable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.mixin.CropBlockAccessor;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

public class CropUtils {

    public static void attemptGiantize(ServerLevel level, BlockPos cropPos, Growable crop, BlockState state, float progress, CropProperties props) {
        if (crop.runecraftory$isAtMaxAge(state) && props.getGiantVersion().isPresent() && progress >= 0.5) {
            if (state.is(props.getGiantVersion().get()))
                return;
            if (!level.getBlockState(cropPos.above()).isAir())
                return;
            if (state.getBlock() instanceof ExtendedCropBlock blockCrop) {
                int age = state.getValue(blockCrop.getAgeProperty());
                if (age != blockCrop.getGiantAge()) {
                    level.setBlock(cropPos, state.setValue(blockCrop.getAgeProperty(), blockCrop.getGiantAge()), Block.UPDATE_ALL);
                } else if (progress >= 1) {
                    FarmlandHandler.get(level.getServer())
                            .scheduleGiantCropMerge(level, cropPos, props.getGiantVersion().get().defaultBlockState());
                }
            } else if (progress >= 1) {
                FarmlandHandler.get(level.getServer())
                        .scheduleGiantCropMerge(level, cropPos, props.getGiantVersion().get().defaultBlockState());
            }
        }
    }

    public static void modifyCropDrops(BlockState state, LootParams.Builder builder, net.minecraft.world.level.block.CropBlock block, List<ItemStack> list) {
        CropProperties prop = DataPackHandler.INSTANCE.cropManager().get(block);
        if (prop != null) {
            Vec3 pos = builder.getOptionalParameter(LootContextParams.ORIGIN);
            int itemLevel = pos != null ? getCropLevel(builder.getLevel(), BlockPos.containing(pos)) : 1;
            if (block.isMaxAge(state)) {
                list.removeIf(item -> prop.getInfo().seed().contains(item.getItemHolder()));
                list.forEach(s -> modifyStack(prop, s, itemLevel));
            }
        }
    }

    private static int getCropLevel(ServerLevel level, BlockPos pos) {
        return FarmlandHandler.get(level.getServer())
                .getData(level, new BlockPos(pos)).map(FarmlandData::getCropLevel).orElse(1);
    }

    private static void modifyStack(CropProperties props, ItemStack stack, int level) {
        if (props.getInfo().crop().contains(stack.getItemHolder()) || props.getInfo().giant().map(p -> stack.is(p.getFirst())).orElse(false)) {
            stack.setCount(props.maxDrops());
            ItemComponentUtils.getLeveledItem(stack, level);
        }
    }

    public static void harvestCropRightClick(BlockState state, Level level, BlockPos pos, Entity entity, ItemStack stack, CropProperties props, InteractionHand hand, Function<ItemStack, ItemStack> stackConsumer) {
        if (!(level instanceof ServerLevel serverLevel) || !(state.getBlock() instanceof net.minecraft.world.level.block.CropBlock cropBlock) || !cropBlock.isMaxAge(state))
            return;
        Growable growable = (Growable) cropBlock;
        growable.onQuickHarvest(state, serverLevel, pos, entity, stack, stackConsumer);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        if (props != null && props.regrowable() && FarmlandHandler.get(level.getServer())
                .getData(serverLevel, growable.getFarmlandPosition(pos, state))
                .map(d -> d.getHealth() > 0).orElse(false)) {
            // Actually handled at block state change detection
            level.setBlock(pos, state.setValue(((CropBlockAccessor) cropBlock).cropAgeProperty(), 0), Block.UPDATE_ALL);
        } else {
            level.removeBlock(pos, false);
        }
        if (entity instanceof ServerPlayer player) {
            RuneCraftoryCriteria.HARVEST_CROP.get().trigger(player, state);
            spawnRuney(player, pos);
            LevelCalc.levelSkill(Platform.INSTANCE.getPlayerData(player), Skills.FARMING, 2f);
        }
        if (entity instanceof LivingEntity living)
            living.swing(hand, true);
    }

    public static void spawnRuney(ServerPlayer player, BlockPos pos) {
        if (player.getRandom().nextFloat() < GeneralConfig.runeyChance) {
            Entity entity = player.getRandom().nextFloat() < 0.4 ? RuneCraftoryEntities.RUNEY.get().create(player.serverLevel()) : RuneCraftoryEntities.STAT_BONUS.get().create(player.serverLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            player.level().addFreshEntity(entity);
        }
    }
}
