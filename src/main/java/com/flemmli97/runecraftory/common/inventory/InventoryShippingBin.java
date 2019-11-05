package com.flemmli97.runecraftory.common.inventory;

import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.utils.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class InventoryShippingBin extends InventoryBasic
{
    public InventoryShippingBin() {
        super("container.shippingbin", false, 54);
    }
    
    public void loadInventoryFromNBT(NBTTagList p_70486_1_) {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, ItemStack.EMPTY);
        }
        for (int k = 0; k < p_70486_1_.tagCount(); ++k) {
            NBTTagCompound nbttagcompound = p_70486_1_.getCompoundTagAt(k);
            int j = nbttagcompound.getByte("Slot") & 0xFF;
            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, new ItemStack(nbttagcompound));
            }
        }
    }
    
    public NBTTagList saveInventoryToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag((NBTBase)nbttagcompound);
            }
        }
        return nbttaglist;
    }
    
    public void shipItems(EntityPlayer player) {
        IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        int money = 0;
        for (int i = 0; i < this.getSizeInventory(); ++i) {
        	if(this.getStackInSlot(i).isEmpty())
        		continue;
            money += ItemUtils.getSellPrice(this.getStackInSlot(i)) * this.getStackInSlot(i).getCount();
            cap.addShippingItem(player, this.removeStackFromSlot(i));
        }
        cap.setMoney(player, cap.getMoney() + money);
        player.sendStatusMessage((ITextComponent)new TextComponentString(TextFormatting.GOLD + "Total earning today: " + money), true);
        this.markDirty();
    }
}
