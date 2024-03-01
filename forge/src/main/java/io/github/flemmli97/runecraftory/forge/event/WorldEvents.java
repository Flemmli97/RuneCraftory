package io.github.flemmli97.runecraftory.forge.event;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientFarmlandHandler;
import io.github.flemmli97.runecraftory.common.events.WorldCalls;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.forge.capability.ArmorCap;
import io.github.flemmli97.runecraftory.forge.capability.EntityCap;
import io.github.flemmli97.runecraftory.forge.capability.PlayerCap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldEvents {

    public static final ResourceLocation PLAYER_CAP = new ResourceLocation(RuneCraftory.MODID, "player_cap");
    public static final ResourceLocation STAFF_CAP = new ResourceLocation(RuneCraftory.MODID, "staff_cap");
    public static final ResourceLocation ARMOR_CAP = new ResourceLocation(RuneCraftory.MODID, "armor_cap");
    public static final ResourceLocation ENTITY_CAP = new ResourceLocation(RuneCraftory.MODID, "entity_cap");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(PLAYER_CAP, new PlayerCap());
        }
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(ENTITY_CAP, new EntityCap());
        }
    }

    @SubscribeEvent
    public void attachCapabilityStack(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof ArmorItem) {
            event.addCapability(ARMOR_CAP, new ArmorCap());
        }
    }

    @SubscribeEvent
    public void command(RegisterCommandsEvent event) {
        WorldCalls.command(event.getDispatcher());
    }

    @SubscribeEvent
    public void biomeLoad(BiomeLoadingEvent event) {
        event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.GATE.get(), 100, 1, 1));
        WorldCalls.addFeatures(event.getGeneration()::addFeature, event.getCategory());
    }

    @SubscribeEvent
    public void daily(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.END && e.world.dimension().equals(Level.OVERWORLD)) {
            WorldCalls.daily(e.world);
        }
    }

    @SubscribeEvent
    public void disableVanillaCrop(BlockEvent.CropGrowEvent.Pre event) {
        if (WorldCalls.disableVanillaCrop(event.getWorld(), event.getState(), event.getPos()))
            event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel level)
            FarmlandHandler.get(level.getServer()).onChunkLoad(level, event.getChunk().getPos());
    }

    @SubscribeEvent
    public void onChunkUnLoad(ChunkEvent.Unload event) {
        if (event.getWorld() instanceof ServerLevel level)
            FarmlandHandler.get(level.getServer()).onChunkUnLoad(level, event.getChunk().getPos());
        else if (event.getWorld().isClientSide())
            ClientFarmlandHandler.INSTANCE.onChunkUnLoad(event.getChunk().getPos());
    }
}
