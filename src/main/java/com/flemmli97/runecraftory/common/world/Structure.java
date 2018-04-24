package com.flemmli97.runecraftory.common.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.Position;
import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.util.Constants;

public class Structure {
	
	private Schematic schematic;
	private boolean underGround;
	private int dimID, freq, yOffset;
	private String name;
	private boolean initialized=false;
	private ArrayList<Type> biomeTypes = new ArrayList<Type>();
    private Set<PlacementProperties> structures = Sets.newHashSet();
	public Structure(String file, int frequency, boolean underGround, int dimID, int yOffset, Type... biomeTypes)
	{
		this.name=file;
		this.schematic=StructureLoader.getSchematic(file);
		this.underGround=underGround;
		this.freq=frequency;
		this.dimID=dimID;
		this.yOffset=yOffset;
		for(Type type:biomeTypes)
			this.biomeTypes.add(type);
	}
	
	public String structureName()
	{
		return this.name;
	}
	
	//Sets up structure pos and properties
	public synchronized void start(World world, int chunkX, int chunkZ, Random random)
	{
		Position p = new Position(chunkX, 0, chunkZ);
		StructureData data = StructureData.get(world);
		if(!data.hasPosition(p))
		{
			if(world.provider.getDimension() ==this.dimID || world.provider.getDimension()==LibReference.dimID)
			{
				if(random.nextInt(this.freq)==0)
				{
					int x = chunkX * 16 + random.nextInt(16)+8;
					int z = chunkZ * 16 + random.nextInt(16)+8;
					int y = random.nextInt(20)+20;
					Rotation rot = Rotation.values()[random.nextInt(Rotation.values().length)];
					Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
					BlockPos pos = this.underGround?new BlockPos(x,y,z):this.calculateGroundHeight(world, new BlockPos(x,255,z)).add(0, this.yOffset, 0);
					if(this.canSpawnAt(world, pos))
						for(Type type : this.biomeTypes)
						{
							if(BiomeDictionary.hasType(world.getBiome(pos), type))
							{
								PlacementProperties props =  new PlacementProperties(pos, rot, mirror, this.name);
								BlockPos corner = Schematic.transformedBlockPos(new Position(this.schematic.getWidth(), this.schematic.getLength(), this.schematic.getLength()), mirror, rot).add(pos);
								props.getUnfinishedChunks().addAll(Structure.getStructureChunks(new StructureBoundingBox(Math.min(pos.getX(), corner.getX()), pos.getY(), Math.min(pos.getZ(), corner.getZ()),Math.max(pos.getX(), corner.getX()), corner.getY(), Math.max(pos.getZ(), corner.getZ()))));//this.schematic.generate(world, props, null);
								this.structures.add(props);
								data.put(p, props);
								return;
							}
						}
				}
			}
		}
	}
	
	private boolean canSpawnAt(World world, BlockPos pos)
	{
		if(this.underGround)
			return true;
		int chunkMinY = world.getChunkFromBlockCoords(pos).getLowestHeight();
		return (pos.getY()-chunkMinY)>this.yOffset;
	}
	
	//Actually generates the structure
	public synchronized void gen(World world)
	{
		this.getData(world);
		Iterator<PlacementProperties> it = this.structures.iterator();
		while(it.hasNext())
		{
			PlacementProperties prop = it.next();
			List<Position> list = new ArrayList<Position>();
			for(int i = 0; i < prop.getUnfinishedChunks().size(); i++)
			{
				Position pos = prop.getUnfinishedChunks().get(i);
				if(world.getChunkProvider().getLoadedChunk(pos.getX(), pos.getZ())!=null)
				{
					this.schematic.generate(world, prop, new StructureBoundingBox(pos.getX()*16, pos.getZ()*16, pos.getX()*16+15, pos.getZ()*16+15), true);
					list.add(pos);
				}
			}
			prop.getUnfinishedChunks().removeAll(list);
			if(prop.isFinished())
				it.remove();
		}
	}
	private BlockPos calculateGroundHeight(World world, BlockPos pos)
	{
		Biome biome = world.getBiome(pos);
		IBlockState state = biome.topBlock;
		while(pos.getY()>1)
		{
			IBlockState check = world.getBlockState(pos);
			if(check.getBlock()==state.getBlock() && (world.getBlockState(pos.up()).getMaterial()==Material.AIR ||world.getBlockState(pos.up()).getMaterial().isReplaceable()))
			{			
				return pos;
			}
			pos = pos.down();
		}
		return pos;
	}
	
