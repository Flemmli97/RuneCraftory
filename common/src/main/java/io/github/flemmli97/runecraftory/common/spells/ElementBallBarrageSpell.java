package io.github.flemmli97.runecraftory.common.spells;

import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.misc.ElementBallBarrageSummoner;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ElementBallBarrageSpell extends Spell {

    private final EnumElement element;

    public ElementBallBarrageSpell(EnumElement element) {
        this.element = element;
    }

    @Override
    public boolean use(ServerLevel level, LivingEntity entity, ItemStack stack, float rpUseMultiplier, int amount, int lvl) {
        if (!Spell.tryUseWithCost(entity, stack, this))
            return false;
        ElementBallBarrageSummoner summoner = new ElementBallBarrageSummoner(level, entity, this.element);
        summoner.setDamageMultiplier(CombatUtils.getAbilityDamageBonus(lvl, 0.75f));
        Vec3 eye = entity.getEyePosition();
        float dirScale = 5;
        Vec3 target = eye.add(entity.getLookAngle().scale(dirScale));
        if (entity instanceof Mob mob) {
            Vec3 delayedPos;
            if (mob instanceof MobAttackExt delayed && (delayedPos = delayed.targetPosition(summoner.position())) != null) {
                target = delayedPos;
            } else if (mob.getTarget() != null) {
                target = EntityUtil.getStraightProjectileTarget(eye, mob.getTarget());
            }
        }
        summoner.setPos(eye.x, eye.y, eye.z);
        summoner.setTarget(target.x, target.y, target.z);
        level.addFreshEntity(summoner);
        return true;
    }
}
