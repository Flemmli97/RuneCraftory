package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketJump implements IMessage
{

    @Override
    public void fromBytes(final ByteBuf buf) {
    }

    @Override
    public void toBytes(final ByteBuf buf) {
    }
    
    public static class Handler implements IMessageHandler<PacketJump, IMessage>
    {
        @Override
        public IMessage onMessage(final PacketJump msg, final MessageContext ctx) {
            final EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
            if (player != null && player.isRiding() && player.getRidingEntity() instanceof EntityMobBase) {
                ((EntityMobBase)player.getRidingEntity()).setDoJumping();
            }
            return null;
        }
    }
}
