package io.github.flemmli97.runecraftory.common.effects;

import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.network.S2CEntityDataSync;
import io.github.flemmli97.runecraftory.platform.ExtendedEffect;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class SyncedMobEffect extends MobEffect implements ExtendedEffect {

    private final S2CEntityDataSync.DataType packetType;

    public SyncedMobEffect(MobEffectCategory type, int color, S2CEntityDataSync.DataType packetType) {
        super(type, color);
        this.packetType = packetType;
    }

    private static void sendPacket(LivingEntity entity, S2CEntityDataSync.DataType type, boolean flag) {
        EntityData data = Platform.INSTANCE.getEntityData(entity);
        switch (type) {
            case POISON -> data.setPoison(entity, flag);
            case SLEEP -> data.setSleeping(entity, flag);
            case PARALYSIS -> data.setParalysis(entity, flag);
            case COLD -> data.setCold(entity, flag);
            case INVIS -> data.setInvis(entity, flag);
            case ORTHOVIEW -> data.setOrthoView(entity, flag);
        }
    }

    @Override
    public void onEffectAdded(LivingEntity entity, MobEffectInstance instance) {
        sendPacket(entity, this.packetType, true);
    }

    @Override
    public void onEffectRemoved(LivingEntity entity, MobEffectInstance instance) {
        sendPacket(entity, this.packetType, false);
    }
}
