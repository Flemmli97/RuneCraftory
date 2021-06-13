package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.PacketHandler;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;

import java.util.ArrayList;
import java.util.List;

public class PermanentEffect extends Effect {

    private int tickDelay;
    private final S2CEntityDataSync.Type packetType;

    public PermanentEffect(EffectType type, int color, S2CEntityDataSync.Type packetType) {
        super(type, color);
        this.tickDelay = 5;
        this.packetType = packetType;
    }

    public PermanentEffect setTickDelay(int value) {
        this.tickDelay = value;
        return this;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % this.tickDelay == 0;
    }

    @Override
    public void performEffect(LivingEntity living, int amplifier) {
        living.removePotionEffect(this);
        living.addPotionEffect(new EffectInstance(this, 2 * this.tickDelay - 1, 0, false, true));
    }

    @Override
    public boolean shouldRender(EffectInstance effect) {
        return false;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entity, AttributeModifierManager manager, int amplifier) {
        sendPacket(entity, this.packetType, true);
        super.applyAttributesModifiersToEntity(entity, manager, amplifier);
    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entity, AttributeModifierManager manager, int amplifier) {
        sendPacket(entity, this.packetType, false);
        super.removeAttributesModifiersFromEntity(entity, manager, amplifier);
    }

    private static void sendPacket(LivingEntity entity, S2CEntityDataSync.Type type, boolean flag) {
        PacketHandler.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getEntityId(), type, flag), entity);
    }
}
