package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.api.datapack.provider.CropProvider;
import com.flemmli97.runecraftory.api.enums.EnumSeason;
import com.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;

public class CropGen extends CropProvider {

    public CropGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat(Items.WHEAT_SEEDS, new CropProperties.MutableCropProps(7, 4, true).addGoodSeason(EnumSeason.FALL));
        this.addStat(Items.CARROT, new CropProperties.MutableCropProps(10, 3, false).addGoodSeason(EnumSeason.FALL));

        this.addStat(ModItems.toyherbSeeds.get(), new CropProperties.MutableCropProps(4, 2, false).addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.moondropSeeds.get(), new CropProperties.MutableCropProps(7, 4, false).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.pinkCatSeeds.get(), new CropProperties.MutableCropProps(6, 3, false).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.charmBlueSeeds.get(), new CropProperties.MutableCropProps(8, 3, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.cherryGrassSeeds.get(), new CropProperties.MutableCropProps(10, 4, false).addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.lampGrassSeeds.get(), new CropProperties.MutableCropProps(16, 3, false).addGoodSeason(EnumSeason.WINTER));
        this.addStat(ModItems.blueCrystalSeeds.get(), new CropProperties.MutableCropProps(55, 3, false).addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));

    }
}
