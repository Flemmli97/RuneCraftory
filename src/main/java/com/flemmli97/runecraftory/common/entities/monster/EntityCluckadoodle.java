package com.flemmli97.runecraftory.common.entities.monster;

import com.flemmli97.runecraftory.common.entities.AnimationType;
import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.common.entities.monster.ai.AnimatedMeleeGoal;
import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EntityCluckadoodle extends BaseMonster {

    public final AnimatedMeleeGoal<EntityCluckadoodle> attack = new AnimatedMeleeGoal<>(this);
    public static final AnimatedAction melee = new AnimatedAction(16, 10, "attack");

    private static final AnimatedAction[] anims = new AnimatedAction[]{melee};

    public EntityCluckadoodle(EntityType<? extends EntityCluckadoodle> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.27);
        super.applyAttributes();
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
    public float attackChance(AnimationType type) {
        return 0.8f;
    }

    @Override
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 0.8;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        return this.getRNG().nextInt(10) + 30;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type == AnimationType.MELEE && anim.getID().equals(melee.getID());
    }
}
