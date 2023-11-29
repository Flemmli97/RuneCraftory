package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.Growable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CropUtils {

    public static void attemptGiantize(ServerLevel level, BlockPos cropPos, Growable crop, BlockState state, float progress, CropProperties props) {
        if (crop.isAtMaxAge(state) && props.getGiantVersion() != Blocks.AIR && progress >= 0.5) {
            if (state.is(props.getGiantVersion()))
                return;
            if (state.getBlock() instanceof BlockCrop blockCrop) {
                int age = state.getValue(blockCrop.getAgeProperty());
                if (age != blockCrop.getGiantAge()) {
                    level.setBlock(cropPos, state.setValue(blockCrop.getAgeProperty(), blockCrop.getGiantAge()), Block.UPDATE_ALL);
                } else if (progress >= 1) {
                    FarmlandHandler.get(level.getServer())
                            .scheduleGiantCropMerge(level, cropPos, props.getGiantVersion().defaultBlockState());
                }
            } else if (progress >= 1) {
                FarmlandHandler.get(level.getServer())
                        .scheduleGiantCropMerge(level, cropPos, props.getGiantVersion().defaultBlockState());
            }
        }
    }

    public static CropProperties getPropertiesFor(CropBlock crop) {
        return DataPackHandler.SERVER_PACK.cropManager().get(((CropBlockAccessor) crop).getSeedItem().asItem());
    }

    public static void modifyCropDrops(BlockState state, LootContext.Builder builder, CropBlock block, List<ItemStack> list) {
        CropProperties prop = getPropertiesFor(block);
        if (prop != null) {
            Vec3 pos = builder.getOptionalParameter(LootContextParams.ORIGIN);
            int itemLevel = pos != null ? getCropLevel(builder.getLevel(), new BlockPos(pos)) : 1;
            if (block.isMaxAge(state)) {
                List<ItemStack> remove = new ArrayList<>();
                boolean removedSeed = list.size() < 2;
                for (ItemStack stack : list) {
                    if (!removedSeed && stack.is(((CropBlockAccessor) block).getSeedItem().asItem())) {
                        remove.add(stack);
                        removedSeed = true;
                    }
                }
                list.removeIf(remove::contains);
                list.forEach(s -> modifyStack(prop, s, itemLevel));
            } else if (block instanceof BlockCrop)
                list.clear();
        }
    }

    private static int getCropLevel(ServerLevel level, BlockPos pos) {
        return FarmlandHandler.get(level.getServer())
                .getData(level, new BlockPos(pos)).map(FarmlandData::getCropLevel).orElse(1);
    }

    private static void modifyStack(CropProperties props, ItemStack stack, int level) {
        if (stack.is(ModTags.CROPS)) {
            stack.setCount(props.maxDrops());
            ItemNBT.getLeveledItem(stack, level);
        }
    }

    public static void harvestCropRightClick(BlockState state, Level level, BlockPos pos, Entity entity, ItemStack stack, CropProperties props, InteractionHand hand, Function<ItemStack, ItemStack> stackConsumer) {
        if (!(level instanceof ServerLevel serverLevel) || !(state.getBlock() instanceof CropBlock cropBlock))
            return;
        if (stackConsumer != null) {
            Block.getDrops(state, serverLevel, pos, null, entity, stack)
                    .forEach(s -> {
                        ItemStack rest = stackConsumer.apply(s);
                        if (!rest.isEmpty())
                            Block.popResource(level, pos, rest);
                    });
            state.spawnAfterBreak(serverLevel, pos, ItemStack.EMPTY);
        } else
            Block.dropResources(state, level, pos, null, entity, stack);
        level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        if (props != null && props.regrowable()) {
            //Actually handled at block state change detection
            level.setBlock(pos, state.setValue(cropBlock.getAgeProperty(), 0), Block.UPDATE_ALL);
        } else
            level.removeBlock(pos, false);
        if (cropBlock.isMaxAge(state) && entity instanceof ServerPlayer player) {
            spawnRuney(player, pos);
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 2f));
        }
        if (entity instanceof LivingEntity living)
            living.swing(hand, true);
    }

    public static void spawnRuney(ServerPlayer player, BlockPos pos) {
        if (player.getRandom().nextFloat() < GeneralConfig.runeyChance) {
            Entity entity = player.getRandom().nextFloat() < 0.4 ? ModEntities.RUNEY.get().create(player.getLevel()) : ModEntities.STAT_BONUS.get().create(player.getLevel());
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            player.level.addFreshEntity(entity);
        }
    }
}
