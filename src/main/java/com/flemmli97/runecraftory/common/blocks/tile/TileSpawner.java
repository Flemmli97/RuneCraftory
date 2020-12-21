package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class TileSpawner extends TileEntity implements ITickableTileEntity {

    private int lastUpdateDay;
    private ResourceLocation savedEntity;
    private BlockPos structurePos;
    private ResourceLocation structureID;
    private StructureStart<?> structure;

    public TileSpawner() {
        super(ModBlocks.bossSpawnerTile.get());
    }

    public StructureStart<?> getStructure() {
        if (this.structureID != null && this.world instanceof ServerWorld)
            this.structure = ((ServerWorld) this.world).getStructureAccessor().getStructureAt(this.getPos(), true, ForgeRegistries.STRUCTURE_FEATURES.getValue(this.structureID));
        return this.structure;
    }

    public void spawnEntity() {
        if (!this.world.isRemote && this.savedEntity != null) {
            Entity e = ForgeRegistries.ENTITIES.getValue(this.savedEntity).create(this.world);
            if (e != null) {
                this.lastUpdateDay = WorldUtils.day(this.world);
                if (this.world.getEntitiesWithinAABB(e.getClass(), (new AxisAlignedBB(this.pos).grow(32))).size() != 0)
                    return;
                if (e instanceof BaseMonster)
                    ((BaseMonster) e).setLevel(LevelCalc.levelFromPos(this.world, this.pos));
                e.setLocationAndAngles(this.pos.getX() + 0.5, this.pos.getY() + 5, this.pos.getZ() + 0.5, this.world.rand.nextFloat() * 360.0F, 0.0F);
                if (e instanceof MobEntity) {
                    ((MobEntity) e).setHomePosAndDistance(this.pos, 16);
                    ((MobEntity) e).onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(e.getBlockPos()), SpawnReason.SPAWNER, null, null);
                }
                this.world.addEntity(e);
            }
        }
    }

    public void setEntity(ResourceLocation entity) {
        this.savedEntity = entity;
        this.spawnEntity();
        this.lastUpdateDay = WorldUtils.day(this.world);
    }

    @Override
    public void tick() {
        if (!this.world.isRemote && this.world.isPlayerWithin(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, 16)) {
            boolean flag = WorldUtils.canUpdateDaily(this.world) || Math.abs(this.lastUpdateDay - WorldUtils.day(this.world)) >= 1;
            /*if(this.structure!=null)
                for(EntityPlayer player : this.world.playerEntities)
                    if(this.base.isInside(player.getPosition()))
                    {

                    }*/
            if (this.savedEntity != null && flag) {
                this.spawnEntity();
            }
        }
    }
}
