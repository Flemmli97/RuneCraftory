package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.network.C2SSetMonsterBehaviour;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

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
        int y = 120;
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
        List<BiFunction<Integer, Integer, Button>> buttons = new ArrayList<>();
        for (C2SSetMonsterBehaviour.Action type : buttonActions) {
            buttons.add((xOff, yOff) -> Button.builder(Component.translatable(type.translation), b -> this.handlePress(type))
                    .bounds(this.leftPos + x + xOff, this.topPos + y + yOff, xSize, 20)
                    .build());
        }
        if (this.minecraft.player.getMainHandItem().getItem() == RuneCraftoryItems.MOB_STAFF.get()) {
            if (this.updatedBehaviour == BaseMonster.Behaviour.WANDER)
                buttons.add((xOff, yOff) -> Button.builder(Component.translatable(C2SSetMonsterBehaviour.Action.CENTER.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.CENTER))
                        .bounds(this.leftPos + x + xOff, this.topPos + y + yOff, xSize, 20)
                        .build());
            if (this.updatedBehaviour == BaseMonster.Behaviour.FARM) {
                buttons.add((xOff, yOff) -> Button.builder(Component.translatable(C2SSetMonsterBehaviour.Action.CENTER_FARM.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.CENTER_FARM))
                        .bounds(this.leftPos + x + xOff, this.topPos + y + yOff, xSize, 20)
                        .build());
                buttons.add((xOff, yOff) -> Button.builder(Component.translatable(C2SSetMonsterBehaviour.Action.HARVESTINV.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.HARVESTINV))
                        .bounds(this.leftPos + x + xOff, this.topPos + y + yOff, xSize, 20)
                        .build());
                buttons.add((xOff, yOff) -> Button.builder(Component.translatable(C2SSetMonsterBehaviour.Action.SEEDINV.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Action.SEEDINV))
                        .bounds(this.leftPos + x + xOff, this.topPos + y + yOff, xSize, 20)
                        .build());
            }
        }

        for (BiFunction<Integer, Integer, Button> button : buttons) {
            if (buttonIndex + 1 == buttons.size() && buttons.size() % 2 == 1)
                this.addRenderableWidget(button.apply((int) ((xSize + 5) * 0.5), (buttonIndex / 2 * 23)));
            else
                this.addRenderableWidget(button.apply((buttonIndex % 2 == 0 ? 0 : xSize + 6), (buttonIndex / 2 * 23)));
            buttonIndex++;
        }
    }

    private void handlePress(C2SSetMonsterBehaviour.Action action) {
        LoaderNetwork.INSTANCE.sendToServer(new C2SSetMonsterBehaviour(this.entity.getId(), action));
        //Cause at this point the data isnt there from the server
        this.updatedBehaviour = action.behaviour;
        this.minecraft.setScreen(null);
        this.minecraft.level.playSound(this.minecraft.player, this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), SoundEvents.NOTE_BLOCK_PLING, SoundSource.PLAYERS, 1, 1.2f);
    }
}
