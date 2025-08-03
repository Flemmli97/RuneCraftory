package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.misc.ChestModel;
import io.github.flemmli97.runecraftory.common.entities.monster.Mimic;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

public class MimicModel<T extends Mimic> extends ChestModel<T> implements RideableModel<T> {

    public MimicModel() {
        super();
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTicks = ClientHandlers.getPartialTicks();
        if (entity.deathTime <= 0 && !entity.playDeath()) {
            if (entity.isAwake())
                this.anim.get().doAnimation(this, "open_idle", entity.tickCount, partialTicks);
            this.anim.get().doAnimation(this, "move", entity.tickCount, partialTicks, entity.interpolatedMoveTick(partialTicks));
        }
        this.anim.get().doAnimation(this, entity.getAnimationHandler(), partialTicks);
    }

    @Override
    public boolean transform(T entity, EntityRenderer<T> entityRenderer, Entity rider, EntityRenderer<?> ridingEntityRenderer, PoseStack poseStack, int riderNum) {
        this.ridingPosition.translateAndRotateWithParents(poseStack);
        ClientHandlers.translateRider(poseStack, entity, rider);
        return true;
    }
}