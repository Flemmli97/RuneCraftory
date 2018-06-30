package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCropSeed extends Item implements IPlantable, IModelRegister{

	private String crop;

	public ItemCropSeed(String name, String cropOreDictName)
	{
		this.crop=cropOreDictName;
		this.setCreativeTab(RuneCraftory.crops);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "seed_"+name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        CropMap.addSeed(cropOreDictName, this);
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getRegistryName().toString();
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft)
    {
		if(entityLiving instanceof EntityPlayer && stack.getCount()!=1)
		{	       
			EntityPlayer player = (EntityPlayer) entityLiving;
	        boolean canUse = (this.getMaxItemUseDuration(stack) - timeLeft)>=10;
	        boolean creative = player.capabilities.isCreativeMode;
	        RayTraceResult result = this.rayTrace(world, player, false);
	        if (result!=null && result.typeOfHit==Type.BLOCK&&result.sideHit == EnumFacing.UP)
	        {
    			BlockPos pos = result.getBlockPos();
		        IBlockState state = world.getBlockState(pos);
	        	if(player.canPlayerEdit(pos.offset(result.sideHit), result.sideHit, stack) && 
	        		state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, this) && world.isAirBlock(pos.up()))
	        	{
					IPlayer capSync = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
					capSync.increaseSkill(EnumSkills.FARMING, player, 1);
					world.setBlockState(pos.up(), CropMap.plantFromString(this.crop).getDefaultState(), 11);
					if(!creative)
					stack.shrink(1);
					if(canUse)
						for(int x = -1; x <= 1; x++)
							for(int z = -1; z <= 1;z++)
							{
								BlockPos side=pos.add(x, 0, z);
						        IBlockState stateNew = world.getBlockState(side);
								if(player.canPlayerEdit(side.offset(result.sideHit), result.sideHit, stack) && 
										stateNew.getBlock().canSustainPlant(stateNew, world, side, EnumFacing.UP, this) && world.isAirBlock(side.up()))
						        {
									world.setBlockState(side.up(), CropMap.plantFromString(this.crop).getDefaultState(), 11);
									capSync.increaseSkill(EnumSkills.FARMING, player, 1);
									if(!creative)
									stack.shrink(1);
								}
							}
	        	}
			}
		}
    }
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
		if(handIn == EnumHand.MAIN_HAND && itemstack.getCount()!=1)
		{
	        playerIn.setActiveHand(handIn);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		else
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        if(hand==EnumHand.MAIN_HAND && itemstack.getCount()==1)
        {
        	IBlockState state = worldIn.getBlockState(pos);
	        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up()))
	        {
	            worldIn.setBlockState(pos.up(), CropMap.plantFromString(this.crop).getDefaultState(), 11);
	            itemstack.shrink(1);
	            return EnumActionResult.SUCCESS;
	        }
	        else
	        {
	            return EnumActionResult.FAIL;
	        }
        }
        else
            return EnumActionResult.FAIL;
    }
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {

        return CropMap.plantFromString(this.crop).getDefaultState();
	}
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));		
	}
}
