package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldEvents {

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
