package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.SkelefangModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.Skelefang;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SkelefangRender<T extends Skelefang> extends RenderMonster<T, SkelefangModel<T>> {

    public static final ResourceLocation TEXTURE = RuneCraftory.modRes("textures/entity/monsters/skelefang.png");

    public SkelefangRender(EntityRendererProvider.Context ctx) {
        super(ctx, new SkelefangModel<>(RenderType::entityTranslucent), TEXTURE, 0);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        int hurt = entity.hurtTime;
        if (entity.hasBones())
            entity.hurtTime = 0;
        super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
        entity.hurtTime = hurt;
    }
}