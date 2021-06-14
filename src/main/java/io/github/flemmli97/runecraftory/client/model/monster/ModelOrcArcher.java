package io.github.flemmli97.runecraftory.client.model.monster;

import com.flemmli97.tenshilib.mixin.ModelRendererAccessors;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;

public class ModelOrcArcher<T extends EntityOrcArcher> extends ModelOrc<T> {

    public ModelOrcArcher() {
        super();
        ((ModelRendererAccessors) this.handRightDown).getChildModels().remove(this.mazeStick);
    }
}
