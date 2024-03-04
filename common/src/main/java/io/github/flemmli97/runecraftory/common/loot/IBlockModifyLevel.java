package io.github.flemmli97.runecraftory.common.loot;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import org.jetbrains.annotations.Nullable;

public interface IBlockModifyLevel {

    int getLevel(BlockState state, @Nullable BlockEntity blockEntity, ItemLevelLootFunction func, LootContext ctx);
}
