package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemStatIncrease extends Item {

    private final Stat stat;

    public ItemStatIncrease(Stat stat, Properties p_i48487_1_) {
        super(p_i48487_1_);
        this.stat = stat;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity entityplayer = (PlayerEntity) entityLiving;
            worldIn.playSound(null, entityplayer.getX(), entityplayer.getY(), entityplayer.getZ(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
            this.increaseStat(stack, worldIn, entityplayer);
            entityplayer.addStat(Stats.ITEM_USED.get(this));

            if (entityplayer instanceof ServerPlayerEntity) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) entityplayer, stack);
            }
        }

        stack.shrink(1);
        return stack;
    }

    private void increaseStat(ItemStack stack, World worldIn, PlayerEntity player) {
        player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {
            switch (this.stat) {
                case HP:
                    cap.setMaxHealth(player, cap.getMaxHealth(player) + 10);
                    break;
                case INT:
                    cap.setIntel(player, cap.getIntel() + 1);
                    break;
                case LEVEL:
                    cap.addXp(player, LevelCalc.xpAmountForLevelUp(cap.getPlayerLevel()[0]) - cap.getPlayerLevel()[1]);
                    break;
                case STR:
                    cap.setStr(player, cap.getStr() + 1);
                    break;
                case VIT:
                    cap.setVit(player, cap.getVit() + 1);
                    break;
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
