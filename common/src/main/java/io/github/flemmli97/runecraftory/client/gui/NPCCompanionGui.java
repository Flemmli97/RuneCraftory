package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class NPCCompanionGui extends CompanionGui<EntityNPCBase> {

    private final boolean isShopOpen;

    public NPCCompanionGui(EntityNPCBase entity, boolean isShopOpen) {
        super(entity);
        this.isShopOpen = isShopOpen;
    }

    @Override
    protected void buttons() {
        int x = 2;
        int xSize = 90;
        int y = 115;
        this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y, xSize, 20, new TranslatableComponent("gui.npc.talk"), b -> {
            Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.TALK));
            this.minecraft.setScreen(null);
        }));
        this.addRenderableWidget(new Button(this.leftPos + x + xSize + 6, this.topPos + y, xSize, 20, new TranslatableComponent("gui.npc.distance"), b -> {
            Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.FOLLOWDISTANCE));
            this.minecraft.setScreen(null);
        }));
        this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y + 23, xSize, 20, new TranslatableComponent("gui.npc.stay"), b -> {
            Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.STAY));
            this.minecraft.setScreen(null);
        }));
        this.addRenderableWidget(new Button(this.leftPos + x + xSize + 6, this.topPos + y + 23, xSize, 20, new TranslatableComponent("gui.npc.stopFollow"), b -> {
            Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.STOPFOLLOW));
            this.minecraft.setScreen(null);
        }));
        if (this.isShopOpen)
            this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + 23 * 2, xSize, 20, new TranslatableComponent("gui.npc.shop"), b -> {
                Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.SHOP));
                this.minecraft.setScreen(null);
            }));
    }

    @Override
    public void onClose() {
        super.onClose();
        Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.CLOSE));
    }
}
