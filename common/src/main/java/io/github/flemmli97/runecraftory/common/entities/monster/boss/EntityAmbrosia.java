package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.AmbrosiaAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPollen;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityAmbrosia extends BossMonster implements DelayedAttacker {
    //Tries kicking target 3 times in a row   
    public static final AnimatedAction kick_1 = new AnimatedAction(12, 6, "kick_1");
    public static final AnimatedAction kick_2 = new AnimatedAction(12, 6, "kick_2");
    public static final AnimatedAction kick_3 = new AnimatedAction(16, 6, "kick_3");

    //Sends a wave of hp-draining(hard) butterflies at target
    public static final AnimatedAction butterfly = new AnimatedAction(45, 5, "butterfly");
    //Shockwave kind of attack surrounding ambrosia
    public static final AnimatedAction wave = new AnimatedAction(45, 5, "wave");
    //Sleep balls
    public static final AnimatedAction sleep = new AnimatedAction(15, 5, "sleep");
    //2 spinning changing direction between them. also scatters earth damage pollen while doing it
    public static final AnimatedAction pollen = new AnimatedAction(15, 5, "pollen");
    public static final AnimatedAction pollen2 = AnimatedAction.copyOf(pollen, "pollen_2");

    public static final AnimatedAction defeat = new AnimatedAction(204, 150, "defeat", "defeat", 1, false);
    public static final AnimatedAction angry = new AnimatedAction(48, 0, "angry");

    public static final AnimatedAction interact = AnimatedAction.copyOf(kick_1, "interact");

    public static final ImmutableList<String> nonChoosableAttacks = ImmutableList.of(pollen2.getID(), kick_2.getID(), kick_3.getID());
    private static final AnimatedAction[] anims = new AnimatedAction[]{kick_1, butterfly, wave, sleep, pollen, pollen2, kick_2, kick_3, defeat, angry, interact};
    private static final List<Vector3f> pollenBase = RayTraceUtils.rotatedVecs(new Vec3(1, 0, 0), new Vec3(0, 1, 0), -180, 135, 45);
    private static final List<Vector3f> pollenInd = RayTraceUtils.rotatedVecs(new Vec3(0.04, 0.07, 0), new Vec3(0, 1, 0), -180, 160, 20);
    public final AmbrosiaAttackGoal<EntityAmbrosia> attack = new AmbrosiaAttackGoal<>(this);
    private final AnimationHandler<EntityAmbrosia> animationHandler = new AnimationHandler<>(this, anims);
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
        if (anim.getID().equals(angry.getID()) || anim.getID().equals(defeat.getID()) || anim.getID().equals(interact.getID()))
            return false;
        if (type == AnimationType.GENERICATTACK)
            return this.isEnraged() || !anim.getID().equals(pollen.getID());
        return false;
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        if (anim != null)
            switch (anim.getID()) {
                case "kick_1":
                case "pollen":
                case "kick_2":
                    return 3;
            }
        return 34 + this.getRandom().nextInt(22) - (this.isEnraged() ? 20 : 0) + diffAdd;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrentAnim(wave.getID(), angry.getID()))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrentAnim(angry.getID(), defeat.getID());
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrentAnim(pollen.getID(), angry.getID(), defeat.getID()))
            return;
        super.push(x, y, z);
    }

    @Override
    public boolean shouldFreezeTravel() {
        return this.getAnimationHandler().isCurrentAnim(wave.getID());
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return defeat;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            if (!anim.getID().equals(pollen.getID()))
                this.lookAt(target, 180.0f, 50.0f);
        }
        switch (anim.getID()) {
            case "butterfly":
                if (anim.canAttack()) {
                    ModSpells.BUTTERFLY.get().use((ServerLevel) this.level, this);
                }
                break;
            case "kick_1":
            case "kick_2":
            case "kick_3":
                if (target != null) {
                    this.getNavigation().moveTo(target, 1.0);
                }
                if (anim.canAttack()) {
                    this.mobAttack(anim, target, this::doHurtTarget);
                }
                break;
            case "sleep":
                this.getNavigation().stop();
                if (anim.canAttack() && !EntityUtils.sealed(this))
                    ModSpells.SLEEPBALLS.get().use((ServerLevel) this.level, this);
                break;
            case "wave":
                this.getNavigation().stop();
                if (anim.canAttack() && !EntityUtils.sealed(this))
                    ModSpells.WAVE.get().use((ServerLevel) this.level, this);
                break;
            case "pollen":
            case "pollen_2":
                if (this.aiVarHelper == null)
                    return;
                this.setDeltaMovement(new Vec3(this.aiVarHelper.x(), 0, this.aiVarHelper.z()));
                if (anim.canAttack() && !EntityUtils.sealed(this)) {
                    this.getNavigation().stop();
                    EntityPollen pollen = new EntityPollen(this.level, this);
                    pollen.setPos(pollen.getX(), pollen.getY() + 0.5, pollen.getZ());
                    this.level.addFreshEntity(pollen);
                }
                break;
        }
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target) {
        if (anim.getID().equals(pollen.getID())) {
            return this.getBoundingBox().inflate(2.0);
        }
        return super.calculateAttackAABB(anim, target);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(wave);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(sleep);
            else
                this.getAnimationHandler().setAnimation(kick_1);
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
            this.getAnimationHandler().setAnimation(angry);
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
        if (prev.equals(pollen2.getID()))
            return other.getID().equals(pollen.getID());
        if (prev.equals(kick_3.getID()))
            return other.getID().equals(kick_1.getID());
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(String prev) {
        return switch (prev) {
            case "kick_1" -> kick_2;
            case "kick_2" -> kick_3;
            case "pollen" -> pollen2;
            default -> null;
        };
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }

    @Override
    public Vec3 targetPosition() {
        return this.aiVarHelper;
    }
}
