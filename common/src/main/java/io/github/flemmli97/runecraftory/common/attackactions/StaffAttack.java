package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.item.AOEWeapon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class StaffAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .build();

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AttackAction.create(PlayerModelAnimations.STAFF.get(comboIdx), speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (anim.isAt("attack")) {
            if (entity.level() instanceof ServerLevel serverLevel) {
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(AOEWeapon.createOBB(entity,
                                CombatUtils.getRange(entity, 0),
                                CombatUtils.getWidth(entity, 0), 0.5)))
                        .executeAttack();
                ModSpells.STAFF_CAST.get().use(serverLevel, entity, stack);
            }
            entity.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    public float movementReduction(AnimationState current) {
        return GeneralConfig.MOVE_SPEED_ATTACK.get().floatValue();
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
