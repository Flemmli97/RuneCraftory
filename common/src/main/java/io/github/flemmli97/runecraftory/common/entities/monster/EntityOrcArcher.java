package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.ActionUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.EvadingRangedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityOrcArcher extends EntityOrc {

    public static final AnimatedAction MELEE = new AnimatedAction(19, 13, "kick");
    public static final AnimatedAction RANGED = new AnimatedAction(20, 12, "bow");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, RANGED, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityOrc>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(MELEE, e -> 0.6f), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityOrc>simpleRangedStrafingAction(RANGED, 8, 1, e -> 1)
                    .withCondition(ActionUtils.chanced(e -> e.getType() == ModEntities.ORC.get() ? 0.85f : 0.95f,
                            (goal, target, previous) -> goal.attacker.getMainHandItem().getItem() instanceof BowItem)), 4)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityOrc>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new EvadingRangedRunner<>(10, 4, 1)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 1),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
    );

    public final AnimatedAttackGoal<EntityOrc> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityOrcArcher> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityOrcArcher(EntityType<? extends EntityOrcArcher> type, Level level) {
        super(type, level);
        this.goalSelector.removeGoal(super.attack);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(RANGED)) {
            if (anim.getTick() == 1)
                this.startUsingItem(InteractionHand.MAIN_HAND);
            this.getNavigation().stop();
            if (anim.canAttack()) {
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
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        arrow.shootAtEntity(target, 1.3f, 14 - this.level.getDifficulty().getId() * 4, 0.2f);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    private void shootArrowFromRotation(LivingEntity shooter) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
