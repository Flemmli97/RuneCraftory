package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record AttackActionData(OptionalSupplier<Holder<AttackAction>> attackAction) {

    public static final AttackActionData DEFAULT = new AttackActionData(OptionalSupplier.empty());
    public static final Codec<AttackActionData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(OptionalSupplier.codec(ModAttackActions.ATTACK_ACTIONS.registry().holderByNameCodec().optionalFieldOf("attack_action")).forGetter(AttackActionData::attackAction)
            ).apply(instance, AttackActionData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttackActionData> STREAM_CODEC = OptionalSupplier.streamCodec(ByteBufCodecs.holderRegistry(ModAttackActions.ATTACK_ACTION_KEY))
            .map(AttackActionData::new, AttackActionData::attackAction);

    public static <T extends AttackAction> AttackActionData of(RegistryEntrySupplier<AttackAction, T> sup) {
        return new AttackActionData(OptionalSupplier.of(sup::asHolder));
    }
}
