package io.github.flemmli97.runecraftory.common.items;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
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

public class NPCSpawnEgg extends RuneCraftoryEggItem {

    public NPCSpawnEgg(Supplier<? extends EntityType<?>> type, Properties props) {
        super(type, 0x452808, 0x7d4c15, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(new TranslatableComponent("tooltip.item.npc").withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(new TranslatableComponent(EnumShop.values()[this.getTier(stack)].translationKey).withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (e instanceof EntityNPCBase npc) {
            if (!npc.isShopDefined())
                npc.setShop(EnumShop.values()[this.getTier(stack)]);
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

    protected int getTier(ItemStack stack) {
        int tier = 0;
        if (stack.hasTag() && stack.getTag().contains("Shop")) {
            tier = stack.getTag().getInt("Shop");
        }
        return Math.min(EnumShop.values().length - 1, tier);
    }

    protected void increaseTier(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Shop", (this.getTier(stack) + 1) % EnumShop.values().length);
    }
}
