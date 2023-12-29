package io.github.flemmli97.runecraftory.fabric.platform;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.equipment.ItemArmorBase;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.network.Packet;
import io.github.flemmli97.runecraftory.fabric.mixin.DamageSourceAccessor;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.EntityDataGetter;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.PlayerDataGetter;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.StaffDataGetter;
import io.github.flemmli97.runecraftory.fabric.network.ClientPacketHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PlatformImpl implements Platform {

    @Override
    public boolean isDatagen() {
        return false;
    }

    @Override
    public Optional<PlayerData> getPlayerData(Player player) {
        return Optional.of(((PlayerDataGetter) player).getPlayerData());
    }

    @Override
    public Optional<EntityData> getEntityData(LivingEntity living) {
        return Optional.of(((EntityDataGetter) living).getEntityData());
    }

    @Override
    public Optional<StaffData> getStaffData(Object stack) {
        if (stack instanceof StaffDataGetter getter)
            return Optional.ofNullable(getter.getStaffData());
        return Optional.empty();
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider) {
        player.openMenu(provider);
    }

    @Override
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, BlockPos pos) {
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                buf.writeBlockPos(pos);
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
    public void openGuiMenu(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> writer) {
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                writer.accept(buf);
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
    public void sendToClient(Packet message, ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        message.write(buf);
        ServerPlayNetworking.send(player, message.getID(), buf);
    }

    @Override
    public void sendToServer(Packet message) {
        ClientPacketHandler.sendToServer(message);
    }

    @Override
    public void sendToAll(Packet message, MinecraftServer server) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        message.write(buf);
        PlayerLookup.all(server).forEach(player -> ServerPlayNetworking.send(player, message.getID(), buf));
    }

    @Override
    public void sendToTrackingAndSelf(Packet message, Entity e) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        message.write(buf);
        PlayerLookup.tracking(e).forEach(player -> ServerPlayNetworking.send(player, message.getID(), buf));
        if (e instanceof ServerPlayer serverPlayer)
            ServerPlayNetworking.send(serverPlayer, message.getID(), buf);
    }

    @Override
    public void sendToTracking(Packet message, ServerLevel level, ChunkPos pos) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        message.write(buf);
        PlayerLookup.tracking(level, pos).forEach(player -> ServerPlayNetworking.send(player, message.getID(), buf));
    }

    @Override
    public boolean isShield(ItemStack stack, Player player) {
        return stack.getUseAnimation() == UseAnim.BLOCK;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return LivingEntity.getEquipmentSlotForItem(stack) == slot;
    }

    @Override
    public ItemStaffBase staff(EnumElement starterElement, int amount, Item.Properties properties) {
        return new ItemStaffBase(starterElement, amount, properties);
    }

    @Override
    public ItemArmorBase armor(EquipmentSlot slot, Item.Properties properties, ResourceLocation id, boolean useItemTexture) {
        return new ItemArmorBase(slot, properties, id, useItemTexture);
    }

    @Override
    public SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity) {
        return state.getSoundType();
    }

    @Override
    public DamageSource createDamageSource(String name, boolean bypassArmor, boolean bypassMagic, boolean bypassInvul) {
        DamageSource source = new CustomDamageSource(name);
        if (bypassArmor)
            ((DamageSourceAccessor) source).setBypassArmor();
        if (bypassMagic)
            ((DamageSourceAccessor) source).setBypassMagic();
        if (bypassInvul)
            ((DamageSourceAccessor) source).setBypassInvul();
        return source;
    }

    @Override
    public SimpleParticleType simple(boolean overrideLimiter) {
        return ReflectiveInvokers.simpleParticleType(overrideLimiter);
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> create, Block... valid) {
        return FabricBlockEntityTypeBuilder.create(create::apply, valid).build();
    }

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> create, Set<Block> valid) {
        return FabricBlockEntityTypeBuilder.create(create::apply, valid.toArray(new Block[0])).build();
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> create) {
        return new MenuType<>(create::apply);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(TriFunction<Integer, Inventory, FriendlyByteBuf, T> create) {
        return new ExtendedScreenHandlerType<>(create::apply);
    }

    @Override
    public Activity activity(String name) {
        return ReflectiveInvokers.activity(name);
    }

    @Override
    public CreativeModeTab tab(String label, Supplier<ItemStack> icon) {
        return FabricItemGroupBuilder.build(new ResourceLocation(RuneCraftory.MODID, label), icon);
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
                if (itemStack.is(stack.getItem())
                        && itemStack.getDamageValue() == stack.getDamageValue()
                        && itemStack.getCount() < stack.getMaxStackSize()
                        && ItemStack.tagMatches(itemStack, stack)) {
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
    public void cropGrowEvent(Level level, BlockPos pos, BlockState state) {
    }

    @Override
    public void craftingEvent(Player player, ItemStack stack, Container containerMenu) {
    }

    @Override
    public void destroyItem(Player player, ItemStack stack, InteractionHand hand) {
    }

    @Override
    public boolean onLivingUpdate(LivingEntity entity) {
        return false;
    }

    @Override
    public boolean canEntitySpawnSpawner(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason) {
        return true;
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource damageSrc, float damageAmount) {
        return damageAmount;
    }

    @Override
    public int getLootingFromCtx(LootContext ctx) {
        Entity entity = ctx.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (entity instanceof LivingEntity living)
            return EnchantmentHelper.getMobLooting(living);
        return 0;
    }

    @Override
    public int getLootingFromEntity(Entity entity, Entity killer, DamageSource source) {
        if (killer instanceof LivingEntity living)
            return EnchantmentHelper.getMobLooting(living);
        return 0;
    }

    public static class CustomDamageSource extends DamageSource {

        public CustomDamageSource(String string) {
            super(string);
        }
    }
}
