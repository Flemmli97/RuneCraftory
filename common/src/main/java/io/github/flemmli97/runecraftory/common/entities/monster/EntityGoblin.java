package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityGoblin extends LeapingMonster {

    protected static final AnimatedAction MELEE = new AnimatedAction(12, 7, "slash");
    protected static final AnimatedAction LEAP = new AnimatedAction(19, 6, "leap");
    protected static final AnimatedAction STONE = new AnimatedAction(14, 9, "throw");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, STONE, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGoblin>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 0.8f), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityGoblin>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .prepare(() -> new WrappedRunner<>(new MoveAwayRunner<>(1.5, 1, 4))), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityGoblin>(LEAP)
                    .cooldown(e -> e.animationCooldown(LEAP))
                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 5))), 2),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(STONE, 8, 3, 1, e -> 1), 3)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityGoblin>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2)
    );

    public final AnimatedAttackGoal<EntityGoblin> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityGoblin> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityGoblin(EntityType<? extends EntityGoblin> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STEEL_SWORD_PROP.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.is(STONE))
            return 8;
        return 1.25;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.STONE_THROW.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(STONE);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_GOBLING_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.ENTITY_GOBLING_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_GOBLIN_DEATH.get();
    }

    @Override
    public AnimationHandler<? extends EntityGoblin> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(STONE)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack()) {
                ModSpells.STONE_THROW.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    protected boolean isLeapingAnim(AnimatedAction anim) {
        return anim.is(LEAP);
    }

    @Override
    public Vec3 getLeapVec(@Nullable Vec3 target) {
        return super.getLeapVec(target).scale(1.25);
    }

    @Override
    public double leapHeightMotion() {
        return 0.3;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 13 / 16d, -5 / 16d);
    }
}
