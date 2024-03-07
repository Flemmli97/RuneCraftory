package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.function.Supplier;

public class SpellAttackAction implements NPCAction {

    public static final Codec<SpellAttackAction> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(CodecHelper.NUMER_PROVIDER_CODEC.fieldOf("walkTime").forGetter(d -> d.walkTime),
                    NPCAction.optionalCooldown(d -> d.cooldown),

                    CodecHelper.ofCustomRegistry(ModSpells.SPELL_REGISTRY, ModSpells.SPELL_REGISTRY_KEY).fieldOf("spell").forGetter(d -> d.spell),
                    Codec.DOUBLE.fieldOf("range").forGetter(d -> d.range),
                    Codec.BOOL.fieldOf("ignoreSeal").forGetter(d -> d.ignoreSeal)
            ).apply(instance, (walkTime, cooldown, spell, range, ignoreSeal) -> new SpellAttackAction(spell, range, ignoreSeal, walkTime, cooldown.orElse(NPCAction.CONST_ZERO))));

    private final Spell spell;
    private final double range;
    private final boolean ignoreSeal;
    private final NumberProvider walkTime;
    private final NumberProvider cooldown;

    public SpellAttackAction(Spell spell, double range, boolean ignoreSeal, NumberProvider walkTime, NumberProvider cooldown) {
        this.spell = spell;
        this.range = range;
        this.ignoreSeal = ignoreSeal;
        this.walkTime = walkTime;
        this.cooldown = cooldown;
    }

    @Override
    public Supplier<NPCActionCodec> codec() {
        return ModNPCActions.SPELL_ATTACK;
    }

    @Override
    public int getDuration(EntityNPCBase npc) {
        return this.walkTime.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public int getCooldown(EntityNPCBase npc) {
        return this.cooldown.getInt(NPCAction.createLootContext(npc));
    }

    @Override
    public AttackAction getAction(EntityNPCBase npc) {
        return this.spell.useAction();
    }

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, AttackAction action) {
        goal.moveToEntityNearer(goal.getAttackTarget(), 1);
        npc.getLookControl().setLookAt(goal.getAttackTarget(), 60, 30);
        return goal.canSeeTarget() && (this.range < 0 || goal.getDistSqr() <= this.range * this.range);
    }
}
