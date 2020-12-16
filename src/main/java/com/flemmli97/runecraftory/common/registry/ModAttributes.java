package com.flemmli97.runecraftory.common.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.lib.LibAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = RuneCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {

    //public static final RegistryObject<Attribute RP_MAX;
    private static int id = 0;
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, RuneCraftory.MODID);
    public static final RegistryObject<Attribute> RF_DEFENCE = register(LibAttributes.rf_defence, id++, 0, -9999, 9999);
    public static final RegistryObject<Attribute> RF_MAGIC = register(LibAttributes.rf_magic, id++, 0, -9999, 9999);
    public static final RegistryObject<Attribute> RF_MAGIC_DEFENCE = register(LibAttributes.rf_magic_defence, id++, 0, -9999, 9999);

    public static final RegistryObject<Attribute> RFPARA = register(LibAttributes.rf_para, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFPOISON = register(LibAttributes.rf_poison, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFSEAL = register(LibAttributes.rf_seal, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFSLEEP = register(LibAttributes.rf_sleep, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFFAT = register(LibAttributes.rf_fatigue, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFCOLD = register(LibAttributes.rf_cold, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFDIZ = register(LibAttributes.rf_diz, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFCRIT = register(LibAttributes.rf_crit, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFSTUN = register(LibAttributes.rf_stun, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFFAINT = register(LibAttributes.rf_faint, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFDRAIN = register(LibAttributes.rf_drain, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFKNOCK = register(LibAttributes.rf_knock, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESWATER = register(LibAttributes.rf_res_water, id++, 0, -100, 200);
    public static final RegistryObject<Attribute> RFRESEARTH = register(LibAttributes.rf_res_earth, id++, 0, -100, 200);
    public static final RegistryObject<Attribute> RFRESWIND = register(LibAttributes.rf_res_wind, id++, 0, -100, 200);
    public static final RegistryObject<Attribute> RFRESFIRE = register(LibAttributes.rf_res_fire, id++, 0, -100, 200);
    public static final RegistryObject<Attribute> RFRESDARK = register(LibAttributes.rf_res_dark, id++, 0, -100, 200);
    public static final RegistryObject<Attribute> RFRESLIGHT = register(LibAttributes.rf_res_light, id++, 0, -100, 200);
    public static final RegistryObject<Attribute> RFRESLOVE = register(LibAttributes.rf_res_love, id++, 0, -100, 200);
    public static final RegistryObject<Attribute> RFRESPARA = register(LibAttributes.rf_res_para, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESPOISON = register(LibAttributes.rf_res_poison, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESSEAL = register(LibAttributes.rf_res_seal, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESSLEEP = register(LibAttributes.rf_res_sleep, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESFAT = register(LibAttributes.rf_res_fatigue, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESCOLD = register(LibAttributes.rf_res_cold, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESDIZ = register(LibAttributes.rf_res_diz, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESCRIT = register(LibAttributes.rf_res_crit, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESSTUN = register(LibAttributes.rf_res_stun, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESFAINT = register(LibAttributes.rf_res_faint, id++, 0, -100, 100);
    public static final RegistryObject<Attribute> RFRESDRAIN = register(LibAttributes.rf_res_drain, id++, 0, -100, 100);
    //public static final RegistryObject<Attribute RFRLUCK;

    private static RegistryObject<Attribute> register(ResourceLocation reg, int id, double base, double min, double max) {
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
        if (o1 == Attributes.GENERIC_MAX_HEALTH)
            return -1;
        if(!(o1 instanceof OrderedAttribute) && !(o2 instanceof OrderedAttribute))
            return o1.getRegistryName().compareTo(o2.getRegistryName());
        if (o1 instanceof OrderedAttribute) {
            if (o2 instanceof OrderedAttribute)
                return Integer.compare(((OrderedAttribute) o1).order, ((OrderedAttribute) o2).order);
            return 1;
        }
        return -1;
    };
}
