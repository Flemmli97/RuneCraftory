package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class S2CWeaponUse implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_weapon_use");

    private final AttackAction action;
    private final ItemStack stack;
    private final int count, entity;

    public S2CWeaponUse(AttackAction action, ItemStack hand, int count, LivingEntity entity) {
        this.action = action;
        this.stack = hand;
        this.count = count;
        this.entity = entity.getId();
    }

    private S2CWeaponUse(AttackAction action, ItemStack hand, int count, int entity) {
        this.action = action;
        this.stack = hand;
        this.count = count;
        this.entity = entity;
    }

    public static S2CWeaponUse read(FriendlyByteBuf buf) {
        return new S2CWeaponUse(ModAttackActions.ATTACK_ACTION_REGISTRY.get().getFromId(buf.readResourceLocation()), buf.readItem(), buf.readInt(), buf.readInt());
    }

    public static void handle(S2CWeaponUse pkt) {
        Player client = ClientHandlers.getPlayer();
        if (client == null)
            return;
        Entity target = client.level.getEntity(pkt.entity);
        if (target instanceof Player player)
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.getWeaponHandler().clientSideUpdate(player, pkt.action, pkt.stack, pkt.count));
        else if (target instanceof EntityNPCBase npc)
            npc.weaponHandler.clientSideUpdate(npc, pkt.action, pkt.stack, pkt.count);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.action.getRegistryName());
        buf.writeItem(this.stack);
        buf.writeInt(this.count);
        buf.writeInt(this.entity);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
