package io.github.flemmli97.runecraftory.client.npc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.features.BlushFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.HairFeatureType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.IndexedColorSettingType;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class NPCTextureLayer<T extends EntityNPCBase, M extends HumanoidModel<T>, A extends PlayerModel<T>> extends RenderLayer<T, M> {

    private final A model;
    private final A slimModel;
    private final LayerType layer;

    public NPCTextureLayer(RenderLayerParent<T, M> renderer, A model, A slimModel, LayerType layer) {
        super(renderer);
        this.model = model;
        this.slimModel = slimModel;
        this.layer = layer;
    }

    public static int color(NPCFeatureContainer features, LayerType layer) {
        int color = switch (layer) {
            case SKIN_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.SKIN.get());
                if (feat == null)
                    yield 0xd5bfa7;
                yield feat.color;
            }
            case IRIS_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.IRIS.get());
                if (feat == null)
                    yield 0x000000;
                yield feat.color;
            }
            case SCLERA_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.SCLERA.get());
                if (feat == null)
                    yield 0xffffff;
                yield feat.color;
            }
            case EYEBROWS_LAYER -> {
                IndexedColorSettingType.IndexedColorFeature feat = features.getFeature(ModNPCLooks.EYEBROWS.get());
                if (feat == null)
                    yield 0xffffff;
                yield feat.color;
            }
            case BLUSH_LAYER -> {
                BlushFeatureType.BlushFeature feat = features.getFeature(ModNPCLooks.BLUSH.get());
                if (feat == null)
                    yield 0xffffff;
                yield feat.color;
            }
            case HAIR_LAYER -> {
                HairFeatureType.HairFeature feat = features.getFeature(ModNPCLooks.HAIR.get());
                if (feat == null)
                    yield 0xffffff;
                yield feat.color;
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
        A layerModel = RenderNPC.isSlim(npc) ? this.slimModel : this.model;
        this.getParentModel().copyPropertiesTo(layerModel);
        layerModel.leftPants.copyFrom(this.getParentModel().leftLeg);
        layerModel.rightPants.copyFrom(this.getParentModel().rightLeg);
        layerModel.leftSleeve.copyFrom(this.getParentModel().leftArm);
        layerModel.rightSleeve.copyFrom(this.getParentModel().rightArm);
        layerModel.jacket.copyFrom(this.getParentModel().body);
        this.setPartVisibility(layerModel);
        Minecraft mc = Minecraft.getInstance();
        boolean bl = !npc.isInvisible();
        boolean bl2 = !bl && !npc.isInvisibleTo(mc.player);
        boolean bl3 = mc.shouldEntityAppearGlowing(npc);
        RenderType renderType = this.getRenderType(npc, layerModel, bl, bl2, bl3);
        if (renderType != null) {
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            int m = LivingEntityRenderer.getOverlayCoords(npc, 0);
            int color = color(npc.lookFeatures, this.layer);
            float a = (float) (color >> 24 & 0xFF) / 255.0f;
            float r = (float) (color >> 16 & 0xFF) / 255.0f;
            float g = (float) (color >> 8 & 0xFF) / 255.0f;
            float b = (float) (color & 0xFF) / 255.0f;
            layerModel.renderToBuffer(poseStack, vertexConsumer, packedLight, m, r, g, b, a * (bl2 ? 0.15f : 1.0f));
        }
    }

    @Nullable
    protected RenderType getRenderType(T entity, A model, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (entity.getPlayDeathTick() > 0 && !entity.playDeath()) {
            if (entity.getPlayDeathTick() > 8) {
                if (entity.getPlayDeathTick() % 2 == 0)
                    return null;
            }
            if (entity.getPlayDeathTick() % 3 == 0)
                return null;
        }
        ResourceLocation resourceLocation = RenderNPC.getTextureFromLook(entity, this.layer);
        if (resourceLocation.equals(RenderNPC.EMPTY))
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

    protected void setPartVisibility(A model) {
        model.setAllVisible(false);
        model.head.visible = true;
        model.hat.visible = true;
        switch (this.layer) {
            case SKIN_LAYER, OUTFIT_LAYER: {
                model.setAllVisible(true);
            }
            case HAIR_LAYER: {
                model.body.visible = true;
                model.jacket.visible = true;
                model.rightArm.visible = true;
                model.rightSleeve.visible = true;
                model.leftArm.visible = true;
                model.leftSleeve.visible = true;
            }
        }
    }

    public enum LayerType {

        SKIN_LAYER("skin", 0),
        OUTFIT_LAYER("outft", 0.005f),
        IRIS_LAYER("iris", 0.006f),
        SCLERA_LAYER("sclera", 0.007f),
        EYEBROWS_LAYER("eyebrows", 0.008f),
        BLUSH_LAYER("blush", 0.009f),
        HAIR_LAYER("hair", 0.01f),
        HAT_LAYER("hats", 0.8f);

        public final ModelLayerLocation location, slimeLocation;
        public final float expand;

        LayerType(String name, float expand) {
            this.location = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "npc_" + name), "main");
            this.slimeLocation = new ModelLayerLocation(new ResourceLocation(RuneCraftory.MODID, "npc_slim_" + name), "main");
            this.expand = expand;
        }
    }
}
