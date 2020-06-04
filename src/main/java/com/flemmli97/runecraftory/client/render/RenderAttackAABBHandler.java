package com.flemmli97.runecraftory.client.render;

import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class RenderAttackAABBHandler {

    private List<RenderAABB> list = Lists.newArrayList();
    private List<RenderAABB> toAdd = Lists.newArrayList();

    public static RenderAttackAABBHandler INST = new RenderAttackAABBHandler();

    public void addNewAABB(AxisAlignedBB aabb, int duration){
        //Client tick not 20 per sec???
        this.toAdd.add(new RenderAABB(aabb, duration));
    }

    public void render(){
        this.list.addAll(toAdd);
        this.toAdd.clear();
        this.list.removeIf(RenderAABB::render);
    }

    private static class RenderAABB{
        private AxisAlignedBB aabb;
        private int duration;

        public RenderAABB(AxisAlignedBB aabb, int duration){
            this.aabb=aabb;
            this.duration=duration;
        }

        public boolean render(){
            RenderUtils.renderBoundingBox(aabb, Minecraft.getMinecraft().player, Minecraft.getMinecraft().getRenderPartialTicks(),1/18f, 1/181f, 1/51f, 1, false);
            return this.duration--<0;
        }
    }
}
