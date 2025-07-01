package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinition;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StayWithinDistanceOfAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector3d;

public class EntityGoblinArcher extends EntityGoblin {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder(EntityGoblin.BUILDER, SLEEP);
    public static final String BOW = BUILDER.add("bow", AnimationsBuilder.definition(0.8).marker("attack", 0.52));
    public static final String TRIPLE = BUILDER.add("triple", BOW);
    public static final String KICK = BUILDER.add("kick", AnimationsBuilder.definition(0.56).marker("attack", 0.32));
    public static final String INTERACT = BUILDER.add("interact", KICK);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<EntityGoblinArcher> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityGoblinArcher(EntityType<? extends EntityGoblin> type, Level level) {
        super(type, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(KICK).play(MonsterBehaviourUtils.requireInRangePlay())
                .condition(MonsterBehaviourUtils.inAABBRange(KICK))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(5)
                .start(BOW).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(e -> e.getMainHandItem().getItem() instanceof BowItem)
                .prepare(new StrafeTarget<BaseMonster>().strafeDistance(10))
                .end(7)
                .start(TRIPLE).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(e -> e.getMainHandItem().getItem() instanceof BowItem)
                .prepare(new StrafeTarget<BaseMonster>().strafeDistance(10))
                .end(4)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(3, new StayWithinDistanceOfAttackTarget<BaseMonster>().maxDistance(15))
                .add(1, new SetRandomWalkTarget<>(), new MoveToWalkTarget<>())
                .add(2, new Idle<>()).build();
    }

    @Override
    public void setupAttack(AnimationDefinition anim) {
        if (anim.is(BOW, TRIPLE)) {
            this.startUsingItem(InteractionHand.MAIN_HAND);
        }
        super.setupAttack(anim);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(BOW, TRIPLE)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                boolean withTarget = this.getTarget() != null && this.getSensing().hasLineOfSight(this.getTarget());
                if (anim.is(BOW)) {
                    if (withTarget)
                        this.shootArrow(this.getTarget());
                    else
                        this.shootArrowFromRotation(this);
                } else {
                    if (withTarget)
                        this.shootTripleArrow(this.getTarget());
                    else
                        this.shootTripleArrowFromRotation(this);
                }
                this.stopUsingItem();
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.3;
        double length = this.getBbWidth() * 1.7;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<EntityGoblinArcher> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.TRIPLE_ARROW.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(TRIPLE);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(BOW);
            else
                this.getAnimationHandler().setAnimation(KICK);
        }
    }

    private void shootArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level(), this, 0.8f);
        Vec3 dir = new Vec3(target.getX() - arrow.getX(), target.getY(0.33) - arrow.getY(), target.getZ() - arrow.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level().getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }

    private void shootArrowFromRotation(LivingEntity shooter) {
        EntityMobArrow arrow = new EntityMobArrow(this.level(), this, 0.8f);
        arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 1.3f, 7 - this.level().getDifficulty().getId() * 2);
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }

    private void shootTripleArrow(LivingEntity target) {
        EntityMobArrow arrow = new EntityMobArrow(this.level(), this, 0.8f);
        Vec3 dir = new Vec3(target.getX() - arrow.getX(), target.getY(0.33) - arrow.getY(), target.getZ() - arrow.getZ());
        double l = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        dir = dir.add(0, l * 0.2, 0);
        arrow.shoot(dir.x, dir.y, dir.z, 1.3f, 7 - this.level().getDifficulty().getId() * 2);
        this.level().addFreshEntity(arrow);
        Vec3 up = this.getUpVector(1);
        Vector3d dir3d = new Vector3d(dir.x(), dir.y(), dir.z());

        for (float y = -15; y <= 15; y += 30) {
            Vector3d newDir = dir3d.rotateAxis(y * Mth.DEG_TO_RAD, up.x(), up.y(), up.z(), new Vector3d());
            EntityMobArrow arrowO = new EntityMobArrow(this.level(), this, 0.8f);
            arrowO.shoot(newDir.x(), newDir.y(), newDir.z(), 1.3f, 7 - this.level().getDifficulty().getId() * 2);
            this.level().addFreshEntity(arrowO);
        }

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    private void shootTripleArrowFromRotation(LivingEntity shooter) {
        for (int i = 0; i < 3; i++) {
            EntityMobArrow arrow = new EntityMobArrow(this.level(), this, 0.8f);
            arrow.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + (i - 1) * 15, 0.0F, 1.3f, 7 - this.level().getDifficulty().getId() * 2);
            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level().addFreshEntity(arrow);
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
