package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.ExtendedEffect;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PermanentEffect extends MobEffect implements ExtendedEffect {

    private int tickDelay;
    private final S2CEntityDataSync.Type packetType;
    private final List<ItemStack> empty = List.of();

    public PermanentEffect(MobEffectCategory type, int color, S2CEntityDataSync.Type packetType) {
        super(type, color);
        this.tickDelay = 5;
        this.packetType = packetType;
    }

    public PermanentEffect setTickDelay(int value) {
        this.tickDelay = value;
        return this;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % this.tickDelay == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        living.removeEffect(this);
        living.addEffect(new MobEffectInstance(this, 2 * this.tickDelay - 1, 0, false, false, false));
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return this.empty;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap manager, int amplifier) {
        sendPacket(entity, this.packetType, true);
        super.addAttributeModifiers(entity, manager, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap manager, int amplifier) {
        sendPacket(entity, this.packetType, false);
        super.removeAttributeModifiers(entity, manager, amplifier);
    }

    private static void sendPacket(LivingEntity entity, S2CEntityDataSync.Type type, boolean flag) {
        Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), type, flag), entity);
    }
}
