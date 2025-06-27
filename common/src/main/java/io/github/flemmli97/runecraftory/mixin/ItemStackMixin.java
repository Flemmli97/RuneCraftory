package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "mineBlock", at = @At("HEAD"))
    private void onBlockMine(Level level, BlockState state, BlockPos pos, Player player, CallbackInfo info) {
        if (player instanceof ServerPlayer serverPlayer) {
            EntityCalls.onBlockBreak(serverPlayer, state, pos);
        }
    }

    @WrapOperation(method = "addAttributeTooltips", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Ljava/util/function/BiConsumer;)V"))
    private void hideTooltip(ItemStack instance, EquipmentSlotGroup slotGroup,
                             BiConsumer<Holder<Attribute>, AttributeModifier> action, Operation<Void> original,
                             Consumer<Component> tooltipAdder, Player player) {
        if (ItemStat.SHOW_STATS_CUSTOM && ItemNBT.shouldHideVanillaAttributeTooltip((ItemStack) (Object) this, slotGroup, player)) {
            return;
        }
        original.call(instance, slotGroup, action);
    }

    @Inject(method = "addModifierTooltip", at = @At("HEAD"), cancellable = true)
    private void noTooltipRender(Consumer<Component> tooltipAdder, Player player, Holder<Attribute> attribute, AttributeModifier modifier, CallbackInfo info) {
        if (attribute.is(RunecraftoryTags.Attributes.DISPLAY_IGNORED)) {
            info.cancel();
        }
    }

    @ModifyVariable(method = "addModifierTooltip", at = @At("HEAD"), argsOnly = true)
    private AttributeModifier updateTooltip(AttributeModifier original, Consumer<Component> tooltipAdder, @Nullable Player player, Holder<Attribute> attribute) {
        return ItemStat.adjustModifier(attribute, original);
    }
}
