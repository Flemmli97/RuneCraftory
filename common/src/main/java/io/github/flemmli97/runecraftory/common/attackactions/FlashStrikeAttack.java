package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FlashStrikeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.FLASH_STRIKE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        handler.store(DataKey.FIXED_LOOK, true);
        if (anim.isAt("move_1")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.5).add(0, 0.3, 0));
            entity.playSound(ModSounds.SPELL_GENERIC_LEAP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.7f);
        }
        if (anim.isAt("move_2")) {
            Vec3 dir = CombatUtils.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.store(DataKey.MOVE_DIRECTION, dir.scale(0.6));
        }
        if (anim.isAt("move_end")) {
            handler.store(DataKey.MOVE_DIRECTION, null);
        }
        handler.applyMoveDirection();
        if (!entity.level.isClientSide) {
            if (anim.isAt("reset"))
                handler.resetHitEntityTracker();
            if (anim.isPast("attack_start") && !anim.isPast("attack_end")) {
                double range = Math.max(0, CombatUtils.getRange(entity, -1));
                handler.addHitEntityTracker(CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.aabbTargets(entity.getBoundingBox()
                                .inflate(1, 0, 0).expandTowards(0, 0, range)))
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .doOnSuccess(target -> CombatUtils.knockBackEntity(entity, target, 1.2f))
                        .executeAttack());
            }
        }
    }

    @Override
    public boolean isInvulnerable(LivingEntity entity, AttackActionHandler handler) {
        return true;
    }
}
