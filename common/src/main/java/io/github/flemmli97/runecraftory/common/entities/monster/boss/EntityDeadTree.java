package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.DeadTreeAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
    });

    private static final EntityDataAccessor<Byte> SUMMON_ANIMATION = SynchedEntityData.defineId(EntityDeadTree.class, EntityDataSerializers.BYTE);

    public final DeadTreeAttackGoal<EntityDeadTree> attack = new DeadTreeAttackGoal<>(this);
    private final AnimationHandler<EntityDeadTree> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (!this.level.isClientSide && anim != null) {
                    if (anim.getID().equals(APPLE_SHIELD.getID()))
                        this.shieldCooldown = 100;
                    if (SUMMONS.contains(anim.getID())) {
                        int rand = this.random.nextInt(3);
                        this.entityData.set(SUMMON_ANIMATION, (byte) rand);
                    }
                }
            });

    private int shieldCooldown;

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
        if (anim.getID().equals(ANGRY.getID()) || anim.getID().equals(DEFEAT.getID()) || anim.getID().equals(INTERACT.getID()))
            return false;
        if (type == AnimationType.GENERICATTACK) {
            if (anim.getID().equals(APPLE_SHIELD.getID()) && this.shieldCooldown > 0)
                return false;
            if (!this.isEnraged())
                return !anim.getID().equals(BIG_FALLING_APPLES.getID()) && !anim.getID().equals(MORE_FALLING_APPLES.getID());
            return !anim.getID().equals(FALLING_APPLES.getID());
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide)
            --this.shieldCooldown;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        return 25 + this.getRandom().nextInt(15) - (this.isEnraged() ? 7 : 0) + diffAdd;
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
            if (command == 2)
                this.getAnimationHandler().setAnimation(SPIKE);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(FALLING_APPLES);
            else
                this.getAnimationHandler().setAnimation(ATTACK);
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
