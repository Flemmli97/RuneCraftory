package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.capability.PlayerCapImpl;
import com.flemmli97.runecraftory.common.commands.RunecraftoryCommand;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldEvents {

    public static final ResourceLocation PlayerCap = new ResourceLocation(RuneCraftory.MODID, "player_cap");
    public static final ResourceLocation StaffCap = new ResourceLocation(RuneCraftory.MODID, "staff_cap");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(PlayerCap, new PlayerCapImpl());
        }
    }

    @SubscribeEvent
    public void command(RegisterCommandsEvent event) {
        RunecraftoryCommand.reg(event.getDispatcher());
    }

    @SubscribeEvent
    public void daily(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.END && !e.world.isRemote && e.world.getRegistryKey().equals(World.OVERWORLD)) {
            WorldHandler.get((ServerWorld) e.world).update((ServerWorld) e.world);
        }
    }

    @SubscribeEvent
    public void disableVanillaCrop(BlockEvent.CropGrowEvent.Pre event) {
        if (event.getState().getBlock() instanceof IGrowable && event.getState().getBlock() instanceof IPlantable) {
            IPlantable growable = (IPlantable) event.getState().getBlock();
            if (growable.getPlantType(event.getWorld(), event.getPos()) == PlantType.CROP) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
