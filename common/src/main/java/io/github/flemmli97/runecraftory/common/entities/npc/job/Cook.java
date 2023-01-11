package io.github.flemmli97.runecraftory.common.entities.npc.job;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Cook extends NPCJob {

    public Cook(NPCJob.Builder builder) {
        super(builder);
    }

    @Override
    public void handleAction(EntityNPCBase npc, Player player, String action) {
        if (action.equals("bread")) {

        }
    }

    @Override
    public List<String> actions() {
        return List.of("bath");
    }
}