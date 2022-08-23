package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class S2CMoney implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_money");

    private final int money;

    private S2CMoney(int money) {
        this.money = money;
    }

    public S2CMoney(PlayerData data) {
        this.money = data.getMoney();
    }

    public static S2CMoney read(FriendlyByteBuf buf) {
        return new S2CMoney(buf.readInt());
    }

    public static void handle(S2CMoney pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.setMoney(player, pkt.money));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.money);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
