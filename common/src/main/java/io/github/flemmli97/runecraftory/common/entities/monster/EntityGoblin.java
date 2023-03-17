package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityGoblin extends ChargingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(12, 7, "slash");
    private static final AnimatedAction LEAP = new AnimatedAction(19, 6, "leap");
    private static final AnimatedAction STONE = new AnimatedAction(14, 9, "throw");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(2, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, STONE, INTERACT, SLEEP};
    public ChargeAttackGoal<EntityGoblin> attack = new ChargeAttackGoal<>(this);
    protected List<LivingEntity> hitEntity;
    private final AnimationHandler<EntityGoblin> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(a -> {
                if (!LEAP.checkID(a))
                    this.hitEntity = null;
            });

    public EntityGoblin(EntityType<? extends EntityGoblin> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.steelSword.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(MELEE.getID()) || anim.getID().equals(STONE.getID());
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(LEAP.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(STONE.getID()))
            return 8;
        return 1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(STONE);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.85f;
    }

    @Override
    public AnimationHandler<? extends EntityGoblin> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(STONE.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                ModSpells.STONETHROW.get().use((ServerLevel) this.level, this);
            }
        } else if (anim.getID().equals(LEAP.getID())) {
            if (anim.canAttack()) {
                Vec3 vec32;
                if (this.getTarget() != null) {
                    Vec3 target = this.getTarget().position();
                    vec32 = new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ())
                            .normalize().scale(1.35);
                } else
                    vec32 = this.getLookAngle();
                this.setDeltaMovement(vec32.x, 0.35f, vec32.z);
            }
            if (anim.getTick() >= anim.getAttackTime()) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
        } else
            super.handleAttack(anim);
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
