package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SpearUseAttack extends AttackAction {

    private final ComboContainer combo;

    public SpearUseAttack() {
        Predicate<WeaponHandler> MAIN = handler -> handler.getCurrentAnim().isPastTick(handler.getCurrentAnim().getAttackTime()) && !handler.getCurrentAnim().isPastTick(0.44);
        ComboContainer.Builder builder = ComboContainer.Builder.builder()
                .addCombo(handler -> handler.getCurrentAnim().isPastTick(handler.getCurrentAnim().getAttackTime()) && !handler.getCurrentAnim().isPastTick(0.4));
        for (int i = 0; i < 19; i++) {
            builder.addCombo(MAIN);
        }
        this.combo = builder.build();
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return comboIdx > 1 ? PlayerModelAnimations.SPEAR_USE_CONTINUE.create(speed) :
                PlayerModelAnimations.SPEAR_USE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        boolean finish = (anim.getID().equals("spear_use_continue") ? anim.isAtTick(0.88) : anim.isAtTick(0.96));
        if ((anim.canAttack() || finish) && entity instanceof ServerPlayer serverPlayer && stack.getItem() instanceof ItemSpearBase spear) {
            spear.useSpear(serverPlayer, stack, finish);
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
