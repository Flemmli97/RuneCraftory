package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;
import net.minecraft.client.model.geom.ModelPart;

public class ModelOrcArcher<T extends EntityOrcArcher> extends ModelOrc<T> {

    public ModelOrcArcher(ModelPart root) {
        super(root);
        this.maze.visible = false;
    }
}
