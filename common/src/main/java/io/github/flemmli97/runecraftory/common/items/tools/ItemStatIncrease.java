package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
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
            level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), RuneCraftorySounds.GENERIC_ITEM_STAT_CONSUME.get(), SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            this.increaseStat(serverPlayer);
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
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    private void increaseStat(Player player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        switch (this.stat) {
            case LEVEL ->
                    data.addXp(LevelCalc.xpAmountForLevelUp(data.getPlayerLevel().getLevel()) - data.getPlayerLevel().getXp());
            case STR, INT, VIT, HP -> data.increaseStatBonus(this.stat);
        }
    }

    public enum Stat {
        LEVEL,
        HP,
        STR,
        INT,
        VIT
    }
}
