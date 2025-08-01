package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.entities.misc.CustomFishingHookEntity;
import io.github.flemmli97.runecraftory.common.items.ToolItemTier;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemComponentUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemToolFishingRod extends FishingRodItem {

    public ItemToolFishingRod(Properties props) {
        super(props);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration(entity) - remainingUseDuration;
            ToolItemTier tier = stack.getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
            int chargeTime = ItemComponentUtils.getChargeTime(entity, tier);
            if (duration > 0 && duration / chargeTime <= tier.getTierLevel() && duration % chargeTime == 0)
                EntityUtils.playSoundForPlayer(player, SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ToolItemTier tier = stack.getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() != 0 && Platform.INSTANCE.getEntityData(player).fishingHook == null) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        this.throwRod(level, player, stack, 0);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        ToolItemTier tier = stack.getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() != 0) {
            int useTime = (stack.getUseDuration(entity) - timeLeft - 1) / ItemComponentUtils.getChargeTime(entity, tier);
            int charge = Math.min(useTime, tier.getTierLevel());
            this.throwRod(level, entity, stack, charge);
            entity.swing(entity.getUsedItemHand());
        }
        super.releaseUsing(stack, level, entity, timeLeft);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    protected void throwRod(Level level, LivingEntity entity, ItemStack stack, int charge) {
        CustomFishingHookEntity hook = Platform.INSTANCE.getEntityData(entity).fishingHook;
        if (hook != null) {
            if (!level.isClientSide) {
                hook.retract(stack);
            }
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
            entity.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (level.getRandom().nextFloat() * 0.4f + 0.8f));
            if (level instanceof ServerLevel serverLevel) {
                float speed = EnchantmentHelper.getFishingTimeReduction(serverLevel, stack, entity);
                int luck = EnchantmentHelper.getFishingLuckBonus(serverLevel, stack, entity);
                ToolItemTier tier = stack.getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
                hook = new CustomFishingHookEntity(level, entity, speed + tier.getTierLevel(), luck, charge);
                hook.setElement(ItemComponentUtils.getElement(stack));
                if (entity instanceof Player player)
                    hook.attackHandlingPlayer(() -> player.getCooldowns().getCooldownPercent(stack.getItem(), 0.0f) <= 0, () -> player.getCooldowns().addCooldown(stack.getItem(), Mth.ceil(20 * EntityUtils.attackSpeedModifier(player))));
                level.addFreshEntity(hook);
            }
            if (entity instanceof Player player)
                player.awardStat(Stats.ITEM_USED.get(this));
            entity.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
    }
}
