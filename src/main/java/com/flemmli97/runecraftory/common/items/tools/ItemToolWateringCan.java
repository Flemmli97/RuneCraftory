package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolWateringCan extends ItemTool implements IItemUsable, IModelRegister, IChargeable
{
	private EnumToolTier tier;
	private int[] levelXP = new int[] {5, 20, 50, 200, 500};
	private int[] chargeRunes = new int[] {1, 5, 15, 50, 100};
	private final int[] waterVol = new int[] {25,35,50,70,150};
    
    public ItemToolWateringCan(EnumToolTier tier) {
        super(ModItems.mat, Sets.newHashSet());
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "wateringCan_" + tier.getName()));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.tier = tier;
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            ItemNBT.initNBT(stack);
        }
        return 1.0f - stack.getTagCompound().getInteger("Water") / this.waterVol[this.tier.getTierLevel()];
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
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
    public String getUnlocalizedName() {
        return this.getRegistryName().toString();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getRegistryName().toString();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack((Item)this);
            ItemNBT.initNBT(stack);
            if (stack.hasTagCompound()) {
                stack.getTagCompound().setInteger("Water", 0);
            }
            items.add(stack);
        }
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
    public int itemCoolDownTicks() {
        return 18;
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
        return EnumToolCharge.CHARGECAN;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) 
    {
        if (entityLiving instanceof EntityPlayer && this.tier.getTierLevel() != 0) 
        {
            EntityPlayer player = (EntityPlayer)entityLiving;
            ItemStack itemstack = player.getHeldItem(player.getActiveHand());
            int useTimeMulti = (this.getMaxItemUseDuration(stack) - timeLeft) / this.getChargeTime()[0];
            int range = Math.min(useTimeMulti, this.tier.getTierLevel());
            BlockPos pos = player.getPosition().down();
            boolean flag = false;
            if (range == 0) {
                RayTraceResult result = this.rayTrace(world, player, false);
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    this.useOnBlock(player, world, result.getBlockPos(), EnumHand.MAIN_HAND, result.sideHit);
                    return;
                }
            }
            else {
                for (int x = -range; x <= range; ++x) {
                    for (int z = -range; z <= range; ++z) {
                        BlockPos posNew = pos.add(x, 0, z);
                        if (player.canPlayerEdit(posNew.offset(EnumFacing.UP), EnumFacing.DOWN, itemstack) && this.moisten(world, posNew, itemstack, player)) {
                            flag = true;
                        }
                    }
                }
            }
            if (flag) {
                IPlayer capSync = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
                capSync.decreaseRunePoints(player, this.chargeRunes[range]);
                capSync.increaseSkill(EnumSkills.WATER, player, this.levelXP[range]);
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
            return this.useOnBlock(player, worldIn, pos, hand, facing);
        }
        return EnumActionResult.FAIL;
    }
    
    private EnumActionResult useOnBlock(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing) {
        EnumActionResult result = EnumActionResult.FAIL;
        ItemStack stack = player.getHeldItem(hand);
        boolean flag = false;
        boolean used = false;
        if (player.canPlayerEdit(pos.offset(EnumFacing.UP), EnumFacing.DOWN, player.getHeldItem(hand))) {
            if (this.moisten(world, pos, stack, player)) {
                used = true;
                flag = true;
                result = EnumActionResult.SUCCESS;
            }
            else {
                RayTraceResult ray = this.rayTrace(world, player, true);
                if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && world.getBlockState(ray.getBlockPos()).getBlock() == Blocks.WATER) {
                    if (!stack.hasTagCompound()) {
                        ItemNBT.initNBT(stack);
                    }
                    stack.getTagCompound().setInteger("Water", this.waterVol[this.tier.getTierLevel()]);
                    flag = true;
                    world.setBlockState(ray.getBlockPos(), Blocks.AIR.getDefaultState(), 11);
                    player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f);
                    result = EnumActionResult.SUCCESS;
                }
            }
        }
        if (!flag) {
            BlockPos newPos = pos.down();
            if (player.canPlayerEdit(newPos.offset(EnumFacing.UP), EnumFacing.DOWN, player.getHeldItem(hand))) {
                if (this.moisten(world, newPos, stack, player)) {
                    flag = true;
                }
                result = EnumActionResult.SUCCESS;
            }
        }
        if (used) {
            IPlayer capSync = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
            capSync.decreaseRunePoints(player, 1);
            capSync.increaseSkill(EnumSkills.WATER, player, 1);
            capSync.increaseSkill(EnumSkills.FARMING, player, 1);
        }
        return result;
    }
    
    private boolean moisten(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        boolean creative = player.capabilities.isCreativeMode;
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (!stack.hasTagCompound()) {
            ItemNBT.initNBT(stack);
        }
        int water = stack.getTagCompound().getInteger("Water");
        if ((creative || water > 0) && block == ModBlocks.farmland && iblockstate.getValue(BlockFarmland.MOISTURE) == 0) 
        {
            for (int j = 0; j < 4; ++j) 
            {
                world.spawnParticle(EnumParticleTypes.WATER_WAKE, true, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 0.0, 0.01, 0.0, new int[0]);
            }
            world.setBlockState(pos, ModBlocks.farmland.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7), 2);
            world.playSound(null, pos, SoundEvents.ENTITY_BOAT_PADDLE_WATER, SoundCategory.BLOCKS, 1.0f, 1.1f);
            if (!creative) 
            {
                stack.getTagCompound().setInteger("Water", water - 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.<String, AttributeModifier>create();
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation((Item)this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
