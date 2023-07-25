package io.github.flemmli97.runecraftory.client.render.layer;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.ModelEnergyOrb;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityHomingEnergyOrb;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;

public class EnergyOrbSwirlLayer extends EnergySwirlLayer<EntityHomingEnergyOrb, EntityModel<EntityHomingEnergyOrb>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/energy_orb_layer.png");
    private final EntityModel<EntityHomingEnergyOrb> model;

    public EnergyOrbSwirlLayer(RenderLayerParent<EntityHomingEnergyOrb, EntityModel<EntityHomingEnergyOrb>> parent, EntityModelSet entityModelSet) {
        super(parent);
        this.model = new ModelEnergyOrb<>(entityModelSet.bakeLayer(ModelEnergyOrb.LAYER_LOCATION_LAYER));
    }

    @Override
    protected float xOffset(float f) {
        return f * 0.01f;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }

    @Override
    protected EntityModel<EntityHomingEnergyOrb> model() {
        return this.model;
    }

}
