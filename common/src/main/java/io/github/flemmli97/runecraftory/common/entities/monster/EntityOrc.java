package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMonsterAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class EntityOrc extends BaseMonster {

    private static final AnimatedAction MELEE_1 = new AnimatedAction(22, 14, "attack_1");
    private static final AnimatedAction MELEE_2 = new AnimatedAction(23, 13, "attack_2");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE_1, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE_1, MELEE_2, INTERACT, SLEEP};
    private final AnimationHandler<EntityOrc> animationHandler = new AnimationHandler<>(this, ANIMS);
    public AnimatedMonsterAttackGoal<EntityOrc> attack = new AnimatedMonsterAttackGoal<>(this);

    public EntityOrc(EntityType<? extends EntityOrc> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ORC_MAZE.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public float attackChance(AnimationType type) {
        return this.getType() == ModEntities.ORC.get() ? 0.85f : 0.95f;
    }

    @Override
    public AnimationHandler<? extends EntityOrc> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.is(MELEE_1, MELEE_2);
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.is(MELEE_2))
            return 1.4;
        return 1.2;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_BRUTE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.9f;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (this.random.nextInt(2) == 0)
                this.getAnimationHandler().setAnimation(MELEE_1);
            else
                this.getAnimationHandler().setAnimation(MELEE_2);
        }
    }

    @Override
    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        super.mobAttack(anim, target, cons);
        if (this.getMainHandItem().is(ModItems.ORC_MAZE.get()))
            this.playSound(ModSounds.ENTITY_ORC_BONK.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }
}
