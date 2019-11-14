package com.flemmli97.runecraftory.common.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityForceField extends Entity{

    public EntityForceField(World world) {
        super(world);
    }
    
    @Override
    public void onUpdate() {}

    @Override
    protected void entityInit() {}

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {}

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return null;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }
}
