package io.github.flemmli97.runecraftory.common.attackactions;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class MindThrustAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.MIND_THRUST.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAtTick(0.68)) {
            Vec3 dir = AttackAction.fromRelativeVector(entity, new Vec3(0, 0, 1));
            handler.setMoveTargetDir(dir.scale(0.5), anim, anim.getTick());
        }
        if (anim.canAttack())
            CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), CombatUtils.getAOE(entity, stack, 0), 0.5f, null,
                    Pair.of(Map.of(ModAttributes.PARA.get(), 0.4,
                                    ModAttributes.POISON.get(), 0.1,
                                    ModAttributes.SEAL.get(), 0.25),
                            Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), null);
    }
}
