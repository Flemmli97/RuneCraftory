package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.FaceFeaturesType;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;

public class NPCFaceLayer<T extends EntityNPCBase, M extends HumanoidModel<T>, A extends PlayerModel<T>> extends NPCTextureLayer<T, M, A> {

    private String textureType;

    public NPCFaceLayer(RenderLayerParent<T, M> renderer, A model, A slimModel) {
        super(renderer, model, slimModel, LayerType.IRIS_LAYER);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        A layerModel = this.getModel(entity);
        this.setup(layerModel);
        this.textureType = null;
        if (entity.tickCount % 50 <= 2 || entity.isSleeping() || entity.isDeadOrDying() || entity.playDeath())
            this.textureType = "eyes_closed";
        this.layer = LayerType.IRIS_LAYER;
        this.actualRender(poseStack, buffer, packedLight, entity, layerModel);
        this.layer = LayerType.SCLERA_LAYER;
        float scale = 1 + this.layer.expand;
        poseStack.scale(scale, scale, scale);
        this.actualRender(poseStack, buffer, packedLight, entity, layerModel);
        this.layer = LayerType.EYEBROWS_LAYER;
        scale = 1 + this.layer.expand;
        poseStack.scale(scale, scale, scale);
        this.actualRender(poseStack, buffer, packedLight, entity, layerModel);
    }

    @Override
    protected int setColor(T entity) {
        if (this.textureType != null && (this.layer == LayerType.IRIS_LAYER || this.layer == LayerType.SCLERA_LAYER)) {
            FaceFeaturesType.FaceFeatures feat = entity.lookFeatures.getFeature(ModNPCLooks.FACE.get());
            boolean skin = this.layer == LayerType.IRIS_LAYER ?
                    feat.useSkinColor(entity.lookFeatures, this.textureType, FaceFeaturesType.ExpressionType.IRIS)
                    : feat.useSkinColor(entity.lookFeatures, this.textureType, FaceFeaturesType.ExpressionType.SCLERA);
            if (skin) {
                return setColor(entity.lookFeatures, LayerType.SKIN_LAYER);
            }
        }
        return setColor(entity.lookFeatures, this.layer);
    }

    @Override
    protected ResourceLocation getTexture(T entity) {
        return RenderNPC.getTextureFromLook(entity, this.layer, this.textureType);
    }
}
