package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class ModAttributes {

    public static final LoaderRegister<Attribute> ATTRIBUTES = LoaderRegistryAccess.INSTANCE.of(Registries.ATTRIBUTE, RuneCraftory.MODID);

    public static final Collection<RegistryEntrySupplier<Attribute, ?>> ENTITY_ATTRIBUTES = new ArrayList<>();
    public static final Collection<RegistryEntrySupplier<Attribute, ?>> PLAYER_ATTRIBUTES = new ArrayList<>();

    public static final Comparator<Holder<Attribute>> SORTED = (h1, h2) -> {
        if (h1.value() == Attributes.MAX_HEALTH.value() && h2.value() != Attributes.MAX_HEALTH.value())
            return -1;
        if (h1.value() != Attributes.MAX_HEALTH.value() && h2.value() == Attributes.MAX_HEALTH.value())
            return 1;
        if (h1.value() instanceof OrderedAttribute o1) {
            if (h2.value() instanceof OrderedAttribute o2)
                return Integer.compare(o1.order, o2.order);
            return 1;
        } else if ((!(h2.value() instanceof OrderedAttribute))) {
            return h1.getRegisteredName().compareTo(h2.getRegisteredName());
        }
        return -1;
    };

    private static int ID = 0;

    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> MAX_RUNEPOINTS = registerPlayerSyncable("max_runepoints", ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    //For ease of use these are attributes
    //Used for e.g. food
    //RP only applies to players
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> HEALTH_GAIN = registerAdditional("health_gain", ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RUNE_POINTS_GAIN = registerAdditional("rune_points_gain", ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DEFENCE = registerSyncable("defence", ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> MAGIC_ATTACK = registerSyncable("magic_attack", ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> MAGIC_DEFENCE = registerSyncable("magic_defence", ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> PARALYSIS = register("paralysis", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> POISON = register("poison", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> SEAL = register("seal", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> SLEEP = register("sleep", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> FATIGUE = register("fatigue", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> COLD = register("cold", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DIZZY = register("dizzy", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> CRITICAL = register("critical", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> STUN = register("stun", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> FAINT = register("faint", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DRAIN = register("drain", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> WATER_RESISTANCE = register("water_resistance", ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> EARTH_RESISTANCE = register("earth_resistance", ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> WIND_RESISTANCE = register("wind_resistance", ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> FIRE_RESISTANCE = register("fire_resistance", ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DARK_RESISTANCE = register("dark_resistance", ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> LIGHT_RESISTANCE = register("light_resistance", ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> LOVE_RESISTANCE = register("love_resistance", ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> PARALYSIS_RESISTANCE = register("paralysis_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> POISON_RESISTANCE = register("poison_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> SEAL_RESISTANCE = register("seal_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> SLEEP_RESISTANCE = register("sleep_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> FATIGUE_RESISTANCE = register("fatigue_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> COLD_RESISTANCE = register("cold_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DIZZY_RESISTANCE = register("dizzy_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> CRITICAL_RESISTANCE = register("critical_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> STUN_RESISTANCE = register("stun_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> FAINT_RESISTANCE = register("faint_resistance", ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DRAIN_RESISTANCE = register("drain_resistance", ID++, 0, -100, 100);

    /**
     * Use a custom attribute for this to remove other influences like haste etc.
     * Attack speed is in ticks
     */
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> ATTACK_SPEED = registerPlayerSyncable("attack_speed", ID++, 1, 0, 2);
    /**
     * Use a custom attribute for this to remove other influences from other sources.
     */
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> ATTACK_RANGE = registerPlayerSyncable("attack_range", ID++, 3, 0, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> ATTACK_WIDTH = registerPlayerSyncable("attack_width", ID++, 0, 0, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> CHARGE_TIME = registerPlayerSyncable("charge_time", ID++, 20, 0, 9999);

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> register(String name, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute, OrderedAttribute> sup = ATTRIBUTES.register(name, res -> new OrderedAttribute("attribute." + res, id, base, min, max));
        ENTITY_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> registerSyncable(String name, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute, OrderedAttribute> sup = ATTRIBUTES.register(name, res -> (OrderedAttribute) new OrderedAttribute("attribute." + res, id, base, min, max).setSyncable(true));
        ENTITY_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> registerPlayerSyncable(String name, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute, OrderedAttribute> sup = ATTRIBUTES.register(name, res -> (OrderedAttribute) new OrderedAttribute("attribute." + res, id, base, min, max).setSyncable(true));
        PLAYER_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> registerAdditional(String name, int id, double base, double min, double max) {
        return ATTRIBUTES.register(name, res -> new OrderedAttribute("attribute." + res, id, base, min, max));
    }

    public static class OrderedAttribute extends RangedAttribute {

        private final int order;

        public OrderedAttribute(String name, int order, double baseValue, double minValue, double maxValue) {
            super(name, baseValue, minValue, maxValue);
            this.order = order;
        }
    }
}
