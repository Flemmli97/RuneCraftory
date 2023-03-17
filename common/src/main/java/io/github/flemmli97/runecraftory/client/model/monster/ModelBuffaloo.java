package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
import io.github.flemmli97.tenshilib.client.model.ModelPartHandler;
import net.minecraft.client.model.geom.ModelPart;

public class ModelBuffaloo<T extends EntityBuffamoo> extends ModelBuffamoo<T> {

    public ModelPartHandler.ModelPartExtended udder;

    public ModelBuffaloo(ModelPart root) {
        super(root);
        this.udder = this.model.getPart("udder");
        this.udder.visible = false;
    }
}
