package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.BlockFarmland;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRFFarmland extends BlockFarmland implements ITileEntityProvider{
	
	public BlockRFFarmland()
	{
		super();
        this.setTickRandomly(false);
        this.setHardness(0.6F);
        this.blockSoundType = SoundType.GROUND;
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "farmland"));
        this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));
        return plantType==EnumPlantType.Crop;
    }
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		entityIn.fall(fallDistance, 1.0F);
    }
	
	@Override
	public void fillWithRain(World world, BlockPos pos)
    {
		world.setBlockState(pos, this.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7));
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFarmland(world);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Blocks.FARMLAND.getRegistryName(), "inventory"));
    }
}
