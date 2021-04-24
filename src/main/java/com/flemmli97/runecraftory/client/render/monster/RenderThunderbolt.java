package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelThunderbolt;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityThunderbolt;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderThunderbolt<T extends EntityThunderbolt> extends RenderMonster<T, ModelThunderbolt<T>> {

    public RenderThunderbolt(EntityRendererManager renderManager) {
        super(renderManager, new ModelThunderbolt<>(), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/thunderbolt.png"));
    }

    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 0;
    }
}