package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.AppleProjectileEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AppleRain extends Spell {

    private final Type type;

    public AppleRain(Type type) {
        this.type = type;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        int apples = switch (this.type) {
            case NORMAL -> 64;
            case BIG -> 50;
            case LOTS -> 96;
        };
        int range = switch (this.type) {
            case NORMAL -> 8;
            case BIG -> 9;
            case LOTS -> 10;
        } * 2;
        for (int i = 0; i < apples; i++) {
            AppleProjectileEntity apple = new AppleProjectileEntity(level, entity);
            if (this.type == Type.BIG) {
                apple.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.2f));
                apple.withSizeInc(1);
            } else
                apple.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 1.1f));
            double x = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * range;
            double y = entity.getY() + entity.getBbHeight() + 2 + entity.getRandom().nextDouble() * 6;
            double z = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * range;
            apple.setPos(x, y, z);
            level.addFreshEntity(apple);
        }
        playSound(entity, RuneCraftorySounds.SPELL_APPLE_RAIN.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        return true;
    }

    public enum Type {
        NORMAL,
        BIG,
        LOTS
    }
}
