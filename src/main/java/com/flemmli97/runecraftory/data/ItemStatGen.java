package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.api.datapack.provider.ItemStatProvider;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;

public class ItemStatGen extends ItemStatProvider {

    public ItemStatGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat("iron", Tags.Items.INGOTS_IRON, new ItemStat.MutableItemStat(1, 1, 0)
                .addAttribute(ModAttributes.RF_DEFENCE.get(), 1));

        this.addStat("emerald", Tags.Items.GEMS_EMERALD, new ItemStat.MutableItemStat(1, 1, 0)
        );
    }
}
