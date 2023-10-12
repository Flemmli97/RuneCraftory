package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class NPCCompanionGui extends CompanionGui<EntityNPCBase> {

    private final boolean isShopOpen;
    private final ResourceLocation quest;

    public NPCCompanionGui(EntityNPCBase entity, boolean isShopOpen, ResourceLocation quest) {
        super(entity);
        this.isShopOpen = isShopOpen;
        this.quest = quest;
    }

    @Override
    protected void buttons() {
        int x = 2;
        int xSize = 90;
        int y = 115;
        this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y, xSize, 20, new TranslatableComponent(C2SNPCInteraction.Type.TALK.translation), b -> {
            Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.TALK));
            this.minecraft.setScreen(null);
        }));
        int buttonIndex = 1;
        if (this.quest != null) {
            this.addRenderableWidget(new Button(this.leftPos + x + xSize + 6, this.topPos + y, xSize, 20, new TranslatableComponent(C2SNPCInteraction.Type.QUEST.translation), b -> {
                Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.QUEST, this.quest.toString()));
                this.minecraft.setScreen(null);
            }));
            buttonIndex++;
        }
        List<C2SNPCInteraction.Type> buttonTypes = new ArrayList<>();
        switch (this.entity.behaviourState()) {
            case FOLLOW -> {
                buttonTypes.add(C2SNPCInteraction.Type.FOLLOWDISTANCE);
                buttonTypes.add(C2SNPCInteraction.Type.STAY);
                buttonTypes.add(C2SNPCInteraction.Type.STOPFOLLOW);
            }
            case FOLLOW_DISTANCE -> {
                buttonTypes.add(C2SNPCInteraction.Type.FOLLOW);
                buttonTypes.add(C2SNPCInteraction.Type.STAY);
                buttonTypes.add(C2SNPCInteraction.Type.STOPFOLLOW);
            }
            case STAY -> {
                buttonTypes.add(C2SNPCInteraction.Type.FOLLOWDISTANCE);
                buttonTypes.add(C2SNPCInteraction.Type.FOLLOW);
                buttonTypes.add(C2SNPCInteraction.Type.STOPFOLLOW);
            }
        }

        for (C2SNPCInteraction.Type type : buttonTypes) {
            if (buttonIndex + 1 == buttonTypes.size() && buttonTypes.size() % 2 == 0)
                this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(type.translation), b -> this.handlePress(type)));
            else
                this.addRenderableWidget(new Button(this.leftPos + x + (buttonIndex % 2 == 0 ? 0 : xSize + 6), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(type.translation), b -> this.handlePress(type)));
            buttonIndex++;
        }

        if (this.isShopOpen)
            this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + 23 * 2, xSize, 20, new TranslatableComponent(C2SNPCInteraction.Type.SHOP.translation), b -> this.handlePress(C2SNPCInteraction.Type.SHOP)));
    }

    private void handlePress(C2SNPCInteraction.Type type) {
        Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), type));
        this.minecraft.setScreen(null);
    }

    @Override
    public void removed() {
        super.removed();
        Platform.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Type.CLOSE));
    }
}
