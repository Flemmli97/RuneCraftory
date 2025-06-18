package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class EntityOrcArcher extends EntityOrc {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("kick", AnimationsBuilder.definition(0.92).marker("attack", 0.58));
    public static final String RANGED = BUILDER.add("bow", AnimationsBuilder.definition(1).marker("attack", 0.6));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    //
//    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityOrc>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(MELEE, e -> 0.6f), 1),
//            WeightedEntry.wrap(MonsterActionUtils.<EntityOrc>simpleRangedStrafingAction(RANGED, 8, 1, e -> 1)
//                    .withCondition(ActionUtils.chanced(e -> e.getType() == ModEntities.ORC.get() ? 0.85f : 0.95f,
//                            (goal, target, previous) -> goal.attacker.getMainHandItem().getItem() instanceof BowItem)), 4)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityOrc>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new KeepDistanceRunner<>(4, 10, 1)), 3),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 1),
//            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityOrc> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityOrcArcher> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityOrcArcher(EntityType<? extends EntityOrcArcher> type, Level level) {
        super(type, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.2;
        double length = this.getBbWidth() * 1.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void setupAttack(AnimationDefinition anim) {
        if (anim.is(RANGED)) {
            this.startUsingItem(InteractionHand.MAIN_HAND);
        }
        super.setupAttack(anim);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(RANGED)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    this.shootArrow(this.getTarget());
                } else if (this.getFirstPassenger() instanceof Player)
                    this.shootArrowFromRotation(this);
                this.stopUsingItem();
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AnimationHandler<EntityOrcArcher> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(MELEE);
            else
                this.getAnimationHandler().setAnimation(RANGED);
        }
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level(), this, 0.8f);
        arrow.shootAtEntity(target, 1.3f, 14 - this.level().getDifficulty().getId() * 4);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }

    private void shootArrowFromRotation(LivingEntity shooter) {
        EntityMobArrow arrow = new EntityMobArrow(this.level(), this, 0.8f);
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 1.3f, 7 - this.level().getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
