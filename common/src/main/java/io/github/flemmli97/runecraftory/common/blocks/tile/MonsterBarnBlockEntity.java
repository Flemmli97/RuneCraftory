package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.world.BarnData;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.core.BlockPos;
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
        super(ModBlocks.monsterBarnBlockEntity.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, MonsterBarnBlockEntity blockEntity) {
        if (blockEntity.owner == null || --blockEntity.cooldown > 0 || !(level instanceof ServerLevel))
            return;
        if (blockEntity.barnData == null) {
            blockEntity.barnData = WorldHandler.get(level.getServer())
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
                    blockEntity.barnData.update(0, false);
                    return;
                }
            }
        }
        int airLayers = 5;
        for (BlockPos pos : BlockPos.betweenClosed(blockPos.getX() - size, blockPos.getY(), blockPos.getZ() - size,
                blockPos.getX() + size, blockPos.getY() + 5, blockPos.getZ() + size)) {
            if ((Math.abs(pos.getY() - blockPos.getY()) > airLayers)) // Skip blocks not needing checks
                continue;
            if (pos.equals(blockPos)) //Barn block position
                continue;
            if (!matches(level, pos, false)) {
                airLayers = pos.getY() - blockPos.getY();
                break;
            }
        }
        if (airLayers >= 3) {
            boolean hasRoof = true;
            for (BlockPos pos : BlockPos.betweenClosed(blockPos.getX() - size, blockPos.getY() + airLayers, blockPos.getZ() - size,
                    blockPos.getX() + size, blockPos.getY() + airLayers, blockPos.getZ() + size)) {
                if (level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ()) <= pos.getY()) {
                    hasRoof = false;
                    break;
                }
            }
            blockEntity.barnData.update(Math.min(size, airLayers), hasRoof);
        }
    }

    private static boolean matches(Level level, BlockPos pos, boolean ground) {
        BlockState state = level.getBlockState(pos);
        return ground ? state.is(ModTags.BARN_GROUND) : !state.is(ModBlocks.monsterBarn.get()) && state.getCollisionShape(level, pos).isEmpty();
    }

    private static boolean cornersMatch(int size, Level level, BlockPos center) {
        BlockPos.MutableBlockPos mutable = center.mutable();
        if (!matches(level, mutable.set(center.getX() + size, center.getY(), center.getZ() + size), true))
            return false;
        if (!matches(level, mutable.set(center.getX() - size, center.getY(), center.getZ() + size), true))
            return false;
        if (!matches(level, mutable.set(center.getX() + size, center.getY(), center.getZ() - size), true))
            return false;
        return matches(level, mutable.set(center.getX() - size, center.getY(), center.getZ() - size), true);
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
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.hasUUID("Owner"))
            this.owner = nbt.getUUID("Owner");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.owner != null)
            nbt.putUUID("Owner", this.owner);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        if (this.owner != null)
            nbt.putUUID("Owner", this.owner);
        return nbt;
    }
}
