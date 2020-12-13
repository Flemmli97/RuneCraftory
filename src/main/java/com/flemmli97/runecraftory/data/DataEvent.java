package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = RuneCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator data = event.getGenerator();
        if (event.includeClient()) {
            data.addProvider(new ItemModels(data, event.getExistingFileHelper()));
            data.addProvider(new Lang(data));
        }
        if (event.includeServer()) {
            BlockTagGen blocks = new BlockTagGen(data, event.getExistingFileHelper());
            data.addProvider(blocks);
            data.addProvider(new ItemTagGen(data, blocks, event.getExistingFileHelper()));
        }
    }

}
