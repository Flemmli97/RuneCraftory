package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SingleTimeSpawner extends BlockEntity {

    private EntityType<?> savedEntity;
    private ResourceLocation shop;
    private CompoundTag tag;
    private int delay = 3;

    public SingleTimeSpawner(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.SINGLE_SPAWNER_TILE.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SingleTimeSpawner blockEntity) {
        if (--blockEntity.delay > 0)
            return;
        if (blockEntity.savedEntity != null) {
            blockEntity.spawnEntity();
        }
        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
    }

    private void spawnEntity() {
        if (!this.level.isClientSide) {
            Entity e = this.savedEntity.create(this.level);
            if (e != null) {
                if (e instanceof Mob mob) {
                    mob.finalizeSpawn((ServerLevelAccessor) this.level, this.level.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.SPAWNER, null, null);
                }
                if (e instanceof EntityNPCBase npc) {
                    npc.randomizeData(this.shop);
                }
                e.moveTo(this.worldPosition.getX() + 0.5, this.worldPosition.getY(), this.worldPosition.getZ() + 0.5, this.level.random.nextFloat() * 360.0F, 0.0F);
                if (this.tag != null) {
                    CompoundTag newTag = e.saveWithoutId(new CompoundTag());
                    newTag.merge(this.tag);
                    e.load(newTag);
                }
                this.level.addFreshEntity(e);
            }
        }
    }

    public void setEntity(ResourceLocation entity, CompoundTag tag) {
        this.savedEntity = PlatformUtils.INSTANCE.entities().getFromId(entity);
        this.tag = tag;
        if (this.tag != null && this.tag.hasUUID(Entity.UUID_TAG))
            this.tag.remove(Entity.UUID_TAG);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.savedEntity = PlatformUtils.INSTANCE.entities().getFromId(new ResourceLocation(nbt.getString("Entity")));
        if (nbt.contains("EntityNBT"))
            this.tag = nbt.getCompound("EntityNBT");
        if (nbt.contains("NPCShop"))
            this.shop = new ResourceLocation(nbt.getString("NPCShop"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.savedEntity != null)
            nbt.putString("Entity", PlatformUtils.INSTANCE.entities().getIDFrom(this.savedEntity).toString());
        if (this.tag != null)
            nbt.put("EntityNBT", this.tag);
        if (this.shop != null)
            nbt.putString("NPCShop", this.shop.toString());
    }
}
