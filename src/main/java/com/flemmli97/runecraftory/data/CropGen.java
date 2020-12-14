package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.api.datapack.provider.CropProvider;
import com.flemmli97.runecraftory.api.enums.EnumSeason;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;

public class CropGen extends CropProvider {

    public CropGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat("wheat_seed", Items.WHEAT_SEEDS, new CropProperties.MutableCropProps(7,4,true).addGoodSeason(EnumSeason.FALL));
        this.addStat("carrot", Items.CARROT, new CropProperties.MutableCropProps(10,3,false).addGoodSeason(EnumSeason.FALL));

    }
}
