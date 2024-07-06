package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTornado;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TornadoSpell extends Spell {

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        EntityTornado tornado = new EntityTornado(level, entity);
        tornado.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.75f));
        Vec3 pos = entity.position();
        float dirScale = 5;
        Vec3 target = pos.add(entity.getLookAngle().scale(dirScale));
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (mob instanceof MobAttackExt delayed && (delayedPos = delayed.targetPosition(tornado.position())) != null) {
                target = delayedPos;
            } else if (mob.getTarget() != null) {
                target = EntityUtil.getStraightProjectileTarget(pos, mob.getTarget());
            }
        }
        tornado.setPos(pos.x, pos.y, pos.z);
        Vec3 dir = target.subtract(pos);
        tornado.shoot(dir.x, dir.y, dir.z, 0.15f, 0);
        level.addFreshEntity(tornado);
        return true;
    }
}