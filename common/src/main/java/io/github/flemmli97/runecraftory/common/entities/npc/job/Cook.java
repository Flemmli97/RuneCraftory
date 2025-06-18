package io.github.flemmli97.runecraftory.common.entities.npc.job;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public class Cook extends NPCJob {

    public static final String FORGE_BREAD_ACTION = "runecraftory.npc.action.bread.forge";
    public static final String ARMOR_BREAD_DESCRIPTION = "runecraftory.npc.action.bread.armor";
    public static final String CHEM_BREAD_SUCCESS = "runecraftory.npc.action.bread.chem";
    public static final String COOKING_BREAD_SUCCESS = "runecraftory.npc.action.bread.cooking";
    public static final String BREAD_ACTION_SUCCESS = "runecraftory.npc.action.bread.success";
    public static final String BREAD_ACTION_SUCCESS_GOOD = "runecraftory.npc.action.bread.success.good";
    public static final String BREAD_ACTION_FAIL = "runecraftory.npc.action.bread.fail";
    public static final String BREAD_COST = "runecraftory.npc.shop.bread.cost";

    public static final int BREAD_PRICE = 500;

    public Cook(NPCJob.Builder builder) {
        super(builder);
    }

    @Override
    public void handleAction(EntityNPCBase npc, Player player, String action) {
        if (npc.updater.getBreadToBuy() <= 0)
            return;
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        if (!data.useMoney(BREAD_PRICE)) {
            player.displayClientMessage(Component.translatable(BREAD_ACTION_FAIL, player.getName(), BREAD_PRICE), false);
            return;
        }
        ItemStack bread = switch (action) {
            case ARMOR_BREAD_DESCRIPTION -> new ItemStack(ModItems.ARMOR_BREAD.get());
            case CHEM_BREAD_SUCCESS -> new ItemStack(ModItems.CHEMISTRY_BREAD.get());
            case COOKING_BREAD_SUCCESS -> new ItemStack(ModItems.COOKING_BREAD.get());
            default -> new ItemStack(ModItems.FORGING_BREAD.get());
        };
        int level = Mth.ceil(Math.abs(npc.getRandom().nextGaussian() * 4));
        ItemNBT.getLeveledItem(bread, level);
        if (!player.addItem(bread))
            player.spawnAtLocation(bread);
        npc.updater.onBuyBread();
        if (level >= 7)
            player.displayClientMessage(Component.translatable(BREAD_ACTION_SUCCESS_GOOD, player.getName()), false);
        else
            player.displayClientMessage(Component.translatable(BREAD_ACTION_SUCCESS, player.getName()), false);
    }

    @Override
    public Map<String, List<Component>> actions(EntityNPCBase entity, ServerPlayer player) {
        int bread = entity.updater.getBreadToBuy();
        if (bread > 0) {
            Component comp = Component.translatable(BREAD_COST, BREAD_PRICE, bread);
            return ImmutableMap.of(
                    FORGE_BREAD_ACTION, List.of(comp),
                    ARMOR_BREAD_DESCRIPTION, List.of(comp),
                    CHEM_BREAD_SUCCESS, List.of(comp),
                    COOKING_BREAD_SUCCESS, List.of(comp));
        }
        return Map.of();
    }
}