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
    private static int id = 0;

    //For ease of use these are attributes
    //Used for e.g. food
    //RP only applies to players
    public static final RegistryEntrySupplier<Attribute> HEALTHGAIN = registerAdditional(LibAttributes.HEALTH_GAIN, id++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute> RPGAIN = registerAdditional(LibAttributes.RP_GAIN, id++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute> RPINCREASE = registerAdditional(LibAttributes.RP_INCREASE, id++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final RegistryEntrySupplier<Attribute> DEFENCE = registerSyncable(LibAttributes.DEFENCE, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> MAGIC = registerSyncable(LibAttributes.MAGIC, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> MAGIC_DEFENCE = registerSyncable(LibAttributes.MAGIC_DEFENCE, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> RF_PARA = register(LibAttributes.PARA, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_POISON = register(LibAttributes.POISON, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_SEAL = register(LibAttributes.SEAL, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_SLEEP = register(LibAttributes.SLEEP, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_FAT = register(LibAttributes.FATIGUE, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_COLD = register(LibAttributes.COLD, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_DIZ = register(LibAttributes.DIZ, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_CRIT = register(LibAttributes.CRIT, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_STUN = register(LibAttributes.STUN, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_FAINT = register(LibAttributes.FAINT, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_DRAIN = register(LibAttributes.DRAIN, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_KNOCK = register(LibAttributes.KNOCK, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_WATER = register(LibAttributes.RES_WATER, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RF_RES_EARTH = register(LibAttributes.RES_EARTH, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RF_RES_WIND = register(LibAttributes.RES_WIND, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RF_RES_FIRE = register(LibAttributes.RES_FIRE, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RF_RES_DARK = register(LibAttributes.RES_DARK, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RF_RES_LIGHT = register(LibAttributes.RES_LIGHT, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RF_RES_LOVE = register(LibAttributes.RES_LOVE, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RF_RES_PARA = register(LibAttributes.RES_PARA, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_POISON = register(LibAttributes.RES_POISON, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_SEAL = register(LibAttributes.RES_SEAL, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_SLEEP = register(LibAttributes.RES_SLEEP, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_FAT = register(LibAttributes.RES_FATIGUE, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_COLD = register(LibAttributes.RES_COLD, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_DIZ = register(LibAttributes.RES_DIZZY, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_CRIT = register(LibAttributes.RES_CRIT, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_STUN = register(LibAttributes.RES_STUN, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RF_RES_FAINT = register(LibAttributes.RES_FAINT, id++, 0, -100, 100);
    //public static final RegistryEntrySupplier<Attribute RF_RLUCK;
    public static final RegistryEntrySupplier<Attribute> RF_RES_DRAIN = register(LibAttributes.RES_DRAIN, id++, 0, -100, 100);

    /**
     * We use a custom attribute for this to remove other influences like haste etc.
     * Attack speed is in ticks
     */
    public static final RegistryEntrySupplier<Attribute> ATTACK_SPEED = registerSyncable(LibAttributes.ATTACK_SPEED, id++, 5, 0, 1024);
    public static final RegistryEntrySupplier<Attribute> ATTACK_RANGE = registerSyncable(LibAttributes.ATTACK_RANGE, id++, 3, 0, 9999);

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
