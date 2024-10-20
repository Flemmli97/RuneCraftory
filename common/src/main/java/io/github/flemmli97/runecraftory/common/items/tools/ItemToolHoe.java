package io.github.flemmli97.runecraftory.common.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
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
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItemToolHoe extends HoeItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;

    public ItemToolHoe(EnumToolTier tier, Item.Properties props) {
        super(ItemTiers.TIER, 0, 0, props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if (this.tier == EnumToolTier.PLATINUM)
            return (int) (DataPackHandler.INSTANCE.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime() * GeneralConfig.platinumChargeTime);
        return DataPackHandler.INSTANCE.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime();
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
    public boolean hasCooldown() {
        return true;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.FARM;
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {
        onHoeUse(player);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration() - remainingUseDuration;
            if (duration > 0 && duration / this.getChargeTime(stack) <= this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
                player.connection.send(new ClientboundSoundPacket(SoundEvents.NOTE_BLOCK_XYLOPHONE, player.getSoundSource(), player.getX(), player.getY(), player.getZ(), 1, 1));
        }
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
        if (this.tier.getTierLevel() != 0 && entity instanceof ServerPlayer player) {
            int useTime = (stack.getUseDuration() - timeLeft - 1) / this.getChargeTime(stack);
            int range = Math.min(useTime, this.tier.getTierLevel());
            BlockHitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
            if (range == 0) {
                if (result != null) {
                    this.useOnBlock(new UseOnContext(player, entity.getUsedItemHand(), result));
                }
            } else {
                BlockPos pos = entity.blockPosition().below();
                if (result != null && result.getType() != HitResult.Type.MISS) {
                    pos = result.getBlockPos();
                }
                Function<BlockPos, BlockHitResult> hit = bh -> new BlockHitResult(Vec3.atCenterOf(bh), Direction.UP, bh, false);
                int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, 0, -range), pos.offset(range, 0, range))
                        .filter(p -> this.hoeBlock(new UseOnContext(player, entity.getUsedItemHand(), hit.apply(p.immutable()))))
                        .count();
                if (amount > 0)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                        LevelCalc.useRP(player, data, 0, true, range * 17.5f, true, EnumSkills.FARMING);
                        LevelCalc.levelSkill(player, data, EnumSkills.FARMING, range * 15);
                        LevelCalc.levelSkill(player, data, EnumSkills.EARTH, range * 2);
                    });
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

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (this.tier.getTierLevel() == 0) {
            return this.useOnBlock(ctx);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult useOnBlock(UseOnContext ctx) {
        if (ctx.getLevel().isClientSide)
            return InteractionResult.PASS;
        if (this.hoeBlock(ctx)) {
            this.onBlockBreak((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean hoeBlock(UseOnContext ctx) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> tille = ItemToolHoe.TILLABLES.get(state.getBlock());
        if (tille != null && tille.getFirst().test(ctx)) {
            tille.getSecond().accept(ctx);
            if (ctx.getLevel().getBlockState(ctx.getClickedPos()).is(Blocks.FARMLAND))
                ctx.getLevel().setBlock(ctx.getClickedPos(), Blocks.FARMLAND.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            ctx.getLevel().playSound(null, ctx.getClickedPos(), SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.1f);
            return true;
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
    }

    public static void onHoeUse(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            LevelCalc.useRP(player, data, 3, true, 0, true, EnumSkills.FARMING, EnumSkills.EARTH);
            LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 3);
            LevelCalc.levelSkill(player, data, EnumSkills.EARTH, 1.5f);
        });
    }
}
