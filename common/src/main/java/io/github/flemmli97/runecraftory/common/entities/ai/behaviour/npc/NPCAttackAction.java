package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.npc;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;

import java.util.Optional;

public record NPCAttackAction(AttackAction action, int comboCount, Optional<Spell> spell) {

    public static NPCAttackAction of(AttackAction action) {
        return new NPCAttackAction(action, 1, Optional.empty());
    }
}
