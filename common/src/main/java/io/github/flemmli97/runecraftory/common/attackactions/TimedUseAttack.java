package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class TimedUseAttack extends AttackAction {

    private final Supplier<AnimatedAction> animation;

    private final BiConsumer<LivingEntity, ItemStack> attack;

    public TimedUseAttack(Supplier<AnimatedAction> animation, BiConsumer<LivingEntity, ItemStack> attack) {
        this.animation = animation;
        this.attack = attack;
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        return this.animation.get();
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && anim.canAttack()) {
            entity.swing(InteractionHand.MAIN_HAND);
            this.attack.accept(entity, stack);
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }
}
