package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;

public class EmptySpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {
    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {

    }

    @Override
    public int coolDown() {
        return 5;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack) {
        return true;
    }

    @Override
    public int rpCost() {
        return 0;
    }
}
