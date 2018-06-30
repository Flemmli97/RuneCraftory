package com.flemmli97.runecraftory.common.network;

import com.flemmli97.runecraftory.RuneCraftory;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketShopResult implements IMessage
{
    public String text;
    
    public PacketShopResult() {
    }
    
    public PacketShopResult(String displayText) {
        this.text = displayText;
    }
    
    public void fromBytes(ByteBuf buf) {
        this.text = ByteBufUtils.readUTF8String(buf);
    }
    
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.text);
    }
    
    public static class Handler implements IMessageHandler<PacketShopResult, IMessage>
    {
        public IMessage onMessage(PacketShopResult msg, MessageContext ctx) {
            EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
            if (player != null) {
                RuneCraftory.proxy.guiTextBubble(msg.text, RuneCraftory.proxy.currentGui(ctx));
            }
            return null;
        }
    }
}
