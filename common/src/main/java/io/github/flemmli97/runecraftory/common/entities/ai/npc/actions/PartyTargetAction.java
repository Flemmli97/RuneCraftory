package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Optional;
import java.util.function.Supplier;

public class PartyTargetAction implements NPCAction {

    public static final Codec<PartyTargetAction> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(CodecHelper.ofCustomRegistry(ModSpells.SPELL_REGISTRY, ModSpells.SPELL_REGISTRY_KEY).fieldOf("spell").forGetter(d -> d.spell),
                    Codec.BOOL.fieldOf("ignoreSeal").forGetter(d -> d.ignoreSeal),
                    NPCAction.optionalCooldown(d -> d.cooldown)
            ).apply(instance, PartyTargetAction::new));

    private final Spell spell;
    private final boolean ignoreSeal;
    private final NumberProvider cooldown;


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private PartyTargetAction(Spell spell, boolean ignoreSeal, Optional<NumberProvider> cooldown) {
        this(spell, ignoreSeal, cooldown.orElse(NPCAction.CONST_ZERO));
    }

    public PartyTargetAction(Spell spell, boolean ignoreSeal) {
        this(spell, ignoreSeal, NPCAction.CONST_ZERO);
    }

    public PartyTargetAction(Spell spell, boolean ignoreSeal, NumberProvider cooldown) {
        this.spell = spell;
        this.ignoreSeal = ignoreSeal;
        this.cooldown = cooldown;
    }

    @Override
    public Supplier<NPCActionCodec> codec() {
        return ModNPCActions.PARTY_TARGET_ACTION;
    }

    @Override
    public int getDuration(EntityNPCBase npc) {
        return 50;
    }

    @Override
    public int getCooldown(EntityNPCBase npc) {
        return this.cooldown.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public AttackAction getAction(EntityNPCBase npc) {
        return ModAttackActions.STAFF_USE.get();
    }

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, AttackAction action) {
        if (npc.followEntity() == null)
            return true;
        npc.getLookControl().setLookAt(npc.followEntity(), 360, 90);
        return true;
    }
}
