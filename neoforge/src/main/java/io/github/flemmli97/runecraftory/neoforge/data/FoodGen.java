package io.github.flemmli97.runecraftory.neoforge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.provider.FoodProvider;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class FoodGen extends FoodProvider {

    public FoodGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, RuneCraftory.MODID, provider);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.addStat("fish", Items.COD, new FoodProperties.Builder(6000).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat("salmon", Items.SALMON, new FoodProperties.Builder(6000).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat("sweet_berry", Items.SWEET_BERRIES, new FoodProperties.Builder(6000).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat("cookie", Items.COOKIE, new FoodProperties.Builder(6000).setRPRegen(3, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 2));
        this.addStat("honey_bottle", Items.HONEY_BOTTLE, new FoodProperties.Builder(6000).setRPRegen(15, 1)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 10));
        this.addStat("apple", Items.APPLE, new FoodProperties.Builder(6000).setRPRegen(7, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));

        this.addStat("chorus_fruit", Items.CHORUS_FRUIT, new FoodProperties.Builder(6000).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat("kelp", Items.DRIED_KELP, new FoodProperties.Builder(6000).setRPRegen(3, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat("melon", Items.MELON_SLICE, new FoodProperties.Builder(6000).setRPRegen(3, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat("potato", Items.POTATO, new FoodProperties.Builder(6000).setRPRegen(7, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 10));
        this.addStat("carrot", Items.CARROT, new FoodProperties.Builder(6000).setRPRegen(7, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 10));

        this.addStat(RuneCraftoryItems.WITHERED_GRASS.get(), new FoodProperties.Builder(6000)
                .addPotion(RuneCraftoryEffects.POISON.asHolder(), 60, 0).setRPRegen(0, -35)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), -10));
        this.addStat(RuneCraftoryItems.WEEDS.get(), new FoodProperties.Builder(6000).setHPRegen(-10, 0)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), -5));
        this.addStat(RuneCraftoryItems.WHITE_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.INDIGO_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.PURPLE_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.GREEN_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.BLUE_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.YELLOW_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.RED_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.ORANGE_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.BLACK_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 3));
        this.addStat(RuneCraftoryItems.ANTIDOTE_GRASS.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0)
                .curePotion(RuneCraftoryEffects.POISON.asHolder()).curePotion(MobEffects.POISON).curePotion(MobEffects.WITHER)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.MEDICINAL_HERB.get(), new FoodProperties.Builder(6000).setHPRegen(25, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 20));
        this.addStat(RuneCraftoryItems.BAMBOO_SPROUT.get(), new FoodProperties.Builder(6000).setHPRegen(12, 0).setRPRegen(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10));

        this.addStat(RuneCraftoryItems.ROUNDOFF.get(), new FoodProperties.Builder(6000).setHPRegen(500, 0).curePotion(RuneCraftoryEffects.SEAL.asHolder()));
        this.addStat(RuneCraftoryItems.PARA_GONE.get(), new FoodProperties.Builder(6000).setHPRegen(500, 0).curePotion(MobEffects.MOVEMENT_SLOWDOWN).curePotion(RuneCraftoryEffects.PARALYSIS.asHolder()));
        this.addStat(RuneCraftoryItems.COLD_MED.get(), new FoodProperties.Builder(6000).setHPRegen(500, 0).curePotion(RuneCraftoryEffects.COLD.asHolder()));
        this.addStat(RuneCraftoryItems.ANTIDOTE.get(), new FoodProperties.Builder(6000).setHPRegen(500, 0).curePotion(MobEffects.POISON).curePotion(RuneCraftoryEffects.POISON.asHolder()).curePotion(MobEffects.WITHER));
        this.addStat(RuneCraftoryItems.RECOVERY_POTION.get(), new FoodProperties.Builder(6000).setHPRegen(50, 0));
        this.addStat(RuneCraftoryItems.HEALING_POTION.get(), new FoodProperties.Builder(6000).setHPRegen(150, 0));
        this.addStat(RuneCraftoryItems.MYSTERY_POTION.get(), new FoodProperties.Builder(6000).setHPRegen(500, 0));
        this.addStat(RuneCraftoryItems.MAGICAL_POTION.get(), new FoodProperties.Builder(6000).setHPRegen(0, 50));
        this.addStat(RuneCraftoryItems.INVINCIROID.get(), new FoodProperties.Builder(6000)
                .addEffect(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 100)
                .addEffect(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 100)
                .addEffect(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 100)
                .addEffect(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 100)
                .addEffect(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 100)
                .addEffect(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 100)
                .curePotion(MobEffects.POISON)
                .curePotion(MobEffects.WITHER)
                .curePotion(MobEffects.MOVEMENT_SLOWDOWN)
                .curePotion(MobEffects.BAD_OMEN)
                .curePotion(MobEffects.BLINDNESS)
                .curePotion(MobEffects.LEVITATION)
                .curePotion(MobEffects.CONFUSION)
                .curePotion(RuneCraftoryEffects.SLEEP.asHolder())
                .curePotion(RuneCraftoryEffects.POISON.asHolder())
                .curePotion(RuneCraftoryEffects.PARALYSIS.asHolder())
                .curePotion(RuneCraftoryEffects.SEAL.asHolder())
                .curePotion(RuneCraftoryEffects.FATIGUE.asHolder())
                .curePotion(RuneCraftoryEffects.COLD.asHolder()));
        this.addStat(RuneCraftoryItems.FORMUADE.get(), new FoodProperties.Builder(6000).setRPIncrease(0, 75)
                .addEffectPercentage(Attributes.MAX_HEALTH, -50)
                .addEffect(Attributes.ATTACK_DAMAGE, 70)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 70)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 35)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 35)
                .addEffect(RuneCraftoryAttributes.POISON_RESISTANCE.asHolder(), 25)
                .addEffect(RuneCraftoryAttributes.PARALYSIS_RESISTANCE.asHolder(), 25)
                .addEffect(RuneCraftoryAttributes.SEAL_RESISTANCE.asHolder(), 25)
                .addEffect(RuneCraftoryAttributes.SLEEP_RESISTANCE.asHolder(), 25)
                .addEffect(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 25)
                .addEffect(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 25)
                .addEffect(RuneCraftoryAttributes.FAINT_RESISTANCE.asHolder(), 25));
        this.addStat(RuneCraftoryItems.OBJECT_X.get(), new FoodProperties.Builder(6000).setRPIncrease(0, -50)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), -15)
                .addCookingBonusPercent(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), -15));

        this.addStat(RuneCraftoryItems.MUSHROOM.get(), new FoodProperties.Builder(6000).setHPRegen(15, 1).setRPRegen(10, 0));
        this.addStat(RuneCraftoryItems.MONARCH_MUSHROOM.get(), new FoodProperties.Builder(6000).setHPRegen(20, 2).setRPRegen(15, 0));

        this.addStat(RuneCraftoryItems.RICE.get(), new FoodProperties.Builder(6000).setRPRegen(20, 0));
        this.addStat(RuneCraftoryItems.RICE_FLOUR.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FLOUR.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.OIL.get(), new FoodProperties.Builder(6000).setHPRegen(0, 0)
                .addEffect(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -30));
        this.addStat(RuneCraftoryItems.CURRY_POWDER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.WINE.get(), new FoodProperties.Builder(6000).setRPRegen(15, 0)
                .addPotion(RuneCraftoryEffects.SLEEP.asHolder(), 80, 0));
        this.addStat(RuneCraftoryItems.CHOCOLATE.get(), new FoodProperties.Builder(6000).setRPRegen(25, 0));
        this.addStat(RuneCraftoryItems.EGG_S.get(), new FoodProperties.Builder(6000).setHPRegen(5, 0)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.EGG_M.get(), new FoodProperties.Builder(6000).setHPRegen(10, 0).setRPRegen(10, 0)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonusPercent(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 10));
        this.addStat(RuneCraftoryItems.EGG_L.get(), new FoodProperties.Builder(6000).setHPRegen(40, 0).setRPRegen(20, 0)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 40)
                .addCookingBonusPercent(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 20));
        this.addStat(RuneCraftoryItems.MILK_S.get(), new FoodProperties.Builder(6000).setRPRegen(5, 0));
        this.addStat(RuneCraftoryItems.MILK_M.get(), new FoodProperties.Builder(6000).setRPRegen(10, 0).setHPRegen(30, 0)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonusPercent(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 30));
        this.addStat(RuneCraftoryItems.MILK_L.get(), new FoodProperties.Builder(6000).setRPRegen(40, 0).setHPRegen(50, 0)
                .addCookingBonusPercent(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 40)
                .addCookingBonusPercent(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 50));

        //Main effects: HP/RP regen, damage, magic dmg, defense stats
        this.addStat(RuneCraftoryItems.ONIGIRI.get(), new FoodProperties.Builder(6000)
                .setHPRegen(60, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 2));
        this.addStat(RuneCraftoryItems.CHEESE.get(), new FoodProperties.Builder(6000).setRPRegen(30, 0));
        this.addStat(RuneCraftoryItems.PICKLED_TURNIP.get(), new FoodProperties.Builder(4800)
                .setHPRegen(175, 0)
                .setRPRegen(100, 0)
                .addEffect(Attributes.ATTACK_DAMAGE, 3)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2));
        this.addStat(RuneCraftoryItems.PICKLES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.BAMBOO_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALMON_ONIGIRI.get(), new FoodProperties.Builder(6000)
                .setHPRegen(250, 5)
                .addEffect(Attributes.ATTACK_DAMAGE, 10)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 5)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 7));
        this.addStat(RuneCraftoryItems.PICKLE_MIX.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SANDWICH.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FRUIT_SANDWICH.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALAD.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.RELAX_TEA_LEAVES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TURNIP_HEAVEN.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, status resistance
        this.addStat(RuneCraftoryItems.DUMPLINGS.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FLAN.get(), new FoodProperties.Builder(6000)
                .setHPRegen(200, 0)
                .setRPRegen(130, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addEffect(RuneCraftoryAttributes.FATIGUE_RESISTANCE.asHolder(), 15)
                .addEffect(RuneCraftoryAttributes.COLD_RESISTANCE.asHolder(), 15));
        this.addStat(RuneCraftoryItems.PUMPKIN_FLAN.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.STEAMED_BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CHEESE_BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.POUND_CAKE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CHOCOLATE_SPONGE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CURRY_MANJU.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CHINESE_MANJU.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.MEAT_DUMPLING.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.STEAMED_GYOZA.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, magic damage
        this.addStat(RuneCraftoryItems.MAYONNAISE.get(), new FoodProperties.Builder(6000).setRPRegen(20, 0));
        this.addStat(RuneCraftoryItems.BUTTER.get(), new FoodProperties.Builder(6000).setRPRegen(30, 0));
        this.addStat(RuneCraftoryItems.KETCHUP.get(), new FoodProperties.Builder(6000).setRPRegen(20, 0));
        this.addStat(RuneCraftoryItems.ICE_CREAM.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.APPLE_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(150, 0)
                .setRPRegen(70, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 2));
        this.addStat(RuneCraftoryItems.ORANGE_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(180, 0)
                .setRPRegen(90, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GRAPE_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(230, 0)
                .setRPRegen(120, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 10));
        this.addStat(RuneCraftoryItems.STRAWBERRY_MILK.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TOMATO_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(430, 0)
                .setRPRegen(160, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 15));
        this.addStat(RuneCraftoryItems.PINEAPPLE_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(1600, 0)
                .setRPRegen(300, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 45));
        this.addStat(RuneCraftoryItems.FRUIT_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(500, 0)
                .setRPRegen(200, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 19));
        this.addStat(RuneCraftoryItems.FRUIT_SMOOTHIE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(900, 0)
                .setRPRegen(300, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 33));
        this.addStat(RuneCraftoryItems.VEGETABLE_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(610, 0)
                .setRPRegen(200, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 22));
        this.addStat(RuneCraftoryItems.VEGGIE_SMOOTHIE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(1050, 0)
                .setRPRegen(300, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 37));
        this.addStat(RuneCraftoryItems.MIXED_JUICE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(860, 0)
                .setRPRegen(200, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 38));
        this.addStat(RuneCraftoryItems.MIXED_SMOOTHIE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(1300, 0)
                .setRPRegen(300, 0)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 40));
        this.addStat(RuneCraftoryItems.HOT_JUICE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.PRELUDE_TO_LOVE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GOLD_JUICE.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, defence
        this.addStat(RuneCraftoryItems.BAKED_ONIGIRI.get(), new FoodProperties.Builder(6000)
                .setHPRegen(60, 0)
                .setRPRegen(50, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2));
        this.addStat(RuneCraftoryItems.SWEET_POTATO.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CORN_ON_THE_COB.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TOAST.get(), new FoodProperties.Builder(6000)
                .setHPRegen(180, 0)
                .setRPRegen(90, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 13)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 6));
        this.addStat(RuneCraftoryItems.RAISIN_BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.YAM_OF_THE_AGES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.BUTTER_ROLL.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.JAM_ROLL.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.APPLE_PIE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(500, 0)
                .setRPRegen(300, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 15)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 15));
        this.addStat(RuneCraftoryItems.CAKE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(750, 0)
                .setRPRegen(350, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 15)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 18));
        this.addStat(RuneCraftoryItems.CHEESECAKE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(1000, 0)
                .setRPRegen(400, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 25)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 20));
        this.addStat(RuneCraftoryItems.CHOCOLATE_CAKE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(1900, 0)
                .setRPRegen(600, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 30)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 30));
        this.addStat(RuneCraftoryItems.COOKIE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(666, 0)
                .setRPRegen(130, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 16)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 16));
        this.addStat(RuneCraftoryItems.CHOCO_COOKIE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(950, 0)
                .setRPRegen(280, 0)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 19)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 19));
        this.addStat(RuneCraftoryItems.DORIA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SEAFOOD_DORIA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.PIZZA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SEAFOOD_PIZZA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRATIN.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SEAFOOD_GRATIN.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, hp, rp increase
        this.addStat(RuneCraftoryItems.YOGURT.get(), new FoodProperties.Builder(6000).setRPRegen(30, 0));
        this.addStat(RuneCraftoryItems.RICE_PORRIDGE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.MILK_PORRIDGE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.MARMALADE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(50, 0)
                .setRPRegen(10, 0));
        this.addStat(RuneCraftoryItems.APPLE_JAM.get(), new FoodProperties.Builder(6000)
                .setHPRegen(50, 0)
                .setRPRegen(10, 0));
        this.addStat(RuneCraftoryItems.GRAPE_JAM.get(), new FoodProperties.Builder(6000)
                .setHPRegen(50, 0)
                .setRPRegen(10, 0));
        this.addStat(RuneCraftoryItems.STRAWBERRY_JAM.get(), new FoodProperties.Builder(6000)
                .setHPRegen(50, 0)
                .setRPRegen(10, 0));
        this.addStat(RuneCraftoryItems.HOT_MILK.get(), new FoodProperties.Builder(6000)
                .setHPRegen(50, 0)
                .setRPRegen(150, 1)
                .setRPIncrease(20, 0)
                .addPotion(MobEffects.MOVEMENT_SPEED, 100, 1));
        this.addStat(RuneCraftoryItems.HOT_CHOCOLATE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(100, 0)
                .setRPRegen(300, 3)
                .setRPIncrease(50, 5)
                .addPotion(MobEffects.MOVEMENT_SPEED, 200, 1));
        this.addStat(RuneCraftoryItems.BOILED_EGG.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.BOILED_SPINACH.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.BOILED_PUMPKIN.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CHEESE_FONDUE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRAPE_LIQUEUR.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GLAZED_YAM.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_MISO.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.STEW.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.ROCKFISH_STEW.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.UNION_STEW.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.EGG_BOWL.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TEMPURA_BOWL.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CURRY_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.UDON.get(), new FoodProperties.Builder(6000)
                .setHPRegen(200, 0)
                .setRPRegen(50, 0)
                .addEffect(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 100)
                .addEffect(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 5));
        this.addStat(RuneCraftoryItems.TEMPURA_UDON.get(), new FoodProperties.Builder(6000)
                .setHPRegen(400, 30)
                .setRPRegen(200, 0)
                .addEffect(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 200)
                .addEffect(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15));
        this.addStat(RuneCraftoryItems.CURRY_UDON.get(), new FoodProperties.Builder(6000)
                .setHPRegen(500, 50)
                .setRPRegen(220, 0)
                .addEffect(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 250)
                .addEffect(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), 15));
        this.addStat(RuneCraftoryItems.BOILED_GYOZA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.RELAX_TEA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.ULTIMATE_CURRY.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.ROYAL_CURRY.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, damage, magic, def
        this.addStat(RuneCraftoryItems.BAKED_APPLE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(130, 0)
                .addEffectPercentage(Attributes.MAX_HEALTH, 10)
                .addEffect(Attributes.ATTACK_DAMAGE, 5)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 2)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 5)
                .addEffect(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), 2));
        this.addStat(RuneCraftoryItems.FRIED_EGGS.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.POPCORN.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FRENCH_FRIES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CORN_CEREAL.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.OMELET.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.OMELET_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FRIED_RICE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FRIED_VEGGIES.get(), new FoodProperties.Builder(6000)
                .setHPRegen(250, 30)
                .setRPRegen(150, 25)
                .setRPIncrease(0, 20)
                .addEffectPercentage(Attributes.MAX_HEALTH, 300)
                .addEffect(RuneCraftoryAttributes.DEFENCE.asHolder(), 1)
                .addEffect(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), 1));
        this.addStat(RuneCraftoryItems.FRENCH_TOAST.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CROQUETTES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.PANCAKES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.DONUT.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.RISOTTO.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.MISO_EGGPLANT.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GYOZA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TEMPURA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CURRY_BREAD.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CABBAGE_CAKES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.DRY_CURRY.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FRIED_UDON.get(), new FoodProperties.Builder(6000));

        //For fish: hp/rp regen, rp max, magic
        this.addStat(RuneCraftoryItems.SALTED_CHAR.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_MASU_TROUT.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_CHERRY_SALMON.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_RAINBOW_TROUT.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_SALMON.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_TAIMEN.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_CHUB.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_SQUID.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_SUNSQUID.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_LAMP_SQUID.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_SAND_FLOUNDER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_SHRIMP.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_LOBSTER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_BLOWFISH.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_FALL_FLOUNDER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_TURBOT.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_FLOUNDER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_PIKE.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_NEEDLEFISH.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.DRIED_SARDINES.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TUNA_TERIYAKI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALTED_POND_SMELT.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_YELLOWTAIL.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_MACKEREL.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_SKIPJACK.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_LOVER_SNAPPER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_GLITTER_SNAPPER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_GIRELLA.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_SNAPPER.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_GIBELIO.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GRILLED_CRUCIAN_CARP.get(), new FoodProperties.Builder(6000));

        //Main effects: HP/RP regen, damage
        this.addStat(RuneCraftoryItems.CHAR_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TROUT_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.CHERRY_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.RAINBOW_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SALMON_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TAIMEN_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SQUID_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SUNSQUID_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.LAMP_SQUID_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SHRIMP_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.LOBSTER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.BLOWFISH_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FALL_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TURBOT_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.FLOUNDER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.PIKE_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.NEEDLEFISH_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SARDINE_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.TUNA_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.YELLOWTAIL_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SKIPJACK_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GIRELLA_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.LOVER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.GLITTER_SASHIMI.get(), new FoodProperties.Builder(6000));
        this.addStat(RuneCraftoryItems.SNAPPER_SASHIMI.get(), new FoodProperties.Builder(6000));

        this.addStat(RuneCraftoryItems.FAILED_DISH.get(), new FoodProperties.Builder(6000)
                .setHPRegen(0, -20)
                .setRPIncrease(0, -10)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -10)
                .addEffectPercentage(RuneCraftoryAttributes.DEFENCE.asHolder(), -10)
                .addEffectPercentage(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), -10)
                .addEffectPercentage(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), -10));
        this.addStat(RuneCraftoryItems.DISASTROUS_DISH.get(), new FoodProperties.Builder(6000)
                .setHPRegen(0, -60)
                .setRPIncrease(0, -30)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -25)
                .addEffectPercentage(RuneCraftoryAttributes.DEFENCE.asHolder(), -25)
                .addEffectPercentage(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), -25)
                .addEffectPercentage(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), -25));
        this.addStat(RuneCraftoryItems.MIXED_HERBS.get(), new FoodProperties.Builder(6000)
                .addEffect(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -30));
        this.addStat(RuneCraftoryItems.SOUR_DROP.get(), new FoodProperties.Builder(6000)
                .addEffect(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), -30));
        this.addStat(RuneCraftoryItems.SWEET_POWDER.get(), new FoodProperties.Builder(6000)
                .addEffect(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), -30));
        this.addStat(RuneCraftoryItems.HEAVY_SPICE.get(), new FoodProperties.Builder(6000)
                .addEffect(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -30));
        this.addStat(RuneCraftoryItems.ORANGE.get(), new FoodProperties.Builder(6000).setHPRegen(15, 0).setRPRegen(15, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 25));
        this.addStat(RuneCraftoryItems.GRAPES.get(), new FoodProperties.Builder(6000).setHPRegen(15, 0).setRPRegen(15, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 25));
        this.addStat(RuneCraftoryItems.MEALY_APPLE.get(), new FoodProperties.Builder(6000)
                .addEffectPercentage(Attributes.ATTACK_DAMAGE, -50)
                .addEffectPercentage(RuneCraftoryAttributes.DEFENCE.asHolder(), -50)
                .addEffectPercentage(RuneCraftoryAttributes.MAGIC_ATTACK.asHolder(), -50)
                .addEffectPercentage(RuneCraftoryAttributes.MAGIC_DEFENCE.asHolder(), -50)
                .addEffectPercentage(RuneCraftoryAttributes.FIRE_RESISTANCE.asHolder(), -20)
                .addEffectPercentage(RuneCraftoryAttributes.WATER_RESISTANCE.asHolder(), -20)
                .addEffectPercentage(RuneCraftoryAttributes.EARTH_RESISTANCE.asHolder(), -20)
                .addEffectPercentage(RuneCraftoryAttributes.WIND_RESISTANCE.asHolder(), -20));

        this.addStat(RuneCraftoryItems.TURNIP.get(), new FoodProperties.Builder(6000)
                .setHPRegen(20, 0)
                .setRPIncrease(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.TURNIP_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(45, 0)
                .setRPIncrease(15, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.TURNIP_PINK.get(), new FoodProperties.Builder(6000)
                .setHPRegen(25, 0)
                .setRPIncrease(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.TURNIP_PINK_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(90, 0)
                .setRPIncrease(18, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CABBAGE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(15, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CABBAGE_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(35, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.PINK_MELON.get(), new FoodProperties.Builder(6000)
                .setHPRegen(100, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.PINK_MELON_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(150, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.PINEAPPLE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(200, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.PINEAPPLE_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(300, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.STRAWBERRY.get(), new FoodProperties.Builder(6000)
                .setHPRegen(60, 0)
                .setRPIncrease(3, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.STRAWBERRY_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(110, 0)
                .setRPIncrease(7, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_TURNIP.get(), new FoodProperties.Builder(6000)
                .setHPRegen(300, 5)
                .setRPIncrease(3, 3)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_TURNIP_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(500, 5)
                .setRPIncrease(30, 5)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_POTATO.get(), new FoodProperties.Builder(6000)
                .setHPRegen(230, 5)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_POTATO_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(400, 7)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_PUMPKIN.get(), new FoodProperties.Builder(6000)
                .setHPRegen(200, 3)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_PUMPKIN_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(300, 7)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_CABBAGE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(150, 5)
                .setRPIncrease(5, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GOLDEN_CABBAGE_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(300, 5)
                .setRPIncrease(40, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.NAPA_CABBAGE.get(), new FoodProperties.Builder(6000)
                .setHPRegen(30, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.NAPA_CABBAGE_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(60, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.LEEK.get(), new FoodProperties.Builder(6000)
                .setHPRegen(20, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.LEEK_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(80, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.RADISH.get(), new FoodProperties.Builder(6000)
                .setHPRegen(25, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.RADISH_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(50, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.SPINACH.get(), new FoodProperties.Builder(6000)
                .setHPRegen(44, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.SPINACH_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(90, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GREEN_PEPPER.get(), new FoodProperties.Builder(6000)
                .setHPRegen(60, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.GREEN_PEPPER_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(100, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.YAM.get(), new FoodProperties.Builder(6000)
                .setHPRegen(33, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.YAM_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(70, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.EGGPLANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(55, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.EGGPLANT_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(120, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.TOMATO.get(), new FoodProperties.Builder(6000)
                .setHPRegen(80, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.TOMATO_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(160, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CORN.get(), new FoodProperties.Builder(6000)
                .setHPRegen(45, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CORN_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(110, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CUCUMBER.get(), new FoodProperties.Builder(6000)
                .setHPRegen(70, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CUCUMBER_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(140, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.PUMPKIN.get(), new FoodProperties.Builder(6000)
                .setHPRegen(20, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.PUMPKIN_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(75, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.ONION.get(), new FoodProperties.Builder(6000)
                .setHPRegen(35, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.ONION_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(60, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));

        this.addStat(RuneCraftoryItems.POTATO_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(80, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));
        this.addStat(RuneCraftoryItems.CARROT_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(80, 0)
                .addCookingBonus(RuneCraftoryAttributes.HEALTH_GAIN.asHolder(), 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 5));

        this.addStat(RuneCraftoryItems.TOYHERB.get(), new FoodProperties.Builder(6000)
                .setRPRegen(15, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.TOYHERB_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(60, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.MOONDROP_FLOWER.get(), new FoodProperties.Builder(6000)
                .setRPRegen(18, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.MOONDROP_FLOWER_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(66, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.PINK_CAT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(20, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.PINK_CAT_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(70, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.CHARM_BLUE.get(), new FoodProperties.Builder(6000)
                .setRPRegen(25, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.CHARM_BLUE_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(90, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.LAMP_GRASS.get(), new FoodProperties.Builder(6000)
                .setRPRegen(33, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.LAMP_GRASS_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(80, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.CHERRY_GRASS.get(), new FoodProperties.Builder(6000)
                .setRPRegen(20, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.CHERRY_GRASS_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(55, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.POM_POM_GRASS.get(), new FoodProperties.Builder(6000)
                .setHPRegen(15, 0)
                .setRPRegen(10, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.POM_POM_GRASS_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(80, 0)
                .setRPRegen(25, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.AUTUMN_GRASS.get(), new FoodProperties.Builder(6000)
                .setHPRegen(20, 0)
                .setRPRegen(15, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.AUTUMN_GRASS_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(20, 0)
                .setRPRegen(35, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.NOEL_GRASS.get(), new FoodProperties.Builder(6000)
                .setHPRegen(80, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.NOEL_GRASS_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(250, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.FIREFLOWER.get(), new FoodProperties.Builder(6000)
                .setRPRegen(35, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.FIREFLOWER_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(85, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.FOUR_LEAF_CLOVER.get(), new FoodProperties.Builder(6000)
                .setRPRegen(25, 0)
                .addPotion(MobEffects.LUCK, 600, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.FOUR_LEAF_CLOVER_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(55, 0)
                .addPotion(MobEffects.LUCK, 600, 1)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.IRONLEAF.get(), new FoodProperties.Builder(6000)
                .setHPRegen(-50, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.IRONLEAF_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(-100, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.WHITE_CRYSTAL.get(), new FoodProperties.Builder(6000)
                .setRPRegen(75, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.WHITE_CRYSTAL_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(150, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.RED_CRYSTAL.get(), new FoodProperties.Builder(6000)
                .setRPRegen(75, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.RED_CRYSTAL_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(150, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.GREEN_CRYSTAL.get(), new FoodProperties.Builder(6000)
                .setRPRegen(75, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.GREEN_CRYSTAL_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(150, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.BLUE_CRYSTAL.get(), new FoodProperties.Builder(6000)
                .setRPRegen(75, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.BLUE_CRYSTAL_GIANT.get(), new FoodProperties.Builder(6000)
                .setRPRegen(150, 0)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 15));
        this.addStat(RuneCraftoryItems.EMERY_FLOWER.get(), new FoodProperties.Builder(6000)
                .setHPRegen(0, 10)
                .setRPRegen(0, 5)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 50));
        this.addStat(RuneCraftoryItems.EMERY_FLOWER_GIANT.get(), new FoodProperties.Builder(6000)
                .setHPRegen(0, 15)
                .setRPRegen(0, 10)
                .addCookingBonus(RuneCraftoryAttributes.RUNE_POINTS_GAIN.asHolder(), 50));
    }
}
