package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FireballUseAttack extends AttackAction {

    private final ComboContainer combo;

    public FireballUseAttack(boolean big) {
        ComboContainer.Builder builder = ComboContainer.Builder.builder()
                .addCombo(handler -> handler.getAnimation().isPast("attack"), 4);
        if (!big)
            builder.addCombo(handler -> handler.getAnimation().isPast("attack"), 4);
        this.combo = builder.build();
    }

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = EntityUtils.attackSpeedModifier(entity);
        return AttackAction.create(PlayerModelAnimations.STAFF_USE, speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimationState anim) {
        if (entity.level() instanceof ServerLevel serverLevel && anim.isAt("attack")) {
            entity.swing(InteractionHand.MAIN_HAND);
            if (handler.get(DataKey.USED_SPELL) != null) {
                Spell spell = handler.get(DataKey.USED_SPELL);
                if (spell.use(serverLevel, entity, stack) && entity instanceof ServerPlayer player) {
                    if (stack.getItem() instanceof ItemSpell)
                        player.getCooldowns().addCooldown(stack.getItem(), spell.properties().cooldown());
                    spell.levelSkill(player);
                }
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
