package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelRafflesia;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.EntityRafflesia;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderRafflesia<T extends EntityRafflesia> extends RenderMonster<T, ModelRafflesia<T>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/rafflesia.png");

    public RenderRafflesia(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelRafflesia<>(ctx.bakeLayer(ModelRafflesia.LAYER_LOCATION)), TEXTURE, 0.5f);
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks) {
        super.setupRotations(entity, stack, ageInTicks, entity.getSpawnDirection().toYRot(), partialTicks);
    }
}