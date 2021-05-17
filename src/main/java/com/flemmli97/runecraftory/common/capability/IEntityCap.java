package com.flemmli97.runecraftory.common.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;

public interface IEntityCap {

    void setSleeping(LivingEntity entity, boolean flag);

    boolean isSleeping();

    void setPoison(LivingEntity entity, boolean flag);

    boolean isPoisoned();

    void setCold(LivingEntity entity, boolean flag);

    boolean hasCold();

    void setParalysis(LivingEntity entity, boolean flag);

    boolean isParalysed();

    void writeAllToPkt(PacketBuffer buffer);

    void readFromPacket(PacketBuffer buffer);
}
