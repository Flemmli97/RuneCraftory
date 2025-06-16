package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class TimedUseAttack extends AttackAction {

    private final String animation;
    private final boolean ignoreAttackSpeed;

    private final BiConsumer<LivingEntity, ItemStack> attack;
    private final boolean mountedUse;

    public TimedUseAttack(String animation, BiConsumer<LivingEntity, ItemStack> attack) {
        this(animation, false, attack, true);
    }

    public TimedUseAttack(String animation, boolean ignoreAttackSpeed, BiConsumer<LivingEntity, ItemStack> attack, boolean mountedUse) {
        this.animation = animation;
        this.ignoreAttackSpeed = ignoreAttackSpeed;
        this.attack = attack;
        this.mountedUse = mountedUse;
    }

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return AttackAction.create(this.animation, this.ignoreAttackSpeed ? 1 : speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (!entity.level().isClientSide && anim.isAt("attack")) {
            entity.swing(InteractionHand.MAIN_HAND);
            this.attack.accept(entity, stack);
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }

    @Override
    public boolean usableOnMounts(int targetCombo) {
        return this.mountedUse;
    }
}
