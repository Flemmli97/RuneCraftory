package com.flemmli97.runecraftory.mobs;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.registry.ModAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class MobUtils {

    public static boolean handleMobAttack(Entity entity, DamageSource src, float dmg) {
        if (RuneCraftory.conf.combatModule) {
            //TODO
            return entity.attackEntityFrom(src, dmg);
        } else
            return entity.attackEntityFrom(src, MathHelper.sqrt(dmg));
    }

    public static float getAttributeValue(LivingEntity attacker, Attribute att, Entity target) {
        float increase = 0;
        /*if (attacker instanceof PlayerEntity) {
            IPlayer cap = entity.getCapability(PlayerCapProvider.PlayerCap, null);
            increase += cap.getAttributeValue(att);
        } else if (attacker instanceof IRFNpc) {
            increase += ((IRFNpc) entity).getAttributeValue(att);
        } else */if (attacker.getAttribute(att) != null) {
            increase += (float) attacker.getAttribute(att).getValue();
        }
        if(!(target instanceof LivingEntity))
            return increase;
        Attribute opp = opposing(att);
        if(opp==null)
            return increase;
        LivingEntity lT = (LivingEntity) target;
        if(lT.getAttribute(opp)!=null)
            increase-= lT.getAttribute(opp).getValue();
        /*if (target instanceof IEntityBase && target.getAttributeMap().getAttributeInstance(resAtt) != null) {
            increase -= (int) target.getAttributeMap().getAttributeInstance(resAtt).getAttributeValue();
        } else if (target instanceof IRFNpc) {
            increase -= ((IRFNpc) target).getAttributeValue(att);
        } else if (target instanceof EntityPlayer) {
            IPlayer cap = target.getCapability(PlayerCapProvider.PlayerCap, null);
            increase -= cap.getAttributeValue(att);
        }*/
        return increase;
    }

    public static Attribute opposing(Attribute att) {
        //if (att == Attributes.GENERIC_ATTACK_DAMAGE)
        //    return ModAttributes.RF_DEFENCE;
        //if (att == ModAttributes.RF_MAGIC)
        //    return ModAttributes.RF_MAGIC_DEFENCE;
        if (att == ModAttributes.RFPARA)
            return ModAttributes.RFRESPARA;
        if (att == ModAttributes.RFPOISON)
            return ModAttributes.RFRESPOISON;
        if (att == ModAttributes.RFSEAL)
            return ModAttributes.RFRESSEAL;
        if (att == ModAttributes.RFSLEEP)
            return ModAttributes.RFRESSLEEP;
        if (att == ModAttributes.RFFAT)
            return ModAttributes.RFRESFAT;
        if (att == ModAttributes.RFCOLD)
            return ModAttributes.RFRESCOLD;
        if (att == ModAttributes.RFDIZ)
            return ModAttributes.RFRESDIZ;
        if (att == ModAttributes.RFCRIT)
            return ModAttributes.RFRESCRIT;
        if (att == ModAttributes.RFSTUN)
            return ModAttributes.RFRESSTUN;
        if (att == ModAttributes.RFFAINT)
            return ModAttributes.RFRESFAINT;
        if (att == ModAttributes.RFDRAIN)
            return ModAttributes.RFRESDRAIN;
        //if(att == ModAttributes.RFKNOCK)
        //    return ModAttributes.RESKNOCK;
        return null;
    }
}
