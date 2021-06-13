package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelAmbrosia;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderAmbrosia<T extends EntityAmbrosia> extends RenderMonster<T, ModelAmbrosia<T>> {

    public RenderAmbrosia(EntityRendererManager renderManager) {
        super(renderManager, new ModelAmbrosia<>(), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/ambrosia.png"));
    }

    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 0;
    }
}
