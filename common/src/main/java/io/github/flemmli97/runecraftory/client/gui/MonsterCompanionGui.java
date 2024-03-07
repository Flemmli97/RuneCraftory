package io.github.flemmli97.runecraftory.client.gui;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.network.C2SSetMonsterBehaviour;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
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
        List<C2SSetMonsterBehaviour.Type> buttonTypes = new ArrayList<>();
        switch (this.updatedBehaviour) {
            case WANDER_HOME -> {
                if (!this.fullParty)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.FOLLOW);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FARM);
            }
            case FOLLOW -> {
                buttonTypes.add(C2SSetMonsterBehaviour.Type.RIDE);
                if (this.hasHome)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.HOME);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FOLLOW_DISTANCE);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.STAY);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.WANDER);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FARM);
            }
            case FOLLOW_DISTANCE -> {
                buttonTypes.add(C2SSetMonsterBehaviour.Type.RIDE);
                if (this.hasHome)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.HOME);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FOLLOW);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.STAY);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.WANDER);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FARM);
            }
            case STAY -> {
                buttonTypes.add(C2SSetMonsterBehaviour.Type.RIDE);
                if (this.hasHome)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.HOME);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FOLLOW_DISTANCE);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FOLLOW);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.WANDER);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FARM);
            }
            case WANDER -> {
                if (this.hasHome)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.HOME);
                if (!this.fullParty)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.FOLLOW);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.FARM);
            }
            case FARM -> {
                if (this.hasHome)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.HOME);
                if (!this.fullParty)
                    buttonTypes.add(C2SSetMonsterBehaviour.Type.FOLLOW);
                buttonTypes.add(C2SSetMonsterBehaviour.Type.WANDER);
            }
        }
        for (C2SSetMonsterBehaviour.Type type : buttonTypes) {
            if (buttonIndex + 1 == buttonTypes.size() && buttonTypes.size() % 2 == 1)
                this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(type.translation), b -> this.handlePress(type)));
            else
                this.addRenderableWidget(new Button(this.leftPos + x + (buttonIndex % 2 == 0 ? 0 : xSize + 6), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(type.translation), b -> this.handlePress(type)));
            buttonIndex++;
        }

        if (this.minecraft.player.getMainHandItem().getItem() == ModItems.MOB_STAFF.get()) {
            if (this.updatedBehaviour == BaseMonster.Behaviour.WANDER)
                this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(C2SSetMonsterBehaviour.Type.CENTER.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Type.CENTER)));
            if (this.updatedBehaviour == BaseMonster.Behaviour.FARM) {
                this.addRenderableWidget(new Button(this.leftPos + x + (int) ((xSize + 5) * 0.5), this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(C2SSetMonsterBehaviour.Type.CENTER_FARM.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Type.CENTER_FARM)));
                buttonIndex += 2;
                this.addRenderableWidget(new Button(this.leftPos + x, this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(C2SSetMonsterBehaviour.Type.HARVESTINV.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Type.HARVESTINV)));
                this.addRenderableWidget(new Button(this.leftPos + x + xSize + 6, this.topPos + y + (buttonIndex / 2 * 23), xSize, 20, new TranslatableComponent(C2SSetMonsterBehaviour.Type.SEEDINV.translation), b -> this.handlePress(C2SSetMonsterBehaviour.Type.SEEDINV)));
            }
        }
    }

    private void handlePress(C2SSetMonsterBehaviour.Type type) {
        Platform.INSTANCE.sendToServer(new C2SSetMonsterBehaviour(this.entity.getId(), type));
        //Cause at this point the data isnt there from the server
        this.updatedBehaviour = type.behaviour;
        this.minecraft.setScreen(null);
        this.minecraft.level.playSound(this.minecraft.player, this.minecraft.player.getX(), this.minecraft.player.getY(), this.minecraft.player.getZ(), SoundEvents.NOTE_BLOCK_PLING, SoundSource.PLAYERS, 1, 1.2f);
    }
}
