package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.WeaponHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SpearUseAttack extends AttackAction {

    private final ComboContainer combo;

    public SpearUseAttack() {
        Predicate<WeaponHandler<?>> MAIN = handler -> handler.matches(state -> state.isPast("attack") && !state.isPast("0.52"));
        ComboContainer.Builder builder = ComboContainer.Builder.builder();
        for (int i = 0; i < 20; i++) {
            builder.addCombo(MAIN);
        }
        this.combo = builder.build();
    }

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (EntityUtils.attackSpeedModifier(entity));
        if (comboIdx > 0) {
            AnimationDefinition definition = PlayerModelAnimations.ANIMS.get(PlayerModelAnimations.SPEAR_USE);
            double offset = definition.marker("chain_offset", 0) * 20;
            return AnimationState.create(definition, 0, AnimationHandler.FALLBACK_TRANSIT_TIME, offset, speed);
        }
        return AttackAction.create(PlayerModelAnimations.SPEAR_USE, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler<?> handler, AnimationState state) {
        if (entity instanceof ServerPlayer serverPlayer && stack.getItem() instanceof ItemSpearBase spear) {
            if (state.isAt("attack")) {
                spear.useSpear(serverPlayer, stack, false);
            }
            if (state.isAt("final")) {
                spear.useSpear(serverPlayer, stack, true);
            }
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
