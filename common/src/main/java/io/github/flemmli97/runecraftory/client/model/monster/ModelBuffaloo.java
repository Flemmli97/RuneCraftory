package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;

public class ModelBuffaloo<T extends EntityBuffamoo> extends ModelBuffamoo<T> {

    public ModelBuffaloo() {
        super();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().getPart("udder").visible = false;
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
