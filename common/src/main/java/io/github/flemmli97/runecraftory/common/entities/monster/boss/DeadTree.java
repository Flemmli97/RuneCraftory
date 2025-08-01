package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.spells.HealT1Spell;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.BiConsumer;

public class DeadTree extends BossMonster {

    private static final EntityDataAccessor<Byte> SUMMON_ANIMATION = SynchedEntityData.defineId(DeadTree.class, EntityDataSerializers.BYTE);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String ATTACK = BUILDER.add("attack", AnimationsBuilder.definition(0.92).marker("attack", 0.44, 0.68));
    public static final String INTERACT = BUILDER.add("interact", ATTACK);
    public static final String FALLING_APPLES = BUILDER.add("falling_apples", AnimationsBuilder.definition(0.72)
            .marker("attack", 0.36).animationId("summon"));
    public static final String APPLE_SHIELD = BUILDER.add("apple_shield", FALLING_APPLES);
    public static final String SPIKE = BUILDER.add("spike", FALLING_APPLES);
    public static final String BIG_FALLING_APPLES = BUILDER.add("big_falling_apples", FALLING_APPLES);
    public static final String MORE_FALLING_APPLES = BUILDER.add("more_falling_apples", FALLING_APPLES);
    public static final String HEAL = BUILDER.add("heal", FALLING_APPLES);
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String ANGRY = BUILDER.add("angry", AnimationsBuilder.definition(1.24));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, DeadTree>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(ATTACK, (anim, entity) -> {
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(FALLING_APPLES, (anim, entity) -> {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.APPLE_RAIN.get().use(entity);
            }
        });
        b.put(APPLE_SHIELD, (anim, entity) -> {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.APPLE_SHIELD.get().use(entity);
            }
        });
        b.put(BIG_FALLING_APPLES, (anim, entity) -> {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.APPLE_RAIN_BIG.get().use(entity);
            }
        });
        b.put(MORE_FALLING_APPLES, (anim, entity) -> {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.APPLE_RAIN_MORE.get().use(entity);
            }
        });
        b.put(SPIKE, (anim, entity) -> {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.ROOT_SPIKE_TRIPLE.get().use(entity);
            }
        });
        b.put(HEAL, (anim, entity) -> {
            if (anim.isAt("attack")) {
                float healAmount = (float) (CombatUtils.getAttributeValue(entity, RuneCraftoryAttributes.MAGIC_ATTACK.asHolder()) * 3);
                entity.heal(healAmount);
                ServerLevel serverLevel = (ServerLevel) entity.level();
                serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + entity.getBbHeight() + 0.5, entity.getZ(), 0, 0, 0.1, 0, 0);
                HealT1Spell.spawnHealParticles(entity);
                HealT1Spell.spawnHealParticles(entity);
                HealT1Spell.spawnHealParticles(entity);
            }
        });
    });

    private final AnimationHandler<DeadTree> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide) {
                    if (anim != null) {
                        if (anim.is(APPLE_SHIELD))
                            this.shieldCooldown = this.getRandom().nextInt(60) + 100;
                        if (anim.is(HEAL))
                            this.healCooldown = this.getRandom().nextInt(100) + 100;
                    }
                } else if (anim != null && anim.animation().equals("summon")) {
                    int rand = this.random.nextInt(3) + 1;
                    AnimationDefinition animNew = AnimationsBuilder.definition(anim.length(), false)
                            .animationId(anim.animation() + "_" + rand)
                            .withTransitionTime(anim.startTransition(), anim.endTransition())
                            .speed(anim.speed()).build(anim.id());
                    this.getAnimationHandler().setAnimationDef(animNew);
                    return true;
                }
                return false;
            });
    private int shieldCooldown, healCooldown;

    public DeadTree(EntityType<? extends DeadTree> type, Level world) {
        super(type, world);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(RuneCraftoryEntities.DEAD_TREE.getID(), this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(RuneCraftorySounds.DEAD_TREE_FIGHT.get());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new SmoothGroundNavigation(this, level) {
            @Nullable
            @Override
            protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
                if (!DeadTree.this.canMove())
                    return null;
                return super.createPath(targets, regionOffset, offsetUpward, accuracy, followRange);
            }
        };
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        super.applyAttributes();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SUMMON_ANIMATION, (byte) 0);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<DeadTree>create()
                .start(MonsterBehaviourUtils.checkedAttack(ATTACK)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifCloserThan(5))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(FALLING_APPLES)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.isEnraged())
                .prepare(new SetWalkTargetToAttackTarget<DeadTree>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(4))
                        .speedMod((e, t) -> 1.1f)).prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(APPLE_SHIELD)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.shieldCooldown <= 0)
                .end(8)
                .start(MonsterBehaviourUtils.checkedAttack(SPIKE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(BIG_FALLING_APPLES)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .prepare(new SetWalkTargetToAttackTarget<DeadTree>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(4))
                        .speedMod((e, t) -> 1.1f)).prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(MORE_FALLING_APPLES)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(BossMonster::isEnraged)
                .prepare(new SetWalkTargetToAttackTarget<DeadTree>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(4))
                        .speedMod((e, t) -> 1.1f)).prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(HEAL)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.healCooldown <= 0)
                .end(7)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(1, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTarget<>()).build();
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            --this.shieldCooldown;
            --this.healCooldown;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(ANGRY))) && super.hurt(source, amount);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 2.1;
        double length = this.getBbWidth() * 1.85;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public int animationCooldown(String anim) {
        int cooldown = super.animationCooldown(anim);
        if (anim != null && anim.equals(SPIKE))
            cooldown += 40;
        return cooldown;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        this.getNavigation().stop();
        BiConsumer<AnimationState, DeadTree> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<DeadTree> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.ROOT_SPIKE_TRIPLE.get()))
                    this.getAnimationHandler().setAnimation(SPIKE);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.APPLE_RAIN.get()))
                    this.getAnimationHandler().setAnimation(FALLING_APPLES);
            } else {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                    this.getAnimationHandler().setAnimation(ATTACK);
            }
        }
    }

    public byte summonAnimationType() {
        return this.entityData.get(SUMMON_ANIMATION);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    public boolean canMove() {
        return this.isTamed() || this.isEnraged();
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT))
            return;
        if (!this.canMove())
            return;
        super.push(x, y, z);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return RuneCraftorySounds.ENTITY_DEAD_TREE_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public String getSleepAnimation() {
        return DEFEAT;
    }
}
