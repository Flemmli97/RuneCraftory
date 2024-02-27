package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class SoundGen extends SoundDefinitionsProvider {

    public SoundGen(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, RuneCraftory.MODID, helper);
    }

    @Override
    public void registerSounds() {
        for (RegistryEntrySupplier<SoundEvent> sup : ModSounds.SOUND_EVENTS.getEntries()) {
            int num = ModSounds.VARIATIONS.getInt(sup.getID());
            if (num > 0)
                this.add(sup.get(), num);
            else
                this.add(sup.get());
        }
    }

    private void add(SoundEvent event) {
        this.add(event, definition().subtitle(event.getLocation().toString()).with(SoundDefinition.Sound.sound(new ResourceLocation(event.getLocation().getNamespace(), event.getLocation().getPath().replace(".", "/")), SoundDefinition.SoundType.SOUND)));
    }

    private void add(SoundEvent event, int num) {
        SoundDefinition def = definition().subtitle(event.getLocation().toString());
        for (int i = 0; i < num; i++)
            def.with(SoundDefinition.Sound.sound(new ResourceLocation(event.getLocation().getNamespace(), event.getLocation().getPath().replace(".", "/") + "_" + (i + 1)), SoundDefinition.SoundType.SOUND));
        this.add(event, def);
    }

}
