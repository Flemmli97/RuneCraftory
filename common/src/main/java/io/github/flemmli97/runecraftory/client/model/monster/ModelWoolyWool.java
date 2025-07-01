package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public class ModelWoolyWool<T extends EntityWooly> extends EntityModel<T> implements ExtendedModel {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("wooly_wool");

    private final ReloadableCache<ModelPartsContainer> model;

    public ModelPartsContainer.ModelPartExtended bodyMain;
    public ModelPartsContainer.ModelPartExtended body;
    public ModelPartsContainer.ModelPartExtended bodyUp;
    public ModelPartsContainer.ModelPartExtended armLeftBase;
    public ModelPartsContainer.ModelPartExtended armRightBase;
    public ModelPartsContainer.ModelPartExtended feetLeftBase;
    public ModelPartsContainer.ModelPartExtended feetRightBase;

    public ModelWoolyWool() {
        super();
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> {
            this.bodyMain = model.getPart("bodyCenter");
            this.body = model.getPart("body");
            this.bodyUp = model.getPart("bodyUp");
            this.armLeftBase = model.getPart("armLeftBase");
            this.armRightBase = model.getPart("armRightBase");
            this.feetLeftBase = model.getPart("feetLeftBase");
            this.feetRightBase = model.getPart("feetRightBase");
        });
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }

    public void syncModel(ModelWooly<T> model) {
        this.sync(this.bodyMain, model.bodyMain);
        this.sync(this.body, model.body);
        this.sync(this.bodyUp, model.bodyUp);
        this.sync(this.armLeftBase, model.armLeftBase);
        this.sync(this.armRightBase, model.armRightBase);
        this.sync(this.feetLeftBase, model.feetLeftBase);
        this.sync(this.feetRightBase, model.feetRightBase);
    }

    private void sync(ModelPartsContainer.ModelPartExtended model, ModelPartsContainer.ModelPartExtended other) {
        model.xRot = other.xRot;
        model.yRot = other.yRot;
        model.zRot = other.zRot;
        model.x = other.x;
        model.y = other.y;
        model.z = other.z;
        model.xScale = other.xScale;
        model.yScale = other.yScale;
        model.zScale = other.zScale;
    }
}