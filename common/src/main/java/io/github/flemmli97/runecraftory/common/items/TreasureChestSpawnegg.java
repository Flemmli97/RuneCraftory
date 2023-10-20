package io.github.flemmli97.runecraftory.common.items;

import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class TreasureChestSpawnegg extends SpawnEgg {

    public TreasureChestSpawnegg(Supplier<? extends EntityType<?>> type, Properties props) {
        super(type, 0xac935e, 0x462f10, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(new TranslatableComponent("runecraftory.tooltip.item.treasure_chest").withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(new TranslatableComponent("runecraftory.tooltip.item.treasure_level", this.getTier(stack) + 1).withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
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
            if (!world.isClientSide)
                this.increaseTier(stack);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(world, player, hand);
    }

    @Override
    public int getColor(ItemStack stack, int i) {
        int tier = this.getTier(stack);
        if (tier == 2 || tier == 3) {
            return i == 0 ? 0x8f9cc4 : 0x343843;
        }
        return super.getColor(stack, i);
    }

    protected int getTier(ItemStack stack) {
        int tier = 0;
        if (stack.hasTag() && stack.getTag().contains("ChestTier")) {
            tier = stack.getTag().getInt("ChestTier");
        }
        return tier;
    }

    protected void increaseTier(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("ChestTier", (this.getTier(stack) + 1) % 4);
    }
}
