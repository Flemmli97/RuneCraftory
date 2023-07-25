package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.AmbrosiaAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;

public class EntityAmbrosia extends BossMonster implements DelayedAttacker {

    public static final AnimatedAction KICK_1 = new AnimatedAction(12, 6, "kick_1");
    public static final AnimatedAction KICK_2 = new AnimatedAction(12, 6, "kick_2");
    public static final AnimatedAction KICK_3 = new AnimatedAction(16, 6, "kick_3");
    public static final AnimatedAction BUTTERFLY = new AnimatedAction(45, 5, "butterfly");
    public static final AnimatedAction WAVE = new AnimatedAction(45, 5, "wave");
    public static final AnimatedAction SLEEP = new AnimatedAction(15, 5, "sleep");
    public static final AnimatedAction POLLEN = new AnimatedAction(15, 5, "pollen");
    public static final AnimatedAction POLLEN_2 = AnimatedAction.copyOf(POLLEN, "pollen_2");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(204, "defeat").marker(150).infinite().build();
    public static final AnimatedAction ANGRY = new AnimatedAction(48, 0, "angry");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(KICK_1, "interact");

    public static final ImmutableList<String> NON_CHOOSABLE_ATTACKS = ImmutableList.of(POLLEN_2.getID(), KICK_2.getID(), KICK_3.getID());
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{KICK_1, BUTTERFLY, WAVE, SLEEP, POLLEN, POLLEN_2, KICK_2, KICK_3, DEFEAT, ANGRY, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityAmbrosia>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(BUTTERFLY, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.BUTTERFLY.get().use(entity);
            }
        });
        BiConsumer<AnimatedAction, EntityAmbrosia> kick = (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
            }
            if (anim.canAttack()) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        };
        b.put(KICK_1, kick);
        b.put(KICK_2, kick);
        b.put(KICK_3, kick);
        b.put(SLEEP, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.SLEEPBALLS.get().use(entity);
        });
        b.put(WAVE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.WAVE.get().use(entity);
        });
        BiConsumer<AnimatedAction, EntityAmbrosia> pollenHandler = (anim, entity) -> {
            if (entity.aiVarHelper == null)
                return;
            entity.setDeltaMovement(new Vec3(entity.aiVarHelper.x(), 0, entity.aiVarHelper.z()));
            if (anim.canAttack() && !EntityUtils.sealed(entity)) {
                entity.getNavigation().stop();
                EntityPollen pollen = new EntityPollen(entity.level, entity);
                pollen.setPos(pollen.getX(), pollen.getY() + 0.5, pollen.getZ());
                entity.level.addFreshEntity(pollen);
            }
        };
        b.put(POLLEN, pollenHandler);
        b.put(POLLEN_2, pollenHandler);
    });

    public final AmbrosiaAttackGoal<EntityAmbrosia> attack = new AmbrosiaAttackGoal<>(this);
    private final AnimationHandler<EntityAmbrosia> animationHandler = new AnimationHandler<>(this, ANIMS);
    private Vec3 aiVarHelper;

    public EntityAmbrosia(EntityType<? extends EntityAmbrosia> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29);
        super.applyAttributes();
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.is(ANGRY, DEFEAT, INTERACT))
            return false;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() || !anim.is(POLLEN);
        return false;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim != null)
            if (anim.is(KICK_1, KICK_2, POLLEN))
                return 3;
        return 34 + this.getRandom().nextInt(22) - (this.isEnraged() ? 20 : 0) + diffAdd;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(WAVE, ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(POLLEN, ANGRY, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public boolean shouldFreezeTravel() {
        return this.getAnimationHandler().isCurrent(WAVE);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!anim.is(POLLEN))
                this.lookAt(target, 180.0f, 50.0f);
        }
        BiConsumer<AnimatedAction, EntityAmbrosia> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.is(POLLEN)) {
            return this.getBoundingBox().inflate(2.0);
        }
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(WAVE);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(SLEEP);
            else
                this.getAnimationHandler().setAnimation(KICK_1);
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

    public void setAiVarHelper(Vec3 aiVarHelper) {
        this.aiVarHelper = aiVarHelper;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }

    @Override
    public AnimationHandler<EntityAmbrosia> getAnimationHandler() {
        return this.animationHandler;
    }

    public boolean isAnimEqual(String prev, AnimatedAction other) {
        if (other == null)
            return true;
        if (prev.equals(POLLEN_2.getID()))
            return other.getID().equals(POLLEN.getID());
        if (prev.equals(KICK_3.getID()))
            return other.getID().equals(KICK_1.getID());
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(String prev) {
        return switch (prev) {
            case "kick_1" -> KICK_2;
            case "kick_2" -> KICK_3;
            case "pollen" -> POLLEN_2;
            default -> null;
        };
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.aiVarHelper;
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }
}
