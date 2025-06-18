package io.github.flemmli97.runecraftory.common.entities.npc.job;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class BathhouseAttendant extends NPCJob {

    public static final String BATH_ACTION = "runecraftory.npc.action.bath";
    public static final String BATH_ACTION_SUCCESS = "runecraftory.npc.action.bath.success";
    public static final String BATH_ACTION_FAIL = "runecraftory.npc.action.bath.fail";
    public static final String BATH_COST = "runecraftory.npc.shop.bath.cost";

    public BathhouseAttendant(NPCJob.Builder builder) {
        super(builder);
    }

    @Override
    public void handleAction(EntityNPCBase npc, Player player, String action) {
        if (npc.canTrade() == ShopState.OPEN)
            if (action.equals(BATH_ACTION)) {
                PlayerData data = Platform.INSTANCE.getPlayerData(player);
                int baths = data.getDailyUpdater().getBathCounter() + 1;
                int amount = 300 * baths + (Math.max(0, baths - 1)) * 100;
                if (data.useMoney(amount)) {
                    player.displayClientMessage(Component.translatable(BATH_ACTION_SUCCESS, player.getName()), false);
                    player.addEffect(new MobEffectInstance(ModEffects.BATH.asHolder(), 1700, 0, false, true, false));
                    data.getDailyUpdater().increaseBathCounter();
                } else {
                    player.displayClientMessage(Component.translatable(BATH_ACTION_FAIL, player.getName(), amount), false);
                }
            }
    }

    @Override
    public Map<String, List<Component>> actions(EntityNPCBase entity, ServerPlayer player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        int baths = data.getDailyUpdater().getBathCounter() + 1;
        int cost = 300 * baths + (Math.max(0, baths - 1)) * 100;
        return Map.of(BATH_ACTION, List.of(Component.translatable(BATH_COST, cost)));
    }
}
