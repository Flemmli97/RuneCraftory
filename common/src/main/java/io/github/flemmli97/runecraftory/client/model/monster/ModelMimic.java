package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import io.github.flemmli97.runecraftory.client.model.ModelChest;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityMimic;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;

public class ModelMimic<T extends EntityMimic> extends ModelChest<T> {

    public ModelMimic(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.resetPoses();
        AnimatedAction anim = entity.getAnimationHandler().getAnimation();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        if (entity.isAwake())
            this.anim.doAnimation(this, "open_iddle", entity.tickCount, partialTicks);
        if (entity.isMoving())
            this.anim.doAnimation(this, "move", entity.tickCount, partialTicks);
        if (anim != null)
            this.anim.doAnimation(this, anim.getAnimationClient(), anim.getTick(), partialTicks);
    }
}