package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityChargingMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIChargeAttackBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityPommePomme extends EntityChargingMobBase {

    private EntityAIChargeAttackBase<EntityPommePomme> ai = new EntityAIChargeAttackBase<EntityPommePomme>(this);
    private static final AnimatedAction chargeAttack = new AnimatedAction(34,2, "roll");
    private static final AnimatedAction kick = new AnimatedAction(17,11, "kick");

    private static final AnimatedAction[] anims = new AnimatedAction[] {kick, chargeAttack};

    public EntityPommePomme(World world) {
        super(world);
        this.tasks.addTask(2, this.ai);
        this.setSize(1.0f, 1.6f);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2);
    }

    @Override
    public float attackChance() {
        return 0.9f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim){
        return 0.8;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if(type==AnimationType.CHARGE) {
            return anim.getID().equals("roll");
        }
        return type != AnimationType.MELEE || anim.getID().equals("kick");
    }
}
