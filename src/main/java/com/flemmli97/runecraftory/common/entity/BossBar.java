//make bossbar health only update for certain players?
/*package com.flemmli97.runecraftory.common.entity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfoServer;

public class BossBar extends BossInfoServer{

	public BossBar(ITextComponent nameIn, Color colorIn, Overlay overlayIn) {
		super(nameIn, colorIn, overlayIn);
	}

    public void setPercent(float percentIn)
    {
        if (percentIn != this.percent)
        {
            this.percent = percentIn;
            this.update(SPacketUpdateBossInfo.Operation.UPDATE_PCT);
        }
    }

	private void update(SPacketUpdateBossInfo.Operation updatePct) 
	{
		if (this.visible)
        {
            SPacketUpdateBossInfo spacketupdatebossinfo = new SPacketUpdateBossInfo(updatePct, this);

            for (EntityPlayerMP entityplayermp : this.players)
            {
                entityplayermp.connection.sendPacket(spacketupdatebossinfo);
            }
        }		
	}
}*/
