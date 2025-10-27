package io.github.flemmli97.runecraftory.platform;

import io.github.flemmli97.runecraftory.common.creativetab.SubTab;
import io.github.flemmli97.tenshilib.loader.LoaderInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Platform {

    Platform INSTANCE = LoaderInitializer.getImplInstance(Platform.class,
            "io.github.flemmli97.runecraftory.fabric.platform.PlatformImpl",
            "io.github.flemmli97.runecraftory.neoforge.platform.PlatformImpl");

    void openGuiMenu(ServerPlayer player, MenuProvider provider);

    default void openGuiMenu(ServerPlayer player, MenuProvider provider, BlockPos pos) {
        this.openGuiMenu(player, provider, b -> BlockPos.STREAM_CODEC.encode(b, pos));
    }

    void openGuiMenu(ServerPlayer player, MenuProvider provider, Consumer<RegistryFriendlyByteBuf> writer);

    boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity);

    SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity);

    //Other

    <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> create);

    <T extends AbstractContainerMenu, D> MenuType<T> menuType(TriFunction<Integer, Inventory, D, T> create, StreamCodec<RegistryFriendlyByteBuf, D> codec);

    CreativeModeTab.Builder tabBuilder(List<SubTab> subTabs);

    boolean matchingInventory(BlockEntity blockEntity, Predicate<ItemStack> func);

    ItemStack findMatchingItem(BlockEntity blockEntity, Predicate<ItemStack> func, int amount);

    ItemStack insertInto(BlockEntity blockEntity, ItemStack stack);

    //Events

    void cropGrowEventPost(Level level, BlockPos pos, BlockState state);

    void craftingEvent(Player player, ItemStack stack, Container container);

    void destroyItem(Player player, ItemStack stack, InteractionHand hand);

    boolean checkSpawnPosition(Mob entity, ServerLevel level, MobSpawnType spawnReason);
}
