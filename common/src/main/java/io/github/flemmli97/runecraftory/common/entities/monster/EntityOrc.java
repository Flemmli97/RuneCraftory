package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.function.Consumer;

public class EntityOrc extends BaseMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE_1 = BUILDER.add("attack_1", AnimationsBuilder.definition(1).marker("attack", 0.72));
    public static final String MELEE_2 = BUILDER.add("attack_2", AnimationsBuilder.definition(1.04).marker("attack", 0.56));
    public static final String INTERACT = BUILDER.add("interact", MELEE_1);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    //
//    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityOrc>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE_1, e -> e.getType() == ModEntities.ORC.get() ? 0.85f : 0.95f), 1),
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE_2, e -> e.getType() == ModEntities.ORC.get() ? 0.85f : 0.95f), 1)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityOrc>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 3),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 1),
//            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityOrc> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityOrc> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityOrc(EntityType<? extends EntityOrc> type, Level world) {
        super(type, world);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ORC_MAZE.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public AnimationHandler<? extends EntityOrc> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.8;
        double length = this.getBbWidth() * 2.1;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        super.mobAttack(anim, target, cons);
        if (this.getMainHandItem().is(ModItems.ORC_MAZE.get()))
            this.playSound(ModSounds.ENTITY_ORC_BONK.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

//    @Override
//    public double getPassengersRidingOffset() {
//        return this.getBbHeight() * 0.85D;
//    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 17.5 / 16d, -7 / 16d);
//    }
}
