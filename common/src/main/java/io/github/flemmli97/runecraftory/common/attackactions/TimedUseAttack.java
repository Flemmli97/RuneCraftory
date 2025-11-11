package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
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
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        return AttackAction.create(this.animation, this.ignoreAttackSpeed ? 1 : speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (!entity.level().isClientSide && state.isAt("attack")) {
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
