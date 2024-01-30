package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemDebug extends Item {

    public ItemDebug(Item.Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);
        components.add(new TranslatableComponent("runecraftory.item.creative.tooltip").withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            /*long time = System.nanoTime();
            Set<ConfiguredStructureFeature<?, ?>> structures = GateSpawning.getStructuresAt(serverLevel, player.blockPosition());
            long delta = System.nanoTime() - time;
            player.sendMessage(new TextComponent("" + structures), Util.NIL_UUID);
            player.sendMessage(new TextComponent("check time " + delta), Util.NIL_UUID);
            long time2 = System.nanoTime();
            boolean hasSpawns = GateSpawning.hasStructureSpawns(serverLevel, player.blockPosition());
            long delta2 = System.nanoTime() - time2;
            player.sendMessage(new TextComponent("" + hasSpawns), Util.NIL_UUID);
            player.sendMessage(new TextComponent("time " + delta2), Util.NIL_UUID);*/
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel() instanceof ServerLevel serverLevel) {
            int lvl = LevelCalc.levelFromPos(serverLevel, Vec3.atCenterOf(context.getClickedPos()));
            context.getPlayer().sendMessage(new TextComponent("GateLevel at pos: " + lvl), Util.NIL_UUID);
            FarmlandHandler.get(serverLevel.getServer()).getData(serverLevel, context.getClickedPos())
                    .ifPresent(d -> context.getPlayer().sendMessage(new TextComponent(d.toStringFull()), Util.NIL_UUID));
            /*int f = serverLevel.getPoiManager().getFreeTickets(context.getClickedPos());
            context.getPlayer().sendMessage(new TextComponent("Free POITickets" + f), Util.NIL_UUID);*/
            return InteractionResult.CONSUME;
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (player.level instanceof ServerLevel serverLevel) {
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}