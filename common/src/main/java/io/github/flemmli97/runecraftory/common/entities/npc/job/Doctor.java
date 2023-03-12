package io.github.flemmli97.runecraftory.common.entities.npc.job;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class Doctor extends NPCJob {

    public static final String CURE_ACTION = "npc.action.doctor.cure";
    public static final String CURE_ACTION_DESC = "npc.action.doctor.cure.desc";
    public static final String CURE_ACTION_SUCCESS = "npc.action.doctor.cure.success";
    public static final String CURE_ACTION_FAIL = "npc.action.doctor.cure.fail";
    public static final String CURE_COST = "npc.shop.doctor.cure.cost";

    public static final int CURE_PRICE = 100;

    public Doctor(NPCJob.Builder builder) {
        super(builder);
    }

    @Override
    public void handleAction(EntityNPCBase npc, Player player, String action) {
        if (npc.updater.getBreadToBuy() <= 0)
            return;
        if (Platform.INSTANCE.getPlayerData(player).map(d -> !d.useMoney(player, CURE_PRICE)).orElse(true)) {
            player.sendMessage(new TranslatableComponent(CURE_ACTION_FAIL, player.getName(), CURE_PRICE), Util.NIL_UUID);
            return;
        }
        List<MobEffect> negativeEffects = player.getActiveEffectsMap().keySet().stream().filter(e -> e.getCategory() == MobEffectCategory.HARMFUL)
                .toList();
        negativeEffects.forEach(player::removeEffect);
        player.sendMessage(new TranslatableComponent(CURE_ACTION_SUCCESS, player.getName()), Util.NIL_UUID);
    }

    @Override
    public Map<String, List<Component>> actions(EntityNPCBase entity, ServerPlayer player) {
        if (player.getActiveEffects().stream().anyMatch(i -> i.getEffect().getCategory() == MobEffectCategory.HARMFUL)) {
            return ImmutableMap.of(CURE_ACTION, List.of(new TranslatableComponent(CURE_ACTION_DESC), new TranslatableComponent(CURE_COST, CURE_PRICE)));
        }
        return Map.of();
    }
}