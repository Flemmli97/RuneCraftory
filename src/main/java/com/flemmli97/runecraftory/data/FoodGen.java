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

    }
}
