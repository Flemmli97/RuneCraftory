package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record C2SOpenInfo(C2SOpenInfo.Action action) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SOpenInfo> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_open_info"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SOpenInfo> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SOpenInfo decode(RegistryFriendlyByteBuf buf) {
            return new C2SOpenInfo(buf.readEnum(Action.class));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SOpenInfo pkt) {
            buf.writeEnum(pkt.action);
        }
    };

    public static void handle(C2SOpenInfo pkt, ServerPlayer sender) {
        switch (pkt.action) {
            case MAIN -> {
                ItemStack stack = sender.containerMenu.getCarried();
                sender.containerMenu.setCarried(ItemStack.EMPTY);
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CCapSync(Platform.INSTANCE.getPlayerData(sender)), sender);
                Platform.INSTANCE.openGuiMenu(sender, ContainerInfoScreen.create());
                if (!stack.isEmpty()) {
                    sender.containerMenu.setCarried(stack);
                }
            }
            case SUB -> {
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CCapSync(Platform.INSTANCE.getPlayerData(sender)), sender);
                Platform.INSTANCE.openGuiMenu(sender, ContainerInfoScreen.createSub());
            }
            case INV -> {
                ItemStack stack = sender.containerMenu.getCarried();
                sender.containerMenu.setCarried(ItemStack.EMPTY);
                sender.doCloseContainer();
                if (!stack.isEmpty()) {
                    sender.containerMenu.setCarried(stack);
                    //sender.connection.send(new ServerboundSetCarriedItemPacket(-1, -1, stack));
                }
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action {
        MAIN,
        SUB,
        INV
    }
}
