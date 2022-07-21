package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCards;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderCards extends RenderTexture<EntityCards> {

    private static final ResourceLocation tex1 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_b_1.png");
    private static final ResourceLocation tex2 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_b_2.png");
    private static final ResourceLocation tex3 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_b_3.png");
    private static final ResourceLocation tex4 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_b_4.png");
    private static final ResourceLocation tex5 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_r_1.png");
    private static final ResourceLocation tex6 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_r_2.png");
    private static final ResourceLocation tex7 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_r_3.png");
    private static final ResourceLocation tex8 = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/card_r_4.png");

    public RenderCards(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCards entity) {
        return switch (entity.getCardType()) {
            case 1 -> tex2;
            case 2 -> tex3;
            case 3 -> tex4;
            case 4 -> tex5;
            case 5 -> tex6;
            case 6 -> tex7;
            case 7 -> tex8;
            default -> tex1;
        };
    }

    @Override
    public void render(EntityCards entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(0.8f, 0.8f, 0.8f);
        stack.translate(0, 0.3, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public boolean facePlayer() {
        return false;
    }

    @Override
    public float yawOffset() {
        return 90.0F;
    }

    @Override
    public float pitchOffset() {
        return -90.0F;
    }
}