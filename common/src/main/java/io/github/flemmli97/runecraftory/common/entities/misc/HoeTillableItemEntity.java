package io.github.flemmli97.runecraftory.common.entities.misc;

import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.datapack.manager.FarmlandHoeTileActionManager;
import io.github.flemmli97.runecraftory.common.network.S2CTriggers;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class HoeTillableItemEntity extends ItemEntity {

    public HoeTillableItemEntity(EntityType<? extends ItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    public HoeTillableItemEntity(Level level, ItemEntity other) {
        this(RuneCraftoryEntities.HOE_TILLABLE_ITEM_ENTITY.get(), level);
        this.setPos(other.getX(), other.getY(), other.getZ());
        this.setDeltaMovement(other.getDeltaMovement().x(), other.getDeltaMovement().y(), other.getDeltaMovement().z());
        this.setItem(other.getItem());
        this.setPickUpDelay(40);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof HoeItem) {
            if (this.level() instanceof ServerLevel serverLevel) {
                FarmlandHoeTileActionManager.Data data = DataPackHandler.INSTANCE.fertilizerManager().get(this.getItem().getItem());
                if (data != null) {
                    boolean[] success = {false};
                    BlockPos.withinManhattanStream(this.blockPosition(), 1, 0, 1)
                            .forEach(pos -> this.applyTo(serverLevel, pos.immutable(), data.amount(), success));
                    if (success[0]) {
                        this.getItem().shrink(1);
                        this.playSound(SoundEvents.GENERIC_EAT, 1, 0.5f);
                    }
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.interact(player, hand);
    }

    private void applyTo(ServerLevel level, BlockPos blockPos, int amount, boolean[] success) {
        Optional<FarmlandData> data = FarmlandHandler.get(level.getServer())
                .getData(level, blockPos);
        if (data.isEmpty() || !data.get().isFarmBlock()) {
            data = FarmlandHandler.get(level.getServer())
                    .getData(level, blockPos.below());
        }
        data.ifPresent(d -> {
            if (d.isFarmBlock()) {
                d.modifyHealth(level, amount);
                success[0] = true;
                LoaderNetwork.INSTANCE.sendToTracking(new S2CTriggers(S2CTriggers.TriggerType.FERTILIZER, blockPos), level, new ChunkPos(blockPos));
            }
        });
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}
