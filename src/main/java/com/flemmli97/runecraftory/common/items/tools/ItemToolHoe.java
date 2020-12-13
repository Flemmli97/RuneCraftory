package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.items.EnumToolCharge;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.flemmli97.runecraftory.lib.enums.EnumToolTier;
import com.flemmli97.runecraftory.lib.enums.EnumWeaponType;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

public class ItemToolHoe extends HoeItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;

    public ItemToolHoe(EnumToolTier tier, Properties props) {
        super(ItemTiers.tier, 0, 0, props);
        this.tier = tier;
    }

    @Override
    public int[] getChargeTime() {
        return new int[0];
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPTOOL;
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

    /*
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof PlayerEntity && this.tier.getTierLevel() != 0)
        {
            ItemStack itemstack = entityLiving.getHeldItem(Hand.MAIN_HAND);
            int useTimeMulti = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime()[0];
            PlayerEntity player = (PlayerEntity)entityLiving;
            int range = Math.min(useTimeMulti, this.tier.getTierLevel());
            BlockPos pos = player.getBlockPos().down();
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (handIn == EnumHand.MAIN_HAND && this.tier.getTierLevel() != 0) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

    @Override
    public EnumActionResult onItemUse(PlayerEntity player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.tier.getTierLevel() == 0) {
            return useOnBlock(player, worldIn, pos, player.getHeldItem(hand), facing);
        }
        return EnumActionResult.PASS;
    }

    public static EnumActionResult useOnBlock(PlayerEntity player, World worldIn, BlockPos pos, ItemStack itemstack, EnumFacing facing) {
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

    public static void setBlockHoeUse(ItemStack stack, PlayerEntity player, World worldIn, BlockPos pos, BlockState state) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, (LivingEntity) player);
        }
    }*/

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
