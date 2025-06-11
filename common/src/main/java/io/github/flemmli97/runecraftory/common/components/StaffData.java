package io.github.flemmli97.runecraftory.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record StaffData(@Nullable Spell tier1, @Nullable Spell tier2, @Nullable Spell tier3, int chargeTime) {

    public static final StaffData DEFAULT = new StaffData(null, null, null, 0);
    public static final Codec<StaffData> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(ModSpells.SPELLS.registry().byNameCodec().optionalFieldOf("first_spell").forGetter(d -> Optional.ofNullable(d.tier1())),
                    ModSpells.SPELLS.registry().byNameCodec().optionalFieldOf("second_spell").forGetter(d -> Optional.ofNullable(d.tier2())),
                    ModSpells.SPELLS.registry().byNameCodec().optionalFieldOf("third_spell").forGetter(d -> Optional.ofNullable(d.tier3())),
                    Codec.INT.fieldOf("charge_time").forGetter(d -> d.chargeTime)
            ).apply(instance, (f, s, t, idx) -> new StaffData(f.orElse(null),
                    s.orElse(null), t.orElse(null), idx)));
    private static final StreamCodec<RegistryFriendlyByteBuf, Spell> SPELL_CODEC = ByteBufCodecs.registry(ModSpells.SPELLS.registry().key());
    public static final StreamCodec<RegistryFriendlyByteBuf, StaffData> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public StaffData decode(RegistryFriendlyByteBuf buf) {
            return new StaffData(FriendlyByteBuf.readNullable(buf, SPELL_CODEC),
                    FriendlyByteBuf.readNullable(buf, SPELL_CODEC),
                    FriendlyByteBuf.readNullable(buf, SPELL_CODEC),
                    buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, StaffData component) {
            FriendlyByteBuf.writeNullable(buf, component.tier1(), SPELL_CODEC);
            FriendlyByteBuf.writeNullable(buf, component.tier2(), SPELL_CODEC);
            FriendlyByteBuf.writeNullable(buf, component.tier3(), SPELL_CODEC);
            buf.writeInt(component.chargeTime);
        }
    };

    public StaffData setTier1Spell(Spell spell) {
        return new StaffData(spell, this.tier2, this.tier3, spell != null ? spell.coolDown() : this.chargeTime);
    }

    public StaffData setTier2Spell(Spell spell) {
        return new StaffData(this.tier1, spell, this.tier3, spell != null && this.tier1 == null ? spell.coolDown() : this.chargeTime);
    }

    public StaffData setTier3Spell(Spell spell) {
        return new StaffData(this.tier1, this.tier2, spell, spell != null && this.tier1 == null && this.tier2 == null ? spell.coolDown() : this.chargeTime);
    }

    public Spell fromChargeLevel(ItemStack stack, int level) {
        Spell spell = switch (level) {
            case 3 -> this.tier3;
            case 2 -> this.tier2;
            case 1 -> this.tier1;
            default -> null;
        };
        if (spell == null) {
            return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(stat -> switch (level) {
                case 3 -> stat.getTier3Spell();
                case 2 -> stat.getTier2Spell();
                case 1 -> stat.getTier1Spell();
                default -> null;
            }).orElse(null);
        }
        return spell;
    }

    public int getChargeLevel() {
        if (this.tier3 != null)
            return 3;
        if (this.tier2 != null)
            return 2;
        if (this.tier1 != null)
            return 1;
        return 0;
    }

    @Override
    public int chargeTime() {
        return this.chargeTime == 0 ? (int) ModAttributes.CHARGE_TIME.get().getDefaultValue() : this.chargeTime;
    }
}
