package com.flemmli97.runecraftory.api.entities;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public interface IRFAttributes {

    public static final IAttribute RFATTACK = (new RangedAttribute((IAttribute)null, "rf.attack", 0.0D, -99999.0D, 99999.0D)).setDescription("Attack").setShouldWatch(true); 
    public static final IAttribute RFDEFENCE = (new RangedAttribute((IAttribute)null, "rf.defence", 0.0D, -99999.0D, 99999.0D)).setDescription("Defence").setShouldWatch(true); 
    public static final IAttribute RFMAGICATT = (new RangedAttribute((IAttribute)null, "rf.magicAtt", 0.0D, -99999.0D, 99999.0D)).setDescription("Magic Attack").setShouldWatch(true); 
    public static final IAttribute RFMAGICDEF = (new RangedAttribute((IAttribute)null, "rf.magicDef", 0.0D, -99999.0D, 99999.0D)).setDescription("Magic Defence").setShouldWatch(true); 

    //=====Percents
    public static final IAttribute RFPOISON = (new RangedAttribute((IAttribute)null, "rf.poison", 0.0D, 0.0D, 100.0D)).setDescription("Poison Chance").setShouldWatch(true); 
    public static final IAttribute RFSLEEP = (new RangedAttribute((IAttribute)null, "rf.sleep", 0.0D, 0.0D, 100.0D)).setDescription("Sleep Chance").setShouldWatch(true); 
    public static final IAttribute RFFAT = (new RangedAttribute((IAttribute)null, "rf.fatigue", 0.0D, 0.0D, 100.0D)).setDescription("Fatigue Chance").setShouldWatch(true); 
    public static final IAttribute RFCOLD = (new RangedAttribute((IAttribute)null, "rf.cold", 0.0D, 0.0D, 100.0D)).setDescription("Cold Chance").setShouldWatch(true); 
    public static final IAttribute RFPARA = (new RangedAttribute((IAttribute)null, "rf.paralysis", 0.0D, 0.0D, 100.0D)).setDescription("Paralysis Chance").setShouldWatch(true); 
    public static final IAttribute RFSEAL = (new RangedAttribute((IAttribute)null, "rf.seal", 0.0D, 0.0D, 100.0D)).setDescription("Seal Chance").setShouldWatch(true); 
    
    public static final IAttribute RFDIZ = (new RangedAttribute((IAttribute)null, "rf.diz", 0.0D, -1000.0D, 1000.0D)).setDescription("Dizziness Chance").setShouldWatch(true); 
    public static final IAttribute RFCRIT = (new RangedAttribute((IAttribute)null, "rf.crit", 0.0D, 0.0D, 100.0D)).setDescription("Critical Chance").setShouldWatch(true); 
    public static final IAttribute RFSTUN = (new RangedAttribute((IAttribute)null, "rf.stun", 0.0D, 0.0D, 100.0D)).setDescription("Stun Chance").setShouldWatch(true); 
    public static final IAttribute RFFAINT = (new RangedAttribute((IAttribute)null, "rf.faint", 0.0D, 0.0D, 100.0D)).setDescription("Faint Chance").setShouldWatch(true); 
    public static final IAttribute RFDRAIN = (new RangedAttribute((IAttribute)null, "rf.drain", 0.0D, 0.0D, 100.0D)).setDescription("Drain Chance").setShouldWatch(true); 
    public static final IAttribute RFKNOCK = (new RangedAttribute((IAttribute)null, "rf.knock", 0.0D, 0.0D, 100.0D)).setDescription("Knockback Chance").setShouldWatch(true); 

    public static final IAttribute RFRESWATER = (new RangedAttribute((IAttribute)null, "rf.resWater", 0.0D, -100.0D, 200.0D)).setDescription("Water Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESEARTH = (new RangedAttribute((IAttribute)null, "rf.resEarth", 0.0D, -100.0D, 200.0D)).setDescription("Earth Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESWIND = (new RangedAttribute((IAttribute)null, "rf.resWind", 0.0D, -100.0D, 200.0D)).setDescription("Wind Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESFIRE = (new RangedAttribute((IAttribute)null, "rf.resFire", 0.0D, -100.0D, 200.0D)).setDescription("Fire Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESDARK = (new RangedAttribute((IAttribute)null, "rf.resDark", 0.0D, -100.0D, 200.0D)).setDescription("Dark Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESLIGHT = (new RangedAttribute((IAttribute)null, "rf.resLight", 0.0D, -100.0D, 200.0D)).setDescription("Light Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESLOVE = (new RangedAttribute((IAttribute)null, "rf.resLove", 0.0D, -100.0D, 200.0D)).setDescription("Love Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESPOISON = (new RangedAttribute((IAttribute)null, "rf.resPoison", 0.0D, -100.0D, 100.0D)).setDescription("Poison Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESSLEEP = (new RangedAttribute((IAttribute)null, "rf.resSleep", 0.0D, -100.0D, 100.0D)).setDescription("Sleep Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESFAT = (new RangedAttribute((IAttribute)null, "rf.resFat", 0.0D, -100.0D, 100.0D)).setDescription("Fatigue Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESCOLD = (new RangedAttribute((IAttribute)null, "rf.resCold", 0.0D, -100.0D, 100.0D)).setDescription("Cold Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESPARA = (new RangedAttribute((IAttribute)null, "rf.resPara", 0.0D, -100.0D, 100.0D)).setDescription("Paralysis Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESSEAL = (new RangedAttribute((IAttribute)null, "rf.resSeal", 0.0D, -100.0D, 100.0D)).setDescription("Seal Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESCRIT = (new RangedAttribute((IAttribute)null, "rf.resCrit", 0.0D, -100.0D, 100.0D)).setDescription("Critical Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESDIZ = (new RangedAttribute((IAttribute)null, "rf.resDiz", 0.0D, -100.0D, 100.0D)).setDescription("Dizziness Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESSTUN = (new RangedAttribute((IAttribute)null, "rf.resStun", 0.0D, -100.0D, 100.0D)).setDescription("Stun Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESFAINT = (new RangedAttribute((IAttribute)null, "rf.resFaint", 0.0D, -100.0D, 100.0D)).setDescription("Faint Resistance").setShouldWatch(true); 
    public static final IAttribute RFRESDRAIN = (new RangedAttribute((IAttribute)null, "rf.resDrain", 0.0D, -100.0D, 100.0D)).setDescription("Drain Resistance").setShouldWatch(true); 
    
    public static final IAttribute RFRLUCK = (new RangedAttribute((IAttribute)null, "rf.resLuck", 0.0D, 0D, 200.0D)).setDescription("Looting").setShouldWatch(true); 

}
