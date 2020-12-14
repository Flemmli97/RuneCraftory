package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.EnumToolCharge;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.UseAction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemToolSickle extends ToolItem implements IItemUsable, IChargeable {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[]{Blocks.CACTUS, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.PUMPKIN, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK, Blocks.VINE});

    private int[] chargeRunes = new int[]{1, 5, 15, 50, 100};
    private int[] levelXP = new int[]{5, 20, 50, 200, 500};
    public final EnumToolTier tier;

    public ItemToolSickle(EnumToolTier tier, Properties props) {
        super(0, 0, ItemTiers.tier, EFFECTIVE_ON, props);
        this.tier = tier;
    }

    @Override
    public int[] getChargeTime() {
        return new int[0];
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGESICKLE;
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
        /*
        IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        cap.decreaseRunePoints(player, this.chargeRunes[0]);
        cap.increaseSkill(EnumSkills.WIND, player, this.tier.getTierLevel() + 1);
        cap.increaseSkill(EnumSkills.FARMING, player, this.tier.getTierLevel() + 1);
         */
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
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.WART_BLOCKS))
            return efficiency;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!world.isRemote && entityLiving instanceof PlayerEntity && this.getDestroySpeed(stack, state) == this.efficiency) {
            this.onBlockBreak((PlayerEntity) entityLiving);
        }
        return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
    }

    /*
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer && this.tier.getTierLevel() != 0) {
            ItemStack itemstack = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
            int useTimeMulti = (this.getMaxItemUseDuration(stack) - timeLeft) / this.getChargeTime()[0];
            EntityPlayer player = (EntityPlayer) entityLiving;
            int range = Math.min(useTimeMulti, this.tier.getTierLevel());
            BlockPos pos = player.getPosition();
            boolean flag = false;
            for (int x = -range; x <= range; ++x) {
                for (int z = -range; z <= range; ++z) {
                    BlockPos posNew = pos.add(x, 0, z);
                    if (player.canPlayerEdit(posNew.offset(EnumFacing.UP), EnumFacing.DOWN, itemstack)) {
                        IBlockState iblockstate = worldIn.getBlockState(posNew);
                        Block block = iblockstate.getBlock();
                        if (ItemToolSickle.EFFECTIVE_ON.contains(block)) {
                            worldIn.destroyBlock(posNew, true);
                            flag = true;
                        }
                    }
                }
            }
            if (flag) {
                IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
                capSync.decreaseRunePoints(player, this.chargeRunes[range]);
                capSync.increaseSkill(EnumSkills.WIND, player, this.levelXP[range]);
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
            EnumActionResult result = EnumActionResult.FAIL;
            if (player.canPlayerEdit(pos.offset(EnumFacing.UP), EnumFacing.DOWN, player.getHeldItem(hand))) {
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();
                if (ItemToolSickle.EFFECTIVE_ON.contains(block)) {
                    worldIn.destroyBlock(pos, true);
                    result = EnumActionResult.SUCCESS;
                }
            }
            if (result == EnumActionResult.SUCCESS) {
                IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
                capSync.decreaseRunePoints(player, 1);
                capSync.increaseSkill(EnumSkills.WIND, player, 1);
                capSync.increaseSkill(EnumSkills.FARMING, player, 1);
            }
            return result;
        }
        return EnumActionResult.FAIL;
    }*/

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
