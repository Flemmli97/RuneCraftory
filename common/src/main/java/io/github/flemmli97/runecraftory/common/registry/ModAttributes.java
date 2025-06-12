package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
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
        if (h1.value() == Attributes.MAX_HEALTH && h2.value() != Attributes.MAX_HEALTH)
            return -1;
        if (h1.value() != Attributes.MAX_HEALTH && h2.value() == Attributes.MAX_HEALTH)
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
    //public static final RegistryEntrySupplier<Attribute RP_MAX;
    private static int ID = 0;

    //For ease of use these are attributes
    //Used for e.g. food
    //RP only applies to players
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> HEALTHGAIN = registerAdditional(LibAttributes.HEALTH_GAIN, ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RPGAIN = registerAdditional(LibAttributes.RP_GAIN, ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RPINCREASE = registerAdditional(LibAttributes.RP_INCREASE, ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DEFENCE = registerSyncable(LibAttributes.DEFENCE, ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> MAGIC = registerSyncable(LibAttributes.MAGIC, ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> MAGIC_DEFENCE = registerSyncable(LibAttributes.MAGIC_DEFENCE, ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> PARA = register(LibAttributes.PARA, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> POISON = register(LibAttributes.POISON, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> SEAL = register(LibAttributes.SEAL, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> SLEEP = register(LibAttributes.SLEEP, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> FATIGUE = register(LibAttributes.FATIGUE, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> COLD = register(LibAttributes.COLD, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DIZZY = register(LibAttributes.DIZZY, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> CRIT = register(LibAttributes.CRIT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> STUN = register(LibAttributes.STUN, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> KNOCK = register(LibAttributes.KNOCK, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> FAINT = register(LibAttributes.FAINT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> DRAIN = register(LibAttributes.DRAIN, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_WATER = register(LibAttributes.RES_WATER, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_EARTH = register(LibAttributes.RES_EARTH, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_WIND = register(LibAttributes.RES_WIND, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_FIRE = register(LibAttributes.RES_FIRE, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_DARK = register(LibAttributes.RES_DARK, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_LIGHT = register(LibAttributes.RES_LIGHT, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_LOVE = register(LibAttributes.RES_LOVE, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_PARA = register(LibAttributes.RES_PARA, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_POISON = register(LibAttributes.RES_POISON, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_SEAL = register(LibAttributes.RES_SEAL, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_SLEEP = register(LibAttributes.RES_SLEEP, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_FAT = register(LibAttributes.RES_FATIGUE, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_COLD = register(LibAttributes.RES_COLD, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_DIZZY = register(LibAttributes.RES_DIZZY, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_CRIT = register(LibAttributes.RES_CRIT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_STUN = register(LibAttributes.RES_STUN, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_FAINT = register(LibAttributes.RES_FAINT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> RES_DRAIN = register(LibAttributes.RES_DRAIN, ID++, 0, -100, 100);

    /**
     * We use a custom attribute for this to remove other influences like haste etc.
     * Attack speed is in ticks
     */
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> ATTACK_SPEED = registerPlayerSyncable(LibAttributes.ATTACK_SPEED, ID++, 1, 0, 2);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> ATTACK_RANGE = registerPlayerSyncable(LibAttributes.ATTACK_RANGE, ID++, 3, 0, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> ATTACK_WIDTH = registerPlayerSyncable(LibAttributes.ATTACK_WIDTH, ID++, 0, 0, 9999);
    public static final RegistryEntrySupplier<Attribute, OrderedAttribute> CHARGE_TIME = registerPlayerSyncable(LibAttributes.CHARGE_TIME, ID++, 20, 0, 9999);

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> register(ResourceLocation reg, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute, OrderedAttribute> sup = ATTRIBUTES.register(reg.getPath(), () -> new OrderedAttribute("attribute." + reg, id, base, min, max));
        ENTITY_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> registerSyncable(ResourceLocation reg, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute, OrderedAttribute> sup = ATTRIBUTES.register(reg.getPath(), () -> (OrderedAttribute) new OrderedAttribute("attribute." + reg, id, base, min, max).setSyncable(true));
        ENTITY_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> registerPlayerSyncable(ResourceLocation reg, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute, OrderedAttribute> sup = ATTRIBUTES.register(reg.getPath(), () -> (OrderedAttribute) new OrderedAttribute("attribute." + reg, id, base, min, max).setSyncable(true));
        PLAYER_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute, OrderedAttribute> registerAdditional(ResourceLocation reg, int id, double base, double min, double max) {
        return ATTRIBUTES.register(reg.getPath(), () -> new OrderedAttribute("attribute." + reg, id, base, min, max));
    }

    public static class OrderedAttribute extends RangedAttribute {

        private final int order;

        public OrderedAttribute(String name, int order, double baseValue, double minValue, double maxValue) {
            super(name, baseValue, minValue, maxValue);
            this.order = order;
        }
    }
}
