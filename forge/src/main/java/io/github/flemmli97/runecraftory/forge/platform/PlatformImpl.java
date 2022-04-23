package io.github.flemmli97.runecraftory.forge.platform;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemStaffBase;
import io.github.flemmli97.runecraftory.common.network.Packet;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.forge.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.forge.item.StaffItem;
import io.github.flemmli97.runecraftory.forge.network.PacketHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class PlatformImpl implements Platform {

    @Override
    public Optional<PlayerData> getPlayerData(Player player) {
        return player.getCapability(CapabilityInsts.PLAYERCAP).resolve();
    }

    @Override
    public Optional<EntityData> getEntityData(LivingEntity living) {
        return living.getCapability(CapabilityInsts.ENTITYCAP).resolve();
    }

    @Override
    public Optional<StaffData> getStaffData(Object obj) {
        if (obj instanceof ItemStack stack)
            return stack.getCapability(CapabilityInsts.ITEMSTACKCAP).resolve();
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
    public boolean isShield(ItemStack stack, Player player) {
        return stack.getUseAnimation() == UseAnim.BLOCK;
    }

    @Override
    public EquipmentSlot slotType(ItemStack stack) {
        return stack.getEquipmentSlot();
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
    public SoundType getSoundType(BlockState state, Level level, BlockPos pos, Entity entity) {
        return state.getSoundType(level, pos, entity);
    }

    @Override
    public DamageSource createDamageSource(String name, boolean bypassArmor, boolean bypassMagic) {
        DamageSource source = new DamageSource(name);
        if (bypassArmor)
            source.bypassArmor();
        if (bypassMagic)
            source.bypassMagic();
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

            @Override
            public void fillItemList(NonNullList<ItemStack> items) {
                super.fillItemList(items);
                items.forEach(ItemNBT::initNBT);
            }
        };
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
}
