package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.ShopItemProvider;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.data.DataGenerator;

public class ShopItemGen extends ShopItemProvider {

    public ShopItemGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {

        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnipSeeds.get(), true);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnipPinkSeeds.get(), true);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cabbageSeeds.get(), true);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pinkMelonSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.hotHotSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldTurnipSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldPotatoSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldPumpkinSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldCabbageSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.bokChoySeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.leekSeeds.get(), true);
        // this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.radishSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.greenPepperSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.spinachSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.yamSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.eggplantSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pineappleSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pumpkinSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.onionSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cornSeeds.get(), true);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.tomatoSeeds.get(), true);
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cucumberSeeds.get(), true);
        //this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.fodderSeeds.get(), true);

        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnip.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.turnipPink.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cabbage.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pinkMelon.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pineapple.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.strawberry.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenTurnip.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenPotato.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenPumpkin.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.goldenCabbage.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.hotHotFruit.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.bokChoy.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.leek.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.radish.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.spinach.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.greenPepper.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.yam.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.eggplant.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.tomato.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.corn.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.cucumber.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.pumpkin.get());
        this.addItem(ModNPCJobs.GENERAL.getSecond(), ModItems.onion.get());

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.toyherbSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.moondropSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pinkCatSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.charmBlueSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.lampGrassSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.cherryGrassSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pomPomGrassSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.autumnGrassSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.noelGrassSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fireflowerSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fourLeafCloverSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.ironleafSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.whiteCrystalSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.redCrystalSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenCrystalSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.blueCrystalSeeds.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.emeryFlowerSeeds.get(), true);

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.toyherb.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.moondropFlower.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pinkCat.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.charmBlue.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.lampGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.cherryGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.pomPomGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.autumnGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.noelGrass.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fireflower.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.fourLeafClover.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.ironleaf.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.whiteCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.redCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.blueCrystal.get());
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.emeryFlower.get());

        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularA.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularB.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.formularC.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.minimizer.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.giantizer.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenifier.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.greenifierPlus.get(), true);
        this.addItem(ModNPCJobs.FLOWER.getSecond(), ModItems.wettablePowder.get(), true);

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hoeScrap.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hoeIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hoeSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.wateringCanScrap.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.wateringCanIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.wateringCanSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.sickleScrap.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.sickleIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.sickleSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hammerScrap.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hammerIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.hammerSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.axeScrap.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.axeIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.axeSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.fishingRodScrap.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.fishingRodIron.get());
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.fishingRodSilver.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.mobStaff.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.brush.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.glass.get(), true);

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.broadSword.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.cutlass.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.claymore.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.greatSword.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.spear.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.needleSpear.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.battleAxe.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.poleAxe.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.battleHammer.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.warHammer.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.shortDagger.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.ironEdge.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.leatherGlove.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.gloves.get());

        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.rod.get(), true);
        this.addItem(ModNPCJobs.SMITH.getSecond(), ModItems.aquamarineRod.get());

        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.recoveryPotion.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.healingPotion.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.mysteryPotion.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.magicalPotion.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.roundoff.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.paraGone.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.coldMed.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.antidote.get(), true);

        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.elliLeaves.get());
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.whiteGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.indigoGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.purpleGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.greenGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.blueGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.yellowGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.redGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.orangeGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.blackGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.antidoteGrass.get(), true);
        this.addItem(ModNPCJobs.DOCTOR.getSecond(), ModItems.medicinalHerb.get(), true);

        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.fireBallSmall.get(), true);
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.fireBallBig.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.explosion.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.waterLaser.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.parallelLaser.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.deltaLaser.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.screwRock.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.earthSpike.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.avengerRock.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.sonicWind.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.doubleSonic.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.penetrateSonic.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.lightBarrier.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.shine.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.prism.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.darkSnake.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.darkBall.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.darkness.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.cure.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.cureAll.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.cureMaster.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.mediPoison.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.mediPara.get());
        this.addItem(ModNPCJobs.MAGIC.getSecond(), ModItems.mediSeal.get());
    }
}
