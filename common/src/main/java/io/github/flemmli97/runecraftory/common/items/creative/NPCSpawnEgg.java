package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.common.components.NPCSpawnData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.network.S2CSpawnEggScreen;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

public class NPCSpawnEgg extends RuneCraftoryEggItem {

    public NPCSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Properties props) {
        super(type, 0x452808, 0x7d4c15, props);
    }

    @Override
    public boolean addToDefaultSpawneggs() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, list, tooltipFlag);
        NPCSpawnData itemData = stack.getOrDefault(RuneCraftoryDataComponentTypes.NPC_SPAWN_DATA.get(), NPCSpawnData.DEFAULT);
        String key = itemData.profession().map(h -> h.value().getTranslationKey()).orElse(RuneCraftoryNPCProfessions.NONE.get().getTranslationKey());
        list.add(Component.translatable(key).withStyle(ChatFormatting.AQUA));
    }

    @Override
    protected void openScreen(ServerPlayer player, InteractionHand hand) {
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CSpawnEggScreen(hand, DataPackHandler.INSTANCE.npcDataManager().getNpcIds()), player);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof NPCEntity npc) {
            NPCSpawnData itemData = stack.getOrDefault(RuneCraftoryDataComponentTypes.NPC_SPAWN_DATA.get(), NPCSpawnData.DEFAULT);
            if (itemData.npcDataId().isPresent()) {
                ReloadableHolder<NPCData> data = DataPackHandler.INSTANCE.npcDataManager().get(itemData.npcDataId().get());
                if (data != null) {
                    npc.setNPCData(data, false);
                    if (data.value().profession().isEmpty()) {
                        itemData.profession().ifPresent(prof -> npc.setProfession(prof.value()));
                    }
                }
            } else {
                itemData.profession().ifPresent(prof -> npc.randomizeData(prof.value(), true));
            }
        }
        return super.onEntitySpawned(e, stack, player);
    }
}
