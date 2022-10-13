package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.network.C2SSetMonsterBehaviour;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class MonsterCompanionGui extends CompanionGui<BaseMonster> {

    private int updatedBehaviour = -1;

    public MonsterCompanionGui(BaseMonster entity) {
        super(entity);
    }

    @Override
    protected void buttons() {
        int x = 2;
        int xSize = 90;
        int y = 115;
        this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y, xSize, 20, new TranslatableComponent("gui.companion.behaviour.wander"), b -> this.handlePress(C2SSetMonsterBehaviour.Type.WANDER)));
        this.addRenderableWidget(new Button(this.leftPos + x + xSize + 5, this.topPos + y, xSize, 20, new TranslatableComponent("gui.companion.behaviour.follow"), b -> this.handlePress(C2SSetMonsterBehaviour.Type.FOLLOW)));
        this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y + 23, xSize, 20, new TranslatableComponent("gui.companion.behaviour.stay"), b -> this.handlePress(C2SSetMonsterBehaviour.Type.STAY)));
        this.addRenderableWidget(new Button(this.leftPos + x + xSize + 5, this.topPos + y + 23, xSize, 20, new TranslatableComponent("gui.companion.behaviour.farm"), b -> this.handlePress(C2SSetMonsterBehaviour.Type.FARM)));

        if (this.updatedBehaviour == -1) {
            BaseMonster.Behaviour behaviour = this.entity.behaviourState();
            if (behaviour == BaseMonster.Behaviour.FARM)
                this.updatedBehaviour = 2;
            else if (behaviour == BaseMonster.Behaviour.WANDER)
                this.updatedBehaviour = 1;
        }
        if (this.updatedBehaviour >= 1)
            this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + 23 * 2, xSize, 20, new TranslatableComponent("gui.companion.behaviour.home"), b -> this.handlePress(C2SSetMonsterBehaviour.Type.HOME)));
        if (this.updatedBehaviour >= 2) {
            this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y + 23 * 3, xSize, 20, new TranslatableComponent("gui.companion.behaviour.inventory.harvest"), b -> this.handlePress(C2SSetMonsterBehaviour.Type.HARVESTINV)));
            this.addRenderableWidget(new Button(this.leftPos + x + xSize + 5, this.topPos + y + 23 * 3, xSize, 20, new TranslatableComponent("gui.companion.behaviour.inventory.seed"), b -> this.handlePress(C2SSetMonsterBehaviour.Type.SEEDINV)));
        }
    }

    private void handlePress(C2SSetMonsterBehaviour.Type type) {
        Platform.INSTANCE.sendToServer(new C2SSetMonsterBehaviour(this.entity.getId(), type));
        //Cause at this point the data isnt there from the server
        switch (type) {
            case STAY, FOLLOW -> this.updatedBehaviour = 0;
            case WANDER -> this.updatedBehaviour = 1;
            case FARM -> this.updatedBehaviour = 2;
            case HOME -> {
                this.updatedBehaviour = this.entity.behaviourState() == BaseMonster.Behaviour.FARM ? 2 : 1;
                this.minecraft.setScreen(null);
                this.minecraft.level.playSound(this.minecraft.player, this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), SoundEvents.NOTE_BLOCK_PLING, SoundSource.PLAYERS, 1, 1.2f);
                return;
            }
            case SEEDINV, HARVESTINV -> {
                this.minecraft.setScreen(null);
                this.minecraft.level.playSound(this.minecraft.player, this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), SoundEvents.NOTE_BLOCK_PLING, SoundSource.PLAYERS, 1, 1.2f);
                return;
            }
        }
        this.clearWidgets();
        this.buttons();
    }
}
