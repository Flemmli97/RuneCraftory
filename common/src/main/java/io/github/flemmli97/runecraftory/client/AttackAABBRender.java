package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.lib.EnumAABBType;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class AttackAABBRender {

    private final List<RenderAABB> list = new ArrayList<>();
    private final List<RenderAABB> toAdd = new ArrayList<>();

    public static AttackAABBRender INST = new AttackAABBRender();

    private static final float[] attackRGB = new float[]{18 / 255f, 181 / 255f, 51 / 255f};
    private static final float[] attemptRGB = new float[]{19 / 255f, 56 / 255f, 191 / 255f};

    public void addNewAABB(AABB aabb, int duration, EnumAABBType type) {
        this.toAdd.add(new RenderAABB(aabb, duration, type));
    }

    public void render(PoseStack stack, MultiBufferSource.BufferSource buffer) {
        this.list.addAll(this.toAdd);
        this.toAdd.clear();
        this.list.removeIf(r -> r.render(stack, buffer));
        buffer.endBatch(RenderType.LINES);
    }

    private static class RenderAABB {

        private final AABB aabb;
        private int duration;
        private final EnumAABBType type;

        public RenderAABB(AABB aabb, int duration, EnumAABBType type) {
            this.aabb = aabb;
            this.duration = duration;
            this.type = type;
        }

        public boolean render(PoseStack stack, MultiBufferSource buffer) {
            float[] color;
            if (this.type == EnumAABBType.ATTEMPT) {
                color = attemptRGB;
            } else {
                color = attackRGB;
            }
            RenderUtils.renderBoundingBox(stack, buffer, this.aabb, color[0], color[1], color[2], 1, false);
            return this.duration-- < 0;
        }
    }
}
