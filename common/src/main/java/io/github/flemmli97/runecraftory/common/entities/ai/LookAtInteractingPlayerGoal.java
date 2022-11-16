package io.github.flemmli97.runecraftory.common.entities.ai;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class LookAtInteractingPlayerGoal extends LookAtPlayerGoal {

    private final EntityNPCBase npc;

    public LookAtInteractingPlayerGoal(EntityNPCBase npc) {
        super(npc, Player.class, 8.0f);
        this.npc = npc;
    }

    @Override
    public boolean canUse() {
        Player player = this.npc.getLastInteractedPlayer();
        if (player != null) {
            this.lookAt = player;
            return true;
        }
        return false;
    }
}
