package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.FoodProvider;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.effect.MobEffects;

public class FoodGen extends FoodProvider {

    public FoodGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        /*this.addStat("fish", Items.COD, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("salmon", Items.SALMON, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("pufferfish", Items.PUFFERFISH, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("tropical_fish", Items.TROPICAL_FISH, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("spider_eye", Items.SPIDER_EYE, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("rotten_flesh", Items.ROTTEN_FLESH, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("sweet_berry", Items.SWEET_BERRIES, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("cookie", Items.COOKIE, new FoodProperties.MutableFoodProps(600).setHPRegen(3, 0));
        this.addStat("honey_bottle", Items.HONEY_BOTTLE, new FoodProperties.MutableFoodProps(600).setHPRegen(4, 1));

        this.addStat("apple", Items.APPLE, new FoodProperties.MutableFoodProps(600).setHPRegen(15, 0).setRPRegen(5, 0));
        this.addStat("chorus_fruit", Items.CHORUS_FRUIT, new FoodProperties.MutableFoodProps(600).setHPRegen(11, 2));
        this.addStat("kelp", Items.DRIED_KELP, new FoodProperties.MutableFoodProps(600).setHPRegen(7, 0));
        this.addStat("melon", Items.MELON, new FoodProperties.MutableFoodProps(600).setHPRegen(4, 0));
        this.addStat("poisonous_potato", Items.POISONOUS_POTATO, new FoodProperties.MutableFoodProps(500).setHPRegen(3, 0));
        this.addStat("potato", Items.POTATO, new FoodProperties.MutableFoodProps(600).setHPRegen(9, 0));
        this.addStat("raw_beef", Items.BEEF, new FoodProperties.MutableFoodProps(600).setHPRegen(5, 5));
        this.addStat("raw_chicken", Items.CHICKEN, new FoodProperties.MutableFoodProps(600).setHPRegen(5, 5));
        this.addStat("raw_pork", Items.PORKCHOP, new FoodProperties.MutableFoodProps(600).setHPRegen(5, 5));
        this.addStat("raw_mutton", Items.MUTTON, new FoodProperties.MutableFoodProps(600).setHPRegen(5, 5));
        this.addStat("raw_rabbit", Items.RABBIT, new FoodProperties.MutableFoodProps(600).setHPRegen(5, 5));

        this.addStat("bread", Items.BREAD, new FoodProperties.MutableFoodProps(500).setHPRegen(3, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 1)
                .addPotion(MobEffects.MOVEMENT_SPEED, 100, 2));
        this.addStat("golden_apple", Items.GOLDEN_APPLE, new FoodProperties.MutableFoodProps(1500)
                .setHPRegen(50, 10).setRPIncrease(50, 5)
                .setRPRegen(25, 10)
                .addEffect(Attributes.ATTACK_DAMAGE, 10)
                .addPotion(MobEffects.MOVEMENT_SPEED, 100, 2)
                .addPotion(MobEffects.REGENERATION, 200, 3));
        this.addStat("golden_apple", Items.GOLDEN_APPLE, new FoodProperties.MutableFoodProps(1500)
                .setHPRegen(50, 10).setRPIncrease(50, 5)
                .setRPRegen(25, 10)
                .addEffect(Attributes.ATTACK_DAMAGE, 10)
                .addEffect(ModAttributes.RF_DEFENCE.get(), 10));
        this.addStat("enchanted_golden_apple", Items.ENCHANTED_GOLDEN_APPLE, new FoodProperties.MutableFoodProps(3000)
                .setHPRegen(100, 40).setRPIncrease(50, 20)
                .setRPRegen(50, 15)
                .addEffect(Attributes.ATTACK_DAMAGE, 30)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, 10)
                .addEffectPercentage(ModAttributes.RF_DEFENCE.get(), 10));*/

        this.addStat(ModItems.witheredGrass.get(), new FoodProperties.MutableFoodProps(100)
                .addPotion(ModEffects.poison.get(), 60, 0).setRPRegen(0, -35));
        this.addStat(ModItems.weeds.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(-10, 0));
        this.addStat(ModItems.whiteGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.indigoGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.purpleGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.greenGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.blueGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.yellowGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.orangeGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.blackGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0).setRPRegen(5, 0));
        this.addStat(ModItems.antidoteGrass.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(10, 0)
                .curePotion(ModEffects.poison.get()).curePotion(MobEffects.POISON));
        this.addStat(ModItems.medicinalHerb.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(25, 0));

        this.addStat(ModItems.roundoff.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(ModEffects.seal.get()));
        this.addStat(ModItems.paraGone.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(MobEffects.MOVEMENT_SLOWDOWN).curePotion(ModEffects.paralysis.get()));
        this.addStat(ModItems.coldMed.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(ModEffects.cold.get()));
        this.addStat(ModItems.antidote.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0).curePotion(MobEffects.POISON).curePotion(ModEffects.poison.get()));
        this.addStat(ModItems.recoveryPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(100, 0));
        this.addStat(ModItems.healingPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(500, 0));
        this.addStat(ModItems.mysteryPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(2000, 0));
        this.addStat(ModItems.magicalPotion.get(), new FoodProperties.MutableFoodProps(100).setHPRegen(0, 50));

    }
}
