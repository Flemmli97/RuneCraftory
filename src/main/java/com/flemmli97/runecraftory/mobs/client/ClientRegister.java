package com.flemmli97.runecraftory.mobs.client;

import com.flemmli97.runecraftory.mobs.client.render.RenderGate;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderAmbrosia;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderAnt;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderBeetle;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderBigMuck;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderBuffamoo;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderChipsqueek;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderCluckadoodle;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderOrc;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderOrcArcher;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderPommePomme;
import com.flemmli97.runecraftory.mobs.client.render.monster.RenderWooly;
import com.flemmli97.runecraftory.mobs.client.render.projectiles.RenderAmbrosiaWave;
import com.flemmli97.runecraftory.mobs.client.render.projectiles.RenderButterfly;
import com.flemmli97.runecraftory.mobs.client.render.projectiles.RenderMobArrow;
import com.flemmli97.runecraftory.mobs.client.render.projectiles.RenderSleepBall;
import com.flemmli97.runecraftory.registry.ModEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientRegister {

    public static void registerEntityRender() {
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.gate, RenderGate::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.wooly, RenderWooly::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.orc, RenderOrc::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.orcArcher, RenderOrcArcher::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ant, RenderAnt::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.beetle, RenderBeetle::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.big_muck, RenderBigMuck::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.buffamoo, RenderBuffamoo::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.chipsqueek, RenderChipsqueek::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.cluckadoodle, RenderCluckadoodle::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.pomme_pomme, RenderPommePomme::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ambrosia, RenderAmbrosia::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.arrow, RenderMobArrow::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.sleep_ball, RenderSleepBall::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ambrosia_wave, RenderAmbrosiaWave::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.butterfly, RenderButterfly::new);
    }
}
