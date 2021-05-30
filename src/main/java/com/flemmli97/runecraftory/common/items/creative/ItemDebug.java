package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.world.GateSpawning;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import java.util.Set;

public class ItemDebug extends Item {

    public ItemDebug(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand handIn) {
        if (!world.isRemote) {
            long time = System.nanoTime();
            Set<Structure<?>> structures = GateSpawning.getStructuresAt((ServerWorld) world, player.getBlockPos());
            long delta = System.nanoTime() - time;
            player.sendMessage(new StringTextComponent("" + structures), Util.NIL_UUID);
            player.sendMessage(new StringTextComponent("check time " + delta), Util.NIL_UUID);

            long time2 = System.nanoTime();
            boolean hasSpawns = GateSpawning.hasStructureSpawns((ServerWorld) world, player.getBlockPos());
            long delta2 = System.nanoTime() - time2;
            player.sendMessage(new StringTextComponent("" + hasSpawns), Util.NIL_UUID);
            player.sendMessage(new StringTextComponent("time " + delta2), Util.NIL_UUID);
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