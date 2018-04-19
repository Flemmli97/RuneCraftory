package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class SchematicsLoader {
	
	//for future?
	
	/*public static Schematic loadSchematic(String filePath, World world)
	{
		try {
			InputStream input = Loader.class.getResourceAsStream("/assets/runecraftory/structures/" + filePath + ".schematic");
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(input);
			short width = nbt.getShort("Width");
			short height = nbt.getShort("Height");
			short length = nbt.getShort("Length");
			byte[] blocks = nbt.getByteArray("Blocks");
			byte[] data = nbt.getByteArray("Data");
			NBTTagList tileEntities = nbt.getTagList("TileEntities", 10);	
			ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();
			Map<NewBlockPos, IBlockState> posBlockMapping = Maps.newHashMap();
			NBTTagCompound idNameMap = nbt.getCompoundTag("SchematicaMapping");
			System.out.println(idNameMap);
			for (int x = 0; x < width; x++) {
	            for (int y = 0; y < height; y++) {
	                for (int z = 0; z < length; z++) {
	                    final int index = x + (y * length + z) * width;
	                    int blockID = blocks[index] & 0xFF;
	                    final int meta = data[index] & 0xFF;
	                    final Block block = Block.REGISTRY.getObjectById(blockID);
	                    NewBlockPos pos = new NewBlockPos(x, y, z);
	                    try {
	                        @SuppressWarnings("deprecation")
							final IBlockState blockState = block.getStateFromMeta(meta);
	                        posBlockMapping.put(pos, blockState);
	                    } catch (final Exception e) {
	                        RuneCraftory.logger.error("Error with block state at {"+pos+"} with block {"+block+"} and metadata {"+meta+"}");
	                    }
	                }
	            }
			}	                 
	                   
			for(int i = 0; i < tileEntities.tagCount(); i++)
			{
				tiles.add(TileEntity.create(world, tileEntities.getCompoundTagAt(i)));
			}
			Schematic schematic = new Schematic(width, height, length, posBlockMapping, tiles);		
			return schematic;
		} 
		catch (IOException e) {
			RuneCraftory.logger.error("Error reading schematic file", e);
			return null;
		}
	}*/
	
	//I feel dumb about this part since the code is already in minecraft...
	
	/*public static Schematic loadStructureBlockFile(String filePath, World world)
	{
		try {
			InputStream input = Loader.class.getResourceAsStream("/assets/runecraftory/structures/" + filePath + ".nbt");
			NBTTagCompound nbt = CompressedStreamTools.readCompressed(input);
			NBTTagList size = nbt.getTagList("size", 3);
			NBTTagList palette = nbt.getTagList("palette", 10);
			NBTTagList blocks = nbt.getTagList("blocks", 10);
			Map<NewBlockPos, IBlockState> posBlockMapping = Maps.newHashMap();
			ArrayList<TileEntity> tiles = new ArrayList<TileEntity>();
			
			for(int i = 0;i < blocks.tagCount(); i++)
			{
				NBTTagList a = blocks.getCompoundTagAt(i).getTagList("pos", 3);
				NewBlockPos pos = new NewBlockPos(a.getIntAt(0), a.getIntAt(1), a.getIntAt(2));
				int stateId = blocks.getCompoundTagAt(i).getInteger("state");				
				Block block = Block.REGISTRY.getObject(new ResourceLocation(palette.getCompoundTagAt(stateId).getString("Name")));
				try {
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("Name", palette.getCompoundTagAt(stateId).getString("Name"));
					if(palette.getCompoundTagAt(stateId).hasKey("Properties"))
						tag.setTag("Properties", palette.getCompoundTagAt(stateId).getCompoundTag("Properties"));
					IBlockState state = NBTUtil.readBlockState(tag);
					posBlockMapping.put(pos, state);

				}
				catch (final Exception e) {
                    RuneCraftory.logger.error("Error with block state at {"+pos+"} with block {"+block+"}");
                }
				if(blocks.getCompoundTagAt(i).hasKey("nbt"))
				{
					NBTTagCompound tileData= blocks.getCompoundTagAt(i).getCompoundTag("nbt");
					TileEntity tile = TileEntity.create(world, tileData);
					if(tile!=null)
					{
						tile.setPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
						tiles.add(tile);
					}
					else
						RuneCraftory.logger.error("Error reading tile entity from nbt" + tileData);
				}
			}
			Schematic schematic = new Schematic(size.getIntAt(0), size.getIntAt(1), size.getIntAt(2), posBlockMapping, tiles);		
			return schematic;
		} 
		catch (IOException e) {
			RuneCraftory.logger.error("Error reading schematic file", e);
			return null;
		}
	}*/

	@SuppressWarnings("incomplete-switch")
	public static void spawnStructureAt(World world, BlockPos pos, String name, EnumFacing facing, Mirror mirror)
	{
		WorldServer worldserver = (WorldServer) world;
		MinecraftServer minecraftserver = world.getMinecraftServer();
		TemplateManager templatemanager = worldserver.getStructureTemplateManager();
		ResourceLocation loc = new ResourceLocation(LibReference.MODID, name);
		Template template = templatemanager.get(minecraftserver, loc);
		Rotation rot = Rotation.NONE;
		switch(facing)
		{
		case EAST:rot=Rotation.CLOCKWISE_90;
			break;
		case NORTH:rot=Rotation.NONE;
			break;
		case SOUTH:rot=Rotation.CLOCKWISE_180;
			break;
		case WEST:rot=Rotation.COUNTERCLOCKWISE_90;
			break;
		}
		if (template != null) 
		{
			IBlockState iblockstate = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);
			PlacementSettings placementsettings = new PlacementSettings();
			placementsettings.setMirror(mirror);
			placementsettings.setRotation(rot);

			template.addBlocksToWorldChunk(world, pos.add(0, 0, 0), placementsettings);
		}
	}
}
