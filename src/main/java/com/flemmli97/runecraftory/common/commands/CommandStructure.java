package com.flemmli97.runecraftory.common.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.world.Schematic;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class CommandStructure implements ICommand{
	
	private final List<String> aliases = new ArrayList<String>();
	private boolean confirm=false;
	public CommandStructure()
	{
		this.aliases.add("template");
	}
	@Override
	public int compareTo(ICommand o) {
		return 0;
	}

	@Override
	public String getName() {
		return "template";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "template <save,load> x y z [x2 y2 z2 name;name]";
	}

	@Override
	public List<String> getAliases() {
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length<1)
        {
			((EntityPlayer)sender.getCommandSenderEntity()).sendStatusMessage(new TextComponentString(TextFormatting.RED + "Usage: /" + this.getUsage(sender)), false);
			return;
        }
		try
		{
            BlockPos blockpos = CommandBase.parseBlockPos(sender, args, 1, false);
    		String s = args[0];	      		
			if(blockpos==null)
				((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /" + this.getUsage(sender)));
			else
			{        				
				if(s.equals("save"))
				{
					if (args.length < 8)
			        {
						((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /" + this.getUsage(sender)));
						return;
			        }
                    BlockPos blockpos1 = CommandBase.parseBlockPos(sender, args, 4, false);
                    if(blockpos1==null)
					{
        				((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /" + this.getUsage(sender)));
        				return;
					}
                    BlockPos size = this.translatePos2(blockpos, blockpos1);
                    blockpos = this.translatePos1(blockpos, blockpos1);
                    size=this.getSize(blockpos, size);
        			String name = args[7];

					World world = server.getEntityWorld();
    				if(world instanceof WorldServer)
    				{
        				WorldServer worldserver = (WorldServer)world;
        	            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        	            Template template = templatemanager.getTemplate(world.getMinecraftServer(), new ResourceLocation(name));
        	            if((template.getSize().getX()!=0||  template.getSize().getY()!=0||template.getSize().getZ()!=0)&&!confirm)
        	            {
        	            	confirm=true;
            				((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "File already exist. Type again to confirm."));
            				return;
        	            }
        	            confirm=false;
        	            template.takeBlocksFromWorld(world, blockpos, size, true, ModBlocks.ignore);
        	            templatemanager.writeTemplate(world.getMinecraftServer(), new ResourceLocation(name));
        				((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "Saved template as " + name + ".nbt"));
    				}
				}
				else if(s.equals("load"))
				{
					if (args.length < 5)
			        {
						((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /" + this.getUsage(sender)));
						return;
			        }
        			String name = args[4];
					World world = server.getEntityWorld();
    				if(world instanceof WorldServer)
    				{
        				WorldServer worldserver = (WorldServer)world;
        	            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        	            Template template = templatemanager.getTemplate(world.getMinecraftServer(), new ResourceLocation(name));
        	            Schematic.fromTemplate(template).generate(world, blockpos, Rotation.NONE, Mirror.NONE, null);
        				((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "Loading template " + name + ".nbt"));
    				}
				}
			}
		}
		catch(NumberFormatException e)
		{
			((EntityPlayer)sender.getCommandSenderEntity()).sendMessage(new TextComponentString(TextFormatting.RED + "Usage: /" + this.getUsage(sender)));
		}		
	}
	
	private BlockPos translatePos1(BlockPos pos1, BlockPos pos2)
	{
		return new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
	}
	
	private BlockPos translatePos2(BlockPos pos1, BlockPos pos2)
	{
		return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
	}
	
	private BlockPos getSize(BlockPos pos1, BlockPos pos2)
	{
		int x = Math.abs(pos2.getX()- pos1.getX())+1;
		int y = Math.abs(pos2.getY()- pos1.getY())+1;
		int z = Math.abs(pos2.getZ()- pos1.getZ())+1;
		return new BlockPos(x,y,z);
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender.canUseCommand(2, this.getName());
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, new String[] {"save", "load"});
        }
		else if (args.length > 1 && args.length <= 4)
        {
            return CommandBase.getTabCompletionCoordinate(args, 1, targetPos);
        }
        else if (args[0].equals("save") && args.length > 4 && args.length <= 7)
        {
            return CommandBase.getTabCompletionCoordinate(args, 4, targetPos);
        }
		return Collections.<String>emptyList();
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}
}
