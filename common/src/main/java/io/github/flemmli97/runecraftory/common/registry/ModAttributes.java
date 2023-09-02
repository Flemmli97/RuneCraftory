package io.github.flemmli97.runecraftory.common.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.LibAttributes;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class ModAttributes {

    public static final PlatformRegistry<Attribute> ATTRIBUTES = PlatformUtils.INSTANCE.of(Registry.ATTRIBUTE_REGISTRY, RuneCraftory.MODID);

    public static final Collection<RegistryEntrySupplier<Attribute>> ENTITY_ATTRIBUTES = new ArrayList<>();

    public static final Comparator<Attribute> SORTED = (o1, o2) -> {
        if (o1 == Attributes.MAX_HEALTH && o2 != Attributes.MAX_HEALTH)
            return -1;
        if (o1 != Attributes.MAX_HEALTH && o2 == Attributes.MAX_HEALTH)
            return 1;
        if (!(o1 instanceof OrderedAttribute) && !(o2 instanceof OrderedAttribute))
            return PlatformUtils.INSTANCE.attributes().getIDFrom(o1).compareTo(PlatformUtils.INSTANCE.attributes().getIDFrom(o2));
        if (o1 instanceof OrderedAttribute) {
            if (o2 instanceof OrderedAttribute)
                return Integer.compare(((OrderedAttribute) o1).order, ((OrderedAttribute) o2).order);
            return 1;
        }
        return -1;
    };
    //public static final RegistryEntrySupplier<Attribute RP_MAX;
    private static int ID = 0;

    //For ease of use these are attributes
    //Used for e.g. food
    //RP only applies to players
    public static final RegistryEntrySupplier<Attribute> HEALTHGAIN = registerAdditional(LibAttributes.HEALTH_GAIN, ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute> RPGAIN = registerAdditional(LibAttributes.RP_GAIN, ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute> RPINCREASE = registerAdditional(LibAttributes.RP_INCREASE, ID++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final RegistryEntrySupplier<Attribute> DEFENCE = registerSyncable(LibAttributes.DEFENCE, ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> MAGIC = registerSyncable(LibAttributes.MAGIC, ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> MAGIC_DEFENCE = registerSyncable(LibAttributes.MAGIC_DEFENCE, ID++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> PARA = register(LibAttributes.PARA, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> POISON = register(LibAttributes.POISON, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> SEAL = register(LibAttributes.SEAL, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> SLEEP = register(LibAttributes.SLEEP, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> FATIGUE = register(LibAttributes.FATIGUE, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> COLD = register(LibAttributes.COLD, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> DIZZY = register(LibAttributes.DIZZY, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> CRIT = register(LibAttributes.CRIT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> STUN = register(LibAttributes.STUN, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> FAINT = register(LibAttributes.FAINT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> DRAIN = register(LibAttributes.DRAIN, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> KNOCK = register(LibAttributes.KNOCK, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_WATER = register(LibAttributes.RES_WATER, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RES_EARTH = register(LibAttributes.RES_EARTH, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RES_WIND = register(LibAttributes.RES_WIND, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RES_FIRE = register(LibAttributes.RES_FIRE, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RES_DARK = register(LibAttributes.RES_DARK, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RES_LIGHT = register(LibAttributes.RES_LIGHT, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RES_LOVE = register(LibAttributes.RES_LOVE, ID++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RES_PARA = register(LibAttributes.RES_PARA, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_POISON = register(LibAttributes.RES_POISON, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_SEAL = register(LibAttributes.RES_SEAL, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_SLEEP = register(LibAttributes.RES_SLEEP, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_FAT = register(LibAttributes.RES_FATIGUE, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_COLD = register(LibAttributes.RES_COLD, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_DIZZY = register(LibAttributes.RES_DIZZY, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_CRIT = register(LibAttributes.RES_CRIT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_STUN = register(LibAttributes.RES_STUN, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_FAINT = register(LibAttributes.RES_FAINT, ID++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RES_DRAIN = register(LibAttributes.RES_DRAIN, ID++, 0, -100, 100);

    /**
     * We use a custom attribute for this to remove other influences like haste etc.
     * Attack speed is in ticks
     */
    public static final RegistryEntrySupplier<Attribute> ATTACK_SPEED = registerSyncable(LibAttributes.ATTACK_SPEED, ID++, 1, 0, 5);
    public static final RegistryEntrySupplier<Attribute> ATTACK_RANGE = registerSyncable(LibAttributes.ATTACK_RANGE, ID++, 3, 0, 9999);

    private static RegistryEntrySupplier<Attribute> register(ResourceLocation reg, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute> sup = ATTRIBUTES.register(reg.getPath(), () -> new OrderedAttribute(reg.toString(), id, base, min, max));
        ENTITY_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute> registerSyncable(ResourceLocation reg, int id, double base, double min, double max) {
        RegistryEntrySupplier<Attribute> sup = ATTRIBUTES.register(reg.getPath(), () -> new OrderedAttribute(reg.toString(), id, base, min, max).setSyncable(true));
        ENTITY_ATTRIBUTES.add(sup);
        return sup;
    }

    private static RegistryEntrySupplier<Attribute> registerAdditional(ResourceLocation reg, int id, double base, double min, double max) {
        return ATTRIBUTES.register(reg.getPath(), () -> new OrderedAttribute(reg.toString(), id, base, min, max));
    }

    public static class OrderedAttribute extends RangedAttribute {

        private final int order;

        public OrderedAttribute(String name, int order, double baseValue, double minValue, double maxValue) {
            super(name, baseValue, minValue, maxValue);
            this.order = order;
        }
    }
}
