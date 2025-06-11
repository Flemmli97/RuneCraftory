package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

import java.util.ArrayList;
import java.util.List;

public class AttackAABBRender {

    private static final float[] ATTACK_RGB = new float[]{18 / 255f, 181 / 255f, 51 / 255f};
    private static final float[] ATTEMPT_RGB = new float[]{19 / 255f, 56 / 255f, 191 / 255f};
    private static final float[] PLAYER_RGB = new float[]{255 / 255f, 224 / 255f, 214 / 255f};

    public static final AttackAABBRender INST = new AttackAABBRender();
    private final List<RenderBB> list = new ArrayList<>();
    private final List<RenderBB> toAdd = new ArrayList<>();

    public void addNewAABB(OrientedBoundingBox obb, int duration, S2CAttackDebug.EnumAABBType type) {
        this.toAdd.add(new RenderBB(obb, duration, type));
    }

    public void render(PoseStack stack, MultiBufferSource.BufferSource buffer) {
        this.list.addAll(this.toAdd);
        this.toAdd.clear();
        this.list.removeIf(r -> r.render(stack, buffer));
        buffer.endBatch(RenderType.LINES);
    }

    private static class RenderBB {

        private final OrientedBoundingBox obb;
        private final S2CAttackDebug.EnumAABBType type;
        private int duration;

        public RenderBB(OrientedBoundingBox obb, int duration, S2CAttackDebug.EnumAABBType type) {
            this.obb = obb;
            this.duration = duration;
            this.type = type;
        }

        public boolean render(PoseStack stack, MultiBufferSource buffer) {
            if (this.type == S2CAttackDebug.EnumAABBType.PLAYER && !Minecraft.getInstance().getEntityRenderDispatcher()
                    .shouldRenderHitBoxes())
                return true;
            float[] color = switch (this.type) {
                case ATTEMPT -> ATTEMPT_RGB;
                case ATTACK -> ATTACK_RGB;
                case PLAYER -> PLAYER_RGB;
            };
            RenderUtils.renderOBB(stack, buffer, this.obb, color[0], color[1], color[2], 1, false);
            return this.duration-- < 0;
        }
    }
}
