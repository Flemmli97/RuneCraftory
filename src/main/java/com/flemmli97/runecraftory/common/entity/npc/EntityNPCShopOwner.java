package com.flemmli97.runecraftory.common.entity.npc;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumShop;
import com.flemmli97.runecraftory.common.lib.enums.EnumShopResult;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
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

public class EntityNPCShopOwner extends EntityNPCBase
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
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
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
}
