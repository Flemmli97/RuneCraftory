package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.common.components.NPCSpawnData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
        NPCSpawnData itemData = stack.getOrDefault(ModDataComponentTypes.NPC_SPAWN_DATA.get(), NPCSpawnData.DEFAULT);
        list.add(Component.translatable("runecraftory.tooltip.item.npc").withStyle(ChatFormatting.GOLD));
        String key = itemData.job().map(h -> h.value().getTranslationKey()).orElse(ModNPCJobs.NONE.get().getTranslationKey());
        list.add(Component.translatable(key).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof EntityNPCBase npc) {
            NPCSpawnData itemData = stack.getOrDefault(ModDataComponentTypes.NPC_SPAWN_DATA.get(), NPCSpawnData.DEFAULT);
            boolean modifyJob = true;
            if (itemData.npcDataId().isPresent()) {
                NPCData data = DataPackHandler.INSTANCE.npcDataManager().get(itemData.npcDataId().get());
                if (data != null) {
                    npc.setNPCData(data, false);
                    modifyJob = data.profession().isEmpty();
                }
            }
            if (modifyJob) {
                itemData.job().ifPresent(job -> npc.randomizeData(job.value(), true));
            }
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            if (!level.isClientSide) {
                NPCSpawnData data = stack.getOrDefault(ModDataComponentTypes.NPC_SPAWN_DATA.get(), NPCSpawnData.DEFAULT);
                stack.set(ModDataComponentTypes.NPC_SPAWN_DATA.get(), data.cycleJob(level.registryAccess()));
            }
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, hand);
    }
}
