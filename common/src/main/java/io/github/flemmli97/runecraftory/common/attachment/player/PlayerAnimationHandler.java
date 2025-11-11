package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.network.S2CPlayerAnimation;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.world.entity.player.Player;

public class PlayerAnimationHandler extends AnimationHandler<Player> {

    public PlayerAnimationHandler(Player entity) {
        super(entity, PlayerModelAnimations.ID, PlayerModelAnimations.ANIMS);
    }

    @Override
    protected void syncToClient(int startTransition, int endTransition, double offset) {
        LoaderNetwork.INSTANCE.sendToTracking(S2CPlayerAnimation.create(this.getEntity(), startTransition, endTransition, offset), this.getEntity());
    }
}
