package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityFlowerLion extends EntityFlowerLily {

    public EntityFlowerLion(EntityType<? extends EntityFlowerLily> type, Level world) {
        super(type, world);
    }

    @Override
    protected Spell rangedAttackSpell() {
        return ModSpells.TRIPLE_FIRE_BULLET.get();
    }
}
