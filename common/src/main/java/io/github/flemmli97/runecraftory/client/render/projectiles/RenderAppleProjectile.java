package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityAppleProjectile;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RenderAppleProjectile extends RenderProjectileItem<EntityAppleProjectile> {

    private static final ItemStack STACK = new ItemStack(Items.APPLE);

    public RenderAppleProjectile(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ItemStack getRenderItemStack(EntityAppleProjectile entity) {
        return STACK;
    }

    @Override
    public void render(EntityAppleProjectile entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        this.scaleX = entity.getScale();
        this.scaleY = entity.getScale();
        this.scaleZ = entity.getScale();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public Type getRenderType(EntityAppleProjectile entity) {
        return Type.NORMAL;
    }
}