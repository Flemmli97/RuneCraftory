package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.DeadTreeAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class EntityDeadTree extends BossMonster {

    //Swipes x2
    public static final AnimatedAction ATTACK = new AnimatedAction(19, 10, "attack");

    public static final AnimatedAction FALLING_APPLES = AnimatedAction.builder(15, "falling_apples").marker(6).withClientID("summon").build();
    public static final AnimatedAction APPLE_SHIELD = AnimatedAction.copyOf(FALLING_APPLES, "apple_shield");
    public static final AnimatedAction SPIKE = AnimatedAction.copyOf(FALLING_APPLES, "spike");

    public static final AnimatedAction BIG_FALLING_APPLES = AnimatedAction.copyOf(FALLING_APPLES, "big_falling_apples");
    public static final AnimatedAction MORE_FALLING_APPLES = AnimatedAction.copyOf(FALLING_APPLES, "more_falling_apples");
    public static final AnimatedAction HEAL = AnimatedAction.copyOf(FALLING_APPLES, "heal");

    public static final AnimatedAction DEFEAT = AnimatedAction.builder(120, "defeat").marker(15).infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(25, 0, "angry");

    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(ATTACK, "interact");

    private static final List<String> SUMMONS = List.of(FALLING_APPLES.getID(),
            APPLE_SHIELD.getID(),
            SPIKE.getID(),
            BIG_FALLING_APPLES.getID(),
            MORE_FALLING_APPLES.getID()
    );
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{ATTACK, FALLING_APPLES, APPLE_SHIELD, SPIKE, BIG_FALLING_APPLES, MORE_FALLING_APPLES, DEFEAT, ANGRY, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityDeadTree>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(ATTACK, (anim, entity) -> {
            if (anim.canAttack() || anim.getTick() == 14) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(FALLING_APPLES, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.APPLE_RAIN.get().use(entity);
            }
        });
        b.put(APPLE_SHIELD, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.APPLE_SHIELD.get().use(entity);
            }
        });
        b.put(BIG_FALLING_APPLES, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.APPLE_RAIN_BIG.get().use(entity);
            }
        });
        b.put(MORE_FALLING_APPLES, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.APPLE_RAIN_MORE.get().use(entity);
            }
        });
        b.put(SPIKE, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.ROOT_SPIKE_TRIPLE.get().use(entity);
            }
        });
        b.put(HEAL, (anim, entity) -> {
            if (anim.canAttack()) {
                float healAmount = (float) (CombatUtils.getAttributeValue(entity, ModAttributes.MAGIC.get()) * (0.6f));
                entity.heal(healAmount);
                ServerLevel serverLevel = (ServerLevel) entity.level;
                serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + entity.getBbHeight() + 0.5, entity.getZ(), 0, 0, 0.1, 0, 0);
                for (int i = 0; i < 10; i++) {
                    serverLevel.sendParticles(new ColoredParticleData(ModParticles.LIGHT.get(), 63 / 255F, 201 / 255F, 63 / 255F, 0.4f, 2f), entity.getX() + entity.getRandom().nextGaussian() * 0.2, entity.getY() + entity.getBbHeight() * 0.5 + entity.getRandom().nextGaussian() * 0.07, entity.getZ() + entity.getRandom().nextGaussian() * 0.2, 1, entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03, entity.getRandom().nextGaussian() * 0.03, 0);
                }
            }
        });
    });

    private static final EntityDataAccessor<Byte> SUMMON_ANIMATION = SynchedEntityData.defineId(EntityDeadTree.class, EntityDataSerializers.BYTE);

    public final DeadTreeAttackGoal<EntityDeadTree> attack = new DeadTreeAttackGoal<>(this);
    private final AnimationHandler<EntityDeadTree> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (!this.level.isClientSide && anim != null) {
                    if (anim.is(APPLE_SHIELD))
                        this.shieldCooldown = 100;
                    if (anim.is(HEAL))
                        this.healCooldown = 100;
                    if (SUMMONS.contains(anim.getID())) {
                        int rand = this.random.nextInt(3);
                        this.entityData.set(SUMMON_ANIMATION, (byte) rand);
                    }
                }
            });

    private int shieldCooldown, healCooldown;

    public EntityDeadTree(EntityType<? extends EntityDeadTree> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        super.applyAttributes();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level) {
            @Nullable
            @Override
            protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
                if (!EntityDeadTree.this.canMove())
                    return null;
                return super.createPath(targets, regionOffset, offsetUpward, accuracy, followRange);
            }
        };
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMON_ANIMATION, (byte) 0);
    }

    public byte summonAnimationType() {
        return this.entityData.get(SUMMON_ANIMATION);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.is(ANGRY, DEFEAT, INTERACT))
            return false;
        if (type == AnimationType.GENERICATTACK) {
            if (anim.is(APPLE_SHIELD) && this.shieldCooldown > 0)
                return false;
            if (anim.is(HEAL) && this.healCooldown > 0 && this.random.nextFloat() < 0.2)
                return false;
            if (!this.isEnraged()) {
                if (anim.is(ATTACK))
                    return this.random.nextFloat() < 0.8;
                return !anim.is(BIG_FALLING_APPLES) && !anim.is(MORE_FALLING_APPLES);
            }
            return !anim.is(FALLING_APPLES);
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            --this.shieldCooldown;
            --this.healCooldown;
        }
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int cooldown = super.animationCooldown(anim);
        if (anim != null && anim.is(SPIKE))
            cooldown += 40;
        return cooldown;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
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
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.lookAt(target, 180.0f, 50.0f);
        }
        this.getNavigation().stop();
        BiConsumer<AnimatedAction, EntityDeadTree> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 3;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.ROOT_SPIKE_TRIPLE.get()))
                    this.getAnimationHandler().setAnimation(SPIKE);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.APPLE_RAIN.get()))
                    this.getAnimationHandler().setAnimation(FALLING_APPLES);
            } else {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                    this.getAnimationHandler().setAnimation(ATTACK);
            }
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_DEAD_TREE_DEATH.get();
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }

    @Override
    public AnimationHandler<EntityDeadTree> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return DEFEAT;
    }

    public boolean canMove() {
        return this.isTamed() || this.isEnraged();
    }
}
