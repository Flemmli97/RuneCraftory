package io.github.flemmli97.runecraftory.fabric.platform;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.network.Packet;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.fabric.mixin.DamageSourceAccessor;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.EntityDataGetter;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.PlayerDataGetter;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.StaffDataGetter;
import io.github.flemmli97.runecraftory.fabric.network.ClientPacketHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.impl.item.group.ItemGroupExtensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class PlatformImpl implements Platform {

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
    public boolean isShield(ItemStack stack, Player player) {
        return stack.getUseAnimation() == UseAnim.BLOCK;
    }

    @Override
    public EquipmentSlot slotType(ItemStack stack) {
        return Mob.getEquipmentSlotForItem(stack);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return Mob.getEquipmentSlotForItem(stack) == slot;
    }

    @Override
    public ItemStaffBase staff(EnumElement starterElement, int amount, Item.Properties properties) {
        return new ItemStaffBase(starterElement, amount, properties);
    }

    @Override
    public SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity) {
        return state.getSoundType();
    }

    @Override
    public DamageSource createDamageSource(String name, boolean bypassArmor, boolean bypassMagic) {
        DamageSource source = new CustomDamageSource(name);
        if (bypassArmor)
            ((DamageSourceAccessor) source).setBypassArmor();
        if (bypassMagic)
            ((DamageSourceAccessor) source).setBypassMagic();
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
        ((ItemGroupExtensions) CreativeModeTab.TAB_BUILDING_BLOCKS).fabric_expandArray();
        return new CreativeModeTab(CreativeModeTab.TABS.length - 1, String.format("%s.%s", RuneCraftory.MODID, label)) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> stacks) {
                super.fillItemList(stacks);
                stacks.forEach(ItemNBT::initNBT);
            }
        };
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

    public static class CustomDamageSource extends DamageSource {

        public CustomDamageSource(String string) {
            super(string);
        }
    }
}
