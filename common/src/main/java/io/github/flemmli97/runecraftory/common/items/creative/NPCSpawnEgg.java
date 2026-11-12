package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.api.registry.NPCProfession;
import io.github.flemmli97.runecraftory.common.components.NPCSpawnData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import io.github.flemmli97.runecraftory.common.network.S2CNPCLook;
import io.github.flemmli97.runecraftory.common.network.S2CSpawnEggScreen;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

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
        NPCSpawnData data = player.getItemInHand(hand).get(RuneCraftoryDataComponentTypes.NPC_SPAWN_DATA.get());
        computeLooks(player,
                data != null ? data.npcDataId().orElse(null) : null,
                data != null ? data.profession().flatMap(Holder::unwrapKey).map(ResourceKey::location).orElse(null) : null);
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

    public static void computeLooks(ServerPlayer player, @Nullable ResourceLocation dataId, @Nullable ResourceLocation professionId) {
        ReloadableHolder<NPCData> data;
        if (dataId != null) {
            data = DataPackHandler.INSTANCE.npcDataManager().get(dataId);
        } else {
            NPCProfession profession = professionId != null ? player.registryAccess().registry(RuneCraftoryNPCProfessions.PROFESSION_REGISTRY_KEY)
                                                              .orElseThrow().get(professionId) : null;
            data = DataPackHandler.INSTANCE.npcDataManager().getRandom(player.getRandom(), d ->
                    (d.value().profession().isEmpty() || d.value().profession().stream().anyMatch(j -> j.equals(profession)))
                            && RunecraftorySavedData.get(player.getServer()).npcHandler.canAssignNPC(d), profession == null ? null :
                    d -> d.value().profession().stream().anyMatch(j -> j.equals(profession)));
        }
        List<ResourceLocation> looks = data.value().look() == null ? List.of() : data.value().look().stream().map(NPCData.NPCLookId::id).toList();
        ReloadableHolder<NPCLook> look;
        if (!looks.isEmpty())
            look = DataPackHandler.INSTANCE.npcLookManager().get(looks.get(player.getRandom().nextInt(looks.size())));
        else
            look = DataPackHandler.INSTANCE.npcLookManager().getRandom(player.getRandom(), player.getRandom().nextBoolean());
        NPCFeatureContainer container = new NPCFeatureContainer();
        container.buildFromLooks(RuneCraftoryEntities.NPC.get().create(player.level()), look.value());
        LoaderNetwork.INSTANCE.sendToPlayer(new S2CNPCLook(look, container), player);
    }
}
