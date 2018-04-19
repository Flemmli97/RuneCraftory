package com.flemmli97.runecraftory.common.core.network;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(LibReference.MODID);
	
	public static final void registerPackets() {
		dispatcher.registerMessage(PacketUpdateClient.Handler.class, PacketUpdateClient.class, 0, Side.CLIENT);
		dispatcher.registerMessage(PacketHealth.Handler.class, PacketHealth.class, 1, Side.CLIENT);
		dispatcher.registerMessage(PacketMaxHealth.Handler.class, PacketMaxHealth.class, 2, Side.CLIENT);
		dispatcher.registerMessage(PacketRunePoints.Handler.class, PacketRunePoints.class, 3, Side.CLIENT);
		dispatcher.registerMessage(PacketMaxRunePoints.Handler.class, PacketMaxRunePoints.class, 4, Side.CLIENT);
		dispatcher.registerMessage(PacketMoney.Handler.class, PacketMoney.class, 5, Side.CLIENT);
		dispatcher.registerMessage(PacketPlayerLevel.Handler.class, PacketPlayerLevel.class, 6, Side.CLIENT);
		dispatcher.registerMessage(PacketPlayerStats.Handler.class, PacketPlayerStats.class, 7, Side.CLIENT);
		dispatcher.registerMessage(PacketSkills.Handler.class, PacketSkills.class, 8, Side.CLIENT);
		dispatcher.registerMessage(PacketStatus.Handler.class, PacketStatus.class, 9, Side.CLIENT);
		dispatcher.registerMessage(PacketWeaponHit.Handler.class, PacketWeaponHit.class, 10, Side.SERVER);
		dispatcher.registerMessage(PacketJump.Handler.class, PacketJump.class, 11, Side.SERVER);
		dispatcher.registerMessage(PacketWeaponAnimation.Handler.class, PacketWeaponAnimation.class, 12, Side.CLIENT);
		dispatcher.registerMessage(PacketOpenGuiContainer.Handler.class, PacketOpenGuiContainer.class, 13, Side.SERVER);
		dispatcher.registerMessage(PacketCrafting.Handler.class, PacketCrafting.class, 14, Side.SERVER);
		dispatcher.registerMessage(PacketDisableInteraction.Handler.class, PacketDisableInteraction.class, 15, Side.CLIENT);
		dispatcher.registerMessage(PacketCastSpell.Handler.class, PacketCastSpell.class, 16, Side.SERVER);
		dispatcher.registerMessage(PacketSwingArm.Handler.class, PacketSwingArm.class, 17, Side.CLIENT);

	}
	
	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		dispatcher.sendTo(message, player);
	}
	
	public static void sendToAll(IMessage message) {
		dispatcher.sendToAll(message);
	}

	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		dispatcher.sendToAllAround(message, point);
	}

	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		sendToAllAround(message, player.world.provider.getDimension(), player.posX, player.posY, player.posZ, range);
	}
	
	public static final void sendToAllAround(IMessage message, TileEntity tileEntity, double range) {
		sendToAllAround(message, tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX() + 0.5D, tileEntity.getPos().getY() + 0.5D, tileEntity.getPos().getZ() + 0.5D, range);
	}

	public static final void sendToDimension(IMessage message, int dimensionId) {
		dispatcher.sendToDimension(message, dimensionId);
	}

	public static final void sendToServer(IMessage message) {
		dispatcher.sendToServer(message);
	}
	
	public static final Packet<?> getPacket(IMessage message) {
		return dispatcher.getPacketFrom(message);
	}

}
