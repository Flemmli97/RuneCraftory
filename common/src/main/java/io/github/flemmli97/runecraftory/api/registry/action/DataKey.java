package io.github.flemmli97.runecraftory.api.registry.action;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.function.Suppliers;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record DataKey<T>(ResourceLocation id, Supplier<T> defaultValue, BiConsumer<LivingEntity, T> onClear) {

    public static final DataKey<Set<LivingEntity>> HIT_ENTITIES = new DataKey<>(RuneCraftory.MODID, "hit_entities", HashSet::new);
    public static final DataKey<ItemStack> USED_WEAPON = new DataKey<>(RuneCraftory.MODID, "used_weapon", () -> ItemStack.EMPTY);
    public static final DataKey<ToolUseData> TOOL_DATA = new DataKey<>(RuneCraftory.MODID, "tool_data");
    public static final DataKey<Boolean> FIXED_LOOK = new DataKey<>(RuneCraftory.MODID, "fixed_look", () -> false);
    public static final DataKey<Spell> USED_SPELL = new DataKey<>(RuneCraftory.MODID, "used_spell");
    public static final DataKey<Float> SPIN_ROTATION = new DataKey<>(RuneCraftory.MODID, "spin_rotation", () -> 0f);
    public static final DataKey<Vec3> MOVE_DIRECTION = new DataKey<>(RuneCraftory.MODID, "move_direction");
    public static final DataKey<LivingEntity> TARGET = new DataKey<>(RuneCraftory.MODID, "target");
    public static final DataKey<Boolean> GRAVITY = new DataKey<>(RuneCraftory.MODID, "gravity", () -> false, LivingEntity::setNoGravity);

    public DataKey(String namespace, String path) {
        this(namespace, path, Suppliers.nul());
    }

    public DataKey(String namespace, String path, Supplier<T> defaultValue) {
        this(ResourceLocation.fromNamespaceAndPath(namespace, path), defaultValue, null);
    }

    public DataKey(String namespace, String path, Supplier<T> defaultValue, BiConsumer<LivingEntity, T> onClear) {
        this(ResourceLocation.fromNamespaceAndPath(namespace, path), defaultValue, onClear);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataKey<?> dataKey)) return false;
        return Objects.equals(this.id, dataKey.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
}
