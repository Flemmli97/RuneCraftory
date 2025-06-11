package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.network.C2SSetMonsterBehaviour;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.List;

public class MonsterCompanionGui extends CompanionGui<BaseMonster> {

    private BaseMonster.Behaviour updatedBehaviour;
    private final boolean fullParty, hasHome;

    public MonsterCompanionGui(BaseMonster entity, boolean fullParty, boolean hasHome) {
        super(entity);
        this.fullParty = fullParty;
        this.updatedBehaviour = this.entity.behaviourState();
        this.hasHome = hasHome;
    }

    @Override
    protected void buttons() {
        int x = 2;
        int xSize = 90;
        int y = 115;
        int buttonIndex = 0;
        List<C2SSetMonsterBehaviour.Action> buttonActions = new ArrayList<>();
        switch (this.updatedBehaviour) {
            case WANDER_HOME -> {
                if (!this.fullParty)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.FOLLOW);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FARM);
            }
            case FOLLOW -> {
                buttonActions.add(C2SSetMonsterBehaviour.Action.RIDE);
                if (this.hasHome)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.HOME);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FOLLOW_DISTANCE);
                buttonActions.add(C2SSetMonsterBehaviour.Action.STAY);
                buttonActions.add(C2SSetMonsterBehaviour.Action.WANDER);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FARM);
            }
            case FOLLOW_DISTANCE -> {
                buttonActions.add(C2SSetMonsterBehaviour.Action.RIDE);
                if (this.hasHome)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.HOME);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FOLLOW);
                buttonActions.add(C2SSetMonsterBehaviour.Action.STAY);
                buttonActions.add(C2SSetMonsterBehaviour.Action.WANDER);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FARM);
            }
            case STAY -> {
                buttonActions.add(C2SSetMonsterBehaviour.Action.RIDE);
                if (this.hasHome)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.HOME);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FOLLOW_DISTANCE);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FOLLOW);
                buttonActions.add(C2SSetMonsterBehaviour.Action.WANDER);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FARM);
            }
            case WANDER -> {
                if (this.hasHome)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.HOME);
                if (!this.fullParty)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.FOLLOW);
                buttonActions.add(C2SSetMonsterBehaviour.Action.FARM);
            }
            case FARM -> {
                if (this.hasHome)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.HOME);
                if (!this.fullParty)
                    buttonActions.add(C2SSetMonsterBehaviour.Action.FOLLOW);
                buttonActions.add(C2SSetMonsterBehaviour.Action.WANDER);
            }
        }
        for (C2SSetMonsterBehaviour.Action type : buttonActions) {
            if (buttonIndex + 1 == buttonActions.size() && buttonActions.size() % 2 == 1)
                this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, Component.translatable(type.translation), b -> this.handlePress(type)));
            else
                this.addRenderableWidget(new Button(this.leftPos + x + (buttonIndex % 2 == 0 ? 0 : xSize + 6), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, Component.translatable(type.translation), b -> this.handlePress(type)));
            buttonIndex++;
        }

        if (this.minecraft.player.getMainHandItem().getItem() == ModItems.MOB_STAFF.get()) {
            if (this.updatedBehaviour == BaseMonster.Behaviour.WANDER)
                this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, Component.translatable(C2SSetMonsterBehaviour.Action.CENTER.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.CENTER)));
            if (this.updatedBehaviour == BaseMonster.Behaviour.FARM) {
                this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, Component.translatable(C2SSetMonsterBehaviour.Action.CENTER_FARM.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.CENTER_FARM)));
                buttonIndex += 2;
                this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, Component.translatable(C2SSetMonsterBehaviour.Action.HARVESTINV.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.HARVESTINV)));
                this.addRenderableWidget(new Button(this.leftPos + x + xSize + 6, this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, Component.translatable(C2SSetMonsterBehaviour.Action.SEEDINV.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.SEEDINV)));
            }
        }
    }

    private void handlePress(C2SSetMonsterBehaviour.Action action) {
        Platform.INSTANCE.sendToServer(new C2SSetMonsterBehaviour(this.entity.getId(), action));
        //Cause at this point the data isnt there from the server
        this.updatedBehaviour = action.behaviour;
        this.minecraft.setScreen(null);
        this.minecraft.level.playSound(this.minecraft.player, this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), SoundEvents.NOTE_BLOCK_PLING, SoundSource.PLAYERS, 1, 1.2f);
    }
}
