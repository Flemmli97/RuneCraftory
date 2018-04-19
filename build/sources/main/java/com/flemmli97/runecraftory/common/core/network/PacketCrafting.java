package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileMultiBase;
import com.flemmli97.runecraftory.common.blocks.tile.TileResearchTable;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCrafting  implements IMessage{

	public int craft;	
	public int[] pos;
	public PacketCrafting(){}
	
	public PacketCrafting(int craft, BlockPos pos)
	{
		this.craft = craft;
		this.pos=new int[] {pos.getX(), pos.getY(), pos.getZ()};
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.craft = compound.getInteger("craft");
		this.pos=compound.getIntArray("pos");
	}

	@Override
	public void toBytes(ByteBuf buf) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("craft", craft);
		compound.setIntArray("pos", pos);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketCrafting, IMessage> {

        @Override
        public IMessage onMessage(PacketCrafting msg, MessageContext ctx) {
        	EntityPlayer player = RuneCraftory.proxy.getPlayerEntity(ctx);
        if(player!=null)
		{
        		TileEntity tile = player.world.getTileEntity(new BlockPos(msg.pos[0],msg.pos[1],msg.pos[2]));
			if(tile instanceof TileMultiBase)
			{
				if(msg.craft==0)
					((TileMultiBase)tile).tryCrafting(player);
				else
					((TileMultiBase)tile).tryUpgrade(player);
			}
			else if(tile instanceof TileResearchTable)
			{
				((TileResearchTable)tile).recipe(player, msg.craft, msg.craft+10);
			}
		}	
            return null;
        }
    }
}