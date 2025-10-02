package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementalTrailEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ElementalSpell extends Spell {

    private final ItemElement element;

    public ElementalSpell(ItemElement element) {
        this.element = element;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        ElementalTrailEntity trail = new ElementalTrailEntity(level, entity, this.element);
        trail.knockback();
        trail.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        if (this.element == ItemElement.DARK) {
            Vec3 target = ProjectileUtils.getAimTarget(entity, trail.position());
            if (target != null)
                trail.shootAtEntity(target, 0.05f, 0);
            else
                trail.shoot(entity, entity.getXRot(), entity.getYRot(), 0, 0.05f, 0);
        } else {
            Vec3 eye = entity.getEyePosition();
            Vec3 dir = entity instanceof Mob mob && mob.getTarget() != null ? mob.getTarget().getEyePosition().subtract(eye).normalize().scale(1.4)
                    : entity.getViewVector(1).scale(1.4);
            trail.setPos(eye.x + dir.x, eye.y + dir.y, eye.z + dir.z);
        }
        level.addFreshEntity(trail);
        return true;
    }
}
