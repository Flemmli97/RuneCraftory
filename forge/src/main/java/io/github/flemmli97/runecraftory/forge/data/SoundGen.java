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
            if (ModSounds.BGM.contains(sup.getID()))
                continue;
            int num = ModSounds.VARIATIONS.getInt(sup.getID());
            if (num > 0)
                this.add(sup.get(), num);
            else
                this.add(sup.get());
        }
        ResourceLocation bgm1 = new ResourceLocation(RuneCraftory.MODID, "bgm/aiwa-konomunede-kuchiteyuku");
        ResourceLocation bgm2 = new ResourceLocation(RuneCraftory.MODID, "bgm/area12");
        ResourceLocation bgm3 = new ResourceLocation(RuneCraftory.MODID, "bgm/catch-them-all");
        ResourceLocation bgm4 = new ResourceLocation(RuneCraftory.MODID, "bgm/cruising-down-8bit-lane");
        ResourceLocation bgm5 = new ResourceLocation(RuneCraftory.MODID, "bgm/yami-no-sekai-no-tatakai");
        ResourceLocation bgm6 = new ResourceLocation(RuneCraftory.MODID, "bgm/yurei");
        ResourceLocation bgm7 = new ResourceLocation(RuneCraftory.MODID, "bgm/golem_battle");
        this.addBgmWith(ModSounds.AMBROSIA_FIGHT.get(), bgm4);
        this.addBgmWith(ModSounds.CHIMERA_FIGHT.get(), bgm2);
        this.addBgmWith(ModSounds.DEAD_TREE_FIGHT.get(), bgm2);
        this.addBgmWith(ModSounds.MARIONETTA_FIGHT.get(), bgm6);
        this.addBgmWith(ModSounds.RACCOON_FIGHT.get(), bgm1);
        this.addBgmWith(ModSounds.SKELEFANG_FIGHT.get(), bgm5);
        this.addBgmWith(ModSounds.RAFFLESIA_FIGHT.get(), bgm3);
        this.addBgmWith(ModSounds.THUNDERBOLT_FIGHT.get(), bgm4);
        this.addBgmWith(ModSounds.GRIMOIRE_FIGHT.get(), bgm2);
        this.addBgmWith(ModSounds.SANO_UNO_FIGHT.get(), bgm4);
        this.addBgmWith(ModSounds.SARCOPHAGUS_FIGHT.get(), bgm7);
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

    private void addBgmWith(SoundEvent event) {
        this.addBgmWith(event, new ResourceLocation(event.getLocation().getNamespace(), event.getLocation().getPath().replace(".", "/")));
    }

    private void addBgmWith(SoundEvent event, ResourceLocation sound) {
        this.add(event, definition().subtitle(event.getLocation().toString()).with(SoundDefinition.Sound.sound(sound, SoundDefinition.SoundType.SOUND)
                .stream()));
    }
}
