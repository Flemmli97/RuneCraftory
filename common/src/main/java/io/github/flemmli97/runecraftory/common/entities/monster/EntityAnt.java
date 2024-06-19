package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityAnt extends BaseMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(23, 12, "attack");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, INTERACT};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityAnt>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 1), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityAnt>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 1)
    );

    public final AnimatedAttackGoal<EntityAnt> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityAnt> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityAnt(EntityType<? extends EntityAnt> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public AnimationHandler<EntityAnt> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        return Math.max(25, (int) (super.animationCooldown(anim) * 0.7));
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public boolean hasSleepingAnimation() {
        return true;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        Vec3 off = new Vec3(0, 6 / 16d, 0);
        if (this.getType() == ModEntities.ANT.get())
            return off.scale(0.7);
        return off;
    }
}
