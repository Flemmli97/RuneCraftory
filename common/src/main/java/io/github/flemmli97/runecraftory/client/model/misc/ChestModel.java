package io.github.flemmli97.runecraftory.client.model.misc;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ChestModel<T extends Entity & AnimatedEntity> extends ExtendedEntityModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/chest");

    public ModelPartsContainer.ModelPartExtended ridingPosition;

    public ChestModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    protected void onModelReload(ModelPartsContainer model) {
        this.ridingPosition = model.getPart("ridingPos");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        float partialTick = this.getPartialTick();
        this.animation.get().doAnimation(this, entity.getAnimationHandler(), partialTick);
    }
}