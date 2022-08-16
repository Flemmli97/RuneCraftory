package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.CropProvider;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

public class CropGen extends CropProvider {

    public CropGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat(Items.WHEAT_SEEDS, new CropProperties.MutableCropProps(5, 4, true).addGoodSeason(EnumSeason.FALL));
        this.addStat(Items.CARROT, new CropProperties.MutableCropProps(6, 3, false).addGoodSeason(EnumSeason.FALL));
        this.addStat(Items.POTATO, new CropProperties.MutableCropProps(6, 3, false).addGoodSeason(EnumSeason.SUMMER));
        this.addStat(Items.BEETROOT_SEEDS, new CropProperties.MutableCropProps(5, 3, false).addGoodSeason(EnumSeason.SPRING));

        this.addStat(ModItems.toyherbSeeds.get(), new CropProperties.MutableCropProps(4, 2, false).addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.moondropSeeds.get(), new CropProperties.MutableCropProps(7, 4, false).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.pinkCatSeeds.get(), new CropProperties.MutableCropProps(6, 3, false).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.charmBlueSeeds.get(), new CropProperties.MutableCropProps(8, 3, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.cherryGrassSeeds.get(), new CropProperties.MutableCropProps(10, 4, false).addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.lampGrassSeeds.get(), new CropProperties.MutableCropProps(16, 3, false).addGoodSeason(EnumSeason.WINTER));
        this.addStat(ModItems.blueCrystalSeeds.get(), new CropProperties.MutableCropProps(55, 3, false).addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.ironleafSeeds.get(), new CropProperties.MutableCropProps(21, 2, false).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.fourLeafCloverSeeds.get(), new CropProperties.MutableCropProps(28, 3, false).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.fireflowerSeeds.get(), new CropProperties.MutableCropProps(42, 4, false).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.greenCrystalSeeds.get(), new CropProperties.MutableCropProps(70, 3, false).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.noelGrassSeeds.get(), new CropProperties.MutableCropProps(33, 4, false).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.autumnGrassSeeds.get(), new CropProperties.MutableCropProps(29, 3, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.pomPomGrassSeeds.get(), new CropProperties.MutableCropProps(14, 3, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.redCrystalSeeds.get(), new CropProperties.MutableCropProps(80, 3, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.whiteCrystalSeeds.get(), new CropProperties.MutableCropProps(90, 3, false).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.FALL));
        this.addStat(ModItems.emeryFlowerSeeds.get(), new CropProperties.MutableCropProps(120, 2, false));

        this.addStat(ModItems.turnipSeeds.get(), new CropProperties.MutableCropProps(4, 3, false).addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL));
        this.addStat(ModItems.turnipPinkSeeds.get(), new CropProperties.MutableCropProps(8, 3, false).addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.cabbageSeeds.get(), new CropProperties.MutableCropProps(7, 3, false).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.pinkMelonSeeds.get(), new CropProperties.MutableCropProps(7, 2, true).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.hotHotSeeds.get(), new CropProperties.MutableCropProps(31, 5, false).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.goldTurnipSeeds.get(), new CropProperties.MutableCropProps(90, 3, false).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.goldPotatoSeeds.get(), new CropProperties.MutableCropProps(50, 3, false).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.goldPumpkinSeeds.get(), new CropProperties.MutableCropProps(75, 3, true).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.goldCabbageSeeds.get(), new CropProperties.MutableCropProps(75, 3, false).addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.bokChoySeeds.get(), new CropProperties.MutableCropProps(5, 4, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.leekSeeds.get(), new CropProperties.MutableCropProps(10, 2, false).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.radishSeeds.get(), new CropProperties.MutableCropProps(4, 1, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.greenPepperSeeds.get(), new CropProperties.MutableCropProps(8, 5, true).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.spinachSeeds.get(), new CropProperties.MutableCropProps(2, 2, false).addGoodSeason(EnumSeason.FALL));
        this.addStat(ModItems.yamSeeds.get(), new CropProperties.MutableCropProps(9, 5, false).addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.eggplantSeeds.get(), new CropProperties.MutableCropProps(7, 4, true).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.pineappleSeeds.get(), new CropProperties.MutableCropProps(30, 2, true).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.onionSeeds.get(), new CropProperties.MutableCropProps(20, 6, false).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER));
        this.addStat(ModItems.cornSeeds.get(), new CropProperties.MutableCropProps(5, 1, false).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.tomatoSeeds.get(), new CropProperties.MutableCropProps(8, 2, true).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.strawberrySeeds.get(), new CropProperties.MutableCropProps(15, 5, true).addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL));
        this.addStat(ModItems.cucumberSeeds.get(), new CropProperties.MutableCropProps(5, 5, true).addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER));
        this.addStat(ModItems.fodderSeeds.get(), new CropProperties.MutableCropProps(5, 1, true));

        this.addStat(ModItems.shieldSeeds.get(), new CropProperties.MutableCropProps(15, 1, false));
        this.addStat(ModItems.swordSeeds.get(), new CropProperties.MutableCropProps(15, 1, false));
        this.addStat(ModItems.dungeonSeeds.get(), new CropProperties.MutableCropProps(25, 1, false));
    }
}
