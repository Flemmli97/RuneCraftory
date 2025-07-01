package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.monster.ModelRaccoon;
import io.github.flemmli97.runecraftory.client.model.monster.ModelRaccoonBase;
import io.github.flemmli97.runecraftory.client.model.monster.ModelRaccoonBerserk;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.boss.EntityRaccoon;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RenderRaccoon<T extends EntityRaccoon> extends RenderMonster<T, ModelRaccoonBase<T>> {

    private static final ResourceLocation BERSERK_TEXTURE = RuneCraftory.modRes("textures/entity/monsters/raccoon_berserk.png");
    private final ModelRaccoonBase<T> normalModel;
    private final ModelRaccoonBase<T> berserkModel;

    private boolean clone;

    public RenderRaccoon(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelRaccoon<>(), RuneCraftory.modRes("textures/entity/monsters/raccoon.png"), 0.5f);
        this.normalModel = this.model;
        this.berserkModel = new ModelRaccoonBerserk<>();
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.model == this.berserkModel ? BERSERK_TEXTURE : super.getTextureLocation(entity);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (entity.isBerserk())
            this.shadowRadius = 0.7f;
        else
            this.shadowRadius = 0.5f;
        AnimationState anim = entity.getAnimationHandler().getAnimation();
        if (anim != null && (anim.is(EntityRaccoon.TRANSFORM) || anim.is(EntityRaccoon.UNTRANSFORM))) {
            int tick = (int) anim.getTick(1);
            if (tick < 10)
                this.model = tick % 3 == 0 ? this.berserkModel : this.normalModel;
            else if (tick < 20)
                this.model = tick % 4 <= 1 ? this.berserkModel : this.normalModel;
            else if (tick < 30)
                this.model = tick % 5 <= 3 ? this.berserkModel : this.normalModel;
            if (anim.is(EntityRaccoon.UNTRANSFORM)) {
                this.model = this.model == this.berserkModel || tick > 30 ? this.normalModel : this.berserkModel;
            }
        } else
            this.model = entity.isBerserk() ? this.berserkModel : this.normalModel;
        if (!this.clone && anim != null && anim.is(EntityRaccoon.CLONE) && entity.cloneCenter().isPresent()) {
            Vec3 center = entity.cloneCenter().get();
            double dx = Mth.lerp(partialTicks, entity.getX(), entity.xOld) - center.x();
            double dy = Mth.lerp(partialTicks, entity.getY(), entity.yOld) - center.y();
            double dz = Mth.lerp(partialTicks, entity.getZ(), entity.zOld) - center.z();
            stack.pushPose();
            stack.translate(-dx, -dy, -dz);
            float tick = Mth.lerp(partialTicks, entity.tickCount, entity.tickCount + 1);
            this.clone = true;
            for (int i = 0; i < EntityRaccoon.CLONE_POS.length; i++) {
                Vec3 vec3 = EntityRaccoon.CLONE_POS[i];
                if ((tick + i * 2) % 8 <= 4)
                    continue;
                stack.pushPose();
                stack.translate(vec3.x(), vec3.y(), vec3.z());
                int rotAmount = i - entity.cloneIndex();
                stack.mulPose(Axis.YP.rotationDegrees(-rotAmount * 90));
                Minecraft.getInstance().getEntityRenderDispatcher()
                        .render(entity, 0, 0, 0, entityYaw, partialTicks, stack, buffer, packedLight);
                stack.popPose();
            }
            this.clone = false;
            stack.popPose();
        } else
            super.render(entity, entityYaw, partialTicks, stack, buffer, packedLight);
    }
}