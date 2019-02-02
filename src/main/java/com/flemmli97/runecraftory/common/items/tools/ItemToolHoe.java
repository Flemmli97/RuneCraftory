package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class ItemToolHoe extends ItemHoe implements IItemUsable, IChargeable
{
    private EnumToolTier tier;
    private int[] levelXP = new int[] { 5, 20, 50, 200, 500 };
    private int[] chargeRunes = new int[] { 1, 5, 15, 50, 100 };
    
    public ItemToolHoe(EnumToolTier tier) {
        super(ModItems.mat);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "hoe_" + tier.getName()));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.tier = tier;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
    
    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.FARM;
    }
    
    @Override
    public int itemCoolDownTicks() {
        return 15;
    }
    
    @Override
    public int[] getChargeTime() {
        int charge = 15;
        if (this.tier == EnumToolTier.PLATINUM) {
            charge = 7;
        }
        return new int[] { charge, this.tier.getTierLevel() };
    }
    
    @Override
    public void levelSkillOnHit(EntityPlayer player) {
    }
    
    @Override
    public void levelSkillOnBreak(EntityPlayer player) {
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }
    
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }
    
    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPTOOL;
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) 
    {
        if (entityLiving instanceof EntityPlayer && this.tier.getTierLevel() != 0) 
        {
            ItemStack itemstack = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
            int useTimeMulti = (this.getMaxItemUseDuration(stack) - timeLeft) / this.getChargeTime()[0];
            EntityPlayer player = (EntityPlayer)entityLiving;
            int range = Math.min(useTimeMulti, this.tier.getTierLevel());
            BlockPos pos = player.getPosition().down();
            boolean flag = false;
            if (range == 0) {
                RayTraceResult result = this.rayTrace(worldIn, player, false);
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    useOnBlock(player, worldIn, result.getBlockPos(), itemstack, result.sideHit);
                    return;
                }
            }
            else 
            {
                for (int x = -range; x <= range; ++x) 
                {
                    for (int z = -range; z <= range; ++z) 
                    {
                        BlockPos posNew = pos.add(x, 0, z);
                        if (player.canPlayerEdit(posNew.offset(EnumFacing.UP), EnumFacing.DOWN, itemstack)) 
                        {
                            int hook = ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
                            if (hook == 0) 
                            {
                                IBlockState iblockstate = worldIn.getBlockState(posNew);
                                Block block = iblockstate.getBlock();
                                if (worldIn.isAirBlock(posNew.up())) 
                                {
                                    IBlockState farmland = ModBlocks.farmland.getDefaultState();
                                    if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) 
                                    {
                                        setBlockHoeUse(itemstack, player, worldIn, posNew, farmland);
                                        flag = true;
                                    }
                                    else if (block == Blocks.DIRT) 
                                    {
                                        switch (iblockstate.getValue(BlockDirt.VARIANT)) {
                                            case DIRT:
                                                flag = true;
                                                setBlockHoeUse(itemstack, player, worldIn, posNew, farmland);
                                                break;
                                            case COARSE_DIRT:
                                                flag = true;
                                                setBlockHoeUse(itemstack, player, worldIn, posNew, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.DIRT));
                                                break;
											case PODZOL:
												break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
                capSync.decreaseRunePoints(player, this.chargeRunes[range]);
                capSync.increaseSkill(EnumSkills.EARTH, player, this.levelXP[range]);
                capSync.increaseSkill(EnumSkills.FARMING, player, this.levelXP[range]);
            }
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (handIn == EnumHand.MAIN_HAND && this.tier.getTierLevel() != 0) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.tier.getTierLevel() == 0) {
            return useOnBlock(player, worldIn, pos, player.getHeldItem(hand), facing);
        }
        return EnumActionResult.PASS;
    }
    
    public static EnumActionResult useOnBlock(EntityPlayer player, World worldIn, BlockPos pos, ItemStack itemstack, EnumFacing facing) {
        EnumActionResult result = EnumActionResult.PASS;
        if (player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
            int hook = ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
            if (hook != 0) {
                return (hook > 0) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            }
            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up())) {
                IBlockState farmland = ModBlocks.farmland.getDefaultState();
                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                    setBlockHoeUse(itemstack, player, worldIn, pos, farmland);
                    result = EnumActionResult.SUCCESS;
                }
                else if (block == Blocks.DIRT) {
                    switch (iblockstate.getValue(BlockDirt.VARIANT)) 
                    {
                        case DIRT:
                            setBlockHoeUse(itemstack, player, worldIn, pos, farmland);
                            result = EnumActionResult.SUCCESS;
                            break;
                        case COARSE_DIRT:
                            setBlockHoeUse(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.DIRT));
                            result = EnumActionResult.SUCCESS;
                            break;
						case PODZOL:
							break;
                    }
                }
            }
            if (result == EnumActionResult.SUCCESS) {
                IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
                capSync.decreaseRunePoints(player, 1);
                capSync.increaseSkill(EnumSkills.EARTH, player, 2);
                capSync.increaseSkill(EnumSkills.FARMING, player, 2);
            }
        }
        return result;
    }
    
    public static void setBlockHoeUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, (EntityLivingBase)player);
        }
    }
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.<String, AttributeModifier>create();
    }
}
