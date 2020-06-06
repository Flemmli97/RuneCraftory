package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBambooSprout extends BlockHerb{

    public BlockBambooSprout(String name, String drop) {
        super(name, drop);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if(worldIn.isRemote && (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemHoe)){
            spawnAsEntity(worldIn, pos, new ItemStack(ModItems.bambooSprout, 1));
        }
        else
            super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }
}
