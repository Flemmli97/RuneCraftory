package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class MillionStrikeAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.MILLION_STRIKE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isPastTick(0.28) && !anim.isPastTick(0.68) && anim.getTickRaw() % (2 * anim.getSpeed()) == 0) {
            if (!entity.level.isClientSide) {
                float mod = (anim.getTickRaw() - Mth.ceil(0.28 * 20)) % (8 * anim.getSpeed());
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.circleTargets(entity.getLookAngle(), 15, 2))
                        .withBonusAttributes(mod == 6 ? Map.of(ModAttributes.CRIT.get(), 100d) : Map.of())
                        .withBonusAttributesMultiplier(Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack)))
                        .executeAttack();
            }
            entity.playSound(ModSounds.PLAYER_ATTACK_SWOOSH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.7f);
        }
    }
}
