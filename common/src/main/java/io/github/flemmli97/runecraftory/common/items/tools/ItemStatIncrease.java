package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.lib.LibConstants;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ItemStatIncrease extends Item {

    private final Stat stat;

    public ItemStatIncrease(Stat stat, Properties properties) {
        super(properties);
        this.stat = stat;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entityLiving) {
        boolean shrink = true;
        if (entityLiving instanceof ServerPlayer serverPlayer) {
            level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            this.increaseStat(stack, level, serverPlayer);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            if (serverPlayer.isCreative())
                shrink = false;
        }
        if (shrink)
            stack.shrink(1);
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    private void increaseStat(ItemStack stack, Level level, Player player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            switch (this.stat) {
                case HP -> {
                    AttributeInstance health = player.getAttribute(Attributes.MAX_HEALTH);
                    AttributeModifier modifier = health.getModifier(LibConstants.maxHealthItemIncrease);
                    double val = modifier == null ? 0 : modifier.getAmount();
                    health.removeModifier(LibConstants.maxHealthItemIncrease);
                    health.addPermanentModifier(new AttributeModifier(LibConstants.maxHealthModifier, "rf.item.hpModifier", val + 10, AttributeModifier.Operation.ADDITION));
                }
                case LEVEL -> data.addXp(player, LevelCalc.xpAmountForLevelUp(data.getPlayerLevel().getLevel()) - data.getPlayerLevel().getXp());
                case STR, INT, VIT -> data.consumeStatBoostItem(player, this.stat);
            }
        });
    }

    public enum Stat {
        LEVEL,
        HP,
        STR,
        INT,
        VIT
    }
}
