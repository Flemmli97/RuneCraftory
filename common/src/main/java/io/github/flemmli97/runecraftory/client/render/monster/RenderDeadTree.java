package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelDeadTree;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityDeadTree;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderDeadTree<T extends EntityDeadTree> extends ScaledEntityRenderer<T, ModelDeadTree<T>> {

    public RenderDeadTree(EntityRendererProvider.Context ctx, float scale) {
        super(ctx, new ModelDeadTree<>(), RuneCraftory.modRes("textures/entity/monsters/dead_tree.png"), scale, 0.65f * scale);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.playDeath() || entity.deathTime > 0 || entity.isSleeping() || EntityData.getSleepStateFrom(entity) != EntityData.SleepState.NONE)
            return RuneCraftory.modRes("textures/entity/monsters/dead_tree_sleep.png");
        return super.getTextureLocation(entity);
    }
}
