package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemEntityLevelUp extends Item {

    public ItemEntityLevelUp(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemstack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity.level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (entity instanceof BaseMonster) {
            //((BaseMonster)entity)();
            player.playSound(SoundEvents.PLAYER_LEVELUP, 1, 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}