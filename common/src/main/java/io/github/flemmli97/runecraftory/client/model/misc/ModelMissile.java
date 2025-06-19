package io.github.flemmli97.runecraftory.client.model.misc;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMissile;
import io.github.flemmli97.tenshilib.client.data.AnimationManager;
import io.github.flemmli97.tenshilib.client.data.ModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.BedrockAnimations;
import io.github.flemmli97.tenshilib.client.model.ExtendedModel;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public class ModelMissile<T extends EntityMissile> extends EntityModel<T> implements ExtendedModel {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("missile");

    protected final ReloadableCache<ModelPartsContainer> model;
    protected final ReloadableCache<BedrockAnimations> anim;

    public ModelPartsContainer.ModelPartExtended head;

    public ModelMissile() {
        super();
        this.model = ModelManager.getInstance().getModel(LOCATION);
        this.anim = AnimationManager.getInstance().getAnimation(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getModel().getMainPart().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getModel().resetPoses();
        this.anim.get().doAnimation(this, "animation", entity.tickCount, ClientHandlers.getPartialTicks());
    }

    @Override
    public ModelPartsContainer getModel() {
        return this.model.get();
    }
}