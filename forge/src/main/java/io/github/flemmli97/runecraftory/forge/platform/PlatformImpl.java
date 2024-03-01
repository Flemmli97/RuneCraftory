package io.github.flemmli97.runecraftory.forge.platform;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.attachment.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.network.Packet;
import io.github.flemmli97.runecraftory.forge.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.forge.item.ForgeArmorBase;
import io.github.flemmli97.runecraftory.forge.item.StaffItem;
import io.github.flemmli97.runecraftory.forge.network.PacketHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
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
import net.minecraft.world.item.UseAnim;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PlatformImpl implements Platform {

    @Override
    public boolean isDatagen() {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public Optional<PlayerData> getPlayerData(Player player) {
        return player.getCapability(CapabilityInsts.PLAYER_CAP).resolve();
    }

    @Override
    public Optional<EntityData> getEntityData(LivingEntity living) {
        return living.getCapability(CapabilityInsts.ENTITY_CAP).resolve();
    }

    @Override
    public Optional<StaffData> getStaffData(Object obj) {
        if (obj instanceof ItemStack stack)
            return stack.getCapability(CapabilityInsts.STAFF_ITEM_CAP).resolve();
        return Optional.empty();
    }

    @Override
    public Optional<ArmorEffectData> getArmorEffects(Object obj) {
        if (obj instanceof ItemStack stack)
            return stack.getCapability(CapabilityInsts.ARMOR_ITEM_CAP).resolve();
        return Optional.empty();
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider) {
        NetworkHooks.openGui(player, provider);
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, BlockPos pos) {
        NetworkHooks.openGui(player, provider, pos);
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> writer) {
        NetworkHooks.openGui(player, provider, writer);
    }

    @Override
    public void sendToClient(Packet message, ServerPlayer player) {
        PacketHandler.sendToClient(message, player);
    }

    @Override
    public void sendToServer(Packet message) {
        PacketHandler.sendToServer(message);
    }

    @Override
    public void sendToAll(Packet message, MinecraftServer server) {
        PacketHandler.sendToAll(message);
    }

    @Override
    public void sendToTrackingAndSelf(Packet message, Entity e) {
        PacketHandler.sendToTrackingAndSelf(message, e);
    }

    @Override
    public void sendToTracking(Packet message, ServerLevel level, ChunkPos pos) {
        PacketHandler.sendToTracking(message, level, pos);
    }

    @Override
    public boolean isShield(ItemStack stack, Player player) {
        return stack.getUseAnimation() == UseAnim.BLOCK;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return stack.canEquip(slot, entity);
    }

    @Override
    public ItemStaffBase staff(EnumElement starterElement, int amount, Item.Properties properties) {
        return new StaffItem(starterElement, amount, properties);
    }

    @Override
    public ItemArmorBase armor(EquipmentSlot slot, Item.Properties properties, ResourceLocation id, boolean useItemTexture) {
        return new ForgeArmorBase(slot, properties, id, useItemTexture);
    }

    @Override
    public SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity) {
        return state.getSoundType(level, pos, entity);
    }

    @Override
    public DamageSource createDamageSource(String name, boolean bypassArmor, boolean bypassMagic, boolean bypassInvul) {
        DamageSource source = new DamageSource(name);
        if (bypassArmor)
            source.bypassArmor();
        if (bypassMagic)
            source.bypassMagic();
        if (bypassInvul)
            source.bypassInvul();
        return source;
    }

    @Override
    public SimpleParticleType simple(boolean overrideLimiter) {
        return new SimpleParticleType(overrideLimiter);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> create, Block... valid) {
        return BlockEntityType.Builder.of(create::apply, valid).build(null);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> create, Set<Block> valid) {
        return new BlockEntityType<>(create::apply, valid, null);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> create) {
        return new MenuType<>(create::apply);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> create) {
        return IForgeMenuType.create(create::apply);
    }

    @Override
    public Activity activity(String name) {
        return new Activity(name);
    }

    @Override
    public CreativeModeTab tab(String label, Supplier<ItemStack> icon) {
        return new CreativeModeTab(String.format("%s.%s", RuneCraftory.MODID, label)) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }

    @Override
    public boolean matchingInventory(BlockEntity blockEntity, Predicate<ItemStack> func) {
        if (blockEntity == null)
            return false;
        if (blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
            return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(cap -> {
                for (int i = 0; i < cap.getSlots(); i++)
                    if (func.test(cap.getStackInSlot(i)))
                        return true;
                return false;
            }).orElse(false);
        return false;
    }

    @Override
    public ItemStack findMatchingItem(BlockEntity blockEntity, Predicate<ItemStack> func, int amount) {
        if (blockEntity == null)
            return ItemStack.EMPTY;
        return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .map(cap -> {
                    for (int i = 0; i < cap.getSlots(); i++)
                        if (func.test(cap.getStackInSlot(i))) {
                            return cap.extractItem(i, amount, false);
                        }
                    return ItemStack.EMPTY;
                }).orElse(ItemStack.EMPTY);
    }

    @Override
    public ItemStack insertInto(BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity == null || stack.isEmpty())
            return stack;
        return blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                .map(cap -> ItemHandlerHelper.insertItem(cap, stack, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public void cropGrowEvent(Level level, BlockPos pos, BlockState state) {
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    @Override
    public void craftingEvent(Player player, ItemStack stack, Container containerMenu) {
        ForgeEventFactory.firePlayerCraftingEvent(player, stack, containerMenu);
    }

    @Override
    public void destroyItem(Player player, ItemStack stack, InteractionHand hand) {
        ForgeEventFactory.onPlayerDestroyItem(player, stack, hand);
    }

    @Override
    public boolean onLivingUpdate(LivingEntity entity) {
        return ForgeHooks.onLivingUpdate(entity);
    }

    @Override
    public boolean canEntitySpawnSpawner(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason) {
        Event.Result res = ForgeEventFactory.canEntitySpawn(entity, level, x, y, z, spawner, spawnReason);
        return res == Event.Result.ALLOW || res == Event.Result.DEFAULT;
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource damageSrc, float damageAmount) {
        return ForgeHooks.onLivingHurt(entity, damageSrc, damageAmount);
    }

    @Override
    public int getLootingFromCtx(LootContext ctx) {
        return ctx.getLootingModifier();
    }

    @Override
    public int getLootingFromEntity(Entity entity, Entity killer, DamageSource source) {
        return ForgeHooks.getLootingLevel(entity, killer, source);
    }
}
