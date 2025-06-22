package io.github.flemmli97.runecraftory.neoforge.event;

import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.commands.RunecraftoryCommand;
import io.github.flemmli97.runecraftory.common.events.WorldCalls;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class WorldEvents {

    @SubscribeEvent
    public void command(RegisterCommandsEvent event) {
        RunecraftoryCommand.reg(event.getDispatcher(), event.getBuildContext());
    }

    @SubscribeEvent
    public void daily(LevelTickEvent.Post event) {
        if (event.getLevel().dimension().equals(Level.OVERWORLD)) {
            WorldCalls.daily(event.getLevel());
        }
    }

    @SubscribeEvent
    public void disableVanillaCrop(CropGrowEvent.Pre event) {
        if (WorldCalls.disableVanillaCrop(event.getLevel(), event.getState(), event.getPos()))
            event.setResult(CropGrowEvent.Pre.Result.DO_NOT_GROW);
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel level)
            FarmlandHandler.get(level.getServer()).onChunkLoad(level, event.getChunk().getPos());
    }

    @SubscribeEvent
    public void onChunkUnLoad(ChunkEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel level)
            FarmlandHandler.get(level.getServer()).onChunkUnLoad(level, event.getChunk().getPos());
        else if (event.getLevel().isClientSide())
            ClientFarmlandHandler.INSTANCE.onChunkUnLoad(event.getChunk().getPos());
    }
}
