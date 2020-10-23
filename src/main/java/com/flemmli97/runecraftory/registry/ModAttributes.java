package com.flemmli97.runecraftory.registry;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.libs.LibAttributes;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.Set;

@Mod.EventBusSubscriber(modid = RuneCraftory.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {

    //public static Attribute RP_MAX;

    public static Attribute RF_DEFENCE;
    public static Attribute RF_MAGIC;
    public static Attribute RF_MAGIC_DEFENCE;

    public static Attribute RFPARA;
    public static Attribute RFPOISON;
    public static Attribute RFSEAL;
    public static Attribute RFSLEEP;
    public static Attribute RFFAT;
    public static Attribute RFCOLD;
    public static Attribute RFDIZ;
    public static Attribute RFCRIT;
    public static Attribute RFSTUN;
    public static Attribute RFFAINT;
    public static Attribute RFDRAIN;
    public static Attribute RFKNOCK;
    public static Attribute RFRESWATER;
    public static Attribute RFRESEARTH;
    public static Attribute RFRESWIND;
    public static Attribute RFRESFIRE;
    public static Attribute RFRESDARK;
    public static Attribute RFRESLIGHT;
    public static Attribute RFRESLOVE;
    public static Attribute RFRESPOISON;
    public static Attribute RFRESSLEEP;
    public static Attribute RFRESFAT;
    public static Attribute RFRESCOLD;
    public static Attribute RFRESPARA;
    public static Attribute RFRESSEAL;
    public static Attribute RFRESCRIT;
    public static Attribute RFRESDIZ;
    public static Attribute RFRESSTUN;
    public static Attribute RFRESFAINT;
    public static Attribute RFRESDRAIN;
    //public static Attribute RFRLUCK;

    public static Set<Attribute> modAttributes;
    @SubscribeEvent
    public static void reg(RegistryEvent.Register<Attribute> event) {
        int id = 0;
        modAttributes = Sets.newHashSet();
        event.getRegistry().registerAll(
                RF_DEFENCE = register(LibAttributes.rf_defence, id++, 0, -9999, 9999),
                RF_MAGIC = register(LibAttributes.rf_magic, id++, 0, -9999, 9999),
                RF_MAGIC_DEFENCE = register(LibAttributes.rf_magic_defence, id++, 0, -9999, 9999),

                RFPARA = register(LibAttributes.rf_para, id++, 0, -100, 100),
                RFPOISON = register(LibAttributes.rf_poison, id++, 0, -100, 100),
                RFSEAL = register(LibAttributes.rf_seal, id++, 0, -100, 100),
                RFSLEEP = register(LibAttributes.rf_sleep, id++, 0, -100, 100),
                RFFAT = register(LibAttributes.rf_fatigue, id++, 0, -100, 100),
                RFCOLD = register(LibAttributes.rf_cold, id++, 0, -100, 100),
                RFDIZ = register(LibAttributes.rf_diz, id++, 0, -100, 100),
                RFCRIT = register(LibAttributes.rf_crit, id++, 0, -100, 100),
                RFSTUN = register(LibAttributes.rf_stun, id++, 0, -100, 100),
                RFFAINT = register(LibAttributes.rf_faint, id++, 0, -100, 100),
                RFDRAIN = register(LibAttributes.rf_drain, id++, 0, -100, 100),
                RFKNOCK = register(LibAttributes.rf_knock, id++, 0, -100, 100),

                RFRESWATER = register(LibAttributes.rf_res_water, id++, 0, -100, 200),
                RFRESEARTH = register(LibAttributes.rf_res_earth, id++, 0, -100, 200),
                RFRESWIND = register(LibAttributes.rf_res_wind, id++, 0, -100, 200),
                RFRESFIRE = register(LibAttributes.rf_res_fire, id++, 0, -100, 200),
                RFRESDARK = register(LibAttributes.rf_res_dark, id++, 0, -100, 200),
                RFRESLIGHT = register(LibAttributes.rf_res_light, id++, 0, -100, 200),
                RFRESLOVE = register(LibAttributes.rf_res_love, id++, 0, -100, 200),

                RFRESPARA = register(LibAttributes.rf_res_para, id++, 0, -100, 100),
                RFRESPOISON = register(LibAttributes.rf_res_poison, id++, 0, -100, 100),
                RFRESSEAL = register(LibAttributes.rf_res_seal, id++, 0, -100, 100),
                RFRESSLEEP = register(LibAttributes.rf_res_sleep, id++, 0, -100, 100),
                RFRESFAT = register(LibAttributes.rf_res_fatigue, id++, 0, -100, 100),
                RFRESCOLD = register(LibAttributes.rf_res_cold, id++, 0, -100, 100),
                RFRESDIZ = register(LibAttributes.rf_res_diz, id++, 0, -100, 100),
                RFRESCRIT = register(LibAttributes.rf_res_crit, id++, 0, -100, 100),
                RFRESSTUN = register(LibAttributes.rf_res_stun, id++, 0, -100, 100),
                RFRESFAINT = register(LibAttributes.rf_res_faint, id++, 0, -100, 100),
                RFRESDRAIN = register(LibAttributes.rf_res_drain, id++, 0, -100, 100)
        );
    }

    private static Attribute register(ResourceLocation reg, int id, double base, double min, double max) {
        Attribute att = new OrderedAttribute(reg.getPath(), id, base, min, max).setRegistryName(reg);
        modAttributes.add(att);
        return att;
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
        if (o1 instanceof OrderedAttribute) {
            if (o2 instanceof OrderedAttribute)
                return Integer.compare(((OrderedAttribute) o1).order, ((OrderedAttribute) o2).order);
            return 1;
        }
        return o1.getRegistryName().compareTo(o2.getRegistryName());
    };
}
