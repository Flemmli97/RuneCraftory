package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.api.datapack.ItemStatProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;

public class ItemStatGen extends ItemStatProvider {

    public ItemStatGen(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addStats() {
        this.addStat(Items.STICK, 1, 1, 0);
    }
}
