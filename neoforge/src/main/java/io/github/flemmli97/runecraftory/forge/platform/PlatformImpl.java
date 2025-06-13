package io.github.flemmli97.runecraftory.forge.platform;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.forge.registry.ModAttachments;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.function.TriFunction;

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
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, BlockPos pos) {
        this.openGuiMenu(player, provider, b -> b.writeBlockPos(pos));
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, Consumer<RegistryFriendlyByteBuf> writer) {
        player.openMenu(provider, writer);
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
    public CreativeModeTab.Builder tabBuilder() {
        return CreativeModeTab.builder();
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

    @Override
    public Supplier<Item> registerRecord(int analogOutput, RegistryEntrySupplier<SoundEvent> sound, Item.Properties properties) {
        return () -> new RecordItem(analogOutput, sound, properties);
    }
}
