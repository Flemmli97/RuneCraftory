package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemToolHammer extends PickaxeItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;
    private static AxisAlignedBB farmlandTop = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
    private int[] chargeRunes = new int[]{1, 5, 15, 50, 100};
    private int[] levelXP = new int[]{5, 20, 50, 100, 200};

    public ItemToolHammer(EnumToolTier tier, Properties props) {
        super(ItemTiers.tier, 0, 0, props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if(this.tier == EnumToolTier.PLATINUM)
            return (int) (GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime() * GeneralConfig.platinumChargeTime);
        return GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime();
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        if(this.tier == EnumToolTier.PLATINUM)
            return this.tier.getTierLevel();
        return this.tier.getTierLevel()+1;
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
        return GeneralConfig.weaponProps.get(this.getWeaponType()).cooldown();
    }

    @Override
    public void onEntityHit(ServerPlayerEntity player) {
        player.getCapability(PlayerCapProvider.PlayerCap)
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.HAMMERAXE, 0.5f));
    }

    @Override
    public void onBlockBreak(ServerPlayerEntity player) {
        player.getCapability(PlayerCapProvider.PlayerCap)
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.MINING, this.tier.getTierLevel() + 1));
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
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        int duration = stack.getUseDuration() - count;
        if (duration != 0 && duration / this.getChargeTime(stack) < this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    /*
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity && this.tier.getTierLevel() != 0) {
            ItemStack itemstack = entityLiving.getHeldItem(Hand.MAIN_HAND);
            int useTimeMulti = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime()[0];
            PlayerEntity player = (PlayerEntity) entityLiving;
            int range = Math.min(useTimeMulti, this.tier.getTierLevel());
            boolean flag = false;
            IPlayerCap capSync = player.getCapability(PlayerCapProvider.PlayerCap).orElseThrow(()->new NullPointerException("Error getting capability"));
            if (range == 0) {
                BlockRayTraceResult result = rayTrace(world, player, RayTraceContext.FluidMode.NONE);
                if (result != null) {
                    this.useOnBlock(player, world, result.getPos(), Hand.MAIN_HAND, result.getFace());
                    return;
                }
            } else {
                for (int x = -range; x <= range; ++x) {
                    for (int y = -1; y <= 1; ++y) {
                        for (int z = -range; z <= range; ++z) {
                            BlockPos posNew = player.getBlockPos().add(x, y, z);
                            if (player.canPlayerEdit(posNew.offset(Direction.UP), Direction.DOWN, itemstack)) {
                                BlockState iblockstate = world.getBlockState(posNew);
                                Block block = iblockstate.getBlock();
                                if (block == Blocks.FARMLAND || block == ModBlocks.farmland || block instanceof GrassPathBlock) {
                                    if (!(world.getBlockState(posNew.up()).getBlock() instanceof IGrowable)) {
                                        for (int j = 0; j < 4; ++j) {
                                            world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, posNew.getX() + 0.5, posNew.getY() + 1.3, posNew.getZ() + 0.5, 0.0, 0.1, 0.0, new int[]{Block.getStateId(Blocks.DIRT.getDefaultState())});
                                        }
                                        turnToDirt(world, posNew);
                                        world.playSound(null, posNew, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 0.5f, 0.1f);
                                        flag = true;
                                    }
                                } else if (block instanceof BlockMineral) {
                                    for (int j = 0; j < 4; ++j) {
                                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, posNew.getX() + 0.5, posNew.getY() + 1.3, posNew.getZ() + 0.5, 0.0, 0.1, 0.0, new int[]{Block.getStateId(Blocks.DIRT.getDefaultState())});
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
                player.setPosition(player.getX(), player.getY() + 0.0625, player.getZ());
                capSync.decreaseRunePoints(player, this.chargeRunes[range], true);
                capSync.increaseSkill(EnumSkills.EARTH, player, this.levelXP[range]);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (handIn == Hand.MAIN_HAND && this.tier.getTierLevel() != 0) {
            playerIn.setActiveHand(handIn);
            return ActionResult.success(itemstack);
        }
        return ActionResult.fail(itemstack);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        if (this.tier.getTierLevel() == 0) {
            return this.useOnBlock(player, world, pos, hand, facing);
        }
        return ActionResultType.PASS;
    }

    private EnumActionResult useOnBlock(EntityPlayer player, World world, BlockPos pos, Hand hand, Direction facing) {
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
        BlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if ((block == Blocks.FARMLAND || block == ModBlocks.farmland || block instanceof BlockGrassPath) && !(world.getBlockState(pos.up()).getBlock() instanceof IGrowable)) {
            for (int j = 0; j < 4; ++j) {
                world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, pos.getX() + 0.5, pos.getY() + 1.3, pos.getZ() + 0.5, 0.0, 0.1, 0.0, new int[]{Block.getStateId(Blocks.DIRT.getDefaultState())});
            }
            turnToDirt(world, pos);
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1.0f, 0.1f);
            return true;
        }
        return false;
    }*/

    private static void turnToDirt(World world, BlockPos pos) {
        AxisAlignedBB axisalignedbb = ItemToolHammer.farmlandTop.offset(pos);
        world.setBlockState(pos, Blocks.DIRT.getDefaultState());
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb)) {
            double d0 = Math.min(axisalignedbb.maxY - axisalignedbb.minY, axisalignedbb.maxY - entity.getBoundingBox().minY);
            entity.setPositionAndUpdate(entity.getX(), entity.getY() + d0 + 0.001, entity.getZ());
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
