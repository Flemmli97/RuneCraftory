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

import java.util.Comparator;

public class ModAttributes {

    //public static final RegistryEntrySupplier<Attribute RP_MAX;
    private static int id = 0;
    public static final PlatformRegistry<Attribute> ATTRIBUTES = PlatformUtils.INSTANCE.of(Registry.ATTRIBUTE_REGISTRY, RuneCraftory.MODID);
    public static final RegistryEntrySupplier<Attribute> RF_DEFENCE = register(LibAttributes.rf_defence, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> RF_MAGIC = register(LibAttributes.rf_magic, id++, 0, -9999, 9999);
    public static final RegistryEntrySupplier<Attribute> RF_MAGIC_DEFENCE = register(LibAttributes.rf_magic_defence, id++, 0, -9999, 9999);

    public static final RegistryEntrySupplier<Attribute> RFPARA = register(LibAttributes.rf_para, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFPOISON = register(LibAttributes.rf_poison, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFSEAL = register(LibAttributes.rf_seal, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFSLEEP = register(LibAttributes.rf_sleep, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFFAT = register(LibAttributes.rf_fatigue, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFCOLD = register(LibAttributes.rf_cold, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFDIZ = register(LibAttributes.rf_diz, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFCRIT = register(LibAttributes.rf_crit, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFSTUN = register(LibAttributes.rf_stun, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFFAINT = register(LibAttributes.rf_faint, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFDRAIN = register(LibAttributes.rf_drain, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFKNOCK = register(LibAttributes.rf_knock, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESWATER = register(LibAttributes.rf_res_water, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESEARTH = register(LibAttributes.rf_res_earth, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESWIND = register(LibAttributes.rf_res_wind, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESFIRE = register(LibAttributes.rf_res_fire, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESDARK = register(LibAttributes.rf_res_dark, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESLIGHT = register(LibAttributes.rf_res_light, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESLOVE = register(LibAttributes.rf_res_love, id++, 0, -100, 200);
    public static final RegistryEntrySupplier<Attribute> RFRESPARA = register(LibAttributes.rf_res_para, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESPOISON = register(LibAttributes.rf_res_poison, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESSEAL = register(LibAttributes.rf_res_seal, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESSLEEP = register(LibAttributes.rf_res_sleep, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESFAT = register(LibAttributes.rf_res_fatigue, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESCOLD = register(LibAttributes.rf_res_cold, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESDIZ = register(LibAttributes.rf_res_diz, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESCRIT = register(LibAttributes.rf_res_crit, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESSTUN = register(LibAttributes.rf_res_stun, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESFAINT = register(LibAttributes.rf_res_faint, id++, 0, -100, 100);
    public static final RegistryEntrySupplier<Attribute> RFRESDRAIN = register(LibAttributes.rf_res_drain, id++, 0, -100, 100);
    //public static final RegistryEntrySupplier<Attribute RFRLUCK;

    private static RegistryEntrySupplier<Attribute> register(ResourceLocation reg, int id, double base, double min, double max) {
        return ATTRIBUTES.register(reg.getPath(), () -> new OrderedAttribute(reg.getPath(), id, base, min, max));
    }

    public static class OrderedAttribute extends RangedAttribute {

        private final int order;

        public OrderedAttribute(String name, int order, double baseValue, double minValue, double maxValue) {
            super(name, baseValue, minValue, maxValue);
            this.order = order;
        }
    }

    public static final Comparator<Attribute> sorted = (o1, o2) -> {
        if (o1 == Attributes.MAX_HEALTH && o2 != Attributes.MAX_HEALTH)
            return -1;
        if (!(o1 instanceof OrderedAttribute) && !(o2 instanceof OrderedAttribute))
            return PlatformUtils.INSTANCE.attributes().getIDFrom(o1).compareTo(PlatformUtils.INSTANCE.attributes().getIDFrom(o2));
        if (o1 instanceof OrderedAttribute) {
            if (o2 instanceof OrderedAttribute)
                return Integer.compare(((OrderedAttribute) o1).order, ((OrderedAttribute) o2).order);
            return 1;
        }
        return -1;
    };
}
