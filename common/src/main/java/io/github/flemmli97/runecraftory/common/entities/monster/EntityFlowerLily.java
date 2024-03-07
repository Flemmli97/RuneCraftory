package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.EvadingRangedAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityFlowerLily extends BaseMonster {

    public static final AnimatedAction LEAP = new AnimatedAction(18, 3, "leap");
    public static final AnimatedAction ATTACK = new AnimatedAction(12, 6, "attack");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(ATTACK, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{LEAP, ATTACK, INTERACT, SLEEP};

    public final EvadingRangedAttackGoal<EntityFlowerLily> attack = new EvadingRangedAttackGoal<>(this, 2, 8, e -> true);
    private final AnimationHandler<EntityFlowerLily> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityFlowerLily(EntityType<? extends EntityFlowerLily> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.RANGED) {
            return anim.is(ATTACK);
        }
        return type == AnimationType.MELEE && anim.is(LEAP);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(LEAP)) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                Vec3 vec32;
                if (this.getTarget() != null) {
                    Vec3 targetPos = this.getTarget().position();
                    vec32 = new Vec3(targetPos.x - this.getX(), 0.0, targetPos.z - this.getZ()).normalize();
                } else
                    vec32 = this.getLookAngle();
                this.setDeltaMovement(vec32.x, 0.1, vec32.z);
                this.lookAt(EntityAnchorArgument.Anchor.EYES, this.position().add(vec32.x, 0, vec32.z));
            }
        } else if (anim.is(ATTACK)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack()) {
                this.rangedAttackSpell().use(this);
            }
        }
    }

    protected Spell rangedAttackSpell() {
        return ModSpells.DOUBLE_BULLET.get();
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 0 ? this.rangedAttackSpell() : null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(ATTACK);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSounds.ENTITY_FLOWER_LILY_STEP.get(), 1, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }

    @Override
    public float attackChance(AnimationType type) {
        return type == AnimationType.MELEE ? 0.4f : 1;
    }

    @Override
    public int animationCooldown(@Nullable AnimatedAction anim) {
        return (int) (super.animationCooldown(anim) * 1.8);
    }

    @Override
    public AnimationHandler<EntityFlowerLily> getAnimationHandler() {
        return this.animationHandler;
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
