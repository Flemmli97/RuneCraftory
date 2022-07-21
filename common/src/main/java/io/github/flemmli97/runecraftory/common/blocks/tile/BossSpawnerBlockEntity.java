package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.phys.AABB;

public class BossSpawnerBlockEntity extends BlockEntity {

    private int lastUpdateDay = -1;
    private EntityType<?> savedEntity;
    private BlockPos structurePos;
    private ResourceLocation structureID;
    private StructureStart structure;

    public BossSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.bossSpawnerTile.get(), blockPos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, BossSpawnerBlockEntity blockEntity) {
        if (level.hasNearbyAlivePlayer(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 16)) {
            boolean flag = blockEntity.lastUpdateDay == -1 || WorldUtils.canUpdateDaily(level) || Math.abs(blockEntity.lastUpdateDay - WorldUtils.day(level)) >= 1;
            /*if(this.structure!=null)
                for(EntityPlayer player : this.world.playerEntities)
                    if(this.base.isInside(player.getPosition()))
                    {

                    }*/
            if (blockEntity.savedEntity != null && flag) {
                blockEntity.spawnEntity();
            }
        }
    }

    public StructureStart getStructure() {
        if (this.structureID != null && this.level instanceof ServerLevel serverLevel)
            this.structure = serverLevel.structureFeatureManager().getStructureAt(this.getBlockPos(), PlatformUtils.INSTANCE.registry(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY).getFromId(this.structureID));
        return this.structure;
    }

    public void spawnEntity() {
        if (!this.level.isClientSide && this.savedEntity != null) {
            Entity e = this.savedEntity.create(this.level);
            if (e != null) {
                this.lastUpdateDay = WorldUtils.day(this.level);
                if (this.level.getEntitiesOfClass(e.getClass(), new AABB(this.worldPosition).inflate(32)).size() != 0)
                    return;
                if (e instanceof BaseMonster)
                    ((BaseMonster) e).setLevel(LevelCalc.levelFromPos(this.level, this.worldPosition));
                e.moveTo(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 5, this.worldPosition.getZ() + 0.5, this.level.random.nextFloat() * 360.0F, 0.0F);
                if (e instanceof Mob mob) {
                    mob.restrictTo(this.worldPosition, 16);
                    mob.finalizeSpawn((ServerLevelAccessor) this.level, this.level.getCurrentDifficultyAt(e.blockPosition()), MobSpawnType.SPAWNER, null, null);
                }
                this.level.addFreshEntity(e);
                this.lastUpdateDay = WorldUtils.day(this.level);
            }
        }
    }

    public void setEntity(ResourceLocation entity) {
        this.savedEntity = PlatformUtils.INSTANCE.entities().getFromId(entity);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.lastUpdateDay = nbt.getInt("LastUpdate");
        this.savedEntity = PlatformUtils.INSTANCE.entities().getFromId(new ResourceLocation(nbt.getString("Entity")));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("LastUpdate", this.lastUpdateDay);
        nbt.putString("Entity", PlatformUtils.INSTANCE.entities().getIDFrom(this.savedEntity).toString());
    }
}
