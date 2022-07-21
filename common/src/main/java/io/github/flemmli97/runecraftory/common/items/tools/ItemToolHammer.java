package io.github.flemmli97.runecraftory.common.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

public class ItemToolHammer extends PickaxeItem implements IItemUsable, IChargeable {

    private static AABB farmlandTop = new AABB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
    public final EnumToolTier tier;
    private int[] chargeRunes = new int[]{1, 5, 15, 50, 100};

    public ItemToolHammer(EnumToolTier tier, Properties props) {
        super(ItemTiers.tier, 0, 0, props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if (this.tier == EnumToolTier.PLATINUM)
            return (int) (GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime() * GeneralConfig.platinumChargeTime);
        return GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime();
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        return this.tier.getTierLevel();
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
    public void onEntityHit(ServerPlayer player, ItemStack stack) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.HAMMERAXE, 0.5f));
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            LevelCalc.useRP(player, data, 7, true, false, true, 1, EnumSkills.MINING);
            LevelCalc.levelSkill(player, data, EnumSkills.MINING, this.tier.getTierLevel() * 0.5f + 1);
        });
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        int duration = stack.getUseDuration() - remainingUseDuration;
        if (duration != 0 && duration / this.getChargeTime(stack) <= this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
            livingEntity.playSound(SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (this.tier.getTierLevel() == 0) {
            return this.useOnBlock(ctx, false);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.tier.getTierLevel() != 0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (this.tier.getTierLevel() != 0 && !world.isClientSide) {
            int useTime = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime(stack);
            int range = Math.min(useTime, this.tier.getTierLevel());
            BlockPos pos = entity.blockPosition();
            if (range == 0) {
                if (entity instanceof Player player) {
                    BlockHitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
                    if (result != null) {
                        this.useOnBlock(new UseOnContext(player, entity.getUsedItemHand(), result), false);
                        return;
                    }
                }
            } else {
                int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, -1, -range), pos.offset(range, 0, range))
                        .filter(p -> this.hammer((ServerLevel) world, p, stack, entity, true))
                        .count();
                if (entity instanceof ServerPlayer player && amount > 0)
                    Platform.INSTANCE.getPlayerData(player)
                            .ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.MINING, (this.tier.getTierLevel() * 0.5f + 1) * amount * 0.7f));
            }
        }
        super.releaseUsing(stack, world, entity, timeLeft);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.tier == EnumToolTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    private InteractionResult useOnBlock(UseOnContext ctx, boolean canHammer) {
        if (ctx.getLevel().isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = ctx.getItemInHand();
        if (this.hammer((ServerLevel) ctx.getLevel(), ctx.getClickedPos(), stack, ctx.getPlayer(), canHammer)) {
            this.onBlockBreak((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean hammer(ServerLevel world, BlockPos pos, ItemStack stack, LivingEntity entity, boolean canHammer) {
        if (entity instanceof Player && !((Player) entity).mayUseItemAt(pos.relative(Direction.UP), Direction.UP, stack))
            return false;
        BlockState state = world.getBlockState(pos);
        if (canHammer && state.is(ModTags.hammerBreakable)) {
            if (entity instanceof ServerPlayer serverPlayer) {
                if (((ServerPlayer) entity).gameMode.destroyBlock(pos)) {
                    world.levelEvent(2001, pos, Block.getId(state));
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, world.getBlockState(pos)));
                    return true;
                }
            } else {
                return world.destroyBlock(pos, true, entity, 3);
            }
        } else if (state.is(ModTags.hammerFlattenable) && world.getBlockState(pos.above()).getMaterial() == Material.AIR) {
            if (world.setBlockAndUpdate(pos, Block.pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), world, pos))) {
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
