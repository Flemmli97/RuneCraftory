package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCustomFishingHook;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemToolFishingRod extends FishingRodItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;

    public ItemToolFishingRod(EnumToolTier tier, Properties props) {
        super(props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if (this.tier == EnumToolTier.PLATINUM)
            return (int) (DataPackHandler.SERVER_PACK.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime() * GeneralConfig.platinumChargeTime);
        return DataPackHandler.SERVER_PACK.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime();
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        if (this.tier == EnumToolTier.PLATINUM)
            return this.tier.getTierLevel();
        return this.tier.getTierLevel() + 1;
    }

    @Override
    public boolean hasCooldown() {
        return true;
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEFISHING;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.FARM;
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {

    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        int duration = stack.getUseDuration() - remainingUseDuration;
        if (duration != 0 && duration / this.getChargeTime(stack) < this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
            livingEntity.playSound(SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.tier.getTierLevel() != 0 && Platform.INSTANCE.getEntityData(player).map(d -> d.fishingHook == null).orElse(false)) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        this.throwRod(world, player, itemstack, 0);
        return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
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
        if (this.tier.getTierLevel() != 0) {
            int useTime = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime(stack);
            int charge = Math.min(useTime, this.tier.getTierLevel());
            this.throwRod(world, entity, stack, charge);
            entity.swing(entity.getUsedItemHand());
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

    protected void throwRod(Level level, LivingEntity entity, ItemStack itemStack, int charge) {
        EntityCustomFishingHook hook = Platform.INSTANCE.getEntityData(entity).map(d -> d.fishingHook).orElse(null);
        if (hook != null) {
            if (!level.isClientSide) {
                hook.retract(itemStack);
            }
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
            level.gameEvent(entity, GameEvent.FISHING_ROD_REEL_IN, entity);
        } else {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!level.isClientSide) {
                int speed = EnchantmentHelper.getFishingSpeedBonus(itemStack);
                int luck = EnchantmentHelper.getFishingLuckBonus(itemStack);
                hook = new EntityCustomFishingHook(level, entity, speed + this.tier.getTierLevel(), luck, charge);
                hook.setElement(ItemNBT.getElement(itemStack));
                if (entity instanceof Player player)
                    hook.attackHandlingPlayer(() -> player.getCooldowns().getCooldownPercent(itemStack.getItem(), 0.0f) <= 0, () -> player.getCooldowns().addCooldown(itemStack.getItem(), Mth.ceil(EnumWeaponType.FARM.defaultWeaponSpeed * ItemNBT.attackSpeedModifier(player))));
                level.addFreshEntity(hook);
            }
            if (entity instanceof Player player)
                player.awardStat(Stats.ITEM_USED.get(this));
            level.gameEvent(entity, GameEvent.FISHING_ROD_CAST, entity);
        }
    }
}
