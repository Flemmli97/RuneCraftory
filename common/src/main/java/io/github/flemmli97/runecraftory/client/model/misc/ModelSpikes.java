package io.github.flemmli97.runecraftory.client.model.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpike;
import io.github.flemmli97.tenshilib.client.data.ModelManager;
import io.github.flemmli97.tenshilib.client.data.ReloadableCache;
import io.github.flemmli97.tenshilib.client.model.ModelPartsContainer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;

public class ModelSpikes<T extends EntitySpike> extends EntityModel<T> {

    public static final ResourceLocation LOCATION = RuneCraftory.modRes("spikes");

    protected final ReloadableCache<ModelPartsContainer> model;

    public ModelSpikes() {
        super();
        this.model = ModelManager.getInstance().getModel(LOCATION);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.model.get().getMainPart().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.model.get().resetPoses();
    }
}