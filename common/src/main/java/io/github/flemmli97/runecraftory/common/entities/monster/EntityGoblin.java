package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
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
import org.jetbrains.annotations.Nullable;

public class EntityGoblin extends LeapingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String MELEE = BUILDER.add("slash", AnimationsBuilder.definition(0.72).marker("attack", 0.36));
    public static final String LEAP = BUILDER.add("leap", AnimationsBuilder.definition(0.92).marker("attack", 0.36));
    public static final String STONE = BUILDER.add("throw", AnimationsBuilder.definition(0.72).marker("attack", 0.48));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    //
//    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityGoblin>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(MELEE, e -> 0.8f), 2),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGoblin>(LEAP)
//                    .cooldown(e -> e.animationCooldown(LEAP))
//                    .prepare(() -> new WrappedRunner<>(new MoveAwayRunner<>(1.5, 1, 4))), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityGoblin>(LEAP)
//                    .cooldown(e -> e.animationCooldown(LEAP))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 5))), 2),
//            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(STONE, 8, 3, 1, e -> 1), 3)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityGoblin>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 1),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 2)
//    );
//
//    public final AnimatedAttackGoal<EntityGoblin> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityGoblin> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityGoblin(EntityType<? extends EntityGoblin> type, Level world) {
        super(type, world);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.STEEL_SWORD_PROP.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 2.1;
        double length = this.getBbWidth() * 2;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.STONE_THROW.get() : null))
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
        return ModSounds.ENTITY_GOBLING_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.ENTITY_GOBLING_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_GOBLIN_DEATH.get();
    }

    @Override
    public AnimationHandler<? extends EntityGoblin> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(STONE)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.STONE_THROW.get().use(this);
            }
        } else
            super.handleAttack(anim);
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
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
//
//    @Override
//    public Vec3 passengerOffset(Entity passenger) {
//        return new Vec3(0, 13 / 16d, -5 / 16d);
//    }
}
