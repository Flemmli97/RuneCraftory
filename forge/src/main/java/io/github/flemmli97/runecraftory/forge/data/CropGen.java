package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.CropProvider;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Items;

public class CropGen extends CropProvider {

    public CropGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat(Items.WHEAT_SEEDS, new CropProperties.Builder(5, 4, true).addGoodSeason(EnumSeason.FALL));
        this.addStat(Items.CARROT, new CropProperties.Builder(6, 3, false).addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.carrotGiant.get()));
        this.addStat(Items.POTATO, new CropProperties.Builder(6, 3, false).addGoodSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.potatoGiant.get()));
        this.addStat(Items.BEETROOT_SEEDS, new CropProperties.Builder(5, 3, false).addGoodSeason(EnumSeason.SPRING));

        this.addStat(ModItems.toyherbSeeds.get(), new CropProperties.Builder(4, 2, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.toyherbGiant.get()));
        this.addStat(ModItems.moondropSeeds.get(), new CropProperties.Builder(7, 4, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.moondropFlowerGiant.get()));
        this.addStat(ModItems.pinkCatSeeds.get(), new CropProperties.Builder(6, 3, false)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.pinkCatGiant.get()));
        this.addStat(ModItems.charmBlueSeeds.get(), new CropProperties.Builder(8, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.charmBlueGiant.get()));
        this.addStat(ModItems.cherryGrassSeeds.get(), new CropProperties.Builder(10, 4, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.cherryGrassGiant.get()));
        this.addStat(ModItems.lampGrassSeeds.get(), new CropProperties.Builder(16, 3, false)
                .addGoodSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.lampGrassGiant.get()));
        this.addStat(ModItems.blueCrystalSeeds.get(), new CropProperties.Builder(55, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.blueCrystalGiant.get()));
        this.addStat(ModItems.ironleafSeeds.get(), new CropProperties.Builder(21, 2, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.ironleafGiant.get()));
        this.addStat(ModItems.fourLeafCloverSeeds.get(), new CropProperties.Builder(28, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.fourLeafCloverGiant.get()));
        this.addStat(ModItems.fireflowerSeeds.get(), new CropProperties.Builder(42, 4, false)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.fireflowerGiant.get()));
        this.addStat(ModItems.greenCrystalSeeds.get(), new CropProperties.Builder(70, 3, false)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.greenCrystalGiant.get()));
        this.addStat(ModItems.noelGrassSeeds.get(), new CropProperties.Builder(33, 4, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.noelGrassGiant.get()));
        this.addStat(ModItems.autumnGrassSeeds.get(), new CropProperties.Builder(29, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.autumnGrassGiant.get()));
        this.addStat(ModItems.pomPomGrassSeeds.get(), new CropProperties.Builder(14, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.pomPomGrassGiant.get()));
        this.addStat(ModItems.redCrystalSeeds.get(), new CropProperties.Builder(80, 3, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.redCrystalGiant.get()));
        this.addStat(ModItems.whiteCrystalSeeds.get(), new CropProperties.Builder(90, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.whiteCrystalGiant.get()));
        this.addStat(ModItems.emeryFlowerSeeds.get(), new CropProperties.Builder(120, 2, false)
                .withGiantVersion(ModBlocks.emeryFlowerGiant.get()));

        this.addStat(ModItems.turnipSeeds.get(), new CropProperties.Builder(4, 3, false)
                .addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.turnipGiant.get()));
        this.addStat(ModItems.turnipPinkSeeds.get(), new CropProperties.Builder(8, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.turnipPinkGiant.get()));
        this.addStat(ModItems.cabbageSeeds.get(), new CropProperties.Builder(7, 3, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.cabbageGiant.get()));
        this.addStat(ModItems.pinkMelonSeeds.get(), new CropProperties.Builder(7, 2, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.pinkMelonGiant.get()));
        this.addStat(ModItems.hotHotSeeds.get(), new CropProperties.Builder(31, 5, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.hotHotFruitGiant.get()));
        this.addStat(ModItems.goldTurnipSeeds.get(), new CropProperties.Builder(90, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.goldenTurnipGiant.get()));
        this.addStat(ModItems.goldPotatoSeeds.get(), new CropProperties.Builder(50, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.goldenPotatoGiant.get()));
        this.addStat(ModItems.goldPumpkinSeeds.get(), new CropProperties.Builder(75, 3, true)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.pumpkinGiant.get()));
        this.addStat(ModItems.goldCabbageSeeds.get(), new CropProperties.Builder(75, 3, false)
                .addGoodSeason(EnumSeason.WINTER).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.goldenCabbageGiant.get()));
        this.addStat(ModItems.bokChoySeeds.get(), new CropProperties.Builder(5, 4, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.bokChoyGiant.get()));
        this.addStat(ModItems.leekSeeds.get(), new CropProperties.Builder(10, 2, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.leekGiant.get()));
        this.addStat(ModItems.radishSeeds.get(), new CropProperties.Builder(4, 1, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.radishGiant.get()));
        this.addStat(ModItems.greenPepperSeeds.get(), new CropProperties.Builder(8, 5, true)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.greenPepperGiant.get()));
        this.addStat(ModItems.spinachSeeds.get(), new CropProperties.Builder(2, 2, false)
                .addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.spinachGiant.get()));
        this.addStat(ModItems.yamSeeds.get(), new CropProperties.Builder(9, 5, false)
                .addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.yamGiant.get()));
        this.addStat(ModItems.eggplantSeeds.get(), new CropProperties.Builder(7, 4, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.eggplantGiant.get()));
        this.addStat(ModItems.pineappleSeeds.get(), new CropProperties.Builder(30, 2, true)
                .addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.SPRING).addBadSeason(EnumSeason.FALL).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.pineappleGiant.get()));
        this.addStat(ModItems.onionSeeds.get(), new CropProperties.Builder(20, 6, false)
                .addGoodSeason(EnumSeason.FALL).addBadSeason(EnumSeason.SUMMER)
                .withGiantVersion(ModBlocks.onionGiant.get()));
        this.addStat(ModItems.cornSeeds.get(), new CropProperties.Builder(5, 1, false)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.cornGiant.get()));
        this.addStat(ModItems.tomatoSeeds.get(), new CropProperties.Builder(8, 2, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.tomatoGiant.get()));
        this.addStat(ModItems.strawberrySeeds.get(), new CropProperties.Builder(15, 5, true)
                .addGoodSeason(EnumSeason.SUMMER).addGoodSeason(EnumSeason.FALL)
                .withGiantVersion(ModBlocks.strawberryGiant.get()));
        this.addStat(ModItems.cucumberSeeds.get(), new CropProperties.Builder(5, 5, true)
                .addGoodSeason(EnumSeason.SPRING).addGoodSeason(EnumSeason.SUMMER).addBadSeason(EnumSeason.WINTER)
                .withGiantVersion(ModBlocks.cucumberGiant.get()));
        this.addStat(ModItems.fodderSeeds.get(), new CropProperties.Builder(5, 1, true));

        this.addStat(ModItems.shieldSeeds.get(), new CropProperties.Builder(15, 1, false));
        this.addStat(ModItems.swordSeeds.get(), new CropProperties.Builder(15, 1, false));
        this.addStat(ModItems.dungeonSeeds.get(), new CropProperties.Builder(25, 1, false));

        this.addStat(ModItems.appleSapling.get(), new CropProperties.Builder(20, 1, false));
    }
}
