package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.common.lib.EnumAABBType;
import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class AttackAABBRender {

    private final List<RenderAABB> list = new ArrayList<>();
    private final List<RenderAABB> toAdd = new ArrayList<>();

    public static AttackAABBRender INST = new AttackAABBRender();

    private static final float[] attackRGB = new float[]{18 / 255f, 181 / 255f, 51 / 255f};
    private static final float[] attemptRGB = new float[]{19 / 255f, 56 / 255f, 191 / 255f};

    public void addNewAABB(AxisAlignedBB aabb, int duration, EnumAABBType type) {
        this.toAdd.add(new RenderAABB(aabb, duration, type));
    }

    public void render(MatrixStack stack, IRenderTypeBuffer.Impl buffer) {
        this.list.addAll(this.toAdd);
        this.toAdd.clear();
        this.list.removeIf(r -> r.render(stack, buffer));
        buffer.finish(RenderType.LINES);
    }

    private static class RenderAABB {

        private final AxisAlignedBB aabb;
        private int duration;
        private final EnumAABBType type;

        public RenderAABB(AxisAlignedBB aabb, int duration, EnumAABBType type) {
            this.aabb = aabb;
            this.duration = duration;
            this.type = type;
        }

        public boolean render(MatrixStack stack, IRenderTypeBuffer buffer) {
            float[] color;
            if (this.type == EnumAABBType.ATTEMPT) {
                color = attemptRGB;
            } else {
                color = attackRGB;
            }
            RenderUtils.renderBoundingBox(stack, buffer, this.aabb, color[0], color[1], color[2], 1, false, false);
            return this.duration-- < 0;
        }
    }
}
