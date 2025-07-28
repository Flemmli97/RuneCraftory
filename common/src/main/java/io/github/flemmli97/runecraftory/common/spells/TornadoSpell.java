package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.misc.TornadoEntity;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ProjectileUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TornadoSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        TornadoEntity tornado = new TornadoEntity(level, entity);
        tornado.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, this));
        Vec3 pos = entity.position();
        Vec3 target = ProjectileUtils.getAimTarget(entity);
        if (target == null) {
            target = pos.add(entity.getLookAngle().scale(10));
        }
        tornado.setPos(pos.x, pos.y, pos.z);
        Vec3 dir = target.subtract(pos);
        tornado.shoot(dir.x, dir.y, dir.z, 0.15f, 0);
        level.addFreshEntity(tornado);
        return true;
    }
}