package io.github.flemmli97.runecraftory.common.entities.monster;


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.ChargeAttackGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EntityBuffamoo extends ChargingMonster {

    public final ChargeAttackGoal<EntityBuffamoo> ai = new ChargeAttackGoal<>(this);
    public static final AnimatedAction chargeAttack = new AnimatedAction(61, 16, "charge");
    public static final AnimatedAction stamp = new AnimatedAction(8, 4, "stamp");

    private static final AnimatedAction[] anims = new AnimatedAction[]{stamp, chargeAttack};

    private final AnimationHandler<EntityBuffamoo> animationHandler = new AnimationHandler<>(this, anims);

    public EntityBuffamoo(EntityType<? extends EntityBuffamoo> type, World world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.ai);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18);
        super.applyAttributes();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COW_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.1f;
    }

    @Override
    protected float getSoundPitch() {
        return 0.7f;
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.7f;
    }

    @Override
    public AnimationHandler<EntityBuffamoo> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void doWhileCharge() {
        if (this.ticksExisted % 3 == 0)
            this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_COW_STEP, SoundCategory.HOSTILE, 1, this.getRNG().nextFloat() * 0.2F);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.2;
    }

    @Override
    public float chargingLength() {
        return 9;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.CHARGE) {
            return anim.getID().equals(chargeAttack.getID());
        }
        return type == AnimationType.MELEE && anim.getID().equals(stamp.getID());
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(chargeAttack);
            else
                this.getAnimationHandler().setAnimation(stamp);
        }
    }
}
