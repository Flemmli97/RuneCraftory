package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelWooly;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderWooly<T extends EntityWooly> extends RenderMonster<T, ModelWooly<T>> {

    public RenderWooly(EntityRendererManager renderManager) {
        super(renderManager, new ModelWooly<>(), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly.png"));
        this.layerRenderers.add(new LayerWooly<>(this));
    }

}
