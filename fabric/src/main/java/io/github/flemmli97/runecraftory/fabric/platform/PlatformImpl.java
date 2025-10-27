package io.github.flemmli97.runecraftory.fabric.platform;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.creativetab.CreativeTabBuilderExtension;
import io.github.flemmli97.runecraftory.common.creativetab.SubTab;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.EntityDataGetter;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlatformImpl implements Platform {

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, Consumer<RegistryFriendlyByteBuf> writer) {
        player.openMenu(new ExtendedScreenHandlerFactory<>() {
            @Override
            public Object getScreenOpeningData(ServerPlayer serverPlayer) {
                RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
                writer.accept(buf);
                return buf;
            }

            @Override
            public Component getDisplayName() {
                return provider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return provider.createMenu(i, inventory, player);
            }
        });
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return entity.getEquipmentSlotForItem(stack) == slot;
    }

    @Override
    public SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity) {
        return state.getSoundType();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> create) {
        return new MenuType<>(create::apply, FeatureFlagSet.of());
    }

    @Override
    public <T extends AbstractContainerMenu, D> MenuType<T> menuType(TriFunction<Integer, Inventory, D, T> create, StreamCodec<RegistryFriendlyByteBuf, D> codec) {
        return new ExtendedScreenHandlerType<>(create::apply, codec);
    }

    @Override
    public CreativeModeTab.Builder tabBuilder(List<SubTab> subTabs) {
        CreativeModeTab.Builder builder = FabricItemGroup.builder();
        ((CreativeTabBuilderExtension) builder).runecraftory$withSubTab(subTabs);
        return builder;
    }

    @Override
    public boolean matchingInventory(BlockEntity blockEntity, Predicate<ItemStack> func) {
        if (blockEntity == null)
            return false;
        if (blockEntity instanceof Container container) {
            for (int i = 0; i < container.getContainerSize(); i++)
                if (func.test(container.getItem(i)))
                    return true;
        }
        return false;
    }

    @Override
    public ItemStack findMatchingItem(BlockEntity blockEntity, Predicate<ItemStack> func, int amount) {
        if (blockEntity == null)
            return ItemStack.EMPTY;
        if (blockEntity instanceof Container container) {
            for (int i = 0; i < container.getContainerSize(); i++)
                if (func.test(container.getItem(i)))
                    return container.removeItem(i, amount);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertInto(BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity == null || stack.isEmpty())
            return stack;
        if (blockEntity instanceof Container container) {
            for (int i = 0; i < container.getContainerSize(); ++i) {
                if (stack.isEmpty())
                    break;
                if (!container.canPlaceItem(i, stack))
                    continue;
                ItemStack itemStack = container.getItem(i);
                if (itemStack.isEmpty()) {
                    container.setItem(i, stack);
                    return ItemStack.EMPTY;
                }
                if (ItemStack.isSameItemSameComponents(itemStack, stack) && itemStack.getCount() < stack.getMaxStackSize()) {
                    int size = Math.min(stack.getCount(), stack.getMaxStackSize() - itemStack.getCount());
                    stack.shrink(size);
                    itemStack.grow(size);
                    if (stack.isEmpty())
                        return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }

    @Override
    public void cropGrowEventPost(Level level, BlockPos pos, BlockState state) {
    }

    @Override
    public void craftingEvent(Player player, ItemStack stack, Container container) {

    }

    @Override
    public void destroyItem(Player player, ItemStack stack, InteractionHand hand) {

    }

    @Override
    public boolean checkSpawnPosition(Mob entity, ServerLevel level, MobSpawnType spawnReason) {
        return true;
    }
}
