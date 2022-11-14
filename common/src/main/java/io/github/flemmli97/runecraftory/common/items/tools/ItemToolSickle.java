package io.github.flemmli97.runecraftory.common.items.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
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
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
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

import java.util.Set;

public class ItemToolSickle extends DiggerItem implements IItemUsable, IChargeable {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CACTUS, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.PUMPKIN, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK, Blocks.VINE);

    public final EnumToolTier tier;

    public ItemToolSickle(EnumToolTier tier, Item.Properties props) {
        super(0, 0, ItemTiers.tier, ModTags.sickleEffective, props);
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
        return EnumToolCharge.CHARGESICKLE;
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
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> LevelCalc.levelSkill(player, data, EnumSkills.WIND, 0.5f));
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            LevelCalc.useRP(player, data, 2, true, false, true, EnumSkills.FARMING, EnumSkills.WIND);
            LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 3);
            LevelCalc.levelSkill(player, data, EnumSkills.WIND, 0.5f);
        });
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(BlockTags.LEAVES) || state.is(BlockTags.WART_BLOCKS))
            return this.speed;
        //if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return efficiency;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayer serverPlayer && this.getDestroySpeed(stack, state) == this.speed) {
            Platform.INSTANCE.getPlayerData(serverPlayer).ifPresent(data -> {
                LevelCalc.levelSkill(serverPlayer, data, EnumSkills.FARMING, 3);
                LevelCalc.levelSkill(serverPlayer, data, EnumSkills.WIND, 0.5f);
            });
        }
        return super.mineBlock(stack, level, state, pos, entityLiving);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return ImmutableMultimap.of();
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
            return this.useOnBlock(ctx);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (this.tier.getTierLevel() != 0) {
            player.startUsingItem(usedHand);
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
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (this.tier.getTierLevel() != 0 && entity instanceof ServerPlayer player) {
            int useTime = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime(stack);
            int range = Math.min(useTime, this.tier.getTierLevel()) + 2;
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (range == 0) {
                if (result != null) {
                    this.useOnBlock(new UseOnContext(player, entity.getUsedItemHand(), result));
                }
            } else {
                BlockPos pos = entity.blockPosition();
                if (result != null && result.getType() != HitResult.Type.MISS) {
                    pos = result.getBlockPos();
                }
                int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, 0, -range), pos.offset(range, 0, range))
                        .filter(p -> this.sickleUse(player.getLevel(), p.immutable(), stack, entity))
                        .count();
                if (amount > 0)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                        LevelCalc.useRP(player, data, range * 10, true, false, true, EnumSkills.FARMING);
                        LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 3.5f);
                        LevelCalc.levelSkill(player, data, EnumSkills.WIND, 0.8f);
                    });
            }
        }
        super.releaseUsing(stack, level, entity, timeLeft);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.tier == EnumToolTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    private InteractionResult useOnBlock(UseOnContext ctx) {
        if (!(ctx.getLevel() instanceof ServerLevel serverLevel))
            return InteractionResult.PASS;
        ItemStack stack = ctx.getItemInHand();
        if (this.sickleUse(serverLevel, ctx.getClickedPos(), stack, ctx.getPlayer())) {
            this.onBlockBreak((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean sickleUse(ServerLevel level, BlockPos pos, ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.mayUseItemAt(pos.relative(Direction.UP), Direction.UP, stack))
            return false;
        BlockState state = level.getBlockState(pos);
        if (state.is(ModTags.sickleDestroyable)) {
            if (entity instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.gameMode.destroyBlock(pos)) {
                    level.levelEvent(2001, pos, Block.getId(state));
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));
                    return true;
                }
            } else {
                return level.destroyBlock(pos, true, entity, 3);
            }
        }
        return false;
    }
}
