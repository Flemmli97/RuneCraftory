package com.flemmli97.runecraftory.mobs.client.model.monster;

import com.flemmli97.runecraftory.mobs.entity.monster.EntityOrcArcher;
import com.flemmli97.tenshilib.mixin.ModelRendererAccessors;

public class ModelOrcArcher<T extends EntityOrcArcher> extends ModelOrc<T> {

    public ModelOrcArcher() {
        super();
        ((ModelRendererAccessors) this.handRightDown).getChildModels().remove(this.mazeStick);
    }
}
