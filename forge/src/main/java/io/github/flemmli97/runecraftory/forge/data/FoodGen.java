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
        this.addStat("fish", Items.COD, new FoodProperties.Builder(0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat("salmon", Items.SALMON, new FoodProperties.Builder(0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat("sweet_berry", Items.SWEET_BERRIES, new FoodProperties.Builder(0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat("cookie", Items.COOKIE, new FoodProperties.Builder(0).setRPRegen(3, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 2));
        this.addStat("honey_bottle", Items.HONEY_BOTTLE, new FoodProperties.Builder(0).setRPRegen(15, 1)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 10));
        this.addStat("apple", Items.APPLE, new FoodProperties.Builder(0).setRPRegen(7, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));

        this.addStat("chorus_fruit", Items.CHORUS_FRUIT, new FoodProperties.Builder(0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat("kelp", Items.DRIED_KELP, new FoodProperties.Builder(0).setRPRegen(3, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat("melon", Items.MELON_SLICE, new FoodProperties.Builder(0).setRPRegen(3, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat("potato", Items.POTATO, new FoodProperties.Builder(0).setRPRegen(7, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 10));
        this.addStat("carrot", Items.CARROT, new FoodProperties.Builder(0).setRPRegen(7, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 10));

        this.addStat(ModItems.WITHERED_GRASS.get(), new FoodProperties.Builder(0)
                .addPotion(ModEffects.POISON.get(), 60, 0).setRPRegen(0, -35)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), -10));
        this.addStat(ModItems.WEEDS.get(), new FoodProperties.Builder(0).setHPRegen(-10, 0)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), -5));
        this.addStat(ModItems.WHITE_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.INDIGO_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.PURPLE_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.GREEN_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.BLUE_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.YELLOW_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.RED_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.ORANGE_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.BLACK_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 3));
        this.addStat(ModItems.ANTIDOTE_GRASS.get(), new FoodProperties.Builder(0).setHPRegen(10, 0)
                .curePotion(ModEffects.POISON.get()).curePotion(MobEffects.POISON).curePotion(MobEffects.WITHER)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 5));
        this.addStat(ModItems.MEDICINAL_HERB.get(), new FoodProperties.Builder(0).setHPRegen(25, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 20));
        this.addStat(ModItems.BAMBOO_SPROUT.get(), new FoodProperties.Builder(0).setHPRegen(12, 0).setRPRegen(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10));

        this.addStat(ModItems.ROUNDOFF.get(), new FoodProperties.Builder(0).setHPRegen(500, 0).curePotion(ModEffects.SEAL.get()));
        this.addStat(ModItems.PARA_GONE.get(), new FoodProperties.Builder(0).setHPRegen(500, 0).curePotion(MobEffects.MOVEMENT_SLOWDOWN).curePotion(ModEffects.PARALYSIS.get()));
        this.addStat(ModItems.COLD_MED.get(), new FoodProperties.Builder(0).setHPRegen(500, 0).curePotion(ModEffects.COLD.get()));
        this.addStat(ModItems.ANTIDOTE.get(), new FoodProperties.Builder(0).setHPRegen(500, 0).curePotion(MobEffects.POISON).curePotion(ModEffects.POISON.get()).curePotion(MobEffects.WITHER));
        this.addStat(ModItems.RECOVERY_POTION.get(), new FoodProperties.Builder(0).setHPRegen(50, 0));
        this.addStat(ModItems.HEALING_POTION.get(), new FoodProperties.Builder(0).setHPRegen(150, 0));
        this.addStat(ModItems.MYSTERY_POTION.get(), new FoodProperties.Builder(0).setHPRegen(500, 0));
        this.addStat(ModItems.MAGICAL_POTION.get(), new FoodProperties.Builder(0).setHPRegen(0, 50));
        this.addStat(ModItems.INVINCIROID.get(), new FoodProperties.Builder(6000)
                .addEffect(ModAttributes.RES_PARA.get(), 100)
                .addEffect(ModAttributes.RES_POISON.get(), 100)
                .addEffect(ModAttributes.RES_SEAL.get(), 100)
                .addEffect(ModAttributes.RES_SLEEP.get(), 100)
                .addEffect(ModAttributes.RES_FAT.get(), 100)
                .addEffect(ModAttributes.RES_COLD.get(), 100)
                .curePotion(MobEffects.POISON)
                .curePotion(MobEffects.WITHER)
                .curePotion(MobEffects.MOVEMENT_SLOWDOWN)
                .curePotion(MobEffects.BAD_OMEN)
                .curePotion(MobEffects.BLINDNESS)
                .curePotion(MobEffects.LEVITATION)
                .curePotion(MobEffects.CONFUSION)
                .curePotion(ModEffects.SLEEP.get())
                .curePotion(ModEffects.POISON.get())
                .curePotion(ModEffects.PARALYSIS.get())
                .curePotion(ModEffects.SEAL.get())
                .curePotion(ModEffects.FATIGUE.get())
                .curePotion(ModEffects.COLD.get()));
        this.addStat(ModItems.FORMUADE.get(), new FoodProperties.Builder(6000).setRPIncrease(0, 75)
                .addEffectPercentage(Attributes.MAX_HEALTH, -50)
                .addEffect(Attributes.ATTACK_DAMAGE, 70)
                .addEffect(ModAttributes.MAGIC.get(), 70)
                .addEffect(ModAttributes.DEFENCE.get(), 35)
                .addEffect(ModAttributes.MAGIC_DEFENCE.get(), 35)
                .addEffect(ModAttributes.RES_POISON.get(), 25)
                .addEffect(ModAttributes.RES_PARA.get(), 25)
                .addEffect(ModAttributes.RES_SEAL.get(), 25)
                .addEffect(ModAttributes.RES_SLEEP.get(), 25)
                .addEffect(ModAttributes.RES_FAT.get(), 25)
                .addEffect(ModAttributes.RES_COLD.get(), 25)
                .addEffect(ModAttributes.RES_FAINT.get(), 25));
        this.addStat(ModItems.OBJECT_X.get(), new FoodProperties.Builder(0).setRPIncrease(0, -50)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), -15)
                .addCookingBonusPercent(ModAttributes.RPGAIN.get(), -15));

        this.addStat(ModItems.MUSHROOM.get(), new FoodProperties.Builder(0).setHPRegen(15, 1).setRPRegen(10, 0));
        this.addStat(ModItems.MONARCH_MUSHROOM.get(), new FoodProperties.Builder(0).setHPRegen(20, 2).setRPRegen(15, 0));

        this.addStat(ModItems.RICE_FLOUR.get(), new FoodProperties.Builder(0));
        this.addStat(ModItems.CURRY_POWDER.get(), new FoodProperties.Builder(0));
        this.addStat(ModItems.OIL.get(), new FoodProperties.Builder(6000).setHPRegen(0, 0)
                .addEffect(ModAttributes.RES_FIRE.get(), -30));
        this.addStat(ModItems.FLOUR.get(), new FoodProperties.Builder(0));
        this.addStat(ModItems.YOGURT.get(), new FoodProperties.Builder(0).setRPRegen(30, 0));
        this.addStat(ModItems.CHEESE.get(), new FoodProperties.Builder(0).setRPRegen(30, 0));
        this.addStat(ModItems.MAYONNAISE.get(), new FoodProperties.Builder(0).setRPRegen(20, 0));
        this.addStat(ModItems.EGG_S.get(), new FoodProperties.Builder(0).setHPRegen(5, 0)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), 5));
        this.addStat(ModItems.EGG_M.get(), new FoodProperties.Builder(0).setHPRegen(10, 0).setRPRegen(10, 0)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonusPercent(ModAttributes.RPGAIN.get(), 10));
        this.addStat(ModItems.EGG_L.get(), new FoodProperties.Builder(0).setHPRegen(40, 0).setRPRegen(20, 0)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), 40)
                .addCookingBonusPercent(ModAttributes.RPGAIN.get(), 20));
        this.addStat(ModItems.MILK_S.get(), new FoodProperties.Builder(0).setRPRegen(5, 0));
        this.addStat(ModItems.MILK_M.get(), new FoodProperties.Builder(0).setRPRegen(10, 0).setHPRegen(30, 0)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonusPercent(ModAttributes.RPGAIN.get(), 30));
        this.addStat(ModItems.MILK_L.get(), new FoodProperties.Builder(0).setRPRegen(40, 0).setHPRegen(50, 0)
                .addCookingBonusPercent(ModAttributes.HEALTHGAIN.get(), 40)
                .addCookingBonusPercent(ModAttributes.RPGAIN.get(), 50));
        this.addStat(ModItems.WINE.get(), new FoodProperties.Builder(0).setRPRegen(15, 0)
                .addPotion(ModEffects.SLEEP.get(), 80, 0));
        this.addStat(ModItems.CHOCOLATE.get(), new FoodProperties.Builder(0).setRPRegen(25, 0));
        this.addStat(ModItems.RICE.get(), new FoodProperties.Builder(0).setRPRegen(20, 0));

        //Main effects: HP/RP regen, damage, magic dmg, def
        this.addStat(ModItems.TURNIP_HEAVEN.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PICKLE_MIX.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALMON_ONIGIRI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.ONIGIRI.get(), new FoodProperties.Builder(6000)
                .setHPRegen(60, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(ModItems.RELAX_TEA_LEAVES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.ICE_CREAM.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.RAISIN_BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BAMBOO_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PICKLES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PICKLED_TURNIP.get(), new FoodProperties.Builder(4800)
                .setHPRegen(175, 0)
                .setRPRegen(100, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 3)
                .addEffect(ModAttributes.DEFENCE.get(), 1)
                .addEffect(ModAttributes.MAGIC.get(), 2));
        this.addStat(ModItems.FRUIT_SANDWICH.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SANDWICH.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALAD.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, status resistance
        this.addStat(ModItems.DUMPLINGS.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PUMPKIN_FLAN.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FLAN.get(), new FoodProperties.Builder(6000)
                .setHPRegen(200, 0)
                .setRPRegen(130, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1)
                .addEffect(ModAttributes.DEFENCE.get(), 1)
                .addEffect(ModAttributes.RES_FAT.get(), 15)
                .addEffect(ModAttributes.RES_COLD.get(), 15));
        this.addStat(ModItems.CHOCOLATE_SPONGE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.POUND_CAKE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.STEAMED_GYOZA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CURRY_MANJU.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CHINESE_MANJU.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.MEAT_DUMPLING.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CHEESE_BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.STEAMED_BREAD.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, magic damage
        this.addStat(ModItems.HOT_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PRELUDETO_LOVE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GOLD_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BUTTER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.KETCHUP.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.MIXED_SMOOTHIE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.MIXED_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.VEGGIE_SMOOTHIE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.VEGETABLE_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRUIT_SMOOTHIE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRUIT_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.STRAWBERRY_MILK.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.APPLE_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.ORANGE_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRAPE_JUICE.get(), new FoodProperties.Builder(4800)
                .setHPRegen(70, 0)
                .setRPRegen(50, 0)
                .addEffect(ModAttributes.MAGIC.get(), 3));
        this.addStat(ModItems.TOMATO_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PINEAPPLE_JUICE.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, defence
        this.addStat(ModItems.APPLE_PIE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CHEESECAKE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CHOCOLATE_CAKE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CAKE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CHOCO_COOKIE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.COOKIE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.YAMOFTHE_AGES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SEAFOOD_GRATIN.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRATIN.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SEAFOOD_DORIA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.DORIA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SEAFOOD_PIZZA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PIZZA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BUTTER_ROLL.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.JAM_ROLL.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TOAST.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SWEET_POTATO.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BAKED_ONIGIRI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CORN_ON_THE_COB.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, hp max inc
        this.addStat(ModItems.ROCKFISH_STEW.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.UNION_STEW.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_MISO.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.RELAX_TEA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.ROYAL_CURRY.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.ULTIMATE_CURRY.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CURRY_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.EGG_BOWL.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TEMPURA_BOWL.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.MILK_PORRIDGE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.RICE_PORRIDGE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TEMPURA_UDON.get(), new FoodProperties.Builder(6000)
                .setHPRegen(400, 30)
                .setRPRegen(200, 0)
                .addEffect(ModAttributes.HEALTHGAIN.get(), 200)
                .addEffect(ModAttributes.RES_EARTH.get(), 15));
        this.addStat(ModItems.CURRY_UDON.get(), new FoodProperties.Builder(6000)
                .setHPRegen(500, 50)
                .setRPRegen(220, 0)
                .addEffect(ModAttributes.HEALTHGAIN.get(), 250)
                .addEffect(ModAttributes.RES_EARTH.get(), 15));
        this.addStat(ModItems.UDON.get(), new FoodProperties.Builder(6000)
                .setHPRegen(200, 0)
                .setRPRegen(50, 0)
                .addEffect(ModAttributes.HEALTHGAIN.get(), 100)
                .addEffect(ModAttributes.RES_EARTH.get(), 5));
        this.addStat(ModItems.CHEESE_FONDUE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.MARMALADE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRAPE_JAM.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.APPLE_JAM.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.STRAWBERRY_JAM.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BOILED_GYOZA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GLAZED_YAM.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BOILED_EGG.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BOILED_SPINACH.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BOILED_PUMPKIN.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRAPE_LIQUEUR.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.HOT_MILK.get(), new FoodProperties.Builder(6000)
                .setHPRegen(50, 0)
                .setRPRegen(150, 1)
                .setRPIncrease(20, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 100, 1));
        this.addStat(ModItems.HOT_CHOCOLATE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(100, 0)
                .setRPRegen(300, 3)
                .setRPIncrease(50, 5)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1));

        //Main effects: HP/RP regen, damage, magic, def
        //For fish: hp/rp regen, rp max, magic
        this.addStat(ModItems.GRILLED_SAND_FLOUNDER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_SHRIMP.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_LOBSTER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_BLOWFISH.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_LAMP_SQUID.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_SUNSQUID.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_SQUID.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_FALL_FLOUNDER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_TURBOT.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_FLOUNDER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_PIKE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_NEEDLEFISH.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.DRIED_SARDINES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TUNA_TERIYAKI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_POND_SMELT.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_YELLOWTAIL.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_MACKEREL.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_SKIPJACK.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_LOVER_SNAPPER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_GLITTER_SNAPPER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_GIRELLA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_SNAPPER.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_GIBELIO.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GRILLED_CRUCIAN_CARP.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_TAIMEN.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_SALMON.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_CHUB.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_CHERRY_SALMON.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_RAINBOW_TROUT.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_CHAR.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALTED_MASU_TROUT.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.DRY_CURRY.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.RISOTTO.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GYOZA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PANCAKES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TEMPURA.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRIED_UDON.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.DONUT.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRENCH_TOAST.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CURRY_BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BAKED_APPLE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(130, 0)
                .addEffectPercentage(Attributes.MAX_HEALTH, 10)
                .addEffect(Attributes.ATTACK_DAMAGE, 5)
                .addEffect(ModAttributes.DEFENCE.get(), 2)
                .addEffect(ModAttributes.MAGIC.get(), 5)
                .addEffect(ModAttributes.MAGIC_DEFENCE.get(), 2));
        this.addStat(ModItems.OMELET_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.OMELET.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRIED_EGGS.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.MISO_EGGPLANT.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CORN_CEREAL.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.POPCORN.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CROQUETTES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRENCH_FRIES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CABBAGE_CAKES.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRIED_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FRIED_VEGGIES.get(), new FoodProperties.Builder(6000)
                .setHPRegen(250, 30)
                .setRPRegen(150, 25)
                .setRPIncrease(0, 20)
                .addEffectPercentage(Attributes.MAX_HEALTH, 300)
                .addEffect(ModAttributes.DEFENCE.get(), 1)
                .addEffect(ModAttributes.MAGIC.get(), 1));

        //Main effects: HP/RP regen, damage
        this.addStat(ModItems.SHRIMP_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.LOBSTER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.BLOWFISH_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.LAMP_SQUID_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SUNSQUID_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SQUID_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FALL_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TURBOT_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.FLOUNDER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.PIKE_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.NEEDLEFISH_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SARDINE_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TUNA_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.YELLOWTAIL_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SKIPJACK_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GIRELLA_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.LOVER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.GLITTER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SNAPPER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TAIMEN_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CHERRY_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.SALMON_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.RAINBOW_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.CHAR_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(ModItems.TROUT_SASHIMI.get(), new FoodProperties.Builder(6000));

        this.addStat(ModItems.FAILED_DISH.get(), new FoodProperties.Builder(6000)
                .setHPRegen(0, -20)
                .setRPIncrease(0, -10)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -10)
                .addEffectPercentage(ModAttributes.DEFENCE.get(), -10)
                .addEffectPercentage(ModAttributes.MAGIC.get(), -10)
                .addEffectPercentage(ModAttributes.MAGIC_DEFENCE.get(), -10));
        this.addStat(ModItems.DISASTROUS_DISH.get(), new FoodProperties.Builder(6000)
                .setHPRegen(0, -60)
                .setRPIncrease(0, -30)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -25)
                .addEffectPercentage(ModAttributes.DEFENCE.get(), -25)
                .addEffectPercentage(ModAttributes.MAGIC.get(), -25)
                .addEffectPercentage(ModAttributes.MAGIC_DEFENCE.get(), -25));
        this.addStat(ModItems.MIXED_HERBS.get(), new FoodProperties.Builder(6000)
                .addEffect(ModAttributes.RES_WIND.get(), -30));
        this.addStat(ModItems.SOUR_DROP.get(), new FoodProperties.Builder(6000)
                .addEffect(ModAttributes.RES_EARTH.get(), -30));
        this.addStat(ModItems.SWEET_POWDER.get(), new FoodProperties.Builder(6000)
                .addEffect(ModAttributes.RES_WATER.get(), -30));
        this.addStat(ModItems.HEAVY_SPICE.get(), new FoodProperties.Builder(6000)
                .addEffect(ModAttributes.RES_FIRE.get(), -30));
        this.addStat(ModItems.ORANGE.get(), new FoodProperties.Builder(0).setHPRegen(15, 0).setRPRegen(15, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 25));
        this.addStat(ModItems.GRAPES.get(), new FoodProperties.Builder(0).setHPRegen(15, 0).setRPRegen(15, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 25));
        this.addStat(ModItems.MEALY_APPLE.get(), new FoodProperties.Builder(0));

        this.addStat(ModItems.TURNIP.get(), new FoodProperties.Builder(0)
                .setHPRegen(20, 0)
                .setRPIncrease(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.TURNIP_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(45, 0)
                .setRPIncrease(15, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.TURNIP_PINK.get(), new FoodProperties.Builder(0)
                .setHPRegen(25, 0)
                .setRPIncrease(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.TURNIP_PINK_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(90, 0)
                .setRPIncrease(18, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.CABBAGE.get(), new FoodProperties.Builder(0)
                .setHPRegen(15, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.CABBAGE_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(35, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.PINK_MELON.get(), new FoodProperties.Builder(0)
                .setHPRegen(100, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.PINK_MELON_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(150, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.PINEAPPLE.get(), new FoodProperties.Builder(0)
                .setHPRegen(200, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.PINEAPPLE_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(300, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.STRAWBERRY.get(), new FoodProperties.Builder(0)
                .setHPRegen(60, 0)
                .setRPIncrease(3, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.STRAWBERRY_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(110, 0)
                .setRPIncrease(7, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_TURNIP.get(), new FoodProperties.Builder(0)
                .setHPRegen(300, 5)
                .setRPIncrease(3, 3)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_TURNIP_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(500, 5)
                .setRPIncrease(30, 5)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_POTATO.get(), new FoodProperties.Builder(0)
                .setHPRegen(230, 5)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_POTATO_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(400, 7)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_PUMPKIN.get(), new FoodProperties.Builder(0)
                .setHPRegen(200, 3)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_PUMPKIN_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(300, 7)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_CABBAGE.get(), new FoodProperties.Builder(0)
                .setHPRegen(150, 5)
                .setRPIncrease(5, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GOLDEN_CABBAGE_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(300, 5)
                .setRPIncrease(40, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.BOK_CHOY.get(), new FoodProperties.Builder(0)
                .setHPRegen(30, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.BOK_CHOY_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(60, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.LEEK.get(), new FoodProperties.Builder(0)
                .setHPRegen(20, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.LEEK_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(80, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.RADISH.get(), new FoodProperties.Builder(0)
                .setHPRegen(25, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.RADISH_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(50, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.SPINACH.get(), new FoodProperties.Builder(0)
                .setHPRegen(44, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.SPINACH_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(90, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GREEN_PEPPER.get(), new FoodProperties.Builder(0)
                .setHPRegen(60, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.GREEN_PEPPER_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(100, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.YAM.get(), new FoodProperties.Builder(0)
                .setHPRegen(33, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.YAM_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(70, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.EGGPLANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(55, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.EGGPLANT_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(120, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.TOMATO.get(), new FoodProperties.Builder(0)
                .setHPRegen(80, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.TOMATO_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(160, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.CORN.get(), new FoodProperties.Builder(0)
                .setHPRegen(45, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.CORN_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(110, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.CUCUMBER.get(), new FoodProperties.Builder(0)
                .setHPRegen(70, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.CUCUMBER_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(140, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.PUMPKIN.get(), new FoodProperties.Builder(0)
                .setHPRegen(20, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.PUMPKIN_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(75, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.ONION.get(), new FoodProperties.Builder(0)
                .setHPRegen(35, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.ONION_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(60, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));

        this.addStat(ModItems.POTATO_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(80, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));
        this.addStat(ModItems.CARROT_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(80, 0)
                .addCookingBonus(ModAttributes.HEALTHGAIN.get(), 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 5));

        this.addStat(ModItems.TOYHERB.get(), new FoodProperties.Builder(0)
                .setRPRegen(15, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.TOYHERB_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(60, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.MOONDROP_FLOWER.get(), new FoodProperties.Builder(0)
                .setRPRegen(18, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.MOONDROP_FLOWER_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(66, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.PINK_CAT.get(), new FoodProperties.Builder(0)
                .setRPRegen(20, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.PINK_CAT_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(70, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.CHARM_BLUE.get(), new FoodProperties.Builder(0)
                .setRPRegen(25, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.CHARM_BLUE_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(90, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.LAMP_GRASS.get(), new FoodProperties.Builder(0)
                .setRPRegen(33, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.LAMP_GRASS_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(80, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.CHERRY_GRASS.get(), new FoodProperties.Builder(0)
                .setRPRegen(20, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.CHERRY_GRASS_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(55, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.POM_POM_GRASS.get(), new FoodProperties.Builder(0)
                .setHPRegen(15, 0)
                .setRPRegen(10, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.POM_POM_GRASS_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(80, 0)
                .setRPRegen(25, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.AUTUMN_GRASS.get(), new FoodProperties.Builder(0)
                .setHPRegen(20, 0)
                .setRPRegen(15, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.AUTUMN_GRASS_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(20, 0)
                .setRPRegen(35, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.NOEL_GRASS.get(), new FoodProperties.Builder(0)
                .setHPRegen(80, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.NOEL_GRASS_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(250, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.FIREFLOWER.get(), new FoodProperties.Builder(0)
                .setRPRegen(35, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.FIREFLOWER_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(85, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.FOUR_LEAF_CLOVER.get(), new FoodProperties.Builder(0)
                .setRPRegen(25, 0)
                .addPotion(MobEffects.LUCK, 600, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.FOUR_LEAF_CLOVER_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(55, 0)
                .addPotion(MobEffects.LUCK, 600, 1)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.IRONLEAF.get(), new FoodProperties.Builder(0)
                .setHPRegen(-50, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.IRONLEAF_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(-100, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.WHITE_CRYSTAL.get(), new FoodProperties.Builder(0)
                .setRPRegen(75, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.WHITE_CRYSTAL_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(150, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.RED_CRYSTAL.get(), new FoodProperties.Builder(0)
                .setRPRegen(75, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.RED_CRYSTAL_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(150, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.GREEN_CRYSTAL.get(), new FoodProperties.Builder(0)
                .setRPRegen(75, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.GREEN_CRYSTAL_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(150, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.BLUE_CRYSTAL.get(), new FoodProperties.Builder(0)
                .setRPRegen(75, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.BLUE_CRYSTAL_GIANT.get(), new FoodProperties.Builder(0)
                .setRPRegen(150, 0)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 15));
        this.addStat(ModItems.EMERY_FLOWER.get(), new FoodProperties.Builder(0)
                .setHPRegen(0, 10)
                .setRPRegen(0, 5)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 50));
        this.addStat(ModItems.EMERY_FLOWER_GIANT.get(), new FoodProperties.Builder(0)
                .setHPRegen(0, 15)
                .setRPRegen(0, 10)
                .addCookingBonus(ModAttributes.RPGAIN.get(), 50));
    }
}
