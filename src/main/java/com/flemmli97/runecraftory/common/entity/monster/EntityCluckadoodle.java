package com.flemmli97.runecraftory.common.entity.monster;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.entity.ai.EntityAIMeleeBase;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityCluckadoodle extends EntityMobBase {

    public EntityAIMeleeBase<EntityCluckadoodle> attack = new EntityAIMeleeBase<EntityCluckadoodle>(this, 1.2f);

    public EntityCluckadoodle(World world) {
        super(world);
        this.tasks.addTask(2, this.attack);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_CHICKEN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CHICKEN_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.95f;
    }

    @Override
    protected float getSoundPitch() {
        return 0.8f;
    }

    @Override
    public float attackChance() {
        return 0.8f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return AnimatedAction.vanillaAttackOnly;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type != AnimationType.MELEE || anim.getID().equals("vanilla");
    }
}
