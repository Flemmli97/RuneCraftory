package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.registry.ArmorEffect;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public record ArmorEffectData(@Nullable ArmorEffect first, @Nullable ArmorEffect second, @Nullable ArmorEffect third,
                              int appendingIndex) {

    public static final ArmorEffectData DEFAULT = new ArmorEffectData(null, null, null, 0);
    public static final Codec<ArmorEffectData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ModArmorEffects.ARMOR_EFFECTS.registry().byNameCodec().optionalFieldOf("first_effect").forGetter(d -> Optional.ofNullable(d.first())),
                    ModArmorEffects.ARMOR_EFFECTS.registry().byNameCodec().optionalFieldOf("second_effect").forGetter(d -> Optional.ofNullable(d.second())),
                    ModArmorEffects.ARMOR_EFFECTS.registry().byNameCodec().optionalFieldOf("third_effect").forGetter(d -> Optional.ofNullable(d.third())),
                    Codec.INT.fieldOf("idx").forGetter(d -> d.appendingIndex)
            ).apply(instance, (f, s, t, idx) -> new ArmorEffectData(f.orElse(null),
                    s.orElse(null), t.orElse(null), idx)));
    private static final StreamCodec<RegistryFriendlyByteBuf, ArmorEffect> ARMOR_EFFECTS_CODEC = ByteBufCodecs.registry(ModArmorEffects.ARMOR_EFFECTS.registry().key());
    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorEffectData> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public ArmorEffectData decode(RegistryFriendlyByteBuf buf) {
            return new ArmorEffectData(FriendlyByteBuf.readNullable(buf, ARMOR_EFFECTS_CODEC),
                    FriendlyByteBuf.readNullable(buf, ARMOR_EFFECTS_CODEC),
                    FriendlyByteBuf.readNullable(buf, ARMOR_EFFECTS_CODEC),
                    buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ArmorEffectData component) {
            FriendlyByteBuf.writeNullable(buf, component.first(), ARMOR_EFFECTS_CODEC);
            FriendlyByteBuf.writeNullable(buf, component.second(), ARMOR_EFFECTS_CODEC);
            FriendlyByteBuf.writeNullable(buf, component.third(), ARMOR_EFFECTS_CODEC);
            buf.writeInt(component.appendingIndex());
        }
    };

    public ArmorEffectData add(ArmorEffect effect) {
        if (effect != null) {
            int next = (this.appendingIndex + 1) % 3;
            return switch (this.appendingIndex) {
                case 2 -> new ArmorEffectData(this.first, this.second, effect, next);
                case 1 -> new ArmorEffectData(this.first, effect, this.third, next);
                default -> new ArmorEffectData(effect, this.second, this.third, next);
            };
        }
        return this;
    }

    public void triggerEvent(ItemStack stack, Consumer<ArmorEffect> consumer) {
        if (this.first == null) {
            ArmorEffect defaultEffect = DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::getArmorEffect).orElse(null);
            if (defaultEffect != null && defaultEffect.canBeAppliedTo(stack))
                consumer.accept(defaultEffect);
        } else {
            consumer.accept(this.first);
            if (this.second != null)
                consumer.accept(this.second);
            if (this.third != null)
                consumer.accept(this.third);
        }
    }
}
