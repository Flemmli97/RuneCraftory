package com.flemmli97.runecraftory.mobs.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.client.model.monster.ModelOrcArcher;
import com.flemmli97.runecraftory.mobs.client.render.ItemLayer;
import com.flemmli97.runecraftory.mobs.client.render.RenderMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityOrcArcher;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class RenderOrcArcher<T extends EntityOrcArcher> extends RenderMonster<T, ModelOrcArcher<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/orc.png");

    public RenderOrcArcher(EntityRendererManager renderManager) {
        super(renderManager, new ModelOrcArcher<>());
        this.layerRenderers.add(new ItemLayer<>(this));
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

}
