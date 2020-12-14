package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.EnumToolCharge;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.UseAction;

public class ItemToolWateringCan extends ToolItem implements IItemUsable, IChargeable {
    private EnumToolTier tier;
    private int[] levelXP = new int[]{5, 20, 50, 200, 500};
    private int[] chargeRunes = new int[]{1, 5, 15, 50, 100};
    private final int[] waterVol = new int[]{25, 35, 50, 70, 150};

    public ItemToolWateringCan(EnumToolTier tier, Item.Properties props) {
        super(0, 0, ItemTiers.tier, Sets.newHashSet(), props);
        this.tier = tier;
    }

    @Override
    public int[] getChargeTime() {
        return new int[0];
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGECAN;
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
    public void onEntityHit(PlayerEntity player) {

    }

    @Override
    public void onBlockBreak(PlayerEntity player) {

    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (!stack.hasTag()) {
            ItemNBT.initNBT(stack, true);
        }
        return 1.0f - stack.getTag().getInt("Water") / this.waterVol[this.tier.getTierLevel()];
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
/*
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer && this.tier.getTierLevel() != 0) {
            EntityPlayer player = (EntityPlayer) entityLiving;
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
            } else {
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
                IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
                capSync.decreaseRunePoints(player, this.chargeRunes[range]);
                capSync.increaseSkill(EnumSkills.WATER, player, this.levelXP[range]);
                capSync.increaseSkill(EnumSkills.FARMING, player, this.levelXP[range]);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        RayTraceResult ray = this.rayTrace(world, player, true);
        ItemStack itemstack = player.getHeldItem(hand);
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && world.getBlockState(ray.getBlockPos()).getBlock() == Blocks.WATER && !this.hasFullWater(itemstack)) {
            if (!itemstack.hasTagCompound()) {
                ItemNBT.initNBT(itemstack);
            }
            itemstack.getTagCompound().setInteger("Water", this.waterVol[this.tier.getTierLevel()]);
            world.setBlockState(ray.getBlockPos(), Blocks.AIR.getDefaultState(), 3);
            player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        if (hand == EnumHand.MAIN_HAND && this.tier.getTierLevel() != 0) {
            player.setActiveHand(hand);
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
            IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
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
        if ((creative || water > 0) && block == ModBlocks.farmland && iblockstate.getValue(BlockFarmland.MOISTURE) == 0) {
            for (int j = 0; j < 4; ++j) {
                world.spawnParticle(EnumParticleTypes.WATER_WAKE, true, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 0.0, 0.01, 0.0, new int[0]);
            }
            world.setBlockState(pos, ModBlocks.farmland.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7), 2);
            world.playSound(null, pos, SoundEvents.ENTITY_BOAT_PADDLE_WATER, SoundCategory.BLOCKS, 1.0f, 1.1f);
            if (!creative) {
                stack.getTagCompound().setInteger("Water", water - 1);
            }
            return true;
        }
        return false;
    }

    private boolean hasFullWater(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            ItemNBT.initNBT(stack);
        }
        return stack.getTagCompound().getInteger("Water") == this.waterVol[this.tier.getTierLevel()];
    }*/

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
