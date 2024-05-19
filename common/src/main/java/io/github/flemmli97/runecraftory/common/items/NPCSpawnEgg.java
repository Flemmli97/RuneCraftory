package io.github.flemmli97.runecraftory.common.items;

import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class NPCSpawnEgg extends RuneCraftoryEggItem {

    public static final String NPC_ID = "NPCId";
    public static final String NPC_SHOP = "Shop";

    public NPCSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, Properties props) {
        super(type, 0x452808, 0x7d4c15, props);
    }

    @Override
    public boolean addToDefaultSpawneggs() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(new TranslatableComponent("runecraftory.tooltip.item.npc").withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(new TranslatableComponent(getJob(stack).getTranslationKey()).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof EntityNPCBase npc) {
            ResourceLocation id = getNpcID(stack);
            if (id != null) {
                NPCData data = DataPackHandler.INSTANCE.npcDataManager().get(id);
                if (data != null) {
                    npc.setNPCData(data, false);
                    return super.onEntitySpawned(e, stack, player);
                }
            }
            NPCJob job = getJob(stack);
            if (job != ModNPCJobs.NONE.getSecond())
                npc.randomizeData(job, true);
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            if (!world.isClientSide)
                next(stack);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(world, player, hand);
    }

    public static NPCJob getJob(ItemStack stack) {
        NPCJob job = ModNPCJobs.NONE.getSecond();
        if (stack.hasTag() && stack.getTag().contains(NPC_SHOP)) {
            job = ModNPCJobs.getFromID(new ResourceLocation(stack.getTag().getString(NPC_SHOP)));
        }
        return job;
    }

    public static void setNpcID(ItemStack stack, ResourceLocation id) {
        CompoundTag tag = stack.getOrCreateTag();
        if (id == null)
            tag.remove(NPC_ID);
        else
            tag.putString(NPC_ID, id.toString());
    }

    public static ResourceLocation getNpcID(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(NPC_ID)) {
            return new ResourceLocation(stack.getTag().getString(NPC_ID));
        }
        return null;
    }

    public static void next(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        List<NPCJob> jobs = ModNPCJobs.allJobs();
        NPCJob current = getJob(stack);
        tag.putString(NPC_SHOP, ModNPCJobs.getIDFrom(jobs.get((jobs.indexOf(current) + 1) % jobs.size())).toString());
    }
}
