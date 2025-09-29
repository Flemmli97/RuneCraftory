package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class SoundGen extends SoundDefinitionsProvider {

    public SoundGen(PackOutput packOutput, ExistingFileHelper helper) {
        super(packOutput, RuneCraftory.MODID, helper);
    }

    @Override
    public void registerSounds() {
        for (RegistryEntrySupplier<SoundEvent, ?> sup : RuneCraftorySounds.SOUND_EVENTS.getEntries()) {
            if (RuneCraftorySounds.BGM.stream().anyMatch(h -> h.sound().equals(sup)))
                continue;
            RuneCraftorySounds.SoundHolder data = RuneCraftorySounds.SOUND_DATA.get(sup.getID());
            if (data != null) {
                this.add(sup.get(), data.location(), data.amount(), data.pitch());
            } else {
                this.add(sup.get());
            }
        }
        for (RuneCraftorySounds.BGMHolder bgm : RuneCraftorySounds.BGM) {
            this.addBgmWith(bgm.sound().get(), bgm.bgm().location());
        }
    }

    private void add(SoundEvent event) {
        this.add(event, event.getLocation(), 1, 1);
    }

    private void add(SoundEvent event, ResourceLocation path, int num, float pitch) {
        SoundDefinition def = definition().subtitle(event.getLocation().toString());
        if (num <= 1) {
            def.with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(path.getNamespace(), path.getPath().replace(".", "/")), SoundDefinition.SoundType.SOUND)
                    .pitch(pitch));
        } else {
            for (int i = 0; i < num; i++) {
                def.with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(path.getNamespace(), path.getPath().replace(".", "/") + (i + 1)), SoundDefinition.SoundType.SOUND)
                        .pitch(pitch));
            }
        }
        this.add(event, def);
    }

    private void addBgmWith(SoundEvent event) {
        this.addBgmWith(event, ResourceLocation.fromNamespaceAndPath(event.getLocation().getNamespace(), event.getLocation().getPath().replace(".", "/")));
    }

    private void addBgmWith(SoundEvent event, ResourceLocation sound) {
        this.add(event, definition().subtitle(event.getLocation().toString())
                .with(SoundDefinition.Sound.sound(sound, SoundDefinition.SoundType.SOUND)
                        .stream()));
    }
}
