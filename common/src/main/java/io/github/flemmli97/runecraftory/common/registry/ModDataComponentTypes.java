package io.github.flemmli97.runecraftory.common.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.components.ArmorEffectData;
import io.github.flemmli97.runecraftory.common.components.AttackActionData;
import io.github.flemmli97.runecraftory.common.components.BabyData;
import io.github.flemmli97.runecraftory.common.components.FoodAttributeData;
import io.github.flemmli97.runecraftory.common.components.ItemAttributeData;
import io.github.flemmli97.runecraftory.common.components.ItemStackHolder;
import io.github.flemmli97.runecraftory.common.components.ListItemStackHolder;
import io.github.flemmli97.runecraftory.common.components.NPCSpawnData;
import io.github.flemmli97.runecraftory.common.components.StaffData;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.items.ToolItemTier;
import io.github.flemmli97.runecraftory.common.items.creative.ItemDebug;
import io.github.flemmli97.runecraftory.common.items.creative.TreasureChestSpawnegg;
import io.github.flemmli97.runecraftory.common.utils.StreamCodecUtils;
import io.github.flemmli97.tenshilib.common.utils.CodecUtils;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;

import java.util.UUID;

public class ModDataComponentTypes {

    public static final LoaderRegister<DataComponentType<?>> DATA_COMPONENTS = LoaderRegistryAccess.INSTANCE.of(Registries.DATA_COMPONENT_TYPE, RuneCraftory.MODID);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ArmorEffectData>> ARMOR_EFFECT = register("armor_effects", ArmorEffectData.CODEC, ArmorEffectData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<StaffData>> STAFF = register("staff", StaffData.CODEC, StaffData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ItemElement>> ELEMENT = register("element", CodecUtils.stringEnumCodec(ItemElement.class, null), StreamCodecUtils.ofEnum(ItemElement.class));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Integer>> LEVEL = register("level", ExtraCodecs.intRange(0, 10), ByteBufCodecs.INT);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ItemAttributeData>> STATS = register("stats", ItemAttributeData.CODEC, ItemAttributeData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<FoodAttributeData>> FOOD_BUFF = register("food_buff", FoodAttributeData.CODEC, FoodAttributeData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ListItemStackHolder>> UPGRADES = register("upgrades", ListItemStackHolder.CODEC, ListItemStackHolder.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ListItemStackHolder>> CRAFTING_BONUS = register("crafting_bonus", ListItemStackHolder.CODEC, ListItemStackHolder.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ItemStackHolder>> ORIGINAL_ITEM = register("original_item", ItemStackHolder.CODEC, ItemStackHolder.STREAM_CODEC);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Integer>> WATER = register("water", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.INT);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Integer>> MAX_WATER = register("max_water", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.INT);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Float>> SHIELD_EFFICIENCY = register("shield_efficiency", Codec.FLOAT.validate((val) -> val >= 0 && val <= 1 ? DataResult.success(val) : DataResult.error(() -> "Value must be between (0,1)")), ByteBufCodecs.FLOAT);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<AttackActionData>> ATTACK_ACTION = register("attack_action", AttackActionData.CODEC, AttackActionData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ToolItemTier>> TOOL_TIER = register("tool_tier", CodecUtils.stringEnumCodec(ToolItemTier.class, null), StreamCodecUtils.ofEnum(ToolItemTier.class));

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> MAGNIFYING_GLASS = register("magnifying_glass", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> SCRAP_METAL_PLUS = register("scrap_metal_plus", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> DRAGON_SCALE = register("dragon_scale", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> INVISIBLE = register("invisible", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> OBJECT_X = register("object_x", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> DOUBLE_STEEL = register("double_steel", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> TENFOLD_STEEL = register("tenfold_steel", Codec.BOOL, ByteBufCodecs.BOOL);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Boolean>> LIGHT_ORE = register("light_ore", Codec.BOOL, ByteBufCodecs.BOOL);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ItemDebug.Mode>> DEBUG_ITEM_MODE = register("debug_item_mode",
            CodecUtils.ordinalEnumCodec(ItemDebug.Mode.class, null), StreamCodecUtils.ofEnum(ItemDebug.Mode.class));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<UUID>> SELECTED_UUID = register("selected_uuid", UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<String>> SELECTED_ANIMATION = register("selected_animation", Codec.STRING, ByteBufCodecs.STRING_UTF8);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Integer>> SPAWN_EGG_LEVEL = register("spawn_egg_level", ExtraCodecs.POSITIVE_INT, ByteBufCodecs.INT);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<TreasureChestSpawnegg.ChestTier>> SPAWN_EGG_CHEST_TIER = register("spawn_egg_chest_tier", CodecUtils.stringEnumCodec(TreasureChestSpawnegg.ChestTier.class, null), StreamCodecUtils.ofEnum(TreasureChestSpawnegg.ChestTier.class));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<NPCSpawnData>> NPC_SPAWN_DATA = register("npc_spawn_data", NPCSpawnData.CODEC, NPCSpawnData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<BabyData>> BABY_DATA = register("baby_data", BabyData.CODEC, BabyData.STREAM_CODEC);

    private static <T> RegistryEntrySupplier<DataComponentType<?>, DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DATA_COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
    }
}
