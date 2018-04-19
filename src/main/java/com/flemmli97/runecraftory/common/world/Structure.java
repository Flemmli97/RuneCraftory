package com.flemmli97.runecraftory.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.Position;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class Structure {
	
	private Schematic schematic;
	private boolean underGround;
	private int dimID, freq, yOffset;
	private String name;
	
	private ArrayList<Type> biomeTypes = new ArrayList<Type>();
    protected Map<Position, PlacementProperties> structureMap = new HashMap<Position, PlacementProperties>();
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
	
	public void generate(World world, int chunkX, int chunkZ, Random random)
	{
		Position p = new Position(chunkX, 0, chunkZ);
		StructureData data = StructureData.get(world);
		if(!data.hasPosition(p))
		{
			if(world.provider.getDimension() ==this.dimID || world.provider.getDimension()==LibReference.dimID)
			{
				if(random.nextInt(this.freq)==0)
				{
					int x = chunkX * 16 + random.nextInt(8)+8;
					int z = chunkZ * 16 + random.nextInt(8)+8;
					int y = random.nextInt(20)+20;
					Rotation rot = Rotation.values()[random.nextInt(Rotation.values().length)];
					Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
					BlockPos pos = this.underGround?new BlockPos(x,y,z):this.calculateGroundHeight(world, new BlockPos(x,255,z)).add(0, this.yOffset, 0);
					int chunkMinY = world.getChunkFromChunkCoords(chunkX, chunkZ).getLowestHeight();
					if(this.underGround || Math.abs(pos.getY()-chunkMinY)<this.yOffset)
						for(Type type : this.biomeTypes)
						{
							if(BiomeDictionary.hasType(world.getBiome(pos), type))
							{
								PlacementProperties props =  new PlacementProperties(pos, rot, mirror, this.name);
								this.schematic.generate(world, props, null);
								props.getUnfinishedChunks().forEach(position->this.structureMap.put(position, props));
								data.put(p, props);
								return;
							}
						}
				}
			}
		}
		else if(this.structureMap.containsKey(p))
		{
			PlacementProperties prop = this.structureMap.get(p);
			prop.getUnfinishedChunks().remove(p);
			this.structureMap.remove(p);
			this.schematic.generate(world, prop, new AxisAlignedBB(chunkX*16, 0, chunkZ*16, chunkX*16+16, 255, chunkZ*16+16));
		}
	}
	
	private BlockPos calculateGroundHeight(World world, BlockPos pos)
	{
		Biome biome = world.getBiome(pos);
		IBlockState state = biome.topBlock;
		//checking for trees since they turn grass into dirt
		boolean grass = state.getBlock()==Blocks.GRASS;
		while(pos.getY()>1)
		{
			IBlockState check = world.getBlockState(pos);
			if(((grass && check.getBlock()==Blocks.DIRT) || check.getBlock()==state.getBlock()) && (world.getBlockState(pos.up()).getMaterial()==Material.AIR ||world.getBlockState(pos.up()).getMaterial().isReplaceable()))
			{			
				return pos;
			}
			pos = pos.down();
		}
		return pos;
	}
	
	public void generate(World world, int chunkX, int chunkZ)
	{
		for(Position p : this.structureMap.keySet())
		{
			if(new Position(chunkX, 0, chunkZ).equals(p))
			{
				this.structureMap.remove(p);
			}
		}
	}

	/*public void generate(World world, int chunkX, int chunkZ, Random random)
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
					//start building a structure
					int y = random.nextInt(20)+20;
					Rotation rot = Rotation.values()[random.nextInt(Rotation.values().length)];
					Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
					BlockPos pos = this.underGround?new BlockPos(x,y,z):world.getHeight(new BlockPos(x,y,z)).add(0, this.yOffset, 0);
					world.getChunkFromChunkCoords(chunkX, chunkZ).getLowestHeight();
					Ints.max(world.getChunkFromChunkCoords(chunkX, chunkZ).getHeightMap());
					for(Type type : this.biomeTypes)
					{
						if(BiomeDictionary.hasType(world.getBiome(pos), type))
						{
							List<Position> list = schematic.generate(world, pos, rot, mirror, null);
							//for(Position chunk : list)
							//	data.put(chunk, new PlacementProperties(pos, rot, mirror, this.name));
							return;
						}
					}
				}
			}	
		}
		else
		{
			//continue building the a structure in case the chunk wasn't fully loaded.
			PlacementProperties prop = data.read(p);
			schematic.generate(world, prop.getOrigin(), prop.rot(), prop.mirror(), new AxisAlignedBB(chunkX*16, 0, chunkZ*16, chunkX*16+16, 255, chunkZ*16+16).grow(2, 0, 2));
		}
	}*/
	/*private AxisAlignedBB fromPos(int chunkX, int chunkZ)
	{
		int i = chunkX * 16;
        int j = chunkZ * 16;
		return new AxisAlignedBB(i, 0, j, i+15, 255, j+15);
	}*/
	/*public BlockPos offSetToMid(BlockPos pos, PlacementProperties props)
	{
		return pos;
	}
	
	public List<Position> calculateChunksFromStructure(PlacementProperties props)
	{
		List<Position> list = new ArrayList<Position>();
		//Position originChunk = props.getOrigin().getX()
		int chunkWidth = this.schematic.getWidth()>>4;
		int chunkLength = this.schematic.getLength()>>4;
		for(int x = 0; x <= chunkWidth; x++)
			for(int z = 0; z <= chunkLength; z++)
				{
					BlockPos pos = Schematic.transformedBlockPos(new Position(x, 0, z), props.mirror(), props.rot());
					System.out.println(pos);
					Position chunk = new Position((props.getOrigin().getX()>>4)+pos.getX(), 0, (props.getOrigin().getZ()>>4)+pos.getZ());
					if(!list.contains(chunk))
						list.add(chunk);
				}
		return list;
	}*/
	
	public static class PlacementProperties
	{
		private BlockPos origin;
		private String schematic;
		private Rotation rotation;
		private Mirror mirror;
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
