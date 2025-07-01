package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.tenshilib.common.item.AnimationDebugger;
import io.github.flemmli97.tenshilib.common.item.ExtendedWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ItemDebug extends AnimationDebugger implements ExtendedWeapon {

    public ItemDebug(Item.Properties props) {
        super(props, ModDataComponentTypes.SELECTED_UUID, ModDataComponentTypes.SELECTED_ANIMATION);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, list, tooltipFlag);
        list.add(Component.translatable("runecraftory.item.creative.tooltip").withStyle(ChatFormatting.DARK_RED));
        list.add(Component.translatable("runecraftory.item.creative.tooltip.mode",
                Component.translatable(this.getCurrentMode(stack).translationKey).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (this.getCurrentMode(player.getItemInHand(hand)) == Mode.ANIMATION)
            return super.use(level, player, hand);
        if (level instanceof ServerLevel serverLevel) {
            /*long time = System.nanoTime();
            Set<ConfiguredStructureFeature<?, ?>> structures = GateSpawning.getStructuresAt(serverLevel, player.blockPosition());
            long delta = System.nanoTime() - time;
            player.sendMessage(Component.literal("" + structures), Util.NIL_UUID);
            player.sendMessage(Component.literal("check time " + delta), Util.NIL_UUID);
            long time2 = System.nanoTime();
            boolean hasSpawns = GateSpawning.hasStructureSpawns(serverLevel, player.blockPosition());
            long delta2 = System.nanoTime() - time2;
            player.sendMessage(Component.literal("" + hasSpawns), Util.NIL_UUID);
            player.sendMessage(Component.literal("time " + delta2), Util.NIL_UUID);*/
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel() instanceof ServerLevel serverLevel) {
            int lvl = LevelCalc.levelFromPos(serverLevel, Vec3.atCenterOf(context.getClickedPos()), LevelCalc.playersAround(serverLevel, Vec3.atCenterOf(context.getClickedPos()), 256));
            context.getPlayer().displayClientMessage(Component.literal("GateLevel at pos: " + lvl), false);
            FarmlandHandler.get(serverLevel.getServer()).getData(serverLevel, context.getClickedPos())
                    .ifPresent(d -> context.getPlayer().displayClientMessage(Component.literal(d.toStringFull()), false));
            /*int f = serverLevel.getPoiManager().getFreeTickets(context.getClickedPos());
            context.getPlayer().sendMessage(Component.literal("Free POITickets" + f), Util.NIL_UUID);*/
            return InteractionResult.CONSUME;
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (this.getCurrentMode(stack) == Mode.ANIMATION)
            return super.interactLivingEntity(stack, player, target, usedHand);
        if (player.level() instanceof ServerLevel serverLevel) {
            if (target instanceof Mob mob)
                mob.travel(new Vec3(0, 0, 5));
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, usedHand);
    }

    @Override
    public double getRange(LivingEntity entity, ItemStack stack) {
        return ExtendedWeapon.super.getRange(entity, stack) + 2;
    }

    @Override
    public void executeAttack(Player player, ItemStack stack) {
        ItemStack main = player.getMainHandItem();
        if (player instanceof ServerPlayer && player.isShiftKeyDown() && stack == main) {
            this.changeMode(stack);
        }
        player.swing(InteractionHand.MAIN_HAND, true);
    }

    private void changeMode(ItemStack stack) {
        Mode mode = this.getCurrentMode(stack);
        stack.set(ModDataComponentTypes.DEBUG_ITEM_MODE.get(), mode == Mode.DEFAULT ? Mode.ANIMATION : Mode.DEFAULT);
    }

    private Mode getCurrentMode(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.DEBUG_ITEM_MODE.get(), Mode.DEFAULT);
    }

    public enum Mode {

        DEFAULT("runecraftory.item.creative.tooltip.mode.default"),
        ANIMATION("runecraftory.item.creative.tooltip.mode.animation");

        private final String translationKey;

        Mode(String translationKey) {
            this.translationKey = translationKey;
        }
    }
}