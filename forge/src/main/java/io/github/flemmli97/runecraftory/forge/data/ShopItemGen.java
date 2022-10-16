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

        this.addItem(EnumShop.WEAPON, ModItems.inspector.get(), true);
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
    }
}
