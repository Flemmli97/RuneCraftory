package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public class TimedUseAttack extends AttackAction {

    private final AnimatedAction animation;
    private final boolean ignoreAttackSpeed;

    private final BiConsumer<LivingEntity, ItemStack> attack;

    public TimedUseAttack(AnimatedAction animation, BiConsumer<LivingEntity, ItemStack> attack) {
        this(animation, false, attack);
    }

    public TimedUseAttack(AnimatedAction animation, boolean ignoreAttackSpeed, BiConsumer<LivingEntity, ItemStack> attack) {
        this.animation = animation;
        this.ignoreAttackSpeed = ignoreAttackSpeed;
        this.attack = attack;
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        if (!this.ignoreAttackSpeed)
            return this.animation.create(1);
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return this.animation.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.isAt("attack")) {
            entity.swing(InteractionHand.MAIN_HAND);
            this.attack.accept(entity, stack);
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }
}
