package io.github.flemmli97.runecraftory.api.registry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

public interface NPCAction {

    NumberProvider CONST_ZERO = ConstantValue.exactly(0);
    NumberProvider CONST_ONE = ConstantValue.exactly(1);
    NumberProvider CONST_SEC = ConstantValue.exactly(20);

    static Optional<NumberProvider> asOpt(NumberProvider val, NumberProvider def) {
        return val.equals(def) ? Optional.empty() : Optional.of(val);
    }

    static <T> RecordCodecBuilder<T, Optional<NumberProvider>> optionalNumCooldown(Function<T, NumberProvider> getter) {
        return optionalNum(getter, "cooldown", CONST_ZERO);
    }

    static <T> RecordCodecBuilder<T, Optional<NumberProvider>> optionalNum(Function<T, NumberProvider> getter, String field, NumberProvider def) {
        Function<T, Optional<NumberProvider>> optGetter = t -> {
            NumberProvider provider = getter.apply(t);
            if (provider.equals(def))
                return Optional.empty();
            return Optional.of(provider);
        };
        return NumberProviders.CODEC.optionalFieldOf(field).forGetter(optGetter);
    }

    static LootContext createLootContext(EntityNPCBase npc) {
        LootParams.Builder builder = new LootParams.Builder((ServerLevel) npc.level())
                .withParameter(LootContextParams.THIS_ENTITY, npc).withParameter(LootContextParams.ORIGIN, npc.position());
        return new LootContext.Builder(builder.create(LootContextParamSets.ADVANCEMENT_ENTITY)).withOptionalRandomSource(npc.getRandom())
                .create(Optional.empty());
    }

    MapCodec<? extends NPCAction> codec();

    int getDuration(EntityNPCBase npc);

    int getCooldown(EntityNPCBase npc);

    default NPCAttackAction getAction(EntityNPCBase npc) {
        return null;
    }

    default Spell getSpell() {
        return null;
    }

    boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, @Nullable NPCAttackAction action);

    record NPCAttackAction(AttackAction action, int comboCount) {

        public static NPCAttackAction of(AttackAction action) {
            return new NPCAttackAction(action, 1);
        }
    }
}
