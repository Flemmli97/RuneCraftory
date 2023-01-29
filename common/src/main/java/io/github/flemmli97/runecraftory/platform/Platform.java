package io.github.flemmli97.runecraftory.platform;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.network.Packet;
import io.github.flemmli97.tenshilib.platform.InitUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Platform {

    Platform INSTANCE = InitUtil.getPlatformInstance(Platform.class,
            "io.github.flemmli97.runecraftory.fabric.platform.PlatformImpl",
            "io.github.flemmli97.runecraftory.forge.platform.PlatformImpl");

    Optional<PlayerData> getPlayerData(Player player);

    Optional<EntityData> getEntityData(LivingEntity living);

    Optional<StaffData> getStaffData(Object stack);

    void openGuiMenu(ServerPlayer player, MenuProvider provider);

    void openGuiMenu(ServerPlayer player, MenuProvider provider, BlockPos pos);

    void openGuiMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> writer);

    //Network

    void sendToClient(Packet message, ServerPlayer player);

    void sendToServer(Packet message);

    void sendToAll(Packet message, MinecraftServer server);

    void sendToTrackingAndSelf(Packet message, Entity e);

    void sendToTracking(Packet message, ServerLevel level, ChunkPos pos);

    //Item Stuff

    boolean isShield(ItemStack stack, Player player);

    boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity);

    ItemStaffBase staff(EnumElement starterElement, int amount, Item.Properties properties);

    ItemArmorBase armor(EquipmentSlot slot, Item.Properties properties, ResourceLocation id, boolean useItemTexture);

    //Block Stuff

    SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity);

    //Other

    DamageSource createDamageSource(String name, boolean bypassArmor, boolean bypassMagic);

    SimpleParticleType simple(boolean overrideLimiter);

    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> create, Block... valid);

    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> create, Set<Block> valid);

    <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> create);

    <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> create);

    Activity activity(String name);

    CreativeModeTab tab(String label, Supplier<ItemStack> icon);

    boolean matchingInventory(BlockEntity blockEntity, Function<ItemStack, Boolean> func);

    ItemStack findMatchingItem(BlockEntity blockEntity, Function<ItemStack, Boolean> func, int amount);

    ItemStack insertInto(BlockEntity blockEntity, ItemStack stack);

    //Events

    void cropGrowEvent(Level level, BlockPos pos, BlockState state);

    void craftingEvent(Player player, ItemStack stack, Container container);

    void destroyItem(Player player, ItemStack stack, InteractionHand hand);

    boolean onLivingUpdate(LivingEntity entity);

    boolean canEntitySpawnSpawner(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason);

    float onLivingHurt(LivingEntity entity, DamageSource damageSrc, float damageAmount);

    int getLootingFromCtx(LootContext ctx);
}
