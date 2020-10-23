package com.flemmli97.runecraftory.mobs.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.client.model.monster.ModelBeetle;
import com.flemmli97.runecraftory.mobs.client.render.RenderMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityBeetle;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderBeetle<T extends EntityBeetle> extends RenderMonster<T, ModelBeetle<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/beetle.png");

    public RenderBeetle(EntityRendererManager renderManager) {
        super(renderManager, new ModelBeetle<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

}
