package com.flemmli97.runecraftory.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    private static final SimpleNetworkWrapper dispatcher;
    
    public static final void registerPackets() {
        int id = 0;
        PacketHandler.dispatcher.registerMessage(PacketUpdateClient.Handler.class, PacketUpdateClient.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketHealth.Handler.class, PacketHealth.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketMaxHealth.Handler.class, PacketMaxHealth.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketRunePoints.Handler.class, PacketRunePoints.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketMaxRunePoints.Handler.class, PacketMaxRunePoints.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketMoney.Handler.class, PacketMoney.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketPlayerLevel.Handler.class, PacketPlayerLevel.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketPlayerStats.Handler.class, PacketPlayerStats.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketSkills.Handler.class, PacketSkills.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketWeaponHit.Handler.class, PacketWeaponHit.class, id++, Side.SERVER);
        PacketHandler.dispatcher.registerMessage(PacketJump.Handler.class, PacketJump.class, id++, Side.SERVER);
        PacketHandler.dispatcher.registerMessage(PacketWeaponAnimation.Handler.class, PacketWeaponAnimation.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketOpenGuiContainer.Handler.class, PacketOpenGuiContainer.class, id++, Side.SERVER);
        PacketHandler.dispatcher.registerMessage(PacketCrafting.Handler.class, PacketCrafting.class, id++, Side.SERVER);
        PacketHandler.dispatcher.registerMessage(PacketCastSpell.Handler.class, PacketCastSpell.class, id++, Side.SERVER);
        PacketHandler.dispatcher.registerMessage(PacketSwingArm.Handler.class, PacketSwingArm.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketCalendar.Handler.class, PacketCalendar.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketEntityLevelUp.Handler.class, PacketEntityLevelUp.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketBuy.Handler.class, PacketBuy.class, id++, Side.SERVER);
        PacketHandler.dispatcher.registerMessage(PacketShopResult.Handler.class, PacketShopResult.class, id++, Side.CLIENT);
        PacketHandler.dispatcher.registerMessage(PacketUpdateShopItems.Handler.class, PacketUpdateShopItems.class, id++, Side.CLIENT);
    }
    
    public static final void sendTo(final IMessage message, final EntityPlayerMP player) {
        PacketHandler.dispatcher.sendTo(message, player);
    }
    
    public static void sendToAll(final IMessage message) {
        PacketHandler.dispatcher.sendToAll(message);
    }
    
    public static final void sendToAllAround(final IMessage message, final NetworkRegistry.TargetPoint point) {
        PacketHandler.dispatcher.sendToAllAround(message, point);
    }
    
    public static final void sendToAllAround(final IMessage message, final int dimension, final double x, final double y, final double z, final double range) {
        sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }
    
    public static final void sendToAllAround(final IMessage message, final EntityPlayer player, final double range) {
        sendToAllAround(message, player.world.provider.getDimension(), player.posX, player.posY, player.posZ, range);
    }
    
    public static final void sendToAllAround(final IMessage message, final TileEntity tileEntity, final double range) {
        sendToAllAround(message, tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5, range);
    }
    
    public static final void sendToDimension(final IMessage message, final int dimensionId) {
        PacketHandler.dispatcher.sendToDimension(message, dimensionId);
    }
    
    public static final void sendToServer(final IMessage message) {
        PacketHandler.dispatcher.sendToServer(message);
    }
    
    public static final Packet<?> getPacket(final IMessage message) {
        return (Packet<?>)PacketHandler.dispatcher.getPacketFrom(message);
    }
    
    static {
        dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("runecraftory");
    }
}
