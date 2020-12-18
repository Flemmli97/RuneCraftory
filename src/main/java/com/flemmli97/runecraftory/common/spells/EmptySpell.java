package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EmptySpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {
    }

    @Override
    public void levelSkill(PlayerEntity player) {

    }

    @Override
    public int coolDown() {
        return 5;
    }

    @Override
    public boolean use(World world, LivingEntity player, ItemStack stack) {
        return true;
    }
}
