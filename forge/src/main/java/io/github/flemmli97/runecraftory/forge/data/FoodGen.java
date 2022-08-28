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
        this.addStat("fish", Items.COD, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("salmon", Items.SALMON, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("sweet_berry", Items.SWEET_BERRIES, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("cookie", Items.COOKIE, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(10, 0));
        this.addStat("honey_bottle", Items.HONEY_BOTTLE, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(13, 1));
        this.addStat("apple", Items.APPLE, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(7, 0));

        this.addStat("chorus_fruit", Items.CHORUS_FRUIT, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("kelp", Items.DRIED_KELP, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("melon", Items.MELON_SLICE, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(5, 0));
        this.addStat("potato", Items.POTATO, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(6, 0));
        this.addStat("carrot", Items.CARROT, new FoodProperties.MutableFoodProps(600).setHPRegen(1, 0).setRPRegen(6, 0));

        this.addStat(ModItems.witheredGrass.get(), new FoodProperties.MutableFoodProps(100)
                .addPotion(ModEffects.poison.get(), 60, 0).setRPRegen(0, -35));
        this.addStat(ModItems.weeds.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(-10, 0));
        this.addStat(ModItems.whiteGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.indigoGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.purpleGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.greenGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.blueGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.yellowGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.redGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.orangeGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.blackGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.antidoteGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(10, 0)
                .curePotion(ModEffects.poison.get()).curePotion(MobEffects.POISON).curePotion(MobEffects.WITHER));
        this.addStat(ModItems.medicinalHerb.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0));
        this.addStat(ModItems.bambooSprout.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(10, 0).setRPRegen(5, 0));

        this.addStat(ModItems.roundoff.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(ModEffects.seal.get()));
        this.addStat(ModItems.paraGone.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(MobEffects.MOVEMENT_SLOWDOWN).curePotion(ModEffects.paralysis.get()));
        this.addStat(ModItems.coldMed.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(ModEffects.cold.get()));
        this.addStat(ModItems.antidote.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(MobEffects.POISON).curePotion(ModEffects.poison.get()).curePotion(MobEffects.WITHER));
        this.addStat(ModItems.recoveryPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(100, 0));
        this.addStat(ModItems.healingPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0));
        this.addStat(ModItems.mysteryPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(2000, 0));
        this.addStat(ModItems.magicalPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(0, 50));

        this.addStat(ModItems.mushroom.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(5, 1).setRPRegen(10, 0));
        this.addStat(ModItems.monarchMushroom.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(5, 2).setRPRegen(15, 0));

        this.addStat(ModItems.eggS.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(5, 0));
        this.addStat(ModItems.eggM.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(10, 0).setRPRegen(10, 0));
        this.addStat(ModItems.eggL.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(40, 0).setRPRegen(20, 0));
        this.addStat(ModItems.milkS.get(), new FoodProperties.MutableFoodProps(100).setRPRegen(5, 0));
        this.addStat(ModItems.milkM.get(), new FoodProperties.MutableFoodProps(100).setRPRegen(10, 0).setHPRegen(30, 0));
        this.addStat(ModItems.milkL.get(), new FoodProperties.MutableFoodProps(100).setRPRegen(40, 0).setHPRegen(50, 0));

        this.addStat(ModItems.onigiri.get(), new FoodProperties.MutableFoodProps(4800)
                .setHPRegen(20, 0)
                .setRPRegen(50, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 1));
        this.addStat(ModItems.pickledTurnip.get(), new FoodProperties.MutableFoodProps(4800)
                .setHPRegen(150, 0)
                .setRPRegen(90, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 3)
                .addEffect(ModAttributes.RF_DEFENCE.get(), 1)
                .addEffect(ModAttributes.RF_MAGIC.get(), 2));
        this.addStat(ModItems.flan.get(), new FoodProperties.MutableFoodProps(4800)
                .setHPRegen(100, 0)
                .setRPRegen(90, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1)
                .addEffect(ModAttributes.RF_DEFENCE.get(), 1)
                .addEffect(ModAttributes.RFRESFAT.get(), 15)
                .addEffect(ModAttributes.RFRESCOLD.get(), 15));
        this.addStat(ModItems.hotMilk.get(), new FoodProperties.MutableFoodProps(4800)
                .setHPRegen(40, 0)
                .setRPRegen(80, 1)
                .setRPIncrease(20, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 100, 1));
        this.addStat(ModItems.hotChocolate.get(), new FoodProperties.MutableFoodProps(4800)
                .setHPRegen(80, 0)
                .setRPRegen(200, 3)
                .setRPIncrease(50, 5)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1));
        this.addStat(ModItems.bakedApple.get(), new FoodProperties.MutableFoodProps(4800)
                .setHPRegen(20, 0)
                .setRPRegen(35, 2)
                .addEffect(Attributes.ATTACK_DAMAGE, 1)
                .addEffect(ModAttributes.RF_DEFENCE.get(), 1)
                .addEffect(ModAttributes.RF_MAGIC.get(), 1)
                .addEffect(ModAttributes.RF_MAGIC_DEFENCE.get(), 1));
        this.addStat(ModItems.friedVeggies.get(), new FoodProperties.MutableFoodProps(4800)
                .setHPRegen(300, 30)
                .setRPRegen(250, 25)
                .setRPIncrease(0, 20)
                .addEffectPercentage(Attributes.MAX_HEALTH, 300)
                .addEffect(ModAttributes.RF_DEFENCE.get(), 1)
                .addEffect(ModAttributes.RF_MAGIC.get(), 1));
        this.addStat(ModItems.failedDish.get(), new FoodProperties.MutableFoodProps(7200)
                .setHPRegen(0, -20)
                .setRPIncrease(0, -10)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -10)
                .addEffectPercentage(ModAttributes.RF_DEFENCE.get(), -10)
                .addEffectPercentage(ModAttributes.RF_MAGIC.get(), -10)
                .addEffectPercentage(ModAttributes.RF_MAGIC_DEFENCE.get(), -10));
        this.addStat(ModItems.disastrousDish.get(), new FoodProperties.MutableFoodProps(7200)
                .setHPRegen(0, -60)
                .setRPIncrease(0, -30)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -25)
                .addEffectPercentage(ModAttributes.RF_DEFENCE.get(), -25)
                .addEffectPercentage(ModAttributes.RF_MAGIC.get(), -25)
                .addEffectPercentage(ModAttributes.RF_MAGIC_DEFENCE.get(), -25));
    }
}
