package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.mappings.Quests;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.quests.QuestMission;
import com.flemmli97.runecraftory.common.core.handler.quests.WeightedTable.Difficulty;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRequestBoard extends Block{
	
	public BlockRequestBoard() {
		super(Material.ROCK);
		this.setCreativeTab(RuneCraftory.blocks);
        this.blockSoundType = SoundType.WOOD;
        this.setResistance(5.0F);
        this.setHardness(3.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "black_board"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	private AxisAlignedBB box = new AxisAlignedBB(0,0,0,1,0.875,1);
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.box;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(world.isRemote)
			return true;
		if(!world.isRemote)
		{
			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
			QuestMission q = new QuestMission(Quests.randomKillObjective(RANDOM, (WorldServer)world, Difficulty.EASY), null);
			if(cap.acceptMission(q))
				player.sendStatusMessage(new TextComponentString(TextFormatting.GOLD + q.questAcceptMsg()), true);
			else
			{
				if(cap.currentMission().questObjective().isFinished())
					cap.finishMission(player);
			}
			return true;
		}
        return false;
    }
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
