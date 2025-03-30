package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
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
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.STAFF_USE.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (entity.getLevel() instanceof ServerLevel serverLevel && anim.isAt("attack")) {
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
        return false;
    }

    @Override
    public ComboContainer combos() {
        return this.combo;
    }
}
