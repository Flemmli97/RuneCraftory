package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelGoblin;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityGoblin;
import com.flemmli97.tenshilib.client.render.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderGoblin<T extends EntityGoblin> extends RenderMonster<T, ModelGoblin<T>> {

    public RenderGoblin(EntityRendererManager renderManager) {
        super(renderManager, new ModelGoblin<>(), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/goblin.png"));
        this.layerRenderers.add(new ItemLayer<>(this));
    }
}
