package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.AttackAction;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class S2CWeaponUse implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_weapon_use");

    private final AttackAction action;
    private final ItemStack stack;

    public S2CWeaponUse(AttackAction action, ItemStack hand) {
        this.action = action;
        this.stack = hand;
    }

    public static S2CWeaponUse read(FriendlyByteBuf buf) {
        return new S2CWeaponUse(AttackAction.get(buf.readUtf()), buf.readItem());
    }

    public static void handle(S2CWeaponUse pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.getWeaponHandler().doWeaponAttack(player, pkt.action, pkt.stack, null));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.action.getId());
        buf.writeItem(this.stack);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
