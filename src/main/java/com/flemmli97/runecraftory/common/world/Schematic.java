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
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
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
	
	/**
	 * Generates this schematic. Ignores structureblocks
	 * @param world	World
	 * @param pos The position of the structure
	 * @param rot Rotation
	 * @param mirror Mirror
	 * @param restriction Restrict structure to the aabb
	 */
	public void generate(World world, BlockPos pos, Rotation rot, Mirror mirror, @Nullable StructureBoundingBox restriction, boolean replaceGroundBelow) {
		for(int z = 0; z < this.length; z++)
			for(int x = 0; x < this.width; x++)
				for(int y = 0; y < this.height; y++)
				{
					Position schemPos = new Position(x,y,z);
					BlockPos place = transformedBlockPos(schemPos, mirror, rot).add(pos);
					if(restriction ==null || restriction.isVecInside(new Vec3i(place.getX(),place.getY(),place.getZ())))
					{
						IBlockState state = this.posBlockMapping.get(schemPos);
						if(state!=null && !(state.getBlock() instanceof BlockStructure))
						{
							world.setBlockState(place, state.withMirror(mirror).withRotation(rot), 18);
							if(replaceGroundBelow && y == 0)
							{
								Biome biome =world.getBiome(place);
								replaceAirAndLiquidDownwards(world, biome.fillerBlock, place.down());
							}
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
								tile.mirror(mirror);
								tile.rotate(rot);
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
	
	public void generate(World world, PlacementProperties props, @Nullable StructureBoundingBox restriction, boolean replaceGroundBelow)
	{
		this.generate(world, props.getOrigin(), props.rot(), props.mirror(), restriction, replaceGroundBelow);
	}
	
	public void generate(World world, BlockPos pos, Rotation rot, Mirror mirror, @Nullable StructureBoundingBox restriction) {
		this.generate(world, pos, rot, mirror, restriction, false);
	}
		
	
	private static void replaceAirAndLiquidDownwards(World worldIn, IBlockState blockstateIn, BlockPos pos)
    {
        while ((worldIn.getBlockState(pos).getMaterial().isReplaceable()||worldIn.isAirBlock(pos) || worldIn.getBlockState(pos).getMaterial().isLiquid()) && pos.getY() > 1)
        {
            worldIn.setBlockState(pos, blockstateIn, 2);
            pos=pos.down();
        }
    }
	
	/*private boolean isNeighborChunkLoaded(World world, BlockPos pos, List<Position> list)
	{
		BlockPos west = pos.offset(EnumFacing.WEST);
		BlockPos north = pos.offset(EnumFacing.NORTH);
		BlockPos east = pos.offset(EnumFacing.EAST);
		BlockPos south = pos.offset(EnumFacing.SOUTH);
		boolean flagPos = world.getChunkProvider().getLoadedChunk(pos.getX()>>4, pos.getZ()>>4)!=null;
		boolean flagNorth = world.getChunkProvider().getLoadedChunk(north.getX()>>4, north.getZ()>>4)!=null;
		boolean flagWest = world.getChunkProvider().getLoadedChunk(west.getX()>>4, west.getZ()>>4)!=null;
		boolean flagSouth = world.getChunkProvider().getLoadedChunk(south.getX()>>4, south.getZ()>>4)!=null;
		boolean flagEast = world.getChunkProvider().getLoadedChunk(east.getX()>>4, east.getZ()>>4)!=null;
		if(!flagPos)
			list.add(new Position(pos.getX()>>4, 0, pos.getZ()>>4));
		if(!flagNorth)
			list.add(new Position(north.getX()>>4, 0, north.getZ()>>4));
		if(!flagWest)
			list.add(new Position(west.getX()>>4, 0, west.getZ()>>4));
		if(!flagSouth)
			list.add(new Position(south.getX()>>4, 0, south.getZ()>>4));
		if(!flagEast)
			list.add(new Position(east.getX()>>4, 0, east.getZ()>>4));
		return flagPos&&flagNorth&&flagWest&& flagSouth&& flagEast;
	}*/
	/**
	 * From net.minecraft.world.gen.structure.template.Template
	 */
	public static BlockPos transformedBlockPos(Position pos, Mirror mirrorIn, Rotation rotationIn)
    {
		return transformedBlockPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), mirrorIn, rotationIn);
    }
    public static BlockPos transformedBlockPos(BlockPos pos, Mirror mirrorIn, Rotation rotationIn)
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
