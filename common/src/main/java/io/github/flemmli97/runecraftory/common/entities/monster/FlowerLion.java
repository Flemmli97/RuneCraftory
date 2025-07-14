package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FlowerLion extends FlowerLily {

    public FlowerLion(EntityType<? extends FlowerLily> type, Level world) {
        super(type, world);
    }

    @Override
    protected Spell rangedAttackSpell() {
        return RuneCraftorySpells.TRIPLE_FIRE_BULLET.get();
    }
}
