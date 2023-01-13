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
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

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
    public void onBlockBreak(ServerPlayer player) {
    }

    public static void onHammering(ServerPlayer player, boolean level) {
        if (getUseRPFlag(player.getMainHandItem()))
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                LevelCalc.useRP(player, data, 5, true, false, true, EnumSkills.MINING);
                if (level)
                    LevelCalc.levelSkill(player, data, EnumSkills.MINING, 10);
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
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (this.tier.getTierLevel() != 0 && entity instanceof ServerPlayer player) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                int useTime = data.getWeaponHandler().getCurrentToolCharge() > 0 ? data.getWeaponHandler().getCurrentToolCharge() : ((this.getUseDuration(stack) - timeLeft) / this.getChargeTime(stack));
                int range = Math.min(useTime, this.tier.getTierLevel());
                BlockHitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
                if (range == 0) {
                    if (result != null) {
                        this.useOnBlock(new UseOnContext((Player) entity, entity.getUsedItemHand(), result), false);
                    }
                } else {
                    setDontUseRPFlagTemp(stack, true);
                    BlockPos pos = entity.blockPosition();
                    if (result != null && result.getType() != HitResult.Type.MISS) {
                        pos = result.getBlockPos();
                    }
                    data.getWeaponHandler().useAxeOrHammer(stack, range);
                    int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, -1, -range), pos.offset(range, 0, range))
                            .filter(p -> this.hammer((ServerLevel) world, p.immutable(), stack, entity, true) != HammerState.FAIL)
                            .count();
                    if (amount > 0) {
                        LevelCalc.useRP(player, data, range * 15, true, false, true, EnumSkills.MINING);
                        LevelCalc.levelSkill(player, data, EnumSkills.MINING, (range + 1) * 10);
                    }
                    setDontUseRPFlagTemp(stack, false);
                }
            });
        }
        super.releaseUsing(stack, world, entity, timeLeft);
    }

    private static void setDontUseRPFlagTemp(ItemStack stack, boolean flag) {
        if (flag) {
            stack.getOrCreateTag().putBoolean("RFInUseFlag", true);
        } else {
            stack.getOrCreateTag().remove("RFInUseFlag");
            if (stack.getTag().isEmpty())
                stack.setTag(null);
        }
    }

    private static boolean getUseRPFlag(ItemStack stack) {
        if (stack.hasTag()) {
            return !stack.getOrCreateTag().getBoolean("RFInUseFlag");
        }
        return true;
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
        if (!(ctx.getPlayer() instanceof ServerPlayer player))
            return InteractionResult.PASS;
        ItemStack stack = ctx.getItemInHand();
        HammerState state = this.hammer((ServerLevel) ctx.getLevel(), ctx.getClickedPos(), stack, ctx.getPlayer(), canHammer);
        if (state != HammerState.FAIL) {
            setDontUseRPFlagTemp(stack, false);
            onHammering(player, state == HammerState.BREAK);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private HammerState hammer(ServerLevel world, BlockPos pos, ItemStack stack, LivingEntity entity, boolean canHammer) {
        if (entity instanceof Player && !((Player) entity).mayUseItemAt(pos.relative(Direction.UP), Direction.UP, stack))
            return HammerState.FAIL;
        BlockState state = world.getBlockState(pos);
        if (canHammer && state.is(ModTags.HAMMER_BREAKABLE)) {
            if (entity instanceof ServerPlayer serverPlayer) {
                if (((ServerPlayer) entity).gameMode.destroyBlock(pos)) {
                    world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, world.getBlockState(pos)));
                    return HammerState.BREAK;
                }
            } else {
                return world.destroyBlock(pos, true, entity, 3) ? HammerState.BREAK : HammerState.FAIL;
            }
        } else if (state.is(ModTags.HAMMER_FLATTENABLE) && world.getBlockState(pos.above()).getMaterial() == Material.AIR) {
            if (world.setBlockAndUpdate(pos, Block.pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), world, pos))) {
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1, 1);
                return HammerState.FLATTEN;
            }
        }
        return HammerState.FAIL;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
    }

    enum HammerState {
        FAIL,
        BREAK,
        FLATTEN
    }
}
