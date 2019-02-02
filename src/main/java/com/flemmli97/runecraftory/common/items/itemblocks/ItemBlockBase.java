package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.BlockMultiBase;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockBase extends Item{
	
	private BlockMultiBase savedBlock;
	public ItemBlockBase(BlockMultiBase savedBlock)
	{
		super();
		this.setCreativeTab(RuneCraftory.blocks);
		this.savedBlock=savedBlock;
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(!worldIn.isRemote)
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);
	        Block block = iblockstate.getBlock();
	        boolean flag = block.isReplaceable(worldIn, pos);
	
	        if (!flag)
	        {
	            pos = pos.up();
	        }
	        EnumFacing blockFacing = player.getHorizontalFacing().getOpposite();
	        BlockPos blockpos = pos.offset(blockFacing.rotateYCCW());
	        ItemStack itemstack = player.getHeldItem(hand);
            if (player.canPlayerEdit(pos, facing, itemstack) && player.canPlayerEdit(blockpos, facing, itemstack))
            {
            		IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
                boolean flag1 = iblockstate1.getBlock().isReplaceable(worldIn, blockpos);
                boolean flag2 = flag || worldIn.isAirBlock(pos);
                boolean flag3 = flag1 || worldIn.isAirBlock(blockpos);

                if (flag2 && flag3 && worldIn.getBlockState(pos.down()).isSideSolid(worldIn, blockpos, EnumFacing.UP) && worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos, EnumFacing.UP))
                {
                    IBlockState iblockstate2 = savedBlock.getDefaultState().withProperty(BlockMultiBase.FACING, blockFacing).withProperty(BlockMultiBase.PART, BlockMultiBase.EnumPartType.LEFT);
                    worldIn.setBlockState(pos, iblockstate2);
                    worldIn.setBlockState(blockpos, iblockstate2.withProperty(BlockMultiBase.PART, BlockMultiBase.EnumPartType.RIGHT));
                    SoundType soundtype = iblockstate2.getBlock().getSoundType(iblockstate2, worldIn, pos, player);
                    worldIn.playSound((EntityPlayer)null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                    worldIn.notifyNeighborsRespectDebug(pos, block, false);
                    worldIn.notifyNeighborsRespectDebug(blockpos, iblockstate1.getBlock(), false);

                    itemstack.shrink(1);
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    return EnumActionResult.FAIL;
                }
             }
             else
             {
                 return EnumActionResult.FAIL;
             }
		}
        else
        {
            return EnumActionResult.FAIL;
        }
    }
}
