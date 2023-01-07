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
import java.util.function.Supplier;

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
    public static final RegistryEntrySupplier<Attribute> HEALTHGAIN = registerAdditional(LibAttributes.health_gain, id++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute> RPGAIN = registerAdditional(LibAttributes.rp_gain, id++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final RegistryEntrySupplier<Attribute> RPINCREASE = registerAdditional(LibAttributes.rp_increase, id++, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final RegistryEntrySupplier<Attribute> DEFENCE = registerSyncable(LibAttributes.defence, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> MAGIC = registerSyncable(LibAttributes.magic, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> MAGIC_DEFENCE = registerSyncable(LibAttributes.magic_defence, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> RFPARA = register(LibAttributes.para, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFPOISON = register(LibAttributes.poison, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFSEAL = register(LibAttributes.seal, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFSLEEP = register(LibAttributes.sleep, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFFAT = register(LibAttributes.fatigue, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFCOLD = register(LibAttributes.cold, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFDIZ = register(LibAttributes.diz, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFCRIT = register(LibAttributes.crit, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFSTUN = register(LibAttributes.stun, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFFAINT = register(LibAttributes.faint, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFDRAIN = register(LibAttributes.drain, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFKNOCK = register(LibAttributes.knock, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESWATER = register(LibAttributes.res_water, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESEARTH = register(LibAttributes.res_earth, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESWIND = register(LibAttributes.res_wind, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESFIRE = register(LibAttributes.res_fire, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESDARK = register(LibAttributes.res_dark, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESLIGHT = register(LibAttributes.res_light, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESLOVE = register(LibAttributes.res_love, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESPARA = register(LibAttributes.res_para, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESPOISON = register(LibAttributes.res_poison, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESSEAL = register(LibAttributes.res_seal, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESSLEEP = register(LibAttributes.res_sleep, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESFAT = register(LibAttributes.res_fatigue, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESCOLD = register(LibAttributes.res_cold, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESDIZ = register(LibAttributes.res_diz, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESCRIT = register(LibAttributes.res_crit, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESSTUN = register(LibAttributes.res_stun, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESFAINT = register(LibAttributes.res_faint, id++, 0, -100, 100);
    //public static final RegistryEntrySupplier<Attribute RFRLUCK;
    public static final RegistryEntrySupplier<Attribute> RFRESDRAIN = register(LibAttributes.res_drain, id++, 0, -100, 100);

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
