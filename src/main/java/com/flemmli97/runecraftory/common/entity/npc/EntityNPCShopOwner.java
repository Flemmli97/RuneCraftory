package com.flemmli97.runecraftory.common.entity.npc;

import java.util.List;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IShop;
import com.flemmli97.runecraftory.api.mappings.NPCShopItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumShopResult;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.PacketUpdateShopItems;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class EntityNPCShopOwner extends EntityNPCBase implements IShop
{
    private static final DataParameter<Integer> SHOPTYPE = EntityDataManager.createKey(EntityNPCShopOwner.class, DataSerializers.VARINT);
    private NonNullList<ItemStack> shopItems = NonNullList.create();;

    public EntityNPCShopOwner(World world) {
        this(world, EnumShop.GENERAL);
    }
    
    public EntityNPCShopOwner(World world, EnumShop profession) {
        super(world);
        if (!world.isRemote) {
            this.setProfession(profession);
        }
    }
    
    @Override
	public void entityInit() {
        super.entityInit();
        this.dataManager.register(SHOPTYPE, EnumShop.GENERAL.ordinal());
    }
    
    public EnumShop profession() {
        return EnumShop.values()[(int)this.dataManager.get(SHOPTYPE)];
    }
    
    public void setProfession(EnumShop profession) {
        this.dataManager.set(EntityNPCShopOwner.SHOPTYPE, profession.ordinal());
        this.generateRandomShopItems();
    }
    
    @Override
    public NonNullList<ItemStack> shopItems() {
        return this.shopItems;
    }
    
    @Override
    public EntityNPCShopOwner shopOwner() {
        return this;
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (RFCalculations.canUpdateDaily(this.world)) {
            this.generateRandomShopItems();
        }
    }
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Shop", ItemStackHelper.saveAllItems(new NBTTagCompound(), this.shopItems));
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Shop")) {
            ItemStackHelper.loadAllItems(compound.getCompoundTag("Shop"), this.shopItems);
        }
        PacketHandler.sendToAll(new PacketUpdateShopItems(this, this.shopItems));
    }
    
    @Override
    public void writeSpawnData(ByteBuf buffer) {
    	NBTTagCompound compound = new NBTTagCompound();
        NBTTagList tagList = new NBTTagList();
        for (ItemStack stack : this.shopItems) {
            tagList.appendTag(stack.writeToNBT(new NBTTagCompound()));
        }
        compound.setTag("Items", tagList);
        ByteBufUtils.writeTag(buffer, compound);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
    	NBTTagCompound compound = ByteBufUtils.readTag(additionalData);
        NBTTagList list = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); ++i) {
            this.shopItems.add(new ItemStack(list.getCompoundTagAt(i)));
        }
    }
    
    public void generateRandomShopItems() 
    {
        this.shopItems.clear();
        List<ItemStack> list = NPCShopItems.getShopList(this.profession());
        for (float chance = 2.0f; this.rand.nextFloat() < chance; chance -= 0.1f) 
        {
            ItemStack stack = list.get(this.rand.nextInt(list.size()));
            if (!this.shopItems.contains(stack)) 
            {
                this.shopItems.add(stack);
            }
        }
        if(!this.world.isRemote)
        	PacketHandler.sendToAll(new PacketUpdateShopItems(this, this.shopItems));
    }
    
    @Override
    public void updateShopItems(NonNullList<ItemStack> list) {
        this.shopItems = list;
    }
    
    @Override
    public String purchase(EnumShopResult result) {
        String s = "";
        switch (result) {
            case NOMONEY: {
                s = "Seems like you have not enough money.";
                break;
            }
            case NOSPACE: {
                s = "Seems like you have not enough space.";
                break;
            }
            case SUCCESS: {
                s = "Thank you.";
                break;
            }
        }
        return s;
    }
    
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (this.world.isRemote) {
            return true;
        }
        player.openGui(RuneCraftory.instance, LibReference.guiShop, this.world, this.getEntityId(), 0, 0);
        return true;
    }
    
    public enum EnumShop
    {
        GENERAL, 
        FLOWER, 
        WEAPON, 
        CLINIC, 
        FOOD, 
        MAGIC, 
        RUNESKILL, 
        RANDOM;
    }
}
