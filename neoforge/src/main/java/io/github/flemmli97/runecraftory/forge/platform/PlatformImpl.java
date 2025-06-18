package io.github.flemmli97.runecraftory.forge.platform;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.forge.registry.ModAttachments;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlatformImpl implements Platform {

    @Override
    public boolean isDatagen() {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public MinecraftServer getCurrentServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public PlayerData getPlayerData(Player player) {
        return player.getData(ModAttachments.PLAYER_DATA);
    }

    @Override
    public EntityData getEntityData(LivingEntity living) {
        return living.getData(ModAttachments.ENTITY_DATA);
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, Consumer<RegistryFriendlyByteBuf> writer) {
        player.openMenu(provider, writer);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return stack.canEquip(slot, entity);
    }

    @Override
    public SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity) {
        return state.getSoundType(level, pos, entity);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> create) {
        return new MenuType<>(create::apply, FeatureFlagSet.of());
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, RegistryFriendlyByteBuf, T> create) {
        return new MenuType<>((IContainerFactory<T>) create::apply, FeatureFlagSet.of());
    }

    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public boolean matchingInventory(BlockEntity entity, Predicate<ItemStack> func) {
        if (entity == null || entity.getLevel() == null)
            return false;
        IItemHandler handler = entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, null);
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++)
                if (func.test(handler.getStackInSlot(i)))
                    return true;
            return false;
        }
        return false;
    }

    @Override
    public ItemStack findMatchingItem(BlockEntity entity, Predicate<ItemStack> func, int amount) {
        if (entity == null || entity.getLevel() == null)
            return ItemStack.EMPTY;
        IItemHandler handler = entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, null);
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++)
                if (func.test(handler.getStackInSlot(i))) {
                    return handler.extractItem(i, amount, false);
                }
            return ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertInto(BlockEntity entity, ItemStack stack) {
        if (entity == null || entity.getLevel() == null || stack.isEmpty())
            return stack;
        IItemHandler handler = entity.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, entity.getBlockPos(), entity.getBlockState(), entity, null);
        return ItemHandlerHelper.insertItem(handler, stack, false);
    }

    @Override
    public void cropGrowEventPost(Level level, BlockPos pos, BlockState state) {
        CommonHooks.fireCropGrowPost(level, pos, state);
    }

    @Override
    public void craftingEvent(Player player, ItemStack stack, Container container) {
        EventHooks.firePlayerCraftingEvent(player, stack, container);
    }

    @Override
    public void destroyItem(Player player, ItemStack stack, InteractionHand hand) {
        EventHooks.onPlayerDestroyItem(player, stack, hand);
    }

    @Override
    public boolean entityTickPre(LivingEntity entity) {
        return EventHooks.fireEntityTickPre(entity).isCanceled();
    }

    @Override
    public boolean checkSpawnPosition(Mob entity, ServerLevel level, MobSpawnType spawnType) {
        return EventHooks.checkSpawnPosition(entity, level, spawnType);
    }
}
