package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WindBladeCircle extends Spell {

    private final int amount;

    public WindBladeCircle(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        int angle = 360 / this.amount;
        for (int i = 0; i < this.amount; i++) {
            EntityWindBlade wind = new EntityWindBlade(level, entity);
            wind.setPos(wind.getX(), entity.getY() + entity.getBbHeight() * 0.4, wind.getZ());
            wind.setDamageMultiplier(1f + (lvl - 1) * 0.05f);
            wind.setPiercing();
            wind.shoot(entity, entity.getXRot(), entity.getYRot() + i * angle, 0, 0.35f, 0);
            level.addFreshEntity(wind);
        }
        return true;
    }
}