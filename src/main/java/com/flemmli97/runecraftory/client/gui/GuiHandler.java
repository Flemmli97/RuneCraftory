package com.flemmli97.runecraftory.client.gui;

import com.flemmli97.runecraftory.common.blocks.tile.TileMultiBase;
import com.flemmli97.runecraftory.common.blocks.tile.TileResearchTable;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.entity.npc.EntityNPCShopOwner;
import com.flemmli97.runecraftory.common.inventory.ContainerInfoScreen;
import com.flemmli97.runecraftory.common.inventory.ContainerInfoScreenSub;
import com.flemmli97.runecraftory.common.inventory.ContainerMaking;
import com.flemmli97.runecraftory.common.inventory.ContainerResearch;
import com.flemmli97.runecraftory.common.inventory.ContainerShop;
import com.flemmli97.runecraftory.common.inventory.ContainerUpgrade;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    {
        if (ID == LibReference.guiInfo) 
            return new ContainerInfoScreen(player);
        if (ID == LibReference.guiInfoSub) 
            return new ContainerInfoScreenSub(player);
        if (ID == LibReference.guiMaking) 
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileMultiBase) 
            {
                return new ContainerMaking(player.inventory, (TileMultiBase)tile);
            }
        }
        else if (ID == LibReference.guiUpgrade) 
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileMultiBase) 
            {
                return new ContainerUpgrade(player.inventory, (TileMultiBase)tile);
            }
        }
        else if (ID == LibReference.guiResearch) 
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileResearchTable) 
            {
                return new ContainerResearch(player.inventory, (TileResearchTable)tile);
            }
        }
        else if (ID == LibReference.guiShipping) 
        {
            return new ContainerChest(player.inventory, player.getCapability(PlayerCapProvider.PlayerCap, null).getShippingInv(), player);
        }
        else if (ID == LibReference.guiShop) 
        {
            Entity e = player.world.getEntityByID(x);
            if (e instanceof EntityNPCShopOwner) 
            {
                return new ContainerShop(player, (EntityNPCShopOwner)e);
            }
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == LibReference.guiInfo)
            return new GuiInfoScreen(Minecraft.getMinecraft());
        if (ID == LibReference.guiInfoSub)
            return new GuiInfoScreenSub(Minecraft.getMinecraft());
        if (ID == LibReference.guiMaking) 
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileMultiBase) 
            {
                return new GuiMaking(((TileMultiBase)tile).type(), player.inventory, (TileMultiBase)tile);
            }
        }
        else if (ID == LibReference.guiUpgrade) 
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileMultiBase) 
            {
                return new GuiUpgrade(((TileMultiBase)tile).type(), player.inventory, (TileMultiBase)tile);
            }
        }
        else if (ID == LibReference.guiResearch) 
        {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            if (tile instanceof TileResearchTable) 
            {
                return new GuiResearch(player.inventory, (TileResearchTable)tile);
            }
        }
        else if (ID == LibReference.guiShipping) 
        {
            return new GuiChest(player.inventory, (player.getCapability(PlayerCapProvider.PlayerCap, null)).getShippingInv());
        }
        else if (ID == LibReference.guiShop) 
        {
            Entity e = player.world.getEntityByID(x);
            if (e instanceof EntityNPCShopOwner) 
            {
                return new GuiShop(player, (EntityNPCShopOwner)e);
            }   
        }
        return null;
    }
}
