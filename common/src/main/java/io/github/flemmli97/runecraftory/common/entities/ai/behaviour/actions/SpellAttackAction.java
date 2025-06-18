package io.github.flemmli97.runecraftory.common.entities.ai.behaviour.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModNPCActions;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

public class SpellAttackAction implements NPCAction {

    public static final MapCodec<SpellAttackAction> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(NumberProviders.CODEC.fieldOf("walk_time").forGetter(d -> d.walkTime),
                    NPCAction.optionalNumCooldown(d -> d.cooldown),
                    NPCAction.optionalNum(d -> d.combos, "combos", CONST_ONE),

                    ModSpells.SPELLS.registry().byNameCodec().fieldOf("spell").forGetter(d -> d.spell),
                    Codec.DOUBLE.fieldOf("range").forGetter(d -> d.range),
                    Codec.BOOL.fieldOf("ignore_seal").forGetter(d -> d.ignoreSeal)
            ).apply(instance, (walkTime, cooldown, combos, spell, range, ignoreSeal) -> new SpellAttackAction(spell, range, ignoreSeal, walkTime, cooldown.orElse(NPCAction.CONST_ZERO), combos.orElse(NPCAction.CONST_ONE))));

    private final Spell spell;
    private final double range;
    private final boolean ignoreSeal;
    private final NumberProvider walkTime;
    private final NumberProvider cooldown;
    private final NumberProvider combos;

    public SpellAttackAction(Spell spell, double range, boolean ignoreSeal, NumberProvider walkTime, NumberProvider cooldown) {
        this(spell, range, ignoreSeal, walkTime, cooldown, ConstantValue.exactly(1));
    }

    public SpellAttackAction(Spell spell, double range, boolean ignoreSeal, NumberProvider walkTime, NumberProvider cooldown, NumberProvider combos) {
        this.spell = spell;
        this.range = range;
        this.ignoreSeal = ignoreSeal;
        this.walkTime = walkTime;
        this.cooldown = cooldown;
        this.combos = combos;
    }

    @Override
    public MapCodec<SpellAttackAction> codec() {
        return ModNPCActions.SPELL_ATTACK.get();
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
    public NPCAttackAction getAction(EntityNPCBase npc) {
        AttackAction act = this.spell.useAction();
        int combos = Mth.clamp(this.combos.getInt(NPCAction.createLootContext(npc)), 1, act.combos().size());
        return new NPCAttackAction(act, combos);
    }

    @Override
    public Spell getSpell() {
        return this.spell;
    }

    @Override
    public boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, NPCAttackAction action) {
        goal.moveToEntityNearer(goal.getAttackTarget(), 1);
        npc.getLookControl().setLookAt(goal.getAttackTarget(), 60, 30);
        return goal.canSeeTarget() && (this.range < 0 || goal.getDistSqr() <= this.range * this.range);
    }
}
