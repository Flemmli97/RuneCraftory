package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemLevelUp extends Item {

    public ItemLevelUp(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap ->
                    cap.addXp(player, LevelCalc.xpAmountForLevelUp(cap.getPlayerLevel()[0]) - cap.getPlayerLevel()[1]));
        }
        return ActionResult.success(player.getHeldItem(hand));
    }
}
