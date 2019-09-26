package com.flemmli97.runecraftory.common.blocks.tile;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.GateSpawning;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.tenshilib.api.block.ITileEntityInitialPlaced;
import com.flemmli97.tenshilib.common.world.structure.StructureBase;
import com.flemmli97.tenshilib.common.world.structure.StructureMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileBossSpawner extends TileEntity implements ITickable, ITileEntityInitialPlaced{

	private long time;
	private ResourceLocation savedEntity;
	private BlockPos structurePos;
	private ResourceLocation structureID;
	private StructureBase base;
	
	public TileBossSpawner() {}
	public TileBossSpawner(World world) {
		this.time=world.getWorldTime();
	}
	
	@Override
	public void update() {
		if(!this.world.isRemote && this.world.isAnyPlayerWithinRangeAt(this.pos.getX()+0.5, this.pos.getY()+0.5, this.pos.getZ()+0.5, 16))
		{
			boolean flag = Math.abs(this.world.getWorldTime()/24000 - this.time/24000)>=1 || this.world.getWorldTime()%24000==1;
			if(this.base!=null)
				for(EntityPlayer player : this.world.playerEntities)
					if(this.base.isInside(player.getPosition()))
					{
						
					}
			if(this.savedEntity!=null && flag)
			{
				this.spawnEntity();
			}
		}
	}
	
	public void spawnEntity()
	{
		if(!this.world.isRemote)
		{
			EntityMobBase mob = GateSpawning.entityFromString(world, savedEntity);
			if(mob!=null)
			{
				this.time=this.world.getWorldTime();
		        int k = world.getEntitiesWithinAABB(mob.getClass(), (new AxisAlignedBB(this.pos).grow(32))).size();
		        if(k!=0)
		        {        
		        		return;
		        }
		        mob.setLevel(LevelCalc.levelFromDistSpawn(world, pos));
		        mob.setLocationAndAngles(this.pos.getX()+0.5, this.pos.getY()+5, this.pos.getZ()+0.5, world.rand.nextFloat() * 360.0F, 0.0F);
				mob.setHomePosAndDistance(this.pos, 16);
		        mob.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(mob)), null);
		        this.world.spawnEntity(mob);
			}
		}
	}
	
	public boolean setEntity(@Nullable EntityPlayer player, ResourceLocation entity)
	{
		if(player==null || player.canUseCommandBlock())
		{
			this.savedEntity=entity;
			this.spawnEntity();
			this.time=this.world.getWorldTime();
			return true;
		}
		return false;
	}
	
	@Override
	public void setWorld(World worldIn)
    {
        super.setWorld(worldIn);
        if(!this.world.isRemote && this.structurePos!=null && this.structureID!=null)
			this.base=StructureMap.get(this.world).getNearestStructure(this.structureID, this.structurePos, this.world);
    }

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("Entity"))
			this.savedEntity= new ResourceLocation(compound.getString("Entity"));
		this.time = compound.getLong("LastSpawned");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if(this.savedEntity!=null)
			compound.setString("Entity", this.savedEntity.toString());
		compound.setLong("LastSpawned", time);
		return compound;
	}

	@Override
	public boolean onlyOpsCanSetNbt() {
		return true;
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, Rotation rot, Mirror mirror) {
		this.spawnEntity();
		StructureBase base = StructureMap.get(world).current(pos);
		if(base!=null)
		{
			this.base=base;
			this.structurePos=base.getPos();
		}
	}
}
