package io.github.flemmli97.runecraftory.common.loot;

import net.minecraft.block.BlockState;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface IBlockModifyLevel {

    int getLevel(BlockState state, @Nullable TileEntity tile, ItemLevelLootFunction func, LootContext ctx);
}
