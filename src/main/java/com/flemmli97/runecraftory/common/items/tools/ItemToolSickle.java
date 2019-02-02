package com.flemmli97.runecraftory.common.items.tools;

import java.util.Set;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolSickle extends ItemTool implements IItemUsable, IChargeable
{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.YELLOW_FLOWER, Blocks.SAPLING, Blocks.LEAVES, Blocks.LEAVES2, Blocks.TALLGRASS, Blocks.CACTUS, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.DEADBUSH, Blocks.DOUBLE_PLANT, Blocks.MELON_BLOCK, Blocks.PUMPKIN, Blocks.RED_FLOWER, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK, Blocks.REEDS, Blocks.VINE, Blocks.WATERLILY });

    private int[] chargeRunes = new int[] { 1, 5, 15, 50, 100 };
    private int[] levelXP = new int[] { 5, 20, 50, 200, 500 };
    private EnumToolTier tier;
    
    public ItemToolSickle(EnumToolTier tier) {
        super(ModItems.mat, EFFECTIVE_ON);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "sickle_" + tier.getName()));
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
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (!world.isRemote && entityLiving instanceof EntityPlayer && this.getDestroySpeed(stack, state) == this.efficiency) {
            this.levelSkillOnBreak((EntityPlayer)entityLiving);
        }
        return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
    }

    @Override
    public void levelSkillOnHit(EntityPlayer player) {
    }

    @Override
    public void levelSkillOnBreak(EntityPlayer player) {
        IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        cap.decreaseRunePoints(player, this.chargeRunes[0]);
        cap.increaseSkill(EnumSkills.WIND, player, this.tier.getTierLevel() + 1);
        cap.increaseSkill(EnumSkills.FARMING, player, this.tier.getTierLevel() + 1);
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
        return EnumToolCharge.CHARGESICKLE;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer && this.tier.getTierLevel() != 0) {
            ItemStack itemstack = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
            int useTimeMulti = (this.getMaxItemUseDuration(stack) - timeLeft) / this.getChargeTime()[0];
            EntityPlayer player = (EntityPlayer)entityLiving;
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
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.<String, AttributeModifier>create();
    }
}
