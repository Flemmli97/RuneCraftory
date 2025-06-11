package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.components.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.components.ItemAttributeData;
import io.github.flemmli97.runecraftory.common.components.ItemStackHolder;
import io.github.flemmli97.runecraftory.common.components.ListItemStackHolder;
import io.github.flemmli97.runecraftory.common.components.StaffData;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;

public class ModDataComponentTypes {

    public static final LoaderRegister<DataComponentType<?>> DATA_COMPONENTS = LoaderRegistryAccess.INSTANCE.of(Registries.DATA_COMPONENT_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ArmorEffectData>> ARMOR_EFFECT = register("armor_effects", ArmorEffectData.CODEC, ArmorEffectData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<StaffData>> STAFF = register("staff", StaffData.CODEC, StaffData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<EnumElement>> ELEMENT = register("element", CodecUtils.stringEnumCodec(EnumElement.class, null), ofEnum(EnumElement.class));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Integer>> LEVEL = register("level", ExtraCodecs.intRange(0, 10), ByteBufCodecs.INT);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ItemAttributeData>> STATS = register("stats", ItemAttributeData.CODEC, ItemAttributeData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ItemAttributeData>> FOOD_BUFF = register("food_buff", ItemAttributeData.CODEC, ItemAttributeData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ListItemStackHolder>> UPGRADES = register("upgrades", ListItemStackHolder.CODEC, ListItemStackHolder.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ListItemStackHolder>> CRAFTING_BONUS = register("crafting_bonus", ListItemStackHolder.CODEC, ListItemStackHolder.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ItemStackHolder>> ORIGINAL_ITEM = register("original_item", ItemStackHolder.CODEC, ItemStackHolder.STREAM_CODEC);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Integer>> WATER = register("water", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.INT);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> MAGNIFYING_GLASS = register("magnifying_glass", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> SCRAP_METAL_PLUS = register("scrap_metal_plus", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> DRAGON_SCALE = register("dragon_scale", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> GLITTA_AUGITE = register("glitta_augite", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> RACCOON_LEAF = register("raccoon_leaf", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> INVISIBLE = register("invisible", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> OBJECT_X = register("object_x", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> DOUBLE_STEEL = register("double_Steel", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> TENFOLD_STEEL = register("tenfold_Steel", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> LIGHT_ORE = register("light_ore", Codec.BOOL, ByteBufCodecs.BOOL);

    private static <T> RegistryEntrySupplier<DataComponentType<?>, DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DATA_COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
    }

    private static <T extends Enum<T>> StreamCodec<ByteBuf, T> ofEnum(Class<T> clss) {
        return ByteBufCodecs.idMapper(i -> clss.getEnumConstants()[i], Enum::ordinal);
    }
}
