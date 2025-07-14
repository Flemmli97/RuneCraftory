package io.github.flemmli97.runecraftory.client.model.monster;

import io.github.flemmli97.runecraftory.common.entities.monster.Buffamoo;

public class BuffalooModel<T extends Buffamoo> extends BuffamooModel<T> {

    public BuffalooModel() {
        super();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().getPart("udder").visible = false;
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}
