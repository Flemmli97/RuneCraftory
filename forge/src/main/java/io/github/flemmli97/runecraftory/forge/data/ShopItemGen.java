package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.provider.ShopItemProvider;
import io.github.flemmli97.runecraftory.common.entities.npc.EnumShop;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;

public class ShopItemGen extends ShopItemProvider {

    public ShopItemGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {

        this.addItem(EnumShop.GENERAL, ModItems.turnipSeeds.get(), true);
        this.addItem(EnumShop.GENERAL, ModItems.turnipPinkSeeds.get(), true);
        this.addItem(EnumShop.GENERAL, ModItems.cabbageSeeds.get(), true);
        this.addItem(EnumShop.GENERAL, ModItems.pinkMelonSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.hotHotSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.goldTurnipSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.goldPotatoSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.goldPumpkinSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.goldCabbageSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.bokChoySeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.leekSeeds.get(), true);
        // this.addItem(EnumShop.GENERAL, ModItems.radishSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.greenPepperSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.spinachSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.yamSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.eggplantSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.pineappleSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.pumpkinSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.onionSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.cornSeeds.get(), true);
        this.addItem(EnumShop.GENERAL, ModItems.tomatoSeeds.get(), true);
        this.addItem(EnumShop.GENERAL, ModItems.cucumberSeeds.get(), true);
        //this.addItem(EnumShop.GENERAL, ModItems.fodderSeeds.get(), true);

        this.addItem(EnumShop.GENERAL, ModItems.turnip.get());
        this.addItem(EnumShop.GENERAL, ModItems.turnipPink.get());
        this.addItem(EnumShop.GENERAL, ModItems.cabbage.get());
        this.addItem(EnumShop.GENERAL, ModItems.pinkMelon.get());
        this.addItem(EnumShop.GENERAL, ModItems.pineapple.get());
        this.addItem(EnumShop.GENERAL, ModItems.strawberry.get());
        this.addItem(EnumShop.GENERAL, ModItems.goldenTurnip.get());
        this.addItem(EnumShop.GENERAL, ModItems.goldenPotato.get());
        this.addItem(EnumShop.GENERAL, ModItems.goldenPumpkin.get());
        this.addItem(EnumShop.GENERAL, ModItems.goldenCabbage.get());
        this.addItem(EnumShop.GENERAL, ModItems.hotHotFruit.get());
        this.addItem(EnumShop.GENERAL, ModItems.bokChoy.get());
        this.addItem(EnumShop.GENERAL, ModItems.leek.get());
        this.addItem(EnumShop.GENERAL, ModItems.radish.get());
        this.addItem(EnumShop.GENERAL, ModItems.spinach.get());
        this.addItem(EnumShop.GENERAL, ModItems.greenPepper.get());
        this.addItem(EnumShop.GENERAL, ModItems.yam.get());
        this.addItem(EnumShop.GENERAL, ModItems.eggplant.get());
        this.addItem(EnumShop.GENERAL, ModItems.tomato.get());
        this.addItem(EnumShop.GENERAL, ModItems.corn.get());
        this.addItem(EnumShop.GENERAL, ModItems.cucumber.get());
        this.addItem(EnumShop.GENERAL, ModItems.pumpkin.get());
        this.addItem(EnumShop.GENERAL, ModItems.onion.get());

        this.addItem(EnumShop.FLOWER, ModItems.toyherbSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.moondropSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.pinkCatSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.charmBlueSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.lampGrassSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.cherryGrassSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.pomPomGrassSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.autumnGrassSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.noelGrassSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.fireflowerSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.fourLeafCloverSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.ironleafSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.whiteCrystalSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.redCrystalSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.greenCrystalSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.blueCrystalSeeds.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.emeryFlowerSeeds.get(), true);

        this.addItem(EnumShop.FLOWER, ModItems.toyherb.get());
        this.addItem(EnumShop.FLOWER, ModItems.moondropFlower.get());
        this.addItem(EnumShop.FLOWER, ModItems.pinkCat.get());
        this.addItem(EnumShop.FLOWER, ModItems.charmBlue.get());
        this.addItem(EnumShop.FLOWER, ModItems.lampGrass.get());
        this.addItem(EnumShop.FLOWER, ModItems.cherryGrass.get());
        this.addItem(EnumShop.FLOWER, ModItems.pomPomGrass.get());
        this.addItem(EnumShop.FLOWER, ModItems.autumnGrass.get());
        this.addItem(EnumShop.FLOWER, ModItems.noelGrass.get());
        this.addItem(EnumShop.FLOWER, ModItems.fireflower.get());
        this.addItem(EnumShop.FLOWER, ModItems.fourLeafClover.get());
        this.addItem(EnumShop.FLOWER, ModItems.ironleaf.get());
        this.addItem(EnumShop.FLOWER, ModItems.whiteCrystal.get());
        this.addItem(EnumShop.FLOWER, ModItems.redCrystal.get());
        this.addItem(EnumShop.FLOWER, ModItems.greenCrystal.get());
        this.addItem(EnumShop.FLOWER, ModItems.blueCrystal.get());
        this.addItem(EnumShop.FLOWER, ModItems.emeryFlower.get());

        this.addItem(EnumShop.FLOWER, ModItems.formularA.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.formularB.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.formularC.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.minimizer.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.giantizer.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.greenifier.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.greenifierPlus.get(), true);
        this.addItem(EnumShop.FLOWER, ModItems.wettablePowder.get(), true);

        this.addItem(EnumShop.WEAPON, ModItems.hoeScrap.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.hoeIron.get());
        this.addItem(EnumShop.WEAPON, ModItems.hoeSilver.get());

        this.addItem(EnumShop.WEAPON, ModItems.wateringCanScrap.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.wateringCanIron.get());
        this.addItem(EnumShop.WEAPON, ModItems.wateringCanSilver.get());

        this.addItem(EnumShop.WEAPON, ModItems.sickleScrap.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.sickleIron.get());
        this.addItem(EnumShop.WEAPON, ModItems.sickleSilver.get());

        this.addItem(EnumShop.WEAPON, ModItems.hammerScrap.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.hammerIron.get());
        this.addItem(EnumShop.WEAPON, ModItems.hammerSilver.get());

        this.addItem(EnumShop.WEAPON, ModItems.axeScrap.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.axeIron.get());
        this.addItem(EnumShop.WEAPON, ModItems.axeSilver.get());

        this.addItem(EnumShop.WEAPON, ModItems.fishingRodScrap.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.fishingRodIron.get());
        this.addItem(EnumShop.WEAPON, ModItems.fishingRodSilver.get());

        this.addItem(EnumShop.WEAPON, ModItems.mobStaff.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.brush.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.glass.get(), true);

        this.addItem(EnumShop.WEAPON, ModItems.broadSword.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.cutlass.get());

        this.addItem(EnumShop.WEAPON, ModItems.claymore.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.greatSword.get());

        this.addItem(EnumShop.WEAPON, ModItems.spear.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.needleSpear.get());

        this.addItem(EnumShop.WEAPON, ModItems.battleAxe.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.poleAxe.get());

        this.addItem(EnumShop.WEAPON, ModItems.battleHammer.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.warHammer.get());

        this.addItem(EnumShop.WEAPON, ModItems.shortDagger.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.ironEdge.get());

        this.addItem(EnumShop.WEAPON, ModItems.leatherGlove.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.gloves.get());

        this.addItem(EnumShop.WEAPON, ModItems.rod.get(), true);
        this.addItem(EnumShop.WEAPON, ModItems.aquamarineRod.get());

        this.addItem(EnumShop.CLINIC, ModItems.recoveryPotion.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.healingPotion.get());
        this.addItem(EnumShop.CLINIC, ModItems.mysteryPotion.get());
        this.addItem(EnumShop.CLINIC, ModItems.magicalPotion.get());
        this.addItem(EnumShop.CLINIC, ModItems.roundoff.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.paraGone.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.coldMed.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.antidote.get(), true);

        this.addItem(EnumShop.CLINIC, ModItems.elliLeaves.get());
        this.addItem(EnumShop.CLINIC, ModItems.whiteGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.indigoGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.purpleGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.greenGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.blueGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.yellowGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.redGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.orangeGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.blackGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.antidoteGrass.get(), true);
        this.addItem(EnumShop.CLINIC, ModItems.medicinalHerb.get(), true);

        this.addItem(EnumShop.MAGIC, ModItems.fireBallSmall.get(), true);
        this.addItem(EnumShop.MAGIC, ModItems.fireBallBig.get());
        this.addItem(EnumShop.MAGIC, ModItems.explosion.get());
        this.addItem(EnumShop.MAGIC, ModItems.waterLaser.get());
        this.addItem(EnumShop.MAGIC, ModItems.parallelLaser.get());
        this.addItem(EnumShop.MAGIC, ModItems.deltaLaser.get());
        this.addItem(EnumShop.MAGIC, ModItems.screwRock.get());
        this.addItem(EnumShop.MAGIC, ModItems.earthSpike.get());
        this.addItem(EnumShop.MAGIC, ModItems.avengerRock.get());
        this.addItem(EnumShop.MAGIC, ModItems.sonicWind.get());
        this.addItem(EnumShop.MAGIC, ModItems.doubleSonic.get());
        this.addItem(EnumShop.MAGIC, ModItems.penetrateSonic.get());
        this.addItem(EnumShop.MAGIC, ModItems.lightBarrier.get());
        this.addItem(EnumShop.MAGIC, ModItems.shine.get());
        this.addItem(EnumShop.MAGIC, ModItems.prism.get());
        this.addItem(EnumShop.MAGIC, ModItems.darkSnake.get());
        this.addItem(EnumShop.MAGIC, ModItems.darkBall.get());
        this.addItem(EnumShop.MAGIC, ModItems.darkness.get());
        this.addItem(EnumShop.MAGIC, ModItems.cure.get());
        this.addItem(EnumShop.MAGIC, ModItems.cureAll.get());
        this.addItem(EnumShop.MAGIC, ModItems.cureMaster.get());
        this.addItem(EnumShop.MAGIC, ModItems.mediPoison.get());
        this.addItem(EnumShop.MAGIC, ModItems.mediPara.get());
        this.addItem(EnumShop.MAGIC, ModItems.mediSeal.get());
    }
}
