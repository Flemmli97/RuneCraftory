package io.github.flemmli97.runecraftory.common.entities.npc.job;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Smith extends NPCJob {

    public static final String BARN_ACTION = "npc.action.barn";
    public static final String BARN_ACTION_DESCRIPTION = "npc.action.barn.description";
    public static final String BARN_ACTION_SUCCESS = "npc.action.barn.success";
    public static final String BARN_ACTION_FAIL = "npc.action.barn.fail";
    public static final String BARN_COST = "npc.shop.barn.cost";
    public static final String BARN_COST_MAT = "npc.shop.barn.cost.mat";
    public static final String BARN_COST_FAIL = "npc.shop.barn.cost.fail";

    private static final Function<Integer, Integer> COST_FUNC = count -> {
        int clamped = Math.min(20, count + 1);
        return 1000 * clamped * clamped - (Math.max(0, clamped - 1) * 2) * 1000;
    };
    private static final Function<Integer, Float> COST_FUNC_MAT_MULTIPLIER = count -> {
        int clamped = Math.min(10, count);
        return 1 + (int) Math.ceil(clamped * clamped * 0.5) * 0.5f;
    };

    private static final Map<ItemPredicate, Integer> MATERIALS = ImmutableMap.of(
            ItemPredicate.Builder.item().of(ItemTags.LOGS).build(), 32,
            ItemPredicate.Builder.item().of(ModTags.COBBLESTONE).build(), 64
    );

    public Smith(NPCJob.Builder builder) {
        super(builder);
    }

    @Override
    public void handleAction(EntityNPCBase npc, Player player, String action) {
        if (npc.canTrade() == ShopState.OPEN)
            if (action.equals(BARN_ACTION)) {
                Platform.INSTANCE.getPlayerData(player)
                        .ifPresent(d -> {
                            int amount = COST_FUNC.apply(d.getBoughtBarns());
                            Map<ItemPredicate, List<ItemStack>> stacks = new HashMap<>();
                            for (ItemStack stack : player.getInventory().items) {
                                for (ItemPredicate pred : MATERIALS.keySet()) {
                                    if (pred.matches(stack))
                                        stacks.computeIfAbsent(pred, k -> new ArrayList<>())
                                                .add(stack);
                                }
                            }
                            float multiplier = COST_FUNC_MAT_MULTIPLIER.apply(d.getBoughtBarns());
                            boolean enough = MATERIALS.entrySet().stream()
                                    .allMatch(p -> stacks.getOrDefault(p.getKey(), List.of()).stream().mapToInt(ItemStack::getCount).sum() >= (int) (p.getValue() * multiplier));
                            if (enough && d.useMoney(player, amount)) {
                                player.sendMessage(new TranslatableComponent(BARN_ACTION_SUCCESS, player.getName()), Util.NIL_UUID);
                                for (Map.Entry<ItemPredicate, List<ItemStack>> e : stacks.entrySet()) {
                                    int needed = (int) (MATERIALS.get(e.getKey()) * multiplier);
                                    for (ItemStack stack : e.getValue()) {
                                        if (needed > stack.getCount()) {
                                            int count = stack.getCount();
                                            stack.setCount(0);
                                            needed -= count;
                                        } else {
                                            stack.shrink(needed);
                                            break;
                                        }
                                    }
                                }
                                if (!player.addItem(new ItemStack(ModItems.monsterBarn.get())))
                                    player.spawnAtLocation(new ItemStack(ModItems.monsterBarn.get()));
                                d.onBuyBarn();
                            } else {
                                player.sendMessage(new TranslatableComponent(BARN_ACTION_FAIL, player.getName(), amount), Util.NIL_UUID);
                            }
                        });
            }
    }

    @Override
    public Map<String, List<Component>> actions(EntityNPCBase entity, ServerPlayer player) {
        return Map.of(BARN_ACTION, getBarnActionComponent(player));
    }

    private static List<Component> getBarnActionComponent(ServerPlayer player) {
        return Platform.INSTANCE.getPlayerData(player).map(d -> {
            Object[] obj = new Object[MATERIALS.size()];
            int i = 0;
            for (Map.Entry<ItemPredicate, Integer> e : MATERIALS.entrySet()) {
                obj[i] = (int) (e.getValue() * COST_FUNC_MAT_MULTIPLIER.apply(d.getBoughtBarns()));
                i++;
            }
            return List.of((Component) new TranslatableComponent(BARN_COST, COST_FUNC.apply(d.getBoughtBarns())),
                    new TranslatableComponent(BARN_COST_MAT, obj));
        }).orElse(List.of(new TranslatableComponent(BARN_ACTION_FAIL)));
    }
}
