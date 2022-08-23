package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SleepEffect extends MobEffect {

    public SleepEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    private static void sendSleepPacket(LivingEntity entity, boolean flag) {
        Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.SLEEP, flag), entity);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (!(living instanceof Player player) || !player.getAbilities().invulnerable) {
            living.setDeltaMovement(new Vec3(living.getDeltaMovement().x, -0.08, -living.getDeltaMovement().z));
        }
        MobEffectInstance eff = living.getEffect(this);
        if (eff.getDuration() > 200) {
            living.removeEffect(this);
            living.addEffect(new MobEffectInstance(this, 200, 0, true, false));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        entity.setSilent(false);
        sendSleepPacket(entity, false);
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        entity.setSilent(true);
        sendSleepPacket(entity, true);
        super.addAttributeModifiers(entity, attributeMap, amplifier);
    }
}
