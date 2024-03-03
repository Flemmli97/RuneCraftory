package io.github.flemmli97.runecraftory.common.entities.ai.npc.actions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.api.action.AttackAction;
import io.github.flemmli97.runecraftory.common.entities.ai.npc.NPCAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.utils.CodecHelper;
import io.github.flemmli97.tenshilib.platform.registry.CustomRegistryEntry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface NPCAction {

    NumberProvider CONST_ZERO = ConstantValue.exactly(0);
    NumberProvider CONST_SEC = ConstantValue.exactly(20);

    static Optional<NumberProvider> asOpt(NumberProvider val, NumberProvider def) {
        return val.equals(def) ? Optional.empty() : Optional.of(val);
    }

    static <T> RecordCodecBuilder<T, Optional<NumberProvider>> optionalCooldown(Function<T, NumberProvider> getter) {
        Function<T, Optional<NumberProvider>> optGetter = t -> {
            NumberProvider provider = getter.apply(t);
            if (provider.equals(CONST_ZERO))
                return Optional.empty();
            return Optional.of(provider);
        };
        return CodecHelper.NUMER_PROVIDER_CODEC.optionalFieldOf("cooldown").forGetter(optGetter);
    }

    static LootContext createLootContext(EntityNPCBase npc) {
        return new LootContext.Builder((ServerLevel) npc.getLevel()).withParameter(LootContextParams.THIS_ENTITY, npc).withParameter(LootContextParams.ORIGIN, npc.position()).withRandom(npc.getRandom()).create(LootContextParamSets.ADVANCEMENT_ENTITY);
    }

    Supplier<NPCActionCodec> codec();

    int getDuration(EntityNPCBase npc);

    int getCooldown(EntityNPCBase npc);

    AttackAction getAction(EntityNPCBase npc);

    default Spell getSpell() {
        return null;
    }

    boolean doAction(EntityNPCBase npc, NPCAttackGoal<?> goal, @Nullable AttackAction action);

    //Wrapper needed for registries on forge
    class NPCActionCodec extends CustomRegistryEntry<NPCActionCodec> {

        public final Codec<NPCAction> codec;

        @SuppressWarnings("unchecked")
        public NPCActionCodec(Codec<? extends NPCAction> codec) {
            this.codec = (Codec<NPCAction>) codec;
        }
    }
}
