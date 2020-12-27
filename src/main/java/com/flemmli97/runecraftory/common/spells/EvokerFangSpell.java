package com.flemmli97.runecraftory.common.spells;

import com.flemmli97.runecraftory.api.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class EvokerFangSpell extends Spell {

    @Override
    public void update(PlayerEntity player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayerEntity player) {

    }

    @Override
    public int coolDown() {
        return 20;
    }

    @Override
    public boolean use(ServerWorld world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount) {
        Vector3d pos = entity.getPositionVec();
        Vector3d look = Vector3d.fromPitchYaw(0, entity.rotationYaw);
        for(int i = 0; i < 7; i ++) {
            pos = pos.add(look.x, 0, look.z);
            EvokerFangsEntity fang = new EvokerFangsEntity(world, pos.getX(), pos.getY(), pos.getZ(), entity.rotationYaw, 10, entity);
            world.addEntity(fang);
        }
        return true;
    }

    @Override
    public int rpCost() {
        return 1;
    }
}
