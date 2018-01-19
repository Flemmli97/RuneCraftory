package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.block.BlockFarmland;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRFFarmland extends BlockFarmland{
	
	public BlockRFFarmland()
	{
		super();
        this.setTickRandomly(false);
        this.setRegistryName(new ResourceLocation(RFReference.MODID, "farmland"));
        this.setUnlocalizedName(this.getRegistryName().toString());
	}

	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		entityIn.fall(fallDistance, 1.0F);
    }
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Blocks.FARMLAND.getRegistryName(), "inventory"));
    }
}
