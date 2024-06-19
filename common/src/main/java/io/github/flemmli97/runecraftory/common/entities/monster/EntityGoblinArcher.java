package io.github.flemmli97.runecraftory.common.entities.monster;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
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
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityGoblinArcher extends EntityGoblin {

    private static final AnimatedAction BOW = new AnimatedAction(15, 9, "bow");
    private static final AnimatedAction TRIPLE = AnimatedAction.copyOf(BOW, "triple");
    private static final AnimatedAction KICK = new AnimatedAction(11, 7, "kick");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(KICK, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{BOW, TRIPLE, KICK, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGoblinArcher>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(KICK, e -> 0.6f), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGoblinArcher>simpleRangedStrafingAction(BOW, 8, 1, e -> 1)
                    .withCondition(ActionUtils.chanced(e -> 1,
                            (goal, target, previous) -> goal.attacker.getMainHandItem().getItem() instanceof BowItem)), 6),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGoblinArcher>simpleRangedStrafingAction(TRIPLE, 8, 1, e -> 1)
                    .withCondition(ActionUtils.chanced(e -> 1,
                            (goal, target, previous) -> goal.attacker.getMainHandItem().getItem() instanceof BowItem)), 3)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityGoblinArcher>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new EvadingRangedRunner<>(10, 4, 1)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 1),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
    );

    public final AnimatedAttackGoal<EntityGoblinArcher> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityGoblinArcher> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityGoblinArcher(EntityType<? extends EntityGoblin> type, Level level) {
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
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.TRIPLE_ARROW.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(TRIPLE);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(BOW);
            else
                this.getAnimationHandler().setAnimation(KICK);
        }
    }

    @Override
    public AnimationHandler<EntityGoblinArcher> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(BOW, TRIPLE)) {
            if (anim.getTick() == 1)
                this.startUsingItem(InteractionHand.MAIN_HAND);
            this.getNavigation().stop();
            if (anim.canAttack()) {
                boolean withTarget = this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget());
                if (anim.is(BOW)) {
                    if (withTarget)
                        this.shootArrow(this.getTarget());
                    else
                        this.shootArrowFromRotation(this);
                } else {
                    if (withTarget)
                        this.shootTripleArrow(this.getTarget());
                    else
                        this.shootTripleArrowFromRotation(this);
                }
                this.stopUsingItem();
            }
        } else
            super.handleAttack(anim);
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        Vec3 dir = new Vec3(target.getX() - arrow.getX(), target.getY(0.33) - arrow.getY(), target.getZ() - arrow.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    private void shootArrowFromRotation(LivingEntity shooter) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    private void shootTripleArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
        Vec3 dir = new Vec3(target.getX() - arrow.getX(), target.getY(0.33) - arrow.getY(), target.getZ() - arrow.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
        this.level.addFreshEntity(arrow);
        Vec3 up = this.getUpVector(1);

        for (float y = -15; y <= 15; y += 30) {
            Quaternion quaternion = new Quaternion(new Vector3f(up), y, true);
            Vector3f newDir = new Vector3f(dir);
            newDir.transform(quaternion);
            EntityMobArrow arrowO = new EntityMobArrow(this.level, this, 0.8f);
            arrowO.shoot(newDir.x(), newDir.y(), newDir.z(), 1.3f, 7 - this.level.getDifficulty().getId() * 2);
            this.level.addFreshEntity(arrowO);
        }

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private void shootTripleArrowFromRotation(LivingEntity shooter) {
        for (int i = 0; i < 3; i++) {
            EntityMobArrow arrow = new EntityMobArrow(this.level, this, 0.8f);
            arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + (i - 1) * 15, 0.0F, 1.3f, 7 - this.level.getDifficulty().getId() * 2);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(arrow);
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
