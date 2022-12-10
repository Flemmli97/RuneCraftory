package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public interface IExtendedMob extends IBaseMob, OwnableEntity {

    TagKey<Item> tamingItem();

    float tamingChance();

    boolean isTamed();

    boolean ridable();

    void setOwner(Player player);

    boolean isFlyingEntity();
}
