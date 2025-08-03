package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MoveToWalkTillClose;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.jetbrains.annotations.Nullable;

public class Goblin extends LeapingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("slash", AnimationsBuilder.definition(0.72).marker("attack", 0.36));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String LEAP = BUILDER.add("leap", AnimationsBuilder.definition(0.92)
            .marker("attack_start", 0.36).marker("attack_end", 0.8));
    public static final String STONE = BUILDER.add("throw", AnimationsBuilder.definition(0.72).marker("attack", 0.48));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Goblin> animationHandler = new AnimationHandler<>(this, ANIMS);

    public Goblin(EntityType<? extends Goblin> type, Level level) {
        super(type, level);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        super.applyAttributes();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(RuneCraftoryItems.STEEL_SWORD_PROP.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(MELEE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(5)
                .start(LEAP).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(2).max(6)).prepareOptional(new MoveToAttackTarget<>())
                .end(3)
                .start(STONE).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(4).max(9)).prepareOptional(new MoveToAttackTarget<>())
                .end(4)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(2, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTillClose<>())
                .add(1, new SetRandomWalkTarget<>(), new MoveToWalkTillClose<>()).build();
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 2.1;
        double length = this.getBbWidth() * 2;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(STONE)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.STONE_THROW.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AnimationHandler<? extends Goblin> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isLeapingAnim(String anim) {
        return anim.equals(LEAP);
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
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.STONE_THROW.get() : null))
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
        return RuneCraftorySounds.ENTITY_GOBLING_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return RuneCraftorySounds.ENTITY_GOBLING_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return RuneCraftorySounds.ENTITY_GOBLIN_DEATH.get();
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}
