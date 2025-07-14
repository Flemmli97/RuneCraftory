package io.github.flemmli97.runecraftory.common.blocks.entity;

import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.world.data.BarnData;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MonsterBarnBlockEntity extends BlockEntity {

    private static final int MAX_SIZE = 5;

    private UUID owner;
    private int cooldown = 0;

    private BarnData barnData;

    public MonsterBarnBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(RuneCraftoryBlocks.MONSTER_BARN_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MonsterBarnBlockEntity blockEntity) {
        if (blockEntity.owner == null || --blockEntity.cooldown > 0 || !(level instanceof ServerLevel))
            return;
        if (blockEntity.barnData == null) {
            blockEntity.barnData = RunecraftorySavedData.get(level.getServer())
                    .getOrCreateFor(blockEntity.owner, level, blockPos);
        }
        blockEntity.cooldown = 40;
        int size = MAX_SIZE;
        for (BlockPos pos : BlockPos.betweenClosed(blockPos.getX() - size, blockPos.getY() - 1, blockPos.getZ() - size,
                blockPos.getX() + size, blockPos.getY() - 1, blockPos.getZ() + size)) {
            if ((Math.abs(pos.getX() - blockPos.getX()) > size || Math.abs(pos.getZ() - blockPos.getZ()) > size)) // Skip blocks not needing checks
                continue;
            if (!matches(level, pos, true)) {
                size = Math.max(Math.abs(pos.getX() - blockPos.getX()), Math.abs(pos.getZ() - blockPos.getZ())) - 1;
                if (size < 2) {
                    blockEntity.barnData.update(0, -1);
                    return;
                }
            }
        }
        int airLayers = 10;
        // Check if barn area is empty
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        exit:
        for (int y = 0; y < airLayers; y++) {
            for (int x = blockPos.getX() - size; x < blockPos.getX() + size; x++) {
                for (int z = blockPos.getZ() - size; z < blockPos.getZ() + size; z++) {
                    pos.set(x, blockPos.getY() + y, z);
                    if (pos.equals(blockPos)) //Barn block position
                        continue;
                    if (!matches(level, pos, false)) {
                        airLayers = y;
                        break exit;
                    }
                }
            }
        }
        // Barn require at least 3 layers of air
        if (airLayers >= 3) {
            boolean hasRoof = true;
            for (BlockPos pos2 : BlockPos.betweenClosed(blockPos.getX() - size, blockPos.getY() + airLayers, blockPos.getZ() - size,
                    blockPos.getX() + size, blockPos.getY() + airLayers, blockPos.getZ() + size)) {
                if (level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos2.getX(), pos2.getZ()) <= pos2.getY()) {
                    hasRoof = false;
                    break;
                }
            }
            blockEntity.barnData.update(size, hasRoof ? airLayers : -1);
        } else {
            blockEntity.barnData.update(0, -1);
        }
    }

    private static boolean matches(Level level, BlockPos pos, boolean ground) {
        BlockState state = level.getBlockState(pos);
        return ground ? state.is(RunecraftoryTags.Blocks.BARN_GROUND) : !state.is(RuneCraftoryBlocks.MONSTER_BARN.get()) && state.getCollisionShape(level, pos).isEmpty();
    }

    @Nullable
    public BarnData getBarnData() {
        return this.barnData;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        this.setChanged();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.hasUUID("Owner"))
            this.owner = tag.getUUID("Owner");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (this.owner != null)
            tag.putUUID("Owner", this.owner);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        if (this.owner != null)
            nbt.putUUID("Owner", this.owner);
        return nbt;
    }
}
