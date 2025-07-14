package io.github.flemmli97.runecraftory.client.model.misc;// Made with Blockbench 3.5.2

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.HomingEnergyOrbEntity;
import io.github.flemmli97.tenshilib.client.data.GeoModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EnergyOrbModel<T extends HomingEnergyOrbEntity> extends EntityModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("entity/energy_orb");
    public static final ResourceLocation LAYER_LOCATION_LAYER = RuneCraftory.modRes("energy_orb_layer");

    private final float growth;
    protected final ReloadableCache<ModelPartsContainer> model;
    protected ModelPartsContainer.ModelPartExtended bone;

    public EnergyOrbModel(float growth) {
        super(RenderType::entityTranslucentCull);
        this.growth = growth;
        this.model = GeoModelManager.getInstance().getModel(LOCATION, model -> this.bone = model.getPart("bone"));
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.model.get().getRoot().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.get().resetPoses();
        float scale = 0.85f + Mth.sin(entity.tickCount * 0.2f) * 0.075f;
        this.bone.xScale = scale;
        this.bone.yScale = scale;
        this.bone.zScale = scale;
    }
}