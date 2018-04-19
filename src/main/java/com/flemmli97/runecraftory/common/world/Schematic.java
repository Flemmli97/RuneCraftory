package com.flemmli97.runecraftory.common.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.blocks.tile.TileBossSpawner;
import com.flemmli97.runecraftory.common.utils.Position;
import com.flemmli97.runecraftory.common.world.Structure.PlacementProperties;

import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;

public class Schematic {
	
	private final int width, height, length;
	private List<TileEntity> tiles;
	private Map<Position, IBlockState> posBlockMapping = new HashMap<Position, IBlockState>();
	private Map<Position, NBTTagCompound> posTileMapping = new HashMap<Position, NBTTagCompound>();
	public Schematic(int width, int height, int length)
	{
		this.width=width;
		this.height=height;
		this.length=length;
	}
	
	public static Schematic fromTemplate(Template t)
	{
		return StructureLoader.loadFromNBT(t.writeToNBT(new NBTTagCompound()));
	}
	
	public void addBlockStateToPos(Position position, IBlockState state)
	{
		this.posBlockMapping.put(position, state);
	}
	
	public void addTileToPos(Position position, NBTTagCompound nbt)
	{
		this.posTileMapping.put(position, nbt);
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public int getLength()
	{
		return this.length;
	}
	
	public List<TileEntity> getTileEntities() 
	{
		return this.tiles;
	}
	
	public IBlockState getBlockAt(BlockPos pos)
	{
		Position pos1 = new Position(pos.getX(), pos.getY(), pos.getZ());
		IBlockState state = this.posBlockMapping.get(pos1);
		return state;
	}
	

	public void generate(World world, BlockPos blockpos, Rotation rot, Mirror mirror, Object object) {
		this.generate(world, new PlacementProperties(blockpos, rot, mirror, ""), null);
	}
	
	/**
	 * Tries to start generating a structure. Ignores structureblocks
	 * @param world	World
	 * @param pos The position of the structure
	 * @param rot Rotation
	 * @param mirror Mirror
	 * @param restriction Restrict structure to the aabb
	 */
	public synchronized void generate(World world, PlacementProperties props, @Nullable AxisAlignedBB restriction)
	{
		for(int z = 0; z < this.length; z++)
			for(int x = 0; x < this.width; x++)
				for(int y = 0; y < this.height; y++)
				{
					Position schemPos = new Position(x,y,z);
					BlockPos place = transformedBlockPos(schemPos, props.mirror(), props.rot()).add(props.getOrigin());
					if(restriction ==null || restriction.contains(new Vec3d(place)))
					{
						//check if setting a block there will cause a new chunk to generate. to prevent cascading worldgen lag
							if(this.isNeighborChunkLoaded(world, place, props.getUnfinishedChunks()))
							{
								IBlockState state = this.posBlockMapping.get(schemPos);
								if(state!=null && !(state.getBlock() instanceof BlockStructure))
								{
									world.setBlockState(place, state.withMirror(props.mirror()).withRotation(props.rot()), 18);
								}
								if(this.posTileMapping.containsKey(schemPos))
								{
									TileEntity tile = world.getTileEntity(place);
									if(tile!=null)
									{
										NBTTagCompound tileNBT = this.posTileMapping.get(schemPos);
										tileNBT.setInteger("x", place.getX());
			                            tileNBT.setInteger("y", place.getY());
			                            tileNBT.setInteger("z", place.getZ());
										tile.readFromNBT(tileNBT);
										tile.mirror(props.mirror());
										tile.rotate(props.rot());
										tile.markDirty();
										if(tile instanceof TileBossSpawner)
										{
											((TileBossSpawner)tile).spawnEntity();
										}
									}
								}
							}
					}
				}		
	}
	
	private boolean isNeighborChunkLoaded(World world, BlockPos pos, List<Position> list)
	{
		BlockPos west = pos.offset(EnumFacing.WEST);
		BlockPos north = pos.offset(EnumFacing.NORTH);
		BlockPos east = pos.offset(EnumFacing.EAST);
		BlockPos south = pos.offset(EnumFacing.SOUTH);
		boolean flagPos = world.isChunkGeneratedAt(pos.getX()>>4, pos.getZ()>>4);
		boolean flagNorth = world.isChunkGeneratedAt(north.getX()>>4, north.getZ()>>4);
		boolean flagWest = world.isChunkGeneratedAt(west.getX()>>4, west.getZ()>>4);
		boolean flagSouth = world.isChunkGeneratedAt(south.getX()>>4, south.getZ()>>4);
		boolean flagEast = world.isChunkGeneratedAt(east.getX()>>4, east.getZ()>>4);
		return flagPos&&flagNorth&&flagWest&& flagSouth&& flagEast;
	}
	/**
	 * From net.minecraft.world.gen.structure.template.Template
	 */
    public static BlockPos transformedBlockPos(Position pos, Mirror mirrorIn, Rotation rotationIn)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        boolean flag = true;

        switch (mirrorIn)
        {
            case LEFT_RIGHT:
                k = -k;
                break;
            case FRONT_BACK:
                i = -i;
                break;
            default:
                flag = false;
        }

        switch (rotationIn)
        {
            case COUNTERCLOCKWISE_90:
                return new BlockPos(k, j, -i);
            case CLOCKWISE_90:
                return new BlockPos(-k, j, i);
            case CLOCKWISE_180:
                return new BlockPos(-i, j, -k);
            default:
                return flag ? new BlockPos(i, j, k) : new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        }
    }
	
	@Override
	public String toString()
	{
		return "Schematic:{[Width:" + this.width + "],[Height:" + this.height + "],[Length:" + this.length+ "],[Tiles:" + this.tiles + "]}";
	}
}
