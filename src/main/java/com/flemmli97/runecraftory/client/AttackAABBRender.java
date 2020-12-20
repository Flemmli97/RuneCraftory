package com.flemmli97.runecraftory.client;

import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class AttackAABBRender {

    private final List<RenderAABB> list = Lists.newArrayList();
    private final List<RenderAABB> toAdd = Lists.newArrayList();

    public static AttackAABBRender INST = new AttackAABBRender();

    public void addNewAABB(AxisAlignedBB aabb, int duration) {
        //Client tick not 20 per sec???
        this.toAdd.add(new RenderAABB(aabb, duration));
    }

    public void render(MatrixStack stack, IRenderTypeBuffer buffer) {
        this.list.addAll(this.toAdd);
        this.toAdd.clear();
        this.list.removeIf(r -> r.render(stack, buffer));
    }

    private static class RenderAABB {
        private final AxisAlignedBB aabb;
        private int duration;

        public RenderAABB(AxisAlignedBB aabb, int duration) {
            this.aabb = aabb;
            this.duration = duration;

        }

        public boolean render(MatrixStack stack, IRenderTypeBuffer buffer) {
            RenderUtils.renderBoundingBox(stack, buffer, this.aabb, Minecraft.getInstance().player, Minecraft.getInstance().getRenderPartialTicks(), 1 / 18f, 1 / 181f, 1 / 51f, 1, false);
            return this.duration-- < 0;
        }
    }
}
