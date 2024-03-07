package io.github.flemmli97.runecraftory.common.entities.ai.npc;

import io.github.flemmli97.runecraftory.api.registry.AttackAction;
import io.github.flemmli97.runecraftory.api.registry.NPCAction;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;
import java.util.List;

public class NPCAttackGoal<T extends EntityNPCBase> extends Goal {

    protected final T attacker;
    protected LivingEntity target;

    protected int pathFindDelay;
    protected double distanceToTargetSq;
    protected boolean canSee;

    private List<NPCAction> actions;
    private int idx, actionDuration, idleTime;
    private boolean initialSelect = true;
    private AttackAction attackAction;

    public NPCAttackGoal(T entity) {
        super();
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        this.attacker = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity living = this.attacker.getTarget();
        if (living == null || !living.isAlive())
            return false;
        if (!this.attacker.isWithinRestriction(living.blockPosition()))
            return false;
        return !this.attacker.getAttackActions().isEmpty();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.actions != null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        LivingEntity livingEntity = this.attacker.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            this.attacker.setTarget(null);
        }
        this.actions = null;
        this.idx = 0;
        this.initialSelect = true;
    }

    protected void selectActionSequence() {
        this.actions = this.attacker.getAttackActions().getAction(this.attacker);
        this.idx = 0;
        this.initialSelect = true;
    }

    public void setupValues() {
        this.target = this.attacker.getTarget();
        this.distanceToTargetSq = this.attacker.distanceToSqr(this.target);
        this.canSee = this.attacker.getSensing().hasLineOfSight(this.target);
        if (this.initialSelect) {
            this.initialSelect = false;
            NPCAction action = this.actions.get(this.idx);
            this.attackAction = action.getAction(this.attacker);
            this.actionDuration = Math.max(1, action.getDuration(this.attacker));
        }
    }

    @Override
    public void tick() {
        --this.actionDuration;
        --this.pathFindDelay;
        if (this.attacker.getTarget() == null || --this.idleTime > 0) {
            return;
        }
        AnimatedAction anim = this.attacker.getAnimationHandler().getAnimation();
        if (anim != null)
            return;
        if (this.actions == null || this.actions.isEmpty()) {
            this.selectActionSequence();
            return;
        }
        this.setupValues();
        NPCAction npcAction = this.actions.get(this.idx);
        boolean done = npcAction.doAction(this.attacker, this, this.attackAction);
        if (done || this.actionDuration <= 0) {
            if (done && this.attackAction != null) {
                this.attacker.weaponHandler.doWeaponAttack(this.attacker, this.attackAction, this.attacker.getMainHandItem(), npcAction.getSpell(), false);
            }
            this.idleTime = npcAction.getCooldown(this.attacker);
            this.idx++;
            this.initialSelect = true;
            if (done)
                this.attacker.getNavigation().stop();
            if (this.idx >= this.actions.size()) {
                this.selectActionSequence();
            }
        }
    }

    public LivingEntity getAttackTarget() {
        return this.target;
    }

    public double getDistSqr() {
        return this.distanceToTargetSq;
    }

    public boolean canSeeTarget() {
        return this.canSee;
    }

    public void moveToEntityNearer(LivingEntity target, float speed) {
        this.moveToEntity(target, speed, 0);
    }

    public void moveToEntity(LivingEntity target, float speed, int accuracy) {
        if (this.pathFindDelay <= 0) {
            Path path = this.attacker.getNavigation().createPath(target, accuracy);
            if (path == null || this.attacker.getNavigation().moveTo(path, speed)) {
                this.pathFindDelay += 15;
            }
            this.pathFindDelay += this.attacker.getRandom().nextInt(10) + 5;
        }
    }
}
