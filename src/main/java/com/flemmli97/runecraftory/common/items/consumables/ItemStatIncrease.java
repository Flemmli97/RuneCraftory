package com.flemmli97.runecraftory.common.items.consumables;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.LevelCalc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemStatIncrease extends ItemFood{

	private Stat stat;
	
	public ItemStatIncrease(String name, Stat stat) {
		super(0,0, false);
		this.stat=stat;
		this.setAlwaysEdible();
		this.setCreativeTab(RuneCraftory.medicine);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());	
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 3;
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		switch(this.stat)
		{
			case HP: cap.setMaxHealth(player, cap.getMaxHealth(player)+10);
				break;
			case INT: cap.setIntel(player, cap.getIntel()+1);
				break;
			case LEVEL: cap.addXp(player, LevelCalc.xpAmountForLevelUp(cap.getPlayerLevel()[0]) - cap.getPlayerLevel()[1]);
				break;
			case STR: cap.setStr(player, cap.getStr()+1);
				break;
			case VIT: cap.setVit(player, cap.getVit()+1);
				break;
		}
	}	
	
	@Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }
	
	@Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.getFoodStats().addStats(this, stack);
            worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, worldIn, entityplayer);
            entityplayer.addStat(StatList.getObjectUseStats(this));

            if (entityplayer instanceof EntityPlayerMP)
            {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
            }
        }

        stack.shrink(1);
        return stack;
    }
	
	public static enum Stat
	{
		LEVEL,
		HP,
		STR,
		INT,
		VIT;
	}
}
