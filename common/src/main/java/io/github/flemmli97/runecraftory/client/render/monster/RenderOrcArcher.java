package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelOrc;
import io.github.flemmli97.runecraftory.client.model.monster.ModelOrcArcher;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityOrcArcher;
import io.github.flemmli97.tenshilib.client.render.ItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderOrcArcher<T extends EntityOrcArcher> extends RenderMonster<T, ModelOrcArcher<T>> {

    public RenderOrcArcher(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelOrcArcher<>(ctx.bakeLayer(ModelOrc.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/orc.png"), 0.5f);
        this.layers.add(new ItemLayer<>(this));
    }
}
