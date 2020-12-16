package com.flemmli97.runecraftory.common.items.creative;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemDebug extends Item {

    public ItemDebug(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        if(!world.isRemote)
        {
            /*ItemStack stack = new ItemStack(ModItems.recipe);
            //IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("Recipe", CraftingHandler.randomRecipeToExclude(EnumCrafting.FORGE, 0, 100));
            stack.setItemDamage(EnumCrafting.FORGE.getID());
            ItemUtils.spawnItemAtEntity(player, stack);*/
        }
        return super.onItemRightClick(world, player, handIn);
    }
}