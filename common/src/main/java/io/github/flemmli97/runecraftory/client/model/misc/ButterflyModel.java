package io.github.flemmli97.runecraftory.client.model.misc;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.ButterflyEntity;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.resources.ResourceLocation;

public class ButterflyModel<T extends ButterflyEntity> extends ExtendedEntityModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/butterfly");

    public ModelPartsContainer.ModelPartExtended head;

    public ButterflyModel() {
        super(LOCATION, LOCATION);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.animation.get().doAnimation(this, "animation", entity.tickCount, this.getPartialTick());
    }
}