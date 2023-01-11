package io.github.flemmli97.runecraftory.common.entities.npc.job;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class BathhouseAttendant extends NPCJob {

    public static final String BATH_ACTION = "npc.action.bath";
    public static final String BATH_ACTION_SUCCESS = "npc.action.bath.success";
    public static final String BATH_ACTION_FAIL = "npc.action.bath.fail";

    public BathhouseAttendant(NPCJob.Builder builder) {
        super(builder);
    }

    @Override
    public void handleAction(EntityNPCBase npc, Player player, String action) {
        if (npc.canTrade() == ShopState.OPEN)
            if (action.equals(BATH_ACTION)) {
                Platform.INSTANCE.getPlayerData(player)
                        .ifPresent(d -> {
                            int baths = d.getDailyUpdater().getBathCounter();
                            int amount = 300 * baths + (Math.max(0, baths - 1)) * 100;
                            if (d.useMoney(player, amount)) {
                                player.sendMessage(new TranslatableComponent(BATH_ACTION_SUCCESS, player.getName()), Util.NIL_UUID);
                                player.addEffect(new MobEffectInstance(ModEffects.bath.get(), 1700, 0, false, true, false));
                                d.getDailyUpdater().increaseBathCounter();
                            } else {
                                player.sendMessage(new TranslatableComponent(BATH_ACTION_FAIL, player.getName(), amount), Util.NIL_UUID);
                            }
                        });
            }
    }

    @Override
    public List<String> actions() {
        return List.of(BATH_ACTION);
    }
}
