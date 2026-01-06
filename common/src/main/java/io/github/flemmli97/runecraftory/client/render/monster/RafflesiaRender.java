package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.RafflesiaModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.rafflesia.Rafflesia;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RafflesiaRender<T extends Rafflesia> extends RenderMonster<T, RafflesiaModel<T>> {

    public static final ResourceLocation TEXTURE = RuneCraftory.modRes("textures/entity/monsters/rafflesia.png");

    public RafflesiaRender(EntityRendererProvider.Context ctx) {
        super(ctx, new RafflesiaModel<>(), TEXTURE, 0.5f);
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTick, float scale) {
        super.setupRotations(entity, stack, ageInTicks, entity.getSpawnDirection().toYRot(), partialTick, scale);
    }
}