package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemFertilizer extends Item
{
    public ItemFertilizer(String name) {
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());

            if (enumdyecolor == EnumDyeColor.WHITE)
            {
                if (this.applyItem(itemstack, worldIn, pos, player, hand))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.playEvent(2005, pos, 0);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
            return EnumActionResult.PASS;
        }
    }
    
    private boolean applyItem(ItemStack stack, World world, BlockPos pos, EntityPlayer player, EnumHand hand)
    {
		TileEntity tile = world.getTileEntity(pos);
    	if(tile instanceof TileFarmland)
    	{
    		if(this.useItemOnFarmland(stack, world, (TileFarmland) tile, player, hand))
    		{
    			stack.shrink(1);
        		for(int x = -1; x <= 1; x++)
        			for(int z = -1; z <= 1; z++)
        			{
        				if(x!=0||z!=0)
        				{
        					TileEntity tile2 = world.getTileEntity(pos.add(x, 0, z));
        		    		if(tile instanceof TileFarmland)
        		    			this.useItemOnFarmland(stack, world, (TileFarmland) tile2, player, hand);
        				}
        			}
        		return true;
    		}
    	}
    	return false;
    }
    
    protected abstract boolean useItemOnFarmland(ItemStack stack, World world, TileFarmland tile, EntityPlayer player, EnumHand hand);
    
}
