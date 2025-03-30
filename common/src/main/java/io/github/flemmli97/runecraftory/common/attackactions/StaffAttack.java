package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.item.IAOEWeapon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class StaffAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(ComboContainer.past("done"), 2)
            .build();

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STAFF.get(comboIdx).create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (anim.isAt("attack")) {
            if (entity.level instanceof ServerLevel serverLevel) {
                CombatUtils.EntityAttack.create(entity, CombatUtils.EntityAttack.obbTargets(IAOEWeapon.createOBB(entity, stack,
                                CombatUtils.getRange(entity, 0),
                                CombatUtils.getWidth(entity, 0))))
                        .executeAttack();
                ModSpells.STAFF_CAST.get().use(serverLevel, entity, stack);
            }
            entity.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    public float movementReduction(AnimatedAction current) {
        return GeneralConfig.moveSpeedAttack.get().floatValue();
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
