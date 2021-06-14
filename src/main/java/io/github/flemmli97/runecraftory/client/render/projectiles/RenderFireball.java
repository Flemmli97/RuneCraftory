package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.flemmli97.tenshilib.client.render.RenderTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderFireball extends RenderTexture<EntityFireball> {

    private static final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/fireball_n.png");

    public RenderFireball(EntityRendererManager renderManager) {
        super(renderManager, 1, 1, 6, 1);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFireball entity) {
        return tex;
    }

    @Override
    public float[] uvOffset(int timer) {
        return super.uvOffset((int) (timer * 0.5));
    }


    @Override
    public void render(EntityFireball entity, float rotation, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight) {
        stack.push();
        stack.translate(0, this.ySize * 0.25, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.pop();
    }
}
