package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.action.AttackActionHandler;
import io.github.flemmli97.runecraftory.api.action.ComboContainer;
import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WaterLaserAttack extends AttackAction {

    private final ComboContainer combo = ComboContainer.Builder.builder()
            .addCombo(handler -> true, 0)
            .build();

    private final int type;

    public WaterLaserAttack(int type) {
        this.type = type;
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int comboIdx) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (comboIdx == 1)
            return PlayerModelAnimations.WATER_LASER_END.create(speed);
        return switch (this.type) {
            case 2 -> PlayerModelAnimations.WATER_LASER_THREE.create(speed);
            case 1 -> PlayerModelAnimations.WATER_LASER_TWO.create(speed);
            default -> PlayerModelAnimations.WATER_LASER_ONE.create(speed);
        };
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, AttackActionHandler handler, AnimatedAction anim) {
        if (handler.getComboCount() == 1) {
            if (entity.level() instanceof ServerLevel serverLevel && anim.isAt("attack")) {
                entity.swing(InteractionHand.MAIN_HAND);
                if (handler.get(DataKey.USED_SPELL) != null) {
                    Spell spell = handler.get(DataKey.USED_SPELL);
                    if (spell.use(serverLevel, entity, stack) && entity instanceof ServerPlayer player) {
                        spell.levelSkill(player);
                    }
                }
            }
            if (!entity.level().isClientSide) {
                if (anim.isPast("continue")) {
                    if (!(entity instanceof ServerPlayer player) || entity.getUseItem().isEmpty() && Platform.INSTANCE.getPlayerData(player).getInv().getInUseStack() != handler.get(DataKey.USED_WEAPON)) {
                        handler.doWeaponAttack(this, handler.get(DataKey.USED_WEAPON), handler.get(DataKey.USED_SPELL));
                    }
                }
            }
        }
    }

    @Override
    public AttackAction onChange(LivingEntity entity, AttackActionHandler handler) {
        if (handler.getComboCount() == 1) {
            if (entity instanceof ServerPlayer player) {
                Spell spell = handler.get(DataKey.USED_SPELL);
                ItemStack stack = handler.get(DataKey.USED_WEAPON);
                if (stack.getItem() instanceof ItemSpell)
                    player.getCooldowns().addCooldown(stack.getItem(), spell.coolDown());
            }
            return this;
        }
        return null;
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