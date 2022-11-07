package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerInfoScreen;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public record C2SOpenInfo(C2SOpenInfo.Type type) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "c2s_open_info");

    public static C2SOpenInfo read(FriendlyByteBuf buf) {
        return new C2SOpenInfo(buf.readEnum(Type.class));
    }

    public static void handle(C2SOpenInfo pkt, ServerPlayer sender) {
        if (sender != null) {
            switch (pkt.type) {
                case MAIN -> {
                    ItemStack stack = sender.containerMenu.getCarried();
                    sender.containerMenu.setCarried(ItemStack.EMPTY);
                    Platform.INSTANCE.sendToClient(new S2CCapSync(Platform.INSTANCE.getPlayerData(sender).orElseThrow(EntityUtils::playerDataException)), sender);
                    Platform.INSTANCE.openGuiMenu(sender, ContainerInfoScreen.create());
                    if (!stack.isEmpty()) {
                        sender.containerMenu.setCarried(stack);
                    }
                }
                case SUB -> {
                    Platform.INSTANCE.sendToClient(new S2CCapSync(Platform.INSTANCE.getPlayerData(sender).orElseThrow(EntityUtils::playerDataException)), sender);
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
        MAIN,
        SUB,
        INV
    }
}
