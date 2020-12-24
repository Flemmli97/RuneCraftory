package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class MobUtils {

    public static boolean handleMobAttack(Entity entity, DamageSource src, float dmg) {
        if (GeneralConfig.combatModule) {
            //TODO
            return entity.attackEntityFrom(src, dmg);
        } else
            return entity.attackEntityFrom(src, MathHelper.sqrt(dmg));
    }

    public static float getAttributeValue(LivingEntity attacker, Attribute att, Entity target) {
        float increase = 0;
        if (attacker instanceof PlayerEntity) {
            increase += attacker.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getAttributeValue((PlayerEntity) attacker, att)).orElse(0);
            //} else if (attacker instanceof IRFNpc) {
            //    increase += ((IRFNpc) entity).getAttributeValue(att);
        } else if (attacker.getAttribute(att) != null) {
            increase += (float) attacker.getAttribute(att).getValue();
        }
        if (!(target instanceof LivingEntity))
            return increase;
        Attribute opp = opposing(att);
        if (opp == null)
            return increase;
        if (target instanceof PlayerEntity) {
            increase -= target.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getAttributeValue((PlayerEntity) target, att)).orElse(0);
        }
                /*
        } else if (target instanceof IRFNpc) {
            increase -= ((IRFNpc) target).getAttributeValue(att);
        */
        else {
            LivingEntity lT = (LivingEntity) target;
            if (lT.getAttribute(opp) != null)
                increase -= lT.getAttribute(opp).getValue();
        }
        return increase;
    }

    public static Attribute opposing(Attribute att) {
        //if (att == Attributes.GENERIC_ATTACK_DAMAGE)
        //    return ModAttributes.RF_DEFENCE;
        //if (att == ModAttributes.RF_MAGIC)
        //    return ModAttributes.RF_MAGIC_DEFENCE;
        if (att == ModAttributes.RFPARA.get())
            return ModAttributes.RFRESPARA.get();
        if (att == ModAttributes.RFPOISON.get())
            return ModAttributes.RFRESPOISON.get();
        if (att == ModAttributes.RFSEAL.get())
            return ModAttributes.RFRESSEAL.get();
        if (att == ModAttributes.RFSLEEP.get())
            return ModAttributes.RFRESSLEEP.get();
        if (att == ModAttributes.RFFAT.get())
            return ModAttributes.RFRESFAT.get();
        if (att == ModAttributes.RFCOLD.get())
            return ModAttributes.RFRESCOLD.get();
        if (att == ModAttributes.RFDIZ.get())
            return ModAttributes.RFRESDIZ.get();
        if (att == ModAttributes.RFCRIT.get())
            return ModAttributes.RFRESCRIT.get();
        if (att == ModAttributes.RFSTUN.get())
            return ModAttributes.RFRESSTUN.get();
        if (att == ModAttributes.RFFAINT.get())
            return ModAttributes.RFRESFAINT.get();
        if (att == ModAttributes.RFDRAIN.get())
            return ModAttributes.RFRESDRAIN.get();
        //if(att == ModAttributes.RFKNOCK)
        //    return ModAttributes.RESKNOCK;
        return null;
    }
}
