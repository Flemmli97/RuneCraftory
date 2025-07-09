package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.C2SNPCInteraction;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
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
        int y = 120;
        this.addRenderableWidget(Button.builder(Component.translatable(C2SNPCInteraction.Action.TALK.translation), b -> {
            LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.TALK));
            this.minecraft.setScreen(null);
        }).bounds(this.leftPos + x, this.topPos + y, xSize, 20).build());
        int buttonIndex = 1;
        if (this.quest != null) {
            this.addRenderableWidget(Button.builder(Component.translatable(C2SNPCInteraction.Action.QUEST.translation), b -> {
                LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.QUEST, this.quest.toString()));
                this.minecraft.setScreen(null);
            }).bounds(this.leftPos + x + xSize + 6, this.topPos + y, xSize, 20).build());
            buttonIndex++;
        }
        List<C2SNPCInteraction.Action> buttonTypes = new ArrayList<>();
        switch (this.entity.behaviourState()) {
            case FOLLOW -> {
                buttonTypes.add(C2SNPCInteraction.Action.FOLLOWDISTANCE);
                buttonTypes.add(C2SNPCInteraction.Action.STAY);
                buttonTypes.add(C2SNPCInteraction.Action.STOPFOLLOW);
            }
            case FOLLOW_DISTANCE -> {
                buttonTypes.add(C2SNPCInteraction.Action.FOLLOW);
                buttonTypes.add(C2SNPCInteraction.Action.STAY);
                buttonTypes.add(C2SNPCInteraction.Action.STOPFOLLOW);
            }
            case STAY -> {
                buttonTypes.add(C2SNPCInteraction.Action.FOLLOWDISTANCE);
                buttonTypes.add(C2SNPCInteraction.Action.FOLLOW);
                buttonTypes.add(C2SNPCInteraction.Action.STOPFOLLOW);
            }
        }

        for (C2SNPCInteraction.Action type : buttonTypes) {
            if (buttonIndex + 1 == buttonTypes.size() && buttonTypes.size() % 2 == 0)
                this.addRenderableWidget(Button.builder(Component.translatable(type.translation), b -> this.handlePress(type))
                        .bounds(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20).build());
            else
                this.addRenderableWidget(Button.builder(Component.translatable(type.translation), b -> this.handlePress(type))
                        .bounds(this.leftPos + x + (buttonIndex % 2 == 0 ? 0 : xSize + 6), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20).build());
            buttonIndex++;
        }

        if (this.isShopOpen)
            this.addRenderableWidget(Button.builder(Component.translatable(C2SNPCInteraction.Action.SHOP.translation), b -> this.handlePress(C2SNPCInteraction.Action.SHOP))
                    .bounds(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + 23 * 2, xSize, 20).build());
    }

    private void handlePress(C2SNPCInteraction.Action type) {
        LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), type));
        this.minecraft.setScreen(null);
    }

    @Override
    public void removed() {
        super.removed();
        LoaderNetwork.INSTANCE.sendToServer(new C2SNPCInteraction(this.entity.getId(), C2SNPCInteraction.Action.CLOSE));
    }
}
