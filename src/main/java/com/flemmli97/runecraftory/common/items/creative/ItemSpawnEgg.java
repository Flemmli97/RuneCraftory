package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileBossSpawner;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.EntitySpawnEggList;
import com.flemmli97.runecraftory.common.init.GateSpawning;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.lib.LibReference;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSpawnEgg extends Item{
	
	public ItemSpawnEgg()
	{
		this.setHasSubtypes(true);
		this.setCreativeTab(RuneCraftory.monsters);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "spawn_egg"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@SuppressWarnings("deprecation")
    @Override
	public String getItemStackDisplayName(ItemStack stack)
    {
        String s = net.minecraft.util.text.translation.I18n.translateToLocal(this.getUnlocalizedName() + ".name").trim();
        ResourceLocation entityName = getEntityIdFromItem(stack);

        if (entityName != null)
        {
            s = s + " " + net.minecraft.util.text.translation.I18n.translateToLocal("entity." + entityName.toString().replaceAll("runecraftory:", "")  + ".name");
        }

        return s;
    }
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + "Rename to a number to set level");
	}

	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if (!player.canPlayerEdit(pos.offset(facing), facing, player.getHeldItem(hand)))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = world.getBlockState(pos);

            int level = 5;
            if (player.getHeldItem(hand).hasDisplayName())
            {
        			try
        			{
        				level = Integer.parseInt(player.getHeldItem(hand).getDisplayName());
        			}
        			catch(Exception e)
        			{}
            }
            if (iblockstate.getBlock() == Blocks.MOB_SPAWNER)
            {
                TileEntity tileentity = world.getTileEntity(pos);

                if (tileentity instanceof TileEntityMobSpawner)
                {
                    MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
                    mobspawnerbaselogic.setEntityId(getEntityIdFromItem(player.getHeldItem(hand)));
                    tileentity.markDirty();
                    world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

                    if (!player.capabilities.isCreativeMode)
                    {
                    		player.getHeldItem(hand).shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
            else if(iblockstate.getBlock()==ModBlocks.bossSpawner)
            {
                TileEntity tileentity = world.getTileEntity(pos);
                if(tileentity instanceof TileBossSpawner)
                {
                		TileBossSpawner spawner = (TileBossSpawner) tileentity;
                		if(spawner.setEntity(player, getEntityIdFromItem(player.getHeldItem(hand))))
                		{
	                		tileentity.markDirty();
	                		world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
	                		if (!player.capabilities.isCreativeMode)
	                		{
	                			player.getHeldItem(hand).shrink(1);
	                		}
	                		return EnumActionResult.SUCCESS;
                		}
                }
            }

            pos = pos.offset(facing);
            double d0 = 0.0D;

            if (facing == EnumFacing.UP && iblockstate instanceof BlockFence)
            {
                d0 = 0.5D;
            }

            EntityMobBase entity = spawnCreature(getEntityIdFromItem(player.getHeldItem(hand)), world, (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D, level);
            if (entity != null)
            {
                if (!player.capabilities.isCreativeMode)
                {
                		player.getHeldItem(hand).shrink(1);
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (world.isRemote)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
        }
        else
        {
            RayTraceResult raytraceresult = this.rayTrace(world, player, true);

            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!(world.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
                }
                else if (world.isBlockModifiable(player, blockpos) && player.canPlayerEdit(blockpos, raytraceresult.sideHit, player.getHeldItem(hand)))
                {
                		int level = 5;
                	 	if (player.getHeldItem(hand).hasDisplayName())
                     {
                 			try
                 			{
                 				level = Integer.parseInt(player.getHeldItem(hand).getDisplayName());
                 			}
                 			catch(Exception e)
                 			{}
                     }

                     EntityMobBase entity = spawnCreature(getEntityIdFromItem(player.getHeldItem(hand)), world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D, level);
                     
                    if (entity != null)
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                        		player.getHeldItem(hand).shrink(1);
                        }

                        player.addStat(StatList.getObjectUseStats(this));
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
                    }
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));

                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
                }
            }
            else
            {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
            }
        }
    }

    @Nullable
    public static EntityMobBase spawnCreature(ResourceLocation entityName, World world, double x, double y, double z, int level)
    {
		EntityMobBase entityliving = GateSpawning.entityFromString(world, entityName);
        if (!world.isRemote)
        {
    		entityliving.setLevel(level);
    		entityliving.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
	        entityliving.rotationYawHead = entityliving.rotationYaw;
	        entityliving.renderYawOffset = entityliving.rotationYaw;
	        entityliving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
	        world.spawnEntity(entityliving);
	        entityliving.playLivingSound();
        }
        return entityliving;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
		if(this.isInCreativeTab(tab)) {
            for (EntityList.EntityEggInfo eggEntity : EntitySpawnEggList.values()) {
                ItemStack stack = new ItemStack(this, 1);
                applyEntityIdToItemStack(stack, eggEntity.spawnedID);
                items.add(stack);
            }
        }
    }

    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation entityId)
    {
        NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        nbttagcompound.setString("entity", entityId.toString());
        stack.setTagCompound(nbttagcompound);
    }

    @Nullable
    public static ResourceLocation getEntityIdFromItem(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null && nbttagcompound.hasKey("entity"))
        {
            return new ResourceLocation(nbttagcompound.getString("entity"));
        }
        return null;
    }
}
