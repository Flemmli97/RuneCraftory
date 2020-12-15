package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.provider.FoodProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;

public class FoodGen extends FoodProvider {

    public FoodGen(DataGenerator gen) {
        super(gen, RuneCraftory.MODID);
    }

    @Override
    protected void add() {
        this.addStat("apple", Items.APPLE, new FoodProperties.MutableFoodProps(500).setHPRegen(3, 0));
        this.addStat("bread", Items.BREAD, new FoodProperties.MutableFoodProps(500).setHPRegen(3, 0)
                .addEffect(Attributes.GENERIC_ATTACK_DAMAGE, 1)
                .addPotion(Effects.SPEED, 100, 2));
        this.addStat("golden_apple", Items.GOLDEN_APPLE, new FoodProperties.MutableFoodProps(1500)
                .setHPRegen(50, 10).setRPIncrease(50, 5)
                .setRPRegen(25, 10)
                .addEffect(Attributes.GENERIC_ATTACK_DAMAGE, 10)
                .addPotion(Effects.SPEED, 100, 2)
                .addPotion(Effects.REGENERATION, 200, 3));
    }
}
