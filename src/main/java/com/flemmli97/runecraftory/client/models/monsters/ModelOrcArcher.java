package com.flemmli97.runecraftory.client.models.monsters;

public class ModelOrcArcher extends ModelOrc{

    public ModelOrcArcher() {
        super();
        this.handRightDown.childModels.remove(this.mazeStick);
    }
}
