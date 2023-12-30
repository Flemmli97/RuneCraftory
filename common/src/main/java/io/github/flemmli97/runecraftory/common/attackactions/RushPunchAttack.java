package io.github.flemmli97.runecraftory.common.attackactions;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class RushPunchAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.RUSH_PUNCH.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.isPastTick(0.16) && !anim.isPastTick(0.92) && anim.getTickRaw() % (3 * anim.getSpeed()) == 0) {
            float mod = (anim.getTickRaw() - Mth.ceil(0.16 * 20)) % (15 * anim.getSpeed());
            CombatUtils.spinAttackHandler(entity, entity.getLookAngle(), 10, 0, null,
                    Pair.of(mod == 12 ? Map.of(ModAttributes.CRIT.get(), 100d) : Map.of(), Map.of(Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack))), null);
        }
    }
}
