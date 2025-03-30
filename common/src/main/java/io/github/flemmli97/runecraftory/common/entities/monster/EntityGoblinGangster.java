package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EntityGoblinGangster extends EntityGoblin {

    private static final AnimatedAction DOUBLE_STAB = AnimatedAction.builder(0.88, "double_stab").marker("attack", 0.4, 0.72).build();
    private static final AnimatedAction DOUBLE_THROW = AnimatedAction.builder(0.96, "double_throw").marker("attack", 0.4, 0.76).build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(DOUBLE_THROW, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{DOUBLE_STAB, DOUBLE_THROW, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGoblinGangster>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(DOUBLE_STAB, e -> 0.8f), 2),
            WeightedEntry.wrap(MonsterActionUtils.<EntityGoblinGangster>simpleRangedEvadingAction(DOUBLE_THROW, 8, 3, 1, e -> 1)
                    .withCondition(((goal, target, previous) -> !goal.attacker.getMainHandItem().isEmpty())), 3)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityGoblinGangster>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2)
    );

    public final AnimatedAttackGoal<EntityGoblinGangster> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityGoblinGangster> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityGoblinGangster(EntityType<? extends EntityGoblin> type, Level level) {
        super(type, level);
        this.goalSelector.removeGoal(super.attack);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.CUTLASS_PROP.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.CUTLASS_PROP.get()));
        this.setDropChance(EquipmentSlot.OFFHAND, 0);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 1 ? ModSpells.THROW_HAND_ITEM.get() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(DOUBLE_THROW);
            else
                this.getAnimationHandler().setAnimation(DOUBLE_STAB);
        }
    }

    @Override
    public AnimationHandler<EntityGoblinGangster> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 2.15;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DOUBLE_THROW)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget())) {
                    ModSpells.THROW_HAND_ITEM.get().use(this);
                }
                this.stopUsingItem();
            }
        } else if (anim.is(DOUBLE_STAB)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::quickAttack);
            }
        }
    }

    public boolean quickAttack(Entity target) {
        CustomDamage.Builder source = new CustomDamage.Builder(this).noKnockback().hurtResistant(1);
        return CombatUtils.mobAttack(this, target, source);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
