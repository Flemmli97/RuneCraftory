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

public class ReaperSlashAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.REAPER_SLASH.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        if (anim.isAt("attack_start")) {
            handler.store(DataKey.SPIN_ROTATION, entity.getYRot() - 80);
        }
        if (anim.isAt("attack_middle"))
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.2f);
        if (!entity.level.isClientSide) {
            CombatUtils.EntityAttack attack = spinAttack(entity, anim, anim.getMarker("attack_start", 0), anim.getMarker("attack_end", 0),
                    handler.get(DataKey.SPIN_ROTATION), handler.get(DataKey.SPIN_ROTATION) + 170, 0);
            if (attack != null) {
                handler.addHitEntityTracker(attack
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))
                        .withTargetPredicate(e -> !handler.getHitEntityTracker().contains(e))
                        .executeAttack());
            }
        }
    }
}
