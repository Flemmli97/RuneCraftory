package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MoveToWalkTillClose;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetWalkTargetWithinDist;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

public class GoblinPirate extends Goblin {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder(Goblin.BUILDER, LEAP, SLEEP);
    public static final String DOUBLE_SLASH = BUILDER.add("double_slash", AnimationsBuilder.definition(1).marker("attack", 0.4, 0.8));
    public static final String INTERACT = BUILDER.add("interact", DOUBLE_SLASH);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<GoblinPirate> animationHandler = new AnimationHandler<>(this, ANIMS);

    public GoblinPirate(EntityType<? extends Goblin> type, Level level) {
        super(type, level);
    }

    public void quickAttack(Entity target) {
        DynamicDamage.Builder source = new DynamicDamage.Builder(this).noKnockback().hurtResistant(1);
        CombatUtils.mobAttack(this, target, source);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(RuneCraftoryItems.THIEF_KNIFE_PROP.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(DOUBLE_SLASH).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(6)
                .start(LEAP).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetWithinDist<BaseMonster>().min(2).max(6)).prepareOptional(new MoveToAttackTarget<>())
                .end(2)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(4, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTillClose<>())
                .add(1, new SetRandomWalkTarget<>(), new MoveToWalkTillClose<>()).build();
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.5;
        double length = this.getBbWidth() * 2;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(DOUBLE_SLASH)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.mobAttack(anim, this.getTarget(), this::quickAttack);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AnimationHandler<GoblinPirate> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
            else
                this.getAnimationHandler().setAnimation(DOUBLE_SLASH);
        }
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }
}
