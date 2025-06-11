package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpearBase;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SpearUseAttack extends AttackAction {

    private final ComboContainer combo;

    public SpearUseAttack() {
        Predicate<AttackActionHandler> MAIN = handler -> handler.getAnimation().isPast("attack") && !handler.getAnimation().isPast("0.52");
        ComboContainer.Builder builder = ComboContainer.Builder.builder();
        for (int i = 0; i < 20; i++) {
            builder.addCombo(MAIN);
        }
        this.combo = builder.build();
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (comboIdx > 0) {
            float offset = (float) (PlayerModelAnimations.SPEAR_USE.getMarker("chain_offset", 0) * 20);
            return PlayerModelAnimations.SPEAR_USE.create(1, AnimationHandler.FALLBACK_TRANSIT_TIME, offset, speed);
        }
        return PlayerModelAnimations.SPEAR_USE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        if (entity instanceof ServerPlayer serverPlayer && stack.getItem() instanceof ItemSpearBase spear) {
            if (anim.isAt("attack")) {
                spear.useSpear(serverPlayer, stack, false);
            }
            if (anim.isAt("final")) {
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
