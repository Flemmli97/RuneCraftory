package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWispFlame;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ElementalSpell extends Spell {

    private final EnumElement element;

    public ElementalSpell(EnumElement element) {
        this.element = element;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityWispFlame flame = new EntityWispFlame(level, entity, this.element);
        flame.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.9f));
        if (this.element == EnumElement.DARK) {
            if (entity instanceof Mob mob && mob.getTarget() != null)
                flame.shootAtEntity(mob.getTarget(), 0.05f, 0, 0, 0.2);
            else
                flame.shoot(entity, entity.getXRot(), entity.getYRot(), 0, 0.05f, 0);
        } else {
            Vec3 eye = entity.getEyePosition();
            Vec3 dir = entity instanceof Mob mob && mob.getTarget() != null ? mob.getTarget().getEyePosition().subtract(eye).normalize().scale(1.4)
                    : entity.getLookAngle().scale(1.4);
            flame.setPos(eye.x + dir.x, eye.y + dir.y, eye.z + dir.z);
        }
        level.addFreshEntity(flame);
        return true;
    }
}
