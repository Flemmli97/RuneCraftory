package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WaterLaserAttack extends AttackAction {

    private final int type;

    public WaterLaserAttack(int type) {
        this.type = type;
    }

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        if (chain == 1)
            return PlayerModelAnimations.WATER_LASER_END.create(speed);
        return switch (this.type) {
            case 2 -> PlayerModelAnimations.WATER_LASER_THREE.create(speed);
            case 1 -> PlayerModelAnimations.WATER_LASER_TWO.create(speed);
            default -> PlayerModelAnimations.WATER_LASER_ONE.create(speed);
        };
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (handler.getChainCount() == 1) {
            if (entity.getLevel() instanceof ServerLevel serverLevel && anim.canAttack()) {
                entity.swing(InteractionHand.MAIN_HAND);
                if (handler.getSpellToCast() != null) {
                    Spell spell = handler.getSpellToCast();
                    if (spell.use(serverLevel, entity, stack) && entity instanceof ServerPlayer player) {
                        spell.levelSkill(player);
                    }
                }
            }
            if (entity instanceof ServerPlayer player && anim.isPastTick(0.4)) {
                if (entity.getUseItem().isEmpty() && Platform.INSTANCE.getPlayerData(player)
                        .map(d -> d.getInv().getInUseStack() != handler.getUsedWeapon()).orElse(false)) {
                    handler.doWeaponAttack(entity, this, handler.getUsedWeapon(), handler.getSpellToCast(), true);
                }
            }
        }
    }

    @Override
    public AttackAction onChange(LivingEntity entity, WeaponHandler handler) {
        if (handler.getChainCount() == 1) {
            if (entity instanceof ServerPlayer player) {
                Spell spell = handler.getSpellToCast();
                ItemStack stack = handler.getUsedWeapon();
                if (stack.getItem() instanceof ItemSpell)
                    player.getCooldowns().addCooldown(stack.getItem(), spell.coolDown());
            }
            return this;
        }
        return null;
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(2, 0);
    }

    @Override
    public boolean disableItemSwitch() {
        return false;
    }
}