package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record StaffData(Optional<Holder<Spell>> tier1, Optional<Holder<Spell>> tier2, Optional<Holder<Spell>> tier3,
                        int chargeTime) {

    public static final StaffData DEFAULT = new StaffData(Optional.empty(), Optional.empty(), Optional.empty(), 0);
    public static final Codec<StaffData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ModSpells.SPELLS.registry().holderByNameCodec().optionalFieldOf("first_spell").forGetter(StaffData::tier1),
                    ModSpells.SPELLS.registry().holderByNameCodec().optionalFieldOf("second_spell").forGetter(StaffData::tier2),
                    ModSpells.SPELLS.registry().holderByNameCodec().optionalFieldOf("third_spell").forGetter(StaffData::tier3),
                    Codec.INT.fieldOf("charge_time").forGetter(d -> d.chargeTime)
            ).apply(instance, StaffData::new));
    private static final StreamCodec<RegistryFriendlyByteBuf, Optional<Holder<Spell>>> SPELL_CODEC = ByteBufCodecs.optional(
            ByteBufCodecs.holderRegistry(ModSpells.SPELLS.registry().key()));
    public static final StreamCodec<RegistryFriendlyByteBuf, StaffData> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public StaffData decode(RegistryFriendlyByteBuf buf) {
            return new StaffData(SPELL_CODEC.decode(buf),
                    SPELL_CODEC.decode(buf),
                    SPELL_CODEC.decode(buf),
                    buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, StaffData component) {
            SPELL_CODEC.encode(buf, component.tier1());
            SPELL_CODEC.encode(buf, component.tier2());
            SPELL_CODEC.encode(buf, component.tier3());
            buf.writeInt(component.chargeTime);
        }
    };

    public StaffData setTier1Spell(@Nullable Holder<Spell> spell) {
        return new StaffData(Optional.ofNullable(spell), this.tier2, this.tier3, spell != null ? spell.value().coolDown() : this.chargeTime);
    }

    public StaffData setTier2Spell(@Nullable Holder<Spell> spell) {
        return new StaffData(this.tier1, Optional.ofNullable(spell), this.tier3, spell != null && this.tier1.isEmpty() ? spell.value().coolDown() : this.chargeTime);
    }

    public StaffData setTier3Spell(@Nullable Holder<Spell> spell) {
        return new StaffData(this.tier1, this.tier2, Optional.ofNullable(spell), spell != null && this.tier1.isEmpty() && this.tier2.isEmpty() ? spell.value().coolDown() : this.chargeTime);
    }

    public Spell fromChargeLevel(ItemStack stack, int level) {
        Spell spell = switch (level) {
            case 3 -> this.tier3.map(Holder::value).orElse(null);
            case 2 -> this.tier2.map(Holder::value).orElse(null);
            case 1 -> this.tier1.map(Holder::value).orElse(null);
            default -> null;
        };
        if (spell == null) {
            return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).flatMap(stat -> switch (level) {
                case 3 -> stat.getTier3Spell();
                case 2 -> stat.getTier2Spell();
                case 1 -> stat.getTier1Spell();
                default -> Optional.empty();
            }).map(Holder::value).orElse(null);
        }
        return spell;
    }

    public int getChargeLevel() {
        if (this.tier3.isPresent())
            return 3;
        if (this.tier2.isPresent())
            return 2;
        if (this.tier1.isPresent())
            return 1;
        return 0;
    }

    @Override
    public int chargeTime() {
        return this.chargeTime == 0 ? (int) ModAttributes.CHARGE_TIME.get().getDefaultValue() : this.chargeTime;
    }
}
