package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelDeadTree;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.layer.RiderLayerRendererExt;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityDeadTree;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderDeadTree<T extends EntityDeadTree> extends RenderMonster<T, ModelDeadTree<T>> {

    public final float scale;

    public RenderDeadTree(EntityRendererProvider.Context ctx, float scale) {
        super(ctx, new ModelDeadTree<>(ctx.bakeLayer(ModelDeadTree.LAYER_LOCATION)), new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/dead_tree.png"), 0.5f, false);
        this.scale = scale;
        this.layers.add(new RiderLayerRendererExt<>(this, (stack, entity) -> stack.scale(1 / this.scale, 1 / this.scale, 1 / this.scale)));
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(this.scale, this.scale, this.scale);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.playDeath() || entity.deathTime > 0 || entity.isSleeping() || EntityData.getSleepState(entity) != EntityData.SleepState.NONE)
            return new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/dead_tree_sleep.png");
        return super.getTextureLocation(entity);
    }
}
