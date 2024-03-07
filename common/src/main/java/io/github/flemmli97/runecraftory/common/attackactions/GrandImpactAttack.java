package io.github.flemmli97.runecraftory.common.attackactions;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.action.PlayerModelAnimations;
import io.github.flemmli97.runecraftory.api.action.WeaponHandler;
import io.github.flemmli97.runecraftory.common.items.weapons.ItemAxeBase;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class GrandImpactAttack extends AttackAction {

    @Override
    public AnimatedAction getAnimation(LivingEntity entity, int chain) {
        float speed = (float) (ItemNBT.attackSpeedModifier(entity));
        return PlayerModelAnimations.GRAND_IMPACT.create(speed);
    }

    @Override
    public void run(LivingEntity entity, ItemStack stack, WeaponHandler handler, AnimatedAction anim) {
        if (!entity.level.isClientSide && (anim.isAtTick(0.4) || anim.isAtTick(1.08))) {
            float reach = (float) entity.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
            Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(4, 1), entity);
            entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, entity.getSoundSource(), 1.0f, 1.0f);
            CombatUtils.applyTempAttribute(entity, Attributes.ATTACK_DAMAGE, CombatUtils.getAbilityDamageBonus(stack));
            ItemAxeBase.performRightClickAction(stack, entity, reach + 1, 0.1f);
            CombatUtils.removeTempAttribute(entity, Attributes.ATTACK_DAMAGE);
        }
    }

    @Override
    public boolean canOverride(LivingEntity entity, WeaponHandler handler) {
        return handler.getCurrentAnim().isPastTick(0.48) && !handler.getCurrentAnim().isPastTick(0.72);
    }

    @Override
    public AttackChain attackChain(LivingEntity entity, int chain) {
        return new AttackChain(4, 0);
    }
}
