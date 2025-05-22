package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.tenshilib.api.item.IExtendedWeapon;
import io.github.flemmli97.tenshilib.common.item.AnimationDebugger;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemDebug extends AnimationDebugger implements IExtendedWeapon {

    private static final String ITEM_MODE = RuneCraftory.MODID + ":debug_mode";

    public ItemDebug(Item.Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);
        components.add(new TranslatableComponent("runecraftory.item.creative.tooltip").withStyle(ChatFormatting.DARK_RED));
        components.add(new TranslatableComponent("runecraftory.item.creative.tooltip.mode",
                new TranslatableComponent(this.getCurrentMode(stack).translationKey).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (this.getCurrentMode(player.getItemInHand(hand)) == Mode.ANIMATION)
            return super.use(level, player, hand);
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
            int lvl = LevelCalc.levelFromPos(serverLevel, Vec3.atCenterOf(context.getClickedPos()), LevelCalc.playersAround(serverLevel, Vec3.atCenterOf(context.getClickedPos()), 256));
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
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand usedHand) {
        if (this.getCurrentMode(stack) == Mode.ANIMATION)
            return super.interactLivingEntity(stack, player, target, usedHand);
        if (player.level instanceof ServerLevel serverLevel) {
            if (target instanceof Mob mob)
                mob.travel(new Vec3(0, 0, 5));
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, target, usedHand);
    }

    @Override
    public float getRange(LivingEntity entity, ItemStack stack) {
        return 4;
    }

    @Override
    public boolean onServerSwing(LivingEntity entity, ItemStack stack) {
        ItemStack main = entity.getMainHandItem();
        if (entity instanceof ServerPlayer player && player.isShiftKeyDown() && stack == main) {
            this.changeMode(stack);
        }
        return false;
    }

    private void changeMode(ItemStack stack) {
        CompoundTag compound = new CompoundTag();
        if (stack.hasTag())
            compound = stack.getTag();
        Mode mode = this.getCurrentMode(stack);
        compound.putInt(ITEM_MODE, (mode.ordinal() + 1) % Mode.values().length);
        compound.remove(SAVED_ENTITY);
        compound.remove(ANIMATION_IDX);
        stack.setTag(compound);
    }

    private Mode getCurrentMode(ItemStack stack) {
        return stack.hasTag() ? Mode.values()[stack.getTag().getInt(ITEM_MODE)] : Mode.DEFAULT;
    }

    private enum Mode {

        DEFAULT("runecraftory.item.creative.tooltip.mode.default"),
        ANIMATION("runecraftory.item.creative.tooltip.mode.animation");

        private final String translationKey;

        Mode(String translationKey) {
            this.translationKey = translationKey;
        }
    }
}