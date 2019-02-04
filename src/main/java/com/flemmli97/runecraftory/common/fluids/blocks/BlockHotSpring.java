package com.flemmli97.runecraftory.common.fluids.blocks;

import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.fluids.FluidHotSpring;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockHotSpring extends BlockFluidClassic
{
    public BlockHotSpring() {
        super(new FluidHotSpring(), Material.WATER);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "hot_spring"));
    }
    
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof EntityPlayer && entity.ticksExisted % 15 == 0) {
            EntityPlayer player = (EntityPlayer)entity;
            IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
            cap.regenHealth(player, cap.getMaxHealth(player) * 0.01f);
            cap.refreshRunePoints(player, Math.min(cap.getMaxRunePoints(), (int)Math.max(1.0f, cap.getMaxRunePoints() * 0.01f)));
            cap.increaseSkill(EnumSkills.BATH, player, 1);
        }
        else if (entity instanceof EntityLiving && entity.ticksExisted % 15 == 0) {
            EntityLiving living = (EntityLiving)entity;
            living.heal(living.getMaxHealth() * 0.01f);
        }
    }
}
