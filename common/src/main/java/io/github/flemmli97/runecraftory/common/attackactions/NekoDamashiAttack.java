package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class NekoDamashiAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.NEKO_DAMASHI, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (state.isAt("attack")) {
            entity.playSound(RuneCraftorySounds.SPELL_GENERIC_POP.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
            if (!entity.level().isClientSide) {
                double range = Math.min(2.5, CombatUtils.getRange(entity, 0));
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(entity.getYRot(), 0, 1.5, range, true))
                        .withBonusAttributes(RuneCraftoryAttributes.PARALYSIS.asHolder(), 30)
                        .withBonusAttributes(RuneCraftoryAttributes.STUN.asHolder(), 20)
                        .withBonusAttributesMultiplier(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack, RuneCraftorySpells.NEKO_DAMASHI))
                        .executeAttack();
            }
        }
    }
}
