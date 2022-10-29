package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerWeaponHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CWeaponUse implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_weapon_use");

    private final PlayerWeaponHandler.WeaponUseState type;

    public S2CWeaponUse(PlayerWeaponHandler.WeaponUseState type) {
        this.type = type;
    }

    public static S2CWeaponUse read(FriendlyByteBuf buf) {
        return new S2CWeaponUse(buf.readEnum(PlayerWeaponHandler.WeaponUseState.class));
    }

    public static void handle(S2CWeaponUse pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            if (pkt.type == PlayerWeaponHandler.WeaponUseState.GLOVERIGHTCLICK)
                data.getWeaponHandler().startGlove(player, null);
            else
                data.getWeaponHandler().setAnimationBasedOnState(player, pkt.type);
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
}
