package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;

public class ItemEntityLevelUp extends Item {

    public ItemEntityLevelUp(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack itemstack, PlayerEntity player, LivingEntity entity, Hand hand)
    {
        if (entity.world.isRemote)
        {
            return ActionResultType.SUCCESS;
        }
        if(entity instanceof BaseMonster)
        {
            //((BaseMonster)entity)();
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1, 1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}