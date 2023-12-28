package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SpellUseAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STAFF_USE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (entity.getLevel() instanceof ServerLevel serverLevel && anim.canAttack()) {
            entity.swing(InteractionHand.MAIN_HAND);
            if (handler.getSpellToCast() != null) {
                Spell spell = handler.getSpellToCast();
                if (spell.use(serverLevel, entity, stack) && entity instanceof ServerPlayer player) {
                    if (stack.getItem() instanceof ItemSpell)
                        player.getCooldowns().addCooldown(stack.getItem(), spell.coolDown());
                    spell.levelSkill(player);
                }
            }
        }
    }

    @Override
    public boolean disableItemSwitch() {
        return true;
    }
}
