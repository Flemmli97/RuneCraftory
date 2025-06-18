package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Consumer;

public record ArmorEffectData(Optional<Holder<ArmorEffect>> first, Optional<Holder<ArmorEffect>> second,
                              Optional<Holder<ArmorEffect>> third,
                              int appendingIndex) {

    public static final ArmorEffectData DEFAULT = new ArmorEffectData(null, null, null, 0);
    public static final Codec<ArmorEffectData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ModArmorEffects.ARMOR_EFFECTS.registry().holderByNameCodec().optionalFieldOf("first_effect").forGetter(ArmorEffectData::first),
                    ModArmorEffects.ARMOR_EFFECTS.registry().holderByNameCodec().optionalFieldOf("second_effect").forGetter(ArmorEffectData::second),
                    ModArmorEffects.ARMOR_EFFECTS.registry().holderByNameCodec().optionalFieldOf("third_effect").forGetter(ArmorEffectData::third),
                    Codec.INT.fieldOf("idx").forGetter(d -> d.appendingIndex)
            ).apply(instance, ArmorEffectData::new));
    private static final StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<ArmorEffect>>> ARMOR_EFFECTS_CODEC = ByteBufCodecs.optional(
            ByteBufCodecs.holderRegistry(ModArmorEffects.ARMOR_EFFECTS.registry().key()));
    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorEffectData> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public ArmorEffectData decode(RegistryFriendlyByteBuf buf) {
            return new ArmorEffectData(ARMOR_EFFECTS_CODEC.decode(buf),
                    ARMOR_EFFECTS_CODEC.decode(buf),
                    ARMOR_EFFECTS_CODEC.decode(buf),
                    buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ArmorEffectData component) {
            ARMOR_EFFECTS_CODEC.encode(buf, component.first());
            ARMOR_EFFECTS_CODEC.encode(buf, component.second());
            ARMOR_EFFECTS_CODEC.encode(buf, component.third());
            buf.writeInt(component.appendingIndex());
        }
    };

    public ArmorEffectData add(Holder<ArmorEffect> effect) {
        if (effect != null) {
            int next = (this.appendingIndex + 1) % 3;
            return switch (this.appendingIndex) {
                case 2 -> new ArmorEffectData(this.first, this.second, Optional.of(effect), next);
                case 1 -> new ArmorEffectData(this.first, Optional.of(effect), this.third, next);
                default -> new ArmorEffectData(Optional.of(effect), this.second, this.third, next);
            };
        }
        return this;
    }

    public void triggerEvent(ItemStack stack, Consumer<ArmorEffect> consumer) {
        if (this.first.isEmpty()) {
            DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).flatMap(ItemStat::getArmorEffect).ifPresent(effect -> {
                if (effect.value().canBeAppliedTo(stack)) {
                    consumer.accept(effect.value());
                }
            });
        } else {
            consumer.accept(this.first.get().value());
            this.second.ifPresent(eff -> consumer.accept(eff.value()));
            this.third.ifPresent(eff -> consumer.accept(eff.value()));
        }
    }
}