	public void getData(World world)
	{
		if(!this.initialized)
		{
			StructureData data = StructureData.get(world);
			for(Position p : data.positions())
			{
				PlacementProperties props = data.read(p);
				if(props.getIdentifier().equals(this.name) && !props.isFinished())
					this.structures.add(props);
			}
			this.initialized=true;
		}
	}
	public static List<Position> getStructureChunks(StructureBoundingBox bb)
	{
		List<Position> list = new ArrayList<Position>();
		if (bb.maxY >= 0 && bb.minY < 256)
        {
            bb.minX = bb.minX >> 4;
            bb.minZ = bb.minZ >> 4;
            bb.maxX = bb.maxX >> 4;
            bb.maxZ = bb.maxZ >> 4;
            for (int chunkX = bb.minX; chunkX <= bb.maxX; ++chunkX)
            {
                for (int chunkZ = bb.minZ; chunkZ <= bb.maxZ; ++chunkZ)
                {
                	list.add(new Position(chunkX,0,chunkZ));
                }
            }
        }
		return list;
	}
	
	public static class PlacementProperties
	{
		private BlockPos origin;
		private String schematic;
		private Rotation rotation;
		private Mirror mirror;
		/** A list of chunks, where it still needs to generate*/
		private List<Position> unfinishedChunks=new ArrayList<Position>();
		
		public PlacementProperties(NBTTagCompound nbt)
		{
			this.readFromNBT(nbt);
			if(this.origin == null || this.rotation==null || this.mirror==null)
				throw new IllegalArgumentException("Error reading nbt from save");
		}
		public PlacementProperties(BlockPos origin, Rotation rot, Mirror mirror, String name)
		{
			this.origin=origin;
			this.rotation=rot;
			this.mirror=mirror;
			this.schematic=name;
		}
		
		public String getIdentifier()
		{
			return this.schematic;
		}
		
		public BlockPos getOrigin()
		{
			return this.origin;
		}
		
		public Rotation rot()
		{
			return this.rotation;
		}
		public Mirror mirror()
		{
			return this.mirror;
		}
		
		public List<Position> getUnfinishedChunks()
		{
			return this.unfinishedChunks;
		}
		
		public boolean isFinished()
		{
			return this.unfinishedChunks.isEmpty();
		}
		
		public void readFromNBT(NBTTagCompound nbt)
		{
			this.origin=new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
			this.rotation=Rotation.values()[nbt.getInteger("Rotation")];
			this.mirror=Mirror.values()[nbt.getInteger("Mirror")];
			this.schematic=nbt.getString("Structure");
			NBTTagList list = nbt.getTagList("Unfinished", Constants.NBT.TAG_INT_ARRAY);
			list.forEach(tag->this.unfinishedChunks.add(new Position(((NBTTagIntArray)tag).getIntArray()[0], 0, ((NBTTagIntArray)tag).getIntArray()[1])));
		}
		
		public NBTTagCompound writeToNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("x", this.origin.getX());
			nbt.setInteger("y", this.origin.getY());
			nbt.setInteger("z", this.origin.getZ());
			nbt.setInteger("Rotation", this.rotation.ordinal());
			nbt.setInteger("Mirror", this.mirror.ordinal());
			nbt.setString("Structure", this.schematic);
			NBTTagList list = new NBTTagList();
			unfinishedChunks.forEach(p->list.appendTag(new NBTTagIntArray(new int[] {p.getX(), p.getZ()})));
			nbt.setTag("Unfinished", list);
			return nbt;
		}
		
		@Override
		public String toString()
		{
			return "Prop:["+this.schematic+"," + this.origin + "," + this.rotation +"," + this.mirror +",Finished Generating:"+ this.unfinishedChunks.isEmpty()+"]"; 
		}
		/*
		 *     @Nullable
    private StructureBoundingBox getBoundingBoxFromChunk(@Nullable ChunkPos pos)
    {
        if (pos == null)
        {
            return null;
        }
        else
        {
            int i = pos.x * 16;
            int j = pos.z * 16;
            return new StructureBoundingBox(i, 0, j, i + 16 - 1, 255, j + 16 - 1);
        }
    }
		 */
	}
}
