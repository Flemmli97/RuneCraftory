package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.FoodProvider;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

public class FoodGen extends FoodProvider {

    public FoodGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat("fish", Items.COD, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("salmon", Items.SALMON, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("sweet_berry", Items.SWEET_BERRIES, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("cookie", Items.COOKIE, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(10, 0));
        this.addStat("honey_bottle", Items.HONEY_BOTTLE, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(13, 1));
        this.addStat("apple", Items.APPLE, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(7, 0)
                .addCookingBonus(Attributes.MAX_HEALTH, 30));

        this.addStat("chorus_fruit", Items.CHORUS_FRUIT, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("kelp", Items.DRIED_KELP, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("melon", Items.MELON_SLICE, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("potato", Items.POTATO, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(6, 0));
        this.addStat("carrot", Items.CARROT, new FoodProperties.Builder(600).setHPRegen(1, 0).setRPRegen(6, 0));

        this.addStat(ModItems.witheredGrass.get(), new FoodProperties.Builder(100)
                .addPotion(ModEffects.poison.get(), 60, 0).setRPRegen(0, -35));
        this.addStat(ModItems.weeds.get(), new FoodProperties.Builder(100).setHPRegen(-10, 0));
        this.addStat(ModItems.whiteGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.indigoGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.purpleGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.greenGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.blueGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.yellowGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.redGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.orangeGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.blackGrass.get(), new FoodProperties.Builder(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.antidoteGrass.get(), new FoodProperties.Builder(100).setHPRegen(10, 0)
                .curePotion(ModEffects.poison.get()).curePotion(MobEffects.POISON).curePotion(MobEffects.WITHER));
        this.addStat(ModItems.medicinalHerb.get(), new FoodProperties.Builder(100).setHPRegen(25, 0));
        this.addStat(ModItems.bambooSprout.get(), new FoodProperties.Builder(100).setHPRegen(10, 0).setRPRegen(5, 0));

        this.addStat(ModItems.roundoff.get(), new FoodProperties.Builder(100).setHPRegen(500, 0).curePotion(ModEffects.seal.get()));
        this.addStat(ModItems.paraGone.get(), new FoodProperties.Builder(100).setHPRegen(500, 0).curePotion(MobEffects.MOVEMENT_SLOWDOWN).curePotion(ModEffects.paralysis.get()));
        this.addStat(ModItems.coldMed.get(), new FoodProperties.Builder(100).setHPRegen(500, 0).curePotion(ModEffects.cold.get()));
        this.addStat(ModItems.antidote.get(), new FoodProperties.Builder(100).setHPRegen(500, 0).curePotion(MobEffects.POISON).curePotion(ModEffects.poison.get()).curePotion(MobEffects.WITHER));
        this.addStat(ModItems.recoveryPotion.get(), new FoodProperties.Builder(100).setHPRegen(100, 0));
        this.addStat(ModItems.healingPotion.get(), new FoodProperties.Builder(100).setHPRegen(500, 0));
        this.addStat(ModItems.mysteryPotion.get(), new FoodProperties.Builder(100).setHPRegen(2000, 0));
        this.addStat(ModItems.magicalPotion.get(), new FoodProperties.Builder(100).setHPRegen(0, 50));
        this.addStat(ModItems.invinciroid.get(), new FoodProperties.Builder(100)
                .curePotion(MobEffects.POISON)
                .curePotion(MobEffects.WITHER)
                .curePotion(MobEffects.MOVEMENT_SLOWDOWN)
                .curePotion(MobEffects.BAD_OMEN)
                .curePotion(MobEffects.BLINDNESS)
                .curePotion(MobEffects.LEVITATION)
                .curePotion(MobEffects.CONFUSION)
                .curePotion(ModEffects.sleep.get())
                .curePotion(ModEffects.poison.get())
                .curePotion(ModEffects.paralysis.get())
                .curePotion(ModEffects.seal.get())
                .curePotion(ModEffects.fatigue.get())
                .curePotion(ModEffects.cold.get()));
        this.addStat(ModItems.formuade.get(), new FoodProperties.Builder(100).setRPIncrease(0, 75)
                .addEffectPercentage(Attributes.MAX_HEALTH, -50)
                .addEffect(Attributes.ATTACK_DAMAGE, 70)
                .addEffect(ModAttributes.MAGIC.get(), 70)
                .addEffect(ModAttributes.DEFENCE.get(), 35)
                .addEffect(ModAttributes.MAGIC_DEFENCE.get(), 35)
                .addEffect(ModAttributes.RFRESPOISON.get(), 25)
                .addEffect(ModAttributes.RFRESPARA.get(), 25)
                .addEffect(ModAttributes.RFRESSEAL.get(), 25)
                .addEffect(ModAttributes.RFRESSLEEP.get(), 25)
                .addEffect(ModAttributes.RFRESFAT.get(), 25)
                .addEffect(ModAttributes.RFRESCOLD.get(), 25)
                .addEffect(ModAttributes.RFRESFAINT.get(), 25));
        this.addStat(ModItems.objectX.get(), new FoodProperties.Builder(100).setRPIncrease(0, -50));

        this.addStat(ModItems.mushroom.get(), new FoodProperties.Builder(100).setHPRegen(5, 1).setRPRegen(10, 0));
        this.addStat(ModItems.monarchMushroom.get(), new FoodProperties.Builder(100).setHPRegen(5, 2).setRPRegen(15, 0));

        this.addStat(ModItems.eggS.get(), new FoodProperties.Builder(100).setHPRegen(5, 0));
        this.addStat(ModItems.eggM.get(), new FoodProperties.Builder(100).setHPRegen(10, 0).setRPRegen(10, 0));
        this.addStat(ModItems.eggL.get(), new FoodProperties.Builder(100).setHPRegen(40, 0).setRPRegen(20, 0));
        this.addStat(ModItems.milkS.get(), new FoodProperties.Builder(100).setRPRegen(5, 0));
        this.addStat(ModItems.milkM.get(), new FoodProperties.Builder(100).setRPRegen(10, 0).setHPRegen(30, 0));
        this.addStat(ModItems.milkL.get(), new FoodProperties.Builder(100).setRPRegen(40, 0).setHPRegen(50, 0));

        this.addStat(ModItems.onigiri.get(), new FoodProperties.Builder(4800)
                .setHPRegen(20, 0)
                .setRPRegen(50, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.pickledTurnip.get(), new FoodProperties.Builder(4800)
                .setHPRegen(150, 0)
                .setRPRegen(90, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 3)
                .addEffect(ModAttributes.DEFENCE.get(), 1)
                .addEffect(ModAttributes.MAGIC.get(), 2));
        this.addStat(ModItems.flan.get(), new FoodProperties.Builder(4800)
                .setHPRegen(100, 0)
                .setRPRegen(90, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1)
                .addEffect(ModAttributes.DEFENCE.get(), 1)
                .addEffect(ModAttributes.RFRESFAT.get(), 15)
                .addEffect(ModAttributes.RFRESCOLD.get(), 15));
        this.addStat(ModItems.hotMilk.get(), new FoodProperties.Builder(4800)
                .setHPRegen(40, 0)
                .setRPRegen(80, 1)
                .setRPIncrease(20, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 100, 1));
        this.addStat(ModItems.hotChocolate.get(), new FoodProperties.Builder(4800)
                .setHPRegen(80, 0)
                .setRPRegen(200, 3)
                .setRPIncrease(50, 5)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1));
        this.addStat(ModItems.bakedApple.get(), new FoodProperties.Builder(4800)
                .setHPRegen(20, 0)
                .setRPRegen(35, 2)
                .addEffect(Attributes.ATTACK_DAMAGE, 1)
                .addEffect(ModAttributes.DEFENCE.get(), 1)
                .addEffect(ModAttributes.MAGIC.get(), 1)
                .addEffect(ModAttributes.MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.friedVeggies.get(), new FoodProperties.Builder(4800)
                .setHPRegen(300, 30)
                .setRPRegen(250, 25)
                .setRPIncrease(0, 20)
                .addEffectPercentage(Attributes.MAX_HEALTH, 300)
                .addEffect(ModAttributes.DEFENCE.get(), 1)
                .addEffect(ModAttributes.MAGIC.get(), 1));
        this.addStat(ModItems.failedDish.get(), new FoodProperties.Builder(7200)
                .setHPRegen(0, -20)
                .setRPIncrease(0, -10)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -10)
                .addEffectPercentage(ModAttributes.DEFENCE.get(), -10)
                .addEffectPercentage(ModAttributes.MAGIC.get(), -10)
                .addEffectPercentage(ModAttributes.MAGIC_DEFENCE.get(), -10));
        this.addStat(ModItems.disastrousDish.get(), new FoodProperties.Builder(7200)
                .setHPRegen(0, -60)
                .setRPIncrease(0, -30)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -25)
                .addEffectPercentage(ModAttributes.DEFENCE.get(), -25)
                .addEffectPercentage(ModAttributes.MAGIC.get(), -25)
                .addEffectPercentage(ModAttributes.MAGIC_DEFENCE.get(), -25));

        this.addStat(ModItems.turnip.get(), new FoodProperties.Builder(600)
                .setHPRegen(20, 0)
                .setRPIncrease(5, 0));
        this.addStat(ModItems.turnipGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(45, 0)
                .setRPIncrease(15, 0));
        this.addStat(ModItems.turnipPink.get(), new FoodProperties.Builder(600)
                .setHPRegen(25, 0)
                .setRPIncrease(5, 0));
        this.addStat(ModItems.turnipPinkGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(90, 0)
                .setRPIncrease(18, 0));
        this.addStat(ModItems.cabbage.get(), new FoodProperties.Builder(600)
                .setHPRegen(15, 0));
        this.addStat(ModItems.cabbageGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(35, 0));
        this.addStat(ModItems.pinkMelon.get(), new FoodProperties.Builder(600)
                .setHPRegen(100, 0));
        this.addStat(ModItems.pinkMelonGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(150, 0));
        this.addStat(ModItems.pineapple.get(), new FoodProperties.Builder(600)
                .setHPRegen(200, 0));
        this.addStat(ModItems.pineappleGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(300, 0));
        this.addStat(ModItems.strawberry.get(), new FoodProperties.Builder(600)
                .setHPRegen(60, 0)
                .setRPIncrease(3, 0));
        this.addStat(ModItems.strawberryGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(110, 0)
                .setRPIncrease(7, 0));
        this.addStat(ModItems.goldenTurnip.get(), new FoodProperties.Builder(600)
                .setHPRegen(300, 5)
                .setRPIncrease(3, 3));
        this.addStat(ModItems.goldenTurnipGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(500, 5)
                .setRPIncrease(30, 5));
        this.addStat(ModItems.goldenPotato.get(), new FoodProperties.Builder(600)
                .setHPRegen(230, 5));
        this.addStat(ModItems.goldenPotatoGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(400, 7));
        this.addStat(ModItems.goldenPumpkin.get(), new FoodProperties.Builder(600)
                .setHPRegen(200, 3));
        this.addStat(ModItems.goldenPumpkinGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(300, 7));
        this.addStat(ModItems.goldenCabbage.get(), new FoodProperties.Builder(600)
                .setHPRegen(150, 5)
                .setRPIncrease(5, 0));
        this.addStat(ModItems.goldenCabbageGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(300, 5)
                .setRPIncrease(40, 0));
        this.addStat(ModItems.bokChoy.get(), new FoodProperties.Builder(600)
                .setHPRegen(30, 0));
        this.addStat(ModItems.bokChoyGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(60, 0));
        this.addStat(ModItems.leek.get(), new FoodProperties.Builder(600)
                .setHPRegen(20, 0));
        this.addStat(ModItems.leekGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(80, 0));
        this.addStat(ModItems.radish.get(), new FoodProperties.Builder(600)
                .setHPRegen(25, 0));
        this.addStat(ModItems.radishGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(50, 0));
        this.addStat(ModItems.spinach.get(), new FoodProperties.Builder(600)
                .setHPRegen(44, 0));
        this.addStat(ModItems.spinachGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(90, 0));
        this.addStat(ModItems.greenPepper.get(), new FoodProperties.Builder(600)
                .setHPRegen(60, 0));
        this.addStat(ModItems.greenPepperGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(100, 0));
        this.addStat(ModItems.yam.get(), new FoodProperties.Builder(600)
                .setHPRegen(33, 0));
        this.addStat(ModItems.yamGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(70, 0));
        this.addStat(ModItems.eggplant.get(), new FoodProperties.Builder(600)
                .setHPRegen(55, 0));
        this.addStat(ModItems.eggplantGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(120, 0));
        this.addStat(ModItems.tomato.get(), new FoodProperties.Builder(600)
                .setHPRegen(80, 0));
        this.addStat(ModItems.tomatoGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(160, 0));
        this.addStat(ModItems.corn.get(), new FoodProperties.Builder(600)
                .setHPRegen(45, 0));
        this.addStat(ModItems.cornGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(110, 0));
        this.addStat(ModItems.cucumber.get(), new FoodProperties.Builder(600)
                .setHPRegen(70, 0));
        this.addStat(ModItems.cucumberGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(140, 0));
        this.addStat(ModItems.pumpkin.get(), new FoodProperties.Builder(600)
                .setHPRegen(20, 0));
        this.addStat(ModItems.pumpkinGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(75, 0));
        this.addStat(ModItems.onion.get(), new FoodProperties.Builder(600)
                .setHPRegen(35, 0));
        this.addStat(ModItems.onionGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(60, 0));

        this.addStat(ModItems.potatoGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(80, 0));
        this.addStat(ModItems.carrotGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(80, 0));

        this.addStat(ModItems.toyherb.get(), new FoodProperties.Builder(600)
                .setRPRegen(15, 0));
        this.addStat(ModItems.toyherbGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(60, 0));
        this.addStat(ModItems.moondropFlower.get(), new FoodProperties.Builder(600)
                .setRPRegen(18, 0));
        this.addStat(ModItems.moondropFlowerGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(66, 0));
        this.addStat(ModItems.pinkCat.get(), new FoodProperties.Builder(600)
                .setRPRegen(20, 0));
        this.addStat(ModItems.pinkCatGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(70, 0));
        this.addStat(ModItems.charmBlue.get(), new FoodProperties.Builder(600)
                .setRPRegen(25, 0));
        this.addStat(ModItems.charmBlueGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(90, 0));
        this.addStat(ModItems.lampGrass.get(), new FoodProperties.Builder(600)
                .setRPRegen(33, 0));
        this.addStat(ModItems.lampGrassGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(80, 0));
        this.addStat(ModItems.cherryGrass.get(), new FoodProperties.Builder(600)
                .setRPRegen(20, 0));
        this.addStat(ModItems.cherryGrassGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(55, 0));
        this.addStat(ModItems.pomPomGrass.get(), new FoodProperties.Builder(600)
                .setHPRegen(15, 0)
                .setRPRegen(10, 0));
        this.addStat(ModItems.pomPomGrassGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(80, 0)
                .setRPRegen(25, 0));
        this.addStat(ModItems.autumnGrass.get(), new FoodProperties.Builder(600)
                .setHPRegen(20, 0)
                .setRPRegen(15, 0));
        this.addStat(ModItems.autumnGrassGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(20, 0)
                .setRPRegen(35, 0));
        this.addStat(ModItems.noelGrass.get(), new FoodProperties.Builder(600)
                .setHPRegen(80, 0));
        this.addStat(ModItems.noelGrassGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(250, 0));
        this.addStat(ModItems.fireflower.get(), new FoodProperties.Builder(600)
                .setRPRegen(35, 0));
        this.addStat(ModItems.fireflowerGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(85, 0));
        this.addStat(ModItems.fourLeafClover.get(), new FoodProperties.Builder(600)
                .setRPRegen(25, 0)
                .addPotion(MobEffects.LUCK, 600, 0));
        this.addStat(ModItems.fourLeafCloverGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(55, 0)
                .addPotion(MobEffects.LUCK, 600, 1));
        this.addStat(ModItems.ironleaf.get(), new FoodProperties.Builder(600)
                .setHPRegen(-50, 0));
        this.addStat(ModItems.ironleafGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(-100, 0));
        this.addStat(ModItems.whiteCrystal.get(), new FoodProperties.Builder(600)
                .setRPRegen(75, 0));
        this.addStat(ModItems.whiteCrystalGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(150, 0));
        this.addStat(ModItems.redCrystal.get(), new FoodProperties.Builder(600)
                .setRPRegen(75, 0));
        this.addStat(ModItems.redCrystalGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(150, 0));
        this.addStat(ModItems.greenCrystal.get(), new FoodProperties.Builder(600)
                .setRPRegen(75, 0));
        this.addStat(ModItems.greenCrystalGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(150, 0));
        this.addStat(ModItems.blueCrystal.get(), new FoodProperties.Builder(600)
                .setRPRegen(75, 0));
        this.addStat(ModItems.blueCrystalGiant.get(), new FoodProperties.Builder(600)
                .setRPRegen(150, 0));
        this.addStat(ModItems.emeryFlower.get(), new FoodProperties.Builder(600)
                .setHPRegen(0, 10)
                .setRPRegen(0, 5));
        this.addStat(ModItems.emeryFlowerGiant.get(), new FoodProperties.Builder(600)
                .setHPRegen(0, 15)
                .setRPRegen(0, 10));

    }
}
