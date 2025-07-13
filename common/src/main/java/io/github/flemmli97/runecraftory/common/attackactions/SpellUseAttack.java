package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.action.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.action.DataKey;
import io.github.flemmli97.runecraftory.api.registry.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.common.attachment.AttackActionHandler;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpellUseAttack extends AttackAction {

    @Override
    public AnimationState getAnimation(LivingEntity entity, int comboIdx) {
        double speed = ItemNBT.attackSpeedModifier(entity);
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
                        player.getCooldowns().addCooldown(stack.getItem(), spell.coolDown());
                    spell.levelSkill(player);
                }
            }
        }
    }

    @Override
    public float movementReduction(AnimationState current) {
        return GeneralConfig.MOVE_SPEED_ATTACK.get().floatValue();
    }
}
