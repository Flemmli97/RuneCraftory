package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class TreasureChestSpawnegg extends SpawnEgg {

    public TreasureChestSpawnegg(Supplier<? extends EntityType<?>> type, Properties props) {
        super(type, 0xac935e, 0x462f10, props);
    }

    @Override
    public boolean addToDefaultSpawneggs() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("runecraftory.tooltip.item.treasure_chest").withStyle(ChatFormatting.GOLD));
        list.add(Component.translatable("runecraftory.tooltip.item.treasure_level", this.getTier(stack).ordinal() + 1).withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, context, list, tooltipFlag);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof EntityTreasureChest chest) {
            chest.setTier(this.getTier(stack));
        }
        //Temporary fix for Forge-Bug-#7730
        if (e.getBbWidth() > 0.7 && e.getBbWidth() < 1) {
            e.setPos(e.getX() + 0.05, e.getY(), e.getZ());
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            if (!world.isClientSide) {
                ChestTier tier = this.getTier(stack);
                stack.set(ModDataComponentTypes.SPAWN_EGG_CHEST_TIER.get(), tier.cycle());
            }
            return InteractionResultHolder.consume(stack);
        }
        return super.use(world, player, hand);
    }

    @Override
    public int getColor(ItemStack stack, int i) {
        ChestTier tier = this.getTier(stack);
        if (tier == ChestTier.RARE || tier == ChestTier.EPIC) {
            return i == 0 ? 0x8f9cc4 : 0x343843;
        }
        return super.getColor(stack, i);
    }

    protected ChestTier getTier(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.SPAWN_EGG_CHEST_TIER.get(), ChestTier.COMMON);
    }

    public enum ChestTier {

        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        QUEST;

        public ChestTier cycle() {
            int next = (this.ordinal() + 1) % ChestTier.values().length;
            return ChestTier.values()[next];
        }
    }
}
