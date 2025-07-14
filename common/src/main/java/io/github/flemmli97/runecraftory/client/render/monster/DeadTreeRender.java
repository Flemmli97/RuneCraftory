package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.DeadTreeModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.DeadTree;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DeadTreeRender<T extends DeadTree> extends RenderMonster<T, DeadTreeModel<T>> {

    public DeadTreeRender(EntityRendererProvider.Context ctx) {
        super(ctx, new DeadTreeModel<>(), RuneCraftory.modRes("textures/entity/monsters/dead_tree.png"), 0.65f);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (entity.playDeath() || entity.deathTime > 0 || entity.isSleeping() || EntityData.getSleepStateFrom(entity) != EntityData.SleepState.NONE)
            return RuneCraftory.modRes("textures/entity/monsters/dead_tree_sleep.png");
        return super.getTextureLocation(entity);
    }
}
