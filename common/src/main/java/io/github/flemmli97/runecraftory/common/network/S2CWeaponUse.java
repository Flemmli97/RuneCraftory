package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CWeaponUse implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_weapon_use");

    private final Type type;

    public S2CWeaponUse(Type type) {
        this.type = type;
    }

    public static S2CWeaponUse read(FriendlyByteBuf buf) {
        return new S2CWeaponUse(buf.readEnum(Type.class));
    }

    public static void handle(S2CWeaponUse pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            if (pkt.type == Type.GLOVERIGHTCLICK)
                data.getWeaponHandler().startGlove(player, null);
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.type);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Type {
        GLOVERIGHTCLICK
    }
}
