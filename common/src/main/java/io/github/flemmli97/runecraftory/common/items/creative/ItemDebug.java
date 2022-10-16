package io.github.flemmli97.runecraftory.common.items.creative;

import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ItemDebug extends Item {

    public ItemDebug(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand handIn) {
        if (level instanceof ServerLevel serverLevel) {
            /*long time = System.nanoTime();
            Set<ConfiguredStructureFeature<?, ?>> structures = GateSpawning.getStructuresAt(serverLevel, player.blockPosition());
            long delta = System.nanoTime() - time;
            player.sendMessage(new TextComponent("" + structures), Util.NIL_UUID);
            player.sendMessage(new TextComponent("check time " + delta), Util.NIL_UUID);

            long time2 = System.nanoTime();
            boolean hasSpawns = GateSpawning.hasStructureSpawns(serverLevel, player.blockPosition());
            long delta2 = System.nanoTime() - time2;
            player.sendMessage(new TextComponent("" + hasSpawns), Util.NIL_UUID);
            player.sendMessage(new TextComponent("time " + delta2), Util.NIL_UUID);
            /*ItemStack stack = new ItemStack(ModItems.recipe);
            //IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("Recipe", CraftingHandler.randomRecipeToExclude(EnumCrafting.FORGE, 0, 100));
            stack.setItemDamage(EnumCrafting.FORGE.getID());
            ItemUtils.spawnItemAtEntity(player, stack);*/
        }
        return super.use(level, player, handIn);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel() instanceof ServerLevel serverLevel) {
            int f = serverLevel.getPoiManager().getFreeTickets(context.getClickedPos());
            context.getPlayer().sendMessage(new TextComponent("Free " + f), Util.NIL_UUID);
        }
        return super.useOn(context);
    }
}