package io.github.flemmli97.runecraftory.common.attachment.player;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.function.BiConsumer;

public class EntitySelector {

    public Entity selectedEntity;

    public BlockPos poi;

    public BiConsumer<ServerPlayer, BlockPos> apply;

    public void reset() {
        this.selectedEntity = null;
        this.poi = null;
        this.apply = null;
    }

}
