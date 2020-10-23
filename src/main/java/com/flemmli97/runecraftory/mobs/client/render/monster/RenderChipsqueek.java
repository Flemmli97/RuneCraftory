package com.flemmli97.runecraftory.mobs.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.client.model.monster.ModelChipsqueek;
import com.flemmli97.runecraftory.mobs.client.render.RenderMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityChipsqueek;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderChipsqueek<T extends EntityChipsqueek> extends RenderMonster<T, ModelChipsqueek<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/chipsqueek.png");

    public RenderChipsqueek(EntityRendererManager renderManager) {
        super(renderManager, new ModelChipsqueek<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }


}
