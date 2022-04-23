package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;

public class SnowballSpell extends Spell {

    @Override
    public void update(Player player, ItemStack stack) {

    }

    @Override
    public void levelSkill(ServerPlayer player) {

    }

    @Override
    public int coolDown() {
        return 5;
    }

    @Override
    public boolean use(ServerLevel world, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int level) {
        Snowball snowball = new Snowball(world, entity);
        snowball.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, 2.0F, 1.0F);
        world.addFreshEntity(snowball);
        return true;
    }

    @Override
    public int rpCost() {
        return 1;
    }
}
