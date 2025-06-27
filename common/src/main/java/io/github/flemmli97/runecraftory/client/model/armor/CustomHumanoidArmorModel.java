package io.github.flemmli97.runecraftory.client.model.armor;

import io.github.flemmli97.runecraftory.client.model.SimpleGeoModel;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CustomHumanoidArmorModel<T extends Entity> extends SimpleGeoModel<T> {

    public ModelPartsContainer.ModelPartExtended head;
    public ModelPartsContainer.ModelPartExtended hat;
    public ModelPartsContainer.ModelPartExtended body;
    public ModelPartsContainer.ModelPartExtended rightArm;
    public ModelPartsContainer.ModelPartExtended leftArm;
    public ModelPartsContainer.ModelPartExtended rightLeg;
    public ModelPartsContainer.ModelPartExtended leftLeg;

    public CustomHumanoidArmorModel(ResourceLocation location) {
        super(location);
    }

    @Override
    protected ReloadableCache<ModelPartsContainer> load(ResourceLocation location) {
        return GeoModelManager.getInstance().getModel(location, model -> {
            this.head = model.getOptionalPart("head").orElse(null);
            this.hat = model.getOptionalPart("hat").orElse(null);
            this.body = model.getOptionalPart("body").orElse(null);
            this.rightArm = model.getOptionalPart("right_arm").orElse(null);
            this.leftArm = model.getOptionalPart("left_arm").orElse(null);
            this.rightLeg = model.getOptionalPart("right_leg").orElse(null);
            this.leftLeg = model.getOptionalPart("left_leg").orElse(null);
        });
    }

    public void setAllVisible(boolean visible) {
        this.head.visible = visible;
        this.hat.visible = visible;
        this.body.visible = visible;
        this.rightArm.visible = visible;
        this.leftArm.visible = visible;
        this.rightLeg.visible = visible;
        this.leftLeg.visible = visible;
    }

    public void copyFrom(HumanoidModel<?> model) {
        this.attackTime = model.attackTime;
        this.riding = model.riding;
        this.young = model.young;
        this.copyPose(model.head, this.head);
        this.copyPose(model.hat, this.hat);
        this.copyPose(model.body, this.body);
        this.copyPose(model.rightArm, this.rightArm);
        this.copyPose(model.leftArm, this.leftArm);
        this.copyPose(model.rightLeg, this.rightLeg);
        this.copyPose(model.leftLeg, this.leftLeg);
    }

    private void copyPose(ModelPart source, ModelPartsContainer.ModelPartExtended target) {
        if (target == null)
            return;
        target.x = source.x;
        target.y = source.y;
        target.z = source.z;
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
        target.xScale = source.xScale;
        target.yScale = source.yScale;
        target.zScale = source.zScale;
    }
}
