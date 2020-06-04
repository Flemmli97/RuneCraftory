package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.render.RenderAttackAABBHandler;
import com.flemmli97.runecraftory.common.core.handler.config.ConfigHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAttackDebug  implements IMessage{

    public AxisAlignedBB aabb;
    public int duration;
    public PacketAttackDebug(){}

    public PacketAttackDebug(AxisAlignedBB aabb) {
        this.aabb = aabb;
        this.duration = 300;
    }

    public PacketAttackDebug(AxisAlignedBB aabb, int duration) {
        this(aabb);
        this.duration = duration;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if(buf.readBoolean())
            this.aabb = new AxisAlignedBB(buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble());
        this.duration = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(ConfigHandler.MainConfig.debugAttack);
        buf.writeDouble(aabb.maxX);
        buf.writeDouble(aabb.maxY);
        buf.writeDouble(aabb.maxZ);
        buf.writeDouble(aabb.minX);
        buf.writeDouble(aabb.minY);
        buf.writeDouble(aabb.minZ);
        buf.writeInt(this.duration);
    }

    public static class Handler implements IMessageHandler<PacketAttackDebug, IMessage> {

        @Override
        public IMessage onMessage(PacketAttackDebug msg, MessageContext ctx) {
            EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
            if(player!=null) {
                if(msg.aabb!=null)
                    RenderAttackAABBHandler.INST.addNewAABB(msg.aabb, msg.duration);
            }
            return null;
        }
    }
}