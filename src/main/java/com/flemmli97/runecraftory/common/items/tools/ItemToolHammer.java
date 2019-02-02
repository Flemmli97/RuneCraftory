package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.blocks.BlockMineral;
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
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemToolHammer extends ItemPickaxe implements IItemUsable, IChargeable
{
    private EnumToolTier tier;
    private static AxisAlignedBB farmlandTop = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
    private int[] chargeRunes = new int[] { 1, 5, 15, 50, 100 };
    private int[] levelXP = new int[] { 5, 20, 50, 100, 200 };
    
    public ItemToolHammer(EnumToolTier tier) {
        super(ModItems.mat);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "hammer_" + tier.getName()));
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

    public EnumToolTier getTier() {
        return this.tier;
    }

    @Override
    public int itemCoolDownTicks() {
        return 25;
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
        IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        cap.increaseSkill(EnumSkills.HAMMERAXE, player, 1);
    }

    @Override
    public void levelSkillOnBreak(EntityPlayer player) {
        IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        cap.increaseSkill(EnumSkills.MINING, player, this.tier.getTierLevel() + 1);
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
        return EnumToolCharge.CHARGEUPWEAPON;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) 
    {
        if (entityLiving instanceof EntityPlayer && this.tier.getTierLevel() != 0) {
            ItemStack itemstack = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
            int useTimeMulti = (this.getMaxItemUseDuration(stack) - timeLeft) / this.getChargeTime()[0];
            EntityPlayer player = (EntityPlayer)entityLiving;
            int range = Math.min(useTimeMulti, this.tier.getTierLevel());
            boolean flag = false;
            IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
            if (range == 0) {
                RayTraceResult result = this.rayTrace(world, player, false);
                if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                    this.useOnBlock(player, world, result.getBlockPos(), EnumHand.MAIN_HAND, result.sideHit);
                    return;
                }
            }
            else {
                for (int x = -range; x <= range; ++x) {
                    for (int y = -1; y <= 1; ++y) {
                        for (int z = -range; z <= range; ++z) {
                            BlockPos posNew = player.getPosition().add(x, y, z);
                            if (player.canPlayerEdit(posNew.offset(EnumFacing.UP), EnumFacing.DOWN, itemstack)) {
                                IBlockState iblockstate = world.getBlockState(posNew);
                                Block block = iblockstate.getBlock();
                                if (block == Blocks.FARMLAND || block == ModBlocks.farmland || block instanceof BlockGrassPath) {
                                    if (!(world.getBlockState(posNew.up()).getBlock() instanceof IGrowable)) {
                                        for (int j = 0; j < 4; ++j) {
                                            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, posNew.getX() + 0.5, posNew.getY() + 1.3, posNew.getZ() + 0.5, 0.0, 0.1, 0.0, new int[] { Block.getStateId(Blocks.DIRT.getDefaultState()) });
                                        }
                                        turnToDirt(world, posNew);
                                        world.playSound((EntityPlayer)null, posNew, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 0.5f, 0.1f);
                                        flag = true;
                                    }
                                }
                                else if (block instanceof BlockMineral) {
                                    for (int j = 0; j < 4; ++j) {
                                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, posNew.getX() + 0.5, posNew.getY() + 1.3, posNew.getZ() + 0.5, 0.0, 0.1, 0.0, new int[] { Block.getStateId(Blocks.DIRT.getDefaultState()) });
                                    }
                                    block.removedByPlayer(iblockstate, world, posNew, player, true);
                                    capSync.increaseSkill(EnumSkills.MINING, player, 1 + this.tier.getTierLevel());
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                player.setPosition(player.posX, player.posY + 0.0625, player.posZ);
                capSync.decreaseRunePoints(player, this.chargeRunes[range]);
                capSync.increaseSkill(EnumSkills.EARTH, player, this.levelXP[range]);
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
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.tier.getTierLevel() == 0) {
            return this.useOnBlock(player, world, pos, hand, facing);
        }
        return EnumActionResult.PASS;
    }
    
    private EnumActionResult useOnBlock(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing) {
        ItemStack itemstack = player.getHeldItem(hand);
        EnumActionResult result = EnumActionResult.PASS;
        if (player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
            result = (this.flattenFarm(world, pos) ? EnumActionResult.SUCCESS : result);
            if (result == EnumActionResult.SUCCESS) {
                if (player.getPosition() == pos.up()) {
                    player.setPosition(player.posX, player.posY + 0.0625, player.posZ);
                }
                IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
                capSync.decreaseRunePoints(player, 1);
                capSync.increaseSkill(EnumSkills.EARTH, player, 1);
                capSync.increaseSkill(EnumSkills.MINING, player, 1);
            }
        }
        return result;
    }
    
    private boolean flattenFarm(World world, BlockPos pos) {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if ((block == Blocks.FARMLAND || block == ModBlocks.farmland || block instanceof BlockGrassPath) && !(world.getBlockState(pos.up()).getBlock() instanceof IGrowable)) {
            for (int j = 0; j < 4; ++j) {
                world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5, 0.0, 0.1, 0.0, new int[] { Block.getStateId(Blocks.DIRT.getDefaultState()) });
            }
            turnToDirt(world, pos);
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 0.1f);
            return true;
        }
        return false;
    }
    
    private static void turnToDirt(World world, BlockPos pos) {
        AxisAlignedBB axisalignedbb = ItemToolHammer.farmlandTop.offset(pos);
        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb)) {
            double d0 = Math.min(axisalignedbb.maxY - axisalignedbb.minY, axisalignedbb.maxY - entity.getEntityBoundingBox().minY);
            entity.setPositionAndUpdate(entity.posX, entity.posY + d0 + 0.001, entity.posZ);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.<String, AttributeModifier>create();
    }
}
