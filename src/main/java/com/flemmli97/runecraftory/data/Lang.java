package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class Lang extends LanguageProvider {

    public Lang(DataGenerator gen) {
        super(gen, RuneCraftory.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (SpawnEgg egg : SpawnEgg.getEggs())
            this.add(egg, "%s" + " Spawn Egg");
    }
}
