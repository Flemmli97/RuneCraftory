package io.github.flemmli97.runecraftory.common.entities.npc.profession;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class Doctor extends NPCProfession {

    public static final String CURE_ACTION = "runecraftory.npc.action.doctor.cure";
    public static final String CURE_ACTION_DESC = "runecraftory.npc.action.doctor.cure.desc";
    public static final String CURE_ACTION_SUCCESS = "runecraftory.npc.action.doctor.cure.success";
    public static final String CURE_ACTION_FAIL = "runecraftory.npc.action.doctor.cure.fail";
    public static final String CURE_COST = "runecraftory.npc.shop.doctor.cure.cost";

    public static final int CURE_PRICE = 100;

    public Doctor(NPCProfession.Builder builder) {
        super(builder);
    }

    @Override
    public void handleAction(NPCEntity npc, Player player, String action) {
        if (npc.updater.getBreadToBuy() <= 0)
            return;
        PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
        if (!data.useMoney(CURE_PRICE)) {
            player.displayClientMessage(Component.translatable(CURE_ACTION_FAIL, player.getName(), CURE_PRICE), false);
            return;
        }
        List<Holder<MobEffect>> negativeEffects = player.getActiveEffectsMap().keySet().stream().filter(e -> e.value().getCategory() == MobEffectCategory.HARMFUL)
                .toList();
        negativeEffects.forEach(player::removeEffect);
        player.displayClientMessage(Component.translatable(CURE_ACTION_SUCCESS, player.getName()), false);
    }

    @Override
    public Map<String, List<Component>> actions(NPCEntity entity, ServerPlayer player) {
        if (player.getActiveEffects().stream().anyMatch(i -> i.getEffect().value().getCategory() == MobEffectCategory.HARMFUL)) {
            return ImmutableMap.of(CURE_ACTION, List.of(Component.translatable(CURE_ACTION_DESC), Component.translatable(CURE_COST, CURE_PRICE)));
        }
        return Map.of();
    }
}