package com.flemmli97.runecraftory.common.entity.npc;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumShop;
import com.flemmli97.runecraftory.common.lib.enums.EnumShopResult;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityNPCShopOwner extends EntityNPCBase
{
    private static final DataParameter<Byte> SHOPTYPE = EntityDataManager.createKey(EntityNPCShopOwner.class, DataSerializers.BYTE);

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
        this.dataManager.register(SHOPTYPE, (byte) EnumShop.GENERAL.ordinal());
    }
    
    public EnumShop profession() {
        return EnumShop.values()[this.dataManager.get(SHOPTYPE)];
    }
    
    public void setProfession(EnumShop profession) {
        this.dataManager.set(EntityNPCShopOwner.SHOPTYPE, (byte) profession.ordinal());
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("ShopType", this.dataManager.get(SHOPTYPE));
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(SHOPTYPE, compound.getByte("ShopType"));
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
