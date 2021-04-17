package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelOrcArcher;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;
import com.flemmli97.tenshilib.client.render.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderOrcArcher<T extends EntityOrcArcher> extends RenderMonster<T, ModelOrcArcher<T>> {

    public RenderOrcArcher(EntityRendererManager renderManager) {
        super(renderManager, new ModelOrcArcher<>(), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/orc.png"));
        this.layerRenderers.add(new ItemLayer<>(this));
    }
}
