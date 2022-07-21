package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.ExtendedEffect;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TrueInvis extends MobEffect implements ExtendedEffect {

    private final List<ItemStack> empty = List.of();

    public TrueInvis() {
        super(MobEffectCategory.NEUTRAL, 0);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return this.empty;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap manager, int amplifier) {
        Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.INVIS, true), entity);
        super.addAttributeModifiers(entity, manager, amplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap manager, int amplifier) {
        Platform.INSTANCE.sendToTrackingAndSelf(new S2CEntityDataSync(entity.getId(), S2CEntityDataSync.Type.INVIS, false), entity);
        super.removeAttributeModifiers(entity, manager, amplifier);
    }
}
