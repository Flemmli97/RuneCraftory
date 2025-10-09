package io.github.flemmli97.runecraftory.client.render.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.client.model.HumanoidBasedModel;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.features.BlushFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.FaceFeaturesType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.HairFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexedColorSettingType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.ModelFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;

public class NPCTextureLayer<T extends NPCEntity> extends RenderLayer<T, HumanoidBasedModel<T>> {

    private final NPCRender<T> renderer;
    protected LayerType layer;

    public NPCTextureLayer(NPCRender<T> renderer, LayerType layer) {
        super(renderer);
        this.renderer = renderer;
        this.layer = layer;
    }

    public static int setColor(NPCEntity entity, LayerType layer) {
        if (layer == LayerType.SKIN_LAYER) {
            if (entity.getLook().value().playerSkin() != null) {
                return 0xffffffff;
            } else {
                ModelFeatureType.ModelFeature modelFeature = entity.lookFeatures.getFeature(RuneCraftoryNPCLooks.MODEL.get());
                if (modelFeature != null && modelFeature.model().flatMap(ModelFeatureType.ModelData::texture).orElse(null) != null)
                    return 0xffffffff;
            }
        }
        return setColor(entity.lookFeatures, layer);
    }

    public static int setColor(NPCFeatureContainer features, LayerType layer) {
        int color = switch (layer) {
            case SKIN_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(RuneCraftoryNPCLooks.SKIN.get());
                if (feat == null)
                    yield 0xd5bfa7;
                yield feat.color();
            }
            case IRIS_LAYER -> {
                FaceFeaturesType.FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
                if (feat == null || feat.iris() == null)
                    yield 0x000000;
                yield feat.iris().color();
            }
            case SCLERA_LAYER -> {
                FaceFeaturesType.FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
                if (feat == null || feat.sclera() == null)
                    yield 0x000000;
                yield feat.sclera().color();
            }
            case EYEBROWS_LAYER -> {
                FaceFeaturesType.FaceFeatures feat = features.getFeature(RuneCraftoryNPCLooks.FACE.get());
                if (feat == null || feat.eyebrow() == null)
                    yield 0x000000;
                yield feat.eyebrow().color();
            }
            case BLUSH_LAYER -> {
                BlushFeatureType.BlushFeature feat = features.getFeature(RuneCraftoryNPCLooks.BLUSH.get());
                if (feat == null)
                    yield 0xffffff;
                yield feat.color();
            }
            case HAIR_LAYER -> {
                HairFeatureType.HairFeature feat = features.getFeature(RuneCraftoryNPCLooks.HAIR.get());
                if (feat == null)
                    yield 0xffffff;
                yield feat.color();
            }
            default -> 0xffffff;
        };
        int a = 255;
        if (layer == LayerType.BLUSH_LAYER)
            a = 88;
        return a << 24 | color;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T npc, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        HumanoidBasedModel<T> layerModel = this.getModel();
        this.setup(layerModel);
        poseStack.pushPose();
        this.actualRender(poseStack, buffer, packedLight, npc, layerModel);
        poseStack.popPose();
    }

    protected HumanoidBasedModel<T> getModel() {
        return this.renderer.getCurrent().get(this.layer.modelType);
    }

    protected void setup(HumanoidBasedModel<T> layerModel) {
        this.getParentModel().copyPropertiesTo(layerModel);
        this.setPartVisibility(layerModel);
    }

    protected void actualRender(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T npc, HumanoidBasedModel<T> layerModel) {
        Minecraft mc = Minecraft.getInstance();
        boolean bl = !npc.isInvisible();
        boolean bl2 = !bl && !npc.isInvisibleTo(mc.player);
        boolean bl3 = mc.shouldEntityAppearGlowing(npc);
        RenderType renderType = this.getRenderType(npc, layerModel, bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            int m = LivingEntityRenderer.getOverlayCoords(npc, 0);
            int color = this.setColor(npc);
            float a = (float) (color >> 24 & 0xFF) / 255.0f;
            float r = (float) (color >> 16 & 0xFF) / 255.0f;
            float g = (float) (color >> 8 & 0xFF) / 255.0f;
            float b = (float) (color & 0xFF) / 255.0f;
            layerModel.renderToBuffer(poseStack, vertexConsumer, packedLight, m,
                    FastColor.ARGB32.colorFromFloat(a * (bl2 ? 0.15f : 1.0f), r, g, b));
        }
    }

    @Nullable
    protected RenderType getRenderType(T entity, HumanoidBasedModel<T> model, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (entity.getPlayDeathTick() > 0 && !entity.playDeath()) {
            if (entity.getPlayDeathTick() > 8) {
                if (entity.getPlayDeathTick() % 2 == 0)
                    return null;
            }
            if (entity.getPlayDeathTick() % 3 == 0)
                return null;
        }
        ResourceLocation resourceLocation = this.getTexture(entity);
        if (resourceLocation.equals(NPCRender.EMPTY))
            return null;
        if (translucent) {
            return RenderType.itemEntityTranslucentCull(resourceLocation);
        }
        if (bodyVisible) {
            return model.renderType(resourceLocation);
        }
        if (glowing) {
            return RenderType.outline(resourceLocation);
        }
        return null;
    }

    protected int setColor(T entity) {
        return setColor(entity, this.layer);
    }

    protected ResourceLocation getTexture(T entity) {
        return NPCRender.getTextureFromLook(entity, this.layer, null);
    }

    protected void setPartVisibility(HumanoidBasedModel<T> model) {
        // Visibility is set from main renderer for skin layer
        if (this.layer == LayerType.SKIN_LAYER)
            return;
        model.setAllVisible(false);
        model.head.visible = this.renderer.getModel().head.visible;
        switch (this.layer) {
            case OUTFIT_LAYER -> model.copyVisibilityFrom(this.renderer.getModel());
            case HAIR_LAYER -> {
                model.body.visible = this.renderer.getModel().body.visible;
                model.rightArm.visible = this.renderer.getModel().rightArm.visible;
                model.leftArm.visible = this.renderer.getModel().leftArm.visible;
            }
        }
    }

    public enum LayerType {

        SKIN_LAYER(ModelType.SKIN_LAYER),
        OUTFIT_LAYER(ModelType.OUTFIT_LAYER),
        IRIS_LAYER(ModelType.FACE_LAYER),
        SCLERA_LAYER(null),
        EYEBROWS_LAYER(null),
        BLUSH_LAYER(ModelType.BLUSH_LAYER),
        HAIR_LAYER(ModelType.HAIR_LAYER),
        HAT_LAYER(ModelType.HAT_LAYER);

        public final ModelType modelType;

        LayerType(ModelType modelType) {
            this.modelType = modelType;
        }
    }

    public enum ModelType {

        SKIN_LAYER(0),
        OUTFIT_LAYER(0.005f),
        FACE_LAYER(0.006f),
        BLUSH_LAYER(0.009f),
        HAIR_LAYER(0.5f),
        HAT_LAYER(0.8f);

        public final float expand;

        ModelType(float expand) {
            this.expand = expand;
        }
    }
}
